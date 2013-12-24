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
    public NewSubCollDetailTransform() {
    }

    @Inject
    CustomerDAO customerDAO;
    @Inject
    NewCollateralSubDetailDAO newCollateralSubDetailDAO;
    @Inject
    CustomerTransform customerTransform;
    @Inject
    NewSubCollCustomerDAO newSubCollCustomerDAO;
    @Inject
    NewSubCollMortgageDAO newSubCollMortgageDAO;
    @Inject
    MortgageTypeDAO mortgageTypeDAO;
    @Inject
    NewSubCollRelateDAO newSubCollRelateDAO;


    public NewCollateralSubDetail transformNewSubCollateralDetailViewToModel(NewSubCollateralDetailView newSubCollateralDetailView, NewCollateralHeadDetail collateralHeaderDetail, User user) {

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

    public List<NewCollateralSubDetail> transformToModel(List<NewSubCollateralDetailView> newSubCollateralDetailViewList, NewCollateralHeadDetail collateralHeaderDetail, User user) {

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

    public List<NewSubCollateralDetailView> transformToView(List<NewCollateralSubDetail> subCollateralDetailList) {

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

    public List<NewCollateralSubDetail> getNewSubDetailForCollateral(List<NewSubCollateralDetailView> newSubCollateralDetailViewList, List<NewCollateralSubDetail> newCollateralSubDetailPersist) {

        List<NewCollateralSubDetail> listReturn = new ArrayList<NewCollateralSubDetail>();
        for (NewSubCollateralDetailView subView : newSubCollateralDetailViewList) {
            for (NewCollateralSubDetail newCollateralSubDetail : newCollateralSubDetailPersist) {
                if (subView.getRelatedWithId() == newCollateralSubDetail.getId()) {
                    listReturn.add(newCollateralSubDetail);
                }
            }
        }

        return listReturn;
    }

    public List<NewSubCollateralDetailView> getNewSubCollateralDetailView(List<NewCollateralSubDetail> newCollateralSubDetails) {

        List<NewSubCollateralDetailView> listReturn = new ArrayList<NewSubCollateralDetailView>();
        /*for (NewSubCollateralDetailView subView : newSubCollateralDetailViewList) {
            for (NewCollateralSubDetail newCollateralSubDetail : newCollateralSubDetails) {
                if (subView.getId() == newCollateralSubDetail.getId()) {
                    listReturn.add(newCollateralSubDetail);
                }
            }
        }
*/
        return listReturn;
    }

}
