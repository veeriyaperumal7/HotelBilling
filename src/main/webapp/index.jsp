<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="javax.servlet.http.HttpSession"%>
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

	cookieFound = true;
	break;
		}
	}
}

if (cookieFound) {
	// If the "Hotel_Billing_Software" cookie is found, redirect to the dashboard page
	if (role.equals("ADMIN")) {
		response.sendRedirect("AdminDashBoard.jsp");
	} else {
		response.sendRedirect("UserDashBoard.jsp");
	}
}
%>

<!DOCTYPE html>

<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Login Form</title>
<link rel="stylesheet" href="index.css">
</head>
<body>
	<div class="header">
		<h1>Hotel Billing Software</h1>
	</div>
	<form>
		<div class="title">

			<h2>Login</h2>
		</div>

		<div id=loginstatus></div>

		<div class="userinputfield">
			<input type="text" id="emailid" required name="emailid" /> <label
				class="label">Email id</label>
		</div>
		<div class="userinputfield">
			<input type="password" id="password" required name="password" /> <label
				class="label">Password</label>
		</div>

		<div class="submit button">
			<input type="submit" value="Login" onclick="submitForm(event)" />
		</div>
	</form>
</body>


<script>
	async function submitForm(event) {
		event.preventDefault();
		var emailid = document.getElementById("emailid").value;
		var password = document.getElementById("password").value;

		var servletURL = "/RestaurantBillingSoftware/LoginController";
		var data = {
			"emailid" : emailid,
			"password" : password
		}
		const response = await fetch(servletURL, {
			method : "POST",
			headers : {
				"Content-Type" : "application/json",
			},
			body : JSON.stringify(data),
		});
		let text = await response.text();

		if (response.status === 200) {
			if (text === "ADMIN") {
				window.location.href = "/RestaurantBillingSoftware/AdminDashBoard.jsp";
			} else if (text === "USER") {
				window.location.href = "/RestaurantBillingSoftware/UserDashboard.jsp";
			} else {
				var messagebar = document.getElementById("loginstatus");
				messagebar.innerHTML = "<p>Invalid Username or Password!Try again</p>";
			}
		} else {
			alert(text);
			console.log(text);
		}

	}
</script>
</html>