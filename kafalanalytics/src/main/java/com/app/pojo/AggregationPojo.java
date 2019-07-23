package com.app.pojo;

import java.util.List;

import com.app.bo.EmployeeRoleBO;

public class AggregationPojo {
	
	private String name;
	
	private String employeeIdFK;
	
	private String teamName;
	
	private double angerTotal;
	
	private double angerCount;
	
	private double joyTotal;
	
	private double joyCount;
	
	private double sadnessTotal;
	
	private double sadnessCount;
	
	private double tentativeTotal;
	
	private double tentativeCount;
	
	private double analyticalTotal;
	
	private double analyticalCount;
	
	private double confidentTotal;
	
	private double confidentCount;
	
	private double fearTotal;
	
	private double fearCount;
	
	//rcv
	private double angerTotalrcv;
	
	private double angerCountrcv;
	
	private double joyTotalrcv;
	
	private double joyCountrcv;
	
	private double sadnessTotalrcv;
	
	private double sadnessCountrcv;
	
	private double tentativeTotalrcv;
	
	private double tentativeCountrcv;
	
	private double analyticalTotalrcv;
	
	private double analyticalCountrcv;
	
	private double confidentTotalrcv;
	
	private double confidentCountrcv;
	
	private double fearTotalrcv;
	
	private double fearCountrcv;
	
	//snt
	
	private double angerTotalsnt;
	
	private double angerCountsnt;
	
	private double joyTotalsnt;
	
	private double joyCountsnt;
	
	private double sadnessTotalsnt;
	
	private double sadnessCountsnt;
	
	private double tentativeTotalsnt;
	
	private double tentativeCountsnt;
	
	private double analyticalTotalsnt;
	
	private double analyticalCountsnt;
	
	private double confidentTotalsnt;
	
	private double confidentCountsnt;
	
	private double fearTotalsnt;
	
	private double fearCountsnt;
	
	//team final data
	private double angerTeamTotal;
	
	private double angerTeamCount;
	
	private double joyTeamTotal;
	
	private double joyTeamCount;
	
	private double sadnessTeamTotal;
	
	private double sadnessTeamCount;
	
	private double tentativeTeamTotal;
	
	private double tentativeTeamCount;
	
	private double analyticalTeamTotal;
	
	private double analyticalTeamCount;
	
	private double confidentTeamTotal;
	
	private double confidentTeamCount;
	
	private double fearTeamTotal;
	
	private double fearTeamCount;
	
	//rcv
	private double angerTeamTotalrcv;
	
	private double angerTeamCountrcv;
	
	private double joyTeamTotalrcv;
	
	private double joyTeamCountrcv;
	
	private double sadnessTeamTotalrcv;
	
	private double sadnessTeamCountrcv;
	
	private double tentativeTeamTotalrcv;
	
	private double tentativeTeamCountrcv;
	
	private double analyticalTeamTotalrcv;
	
	private double analyticalTeamCountrcv;
	
	private double confidentTeamTotalrcv;
	
	private double confidentTeamCountrcv;
	
	private double fearTeamTotalrcv;
	
	private double fearTeamCountrcv;
	
	//snt
	private double angerTeamTotalsnt;
	
	private double angerTeamCountsnt;
	
	private double joyTeamTotalsnt;
	
	private double joyTeamCountsnt;
	
	private double sadnessTeamTotalsnt;
	
	private double sadnessTeamCountsnt;
	
	private double tentativeTeamTotalsnt;
	
	private double tentativeTeamCountsnt;
	
	private double analyticalTeamTotalsnt;
	
	private double analyticalTeamCountsnt;
	
	private double confidentTeamTotalsnt;
	
	private double confidentTeamCountsnt;
	
	private double fearTeamTotalsnt;
	
	private double fearTeamCountsnt;
	
	private String employeeId;
	
	// total mail count
	
	private Long totalMailRecevied;
	
	private Long totalMailSent;
	
	private Long totalMail;
	
	private Long totalTeamMailRecevied;
	
	private Long totalTeamMailSent;
	
	private Long totalTeamMail;
	
