package com.yy.statement.domain;


public class Sale {

	private String dlm;
	private String dlmc;
	private int tdbh;
	private int ts;
	private double saleroomn;

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

	public int getTdbh() {
		return tdbh;
	}

	public void setTdbh(int tdbh) {
		this.tdbh = tdbh;
	}

	public int getTs() {
		return ts;
	}

	public void setTs(int ts) {
		this.ts = ts;
	}

	public double getSaleroomn() {
		return saleroomn;
	}

	public void setSaleroomn(double saleroomn) {
		this.saleroomn = saleroomn;
	}

	@Override
	public String toString() {
		return "Sale [dlm=" + dlm + ", dlmc=" + dlmc + ", tdbh=" + tdbh
				+ ", ts=" + ts + ", saleroomn=" + saleroomn + "]";
	}

}
