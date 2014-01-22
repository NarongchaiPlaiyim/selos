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
    NewCollateralRelationDAO newCollateralRelationDAO;
    @Inject
    NewCreditDetailTransform newCreditDetailTransform;
    @Inject
    NewCollateralHeadDetailDAO newCollateralHeadDetailDAO;
    @Inject
    NewCollateralSubDetailDAO newCollateralSubDetailDAO;
    @Inject
    NewSubCollCustomerDAO newSubCollCustomerDAO;
    @Inject
    NewSubCollMortgageDAO newSubCollMortgageDAO;
    @Inject
    MortgageTypeDAO mortgageTypeDAO;
    @Inject
    NewSubCollRelateDAO newSubCollRelateDAO;


    public List<NewCollateralDetail> transformsCollateralToModel(List<NewCollateralInfoView> newCollateralInfoViewList, NewCreditFacility newCreditFacility, User user) {
        List<NewCollateralDetail> newCollateralDetailList = new ArrayList<NewCollateralDetail>();
        NewCollateralDetail newCollateralDetail;

        for (NewCollateralInfoView newCollateralInfoView : newCollateralInfoViewList) {
            newCollateralDetail = new NewCollateralDetail();

            if (newCollateralInfoView.getId() != 0) {
                newCollateralDetail.setId(newCollateralInfoView.getId());
                newCollateralDetail.setCreateDate(newCollateralInfoView.getCreateDate());
                newCollateralDetail.setCreateBy(newCollateralInfoView.getCreateBy());
            } else { // id = 0 create new
                newCollateralDetail.setCreateDate(new Date());
                newCollateralDetail.setCreateBy(user);
            }
            newCollateralDetail.setJobID(newCollateralInfoView.getJobID());
            newCollateralDetail.setAadDecision(newCollateralInfoView.getAadDecision());
            newCollateralDetail.setAadDecisionReason(newCollateralInfoView.getAadDecisionReason());
            newCollateralDetail.setAadDecisionReasonDetail(newCollateralInfoView.getAadDecisionReasonDetail());
            newCollateralDetail.setAppraisalDate(newCollateralInfoView.getAppraisalDate());
            newCollateralDetail.setBdmComments(newCollateralInfoView.getBdmComments());
            newCollateralDetail.setMortgageCondition(newCollateralInfoView.getMortgageCondition());
            newCollateralDetail.setMortgageConditionDetail(newCollateralInfoView.getMortgageConditionDetail());
            newCollateralDetail.setTypeOfUsage(newCollateralInfoView.getTypeOfUsage());
            newCollateralDetail.setUsage(newCollateralInfoView.getUsage());
            newCollateralDetail.setUwDecision(newCollateralInfoView.getUwDecision());
            newCollateralDetail.setUwRemark(newCollateralInfoView.getUwRemark());
            newCollateralDetail.setNewCreditFacility(newCreditFacility);
            newCollateralDetailList.add(newCollateralDetail);
        }
        return newCollateralDetailList;
    }

    public List<NewCollateralInfoView> transformsCollateralToView(List<NewCollateralDetail> newCollateralInfoViewList) {
        List<NewCollateralInfoView> newCollateralViewList = new ArrayList<NewCollateralInfoView>();
        NewCollateralInfoView newCollateralInfoView;

        for (NewCollateralDetail newCollateralDetail1 : newCollateralInfoViewList) {
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

            List<NewCollateralRelCredit> newCollateralRelCredits = newCollateralRelationDAO.getListCollRelationByNewCollateral(newCollateralDetail1);
            if (newCollateralRelCredits != null) {
                List<NewCreditDetail> newCreditDetailList = new ArrayList<NewCreditDetail>();

                for (NewCollateralRelCredit newCollateralRelCredit : newCollateralRelCredits) {
                    newCreditDetailList.add(newCollateralRelCredit.getNewCreditDetail());
                }

                List<NewCreditDetailView> newCreditDetailViewList = newCreditDetailTransform.transformToView(newCreditDetailList);
                newCollateralInfoView.setNewCreditDetailViewList(newCreditDetailViewList);

            }

            List<NewCollateralHeadDetail> newCollateralHeadDetails = newCollateralHeadDetailDAO.findByNewCollateralDetail(newCollateralDetail1);
            if (newCollateralDetail1.getNewCollateralHeadDetailList() != null) {
                List<NewCollateralHeadDetailView> newCollateralHeadDetailViews = transformCollateralHeadToView(newCollateralHeadDetails);
                newCollateralInfoView.setNewCollateralHeadDetailViewList(newCollateralHeadDetailViews);
            }


            newCollateralViewList.add(newCollateralInfoView);
        }
        return newCollateralViewList;
    }

    public List<NewCollateralHeadDetail> transformCollateralHeadToModel(List<NewCollateralHeadDetailView> newCollateralHeadDetailViewList, NewCollateralDetail collateralDetail, User user) {

        List<NewCollateralHeadDetail> collateralHeaderDetailList = new ArrayList<NewCollateralHeadDetail>();
        NewCollateralHeadDetail collateralHeaderDetail;

        for (NewCollateralHeadDetailView newCollateralHeadDetailView : newCollateralHeadDetailViewList) {
            collateralHeaderDetail = new NewCollateralHeadDetail();

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
            collateralHeaderDetail.setNewCollateralDetail(collateralDetail);
            collateralHeaderDetailList.add(collateralHeaderDetail);
        }

        return collateralHeaderDetailList;
    }

    public NewCollateralHeadDetail transformCollateralHeadToModel(NewCollateralHeadDetailView newCollateralHeadDetailView, NewCollateralDetail collateralDetail, User user) {

        NewCollateralHeadDetail collateralHeaderDetail = new NewCollateralHeadDetail();

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
        collateralHeaderDetail.setNewCollateralDetail(collateralDetail);

        return collateralHeaderDetail;
    }

    public List<NewCollateralHeadDetailView> transformCollateralHeadToView(List<NewCollateralHeadDetail> collateralHeaderDetailList) {

        List<NewCollateralHeadDetailView> newCollateralHeadDetailViewList = new ArrayList<NewCollateralHeadDetailView>();
        NewCollateralHeadDetailView newCollateralHeadDetailView;

        for (NewCollateralHeadDetail collateralHeaderDetail : collateralHeaderDetailList) {
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

            List<NewCollateralSubDetail> newCollateralSubDetails = newCollateralSubDetailDAO.getAllNewSubCollateralDetail(collateralHeaderDetail);
            if (newCollateralSubDetails.size() > 0) {
                List<NewSubCollateralDetailView> newSubCollateralDetailViews = transformCollateralSubToView(newCollateralSubDetails);
                newCollateralHeadDetailView.setNewSubCollateralDetailViewList(newSubCollateralDetailViews);
            }

            newCollateralHeadDetailViewList.add(newCollateralHeadDetailView);
        }

        return newCollateralHeadDetailViewList;
    }

    public List<NewCollateralSubDetail> transformCollateralSubToModel(List<NewSubCollateralDetailView> newSubCollateralDetailViewList, NewCollateralHeadDetail collateralHeaderDetail, User user) {

        List<NewCollateralSubDetail> subCollateralDetailList = new ArrayList<NewCollateralSubDetail>();
        NewCollateralSubDetail subCollateralDetail;

        for (NewSubCollateralDetailView newSubCollateralDetailView : newSubCollateralDetailViewList) {
            subCollateralDetail = new NewCollateralSubDetail();

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
            subCollateralDetail.setSubCollTypeCaption(newSubCollateralDetailView.getSubCollateralType());
            subCollateralDetail.setNewCollateralHeadDetail(collateralHeaderDetail);
            subCollateralDetailList.add(subCollateralDetail);
        }

        return subCollateralDetailList;
    }

    public List<NewSubCollateralDetailView> transformCollateralSubToView(List<NewCollateralSubDetail> subCollateralDetailList) {

        List<NewSubCollateralDetailView> newSubCollateralDetailViewList = new ArrayList<NewSubCollateralDetailView>();
        NewSubCollateralDetailView newSubCollateralDetailView;

        for (NewCollateralSubDetail subCollateralDetail : subCollateralDetailList) {
            newSubCollateralDetailView = new NewSubCollateralDetailView();
            newSubCollateralDetailView.setId(subCollateralDetail.getId());
            newSubCollateralDetailView.setTitleDeed(subCollateralDetail.getTitleDeed());
            newSubCollateralDetailView.setAppraisalValue(subCollateralDetail.getAppraisalValue());
            newSubCollateralDetailView.setAddress(subCollateralDetail.getAddress());
            newSubCollateralDetailView.setCollateralOwner(subCollateralDetail.getCollateralOwner());
            newSubCollateralDetailView.setSubCollateralType(subCollateralDetail.getSubCollTypeCaption());
            newSubCollateralDetailView.setCreateBy(subCollateralDetail.getCreateBy());
            newSubCollateralDetailView.setCreateDate(subCollateralDetail.getCreateDate());
            newSubCollateralDetailView.setModifyBy(subCollateralDetail.getModifyBy());
            newSubCollateralDetailView.setModifyDate(subCollateralDetail.getModifyDate());

            List<NewCollateralSubCustomer> newCollateralSubCustomerList = newSubCollCustomerDAO.getListNewCollateralSubCustomer(subCollateralDetail);
            List<CustomerInfoView> collateralOwnerUWList = new ArrayList<CustomerInfoView>();
            if (newCollateralSubCustomerList != null) {
                for (NewCollateralSubCustomer newCollateralSubCustomer : newCollateralSubCustomerList) {
                    CustomerInfoView customer = customerTransform.transformToView(newCollateralSubCustomer.getCustomer());
                    collateralOwnerUWList.add(customer);
                }
                newSubCollateralDetailView.setCollateralOwnerUWList(collateralOwnerUWList);
            }

            List<NewCollateralSubMortgage> newCollateralSubMortgages = newSubCollMortgageDAO.getListNewCollateralSubMortgage(subCollateralDetail);
            List<MortgageType> mortgageTypes = new ArrayList<MortgageType>();
            if (newCollateralSubMortgages != null) {
                for (NewCollateralSubMortgage newCollateralSubMortgage : newCollateralSubMortgages) {
                    MortgageType mortgageType = mortgageTypeDAO.findById(newCollateralSubMortgage.getMortgageType().getId());
                    mortgageTypes.add(mortgageType);
                }
                newSubCollateralDetailView.setMortgageList(mortgageTypes);
            }

            List<NewCollateralSubRelate> newCollateralSubRelateList = newSubCollRelateDAO.getListNewCollateralSubRelate(subCollateralDetail);
            List<NewSubCollateralDetailView> relateList = new ArrayList<NewSubCollateralDetailView>();
            if (newCollateralSubRelateList != null) {
                for (NewCollateralSubRelate newCollateralSubRelate : newCollateralSubRelateList) {
                    // transform to view???
                }
                newSubCollateralDetailView.setRelatedWithList(relateList);
            }


            newSubCollateralDetailViewList.add(newSubCollateralDetailView);
        }

        return newSubCollateralDetailViewList;
    }

    public NewCollateralSubDetail transformCollateralSubToModel(NewSubCollateralDetailView newSubCollateralDetailView, NewCollateralHeadDetail collateralHeaderDetail, User user) {

        NewCollateralSubDetail subCollateralDetail = new NewCollateralSubDetail();

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
        subCollateralDetail.setSubCollTypeCaption(newSubCollateralDetailView.getSubCollateralType());
        subCollateralDetail.setNewCollateralHeadDetail(collateralHeaderDetail);


        return subCollateralDetail;
    }
}
