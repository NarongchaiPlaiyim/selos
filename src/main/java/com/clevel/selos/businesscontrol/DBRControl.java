package com.clevel.selos.businesscontrol;

import com.clevel.selos.dao.master.UserDAO;
import com.clevel.selos.dao.working.BizInfoSummaryDAO;
import com.clevel.selos.dao.working.DBRDAO;
import com.clevel.selos.dao.working.DBRDetailDAO;
import com.clevel.selos.dao.working.WorkCaseDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.RoleUser;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.working.BizInfoSummary;
import com.clevel.selos.model.db.working.DBR;
import com.clevel.selos.model.db.working.DBRDetail;
import com.clevel.selos.model.db.working.WorkCase;
import com.clevel.selos.model.view.DBRDetailView;
import com.clevel.selos.model.view.DBRView;
import com.clevel.selos.transform.DBRDetailTransform;
import com.clevel.selos.transform.DBRTransform;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class DBRControl extends BusinessControl {
    @Inject
    @SELOS
    private Logger log;

    @Inject
    private UserDAO userDAO;
    @Inject
    DBRDAO dbrdao;
    @Inject
    DBRDetailDAO dbrDetailDAO;
    @Inject
    WorkCaseDAO workCaseDAO;
    @Inject
    BizInfoSummaryDAO bizInfoSummaryDAO;

    @Inject
    DBRTransform dbrTransform;
    @Inject
    DBRDetailTransform dbrDetailTransform;

    @Inject
    public DBRControl() {

    }

    public void saveDBRInfo(DBRView dbrView) {
        WorkCase workCase = workCaseDAO.findById(dbrView.getWorkCaseId());
        User user = userDAO.findById(dbrView.getUserId());

        DBR dbr = calculateDBR(dbrView, user, workCase);

        DBR returnDBRInfo = dbrdao.persist(dbr);
        List<DBRDetailView> dbrDetailViews = dbrView.getDbrDetailViews();
        List<DBRDetail> newDbrDetails = new ArrayList<DBRDetail>();  // new record
        List<DBRDetail> oldDbrDetails = dbrDetailDAO.createCriteria().add(Restrictions.eq("dbr", dbr)).list();  // old record
        if (dbrDetailViews != null && !dbrDetailViews.isEmpty()) {
            newDbrDetails = dbrDetailTransform.getDbrDetailModels(dbrDetailViews, user, returnDBRInfo);
            if (oldDbrDetails.size() == 0) {
                dbrDetailDAO.persist(newDbrDetails); //ADD New OR Update
            } else {
                //delete old without new record
                for (DBRDetail oldDbrDetail : oldDbrDetails) {
                    boolean isDelete = true;
                    for (DBRDetail newDbrDetail : newDbrDetails) {
                        if (oldDbrDetail.getId() == newDbrDetail.getId()) {
                            isDelete = false;
                        }
                    }
                    if (isDelete) {
                        dbrDetailDAO.delete(oldDbrDetail);
                    }
                }
                //Add new record
                dbrDetailDAO.persist(newDbrDetails);
            }
        } else {  //Delete all record from DBR
            if (oldDbrDetails != null && !oldDbrDetails.isEmpty()) {
                dbrDetailDAO.delete(oldDbrDetails);
            }
        }
    }

    public DBRView getDBRByWorkCase(long workCaseId) {
        WorkCase workCase = workCaseDAO.findById(workCaseId);
        DBR dbr = (DBR) dbrdao.createCriteria().add(Restrictions.eq("workCase", workCase)).uniqueResult();
        BizInfoSummary bizInfoSummary = bizInfoSummaryDAO.onSearchByWorkCase(workCase);
        dbr.setIncomeFactor(bizInfoSummary.getWeightIncomeFactor());
        BigDecimal dbrInterest = BigDecimal.valueOf(7).add(BigDecimal.valueOf(3));
        dbr.setDbrInterest(dbrInterest);
        DBRView dbrView = dbrTransform.getDBRView(dbr);
        return dbrView;
    }

    private DBR calculateDBR(DBRView dbrView, User user, WorkCase workCase){
        DBR result = new DBR();
        int roleId = user.getRole().getId();
        DBR dbr = dbrTransform.getDBRInfoModel(dbrView, workCase, user);

        BigDecimal dbrBeforeRequest = BigDecimal.ZERO;
        BigDecimal netMonthlyIncome = BigDecimal.ZERO;
        BigDecimal monthlyIncomePerMonth = BigDecimal.ZERO;
        BigDecimal currentDBR = BigDecimal.ZERO;

        //DbrIf

        netMonthlyIncome = dbrView.getTotalMonthDebtBorrower().add(dbrView.getTotalMonthDebtRelated());

        dbrBeforeRequest = currentDBR.divide(netMonthlyIncome, 20, RoundingMode.HALF_UP);


        if(roleId == RoleUser.UW.getValue()){
            //netMinthlyIncome
            monthlyIncomePerMonth = dbrView.getMonthlyIncomeAdjust().multiply(dbrView.getIncomeFactor());
            if(monthlyIncomePerMonth.compareTo(BigDecimal.ZERO) == 0){
                monthlyIncomePerMonth = dbrView.getMonthlyIncomeAdjust().multiply(dbrView.getIncomeFactor());
            }
            monthlyIncomePerMonth = monthlyIncomePerMonth.add(dbrView.getMonthlyIncomePerMonth());

        }else if(roleId == RoleUser.BDM.getValue()){
            //netMinthlyIncome
            monthlyIncomePerMonth = dbrView.getMonthlyIncomeAdjust().multiply(dbrView.getIncomeFactor());
            monthlyIncomePerMonth = monthlyIncomePerMonth.add(dbrView.getMonthlyIncomePerMonth());
        }
        log.debug("calculateDBR complete");
        return result;
    }

}
