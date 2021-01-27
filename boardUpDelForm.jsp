<!-- ************************************************************* -->
<!-- JSP 기술의 한종류인 [Page Directive]를 이용하여 현 JSP 페이지 처리 방식 선언하기 -->
<!-- ************************************************************* -->
	<!-- 현재 이 JSP 페이지 실행 후 생성되는 문서는 HTML 이고, 이 문서 안의 데이터는 UTF-8 방식으로 인코딩한다 라고 설정함 -->
	<!-- 현재 이 JSP 페이지는 UTF-8 방식으로 인코딩한다 -->
	<!-- UTF-8 인코딩 방식은 한글을 포함 전 세계 모든 문자열을 부호화 할 수 있는 방법이다. -->
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.naver.erp.BoardDTO" %>
<% int b_no = (Integer)request.getAttribute("b_no"); %>

<!DOCTYPE html>
<html>

<!--*****************************************************-->
<!-- JSP 기술의 한 종류인 [Include Directive]를 이용하여 -->
<!-- common.jsp 파일 내의 소스를 삽입하기 -->
<!--*****************************************************-->
<%@include file="common.jsp" %>

<head>
<title>게시판 수정/삭제</title>
	<script>
	//****************************************
	//[게시판 등록 화면]에 입력된 데이터의 유효성 체크 함수 선언
	//****************************************
	function checkBoardUpDelForm(upDel){
		
		alert(upDel)
		//------------------------------
		// 매개변수로 들어온 upDel 에 "del" 이 저장되었으면
		// 즉, 삭제 버튼을 눌렀으면 암호 확인하고 삭제 여부를 물어보기 
		//------------------------------
	 	if(upDel=='del'){
	 		var pwd = $("[name=pwd]").val();
	 		//입력한 암호가 비어있으면
			if(pwd.split(" ").join("")==""){
				// 경고하기
				alert("암호를 입력해주세요.");
				// 입력한 암호 비우기
				$("[name=pwd]").val("");
				// 커서 들여놓기
				$("[name=pwd]").focus();
				// 자스에서 리턴은 함수의 암살자.
				return;
	 		}
			if(confirm("정말 삭제하시겠습니까?")==false){return;}
			// name=upDel 가진 입력양식에 "del" 저장하기
			$("[name=upDel]").val("del");
	 	}
		//------------------------------
		// 매개변수로 들어온 upDel 에 "up" 이 저장되었으면
		// 즉, 수정 버튼을 눌렀으면 입력양식의 유효성 체크하고 수정 여부를 물어보기  
		//------------------------------
		else if(upDel=="up"){
			
			var writer = $("[name=writer]").val();
			if(writer.split(" ").join("")==""){
				alert("이름을 입력해주세요.");
				$("[name=writer]").focus();
				return;
			}

			var subject = $("[name=subject]").val();
			if(subject.split(" ").join("")==""){
				alert("제목을 입력해주세요.");
				$("[name=subject]").focus();
				return;
			}
			
			var email = $("[name=email]").val();
			if(email.split(" ").join("")==""){
				alert("이메일을 입력해주세요.");
				$("[name=email]").focus();
				return;
			}

			var content = $("[name=content]").val();
			if(content.split(" ").join("")==""){
				alert("내용을 입력해주세요.");
				$("[name=content]").focus();
				return;
			}
			
			var pwd = $("[name=pwd]").val();
			if(pwd.split(" ").join("")==""){
				alert("암호를 입력해주세요.");
				$("[name=pwd]").focus();
				return;
			}
			if(confirm("정말 수정하시겠습니까?")==false){return;}
			
		}	
		$("name=upDel").val("upDel");
		
		//$("body").append($('[name=boardUpDelForm]').serialize()); return;
		
		//------------------------------------------------------------
		// 현재 화면에서 페이지 이동 없이 서버쪽 "/z_spring/boardUpDelProc.do"을 호출하여
		// [게시판 수정 또는 삭제 적용 개수]가 있는 html 소스를 받는다.  
		//------------------------------------------------------------
		$.ajax({
			//------------------------
			// 호출할 서버 쪽 URL 주소 설정
			//------------------------
			url : "/z_spring/boardUpDelProc.do"
			//------------------------
			// 호출할 서버 쪽 URL 주소 설정
			//------------------------
			,type : "post"
			//------------------------
			// 서버로 보낼 파라미터명과 파라미터 값을 설정
			//------------------------
			,data : $('name=boardUpDelForm').serialize()
			//------------------------
			// 서버의 응답을 성공적으로 받았을 경우 실행할 익명함수 설정
			// 익명함수의 매개변수 upDelCnt에는 수정 또는 삭제 행의 개수가 문자열로 들어옴
			//------------------------
			,success : function(upDelCnt){
				if(upDel=='up'){
					if(upDelCnt==1){
						alert=("수정 성공!");
					}
					else if(upDelCnt==-1){
						alert=("게시물이 삭제되어 수정할 수 없습니다!");
					}
					else if(upDelCnt==-2){
						alert=("비밀번호가 잘못 입력 되었습니다!");
					}
					else {
						alert=("서보쪽 DB연동 실패!");
					}
				}
				else if(upDel=="del"){
					if(upDelCnt==1){
						alert=("삭제 성공!");
					}
					else if(upDelCnt==-1){
						alert=("게시물이 삭제되어 삭제할수 없습니다!");
					}
					else if(upDelCnt==-2){
						alert=("비밀번호가 잘못 입력 되었습니다!");
					}
					else if(upDelCnt==-3){
						alert=("댓글이 있어 삭제 불가능합니다!");
					}
					else {
						alert=("서보쪽 DB연동 실패!");
					}
				}
			}
			//------------------------
			// 서버의 응답을 못 받았을 경우 실행할 익명함수 설정
			//------------------------
			,error : function(){
				alert("서버와 통신 실패!");
			}
			
		});
	}
		
		
	
	</script>
