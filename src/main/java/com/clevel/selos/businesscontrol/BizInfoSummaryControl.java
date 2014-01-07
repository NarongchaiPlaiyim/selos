package com.clevel.selos.businesscontrol;

import com.clevel.selos.dao.working.BankStatementSummaryDAO;
import com.clevel.selos.dao.working.BizInfoDetailDAO;
import com.clevel.selos.dao.working.BizInfoSummaryDAO;
import com.clevel.selos.dao.working.WorkCaseDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.working.BankStatementSummary;
import com.clevel.selos.model.db.working.BizInfoDetail;
import com.clevel.selos.model.db.working.BizInfoSummary;
import com.clevel.selos.model.db.working.WorkCase;
import com.clevel.selos.model.view.BankStmtSummaryView;
import com.clevel.selos.model.view.BizInfoDetailView;
import com.clevel.selos.model.view.BizInfoSummaryView;
import com.clevel.selos.transform.BizInfoDetailTransform;
import com.clevel.selos.transform.BizInfoSummaryTransform;
import org.joda.time.DateTime;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class BizInfoSummaryControl extends BusinessControl {
	@Inject
    @SELOS
    private Logger log;

    @Inject
    BizInfoSummaryDAO bizInfoSummaryDAO;
    @Inject
    BizInfoDetailDAO bizInfoDetailDAO;
    @Inject
    WorkCaseDAO workCaseDAO;
    @Inject
    BankStatementSummaryDAO bankStmtSummaryDAO;

    @Inject
    BizInfoDetailTransform bizInfoDetailTransform;
    @Inject
    BizInfoSummaryTransform bizInfoSummaryTransform;

    @Inject
    public BizInfoSummaryControl(){

    }

    public void onSaveBizSummaryToDB(BizInfoSummaryView bizInfoSummaryView, long workCaseId) {

        log.info("onSaveBizSummaryToDB begin");
        BizInfoSummary bizInfoSummary;

        log.info("find workCase begin");
        WorkCase workCase = workCaseDAO.findById(workCaseId);
        log.info("find workCase end workCase is " + workCase.toString());

        User user = getCurrentUser();
        if (bizInfoSummaryView.getId() == 0) {
            bizInfoSummaryView.setCreateBy(user);
            bizInfoSummaryView.setCreateDate(DateTime.now().toDate());
        }
        bizInfoSummaryView.setModifyBy(user);

        bizInfoSummary = bizInfoSummaryTransform.transformToModel(bizInfoSummaryView);
        bizInfoSummary.setWorkCase(workCase);

        log.info("bizInfoSummaryDAO.persist begin " + bizInfoSummary.toString());

        bizInfoSummaryDAO.persist(bizInfoSummary);
        log.info("onSaveBizSummaryToDB end");
    }


    public BizInfoSummaryView onGetBizInfoSummaryByWorkCase(long workCaseId) {
        log.info("onGetBizInfoSummaryByWorkCase workCaseId is " + workCaseId);

        BizInfoSummary bizInfoSummary;
        BizInfoSummaryView bizInfoSummaryView;

        WorkCase workCase = workCaseDAO.findById(workCaseId);

        //log.info("workCase after findById " + workCase);

        bizInfoSummary = bizInfoSummaryDAO.onSearchByWorkCase(workCase);

        if (bizInfoSummary != null) {
            bizInfoSummaryView = bizInfoSummaryTransform.transformToView(bizInfoSummary);
        } else {
            bizInfoSummaryView = null;
        }
        return bizInfoSummaryView;
    }

    public List<BizInfoDetailView> onGetBizInfoDetailByBizInfoSummary(long bizInfoSummaryID) {
        log.info("onGetBizInfoSummaryByWorkCase ");

        List<BizInfoDetail> bizInfoDetailList;
        List<BizInfoDetailView> bizInfoDetailViewList;
        BizInfoDetailView bizInfoDetailViewTemp;
        BizInfoDetail bizInfoDetailTemp;
        bizInfoDetailViewList = new ArrayList<BizInfoDetailView>();

        BizInfoSummary bizInfoSummary = bizInfoSummaryDAO.findById(bizInfoSummaryID);

        log.info("workCase after findById " + bizInfoSummary);

        bizInfoDetailList = bizInfoDetailDAO.findByBizInfoSummaryId(bizInfoSummary);

        if (bizInfoDetailList.size() != 0) {
            for (int i = 0; i < bizInfoDetailList.size(); i++) {
                bizInfoDetailTemp = bizInfoDetailList.get(i);
                bizInfoDetailViewTemp = bizInfoDetailTransform.transformToView(bizInfoDetailTemp);
                bizInfoDetailViewList.add(bizInfoDetailViewTemp);
            }
        } else {
            //bizInfoSummaryView = null;
        }
        return bizInfoDetailViewList;
    }

    public BankStmtSummaryView getBankStmtSummary(long workCaseId){
        BankStmtSummaryView bankStmtSummaryView = new BankStmtSummaryView();
        BankStatementSummary bankStmtSummary = bankStmtSummaryDAO.findByWorkCaseId(workCaseId);

        if(bankStmtSummary != null){
            bankStmtSummaryView.setGrdTotalIncomeNetBDM(bankStmtSummary.getGrdTotalIncomeNetBDM());
            bankStmtSummaryView.setGrdTotalIncomeNetUW(bankStmtSummary.getGrdTotalIncomeNetUW());
        } else {
            bankStmtSummaryView.setGrdTotalIncomeNetBDM(BigDecimal.ZERO);
            bankStmtSummaryView.setGrdTotalIncomeNetUW(BigDecimal.ZERO);
        }

        return bankStmtSummaryView;
    }

}
