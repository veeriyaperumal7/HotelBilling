<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page
	import="com.veeriyaperumal.restaurantbillingsoftware.repository.Repository"%>
<%@ page
	import="com.veeriyaperumal.restaurantbillingsoftware.model.User"%>
<%@ page
	import="com.veeriyaperumal.restaurantbillingsoftware.model.Dish"%>
<%@ page import="java.io.PrintWriter"%>
<%@ page import="java.util.Arrays"%>
<%@ page import="org.json.simple.JSONObject"%>
<%@ page import="org.json.simple.parser.JSONParser"%>
<%@ page import="org.json.simple.parser.ParseException"%>
<%@ page import="java.net.URLDecoder"%>

<%
// Retrieve cookies from the request
Cookie[] cookies = request.getCookies();

boolean cookieFound = false;
String userJsonString = null, title = null, username = null, role = null;

if (cookies != null) {
	for (Cookie cookie : cookies) {
		if (cookie.getName().equals("UserData")) {
	userJsonString = URLDecoder.decode(cookie.getValue(), "UTF-8");
	JSONObject userJson = (JSONObject) new JSONParser().parse(userJsonString);

	title = (String) userJson.get("title");
	username = (String) userJson.get("username");
	role = (String) userJson.get("role");

	// Use user details as needed

	cookieFound = true;
	break;
		}
	}
}

if (!cookieFound) {
	// If the cookie is not found, redirect to the login page
	response.sendRedirect("index.jsp");
}
%>



<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Bill Cancel here</title>
<link rel="stylesheet" href="CancelBill.css">
</head>
<body>

	<section id="navBlock">
		<article>
			<div class="userBlock">
				<img class="usericonimage" src="./assests/usericon.png" alt="image">
				<div class="UserName">
					<%
					out.print(username + "   " + role);
					%>
				</div>
			</div>
			<div class="menuBlock">
				<ul>
					<li>
						<h1>BILL CANCEL</h1>
					</li>
				</ul>
			</div>
		</article>
	</section>

	<div class="InteractArea">
		<div class="DatePickerArea">
			<label for="datepicker">Bill date:</label> <input type="date"
				id="datepicker">
		</div>
		<div class="BillComboBox">
			<label for="BilllNoSelect">Select Bill No :</label> <select
				id="BillNoBox">
				<option value=100>
			</select>
		</div>

	</div>





</body>
</html>