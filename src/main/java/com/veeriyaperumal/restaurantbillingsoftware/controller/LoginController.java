package com.veeriyaperumal.restaurantbillingsoftware.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.veeriyaperumal.restaurantbillingsoftware.model.User;
import com.veeriyaperumal.restaurantbillingsoftware.repository.Repository;

@WebServlet("/LoginController")
public class LoginController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public LoginController() {
        super();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        StringBuilder requestBody = new StringBuilder();

        try {
            InputStream inputStream = request.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                requestBody.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"message\":\"Error reading request body\"}");
            return;
        }

        JSONParser parser = new JSONParser();
        JSONObject json = null;

        try {
            Object obj = parser.parse(requestBody.toString());
            json = (JSONObject) obj;
        } catch (ParseException e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"message\":\"Invalid JSON format\"}");
            return;
        }

        String emailId = (String) json.get("emailid");
        String password = (String) json.get("password");
        User user = new User();
        user.setEmailId(emailId);
        user.setPassword(password);
        try {
            if (Repository.getInstance().isValidUser(user)) {
                JSONObject userJson = createUserJson(user);
                String userJsonString = userJson.toString();

                // Encode the JSON string to ensure it's safe for a cookie
                String encodedUserJsonString = URLEncoder.encode(userJsonString, "UTF-8");

                Cookie userCookie = new Cookie("UserData", encodedUserJsonString);
                response.addCookie(userCookie);

                response.setStatus(HttpServletResponse.SC_OK);
                if (user.getRole().equals("ADMIN")) {
                    response.getWriter().write("ADMIN");
                } else {
                    response.getWriter().write("USER");
                }
            } else {
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().write("Invalid");
            }

        } catch (SQLException | ClassNotFoundException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"message\":\"Internal Server Error\"}");
            e.printStackTrace();
        }
    }

    private JSONObject createUserJson(User user) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("username", user.getName());
        jsonObject.put("userid", user.getUserId());
        jsonObject.put("role", user.getRole());
        return jsonObject;
    }
}