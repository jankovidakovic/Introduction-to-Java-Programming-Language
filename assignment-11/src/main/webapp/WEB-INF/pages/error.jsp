<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" session="true"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>

<body bgcolor="<% 
	if (request.getSession().getAttribute("pickedBgCol") == null) {
		out.write("white");
	} else {
		out.write(request.getSession().getAttribute("pickedBgCol").toString());
	}
	  %>">
	<h1>Error: invalid parameters.</h1>
	<h3>Valid parameter values:</h3>
	<ul>
		<li>a: integer not smaller than -100 and not greater than 100</li>
		<li>b: integer not smaller than -100 and not greater than 100</li>
		<li>a: integer not smaller than 1 and not greater than 5</li>
	</ul>
	<a href="index.jsp">Back to home</a>
	
</body>
</html>