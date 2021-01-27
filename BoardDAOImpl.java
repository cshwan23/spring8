package com.naver.erp;

//import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

//mmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmm
//[DAO 클래스]인 [LoginDAOImpl 클래스]선언 
//mmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmm
	// @Repository를 붙임으로써 [DAO 클래스]임을 지정하게되고
	// bean 태그로 자동 등록 된다. 
//mmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmm

@Repository
public class BoardDAOImpl implements BoardDAO {
		
	//++++++++++++++++++++++++++++++++++++++++
	// SqlSessionTemplate 객체를 생성해 속변 sqlSession 에 저장
	// @Autowired 어노테이션을 붙이면 자료형에 맞는 SqlSessionTemplate 객체를 생성한다. 
	//++++++++++++++++++++++++++++++++++++++++
	@Autowired
	private SqlSessionTemplate sqlSession;
	
	
	//********************************************
	// [1개 게시판 글 출력번호 수정하고 수정 행의 개수]를 리턴하는 메소드 선언
	//********************************************
	public int updatePrintNo (BoardDTO boardDTO) {
		//------------------------------------------
		// [SqlSessionTemplate 객체]의 update(~,~)를 호출하여
		// [게시판 글 출력번호 1 증가하고 수정 행의 개수] 얻기
		//------------------------------------------
		int updatePrintNoCnt = sqlSession.update(
			"com.naver.erp.BoardDAO.updatePrintNo" // 실행할 SQL 구문의 위치 지
			,boardDTO							// 실행할 SQL구문에서 사용할 데이터 지정
			
			);
		//------------------------------------------
		//[수정 행의 개수] 리턴하기 
		//------------------------------------------
		return updatePrintNoCnt;
		
	}
	
	//+++++++++++++++++++++++++++++++++++++++++
	// [게시판 글 입력 후 입력 적용 행의 개수를 리턴하는 메소드 선언
	//+++++++++++++++++++++++++++++++++++++++++
	public int insertBoard(BoardDTO boardDTO) {
		//------------------------------------------
		// SqlSessionTemplate 객체의 selectOne 메소드 호출로
		// 게시판 글 입력 SQL 구문을 실행하고 입력 성공 행의 개수 얻기 
		//------------------------------------------
		//********************************************
		// insert("com.naver.erp.BoardDAO.insertBoard", boardDTO); 의미 
		//********************************************
			// mybatis SQL 구문 설정 XML 파일에서
			// <mapper namespace="com.naver.erp.BoardDAO.insertBoard"> 태그 내부의
			// <insert id="insertBoard" ~> 태그 내부의
			// [insert 쿼리문]을 실행하고 입력 성공 행의 개수를 얻기
		int boardRegCnt = this.sqlSession.insert(
				"com.naver.erp.BoardDAO.insertBoard" //실행할 SQL 구문의 위치 지정    
				,boardDTO							// 실행할 SQL 구문에서 사용할 데이터 지정
		);
		System.out.println("BoardDAOImpl.insertBoard 실행 성공");
		
		return boardRegCnt;
	}
	//*********************************************
	// [검색한 게시판 목록] 리턴하는 메소드 선언
	//*********************************************
	public List<Map<String,String>> getBoardList(BoardSearchDTO boardSearchDTO){
		
		
		List<Map<String,String>> boardList = this.sqlSession.selectList(
				"com.naver.erp.BoardDAO.getBoardList" //실행할 SQL 구문의 위치 지정
				,boardSearchDTO
		);
		return boardList;
	}
	//*********************************************
	// [1개 게시판 글 정보] 리턴하는 메소드 선언
	//*********************************************
	public BoardDTO getBoard(int b_no) {
		//------------------------------------------
		// [SqlSessionTemplate 객체]의 selectOne(~,~) 를 호출하여
		// [1개 게시판 글 정보] 얻기
		//------------------------------------------
		BoardDTO board = this.sqlSession.selectOne(
				"com.naver.erp.BoardDAO.getBoard"
				,b_no 
		);
		//------------------------------------------
		// [1개 게시판 글] 정보 리턴하기
		//------------------------------------------
		return board;
				
	}
	//*********************************************
	//[게시판 글 조회수 증가하고 수정행의 개수] 리턴하는 메소드 선언
	//*********************************************
	public int updateReadcount(int b_no) {
		//------------------------------------------
		//[SqlSessionTemplate 객체]의 update(~,~) 를 호출하여 [조회수 증가] 하기 
		//------------------------------------------
		int updateCnt = this.sqlSession.update(
				"com.naver.erp.BoardDAO.updateReadcount" // 실행할 SQL 구문의 위치 지정 
				,b_no							 // 실행할 SQL 구문에서 사용할 데이터 지정
				);
		return updateCnt;
	}

