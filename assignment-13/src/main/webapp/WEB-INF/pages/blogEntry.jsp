<%@page import="hr.fer.zemris.java.blogapp.model.BlogEntry"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" session="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>${blogEntry.title}</title>
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

		<h1>${blogEntry.title}</h1>
		<p>${blogEntry.text}</p>
		<% if (request.getSession().getAttribute("current.user.id") != null
			&& request.getSession().getAttribute("current.user.nick").equals(request.getAttribute("author.nick"))) { %>
			<a href="edit?id=${blogEntry.id}">Edit blog entry</a>
		<% } %>

		<c:if test="${!blogEntry.comments.isEmpty()}">
		<h3>Comments:</h3>
			<ul>
				<c:forEach var="e" items="${blogEntry.comments}">
					<li>
						<div style="font-weight: bold">
							[User=
							<c:out value="${e.usersNick}" />
							]
							<c:out value="${e.postedOn}" />
						</div>
						<div style="padding-left: 10px;">
							<c:out value="${e.message}" />
						</div>
					</li>
				</c:forEach>
			</ul>
		</c:if>
		<% if (request.getSession().getAttribute("current.user.id") != null) { %>
				<form method="post">
					<fieldset>
						<legend>Add new comment</legend>
						<label for="comment">Comment:</label> <input type="text"
							name="comment" id="comment"> 
						<c:if test="${error != null}">
							<span>${error}</span>
						</c:if>
							<br> <br> <input
							type="submit">
					</fieldset>
				</form>
			<% } else { %>
				Only logged-in users can post comments. Log in to comment!
		<% } %>

	</main>
</body>
</html>