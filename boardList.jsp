<!-- ************************************************************* -->
<!-- JSP 기술의 한종류인 [Page Directive]를 이용하여 현 JSP 페이지 처리 방식 선언하기 -->
<!-- ************************************************************* -->
	<!-- 현재 이 JSP 페이지 실행 후 생성되는 문서는 HTML 이고, 이 문서 안의 데이터는 UTF-8 방식으로 인코딩한다 라고 설정함 -->
	<!-- 현재 이 JSP 페이지는 UTF-8 방식으로 인코딩한다 -->
	<!-- UTF-8 인코딩 방식은 한글을 포함 전 세계 모든 문자열을 부호화 할 수 있는 방법이다. -->
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>



<!-- ****************************************************** -->
<!-- 현재 JSP 페이지에서 사용할 클래스의 패키지 수입하기 -->
<!-- ****************************************************** -->
<%@ page import="java.util.*"%>
<%@ page import="com.naver.erp.BoardSearchDTO"%>


<!--*****************************************************-->
<!-- JSP 기술의 한 종류인 [Include Directive]를 이용하여 -->
<!-- common.jsp 파일 내의 소스를 삽입하기 -->
<!--*****************************************************-->
<%@include file="common.jsp" %>
<% 
BoardSearchDTO boardSearchDTO = (BoardSearchDTO)request.getAttribute("boardSearchDTO");
  String keyword1 = boardSearchDTO.getKeyword1(); 
  if (keyword1==null){
	  keyword1 = "";
  }
 int selectPageNo = boardSearchDTO.getSelectPageNo();
 int rowCntPerPage = boardSearchDTO.getRowCntPerPage();
 
 int boardListAllCnt = (int)request.getAttribute("boardListAllCnt");
%>

<!DOCTYPE html>
<html>

<head>
<title>게시판 목록</title>
	<script>

	//*********************************
	// body 태그 안의 모든 태그를 읽어들인 후 실행할 자스 코딩 설정 
	//*********************************
	$(document).ready(function(){
		

		//---------------------------------------
		// name="rowCntPerPage" 에 change 이벤트가 발생하면 실행할 코드 설정하기
		//---------------------------------------
		$('[name=rowCntPerPage]').change(function(){
			search_when_pageNoClick();
		});

		$(".keyword1").val("<%=keyword1%>");
		$(".selectPageNo").val("<%=selectPageNo%>");
		$(".rowCntPerPage").val("<%=rowCntPerPage%>");
		
		//---------------------------------------
		// 페이징 처리 관련 HTML 소스를 class=pagingNumber 가진 태그 안에 삽입하기 
		//---------------------------------------
		$(".pagingNumber").html(
			getPagingNumber(
					
				 "<%=boardListAllCnt%>"
				 // 검색 결과 총 행 개수
				,"<%=selectPageNo%>"
				// 선택된 현재 페이지 번호
				,"<%=rowCntPerPage%>"
				// 페이지 당 출력행의 개수 
				,"15"
				,"search_when_pageNoClick( );"
				// 페이지 번호 클릭 후 실행할 자스코드
			)	
		);
	});
	
	//*********************************
	// [게시판 입력 화면]으로 이동하는 함수 선언 
	//*********************************
	function goBoardRegForm(){
		return;
		//-----------------
		// 웹서버에 boardRegForm.do URL로 접속하기 
		//-----------------
		location.replace("/z_spring/boardRegForm.do");
	}
	function goContentForm(b_no){
		location.replace("/z_spring/boardContentFrom.do");
	}
	
	

	//*********************************
	// 페이지 번호 클릭하면 호출되는 함수 선언 
	//*********************************
	function search_when_pageNoClick(){
		// 입력한 키워드 얻기
		var keyword1 = $("[name=keyword1]").val();
		
		// 입력한 키워드가 비어있지 않으면
		if(keyword1!=null && keyword1.split(" ").join("")!=""){
		
			// 입력한 키워드의 앞뒤 공백 제거하기
			keyword1 = $.trim(keyword1);
			
			// name=keyword1 가진 입력 양식에 앞뒤 공백 제거한 키워드 넣어주기
			$("[name=keyword1]").val(keyword1);
		}
		// name=boardListForm을 가진 form 태그 안의 action값의 URL주소로 웹서버로 접속하기
		document.boardListForm.submit();
		
	}	
	
	
	//*********************************
	// [게시판 목록 화면]으로 이동하는 함수 선언 
	//*********************************
	function search(){

		//-----------------------------
		// 만약 키워드가 비워있거나 공백이면 경고하고 ""로 비우기
		// <주의> 공백이면 공백으로 DB 연동하므로 공백으로만 되어있으면 ""로 세팅한다.
		//-----------------------------
		var keyword1 = $("[name=keyword1]").val();
		if(keyword1.split(" ").join("")==""){
			alert("키워드가 없어 검색할 수 없습니다.");
			$("[name=keyword1]").val("");
			return;
		}
		//-----------------------------
		// 키워드의 앞뒤 공백 제거하기 
		//-----------------------------
		keyword1 = $.trim(keyword1);
		//-----------------------------
		// name=boardListForm 을 가진 form 태그의 action 값의 URL로 웹서버에 접속하기
		// 이동시 form 태그 안의 모든 입력 양식이 파라미터값으로 전송된다.
		//-----------------------------
		document.boardListForm.submit();
	}
	//*********************************
	// 키워드없이 [게시판 목록 화면]으로 이동하는 함수 선언 
	//*********************************
	function searchAll(){

		//-----------------------------
		// 키워드 비우기 
		//-----------------------------
		$("[name=keyword1]").val("");
		//-----------------------------
		// name=boardListForm 을 가진 form 태그의 action 값의 URL로 웹서버에 접속하기
		// 이동시 form 태그 안의 모든 입력 양식이 파라미터값으로 전송된다.
		//-----------------------------
		document.boardListForm.submit();
	}
		
	
	</script>
