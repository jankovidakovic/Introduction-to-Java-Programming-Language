<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>${poll.title}</title>
</head>
<body>
	<h1>${poll.title}</h1>
	<p>${poll.message}</p>
	<ol>
		<c:forEach var="option" items="${options}">
			<li>
				<a href="glasanje-glasaj?id=${option.id}">${option.optionTitle}</a>
		</c:forEach>
	</ol>
</body>
</html>