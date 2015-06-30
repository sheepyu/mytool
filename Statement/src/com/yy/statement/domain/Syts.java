package com.yy.statement.domain;

public class Syts {

	private String tdbh;// 通道编号
	private int sumSyts;// 上游总条数

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

	public int getSumSyts() {
		return sumSyts;
	}

	public void setSumSyts(int sumSyts) {
		this.sumSyts = sumSyts;
	}

	

}
