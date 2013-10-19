package com.clevel.selos.businesscontrol;

import com.clevel.selos.integration.DWHInterface;
import com.clevel.selos.integration.RMInterface;
import com.clevel.selos.integration.corebanking.model.customeraccount.CustomerAccountListModel;
import com.clevel.selos.integration.corebanking.model.customeraccount.CustomerAccountResult;
import com.clevel.selos.integration.dwh.bankstatement.model.BankStatementResult;
import com.clevel.selos.model.ActionResult;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.working.BankStatement;
import com.clevel.selos.model.db.working.BankStatementSummary;
import com.clevel.selos.model.view.ActionStatusView;
import com.clevel.selos.model.view.CustomerInfoView;
import com.clevel.selos.model.view.PrescreenView;
import com.clevel.selos.util.DateTimeUtil;
import com.clevel.selos.util.Util;
import org.joda.time.DateTime;

import javax.inject.Inject;
import java.util.Calendar;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BankStmtControl extends BusinessControl{

    @Inject
    private RMInterface rmInterface;

    @Inject
    DWHInterface dwhInterface;

    public BankStatementSummary retreiveBankStmtInterface(List<CustomerInfoView> customerInfoViewList, BankStatementSummary bankStatementSummary){
        Date startBankStmtDate = new Date();
        int numberOfMonthBankStmt = 6;

        List<ActionStatusView> actionStatusViewList = new ArrayList<ActionStatusView>();
        for(CustomerInfoView customerInfoView : customerInfoViewList){
            if(!Util.isEmpty(customerInfoView.getTmbCustomerId())){
                CustomerAccountResult customerAccountResult = rmInterface.getCustomerAccountInfo(getCurrentUserID(), customerInfoView.getTmbCustomerId());
                if(customerAccountResult.getActionResult().equals(ActionResult.SUCCEED)){
                    List<CustomerAccountListModel> accountListModelList = customerAccountResult.getAccountListModels();
                    for(CustomerAccountListModel customerAccountListModel : accountListModelList){
                        BankStatementResult bankStatementResult = dwhInterface.getBankStatementData(getCurrentUserID(), customerAccountListModel.getAccountNo(), startBankStmtDate, numberOfMonthBankStmt);



                    }
                } else {
                    ActionStatusView actionStatusView = new ActionStatusView();
                    actionStatusView.setStatusCode(customerAccountResult.getActionResult());
                    actionStatusView.setStatusDesc(customerAccountResult.getReason());
                    actionStatusViewList.add(actionStatusView);
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
     * If seasonal flag is 'Yes' then retrieves 12 months and 'No' then retrieves 6 months
     * If expected submission date less than 15 get current month -2 (T-2), If more than 15 get current month -1 (T-1)
     * Ex. if expectedSubmissionDate: 15/10/2013 -> (T-1) -> start previous month at 'Sep', 'Aug', 'Jul', 'Jun', 'May', 'Apr'
     * @param expectedSubmissionDate
     * @return
     */
    public Date getStartBankStmtDate(Date expectedSubmissionDate) {
        if (expectedSubmissionDate != null) {
            int days = Util.getDayOfDate(expectedSubmissionDate);
            int retrieveMonth = days < 15 ? 2 : 1;
            return DateTimeUtil.getOnlyDatePlusMonth(expectedSubmissionDate, -retrieveMonth);
        }
        return null;
    }

    public int getRetrieveMonthBankStmt(boolean seasonalFlag) {
        return seasonalFlag ? 12 : 6;
    }
}
