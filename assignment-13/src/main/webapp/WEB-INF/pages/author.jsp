<%@page import="hr.fer.zemris.java.blogapp.model.BlogUser"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" session="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Author home page</title>
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
		<h1>
			<%=request.getAttribute("author.nick")%>'s blog
		</h1>
		<c:choose>
			<c:when test="${ !blogEntries.isEmpty() }">
				<h3>Blog Entries:</h3>
				<ul>
					<c:forEach var="blogEntry" items="${blogEntries}">
						<li><a
							href="<%= request.getAttribute("author.nick")%>/${blogEntry.id}">${blogEntry.title}</a></li>
					</c:forEach>
				</ul>
			</c:when>
			<c:otherwise>
				<c:out value="No blog entries yet"></c:out>
			</c:otherwise>
		</c:choose>

		<%
			if (request.getSession().getAttribute("current.user.id") != null
				&& (request.getSession().getAttribute("current.user.nick")
				.equals(request.getAttribute("author.nick")))) {
		%>
		<a href="<%=request.getAttribute("author.nick")%>/new">Add a new
			blog entry</a>
		<%
			}
		%>

	</main>
</body>
</html>