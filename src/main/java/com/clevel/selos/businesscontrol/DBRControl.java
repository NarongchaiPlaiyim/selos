package com.clevel.selos.businesscontrol;

import com.clevel.selos.dao.master.UserDAO;
import com.clevel.selos.dao.working.*;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.RoleUser;
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
    DBRDAO dbrdao;

    @Inject
    DBRDetailDAO dbrDetailDAO;

    @Inject
    WorkCaseDAO workCaseDAO;

    @Inject
    UserDAO userDAO;

    @Inject
    DBRTransform dbrTransform;

    @Inject
    DBRDetailTransform dbrDetailTransform;

    @Inject
    BizInfoSummaryDAO bizInfoSummaryDAO;

    @Inject
    BankStatementSummaryDAO bankStatementSummaryDAO;

    @Inject
    NCBInfoControl ncbInfoControl;



    public DBRControl() {

    }

    public void saveDBRInfo(DBRView dbrView, List<NCBDetailView> ncbDetailViews) {
        WorkCase workCase = workCaseDAO.findById(dbrView.getWorkCaseId());
        User user = userDAO.findById(dbrView.getUserId());
        DBR dbr = calculateDBR(dbrView, ncbDetailViews, user, workCase);
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
    }

    public DBRView getDBRByWorkCase(long workCaseId) {
        WorkCase workCase = workCaseDAO.findById(workCaseId);
        DBR dbr = (DBR) dbrdao.createCriteria().add(Restrictions.eq("workCase", workCase)).uniqueResult();
        if(dbr == null){
            dbr = new DBR();
            BizInfoSummary bizInfoSummary = bizInfoSummaryDAO.onSearchByWorkCase(workCase);
            if(bizInfoSummary != null){
                dbr.setIncomeFactor(bizInfoSummary.getWeightIncomeFactor());
            }
            BankStatementSummary bankStatementSummary = bankStatementSummaryDAO.getByWorkcase(workCase);
            if(bankStatementSummary != null){
                dbr.setMonthlyIncome(getGrandTotalIncome(bankStatementSummary, workCase));
            }
            dbr.setDbrBeforeRequest(BigDecimal.ZERO);

        }

        dbr.setDbrInterest(getDBRInterest());
        DBRView dbrView = dbrTransform.getDBRView(dbr);
        return dbrView;
    }


    public void updateValueAndCalculate(BankStatementSummary bankStatementSummary, BizInfoSummary bizInfoSummary, long workCaseId, String userId){
        WorkCase workCase = workCaseDAO.findById(workCaseId);
        DBRView dbrView =  getDBRByWorkCase(workCaseId);
        if(bankStatementSummary != null){
            dbrView.setMonthlyIncome(getGrandTotalIncome(bankStatementSummary, workCase));
        }
        if(bizInfoSummary != null){
            dbrView.setIncomeFactor(bizInfoSummary.getSumWeightInterviewedIncomeFactorPercent());
        }

        User user = userDAO.findById(userId);
        List<NCBDetailView> ncbDetailViews = ncbInfoControl.getNCBForCalDBR(workCaseId);
        DBR dbr =  calculateDBR(dbrView, ncbDetailViews, user, workCase);
        dbrdao.persist(dbr);


    }

    private DBR calculateDBR(DBRView dbrView, List<NCBDetailView> ncbDetailViews, User user, WorkCase workCase){

        int roleId = user.getRole().getId();
        DBR dbr = dbrTransform.getDBRInfoModel(dbrView, workCase, user);
        List<DBRDetail> dbrDetails = dbrDetailTransform.getDbrDetailModels(dbrView.getDbrDetailViews(), user, dbr);

        //**NCB Borrower totalDebtForCalculate
        BigDecimal totalMonthDebtBorrower = BigDecimal.ZERO;
        for(NCBDetailView ncbDetailView : Util.safetyList(ncbDetailViews)){
            totalMonthDebtBorrower = totalMonthDebtBorrower.add(ncbDetailView.getDebtForCalculate());
        }

        //**Relate DbrDetail Calculate
        BigDecimal totalMonthDebtRelated = BigDecimal.ZERO;
        for(DBRDetail dbrDetail : Util.safetyList(dbrDetails)){
            int loanType = dbrDetail.getLoanType().getCalculateType();
            final  BigDecimal month = BigDecimal.valueOf(12);
            BigDecimal debtForCalculate = BigDecimal.ZERO;
            switch (loanType){
                case 1:  //normal
                    if(dbrDetail.getInstallment().compareTo(BigDecimal.ZERO) != 0){  // Installment != 0
                        debtForCalculate = debtForCalculate.add(dbrDetail.getInstallment());
                    }else {
                        debtForCalculate =  dbrDetail.getLimit().multiply(dbr.getDbrInterest());
                        debtForCalculate = debtForCalculate.divide(BigDecimal.valueOf(100));
                        debtForCalculate = debtForCalculate.divide(month, 2, RoundingMode.HALF_UP);
                    }
                    break;
                case 2:    //*5%
                    debtForCalculate =  dbrDetail.getLimit().multiply(BigDecimal.valueOf(5));
                    debtForCalculate = debtForCalculate.divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
                    break;
                case 3:  // *10%
                    debtForCalculate = dbrDetail.getLimit().multiply(BigDecimal.valueOf(10));
                    debtForCalculate = debtForCalculate.divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
                    break;
                default:
                    break;
            }
            dbrDetail.setDebtForCalculate(debtForCalculate);
            totalMonthDebtRelated = totalMonthDebtRelated.add(dbrDetail.getDebtForCalculate());
        }
        //** END DbrDetail
        BigDecimal dbrBeforeRequest = BigDecimal.ZERO;
        BigDecimal netMonthlyIncome = BigDecimal.ZERO;
        BigDecimal currentDBR = BigDecimal.ZERO;
        //DbrInfo

        currentDBR = totalMonthDebtBorrower.add(totalMonthDebtRelated);

        if(roleId == RoleUser.UW.getValue()){
            //todo confirm formula adjusted income Factor
            netMonthlyIncome = dbr.getMonthlyIncome().multiply(dbr.getIncomeFactor());
            netMonthlyIncome = netMonthlyIncome.divide(BigDecimal.valueOf(100));
            if(netMonthlyIncome.compareTo(BigDecimal.ZERO) == 0){
                netMonthlyIncome = dbr.getMonthlyIncome().multiply(dbr.getIncomeFactor());
            }
            netMonthlyIncome = netMonthlyIncome.add(dbr.getMonthlyIncomePerMonth());

        }else if(roleId == RoleUser.BDM.getValue()){
            //netMonthlyIncome
            netMonthlyIncome = dbr.getMonthlyIncome().multiply(dbr.getIncomeFactor());
            netMonthlyIncome = netMonthlyIncome.divide(BigDecimal.valueOf(100));
            netMonthlyIncome = netMonthlyIncome.add(dbr.getMonthlyIncomePerMonth());
        }

        dbrBeforeRequest = Util.divide(currentDBR, netMonthlyIncome);

        // update dbr
        dbr.setNetMonthlyIncome(netMonthlyIncome);
        dbr.setCurrentDBR(currentDBR);
        dbr.setDbrBeforeRequest(dbrBeforeRequest);
        dbr.setDbrDetails(dbrDetails);
        log.debug("calculateDBR complete");
        return dbr;
    }


    private BigDecimal getDBRInterest(){
        BigDecimal result = BigDecimal.ZERO;
        //todo waiting get form to Database
        BigDecimal mrr = BigDecimal.TEN;
        result = mrr.add(BigDecimal.valueOf(3));
        return result;
    }

    private BigDecimal getGrandTotalIncome(BankStatementSummary bankStatementSummary, WorkCase workCase){
        //todo set monthlyIncome
        BigDecimal monthlyIncome = BigDecimal.ZERO;
        if(workCase.getBorrowerType().getId() == RoleUser.UW.getValue()){
            if(bankStatementSummary.getGrdTotalIncomeNetUW() == null || bankStatementSummary.getGrdTotalIncomeNetUW().compareTo(BigDecimal.ZERO) == 0 )
                monthlyIncome = bankStatementSummary.getGrdTotalIncomeNetBDM();
            else
                monthlyIncome = bankStatementSummary.getGrdTotalIncomeNetUW();

        }else if(workCase.getBorrowerType().getId() == RoleUser.BDM.getValue()){
            monthlyIncome = bankStatementSummary.getGrdTotalIncomeNetBDM();
        }
        return monthlyIncome;

    }


}