</head>
<body><center>

	<div style='cursor:pointer;' onclick="location.replace('/z_spring/logout.do')">
	[로그아웃]
	</div>

	<!-- ************************************************ -->
	<!-- [로그인 정보 입력 양식] 내포한 form 태그 선언 -->
	<!-- ************************************************ -->
	<form name="boardListForm" method="post" action="/z_spring/boardList.do">
	
		<div>
			<!----------------------------->
			<!-- 키워드 검색 입력 양식 표현하기 -->
			<!----------------------------->
			[키워드] : <input type="text" name="keyword1" class="keyword1">&nbsp;&nbsp;&nbsp;
			
			<!----------------------------->
			<!--  선택한 페이지번호가 저장되는 입력양식 표현하기  -->
			<!-- 선택한 페이지번호는 DB 연동시 아주 중요한 역할을 한다. -->
			<!----------------------------->
			<input type="hidden" name="selectPageNo" class="selectPageNo">
			
			<!----------------------------->
			<!-- 한 화면에 보여줄 행의개수가 저장된 입력양식 표현하기 -->  
			<!----------------------------->
			<!-- <input type="hidden" name="rowCntPerPage"> -->
			<select name="rowCntPerPage" class="rowCntPerPage">
				<option value="10">10
				<option value="15">15
				<option value="20">20
				<option value="25">25
				<option value="30">30
				
			</select>
			
			<!----------------------------->
			<!-- 버튼 표현하기 				 -->
			<!----------------------------->
			<input type="button" value="검색" class="contactSearch" onClick="search();">&nbsp;
			<input type="button" value="     모두검색     " class="contactSearchAll" onClick="searchAll();">&nbsp;
			<a href="javascript:goBoardRegForm();">[새글쓰기]</a>
		</div>
		<!------------------------------->
		<table border=0 width=700>
			<tr>
				<td align=right>
				<!------------------------>
				<!--게시판 검색 총 개수 출력  -->
				<!------------------------>
				[총개수]: <%=boardListAllCnt %>
		
		</table>
		<!-- ************************************************ -->
		<!-- 페이징 번호를 삽입할 span 태그 선언하기 -->
		<!-- ************************************************ -->
		<div>&nbsp;<span class="pagingNumber"></span>&nbsp;</div>
		
		<!-- ************************************************ -->
		<!-- 게시판 목록 출력하기 -->
		<!-- ************************************************ -->
		<table border=1 class="tbcss1" width=700>
		<tr><th>제목<th>글쓴이<th>등록일<th>조회수
		
		<%
		//여긴 자바 세상 .
		//---------------------------------------------------------
		// HttpServletRequest 객체에 "boardList" 란 키값으로 저장된 객체 꺼내기
		// ModelAndView 객체에 addObject("boardList",boardList)로 저장된 놈이다. 
		//---------------------------------------------------------
		List<Map<String,String>> boardList = (List<Map<String,String>>)request.getAttribute("boardList");
		if(boardList!=null){
			for( int i=0 ; i<boardList.size(); i++){
				
				String bgColor = "";
				if(i%2==1){
					bgColor = "bgcolor='lightgray";
				}
				
				Map<String,String> map = boardList.get(i);
				
				out.print( "<tr style='cusor:pointer'"+bgColor+" onClick='goContentForm("+map.get("b_no")+")'>" );
				
				int print_level = Integer.parseInt(map.get("print_level"),10);
				String blank = "";
				
				
				for(int j=0 ; j<print_level ; j++){
					blank = blank + "&bsp;&bsp;&bsp;";
				}
				if(print_level>0){
					blank=blank + "ㄴ";
				}
				//out.print("<td>" + map.get("b_no"));
				out.print("<td>&nbsp;&nbsp;&nbsp;" + blank + map.get("subject"));
				out.print("<td>&nbsp;&nbsp;&nbsp;" + map.get("writer"));
				out.print("<td>&nbsp;&nbsp;&nbsp;" + map.get("reg_date"));
				out.print("<td>&nbsp;&nbsp;&nbsp;" + map.get("readcount"));
			}
		}
		%>
		</table>
	</form>
	
</body>
</html>

