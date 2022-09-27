package com.pcc.reservation.db;

import java.sql.Date;

public class ReservationDTO {
	private int res_num;
	private int mem_num;
	private String name;
	private Date res_date;
	private int res_time;
	private int res_persons;
	private int res_table;
	
	
	public int getRes_num() {
		return res_num;
	}
	public void setRes_num(int res_num) {
		this.res_num = res_num;
	}
	public int getMem_num() {
		return mem_num;
	}
	public void setMem_num(int mem_num) {
		this.mem_num = mem_num;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Date getRes_date() {
		return res_date;
	}
	public void setRes_date(Date res_date) {
		this.res_date = res_date;
	}
	public int getRes_time() {
		return res_time;
	}
	public void setRes_time(int res_time) {
		this.res_time = res_time;
	}
	public int getRes_persons() {
		return res_persons;
	}
	public void setRes_persons(int res_persons) {
		this.res_persons = res_persons;
	}
	public int getRes_table() {
		return res_table;
	}
	public void setRes_table(int res_table) {
		this.res_table = res_table;
	}
	
	
	
	
}
