package com.clevel.selos.businesscontrol;

import com.clevel.selos.dao.working.AppraisalContactDetailDAO;
import com.clevel.selos.dao.working.AppraisalDAO;
import com.clevel.selos.dao.working.AppraisalDetailDAO;
import com.clevel.selos.dao.working.WorkCaseDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.working.Appraisal;
import com.clevel.selos.model.db.working.AppraisalContactDetail;
import com.clevel.selos.model.db.working.AppraisalPurpose;
import com.clevel.selos.model.db.working.WorkCase;
import com.clevel.selos.model.view.AppraisalContactDetailView;
import com.clevel.selos.model.view.AppraisalDetailView;
import com.clevel.selos.model.view.AppraisalView;
import com.clevel.selos.transform.AppraisalContactDetailTransform;
import com.clevel.selos.transform.AppraisalDetailTransform;
import com.clevel.selos.transform.AppraisalTransform;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;
@Stateless
public class AppraisalRequestControl extends BusinessControl {
    @Inject
    @SELOS
    private Logger log;

    @Inject
    AppraisalDAO appraisalDAO;
    @Inject
    AppraisalContactDetailDAO appraisalContactDetailDAO;
    @Inject
    AppraisalDetailDAO appraisalDetailDAO;
    @Inject
    WorkCaseDAO workCaseDAO;

    @Inject
    AppraisalTransform appraisalTransform;
    @Inject
    AppraisalDetailTransform appraisalDetailTransform;
    @Inject
    AppraisalContactDetailTransform appraisalContactDetailTransform;

    @Inject
    public AppraisalRequestControl(){

    }
	
	public AppraisalView getAppraisalRequestByWorkCase(long workCaseId){
        log.info("getAppraisalByWorkCase ");

        Appraisal appraisal;
        AppraisalView appraisalView;
        List<AppraisalPurpose> appraisalDetailList;
        List<AppraisalDetailView> appraisalDetailViewList;
        List<AppraisalContactDetail> appraisalContactDetailList;
        List<AppraisalContactDetailView> appraisalContactDetailViewList;

        WorkCase workCase = workCaseDAO.findById(workCaseId);
        log.info("workCase after findById " + workCase );

        appraisal  = appraisalDAO.onSearchByWorkCase(workCase);


        if( appraisal != null){
            appraisalView = appraisalTransform.transformToView(appraisal);

            appraisalDetailList = appraisalDetailDAO.findByAppraisal(appraisal);
            if(appraisalDetailList.size()>0){
                appraisalDetailViewList = appraisalDetailTransform.transformToView(appraisalDetailList);
                appraisalView.setAppraisalDetailViewList(appraisalDetailViewList);
            }

            appraisalContactDetailList= appraisalContactDetailDAO.findByAppraisal(appraisal);
            if(appraisalContactDetailList.size()>0){
                appraisalContactDetailViewList = appraisalContactDetailTransform.transformToView(appraisalContactDetailList);
                appraisalView.setAppraisalContactDetailViewList(appraisalContactDetailViewList);
            }
        }else{
            appraisalView = null;
        }
        return appraisalView;
    }

    public void onSaveAppraisalRequest(final AppraisalView appraisalView,final long workCaseId, final User user){
        log.info("onSaveAppraisalRequest ");
        Appraisal appraisal;
        List<AppraisalDetailView> appraisalDetailViewList;
        List<AppraisalPurpose> appraisalDetailList;
        List<AppraisalContactDetailView> appraisalContactDetailViewList;
        List<AppraisalContactDetail> appraisalContactDetailList;

        WorkCase workCase = workCaseDAO.findById(workCaseId);

        appraisal = appraisalTransform.transformToModel(appraisalView, workCase, user);
        appraisal.setWorkCase(workCase);

        appraisalDAO.persist(appraisal);
        log.info( "appraisalDAO persist end" );

        appraisalDetailViewList = appraisalView.getAppraisalDetailViewList();
        appraisalContactDetailViewList = appraisalView.getAppraisalContactDetailViewList();

        if(appraisalDetailViewList.size()>0){
            List<AppraisalPurpose>   appraisalDetailListDel = appraisalDetailDAO.findByAppraisal(appraisal);
            appraisalDetailDAO.delete(appraisalDetailListDel);
        }
        appraisalDetailList = appraisalDetailTransform.transformToModel(appraisalDetailViewList, appraisal);
        appraisalDetailDAO.persist(appraisalDetailList);
        log.info( "appraisalDetailDAO persist end" );

        if(appraisalContactDetailViewList.size()>0){
            List<AppraisalContactDetail>   appraisalContactDetailListDel = appraisalContactDetailDAO.findByAppraisal(appraisal);
            appraisalContactDetailDAO.delete(appraisalContactDetailListDel);
        }
        appraisalContactDetailList = appraisalContactDetailTransform.transformToModel(appraisalContactDetailViewList, appraisal);
        appraisalContactDetailDAO.persist(appraisalContactDetailList);
        log.info( "appraisalContactDetailDAO persist end" );

    }
}