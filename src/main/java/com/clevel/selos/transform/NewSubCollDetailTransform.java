package com.clevel.selos.transform;

import com.clevel.selos.dao.master.MortgageTypeDAO;
import com.clevel.selos.dao.working.*;
import com.clevel.selos.model.db.master.MortgageType;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.working.*;
import com.clevel.selos.model.view.CustomerInfoView;
import com.clevel.selos.model.view.NewSubCollateralDetailView;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NewSubCollDetailTransform extends Transform {

    @Inject
    public NewSubCollDetailTransform() {}

    @Inject
    CustomerDAO customerDAO;
    @Inject
    NewCollateralSubDAO newCollateralSubDetailDAO;
    @Inject
    CustomerTransform customerTransform;
    @Inject
    NewCollateralSubOwnerDAO newSubCollCustomerDAO;
    @Inject
    NewCollateralSubMortgageDAO newSubCollMortgageDAO;
    @Inject
    MortgageTypeDAO mortgageTypeDAO;
    @Inject
    NewCollateralSubRelatedDAO newSubCollRelateDAO;


    public NewCollateralSub transformNewSubCollateralDetailViewToModel(NewSubCollateralDetailView newSubCollateralDetailView, NewCollateralHead collateralHeaderDetail, User user) {

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

    public List<NewCollateralSub> transformToModel(List<NewSubCollateralDetailView> newSubCollateralDetailViewList, NewCollateralHead collateralHeaderDetail, User user) {

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

    public List<NewSubCollateralDetailView> transformToView(List<NewCollateralSub> subCollateralDetailList) {

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

            List<NewCollateralSubOwner> newCollateralSubCustomerList = newSubCollCustomerDAO.getListNewCollateralSubCustomer(subCollateralDetail);
            List<CustomerInfoView> collateralOwnerUWList = new ArrayList<CustomerInfoView>();
            if (newCollateralSubCustomerList != null) {
                for (NewCollateralSubOwner newCollateralSubCustomer : newCollateralSubCustomerList) {
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

            List<NewCollateralSubRelated> newCollateralSubRelateList = newSubCollRelateDAO.getListNewCollateralSubRelate(subCollateralDetail);
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

    public List<NewCollateralSub> getNewSubDetailForRelated(List<NewSubCollateralDetailView> newSubCollateralDetailViewList, List<NewCollateralSub> newCollateralSubDetailPersist) {

        List<NewCollateralSub> listReturn = new ArrayList<NewCollateralSub>();
        for (NewSubCollateralDetailView subView : newSubCollateralDetailViewList) {
            for (NewCollateralSub newCollateralSubDetail : newCollateralSubDetailPersist) {
                if (subView.getId() == newCollateralSubDetail.getId()) {
                    listReturn.add(newCollateralSubDetail);
                }
            }
        }

        return listReturn;
    }



}
