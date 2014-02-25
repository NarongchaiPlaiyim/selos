package com.clevel.selos.transform;

import javax.inject.Inject;

import com.clevel.selos.dao.master.CountryDAO;
import com.clevel.selos.dao.master.DistrictDAO;
import com.clevel.selos.dao.master.DocumentTypeDAO;
import com.clevel.selos.dao.master.MaritalStatusDAO;
import com.clevel.selos.dao.master.NationalityDAO;
import com.clevel.selos.dao.master.ProvinceDAO;
import com.clevel.selos.dao.master.RaceDAO;
import com.clevel.selos.dao.master.SubDistrictDAO;
import com.clevel.selos.dao.master.TitleDAO;
import com.clevel.selos.dao.working.CustomerDAO;
import com.clevel.selos.model.AttorneyType;
import com.clevel.selos.model.Gender;
import com.clevel.selos.model.db.working.Address;
import com.clevel.selos.model.db.working.Customer;
import com.clevel.selos.model.db.working.CustomerAttorney;
import com.clevel.selos.model.db.working.Individual;
import com.clevel.selos.model.db.working.WorkCase;
import com.clevel.selos.model.view.CustomerAttorneyView;

public class CustomerAttorneyTransform extends Transform {
	
	private static final long serialVersionUID = 5087069234814979143L;
	
	@Inject private DocumentTypeDAO documentTypeDAO;
	@Inject private TitleDAO titleDAO;
	@Inject private RaceDAO raceDAO;
	@Inject private NationalityDAO nationalityDAO;
	@Inject private ProvinceDAO provinceDAO;
	@Inject private DistrictDAO districtDAO;
	@Inject private SubDistrictDAO subDistrictDAO;
	@Inject private MaritalStatusDAO maritalStatusDAO;
	@Inject private CountryDAO countryDAO;
	@Inject private CustomerDAO customerDAO;
	
	public CustomerAttorneyView transformToView(CustomerAttorney model) {
		if (model == null)
			return new CustomerAttorneyView();
		
		CustomerAttorneyView view = new CustomerAttorneyView();
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
		
		if (model.getCustomer() != null) {
			view.setCanUpdateGeneralData(model.getCustomer().getIndividual() == null);
		} else {
			view.setCanUpdateGeneralData(true);	
		}
		
		view.setCanUpdateRelationInfo(true);
		view.setCanUpdateAddress(true);
		view.setCanUpdateOthers(true);
		return view;
	}
	
	public CustomerAttorneyView transformToView(Customer model,Address address) {
		if (model == null)
			return new CustomerAttorneyView();
		CustomerAttorneyView view = new CustomerAttorneyView();
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
		if (model.getSpouseId() > 0) {
    		Customer spouseModel = customerDAO.findById(model.getSpouseId());
    		if (spouseModel != null) {
    			if (spouseModel.getTitle() != null)
    				view.setSpouseTitleId(spouseModel.getTitle().getId());
    			view.setSpouseNameTh(spouseModel.getNameTh());
    			view.setSpouseLastNameTh(spouseModel.getLastNameTh());
    		}
    	}
		/*
		 * No data from customer
		 * father -> Info
		 * mother -> Info
		 * Home Phone
		 * Home Phone Ext
		 * Mobile 2
		 */
		view.setCanUpdateGeneralData(model.getIndividual() == null);
		view.setCanUpdateRelationInfo(true);
		view.setCanUpdateAddress(true);
		view.setCanUpdateOthers(true);
		return view;
	}
	
	public CustomerAttorney createNewModel(CustomerAttorneyView view,WorkCase workCase,AttorneyType type) {
		CustomerAttorney model = new CustomerAttorney();
		model.setAttorneyType(type);
		model.setWorkCase(workCase);
		updateModelFromView(model, view);
		return model;
	}

	public void updateModelFromView(CustomerAttorney model,CustomerAttorneyView view) {
		//TODO Check about customer mapping
		if (view.getCustomerId() > 0)
			model.setCustomer(customerDAO.findRefById(view.getCustomerId()));
		else
			model.setCustomer(null);
		
		
		if (view.getDocumentTypeId() > 0)
			model.setDocumentType(documentTypeDAO.findRefById(view.getDocumentTypeId()));
		else
			model.setDocumentType(null);
		model.setPersonalID(view.getPersonalId());
		if (view.getTitleId() > 0)
			model.setTitle(titleDAO.findRefById(view.getTitleId()));
		else
			model.setTitle(null);
		model.setNameTh(view.getNameTh());
		model.setLastNameTh(view.getLastNameTh());
		model.setBirthDate(view.getBirthDate());
		model.setAge(view.getAge());
		model.setGender(view.getGender());
		if (view.getNationalityId() > 0)
			model.setNationality(nationalityDAO.findRefById(view.getNationalityId()));
		else
			model.setRace(null);
		if (view.getRaceId() > 0)
			model.setRace(raceDAO.findRefById(view.getRaceId()));
		else
			model.setRace(null);
		
		model.setAddressNo(view.getAddressNo());
		model.setMoo(view.getMoo());
		model.setRoad(view.getRoad());
		if (view.getProvinceId() > 0)
			model.setProvince(provinceDAO.findRefById(view.getProvinceId()));
		else
			model.setProvince(null);
		if (view.getDistrictId() > 0)
			model.setDistrict(districtDAO.findRefById(view.getDistrictId()));
		else
			model.setDistrict(null);
		if (view.getSubDistrictId() > 0)
			model.setSubDistrict(subDistrictDAO.findRefById(view.getSubDistrictId()));
		else
			model.setSubDistrict(null);
		model.setPostalCode(view.getPostalCode());
		if (view.getCountryId() > 0)
			model.setCountry(countryDAO.findRefById(view.getCountryId()));
		else
			model.setCountry(null);
		model.setPhoneNumber(view.getPhoneNumber());
		model.setExtension(view.getPhoneExt());
		if (view.getMaritalStatusId() > 0)
			model.setMaritalStatus(maritalStatusDAO.findRefById(view.getMaritalStatusId()));
		else
			model.setMaritalStatus(null);
		if (view.getSpouseTitleId() > 0)
			model.setSpouseTitle(titleDAO.findRefById(view.getSpouseTitleId()));
		else
			model.setSpouseTitle(null);
		model.setSpouseNameTh(view.getSpouseNameTh());
		model.setSpouseLastNameTh(view.getSpouseLastNameTh());
		if (view.getFatherTitleId() > 0)
			model.setFatherTitle(titleDAO.findRefById(view.getFatherTitleId()));
		else
			model.setFatherTitle(null);
		model.setFatherNameTh(view.getFatherNameTh());
		model.setFatherLastNameTh(view.getFatherLastNameTh());
		if (view.getMotherTitleId() > 0)
			model.setMotherTitle(titleDAO.findRefById(view.getMotherTitleId()));
		else
			model.setMotherTitle(null);
		model.setMotherNameTh(view.getMotherNameTh());
		model.setMotherLastNameTh(view.getMotherLastNameTh());
		model.setHomePhoneNumber(view.getHomePhoneNumber());
		model.setHomePhoneExtension(view.getHomePhoneExt());
		model.setMobile1(view.getMobile1());
		model.setMobile2(view.getMobile2());
	}
}
