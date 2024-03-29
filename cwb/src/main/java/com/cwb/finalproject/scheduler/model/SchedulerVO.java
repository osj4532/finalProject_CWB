package com.cwb.finalproject.scheduler.model;

public class SchedulerVO {
	private int scdNo; /* 스케줄번호 */
	private int memNo;/* 사원번호 */
	private String scdStart; /* 시작날짜 */
	private String scdEnd; /* 끝날짜 */
	private String scdTitle; /* 스케줄내용 */
	private String scdContent; /* 스케줄내용 */
	
	private boolean isday;
	
	
	public String getScdTitle() {
		return scdTitle;
	}

	public void setScdTitle(String scdTitle) {
		this.scdTitle = scdTitle;
	}


	public int getScdNo() {
		return scdNo;
	}

	public void setScdNo(int scdNo) {
		this.scdNo = scdNo;
	}

	public int getMemNo() {
		return memNo;
	}

	public void setMemNo(int memNo) {
		this.memNo = memNo;
	}

	public String getScdStart() {
		return scdStart;
	}

	public void setScdStart(String scdStart) {
		this.scdStart = scdStart;
	}

	public String getScdEnd() {
		return scdEnd;
	}

	public void setScdEnd(String scdEnd) {
		this.scdEnd = scdEnd;
	}

	public String getScdContent() {
		return scdContent;
	}

	public void setScdContent(String scdContent) {
		this.scdContent = scdContent;
	}

	public boolean isIsday() {
		return isday;
	}

	public void setIsday(boolean isday) {
		this.isday = isday;
	}

	@Override
	public String toString() {
		return "SchedulerVO [scdNo=" + scdNo + ", memNo=" + memNo + ", scdStart=" + scdStart + ", scdEnd=" + scdEnd
				+ ", scdTitle=" + scdTitle + ", scdContent=" + scdContent + ", isday=" + isday + "]";
	}


	
	
	
}
