package com.yy.momitor.domain;

public class TD {

	private String tdbh;// 通道编号
	private int ts;// 条数

	public String getTdbh() {
		return tdbh;
	}

	public void setTdbh(String tdbh) {
		this.tdbh = tdbh;
	}

	public void setTs(int ts) {
		this.ts = ts;
	}

	public int getTs() {
		return ts;
	}

	@Override
	public String toString() {
		return "TD [tdbh=" + tdbh + ", ts=" + ts + "]";
	}

	
}
