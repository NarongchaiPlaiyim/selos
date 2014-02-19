package com.clevel.selos.transform;


import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.working.CreditTypeDetail;
import com.clevel.selos.model.db.working.NewCollateral;
import com.clevel.selos.model.db.working.NewGuarantorDetail;
import com.clevel.selos.model.view.CreditTypeDetailView;
import com.clevel.selos.util.Util;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CreditTypeDetailTransform extends Transform {

    @Inject
    public CreditTypeDetailTransform() {
    }

    public List<CreditTypeDetail> transformToModelForGuarantor(List<CreditTypeDetailView> creditTypeDetailViewList,NewGuarantorDetail newGuarantorDetail,User user){

        List<CreditTypeDetail> creditTypeDetailList = new ArrayList<CreditTypeDetail>();
        CreditTypeDetail creditTypeDetail;

        for (CreditTypeDetailView creditTypeDetailView : creditTypeDetailViewList) {
            creditTypeDetail = new CreditTypeDetail();
            if (creditTypeDetailView.getId() != 0) {
                creditTypeDetail.setId(creditTypeDetailView.getId());
                creditTypeDetail.setCreateDate(creditTypeDetailView.getCreateDate());
                creditTypeDetail.setCreateBy(creditTypeDetailView.getCreateBy());
            } else { // id = 0 create new
                creditTypeDetail.setCreateDate(new Date());
                creditTypeDetail.setCreateBy(user);
            }

            creditTypeDetail.setSeq(creditTypeDetailView.getSeq());
            creditTypeDetail.setNo(Util.returnNumForFlag(creditTypeDetailView.isNoFlag()));
            creditTypeDetail.setType(creditTypeDetailView.getType());
            creditTypeDetail.setRequestType(creditTypeDetailView.getRequestType());
            creditTypeDetail.setAccount(creditTypeDetailView.getAccountName());
            creditTypeDetail.setAccountNumber(creditTypeDetailView.getAccountNumber());
            creditTypeDetail.setAccountSuf(creditTypeDetailView.getAccountSuf());
            creditTypeDetail.setCreditFacility(creditTypeDetailView.getCreditFacility());
            creditTypeDetail.setGuaranteeAmount(creditTypeDetailView.getGuaranteeAmount());
            creditTypeDetail.setLimit(creditTypeDetailView.getLimit());
            creditTypeDetail.setProductProgram(creditTypeDetailView.getProductProgram());
            creditTypeDetail.setUseCount(creditTypeDetailView.getUseCount());
//            creditTypeDetail.setNewGuarantorDetail(newGuarantorDetail);
            creditTypeDetailList.add(creditTypeDetail);
        }

        return creditTypeDetailList;
    }

    public List<CreditTypeDetail> transformToModelForCollateral(List<CreditTypeDetailView> creditTypeDetailViewList,NewCollateral newCollateralDetail,User user){

        List<CreditTypeDetail> creditTypeDetailList = new ArrayList<CreditTypeDetail>();
        CreditTypeDetail creditTypeDetail;

        for (CreditTypeDetailView creditTypeDetailView : creditTypeDetailViewList) {
            creditTypeDetail = new CreditTypeDetail();
            if (creditTypeDetailView.getId() != 0) {
                creditTypeDetail.setId(creditTypeDetailView.getId());
                creditTypeDetail.setCreateDate(creditTypeDetailView.getCreateDate());
                creditTypeDetail.setCreateBy(creditTypeDetailView.getCreateBy());
            } else { // id = 0 create new
                creditTypeDetail.setCreateDate(new Date());
                creditTypeDetail.setCreateBy(user);
            }

            creditTypeDetail.setSeq(creditTypeDetailView.getSeq());
            creditTypeDetail.setNo(Util.returnNumForFlag(creditTypeDetailView.isNoFlag()));
            creditTypeDetail.setType(creditTypeDetailView.getType());
            creditTypeDetail.setRequestType(creditTypeDetailView.getRequestType());
            creditTypeDetail.setAccount(creditTypeDetailView.getAccountName());
            creditTypeDetail.setAccountNumber(creditTypeDetailView.getAccountNumber());
            creditTypeDetail.setAccountSuf(creditTypeDetailView.getAccountSuf());
            creditTypeDetail.setCreditFacility(creditTypeDetailView.getCreditFacility());
            creditTypeDetail.setGuaranteeAmount(creditTypeDetailView.getGuaranteeAmount());
            creditTypeDetail.setLimit(creditTypeDetailView.getLimit());
            creditTypeDetail.setProductProgram(creditTypeDetailView.getProductProgram());
            creditTypeDetail.setUseCount(creditTypeDetailView.getUseCount());
//            creditTypeDetail.setNewCollateralDetail(newCollateralDetail);
            creditTypeDetailList.add(creditTypeDetail);
        }

        return creditTypeDetailList;
    }

    public List<CreditTypeDetailView> transformToView(List<CreditTypeDetail> creditTypeDetailList) {
        List<CreditTypeDetailView> creditTypeDetailViewList = new ArrayList<CreditTypeDetailView>();
        CreditTypeDetailView creditTypeDetailView;
        
        for(CreditTypeDetail creditTypeDetail :creditTypeDetailList){
        	creditTypeDetailView = new CreditTypeDetailView();
            creditTypeDetailView.setCreateDate(creditTypeDetail.getCreateDate());
            creditTypeDetailView.setCreateBy(creditTypeDetail.getCreateBy());
            creditTypeDetailView.setModifyDate(creditTypeDetail.getModifyDate());
            creditTypeDetailView.setModifyBy(creditTypeDetail.getModifyBy());
            creditTypeDetailView.setSeq(creditTypeDetail.getSeq());
            creditTypeDetailView.setNoFlag(Util.isTrue(creditTypeDetail.getNo()));
            creditTypeDetailView.setType(creditTypeDetail.getType());
            creditTypeDetailView.setRequestType(creditTypeDetail.getRequestType());
            creditTypeDetailView.setAccountName(creditTypeDetail.getAccount());
            creditTypeDetailView.setAccountNumber(creditTypeDetail.getAccountNumber());
            creditTypeDetailView.setAccountSuf(creditTypeDetail.getAccountSuf());
            creditTypeDetailView.setCreditFacility(creditTypeDetail.getCreditFacility());
            creditTypeDetailView.setGuaranteeAmount(creditTypeDetail.getGuaranteeAmount());
            creditTypeDetailView.setLimit(creditTypeDetail.getLimit());
            creditTypeDetailView.setProductProgram(creditTypeDetail.getProductProgram());
            creditTypeDetailView.setUseCount(creditTypeDetail.getUseCount());
        	creditTypeDetailViewList.add(creditTypeDetailView);  
        }
        return creditTypeDetailViewList;
    }
    
  
}
