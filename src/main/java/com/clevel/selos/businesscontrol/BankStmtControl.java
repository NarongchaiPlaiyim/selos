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
import com.clevel.selos.util.Util;
import org.joda.time.DateTime;

import javax.inject.Inject;
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
}
