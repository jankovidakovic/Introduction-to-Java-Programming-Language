<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" session="true"%>
<!DOCTYPE html>

<html>
	<head>
		<meta charset="UTF-8">
		<title>Home page</title>
		<style>
			a {
				display: block;
			}
		</style>
	</head>
	
	<%
	String color = "white";
	try {
		color = (String) request.getSession().getAttribute("pickedBgCol");
		if (color == null) {
			color = "white";
		}
	} catch (Exception e) {}
	%>
	
<body bgcolor="<% 
	if (request.getSession().getAttribute("pickedBgCol") == null) {
		out.write("white");
	} else {
		out.write(request.getSession().getAttribute("pickedBgCol").toString());
	}
	  %>">
	<a href="./colors.jsp">Background color chooser</a>
	
	<form action="trigonometric" method="GET">
	  Početni kut:<br>
	  	<input type="number" name="a" min="0" max="360" step="1" value="0">
	  <br>  
	  Završni kut:<br>
	  <input type="number" name="b" min="0" max="360" step="1" value="360"
	  ><br>  
	  <input type="submit" value="Tabeliraj">
	  <input type="reset" value="Reset">
	 </form>
	 
	 <a href="trigonometric?a=0&b=90">Example of a trigonometric table</a>
	 <a href="stories/funny.jsp">Funny story</a>
	 <a href="powers?a=1&b=100&n=3">Powers</a>
	 <a href="appinfo.jsp">Web app info</a>
	 <a href="glasanje">Favorite band voting</a>
</body>
</html>