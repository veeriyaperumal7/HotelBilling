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
    <link rel="stylesheet" href="DashBoard.css">
    <title>Dash Board</title>
</head>

<body>

    <section id="navBlock">
        <article>
            <div class="userBlock">
                <img class="usericonimage" src="./assests/usericon.png" alt="image" img>
                <div class="UserName">
                    <%
                    if (cookieFound) {
                        out.print(username + "   " + role);
                    }
                    %>
                </div>
            </div>
            <div class="menuBlock">
                <ul>
                    <li><a href="Billing.jsp">Billing</a></li>
                     <li><a href="CancelBill.jsp">BillBill</a></li>
                    <li><a href="#">Employee</a></li>
                    <li><a href="#">Product</a></li>
                    <li><a href="#">Report</a></li>
                    <li><a href="#">Help</a></li>
                    <li><a href="#">Log out</a></li>
                </ul>
            </div>
        </article>
    </section>

</body>

</html>