package com.clevel.selos.transform;

import com.clevel.selos.model.db.working.NCB;
import com.clevel.selos.model.view.NCBInfoView;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;


public class NCBTransform extends Transform {


    @Inject
    LoanAccountTypeTransform loanAccountTypeTransform;
    @Inject
    CustomerTransform customerTransform;

    public NCB transformToModel(NCBInfoView NCBInfoView) {
        NCB ncb = new NCB();

        if (NCBInfoView.getId() != 0) {
            ncb.setId(NCBInfoView.getId());
        }

        ncb.setActive(true);
        ncb.setCreateBy(NCBInfoView.getCreateBy());
        ncb.setModifyBy(NCBInfoView.getModifyBy());
        ncb.setCreateDate(NCBInfoView.getCreateDate());
        ncb.setModifyDate(NCBInfoView.getModifyDate());
        ncb.setCustomer(NCBInfoView.getCustomer());
        ncb.setCheckIn6Month(NCBInfoView.getCheckIn6Month());
        ncb.setCheckingDate(NCBInfoView.getCheckingDate());
        ncb.setCurrentPaymentType(NCBInfoView.getCurrentPaymentType());
        ncb.setHistoryPaymentType(NCBInfoView.getHistoryPaymentType());
        ncb.setNplFlag(NCBInfoView.getNplFlag());
        ncb.setNplOtherFlag(transFormBooleanToDb(NCBInfoView.isNplOtherFlag()));
        ncb.setNplOtherMonth(NCBInfoView.getNplOtherMonth());
        ncb.setNplOtherYear(NCBInfoView.getNplOtherYear());
        ncb.setNplTMBFlag(transFormBooleanToDb(NCBInfoView.isNplTMBFlag()));
        ncb.setNplTMBMonth(NCBInfoView.getNplTMBMonth());
        ncb.setNplTMBYear(NCBInfoView.getNplTMBYear());
        ncb.setPaymentClass(NCBInfoView.getPaymentClass());
        ncb.setPersonalId(NCBInfoView.getPersonalId());
        ncb.setRemark(NCBInfoView.getRemark());
        ncb.setTdrFlag(NCBInfoView.getTdrFlag());
        ncb.setTdrOhterFlag(transFormBooleanToDb(NCBInfoView.isTdrOtherFlag()));
        ncb.setTdrOtherMonth(NCBInfoView.getTdrOtherMonth());
        ncb.setTdrOtherYear(NCBInfoView.getTdrOtherYear());
        ncb.setTdrTMBFlag(transFormBooleanToDb(NCBInfoView.isTdrTMBFlag()));
        ncb.setTdrTMBMonth(NCBInfoView.getTdrTMBMonth());
        ncb.setTdrTMBYear(NCBInfoView.getTdrTMBYear());
        ncb.setNcbCusName(NCBInfoView.getNcbCusName());
        ncb.setEnquiry(NCBInfoView.getEnquiry());
        ncb.setNcbCusMarriageStatus(NCBInfoView.getNcbCusMarriageStatus());
        ncb.setNcbCusAddress(NCBInfoView.getNcbCusAddress());

        if (NCBInfoView.getTdrCondition() != null && NCBInfoView.getTdrCondition().getId() != 0) {
            ncb.setTdrCondition(NCBInfoView.getTdrCondition());
        } else {
            ncb.setTdrCondition(null);
        }

        return ncb;
    }

    public NCBInfoView transformToView(NCB ncb) {
        NCBInfoView NCBInfoView = new NCBInfoView();
        NCBInfoView.setId(ncb.getId());
        NCBInfoView.setActive(true);
        NCBInfoView.setCreateBy(ncb.getCreateBy());
        NCBInfoView.setModifyBy(ncb.getModifyBy());
        NCBInfoView.setCreateDate(ncb.getCreateDate());
        NCBInfoView.setModifyDate(ncb.getModifyDate());
        NCBInfoView.setCheckIn6Month(ncb.getCheckIn6Month());
        NCBInfoView.setCheckingDate(ncb.getCheckingDate());
        NCBInfoView.setCurrentPaymentType(ncb.getCurrentPaymentType());
        NCBInfoView.setHistoryPaymentType(ncb.getHistoryPaymentType());
        NCBInfoView.setNplFlag(ncb.getNplFlag());
        NCBInfoView.setNplOtherFlag(transFormBooleanToView(ncb.getNplOtherFlag()));
        NCBInfoView.setNplOtherMonth(ncb.getNplOtherYear());
        NCBInfoView.setNplOtherYear(ncb.getNplOtherYear());
        NCBInfoView.setNplTMBFlag(transFormBooleanToView(ncb.getNplTMBFlag()));
        NCBInfoView.setNplTMBMonth(ncb.getNplTMBMonth());
        NCBInfoView.setNplTMBYear(ncb.getNplTMBYear());
        NCBInfoView.setPaymentClass(ncb.getPaymentClass());
        NCBInfoView.setPersonalId(ncb.getPersonalId());
        NCBInfoView.setRemark(ncb.getRemark());
        NCBInfoView.setTdrFlag(ncb.getTdrFlag());
        NCBInfoView.setTdrOtherFlag(transFormBooleanToView(ncb.getTdrOhterFlag()));
        NCBInfoView.setTdrOtherMonth(ncb.getTdrOtherMonth());
        NCBInfoView.setTdrOtherYear(ncb.getTdrOtherYear());
        NCBInfoView.setTdrTMBFlag(transFormBooleanToView(ncb.getTdrTMBFlag()));
        NCBInfoView.setTdrTMBMonth(ncb.getTdrTMBMonth());
        NCBInfoView.setTdrTMBYear(ncb.getTdrTMBYear());
        NCBInfoView.setTdrCondition(ncb.getTdrCondition());
        NCBInfoView.setCustomer(ncb.getCustomer());
        NCBInfoView.setNcbCusName(ncb.getNcbCusName());
        NCBInfoView.setEnquiry(ncb.getEnquiry());
        NCBInfoView.setNcbCusMarriageStatus(ncb.getNcbCusMarriageStatus());
        NCBInfoView.setNcbCusAddress(ncb.getNcbCusAddress());
        return NCBInfoView;
    }



    // convert value for checkbox boolean

    public int transFormBooleanToDb(boolean dbObject) {

        if (dbObject == true) {
            return 1;
        } else {
            return 0;
        }
    }

    public boolean transFormBooleanToView(int viewObject) {
        return viewObject == 1 ? true : false;
    }

}
