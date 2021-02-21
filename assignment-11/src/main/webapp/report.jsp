<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" session="true"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>OS Usage report</title>
</head>


<body bgcolor="<% 
	if (request.getSession().getAttribute("pickedBgCol") == null) {
		out.write("white");
	} else {
		out.write(request.getSession().getAttribute("pickedBgCol").toString());
	}
	  %>">
	<h1>OS Usage</h1>
	<p>
		Here are the results of OS usage in survey that we completed.
	</p>
	
	<img src="./reportImage" alt="Report image"/>
	
</body>
</html>