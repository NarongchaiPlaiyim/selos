package com.clevel.selos.businesscontrol;

import com.clevel.selos.integration.RMInterface;
import com.clevel.selos.integration.corebanking.model.customeraccount.CustomerAccountResult;
import com.clevel.selos.model.ActionResult;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.working.BankStatement;
import com.clevel.selos.model.db.working.BankStatementSummary;
import com.clevel.selos.model.view.CustomerInfoView;
import com.clevel.selos.model.view.PrescreenView;
import com.clevel.selos.util.Util;
import org.joda.time.DateTime;

import javax.inject.Inject;
import java.util.Date;
import java.util.List;

public class BankStmtControl extends BusinessControl{

    @Inject
    private RMInterface rmInterface;

    // *** Function for DWH (BankStatement) *** //
    public void getBankStatementFromDWH(PrescreenView prescreenView, User user) throws Exception{
        Date expectedSubmitDate = prescreenView.getExpectedSubmitDate();
        //TODO if expected submit date less than 15 get current month -2 if more than 15 get current month -1
        expectedSubmitDate = DateTime.now().toDate();
        int months = Util.getMonthOfDate(expectedSubmitDate);
        int days = Util.getDayOfDate(expectedSubmitDate);
        log.info("getBankStatementFromDWH ::: months : {}", months);
        log.info("getBankStatementFromDWH ::: days : {}", days);

        if(days < 15){
            // *** Get data from database *** //

        }else {
            // *** Get data from database *** //

        }

    }

    public BankStatementSummary getBankStatment(List<CustomerInfoView> customerInfoViewList){
        for(CustomerInfoView customerInfoView : customerInfoViewList){
            if(!Util.isEmpty(customerInfoView.getTmbCustomerId())){
                CustomerAccountResult customerAccountResult = rmInterface.getCustomerAccountInfo(getCurrentUserID(), customerInfoView.getTmbCustomerId());
                if(customerAccountResult.getActionResult().equals(ActionResult.SUCCEED)){

                }
            }
        }
        return null;
    }
}
