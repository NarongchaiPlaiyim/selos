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

import com.clevel.selos.dao.master.BankAccountProductDAO;
import com.clevel.selos.dao.master.BankAccountPurposeDAO;
import com.clevel.selos.dao.master.BankBranchDAO;
import com.clevel.selos.dao.master.MortgageTypeDAO;
import com.clevel.selos.dao.master.UserZoneDAO;
import com.clevel.selos.dao.working.AgreementInfoDAO;
import com.clevel.selos.dao.working.GuarantorInfoDAO;
import com.clevel.selos.dao.working.MortgageInfoCollOwnerDAO;
import com.clevel.selos.dao.working.MortgageInfoCollSubDAO;
import com.clevel.selos.dao.working.MortgageInfoCreditDAO;
import com.clevel.selos.dao.working.MortgageInfoDAO;
import com.clevel.selos.dao.working.MortgageInfoMortgageDAO;
import com.clevel.selos.dao.working.MortgageSummaryDAO;
import com.clevel.selos.dao.working.NewCollateralSubDAO;
import com.clevel.selos.dao.working.NewGuarantorDetailDAO;
import com.clevel.selos.dao.working.OpenAccountCreditDAO;
import com.clevel.selos.dao.working.OpenAccountDAO;
import com.clevel.selos.dao.working.OpenAccountNameDAO;
import com.clevel.selos.dao.working.PledgeInfoDAO;
import com.clevel.selos.dao.working.WorkCaseDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.AttorneyRelationType;
import com.clevel.selos.model.ConfirmAccountType;
import com.clevel.selos.model.ProposeType;
import com.clevel.selos.model.RadioValue;
import com.clevel.selos.model.RequestAccountType;
import com.clevel.selos.model.db.master.BankAccountProduct;
import com.clevel.selos.model.db.master.BankAccountPurpose;
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
import com.clevel.selos.model.db.working.MortgageInfoMortgage;
import com.clevel.selos.model.db.working.MortgageSummary;
import com.clevel.selos.model.db.working.NewCollateralCredit;
import com.clevel.selos.model.db.working.NewCollateralHead;
import com.clevel.selos.model.db.working.NewCollateralSub;
import com.clevel.selos.model.db.working.NewCollateralSubMortgage;
import com.clevel.selos.model.db.working.NewCollateralSubOwner;
import com.clevel.selos.model.db.working.NewCollateralSubRelated;
import com.clevel.selos.model.db.working.NewGuarantorDetail;
import com.clevel.selos.model.db.working.OpenAccount;
import com.clevel.selos.model.db.working.OpenAccountCredit;
import com.clevel.selos.model.db.working.OpenAccountDeposit;
import com.clevel.selos.model.db.working.OpenAccountName;
import com.clevel.selos.model.db.working.OpenAccountPurpose;
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
    
    @Inject private MortgageInfoDAO mortgageInfoDAO;
    @Inject private PledgeInfoDAO pledgeInfoDAO;
    @Inject private GuarantorInfoDAO guarantorInfoDAO;
    @Inject private NewCollateralSubDAO newCollateralSubDAO;
    @Inject private WorkCaseDAO workCaseDAO;
    @Inject private NewGuarantorDetailDAO newGuarantorDetailDAO;
    @Inject private OpenAccountDAO openAccountDAO;
    @Inject private OpenAccountNameDAO openAccountNameDAO;
    @Inject private OpenAccountCreditDAO openAccountCreditDAO;
    
    @Inject private MortgageSummaryDAO mortgageSummaryDAO;
    @Inject private AgreementInfoDAO agreementInfoDAO;
    @Inject private MortgageInfoCollOwnerDAO mortgageInfoCollOwnerDAO;
    @Inject private MortgageInfoCreditDAO mortgageInfoCreditDAO;
    @Inject private MortgageInfoCollSubDAO mortgageInfoCollSubDAO;
    @Inject private MortgageInfoMortgageDAO mortgageInfoMortgageDAO;
    
    @Inject private MortgageSummaryTransform mortgageSummaryTransform;
    @Inject private MortgageInfoTransform mortgageInfoTransform;
    @Inject private PledgeInfoTransform pledgeInfoTransform;
    @Inject private GuarantorInfoTransform guarantorInfoTransform;
    
    @Inject private UserZoneDAO userZoneDAO;
    @Inject private BankBranchDAO bankBranchDAO;
    @Inject private MortgageTypeDAO mortgageTypeDAO;
   
    @Inject private BankAccountPurposeDAO bankAccountPurposeDAO;
    @Inject private BankAccountProductDAO bankAccountProductDAO;
    
    
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
    		rtnDatas.add(mortgageInfoTransform.transformToView(data,workCaseId));
    	}
    	return rtnDatas;
    }
    public List<PledgeInfoView> getPledgeInfoList(long workCaseId) {
    	if (workCaseId <= 0)
    		return Collections.emptyList();
    	List<PledgeInfoView> rtnDatas = new ArrayList<PledgeInfoView>();
    	List<PledgeInfo> datas = pledgeInfoDAO.findAllByWorkCaseId(workCaseId);
    	for (PledgeInfo data : datas) {
    		rtnDatas.add(pledgeInfoTransform.transformToView(data,workCaseId));
    	}
    	return rtnDatas;
    }
    public List<GuarantorInfoView> getGuarantorInfoList(long workCaseId) {
    	if (workCaseId <= 0)
    		return Collections.emptyList();
    	List<GuarantorInfoView> rtnDatas = new ArrayList<GuarantorInfoView>();
    	List<GuarantorInfo> datas = guarantorInfoDAO.findAllByWorkCaseId(workCaseId);
    	for (GuarantorInfo data : datas) {
    		rtnDatas.add(guarantorInfoTransform.transformToView(data,workCaseId));
    	}
    	return rtnDatas;
    }

    public MortgageSummaryView calculateMortgageSummary(long workCaseId) {
    	if (workCaseId <= 0)
    		return mortgageSummaryTransform.transformToView(null,null);
    	User user = getCurrentUser();
    	WorkCase workCase = workCaseDAO.findRefById(workCaseId);
    	
    	//calculate mortgage summary by grouping into 3 types (Mortgage, Pledge and Guarantor)
    	List<NewCollateralSub> subColleterals = newCollateralSubDAO.findForMortgageSummary(workCaseId);
    	
    	//For validate 
    	HashMap<Long, NewCollateralSub> collateralMap = new HashMap<Long, NewCollateralSub>();
    	
    	HashSet<Long> pledgeSet = new HashSet<Long>();

    	HashMap<Long,ArrayList<NewCollateralSub>> mortgageGroup = new HashMap<Long, ArrayList<NewCollateralSub>>();
    	HashMap<String,ArrayList<NewCollateralSub>> referredGroup = new HashMap<String, ArrayList<NewCollateralSub>>();
    	
    	// For calculate Main+Join , Key = collateral id, value = list of main coll id
    	HashMap<Long,ArrayList<Long>> joinMortgageList = new HashMap<Long, ArrayList<Long>>();
    	for (NewCollateralSub subColleteral : subColleterals) {
    		collateralMap.put(subColleteral.getId(),subColleteral);
    		
    		long collateralId = subColleteral.getId();
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
    				List<NewCollateralSubRelated> relateds = subColleteral.getNewCollateralSubRelatedList();
    				if (relateds != null && !relateds.isEmpty()) {
    					//it's sub
    					ArrayList<Long> list = new ArrayList<Long>();
    					for (NewCollateralSubRelated related : relateds) {
    						long mainCollId = related.getNewCollateralSubRelated().getId();
    						list.add(mainCollId);
    					}
    					joinMortgageList.put(collateralId, list);
    				} else {
    					//it's main
    					ArrayList<NewCollateralSub> list = new ArrayList<NewCollateralSub>();
    					list.add(subColleteral);
    					mortgageGroup.put(collateralId, list);
    				}
    			} else {
    				String key = _generateKeyForReferred(subColleteral);
    				ArrayList<NewCollateralSub> list = referredGroup.get(key);
    				if (list == null) {
    					list = new ArrayList<NewCollateralSub>();
    					referredGroup.put(key, list);
    				}
    				list.add(subColleteral);
    			}
    		}
    	}

    	_processMortgageGroup(collateralMap,mortgageGroup,joinMortgageList,referredGroup,
    			user,workCase);
    	_processGuarantorData(user,workCase);
    	_processPledgeData(collateralMap, pledgeSet, user, workCase);
    	
    	//Loading mortgage summary
    	MortgageSummary mortgage = null;
    	AgreementInfo agreement = null;
    	try {
    		mortgage = mortgageSummaryDAO.findByWorkCaseId(workCaseId);
    	} catch (Throwable e) {}
    	try {
    		agreement = agreementInfoDAO.findByWorkCaseId(workCaseId);
    	} catch (Throwable e) {}
    	
    	if (mortgage == null) {
    		mortgage = mortgageSummaryTransform.createMortgageSummary(user, workCase);
    		mortgageSummaryDAO.save(mortgage);
    	} else {
    		mortgageSummaryTransform.updateMortgageSummary(mortgage, user);
    		mortgageSummaryDAO.persist(mortgage);
    	}
    	if (agreement == null) {
    		agreement = mortgageSummaryTransform.creatAgreementInfo(null, workCase);
    		agreementInfoDAO.save(agreement);
    	} 
    	return mortgageSummaryTransform.transformToView(mortgage,agreement);
    }
    
    public void saveMortgageSummary(MortgageSummaryView view,long workCaseId) {
    	User user = getCurrentUser();
    	WorkCase workCase = workCaseDAO.findRefById(workCaseId);
    	if (view.getId() <= 0) {
    		MortgageSummary model = mortgageSummaryTransform.createMortgageSummary(user, workCase);
    		mortgageSummaryDAO.save(model);
    	} else {
    		MortgageSummary model = mortgageSummaryDAO.findById(view.getId());
    		mortgageSummaryTransform.updateMortgageSummary(model, user);
    		mortgageSummaryDAO.persist(model);
    	}
    	
    	if (view.getAgreementId() <= 0) {
    		AgreementInfo model = mortgageSummaryTransform.creatAgreementInfo(view, workCase);
    		agreementInfoDAO.save(model);
    	} else {
    		AgreementInfo model = agreementInfoDAO.findById(view.getAgreementId());
    		mortgageSummaryTransform.updateAgreementInfo(model, view);
    		agreementInfoDAO.persist(model);
    	}
    }
    
    /*
     * Private
     */
    private String _generateKeyForReferred(NewCollateralSub collateralSub) {
    	StringBuilder builder = new StringBuilder();
    	//Collateral Type
    	if (collateralSub.getCollateralTypeType() != null)
    		builder.append(collateralSub.getCollateralTypeType().getId());
    	else
    		builder.append(0);
    	builder.append("::");
    	if (collateralSub.getSubCollateralType() != null)
    		builder.append(collateralSub.getSubCollateralType().getId());
    	else
    		builder.append(0);
    	builder.append("&&");
    	
    	//Owner
    	List<NewCollateralSubOwner> owners = collateralSub.getNewCollateralSubOwnerList();
    	if (owners != null && !owners.isEmpty()) {
    		long[] ownerIds = new long[owners.size()];
	    	for (int i=0;i<ownerIds.length;i++) {
	    		Customer customer = owners.get(i).getCustomer();
	    		if (customer != null)
	    			ownerIds[i] = owners.get(i).getCustomer().getId();
	    		else
	    			ownerIds[i] = 0;
	    	}
	    	Arrays.sort(ownerIds);
	    	for (long ownerId : ownerIds) {
	    		builder.append(ownerId);
	    		builder.append("::");
	    	}
    	}
    	builder.append("&&");
    	
    	//Mortgage Type
    	List<NewCollateralSubMortgage> mortgageTypes = collateralSub.getNewCollateralSubMortgageList();
    	if (mortgageTypes != null && !mortgageTypes.isEmpty()) {
    		int[] typeIds = new int[mortgageTypes.size()];
    		for (int i=0;i<typeIds.length;i++) {
    			MortgageType type = mortgageTypes.get(i).getMortgageType();
    			if (type != null)
    				typeIds[i] = type.getId();
    			else
    				typeIds[i] = 0;
    		}
    		Arrays.sort(typeIds);
	    	for (int typeId : typeIds) {
	    		builder.append(typeId);
	    		builder.append("::");
	    	}
    	}
    	builder.append("&&");
    	
    	//Mortgage Related (Main ref)
    	List<NewCollateralSubRelated> relateds = collateralSub.getNewCollateralSubRelatedList();
    	if (relateds != null && !relateds.isEmpty()) {
    		long[] relatedIds = new long[relateds.size()];
	    	for (int i=0;i<relatedIds.length;i++) {
	    		NewCollateralSub collateralRelated = relateds.get(i).getNewCollateralSubRelated();
	    		if (collateralRelated != null)
	    			relatedIds[i] = collateralRelated.getId();
	    		else
	    			relatedIds[i] = 0;
	    	}
	    	Arrays.sort(relatedIds);
	    	for (long relatedId : relatedIds) {
	    		builder.append(relatedId);
	    		builder.append("::");
	    	}
    	}
    	builder.append("&&");
    	return "HASH::"+builder.toString().hashCode();
    }
    private void _findMainCollateralSub(long mainCollSubId,
    		HashMap<Long, NewCollateralSub> collateralMap,
    		HashMap<Long,ArrayList<NewCollateralSub>> mortgageGroup,
    		HashMap<Long,ArrayList<Long>> joinMortgageList,
    		HashSet<Long> mainCollSet,
    		HashSet<Long> checkRecursive) {
    	if (checkRecursive.contains(mainCollSubId))
    		return;
    	checkRecursive.add(mainCollSubId);
    	
    	if (!collateralMap.containsKey(mainCollSubId)) //Invalid main id
    		return;
    	
    	if (mortgageGroup.containsKey(mainCollSubId)) { //it's main
    		mainCollSet.add(mainCollSubId);
    		return;
    	}
    	//Find real main
    	ArrayList<Long> mainCollSubIdList = joinMortgageList.get(mainCollSubId);
    	if (mainCollSubIdList == null) //Invalid id (Dead link)
    		return;
    	
    	for (Long checkMainCollSubId : mainCollSubIdList) {
    		HashSet<Long> toCheck = new HashSet<Long>(checkRecursive);
    		_findMainCollateralSub(checkMainCollSubId, collateralMap, mortgageGroup, joinMortgageList, mainCollSet,toCheck);
    	}
    }
    
    private void _processMortgageInfo(String refKey,MortgageInfo mortgageInfo,List<NewCollateralSub> collaterals,User user,WorkCase workCase) {
    	Date now = new Date();
    	BigDecimal mortgageAmount = new BigDecimal(0);
		HashMap<Long,MortgageInfoCollSub> subMap = new HashMap<Long, MortgageInfoCollSub>();
		HashMap<Long,MortgageInfoCollOwner> ownerMap = new HashMap<Long, MortgageInfoCollOwner>();
		HashMap<Long,MortgageInfoCredit> creditMap = new HashMap<Long, MortgageInfoCredit>();

    	if (mortgageInfo == null) {
    		//It's new info
    		mortgageInfo = new MortgageInfo();
    		mortgageInfo.setMortgageSigningDate(null);
    		mortgageInfo.setMortgageOSCompany(null);
    		mortgageInfo.setMortgageLandOffice(null);
    		mortgageInfo.setMortgageOrder(0);
    		mortgageInfo.setMortgageRefKey(refKey);
    		mortgageInfo.setAttorneyRequired(RadioValue.NA);
    		mortgageInfo.setAttorneyRelation(AttorneyRelationType.NA);
    		mortgageInfo.setCustomerAttorney(null);
    		mortgageInfo.setWorkCase(workCase);
    		mortgageInfo.setCreateBy(user);
    		mortgageInfo.setCreateDate(now);
    		mortgageInfo.setModifyBy(user);
    		mortgageInfo.setModifyDate(now);
    		mortgageInfoDAO.save(mortgageInfo);
    	} else {
    		//Just update it
    		mortgageInfo.setModifyBy(user);
    		mortgageInfo.setModifyDate(now);
    		//Load current data of mortgageInfo
    		long infoId = mortgageInfo.getId();
    		List<MortgageInfoCollSub> mortgageSubs = mortgageInfoCollSubDAO.findAllByMortgageInfoId(infoId);
    		List<MortgageInfoCollOwner> mortgageOwners = mortgageInfoCollOwnerDAO.findAllByMortgageInfoId(infoId);
    		List<MortgageInfoCredit> mortgageCredits = mortgageInfoCreditDAO.findAllByMortgageInfoId(infoId);
    		
    		for (MortgageInfoCollSub sub : mortgageSubs)
    			subMap.put(sub.getNewCollateralSub().getId(),sub);
    		for (MortgageInfoCollOwner owner : mortgageOwners)
    			ownerMap.put(owner.getCustomer().getId(),owner);
    		for (MortgageInfoCredit credit : mortgageCredits)
    			creditMap.put(credit.getNewCollateralCredit().getId(),credit);    		
    	}
    	
    	HashSet<NewCollateralCredit> creditSet = new HashSet<NewCollateralCredit>();
		HashSet<Customer> ownerSet = new HashSet<Customer>();
		for (NewCollateralSub collateral : collaterals) {
			if (collateral.getMortgageValue() != null)
				mortgageAmount =mortgageAmount.add(collateral.getMortgageValue());
			
			MortgageInfoCollSub mortgageColl = subMap.get(collateral.getId());
			if (mortgageColl == null) {
    			mortgageColl = new MortgageInfoCollSub();
    			mortgageColl.setMortgageInfo(mortgageInfo);
    			mortgageColl.setNewCollateralSub(collateral);
    			mortgageColl.setMain(refKey.equals(Long.toString(collateral.getId())));
    			mortgageInfoCollSubDAO.save(mortgageColl);
			} else {
				subMap.remove(collateral.getId());
			}
			
			//Retrieve credit
			NewCollateralHead head = collateral.getNewCollateralHead();
			if (head != null) {
				List<NewCollateralCredit> credits = head.getNewCollateral().getNewCollateralCreditList();
				if (credits != null && !credits.isEmpty()) {
					for (NewCollateralCredit credit : credits) {
						creditSet.add(credit);
					}
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
		
		//process credit 
		for (NewCollateralCredit credit : creditSet) {
			MortgageInfoCredit mortgageCredit = creditMap.get(credit.getId());
			if (mortgageCredit == null) {
    			mortgageCredit = new MortgageInfoCredit();
    			mortgageCredit.setMortgageInfo(mortgageInfo);
    			mortgageCredit.setNewCollateralCredit(credit);
    			mortgageInfoCreditDAO.save(mortgageCredit);
			} else {
				creditMap.remove(credit.getId());
			}
		}
		
		//Process owner
		for (Customer owner : ownerSet) {
			MortgageInfoCollOwner mortgageOwner = ownerMap.get(owner.getId()); 
			if (mortgageOwner == null) {
				mortgageOwner = new MortgageInfoCollOwner();
    			mortgageOwner.setCustomer(owner);
    			mortgageOwner.setMortgageInfo(mortgageInfo);
    			mortgageOwner.setPoa(false);
    			mortgageInfoCollOwnerDAO.save(mortgageOwner);
			} else {
				ownerMap.remove(owner.getId());
			}
		}
		
		//process mortgage type
		//Main is always in index 0 , and refer is always the same then can use 0 as input
		List<MortgageInfoMortgage> mortgageTypeList = mortgageInfo.getMortgageTypeList();
		if (mortgageTypeList == null) {
			mortgageTypeList = new ArrayList<MortgageInfoMortgage>();
			mortgageInfo.setMortgageTypeList(mortgageTypeList);
		}
		HashMap<Integer,MortgageInfoMortgage> mortgageTypeMap = new HashMap<Integer,MortgageInfoMortgage>();
		for (MortgageInfoMortgage type : mortgageTypeList) {
			mortgageTypeMap.put(type.getMortgageType().getId(),type);
		}
		
		NewCollateralSub collateral = collaterals.get(0);
		List<NewCollateralSubMortgage> types = collateral.getNewCollateralSubMortgageList();
		for (NewCollateralSubMortgage type : types) {
			MortgageInfoMortgage mortgageType = mortgageTypeMap.get(type.getMortgageType().getId());
			if (mortgageType == null) {
				mortgageType = new MortgageInfoMortgage();
				mortgageType.setMortgageInfo(mortgageInfo);
				mortgageType.setMortgageType(type.getMortgageType());
				mortgageTypeList.add(mortgageType);	
			} else {
				mortgageTypeMap.remove(mortgageType);
			}
		}
		//remove
		for (Integer key : mortgageTypeMap.keySet()) {
			MortgageInfoMortgage mortgageType = mortgageTypeMap.get(key);
			mortgageTypeList.remove(mortgageType);
			mortgageType.setMortgageType(null);
			mortgageInfoMortgageDAO.delete(mortgageType);
		}
		mortgageInfo.setMortgageAmount(mortgageAmount);
		mortgageInfoDAO.persist(mortgageInfo);
		
		//Clean up credit, collsub ,owner
		for (Long key : creditMap.keySet())
			mortgageInfoCreditDAO.delete(creditMap.get(key));
		for (Long key : ownerMap.keySet())
			mortgageInfoCollOwnerDAO.delete(ownerMap.get(key));
		for (Long key : subMap.keySet())
			mortgageInfoCollSubDAO.delete(subMap.get(key));
    }
    
    
    private void _processMortgageGroup (HashMap<Long, NewCollateralSub> collateralMap,
    		HashMap<Long,ArrayList<NewCollateralSub>> mortgageGroup,
    		HashMap<Long,ArrayList<Long>> joinMortgageList,
    		HashMap<String,ArrayList<NewCollateralSub>> referredGroup,
    		User user, WorkCase workCase
    		) {
    
    	HashMap<String,MortgageInfo> currMortgageHash = new HashMap<String, MortgageInfo>(); 
    	List<MortgageInfo> currMortgageInfos = mortgageInfoDAO.findAllByWorkCaseId(workCase.getId());
    	for (MortgageInfo mortgageInfo : currMortgageInfos) {
    		currMortgageHash.put(mortgageInfo.getMortgageRefKey(),mortgageInfo);
    	}
    	
	    //calculate real main and attach to mortgageGroup
		for (Long joinCollSubId : joinMortgageList.keySet()) {
			HashSet<Long> mainCollSet = new HashSet<Long>();
			ArrayList<Long> mainCollSubIdList = joinMortgageList.get(joinCollSubId);
			for (Long mainCollSubId : mainCollSubIdList) {
				_findMainCollateralSub(mainCollSubId, collateralMap, mortgageGroup, joinMortgageList, mainCollSet,new HashSet<Long>());
			}
			
			if (mainCollSet.isEmpty()) //Invalid join
				continue;
			for (Long mainCollSubId : mainCollSet) {
				ArrayList<NewCollateralSub> list = mortgageGroup.get(mainCollSubId);
				list.add(collateralMap.get(joinCollSubId));
			}
		}
    	
		//Process Main+Join group
    	Long[] mainIds = mortgageGroup.keySet().toArray(new Long[mortgageGroup.size()]);
    	Arrays.sort(mainIds);
    	for (Long mainId : mainIds) {
    		List<NewCollateralSub> collaterals = mortgageGroup.get(mainId);
    		MortgageInfo mortgageInfo = currMortgageHash.remove(mainId.toString());
    		_processMortgageInfo(mainId.toString(), mortgageInfo, collaterals, user, workCase);
    	}
    	
    	//Process Refer group
    	for (String referKey : referredGroup.keySet()) {
    		List<NewCollateralSub> collaterals = referredGroup.get(referKey);
    		MortgageInfo mortgageInfo = currMortgageHash.remove(referKey);
    		_processMortgageInfo(referKey, mortgageInfo, collaterals, user, workCase);
    	}
    	
    	//Cleanup unused mortgage
    	for (String key : currMortgageHash.keySet()) {
    		MortgageInfo info = currMortgageHash.get(key);
    		long id = info.getId();
    		mortgageInfoCollSubDAO.deleteByMortgageInfoId(id);
    		mortgageInfoCollOwnerDAO.deleteByMortgageInfoId(id);
    		mortgageInfoCreditDAO.deleteByMortgageInfoId(id);
    		mortgageInfoDAO.delete(info);
    	}
    }
    
    private void _processGuarantorData(User user,WorkCase workCase) {
    	List<MortgageType> mortgageTypes = mortgageTypeDAO.findMortgageTypeForGuarantor();
    	MortgageType defaultType = null;
    	if (mortgageTypes != null && !mortgageTypes.isEmpty()) {
    		defaultType = mortgageTypes.get(0);
    		
    	}
    	List<NewGuarantorDetail> newGuarantors = newGuarantorDetailDAO.findGuarantorByProposeType(workCase.getId(), ProposeType.A);
    	List<GuarantorInfo> guarantorInfos = guarantorInfoDAO.findAllByWorkCaseId(workCase.getId());
    	
    	HashMap<Long,GuarantorInfo> guarantorMap = new HashMap<Long, GuarantorInfo>();
    	for (GuarantorInfo info : guarantorInfos) {
    		guarantorMap.put(info.getNewGuarantorDetail().getId(), info);
    	}
    	
    	for (NewGuarantorDetail newGuarantor : newGuarantors) {
    		MortgageType type = null;
    		if (newGuarantor.getGuarantorName() != null)
    			type = defaultType;
    		
    		GuarantorInfo guarantorInfo = guarantorMap.get(newGuarantor.getId());
    		if (guarantorInfo == null) {
    			guarantorInfo = guarantorInfoTransform.createGuaratorInfo(user, workCase);
    			guarantorInfo.setNewGuarantorDetail(newGuarantor);
    			guarantorInfo.setGuarantorType(type);
    			guarantorInfoDAO.save(guarantorInfo);
    		} else {
    			guarantorMap.remove(newGuarantor.getId());
    			
    			guarantorInfo.setModifyBy(user);
    			guarantorInfo.setModifyDate(new Date());
    			guarantorInfo.setGuarantorType(type);
    			guarantorInfoDAO.persist(guarantorInfo);
    		}
    	}
    	
    	//Clean up
    	for (Object key : guarantorMap.keySet()) {
    		guarantorInfoDAO.delete(guarantorMap.get(key));
    	}
    }
    
    private void _processPledgeData(HashMap<Long, NewCollateralSub> collateralMap,HashSet<Long> pledgeSet,User user, WorkCase workCase) {
    	BankAccountPurpose defaultPurpose = bankAccountPurposeDAO.getDefaultProposeForPledge();
    	List<OpenAccount> accounts = openAccountDAO.findByWorkCaseId(workCase.getId());
    	List<PledgeInfo> pledgeInfos = pledgeInfoDAO.findAllByWorkCaseId(workCase.getId());
    	
    	HashMap<Long, PledgeInfo> pledgeMap = new HashMap<Long, PledgeInfo>();
    	for (PledgeInfo info : pledgeInfos) {
    		pledgeMap.put(info.getNewCollateralSub().getId(), info);
    	}
    	HashMap<Long,OpenAccount> accountMap = new HashMap<Long, OpenAccount>();
    	for (OpenAccount account : accounts) {
    		accountMap.put(account.getId(), account);
    	}
    	
    	for (Long subCollateralId : pledgeSet) {
    		NewCollateralSub collateral = collateralMap.get(subCollateralId);
    		PledgeInfo pledgeInfo = pledgeMap.get(subCollateralId);
    		if (pledgeInfo == null) {
    			OpenAccount account = new OpenAccount();
        		account.setWorkCase(workCase);
        		account.setTerm(null);
        		account.setNumberOfDep(0);
        		updateOpenAccountData(account, collateral, defaultPurpose);
        		openAccountDAO.save(account);
        		
    			pledgeInfo = new PledgeInfo();
    			pledgeInfo.setCreateBy(user);
    			pledgeInfo.setCreateDate(new Date());
    			pledgeInfo.setNewCollateralSub(collateral);
    			pledgeInfo.setWorkCase(workCase);
    			pledgeInfo.setTotalHoldAmount(new BigDecimal(0));
    			pledgeInfo.setOpenAccount(account);
    			
    			pledgeInfo.setModifyBy(user);
    			pledgeInfo.setModifyDate(new Date());
    			pledgeInfo.setPledgeType(collateral.getNewCollateralSubMortgageList().get(0).getMortgageType());
    			pledgeInfo.setPledgeAmount(collateral.getMortgageValue());
    			pledgeInfoDAO.save(pledgeInfo);
    		} else {
    			pledgeMap.remove(subCollateralId);
    			
    			pledgeInfo.setModifyBy(user);
    			pledgeInfo.setModifyDate(new Date());
    			pledgeInfo.setPledgeType(collateral.getNewCollateralSubMortgageList().get(0).getMortgageType());
    			pledgeInfo.setPledgeAmount(collateral.getMortgageValue());
    			
    			OpenAccount account = pledgeInfo.getOpenAccount();
    			if (account == null) {
    				account = new OpenAccount();
            		account.setWorkCase(workCase);
            		account.setTerm(null);
            		account.setNumberOfDep(0);
            		updateOpenAccountData(account, collateral, defaultPurpose);
            		openAccountDAO.save(account);
    			} else {
    				updateOpenAccountData(account, collateral, defaultPurpose);
            		openAccountDAO.persist(account);
            		
            		accountMap.remove(account.getId());
    			}
    		}
    	}
    	
    	//clean pledge info
    	for (Long key : pledgeMap.keySet()) {
    		PledgeInfo info = pledgeMap.get(key);
    		pledgeInfoDAO.delete(info);
    	}
    	
    	//mark open account from pledge = false
    	for (Long key : accountMap.keySet()) {
    		OpenAccount account = accountMap.get(key);
    		if (!account.isPledgeAccount())
    			continue;
    		account.setPledgeAccount(false);
    		List<OpenAccountCredit> credits = account.getOpenAccountCreditList();
    		if (credits != null && !credits.isEmpty()) {
    			for (OpenAccountCredit credit : credits) {
    				credit.setFromPledge(false);
    			}
    		}
    		openAccountDAO.persist(account);
    	}
    }
	private void updateOpenAccountData(OpenAccount account,NewCollateralSub collateral,BankAccountPurpose defaultPurpose) {
		account.setRequestType(RequestAccountType.EXISTING);
		account.setAccountNumber(collateral.getTitleDeed());
		account.setBankBranch(null);
		BankAccountProduct accountProduct = bankAccountProductDAO.findByCollateral(collateral.getCollateralTypeType(), collateral.getSubCollateralType());
		account.setBankAccountProduct(accountProduct);
		if (accountProduct != null)
			account.setBankAccountType(accountProduct.getBankAccountType());
		account.setConfirmOpenAccount(ConfirmAccountType.NOT_OPEN);
		account.setPledgeAccount(true);
		
		//Process Open Account Name
		List<OpenAccountName> accountNames = account.getOpenAccountNameList();
		if (accountNames == null) {
			accountNames = new ArrayList<OpenAccountName>();
			account.setOpenAccountNameList(accountNames);
		}
		HashMap<Long,OpenAccountName> accountNameMap = new HashMap<Long,OpenAccountName> ();
		for (OpenAccountName name : accountNames)
			accountNameMap.put(name.getCustomer().getId(),name);
		List<NewCollateralSubOwner> owners = collateral.getNewCollateralSubOwnerList();
		for (NewCollateralSubOwner owner : owners) {
			if (owner.getCustomer() == null)
				continue;
			OpenAccountName accountName = accountNameMap.get(owner.getCustomer().getId());
			if (accountName == null) {
				accountName = new OpenAccountName();
				accountName.setCustomer(owner.getCustomer());
				accountName.setOpenAccount(account);
				accountName.setFromPledge(true);
				accountNames.add(accountName);
			} else {
				accountName.setFromPledge(true);
				accountNameMap.remove(owner.getCustomer().getId());
			}
		}
		//remove
		for (Long key : accountNameMap.keySet()) {
			OpenAccountName accountName = accountNameMap.get(key);
			if (!accountName.isFromPledge())
				continue;
			
			//detach from OpenAccount
			accountName.setOpenAccount(null);
			accountNames.remove(accountName);
			openAccountNameDAO.delete(accountName);
		}
		
		
		//Process Account Purpose
		List<OpenAccountPurpose> purposes = account.getOpenAccountPurposeList();
		if (purposes == null) {
			purposes = new ArrayList<OpenAccountPurpose>();
			account.setOpenAccountPurposeList(purposes);
		}
		if (defaultPurpose != null) {
			boolean foundDefault = false;
			for (OpenAccountPurpose purpose : purposes) {
				if (purpose.getAccountPurpose().getId() == defaultPurpose.getId()) {
					foundDefault = true;
					break;
				}
			}
			if (!foundDefault) {
				OpenAccountPurpose purpose = new OpenAccountPurpose();
				purpose.setAccountPurpose(defaultPurpose);
				purpose.setOpenAccount(account);
				purposes.add(purpose);
			}
		}
		
		//Process Account Credit
		List<OpenAccountCredit> credits = account.getOpenAccountCreditList();
		if (credits == null) {
			credits = new ArrayList<OpenAccountCredit>();
			account.setOpenAccountCreditList(credits);
		}
		List<NewCollateralCredit> collCredits = collateral.getNewCollateralHead().getNewCollateral().getNewCollateralCreditList();
		HashMap<String,OpenAccountCredit> creditHash = new HashMap<String, OpenAccountCredit>();
		for (OpenAccountCredit credit : credits) {
			if (credit.getExistingCreditDetail()  != null) {
				creditHash.put("E::"+credit.getExistingCreditDetail().getId(), credit);
			} else if (credit.getNewCreditDetail() != null) {
				creditHash.put("N::"+credit.getNewCreditDetail().getId(), credit);
			}
		}
		
		//new and update
		for (NewCollateralCredit collCredit : collCredits) {
			if (collCredit.getExistingCreditDetail() != null) {
				String key = "E::"+collCredit.getExistingCreditDetail().getId();
				OpenAccountCredit credit = creditHash.get(key);
				if (credit == null) {
					credit = new OpenAccountCredit();
					credit.setOpenAccount(account);
					credit.setExistingCreditDetail(collCredit.getExistingCreditDetail());
					credits.add(credit);
				} else {
					creditHash.remove(key);
				}
				credit.setFromPledge(true);
			} else if (collCredit.getNewCreditDetail() != null) {
				String key = "N::"+collCredit.getNewCreditDetail().getId();
				OpenAccountCredit credit = creditHash.get(key);
				if (credit == null) {
					credit = new OpenAccountCredit();
					credit.setOpenAccount(account);
					credit.setNewCreditDetail(collCredit.getNewCreditDetail());
					credits.add(credit);
				} else {
					creditHash.remove(key);
				}
				credit.setFromPledge(true);
			}
		}
		//remove
		for (String key : creditHash.keySet()) {
			OpenAccountCredit credit = creditHash.get(key);
			if (!credit.isFromPledge()) //keep only created by user
				continue;
			//detach from OpenAccount
			credit.setOpenAccount(null);
			credits.remove(credit);
			openAccountCreditDAO.delete(credit);
		}
		
		//Process Account DEP
		List<OpenAccountDeposit> deposits = account.getOpenAccountDepositList();
		if (deposits == null) {
			deposits = new ArrayList<OpenAccountDeposit>();
			account.setOpenAccountDepositList(deposits);
		}
			
	}
}
