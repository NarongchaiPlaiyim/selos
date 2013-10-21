package com.clevel.selos.transform;

import com.clevel.selos.model.db.working.NCB;
import com.clevel.selos.model.view.NCBInfoView;
import org.joda.time.DateTime;


public class NCBTransform extends Transform {

    public NCB transformToModel(NCBInfoView ncbInfoView ){
        NCB ncb = new NCB();

        if(ncbInfoView.getId() != 0){
            ncb.setId(ncbInfoView.getId());
        }

        ncb.setActive(true);
        ncb.setCreateBy(ncbInfoView.getCreateBy());
        ncb.setModifyBy(ncbInfoView.getModifyBy());
        ncb.setCreateDate(ncbInfoView.getCreateDate());
        ncb.setModifyDate(ncbInfoView.getModifyDate());
        ncb.setCustomer(ncbInfoView.getCustomer());
        ncb.setCheckIn6Month(ncbInfoView.getCheckIn6Month());
        ncb.setCheckingDate(DateTime.now().toDate());
        ncb.setCurrentPaymentType(ncbInfoView.getCurrentPaymentType());
        ncb.setHistoryPaymentType(ncbInfoView.getHistoryPaymentType());
        ncb.setNplFlag(ncbInfoView.getNplFlag());
        ncb.setNplOtherFlag(ncbInfoView.getNplOtherFlag());
        ncb.setNplOtherMonth(ncbInfoView.getNplOtherMonth());
        ncb.setNplOtherYear(ncbInfoView.getNplOtherYear());
        ncb.setNplTMBFlag(ncbInfoView.getNplTMBFlag());
        ncb.setNplTMBMonth(ncbInfoView.getNplTMBMonth());
        ncb.setNplTMBYear(ncbInfoView.getNplTMBYear());
        ncb.setPaymentClass(ncbInfoView.getPaymentClass());
        ncb.setPersonalId("");
        ncb.setRemark(ncbInfoView.getRemark());
        ncb.setTdrFlag(ncbInfoView.getTdrFlag());
        ncb.setTdrOhterFlag(ncbInfoView.getTdrOtherFlag());
        ncb.setTdrOtherMonth(ncbInfoView.getTdrOtherMonth());
        ncb.setTdrOtherYear(ncbInfoView.getTdrOtherYear());
        ncb.setTdrTMBFlag(ncbInfoView.getTdrTMBFlag());
        ncb.setTdrTMBMonth(ncbInfoView.getTdrTMBMonth());
        ncb.setTdrTMBYear(ncbInfoView.getTdrTMBYear());
        ncb.setTdrCondition(ncbInfoView.getTdrCondition());

        return ncb;
    }

    public NCBInfoView transformToView(NCB ncb){
        NCBInfoView ncbInfoView = new NCBInfoView();

        ncbInfoView.setId(ncb.getId());
        ncbInfoView.setActive(true);
        ncbInfoView.setCreateBy(ncb.getCreateBy());
        ncbInfoView.setModifyBy(ncb.getModifyBy());
        ncbInfoView.setCreateDate(ncb.getCreateDate());
        ncbInfoView.setModifyDate(ncb.getModifyDate());
        ncbInfoView.setCheckIn6Month(ncb.getCheckIn6Month());
        ncbInfoView.setCheckingDate(DateTime.now().toDate());
        ncbInfoView.setCurrentPaymentType(ncb.getCurrentPaymentType());
        ncbInfoView.setHistoryPaymentType(ncb.getHistoryPaymentType());
        ncbInfoView.setNplFlag(ncb.getNplFlag());
        ncbInfoView.setNplOtherFlag(ncb.getNplOtherFlag());
        ncbInfoView.setNplOtherMonth(ncb.getNplOtherYear());
        ncbInfoView.setNplOtherYear(ncb.getNplOtherYear());
        ncbInfoView.setNplTMBFlag(ncb.getNplTMBFlag());
        ncbInfoView.setNplTMBMonth(ncb.getNplTMBMonth());
        ncbInfoView.setNplTMBYear(ncb.getNplTMBYear());
        ncbInfoView.setPaymentClass(ncb.getPaymentClass());
        ncbInfoView.setPersonalId("");
        ncbInfoView.setRemark(ncb.getRemark());
        ncbInfoView.setTdrFlag(ncb.getTdrFlag());
        ncbInfoView.setTdrOtherFlag(ncb.getTdrOhterFlag());
        ncbInfoView.setTdrOtherMonth(ncb.getTdrOtherMonth());
        ncbInfoView.setTdrOtherYear(ncb.getTdrOtherYear());
        ncbInfoView.setTdrTMBFlag(ncb.getTdrTMBFlag());
        ncbInfoView.setTdrTMBMonth(ncb.getTdrTMBMonth());
        ncbInfoView.setTdrTMBYear(ncb.getTdrTMBYear());
        ncbInfoView.setTdrCondition(ncb.getTdrCondition());
        ncbInfoView.setCustomer(ncb.getCustomer());

        return ncbInfoView;
    }

    // convert value for checkbox boolean
    public int transFormBooleanToDb(boolean viewObj){

         if(viewObj == true){
             return 1;
         }else{
             return 0;
         }
    }

    public boolean transFormBooleanToView(int viewObj){
        return (viewObj==1?true:false);
    }

}
