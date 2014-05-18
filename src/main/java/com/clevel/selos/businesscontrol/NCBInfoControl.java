package com.clevel.selos.businesscontrol;

import com.clevel.selos.dao.master.SettlementStatusDAO;
import com.clevel.selos.dao.working.CustomerDAO;
import com.clevel.selos.dao.working.NCBDAO;
import com.clevel.selos.dao.working.NCBDetailDAO;
import com.clevel.selos.dao.working.WorkCaseDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.NCBPaymentCode;
import com.clevel.selos.model.RadioValue;
import com.clevel.selos.model.db.master.AccountStatus;
import com.clevel.selos.model.db.master.AccountType;
import com.clevel.selos.model.db.working.Customer;
import com.clevel.selos.model.db.working.NCB;
import com.clevel.selos.model.db.working.NCBDetail;
import com.clevel.selos.model.view.NCBDetailView;
import com.clevel.selos.model.view.NCBInfoView;
import com.clevel.selos.transform.LoanAccountTypeTransform;
import com.clevel.selos.transform.NCBDetailTransform;
import com.clevel.selos.transform.NCBTransform;
import com.clevel.selos.util.Util;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.Restrictions;
import org.joda.time.DateTime;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


@Stateless
public class NCBInfoControl extends BusinessControl {
    @Inject
    @SELOS
    private Logger log;

    @Inject
    NCBDAO ncbDAO;
    @Inject
    NCBDetailDAO ncbDetailDAO;
    @Inject
    WorkCaseDAO workCaseDAO;
    @Inject
    SettlementStatusDAO settlementStatusDAO;
    @Inject
    private CustomerDAO customerDAO;

    @Inject
    NCBDetailTransform ncbDetailTransform;
    @Inject
    NCBTransform ncbTransform;
    @Inject
    private LoanAccountTypeTransform loanAccountTypeTransform;

    @Inject
    public NCBInfoControl() {}

    private final BigDecimal plusMRR = BigDecimal.valueOf(6);


    public void onSaveNCBToDB(NCBInfoView ncbInfoView, List<NCBDetailView> ncbDetailViewList, Customer customerInfo) {
        log.info("onSaveNCBToDB begin");

        if(ncbInfoView.getId() == 0){
            ncbInfoView.setCreateBy(getCurrentUser());
            ncbInfoView.setCreateDate(DateTime.now().toDate());
        } else {
            ncbInfoView.setModifyBy(getCurrentUser());
            ncbInfoView.setModifyDate(DateTime.now().toDate());
        }

        //calculate summary detail
        ncbInfoView = calculateNCBInfo(ncbInfoView, ncbDetailViewList, customerInfo);

        NCB ncb = ncbTransform.transformToModel(ncbInfoView);
        //ncbDAO.persist(ncb);
        List<NCBDetail> NCBDetailListToDelete = ncbDetailDAO.findNCBDetailByNcbId(ncb.getId());
        log.info("NCBDetailListToDelete :: {}", NCBDetailListToDelete.size());
        ncbDetailDAO.delete(NCBDetailListToDelete);
        log.info("delete NCBDetailListToDelete");

        log.debug("ncbDetailViewList : {}", ncbDetailViewList);
        List<NCBDetail> ncbDetailList = ncbDetailTransform.transformToModel(ncbDetailViewList, ncb);
        calculateLoanCredit(ncb, ncbDetailList);
        ncbDAO.persist(ncb);
        log.info("persist ncb");
        ncbDetailDAO.persist(ncbDetailList);
        //TODO Call function

    }

