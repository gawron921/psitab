<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Welcome User</title>
</head>
<body>
<% 
request.getSession(false);
if (session == null) { %>
  <a href="http://localhost:8080/web01/home.jsp"> Home</a>
  <a href="http://localhost:8080/web01/login.jsp"> Login</a>
  <% 
} else {
    // Already created.
    %>
  <a href="http://localhost:8080/web01/logout.jsp"> Logout</a><% 
}
String name = request.getParameter("name");
%>
<br><br>
<%
out.println("Welcome: "+name);
%>
</body>
</html>