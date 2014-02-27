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
import com.clevel.selos.model.db.working.Juristic;
import com.clevel.selos.model.db.working.WorkCase;
import com.clevel.selos.model.view.CustomerAttorneySelectView;
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
		view.setId(model.getId());
		if (model.getCustomer() != null) {
			view.setCustomerId(model.getCustomer().getId());
			view.setJuristic(model.getCustomer().getJuristic() != null);
		} else {
			view.setJuristic(false);
		}
		
		if (model.getDocumentType() != null) {
			view.setDocumentTypeId(model.getDocumentType().getId());
			view.setDisplayDocumentType(model.getDocumentType().getDescription());
		}
		
		view.setAge(model.getAge());
    	if (model.getTitle() != null) {
    		view.setTitleId(model.getTitle().getId());
    		view.setDisplayTitle(model.getTitle().getTitleTh());
    	}
    	view.setNameTH(model.getNameTh());
		view.setLastNameTH(model.getLastNameTh());
		view.setPersonalId(model.getPersonalID());
		view.setBirthDate(model.getBirthDate());
		view.setGender(model.getGender());
		if (model.getRace() != null) {
			view.setRaceId(model.getRace().getId());
			view.setDisplayRace(model.getRace().getName());
		}
		if (model.getNationality() != null) {
			view.setNationalityId(model.getNationality().getId());
			view.setDisplayNationality(model.getNationality().getName());
		}
		
		if (model.getMaritalStatus() != null) {
    		view.setMaritalStatusId(model.getMaritalStatus().getId());
    		view.setDisplayMaritalStatus(model.getMaritalStatus().getName());
    	}
		if (model.getSpouseTitle() != null) {
			view.setSpouseTitleId(model.getSpouseTitle().getId());
			view.setDisplaySpouseTitle(model.getSpouseTitle().getTitleTh());
		}
		view.setSpouseNameTH(model.getSpouseNameTh());
		view.setSpouseLastNameTH(model.getSpouseLastNameTh());
		
    	if (model.getFatherTitle() != null) {
    		view.setFatherTitleId(model.getFatherTitle().getId());
    		view.setDisplayFatherTitle(model.getFatherTitle().getTitleTh());
    	}
    	view.setFatherNameTH(model.getFatherNameTh());
    	view.setFatherLastNameTH(model.getFatherLastNameTh());
    	if (model.getMotherTitle() != null) {
    		view.setMotherTitleId(model.getMotherTitle().getId());
    		view.setDisplayMotherTitle(model.getMotherTitle().getTitleTh());
    	}
    	view.setMotherNameTH(model.getMotherNameTh());
    	view.setMotherLastNameTH(model.getMotherLastNameTh());
    	
		view.setAddressNo(model.getAddressNo());
		view.setMoo(model.getMoo());
		view.setBuilding(model.getBuilding());
		view.setRoad(model.getRoad());
		if (model.getProvince() != null) {
			view.setProvinceId(model.getProvince().getCode());
			view.setDisplayProvince(model.getProvince().getName());
		}
		if (model.getDistrict() != null) {
			view.setDistrictId(model.getDistrict().getId());
			view.setDisplayDistrict(model.getDistrict().getName());
		}
		if (model.getSubDistrict() != null) {
			view.setSubDistrictId(model.getSubDistrict().getCode());
			view.setDisplaySubDistrict(model.getSubDistrict().getName());
		}
		view.setPostalCode(model.getPostalCode());
		if (model.getCountry() != null) {
			view.setCountryId(model.getCountry().getId());
			view.setDisplayCountry(model.getCountry().getName());
		}
		view.setPhoneNumber(model.getPhoneNumber());
		view.setPhoneExt(model.getExtension());
		
		view.setHomePhoneNumber(model.getHomePhoneNumber());
		view.setHomePhoneExt(model.getHomePhoneExtension());
		
		view.setMobile1(model.getMobile1());
		view.setMobile2(model.getMobile2());
		return view;
	}
	
	public CustomerAttorneyView transformToView(Customer model) {
		if (model == null)
			return new CustomerAttorneyView();
		 
		CustomerAttorneyView view = new CustomerAttorneyView();
		view.setCustomerId(model.getId());
		
		if (model.getDocumentType() != null) {
			view.setDocumentTypeId(model.getDocumentType().getId());
			view.setDisplayDocumentType(model.getDocumentType().getDescription());
		}
		
		view.setAge(model.getAge());
    	if (model.getTitle() != null) {
    		view.setTitleId(model.getTitle().getId());
    		view.setDisplayTitle(model.getTitle().getTitleTh());
    	}
    	view.setNameTH(model.getNameTh());
		view.setLastNameTH(model.getLastNameTh());
		
		int useAddressTypeId = 0;
		if (model.getIndividual() != null) {
			Individual indv = model.getIndividual();
			view.setPersonalId(indv.getCitizenId());
			view.setBirthDate(indv.getBirthDate());
			view.setGender(Gender.lookup(indv.getGender()));
			if (indv.getRace() != null) {
				view.setRaceId(indv.getRace().getId());
				view.setDisplayRace(indv.getRace().getName());
			}
			if (indv.getNationality() != null) {
				view.setNationalityId(indv.getNationality().getId());
				view.setDisplayNationality(indv.getNationality().getName());
			}
			
			if (indv.getMaritalStatus() != null) {
	    		view.setMaritalStatusId(indv.getMaritalStatus().getId());
	    		view.setDisplayMaritalStatus(indv.getMaritalStatus().getName());
	    	}
			if (model.getSpouseId() > 0) {
	    		Customer spouseModel = customerDAO.findById(model.getSpouseId());
	    		if (spouseModel != null) {
	    			if (spouseModel.getTitle() != null) {
	    				view.setSpouseTitleId(spouseModel.getTitle().getId());
	    				view.setDisplaySpouseTitle(model.getTitle().getTitleTh());
	    			}
	    			view.setSpouseNameTH(spouseModel.getNameTh());
	    			view.setSpouseLastNameTH(spouseModel.getLastNameTh());
	    		}
	    	}
	    	
	    	if (indv.getFatherTitle() != null) {
	    		view.setFatherTitleId(indv.getFatherTitle().getId());
	    		view.setDisplayFatherTitle(indv.getFatherTitle().getTitleTh());
	    	}
	    	view.setFatherNameTH(indv.getFatherNameTh());
	    	view.setFatherLastNameTH(indv.getFatherLastNameTh());
	    	if (indv.getMotherTitle() != null) {
	    		view.setMotherTitleId(indv.getMotherTitle().getId());
	    		view.setDisplayMotherTitle(indv.getMotherTitle().getTitleTh());
	    	}
	    	view.setMotherNameTH(indv.getMotherNameTh());
	    	view.setMotherLastNameTH(indv.getMotherLastNameTh());
	    	
	    	//for individual using address id - 2 Register
	    	useAddressTypeId = 2;
		} else if (model.getJuristic() != null) {
			view.setJuristic(true);
			Juristic juris = model.getJuristic();
			view.setPersonalId(juris.getRegistrationId());
			view.setBirthDate(juris.getRegisterDate());
			
			//for juristic using address id - 4 Register
			useAddressTypeId = 4;
		}
		
		Address address = null;
		if (useAddressTypeId != 0 && model.getAddressesList() != null && !model.getAddressesList().isEmpty()) {
			for (Address check : model.getAddressesList()) {
				if (check.getAddressType().getId() == useAddressTypeId) {
					address = check;
					break;
				}
			}
		}
		
		if (address != null) {
			view.setAddressNo(address.getAddressNo());
			view.setMoo(address.getMoo());
			view.setBuilding(address.getBuilding());
			view.setRoad(address.getRoad());
			if (address.getProvince() != null) {
				view.setProvinceId(address.getProvince().getCode());
				view.setDisplayProvince(address.getProvince().getName());
			}
			if (address.getDistrict() != null) {
				view.setDistrictId(address.getDistrict().getId());
				view.setDisplayDistrict(address.getDistrict().getName());
			}
			if (address.getSubDistrict() != null) {
				view.setSubDistrictId(address.getSubDistrict().getCode());
				view.setDisplaySubDistrict(address.getSubDistrict().getName());
			}
			view.setPostalCode(address.getPostalCode());
			if (address.getCountry() != null) {
				view.setCountryId(address.getCountry().getId());
				view.setDisplayCountry(address.getCountry().getName());
			}
			view.setPhoneNumber(address.getPhoneNumber());
			view.setPhoneExt(address.getExtension());
			
			view.setHomePhoneNumber(address.getPhoneNumber());
			view.setHomePhoneExt(address.getExtension());
		}
		
		view.setMobile1(model.getMobileNumber());
		view.setMobile2("");
		return view;
	}
	
	public CustomerAttorney createNewModel(CustomerAttorneyView view,WorkCase workCase,AttorneyType type) {
		CustomerAttorney model = new CustomerAttorney();
		model.setAttorneyType(type);
		model.setWorkCase(workCase);
		updateModelFromView(model, view);
		return model;
	}
	
	public CustomerAttorneySelectView transformAttorneySelectToView(Customer model) {
		CustomerAttorneySelectView view = new CustomerAttorneySelectView();
		view.setCustomerId(model.getId());
		view.setCustomerName(model.getDisplayName());
		
		if (model.getIndividual() != null)
			view.setCitizenId(model.getIndividual().getCitizenId());
		else if (model.getJuristic() != null)
			view.setCitizenId(model.getJuristic().getRegistrationId());
		else
			view.setCitizenId("-");
		view.setTmbCustomerId(model.getTmbCustomerId());
		if (model.getRelation() != null)
			view.setRelation(model.getRelation().getDescription());
		if (model.getJuristic() != null)
			view.setJuristic(true);
		else
			view.setJuristic(false);
		return view;
	}

	public void updateModelFromView(CustomerAttorney model,CustomerAttorneyView view) {
		view.calculateAge();
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
		model.setNameTh(view.getNameTH());
		model.setLastNameTh(view.getLastNameTH());
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
		model.setSpouseNameTh(view.getSpouseNameTH());
		model.setSpouseLastNameTh(view.getSpouseLastNameTH());
		if (view.getFatherTitleId() > 0)
			model.setFatherTitle(titleDAO.findRefById(view.getFatherTitleId()));
		else
			model.setFatherTitle(null);
		model.setFatherNameTh(view.getFatherNameTH());
		model.setFatherLastNameTh(view.getFatherLastNameTH());
		if (view.getMotherTitleId() > 0)
			model.setMotherTitle(titleDAO.findRefById(view.getMotherTitleId()));
		else
			model.setMotherTitle(null);
		model.setMotherNameTh(view.getMotherNameTH());
		model.setMotherLastNameTh(view.getMotherLastNameTH());
		model.setHomePhoneNumber(view.getHomePhoneNumber());
		model.setHomePhoneExtension(view.getHomePhoneExt());
		model.setMobile1(view.getMobile1());
		model.setMobile2(view.getMobile2());
	}
}