    public NCBInfoView calculateNCBInfo(NCBInfoView ncbInfoView, List<NCBDetailView> ncbDetailViewList, Customer customerInfo) {
        if(ncbDetailViewList!=null && ncbDetailViewList.size()>0){
            String currentPaymentType = "";
            String historyPaymentType = "";
            int currentPayment = 0;
            int historyPayment = 0;

            Date lastTDRDateTMB = null;
            Date lastTDRDateOther = null;
            boolean isTDRTMB = false;
            boolean isTDROther = false;

            Date lastNPLDateTMB = null;
            Date lastNPLDateOther = null;
            boolean isNPLTMB = false;
            boolean isNPLOther = false;

            Date lastInfoDate = null;

            for(NCBDetailView ncbDetailView: ncbDetailViewList){
                //get current payment

                if(NCBPaymentCode.getValue(ncbDetailView.getCurrentPayment().getNcbCode()).value() > currentPayment){
                    currentPayment = NCBPaymentCode.getValue(ncbDetailView.getCurrentPayment().getNcbCode()).value();
                    currentPaymentType = ncbDetailView.getCurrentPayment().getName();
                }

                //get history payment
                if(NCBPaymentCode.getValue(ncbDetailView.getHistoryPayment().getNcbCode()).value() > historyPayment){
                    historyPayment = NCBPaymentCode.getValue(ncbDetailView.getHistoryPayment().getNcbCode()).value();
                    historyPaymentType = ncbDetailView.getHistoryPayment().getName();
                }

                //get TDR
                if(ncbDetailView.getDateOfDebtRestructuring()!=null){
                    if(ncbDetailView.getTmbCheck()){
                        isTDRTMB = true;
                        if(lastTDRDateTMB!=null){
                            if(lastTDRDateTMB.before(ncbDetailView.getDateOfDebtRestructuring())){
                                lastTDRDateTMB = ncbDetailView.getDateOfDebtRestructuring();
                            }
                        } else {
                            lastTDRDateTMB = ncbDetailView.getDateOfDebtRestructuring();
                        }
                    } else {
                        isTDROther = true;
                        if(lastTDRDateOther!=null){
                            if(lastTDRDateOther.before(ncbDetailView.getDateOfDebtRestructuring())){
                                lastTDRDateOther = ncbDetailView.getDateOfDebtRestructuring();
                            }
                        } else {
                            lastTDRDateOther = ncbDetailView.getDateOfDebtRestructuring();
                        }
                    }
                }

                //get NPL
                if(customerInfo.getCustomerEntity()!=null && customerInfo.getCustomerEntity().getId() == 1){ //Individual
                    if((NCBPaymentCode.getValue(ncbDetailView.getCurrentPayment().getNcbCode()).value() >= 6 || NCBPaymentCode.getValue(ncbDetailView.getHistoryPayment().getNcbCode()).value() >= 6 )
                            && ncbDetailView.getDateOfInfo()!=null) {
                        if(ncbDetailView.getTmbCheck()){
                            isNPLTMB = true;
                            if(lastNPLDateTMB!=null){
                                if(lastNPLDateTMB.before(ncbDetailView.getDateOfInfo())){
                                    lastNPLDateTMB = ncbDetailView.getDateOfInfo();
                                }
                            } else {
                                lastNPLDateTMB = ncbDetailView.getDateOfInfo();
                            }
                        } else {
                            isNPLOther = true;
                            if(lastNPLDateOther!=null){
                                if(lastNPLDateOther.before(ncbDetailView.getDateOfInfo())){
                                    lastNPLDateOther = ncbDetailView.getDateOfInfo();
                                }
                            } else {
                                lastNPLDateOther = ncbDetailView.getDateOfInfo();
                            }
                        }
                    }
                } else {
                    if((NCBPaymentCode.getValue(ncbDetailView.getCurrentPayment().getNcbCode()).value() >= 7 || NCBPaymentCode.getValue(ncbDetailView.getHistoryPayment().getNcbCode()).value() >= 7 )
                            && ncbDetailView.getDateOfInfo()!=null) {
                        if(ncbDetailView.getTmbCheck()){
                            isNPLTMB = true;
                            if(lastNPLDateTMB!=null){
                                if(lastNPLDateTMB.before(ncbDetailView.getDateOfInfo())){
                                    lastNPLDateTMB = ncbDetailView.getDateOfInfo();
                                }
                            } else {
                                lastNPLDateTMB = ncbDetailView.getDateOfInfo();
                            }
                        } else {
                            isNPLOther = true;
                            if(lastNPLDateOther!=null){
                                if(lastNPLDateOther.before(ncbDetailView.getDateOfInfo())){
                                    lastNPLDateOther = ncbDetailView.getDateOfInfo();
                                }
                            } else {
                                lastNPLDateOther = ncbDetailView.getDateOfInfo();
                            }
                        }
                    }
                }

                //get last info date
                if(lastInfoDate!=null) {
                    if(lastInfoDate.before(ncbDetailView.getDateOfInfo())){
                        lastInfoDate = ncbDetailView.getDateOfInfo();
                    }
                } else {
                    lastInfoDate = ncbDetailView.getDateOfInfo();
                }
            }

            ncbInfoView.setCurrentPaymentType(currentPaymentType);
            ncbInfoView.setHistoryPaymentType(historyPaymentType);
            ncbInfoView.setNcbLastInfoAsOfDate(lastInfoDate);

            ncbInfoView.setTdrFlag(0);
            if(isTDRTMB || isTDROther){
                ncbInfoView.setTdrFlag(2);
                if(isTDRTMB){
                    ncbInfoView.setTdrTMBFlag(true);
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(lastTDRDateTMB);
                    ncbInfoView.setTdrTMBMonth(cal.get(Calendar.MONTH) + 1);
                    ncbInfoView.setTdrTMBYear(cal.get(Calendar.YEAR));
                }
                if(isTDROther){
                    ncbInfoView.setTdrOtherFlag(true);
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(lastTDRDateOther);
                    ncbInfoView.setTdrOtherMonth(cal.get(Calendar.MONTH) + 1);
                    ncbInfoView.setTdrOtherYear(cal.get(Calendar.YEAR));
                }
            } else {
                ncbInfoView.setTdrFlag(1);
            }

            ncbInfoView.setNplFlag(0);
            if(isNPLTMB || isNPLOther){
                ncbInfoView.setNplFlag(2);
                if(isNPLTMB){
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(lastNPLDateTMB);
                    ncbInfoView.setNplTMBFlag(true);
                    ncbInfoView.setNplTMBMonth(cal.get(Calendar.MONTH) + 1);
                    ncbInfoView.setNplTMBYear(cal.get(Calendar.YEAR));
                }
                if(isNPLOther){
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(lastNPLDateOther);
                    ncbInfoView.setNplOtherFlag(true);
                    ncbInfoView.setNplOtherMonth(cal.get(Calendar.MONTH) + 1);
                    ncbInfoView.setNplOtherYear(cal.get(Calendar.YEAR));
                }
            } else {
                ncbInfoView.setTdrFlag(1);
            }
        }
        return ncbInfoView;
    }

