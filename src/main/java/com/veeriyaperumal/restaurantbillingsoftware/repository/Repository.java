package com.veeriyaperumal.restaurantbillingsoftware.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.veeriyaperumal.restaurantbillingsoftware.databaseconnection.JdbcConnection;
import com.veeriyaperumal.restaurantbillingsoftware.model.Bill;
import com.veeriyaperumal.restaurantbillingsoftware.model.Dish;
import com.veeriyaperumal.restaurantbillingsoftware.model.User;

public class Repository {

	private static Repository repository;
	private String query;
	private ResultSet resultSet;
	// private User currentUser;
	private ArrayList<Dish> dishList = new ArrayList<>();

	private Repository() throws ClassNotFoundException, SQLException {
		// currentUser = new User();
		loadDishList();
	}

	private void loadDishList() throws ClassNotFoundException, SQLException {
		query = "Select * from dish where dish_status=1";
		resultSet = JdbcConnection.getInstance().executeSelectQuery(query);
		while (resultSet.next()) {
			Dish dish = new Dish();
			dish.setDishId(resultSet.getInt("dish_id"));
			dish.setDishName(resultSet.getString("dish_name"));
			dish.setImagePath(resultSet.getString("dish_imagepath"));
			dish.setPrice(resultSet.getFloat("dish_price"));
			dishList.add(dish);
		}

	}

	public static Repository getInstance() throws ClassNotFoundException, SQLException {
		if (repository == null) {
			repository = new Repository();
		}
		return repository;
	}

	public boolean isValidUser(User user) throws SQLException, ClassNotFoundException {
		query = "Select * from employee where email_address='" + user.getEmailId() + "' and employee_password='"
				+ user.getPassword() + "' and employee_status=1";
		resultSet = JdbcConnection.getInstance().executeSelectQuery(query);
		if (resultSet.next()) {
			user.setUserId(resultSet.getInt("employee_id"));
			user.setEmailId(resultSet.getString("email_address"));
			user.setRole(resultSet.getString("employee_role"));
			user.setName(resultSet.getString("employee_name"));
			user.setMobileNumber(resultSet.getString("employee_mobile"));
			user.setUserId(resultSet.getInt("employee_id"));
			return true;
		}

		return false;
	}

	public ArrayList<Dish> getDishList() {
		return dishList;
	}

	public boolean finishBill(Bill newBill) throws ClassNotFoundException, SQLException {
		int billNo = newBill.getBillNo(), rowsAffected = 0;
		for (Dish dish : newBill.getPurchasedDish()) {
			query = "insert into billing_details (bill_no, dish_id, quantity, subtotal, billing_detail__status) "
					+ "values(" + billNo + "," + dish.getDishId() + "," + dish.getQuantity() + ","
					+ dish.getPrice() * dish.getQuantity() + ",1)";
			rowsAffected = JdbcConnection.getInstance().executeInsertOrUpdateQuery(query);
			if (rowsAffected < 1) {
				return false;
			}
		}
		query = "INSERT INTO billing (bill_no, user_id, billing_date, billing_time, total_amount,payment_type, bill_status) "
				+ "VALUES (" + billNo + ", " + newBill.getUserId() + ", '" + newBill.getBillDate()
				+ "', CURRENT_TIME(), " + newBill.getBillPrice() + ",'" + newBill.getPaymentType() + "', 1)";
		return JdbcConnection.getInstance().executeInsertOrUpdateQuery(query) >= 1;
	}

	public int getNewBillNo() throws ClassNotFoundException, SQLException {
		query = "Select max(bill_no) as maxBillNo from billing";
		resultSet = JdbcConnection.getInstance().executeSelectQuery(query);
		if (resultSet.next()) {
			return resultSet.getInt("maxBillNo") + 1;
		}
		return 1;
	}

}
