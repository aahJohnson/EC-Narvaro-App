<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@page import="Controller.adminPage" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">

<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0-beta3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-eOJMYsd53ii+scO/bJGFsiCZc+5NDVN2yr8+0RDqr0Ql0h+rP48ckxlpbzKgwra6" crossorigin="anonymous">
<link rel="stylesheet" href="AdminCSS/style.css">
<link rel="stylesheet" href="AdminCSS/adminStyle.css" type="text/css">

<title>Adminsida</title>
</head>
<body>

<form action="<%= request.getContextPath() %>/logout" method="get">
<input type="submit" value="Logout">
</form>

<main>

<div class="blue-bg"></div>
<div class="white-bg shadow"></div>
<div class="content"></div>

<h1>Admin</h1>
<h1>${overview}</h1>

<button id="settings" name="settings">Settings</button>

<form>
<label for="overview">Välj översikt: </label>
<select name="overview" id="overview">

<option value="class">Klass</option>
<option value="course">Kurs</option>
<option value="location">Ort</option>

</select><br>

<label for="overview-secondary">Välj: </label>
<select name="overview-secondary" id="overview-secondary">

<option value="choice1">Klass 1</option>
<option value="choice2">Klass 2</option>
<option value="choice3">Klass 3</option>

</select><br>

<label>Välj var du vill ha notiser:</label>
<button type="submit">Email</button>
<button type="submit">Systemet</button>
</form>

<table class="table" id="table">
  <thead>
    <tr>
      <th scope="col">#</th>
      <th scope="col">First</th>
      <th scope="col">Last</th>
      <th scope="col">Handle</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <th scope="row">1</th>
      <td>Mark</td>
      <td>Otto</td>
      <td>@mdo</td>
    </tr>
    <tr>
      <th scope="row">2</th>
      <td>Jacob</td>
      <td>Thornton</td>
      <td>@fat</td>
    </tr>
    <tr>
      <th scope="row">3</th>
      <td colspan="2">Larry the Bird</td>
      <td>@twitter</td>
    </tr>
  </tbody>
</table>

</main>
</body>
</html>