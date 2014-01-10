package com.example.test;

import android.graphics.Bitmap;

public class RowItem {
	private Bitmap ImageURL;
	private String Title;
	private String Year;
	private int search;
	
	public RowItem(String title1, String year1, Bitmap img, int num){
		this.ImageURL = img;
		this.Title = title1;
		this.Year = year1;
		this.search = num;
	}
	
	public Bitmap getImageURL(){
		return this.ImageURL;
	}
	public void setImageURL(Bitmap img){
		this.ImageURL = img;
	}
	public String getTitle(){
		return this.Title;
	}
	public void SetTitle(String title){
		this.Title = title;
	}
	public String getYear(){
		return this.Year;
	}
	public void setYear(String year){
		this.Year = year;
	}
	public int getsearch(){
		return this.search;
	}
	public void setsearch(int num){
		this.search = num;
	}
	
}
