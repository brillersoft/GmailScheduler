package com.app.pojo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class OrganisationDashboardPOJO {
	
private HashMap<Integer , List<Integer>> map;
	
	private ArrayList<String> dataBarLabels;
	
	private ArrayList<Integer> dataBarRevenue;
	
	private ArrayList<Integer> dataBarMargin;

	public HashMap<Integer, List<Integer>> getMap() {
		return map;
	}

	public void setMap(HashMap<Integer, List<Integer>> map) {
		this.map = map;
	}

	

	public ArrayList<String> getDataBarLabels() {
		return dataBarLabels;
	}

	public void setDataBarLabels(ArrayList<String> dataBarLabels) {
		this.dataBarLabels = dataBarLabels;
	}

	public ArrayList<Integer> getDataBarRevenue() {
		return dataBarRevenue;
	}

	public void setDataBarRevenue(ArrayList<Integer> dataBarRevenue) {
		this.dataBarRevenue = dataBarRevenue;
	}

	public ArrayList<Integer> getDataBarMargin() {
		return dataBarMargin;
	}

	public void setDataBarMargin(ArrayList<Integer> dataBarMargin) {
		this.dataBarMargin = dataBarMargin;
	}

}
