package com.clevel.selos.businesscontrol;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.faces.model.SelectItem;
import javax.inject.Inject;

import org.slf4j.Logger;

import com.clevel.selos.dao.master.CountryDAO;
import com.clevel.selos.dao.master.DistrictDAO;
import com.clevel.selos.dao.master.DocumentTypeDAO;
import com.clevel.selos.dao.master.MaritalStatusDAO;
import com.clevel.selos.dao.master.NationalityDAO;
import com.clevel.selos.dao.master.ProvinceDAO;
import com.clevel.selos.dao.master.RaceDAO;
import com.clevel.selos.dao.master.SubDistrictDAO;
import com.clevel.selos.dao.master.TitleDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.Language;
import com.clevel.selos.model.db.master.Country;
import com.clevel.selos.model.db.master.District;
import com.clevel.selos.model.db.master.DocumentType;
import com.clevel.selos.model.db.master.MaritalStatus;
import com.clevel.selos.model.db.master.Nationality;
import com.clevel.selos.model.db.master.Province;
import com.clevel.selos.model.db.master.Race;
import com.clevel.selos.model.db.master.SubDistrict;
import com.clevel.selos.model.db.master.Title;


@Stateless
public class GeneralPeopleInfoControl extends BusinessControl {
	 private static final long serialVersionUID = 3165380054098839877L;
	 @Inject @SELOS
	 private Logger log;
	 private static final int CUSTOMER_ENTITY_INDIVIDUAL = 1;
	 @Inject private DocumentTypeDAO documentTypeDAO;
	 @Inject private TitleDAO titleDAO;
	 @Inject private RaceDAO raceDAO;
	 @Inject private NationalityDAO nationalityDAO;
	 @Inject private ProvinceDAO provinceDAO;
	 @Inject private DistrictDAO districtDAO;
	 @Inject private SubDistrictDAO subDistrictDAO;
	 @Inject private MaritalStatusDAO maritalStatusDAO;
	 @Inject private CountryDAO countryDAO;
	 
	 public GeneralPeopleInfoControl() {
	 }
	 
	 public List<SelectItem> listIndividualDocumentTypes() {
		 List<SelectItem> rtnDatas = new ArrayList<SelectItem>();
		 
		 List<DocumentType> list = documentTypeDAO.findByCustomerEntityId(CUSTOMER_ENTITY_INDIVIDUAL);
		 for (DocumentType data : list) {
			 SelectItem item = new SelectItem();
			 item.setValue(data.getId());
			 item.setLabel(data.getDescription());
			 item.setDescription(data.getDescription());
			 
			 rtnDatas.add(item);
		 }
		 return rtnDatas;
	 }
	 
	 public List<SelectItem> listIndividualTitles(Language lang) {
		 List<SelectItem> rtnDatas = new ArrayList<SelectItem>();
		 
		 List<Title> list = titleDAO.getListByCustomerEntityId(CUSTOMER_ENTITY_INDIVIDUAL);
		 for (Title data : list) {
			 SelectItem item = new SelectItem();
			 item.setValue(data.getId());
			 if (Language.TH.equals(lang)) {
				 item.setLabel(data.getTitleTh());
				 item.setDescription(data.getTitleTh());
			 } else {
				 item.setLabel(data.getTitleEn());
				 item.setDescription(data.getTitleEn());
			 }
			 rtnDatas.add(item);
		 }
		 return rtnDatas;
	 }
	 
	 public List<SelectItem> listRaces() {
		 List<SelectItem> rtnDatas = new ArrayList<SelectItem>();
		 
		 List<Race> list = raceDAO.findActiveAll();
		 for (Race data : list) {
			 SelectItem item = new SelectItem();
			 item.setValue(data.getId());
			 item.setLabel(data.getName());
			 item.setDescription(data.getName());
			 
			 rtnDatas.add(item);
		 }
		 return rtnDatas;
	 }
	 public List<SelectItem> listNationalities() {
		 List<SelectItem> rtnDatas = new ArrayList<SelectItem>();
		 
		 List<Nationality> list = nationalityDAO.findActiveAll();
		 for (Nationality data : list) {
			 SelectItem item = new SelectItem();
			 item.setValue(data.getId());
			 item.setLabel(data.getName());
			 item.setDescription(data.getName());
			 
			 rtnDatas.add(item);
		 }
		 return rtnDatas;
	 }
	 public List<SelectItem> listProvinces() {
		 List<SelectItem> rtnDatas = new ArrayList<SelectItem>();
		 
		 List<Province> list = provinceDAO.findActiveAll();
		 for (Province data : list) {
			 SelectItem item = new SelectItem();
			 item.setValue(data.getCode());
			 item.setLabel(data.getName());
			 item.setDescription(data.getName());
			 
			 rtnDatas.add(item);
		 }
		 return rtnDatas; 
	 }
	 public List<SelectItem> listDistricts(int provinceId) {
		 List<SelectItem> rtnDatas = new ArrayList<SelectItem>();
		 
		 List<District> list = districtDAO.getListByProvince(provinceId);
		 for (District data : list) {
			 SelectItem item = new SelectItem();
			 item.setValue(data.getId());
			 item.setLabel(data.getName());
			 item.setDescription(data.getName());
			 
			 rtnDatas.add(item);
		 }
		 return rtnDatas; 
	 }
	 public List<SelectItem> listSubDistricts(int districtId) {
		 List<SelectItem> rtnDatas = new ArrayList<SelectItem>();
		 
		 List<SubDistrict> list = subDistrictDAO.getListByDistrict(districtId);
		 for (SubDistrict data : list) {
			 SelectItem item = new SelectItem();
			 item.setValue(data.getCode());
			 item.setLabel(data.getName());
			 item.setDescription(data.getName());
			 
			 rtnDatas.add(item);
		 }
		 return rtnDatas; 
	 }
	 public List<SelectItem> listMaritialStatuses() {
		 List<SelectItem> rtnDatas = new ArrayList<SelectItem>();
		 
		 List<MaritalStatus> list = maritalStatusDAO.findActiveAll();
		 for (MaritalStatus data : list) {
			 SelectItem item = new SelectItem();
			 item.setValue(data.getId());
			 item.setLabel(data.getName());
			 item.setDescription(data.getName());
			 
			 rtnDatas.add(item);
		 }
		 return rtnDatas; 
	 }
	 public List<SelectItem> listCountries() {
		 List<SelectItem> rtnDatas = new ArrayList<SelectItem>();
		 
		 List<Country> list = countryDAO.findActiveAll();
		 for (Country data : list) {
			 SelectItem item = new SelectItem();
			 item.setValue(data.getId());
			 item.setLabel(data.getName());
			 item.setDescription(data.getName());
			 
			 rtnDatas.add(item);
		 }
		 return rtnDatas; 
	 }
	 
}