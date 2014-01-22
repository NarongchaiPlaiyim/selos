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

public class NewCollateralInfoTransform extends Transform {
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


    public List<NewCollateral> transformsCollateralToModel(List<NewCollateralInfoView> newCollateralInfoViewList, NewCreditFacility newCreditFacility, User user) {
        List<NewCollateral> newCollateralList = new ArrayList<NewCollateral>();
        NewCollateral newCollateral;

        for (NewCollateralInfoView newCollateralInfoView : newCollateralInfoViewList) {
            newCollateral = new NewCollateral();

            if (newCollateralInfoView.getId() != 0) {
                newCollateral.setId(newCollateralInfoView.getId());
                newCollateral.setCreateDate(newCollateralInfoView.getCreateDate());
                newCollateral.setCreateBy(newCollateralInfoView.getCreateBy());
            } else { // id = 0 create new
                newCollateral.setCreateDate(new Date());
                newCollateral.setCreateBy(user);
            }
            newCollateral.setJobID(newCollateralInfoView.getJobID());
            newCollateral.setAadDecision(newCollateralInfoView.getAadDecision());
            newCollateral.setAadDecisionReason(newCollateralInfoView.getAadDecisionReason());
            newCollateral.setAadDecisionReasonDetail(newCollateralInfoView.getAadDecisionReasonDetail());
            newCollateral.setAppraisalDate(newCollateralInfoView.getAppraisalDate());
            newCollateral.setBdmComments(newCollateralInfoView.getBdmComments());
            newCollateral.setMortgageCondition(newCollateralInfoView.getMortgageCondition());
            newCollateral.setMortgageConditionDetail(newCollateralInfoView.getMortgageConditionDetail());
            newCollateral.setTypeOfUsage(newCollateralInfoView.getTypeOfUsage());
            newCollateral.setUsage(newCollateralInfoView.getUsage());
            newCollateral.setUwDecision(newCollateralInfoView.getUwDecision());
            newCollateral.setUwRemark(newCollateralInfoView.getUwRemark());
            newCollateral.setNewCreditFacility(newCreditFacility);
            newCollateralList.add(newCollateral);
        }
        return newCollateralList;
    }

    public List<NewCollateralInfoView> transformsCollateralToView(List<NewCollateral> newCollateralList) {
        List<NewCollateralInfoView> newCollateralViewList = new ArrayList<NewCollateralInfoView>();
        NewCollateralInfoView newCollateralInfoView;

        for (NewCollateral newCollateralDetail1 : newCollateralList) {
            newCollateralInfoView = new NewCollateralInfoView();
            newCollateralInfoView.setId(newCollateralDetail1.getId());
            newCollateralInfoView.setCreateDate(newCollateralDetail1.getCreateDate());
            newCollateralInfoView.setCreateBy(newCollateralDetail1.getCreateBy());
            newCollateralInfoView.setCreateDate(newCollateralDetail1.getCreateDate());
            newCollateralInfoView.setCreateBy(newCollateralDetail1.getCreateBy());
            newCollateralInfoView.setJobID(newCollateralDetail1.getJobID());
            newCollateralInfoView.setAadDecision(newCollateralDetail1.getAadDecision());
            newCollateralInfoView.setAadDecisionReason(newCollateralDetail1.getAadDecisionReason());
            newCollateralInfoView.setAadDecisionReasonDetail(newCollateralDetail1.getAadDecisionReasonDetail());
            newCollateralInfoView.setAppraisalDate(newCollateralDetail1.getAppraisalDate());
            newCollateralInfoView.setBdmComments(newCollateralDetail1.getBdmComments());
            newCollateralInfoView.setMortgageCondition(newCollateralDetail1.getMortgageCondition());
            newCollateralInfoView.setMortgageConditionDetail(newCollateralDetail1.getMortgageConditionDetail());
            newCollateralInfoView.setTypeOfUsage(newCollateralDetail1.getTypeOfUsage());
            newCollateralInfoView.setUsage(newCollateralDetail1.getUsage());
            newCollateralInfoView.setUwDecision(newCollateralDetail1.getUwDecision());
            newCollateralInfoView.setUwRemark(newCollateralDetail1.getUwRemark());

            List<NewCollateralCredit> newCollateralRelCredits = newCollateralCreditDAO.getListCollRelationByNewCollateral(newCollateralDetail1);
            if (newCollateralRelCredits != null) {
                List<NewCreditDetail> newCreditDetailList = new ArrayList<NewCreditDetail>();

                for (NewCollateralCredit newCollateralCredit : newCollateralRelCredits) {
                    newCreditDetailList.add(newCollateralCredit.getNewCreditDetail());
                }

                List<NewCreditDetailView> newCreditDetailViewList = newCreditDetailTransform.transformToView(newCreditDetailList);
                newCollateralInfoView.setNewCreditDetailViewList(newCreditDetailViewList);

            }

            List<NewCollateralHead> newCollateralHeadDetails = newCollateralHeadDAO.findByNewCollateralDetail(newCollateralDetail1);
            if (newCollateralDetail1.getNewCollateralHeadList() != null) {
                List<NewCollateralHeadDetailView> newCollateralHeadDetailViews = transformCollateralHeadToView(newCollateralHeadDetails);
                newCollateralInfoView.setNewCollateralHeadDetailViewList(newCollateralHeadDetailViews);
            }


            newCollateralViewList.add(newCollateralInfoView);
        }
        return newCollateralViewList;
    }

