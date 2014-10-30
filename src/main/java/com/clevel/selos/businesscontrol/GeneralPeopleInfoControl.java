package com.clevel.selos.businesscontrol;

import com.clevel.selos.dao.master.*;
import com.clevel.selos.dao.working.WorkCaseDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.Language;
import com.clevel.selos.model.db.master.*;
import com.clevel.selos.model.db.working.WorkCase;
import com.clevel.selos.model.view.LastUpdateDataView;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Stateless
public class GeneralPeopleInfoControl extends BusinessControl {
	 private static final long serialVersionUID = 3165380054098839877L;
	 @Inject @SELOS
	 private Logger log;
	 private static final int CUSTOMER_ENTITY_INDIVIDUAL = 1;
	 private static final int CUSTOMER_ENTITY_JURISTIC = 2;
	 @Inject private RelationDAO relationDAO;
	 @Inject private DocumentTypeDAO documentTypeDAO;
	 @Inject private TitleDAO titleDAO;
	 @Inject private RaceDAO raceDAO;
	 @Inject private NationalityDAO nationalityDAO;
	 @Inject private ProvinceDAO provinceDAO;
	 @Inject private DistrictDAO districtDAO;
	 @Inject private SubDistrictDAO subDistrictDAO;
	 @Inject private MaritalStatusDAO maritalStatusDAO;
	 @Inject private CountryDAO countryDAO;
	 @Inject private AddressTypeDAO addressTypeDAO;
	 @Inject private BusinessTypeDAO businessTypeDAO;
	 @Inject private WorkCaseDAO workCaseDAO;
	 
	 @Inject private ReasonDAO reasonDAO;
	 
	 public GeneralPeopleInfoControl() {
	 }
	 
	 public List<SelectItem> listCancelReasons() {
		 List<SelectItem> rtnDatas = new ArrayList<SelectItem>();
		 List<Reason> reasons = reasonDAO.getCancelList();
		 for (Reason reason : reasons) {
			 SelectItem item = new SelectItem();
			 item.setValue(reason.getId());
			 item.setLabel(reason.getDescription());
			 item.setDescription(reason.getDescription());
			 rtnDatas.add(item);
		 }
		 return rtnDatas;
	 }
	 public List<SelectItem> listReturnReasons() {
		 List<SelectItem> rtnDatas = new ArrayList<SelectItem>();
		 List<Reason> reasons = reasonDAO.getReasonList();
		 for (Reason reason : reasons) {
			 SelectItem item = new SelectItem();
			 item.setValue(reason.getId());
			 item.setLabel(reason.getDescription());
			 item.setDescription(reason.getDescription());
			 rtnDatas.add(item);
		 }
		 return rtnDatas;
	 }
	
