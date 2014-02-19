package com.clevel.selos.businesscontrol;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import javax.ejb.Stateless;
import javax.faces.model.SelectItem;
import javax.inject.Inject;

import org.slf4j.Logger;

import com.clevel.selos.dao.master.BankBranchDAO;
import com.clevel.selos.dao.master.UserZoneDAO;
import com.clevel.selos.dao.working.AgreementInfoDAO;
import com.clevel.selos.dao.working.GuarantorInfoDAO;
import com.clevel.selos.dao.working.MortgageInfoCollOwnerDAO;
import com.clevel.selos.dao.working.MortgageInfoCollSubDAO;
import com.clevel.selos.dao.working.MortgageInfoCreditDAO;
import com.clevel.selos.dao.working.MortgageInfoDAO;
import com.clevel.selos.dao.working.MortgageSummaryDAO;
import com.clevel.selos.dao.working.NewCollateralSubDAO;
import com.clevel.selos.dao.working.PledgeInfoDAO;
import com.clevel.selos.dao.working.WorkCaseDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.AttorneyRelationType;
import com.clevel.selos.model.RadioValue;
import com.clevel.selos.model.db.master.BankBranch;
import com.clevel.selos.model.db.master.MortgageType;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.master.UserZone;
import com.clevel.selos.model.db.working.AgreementInfo;
import com.clevel.selos.model.db.working.Customer;
import com.clevel.selos.model.db.working.GuarantorInfo;
import com.clevel.selos.model.db.working.MortgageInfo;
import com.clevel.selos.model.db.working.MortgageInfoCollOwner;
import com.clevel.selos.model.db.working.MortgageInfoCollSub;
import com.clevel.selos.model.db.working.MortgageInfoCredit;
import com.clevel.selos.model.db.working.MortgageSummary;
import com.clevel.selos.model.db.working.NewCollateralCredit;
import com.clevel.selos.model.db.working.NewCollateralHead;
import com.clevel.selos.model.db.working.NewCollateralSub;
import com.clevel.selos.model.db.working.NewCollateralSubMortgage;
import com.clevel.selos.model.db.working.NewCollateralSubOwner;
import com.clevel.selos.model.db.working.NewCollateralSubRelated;
import com.clevel.selos.model.db.working.PledgeInfo;
import com.clevel.selos.model.db.working.WorkCase;
import com.clevel.selos.model.view.GuarantorInfoView;
import com.clevel.selos.model.view.MortgageInfoView;
import com.clevel.selos.model.view.MortgageSummaryView;
import com.clevel.selos.model.view.PledgeInfoView;
import com.clevel.selos.transform.GuarantorInfoTransform;
import com.clevel.selos.transform.MortgageInfoTransform;
import com.clevel.selos.transform.MortgageSummaryTransform;
import com.clevel.selos.transform.PledgeInfoTransform;

@Stateless
public class MortgageSummaryControl extends BusinessControl {
    private static final long serialVersionUID = 1949436898372853571L;

	@Inject
    @SELOS
    private Logger log;
    
    @Inject private MortgageSummaryDAO mortgageSummaryDAO;
    @Inject private AgreementInfoDAO agreementInfoDAO;
    @Inject private MortgageInfoDAO mortgageInfoDAO;
    @Inject private PledgeInfoDAO pledgeInfoDAO;
    @Inject private GuarantorInfoDAO guarantorInfoDAO;
    
    
    @Inject private MortgageSummaryTransform mortgageSummaryTransform;
    @Inject private MortgageInfoTransform mortgageInfoTransform;
    @Inject private PledgeInfoTransform pledgeInfoTransform;
    @Inject private GuarantorInfoTransform guarantorInfoTransform;
    
    @Inject
    private UserZoneDAO userZoneDAO;
    @Inject
    private BankBranchDAO bankBranchDAO;
    
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
    