    public List<NewCollateralHead> transformCollateralHeadToModel(List<NewCollateralHeadDetailView> newCollateralHeadDetailViewList, NewCollateral collateralDetail, User user) {

        List<NewCollateralHead> collateralHeaderDetailList = new ArrayList<NewCollateralHead>();
        NewCollateralHead collateralHeaderDetail;

        for (NewCollateralHeadDetailView newCollateralHeadDetailView : newCollateralHeadDetailViewList) {
            collateralHeaderDetail = new NewCollateralHead();

            if (newCollateralHeadDetailView.getId() != 0) {
                collateralHeaderDetail.setId(newCollateralHeadDetailView.getId());
                collateralHeaderDetail.setCreateDate(newCollateralHeadDetailView.getCreateDate());
                collateralHeaderDetail.setCreateBy(newCollateralHeadDetailView.getCreateBy());
            } else { // id = 0 create new
                collateralHeaderDetail.setCreateDate(new Date());
                collateralHeaderDetail.setCreateBy(user);
            }

            collateralHeaderDetail.setHeadCollType(newCollateralHeadDetailView.getHeadCollType());
            collateralHeaderDetail.setPotential(newCollateralHeadDetailView.getPotentialCollateral());
            collateralHeaderDetail.setCollateralLocation(newCollateralHeadDetailView.getCollateralLocation());
            collateralHeaderDetail.setTitleDeed(newCollateralHeadDetailView.getTitleDeed());
            collateralHeaderDetail.setCollTypePercentLTV(newCollateralHeadDetailView.getCollTypePercentLTV());
            collateralHeaderDetail.setExistingCredit(newCollateralHeadDetailView.getExistingCredit());
            collateralHeaderDetail.setInsuranceCompany(newCollateralHeadDetailView.getInsuranceCompany());
            collateralHeaderDetail.setAppraisalValue(newCollateralHeadDetailView.getAppraisalValue());
            collateralHeaderDetail.setModifyBy(newCollateralHeadDetailView.getModifyBy());
            collateralHeaderDetail.setModifyDate(newCollateralHeadDetailView.getModifyDate());
            collateralHeaderDetail.setNewCollateral(collateralDetail);
            collateralHeaderDetailList.add(collateralHeaderDetail);
        }

        return collateralHeaderDetailList;
    }

    public NewCollateralHead transformCollateralHeadToModel(NewCollateralHeadDetailView newCollateralHeadDetailView, NewCollateral collateralDetail, User user) {

        NewCollateralHead collateralHeaderDetail = new NewCollateralHead();

        if (newCollateralHeadDetailView.getId() != 0) {
            collateralHeaderDetail.setId(newCollateralHeadDetailView.getId());
            collateralHeaderDetail.setCreateDate(newCollateralHeadDetailView.getCreateDate());
            collateralHeaderDetail.setCreateBy(newCollateralHeadDetailView.getCreateBy());
        } else { // id = 0 create new
            collateralHeaderDetail.setCreateDate(new Date());
            collateralHeaderDetail.setCreateBy(user);
        }

        collateralHeaderDetail.setHeadCollType(newCollateralHeadDetailView.getHeadCollType());
        collateralHeaderDetail.setPotential(newCollateralHeadDetailView.getPotentialCollateral());
        collateralHeaderDetail.setCollateralLocation(newCollateralHeadDetailView.getCollateralLocation());
        collateralHeaderDetail.setTitleDeed(newCollateralHeadDetailView.getTitleDeed());
        collateralHeaderDetail.setCollTypePercentLTV(newCollateralHeadDetailView.getCollTypePercentLTV());
        collateralHeaderDetail.setExistingCredit(newCollateralHeadDetailView.getExistingCredit());
        collateralHeaderDetail.setInsuranceCompany(newCollateralHeadDetailView.getInsuranceCompany());
        collateralHeaderDetail.setAppraisalValue(newCollateralHeadDetailView.getAppraisalValue());
        collateralHeaderDetail.setModifyBy(newCollateralHeadDetailView.getModifyBy());
        collateralHeaderDetail.setModifyDate(newCollateralHeadDetailView.getModifyDate());
        collateralHeaderDetail.setNewCollateral(collateralDetail);

        return collateralHeaderDetail;
    }

