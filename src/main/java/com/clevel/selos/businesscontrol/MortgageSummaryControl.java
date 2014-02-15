package com.clevel.selos.businesscontrol;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.faces.model.SelectItem;
import javax.inject.Inject;

import org.slf4j.Logger;

import com.clevel.selos.dao.master.BankBranchDAO;
import com.clevel.selos.dao.master.UserZoneDAO;
import com.clevel.selos.dao.working.AgreementInfoDAO;
import com.clevel.selos.dao.working.MortgageSummaryDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.BankBranch;
import com.clevel.selos.model.db.master.UserZone;
import com.clevel.selos.model.db.working.AgreementInfo;
import com.clevel.selos.model.db.working.MortgageSummary;
import com.clevel.selos.model.view.MortgageSummaryView;
import com.clevel.selos.transform.MortgageSummaryTransform;

@Stateless
public class MortgageSummaryControl extends BusinessControl {
    private static final long serialVersionUID = 1949436898372853571L;

	@Inject
    @SELOS
    private Logger log;
    
    @Inject
    private MortgageSummaryDAO mortgageSummaryDAO;
    @Inject
    private AgreementInfoDAO agreementInfoDAO;
    
    @Inject
    private UserZoneDAO userZoneDAO;
    @Inject
    private BankBranchDAO bankBranchDAO;
    
    @Inject
    MortgageSummaryTransform mortgageSummaryTransform;

    @Inject
    public MortgageSummaryControl(){

    }
    
    public List<SelectItem> listUserZones() {
    	List<SelectItem> rtnDatas = new ArrayList<SelectItem>();
    	List<UserZone> userZones = userZoneDAO.findActiveAll();
    	for (UserZone userZone : userZones) {
    		SelectItem item = new SelectItem();
    		item.setLabel(userZone.getName());
    		item.setDescription(userZone.getName());
    		item.setValue(userZone.getId());
    		
    		rtnDatas.add(item);
    	}
    	return rtnDatas;
    }
    public List<SelectItem> listBankBranches() {
    	List<SelectItem> rtnDatas = new ArrayList<SelectItem>();
    	List<BankBranch> bankBranches = bankBranchDAO.findActiveAll();
    	for (BankBranch bankBranch : bankBranches) {
    		SelectItem item = new SelectItem();
    		item.setLabel(bankBranch.getName());
    		item.setDescription(bankBranch.getDescription());
    		item.setValue(bankBranch.getId());
    		
    		rtnDatas.add(item);
    	}
    	return rtnDatas;
    }
    public MortgageSummaryView getMortgageSummaryView(long workCaseId) {
        MortgageSummary mortgage = null;
        AgreementInfo agreement = null;
        if (workCaseId > 0) {
        	try {
        		mortgage = mortgageSummaryDAO.findByWorkCaseId(workCaseId);
        	} catch (Throwable e) {}
        	try {
        		agreement = agreementInfoDAO.findByWorkCaseId(workCaseId);
        	} catch (Throwable e) {}
        }
        
        return mortgageSummaryTransform.transformToView(mortgage, agreement);
    }
}
