<%@page import="Bean.LoginBean"%>
<%@page import="java.time.LocalDate"%>
<%@page import="Bean.NarvaroBean"%>
<%@page import="org.apache.jasper.tagplugins.jstl.core.ForEach"%>
<%@page import="conDB.narvaroDAO"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Date"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

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

		<label>Kurs: <%=request.getAttribute("kurs")%></label>

		<div class="attendanceButtons">
			<label>Lektion: <%=request.getAttribute("lektion")%>
			</label> <br> <label>Närvaro:
				<button type="submit" name="attending" onclick="setAttentionPercentage(${user.users_id}, 100)">Närvaro</button>
					
				<select id="items" onchange="setAttentionPercentage(${user.users_id}, this.value)">
				<option value="0">Välj procent</option>
					<option value="75">75%</option>
					<option value="50">50%</option>
					<option value="25">25%</option>
			</select>
			
				<button type="submit" name="sick"
					onclick="setAttentionPercentage(${user.users_id}, 0)">Sjuk</button>

				<button type="submit" name="absent"
					onclick="setAttentionPercentage(${user.users_id}, 0)">Frånvarande</button>

				<button type="submit" name="childcare" onclick="setAttentionPercentage(${user.users_id}, 0)">Vård av barn</button> 
					


			</label>

		</div>

		<div class="div3">
			<label for="items">Kurs1:</label>

			<!-- The value for `for=""` and `id=""` has to be same. -->


			<select id="items">
				<option value="item-0"></option>
				<option value="item-1">Item 1</option>
				<option value="item-2">Item 2</option>
				<option value="item-3">Item 3</option>
				<option value="item-4">Item 4</option>
			</select>
		</div>


		<label>Namnet:${user.firstName } ${user.lastName } </label> <label>Total
			Närvaro:<%=request.getAttribute("narvaro")%>%
		</label> <label>Kurs Närvaro:<%=request.getAttribute("course")%>%
		</label>

		<table class="table table-info">
			<thead>
				<tr>
					<th scope="col">Datum</th>

					<c:forEach items="${dateLesson}" var="date">

						<th scope="col">${date.getLektion().getDatum()}</th>

					</c:forEach>

				</tr>
			</thead>
			<tbody>
				<tr>
					<th scope="row">Lektion</th>
					<c:forEach items="${lessonAttendance }" var="attendance">

						<td><button type="submit">${attendance }%</button></td>

					</c:forEach>
				</tr>

			</tbody>
		</table>




		<form action="<%=request.getContextPath()%>/Logout" method="get">

			<input type="submit" value="Logout">

		</form>
	</div>

	<script type="text/javascript" src="script.js"></script>

</body>
</html>