    public List<NewCollateralHeadDetailView> transformCollateralHeadToView(List<NewCollateralHead> collateralHeaderDetailList) {

        List<NewCollateralHeadDetailView> newCollateralHeadDetailViewList = new ArrayList<NewCollateralHeadDetailView>();
        NewCollateralHeadDetailView newCollateralHeadDetailView;

        for (NewCollateralHead collateralHeaderDetail : collateralHeaderDetailList) {
            newCollateralHeadDetailView = new NewCollateralHeadDetailView();
            newCollateralHeadDetailView.setId(collateralHeaderDetail.getId());
            newCollateralHeadDetailView.setCreateBy(collateralHeaderDetail.getCreateBy());
            newCollateralHeadDetailView.setCreateDate(collateralHeaderDetail.getCreateDate());
            newCollateralHeadDetailView.setModifyBy(collateralHeaderDetail.getModifyBy());
            newCollateralHeadDetailView.setModifyDate(collateralHeaderDetail.getModifyDate());
            newCollateralHeadDetailView.setHeadCollType(collateralHeaderDetail.getHeadCollType());
            newCollateralHeadDetailView.setPotentialCollateral(collateralHeaderDetail.getPotential());
            newCollateralHeadDetailView.setCollateralLocation(collateralHeaderDetail.getCollateralLocation());
            newCollateralHeadDetailView.setTitleDeed(collateralHeaderDetail.getTitleDeed());
            newCollateralHeadDetailView.setCollTypePercentLTV(collateralHeaderDetail.getCollTypePercentLTV());
            newCollateralHeadDetailView.setExistingCredit(collateralHeaderDetail.getExistingCredit());
            newCollateralHeadDetailView.setInsuranceCompany(collateralHeaderDetail.getInsuranceCompany());
            newCollateralHeadDetailView.setAppraisalValue(collateralHeaderDetail.getAppraisalValue());
            newCollateralHeadDetailView.setModifyBy(collateralHeaderDetail.getModifyBy());
            newCollateralHeadDetailView.setModifyDate(collateralHeaderDetail.getModifyDate());
            newCollateralHeadDetailView.setCreateBy(collateralHeaderDetail.getCreateBy());
            newCollateralHeadDetailView.setCreateDate(collateralHeaderDetail.getCreateDate());
            newCollateralHeadDetailView.setModifyBy(collateralHeaderDetail.getModifyBy());
            newCollateralHeadDetailView.setModifyDate(collateralHeaderDetail.getModifyDate());

            List<NewCollateralSub> newCollateralSubDetails = newCollateralSubDAO.getAllNewSubCollateral(collateralHeaderDetail);
            if (newCollateralSubDetails.size() > 0) {
                List<NewSubCollateralDetailView> newSubCollateralDetailViews = transformCollateralSubToView(newCollateralSubDetails);
                newCollateralHeadDetailView.setNewSubCollateralDetailViewList(newSubCollateralDetailViews);
            }

            newCollateralHeadDetailViewList.add(newCollateralHeadDetailView);
        }

        return newCollateralHeadDetailViewList;
    }

    public List<NewCollateralSub> transformCollateralSubToModel(List<NewSubCollateralDetailView> newSubCollateralDetailViewList, NewCollateralHead collateralHeaderDetail, User user) {

        List<NewCollateralSub> subCollateralDetailList = new ArrayList<NewCollateralSub>();
        NewCollateralSub subCollateralDetail;

        for (NewSubCollateralDetailView newSubCollateralDetailView : newSubCollateralDetailViewList) {
            subCollateralDetail = new NewCollateralSub();

            if (newSubCollateralDetailView.getId() != 0) {
                subCollateralDetail.setId(newSubCollateralDetailView.getId());
                subCollateralDetail.setCreateDate(newSubCollateralDetailView.getCreateDate());
                subCollateralDetail.setCreateBy(newSubCollateralDetailView.getCreateBy());
            } else { // id = 0 create new
                subCollateralDetail.setCreateDate(new Date());
                subCollateralDetail.setCreateBy(user);
            }

            subCollateralDetail.setTitleDeed(newSubCollateralDetailView.getTitleDeed());
            subCollateralDetail.setAppraisalValue(newSubCollateralDetailView.getAppraisalValue());
            subCollateralDetail.setAddress(newSubCollateralDetailView.getAddress());
            subCollateralDetail.setCollateralOwner(newSubCollateralDetailView.getCollateralOwner());
            subCollateralDetail.setSubCollateralType(newSubCollateralDetailView.getSubCollateralType());
            subCollateralDetail.setNewCollateralHead(collateralHeaderDetail);
            subCollateralDetailList.add(subCollateralDetail);
        }

        return subCollateralDetailList;
    }