    public NCB calculateLoanCredit(NCB ncb, List<NCBDetail> ncbDetailList){
        // วงเงินสินเชื่อหมุนเวียนจากหน้า NCB
        BigDecimal loanCredit = BigDecimal.ZERO;
        // ภาระสินเชื่อประเภทอื่นๆ จากหน้า NCB ที่มี flag W/C = Yes
        BigDecimal loanCreditWC = BigDecimal.ZERO;
        // วงเงินสินเชื่อหมุนเวียนใน NCB ที่ flag เป็น TMB
        BigDecimal loanCreditTMB = BigDecimal.ZERO;
        // ภาระสินเชื่อประเภทอื่น ที่ flag TMB และ flag W/C
        BigDecimal loanCreditWCTMB = BigDecimal.ZERO;

        for(NCBDetail item : ncbDetailList){
            if(item.getAccountType() != null && item.getAccountType().getWcFlag() == 1){
                loanCredit = loanCredit.add(item.getLimit());
            }
            if(item.getAccountType() != null && item.getAccountType().getWcFlag() == 1 &&
                    item.getWcFlag() == RadioValue.YES.value()){
                loanCreditWC = loanCreditWC.add(item.getOutstanding());
            }
            if(item.getAccountType() != null && item.getAccountType().getWcFlag() == 1 &&
                    item.getAccountTMBFlag() == RadioValue.YES.value()){
                loanCreditTMB = loanCreditTMB.add(item.getOutstanding());
            }
            if(item.getAccountType() != null && item.getAccountType().getWcFlag() == 1 &&
                    item.getAccountTMBFlag() == RadioValue.YES.value() &&
                    item.getWcFlag() == RadioValue.YES.value()){
                loanCreditWCTMB = loanCreditWCTMB.add(item.getLimit());
            }
        }
        ncb.setLoanCreditNCB(loanCredit);
        ncb.setLoanCreditTMB(loanCreditTMB);
        ncb.setLoanCreditWC(loanCreditWC);
        ncb.setLoanCreditWCTMB(loanCreditWCTMB);

        return ncb;
    }


