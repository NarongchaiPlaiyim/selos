package com.clevel.selos.transform;

import com.clevel.selos.dao.working.CustomerDAO;
import com.clevel.selos.dao.working.NewGuarantorDetailDAO;
import com.clevel.selos.dao.working.NewGuarantorRelationDAO;
import com.clevel.selos.model.ProposeType;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.working.*;
import com.clevel.selos.model.view.*;
import com.clevel.selos.util.Util;
import org.joda.time.DateTime;

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
    NewGuarantorDetailDAO newGuarantorDetailDAO;
    @Inject
    NewGuarantorRelationDAO newGuarantorRelationDAO;
    @Inject
    NewCreditDetailTransform newCreditDetailTransform;
    @Inject
    ExistingCreditDetailTransform existingCreditDetailTransform;
    @Inject
    NewGuarantorCreditTransform newGuarantorCreditTransform;
    @Inject
    ProposeCreditDetailTransform proposeCreditDetailTransform;


    public List<NewGuarantorDetail> transformToModel(List<NewGuarantorDetailView> newGuarantorDetailViewList, NewCreditFacility newCreditFacility, User user, ProposeType proposeType) {

        List<NewGuarantorDetail> newGuarantorDetailList = new ArrayList<NewGuarantorDetail>();
        NewGuarantorDetail newGuarantorDetail;

        for (NewGuarantorDetailView newGuarantorDetailView : newGuarantorDetailViewList) {
            log.debug("Start.. transformToModel newGuarantorDetailView : {}", newGuarantorDetailView);
            newGuarantorDetail = new NewGuarantorDetail();
            if (newGuarantorDetailView.getId() != 0) {
                newGuarantorDetail = newGuarantorDetailDAO.findById(newGuarantorDetailView.getId());
                newGuarantorDetail.setModifyDate(DateTime.now().toDate());
                newGuarantorDetail.setModifyBy(user);
            } else { // id = 0 create new
                newGuarantorDetail.setCreateDate(new Date());
                newGuarantorDetail.setCreateBy(user);
            }
            newGuarantorDetail.setProposeType(proposeType);
            Customer guarantor = customerDAO.findById(newGuarantorDetailView.getGuarantorName().getId());
            newGuarantorDetail.setGuarantorName(guarantor);
            newGuarantorDetail.setGuarantorCategory(newGuarantorDetailView.getGuarantorCategory());
            newGuarantorDetail.setTcgLgNo(newGuarantorDetailView.getTcgLgNo());
            newGuarantorDetail.setNewCreditFacility(newCreditFacility);
            newGuarantorDetail.setTotalLimitGuaranteeAmount(newGuarantorDetailView.getTotalLimitGuaranteeAmount());
            newGuarantorDetail.setUwDecision(newGuarantorDetailView.getUwDecision());

            if (Util.safetyList(newGuarantorDetailView.getProposeCreditDetailViewList()).size() > 0) {
                log.debug("Start.. transformToModel proposeCreditDetailViewList : {}", newGuarantorDetailView.getProposeCreditDetailViewList());
                List<NewGuarantorCredit> newGuarantorCreditList = newGuarantorCreditTransform.transformsToModelForGuarantor(newGuarantorDetailView.getProposeCreditDetailViewList(), newCreditFacility.getNewCreditDetailList(), newGuarantorDetail, user);
                log.debug("End.. transformToModel newGuarantorCreditList size :: {}", newGuarantorCreditList.size());
                newGuarantorDetail.setNewGuarantorCreditList(newGuarantorCreditList);
            }

            log.debug("End.. transformToModel newGuarantorDetail : {}", newGuarantorDetail);
            newGuarantorDetailList.add(newGuarantorDetail);
        }

        return newGuarantorDetailList;
    }

    public List<NewGuarantorDetailView> transformToView(List<NewGuarantorDetail> newGuarantorDetailList) {
        List<NewGuarantorDetailView> newGuarantorDetailViews = new ArrayList<NewGuarantorDetailView>();
        NewGuarantorDetailView newGuarantorDetailView;

        for (NewGuarantorDetail newGuarantorDetail : newGuarantorDetailList) {
            newGuarantorDetailView = new NewGuarantorDetailView();
            newGuarantorDetailView.setId(newGuarantorDetail.getId());
            newGuarantorDetailView.setProposeType(newGuarantorDetail.getProposeType());
            CustomerInfoView guarantorView = customerTransform.transformToView(newGuarantorDetail.getGuarantorName());
            newGuarantorDetailView.setCreateDate(newGuarantorDetail.getCreateDate());
            newGuarantorDetailView.setCreateBy(newGuarantorDetail.getCreateBy());
            newGuarantorDetailView.setModifyDate(newGuarantorDetail.getModifyDate());
            newGuarantorDetailView.setModifyBy(newGuarantorDetail.getModifyBy());
            newGuarantorDetailView.setGuarantorName(guarantorView);
            newGuarantorDetailView.setTcgLgNo(newGuarantorDetail.getTcgLgNo());
            newGuarantorDetailView.setGuarantorCategory(newGuarantorDetail.getGuarantorCategory());
            newGuarantorDetailView.setTotalLimitGuaranteeAmount(newGuarantorDetail.getTotalLimitGuaranteeAmount());
            newGuarantorDetailView.setUwDecision(newGuarantorDetail.getUwDecision());

            List<NewGuarantorCredit> newGuarantorCreditList = newGuarantorRelationDAO.getListGuarantorRelationByNewGuarantor(newGuarantorDetail);
            log.info("newGuarantorCreditList :: {}", newGuarantorCreditList.size());
            List<NewCreditDetail> newCreditDetailList = new ArrayList<NewCreditDetail>();
            List<ExistingCreditDetail> existingCreditDetailList = new ArrayList<ExistingCreditDetail>();

            for (NewGuarantorCredit newGuarantorCredit : newGuarantorCreditList) {
                if (newGuarantorCredit.getExistingCreditDetail() != null) {
                    log.info("newGuarantorCredit.getExistingCreditDetail :: {}", newGuarantorCredit.getExistingCreditDetail().getId());
                    existingCreditDetailList.add(newGuarantorCredit.getExistingCreditDetail());
                } else if (newGuarantorCredit.getNewCreditDetail() != null) {
                    log.info("newGuarantorCredit.getNewCreditDetail :: {}", newGuarantorCredit.getNewCreditDetail().getId());
                    newCreditDetailList.add(newGuarantorCredit.getNewCreditDetail());
                }
            }

            log.info("newCreditDetailList Guarantor:: {}", newCreditDetailList.size());
            log.info("getExistingCreditDetail Guarantor:: {}", existingCreditDetailList.size());
            List<ProposeCreditDetailView> proposeCreditDetailViewList = proposeCreditDetailTransform(newCreditDetailList, existingCreditDetailList, newGuarantorCreditList);
            newGuarantorDetailView.setProposeCreditDetailViewList(proposeCreditDetailViewList);

            newGuarantorDetailViews.add(newGuarantorDetailView);

        }

        return newGuarantorDetailViews;
    }

    public NewGuarantorDetailView copyToNewView(NewGuarantorDetailView originalNewGuarantorDetailView, ProposeType proposeType, boolean isNewId) {
        NewGuarantorDetailView newGuarantorDetailView = new NewGuarantorDetailView();
        if (originalNewGuarantorDetailView != null) {
            newGuarantorDetailView = new NewGuarantorDetailView();
            newGuarantorDetailView.setId(isNewId ? 0 : originalNewGuarantorDetailView.getId());
            newGuarantorDetailView.setProposeType(proposeType);
            newGuarantorDetailView.setGuarantorName(originalNewGuarantorDetailView.getGuarantorName());
            newGuarantorDetailView.setTcgLgNo(originalNewGuarantorDetailView.getTcgLgNo());
            newGuarantorDetailView.setGuarantorCategory(originalNewGuarantorDetailView.getGuarantorCategory());
            newGuarantorDetailView.setTotalLimitGuaranteeAmount(originalNewGuarantorDetailView.getTotalLimitGuaranteeAmount());
            newGuarantorDetailView.setUwDecision(originalNewGuarantorDetailView.getUwDecision());
            newGuarantorDetailView.setCreateDate(originalNewGuarantorDetailView.getCreateDate());
            newGuarantorDetailView.setCreateBy(originalNewGuarantorDetailView.getCreateBy());
            newGuarantorDetailView.setModifyDate(originalNewGuarantorDetailView.getModifyDate());
            newGuarantorDetailView.setModifyBy(originalNewGuarantorDetailView.getModifyBy());
            newGuarantorDetailView.setProposeCreditDetailViewList(proposeCreditDetailTransform.copyToNewViews(originalNewGuarantorDetailView.getProposeCreditDetailViewList(), isNewId));
        }
        return newGuarantorDetailView;
    }

    public List<NewGuarantorDetailView> copyToNewViews(List<NewGuarantorDetailView> originalNewGuarantorDetailViews, ProposeType proposeType, boolean isNewId) {
        List<NewGuarantorDetailView> newGuarantorDetailViewList = new ArrayList<NewGuarantorDetailView>();
        if (originalNewGuarantorDetailViews != null && originalNewGuarantorDetailViews.size() > 0) {
            for (NewGuarantorDetailView originalGuarantorDetailView : originalNewGuarantorDetailViews) {
                newGuarantorDetailViewList.add(copyToNewView(originalGuarantorDetailView, proposeType, isNewId));
            }
        }
        return newGuarantorDetailViewList;
    }

    public List<ProposeCreditDetailView> proposeCreditDetailTransform(List<NewCreditDetail> newCreditDetailList, List<ExistingCreditDetail> existingCreditDetailList, List<NewGuarantorCredit> newGuarantorCreditList) {
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
                log.info("newGuarantorCreditList.get(i).getNewCreditDetail() ::: {}",findNewGuarantorCredit(newGuarantorCreditList,tmp).getGuaranteeAmount());
                proposeCreditDetailView.setGuaranteeAmount(findNewGuarantorCredit(newGuarantorCreditList,tmp).getGuaranteeAmount());
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
            proposeCreditDetailView.setNoFlag(true);
            proposeCreditDetailView.setTypeOfStep("E");
            proposeCreditDetailView.setAccountName(existingCreditDetailView.getAccountName());
            proposeCreditDetailView.setAccountNumber(existingCreditDetailView.getAccountNumber());
            proposeCreditDetailView.setAccountSuf(existingCreditDetailView.getAccountSuf());
            proposeCreditDetailView.setProductProgramView(existingCreditDetailView.getExistProductProgramView());
            proposeCreditDetailView.setCreditFacilityView(existingCreditDetailView.getExistCreditTypeView());
            proposeCreditDetailView.setLimit(existingCreditDetailView.getLimit());

            for (int i = 0; i < newGuarantorCreditList.size(); i++) {
                if (existingCreditDetailView.getSeq() == newGuarantorCreditList.get(i).getExistingCreditDetail().getSeq()) {
                    log.info("newGuarantorCreditList.get(i).getNewCreditDetail() ::: {}",newGuarantorCreditList.get(i).getGuaranteeAmount());
                    proposeCreditDetailView.setGuaranteeAmount(newGuarantorCreditList.get(i).getGuaranteeAmount());
                }
            }

            proposeCreditDetailViewList.add(proposeCreditDetailView);

            rowCount++;
        }

        return proposeCreditDetailViewList;
    }

    public NewGuarantorCredit findNewGuarantorCredit(List<NewGuarantorCredit> newGuarantorCreditList , NewCreditDetailView newCreditDetail){
        NewGuarantorCredit newGuarantorCreditReturn = new NewGuarantorCredit();
           for(NewGuarantorCredit newGuarantorCredit : newGuarantorCreditList){
              if(newGuarantorCredit.getNewCreditDetail().getSeq()==newCreditDetail.getSeq()){
                  newGuarantorCreditReturn = newGuarantorCredit;
                  break;
              }
           }
        log.info("newGuarantorCreditReturn ::: {}",newGuarantorCreditReturn.getGuaranteeAmount());
        return newGuarantorCreditReturn;
    }

}