    public List<NewSubCollateralDetailView> transformCollateralSubToView(List<NewCollateralSub> subCollateralDetailList) {

        List<NewSubCollateralDetailView> newSubCollateralDetailViewList = new ArrayList<NewSubCollateralDetailView>();
        NewSubCollateralDetailView newSubCollateralDetailView;

        for (NewCollateralSub subCollateralDetail : subCollateralDetailList) {
            newSubCollateralDetailView = new NewSubCollateralDetailView();
            newSubCollateralDetailView.setId(subCollateralDetail.getId());
            newSubCollateralDetailView.setTitleDeed(subCollateralDetail.getTitleDeed());
            newSubCollateralDetailView.setAppraisalValue(subCollateralDetail.getAppraisalValue());
            newSubCollateralDetailView.setAddress(subCollateralDetail.getAddress());
            newSubCollateralDetailView.setCollateralOwner(subCollateralDetail.getCollateralOwner());
            newSubCollateralDetailView.setSubCollateralType(subCollateralDetail.getSubCollateralType());
            newSubCollateralDetailView.setCreateBy(subCollateralDetail.getCreateBy());
            newSubCollateralDetailView.setCreateDate(subCollateralDetail.getCreateDate());
            newSubCollateralDetailView.setModifyBy(subCollateralDetail.getModifyBy());
            newSubCollateralDetailView.setModifyDate(subCollateralDetail.getModifyDate());

            List<NewCollateralSubOwner> newCollateralSubCustomerList = newCollateralSubOwnerDAO.getListNewCollateralSubCustomer(subCollateralDetail);
            List<CustomerInfoView> collateralOwnerUWList = new ArrayList<CustomerInfoView>();
            if (newCollateralSubCustomerList != null) {
                for (NewCollateralSubOwner newCollateralSubCustomer : newCollateralSubCustomerList) {
                    CustomerInfoView customer = customerTransform.transformToView(newCollateralSubCustomer.getCustomer());
                    collateralOwnerUWList.add(customer);
                }
                newSubCollateralDetailView.setCollateralOwnerUWList(collateralOwnerUWList);
            }

            List<NewCollateralSubMortgage> newCollateralSubMortgages = newCollateralSubMortgageDAO.getListNewCollateralSubMortgage(subCollateralDetail);
            List<MortgageType> mortgageTypes = new ArrayList<MortgageType>();
            if (newCollateralSubMortgages != null) {
                for (NewCollateralSubMortgage newCollateralSubMortgage : newCollateralSubMortgages) {
                    MortgageType mortgageType = mortgageTypeDAO.findById(newCollateralSubMortgage.getMortgageType().getId());
                    mortgageTypes.add(mortgageType);
                }
                newSubCollateralDetailView.setMortgageList(mortgageTypes);
            }

            List<NewCollateralSubRelated> newCollateralSubRelateList = newCollateralSubRelatedDAO.getListNewCollateralSubRelate(subCollateralDetail);
            List<NewSubCollateralDetailView> relateList = new ArrayList<NewSubCollateralDetailView>();
            if (newCollateralSubRelateList != null) {
                for (NewCollateralSubRelated newCollateralSubRelate : newCollateralSubRelateList) {
                    // transform to view???
                }
                newSubCollateralDetailView.setRelatedWithList(relateList);
            }


            newSubCollateralDetailViewList.add(newSubCollateralDetailView);
        }

        return newSubCollateralDetailViewList;
    }

    public NewCollateralSub transformCollateralSubToModel(NewSubCollateralDetailView newSubCollateralDetailView, NewCollateralHead collateralHeaderDetail, User user) {

        NewCollateralSub subCollateralDetail = new NewCollateralSub();

        if (newSubCollateralDetailView.getId() != 0) {
            subCollateralDetail.setId(newSubCollateralDetailView.getId());
            subCollateralDetail.setCreateDate(newSubCollateralDetailView.getCreateDate());
            subCollateralDetail.setCreateBy(newSubCollateralDetailView.getCreateBy());
        } else { // id = 0 create new
            subCollateralDetail.setCreateDate(new Date());
            subCollateralDetail.setCreateBy(user);
        }

        subCollateralDetail.setTitleDeed(newSubCollateralDetailView.getTitleDeed());
        subCollateralDetail.setAppraisalValue(newSubCollateralDetailView.getAppraisalValue());
        subCollateralDetail.setAddress(newSubCollateralDetailView.getAddress());
        subCollateralDetail.setCollateralOwner(newSubCollateralDetailView.getCollateralOwner());
        subCollateralDetail.setSubCollateralType(newSubCollateralDetailView.getSubCollateralType());
        subCollateralDetail.setNewCollateralHead(collateralHeaderDetail);


        return subCollateralDetail;
    }
}
