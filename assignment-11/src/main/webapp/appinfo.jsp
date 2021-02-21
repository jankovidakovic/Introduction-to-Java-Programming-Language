<%@page import="java.util.Date"%>
<%@page import="java.time.format.DateTimeFormatter"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Timer"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" session="true"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Web application info</title>
</head>

<body bgcolor="<% 
	if (request.getSession().getAttribute("pickedBgCol") == null) {
		out.write("white");
	} else {
		out.write(request.getSession().getAttribute("pickedBgCol").toString());
	}
	  %>">
	<h1>Applicarion running time: <%
		Long startTime = (long) request.getServletContext().getAttribute("startTime");
		Long currentTime = System.currentTimeMillis();
		Long diff = currentTime - startTime;
		//milliseconds
		String formattedOutput = diff % 1000 + " milliseconds";
		diff /= 1000;
		//seconds
		formattedOutput = diff % 60 + " seconds " + formattedOutput;
		diff /= 60;
		//minutes
		formattedOutput = diff % 60 + " minutes " + formattedOutput;
		diff /= 60;
		//hours
		formattedOutput = diff % 60 + " hours " + formattedOutput;
		diff /= 60;
		//days
		formattedOutput = diff % 24 + " days " + formattedOutput;
		out.write(formattedOutput);
	%></h1>
</body>
</html>