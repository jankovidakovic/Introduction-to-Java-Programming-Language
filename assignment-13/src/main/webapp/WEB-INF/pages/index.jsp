<%@page import="hr.fer.zemris.java.blogapp.model.form.LoginForm"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" session="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Home page</title>
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
		Not logged in.
		<%
			}
		%>
	</header>
	<main class="main">
		<%
			if (request.getSession().getAttribute("current.user.id") == null) {
		%>
		<form method="post">
			<fieldset>
				<legend>Login</legend>
				<label for="nick">Nick:</label> <input type="text" name="nick"
					id="nick"
					<% if ( request.getAttribute("loginForm") != null 
						&& ((LoginForm) request.getAttribute("loginForm")).hasError("nick") == false)  { %>
					value="${loginForm.nick }" <% } %>>
				<c:if test="${loginForm.hasError('nick')}">
					<span> ${loginForm.getError('nick')} </span>
				</c:if>
				<br> <label for="password">Password:</label> <input
					type="password" name="password" id="password">
				<c:if test="${loginForm.hasError('password')}">
					<span> ${loginForm.getError('password')} </span>
				</c:if>
				<br> <br> <input type="submit">
				<!-- add error rendering for wrong username and password -->
			</fieldset>
		</form>
		<p>
			Don't have an account? <a href="register">Sign up</a>
		</p>
		<%
			}
		%>
		<h2>Registered authors:</h2>
		<ul>
			<c:forEach var="author" items="${authors}">
				<li><a href="author/${author.nick}">${author.nick}</a></li>
			</c:forEach>
		</ul>
	</main>
</body>
</html>