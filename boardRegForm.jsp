<!-- ************************************************************* -->
<!-- JSP 기술의 한종류인 [Page Directive]를 이용하여 현 JSP 페이지 처리 방식 선언하기 -->
<!-- ************************************************************* -->
	<!-- 현재 이 JSP 페이지 실행 후 생성되는 문서는 HTML 이고, 이 문서 안의 데이터는 UTF-8 방식으로 인코딩한다 라고 설정함 -->
	<!-- 현재 이 JSP 페이지는 UTF-8 방식으로 인코딩한다 -->
	<!-- UTF-8 인코딩 방식은 한글을 포함 전 세계 모든 문자열을 부호화 할 수 있는 방법이다. -->
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<% int b_no = (Integer)request.getAttribute("b_no"); %>

<!DOCTYPE html>
<html>

<!--*****************************************************-->
<!-- JSP 기술의 한 종류인 [Include Directive]를 이용하여 -->
<!-- common.jsp 파일 내의 소스를 삽입하기 -->
<!--*****************************************************-->
<%@include file="common.jsp" %>

<head>
<title>게시판 글쓰기</title>
	<script>
	//****************************************
	//[게시판 등록 화면]에 입력된 데이터의 유효성 체크 함수 선언
	//****************************************
	function checkBoardRegForm(){
		
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
		
		if(new RegExp(/^[0-9]{4}$/).test(pwd)==false){
			alert("암호는 숫자 4개를 입력해주세요");
			return;
		}
		//----------------------------
		// 현재 화면에서 페이지 이동없이 서버쪽 "/z_spring/bpardRegProc.do"를 호출하여
		// 게시판 글을 입력하고 [게시판 입력 행 적용 개수]를 받기
		//----------------------------
		$.ajax({
			//------------------
			// 호출할 서버쪽 URL 주소 설정
			//------------------
			url : "/z_spring/boardRegProc.do"
			//------------------
			// 전송 방법 설정
			//------------------
			,type : "post"
			//------------------
			// 서버로 보낼 파라미터명과 파라미터값을 설정
			//------------------
			,data : $('[name=boardRegForm]').serialize()
			//----------------------------
			// 서버의 응답을 성공적으로 받았을 경우 실행할 익명함수 설정.
			// 매개변수 boardRegCnt 에는 입력 행의 개수가 들어온다. 
			//----------------------------
			,success : function(boardRegCnt){
				if(boardRegCnt==1){
					<% if(b_no==0){ %>
							alert("게시판 새글쓰기 성공!");
					<% }else{ %>
							alert("게시판 댓글쓰기 성공!");
					<% } %>
					location.replace("/z_spring/boardList.do");
				}
				// [게시판 새글 입력 행 적용 개수]가 1개가 아니면 경고하기
				else{
					alert("게시판 등록 실패! 관리자에게 문의 바람.");
				}
			}
			//----------------------------
			// 서버의 응답을 못 받았을 경우 실행할 익명함수 설정
			//----------------------------
			,error : function(){
				alert("서버 접속 실패")
			}
			
			
		});
	}
	
	</script>
</head>
<body><center>
<div style='cursor:pointer;' onclick="location.replace('/z_spring/logout.do')">
	[로그아웃]
	</div>
	<!-- ************************************************ -->
	<!-- [게시판 글쓰기] 화면을 출력하는 form 태그 선언 -->
	<!-- ************************************************ -->
	<form name="boardRegForm" method="post" action="/z_spring/boardRegProc.do">
		<%
		 
		 if(b_no==0){
			 out.print("<b>[새 글쓰기]</b><br>");
		 }else{
			 out.print("<b>[댓글쓰기]</b><br>");
		 }
		%>
	
	
		<b>[새 글쓰기]</b><br> 
		<table class="tbcss1" border=1 cellpadding=5 bordercolor="lightgray" align="center">
			<tr>
				<th bgcolor="lightgray">이 름 
				<!---------------------------->
				<td><input type="text" name="writer" maxlength="10" size="10">
			<tr><!---------------------------->
				<th bgcolor="lightgray">제 목  
				<!---------------------------->
				<td><input type="text" name="subject" maxlength="30" size="40">
			<tr><!---------------------------->
				<th bgcolor="lightgray">이메일 
				<!---------------------------->
				<td><input type="text" name="email" maxlength="30" size="40">
			<tr><!---------------------------->
				<th bgcolor="lightgray">내 용 
				<!---------------------------->
				<td><textarea name="content" maxlength="300" rows="13" cols="40"></textarea>
			<tr><!---------------------------->
				<th bgcolor="lightgray">비밀번호  
				<!---------------------------->
				<td><input type="password" name="pwd" maxlength="4" size="8">
				<!---------------------------->
		</table>
		<!---------------------------->
		<div style="height:6"></div>
		<!---------------------------->
		<input type="hidden" name="b_no" value="<%=b_no%>">
		<!---------------------------->
		<input type="button" value="저장" onClick="checkBoardRegForm()">
		<!---------------------------->
		<input type="reset" value="다시작성">
		<!---------------------------->
		<input type="button" value="목록보기" onClick="document.boardListForm.submit();">
		<!---------------------------->
	</form>
	
	<!-- ************************************************ -->
	<!-- [게시판 목록] 화면으로 이동하는 form 태그 선언 -->
	<!-- ************************************************ -->
	<form name="boardListForm" method="post" action="/z_spring/boardList.do"></form>
</body>
</html>




