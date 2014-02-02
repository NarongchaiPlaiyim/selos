package com.clevel.selos.businesscontrol;

import com.clevel.selos.dao.working.CustomerDAO;
import com.clevel.selos.dao.working.NCBDAO;
import com.clevel.selos.dao.working.NCBDetailDAO;
import com.clevel.selos.dao.working.WorkCaseDAO;
import com.clevel.selos.model.db.master.AccountStatus;
import com.clevel.selos.integration.SELOS;
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
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
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
    private CustomerDAO customerDAO;

    @Inject
    NCBDetailTransform ncbDetailTransform;
    @Inject
    NCBTransform ncbTransform;
    @Inject
    private LoanAccountTypeTransform loanAccountTypeTransform;

    @Inject
    public NCBInfoControl() {

    }

    private final BigDecimal plusMRR = BigDecimal.valueOf(6);


    public void onSaveNCBToDB(NCBInfoView NCBInfoView, List<NCBDetailView> NCBDetailViewList) {
        log.info("onSaveNCBToDB begin");

        NCB ncb = ncbTransform.transformToModel(NCBInfoView);
        ncbDAO.persist(ncb);
        log.info("persist ncb");

        List<NCBDetail> NCBDetailListToDelete = ncbDetailDAO.findNCBDetailByNcbId(ncb.getId());
        log.info("NCBDetailListToDelete :: {}", NCBDetailListToDelete.size());
        ncbDetailDAO.delete(NCBDetailListToDelete);
        log.info("delete NCBDetailListToDelete");

        List<NCBDetail> ncbDetailList = ncbDetailTransform.transformToModel(NCBDetailViewList, ncb);
        ncbDetailDAO.persist(ncbDetailList);

    }


    public NCBInfoView getNCBInfoView(long customerId) {
        log.info("getNcbInfoView :: customer id  :: {}", customerId);
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
                StringBuilder accountName = new StringBuilder();
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
}
