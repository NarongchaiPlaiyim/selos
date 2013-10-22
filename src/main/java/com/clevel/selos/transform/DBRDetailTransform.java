package com.clevel.selos.transform;

import com.clevel.selos.model.db.working.DBRDetail;
import com.clevel.selos.model.view.DBRDetailView;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class DBRDetailTransform extends Transform {

    @Inject
    Logger logger;

    @Inject
    LoanTypeTransform loanTypeTransform;

    public DBRDetailView getDBRDetailView(DBRDetail dbrDetail){
        DBRDetailView dbrDetailView = new DBRDetailView();
        if(dbrDetail == null){
            return dbrDetailView;
        }
        dbrDetailView.setId(dbrDetail.getId());
        dbrDetailView.setAccountName(dbrDetail.getAccountName());
        dbrDetailView.setDebtForCalculate(dbrDetail.getDebtForCalculate());
        dbrDetailView.setInstallment(dbrDetail.getInstallment());
        dbrDetailView.setLimit(dbrDetail.getLimit());
        dbrDetailView.setLoanTypeView(loanTypeTransform.getLoanTypeView(dbrDetail.getLoanType()));
        return dbrDetailView;
    }

    public List<DBRDetailView> getDbrDetailViews(List<DBRDetail> dbrDetails){
        List<DBRDetailView> dbrDetailViews = new ArrayList<DBRDetailView>();
        if(dbrDetails == null && dbrDetails.isEmpty()){
            return dbrDetailViews;
        }
        for(DBRDetail dbrDetail : dbrDetails){
           dbrDetailViews.add(getDBRDetailView(dbrDetail));
        }
        return dbrDetailViews;
    }
}
