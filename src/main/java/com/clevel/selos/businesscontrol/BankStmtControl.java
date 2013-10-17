package com.clevel.selos.businesscontrol;

import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.view.PrescreenView;
import com.clevel.selos.util.DateTimeUtil;
import com.clevel.selos.util.Util;
import org.joda.time.DateTime;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class BankStmtControl extends BusinessControl{

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

    public Date getPreviousStartDate(boolean seasonalFlag, Date expectedSubmissionDate) {
        if (expectedSubmissionDate != null) {
            int days = Util.getDayOfDate(expectedSubmissionDate);
            //If seasonal flag is 'Yes' then retrieves 12 months and 'No' then retrieves 6 months
            //If expected submission date less than 15 get current month -2 (T-2), If more than 15 get current month -1 (T-1)
            //Ex. if expectedSubmissionDate: 15/10/2013 -> (T-1) -> start previous month at 'Sep', 'Aug', 'Jul', 'Jun', 'May', 'Apr'
            int retrieveMonth = days < 15 ? 2 : 1;
                retrieveMonth += seasonalFlag ? 11 : 5;
            return DateTimeUtil.getOnlyDatePlusMonth(expectedSubmissionDate, -retrieveMonth);
        }
        return null;
    }
}
