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
            ncbDetail.setNoOfMonthPayment(NCBDetailView.getNoOfmonthsPayment());
            ncbDetail.setLimit(NCBDetailView.getLimit());
            ncbDetail.setMonth1(NCBDetailView.getMonth1());
            ncbDetail.setMonth2(NCBDetailView.getMonth2());
            ncbDetail.setMonth3(NCBDetailView.getMonth3());
            ncbDetail.setMonth4(NCBDetailView.getMonth4());
            ncbDetail.setMonth5(NCBDetailView.getMonth5());
            ncbDetail.setMonth6(NCBDetailView.getMonth6());
            ncbDetail.setWcFlag(NCBDetailView.getWcFlag());
            ncbDetail.setOverLimit(NCBDetailView.getNoOfOverLimit());
            ncbDetail.setCanToEdit(NCBDetailView.isCanToEdit());
            NCBDetailList.add(ncbDetail);
        }
        return NCBDetailList;
    }

    public List<NCBDetailView> transformToView(List<NCBDetail> ncbDetailList) {
        List<NCBDetailView> ncbDetailViewList = new ArrayList<NCBDetailView>();

        for (NCBDetail ncbDetail : ncbDetailList) {
            NCBDetailView ncbDetailView = new NCBDetailView();
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
            ncbDetailView.setNoOfmonthsPayment(ncbDetail.getNoOfMonthPayment());
            ncbDetailView.setLimit(ncbDetail.getLimit());
            ncbDetailView.setMonth1(ncbDetail.getMonth1());
            ncbDetailView.setMonth2(ncbDetail.getMonth2());
            ncbDetailView.setMonth3(ncbDetail.getMonth3());
            ncbDetailView.setMonth4(ncbDetail.getMonth4());
            ncbDetailView.setMonth5(ncbDetail.getMonth5());
            ncbDetailView.setMonth6(ncbDetail.getMonth6());
            ncbDetailView.setWcFlag(ncbDetail.getWcFlag());
            ncbDetailView.setNoOfOverLimit(ncbDetail.getOverLimit());
            ncbDetailView.setCanToEdit(ncbDetail.isCanToEdit());
            ncbDetailViewList.add(ncbDetailView);
        }

        return ncbDetailViewList;

    }

}
