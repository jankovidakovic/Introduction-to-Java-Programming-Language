<%@page import="hr.fer.zemris.java.blogapp.model.BlogUser"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" session="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>New blog entry</title>
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
		<form method="post">
			<fieldset>
				<legend>
					<c:choose>
						<c:when test="${blogEntry == null}">
									Create a new blog entry
								</c:when>
						<c:otherwise>
									Edit a blog entry
								</c:otherwise>
					</c:choose>
				</legend>
				<label for="title">Title:</label> <input type="text" name="title"
					id="title" <% if (request.getAttribute("blogEntry") != null) { %>
					value="${blogEntry.title}" <% } %>>
				<c:if test="${blogEntryForm.hasError('title')}">
					<span>${blogEntryForm.getError('title')}</span>
				</c:if>
				<br> <label for="content">Content:</label>
				<textarea name="text" id="text" rows="4" cols="50"><c:out
						value="${blogEntry.text}"></c:out></textarea>
				<c:if test="${blogEntryForm.hasError('text')}">
					<span>${blogEntryForm.getError('text')}</span>
				</c:if>
				<br> <br> <input type="submit">
			</fieldset>
		</form>

	</main>
</body>
</html>