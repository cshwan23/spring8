package com.naver.erp;

import java.util.List;


import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

//mmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmm
//어노테이션을 붙여 클라스를 맞이할수있는자격이 생긴다 
// 가상 URL 주소로 접속하며 호출되는 메소드를 소유한 [컨트롤러 클래스] 선언
// @Controller 를 붙임으로써 [컨트롤러 클래스]임을 지정한다. 
//mmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmm
@Controller
public class BoardController {
	//mmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmm
	// 속성변수 boardService 선언하고 [BoardService 인터페이스]를 구현받은 클래스를 찾아 객체 생성해 저장
	// 관용적으로 [BoardService 인터페이스]를 구현받은 클래스명은 BoardServiceImpl 이다. 
	//mmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmm
		//@Autowired 역할 -> 속성변수에 붙은 자료형인 [인터페이스]를 구현한 [클래스]를 객체화하여 저장한다.
		// [인터페이스]를 구현한 [클래스]가 1개가 아니면 에러가 발생한다.
		// 단 @Autowired( required=false)로 선언하면 0개 여도 에러없이 null 이 저장된다.
		// Spring 에서만 지원하는 어노테이션이다.
	@Autowired
	private BoardService boardService;
	
	//mmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmm
	// 가상주소 /boardList.do 로 접근하면 호출되는 메소드 선언
	//		@RequestMapping 내부에, method="RequestMethod.POST가 없으므로
	//		가상주소 / boardListForm.do로 접근시 get 또는 post 방식 접근 모두 허용한다. 
	//mmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmm
	
