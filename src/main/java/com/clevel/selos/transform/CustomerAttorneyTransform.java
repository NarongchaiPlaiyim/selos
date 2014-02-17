package com.clevel.selos.transform;

import com.clevel.selos.model.Gender;
import com.clevel.selos.model.db.working.Address;
import com.clevel.selos.model.db.working.Customer;
import com.clevel.selos.model.db.working.CustomerAttorney;
import com.clevel.selos.model.db.working.Individual;
import com.clevel.selos.model.view.CustomerAttorneyView;

public class CustomerAttorneyTransform extends Transform {
	
	private static final long serialVersionUID = 5087069234814979143L;

	public CustomerAttorneyView transformToView(CustomerAttorney model) {
		if (model == null)
			return new CustomerAttorneyView();
		
		CustomerAttorneyView view = new CustomerAttorneyView();
		view.setId(model.getId());
		if (model.getCustomer() != null)
			view.setCustomerId(model.getCustomer().getId());
		if (model.getDocumentType() != null)
			view.setDocumentTypeId(model.getDocumentType().getId());
		view.setPersonalId(model.getPersonalID());
		if (model.getTitle() != null)
			view.setTitleId(model.getTitle().getId());
		view.setNameTh(model.getNameTh());
		view.setLastNameTh(model.getLastNameTh());
		view.setBirthDate(model.getBirthDate());
		view.setAge(model.getAge());
		if (model.getGender() != null)
			view.setGender(model.getGender());
		else
			view.setGender(Gender.NA);
		if (model.getRace() != null)
			view.setRaceId(model.getRace().getId());
		if (model.getNationality() != null)
			view.setNationalityId(model.getNationality().getId());
		view.setAddressNo(model.getAddressNo());
		view.setMoo(model.getMoo());
		view.setBuilding(model.getBuilding());
		view.setRoad(model.getRoad());
		if (model.getProvince() != null)
			view.setProvinceId(model.getProvince().getCode());
		if (model.getDistrict() != null)
			view.setDistrictId(model.getDistrict().getId());
		if (model.getSubDistrict() != null)
			view.setSubDistrictId(model.getSubDistrict().getCode());
		view.setPostalCode(model.getPostalCode());
		if (model.getCountry() != null)
			view.setCountryId(model.getCountry().getId());
		view.setPhoneNumber(model.getPhoneNumber());
		view.setPhoneExt(model.getExtension());
		if (model.getMaritalStatus() != null)
			view.setMaritalStatusId(model.getMaritalStatus().getId());
		if (model.getSpouseTitle() != null)
			view.setSpouseTitleId(model.getSpouseTitle().getId());
		view.setSpouseNameTh(model.getSpouseNameTh());
		view.setSpouseLastNameTh(model.getSpouseLastNameTh());
		if (model.getFatherTitle() != null)
			view.setFatherTitleId(model.getFatherTitle().getId());
		view.setFatherNameTh(model.getFatherNameTh());
		view.setFatherLastNameTh(model.getFatherLastNameTh());
		if (model.getMotherTitle() != null)
			view.setMotherTitleId(model.getMotherTitle().getId());
		view.setMotherNameTh(model.getMotherNameTh());
		view.setMotherLastNameTh(model.getMotherLastNameTh());
		view.setHomePhoneNumber(model.getHomePhoneNumber());
		view.setHomePhoneExt(model.getHomePhoneExtension());
		view.setMobile1(model.getMobile1());
		view.setMobile2(model.getMobile2());
		
		view.setCanUpdateGeneralData(true);
		view.setCanUpdateRelationInfo(true);
		view.setCanUpdateAddress(true);
		view.setCanUpdateOthers(true);
		return view;
	}
	
	public CustomerAttorneyView transformToView(Customer model,Address address) {
		if (model == null)
			return new CustomerAttorneyView();
		CustomerAttorneyView view = new CustomerAttorneyView();
		view.setId(0);
		view.setCustomerId(model.getId());
		if (model.getDocumentType() != null)
			view.setDocumentTypeId(model.getDocumentType().getId());
		if (model.getIndividual() != null) {
			Individual individual = model.getIndividual();
			view.setPersonalId(individual.getCitizenId());
			view.setBirthDate(individual.getBirthDate());
			view.setGender(Gender.lookup(individual.getGender()));
			if (individual.getRace() != null)
				view.setRaceId(individual.getRace().getId());
			if (individual.getNationality() != null)
				view.setNationalityId(individual.getNationality().getId());
			if (individual.getMaritalStatus() != null)
				view.setMaritalStatusId(individual.getMaritalStatus().getId());
		}
		
		if (model.getTitle() != null)
			view.setTitleId(model.getTitle().getId());
		view.setNameTh(model.getNameTh());
		view.setLastNameTh(model.getLastNameTh());
		view.setAge(model.getAge());
		
		if (address != null) {
			view.setAddressNo(address.getAddressNo());
			view.setMoo(address.getMoo());
			view.setBuilding(address.getBuilding());
			view.setRoad(address.getRoad());
			if (address.getProvince() != null)
				view.setProvinceId(address.getProvince().getCode());
			if (address.getDistrict() != null)
				view.setDistrictId(address.getDistrict().getId());
			if (address.getSubDistrict() != null)
				view.setSubDistrictId(address.getSubDistrict().getCode());
			view.setPostalCode(address.getPostalCode());
			if (address.getCountry() != null)
				view.setCountryId(address.getCountry().getId());
			view.setPhoneNumber(address.getPhoneNumber());
			view.setPhoneExt(address.getExtension());
			
		}
		
		view.setMobile1(model.getMobileNumber());
		/*
		 * No data from customer
		 * spouse -> Info
		 * father -> Info
		 * mother -> Info
		 * Home Phone
		 * Home Phone Ext
		 * Mobile 2
		 */
		view.setCanUpdateGeneralData(model.getIndividual() == null);
		view.setCanUpdateRelationInfo(true);
		view.setCanUpdateAddress(address == null);
		view.setCanUpdateOthers(true);
		return view;
	}

}
