<%@page import="Bean.LoginBean"%>
<%@page import="java.time.LocalDate"%>
<%@page import="Bean.AttendanceBean"%>
<%@page import="org.apache.jasper.tagplugins.jstl.core.ForEach"%>
<%@page import="conDB.AttendanceDAO"%>
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

<title>Student page</title>

<script type="text/javascript" src="script.js"></script>

</head>
<body>

	<div class="blue-bg"></div>
	<div class="white-bg shadow"></div>
	<div class="content"></div>

	<div class="container">

		<label class="textStyle">Aktuell Kurs: <%=request.getAttribute("courseName")%></label>

		<div class="attendanceButtons">
			<label class="textStyle">Lektion: <%=request.getAttribute("cookieValue")%> <br>
				<select id="items" onchange="date(this.value)">
					<option value="0">Välj tidigare datum</option>
					<c:forEach items="${dateLesson }" var="date">
						<option value="${date }">${date }</option>
					</c:forEach>
			</select>

			</label> <br>
			<div class="btn-group">
				<button type="submit" name="attending" class="btn btn-success"
					onclick="setAttentionPercentage(${user.users_id}, 100); ">Närvarande</button>
				<select id="items"
					class="btn btn-success dropdown-toggle dropdown-toggle-split"
					data-toggle="dropdown" aria-haspopup="true" aria-expanded="false"
					onchange="setAttentionPercentage(${user.users_id}, this.value);">
					<option></option>
					<option value="75">75%</option>
					<option value="50">50%</option>
					<option value="25">25%</option>
				</select>
			</div>

			<button type="submit" name="sick" class="btn btn-danger"
				onclick="setAttentionPercentage(${user.users_id}, 0); ">Sjuk</button>

			<button type="submit" name="absent" class="btn btn-danger"
				onclick="setAttentionPercentage(${user.users_id}, 0); ">Frånvarande</button>

			<button type="submit" name="childcare" class="btn btn-danger"
				onclick="setAttentionPercentage(${user.users_id}, 0); ">Vård
				av barn</button>


		</div>

		<div class="div3">
			<select id="items" onchange="location.reload();">
				<option value="0" class="btn btn-dark">Välj kurs</option>
				<c:forEach items="${courseNameList }" var="name">
					<option value="${name }" class="btn btn-dark">${name }</option>
				</c:forEach>

			</select>
		</div>

		<div class="name">
		<label>Namn: ${user.firstName } ${user.lastName }</label> <br><br>
		<label>	Total Närvaro: <%=request.getAttribute("totalAttention")%>%</label> <br>
		<label>Kurs Närvaro: <%=request.getAttribute("courseAttention")%>%</label>
		</div>

		<table class="table">
			<thead>
				<tr>
					<th scope="col">Datum</th>

					<c:forEach items="${dateLesson}" var="date">

						<th scope="col" class="dates">${date}</th>

					</c:forEach>

				</tr>
			</thead>
			<tbody>
				<tr>
					<th scope="row">Lektion</th>
					<c:forEach items="${lessonAttendance }" var="attendance">

						<td><button type="submit" class="btn btn-secondary">${attendance }%</button></td>

					</c:forEach>
				</tr>

			</tbody>
		</table>

		<form action="<%=request.getContextPath()%>/Logout" method="get">

			<input type="submit" value="Logout" class="btn btn-danger">

		</form>
	</div>
	
</body>
<style>
.container {
	margin-top: 500px;
	align-content : center;
	text-align: center;
}

.textStyle {
	font-family: serif;
	font-size: 20px;
	margin-bottom: 20px;
}

.dates {
	font-size: 20px;
}

.activeCourse {
font-style: italic;
margin-bottom: 20px;
font-size: 20px;
}

.div3 {
margin: 1em;

}

.name {
margin: 1em;
font-size: 20px;
font-family: cursive;

}

<!-- Bootstrap -->
@import url(https://fonts.googleapis.com/css?family=Montserrat);

@import url("https://fonts.googleapis.com/css?family=Open+Sans");

h1 {
	font-family: "Montserrat", sans-serif;
}

p {
	font-family: "Open Sans", sans-serif;
	text-align: justify;
}

.blue-bg {
	position: fixed;
	background-image: url("images/header.jpg");
	background-size: cover;
	background-color: #58aff6;
	top: 0;
	height: 600px;
	width: 100vw;
	z-index: -1;
}

.white-bg {
	position: absolute;
	top: 0;
	background-image: url("images/background.jpg");
	min-height: 500px;
	margin: 410px 0px;
	width: 100vw;
	height: 100vh;
	-webkit-transform: skewY(5deg);
	transform: skewY(5deg);
	z-index: -1;
}

.content {
	position: absolute;
	top: 0;
	margin: 250px 10vw;
	max-width: 60vw;
	background-color: #fff;
}

.shadow {
	box-shadow: -2px -5px 5px 0px rgba(0, 0, 0, 0.3);
}
</style>
</html>