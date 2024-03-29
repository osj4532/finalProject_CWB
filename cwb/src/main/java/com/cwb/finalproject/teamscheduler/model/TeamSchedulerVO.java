package com.cwb.finalproject.teamscheduler.model;


public class TeamSchedulerVO {
	private int tscdNo;
	private int memNo;
	private int deptNo;
	private String tscdStartdate;
	private String tscdEnddate;
	private String tscdTitle;
	private String tscdContent;
	
	public String getTscdTitle() {
		return tscdTitle;
	}
	public void setTscdTitle(String tscdTitle) {
		this.tscdTitle = tscdTitle;
	}
	private boolean isday;
	
	public boolean isIsday() {
		return isday;
	}
	public void setIsday(boolean isday) {
		this.isday = isday;
	}
	public int getTscdNo() {
		return tscdNo;
	}
	public void setTscdNo(int tscdNo) {
		this.tscdNo = tscdNo;
	}
	public int getMemNo() {
		return memNo;
	}
	public void setMemNo(int memNo) {
		this.memNo = memNo;
	}
	public int getDeptNo() {
		return deptNo;
	}
	public void setDeptNo(int deptNo) {
		this.deptNo = deptNo;
	}
	public String getTscdStartdate() {
		return tscdStartdate;
	}
	public void setTscdStartdate(String tscdStartdate) {
		this.tscdStartdate = tscdStartdate;
	}
	public String getTscdEnddate() {
		return tscdEnddate;
	}
	public void setTscdEnddate(String tscdEnddate) {
		this.tscdEnddate = tscdEnddate;
	}
	public String getTscdContent() {
		return tscdContent;
	}
	public void setTscdContent(String tscdContent) {
		this.tscdContent = tscdContent;
	}
	@Override
	public String toString() {
		return "TeamSchedulerVO [tscdNo=" + tscdNo + ", memNo=" + memNo + ", deptNo=" + deptNo + ", tscdStartdate="
				+ tscdStartdate + ", tscdEnddate=" + tscdEnddate + ", tscdTitle=" + tscdTitle + ", tscdContent="
				+ tscdContent + ", isday=" + isday + "]";
	}
 	
}
