<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" session="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Voting</title>
</head>

<body bgcolor="<% 
	if (request.getSession().getAttribute("pickedBgCol") == null) {
		out.write("white");
	} else {
		out.write(request.getSession().getAttribute("pickedBgCol").toString());
	}
	  %>">
	<h1>Voting for the favorite band:</h1>
	<p>From the following bands, which is your favorite one? Click on the link to vote! </p>
	<ol>
		<c:forEach var="band" items="${bands}">
			<li><a href="glasanje-glasaj?id=${band.id}">${band.name}</a></li>
		</c:forEach>
	</ol>
</body>
</html>