</head>
<body><center>

	<div style='cursor:pointer;' onclick="location.replace('/z_spring/logout.do')">
	[로그아웃]
	</div>
	
	<%
		BoardDTO board = (BoardDTO)request.getAttribute("board");
	%>

	<!-- ************************************************ -->
	<!-- [게시판 글쓰기] 화면을 출력하는 form 태그 선언 -->
	<!-- ************************************************ -->
	<form name="boardUpDelForm" method="post">
	
		<b>[글 수정/삭제]</b><br> 
		<table class="tbcss1" border=1 cellpadding=5 bordercolor="lightgray" align="center">
			<tr>
				<th bgcolor="lightgray">이 름 
				<!---------------------------->
				<td><input type="text" name="writer" maxlength="10" size="10" value="<%=board.getWriter()%>">
			<tr><!---------------------------->
				<th bgcolor="lightgray">제 목  
				<!---------------------------->
				<td><input type="text" name="subject" maxlength="30" size="40" value="<%=board.getSubject()%>">
			<tr><!---------------------------->
				<th bgcolor="lightgray">이메일 
				<!---------------------------->
				<td><input type="text" name="email" maxlength="30" size="40" value="<%=board.getEmail()%>">
			<tr><!---------------------------->
				<th bgcolor="lightgray">내 용 
				<!---------------------------->
				<td><textarea name="content" maxlength="300" rows="13" cols="40"><%=board.getContent()%></textarea>
			<tr><!---------------------------->
				<th bgcolor="lightgray">비밀번호  
				<!---------------------------->
				<td><input type="password" name="pwd" maxlength="4" size="8">
				<!---------------------------->
		</table>
		<!----------------------------------------------------->
		<div style="height:6"></div>
		<!----------------------------------------------------->
		<input type="hidden" name="b_no" value="<%=board.getB_no()%>">
		<input type="hidden" name="upDel" value="up">
		<!----------------------------------------------------->
		<input type="button" value="수정" onClick="checkBoardUpDelForm('up')">
		<!----------------------------------------------------->
		<input type="reset" value="삭제" onClick="checkBoardUpDelForm('del')">
		<!----------------------------------------------------->
		<input type="button" value="목록보기" onClick="document.boardListForm.submit();">
		<!----------------------------------------------------->
	</form>
	
	<!-- ************************************************ -->
	<!-- [게시판 목록] 화면으로 이동하는 form 태그 선언 -->
	<!-- ************************************************ -->
	<form name="boardListForm" method="post" action="/z_spring/boardList.do">
	</form>
</body>
</html>




