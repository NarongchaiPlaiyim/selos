package com.clevel.selos.transform;

import com.clevel.selos.dao.working.DBRDetailDAO;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.working.DBR;
import com.clevel.selos.model.db.working.DBRDetail;
import com.clevel.selos.model.view.DBRDetailView;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DBRDetailTransform extends Transform {

    @Inject
    LoanAccountTypeTransform loanAccountTypeTransform;
    @Inject
    DBRDetailDAO dbrDetailDAO;

    public DBRDetailView getDBRDetailView(DBRDetail dbrDetail) {
        DBRDetailView dbrDetailView = new DBRDetailView();
        if (dbrDetail == null) {
            return dbrDetailView;
        }
        dbrDetailView.setId(dbrDetail.getId());
        dbrDetailView.setAccountName(dbrDetail.getAccountName());
        dbrDetailView.setDebtForCalculate(dbrDetail.getDebtForCalculate());
        dbrDetailView.setInstallment(dbrDetail.getInstallment());
        dbrDetailView.setLimit(dbrDetail.getLimit());
        dbrDetailView.setLoanAccountTypeView(loanAccountTypeTransform.getLoanAccountTypeView(dbrDetail.getLoanType()));
        return dbrDetailView;
    }

    public List<DBRDetailView> getDbrDetailViews(List<DBRDetail> dbrDetails) {
        List<DBRDetailView> dbrDetailViews = new ArrayList<DBRDetailView>();
        if (dbrDetails == null || dbrDetails.isEmpty()) {
            return dbrDetailViews;
        }
        for (DBRDetail dbrDetail : dbrDetails) {
            dbrDetailViews.add(getDBRDetailView(dbrDetail));
        }
        return dbrDetailViews;
    }

    public DBRDetail getDBRDetailModel(DBRDetailView dbrDetailView, User user, DBR dbr) {
        DBRDetail dbrDetail = new DBRDetail();
        if (dbrDetailView == null) {
            return dbrDetail;
        }
        Date now = new Date();
        if (dbrDetailView.getId() == 0) {
            dbrDetail.setCreateBy(user);
            dbrDetail.setCreateDate(now);
        } else {
            dbrDetail = dbrDetailDAO.findById(dbrDetailView.getId());
            if (dbrDetail == null) {
                dbrDetail.setCreateBy(user);
                dbrDetail.setCreateDate(now);
            } else {
                dbrDetail.setId(dbrDetailView.getId());
            }
        }
        dbrDetail.setLimit(dbrDetailView.getLimit());
        dbrDetail.setAccountName(dbrDetailView.getAccountName().trim());
        dbrDetail.setDebtForCalculate(dbrDetailView.getDebtForCalculate());
        dbrDetail.setInstallment(dbrDetailView.getInstallment());
        dbrDetail.setLoanType(loanAccountTypeTransform.getLoanType(dbrDetailView.getLoanAccountTypeView()));
        dbrDetail.setModifyDate(now);
        dbrDetail.setModifyBy(user);
        dbrDetail.setDbr(dbr);
        return dbrDetail;
    }

    public List<DBRDetail> getDbrDetailModels(List<DBRDetailView> dbrDetailViews, User user, DBR dbr) {
        List<DBRDetail> dbrDetails = new ArrayList<DBRDetail>();
        if (dbrDetailViews == null && dbrDetailViews.isEmpty()) {
            return dbrDetails;
        }
        for (DBRDetailView dbrDetailView : dbrDetailViews) {
            dbrDetails.add(getDBRDetailModel(dbrDetailView, user, dbr));
        }
        return dbrDetails;
    }


}
