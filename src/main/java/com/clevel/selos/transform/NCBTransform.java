package com.clevel.selos.transform;

import com.clevel.selos.model.db.working.Customer;
import com.clevel.selos.model.db.working.NCB;
import com.clevel.selos.model.view.NCBInfoView;
import org.joda.time.DateTime;

/**
 * Created with IntelliJ IDEA.
 * User: acer
 * Date: 24/9/2556
 * Time: 19:49 น.
 * To change this template use File | Settings | File Templates.
 */
public class NCBTransform extends Transform {

    public NCB transformToModel(NCBInfoView NCBInfoView){
        NCB ncb = new NCB();

        if(NCBInfoView.getId() != 0){
            ncb.setId(NCBInfoView.getId());
        }
        ncb.setCheckIn6Month(NCBInfoView.getCheckIn6Month());
        ncb.setCheckingDate(DateTime.now().toDate());
        ncb.setCurrentPaymentType(NCBInfoView.getCurrentPaymentType());
        ncb.setHistoryPaymentType(NCBInfoView.getHistoryPaymentType());
        ncb.setNplFlag(NCBInfoView.isNplFlag());
        ncb.setNplOtherFlag(NCBInfoView.isNplOtherFlag());
        ncb.setNplOtherMonth(NCBInfoView.getNplOtherYear());
        ncb.setNplOtherYear(NCBInfoView.getNplOtherYear());
        ncb.setNplTMBFlag(NCBInfoView.isNplTMBFlag());
        ncb.setNplTMBMonth(NCBInfoView.getNplTMBMonth());
        ncb.setNplTMBYear(NCBInfoView.getNplTMBYear());
        ncb.setPaymentClass(NCBInfoView.getPaymentClass());
        ncb.setPersonalId("12345");
        ncb.setRemark(NCBInfoView.getRemark());
        ncb.setTdrFlag(NCBInfoView.isTdrFlag());
        ncb.setTdrOhterFlag(NCBInfoView.isTdrOhterFlag());
        ncb.setTdrOtherMonth(NCBInfoView.getTdrOtherMonth());
        ncb.setTdrOtherYear(NCBInfoView.getTdrOtherYear());
        ncb.setTdrTMBFlag(NCBInfoView.isTdrTMBFlag());
        ncb.setTdrTMBMonth(NCBInfoView.getTdrTMBMonth());
        ncb.setTdrTMBYear(NCBInfoView.getTdrTMBYear());
        ncb.setTdrCondition(NCBInfoView.getTdrCondition());
        ncb.setCustomer(new Customer());
        return ncb;
    }

    public NCBInfoView transformToView(NCB ncb){
        NCBInfoView NCBInfoView = new NCBInfoView();

        NCBInfoView.setId(ncb.getId());
        NCBInfoView.setCheckIn6Month(ncb.getCheckIn6Month());
        NCBInfoView.setCheckingDate(DateTime.now().toDate());
        NCBInfoView.setCurrentPaymentType(ncb.getCurrentPaymentType());
        NCBInfoView.setHistoryPaymentType(ncb.getHistoryPaymentType());
        NCBInfoView.setNplFlag(ncb.isNplFlag());
        NCBInfoView.setNplOtherFlag(ncb.isNplOtherFlag());
        NCBInfoView.setNplOtherMonth(ncb.getNplOtherYear());
        NCBInfoView.setNplOtherYear(ncb.getNplOtherYear());
        NCBInfoView.setNplTMBFlag(ncb.isNplTMBFlag());
        NCBInfoView.setNplTMBMonth(ncb.getNplTMBMonth());
        NCBInfoView.setNplTMBYear(ncb.getNplTMBYear());
        NCBInfoView.setPaymentClass(ncb.getPaymentClass());
        NCBInfoView.setPersonalId("12345");
        NCBInfoView.setRemark(ncb.getRemark());
        NCBInfoView.setTdrFlag(ncb.isTdrFlag());
        NCBInfoView.setTdrOhterFlag(ncb.isTdrOhterFlag());
        NCBInfoView.setTdrOtherMonth(ncb.getTdrOtherMonth());
        NCBInfoView.setTdrOtherYear(ncb.getTdrOtherYear());
        NCBInfoView.setTdrTMBFlag(ncb.isTdrTMBFlag());
        NCBInfoView.setTdrTMBMonth(ncb.getTdrTMBMonth());
        NCBInfoView.setTdrTMBYear(ncb.getTdrTMBYear());
        NCBInfoView.setTdrCondition(ncb.getTdrCondition());
        NCBInfoView.setCustomer(new Customer());

        return NCBInfoView;
    }

}