    public List<MortgageInfoView> getMortgageInfoList(long workCaseId) {
    	if (workCaseId <= 0)
    		return Collections.emptyList();
    	List<MortgageInfoView> rtnDatas = new ArrayList<MortgageInfoView>();
    	List<MortgageInfo> datas = mortgageInfoDAO.findAllByWorkCaseId(workCaseId);
    	for (MortgageInfo data : datas) {
    		rtnDatas.add(mortgageInfoTransform.transformToView(data));
    	}
    	return rtnDatas;
    }
    public List<PledgeInfoView> getPledgeInfoList(long workCaseId) {
    	if (workCaseId <= 0)
    		return Collections.emptyList();
    	List<PledgeInfoView> rtnDatas = new ArrayList<PledgeInfoView>();
    	List<PledgeInfo> datas = pledgeInfoDAO.findAllByWorkCaseId(workCaseId);
    	for (PledgeInfo data : datas) {
    		rtnDatas.add(pledgeInfoTransform.transformToView(data));
    	}
    	return rtnDatas;
    }
    public List<GuarantorInfoView> getGuarantorInfoList(long workCaseId) {
    	if (workCaseId <= 0)
    		return Collections.emptyList();
    	List<GuarantorInfoView> rtnDatas = new ArrayList<GuarantorInfoView>();
    	List<GuarantorInfo> datas = guarantorInfoDAO.findAllByWorkCaseId(workCaseId);
    	for (GuarantorInfo data : datas) {
    		rtnDatas.add(guarantorInfoTransform.transformToView(data));
    	}
    	return rtnDatas;
    }
  
    @Inject private NewCollateralSubDAO newCollateralSubDAO;
    
    
    @Inject private WorkCaseDAO workCaseDAO;
    
    
    @Inject private MortgageInfoCollOwnerDAO mortgageInfoCollOwnerDAO;
    @Inject private MortgageInfoCreditDAO mortgageInfoCreditDAO;
    @Inject private MortgageInfoCollSubDAO mortgageInfoCollSubDAO;
    
    public void calculateMortgageSummary(long workCaseId) {
    	if (workCaseId <= 0)
    		return;
    	User user = getCurrentUser();
    	WorkCase workCase = workCaseDAO.findRefById(workCaseId);
    	
    	//calculate mortgage summary by grouping into 3 types (Mortgage, Pledge and Guarantor)
    	List<NewCollateralSub> subColleterals = newCollateralSubDAO.findForMortgageSummary(workCaseId);
    	HashMap<Long, NewCollateralSub> collateralMap = new HashMap<Long, NewCollateralSub>();
    	HashSet<Long> pledgeSet = new HashSet<Long>();
    	
    	
    	//Assumption
    	/*
    	 * Main Colleteral Sub = 
    	 * that SubRelated is join flag 
    	 */
    	
    	//Key = Sub Id (Join|Main) -> Value = Sub Id (Main)
    	HashMap<Long,Long> subMortgageMap = new HashMap<Long, Long>();
    	for (NewCollateralSub subColleteral : subColleterals) {
    		collateralMap.put(subColleteral.getId(),subColleteral);
    		
    		List<NewCollateralSubMortgage> mortgageCheckTypes = subColleteral.getNewCollateralSubMortgageList();
    		boolean isMortgage = false;
    		boolean isReferred = false;
    		
    		for (NewCollateralSubMortgage mortgageCheckType : mortgageCheckTypes) {
    			MortgageType type = mortgageCheckType.getMortgageType();
    			if (type.isPledgeFlag()) {
    				pledgeSet.add(subColleteral.getId());
    			}
    			if (type.isMortgageFlag()) {
    				isMortgage = true;
    				if (type.isReferredFlag())
    					isReferred = true;
    			}
    		}
    		
    		if (isMortgage) {
    			if (!isReferred) {
    				subMortgageMap.put(subColleteral.getId(), subColleteral.getId());
    				List<NewCollateralSubRelated> relateds = subColleteral.getNewCollateralSubRelatedList();
    				if (relateds != null && !relateds.isEmpty()) {
    					for (NewCollateralSubRelated related : relateds) {
    						subMortgageMap.put(related.getNewCollateralSubRelated().getId(), subColleteral.getId());
    					}
    				}
    			} else {
    				
    			}
    		}
    	}
    	
    	//Key = main sub id, value = list of collateral
    	HashMap<Long,ArrayList<NewCollateralSub>> mortgageGroup = new HashMap<Long, ArrayList<NewCollateralSub>>();
    	for (long subId : subMortgageMap.keySet()) {
    		long mainId = subMortgageMap.get(subId);
    		//check is it real main id ?
    		while (true) {
    			Long tmpId = subMortgageMap.get(mainId);
    			if (tmpId == null) //this main isn't sub of any colleteral but it's not approved yet
    				break;
    			if (tmpId != mainId) //The main id is sub of tmpId
    				mainId = tmpId;
    			else
    				break;
    		}
    		//validate mainId is approved ?
    		NewCollateralSub mainColleteralSub = collateralMap.get(mainId);
    		if (mainColleteralSub == null) //not approved yet
    			continue;
    		//validate subId is approved ?
    		NewCollateralSub subCollateralSub = collateralMap.get(subId);
    		if (subCollateralSub == null) //not approved yet
    			continue;
    		
    		ArrayList<NewCollateralSub> list = mortgageGroup.get(mainId);
    		if (list == null) {
    			list = new ArrayList<NewCollateralSub>();
    			mortgageGroup.put(mainId,list);
    		}
    		list.add(subCollateralSub);
    	}
    	
    	_processMortgageInfoData(mortgageGroup,user,workCase);
    }
    
