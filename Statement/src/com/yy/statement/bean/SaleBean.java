package com.yy.statement.bean;

import java.util.HashMap;
import java.util.Map;

public class SaleBean {
	private String dlm;
	private String dlmc;
	private Map<Integer, Integer> tdMap = new HashMap<Integer, Integer>();
	private double saleroomn;

	public SaleBean() {
		super();
	}

	public SaleBean(String dlm, String dlmc, double saleroomn) {
		super();
		this.dlm = dlm;
		this.dlmc = dlmc;
		this.saleroomn = saleroomn;
	}

	public String getDlm() {
		return dlm;
	}

	public void setDlm(String dlm) {
		this.dlm = dlm;
	}

	public String getDlmc() {
		return dlmc;
	}

	public void setDlmc(String dlmc) {
		this.dlmc = dlmc;
	}

	public Map<Integer, Integer> getTdMap() {
		return tdMap;
	}

	public void setTdMap(Map<Integer, Integer> tdMap) {
		this.tdMap = tdMap;
	}

	public double getSaleroomn() {
		return saleroomn;
	}

	public void setSaleroomn(double saleroomn) {
		this.saleroomn = saleroomn;
	}

	public void addSaleroomn(double saleroomn2) {
		this.saleroomn += saleroomn2;
	}

	@Override
	public String toString() {
		return "SaleBean [dlm=" + dlm + ", dlmc=" + dlmc + ", tdMap=" + tdMap + ", saleroomn=" + saleroomn + "]";
	}

}
