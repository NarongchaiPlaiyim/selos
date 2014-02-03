package com.clevel.selos.businesscontrol;

import com.clevel.selos.dao.working.*;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.working.*;
import com.clevel.selos.model.view.AppraisalContactDetailView;
import com.clevel.selos.model.view.AppraisalDetailView;
import com.clevel.selos.model.view.AppraisalView;
import com.clevel.selos.model.view.ContactRecordDetailView;
import com.clevel.selos.transform.AppraisalContactDetailTransform;
import com.clevel.selos.transform.AppraisalDetailTransform;
import com.clevel.selos.transform.AppraisalTransform;
import com.clevel.selos.transform.ContactRecordDetailTransform;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;

@Stateless
public class AppraisalAppointmentControl extends BusinessControl {
    @Inject
    @SELOS
    private Logger log;

    @Inject
    WorkCaseDAO workCaseDAO;
    @Inject
    AppraisalDAO appraisalDAO;
    @Inject
    AppraisalDetailDAO appraisalDetailDAO;
    @Inject
    AppraisalContactDetailDAO appraisalContactDetailDAO;
    @Inject
    ContactRecordDetailDAO contactRecordDetailDAO;

    @Inject
    AppraisalTransform appraisalTransform;
    @Inject
    AppraisalDetailTransform appraisalDetailTransform;
    @Inject
    AppraisalContactDetailTransform appraisalContactDetailTransform;
    @Inject
    ContactRecordDetailTransform contactRecordDetailTransform;

    @Inject
    public AppraisalAppointmentControl(){

    }
	
	public AppraisalView getAppraisalAppointmentByWorkCase(long workCaseId){
        log.info("getAppraisalByWorkCase ");

        Appraisal appraisal;
        AppraisalView appraisalView;
        List<AppraisalPurpose> appraisalDetailList;
        List<AppraisalDetailView> appraisalDetailViewList;
        List<AppraisalContactDetail> appraisalContactDetailList;
        List<AppraisalContactDetailView> appraisalContactDetailViewList;
        List<ContactRecordDetail> contactRecordDetailList;
        List<ContactRecordDetailView> contactRecordDetailViewList;

        WorkCase workCase = workCaseDAO.findById(workCaseId);
        log.info("workCase after findById " + workCase );

        appraisal  = appraisalDAO.onSearchByWorkCase(workCase);


        if( appraisal != null){
            log.info("appraisal != null ");
            appraisalView = appraisalTransform.transformToView(appraisal);

            appraisalDetailList = appraisalDetailDAO.findByAppraisal(appraisal);
            if(appraisalDetailList.size()>0){
                appraisalDetailViewList = appraisalDetailTransform.transformToView(appraisalDetailList);
                appraisalView.setAppraisalDetailViewList(appraisalDetailViewList);
            }

            appraisalContactDetailList= appraisalContactDetailDAO.findByAppraisal(appraisal);
            if(appraisalContactDetailList.size()>0){
                appraisalContactDetailViewList = appraisalContactDetailTransform.transformToView(appraisalContactDetailList);
            }

            contactRecordDetailList = contactRecordDetailDAO.findByAppraisal(appraisal);
            if(contactRecordDetailList.size()>0){
                contactRecordDetailViewList = contactRecordDetailTransform.transformToView(contactRecordDetailList);
                appraisalView.setContactRecordDetailViewList(contactRecordDetailViewList);
            }

        }else{
            appraisalView = null;
        }
        return appraisalView;
    }

    public void onSaveAppraisalAppointment(AppraisalView appraisalView,long workCaseId){
        log.info("onSaveAppraisalRequest ");
        Appraisal appraisal;
        List<AppraisalDetailView> appraisalDetailViewList;
        List<AppraisalPurpose> appraisalDetailList;
        List<AppraisalContactDetailView> appraisalContactDetailViewList;
        List<AppraisalContactDetail> appraisalContactDetailList;
        List<ContactRecordDetail> contactRecordDetailList;
        List<ContactRecordDetailView> contactRecordDetailViewList;

        WorkCase workCase = workCaseDAO.findById(workCaseId);

        appraisal = appraisalTransform.transformToModel(appraisalView, workCase, getCurrentUser());
        appraisal.setWorkCase(workCase);

        appraisalDAO.persist(appraisal);
        log.info( "appraisalDAO persist end" );

        appraisalDetailViewList = appraisalView.getAppraisalDetailViewList();
        contactRecordDetailViewList = appraisalView.getContactRecordDetailViewList();

        if(appraisalDetailViewList.size()>0){
            List<AppraisalPurpose>   appraisalDetailListDel = appraisalDetailDAO.findByAppraisal(appraisal);
            appraisalDetailDAO.delete(appraisalDetailListDel);
        }
        appraisalDetailList = appraisalDetailTransform.transformToModel(appraisalDetailViewList, appraisal);
        appraisalDetailDAO.persist(appraisalDetailList);
        log.info( "appraisalDetailDAO persist end" );



//        if(appraisalContactDetailViewList.size()>0){
//            List<AppraisalContactDetail>   appraisalContactDetailListDel = appraisalContactDetailDAO.findByAppraisal(appraisal);
//            appraisalContactDetailDAO.delete(appraisalContactDetailListDel);
//        }
//        appraisalContactDetailList = appraisalContactDetailTransform.transformToModel(appraisalContactDetailViewList, appraisal);
//        appraisalContactDetailDAO.persist(appraisalContactDetailList);
//        log.info( "appraisalContactDetailDAO persist end" );



        if(contactRecordDetailViewList.size()>0){
            List<ContactRecordDetail>   contactRecordDetailListDel = contactRecordDetailDAO.findByAppraisal(appraisal);
            contactRecordDetailDAO.delete(contactRecordDetailListDel);
        }
        contactRecordDetailList = contactRecordDetailTransform.transformToModel(contactRecordDetailViewList, appraisal,workCase);
        contactRecordDetailDAO.persist(contactRecordDetailList);
        log.info( "contactRecordDetailDAO persist end" );

    }
}