    private void _processMortgageInfoData(HashMap<Long,ArrayList<NewCollateralSub>> mortgageGroup,
    		User user,WorkCase workCase) {
    	if (mortgageGroup.isEmpty())
    		return;
    	Date now = new Date();
    	Long[] mainIds = mortgageGroup.keySet().toArray(new Long[mortgageGroup.size()]);
    	Arrays.sort(mainIds);
    	for (Long mainId : mainIds) {
    		MortgageInfo mortgageInfo = new MortgageInfo();
    		mortgageInfo.setMortgageSigningDate(null);
    		mortgageInfo.setMortgageOSCompany(null);
    		mortgageInfo.setMortgageLandOffice(null);
    		//TODO
    		mortgageInfo.setMortgageType(null);
    		mortgageInfo.setMortgageOrder(0);
    		mortgageInfo.setAttorneyRequired(RadioValue.NA);
    		mortgageInfo.setAttorneyRelation(AttorneyRelationType.NA);
    		mortgageInfo.setCustomerAttorney(null);
    		mortgageInfo.setWorkCase(workCase);
    		mortgageInfo.setCreateBy(user);
    		mortgageInfo.setCreateDate(now);
    		mortgageInfo.setModifyBy(user);
    		mortgageInfo.setModifyDate(now);
    		
    		mortgageInfoDAO.save(mortgageInfo);
    		
    		BigDecimal mortgageAmount = new BigDecimal(0);
    		
    		HashSet<NewCollateralCredit> creditSet = new HashSet<NewCollateralCredit>();
    		HashSet<Customer> ownerSet = new HashSet<Customer>();
    		//add sub
    		List<NewCollateralSub> collaterals = mortgageGroup.get(mainId);
    		for (NewCollateralSub collateral : collaterals) {
    			if (collateral.getAppraisalValue() != null)
    				mortgageAmount =mortgageAmount.add(collateral.getAppraisalValue());
    			
    			MortgageInfoCollSub mortgageColl = new MortgageInfoCollSub();
    			mortgageColl.setMortgageInfo(mortgageInfo);
    			mortgageColl.setNewCollateralSub(collateral);
    			mortgageColl.setMain(collateral.getId() == mainId);
    			mortgageInfoCollSubDAO.save(mortgageColl);
    			
    			//Retrieve credit
    			NewCollateralHead head = collateral.getNewCollateralHead();
    			if (head == null)
    				continue;
    			List<NewCollateralCredit> credits = head.getNewCollateral().getNewCollateralCreditList();
    			if (credits != null && !credits.isEmpty()) {
    				for (NewCollateralCredit credit : credits) {
    					creditSet.add(credit);
    				}
    			}
    			
    			//Retrieve owner
    			List<NewCollateralSubOwner> owners =collateral.getNewCollateralSubOwnerList();
    			if (owners != null && !owners.isEmpty()) {
    				for (NewCollateralSubOwner owner : owners) {
    					if (owner.getCustomer() != null)
    						ownerSet.add(owner.getCustomer());
    				}
    			}
    		}
    		
    		//process credit and owner
    		for (NewCollateralCredit credit : creditSet) {
    			MortgageInfoCredit mortgageCredit = new MortgageInfoCredit();
    			mortgageCredit.setMortgageInfo(mortgageInfo);
    			mortgageCredit.setNewCreditDetail(credit.getNewCreditDetail());
    			mortgageInfoCreditDAO.save(mortgageCredit);
    		}
    		for (Customer owner : ownerSet) {
    			MortgageInfoCollOwner mortgageOwner = new MortgageInfoCollOwner();
    			mortgageOwner.setCustomer(owner);
    			mortgageOwner.setMortgageInfo(mortgageInfo);
    			mortgageOwner.setPoa(false);
    			mortgageInfoCollOwnerDAO.save(mortgageOwner);
    		}
    	}
    }
}
