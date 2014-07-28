package com.clevel.selos.businesscontrol;

import com.clevel.selos.dao.master.UserDAO;
import com.clevel.selos.dao.working.*;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.ActionResult;
import com.clevel.selos.model.RoleValue;
import com.clevel.selos.model.db.working.*;
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
import java.util.List;

@Stateless
public class DBRControl extends BusinessControl {
    @Inject
    @SELOS
    private Logger log;

    @Inject
    private UserDAO userDAO;

    @Inject
    DBRDAO dbrDAO;
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
    ProposeLineDAO newCreditFacilityDAO;

    @Inject
    NCBInfoControl ncbInfoControl;

    @Inject
    public DBRControl() {
    }

    public ActionResult saveDBRInfo(DBRView dbrView, List<NCBDetailView> ncbDetailViews) {
        log.debug("begin saveDBRInfo");
        try {
            WorkCase workCase = workCaseDAO.findById(dbrView.getWorkCaseId());
            DBR dbr = calculateDBR(dbrView, workCase, ncbDetailViews);
            List<DBRDetail> newDbrDetails;
            newDbrDetails = dbr.getDbrDetails();
            dbr.setDbrDetails(null);
            dbrDAO.persist(dbr);
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
        }catch (Exception e){
            log.debug("Exception saveDBRInfo", e);
        }
        log.debug("complete saveDBRInfo");
        return ActionResult.SUCCEED;
    }

    public DBRView getDBRByWorkCase(long workCaseId) {
        WorkCase workCase = workCaseDAO.findById(workCaseId);
        DBR dbr = dbrDAO.findByWorkCaseId(workCaseId);
        BizInfoSummary bizInfoSummary = bizInfoSummaryDAO.onSearchByWorkCase(workCase);
        BankStatementSummary bankStatementSummary = bankStatementSummaryDAO.getByWorkCase(workCase);

        if(dbr != null && dbr.getId() != 0){
            if(bizInfoSummary != null){
                dbr.setIncomeFactor(bizInfoSummary.getWeightIncomeFactor());
            }

            if(bankStatementSummary != null){
                dbr.setMonthlyIncome(getMonthlyIncome(bankStatementSummary));
            }

            if(dbr.getMonthlyIncomeAdjust() == null){
                dbr.setMonthlyIncomeAdjust(dbr.getMonthlyIncomeAdjust());
            }
        } else {
            dbr = new DBR();
            if(bizInfoSummary != null){
                dbr.setIncomeFactor(bizInfoSummary.getWeightIncomeFactor());
            }
            if(bankStatementSummary != null){
                dbr.setMonthlyIncome(getMonthlyIncome(bankStatementSummary));
            }
            dbr.setDbrBeforeRequest(BigDecimal.ZERO);
            // MonthlyIncomeAdjust default from MonthlyIncome
            dbr.setMonthlyIncomeAdjust(getMonthlyIncome(bankStatementSummary));
            //MonthlyIncomePerMonth Default = 0
            dbr.setMonthlyIncomePerMonth(BigDecimal.ZERO);
        }

        dbr.setDbrInterest(getDBRInterest());

        DBRView dbrView = dbrTransform.transformToView(dbr);

        return dbrView;
    }

    private DBR calculateDBR(DBRView dbrView, WorkCase workCase, List<NCBDetailView> ncbDetailViews) throws Exception{
        log.debug("Begin calculateDBR");
        DBR dbr = dbrTransform.transformToModel(dbrView, workCase, getCurrentUser());
        List<DBRDetail> dbrDetails = dbrDetailTransform.getDbrDetailModels(dbrView.getDbrDetailViews(), getCurrentUser(), dbr);

        BigDecimal totalMonthDebtBorrowerStart = BigDecimal.ZERO;
        BigDecimal totalMonthDebtBorrowerFinal = BigDecimal.ZERO;

        //**NCB Borrower totalDebtForCalculate
        for(NCBDetailView ncbDetailView : Util.safetyList(ncbDetailViews)){
            totalMonthDebtBorrowerStart = Util.add(totalMonthDebtBorrowerStart, ncbDetailView.getDebtForCalculate());
            if(ncbDetailView.getRefinanceFlag() == 2){
                totalMonthDebtBorrowerFinal = Util.add(totalMonthDebtBorrowerFinal, ncbDetailView.getDebtForCalculate());
            }
        }

        //**Relate DbrDetail Calculate
        BigDecimal totalMonthDebtRelated = BigDecimal.ZERO;
        BigDecimal totalMonthDebtRelatedWc = BigDecimal.ZERO;
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
                    debtForCalculate = Util.multiply(dbrDetail.getInstallment(), BigDecimal.valueOf(5));
                    debtForCalculate = Util.divide(debtForCalculate, 100);
                    break;
                case 3:  // *10%
                    debtForCalculate = Util.multiply(dbrDetail.getInstallment(), BigDecimal.valueOf(10));
                    debtForCalculate = Util.divide(debtForCalculate, 100);
                    break;
                default: // non calculator
                    break;
            }
            dbrDetail.setDebtForCalculate(debtForCalculate);

            if(dbrDetail.getLoanType() != null && dbrDetail.getLoanType().getWcFlag() == 1){
                totalMonthDebtRelatedWc = Util.add(totalMonthDebtRelatedWc, dbrDetail.getDebtForCalculate());
            }

