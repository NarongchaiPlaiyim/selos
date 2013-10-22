package com.clevel.selos.transform;

import com.clevel.selos.model.db.working.DBR;
import com.clevel.selos.model.view.DBRView;
import org.slf4j.Logger;

import javax.inject.Inject;

public class DBRTransform extends Transform {
    @Inject
    Logger log;
    @Inject
    DBRDetailTransform dbrDetailTransform;


    public DBRView getDBRView(DBR dbr){
        DBRView dbrView = new DBRView();
        if(dbr == null){
         return dbrView;
        }
        dbrView.setId(dbr.getId());
        dbrView.setCurrentDBR(dbr.getCurrentDBR());
        dbrView.setDbrBeforeRequest(dbr.getDbrBeforeRequest());
        dbrView.setIncomeFactor(dbr.getIncomeFactor());
        dbrView.setMonthlyIncome(dbr.getMonthlyIncome());
        dbrView.setMonthlyIncomePerMonth(dbr.getMonthlyIncomePerMonth());
        dbrView.setMonthlyIncomeAdjust(dbr.getMonthlyIncomeAdjust());
        dbrView.setNetMonthlyIncome(dbr.getNetMonthlyIncome());
        dbrView.setDbrDetailViews(dbrDetailTransform.getDbrDetailViews(dbr.getDbrDetails()));
        return dbrView;
    }
}