    public NCBInfoView getNCBInfoView(long customerId) {
        log.info("getNCBInfoView :: customer id  :: {}", customerId);
        NCBInfoView ncbInfoView = null;

        try {
            NCB ncb = ncbDAO.findNcbByCustomer(customerId);
            if (ncb != null) {
                log.info("ncb :: {} ", ncb.getId());
                ncbInfoView = ncbTransform.transformToView(ncb);
            }
        } catch (Exception e) {
            log.error("getNcbInfoView error :: " + e.getMessage());
        } finally {
            log.info("getNcbInfoView end");
        }

        return ncbInfoView;
    }

    public List<NCBDetailView> getNcbDetailListView(NCBInfoView ncbInfoView) {
        log.info("getNcbDetailListView :: ncbId  :: {}", ncbInfoView.getId());
        List<NCBDetailView> ncbDetailViewList = new ArrayList<NCBDetailView>();

        try {
            List<NCBDetail> NCBDetailList = ncbDetailDAO.findNCBDetailByNcbId(ncbInfoView.getId());

            if (NCBDetailList.size() > 0) {
                ncbDetailViewList = ncbDetailTransform.transformToViews(NCBDetailList);
            }

        } catch (Exception e) {
            log.error("getNcbDetailListView error :: " + e.getMessage());
        } finally {
            log.info("getNcbDetailListView end");
        }

        return ncbDetailViewList;
    }

