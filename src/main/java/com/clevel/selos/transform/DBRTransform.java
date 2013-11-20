package com.clevel.selos.transform;

import com.clevel.selos.dao.working.DBRDAO;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.working.DBR;
import com.clevel.selos.model.db.working.WorkCase;
import com.clevel.selos.model.view.DBRView;

import javax.inject.Inject;
import java.util.Date;

public class DBRTransform extends Transform {

    @Inject
    DBRDetailTransform dbrDetailTransform;
    @Inject
    DBRDAO dbrdao;


    public DBRView getDBRView(DBR dbr) {
        DBRView dbrView = new DBRView();
        if (dbr == null) {
            return dbrView;
        }
        dbrView.setId(dbr.getId());
        dbrView.setDbrInterest(dbr.getDbrInterest());
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

    public DBR getDBRInfoModel(DBRView dbrView, WorkCase workCase, User user) {
        DBR dbr = new DBR();
        if (dbrView == null) {
            return dbr;
        }
        Date now = new Date();
        if (dbrView.getId() == 0) {
            dbr.setCreateBy(user);
            dbr.setCreateDate(now);
        } else {
            dbr = dbrdao.findById(dbrView.getId());
            if (dbr == null) {
                dbr.setCreateBy(user);
                dbr.setCreateDate(now);
            } else {
                dbr.setId(dbrView.getId());
            }
        }
        dbr.setMonthlyIncomeAdjust(dbrView.getMonthlyIncomeAdjust());
        dbr.setMonthlyIncome(dbrView.getMonthlyIncome());
        dbr.setCurrentDBR(dbrView.getCurrentDBR());
        dbr.setDbrBeforeRequest(dbrView.getDbrBeforeRequest());
        dbr.setMonthlyIncomePerMonth(dbrView.getMonthlyIncomePerMonth());
        dbr.setDbrInterest(dbrView.getDbrInterest());
        dbr.setNetMonthlyIncome(dbrView.getNetMonthlyIncome());
        dbr.setWorkCase(dbr.getWorkCase());
        dbr.setModifyBy(user);
        dbr.setModifyDate(now);
        dbr.setWorkCase(workCase);
        dbr.setIncomeFactor(dbrView.getIncomeFactor());
        return dbr;
    }
}
