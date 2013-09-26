package com.clevel.selos.transform;

import com.clevel.selos.model.db.working.NCB;
import com.clevel.selos.model.db.working.NCBDetail;
import com.clevel.selos.model.view.NCBDetailView;

import java.util.ArrayList;
import java.util.List;


public class NCBDetailTransform extends Transform{

    public List<NCBDetail> transformToModel(List<NCBDetailView> NCBDetailViewList,NCB ncb){
        List<NCBDetail> NCBDetailList = new ArrayList<NCBDetail>();

        for(NCBDetailView NCBDetailView : NCBDetailViewList){
            NCBDetail ncbDetail = new NCBDetail();
            ncbDetail.setId(NCBDetailView.getId());
            ncbDetail.setNcb(ncb);
            ncbDetail.setAccountStatus(NCBDetailView.getAccountStatus());
            ncbDetail.setAccountType(NCBDetailView.getAccountType());
            ncbDetail.setAccountOpenDate(NCBDetailView.getAccountOpenDate());
            ncbDetail.setAccountTBMFlag(NCBDetailView.isTMBAccount());
            ncbDetail.setCurrentPayment(NCBDetailView.getCurrentPayment());
            ncbDetail.setHistoryPayment(NCBDetailView.getHistoryPayment());
            ncbDetail.setAsOfDate(NCBDetailView.getDateOfInfo());
            ncbDetail.setLastReStructureDate(NCBDetailView.getDateOfDebtRestructuring());
            ncbDetail.setInstallment(NCBDetailView.getInstallment());
            ncbDetail.setRefinanceFlag(NCBDetailView.isRefinanceFlag());
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

            NCBDetailList.add(ncbDetail);
        }


        return NCBDetailList;
    }

    public List<NCBDetailView> transformToView(List<NCBDetail> ncbDetailList ,NCB ncb){

        return null;

    }

}
