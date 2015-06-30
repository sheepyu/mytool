package com.yy.statement.domain;

public class Syts {

	private String tdbh;// 通道编号
	private String sumSyts;// 上游总条数

	@Override
	public String toString() {
		return "TD [tdbh=" + tdbh + ", sumSyts=" + sumSyts + "]";
	}

	public String getTdbh() {
		return tdbh;
	}

	public void setTdbh(String tdbh) {
		this.tdbh = tdbh;
	}

	public String getSumSyts() {
		return sumSyts;
	}

	public void setSumSyts(String sumSyts) {
		this.sumSyts = sumSyts;
	}

}
