package com.clevel.selos.transform;

import com.clevel.selos.dao.master.CollateralTypeDAO;
import com.clevel.selos.dao.master.MortgageTypeDAO;
import com.clevel.selos.dao.master.SubCollateralTypeDAO;
import com.clevel.selos.dao.working.*;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.ProposeType;
import com.clevel.selos.model.db.master.MortgageType;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.working.*;
import com.clevel.selos.model.view.*;
import com.clevel.selos.util.Util;
import org.joda.time.DateTime;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NewCollateralTransform extends Transform {
    @Inject
    @SELOS
    Logger log;
    @Inject
    CustomerTransform customerTransform;
    @Inject
    CustomerDAO customerDAO;
    @Inject
    SubCollateralTypeDAO subCollateralTypeDAO;
    @Inject
    CollateralTypeDAO collateralTypeDAO;
    @Inject
    BasicInfoDAO basicInfoDAO;
    @Inject
    NewCollateralCreditDAO newCollateralCreditDAO;
    @Inject
    NewCreditDetailTransform newCreditDetailTransform;
    @Inject
    NewCollateralHeadDAO newCollateralHeadDAO;
    @Inject
    NewCollateralSubDAO newCollateralSubDAO;
    @Inject
    NewCollateralSubOwnerDAO newCollateralSubOwnerDAO;
    @Inject
    NewCollateralSubMortgageDAO newCollateralSubMortgageDAO;
    @Inject
    MortgageTypeDAO mortgageTypeDAO;
    @Inject
    NewCollateralSubRelatedDAO newCollateralSubRelatedDAO;
    @Inject
    private NewCollateralHeadTransform newCollateralHeadTransform;
    @Inject
    private NewCollateralCreditTransform newCollateralCreditTransform;
    @Inject
    ExistingCreditDetailTransform existingCreditDetailTransform;
    @Inject
    private NewCollateralDAO newCollateralDAO;
    private List<NewCollateral> newCollateralList;
    private List<NewCollateralView> newCollateralViewList;

    public List<NewCollateral> transformsCollateralToModel(List<NewCollateralView> newCollateralViewList, NewCreditFacility newCreditFacility, User user, WorkCase workCase) {
        List<NewCollateral> newCollateralList = new ArrayList<NewCollateral>();
        NewCollateral newCollateral;

        for (NewCollateralView newCollateralView : newCollateralViewList) {
            log.debug("Start... transformToModel ::: newCollateralView : {}", newCollateralView);
            newCollateral = new NewCollateral();

            if (newCollateralView.getId() != 0) {
                newCollateral = newCollateralDAO.findById(newCollateralView.getId());
                newCollateral.setModifyDate(DateTime.now().toDate());
                newCollateral.setModifyBy(user);
            } else { // id = 0 create new
                newCollateral.setCreateDate(DateTime.now().toDate());
                newCollateral.setCreateBy(user);
            }
            newCollateral.setComs(Util.isTrue(newCollateralView.isComs()));
            newCollateral.setProposeType(ProposeType.P);
            newCollateral.setWorkCase(workCase);
            newCollateral.setJobID(newCollateralView.getJobID());
            newCollateral.setAadDecision(newCollateralView.getAadDecision());
            newCollateral.setAadDecisionReason(newCollateralView.getAadDecisionReason());
            newCollateral.setAadDecisionReasonDetail(newCollateralView.getAadDecisionReasonDetail());
            newCollateral.setAppraisalDate(newCollateralView.getAppraisalDate());
            newCollateral.setBdmComments(newCollateralView.getBdmComments());
            newCollateral.setMortgageCondition(newCollateralView.getMortgageCondition());
            newCollateral.setMortgageConditionDetail(newCollateralView.getMortgageConditionDetail());
            newCollateral.setTypeOfUsage(newCollateralView.getTypeOfUsage());
            newCollateral.setUsage(newCollateralView.getUsage());
            newCollateral.setUwDecision(newCollateralView.getUwDecision());
            newCollateral.setUwRemark(newCollateralView.getUwRemark());
            newCollateral.setNewCreditFacility(newCreditFacility);
            newCollateral.setPremiumAmount(newCollateralView.getPremiumAmount());

            if (Util.safetyList(newCollateralView.getProposeCreditDetailViewList()).size() > 0) {
                List<NewCollateralCredit> newCollateralCreditList = newCollateralCreditTransform.transformsToModelForCollateral(newCollateralView.getProposeCreditDetailViewList(), newCreditFacility.getNewCreditDetailList(), newCollateral, user);
                newCollateral.setNewCollateralCreditList(newCollateralCreditList);
            }

            if (Util.safetyList(newCollateralView.getNewCollateralHeadViewList()).size() > 0) {
                List<NewCollateralHead> newCollateralHeadList = new ArrayList<NewCollateralHead>();
                for (NewCollateralHeadView newCollateralHeadView : newCollateralView.getNewCollateralHeadViewList()) {
                    //--- Transform for Collateral Head ---//
                    NewCollateralHead newCollateralHead = transformCollateralHeadToModel(newCollateralHeadView, newCollateral, user);
                    newCollateralHeadList.add(newCollateralHead);
                }
                newCollateral.setNewCollateralHeadList(newCollateralHeadList);
            }

            log.debug("End... transformToModel ::: newCollateral : {}", newCollateral);
            newCollateralList.add(newCollateral);
        }
        return newCollateralList;
    }

    public NewCollateralHead transformCollateralHeadToModel(NewCollateralHeadView newCollateralHeadView, NewCollateral collateralDetail, User user) {
        NewCollateralHead collateralHeaderDetail = new NewCollateralHead();
        log.debug("Start... transformCollateralHeadToModel ::: newCollateralHeadView : {}", newCollateralHeadView);
        if (newCollateralHeadView.getId() != 0) {
            collateralHeaderDetail = newCollateralHeadDAO.findById(newCollateralHeadView.getId());
            collateralHeaderDetail.setModifyDate(newCollateralHeadView.getModifyDate());
            collateralHeaderDetail.setModifyBy(user);
        } else {
            collateralHeaderDetail.setCreateDate(newCollateralHeadView.getCreateDate());
            collateralHeaderDetail.setCreateBy(newCollateralHeadView.getCreateBy());
        }

        collateralHeaderDetail.setHeadCollType(newCollateralHeadView.getHeadCollType());
        collateralHeaderDetail.setPotential(newCollateralHeadView.getPotentialCollateral());
        collateralHeaderDetail.setCollateralLocation(newCollateralHeadView.getCollateralLocation());
        collateralHeaderDetail.setTitleDeed(newCollateralHeadView.getTitleDeed());
        collateralHeaderDetail.setCollTypePercentLTV(newCollateralHeadView.getCollTypePercentLTV());
        collateralHeaderDetail.setExistingCredit(newCollateralHeadView.getExistingCredit());
        collateralHeaderDetail.setInsuranceCompany(newCollateralHeadView.getInsuranceCompany());
        collateralHeaderDetail.setAppraisalValue(newCollateralHeadView.getAppraisalValue());
        collateralHeaderDetail.setModifyBy(newCollateralHeadView.getModifyBy());
        collateralHeaderDetail.setModifyDate(newCollateralHeadView.getModifyDate());

        if (newCollateralHeadView.getNewCollateralSubViewList().size() > 0) {
            List<NewCollateralSub> newCollateralSubList = transformCollateralSubToModel(newCollateralHeadView, collateralHeaderDetail, user);
            collateralHeaderDetail.setNewCollateralSubList(newCollateralSubList);
        }

        collateralHeaderDetail.setNewCollateral(collateralDetail);
        log.debug("End... transformCollateralHeadToModel ::: collateralHeaderDetail : {}", collateralHeaderDetail);

        return collateralHeaderDetail;
    }

    public List<NewCollateralSub> transformCollateralSubToModel(NewCollateralHeadView newCollateralHeadView, NewCollateralHead newCollateralHead, User user) {
        //--- Transform for Collateral Sub ---//
        List<NewCollateralSub> newCollateralSubList = new ArrayList<NewCollateralSub>();
        NewCollateralSub newCollateralSub;

        for (NewCollateralSubView newCollateralSubView : newCollateralHeadView.getNewCollateralSubViewList()) {
            newCollateralSub = new NewCollateralSub();

            if (newCollateralSubView.getId() != 0) {
                newCollateralSub = newCollateralSubDAO.findById(newCollateralSubView.getId());
                newCollateralSub.setCreateBy(newCollateralSubView.getCreateBy());
            } else { // id = 0 create new
                newCollateralSub.setCreateDate(new Date());
                newCollateralSub.setCreateBy(user);
            }

            newCollateralSub.setTitleDeed(newCollateralSubView.getTitleDeed());
            newCollateralSub.setAppraisalValue(newCollateralSubView.getAppraisalValue());
            newCollateralSub.setAddress(newCollateralSubView.getAddress());
            newCollateralSub.setCollateralOwner(newCollateralSubView.getCollateralOwner());
            newCollateralSub.setCollateralOwnerAAD(newCollateralSubView.getCollateralOwnerAAD());
            newCollateralSub.setSubCollateralType(newCollateralSubView.getSubCollateralType());

//            if (newCollateralSubView.getMortgageList() != null) {
//                List<NewCollateralSubMortgage> newCollateralSubMortgageList = new ArrayList<NewCollateralSubMortgage>();
//                newCollateralSub.setNewCollateralSubMortgageList(newCollateralSubMortgageList);
//                NewCollateralSubMortgage newCollateralSubMortgage;
//                for (MortgageType mortgageType : newCollateralSubView.getMortgageList()) {
//                    MortgageType mortgage = mortgageTypeDAO.findById(mortgageType.getId());
//                    newCollateralSubMortgage = new NewCollateralSubMortgage();
//                    newCollateralSubMortgage.setMortgageType(mortgage);
//                    newCollateralSubMortgage.setNewCollateralSub(newCollateralSub);
//                    newCollateralSubMortgageList.add(newCollateralSubMortgage);
//                }
////                newCollateralSub.setNewCollateralSubMortgageList(newCollateralSubMortgageList);
//            }

//            if (newCollateralSubView.getCollateralOwnerUWList() != null) {
//                List<NewCollateralSubOwner> newCollateralSubOwnerList = new ArrayList<NewCollateralSubOwner>();
//                newCollateralSub.setNewCollateralSubOwnerList(newCollateralSubOwnerList);
//                for (CustomerInfoView customerInfoView : newCollateralSubView.getCollateralOwnerUWList()) {
//                    NewCollateralSubOwner newCollateralSubOwner = new NewCollateralSubOwner();
//                    Customer customer = customerDAO.findById(customerInfoView.getId());
//                    newCollateralSubOwner.setCustomer(customer);
//                    newCollateralSubOwner.setNewCollateralSub(newCollateralSub);
//                    newCollateralSubOwnerList.add(newCollateralSubOwner);
//                }
////                newCollateralSub.setNewCollateralSubOwnerList(newCollateralSubOwnerList);
//            }

//            if (newCollateralSubView.getRelatedWithList() != null) {
//                List<NewCollateralSubRelated> newCollateralSubRelatedList = new ArrayList<NewCollateralSubRelated>();
//                newCollateralSub.setNewCollateralSubRelatedList(newCollateralSubRelatedList);
//                NewCollateralSubRelated newCollateralSubRelate;
//                for (NewCollateralSubView relatedView : newCollateralSubView.getRelatedWithList()) {
//                    log.debug("relatedView.getId() ::: {} ", relatedView.getId());
//                    NewCollateralSub relatedDetail = newCollateralSubDAO.findById(relatedView.getId());
//                    log.debug("relatedDetail.getId() ::: {} ", relatedDetail.getId());
//                    newCollateralSubRelate = new NewCollateralSubRelated();
//                    newCollateralSubRelate.setNewCollateralSubRelated(relatedDetail);
//                    newCollateralSubRelate.setNewCollateralSub(newCollateralSub);
//                    newCollateralSubRelatedList.add(newCollateralSubRelate);
//                }
//
////                newCollateralSub.setNewCollateralSubRelatedList(newCollateralSubRelatedList);
//            }

            newCollateralSub.setNewCollateralHead(newCollateralHead);
            newCollateralSubList.add(newCollateralSub);
        }

        return newCollateralSubList;
    }

    public List<NewCollateralView> transformsCollateralToView(List<NewCollateral> newCollateralList) {
        List<NewCollateralView> newCollateralViewList = new ArrayList<NewCollateralView>();
        NewCollateralView newCollateralView;

        for (NewCollateral newCollateralDetail1 : newCollateralList) {
            newCollateralView = new NewCollateralView();
            newCollateralView.setProposeType(newCollateralDetail1.getProposeType());
            newCollateralView.setComs(Util.isTrue(newCollateralDetail1.getComs()));
            newCollateralView.setId(newCollateralDetail1.getId());
            newCollateralView.setCreateDate(newCollateralDetail1.getCreateDate());
            newCollateralView.setCreateBy(newCollateralDetail1.getCreateBy());
            newCollateralView.setCreateDate(newCollateralDetail1.getCreateDate());
            newCollateralView.setCreateBy(newCollateralDetail1.getCreateBy());
            newCollateralView.setJobID(newCollateralDetail1.getJobID());
            newCollateralView.setAadDecision(newCollateralDetail1.getAadDecision());
            newCollateralView.setAadDecisionReason(newCollateralDetail1.getAadDecisionReason());
            newCollateralView.setAadDecisionReasonDetail(newCollateralDetail1.getAadDecisionReasonDetail());
            newCollateralView.setAppraisalDate(newCollateralDetail1.getAppraisalDate());
            newCollateralView.setBdmComments(newCollateralDetail1.getBdmComments());
            newCollateralView.setMortgageCondition(newCollateralDetail1.getMortgageCondition());
            newCollateralView.setMortgageConditionDetail(newCollateralDetail1.getMortgageConditionDetail());
            newCollateralView.setTypeOfUsage(newCollateralDetail1.getTypeOfUsage());
            newCollateralView.setUsage(newCollateralDetail1.getUsage());
            newCollateralView.setUwDecision(newCollateralDetail1.getUwDecision());
            newCollateralView.setUwRemark(newCollateralDetail1.getUwRemark());
            newCollateralView.setPremiumAmount(newCollateralDetail1.getPremiumAmount());

            List<NewCollateralCredit> newCollateralRelCredits = newCollateralCreditDAO.getListCollRelationByNewCollateral(newCollateralDetail1);
            List<NewCreditDetail> newCreditDetailList = new ArrayList<NewCreditDetail>();
            List<ExistingCreditDetail> existingCreditDetailList = new ArrayList<ExistingCreditDetail>();

            for (NewCollateralCredit newCollateralCredit : newCollateralRelCredits) {
                if (newCollateralCredit.getExistingCreditDetail() != null) {
                    log.info("newGuarantorCredit.getExistingCreditDetail :: {}", newCollateralCredit.getExistingCreditDetail().getId());
                    existingCreditDetailList.add(newCollateralCredit.getExistingCreditDetail());
                } else if (newCollateralCredit.getNewCreditDetail() != null) {
                    log.info("newGuarantorCredit.getNewCreditDetail :: {}", newCollateralCredit.getNewCreditDetail().getId());
                    newCreditDetailList.add(newCollateralCredit.getNewCreditDetail());
                }
            }
            log.info("newCreditDetailList Guarantor:: {}", newCreditDetailList.size());
            log.info("getExistingCreditDetail Guarantor:: {}", existingCreditDetailList.size());
            List<ProposeCreditDetailView> proposeCreditDetailViewList = proposeCreditDetailTransform(newCreditDetailList, existingCreditDetailList);
            newCollateralView.setProposeCreditDetailViewList(proposeCreditDetailViewList);

            List<NewCollateralHead> newCollateralHeadDetails = newCollateralHeadDAO.findByNewCollateral(newCollateralDetail1);
            if (newCollateralDetail1.getNewCollateralHeadList() != null) {
                List<NewCollateralHeadView> newCollateralHeadViews = transformCollateralHeadToView(newCollateralHeadDetails);
                newCollateralView.setNewCollateralHeadViewList(newCollateralHeadViews);
            }

            newCollateralViewList.add(newCollateralView);
        }
        return newCollateralViewList;
    }

    public List<ProposeCreditDetailView> proposeCreditDetailTransform(List<NewCreditDetail> newCreditDetailList, List<ExistingCreditDetail> existingCreditDetailList) {
        log.info("proposeCreditDetailTransform :: newCreditDetailList size :: {}", newCreditDetailList.size());
        log.info("proposeCreditDetailTransform :: existingCreditDetailList size :: {}", existingCreditDetailList.size());

        List<NewCreditDetailView> newCreditDetailViewList = newCreditDetailTransform.transformToView(newCreditDetailList);
        // todo: find credit existing and propose in this workCase
        List<ProposeCreditDetailView> proposeCreditDetailViewList = new ArrayList<ProposeCreditDetailView>();
        ProposeCreditDetailView proposeCreditDetailView;
        int rowCount = 1;

        if (newCreditDetailViewList != null && newCreditDetailViewList.size() > 0) {
            for (NewCreditDetailView tmp : newCreditDetailViewList) {
                proposeCreditDetailView = new ProposeCreditDetailView();
                proposeCreditDetailView.setSeq(tmp.getSeq());
                proposeCreditDetailView.setId(rowCount);
                proposeCreditDetailView.setTypeOfStep("N");
                proposeCreditDetailView.setAccountName(tmp.getAccountName());
                proposeCreditDetailView.setAccountNumber(tmp.getAccountNumber());
                proposeCreditDetailView.setAccountSuf(tmp.getAccountSuf());
                proposeCreditDetailView.setRequestType(tmp.getRequestType());
                proposeCreditDetailView.setProductProgramView(tmp.getProductProgramView());
                proposeCreditDetailView.setCreditFacilityView(tmp.getCreditTypeView());
                proposeCreditDetailView.setLimit(tmp.getLimit());
                proposeCreditDetailView.setGuaranteeAmount(tmp.getGuaranteeAmount());
                proposeCreditDetailViewList.add(proposeCreditDetailView);
                rowCount++;
            }
        }

        rowCount = newCreditDetailViewList.size() > 0 ? newCreditDetailViewList.size() + 1 : rowCount;

        List<ExistingCreditDetailView> existingCreditDetailViewList = existingCreditDetailTransform.transformsToView(existingCreditDetailList);

        for (ExistingCreditDetailView existingCreditDetailView : existingCreditDetailViewList) {
            proposeCreditDetailView = new ProposeCreditDetailView();
            proposeCreditDetailView.setSeq((int) existingCreditDetailView.getId());
            proposeCreditDetailView.setId(rowCount);
            proposeCreditDetailView.setTypeOfStep("E");
            proposeCreditDetailView.setAccountName(existingCreditDetailView.getAccountName());
            proposeCreditDetailView.setAccountNumber(existingCreditDetailView.getAccountNumber());
            proposeCreditDetailView.setAccountSuf(existingCreditDetailView.getAccountSuf());
            proposeCreditDetailView.setProductProgramView(existingCreditDetailView.getExistProductProgramView());
            proposeCreditDetailView.setCreditFacilityView(existingCreditDetailView.getExistCreditTypeView());
            proposeCreditDetailView.setLimit(existingCreditDetailView.getLimit());
            proposeCreditDetailViewList.add(proposeCreditDetailView);
            rowCount++;
        }

        return proposeCreditDetailViewList;
    }

    public List<NewCollateralHead> transformCollateralHeadToModel(List<NewCollateralHeadView> newCollateralHeadViewList, NewCollateral collateralDetail, User user) {
        List<NewCollateralHead> newCollateralHeadList = new ArrayList<NewCollateralHead>();
        NewCollateralHead newCollateralHead;

        for (NewCollateralHeadView newCollateralHeadView : newCollateralHeadViewList) {
            newCollateralHead = new NewCollateralHead();
            newCollateralHead.setCreateDate(newCollateralHeadView.getCreateDate());
            newCollateralHead.setCreateBy(newCollateralHeadView.getCreateBy());
            newCollateralHead.setModifyDate(newCollateralHeadView.getModifyDate());
            newCollateralHead.setModifyBy(user);
            newCollateralHead.setHeadCollType(newCollateralHeadView.getHeadCollType());
            newCollateralHead.setPotential(newCollateralHeadView.getPotentialCollateral());
            newCollateralHead.setCollateralLocation(newCollateralHeadView.getCollateralLocation());
            newCollateralHead.setTitleDeed(newCollateralHeadView.getTitleDeed());
            newCollateralHead.setCollTypePercentLTV(newCollateralHeadView.getCollTypePercentLTV());
            newCollateralHead.setExistingCredit(newCollateralHeadView.getExistingCredit());
            newCollateralHead.setInsuranceCompany(newCollateralHeadView.getInsuranceCompany());
            newCollateralHead.setAppraisalValue(newCollateralHeadView.getAppraisalValue());
            newCollateralHead.setModifyBy(newCollateralHeadView.getModifyBy());
            newCollateralHead.setModifyDate(newCollateralHeadView.getModifyDate());
            newCollateralHead.setNewCollateral(collateralDetail);
            newCollateralHeadList.add(newCollateralHead);
        }
        return newCollateralHeadList;
    }

    public List<NewCollateralHeadView> transformCollateralHeadToView(List<NewCollateralHead> collateralHeaderDetailList) {
        log.info("transformCollateralHeadToView ::collateralHeaderDetailList.size :: {}", collateralHeaderDetailList.size());
        List<NewCollateralHeadView> newCollateralHeadViewList = new ArrayList<NewCollateralHeadView>();
        NewCollateralHeadView newCollateralHeadView;

        for (NewCollateralHead collateralHeaderDetail : collateralHeaderDetailList) {
            newCollateralHeadView = new NewCollateralHeadView();
            newCollateralHeadView.setId(collateralHeaderDetail.getId());
            newCollateralHeadView.setCreateBy(collateralHeaderDetail.getCreateBy());
            newCollateralHeadView.setCreateDate(collateralHeaderDetail.getCreateDate());
            newCollateralHeadView.setModifyBy(collateralHeaderDetail.getModifyBy());
            newCollateralHeadView.setModifyDate(collateralHeaderDetail.getModifyDate());
            newCollateralHeadView.setHeadCollType(collateralHeaderDetail.getHeadCollType());
            newCollateralHeadView.setPotentialCollateral(collateralHeaderDetail.getPotential());
            newCollateralHeadView.setCollateralLocation(collateralHeaderDetail.getCollateralLocation());
            newCollateralHeadView.setTitleDeed(collateralHeaderDetail.getTitleDeed());
            newCollateralHeadView.setCollTypePercentLTV(collateralHeaderDetail.getCollTypePercentLTV());
            newCollateralHeadView.setExistingCredit(collateralHeaderDetail.getExistingCredit());
            newCollateralHeadView.setInsuranceCompany(collateralHeaderDetail.getInsuranceCompany());
            newCollateralHeadView.setAppraisalValue(collateralHeaderDetail.getAppraisalValue());
            newCollateralHeadView.setModifyBy(collateralHeaderDetail.getModifyBy());
            newCollateralHeadView.setModifyDate(collateralHeaderDetail.getModifyDate());
            newCollateralHeadView.setCreateBy(collateralHeaderDetail.getCreateBy());
            newCollateralHeadView.setCreateDate(collateralHeaderDetail.getCreateDate());
            newCollateralHeadView.setModifyBy(collateralHeaderDetail.getModifyBy());
            newCollateralHeadView.setModifyDate(collateralHeaderDetail.getModifyDate());

            List<NewCollateralSub> newCollateralSubDetails = newCollateralSubDAO.getAllNewSubCollateral(collateralHeaderDetail);
            if (newCollateralSubDetails.size() > 0) {
                List<NewCollateralSubView> newCollateralSubViews = transformCollateralSubToView(newCollateralSubDetails);
                newCollateralHeadView.setNewCollateralSubViewList(newCollateralSubViews);
            }

            newCollateralHeadViewList.add(newCollateralHeadView);
        }

        return newCollateralHeadViewList;
    }

    public List<NewCollateralSubView> transformCollateralSubToView(List<NewCollateralSub> subCollateralDetailList) {
        log.info(" transformCollateralSubToView ::subCollateralDetailList.size :: {} ", subCollateralDetailList.size());
        List<NewCollateralSubView> newCollateralSubViewList = new ArrayList<NewCollateralSubView>();
        NewCollateralSubView newCollateralSubView;

        for (NewCollateralSub subCollateralDetail : subCollateralDetailList) {
            newCollateralSubView = new NewCollateralSubView();
            newCollateralSubView.setId(subCollateralDetail.getId());
            newCollateralSubView.setTitleDeed(subCollateralDetail.getTitleDeed());
            newCollateralSubView.setAppraisalValue(subCollateralDetail.getAppraisalValue());
            newCollateralSubView.setAddress(subCollateralDetail.getAddress());
            newCollateralSubView.setCollateralOwnerAAD(subCollateralDetail.getCollateralOwnerAAD());
            newCollateralSubView.setCollateralOwner(subCollateralDetail.getCollateralOwner());
            newCollateralSubView.setSubCollateralType(subCollateralDetail.getSubCollateralType());
            newCollateralSubView.setCreateBy(subCollateralDetail.getCreateBy());
            newCollateralSubView.setCreateDate(subCollateralDetail.getCreateDate());
            newCollateralSubView.setModifyBy(subCollateralDetail.getModifyBy());
            newCollateralSubView.setModifyDate(subCollateralDetail.getModifyDate());

            List<NewCollateralSubOwner> newCollateralSubCustomerList = newCollateralSubOwnerDAO.getListNewCollateralSubCustomer(subCollateralDetail);
            List<CustomerInfoView> newCollOwnerViewList = transformCustomerInfoView(newCollateralSubCustomerList);
            newCollateralSubView.setCollateralOwnerUWList(newCollOwnerViewList);
            List<CustomerInfoView> collateralOwnerUWList = new ArrayList<CustomerInfoView>();
            if (newCollateralSubCustomerList != null) {
                for (NewCollateralSubOwner newCollateralSubCustomer : newCollateralSubCustomerList) {
                    log.info("newCollateralSubCustomer id ::{}",newCollateralSubCustomer.getId());
                    CustomerInfoView customer = customerTransform.transformToView(newCollateralSubCustomer.getCustomer());
                    collateralOwnerUWList.add(customer);
                }
                newCollateralSubView.setCollateralOwnerUWList(collateralOwnerUWList);
            }

            List<NewCollateralSubMortgage> newCollateralSubMortgages = newCollateralSubMortgageDAO.getListNewCollateralSubMortgage(subCollateralDetail);
            List<MortgageType> mortgageTypes = new ArrayList<MortgageType>();
            if (newCollateralSubMortgages != null) {
                for (NewCollateralSubMortgage newCollateralSubMortgage : newCollateralSubMortgages) {
                    log.info("newCollateralSubMortgage id ::{}",newCollateralSubMortgage.getId());
                    MortgageType mortgageType = mortgageTypeDAO.findById(newCollateralSubMortgage.getMortgageType().getId());
                    mortgageTypes.add(mortgageType);
                }
                newCollateralSubView.setMortgageList(mortgageTypes);
            }

            List<NewCollateralSubRelated> newCollateralSubRelateList = newCollateralSubRelatedDAO.getListNewCollateralSubRelate(subCollateralDetail);
            List<NewCollateralSubView> relateList = new ArrayList<NewCollateralSubView>();
            if (newCollateralSubRelateList != null) {
                for (NewCollateralSubRelated newCollateralSubRelate : newCollateralSubRelateList) {
                    log.info("newCollateralSubRelate id ::{}",newCollateralSubRelate.getId());
                    NewCollateralSub newCollateralSub = newCollateralSubDAO.findById(newCollateralSubRelate.getId());
                    NewCollateralSubView newCollSubView = transformCollateralSubToView(newCollateralSub);
                    relateList.add(newCollSubView);
                }
                newCollateralSubView.setRelatedWithList(relateList);
            }


            newCollateralSubViewList.add(newCollateralSubView);
        }

        return newCollateralSubViewList;
    }

    public List<CustomerInfoView> transformCustomerInfoView(List<NewCollateralSubOwner> newCollateralSubCustomerList){
        List<CustomerInfoView> collateralOwnerUWList = new ArrayList<CustomerInfoView>();
            if (newCollateralSubCustomerList != null) {
                for (NewCollateralSubOwner newCollateralSubCustomer : newCollateralSubCustomerList) {
                    log.info("newCollateralSubCustomer id ::{}",newCollateralSubCustomer.getId());
                    CustomerInfoView customer = customerTransform.transformToView(newCollateralSubCustomer.getCustomer());
                    collateralOwnerUWList.add(customer);
                }
            }
        return  collateralOwnerUWList;
    }

    public NewCollateralSubView transformCollateralSubToView(NewCollateralSub subCollateralDetail) {
        log.info(" transformCollateralSubToView :: subCollateralDetail :: {} ", subCollateralDetail.getId());

            NewCollateralSubView newCollateralSubView = new NewCollateralSubView();
            newCollateralSubView.setId(subCollateralDetail.getId());
            newCollateralSubView.setTitleDeed(subCollateralDetail.getTitleDeed());
            newCollateralSubView.setAppraisalValue(subCollateralDetail.getAppraisalValue());
            newCollateralSubView.setAddress(subCollateralDetail.getAddress());
            newCollateralSubView.setCollateralOwnerAAD(subCollateralDetail.getCollateralOwnerAAD());
            newCollateralSubView.setCollateralOwner(subCollateralDetail.getCollateralOwner());
            newCollateralSubView.setSubCollateralType(subCollateralDetail.getSubCollateralType());
            newCollateralSubView.setCreateBy(subCollateralDetail.getCreateBy());
            newCollateralSubView.setCreateDate(subCollateralDetail.getCreateDate());
            newCollateralSubView.setModifyBy(subCollateralDetail.getModifyBy());
            newCollateralSubView.setModifyDate(subCollateralDetail.getModifyDate());

            List<NewCollateralSubOwner> newCollateralSubCustomerList = newCollateralSubOwnerDAO.getListNewCollateralSubCustomer(subCollateralDetail);
            List<CustomerInfoView> collateralOwnerUWList = new ArrayList<CustomerInfoView>();
            if (newCollateralSubCustomerList != null) {
                for (NewCollateralSubOwner newCollateralSubCustomer : newCollateralSubCustomerList) {
                    CustomerInfoView customer = customerTransform.transformToView(newCollateralSubCustomer.getCustomer());
                    collateralOwnerUWList.add(customer);
                }
                newCollateralSubView.setCollateralOwnerUWList(collateralOwnerUWList);
            }

            List<NewCollateralSubMortgage> newCollateralSubMortgages = newCollateralSubMortgageDAO.getListNewCollateralSubMortgage(subCollateralDetail);
            List<MortgageType> mortgageTypes = new ArrayList<MortgageType>();
            if (newCollateralSubMortgages != null) {
                for (NewCollateralSubMortgage newCollateralSubMortgage : newCollateralSubMortgages) {
                    MortgageType mortgageType = mortgageTypeDAO.findById(newCollateralSubMortgage.getMortgageType().getId());
                    mortgageTypes.add(mortgageType);
                }
                newCollateralSubView.setMortgageList(mortgageTypes);
            }

            List<NewCollateralSubRelated> newCollateralSubRelateList = newCollateralSubRelatedDAO.getListNewCollateralSubRelate(subCollateralDetail);
            List<NewCollateralSubView> relateList = new ArrayList<NewCollateralSubView>();
            if (newCollateralSubRelateList != null) {
                for (NewCollateralSubRelated newCollateralSubRelate : newCollateralSubRelateList) {
                    NewCollateralSub newCollateralSub = newCollateralSubDAO.findById(newCollateralSubRelate.getId());
                    NewCollateralSubView newCollSubView = transformCollateralSubToView(newCollateralSub);
                    relateList.add(newCollSubView);
                }
                newCollateralSubView.setRelatedWithList(relateList);
            }

        return newCollateralSubView;
    }

    public List<NewCollateral> transformToModel(final List<NewCollateralView> newCollateralViewList, final User user, final NewCreditFacility newCreditFacility) {
        log.debug("-- transformToModel [NewCollateralViewList.size[{}]]", newCollateralViewList.size());
        newCollateralList = new ArrayList<NewCollateral>();
        NewCollateral model = null;
        for (NewCollateralView view : newCollateralViewList) {
            model = new NewCollateral();
            log.debug("-- NewCollateralHead created");
            if (!Util.isZero(view.getId())) {
                model.setId(view.getId());
                log.debug("-- NewCollateralHead.id[{}]", model.getId());
            } else {
                model.setCreateDate(DateTime.now().toDate());
                model.setCreateBy(user);
            }
            model.setAppraisalDate(view.getAppraisalDate());
            model.setJobID(view.getJobID());
            model.setAadDecision(view.getAadDecision());
            model.setAadDecisionReason(view.getAadDecisionReason());
            model.setAadDecisionReasonDetail(view.getAadDecisionReasonDetail());
            model.setUsage(view.getUsage());
            model.setTypeOfUsage(view.getTypeOfUsage());
            model.setUwDecision(view.getUwDecision());
            model.setUwRemark(view.getUwRemark());
            model.setMortgageCondition(view.getMortgageCondition());
            model.setMortgageConditionDetail(view.getMortgageConditionDetail());
            model.setBdmComments(view.getBdmComments());
            model.setCreateBy(view.getCreateBy());
            model.setCreateDate(view.getCreateDate());
            model.setNewCollateralHeadList(newCollateralHeadTransform.transformToModel(Util.safetyList(view.getNewCollateralHeadViewList()), user));
            model.setModifyBy(user);
            model.setModifyDate(DateTime.now().toDate());
            model.setNewCreditFacility(newCreditFacility);
            model.setPremiumAmount(view.getPremiumAmount());
            newCollateralList.add(model);
        }
        log.debug("--[RETURNED] NewCollateralList.size[{}]", newCollateralList.size());
        return newCollateralList;
    }

    public List<NewCollateral> transformToNewModel(final List<NewCollateralView> newCollateralViewList, final User user, final NewCreditFacility newCreditFacility) {
        log.debug("-- transformToNewModel [NewCollateralList.size[{}]]", newCollateralViewList.size());
        newCollateralList = new ArrayList<NewCollateral>();
        NewCollateral model = null;
        for (NewCollateralView view : newCollateralViewList) {
            model = new NewCollateral();
            log.debug("-- NewCollateral created");
            model.setCreateDate(DateTime.now().toDate());
            model.setCreateBy(user);
            model.setAppraisalDate(view.getAppraisalDate());
            model.setJobID(view.getJobID());
            model.setAadDecision(view.getAadDecision());
            model.setAadDecisionReason(view.getAadDecisionReason());
            model.setAadDecisionReasonDetail(view.getAadDecisionReasonDetail());
            model.setUsage(view.getUsage());
            model.setTypeOfUsage(view.getTypeOfUsage());
            model.setUwDecision(view.getUwDecision());
            model.setUwRemark(view.getUwRemark());
            model.setMortgageCondition(view.getMortgageCondition());
            model.setMortgageConditionDetail(view.getMortgageConditionDetail());
            model.setBdmComments(view.getBdmComments());
            model.setCreateBy(view.getCreateBy());
            model.setCreateDate(view.getCreateDate());
            model.setNewCollateralHeadList(newCollateralHeadTransform.transformToNewModel(Util.safetyList(view.getNewCollateralHeadViewList()), user));
            model.setModifyBy(user);
            model.setModifyDate(DateTime.now().toDate());
            model.setNewCreditFacility(newCreditFacility);
            model.setPremiumAmount(view.getPremiumAmount());
            newCollateralList.add(model);
        }
        log.debug("--[RETURNED] NewCollateralList.size[{}]", newCollateralList.size());
        return newCollateralList;
    }

    public List<NewCollateralView> transformToView(final List<NewCollateral> newCollateralList) {
        log.debug("-- transformToView [NewCollateralList.size[{}]]", newCollateralList.size());
        newCollateralViewList = new ArrayList<NewCollateralView>();
        NewCollateralView view = null;
        for (NewCollateral model : newCollateralList) {
            view = new NewCollateralView();
            log.debug("-- NewCollateralView created");
            if (!Util.isZero(model.getId())) {
                view.setId(model.getId());
                log.debug("-- NewCollateralView.id[{}]", view.getId());
            } else {
                view.setCreateDate(model.getCreateDate());
                view.setCreateBy(model.getCreateBy());
            }
            view.setAppraisalDate(model.getAppraisalDate());
            view.setJobID(model.getJobID());
            view.setAadDecision(model.getAadDecision());
            view.setAadDecisionReason(model.getAadDecisionReason());
            view.setAadDecisionReasonDetail(model.getAadDecisionReasonDetail());
            view.setUsage(model.getUsage());
            view.setTypeOfUsage(model.getTypeOfUsage());
            view.setUwDecision(model.getUwDecision());
            view.setUwRemark(model.getUwRemark());
            view.setMortgageCondition(model.getMortgageCondition());
            view.setMortgageConditionDetail(model.getMortgageConditionDetail());
            view.setBdmComments(model.getBdmComments());
            view.setCreateBy(model.getCreateBy());
            view.setCreateDate(model.getCreateDate());
            view.setNewCollateralHeadViewList(newCollateralHeadTransform.transformToView(Util.safetyList(model.getNewCollateralHeadList())));
            view.setModifyDate(model.getModifyDate());
            view.setModifyBy(model.getModifyBy());
            view.setPremiumAmount(model.getPremiumAmount());
            newCollateralViewList.add(view);
            //model.setNewCollateralCreditList();
            /*view.setProposeType();
            view.setAppraisalRequest()
            view.setNewCreditFacility();*/
        }
        log.debug("--[RETURNED] NewCollateralViewList.size[{}]", newCollateralViewList.size());
        return newCollateralViewList;
    }

}