	@RequestMapping(value="/boardList.do")
	public ModelAndView getBoardList(
			
		BoardSearchDTO boardSearchDTO
		//-------------------------------
		// HttpSession 객체를 받아오는 매개변수 선언
		//-------------------------------
		,HttpSession session
			
	) {
		//**************************
		// HttpSession 객체에서 로그인 성공했을 때 저장했던 아이디 꺼내기
		// 꺼낸 아이디가 없으면(=로그인 상태가 아니면) ModelAndView 객체에
		// "logout.jsp" 저장하고 ModelAndView 객체 리턴하기 
		//**************************
		String admin_id = (String)session.getAttribute("admin_id");
		if(admin_id==null) {
			ModelAndView mav = new ModelAndView();
			mav.setViewName("logout.jsp");
			return mav;
		}
		
		
		System.out.println("getBoardList 메소드 호출");
		
		//**************************
		// 게시판 목록의 총개수 얻기
		//**************************
		int boardListAllCnt = this.boardService.getBoardListAllCnt(boardSearchDTO);
		
	
		//***********************************************
		// [선택된 페이지 번호] 보정하기
		//***********************************************
		//------------------------------------------------
		// 검색된 게시판 총 개수가 1개이상 일때 [선택된 페이지 번호]보정하기
		// [선택된 페이지 번호]와 총개수 간의 관계가 이상할 경우있다.
		// 예> 검색 총 개수 3개인데 선택 페이지번호가 2페이지일 경우
		// (단 보여질 행의 개수가 10개일때) 모이다. 
		// 2페이지라면 11행부터 20행까지 짤라서 가져오란 얘기인데
		// 현재 3행밖에 없는데 어떻게 가져올수있냐 이얘기다.
		// 이럴 경우 선택페이지 번호를 1로 수정한다. 
		//------------------------------------------------
		if(boardListAllCnt>0) {
			int beginRowNo = boardSearchDTO.getSelectPageNo()
					*boardSearchDTO.getRowCntPerPage()
					-boardSearchDTO.getRowCntPerPage()
					+1;
			// [시작행번호 번호] > [총검색 개수] 일 경우
			// [선택된 페이지 번호]를 1로하고 검색 행의 [시작행 번호]를 1로 하기
			if(boardListAllCnt<beginRowNo) {
				boardSearchDTO.setSelectPageNo(1);
			}
		}
		
		
		//**************************
		// [게시판 목록] 얻기
		//**************************
			// BoardService 인터페이스를 구현한 객체(=BoardServiceImpl) 소유의
			// getBoardList 메소드 호출해서 [검색한 게시판 목록]을
			// List<Map<String,String>> 객체로 얻기
			// 이 때 BoardService 인터페이스를 구현한 객체의 이름을 알 필요가 없다.
			// 스프링이 알아서 찾아주기 때문이다.
		List<Map<String,String>> boardList = this.boardService.getBoardList(boardSearchDTO);
				
		
		
		//*****************************************		
		// [ModelAndView 객체] 생성하기
		//*****************************************
		ModelAndView mav = new ModelAndView();
		
		//*****************************************
		// [ModelAndView 객체]에 [호출 JSP 페이지명]을 저장하기
		//*****************************************
		mav.setViewName("boardList.jsp");
		
		//*****************************************
		// [ModelAndView 객체]에 JSP페이지에서 꺼내볼 게시판 목록이 저장된 List<Map<String,String>>객체 저장하기
		// [ModelAndView 객체]에 addObject 메소드로 저장된 DB 연동 결과물 HttpServletRequest 객체에도 저장된다.
		//*****************************************
		mav.addObject("boardList",boardList);
		

		//*****************************************
		// [ModelAndView 객체]에 [게시판 목록의 총개수]저장하기
		// [ModelAndView 객체]의 addObject 메소드로 저장된 DB 연동 결과물 HttpServletRequest 객체에도 저장된다.
		//*****************************************
		mav.addObject("boardListAllCnt",boardListAllCnt);
		
		mav.addObject("boardSearchDTO",boardSearchDTO);
		
		
		//*****************************************
		// [ModelAndView 객체] 리턴하기
		//*****************************************
		return mav;
	}
	//mmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmm
	// 가상주소 /boardRegForm.do 로 접근하면 호출되는 메소드 선언 
	//mmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmm
	@RequestMapping(value="/boardRegForm.do")
	public ModelAndView getBoardRegForm(
			// 파라미터명이 b_no 인 파라미터값을 받아오는 매개변수 b_no 선언하기
			// 만약 파라미터명이 없으면 null값이 들어오므로
			// 매개변수의 자료형은 String으로 하던가
			// 아니면 defaultValue 를 사용하여 원하는 기본값을 받아오도록 한다.
			@RequestParam(
				  value="b_no"  	// 파라미터명 설정
				, required=false    // 파라미터명,값이 안들어와도 된다는 의미
				, defaultValue="0"  // 파라미터값이 없으면 0이 저장
				) int b_no
			
			,HttpSession session
	) {

		//**************************
		// HttpSession 객체에서 로그인 성공했을 때 저장했던 아이디 꺼내기
		// 꺼낸 아이디가 없으면(=로그인 상태가 아니면) ModelAndView 객체에
		// "logout.jsp" 저장하고 ModelAndView 객체 리턴하기 
		//**************************
		String admin_id = (String)session.getAttribute("admin_id");
		if(admin_id==null) {
			ModelAndView mav = new ModelAndView();
			mav.setViewName("logout.jsp");
			return mav;
		}
		
		//-----------------------------------
		// [ModelAndView 객체] 생성하기 
		//-----------------------------------
		ModelAndView mav = new ModelAndView();
		//-----------------------------------
		// [ModelAndView 객체]에 [호출 JSP 페이지명]을 저장하기
		//-----------------------------------
		mav.setViewName("boardRegForm.jsp");
		//-----------------------------------
		// [ModelAndView 객체]에 JSP 페이지에서 꺼내볼 게시판 pk번호 저장하기
		// [ModelAndView 객체]의 addObject 메소드로 저장된 DB 연동 결과물은 HttpServletRequest 객체에도 저장된다.
		//-----------------------------------
		mav.addObject("b_no", b_no);
		//-----------------------------------
		// [ModelAndView 객체] 리턴하기
		//-----------------------------------
		return mav;
	}

