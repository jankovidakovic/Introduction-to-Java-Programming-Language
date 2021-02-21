<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>New user registration</title>
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
		<form action="register" method="post">
			<fieldset>
				<legend>Registration</legend>
				<label for="firstName">First name:</label>
				<input type="text" name="firstName" id="firstName">
				<c:if test="${registrationForm != null && registrationForm.hasError('firstName')}">
					<span>${registrationForm.getError('firstName')}</span>
				</c:if> 
				<br>
				
				<label for="lastName">Last name:</label>
				<input type="text" name="lastName" id="lastName">
				<c:if test="${registrationForm != null && registrationForm.hasError('lastName')}">
					<span>${registrationForm.getError('lastName')}</span>
				</c:if>  
				<br>
				
				<label for="email">E-mail:</label>
				<input type="text" name="email" id="email">
				<c:if test="${registrationForm != null && registrationForm.hasError('email')}">
					<span>${registrationForm.getError('email')}</span>
				</c:if>  
				<br>
				
				<label for="nick">Nick:</label>
				<input type="text" name="nick" id="nick">
				<c:if test="${registrationForm != null && registrationForm.hasError('nick')}">
					<span>${registrationForm.getError('nick')}</span>
				</c:if>  
				<br>
				
				<label for="password">Password:</label>
				<input type="password" name="password" id="password">
				<c:if test="${registrationForm != null && registrationForm.hasError('password')}">
					<span>${registrationForm.getError('password')}</span>
				</c:if>  
				<br>
				
				<br> <input type="submit">
				
			</fieldset>
		</form>
	</main>
</body>
</html>