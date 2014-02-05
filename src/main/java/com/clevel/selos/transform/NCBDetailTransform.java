package com.clevel.selos.transform;

import com.clevel.selos.model.db.working.NCB;
import com.clevel.selos.model.db.working.NCBDetail;
import com.clevel.selos.model.view.NCBDetailView;

import java.util.ArrayList;
import java.util.List;


public class NCBDetailTransform extends Transform {

    public List<NCBDetail> transformToModel(List<NCBDetailView> NCBDetailViewList, NCB ncb) {
        List<NCBDetail> NCBDetailList = new ArrayList<NCBDetail>();

        for (NCBDetailView NCBDetailView : NCBDetailViewList) {
            NCBDetail ncbDetail = new NCBDetail();
            ncbDetail.setNcb(ncb);
            ncbDetail.setAccountStatus(NCBDetailView.getAccountStatus());
            ncbDetail.setAccountType(NCBDetailView.getAccountType());
            ncbDetail.setAccountOpenDate(NCBDetailView.getAccountOpenDate());
            ncbDetail.setAccountTMBFlag(NCBDetailView.getTMBAccount());
            if (NCBDetailView.getCurrentPayment() != null && NCBDetailView.getCurrentPayment().getId() != 0) {
                ncbDetail.setCurrentPayment(NCBDetailView.getCurrentPayment());
            } else {
                ncbDetail.setCurrentPayment(null);
            }
            if (NCBDetailView.getHistoryPayment() != null && NCBDetailView.getHistoryPayment().getId() != 0) {
                ncbDetail.setHistoryPayment(NCBDetailView.getHistoryPayment());
            } else {
                ncbDetail.setHistoryPayment(null);
            }
            ncbDetail.setAsOfDate(NCBDetailView.getDateOfInfo());
            ncbDetail.setLastReStructureDate(NCBDetailView.getDateOfDebtRestructuring());
            ncbDetail.setInstallment(NCBDetailView.getInstallment());
            ncbDetail.setRefinanceFlag(NCBDetailView.getRefinanceFlag());
            ncbDetail.setOutstanding(NCBDetailView.getOutstanding());
            ncbDetail.setOutstandingIn12Month(NCBDetailView.getNoOfOutstandingPaymentIn12months());
            ncbDetail.setLimit(NCBDetailView.getLimit());
            ncbDetail.setWcFlag(NCBDetailView.getWcFlag());
            ncbDetail.setOverLimit(NCBDetailView.getNoOfOverLimit());
            ncbDetail.setCanToEdit(NCBDetailView.isCanToEdit());
            NCBDetailList.add(ncbDetail);
        }
        return NCBDetailList;
    }

    public NCBDetailView transformToView(NCBDetail ncbDetail){
        NCBDetailView ncbDetailView = new NCBDetailView();
        if(ncbDetail == null){
            return ncbDetailView;
        }
        ncbDetailView.setAccountStatus(ncbDetail.getAccountStatus());
        ncbDetailView.setAccountType(ncbDetail.getAccountType());
        ncbDetailView.setAccountOpenDate(ncbDetail.getAccountOpenDate());
        ncbDetailView.setTMBAccount(ncbDetail.getAccountTMBFlag());
        ncbDetailView.setCurrentPayment(ncbDetail.getCurrentPayment());
        ncbDetailView.setHistoryPayment(ncbDetail.getHistoryPayment());
        ncbDetailView.setDateOfInfo(ncbDetail.getAsOfDate());
        ncbDetailView.setDateOfDebtRestructuring(ncbDetail.getLastReStructureDate());
        ncbDetailView.setInstallment(ncbDetail.getInstallment());
        ncbDetailView.setRefinanceFlag(ncbDetail.getRefinanceFlag());
        ncbDetailView.setOutstanding(ncbDetail.getOutstanding());
        ncbDetailView.setNoOfOutstandingPaymentIn12months(ncbDetail.getOutstandingIn12Month());
        ncbDetailView.setLimit(ncbDetail.getLimit());
        ncbDetailView.setWcFlag(ncbDetail.getWcFlag());
        ncbDetailView.setNoOfOverLimit(ncbDetail.getOverLimit());
        ncbDetailView.setCanToEdit(ncbDetail.isCanToEdit());
        return ncbDetailView;
    }

    public List<NCBDetailView> transformToViews(List<NCBDetail> ncbDetailList) {
        List<NCBDetailView> ncbDetailViewList = new ArrayList<NCBDetailView>();
        if(ncbDetailList == null && ncbDetailList.isEmpty()){
            return ncbDetailViewList;
        }
        for (NCBDetail ncbDetail : ncbDetailList) {
            ncbDetailViewList.add(transformToView(ncbDetail));
        }
        return ncbDetailViewList;
    }



}
