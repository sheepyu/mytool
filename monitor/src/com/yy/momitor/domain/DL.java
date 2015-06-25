package com.yy.momitor.domain;

/**
 * @author Administrator
 *
 */
/**
 * @author Administrator
 * 
 */
public class DL {
	private int dlid;
	private float zjye;
	private String dlm;

	public DL() {
		super();
	}

	public DL(int dlid, float zjye, String dlm) {
		super();
		this.dlid = dlid;
		this.zjye = zjye;
		this.dlm = dlm;
	}

	public int getDlid() {
		return dlid;
	}

	public void setDlid(int dlid) {
		this.dlid = dlid;
	}

	public float getZjye() {
		return zjye;
	}

	public void setZjye(float zjye) {
		this.zjye = zjye;
	}

	public String getDlm() {
		return dlm;
	}

	public void setDlm(String dlm) {
		this.dlm = dlm;
	}

	@Override
	public String toString() {
		return "DL [dlid=" + dlid + ", zjye=" + zjye + ", dlm=" + dlm + "]";
	}

}
