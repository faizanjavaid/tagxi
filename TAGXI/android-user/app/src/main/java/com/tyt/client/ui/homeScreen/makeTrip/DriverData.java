package com.tyt.client.ui.homeScreen.makeTrip;

import com.google.gson.annotations.SerializedName;

public class DriverData{

	@SerializedName("is_active")
	private int isActive;

	@SerializedName("vehicle_type_name")
	private String vehicleTypeName;

	@SerializedName("bearing")
	private double bearing;

	@SerializedName("g")
	private String G;

	@SerializedName("mobile")
	private String mobile;

	@SerializedName("active")
	private int active;

	@SerializedName("vehicle_type")
	private String vehicleType;

	@SerializedName("company_key")
	private String companyKey;

	@SerializedName("l")
	private String L;

	@SerializedName("is_available")
	private boolean isAvailable;

	@SerializedName("updated_at")
	private long updatedAt;

	@SerializedName("vehicle_number")
	private String vehicleNumber;

	@SerializedName("demo_key")
	private String demoKey;

	@SerializedName("name")
	private String name;

	@SerializedName("id")
	private int id;

	@SerializedName("first_name")
	private String firstName;

	public void setIsActive(int isActive){
		this.isActive = isActive;
	}

	public int getIsActive(){
		return isActive;
	}

	public void setVehicleTypeName(String vehicleTypeName){
		this.vehicleTypeName = vehicleTypeName;
	}

	public String getVehicleTypeName(){
		return vehicleTypeName;
	}

	public void setBearing(double bearing){
		this.bearing = bearing;
	}

	public double getBearing(){
		return bearing;
	}

	public void setG(String G){
		this.G = G;
	}

	public String getG(){
		return G;
	}

	public void setMobile(String mobile){
		this.mobile = mobile;
	}

	public String getMobile(){
		return mobile;
	}

	public void setActive(int active){
		this.active = active;
	}

	public int getActive(){
		return active;
	}

	public void setVehicleType(String vehicleType){
		this.vehicleType = vehicleType;
	}

	public String getVehicleType(){
		return vehicleType;
	}

	public void setCompanyKey(String companyKey){
		this.companyKey = companyKey;
	}

	public String getCompanyKey(){
		return companyKey;
	}

	public void setL(String L){
		this.L = L;
	}

	public String getL(){
		return L;
	}

	public void setIsAvailable(boolean isAvailable){
		this.isAvailable = isAvailable;
	}

	public boolean isIsAvailable(){
		return isAvailable;
	}

	public void setUpdatedAt(long updatedAt){
		this.updatedAt = updatedAt;
	}

	public long getUpdatedAt(){
		return updatedAt;
	}

	public void setVehicleNumber(String vehicleNumber){
		this.vehicleNumber = vehicleNumber;
	}

	public String getVehicleNumber(){
		return vehicleNumber;
	}

	public void setDemoKey(String demoKey){
		this.demoKey = demoKey;
	}

	public String getDemoKey(){
		return demoKey;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setFirstName(String firstName){
		this.firstName = firstName;
	}

	public String getFirstName(){
		return firstName;
	}
}