    public List<NCBDetailView> getNCBForCalDBR(long workcaseId){
        List<NCBDetailView> ncbDetailViews = new ArrayList<NCBDetailView>();
        log.debug("BegetNCBForCalDBRBR workcase:{}", workcaseId);
        List<Customer> customers = customerDAO.findByWorkCaseId(workcaseId);
        if(customers == null || customers.size() == 0) return ncbDetailViews;
        List<NCB> ncbs = ncbDAO.createCriteria().add(Restrictions.in("customer", customers)).list();
        List<NCBDetail> ncbDetails = new ArrayList<NCBDetail>();
        ncbDetails = ncbDetailDAO.createCriteria().add(Restrictions.in("ncb", ncbs)).list();
        log.debug("ncbDetails size:{}", ncbDetails.size());
        AccountType accountType;
        AccountStatus accountStatus;
        StringBuilder accountName = new StringBuilder();
            for(NCBDetail ncbDetail : Util.safetyList(ncbDetails)){
                Customer customer = ncbDetail.getNcb().getCustomer();
                accountType = ncbDetail.getAccountType();
                accountStatus = ncbDetail.getAccountStatus();
                if(accountStatus == null || accountType == null) continue;

                if(accountStatus.getDbrFlag() == 1 && accountType.getDbrFlag() == 1){
                    NCBDetailView ncbDetailView = new NCBDetailView();
                    ncbDetailView.setId(ncbDetail.getId());
                    ncbDetailView.setLimit(ncbDetail.getLimit());
                    ncbDetailView.setInstallment(ncbDetail.getInstallment());
                    BigDecimal debtForCalculate = BigDecimal.ZERO;

                    BigDecimal dbrInterest = getDBRInterest();
                    switch (accountType.getCalculateType()){
                        case 1:
                            if(ncbDetail.getInstallment() == null || ncbDetail.getInstallment().compareTo(BigDecimal.ZERO) == 0){
                                debtForCalculate = Util.multiply(ncbDetail.getLimit(), dbrInterest);
                                debtForCalculate = Util.divide(debtForCalculate, 100);
                                debtForCalculate = Util.divide(debtForCalculate, 12);
                            }else{
                                debtForCalculate = ncbDetail.getInstallment();
                            }
                            break;
                        case 2: //5%
                            debtForCalculate = Util.multiply(ncbDetail.getOutstanding(), BigDecimal.valueOf(5));
                            debtForCalculate = Util.divide(debtForCalculate, 100);
                            break;
                        case 3: //10 %
                            debtForCalculate = Util.multiply(ncbDetail.getOutstanding(), BigDecimal.valueOf(10));
                            debtForCalculate = Util.divide(debtForCalculate, 100);
                            break;
                        default:
                            break;
                    }
                ncbDetailView.setDebtForCalculate(debtForCalculate);
                accountName.setLength(0);
                accountName.append(customer.getTitle().getTitleTh())
                        .append(" ").append(StringUtils.defaultString(customer.getNameTh()))
                        .append(" ").append(StringUtils.defaultString(customer.getLastNameTh()));
                ncbDetailView.setAccountName(accountName.toString());
                ncbDetailView.setLoanAccountTypeView(loanAccountTypeTransform.getLoanAccountTypeView(ncbDetail.getAccountType()));
                ncbDetailView.setRefinanceFlag(ncbDetail.getRefinanceFlag());
                ncbDetailViews.add(ncbDetailView);
            }

        }
        return ncbDetailViews;
    }

    public List<NCBInfoView> getNCBInfoViewByWorkCaseId(long workCaseId){
        log.debug("getNCBInfoViewByWorkCaseId ::: workCaseId : {}", workCaseId);
        List<NCBInfoView> ncbInfoViewList = new ArrayList<NCBInfoView>();
        //TODO Reduce Bandwidth for Get CustomerOnly ( without individual juristic )
        List<Customer> customerList = customerDAO.findByWorkCaseId(workCaseId);
        if (customerList != null && customerList.size() > 0) {
            log.debug("getNCBInfoViewByWorkCaseId ::: customerList.size : {}", customerList.size());
            for(Customer cus : customerList){
                if(cus.getNcb() != null){
                    log.debug("getNCBInfoViewByWorkCaseId ::: ncb : {}", cus.getNcb());
                    NCBInfoView ncbView = ncbTransform.transformToView(cus.getNcb());
                    ncbInfoViewList.add(ncbView);
                }
            }
        }
        return ncbInfoViewList;
    }

    public List<NCB> getNCBByWorkCaseId(long workCaseId){
        log.debug("getNCBByWorkCaseId ::: workCaseId : {}", workCaseId);
        List<NCB> ncbList = new ArrayList<NCB>();
        List<Customer> customerList = customerDAO.findByWorkCaseId(workCaseId);
        if (customerList != null && customerList.size() > 0) {
            log.debug("getNCBByWorkCaseId ::: customerList.size : {}", customerList.size());
            for(Customer cus : customerList){
                if(cus.getNcb() != null){
                    log.debug("getNCBByWorkCaseId ::: ncb : {}", cus.getNcb());
                    ncbList.add(cus.getNcb());
                }
            }
        }
        return ncbList;
    }
}
