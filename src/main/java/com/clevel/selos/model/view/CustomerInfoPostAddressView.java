package com.clevel.selos.model.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.faces.model.SelectItem;

public class CustomerInfoPostAddressView implements Serializable {
	private static final long serialVersionUID = 6453045708735225439L;
	private long id;
	private int addressType;
	private String displayAddressType;
	
	private String addressNo;
	private String moo;
	private String building;
	private String road;
	private int provinceId;
	private int districtId;
	private int subDistrictId;
	private String postalCode;
	private int countryId;
	private String phoneNumber;
	private String phoneExt;
	
	private String displayProvince;
	private String displayDistrict;
	private String displaySubDistrict;
	private String displayCountry;
	
	private int index;
	private int addressFlag;
	
	//For using in screen
	private List<SelectItem> districtList;
	private List<SelectItem> subDistrictList;
	public CustomerInfoPostAddressView() {
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getAddressType() {
		return addressType;
	}

	public void setAddressType(int addressType) {
		this.addressType = addressType;
	}

	public String getDisplayAddressType() {
		return displayAddressType;
	}

	public void setDisplayAddressType(String displayAddressType) {
		this.displayAddressType = displayAddressType;
	}

	public String getAddressNo() {
		return addressNo;
	}

	public void setAddressNo(String addressNo) {
		this.addressNo = addressNo;
	}

	public String getMoo() {
		return moo;
	}

	public void setMoo(String moo) {
		this.moo = moo;
	}

	public String getBuilding() {
		return building;
	}

	public void setBuilding(String building) {
		this.building = building;
	}

	public String getRoad() {
		return road;
	}

	public void setRoad(String road) {
		this.road = road;
	}

	public int getProvinceId() {
		return provinceId;
	}

	public void setProvinceId(int provinceId) {
		this.provinceId = provinceId;
	}

	public int getDistrictId() {
		return districtId;
	}

	public void setDistrictId(int districtId) {
		this.districtId = districtId;
	}

	public int getSubDistrictId() {
		return subDistrictId;
	}

	public void setSubDistrictId(int subDistrictId) {
		this.subDistrictId = subDistrictId;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public int getCountryId() {
		return countryId;
	}

	public void setCountryId(int countryId) {
		this.countryId = countryId;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getPhoneExt() {
		return phoneExt;
	}

	public void setPhoneExt(String phoneExt) {
		this.phoneExt = phoneExt;
	}
	
	
	public String getDisplayProvince() {
		return displayProvince;
	}

	public void setDisplayProvince(String displayProvince) {
		this.displayProvince = displayProvince;
	}

	public String getDisplayDistrict() {
		return displayDistrict;
	}

	public void setDisplayDistrict(String displayDistrict) {
		this.displayDistrict = displayDistrict;
	}

	public String getDisplaySubDistrict() {
		return displaySubDistrict;
	}

	public void setDisplaySubDistrict(String displaySubDistrict) {
		this.displaySubDistrict = displaySubDistrict;
	}

	public String getDisplayCountry() {
		return displayCountry;
	}

	public void setDisplayCountry(String displayCountry) {
		this.displayCountry = displayCountry;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public int getAddressFlag() {
		return addressFlag;
	}

	public void setAddressFlag(int addressFlag) {
		this.addressFlag = addressFlag;
	}
	public List<SelectItem> getDistrictList() {
		return districtList;
	}
	public void setDistrictList(List<SelectItem> districtList) {
		this.districtList = districtList;
	}
	public List<SelectItem> getSubDistrictList() {
		return subDistrictList;
	}
	public void setSubDistrictList(List<SelectItem> subDistrictList) {
		this.subDistrictList = subDistrictList;
	}
	public boolean canUpdate() {
		return index == 0 || addressFlag == 3;
	}
	public List<Integer> getAvailableFlagList() {
		if (index == 0)
			return Collections.emptyList();
		List<Integer> rtnDatas = new ArrayList<Integer>();
		for (int i=0;i<index;i++)
			rtnDatas.add(i);
		rtnDatas.add(3);
		return rtnDatas;
	}
	public void updateValue(CustomerInfoPostAddressView view) {
		this.id = view.id;
		this.index = view.index;
		this.addressFlag = view.addressFlag;
		this.addressType = view.addressType;
		this.displayAddressType = view.displayAddressType;
		duplicateData(view);
	}
	
	public void duplicateData(CustomerInfoPostAddressView view) {
		this.addressNo = view.addressNo;
		this.moo = view.moo;
		this.building = view.building;
		this.road = view.road;
		this.provinceId = view.provinceId;
		this.districtId = view.districtId;
		this.subDistrictId = view.subDistrictId;
		this.postalCode = view.postalCode;
		this.countryId = view.countryId;
		this.phoneNumber = view.phoneNumber;
		this.phoneExt = view.phoneExt;
		
		this.displayProvince = view.displayProvince;
		this.displayDistrict = view.displayDistrict;
		this.displaySubDistrict = view.displaySubDistrict;
		this.displayCountry = view.displayCountry;
	}
}
