package com.clevel.selos.businesscontrol;

import com.clevel.selos.integration.DWHInterface;
import com.clevel.selos.integration.RMInterface;
import com.clevel.selos.integration.corebanking.model.customeraccount.CustomerAccountListModel;
import com.clevel.selos.integration.corebanking.model.customeraccount.CustomerAccountResult;
import com.clevel.selos.integration.dwh.bankstatement.model.DWHBankStatement;
import com.clevel.selos.integration.dwh.bankstatement.model.DWHBankStatementResult;
import com.clevel.selos.model.ActionResult;
import com.clevel.selos.model.db.working.BankStatementSummary;
import com.clevel.selos.model.view.ActionStatusView;
import com.clevel.selos.model.view.BankStmtSummaryView;
import com.clevel.selos.model.view.BankStmtView;
import com.clevel.selos.model.view.CustomerInfoView;
import com.clevel.selos.transform.ActionStatusTransform;
import com.clevel.selos.util.DateTimeUtil;
import com.clevel.selos.util.Util;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BankStmtControl extends BusinessControl{

    @Inject
    private RMInterface rmInterface;

    @Inject
    DWHInterface dwhInterface;

    @Inject
    ActionStatusTransform actionStatusTransform;

    public BankStatementSummary retreiveBankStmtInterface(List<CustomerInfoView> customerInfoViewList, BankStmtSummaryView bankStmtSummaryView){
        Date startBankStmtDate = getStartBankStmtDate(bankStmtSummaryView.getExpectedSubmitDate());
        int numberOfMonthBankStmt = getRetrieveMonthBankStmt(bankStmtSummaryView.getSeasonal());

        List<ActionStatusView> actionStatusViewList = new ArrayList<ActionStatusView>();
        for(CustomerInfoView customerInfoView : customerInfoViewList){
            if(!Util.isEmpty(customerInfoView.getTmbCustomerId())){
                CustomerAccountResult customerAccountResult = rmInterface.getCustomerAccountInfo(getCurrentUserID(), customerInfoView.getTmbCustomerId());
                if(customerAccountResult.getActionResult().equals(ActionResult.SUCCESS)){
                    List<CustomerAccountListModel> accountListModelList = customerAccountResult.getAccountListModels();
                    for(CustomerAccountListModel customerAccountListModel : accountListModelList){
                        DWHBankStatementResult bankStatementResult = dwhInterface.getBankStatementData(getCurrentUserID(), customerAccountListModel.getAccountNo(), startBankStmtDate, numberOfMonthBankStmt);

                        if(bankStatementResult.getActionResult().equals(ActionResult.SUCCESS)){
                            List<DWHBankStatement> bankStatementList = bankStatementResult.getBankStatementList();
                            BankStmtView bankStmtView = null;
                            for(DWHBankStatement dwhBankStatement : bankStatementList){


                            }
                        } else {
                            actionStatusViewList.add(actionStatusTransform.getActionStatusView(bankStatementResult.getActionResult(), bankStatementResult.getReason()));
                        }

                    }
                } else {
                    actionStatusViewList.add(actionStatusTransform.getActionStatusView(customerAccountResult.getActionResult(), customerAccountResult.getReason()));
                }
            }
        }
        return null;
    }

    public BankStatementSummary getBankStmt(){

        return null;
    }

    /**
     * To get starting date of retrieving bank account
     * If expected submission date less than 15 get current month -2 (T-2), If more than 15 get current month -1 (T-1)
     * Ex. expectedSubmissionDate: 15/10/2013 -> (T-1) -> start previous month at 'September'
     * @param expectedSubmissionDate
     * @return Start previous date by bank account condition
     */
    public Date getStartBankStmtDate(Date expectedSubmissionDate) {
        if (expectedSubmissionDate != null) {
            int days = Util.getDayOfDate(expectedSubmissionDate);
            int retrieveMonth = days < 15 ? 2 : 1;
            return DateTimeUtil.getOnlyDatePlusMonth(expectedSubmissionDate, -retrieveMonth);
        }
        return null;
    }

    public int getRetrieveMonthBankStmt(int seasonalFlag) {
        return seasonalFlag == 1 ? 12 : 6;
    }
}
