<%@page import="java.util.Date"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	Formularz GET <BR>
	<form action="dane1.jsp" method="get">
		Podaj id:<br> <input name="name1" value="Łódź" type="text"><br>
		<input type="submit" value="OK">
	</form>
	<HR>
	Formularz POST <BR>
	<form action="dane1.jsp" method="post">
		Podaj id:<br> <input name="name2" value="Łódź" type="text"><br>
		<input type="submit" value="OK">
	</form>
	<HR>
	Formularz GET SERVLET <BR>
	<form action="dane1.do" method="get">
		Podaj id:<br> <input name="name3" value="Łódź" type="text"><br>
		<input type="submit" value="OK">
	</form>
	<HR>
	Formularz POST SERVLET <BR>
	<form action="dane1.do" method="post">
		Podaj id:<br> <input name="name4" value="Łódź" type="text"><br>
		<input type="submit" value="OK">
	</form>
</body>
</html>