package com.clevel.selos.transform;

import com.clevel.selos.model.db.master.TDRCondition;
import com.clevel.selos.model.db.working.Customer;
import com.clevel.selos.model.db.working.NCB;
import com.clevel.selos.model.view.NcbResultView;
import com.clevel.selos.transform.Transform;
import org.joda.time.DateTime;

/**
 * Created with IntelliJ IDEA.
 * User: acer
 * Date: 24/9/2556
 * Time: 19:49 น.
 * To change this template use File | Settings | File Templates.
 */
public class NcbTransform extends Transform {

    public NCB transformToModel(NcbResultView ncbResultView){
        NCB ncb = new NCB();

        if(ncbResultView.getId() != 0){
            ncb.setId(ncbResultView.getId());
        }
        ncb.setCheckIn6Month(ncbResultView.getCheckIn6Month());
        ncb.setCheckingDate(DateTime.now().toDate());
        ncb.setCurrentPaymentType(ncbResultView.getCurrentPaymentType());
        ncb.setHistoryPaymentType(ncbResultView.getHistoryPaymentType());
        ncb.setNplFlag(ncbResultView.isNplFlag());
        ncb.setNplOtherFlag(ncbResultView.isNplOtherFlag());
        ncb.setNplOtherMonth(ncbResultView.getNplOtherYear());
        ncb.setNplOtherYear(ncbResultView.getNplOtherYear());
        ncb.setNplTMBFlag(ncbResultView.isNplTMBFlag());
        ncb.setNplTMBMonth(ncbResultView.getNplTMBMonth());
        ncb.setNplTMBYear(ncbResultView.getNplTMBYear());
        ncb.setPaymentClass(ncbResultView.getPaymentClass());
        ncb.setPersonalId("12345");
        ncb.setRemark(ncbResultView.getRemark());
        ncb.setTdrFlag(ncbResultView.isTdrFlag());
        ncb.setTdrOhterFlag(ncbResultView.isTdrOhterFlag());
        ncb.setTdrOtherMonth(ncbResultView.getTdrOtherMonth());
        ncb.setTdrOtherYear(ncbResultView.getTdrOtherYear());
        ncb.setTdrTMBFlag(ncbResultView.isTdrTMBFlag());
        ncb.setTdrTMBMonth(ncbResultView.getTdrTMBMonth());
        ncb.setTdrTMBYear(ncbResultView.getTdrTMBYear());
        ncb.setTdrCondition(ncbResultView.getTdrCondition());
        ncb.setCustomer(new Customer());
        return ncb;
    }

    public NcbResultView transformToView(NCB ncb){
        NcbResultView  ncbResultView = new NcbResultView();

        ncbResultView.setId(ncb.getId());
        ncbResultView.setCheckIn6Month(ncb.getCheckIn6Month());
        ncbResultView.setCheckingDate(DateTime.now().toDate());
        ncbResultView.setCurrentPaymentType(ncb.getCurrentPaymentType());
        ncbResultView.setHistoryPaymentType(ncb.getHistoryPaymentType());
        ncbResultView.setNplFlag(ncb.isNplFlag());
        ncbResultView.setNplOtherFlag(ncb.isNplOtherFlag());
        ncbResultView.setNplOtherMonth(ncb.getNplOtherYear());
        ncbResultView.setNplOtherYear(ncb.getNplOtherYear());
        ncbResultView.setNplTMBFlag(ncb.isNplTMBFlag());
        ncbResultView.setNplTMBMonth(ncb.getNplTMBMonth());
        ncbResultView.setNplTMBYear(ncb.getNplTMBYear());
        ncbResultView.setPaymentClass(ncb.getPaymentClass());
        ncbResultView.setPersonalId("12345");
        ncbResultView.setRemark(ncb.getRemark());
        ncbResultView.setTdrFlag(ncb.isTdrFlag());
        ncbResultView.setTdrOhterFlag(ncb.isTdrOhterFlag());
        ncbResultView.setTdrOtherMonth(ncb.getTdrOtherMonth());
        ncbResultView.setTdrOtherYear(ncb.getTdrOtherYear());
        ncbResultView.setTdrTMBFlag(ncb.isTdrTMBFlag());
        ncbResultView.setTdrTMBMonth(ncb.getTdrTMBMonth());
        ncbResultView.setTdrTMBYear(ncb.getTdrTMBYear());
        ncbResultView.setTdrCondition(ncb.getTdrCondition());
        ncbResultView.setCustomer(new Customer());

        return ncbResultView;
    }

}
