package com.clevel.selos.businesscontrol;

import com.clevel.selos.businesscontrol.master.BaseRateControl;
import com.clevel.selos.dao.master.SettlementStatusDAO;
import com.clevel.selos.dao.working.*;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.NCBPaymentCode;
import com.clevel.selos.model.RadioValue;
import com.clevel.selos.model.db.working.Customer;
import com.clevel.selos.model.db.working.NCB;
import com.clevel.selos.model.db.working.NCBDetail;
import com.clevel.selos.model.db.working.WorkCase;
import com.clevel.selos.model.view.NCBDetailView;
import com.clevel.selos.model.view.NCBInfoView;
import com.clevel.selos.transform.NCBDetailTransform;
import com.clevel.selos.transform.NCBTransform;
import com.clevel.selos.util.Util;
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
    private NCBDAO ncbDAO;
    @Inject
    private NCBDetailDAO ncbDetailDAO;
    @Inject
    private WorkCaseDAO workCaseDAO;
    @Inject
    private SettlementStatusDAO settlementStatusDAO;
    @Inject
    private CustomerDAO customerDAO;
    @Inject
    private DBRDAO dbrDAO;

    @Inject
    private BaseRateControl baseRateControl;
    @Inject
    private UserSysParameterControl userSysParameterControl;

    @Inject
    private NCBDetailTransform ncbDetailTransform;
    @Inject
    private NCBTransform ncbTransform;

    @Inject
    public NCBInfoControl() {}

    public void onSaveNCBToDB(NCBInfoView ncbInfoView, List<NCBDetailView> ncbDetailViewList, Customer customerInfo, long workCaseId) {
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

        //--Update flag in WorkCase ( for check before submit )
        WorkCase workCase = workCaseDAO.findById(workCaseId);
        workCase.setCaseUpdateFlag(1);
        workCaseDAO.persist(workCase);

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

            isNPLOther = ncbInfoView.isNplOtherFlagNCB();
            isNPLTMB = ncbInfoView.isNplTMBFlagNCB();

            isTDROther = ncbInfoView.isTdrOtherFlagNCB();
            isTDRTMB = ncbInfoView.isNplTMBFlagNCB();

            if(isNPLOther){
                lastNPLDateOther = ncbInfoView.getNplOtherDateNCB();
            }

            if(isNPLTMB){
                lastNPLDateTMB = ncbInfoView.getNplTMBDateNCB();
            }

            if(isTDROther){
                lastTDRDateOther = ncbInfoView.getTdrOtherDateNCB();
            }

            if(isTDRTMB){
                lastTDRDateTMB = ncbInfoView.getTdrTMBDateNCB();
            }

            for(NCBDetailView ncbDetailView: ncbDetailViewList){
                boolean isTDRFlag = false;
                boolean isNPLFlag = false;
                //get current payment
                if(ncbDetailView.getCurrentPayment()!=null && ncbDetailView.getCurrentPayment().getNcbCode()!=null) {
                    if((NCBPaymentCode.getValue(ncbDetailView.getCurrentPayment().getNcbCode()) !=null)
                        && (NCBPaymentCode.getValue(ncbDetailView.getCurrentPayment().getNcbCode()).value() > currentPayment)){
                        currentPayment = NCBPaymentCode.getValue(ncbDetailView.getCurrentPayment().getNcbCode()).value();
                        currentPaymentType = ncbDetailView.getCurrentPayment().getName();
                    }
                }

                //get history payment
                if(ncbDetailView.getHistoryPayment()!=null && ncbDetailView.getHistoryPayment().getNcbCode()!=null) {
                    if((NCBPaymentCode.getValue(ncbDetailView.getHistoryPayment().getNcbCode()) != null)
                        && (NCBPaymentCode.getValue(ncbDetailView.getHistoryPayment().getNcbCode()).value() > historyPayment)){
                        historyPayment = NCBPaymentCode.getValue(ncbDetailView.getHistoryPayment().getNcbCode()).value();
                        historyPaymentType = ncbDetailView.getHistoryPayment().getName();
                    }
                }

                //get TDR
                if(ncbDetailView.getDateOfDebtRestructuring()!=null){
                    isTDRFlag = true;
                    if(ncbDetailView.getTmbCheck()){
                        isTDRTMB = true;
                        if(lastTDRDateTMB!=null){
                            if(ncbDetailView.getAccountClosedDate()!=null){
                                if(lastTDRDateTMB.before(ncbDetailView.getAccountClosedDate())){
                                    lastTDRDateTMB = ncbDetailView.getAccountClosedDate();
                                }
                            }
                        } else {
                            if(ncbDetailView.getAccountClosedDate()!=null){
                                lastTDRDateTMB = ncbDetailView.getAccountClosedDate();
                            }
                        }
                    } else {
                        isTDROther = true;
                        if(lastTDRDateOther!=null){
                            if(ncbDetailView.getAccountClosedDate()!=null){
                                if(lastTDRDateOther.before(ncbDetailView.getAccountClosedDate())){
                                    lastTDRDateOther = ncbDetailView.getAccountClosedDate();
                                }
                            }
                        } else {
                            if(ncbDetailView.getAccountClosedDate()!=null){
                                lastTDRDateOther = ncbDetailView.getDateOfDebtRestructuring();
                            }
                        }
                    }
                }

                //get NPL
                if(customerInfo.getCustomerEntity()!=null && customerInfo.getCustomerEntity().getId() == 1){ //Individual
                    if((ncbDetailView.getCurrentPayment()!=null && ncbDetailView.getCurrentPayment().getNcbCode()!=null)
                        || (ncbDetailView.getHistoryPayment()!=null && ncbDetailView.getHistoryPayment().getNcbCode()!=null)){
                        if(((NCBPaymentCode.getValue(ncbDetailView.getCurrentPayment().getNcbCode())!=null && NCBPaymentCode.getValue(ncbDetailView.getCurrentPayment().getNcbCode()).value() >= NCBPaymentCode.CODE_003.value())
                                || (NCBPaymentCode.getValue(ncbDetailView.getHistoryPayment().getNcbCode())!=null && NCBPaymentCode.getValue(ncbDetailView.getHistoryPayment().getNcbCode()).value() >= NCBPaymentCode.CODE_003.value()))
                                && ncbDetailView.getDateOfInfo()!=null) {
                            isNPLFlag = true;
                            if(ncbDetailView.getTmbCheck()){
                                isNPLTMB = true;
                                if(lastNPLDateTMB!=null){
                                    if(ncbDetailView.getNplInfoDate()!=null){
                                        if(lastNPLDateTMB.before(ncbDetailView.getNplInfoDate())){
                                            lastNPLDateTMB = ncbDetailView.getNplInfoDate();
                                        }
                                    }
                                } else {
                                    if(ncbDetailView.getNplInfoDate()!=null){
                                        lastNPLDateTMB = ncbDetailView.getNplInfoDate();
                                    }
                                }
                            } else {
                                isNPLOther = true;
                                if(lastNPLDateOther!=null){
                                    if(ncbDetailView.getNplInfoDate()!=null){
                                        if(lastNPLDateOther.before(ncbDetailView.getNplInfoDate())){
                                            lastNPLDateOther = ncbDetailView.getNplInfoDate();
                                        }
                                    }
                                } else {
                                    if(ncbDetailView.getNplInfoDate()!=null){
                                        lastNPLDateOther = ncbDetailView.getNplInfoDate();
                                    }
                                }
                            }
                        }
                    }
                } else {
                    if((ncbDetailView.getCurrentPayment()!=null && ncbDetailView.getCurrentPayment().getNcbCode()!=null)
                            || (ncbDetailView.getHistoryPayment()!=null && ncbDetailView.getHistoryPayment().getNcbCode()!=null)){
                        if(((NCBPaymentCode.getValue(ncbDetailView.getCurrentPayment().getNcbCode())!=null && NCBPaymentCode.getValue(ncbDetailView.getCurrentPayment().getNcbCode()).value() >= NCBPaymentCode.CODE_004.value())
                                || (NCBPaymentCode.getValue(ncbDetailView.getHistoryPayment().getNcbCode())!=null && NCBPaymentCode.getValue(ncbDetailView.getHistoryPayment().getNcbCode()).value() >= NCBPaymentCode.CODE_004.value()))
                                && ncbDetailView.getDateOfInfo()!=null) {
                            isNPLFlag =true;
                            if(ncbDetailView.getTmbCheck()){
                                isNPLTMB = true;
                                if(lastNPLDateTMB!=null){
                                    if(ncbDetailView.getNplInfoDate()!=null){
                                        if(lastNPLDateTMB.before(ncbDetailView.getNplInfoDate())){
                                            lastNPLDateTMB = ncbDetailView.getNplInfoDate();
                                        }
                                    }
                                } else {
                                    if(ncbDetailView.getNplInfoDate()!=null){
                                        lastNPLDateTMB = ncbDetailView.getNplInfoDate();
                                    }
                                }
                            } else {
                                isNPLOther = true;
                                if(lastNPLDateOther!=null){
                                    if(ncbDetailView.getNplInfoDate()!=null){
                                        if(lastNPLDateOther.before(ncbDetailView.getNplInfoDate())){
                                            lastNPLDateOther = ncbDetailView.getNplInfoDate();
                                        }
                                    }
                                } else {
                                    if(ncbDetailView.getNplInfoDate()!=null){
                                        lastNPLDateOther = ncbDetailView.getNplInfoDate();
                                    }
                                }
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

                if(isNPLFlag){
                    ncbDetailView.setNplFlag(RadioValue.YES.value());
                } else {
                    ncbDetailView.setNplFlag(RadioValue.NO.value());
                }

                if(isTDRFlag){
                    ncbDetailView.setTdrFlag(RadioValue.YES.value());
                } else {
                    ncbDetailView.setTdrFlag(RadioValue.NO.value());
                }
            }

            ncbInfoView.setCurrentPaymentType(currentPaymentType);
            ncbInfoView.setHistoryPaymentType(historyPaymentType);
            ncbInfoView.setNcbLastInfoAsOfDate(lastInfoDate);

            ncbInfoView.setTdrFlag(0);
            if(isTDRTMB || isTDROther){
                ncbInfoView.setTdrFlag(2);
                ncbInfoView.setTdrTMBFlag(false);
                ncbInfoView.setTdrTMBMonth(0);
                ncbInfoView.setTdrTMBYear(0);
                ncbInfoView.setTdrOtherFlag(false);
                ncbInfoView.setTdrOtherMonth(0);
                ncbInfoView.setTdrOtherYear(0);
                if(isTDRTMB){
                    ncbInfoView.setTdrTMBFlag(true);
                    if(lastTDRDateTMB!=null){
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(lastTDRDateTMB);
                        ncbInfoView.setTdrTMBMonth(cal.get(Calendar.MONTH) + 1);
                        ncbInfoView.setTdrTMBYear(cal.get(Calendar.YEAR));
                    }
                }
                if(isTDROther){
                    ncbInfoView.setTdrOtherFlag(true);
                    if(lastTDRDateOther!=null){
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(lastTDRDateOther);
                        ncbInfoView.setTdrOtherMonth(cal.get(Calendar.MONTH) + 1);
                        ncbInfoView.setTdrOtherYear(cal.get(Calendar.YEAR));
                    }
                }
            } else {
                ncbInfoView.setTdrFlag(1);
            }

            ncbInfoView.setNplFlag(0);
            if(isNPLTMB || isNPLOther){
                Calendar calTmp = Calendar.getInstance();
                calTmp.setTime(new Date());
                ncbInfoView.setNplFlag(2);
                ncbInfoView.setNplTMBFlag(false);
                ncbInfoView.setNplTMBMonth(0);
                ncbInfoView.setNplTMBYear(0);
                ncbInfoView.setNplOtherFlag(false);
                ncbInfoView.setNplOtherMonth(0);
                ncbInfoView.setNplOtherYear(0);
                if(isNPLTMB){
                    ncbInfoView.setNplTMBFlag(true);
                    if(lastNPLDateTMB!=null){
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(lastNPLDateTMB);
                        ncbInfoView.setNplTMBMonth(cal.get(Calendar.MONTH) + 1);
                        ncbInfoView.setNplTMBYear(cal.get(Calendar.YEAR));
                    }
                }
                if(isNPLOther){
                    ncbInfoView.setNplOtherFlag(true);
                    if(lastNPLDateOther!=null){
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(lastNPLDateOther);
                        ncbInfoView.setNplOtherMonth(cal.get(Calendar.MONTH) + 1);
                        ncbInfoView.setNplOtherYear(cal.get(Calendar.YEAR));
                    }
                }
            } else {
                ncbInfoView.setNplFlag(1);
            }
        } else {
            ncbInfoView.setNplFlag(1);
            ncbInfoView.setNplTMBFlag(false);
            ncbInfoView.setNplTMBMonth(0);
            ncbInfoView.setNplTMBYear(0);
            ncbInfoView.setNplOtherFlag(false);
            ncbInfoView.setNplOtherMonth(0);
            ncbInfoView.setNplOtherYear(0);
            ncbInfoView.setTdrFlag(1);
            ncbInfoView.setTdrTMBFlag(false);
            ncbInfoView.setTdrTMBMonth(0);
            ncbInfoView.setTdrTMBYear(0);
            ncbInfoView.setTdrOtherFlag(false);
            ncbInfoView.setTdrOtherMonth(0);
            ncbInfoView.setTdrOtherYear(0);

            ncbInfoView.setCurrentPaymentType(null);
            ncbInfoView.setHistoryPaymentType(null);
            ncbInfoView.setNcbLastInfoAsOfDate(null);
        }
        return ncbInfoView;
    }

    public void calculateLoanCredit(NCB ncb, List<NCBDetail> ncbDetailList){
        // วงเงินสินเชื่อหมุนเวียนจากหน้า NCB
        BigDecimal loanCredit = BigDecimal.ZERO;
        // ภาระสินเชื่อประเภทอื่นๆ จากหน้า NCB ที่มี flag W/C = Yes
        BigDecimal loanCreditWC = BigDecimal.ZERO;
        // วงเงินสินเชื่อหมุนเวียนใน NCB ที่ flag เป็น TMB
        BigDecimal loanCreditTMB = BigDecimal.ZERO;
        // ภาระสินเชื่อประเภทอื่น ที่ flag TMB และ flag W/C
        BigDecimal loanCreditWCTMB = BigDecimal.ZERO;

        for(NCBDetail item : ncbDetailList){
            /*if(item.getAccountType() != null && (item.getAccountType().getWcFlag() == 1 || item.getWcFlag() == RadioValue.YES.value())){
                // วงเงินสินเชื่อหมุนเวียนจากหน้า NCB
                loanCredit = loanCredit.add(item.getLimit());
            }
            if(item.getAccountType() != null && (item.getAccountType().getWcFlag() == 1 || item.getWcFlag() == RadioValue.YES.value())){
                // ภาระสินเชื่อประเภทอื่นๆ จากหน้า NCB ที่มี flag W/C = Yes
                loanCreditWC = loanCreditWC.add(item.getOutstanding());
            }
            if(item.getAccountType() != null && item.getAccountTMBFlag() == RadioValue.YES.value() && (item.getAccountType().getWcFlag() == 1 || item.getWcFlag() == RadioValue.YES.value())){
                // วงเงินสินเชื่อหมุนเวียนใน NCB ที่ flag เป็น TMB
                loanCreditTMB = loanCreditTMB.add(item.getLimit());
            }
            if(item.getAccountType() != null && item.getAccountTMBFlag() == RadioValue.YES.value() && ( item.getAccountType().getWcFlag() == 1 || item.getWcFlag() == RadioValue.YES.value())){
                // ภาระสินเชื่อประเภทอื่น ที่ flag TMB และ flag W/C
                loanCreditWCTMB = loanCreditWCTMB.add(item.getOutstanding());
            }*/

            if(item.getRefinanceFlag() == RadioValue.NO.value()) {
                if(!Util.isNull(item.getAccountStatus()) && !Util.isZero(item.getAccountStatus().getDbrFlag()) && item.getAccountType() != null) {
                    if(item.getAccountType().getWcFlag() == 1){
                        loanCredit = loanCredit.add(item.getLimit());
                        if(item.getAccountTMBFlag() == RadioValue.YES.value()) {
                            loanCreditTMB = loanCreditTMB.add(item.getLimit());
                        }
                    } else if(item.getAccountType().getWcFlag() == 0) {
                        if(item.getWcFlag() == RadioValue.YES.value()) {
                            loanCreditWC = loanCreditWC.add(item.getOutstanding());
                            if(item.getAccountTMBFlag() == RadioValue.YES.value()) {
                                loanCreditWCTMB = loanCreditWCTMB.add(item.getOutstanding());
                            }
                        }
                    }
                }
            }
        }
        ncb.setLoanCreditNCB(loanCredit);
        ncb.setLoanCreditTMB(loanCreditTMB);
        ncb.setLoanCreditWC(loanCreditWC);
        ncb.setLoanCreditWCTMB(loanCreditWCTMB);
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
