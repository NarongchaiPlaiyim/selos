package com.clevel.selos.transform;

import com.clevel.selos.model.db.working.NCB;
import com.clevel.selos.model.db.working.NCBDetail;
import com.clevel.selos.model.view.NcbRecordView;

import java.util.ArrayList;
import java.util.List;

public class NCBDetailTransform extends Transform{

    public List<NCBDetail> transformToModel(List<NcbRecordView> ncbRecordViewList ,NCB ncb){
        List<NCBDetail> NCBDetailList = new ArrayList<NCBDetail>();

        for(NcbRecordView ncbRecordView : ncbRecordViewList){
            NCBDetail ncbDetail = new NCBDetail();
            ncbDetail.setId(ncbRecordView.getId());
            ncbDetail.setNcb(ncb);
            ncbDetail.setAccountStatus(ncbRecordView.getAccountStatus());
            ncbDetail.setAccountType(ncbRecordView.getAccountType());
            ncbDetail.setAccountOpenDate(ncbRecordView.getAccountOpenDate());
            ncbDetail.setAccountTBMFlag(ncbRecordView.isTMBAccount());
            ncbDetail.setCurrentPayment(ncbRecordView.getCurrentPayment());
            ncbDetail.setHistoryPayment(ncbRecordView.getHistoryPayment());
            ncbDetail.setAsOfDate(ncbRecordView.getDateOfInfo());
            ncbDetail.setLastReStructureDate(ncbRecordView.getDateOfDebtRestructuring());
            ncbDetail.setInstallment(ncbRecordView.getInstallment());
            ncbDetail.setRefinanceFlag(ncbRecordView.isRefinanceFlag());
            ncbDetail.setOutstanding(ncbRecordView.getOutstanding());
            ncbDetail.setOutstandingIn12Month(ncbRecordView.getNoOfOutstandingPaymentIn12months());
            ncbDetail.setNoOfMonthPayment(ncbRecordView.getNoOfmonthsPayment());
            ncbDetail.setLimit(ncbRecordView.getLimit());
            ncbDetail.setMonth1(ncbRecordView.getMonth1());
            ncbDetail.setMonth2(ncbRecordView.getMonth2());
            ncbDetail.setMonth3(ncbRecordView.getMonth3());
            ncbDetail.setMonth4(ncbRecordView.getMonth4());
            ncbDetail.setMonth5(ncbRecordView.getMonth5());
            ncbDetail.setMonth6(ncbRecordView.getMonth6());

            NCBDetailList.add(ncbDetail);
        }


        return NCBDetailList;
    }

    public List<NcbRecordView> transformToView(List<NCBDetail> ncbDetailList ,NCB ncb){

        return null;

    }

}
