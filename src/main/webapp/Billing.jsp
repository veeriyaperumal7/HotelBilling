<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
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
<meta charset="UTF-8">
<title>Billing</title>
<link rel="stylesheet" href="Billing.css">
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
						<h1>BILLING</h1>
					</li>
				</ul>
			</div>
		</article>
	</section>

	<div class="Container">
		<div class="billing">
			<div class="BillingTableHeaderSection">
				<div id="BillNumberDiv">
					Bill Number: <span id="BillNumberValue"><%=Repository.getInstance().getNewBillNo()%></span>
				</div>
				<div id="BillDateDiv">
					Bill Date: <span id="BillDateValue"></span>
				</div>
			</div>
			<div class="BillingArea">
				<table class="BillingTable" style="width: 100%">
					<thead>
						<th class="BillingTableHeader">S.NO</th>
						<th class="BillingTableHeader">Item Name</th>
						<th class="BillingTableHeader">Quantity</th>
						<th class="BillingTableHeader">Price</th>
					</thead>
					<tbody class="tableBody">
					</tbody>
				</table>
			</div>
			<div class="BillingControlsArea">
				<div class="BillData">
					<div id="TotalText">Total</div>
					<div id="TotalAmount">Rs : 0</div>
				</div>
				<div class="PaymentType">
					<div class="PaymentContainer">
						<label for="paymentType">Payment Type:</label> <select
							id="paymentType" name="paymentType" required>
							<option value="" disabled selected>Select Payment Type</option>
							<option value="CASH">Cash</option>
							<option value="ONLINE PAYMENT">Online Banking</option>
							<option value="CARD">Card</option>
						</select>
					</div>
				</div>
				<div class="BillingButtons">
					<button class="BillButtons" id="Submit" onclick="submitBill(event)">SUBMIT</button>
					<button class="BillButtons" id="Clear" onclick="clearBill()">CLEAR</button>
				</div>


			</div>
		</div>
		<div class="DishArea">
			<div class="userinputfield">
				<input type="text" id="search" name="Search" oninput="filterDishes()" /> <label
					class="label">Search</label>
			</div>
			<div class="DishGrid">
				<%
				for (Dish dish : Repository.getInstance().getDishList()) {
				%>
				<div class="dishItem" data-food="<%=dish.getDishName()%>"
					data-price="<%=dish.getPrice()%>" data-id="<%=dish.getDishId()%>">
					<img src="<%=request.getContextPath()%><%=dish.getImagePath()%>"
						alt="<%=dish.getDishName()%>" class="dishImage">
					<div class="dishDetails">
						<span class="dishName"><%=dish.getDishName()%></span> <br> <span
							class="dishPrice">₹ <%=dish.getPrice()%></span>
					</div>
				</div>
				<%
				}
				%>
			</div>
		</div>
	</div>

	<script src="Billing.js"></script>
</body>

</html>