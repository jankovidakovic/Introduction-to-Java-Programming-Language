<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
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
<body>
	<h1>Poll results</h1>
	<p>These are the poll results:</p>
	<table border="1" cellspacing="0" class="rez">
		<thead><tr>
			<th>${pollElement}</th><th>Votes</th>
		</tr></thead>
		<tbody>
			<c:forEach var="option" items="${options}">
				<tr>
					<td>${option.optionTitle}</td>
					<td>${option.votesCount}</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	
	<h2>Graphical representation of the results:</h2>
	<img src="./glasanje-grafika?pollID=${pollID}" alt="Pie-chart"/>
	
	<h2>Results in XLS:</h2>
	<p>Results are available <a href="./glasanje-xls?pollID=${pollID}&pollElement=${pollElement}">here</a></p>
	
	<h2>Miscellaneous</h2>
	<p>Winners:</p>
	<ul>
		<c:forEach var="winner" items="${winners}">
			<li>
				<a href="${winner.optionLink}" target="_blank">
					${winner.optionTitle}
				</a>
			</li>
		</c:forEach>
	</ul>
</body>
</html>