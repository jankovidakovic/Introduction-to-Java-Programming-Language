<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Polls</title>
</head>
<body>
	<ul>
		<c:forEach var="poll" items="${polls}">
			<li>
				<a href="glasanje?pollID=${poll.id}">
					${poll.title}
				</a>
			</li>
		</c:forEach>
	</ul>
</body>
</html>