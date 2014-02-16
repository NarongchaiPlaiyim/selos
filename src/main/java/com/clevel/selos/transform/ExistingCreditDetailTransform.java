package com.clevel.selos.transform;

import com.clevel.selos.model.db.master.BankAccountStatus;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.working.ExistingCreditDetail;
import com.clevel.selos.model.db.working.ExistingCreditFacility;
import com.clevel.selos.model.view.ExistingCreditDetailView;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ExistingCreditDetailTransform extends Transform {

    @Inject
    ProductTransform productTransform;

    @Inject
    public ExistingCreditDetailTransform() {
    }

    public List<ExistingCreditDetail> transformsToModel(List<ExistingCreditDetailView> existingCreditDetailViewList, ExistingCreditFacility existingCreditFacility, User user){
        log.debug("transformsToModel");
        List<ExistingCreditDetail> existingCreditDetailList = new ArrayList<ExistingCreditDetail>();
        ExistingCreditDetail existingCreditDetail;

        for (ExistingCreditDetailView existingCreditDetailView : existingCreditDetailViewList) {
            existingCreditDetail = new ExistingCreditDetail();
            if (existingCreditDetailView.getId() != 0) {
                //existingCreditDetail.setId(existingCreditDetailView.getId());
                existingCreditDetail.setCreateDate(existingCreditDetailView.getCreateDate());
                existingCreditDetail.setCreateBy(existingCreditDetailView.getCreateBy());
            } else { // id = 0 create new
                existingCreditDetail.setCreateDate(new Date());
                existingCreditDetail.setCreateBy(user);
            }

            existingCreditDetail.setNo(existingCreditDetailView.getNo());
            existingCreditDetail.setBorrowerType(existingCreditDetailView.getBorrowerType());
            existingCreditDetail.setExistingCreditFrom(existingCreditDetailView.getExistingCreditFrom());

            log.debug(" existingCreditDetailView seq is {}",existingCreditDetailView.getSeq());
            existingCreditDetail.setSeq(existingCreditDetailView.getSeq());
            log.debug(" existingCreditDetail seq is {}",existingCreditDetail.getSeq());

            existingCreditDetail.setInUsed(existingCreditDetailView.getInUsed());
            existingCreditDetail.setCreateDate(existingCreditDetailView.getCreateDate());
            existingCreditDetail.setCreateBy(existingCreditDetailView.getCreateBy());
            existingCreditDetail.setModifyDate(new Date());
            existingCreditDetail.setModifyBy(user);
            existingCreditDetail.setAccountNumber(existingCreditDetailView.getAccountNumber());
            existingCreditDetail.setAccountName(existingCreditDetailView.getAccountName());
            existingCreditDetail.setAccountSuf(existingCreditDetailView.getAccountSuf());
            existingCreditDetail.setExistAccountStatus(existingCreditDetailView.getExistAccountStatus());
            existingCreditDetail.setExistProductProgram(productTransform.transformToModel(existingCreditDetailView.getExistProductProgramView()));
            existingCreditDetail.setExistCreditType(productTransform.transformToModel(existingCreditDetailView.getExistCreditTypeView()));
            existingCreditDetail.setProductProgram(existingCreditDetailView.getProductProgram());
            existingCreditDetail.setCreditType(existingCreditDetailView.getCreditType());
            existingCreditDetail.setProductProgram(existingCreditDetailView.getProductProgram());
            existingCreditDetail.setInstallment(existingCreditDetailView.getInstallment());
            existingCreditDetail.setLimit(existingCreditDetailView.getLimit());
            existingCreditDetail.setOutstanding(existingCreditDetailView.getOutstanding());
            existingCreditDetail.setPceLimit(existingCreditDetailView.getPceLimit());
            existingCreditDetail.setPcePercent(existingCreditDetailView.getPcePercent());
            existingCreditDetail.setProductCode(existingCreditDetailView.getProductCode());
            existingCreditDetail.setProjectCode(existingCreditDetailView.getProjectCode());
            existingCreditDetail.setTenor(existingCreditDetailView.getTenor());
            existingCreditDetail.setExistingCreditFacility(existingCreditFacility);

            existingCreditDetailList.add(existingCreditDetail);
        }

        return existingCreditDetailList;
    }

    public List<ExistingCreditDetailView> transformsToView(List<ExistingCreditDetail> existingCreditDetailList) {

        List<ExistingCreditDetailView> existingCreditDetailViewList = new ArrayList<ExistingCreditDetailView>();
        ExistingCreditDetailView existingCreditDetailView;

        for (ExistingCreditDetail existingCreditDetail : existingCreditDetailList) {
            existingCreditDetailView = new ExistingCreditDetailView();
            existingCreditDetailView.setId(existingCreditDetail.getId());
            existingCreditDetailView.setNo(existingCreditDetail.getNo());
            existingCreditDetailView.setBorrowerType(existingCreditDetail.getBorrowerType());
            existingCreditDetailView.setExistingCreditFrom(existingCreditDetail.getExistingCreditFrom());
            existingCreditDetailView.setSeq(existingCreditDetail.getSeq());
            existingCreditDetailView.setInUsed(existingCreditDetail.getInUsed());
            existingCreditDetailView.setCreateDate(existingCreditDetail.getCreateDate());
            existingCreditDetailView.setCreateBy(existingCreditDetail.getCreateBy());
            existingCreditDetailView.setModifyDate(existingCreditDetail.getModifyDate());
            existingCreditDetailView.setModifyBy(existingCreditDetail.getModifyBy());
            existingCreditDetailView.setAccountNumber(existingCreditDetail.getAccountNumber());
            existingCreditDetailView.setAccountName(existingCreditDetail.getAccountName());
            existingCreditDetailView.setAccountSuf(existingCreditDetail.getAccountSuf());
            existingCreditDetailView.setExistAccountStatus(existingCreditDetail.getExistAccountStatus());
            existingCreditDetailView.setExistProductProgramView(productTransform.transformToView(existingCreditDetail.getExistProductProgram()));
            existingCreditDetailView.setExistCreditTypeView(productTransform.transformToView(existingCreditDetail.getExistCreditType()));
            existingCreditDetailView.setProductProgram(existingCreditDetail.getProductProgram());
            existingCreditDetailView.setCreditType(existingCreditDetail.getCreditType());
            existingCreditDetailView.setProductProgram(existingCreditDetail.getProductProgram());
            existingCreditDetailView.setInstallment(existingCreditDetail.getInstallment());
            existingCreditDetailView.setLimit(existingCreditDetail.getLimit());
            existingCreditDetailView.setOutstanding(existingCreditDetail.getOutstanding());
            existingCreditDetailView.setPceLimit(existingCreditDetail.getPceLimit());
            existingCreditDetailView.setPcePercent(existingCreditDetail.getPcePercent());
            existingCreditDetailView.setProductCode(existingCreditDetail.getProductCode());
            existingCreditDetailView.setProjectCode(existingCreditDetail.getProjectCode());
            existingCreditDetailView.setTenor(existingCreditDetail.getTenor());

            /*existingCreditDetailView.setPurpose(existingCreditDetail.getPurpose());
            existingCreditDetailView.setReduceFrontEndFee(existingCreditDetail.getReduceFrontEndFee());
            existingCreditDetailView.setReducePriceFlag(existingCreditDetail.getReducePriceFlag());
            existingCreditDetailView.setRemark(existingCreditDetail.getRemark());
            existingCreditDetailView.setStandardInterest(existingCreditDetail.getStandardInterest());
            existingCreditDetailView.setStandardBasePrice(existingCreditDetail.getStandardBasePrice());
            existingCreditDetailView.setStandardPrice(existingCreditDetail.getStandardPrice());
            existingCreditDetailView.setSuggestInterest(existingCreditDetail.getSuggestInterest());
            existingCreditDetailView.setSuggestBasePrice(existingCreditDetail.getSuggestBasePrice());
            existingCreditDetailView.setSuggestPrice(existingCreditDetail.getSuggestPrice());*/

            existingCreditDetailViewList.add(existingCreditDetailView);
        }

        return existingCreditDetailViewList;
    }
}
