package com.clevel.selos.businesscontrol;

import com.clevel.selos.integration.RMInterface;
import com.clevel.selos.integration.corebanking.model.customeraccount.CustomerAccountResult;
import com.clevel.selos.model.ActionResult;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.working.BankStatementSummary;
import com.clevel.selos.model.view.CustomerInfoView;
import com.clevel.selos.model.view.PrescreenView;
import com.clevel.selos.util.DateTimeUtil;
import com.clevel.selos.util.Util;
import org.joda.time.DateTime;

import javax.inject.Inject;
import java.util.Calendar;
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
                if(customerAccountResult.getActionResult().equals(ActionResult.SUCCESS)){

                }
            }
        }
        return null;
    }

    /**
     * To get starting date of retrieving bank account
     * If seasonal flag is 'Yes' then retrieves 12 months and 'No' then retrieves 6 months
     * If expected submission date less than 15 get current month -2 (T-2), If more than 15 get current month -1 (T-1)
     * Ex. if expectedSubmissionDate: 15/10/2013 -> (T-1) -> start previous month at 'Sep', 'Aug', 'Jul', 'Jun', 'May', 'Apr'
     * @param seasonalFlag
     * @param expectedSubmissionDate
     * @return
     */
    public Date getStartBankStmtDate(boolean seasonalFlag, Date expectedSubmissionDate) {
        if (expectedSubmissionDate != null) {
            int days = Util.getDayOfDate(expectedSubmissionDate);
            int retrieveMonth = days < 15 ? 2 : 1;
            retrieveMonth += seasonalFlag ? 11 : 5;
            return DateTimeUtil.getOnlyDatePlusMonth(expectedSubmissionDate, -retrieveMonth);
        }
        return null;
    }
}
