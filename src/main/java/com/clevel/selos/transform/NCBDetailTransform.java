package com.clevel.selos.transform;

import com.clevel.selos.dao.master.AccountStatusDAO;
import com.clevel.selos.dao.master.AccountTypeDAO;
import com.clevel.selos.dao.master.SettlementStatusDAO;
import com.clevel.selos.model.RadioValue;
import com.clevel.selos.model.db.working.NCB;
import com.clevel.selos.model.db.working.NCBDetail;
import com.clevel.selos.model.view.NCBDetailView;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;


public class NCBDetailTransform extends Transform {

    @Inject
    SettlementStatusDAO settlementStatusDAO;
    @Inject
    AccountStatusDAO accountStatusDAO;
    @Inject
    AccountTypeDAO accountTypeDAO;

    public List<NCBDetail> transformToModel(List<NCBDetailView> ncbDetailViewList, NCB ncb) {
        List<NCBDetail> ncbDetailList = new ArrayList<NCBDetail>();
        NCBDetail ncbDetail;

        for (NCBDetailView ncbDetailViewItem : ncbDetailViewList) {
            ncbDetail = new NCBDetail();
            ncbDetail.setNcb(ncb);

            ncbDetail.setAccountOpenDate(ncbDetailViewItem.getAccountOpenDate());
            ncbDetail.setAccountTMBFlag(ncbDetailViewItem.getTMBAccount());

            if(ncbDetailViewItem.getAccountStatus() != null && ncbDetailViewItem.getAccountStatus().getId() != 0){
                ncbDetail.setAccountStatus(accountStatusDAO.findById(ncbDetailViewItem.getAccountStatus().getId()));
            } else {
                ncbDetail.setAccountStatus(null);
            }

            if(ncbDetailViewItem.getAccountType() != null && ncbDetailViewItem.getAccountType().getId() != 0){
                ncbDetail.setAccountType(accountTypeDAO.findById(ncbDetailViewItem.getAccountType().getId()));
            } else {
                ncbDetail.setAccountType(null);
            }

            if (ncbDetailViewItem.getCurrentPayment() != null && ncbDetailViewItem.getCurrentPayment().getId() != 0) {
                ncbDetail.setCurrentPayment(settlementStatusDAO.findById(ncbDetailViewItem.getCurrentPayment().getId()));
            } else {
                ncbDetail.setCurrentPayment(null);
            }

            if (ncbDetailViewItem.getHistoryPayment() != null && ncbDetailViewItem.getHistoryPayment().getId() != 0) {
                ncbDetail.setHistorySixPayment(settlementStatusDAO.findById(ncbDetailViewItem.getHistoryPayment().getId()));
                ncbDetail.setHistoryTwelvePayment(settlementStatusDAO.findById(ncbDetailViewItem.getHistoryPayment().getId()));
            } else {
                ncbDetail.setHistorySixPayment(null);
                ncbDetail.setHistoryTwelvePayment(null);
            }

            ncbDetail.setAsOfDate(ncbDetailViewItem.getDateOfInfo());
            ncbDetail.setLastReStructureDate(ncbDetailViewItem.getDateOfDebtRestructuring());
            ncbDetail.setInstallment(ncbDetailViewItem.getInstallment());
            ncbDetail.setRefinanceFlag(ncbDetailViewItem.getRefinanceCheck() == true ? 2:1);
            ncbDetail.setOutstanding(ncbDetailViewItem.getOutstanding());
            ncbDetail.setOutstandingIn12Month(ncbDetailViewItem.getNoOfOutstandingPaymentIn12months());
            ncbDetail.setLimit(ncbDetailViewItem.getLimit());
            ncbDetail.setWcFlag(ncbDetailViewItem.getWcCheck() == true ? 2:1);
            ncbDetail.setOverLimit(ncbDetailViewItem.getNoOfOverLimit());
            ncbDetail.setCanToEdit(ncbDetailViewItem.isCanToEdit());

            //Add new 2 flag for BRMS//
            ncbDetail.setTdrFlag(ncb.getTdrFlag());

            ncbDetailList.add(ncbDetail);
        }
        return ncbDetailList;
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
        ncbDetailView.setHistoryPayment(ncbDetail.getHistorySixPayment());
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

        //SET VALUE FOR CHECKBOX
        if(ncbDetail.getAccountTMBFlag() == RadioValue.YES.value()){
            ncbDetailView.setTmbCheck(true);
        } else {
            ncbDetailView.setTmbCheck(false);
        }

        if(ncbDetail.getRefinanceFlag() == RadioValue.YES.value()){
            ncbDetailView.setRefinanceCheck(true);
        } else {
            ncbDetailView.setRefinanceCheck(false);
        }

        if(ncbDetail.getWcFlag() == RadioValue.YES.value()){
            ncbDetailView.setWcCheck(true);
        } else {
            ncbDetailView.setWcCheck(false);
        }

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