	 public List<SelectItem> listRelationTypes() {
		 List<SelectItem> rtnDatas = new ArrayList<SelectItem>();
		 List<Relation> list = relationDAO.findActiveAll();
		 for (Relation data : list) {
			 SelectItem item = new SelectItem();
			 item.setValue(data.getId());
			 item.setLabel(data.getDescription());
			 item.setDescription(data.getDescription());
			 rtnDatas.add(item);
		 }
		 return rtnDatas;
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
	 public List<SelectItem> listJuristicDocumentTypes() {
		 List<SelectItem> rtnDatas = new ArrayList<SelectItem>();
		 
		 List<DocumentType> list = documentTypeDAO.findByCustomerEntityId(CUSTOMER_ENTITY_JURISTIC);
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
			 //Not use Langauge flag, send default as TH
			 item.setLabel(data.getTitleTh());
			 item.setDescription(data.getTitleTh());
//			 if (Language.TH.equals(lang)) {
//				 item.setLabel(data.getTitleTh());
//				 item.setDescription(data.getTitleTh());
//			 } else {
//				 item.setLabel(data.getTitleEn());
//				 item.setDescription(data.getTitleEn());
//			 }
			 rtnDatas.add(item);
		 }
		 return rtnDatas;
	 }
	 public List<SelectItem> listJuristicTitles(Language lang) {
		 List<SelectItem> rtnDatas = new ArrayList<SelectItem>();
		 
		 List<Title> list = titleDAO.getListByCustomerEntityId(CUSTOMER_ENTITY_JURISTIC);
		 for (Title data : list) {
			 SelectItem item = new SelectItem();
			 item.setValue(data.getId());
			 //Not use Langauge flag, send default as TH
			 item.setLabel(data.getTitleTh());
			 item.setDescription(data.getTitleTh());
//			 if (Language.TH.equals(lang)) {
//				 item.setLabel(data.getTitleTh());
//				 item.setDescription(data.getTitleTh());
//			 } else {
//				 item.setLabel(data.getTitleEn());
//				 item.setDescription(data.getTitleEn());
//			 }
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
	 public List<SelectItem> listMaritalStatuses() {
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
	 public Set<Integer> listSpouseReqMaritalStatues() {
		 List<MaritalStatus> list = maritalStatusDAO.findActiveAll();
		 Set<Integer> spouseSet = new HashSet<Integer>();
		 for (MaritalStatus data : list) {
			 if (data.getSpouseFlag() == 1)
				 spouseSet.add(data.getId());
		 }
		 return spouseSet;
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
	 
	 public List<SelectItem> listIndividualAddressTypes() {
		 List<SelectItem> rtnDatas = new ArrayList<SelectItem>();
         SelectItem selectItem = new SelectItem();
         selectItem.setValue(0);
         selectItem.setLabel("");
         selectItem.setDescription("");
         rtnDatas.add(selectItem);
		 List<AddressType> list = addressTypeDAO.findByCustomerEntityId(CUSTOMER_ENTITY_INDIVIDUAL);
		 for (AddressType data : list) {
			 SelectItem item = new SelectItem();
			 item.setValue(data.getId());
			 item.setLabel(data.getName());
			 item.setDescription(data.getName());
             log.debug("listIndividualAddressTypes (AddressType : {})",data);
			 rtnDatas.add(item);
		 }
		 return rtnDatas; 
	 }
	 public List<SelectItem> listJuristicAddressTypes() {
		 List<SelectItem> rtnDatas = new ArrayList<SelectItem>();
         SelectItem selectItem = new SelectItem();
         selectItem.setValue(0);
         selectItem.setLabel("");
         selectItem.setDescription("");
         rtnDatas.add(selectItem);
		 List<AddressType> list = addressTypeDAO.findByCustomerEntityId(CUSTOMER_ENTITY_JURISTIC);
		 for (AddressType data : list) {
			 SelectItem item = new SelectItem();
			 item.setValue(data.getId());
			 item.setLabel(data.getName());
			 item.setDescription(data.getName());
			 
			 rtnDatas.add(item);
		 }
		 return rtnDatas; 
	 }
	 public List<SelectItem> listBusinessTypes() {
		 List<SelectItem> rtnDatas = new ArrayList<SelectItem>();
		 
		 List<BusinessType> list = businessTypeDAO.findActiveAll();
		 for (BusinessType data : list) {
			 SelectItem item = new SelectItem();
			 item.setValue(data.getId());
			 item.setLabel(data.getName());
			 item.setDescription(data.getDescription());
			 
			 rtnDatas.add(item);
		 }
		 return rtnDatas; 
	 }
	 
	 public LastUpdateDataView getWorkCaseLastUpdate(long workCaseId) {
		 LastUpdateDataView view = new LastUpdateDataView();
		 if (workCaseId <= 0)
			 return view;
		 
		 WorkCase workCase = null;
		 try {
			 workCase = workCaseDAO.findById(workCaseId);
		 } catch (Throwable e) {}
		 if (workCase != null) {
			 view.setModifyDate(workCase.getModifyDate());
			 if (workCase.getModifyBy() != null)
				 view.setModifyUser(workCase.getModifyBy().getDisplayName());
		 }
		 return view;
	 }
}
