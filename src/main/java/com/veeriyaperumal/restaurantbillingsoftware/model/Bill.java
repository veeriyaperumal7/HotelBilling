package com.veeriyaperumal.restaurantbillingsoftware.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class Bill {

	private int billNo,userId;
	private LocalDate billDate;
	private LocalTime billTime;	
	private ArrayList<Dish> purchasedDish = new ArrayList<>();
	private float billPrice;
    private String paymentType;

	public int getBillNo() {
		return billNo;
	}

	public void setBillNo(int billNo) {
		this.billNo = billNo;
	}

	public LocalDate getBillDate() {
		return billDate;
	}

	public void setBillDate(LocalDate localDate) {
		this.billDate = localDate;
	}

	public LocalTime getBillTime() {
		return billTime;
	}

	public void setBillTime(LocalTime billTime) {
		this.billTime = billTime;
	}

	public ArrayList<Dish> getPurchasedDish() {
		return purchasedDish;
	}

	public void setPurchasedDish(ArrayList<Dish> purchasedDish) {
		this.purchasedDish = purchasedDish;
	}

	public float getBillPrice() {
		return billPrice;
	}

	public void setBillPrice(float billPrice) {
		this.billPrice = billPrice;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}
}
