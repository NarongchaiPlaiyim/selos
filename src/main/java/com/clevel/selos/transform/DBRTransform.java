package com.clevel.selos.transform;

import com.clevel.selos.dao.working.DBRDAO;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.working.DBR;
import com.clevel.selos.model.db.working.WorkCase;
import com.clevel.selos.model.view.DBRDetailView;
import com.clevel.selos.model.view.DBRView;
import com.clevel.selos.model.view.ProposeCreditInfoDetailView;
import com.clevel.selos.util.Util;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class DBRTransform extends Transform {

    @Inject
    DBRDetailTransform dbrDetailTransform;
    @Inject
    DBRDAO dbrdao;


    public DBRView transformToView(DBR dbr) {
        DBRView dbrView = new DBRView();

        if(dbr != null && dbr.getId() != 0){
            dbrView.setId(dbr.getId());
            dbrView.setDbrInterest(dbr.getDbrInterest());
            dbrView.setCurrentDBR(dbr.getCurrentDBR());
            dbrView.setDbrBeforeRequest(dbr.getDbrBeforeRequest());
            dbrView.setIncomeFactor(dbr.getIncomeFactor());
            dbrView.setMonthlyIncome(dbr.getMonthlyIncome());
            dbrView.setMonthlyIncomePerMonth(dbr.getMonthlyIncomePerMonth());
            dbrView.setMonthlyIncomeAdjust(dbr.getMonthlyIncomeAdjust());
            dbrView.setNetMonthlyIncome(dbr.getNetMonthlyIncome());
            dbrView.setTotalMonthDebtBorrowerFinal(dbr.getTotalMonthDebtBorrowerFinal());
            dbrView.setTotalMonthDebtBorrowerStart(dbr.getTotalMonthDebtBorrowerStart());
            dbrView.setTotalMonthDebtRelatedWc(dbr.getTotalMonthDebtRelatedWc());

            List<DBRDetailView> dbrDetailViewList = dbrDetailTransform.getDbrDetailViews(dbr.getDbrDetails());
            Collections.sort(dbrDetailViewList, new Comparator<DBRDetailView>() {
                public int compare(DBRDetailView o1, DBRDetailView o2) {
                    if (Util.isZero(o1.getId()) || Util.isZero(o2.getId()))
                        return 0;
                    return BigDecimal.valueOf(o1.getId()).compareTo(BigDecimal.valueOf(o2.getId()));
                }
            });
            dbrView.setDbrDetailViews(dbrDetailViewList);

            dbrView.setModifyBy(dbr.getModifyBy() == null ? "": dbr.getModifyBy().getId());
            dbrView.setModifyDate(dbr.getModifyDate());
            dbrView.setDbrMarketableFlag(dbr.getMarketableFlag());
        }

        return dbrView;
    }

    public DBR transformToModel(DBRView dbrView, WorkCase workCase, User user) {
        DBR dbr = new DBR();

        if(dbrView.getId() != 0){
            dbr = dbrdao.findById(dbrView.getId());
        } else {
            dbr.setCreateDate(new Date());
            dbr.setCreateBy(user);
        }

        dbr.setModifyBy(user);
        dbr.setModifyDate(new Date());
        dbr.setWorkCase(workCase);
        dbr.setMonthlyIncomeAdjust(dbrView.getMonthlyIncomeAdjust());
        dbr.setMonthlyIncome(dbrView.getMonthlyIncome());
        dbr.setCurrentDBR(dbrView.getCurrentDBR());
        dbr.setDbrBeforeRequest(dbrView.getDbrBeforeRequest());
        dbr.setMonthlyIncomePerMonth(dbrView.getMonthlyIncomePerMonth());
        dbr.setDbrInterest(dbrView.getDbrInterest());
        dbr.setNetMonthlyIncome(dbrView.getNetMonthlyIncome());
        dbr.setIncomeFactor(dbrView.getIncomeFactor());
        dbr.setTotalMonthDebtBorrowerStart(dbrView.getTotalMonthDebtBorrowerStart());
        dbr.setTotalMonthDebtBorrowerFinal(dbrView.getTotalMonthDebtBorrowerFinal());
        dbr.setTotalMonthDebtRelatedWc(dbrView.getTotalMonthDebtRelatedWc());
        dbr.setMarketableFlag(dbrView.getDbrMarketableFlag());

        return dbr;
    }
}
