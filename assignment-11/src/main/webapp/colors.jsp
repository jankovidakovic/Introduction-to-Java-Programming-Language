<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" session="true" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Color chooser</title>
</head>

<body bgcolor="<% 
	if (request.getSession().getAttribute("pickedBgCol") == null) {
		out.write("white");
	} else {
		out.write(request.getSession().getAttribute("pickedBgCol").toString());
	}
	  %>">
	
	<a href="setcolor?color=white">WHITE</a>
	
	<a href="setcolor?color=red">RED</a>
	
	<a href="setcolor?color=green">GREEN</a>
	
	<a href="setcolor?color=cyan">CYAN</a>
	
</body>
</html>