	private List<EmployeeRoleBO> employeeRoleBOs;
	
	
	
	
	
	
	public String getTeamName() {
		return teamName;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<EmployeeRoleBO> getEmployeeRoleBOs() {
		return employeeRoleBOs;
	}

	public void setEmployeeRoleBOs(List<EmployeeRoleBO> employeeRoleBOs) {
		this.employeeRoleBOs = employeeRoleBOs;
	}

	public double getJoyTotal() {
		return joyTotal;
	}

	public void setJoyTotal(double joyTotal) {
		this.joyTotal = joyTotal;
	}

	public double getJoyCount() {
		return joyCount;
	}

	public void setJoyCount(double joyCount) {
		this.joyCount = joyCount;
	}

	public double getSadnessTotal() {
		return sadnessTotal;
	}

	public void setSadnessTotal(double sadnessTotal) {
		this.sadnessTotal = sadnessTotal;
	}

	public double getSadnessCount() {
		return sadnessCount;
	}

	public void setSadnessCount(double sadnessCount) {
		this.sadnessCount = sadnessCount;
	}

	public double getTentativeTotal() {
		return tentativeTotal;
	}

	public void setTentativeTotal(double tentativeTotal) {
		this.tentativeTotal = tentativeTotal;
	}

	public double getTentativeCount() {
		return tentativeCount;
	}

	public void setTentativeCount(double tentativeCount) {
		this.tentativeCount = tentativeCount;
	}

	public double getAnalyticalTotal() {
		return analyticalTotal;
	}

	public void setAnalyticalTotal(double analyticalTotal) {
		this.analyticalTotal = analyticalTotal;
	}

	public double getAnalyticalCount() {
		return analyticalCount;
	}

	public void setAnalyticalCount(double analyticalCount) {
		this.analyticalCount = analyticalCount;
	}

	public double getConfidentTotal() {
		return confidentTotal;
	}

	public void setConfidentTotal(double confidentTotal) {
		this.confidentTotal = confidentTotal;
	}

	public double getConfidentCount() {
		return confidentCount;
	}

	public void setConfidentCount(double confidentCount) {
		this.confidentCount = confidentCount;
	}

	public double getFearTotal() {
		return fearTotal;
	}

	public void setFearTotal(double fearTotal) {
		this.fearTotal = fearTotal;
	}

	public double getFearCount() {
		return fearCount;
	}

	public void setFearCount(double fearCount) {
		this.fearCount = fearCount;
	}

	public double getAngerTeamTotal() {
		return angerTeamTotal;
	}

	public void setAngerTeamTotal(double angerTeamTotal) {
		this.angerTeamTotal = angerTeamTotal;
	}

	public double getAngerTeamCount() {
		return angerTeamCount;
	}

	public void setAngerTeamCount(double angerTeamCount) {
		this.angerTeamCount = angerTeamCount;
	}

	public double getJoyTeamTotal() {
		return joyTeamTotal;
	}

	public void setJoyTeamTotal(double joyTeamTotal) {
		this.joyTeamTotal = joyTeamTotal;
	}

	public double getJoyTeamCount() {
		return joyTeamCount;
	}

	public void setJoyTeamCount(double joyTeamCount) {
		this.joyTeamCount = joyTeamCount;
	}

	public double getSadnessTeamTotal() {
		return sadnessTeamTotal;
	}

	public void setSadnessTeamTotal(double sadnessTeamTotal) {
		this.sadnessTeamTotal = sadnessTeamTotal;
	}

	public double getSadnessTeamCount() {
		return sadnessTeamCount;
	}

	public void setSadnessTeamCount(double sadnessTeamCount) {
		this.sadnessTeamCount = sadnessTeamCount;
	}

	public double getTentativeTeamTotal() {
		return tentativeTeamTotal;
	}

	public void setTentativeTeamTotal(double tentativeTeamTotal) {
		this.tentativeTeamTotal = tentativeTeamTotal;
	}

	public double getTentativeTeamCount() {
		return tentativeTeamCount;
	}

	public void setTentativeTeamCount(double tentativeTeamCount) {
		this.tentativeTeamCount = tentativeTeamCount;
	}

	public double getAnalyticalTeamTotal() {
		return analyticalTeamTotal;
	}

	public void setAnalyticalTeamTotal(double analyticalTeamTotal) {
		this.analyticalTeamTotal = analyticalTeamTotal;
	}

	public double getAnalyticalTeamCount() {
		return analyticalTeamCount;
	}

	public void setAnalyticalTeamCount(double analyticalTeamCount) {
		this.analyticalTeamCount = analyticalTeamCount;
	}

	public double getConfidentTeamTotal() {
		return confidentTeamTotal;
	}

	public void setConfidentTeamTotal(double confidentTeamTotal) {
		this.confidentTeamTotal = confidentTeamTotal;
	}

	public double getConfidentTeamCount() {
		return confidentTeamCount;
	}

	public void setConfidentTeamCount(double confidentTeamCount) {
		this.confidentTeamCount = confidentTeamCount;
	}

	public double getFearTeamTotal() {
		return fearTeamTotal;
	}

	public void setFearTeamTotal(double fearTeamTotal) {
		this.fearTeamTotal = fearTeamTotal;
	}

	public double getFearTeamCount() {
		return fearTeamCount;
	}

	public void setFearTeamCount(double fearTeamCount) {
		this.fearTeamCount = fearTeamCount;
	}

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	public String getEmployeeIdFK() {
		return employeeIdFK;
	}

	public void setEmployeeIdFK(String employeeIdFK) {
		this.employeeIdFK = employeeIdFK;
	}

	public double getAngerTotal() {
		return angerTotal;
	}

	public void setAngerTotal(double angerTotal) {
		this.angerTotal = angerTotal;
	}

	public double getAngerCount() {
		return angerCount;
	}

	public void setAngerCount(double angerCount) {
		this.angerCount = angerCount;
	}

	public double getAngerTotalrcv() {
		return angerTotalrcv;
	}

	public void setAngerTotalrcv(double angerTotalrcv) {
		this.angerTotalrcv = angerTotalrcv;
	}

	public double getAngerCountrcv() {
		return angerCountrcv;
	}

	public void setAngerCountrcv(double angerCountrcv) {
		this.angerCountrcv = angerCountrcv;
	}

	public double getJoyTotalrcv() {
		return joyTotalrcv;
	}

	public void setJoyTotalrcv(double joyTotalrcv) {
		this.joyTotalrcv = joyTotalrcv;
	}

	public double getJoyCountrcv() {
		return joyCountrcv;
	}

	public void setJoyCountrcv(double joyCountrcv) {
		this.joyCountrcv = joyCountrcv;
	}

	public double getSadnessTotalrcv() {
		return sadnessTotalrcv;
	}

	public void setSadnessTotalrcv(double sadnessTotalrcv) {
		this.sadnessTotalrcv = sadnessTotalrcv;
	}

	public double getSadnessCountrcv() {
		return sadnessCountrcv;
	}

	public void setSadnessCountrcv(double sadnessCountrcv) {
		this.sadnessCountrcv = sadnessCountrcv;
	}

	public double getTentativeTotalrcv() {
		return tentativeTotalrcv;
	}

	public void setTentativeTotalrcv(double tentativeTotalrcv) {
		this.tentativeTotalrcv = tentativeTotalrcv;
	}

	public double getTentativeCountrcv() {
		return tentativeCountrcv;
	}

	public void setTentativeCountrcv(double tentativeCountrcv) {
		this.tentativeCountrcv = tentativeCountrcv;
	}

	public double getAnalyticalTotalrcv() {
		return analyticalTotalrcv;
	}

	public void setAnalyticalTotalrcv(double analyticalTotalrcv) {
		this.analyticalTotalrcv = analyticalTotalrcv;
	}

	public double getAnalyticalCountrcv() {
		return analyticalCountrcv;
	}

	public void setAnalyticalCountrcv(double analyticalCountrcv) {
		this.analyticalCountrcv = analyticalCountrcv;
	}

	public double getConfidentTotalrcv() {
		return confidentTotalrcv;
	}

	public void setConfidentTotalrcv(double confidentTotalrcv) {
		this.confidentTotalrcv = confidentTotalrcv;
	}

	public double getConfidentCountrcv() {
		return confidentCountrcv;
	}

	public void setConfidentCountrcv(double confidentCountrcv) {
		this.confidentCountrcv = confidentCountrcv;
	}

	public double getFearTotalrcv() {
		return fearTotalrcv;
	}

	public void setFearTotalrcv(double fearTotalrcv) {
		this.fearTotalrcv = fearTotalrcv;
	}

	public double getFearCountrcv() {
		return fearCountrcv;
	}

	public void setFearCountrcv(double fearCountrcv) {
		this.fearCountrcv = fearCountrcv;
	}

	public double getAngerTotalsnt() {
		return angerTotalsnt;
	}

	public void setAngerTotalsnt(double angerTotalsnt) {
		this.angerTotalsnt = angerTotalsnt;
	}

	public double getAngerCountsnt() {
		return angerCountsnt;
	}

	public void setAngerCountsnt(double angerCountsnt) {
		this.angerCountsnt = angerCountsnt;
	}

	public double getJoyTotalsnt() {
		return joyTotalsnt;
	}

	public void setJoyTotalsnt(double joyTotalsnt) {
		this.joyTotalsnt = joyTotalsnt;
	}

	public double getJoyCountsnt() {
		return joyCountsnt;
	}

	public void setJoyCountsnt(double joyCountsnt) {
		this.joyCountsnt = joyCountsnt;
	}

	public double getSadnessTotalsnt() {
		return sadnessTotalsnt;
	}

	public void setSadnessTotalsnt(double sadnessTotalsnt) {
		this.sadnessTotalsnt = sadnessTotalsnt;
	}

	public double getSadnessCountsnt() {
		return sadnessCountsnt;
	}

	public void setSadnessCountsnt(double sadnessCountsnt) {
		this.sadnessCountsnt = sadnessCountsnt;
	}

	public double getTentativeTotalsnt() {
		return tentativeTotalsnt;
	}

	public void setTentativeTotalsnt(double tentativeTotalsnt) {
		this.tentativeTotalsnt = tentativeTotalsnt;
	}

	public double getTentativeCountsnt() {
		return tentativeCountsnt;
	}

	public void setTentativeCountsnt(double tentativeCountsnt) {
		this.tentativeCountsnt = tentativeCountsnt;
	}

	public double getAnalyticalTotalsnt() {
		return analyticalTotalsnt;
	}

	public void setAnalyticalTotalsnt(double analyticalTotalsnt) {
		this.analyticalTotalsnt = analyticalTotalsnt;
	}

	public double getAnalyticalCountsnt() {
		return analyticalCountsnt;
	}

	public void setAnalyticalCountsnt(double analyticalCountsnt) {
		this.analyticalCountsnt = analyticalCountsnt;
	}

	public double getConfidentTotalsnt() {
		return confidentTotalsnt;
	}

	public void setConfidentTotalsnt(double confidentTotalsnt) {
		this.confidentTotalsnt = confidentTotalsnt;
	}

	public double getConfidentCountsnt() {
		return confidentCountsnt;
	}

	public void setConfidentCountsnt(double confidentCountsnt) {
		this.confidentCountsnt = confidentCountsnt;
	}

	public double getFearTotalsnt() {
		return fearTotalsnt;
	}

	public void setFearTotalsnt(double fearTotalsnt) {
		this.fearTotalsnt = fearTotalsnt;
	}

	public double getFearCountsnt() {
		return fearCountsnt;
	}

	public void setFearCountsnt(double fearCountsnt) {
		this.fearCountsnt = fearCountsnt;
	}

	public double getAngerTeamTotalrcv() {
		return angerTeamTotalrcv;
	}

	public void setAngerTeamTotalrcv(double angerTeamTotalrcv) {
		this.angerTeamTotalrcv = angerTeamTotalrcv;
	}

	public double getAngerTeamCountrcv() {
		return angerTeamCountrcv;
	}

	public void setAngerTeamCountrcv(double angerTeamCountrcv) {
		this.angerTeamCountrcv = angerTeamCountrcv;
	}

	public double getJoyTeamTotalrcv() {
		return joyTeamTotalrcv;
	}

	public void setJoyTeamTotalrcv(double joyTeamTotalrcv) {
		this.joyTeamTotalrcv = joyTeamTotalrcv;
	}

	public double getJoyTeamCountrcv() {
		return joyTeamCountrcv;
	}

	public void setJoyTeamCountrcv(double joyTeamCountrcv) {
		this.joyTeamCountrcv = joyTeamCountrcv;
	}

	public double getSadnessTeamTotalrcv() {
		return sadnessTeamTotalrcv;
	}

	public void setSadnessTeamTotalrcv(double sadnessTeamTotalrcv) {
		this.sadnessTeamTotalrcv = sadnessTeamTotalrcv;
	}

	public double getSadnessTeamCountrcv() {
		return sadnessTeamCountrcv;
	}

	public void setSadnessTeamCountrcv(double sadnessTeamCountrcv) {
		this.sadnessTeamCountrcv = sadnessTeamCountrcv;
	}

	public double getTentativeTeamTotalrcv() {
		return tentativeTeamTotalrcv;
	}

	public void setTentativeTeamTotalrcv(double tentativeTeamTotalrcv) {
		this.tentativeTeamTotalrcv = tentativeTeamTotalrcv;
	}

	public double getTentativeTeamCountrcv() {
		return tentativeTeamCountrcv;
	}

	public void setTentativeTeamCountrcv(double tentativeTeamCountrcv) {
		this.tentativeTeamCountrcv = tentativeTeamCountrcv;
	}

	public double getAnalyticalTeamTotalrcv() {
		return analyticalTeamTotalrcv;
	}

	public void setAnalyticalTeamTotalrcv(double analyticalTeamTotalrcv) {
		this.analyticalTeamTotalrcv = analyticalTeamTotalrcv;
	}

	public double getAnalyticalTeamCountrcv() {
		return analyticalTeamCountrcv;
	}

	public void setAnalyticalTeamCountrcv(double analyticalTeamCountrcv) {
		this.analyticalTeamCountrcv = analyticalTeamCountrcv;
	}

	public double getConfidentTeamTotalrcv() {
		return confidentTeamTotalrcv;
	}

	public void setConfidentTeamTotalrcv(double confidentTeamTotalrcv) {
		this.confidentTeamTotalrcv = confidentTeamTotalrcv;
	}

	public double getConfidentTeamCountrcv() {
		return confidentTeamCountrcv;
	}

	public void setConfidentTeamCountrcv(double confidentTeamCountrcv) {
		this.confidentTeamCountrcv = confidentTeamCountrcv;
	}

	public double getFearTeamTotalrcv() {
		return fearTeamTotalrcv;
	}

	public void setFearTeamTotalrcv(double fearTeamTotalrcv) {
		this.fearTeamTotalrcv = fearTeamTotalrcv;
	}

	public double getFearTeamCountrcv() {
		return fearTeamCountrcv;
	}

	public void setFearTeamCountrcv(double fearTeamCountrcv) {
		this.fearTeamCountrcv = fearTeamCountrcv;
	}

	public double getAngerTeamTotalsnt() {
		return angerTeamTotalsnt;
	}

	public void setAngerTeamTotalsnt(double angerTeamTotalsnt) {
		this.angerTeamTotalsnt = angerTeamTotalsnt;
	}

	public double getAngerTeamCountsnt() {
		return angerTeamCountsnt;
	}

	public void setAngerTeamCountsnt(double angerTeamCountsnt) {
		this.angerTeamCountsnt = angerTeamCountsnt;
	}

	public double getJoyTeamTotalsnt() {
		return joyTeamTotalsnt;
	}

	public void setJoyTeamTotalsnt(double joyTeamTotalsnt) {
		this.joyTeamTotalsnt = joyTeamTotalsnt;
	}

	public double getJoyTeamCountsnt() {
		return joyTeamCountsnt;
	}

	public void setJoyTeamCountsnt(double joyTeamCountsnt) {
		this.joyTeamCountsnt = joyTeamCountsnt;
	}

	public double getSadnessTeamTotalsnt() {
		return sadnessTeamTotalsnt;
	}

	public void setSadnessTeamTotalsnt(double sadnessTeamTotalsnt) {
		this.sadnessTeamTotalsnt = sadnessTeamTotalsnt;
	}

	public double getSadnessTeamCountsnt() {
		return sadnessTeamCountsnt;
	}

	public void setSadnessTeamCountsnt(double sadnessTeamCountsnt) {
		this.sadnessTeamCountsnt = sadnessTeamCountsnt;
	}

	public double getTentativeTeamTotalsnt() {
		return tentativeTeamTotalsnt;
	}

	public void setTentativeTeamTotalsnt(double tentativeTeamTotalsnt) {
		this.tentativeTeamTotalsnt = tentativeTeamTotalsnt;
	}

	public double getTentativeTeamCountsnt() {
		return tentativeTeamCountsnt;
	}

	public void setTentativeTeamCountsnt(double tentativeTeamCountsnt) {
		this.tentativeTeamCountsnt = tentativeTeamCountsnt;
	}

	public double getAnalyticalTeamTotalsnt() {
		return analyticalTeamTotalsnt;
	}

	public void setAnalyticalTeamTotalsnt(double analyticalTeamTotalsnt) {
		this.analyticalTeamTotalsnt = analyticalTeamTotalsnt;
	}

	public double getAnalyticalTeamCountsnt() {
		return analyticalTeamCountsnt;
	}

	public void setAnalyticalTeamCountsnt(double analyticalTeamCountsnt) {
		this.analyticalTeamCountsnt = analyticalTeamCountsnt;
	}

	public double getConfidentTeamTotalsnt() {
		return confidentTeamTotalsnt;
	}

	public void setConfidentTeamTotalsnt(double confidentTeamTotalsnt) {
		this.confidentTeamTotalsnt = confidentTeamTotalsnt;
	}

	public double getConfidentTeamCountsnt() {
		return confidentTeamCountsnt;
	}

	public void setConfidentTeamCountsnt(double confidentTeamCountsnt) {
		this.confidentTeamCountsnt = confidentTeamCountsnt;
	}

	public double getFearTeamTotalsnt() {
		return fearTeamTotalsnt;
	}

	public void setFearTeamTotalsnt(double fearTeamTotalsnt) {
		this.fearTeamTotalsnt = fearTeamTotalsnt;
	}

	public double getFearTeamCountsnt() {
		return fearTeamCountsnt;
	}

	public void setFearTeamCountsnt(double fearTeamCountsnt) {
		this.fearTeamCountsnt = fearTeamCountsnt;
	}

	public Long getTotalMailRecevied() {
		return totalMailRecevied;
	}

	public void setTotalMailRecevied(Long totalMailRecevied) {
		this.totalMailRecevied = totalMailRecevied;
	}

	public Long getTotalMailSent() {
		return totalMailSent;
	}

	public void setTotalMailSent(Long totalMailSent) {
		this.totalMailSent = totalMailSent;
	}

	public Long getTotalMail() {
		return totalMail;
	}

	public void setTotalMail(Long totalMail) {
		this.totalMail = totalMail;
	}

	public Long getTotalTeamMailRecevied() {
		return totalTeamMailRecevied;
	}

	public void setTotalTeamMailRecevied(Long totalTeamMailRecevied) {
		this.totalTeamMailRecevied = totalTeamMailRecevied;
	}

	public Long getTotalTeamMailSent() {
		return totalTeamMailSent;
	}

	public void setTotalTeamMailSent(Long totalTeamMailSent) {
		this.totalTeamMailSent = totalTeamMailSent;
	}

	public Long getTotalTeamMail() {
		return totalTeamMail;
	}

	public void setTotalTeamMail(Long totalTeamMail) {
		this.totalTeamMail = totalTeamMail;
	}
	
	
	
	
	
	

}
