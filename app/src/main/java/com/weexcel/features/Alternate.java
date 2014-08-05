package com.weexcel.features;

import java.util.HashMap;
import java.util.List;

import com.google.android.gms.maps.model.Marker;

public class Alternate {
	private HashMap<Integer, List<String>> revi;
	public Alternate( String type, String name, Marker marker,String vicinity,String pricelevel,String rating,String phone,String website,String status,HashMap<Integer, List<String>> review) {
		super();

		this.type = type;
		this.name = name;
		this.marker = marker;
		this.vicinity=vicinity;
		this.pricelevel=pricelevel;
		this.rating=rating;
		this.website=website;
		this.phoneno=phone;
		this.status=status;
		this.revi=review;
	}

	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Marker getMarker() {
		return marker;
	}
	public void setMarker(Marker marker) {
		this.marker = marker;
	}

	String type;


	String name;
	Marker marker;
	String vicinity;
	String rating;
	String website;
	String phoneno="";
	String status="";
	public String getWebsite() {
		return website;
	}
	public String getStatus() {
		return status;
	}
	public void setWebsite(String website) {
		this.website = website;
	}
	public String getPhoneno() {
		return phoneno;
	}
	public void setPhoneno(String phoneno) {
		this.phoneno = phoneno;
	}
	public String getRating() {
		return rating;
	}
	public void setRating(String rating) {
		this.rating = rating;
	}
	public String getPricelevel() {
		return pricelevel;
	}
	public void setPricelevel(String pricelevel) {
		this.pricelevel = pricelevel;
	}
	String pricelevel;

	public String getVicinity() {
		return vicinity;
	}
	public void setVicinity(String vicinity) {
		this.vicinity = vicinity;
	}
	public HashMap<Integer, List<String>> getReviews() {
		return revi;
	}
	public void setReviews(HashMap<Integer, List<String>> reviews) {
		this.revi = reviews;
	}
}