	//*********************************************
	// 삭제/ 수정할 게시판의 존재 개수를 리턴하는 메소드 선언
	//*********************************************
	public int getBoardCnt(BoardDTO board) {

		//------------------------------------------
		//[SqlSessionTemplate 객체]의 selectOne(~,~) 를 호출하여 [게시판 존재 개수] 하기 
		//------------------------------------------
		int boardCnt = this.sqlSession.selectOne(
				"com.naver.erp.BoardDAO.getBoardCnt" // 실행할 SQL 구문의 위치 지정
				,board
		);
		return boardCnt;
	}
	//*********************************************
	// 삭제할 게시판의 비밀번호 존재 개수를 리턴하는 메소드 선언
	//*********************************************
	public int getPwdCnt(BoardDTO board) {

		//------------------------------------------
		//[SqlSessionTemplate 객체]의 selectOne(~,~) 를 호출하여 [비밀번호 존재 개수] 하기 
		//------------------------------------------
		int pwdCnt = this.sqlSession.selectOne(
				"com.naver.erp.BoardDAO.getPwdCnt" // 실행할 SQL 구문의 위치 지정
				,board							   // 실행할 SQL 구문에서 사용할 데이터 지정
		);
		return pwdCnt;
	}
	//*********************************************
	// 게시판 수정 후 수정행의 적용 개수를 리턴하는 메소드 선언
	//*********************************************
	public int updateBoard(BoardDTO board) {

		//------------------------------------------
		//[SqlSessionTemplate 객체]의 update(~,~) 를 호출하여 [게시판 수정] 하기 
		//------------------------------------------
		int updateCnt = this.sqlSession.selectOne(
				"com.naver.erp.BoardDAO.updateBoard" // 실행할 SQL 구문의 위치 지정
				,board 								 // 실행할 SQL 구문에서 사용할 데이터 지정
		);
		return updateCnt;
	}
	
	//*********************************************
	// [삭제할 게시판의 자식 글 존재개수]를 얻는 메소드 선언
	//*********************************************
	public int getChildrenCnt(BoardDTO board) {

		//------------------------------------------
		//[SqlSessionTemplate 객체]의 selectOne(~,~) 를 호출하여 [자식글 존재 개수] 얻기 
		//------------------------------------------
		int ChildrenCnt = this.sqlSession.selectOne(
				"com.naver.erp.BoardDAO.getChildrenCnt" // 실행할 SQL 구문의 위치 지정
				,board 								 	// 실행할 SQL 구문에서 사용할 데이터 지정
		);
		return ChildrenCnt;
	}
	//*********************************************
	// 삭제될 게시판 이후 글의 출력 순서번호를 1씩 감소 시킨 후 수정 적용행의 개수를 리턴하는 메소드 선언
	//*********************************************
	public int downPrintNo(BoardDTO board) {

		//------------------------------------------
		//[SqlSessionTemplate 객체]의 selectOne(~,~) 를 호출하여 
		// 삭제될 게시판 이후 글의 출력 순서번호를 1씩 감소 시킨 후 수정 적용행의 개수 얻기 
		//------------------------------------------
		int downPrintNoCnt = this.sqlSession.update(
				"com.naver.erp.BoardDAO.downPrintNo" // 실행할 SQL 구문의 위치 지정
				,board 								 // 실행할 SQL 구문에서 사용할 데이터 지정
		);
		return downPrintNoCnt;
	}
	//*********************************************
	// [게시판 삭제 명령한후 삭제 적용행의 개수]를 얻는 메소드 선언
	//*********************************************
	public int deleteBoard(BoardDTO board) {

		//------------------------------------------
		// [SqlSessionTemplate 객체]의 selectOne(~,~) 를 호출하여 
		// [게시판 삭제 명령]한 후 삭제 적용행의 개수 얻기
		//------------------------------------------
		int deleteBoardCnt = this.sqlSession.delete(
				"com.naver.erp.BoardDAO.deleteBoard" // 실행할 SQL 구문의 위치 지정
				,board 								 // 실행할 SQL 구문에서 사용할 데이터 지정
		);
		return deleteBoardCnt;
	}
	
	//*********************************************
	// [검색한 게시판 목록 개수] 리턴하는 메소드 선언
	//*********************************************
	public int getBoardListAllCnt(BoardSearchDTO boardSearchDTO) {
		int boardListAllCnt = this.sqlSession.selectOne(
				"com.naver.erp.BoardDAO.getBoardListAllCnt" // 실행할 SQL 구문의 위치 지정
				,boardSearchDTO								// 실행할 SQL 구문에서 사용할 데이터 지정
		);
		return boardListAllCnt;
	}
}