            totalMonthDebtRelated = Util.add(totalMonthDebtRelated, dbrDetail.getDebtForCalculate());
        }
        //** END DbrDetail

        BigDecimal dbrBeforeRequest = BigDecimal.ZERO;
        BigDecimal netMonthlyIncome = BigDecimal.ZERO;
        BigDecimal currentDBR = BigDecimal.ZERO;
        //**** Begin DBRInfo ****//
        currentDBR = Util.add(totalMonthDebtBorrowerStart, totalMonthDebtRelated);

        netMonthlyIncome = Util.multiply(dbr.getMonthlyIncomeAdjust(), dbr.getIncomeFactor());
        netMonthlyIncome = Util.divide(netMonthlyIncome, 100);
        netMonthlyIncome = Util.add(netMonthlyIncome,dbr.getMonthlyIncomePerMonth());

        dbrBeforeRequest = Util.divide(currentDBR, netMonthlyIncome);
        dbrBeforeRequest = Util.multiply(dbrBeforeRequest, BigDecimal.valueOf(100));

        //Ex summary Final DBR BigDecimal debt = BigDecimal.ZERO;

        BigDecimal finalDBR = BigDecimal.ZERO;
        finalDBR = calculateFinalDBR(totalMonthDebtBorrowerFinal, totalMonthDebtRelated, netMonthlyIncome, workCase);

        // update dbr
        dbr.setNetMonthlyIncome(netMonthlyIncome);
        dbr.setCurrentDBR(currentDBR);
        dbr.setDbrBeforeRequest(dbrBeforeRequest);
        dbr.setDbrDetails(dbrDetails);
        dbr.setFinalDBR(finalDBR);
        dbr.setTotalMonthDebtBorrowerStart(totalMonthDebtBorrowerStart);
        dbr.setTotalMonthDebtRelatedWc(totalMonthDebtRelatedWc);
        dbr.setTotalMonthDebtBorrowerFinal(totalMonthDebtBorrowerFinal);
        log.debug("calculateDBR complete");
        return dbr;
    }

    public ActionResult updateValueOfDBR(long workCaseId){
        log.debug("Begin updateValueOfDBR");
        DBRView dbrView =  getDBRByWorkCase(workCaseId);

        if(!Util.isNull(dbrView)){
            WorkCase workCase = workCaseDAO.findById(workCaseId);
            BankStatementSummary bankStatementSummary = bankStatementSummaryDAO.getByWorkCase(workCase);
            BizInfoSummary bizInfoSummary = bizInfoSummaryDAO.onSearchByWorkCase(workCase);

            if(!Util.isNull(bankStatementSummary)){
                dbrView.setMonthlyIncome(getMonthlyIncome(bankStatementSummary));
                if (Util.isNull(dbrView.getMonthlyIncomeAdjust()) || Util.isZero(dbrView.getMonthlyIncomeAdjust())){
                    dbrView.setMonthlyIncomeAdjust(dbrView.getMonthlyIncome());
                }
            }

            if(!Util.isNull(bizInfoSummary)){
                dbrView.setIncomeFactor(bizInfoSummary.getWeightIncomeFactor());
            }
            List<NCBDetailView> ncbDetailViews = ncbInfoControl.getNCBForCalDBR(workCaseId);
            DBR dbr = null;
            try {
                dbr = calculateDBR(dbrView, workCase, ncbDetailViews);
                dbrDAO.persist(dbr);
            } catch (Exception e) {
                log.debug("Exception updateValueOfDBR", e);
            }
        log.debug("complete updateValueOfDBR");
        }
        return ActionResult.SUCCESS;
    }



    private BigDecimal calculateFinalDBR(BigDecimal totalMonthDebtBorrowerFinal, BigDecimal totalMonthDebtRelated,BigDecimal netMonthlyIncome, WorkCase workCase){
        BigDecimal result = BigDecimal.ZERO;
        BigDecimal totalPurposeForDBR = BigDecimal.ZERO;
        int roleId = getCurrentUser().getRole().getId();
        ProposeLine newCreditFacility = newCreditFacilityDAO.findByWorkCaseId(workCase.getId());
        //todo not confirm
        BigDecimal debt = BigDecimal.ZERO;
        debt = Util.add(totalMonthDebtBorrowerFinal, totalMonthDebtRelated);
        if(newCreditFacility != null && newCreditFacility.getId() > 0)  {
            totalPurposeForDBR = newCreditFacility.getTotalProposeLoanDBR();
        }
        debt = Util.add(debt, totalPurposeForDBR);
        result = Util.divide(debt, netMonthlyIncome);
        result = Util.multiply(result, BigDecimal.valueOf(100));
        return result;
    }

    private BigDecimal getMonthlyIncome(BankStatementSummary bankStatementSummary){
        BigDecimal monthlyIncome = BigDecimal.ZERO;
        int roleId = getCurrentUser().getRole().getId();
        if(roleId == RoleValue.UW.id()){
            if(bankStatementSummary.getGrdTotalIncomeNetUW() == null)
                monthlyIncome = bankStatementSummary.getGrdTotalIncomeNetBDM();
            else
                monthlyIncome = bankStatementSummary.getGrdTotalIncomeNetUW();

        }else if(roleId == RoleValue.BDM.id()){
            monthlyIncome = bankStatementSummary.getGrdTotalIncomeNetBDM();
        }
        return monthlyIncome;
    }

}
