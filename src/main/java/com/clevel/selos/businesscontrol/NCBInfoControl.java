package com.clevel.selos.businesscontrol;

import com.clevel.selos.dao.working.CustomerDAO;
import com.clevel.selos.dao.working.NCBDAO;
import com.clevel.selos.dao.working.NCBDetailDAO;
import com.clevel.selos.dao.working.WorkCaseDAO;
import com.clevel.selos.model.db.master.AccountType;
import com.clevel.selos.model.db.working.Customer;
import com.clevel.selos.model.db.working.NCB;
import com.clevel.selos.model.db.working.NCBDetail;
import com.clevel.selos.model.view.NCBDetailView;
import com.clevel.selos.model.view.NCBInfoView;
import com.clevel.selos.transform.LoanAccountTypeTransform;
import com.clevel.selos.transform.NCBDetailTransform;
import com.clevel.selos.transform.NCBTransform;
import org.hibernate.criterion.Restrictions;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;


@Stateless
public class NCBInfoControl extends BusinessControl {
    @Inject
    NCBDetailTransform ncbDetailTransform;

    @Inject
    NCBTransform ncbTransform;

    @Inject
    NCBDAO ncbDAO;

    @Inject
    NCBDetailDAO ncbDetailDAO;

    @Inject
    WorkCaseDAO workCaseDAO;

    @Inject
    private CustomerDAO customerDAO;

    @Inject
    private LoanAccountTypeTransform loanAccountTypeTransform;

    @Inject
    public NCBInfoControl() {

    }

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
        List<NCBDetailView> ncbDetailViewList = null;

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
        log.info("Begin getNCBForCalDBR workcase:{}", workcaseId);
        List<Customer> customers = customerDAO.findByWorkCaseId(workcaseId);

        List<NCB> ncbs = ncbDAO.createCriteria().add(Restrictions.in("customer", customers)).list();
        log.info("ncbs :{}", ncbs.size());
        for(NCB ncb : ncbs){
            Customer customer = ncb.getCustomer();
            List<NCBDetail> ncbDetails = ncbDetailDAO.createCriteria().add(Restrictions.eq("ncb", ncb)).list();
            AccountType accountType;
            for(NCBDetail ncbDetail : ncbDetails){
                log.info("ncbDetail :{}", ncbDetail);
                accountType = ncbDetail.getAccountType();
                if(accountType.getDbrFlag() == 1){
                    NCBDetailView ncbDetailView = new NCBDetailView();
                    ncbDetailView.setId(ncbDetail.getId());
                    ncbDetailView.setLimit(ncbDetail.getLimit());
                    ncbDetailView.setInstallment(ncbDetail.getInstallment());
                    BigDecimal debtForCalculate = BigDecimal.ZERO;
                    BigDecimal dbrInterest = BigDecimal.TEN.add(BigDecimal.valueOf(3));
                    switch (accountType.getCalculateType()){
                        case 1:
                            if(ncbDetail.getInstallment().compareTo(BigDecimal.ZERO) == 0){
                                debtForCalculate = ncbDetail.getLimit().multiply(dbrInterest);
                                debtForCalculate = debtForCalculate.divide(BigDecimal.valueOf(12), 2, RoundingMode.HALF_UP);
                            }else{
                                debtForCalculate = ncbDetail.getInstallment();
                            }
                            break;
                        case 2:
                            debtForCalculate = ncbDetail.getOutstanding().multiply(BigDecimal.valueOf(5));
                            debtForCalculate = debtForCalculate.divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
                            break;
                        case 3:
                            debtForCalculate = ncbDetail.getOutstanding().multiply(BigDecimal.valueOf(10));
                            debtForCalculate = debtForCalculate.divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
                            break;
                        default:
                            break;
                    }
                    ncbDetailView.setDebtForCalculate(debtForCalculate);
                    StringBuilder accountName = new StringBuilder();
                    accountName.append(customer.getTitle().getTitleTh())
                            .append(" ").append(customer.getNameTh())
                            .append(" ").append(customer.getLastNameTh());
                    ncbDetailView.setAccountName(accountName.toString());
                    ncbDetailView.setLoanAccountTypeView(loanAccountTypeTransform.getLoanAccountTypeView(ncbDetail.getAccountType()));
                    ncbDetailViews.add(ncbDetailView);
                }
            }
        }
        return ncbDetailViews;
    }
}
