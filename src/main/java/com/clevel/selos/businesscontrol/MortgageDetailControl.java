package com.clevel.selos.businesscontrol;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.faces.model.SelectItem;
import javax.inject.Inject;

import org.slf4j.Logger;

import com.clevel.selos.dao.master.MortgageLandOfficeDAO;
import com.clevel.selos.dao.master.MortgageOSCompanyDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.MortgageLandOffice;
import com.clevel.selos.model.db.master.MortgageOSCompany;

@Stateless
public class MortgageDetailControl extends BusinessControl {
	 private static final long serialVersionUID = 2723725914845871936L;

	@Inject @SELOS
	 private Logger log;
	 
	 @Inject private MortgageOSCompanyDAO mortgageOSCompanyDAO;
	 @Inject private MortgageLandOfficeDAO mortgageLandOfficeDAO;
	 
	 public MortgageDetailControl() {
	 }
	 
	 public List<SelectItem> listMortgageOSCompanies() {
		 List<SelectItem> rtnDatas = new ArrayList<SelectItem>();
		 
		 List<MortgageOSCompany> list = mortgageOSCompanyDAO.findActiveAll();
		 for (MortgageOSCompany data : list) {
			 SelectItem item = new SelectItem();
			 item.setValue(data.getId());
			 item.setLabel(data.getName());
			 item.setDescription(data.getDescription());
			 
			 rtnDatas.add(item);
		 }
		 return rtnDatas;
	 }
	 public List<SelectItem> listMortgageLandOffices() {
		 List<SelectItem> rtnDatas = new ArrayList<SelectItem>();
		 
		 List<MortgageLandOffice> list = mortgageLandOfficeDAO.findActiveAll();
		 for (MortgageLandOffice data : list) {
			 SelectItem item = new SelectItem();
			 item.setValue(data.getId());
			 item.setLabel(data.getName());
			 item.setDescription(data.getDescription());
			 
			 rtnDatas.add(item);
		 }
		 return rtnDatas;
	 }
}
