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
    ExistingCreditDetailTransform existingCreditDetailTransform;
    @Inject
    private NewCollateralDAO newCollateralDAO;
    private List<NewCollateral> newCollateralList;
    private List<NewCollateralView> newCollateralViewList;

    public List<NewCollateral> transformsCollateralToModel(List<NewCollateralView> newCollateralViewList, NewCreditFacility newCreditFacility, User user , WorkCase workCase) {
        List<NewCollateral> newCollateralList = new ArrayList<NewCollateral>();
        NewCollateral newCollateral;

        for (NewCollateralView newCollateralView : newCollateralViewList) {
            newCollateral = new NewCollateral();
            newCollateral.setProposeType(ProposeType.P.type());
            if (newCollateralView.getId() != 0) {
                newCollateral.setCreateDate(newCollateralView.getCreateDate());
                newCollateral.setCreateBy(newCollateralView.getCreateBy());
            } else { // id = 0 create new
                newCollateral.setCreateDate(new Date());
                newCollateral.setCreateBy(user);
            }
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
            newCollateralList.add(newCollateral);
        }
        return newCollateralList;
    }

    public List<NewCollateralView> transformsCollateralToView(List<NewCollateral> newCollateralList) {
        List<NewCollateralView> newCollateralViewList = new ArrayList<NewCollateralView>();
        NewCollateralView newCollateralView;

        for (NewCollateral newCollateralDetail1 : newCollateralList) {
            newCollateralView = new NewCollateralView();
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
                proposeCreditDetailView.setProductProgram(tmp.getProductProgram());
                proposeCreditDetailView.setCreditFacility(tmp.getCreditType());
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
            proposeCreditDetailView.setProductProgram(existingCreditDetailView.getExistProductProgram());
            proposeCreditDetailView.setCreditFacility(existingCreditDetailView.getExistCreditType());
            proposeCreditDetailView.setLimit(existingCreditDetailView.getLimit());
            proposeCreditDetailViewList.add(proposeCreditDetailView);
            rowCount++;
        }

        return proposeCreditDetailViewList;
    }

    public NewCollateralHead transformCollateralHeadToModel(NewCollateralHeadView newCollateralHeadView, NewCollateral collateralDetail, User user) {

        NewCollateralHead collateralHeaderDetail = new NewCollateralHead();
        collateralHeaderDetail.setCreateDate(newCollateralHeadView.getCreateDate());
        collateralHeaderDetail.setCreateBy(newCollateralHeadView.getCreateBy());
        collateralHeaderDetail.setModifyDate(newCollateralHeadView.getModifyDate());
        collateralHeaderDetail.setModifyBy(user);
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
        collateralHeaderDetail.setNewCollateral(collateralDetail);

        return collateralHeaderDetail;
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
                    // transform to view???
                }
                newCollateralSubView.setRelatedWithList(relateList);
            }


            newCollateralSubViewList.add(newCollateralSubView);
        }

        return newCollateralSubViewList;
    }

    public NewCollateralSub transformCollateralSubToModel(NewCollateralSubView newCollateralSubView, NewCollateralHead collateralHeaderDetail, User user) {

        NewCollateralSub subCollateralDetail = new NewCollateralSub();

//        if (newCollateralSubView.getId() != 0) {
//            subCollateralDetail.setId(newCollateralSubView.getId());
//            subCollateralDetail.setCreateDate(newCollateralSubView.getCreateDate());
//            subCollateralDetail.setCreateBy(newCollateralSubView.getCreateBy());
//        } else { // id = 0 create new
//            subCollateralDetail.setCreateDate(new Date());
//            subCollateralDetail.setCreateBy(user);
//        }

        subCollateralDetail.setTitleDeed(newCollateralSubView.getTitleDeed());
        subCollateralDetail.setAppraisalValue(newCollateralSubView.getAppraisalValue());
        subCollateralDetail.setAddress(newCollateralSubView.getAddress());
        subCollateralDetail.setCollateralOwner(newCollateralSubView.getCollateralOwner());
        subCollateralDetail.setSubCollateralType(newCollateralSubView.getSubCollateralType());
        subCollateralDetail.setNewCollateralHead(collateralHeaderDetail);


        return subCollateralDetail;
    }

    public List<NewCollateral> transformToModel(final List<NewCollateralView> newCollateralViewList, final User user, final NewCreditFacility newCreditFacility){
        log.debug("-- transformToModel [NewCollateralList.size[{}]]", newCollateralViewList.size());
        newCollateralList = new ArrayList<NewCollateral>();
        NewCollateral model = null;
        for(NewCollateralView view : newCollateralViewList){
            model = new NewCollateral();
            log.debug("-- NewCollateralHead created");
            if(!Util.isZero(view.getId())){
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
            newCollateralList.add(model);
        }
        log.debug("--[RETURNED] NewCollateralList.size[{}]", newCollateralList.size());
        return newCollateralList;
    }

    public List<NewCollateral> transformToNewModel(final List<NewCollateralView> newCollateralViewList, final User user, final NewCreditFacility newCreditFacility){
        log.debug("-- transformToNewModel [NewCollateralList.size[{}]]", newCollateralViewList.size());
        newCollateralList = new ArrayList<NewCollateral>();
        NewCollateral model = null;
        for(NewCollateralView view : newCollateralViewList){
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
            newCollateralList.add(model);
        }
        log.debug("--[RETURNED] NewCollateralList.size[{}]", newCollateralList.size());
        return newCollateralList;
    }

    public List<NewCollateralView> transformToView(final List<NewCollateral> newCollateralList){
        log.debug("-- transformToView [NewCollateralList.size[{}]]", newCollateralList.size());
        newCollateralViewList = new ArrayList<NewCollateralView>();
        NewCollateralView view = null;
        for(NewCollateral model : newCollateralList){
            view = new NewCollateralView();
            log.debug("-- NewCollateralView created");
            if(!Util.isZero(model.getId())){
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
