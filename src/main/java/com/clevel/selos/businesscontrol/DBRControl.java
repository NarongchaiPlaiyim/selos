package com.clevel.selos.businesscontrol;

import com.clevel.selos.controller.CreditFacPropose;
import com.clevel.selos.dao.master.UserDAO;
import com.clevel.selos.dao.working.*;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.ActionResult;
import com.clevel.selos.model.RoleUser;
import com.clevel.selos.model.db.master.RoleType;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.working.*;
import com.clevel.selos.model.view.DBRDetailView;
import com.clevel.selos.model.view.DBRView;
import com.clevel.selos.model.view.NCBDetailView;
import com.clevel.selos.transform.DBRDetailTransform;
import com.clevel.selos.transform.DBRTransform;
import com.clevel.selos.util.Util;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class DBRControl extends BusinessControl {
    @Inject
    @SELOS
    private Logger log;

    @Inject
    private UserDAO userDAO;

    @Inject
    DBRDAO dbrdao;
    @Inject
    DBRDetailDAO dbrDetailDAO;
    @Inject
    WorkCaseDAO workCaseDAO;
    @Inject
    BizInfoSummaryDAO bizInfoSummaryDAO;
    @Inject
    BankStatementSummaryDAO bankStatementSummaryDAO;
    @Inject
    DBRTransform dbrTransform;
    @Inject
    DBRDetailTransform dbrDetailTransform;
    @Inject
    NewCreditFacilityDAO newCreditFacilityDAO;

    @Inject
    NCBInfoControl ncbInfoControl;

    @Inject
    public DBRControl() {
    }

    public ActionResult saveDBRInfo(DBRView dbrView, List<NCBDetailView> ncbDetailViews) {
        WorkCase workCase = workCaseDAO.findById(dbrView.getWorkCaseId());
        DBR dbr = calculateDBR(dbrView, ncbDetailViews, workCase);
        List<DBRDetail> newDbrDetails = new ArrayList<DBRDetail>();  // new record
        newDbrDetails = dbr.getDbrDetails();
        dbr.setDbrDetails(null);
        dbrdao.persist(dbr);
        List<DBRDetail> oldDbrDetails = dbrDetailDAO.createCriteria().add(Restrictions.eq("dbr", dbr)).list();  // old record
        if (newDbrDetails != null && !newDbrDetails.isEmpty()) {
            if (oldDbrDetails == null || oldDbrDetails.isEmpty() ) {
                dbrDetailDAO.persist(newDbrDetails); //ADD New OR Update
            } else {
                //delete old without new record
                for (DBRDetail oldDbrDetail : oldDbrDetails) {
                    boolean isDelete = true;
                    for (DBRDetail newDbrDetail : newDbrDetails) {
                        if (oldDbrDetail.getId() == newDbrDetail.getId()) {
                            isDelete = false;
                        }
                    }
                    if (isDelete) {
                        dbrDetailDAO.delete(oldDbrDetail);
                    }
                }
                //Add new record
                dbrDetailDAO.persist(newDbrDetails);
            }
        } else {  //Delete all record from DBR
            if (oldDbrDetails != null && !oldDbrDetails.isEmpty()) {
                dbrDetailDAO.delete(oldDbrDetails);
            }
        }
        return ActionResult.SUCCEED;
    }

    public DBRView getDBRByWorkCase(long workCaseId) {
        WorkCase workCase = workCaseDAO.findById(workCaseId);
        User user = getCurrentUser();
        DBR dbr = (DBR) dbrdao.createCriteria().add(Restrictions.eq("workCase", workCase)).uniqueResult();
        if(dbr == null){
            dbr = new DBR();
            BizInfoSummary bizInfoSummary = bizInfoSummaryDAO.onSearchByWorkCase(workCase);
            if(bizInfoSummary != null){
                dbr.setIncomeFactor(bizInfoSummary.getWeightIncomeFactor());
            }
            BankStatementSummary bankStatementSummary = bankStatementSummaryDAO.getByWorkCase(workCase);
            if(bankStatementSummary != null){
                dbr.setMonthlyIncome(getMonthlyIncome(bankStatementSummary));
            }
            dbr.setDbrBeforeRequest(BigDecimal.ZERO);
            // MonthlyIncomeAdjust default from MonthlyIncome
            dbr.setMonthlyIncomeAdjust(dbr.getMonthlyIncome());
            //MonthlyIncomePerMonth Default = 0
            dbr.setMonthlyIncomePerMonth(BigDecimal.ZERO);

        }else{
            if(dbr.getIncomeFactor() == null){
                BizInfoSummary bizInfoSummary = bizInfoSummaryDAO.onSearchByWorkCase(workCase);
                if(bizInfoSummary != null){
                    dbr.setIncomeFactor(bizInfoSummary.getWeightIncomeFactor());
                }
            }
            if(dbr.getMonthlyIncome() == null){
                BankStatementSummary bankStatementSummary = bankStatementSummaryDAO.getByWorkCase(workCase);
                if(bankStatementSummary != null){
                    dbr.setMonthlyIncome(getMonthlyIncome(bankStatementSummary));
                }
            }
            if(dbr.getMonthlyIncomeAdjust() == null){
                dbr.setMonthlyIncomeAdjust(dbr.getMonthlyIncome());
            }
        }
        dbr.setDbrInterest(getDBRInterest());

        DBRView dbrView = dbrTransform.getDBRView(dbr);
        return dbrView;
    }

    private DBR calculateDBR(DBRView dbrView, List<NCBDetailView> ncbDetailViews, WorkCase workCase){
        int roleId = getCurrentUser().getRole().getId();
        DBR dbr = dbrTransform.getDBRInfoModel(dbrView, workCase, getCurrentUser());
        List<DBRDetail> dbrDetails = dbrDetailTransform.getDbrDetailModels(dbrView.getDbrDetailViews(), getCurrentUser(), dbr);

        //**NCB Borrower totalDebtForCalculate
        BigDecimal totalMonthDebtBorrower = BigDecimal.ZERO;
        for(NCBDetailView ncbDetailView : Util.safetyList(ncbDetailViews)){
            totalMonthDebtBorrower = Util.add(totalMonthDebtBorrower, ncbDetailView.getDebtForCalculate());
        }

        //**Relate DbrDetail Calculate
        BigDecimal totalMonthDebtRelated = BigDecimal.ZERO;
        for(DBRDetail dbrDetail : Util.safetyList(dbrDetails)){
            int loanType = dbrDetail.getLoanType().getCalculateType();
            final BigDecimal month = BigDecimal.valueOf(12);
            BigDecimal debtForCalculate = BigDecimal.ZERO;
            switch (loanType){
                case 1:  //normal
                    if(dbrDetail.getInstallment().compareTo(BigDecimal.ZERO) != 0){  // Installment != 0
                        debtForCalculate = dbrDetail.getInstallment();
                    }else {
                        debtForCalculate = Util.multiply(dbrDetail.getLimit(), dbr.getDbrInterest());
                        debtForCalculate = Util.divide(debtForCalculate, 100);
                        debtForCalculate = Util.divide(debtForCalculate, month);
                    }
                    break;
                case 2:    //*5%
                    debtForCalculate = Util.multiply(dbrDetail.getLimit(), BigDecimal.valueOf(5));
                    debtForCalculate = Util.divide(debtForCalculate, 100);
                    break;
                case 3:  // *10%
                    debtForCalculate = Util.multiply(dbrDetail.getLimit(), BigDecimal.valueOf(10));
                    debtForCalculate = Util.divide(debtForCalculate, 100);
                    break;
                default:
                    break;
            }
            dbrDetail.setDebtForCalculate(debtForCalculate);

            totalMonthDebtRelated = Util.add(totalMonthDebtRelated, dbrDetail.getDebtForCalculate());
        }
        //** END DbrDetail

        BigDecimal dbrBeforeRequest = BigDecimal.ZERO;
        BigDecimal netMonthlyIncome = BigDecimal.ZERO;
        BigDecimal currentDBR = BigDecimal.ZERO;
        //**** Begin DBRInfo ****//
        currentDBR = Util.add(totalMonthDebtBorrower, totalMonthDebtRelated);

        netMonthlyIncome = Util.multiply(dbr.getMonthlyIncomeAdjust(), dbr.getIncomeFactor());
        netMonthlyIncome = Util.divide(netMonthlyIncome, 100);
        netMonthlyIncome = Util.add(netMonthlyIncome,dbr.getMonthlyIncomePerMonth());

        dbrBeforeRequest = Util.divide(currentDBR, netMonthlyIncome);

        //Ex summary Final DBR BigDecimal debt = BigDecimal.ZERO;

        BigDecimal finalDBR = BigDecimal.ZERO;
        finalDBR = calculateFinalDBR(totalMonthDebtBorrower, totalMonthDebtRelated, netMonthlyIncome, workCase);

        // update dbr
        dbr.setNetMonthlyIncome(netMonthlyIncome);
        dbr.setCurrentDBR(currentDBR);
        dbr.setDbrBeforeRequest(dbrBeforeRequest);
        dbr.setDbrDetails(dbrDetails);
        dbr.setFinalDBR(finalDBR);
        log.debug("calculateDBR complete");
        return dbr;
    }

    public ActionResult updateValueOfDBR(long workCaseId){
        DBRView dbrView =  getDBRByWorkCase(workCaseId);
        if(dbrView != null){
            if(dbrView.getId() == 0){
                return ActionResult.FAILED;
            }
            WorkCase workCase = workCaseDAO.findById(workCaseId);
            BankStatementSummary bankStatementSummary = bankStatementSummaryDAO.getByWorkCase(workCase);
            BizInfoSummary bizInfoSummary = bizInfoSummaryDAO.onSearchByWorkCase(workCase);
            if(bankStatementSummary != null){
                dbrView.setMonthlyIncome(getMonthlyIncome(bankStatementSummary));
            }
            if(bizInfoSummary != null){
                dbrView.setIncomeFactor(bizInfoSummary.getWeightIncomeFactor());
            }
            List<NCBDetailView> ncbDetailViews = ncbInfoControl.getNCBForCalDBR(workCaseId);
            DBR dbr =  calculateDBR(dbrView, ncbDetailViews, workCase);
            dbrdao.persist(dbr);
        }
        return ActionResult.SUCCESS;
    }

    public void updateFinalDBR(long workCaseId){
        WorkCase workCase = workCaseDAO.findById(workCaseId);
        List<NCBDetailView> ncbDetailViews = ncbInfoControl.getNCBForCalDBR(workCaseId);
        BigDecimal totalMonthDebtBorrower = BigDecimal.ZERO;
        for(NCBDetailView ncbDetailView : Util.safetyList(ncbDetailViews)){
            totalMonthDebtBorrower = totalMonthDebtBorrower.add(ncbDetailView.getDebtForCalculate());
        }
        BigDecimal totalMonthDebtRelated = BigDecimal.ZERO;
        DBR dbr = (DBR) dbrdao.createCriteria().add(Restrictions.eq("workCase", workCase)).uniqueResult();
        if(dbr != null){
            List<DBRDetail> dbrDetails = dbr.getDbrDetails();
            for(DBRDetail dbrDetail : Util.safetyList(dbrDetails)){
                totalMonthDebtRelated = Util.add(totalMonthDebtRelated, dbrDetail.getDebtForCalculate());
            }
            BigDecimal finalDBR = BigDecimal.ZERO;
            finalDBR =  calculateFinalDBR(totalMonthDebtBorrower, totalMonthDebtRelated, dbr.getNetMonthlyIncome(), workCase);
            dbr.setFinalDBR(finalDBR);
            dbrdao.persist(dbr);
        }

    }


    private BigDecimal calculateFinalDBR(BigDecimal totalMonthDebtBorrower, BigDecimal totalMonthDebtRelated,BigDecimal netMonthlyIncome, WorkCase workCase){
        BigDecimal result = BigDecimal.ZERO;
        BigDecimal totalPurposeForDBR = BigDecimal.ZERO;
        int roleId = getCurrentUser().getRole().getId();
        NewCreditFacility newCreditFacility = newCreditFacilityDAO.findByWorkCase(workCase);
        //todo non confirm
        if(newCreditFacility != null){
            totalPurposeForDBR = newCreditFacility.getTotalProposeLoanDBR();
            if(roleId == RoleUser.UW.getValue()){

            }else if(roleId == RoleUser.BDM.getValue()){

            }
        }
        BigDecimal debt = BigDecimal.ZERO;
        debt = Util.add(totalMonthDebtBorrower, totalMonthDebtRelated);
        debt = Util.add(debt, totalPurposeForDBR);
        result = Util.divide(debt, netMonthlyIncome);
        return debt;
    }

    private BigDecimal getMonthlyIncome(BankStatementSummary bankStatementSummary){
        BigDecimal monthlyIncome = BigDecimal.ZERO;
        int roleId = getCurrentUser().getRole().getId();
        if(roleId == RoleUser.UW.getValue()){
            if(bankStatementSummary.getGrdTotalIncomeNetUW() == null)
                monthlyIncome = bankStatementSummary.getGrdTotalIncomeNetBDM();
            else
                monthlyIncome = bankStatementSummary.getGrdTotalIncomeNetUW();

        }else if(roleId == RoleUser.BDM.getValue()){
            monthlyIncome = bankStatementSummary.getGrdTotalIncomeNetBDM();
        }

        return monthlyIncome;

    }
}