	//mmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmm
	// /boardRegProc.do 로 접근하면 호출되는 메소드 선언 
	//mmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmm
	@RequestMapping(
			value="/boardRegForm.do"
			,method=RequestMethod.POST
			,produces="application/json;charset=UTF-8"
	)
	@ResponseBody
	public int insertBoard(
			
			BoardDTO boardDTO
			//**************************
			// 파라미터값을 저장할 [BoardDTO 객체]를 매개변수로 선언
			//**************************
			// [파라미터명]과 [BoardDTO 객체]의 [속성변수명]이 같으면 
			// setter 메소드가 작동되어 [파라미터값]이 [속성변수]에 저장된다.
			// [속성변수명]에 대응하는 [파라미터명]이 없으면 setter 메소드가 작동되지 않는다.
			// [속성변수명]에 대응하는 [파라미터명]이 있는데 [파라미터값]이 없으면
			// [속성변수]의 자료형에 관계없이 무조건 null값이 저장된다.
			// 이때 [속성변수]의 자료형이 기본형일 경우 null값이 저장될 수 없어 에러가 발생한다.
			// 이런 에러를 피하려면 파라미터 값이 기본형이거나 속성변수의 자료형 String으로 해야한다.
			// 이런 에러가 발생하면 메소드 안의 실행구문은 하나도 실행되지 않음에 주의한다.
			// 매개변수로 들어온 [DTO 객체]는 이 메소드가 끝난 후 호출되는 JSP 페이지로 그대로 이동한다.
			// 즉, HttpServletRequest 객체에 boardDTO라는 키값명으로 저장된다. 
			) {
	//--------------------------------------------
	// 게시판 글 입력하고 [게시판 입력 적용행의 개수] 저장할 변수 선언
	//--------------------------------------------
	int boardRegCnt = 0;
	try {
		
		//--------------------------------------------
		// [BoardServiceImpl 객체]의 insertBoard 메소드 호출로
		// 게시판 글 입력하고 [게시판 입력 적용행의 개수] 얻기
		//--------------------------------------------	
		boardRegCnt = this.boardService.insertBoard(boardDTO);
	
		
		
	}
	catch(Exception ex) {
		System.out.println("BoardController.insertBoard 메소드 실행시 예외 발생 ");
		boardRegCnt = -1;
	}
		
		return boardRegCnt;
	}
	//mmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmm
	// /boardContentForm.do 접속 시 호출되는 메소드 선언
	//mmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmm
	@RequestMapping(
			value="/boardContentForm.do"
	)
	public ModelAndView goBoardContentForm(
		//--------------------------------
		// b_no 라는 파라미터명에 해당하는 파라미터값이 저장되는 매개변수 b_no 선언
		// 관용적으로 파라미터명과 파라미터값 저장 변수의 스펠링은 동일하게 준다.
		// 게시판 PK 번호가 매개변수로 들어오므로 매개변수 자료형은 int로 한다.
		// String 으로 받아도 문제 없다.
		//--------------------------------
		@RequestParam(value="b_no") int b_no
		
		,HttpSession session
	) {

		//**************************
		// HttpSession 객체에서 로그인 성공했을 때 저장했던 아이디 꺼내기
		// 꺼낸 아이디가 없으면(=로그인 상태가 아니면) ModelAndView 객체에
		// "logout.jsp" 저장하고 ModelAndView 객체 리턴하기 
		//**************************
		String admin_id = (String)session.getAttribute("admin_id");
		if(admin_id==null) {
			ModelAndView mav = new ModelAndView();
			mav.setViewName("logout.jsp");
			return mav;
		}
		
		
		System.out.println("b_no => " + b_no);
		//**************************
		// [ModelAndView 객체] 생성하기
		//**************************
		ModelAndView mav = new ModelAndView();
		//**************************
		// [ModelAndView 객체]에 [호출 JSP 페이지명]을 저장하기
		//**************************
		mav.setViewName("boardContentForm.jsp");
		
		
		//********************************************
		// [BoardServiceImpl 객체]의 getBoard 메소드 호출로
		// [1개의 게시판 글]을 boardDTO 객체에 담아오기
		//********************************************
		BoardDTO board = this.boardService.getBoard(b_no);
		
		//********************************************
		// [ModelAndView 객체]에 검색한 BoardDTO 객체 저장하기
		// [ModelAndView 객체]의 addObject 메소드로 저장된 놈은 
		// HttpServletRequest 객체에도 저장된다. 
		//********************************************
		mav.addObject("board", board);
		
		// [ModelAndView 객체] 리턴하기 
		return mav;
	}
	//mmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmm
	// /boardUpDelForm.do 접속시 호출되는 메소드 선언
	//mmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmm
	@RequestMapping(value="/boardUpDelForm.do",method=RequestMethod.POST)
	public ModelAndView goBoardUpDelForm(
			//"b_no" 라는 파라미터명의 파라미터값이 저장되는 매개변수 b_no 선언
			@RequestParam(value="b_no") int b_no
			
			,HttpSession session
	) {

		//**************************
		// HttpSession 객체에서 로그인 성공했을 때 저장했던 아이디 꺼내기
		// 꺼낸 아이디가 없으면(=로그인 상태가 아니면) ModelAndView 객체에
		// "logout.jsp" 저장하고 ModelAndView 객체 리턴하기 
		//**************************
		String admin_id = (String)session.getAttribute("admin_id");
		if(admin_id==null) {
			ModelAndView mav = new ModelAndView();
			mav.setViewName("logout.jsp");
			return mav;
		}		
		
		//********************************************
		//[ModelAndView 객체]생성하기
		//[ModelAndView 객체]에 [호출 JSP 페이지명]을 저장하기
		//********************************************
		ModelAndView mav = new ModelAndView();
		mav.setViewName("boardUpDelForm.jsp");
		
		//********************************************
		//[수정/삭제 할 1개의 게시판 글 정보] 얻기
		//[ModelAndView 객체]에 [수정/삭제할 1개의 게시판 글 정보] 저장하기
		//********************************************
		BoardDTO boardDTO = this.boardService.getBoard_without_updateReadCnt(b_no);
		mav.addObject("board",boardDTO);
		
		//********************************************
		//[ModelAndView 객체] 리턴하기
		//********************************************
		return mav;
		
	}
	//mmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmm
	// /boardUpDelForm.do 접속시 호출되는 메소드 선언
	//mmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmm
	@RequestMapping(
			value="/boardUpDelProc.do"
			,method=RequestMethod.POST
			,produces="application/json;charset=UTF-8"
	)
	@ResponseBody
	public int boardUpDelProc(
			BoardDTO boardDTO
			,@RequestParam(value="upDel") String upDel
	) {
		//********************************************
		// 게시판 글을 수정/삭제하고 수정/삭제 적용행의 개수 저장할 변수 선언하기
		//********************************************
		int boardUpDelCnt=0;
		
		//********************************************
		//만약 수정모드이면 수정 실행하고 수정 적용행의 개수 얻기 
		//********************************************
		if(upDel.equals("up")) {
			
			boardUpDelCnt = this.boardService.updateBoard(boardDTO);
		
		}
		//********************************************
		// 만약 삭제 모드이면 삭제 실행하고 삭제 적용행의 개수 얻기	
		//********************************************
		else {
			
			boardUpDelCnt = this.boardService.deleteBoard(boardDTO);
		}
		
		//********************************************
		//게시판 글을 수정/삭제하고 수정/삭제 적용행의 개수 리턴하기
		//********************************************
		return boardUpDelCnt;
	}
}







