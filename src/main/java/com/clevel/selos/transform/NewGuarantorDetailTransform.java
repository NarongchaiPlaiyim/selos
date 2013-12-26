package com.clevel.selos.transform;

import com.clevel.selos.dao.working.CustomerDAO;
import com.clevel.selos.dao.working.NewGuarantorRelationDAO;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.working.*;
import com.clevel.selos.model.view.CustomerInfoView;
import com.clevel.selos.model.view.NewCreditDetailView;
import com.clevel.selos.model.view.NewGuarantorDetailView;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NewGuarantorDetailTransform extends Transform {

    @Inject
    public NewGuarantorDetailTransform() {
    }

    @Inject
    CustomerDAO customerDAO;
    @Inject
    CustomerTransform customerTransform;
    @Inject
    NewGuarantorRelationDAO newGuarantorRelationDAO;
    @Inject
    NewCreditDetailTransform newCreditDetailTransform;

    public NewGuarantorDetail transformToModelOne(NewGuarantorDetailView newGuarantorDetailView, NewCreditFacility newCreditFacility, User user) {

        NewGuarantorDetail newGuarantorDetail = new NewGuarantorDetail();
        if (newGuarantorDetailView.getId() != 0) {
            newGuarantorDetail.setId(newGuarantorDetailView.getId());
            newGuarantorDetail.setCreateDate(newGuarantorDetailView.getCreateDate());
            newGuarantorDetail.setCreateBy(newGuarantorDetailView.getCreateBy());
        } else { // id = 0 create new
            newGuarantorDetail.setCreateDate(new Date());
            newGuarantorDetail.setCreateBy(user);
        }

        Customer guarantor = customerDAO.findById(newGuarantorDetailView.getGuarantorName().getId());
        newGuarantorDetail.setGuarantorName(guarantor);
        newGuarantorDetail.setTcgLgNo(newGuarantorDetailView.getTcgLgNo());
        newGuarantorDetail.setNewCreditFacility(newCreditFacility);
        newGuarantorDetail.setTotalLimitGuaranteeAmount(newGuarantorDetailView.getTotalLimitGuaranteeAmount());

        return newGuarantorDetail;
    }

    public List<NewGuarantorDetail> transformToModel(List<NewGuarantorDetailView> newGuarantorDetailViewList, NewCreditFacility newCreditFacility, User user) {

        List<NewGuarantorDetail> newGuarantorDetailList = new ArrayList<NewGuarantorDetail>();
        NewGuarantorDetail newGuarantorDetail;

        for (NewGuarantorDetailView newGuarantorDetailView : newGuarantorDetailViewList) {
            newGuarantorDetail = new NewGuarantorDetail();
            if (newGuarantorDetailView.getId() != 0) {
                newGuarantorDetail.setId(newGuarantorDetailView.getId());
                newGuarantorDetail.setCreateDate(newGuarantorDetailView.getCreateDate());
                newGuarantorDetail.setCreateBy(newGuarantorDetailView.getCreateBy());
            } else { // id = 0 create new
                newGuarantorDetail.setCreateDate(new Date());
                newGuarantorDetail.setCreateBy(user);
            }

            Customer guarantor = customerDAO.findById(newGuarantorDetailView.getGuarantorName().getId());
            newGuarantorDetail.setGuarantorName(guarantor);
            newGuarantorDetail.setTcgLgNo(newGuarantorDetailView.getTcgLgNo());
            newGuarantorDetail.setNewCreditFacility(newCreditFacility);
            newGuarantorDetail.setTotalLimitGuaranteeAmount(newGuarantorDetailView.getTotalLimitGuaranteeAmount());

            newGuarantorDetailList.add(newGuarantorDetail);
        }

        return newGuarantorDetailList;
    }

    public List<NewGuarantorDetailView> transformToView(List<NewGuarantorDetail> newGuarantorDetailList) {
        List<NewGuarantorDetailView> newGuarantorDetailViews = new ArrayList<NewGuarantorDetailView>();
        NewGuarantorDetailView newGuarantorDetailView;

        for (NewGuarantorDetail newGuarantorDetail : newGuarantorDetailList) {
            newGuarantorDetailView = new NewGuarantorDetailView();
            CustomerInfoView guarantorView = customerTransform.transformToView(newGuarantorDetail.getGuarantorName());
            newGuarantorDetailView.setCreateDate(newGuarantorDetail.getCreateDate());
            newGuarantorDetailView.setCreateBy(newGuarantorDetail.getCreateBy());
            newGuarantorDetailView.setModifyDate(newGuarantorDetail.getModifyDate());
            newGuarantorDetailView.setModifyBy(newGuarantorDetail.getModifyBy());
            newGuarantorDetailView.setGuarantorName(guarantorView);
            newGuarantorDetailView.setTcgLgNo(newGuarantorDetail.getTcgLgNo());
            newGuarantorDetailView.setTotalLimitGuaranteeAmount(newGuarantorDetail.getTotalLimitGuaranteeAmount());

            List<NewGuarantorRelCredit> newGuarantorRelCreditList = newGuarantorRelationDAO.getListGuarantorRelationByNewGuarantor(newGuarantorDetail);
            log.info("newGuarantorRelCreditList :: {}", newGuarantorRelCreditList.size());
            if (newGuarantorRelCreditList != null) {
                List<NewCreditDetail> newCreditDetailList = new ArrayList<NewCreditDetail>();

                for (NewGuarantorRelCredit newGuarantorRelCredit : newGuarantorRelCreditList) {
                    newCreditDetailList.add(newGuarantorRelCredit.getNewCreditDetail());
                }
                log.info("newCreditDetailList Guarantor:: {}", newCreditDetailList.size());
                List<NewCreditDetailView> newCreditDetailViewList = newCreditDetailTransform.transformToView(newCreditDetailList);
                newGuarantorDetailView.setNewCreditDetailViewList(newCreditDetailViewList);

            }

            newGuarantorDetailViews.add(newGuarantorDetailView);
        }

        return newGuarantorDetailViews;
    }

    public NewGuarantorDetailView transformNewGuarantorDetailToView(NewGuarantorDetail newGuarantorDetail) {

        NewGuarantorDetailView newGuarantorDetailView = new NewGuarantorDetailView();
        CustomerInfoView guarantorView = customerTransform.transformToView(newGuarantorDetail.getGuarantorName());
        newGuarantorDetailView.setCreateDate(newGuarantorDetail.getCreateDate());
        newGuarantorDetailView.setCreateBy(newGuarantorDetail.getCreateBy());
        newGuarantorDetailView.setModifyDate(newGuarantorDetail.getModifyDate());
        newGuarantorDetailView.setModifyBy(newGuarantorDetail.getModifyBy());
        newGuarantorDetailView.setGuarantorName(guarantorView);
        newGuarantorDetailView.setTcgLgNo(newGuarantorDetail.getTcgLgNo());
        newGuarantorDetailView.setTotalLimitGuaranteeAmount(newGuarantorDetail.getTotalLimitGuaranteeAmount());

        List<NewGuarantorRelCredit> newGuarantorRelCreditList = newGuarantorRelationDAO.getListGuarantorRelationByNewGuarantor(newGuarantorDetail);
        log.info("newGuarantorRelCreditList :: {}", newGuarantorRelCreditList.size());
        if (newGuarantorRelCreditList != null) {
            List<NewCreditDetail> newCreditDetailList = new ArrayList<NewCreditDetail>();

            for (NewGuarantorRelCredit newGuarantorRelCredit : newGuarantorRelCreditList) {
                newCreditDetailList.add(newGuarantorRelCredit.getNewCreditDetail());
            }
            log.info("newCreditDetailList Guarantor:: {}", newCreditDetailList.size());
            List<NewCreditDetailView> newCreditDetailViewList = newCreditDetailTransform.transformToView(newCreditDetailList);
            newGuarantorDetailView.setNewCreditDetailViewList(newCreditDetailViewList);

        }


        return newGuarantorDetailView;
    }
}
