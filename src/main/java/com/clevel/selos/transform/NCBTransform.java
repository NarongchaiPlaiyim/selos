package com.clevel.selos.transform;

import com.clevel.selos.dao.master.MaritalStatusDAO;
import com.clevel.selos.dao.working.CustomerDAO;
import com.clevel.selos.dao.working.NCBDAO;
import com.clevel.selos.model.BorrowerType;
import com.clevel.selos.model.db.master.TDRCondition;
import com.clevel.selos.model.db.working.Customer;
import com.clevel.selos.model.db.working.NCB;
import com.clevel.selos.model.view.NCBInfoView;
import com.clevel.selos.util.Util;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;

import javax.inject.Inject;


public class NCBTransform extends Transform {


    @Inject
    LoanAccountTypeTransform loanAccountTypeTransform;
    @Inject
    CustomerTransform customerTransform;
    @Inject
    CustomerDAO customerDAO;
    @Inject
    MaritalStatusDAO maritalStatusDAO;
    @Inject
    NCBDAO ncbDAO;


    public NCB transformToModel(NCBInfoView ncbInfoView) {
        NCB ncb = new NCB();

        if(ncbInfoView.getId() != 0){
            ncb = ncbDAO.findById(ncbInfoView.getId());
        }

        ncb.setActive(true);
        ncb.setModifyBy(ncbInfoView.getModifyBy());
        ncb.setModifyDate(ncbInfoView.getModifyDate());
        ncb.setCreateBy(ncbInfoView.getCreateBy());
        ncb.setCreateDate(ncbInfoView.getCreateDate());

        //,, SET CUSTOMER FOR NCB ,,
        Customer customer = customerDAO.findById(ncbInfoView.getCustomerId());
        ncb.setCustomer(customer);

        ncb.setCheckIn6Month(ncbInfoView.getCheckIn6Month());
        ncb.setCheckingDate(ncbInfoView.getCheckingDate());
        ncb.setCurrentPaymentType(ncbInfoView.getCurrentPaymentType());
        ncb.setHistoryPaymentType(ncbInfoView.getHistoryPaymentType());
        ncb.setNplFlag(ncbInfoView.getNplFlag());
        ncb.setNplOtherFlag(transFormBooleanToDb(ncbInfoView.isNplOtherFlag()));
        ncb.setNplOtherMonth(ncbInfoView.getNplOtherMonth());
        ncb.setNplOtherYear(ncbInfoView.getNplOtherYear());
        ncb.setNplTMBFlag(transFormBooleanToDb(ncbInfoView.isNplTMBFlag()));
        ncb.setNplTMBMonth(ncbInfoView.getNplTMBMonth());
        ncb.setNplTMBYear(ncbInfoView.getNplTMBYear());
        ncb.setPaymentClass(ncbInfoView.getPaymentClass());
        ncb.setPersonalId(ncbInfoView.getPersonalId());
        ncb.setRemark(ncbInfoView.getRemark());
        ncb.setTdrFlag(ncbInfoView.getTdrFlag());
        ncb.setTdrOhterFlag(transFormBooleanToDb(ncbInfoView.isTdrOtherFlag()));
        ncb.setTdrOtherMonth(ncbInfoView.getTdrOtherMonth());
        ncb.setTdrOtherYear(ncbInfoView.getTdrOtherYear());
        ncb.setTdrTMBFlag(transFormBooleanToDb(ncbInfoView.isTdrTMBFlag()));
        ncb.setTdrTMBMonth(ncbInfoView.getTdrTMBMonth());
        ncb.setTdrTMBYear(ncbInfoView.getTdrTMBYear());
        ncb.setNcbCusName(ncbInfoView.getNcbCusName());
        ncb.setEnquiry(ncbInfoView.getEnquiry());
        ncb.setNcbCusMarriageStatus(ncbInfoView.getNcbCusMarriageStatus());

        if (ncbInfoView.getTdrCondition() != null && ncbInfoView.getTdrCondition().getId() != 0) {
            ncb.setTdrCondition(ncbInfoView.getTdrCondition());
        } else {
            ncb.setTdrCondition(null);
        }

        return ncb;
    }

