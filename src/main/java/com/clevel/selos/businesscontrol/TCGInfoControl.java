package com.clevel.selos.businesscontrol;

import com.clevel.selos.dao.working.TCGDAO;
import com.clevel.selos.dao.working.TCGDetailDAO;
import com.clevel.selos.dao.working.WorkCaseDAO;
import com.clevel.selos.model.db.working.TCG;
import com.clevel.selos.model.db.working.TCGDetail;
import com.clevel.selos.model.db.working.WorkCase;
import com.clevel.selos.model.view.TCGDetailView;
import com.clevel.selos.model.view.TCGView;
import com.clevel.selos.transform.TCGDetailTransform;
import com.clevel.selos.transform.TCGTransform;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: SUKANDA CHITSUP
 * Date: 26/9/2556
 * Time: 16:01 น.
 * To change this template use File | Settings | File Templates.
 */


@Stateless
public class TCGInfoControl extends BusinessControl {
    @Inject
    Logger log;
    @Inject
    TCGDetailTransform tcgDetailTransform;
    @Inject
    TCGTransform tcgTransform;
    @Inject
    TCGDAO tcgDAO;
    @Inject
    TCGDetailDAO tcgDetailDAO;
    @Inject
    WorkCaseDAO workCaseDAO;

    @Inject
    public void TCGInfoControl() {

    }

    public void onSaveTCGToDB(TCGView tcgView, List<TCGDetailView> tcgDetailViewList, Long workCaseId) {
        log.info("onSaveTCGToDB begin");
        log.info("workCaseId {} ", workCaseId);
        log.info("tcgView  {} ", tcgView.toString());
        WorkCase workCase = workCaseDAO.findById(workCaseId);
        TCG tcg = tcgTransform.transformTCGViewToModel(tcgView, workCase);
        log.info("transform comeback {} ", tcg.toString());
        tcgDAO.persist(tcg);
        log.info("persist tcg");
        List<TCGDetail> tcgDetailList = tcgDetailTransform.transformTCGDetailViewToModel(tcgDetailViewList, tcg);
        tcgDetailDAO.persist(tcgDetailList);
    }

    public void onEditTCGToDB(TCGView tcgView, List<TCGDetailView> tcgDetailViewList, Long workCaseId) {

        log.info("onEditTCGToDB begin");
        log.info("workCaseId {} ", workCaseId);
        WorkCase workCase = workCaseDAO.findById(workCaseId);
        TCG tcg = tcgTransform.transformTCGViewToModel(tcgView, workCase);
        tcgDAO.persist(tcg);
        log.info("persist tcg");

        List<TCGDetail> tcgDetailListToDelete = tcgDetailDAO.findTCGDetailByTcgId(tcg.getId());
        log.info("tcgDetailListToDelete :: {}", tcgDetailListToDelete.size());
        tcgDetailDAO.delete(tcgDetailListToDelete);
        log.info("delete tcgDetailListToDelete");

        List<TCGDetail> tcgDetailList = tcgDetailTransform.transformTCGDetailViewToModel(tcgDetailViewList, tcg);
        tcgDetailDAO.persist(tcgDetailList);
        log.info("persist tcgDetailList");

    }

    public TCGView getTcgView(long workCaseId) {
        log.info("getTcgView :: workCaseId  :: {}", workCaseId);
        TCGView tcgView = null;

        WorkCase workCase = workCaseDAO.findById(workCaseId);
        log.info("getTcgView :: workCase AppNumber :: {}", workCase.getAppNumber());
        if (workCase != null) {
            TCG tcg = tcgDAO.findByWorkCase(workCase);

            if (tcg != null) {
                log.info("tcg :: {} ", tcg.getId());
                tcgView = tcgTransform.transformTCGToTcgView(tcg);
            }
        }

        return tcgView;
    }

    public List<TCGDetailView> getTcgDetailListView(TCGView tcgView) {
        log.info("getTcgDetailListView :: tcgId  :: {}", tcgView.getId());
        List<TCGDetailView> tcgDetailViewList = null;

        List<TCGDetail> TCGDetailList = tcgDetailDAO.findTCGDetailByTcgId(tcgView.getId());

        if (TCGDetailList.size() > 0) {
            tcgDetailViewList = tcgDetailTransform.transformTCGDetailModelToView(TCGDetailList);
        }

        return tcgDetailViewList;
    }

}
