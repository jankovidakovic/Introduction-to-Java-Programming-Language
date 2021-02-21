<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" session="true"%>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Voting results</title>
<style type="text/css">
	table.rez td {
		text-align: center;
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
	<h1>Voting results</h1>
	<p>These are the voting results:</p>
	<table border="1" cellspacing="0" class="rez">
		<thead><tr><th>Band</th><th>Votes</th></tr></thead>
		<tbody>
			<c:forEach var="result" items="${results}">
				<tr><td>${result.band.name}</td><td>${result.votes}</td></tr>
			</c:forEach>
		</tbody>
	</table>
	
	<h2>Graphical representation of the results:</h2>
	<img src="./glasanje-grafika" alt="Pie-chart"/>
	
	<h2>Results in XLS:</h2>
	<p>Results are available <a href="./glasanje-xls">here</a></p>
	
	<h2>Miscellaneous</h2>
	<p>Songs from the winning bands:</p>
	<ul>
		<c:forEach var="victor" items="${victors}">
			<li><a href="${victor.songUrl}" target="_blank">${victor.name}</a></li>
		</c:forEach>
	</ul>
</body>
</html>