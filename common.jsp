<!-- ************************************************************* -->
<!-- JSP 기술의 한종류인 [Page Directive]를 이용하요 현 JSP 페이지 처리 방식 선언하기 -->
<!-- ************************************************************* -->
	<!-- 현재 이 JSP 페이지 실행 후 생성되는 문서는 HTML 이고, 이 문서 안의 데이터는 UTF-8 방식으로 인코딩한다 라고 설정함 -->
	<!-- 현재 이 JSP 페이지는 UTF-8 방식으로 인코딩한다 -->
	<!-- UTF-8 인코딩 방식은 한글을 포함 전 세계 모든 문자열을 부호화 할 수 있는 방법이다. -->
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!--mmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmm-->
<!-- JSP 페이지에서 사용할 [자바스크립트 파일], [CSS 파일] 수입하기 -->
<!--mmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmm-->
<!------------------------------------------------------------------->
<!-- JSP 페이지에서 사용할 [JQuery API] 수입 -->
<!-- JSP 페이지에서 사용할 [공용 함수 내장된 common.jsp 파일] 수입 -->
<!------------------------------------------------------------------->

<link href="/z_spring/resources/common.css" rel="stylesheet" type="text/css">
<script src="/z_spring/resources/jquery-1.11.0.min.js" type="text/javascript"></script>
<script src="/z_spring/resources/common.js" type="text/javascript"></script>