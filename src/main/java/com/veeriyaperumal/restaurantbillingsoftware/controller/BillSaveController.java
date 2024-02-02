package com.veeriyaperumal.restaurantbillingsoftware.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.io.InputStream;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.veeriyaperumal.restaurantbillingsoftware.model.Bill;
import com.veeriyaperumal.restaurantbillingsoftware.model.Dish;
import com.veeriyaperumal.restaurantbillingsoftware.repository.Repository;

@WebServlet("/BillSaveController")
public class BillSaveController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public BillSaveController() {
		super();
		// TODO Auto-generated constructor stub
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
//		response.getWriter().append("Served at: ").append(request.getContextPath());
		response.setContentType("application/json");
		StringBuilder requestBody = new StringBuilder();
		JSONParser parser = new JSONParser();
		JSONObject json = null;
		Bill newBill = new Bill();
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
		try {
			Object obj = parser.parse(requestBody.toString());
			json = (JSONObject) obj;
		} catch (ParseException e) {
			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().write("{\"message\":\"Invalid JSON format\"}");
			return;
		}
        try {
			newBill.setBillNo(Repository.getInstance().getNewBillNo());
		} catch (ClassNotFoundException | SQLException e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().write("{\"message\":\"Error while putting into data\"}");
			e.printStackTrace();
		}
		newBill.setBillDate(LocalDate.now());
		newBill.setBillTime(LocalTime.now());
		newBill.setPaymentType((String) json.get("paymentType"));
		JSONObject userData = (JSONObject) json.get("userData");
		newBill.setUserId(Math.toIntExact((Long) userData.get("userid")));
		JSONArray dishDataArray = (JSONArray) json.get("dishData");
        float billAmount=0.0f;
		for (int i = 0; i < dishDataArray.size(); i++) {
			JSONObject jsonObject = (JSONObject) dishDataArray.get(i);
			Dish dish = new Dish();
			dish.setDishId(Integer.parseInt((String) jsonObject.get("dishId")));
			dish.setDishName((String) jsonObject.get("dishName"));
			dish.setQuantity(Math.toIntExact((Long) jsonObject.get("dishQuantity")));
			dish.setPrice(Math.toIntExact((Long) jsonObject.get("dishAmount")));
			billAmount+=dish.getPrice()*dish.getQuantity();
			newBill.getPurchasedDish().add(dish);
		}
		newBill.setBillPrice(billAmount);
		
		try {
			if(Repository.getInstance().finishBill(newBill)) {
				response.setStatus(HttpServletResponse.SC_OK);
				response.getWriter().write("{\"message\":\"Bill Saved Successfully\"}");
			}else {
				response.setStatus(HttpServletResponse.SC_OK);
				response.getWriter().write("{\"message\":\"Bill Save Failed\"}");
			}
		} catch (ClassNotFoundException | SQLException e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().write("{\"message\":\"Error while putting into data\"}");
			e.printStackTrace();
		}
	}

}
