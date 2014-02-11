package com.clevel.selos.transform;

import com.clevel.selos.dao.working.CustomerDAO;
import com.clevel.selos.dao.working.NewGuarantorRelationDAO;
import com.clevel.selos.model.ProposeType;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.working.*;
import com.clevel.selos.model.view.*;

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
    @Inject
    ExistingCreditDetailTransform existingCreditDetailTransform;


    public List<NewGuarantorDetail> transformToModel(List<NewGuarantorDetailView> newGuarantorDetailViewList, NewCreditFacility newCreditFacility, User user) {

        List<NewGuarantorDetail> newGuarantorDetailList = new ArrayList<NewGuarantorDetail>();
        NewGuarantorDetail newGuarantorDetail;

        for (NewGuarantorDetailView newGuarantorDetailView : newGuarantorDetailViewList) {
            newGuarantorDetail = new NewGuarantorDetail();
            if (newGuarantorDetailView.getId() != 0) {
                newGuarantorDetail.setCreateDate(newGuarantorDetailView.getCreateDate());
                newGuarantorDetail.setCreateBy(newGuarantorDetailView.getCreateBy());
            } else { // id = 0 create new
                newGuarantorDetail.setCreateDate(new Date());
                newGuarantorDetail.setCreateBy(user);
            }
            newGuarantorDetail.setProposeType(ProposeType.P.type());
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

            List<NewGuarantorCredit> newGuarantorCreditList = newGuarantorRelationDAO.getListGuarantorRelationByNewGuarantor(newGuarantorDetail);
            log.info("newGuarantorCreditList :: {}", newGuarantorCreditList.size());
            if (newGuarantorCreditList != null) {
                List<NewCreditDetail> newCreditDetailList = new ArrayList<NewCreditDetail>();
                List<ExistingCreditDetail> existingCreditDetailList = new ArrayList<ExistingCreditDetail>();

                for (NewGuarantorCredit newGuarantorCredit : newGuarantorCreditList) {
                    if (newGuarantorCredit.getExistingCreditDetail() != null) {
                        log.info("newGuarantorCredit.getExistingCreditDetail :: {}", newGuarantorCredit.getExistingCreditDetail().getId());
                        existingCreditDetailList.add(newGuarantorCredit.getExistingCreditDetail());
                    } else if (newGuarantorCredit.getNewCreditDetail() != null) {
                        log.info("newGuarantorCredit.getNewCreditDetail :: {}", newGuarantorCredit.getNewCreditDetail().getId());
                        newGuarantorCredit.getNewCreditDetail().setGuaranteeAmount(newGuarantorCredit.getGuaranteeAmount());
                        newCreditDetailList.add(newGuarantorCredit.getNewCreditDetail());
                        log.info("newGuarantorCredit.getGuaranteeAmount() ::: {}", newGuarantorCredit.getGuaranteeAmount());
                    }
                }
                log.info("newCreditDetailList Guarantor:: {}", newCreditDetailList.size());
                log.info("getExistingCreditDetail Guarantor:: {}", existingCreditDetailList.size());
                List<ProposeCreditDetailView> proposeCreditDetailViewList = proposeCreditDetailTransform(newCreditDetailList, existingCreditDetailList);
                newGuarantorDetailView.setProposeCreditDetailViewList(proposeCreditDetailViewList);

            }

            newGuarantorDetailViews.add(newGuarantorDetailView);
        }

        return newGuarantorDetailViews;
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

}
