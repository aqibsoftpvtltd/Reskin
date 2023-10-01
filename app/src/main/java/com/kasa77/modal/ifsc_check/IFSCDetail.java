package com.kasa77.modal.ifsc_check;


import com.google.gson.annotations.SerializedName;


public class IFSCDetail{

	@SerializedName("BANK")
	public String bank;

	@SerializedName("ADDRESS")
	public String address;

	@SerializedName("CITY")
	public String city;

	@SerializedName("CONTACT")
	public String contact;

	@SerializedName("DISTRICT")
	public String district;

	@SerializedName("MICR")
	public String micr;

	@SerializedName("STATE")
	public String state;

	@SerializedName("IFSC")
	public String ifsc;

	@SerializedName("error")
	public String error;

	@SerializedName("BRANCH")
	public String branch;

	public String getBank() {
		return bank;
	}

	public String getAddress() {
		return address;
	}

	public String getCity() {
		return city;
	}

	public String getContact() {
		return contact;
	}

	public String getDistrict() {
		return district;
	}

	public String getMicr() {
		return micr;
	}

	public String getState() {
		return state;
	}

	public String getIfsc() {
		return ifsc;
	}

	public String getError() {
		return error;
	}

	public String getBranch() {
		return branch;
	}
}