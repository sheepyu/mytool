package com.yy.statement.domain;

public class Remain {
	private String dlm;// 代理名
	private String dlmc;// 代理名称
	private double zjye;// 资金余额
	private double expendDay;// 昨日花费
	private double expendWeek;// 一周花费
	private int canUse;// 可使用天数

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

	public double getZjye() {
		return zjye;
	}

	public void setZjye(double zjye) {
		this.zjye = zjye;
	}

	public double getExpendDay() {
		return expendDay;
	}

	public void setExpendDay(double expendDay) {
		this.expendDay = expendDay;
	}

	public double getExpendWeek() {
		return expendWeek;
	}

	public void setExpendWeek(double expendWeek) {
		this.expendWeek = expendWeek;
	}

	public int getCanUse() {
		return canUse;
	}

	public void setCanUse(int canUse) {
		this.canUse = canUse;
	}

	@Override
	public String toString() {
		return "Remain [dlm=" + dlm + ", dlmc=" + dlmc + ", zjye=" + zjye
				+ ", expendDay=" + expendDay + ", expendWeek=" + expendWeek
				+ ", canUse=" + canUse + "]";
	}
}
