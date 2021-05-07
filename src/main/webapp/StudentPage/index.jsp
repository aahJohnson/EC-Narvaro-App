<%@page import="Controller.StudentServlet"%>
<%@page import="org.apache.jasper.tagplugins.jstl.core.ForEach"%>
<%@page import="ConDB.narvaroDAO"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Date"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@page import="Bean.CourseBean"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">

<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0-beta3/dist/css/bootstrap.min.css"
	rel="stylesheet"
	integrity="sha384-eOJMYsd53ii+scO/bJGFsiCZc+5NDVN2yr8+0RDqr0Ql0h+rP48ckxlpbzKgwra6"
	crossorigin="anonymous">

<link rel="stylesheet" href="StudentCSS/style.css">

<link rel="stylesheet" href="StudentCSS/divStyle.css">

<title>Student page</title>
</head>
<body>

	<div class="blue-bg"></div>
	<div class="white-bg shadow"></div>
	<div class="content"></div>

	<div class="container">
		<div class="div1">

			<label>Kurs: <%=request.getAttribute("kurs")%></label>

			<div class="div2">
				<label>Lektion: <%=request.getAttribute("lektion")%>
				</label>

				<div class="div3">
					<label>Närvaro:
						<button type="submit" name="attending">Närvarande</button>

						<button type="submit" name="sick">Sjuk</button>

						<button type="submit" name="absent">Frånvarande</button>

						<button type="submit" name="childcare">Vård av barn</button>
					</label>
				</div>
			</div>

			<div class="div3">
				<label for="items">Kurs1:</label>

				<!-- The value for `for=""` and `id=""` has to be same. -->

				<select id="items">
					<option value="item-1">Item 1</option>
					<option value="item-2">Item 2</option>
					<option value="item-3">Item 3</option>
					<option value="item-4">Item 4</option>
				</select>
			</div>

			<label>Namn: ${user.firstName } ${user.lastName }</label> <label>Total
				närvaro: <%=request.getAttribute("narvaro")%>%
			</label> <label>Kurs närvaro: <%=request.getAttribute("course")%>%
			</label>

			<table class="table table-info">
				<thead>
					<tr>
						<th scope="col">Datum</th>
						<c:forEach items="${dateLesson}" var="date">

							<th scope="col">${date}</th>

						</c:forEach>
					</tr>
				</thead>
				<tbody>
					<tr>
						<th scope="row">1</th>

						<c:forEach items="${lessonAttendance}" var="attendance">
							<td><button type="submit">${attendance}%</button></td>
						</c:forEach>

					</tr>
				</tbody>
			</table>

			<form action="<%=request.getContextPath()%>/Logout" method="get">

				<input type="submit" value="Logout">

			</form>
</body>
</html>