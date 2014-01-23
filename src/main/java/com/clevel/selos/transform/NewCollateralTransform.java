package com.clevel.selos.transform;

import com.clevel.selos.dao.master.CollateralTypeDAO;
import com.clevel.selos.dao.master.MortgageTypeDAO;
import com.clevel.selos.dao.master.SubCollateralTypeDAO;
import com.clevel.selos.dao.working.*;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.MortgageType;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.working.*;
import com.clevel.selos.model.view.*;
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


    public List<NewCollateral> transformsCollateralToModel(List<NewCollateralView> newCollateralViewList, NewCreditFacility newCreditFacility, User user) {
        List<NewCollateral> newCollateralList = new ArrayList<NewCollateral>();
        NewCollateral newCollateral;

        for (NewCollateralView newCollateralView : newCollateralViewList) {
            newCollateral = new NewCollateral();

            if (newCollateralView.getId() != 0) {
                newCollateral.setId(newCollateralView.getId());
                newCollateral.setCreateDate(newCollateralView.getCreateDate());
                newCollateral.setCreateBy(newCollateralView.getCreateBy());
            } else { // id = 0 create new
                newCollateral.setCreateDate(new Date());
                newCollateral.setCreateBy(user);
            }
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

            List<NewCollateralCredit> newCollateralRelCredits = newCollateralCreditDAO.getListCollRelationByNewCollateral(newCollateralDetail1);
            if (newCollateralRelCredits != null) {
                List<NewCreditDetail> newCreditDetailList = new ArrayList<NewCreditDetail>();

                for (NewCollateralCredit newCollateralCredit : newCollateralRelCredits) {
                    newCreditDetailList.add(newCollateralCredit.getNewCreditDetail());
                }

                List<NewCreditDetailView> newCreditDetailViewList = newCreditDetailTransform.transformToView(newCreditDetailList);
                newCollateralView.setNewCreditDetailViewList(newCreditDetailViewList);

            }

            List<NewCollateralHead> newCollateralHeadDetails = newCollateralHeadDAO.findByNewCollateralDetail(newCollateralDetail1);
            if (newCollateralDetail1.getNewCollateralHeadList() != null) {
                List<NewCollateralHeadView> newCollateralHeadViews = transformCollateralHeadToView(newCollateralHeadDetails);
                newCollateralView.setNewCollateralHeadViewList(newCollateralHeadViews);
            }


            newCollateralViewList.add(newCollateralView);
        }
        return newCollateralViewList;
    }

    public List<NewCollateralHead> transformCollateralHeadToModel(List<NewCollateralHeadView> newCollateralHeadViewList, NewCollateral collateralDetail, User user) {

        List<NewCollateralHead> collateralHeaderDetailList = new ArrayList<NewCollateralHead>();
        NewCollateralHead collateralHeaderDetail;

        for (NewCollateralHeadView newCollateralHeadView : newCollateralHeadViewList) {
            collateralHeaderDetail = new NewCollateralHead();

            if (newCollateralHeadView.getId() != 0) {
                collateralHeaderDetail.setId(newCollateralHeadView.getId());
                collateralHeaderDetail.setCreateDate(newCollateralHeadView.getCreateDate());
                collateralHeaderDetail.setCreateBy(newCollateralHeadView.getCreateBy());
            } else { // id = 0 create new
                collateralHeaderDetail.setCreateDate(new Date());
                collateralHeaderDetail.setCreateBy(user);
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
            collateralHeaderDetail.setNewCollateral(collateralDetail);
            collateralHeaderDetailList.add(collateralHeaderDetail);
        }

        return collateralHeaderDetailList;
    }

    public NewCollateralHead transformCollateralHeadToModel(NewCollateralHeadView newCollateralHeadView, NewCollateral collateralDetail, User user) {

        NewCollateralHead collateralHeaderDetail = new NewCollateralHead();

        if (newCollateralHeadView.getId() != 0) {
            collateralHeaderDetail.setId(newCollateralHeadView.getId());
            collateralHeaderDetail.setCreateDate(newCollateralHeadView.getCreateDate());
            collateralHeaderDetail.setCreateBy(newCollateralHeadView.getCreateBy());
        } else { // id = 0 create new
            collateralHeaderDetail.setCreateDate(new Date());
            collateralHeaderDetail.setCreateBy(user);
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
        collateralHeaderDetail.setNewCollateral(collateralDetail);

        return collateralHeaderDetail;
    }

    public List<NewCollateralHeadView> transformCollateralHeadToView(List<NewCollateralHead> collateralHeaderDetailList) {

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

    public List<NewCollateralSub> transformCollateralSubToModel(List<NewCollateralSubView> newCollateralSubViewList, NewCollateralHead collateralHeaderDetail, User user) {

        List<NewCollateralSub> subCollateralDetailList = new ArrayList<NewCollateralSub>();
        NewCollateralSub subCollateralDetail;

        for (NewCollateralSubView newCollateralSubView : newCollateralSubViewList) {
            subCollateralDetail = new NewCollateralSub();

            if (newCollateralSubView.getId() != 0) {
                subCollateralDetail.setId(newCollateralSubView.getId());
                subCollateralDetail.setCreateDate(newCollateralSubView.getCreateDate());
                subCollateralDetail.setCreateBy(newCollateralSubView.getCreateBy());
            } else { // id = 0 create new
                subCollateralDetail.setCreateDate(new Date());
                subCollateralDetail.setCreateBy(user);
            }

            subCollateralDetail.setTitleDeed(newCollateralSubView.getTitleDeed());
            subCollateralDetail.setAppraisalValue(newCollateralSubView.getAppraisalValue());
            subCollateralDetail.setAddress(newCollateralSubView.getAddress());
            subCollateralDetail.setCollateralOwner(newCollateralSubView.getCollateralOwner());
            subCollateralDetail.setSubCollateralType(newCollateralSubView.getSubCollateralType());
            subCollateralDetail.setNewCollateralHead(collateralHeaderDetail);
            subCollateralDetailList.add(subCollateralDetail);
        }

        return subCollateralDetailList;
    }

    public List<NewCollateralSubView> transformCollateralSubToView(List<NewCollateralSub> subCollateralDetailList) {

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

        if (newCollateralSubView.getId() != 0) {
            subCollateralDetail.setId(newCollateralSubView.getId());
            subCollateralDetail.setCreateDate(newCollateralSubView.getCreateDate());
            subCollateralDetail.setCreateBy(newCollateralSubView.getCreateBy());
        } else { // id = 0 create new
            subCollateralDetail.setCreateDate(new Date());
            subCollateralDetail.setCreateBy(user);
        }

        subCollateralDetail.setTitleDeed(newCollateralSubView.getTitleDeed());
        subCollateralDetail.setAppraisalValue(newCollateralSubView.getAppraisalValue());
        subCollateralDetail.setAddress(newCollateralSubView.getAddress());
        subCollateralDetail.setCollateralOwner(newCollateralSubView.getCollateralOwner());
        subCollateralDetail.setSubCollateralType(newCollateralSubView.getSubCollateralType());
        subCollateralDetail.setNewCollateralHead(collateralHeaderDetail);


        return subCollateralDetail;
    }
}
