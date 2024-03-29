package com.clevel.selos.businesscontrol;

import com.clevel.selos.businesscontrol.master.BaseRateControl;
import com.clevel.selos.dao.master.UserDAO;
import com.clevel.selos.dao.working.*;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.ActionResult;
import com.clevel.selos.model.RadioValue;
import com.clevel.selos.model.RoleValue;
import com.clevel.selos.model.db.master.AccountStatus;
import com.clevel.selos.model.db.master.AccountType;
import com.clevel.selos.model.db.working.*;
import com.clevel.selos.model.view.DBRView;
import com.clevel.selos.model.view.NCBDetailView;
import com.clevel.selos.model.view.UserSysParameterView;
import com.clevel.selos.transform.DBRDetailTransform;
import com.clevel.selos.transform.DBRTransform;
import com.clevel.selos.transform.LoanAccountTypeTransform;
import com.clevel.selos.util.Util;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.math.BigDecimal;
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
    private DBRDAO dbrDAO;
    @Inject
    private DBRDetailDAO dbrDetailDAO;
    @Inject
    private WorkCaseDAO workCaseDAO;
    @Inject
    private BizInfoSummaryDAO bizInfoSummaryDAO;
    @Inject
    private BankStatementSummaryDAO bankStatementSummaryDAO;
    @Inject
    private ProposeLineDAO newCreditFacilityDAO;
    @Inject
    private NCBDetailDAO ncbDetailDAO;

    @Inject
    private DBRTransform dbrTransform;
    @Inject
    private DBRDetailTransform dbrDetailTransform;
    @Inject
    private LoanAccountTypeTransform loanAccountTypeTransform;

    @Inject
    private BaseRateControl baseRateControl;
    @Inject
    private UserSysParameterControl userSysParameterControl;

    @Inject
    public DBRControl() {
    }

    public ActionResult saveDBRInfo(DBRView dbrView) {
        log.debug("begin saveDBRInfo");
        try {
            WorkCase workCase = workCaseDAO.findById(dbrView.getWorkCaseId());
            DBR dbr = calculateDBR(dbrView, workCase);
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
        DBRView dbrView = new DBRView();

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

            dbrView = dbrTransform.transformToView(dbr);

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

            dbrView = dbrTransform.transformToView(dbr);

            UserSysParameterView userSysParameterView = userSysParameterControl.getSysParameterValue("FIX_RATE");
            int dbrInterest = 0;
            if(!Util.isNull(userSysParameterView)){
                dbrInterest = Util.parseInt(userSysParameterView.getValue(), 0);
            }

            dbrView.setDbrMarketableFlag(1);
            dbrView.setDbrInterest(Util.add(baseRateControl.getMRRValue(),BigDecimal.valueOf(dbrInterest)));
        }
        return dbrView;
    }

    private DBR calculateDBR(DBRView dbrView, WorkCase workCase) throws Exception{
        log.debug("Begin calculateDBR");
        if(dbrView.getDbrMarketableFlag() != 2) {
            UserSysParameterView userSysParameterView = userSysParameterControl.getSysParameterValue("FIX_RATE");
            int dbrInterest = 0;
            if(!Util.isNull(userSysParameterView)){
                dbrInterest = Util.parseInt(userSysParameterView.getValue(), 0);
            }
            dbrView.setDbrInterest(Util.add(baseRateControl.getMRRValue(),BigDecimal.valueOf(dbrInterest)));
        } else {
            dbrView.setDbrInterest(baseRateControl.getMRRValue());
        }
        DBR dbr = dbrTransform.transformToModel(dbrView, workCase, getCurrentUser());
        List<DBRDetail> dbrDetails = dbrDetailTransform.getDbrDetailModels(dbrView.getDbrDetailViews(), getCurrentUser(), dbr);

        BigDecimal totalMonthDebtBorrowerStart = BigDecimal.ZERO;
        BigDecimal totalMonthDebtBorrowerFinal = BigDecimal.ZERO;

        //**NCB Borrower totalDebtForCalculate
        List<NCBDetailView> ncbDetailViews = getNCBForDBR(workCase.getId() , dbrView.getDbrMarketableFlag());
        for(NCBDetailView ncbDetailView : Util.safetyList(ncbDetailViews)){
            if(ncbDetailView.getRefinanceFlag() == RadioValue.NO.value()){
                totalMonthDebtBorrowerStart = Util.add(totalMonthDebtBorrowerStart, ncbDetailView.getDebtForCalculate());
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
                totalMonthDebtRelatedWc = Util.add(totalMonthDebtRelatedWc, dbrDetail.getLimit());
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

        BigDecimal finalDBR;
        ProposeLine proposeLine = newCreditFacilityDAO.findByWorkCaseId(workCase.getId());
        BigDecimal totalProposeLoanDBR = BigDecimal.ZERO;
        if(!Util.isNull(proposeLine)) {
            totalProposeLoanDBR = proposeLine.getTotalProposeLoanDBR();
        }
        finalDBR = calculateFinalDBR(totalMonthDebtBorrowerFinal, totalMonthDebtRelated, netMonthlyIncome, totalProposeLoanDBR);

        // update dbr
        dbr.setNetMonthlyIncome(netMonthlyIncome);
        dbr.setCurrentDBR(currentDBR);
        dbr.setDbrBeforeRequest(dbrBeforeRequest);
        dbr.setDbrDetails(dbrDetails);
        dbr.setFinalDBR(finalDBR);
        dbr.setTotalMonthDebtBorrowerStart(totalMonthDebtBorrowerStart);
        dbr.setTotalMonthDebtBorrowerFinal(totalMonthDebtBorrowerFinal);
        dbr.setTotalMonthDebtRelatedWc(totalMonthDebtRelatedWc);
        dbr.setTotalMonthDebtRelated(totalMonthDebtRelated);
        log.debug("calculateDBR complete");
        return dbr;
    }

    private BigDecimal calculateFinalDBR(BigDecimal totalMonthDebtBorrowerFinal, BigDecimal totalMonthDebtRelated,BigDecimal netMonthlyIncome, BigDecimal totalProposeLoanDBR){
        return Util.multiply(Util.divide(Util.add(Util.add(totalMonthDebtBorrowerFinal, totalMonthDebtRelated),totalProposeLoanDBR), netMonthlyIncome), BigDecimal.valueOf(100));
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

            try {
                DBR dbr = calculateDBR(dbrView, workCase);
                dbrDAO.persist(dbr);
            } catch (Exception e) {
                log.debug("Exception updateValueOfDBR", e);
            }
        log.debug("complete updateValueOfDBR");
        }
        return ActionResult.SUCCESS;
    }

    private BigDecimal getMonthlyIncome(BankStatementSummary bankStatementSummary){
        BigDecimal monthlyIncome;
        int roleId = getCurrentUser().getRole().getId();
        if(roleId == RoleValue.UW.id()){
            if(bankStatementSummary.getGrdTotalIncomeNetUW() == null)
                monthlyIncome = bankStatementSummary.getGrdTotalIncomeNetBDM();
            else
                monthlyIncome = bankStatementSummary.getGrdTotalIncomeNetUW();

        }else {
            monthlyIncome = bankStatementSummary.getGrdTotalIncomeNetBDM();
        }
        return monthlyIncome;
    }

    public List<NCBDetailView> getNCBForDBR(long workCaseId, int marketableFlag){
        log.debug("getNCBForDBR workCaseId :: {}", workCaseId);
        List<NCBDetailView> ncbDetailViews = new ArrayList<NCBDetailView>();

        List<NCBDetail> ncbDetailList = ncbDetailDAO.getNCBForDBRList(workCaseId);
        StringBuilder accountName = new StringBuilder();
        for(NCBDetail ncbDetail : Util.safetyList(ncbDetailList)){
            Customer customer = ncbDetail.getNcb().getCustomer();
            AccountType accountType = ncbDetail.getAccountType();
            AccountStatus accountStatus = ncbDetail.getAccountStatus();
            if(accountStatus == null || accountType == null) continue;

            if(accountStatus.getDbrFlag() == 1 && accountType.getDbrFlag() == 1){
                NCBDetailView ncbDetailView = new NCBDetailView();
                ncbDetailView.setId(ncbDetail.getId());
                ncbDetailView.setLimit(ncbDetail.getLimit());
                ncbDetailView.setInstallment(ncbDetail.getInstallment());
                BigDecimal debtForCalculate = BigDecimal.ZERO;

                BigDecimal dbrInterest = baseRateControl.getMRRValue();
                if(marketableFlag != 2) {
                    UserSysParameterView userSysParameterView = userSysParameterControl.getSysParameterValue("FIX_RATE");
                    int dbrInt = 0;
                    if(!Util.isNull(userSysParameterView)){
                        dbrInt = Util.parseInt(userSysParameterView.getValue(), 0);
                    }
                    dbrInterest = Util.add(baseRateControl.getMRRValue(),BigDecimal.valueOf(dbrInt));
                }

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
}
