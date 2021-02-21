<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" session="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Error</title>
</head>
<body>
	<header>
		<%
			if (request.getSession().getAttribute("current.user.id") != null) {
		%>
		Logged in as:
		<%=request.getSession().getAttribute("current.user.firstName") + " "
		+ request.getSession().getAttribute("current.user.lastName")%>
		<a href="/blog/servleti/main/logout">Log out</a>
		<%
			} else {
		%>
		Not logged in. <a href="/blog/servleti/main">Log in</a>
		<%
			}
		%>
	</header>
	<main class="main">
		<h1>Error</h1>
		<p><%= request.getAttribute("error") %></p>
		<a href="/blog/servleti/main">Back to home page</a>
	</main>
</body>
</html>