    public NCBInfoView transformToView(NCB ncb) {
        NCBInfoView ncbInfoView = new NCBInfoView();
        ncbInfoView.setId(ncb.getId());
        ncbInfoView.setActive(true);
        ncbInfoView.setCreateBy(ncb.getCreateBy());
        ncbInfoView.setModifyBy(ncb.getModifyBy());
        ncbInfoView.setCreateDate(ncb.getCreateDate());
        ncbInfoView.setModifyDate(ncb.getModifyDate());
        ncbInfoView.setCheckIn6Month(ncb.getCheckIn6Month());
        ncbInfoView.setCheckingDate(ncb.getCheckingDate());
        ncbInfoView.setCurrentPaymentType(ncb.getCurrentPaymentType());
        ncbInfoView.setHistoryPaymentType(ncb.getHistoryPaymentType());
        ncbInfoView.setNplFlag(ncb.getNplFlag());
        ncbInfoView.setNplOtherFlag(transFormBooleanToView(ncb.getNplOtherFlag()));
        ncbInfoView.setNplOtherMonth(ncb.getNplOtherYear());
        ncbInfoView.setNplOtherYear(ncb.getNplOtherYear());
        ncbInfoView.setNplTMBFlag(transFormBooleanToView(ncb.getNplTMBFlag()));
        ncbInfoView.setNplTMBMonth(ncb.getNplTMBMonth());
        ncbInfoView.setNplTMBYear(ncb.getNplTMBYear());
        ncbInfoView.setPaymentClass(ncb.getPaymentClass());
        ncbInfoView.setRemark(ncb.getRemark());
        ncbInfoView.setTdrFlag(ncb.getTdrFlag());
        ncbInfoView.setTdrOtherFlag(transFormBooleanToView(ncb.getTdrOhterFlag()));
        ncbInfoView.setTdrOtherMonth(ncb.getTdrOtherMonth());
        ncbInfoView.setTdrOtherYear(ncb.getTdrOtherYear());
        ncbInfoView.setTdrTMBFlag(transFormBooleanToView(ncb.getTdrTMBFlag()));
        ncbInfoView.setTdrTMBMonth(ncb.getTdrTMBMonth());
        ncbInfoView.setTdrTMBYear(ncb.getTdrTMBYear());

       if (ncb.getTdrCondition() != null && ncb.getTdrCondition().getId() != 0) {
            ncbInfoView.setTdrCondition(ncb.getTdrCondition());
        } else {
            ncbInfoView.setTdrCondition(new TDRCondition());
        }

        if(ncb.getCustomer() != null){
            ncbInfoView.setCustomerId(ncb.getCustomer().getId());
            Customer customer = customerDAO.findById(ncb.getCustomer().getId());

            if(customer != null){
                StringBuilder customerName = new StringBuilder();
                customerName.append(customer.getTitle().getTitleTh())
                        .append(" ").append(StringUtils.defaultString(customer.getNameTh()))
                        .append(" ").append(StringUtils.defaultString(customer.getLastNameTh()));
                ncbInfoView.setNcbCusName(customerName.toString());

                if(ncb.getCustomer().getCustomerEntity().getId() == BorrowerType.INDIVIDUAL.value()){
                    if(ncb.getCustomer().getIndividual() != null){
                        ncbInfoView.setPersonalId(ncb.getCustomer().getIndividual().getCitizenId());
                        if( ncb.getCustomer().getIndividual() != null && ncb.getCustomer().getIndividual().getMaritalStatus() != null ){
                            ncbInfoView.setNcbCusMarriageStatus(ncb.getCustomer().getIndividual().getMaritalStatus().getName());
                        }
                    }
                } else if(ncb.getCustomer().getCustomerEntity().getId() == BorrowerType.JURISTIC.value()) {
                    if(ncb.getCustomer().getJuristic() != null){
                        ncbInfoView.setPersonalId(ncb.getCustomer().getJuristic().getRegistrationId());
                    }
                }
            }
        }

        ncbInfoView.setEnquiry(ncb.getEnquiry());
        return ncbInfoView;
    }

    // convert value for checkbox boolean

    public int transFormBooleanToDb(boolean dbObject) {
        return Util.returnNumForFlag(dbObject);
    }

    public boolean transFormBooleanToView(int viewObject) {
        return Util.isTrue(viewObject);
    }

}
