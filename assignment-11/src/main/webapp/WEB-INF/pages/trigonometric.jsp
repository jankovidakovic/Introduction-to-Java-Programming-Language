<%@page import="java.io.IOException"%>
<%@page import="hr.fer.zemris.java.webapp2.models.TrigEntry"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" session="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Trigonometric values</title>
<style>
	table, td {
		border: 1px solid black;
		border-collapse: collapse;
	}
	thead {
		font-weight: bold;
	}
</style>
</head>

<body bgcolor="<% 
	if (request.getSession().getAttribute("pickedBgCol") == null) {
		out.write("white");
	} else {
		out.write(request.getSession().getAttribute("pickedBgCol").toString());
	}
	  %>">
	<h1>Table of trigonometric values for the range ${a}, ${b} </h1>
	<table>
		<thead>
			<tr>
				<td>Number</td>
				<td>Sine value</td>
				<td>Cosine value</td>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="entry" items="${trigEntries}" >
				<tr>
					<td style="font-weight: bold">${entry.num}</td>
					<td>${entry.sinValue}</td>
					<td>${entry.cosValue}</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</body>
</html>