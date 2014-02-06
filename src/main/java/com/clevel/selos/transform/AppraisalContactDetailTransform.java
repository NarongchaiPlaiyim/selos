package com.clevel.selos.transform;

import com.clevel.selos.dao.working.AppraisalContactDetailDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.working.Appraisal;
import com.clevel.selos.model.db.working.AppraisalContactDetail;
import com.clevel.selos.model.view.AppraisalContactDetailView;
import com.clevel.selos.util.Util;
import org.joda.time.DateTime;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class AppraisalContactDetailTransform extends Transform {
    @Inject
    @SELOS
    Logger log;
    @Inject
    private AppraisalContactDetailDAO appraisalContactDetailDAO;
    private AppraisalContactDetailView appraisalContactDetailView;
    private List<AppraisalContactDetail> appraisalContactDetailList;
    @Inject
    public AppraisalContactDetailTransform() {

    }

    public List<AppraisalContactDetail> transformToModel(final AppraisalContactDetailView view, final long appraisal, final User user){
        log.debug("-- transform AppraisalContactDetailView to List<AppraisalContactDetail> {}", ""+appraisalContactDetailView.toString());
        long contractId = 0;
        AppraisalContactDetail model = null;
        String contractForCheck;
        appraisalContactDetailList = new ArrayList<AppraisalContactDetail>();
        log.debug("-- new ArrayList<AppraisalContactDetail>() created");

        contractForCheck = view.getContactNo1();
        log.debug("-- Contract = {}", contractForCheck);
        if(!Util.isLengthZero(contractForCheck)){
            contractId =  view.getContractId1();
            if(!Util.isZero(contractId)){
                model = appraisalContactDetailDAO.findById(contractId);
                log.debug("-- AppraisalContactDetail[1] findById [{}]", contractId);
            } else {
                model = new AppraisalContactDetail();
                log.debug("-- new AppraisalContactDetail[1] created");
                model.setCreateBy(user);
                model.setCreateDate(DateTime.now().toDate());
            }
            model.setContactNo(view.getContactNo1());
            model.setCustomerName(view.getCustomerName1());
            model.setModifyBy(user);
            model.setModifyDate(DateTime.now().toDate());
            appraisalContactDetailList.add(model);
            log.debug("-- AppraisalContactDetail added to appraisalContactDetailList[{}]", appraisalContactDetailList.size());
        }

        contractForCheck = view.getContactNo2();
        log.debug("-- Contract = {}", contractForCheck);
        if(!Util.isLengthZero(contractForCheck)){
            contractId =  view.getContractId2();
            if(!Util.isZero(contractId)){
                model = appraisalContactDetailDAO.findById(contractId);
                log.debug("-- AppraisalContactDetail[2] findById [{}]", contractId);
            } else {
                model = new AppraisalContactDetail();
                log.debug("-- new AppraisalContactDetail[2] created");
                model.setCreateBy(user);
                model.setCreateDate(DateTime.now().toDate());
            }
            model.setContactNo(view.getContactNo2());
            model.setCustomerName(view.getCustomerName2());
            model.setModifyBy(user);
            model.setModifyDate(DateTime.now().toDate());
            appraisalContactDetailList.add(model);
        }

        contractForCheck = view.getContactNo3();
        log.debug("-- Contract = {}", contractForCheck);
        if(!Util.isLengthZero(contractForCheck)){
            contractId =  view.getContractId3();
            if(!Util.isZero(contractId)){
                model = appraisalContactDetailDAO.findById(contractId);
                log.debug("-- AppraisalContactDetail[3] findById [{}]", contractId);
            } else {
                model = new AppraisalContactDetail();
                log.debug("-- new AppraisalContactDetail[3] created");
                model.setCreateBy(user);
                model.setCreateDate(DateTime.now().toDate());
            }
            model.setContactNo(view.getContactNo3());
            model.setCustomerName(view.getCustomerName3());
            model.setModifyBy(user);
            model.setModifyDate(DateTime.now().toDate());
            appraisalContactDetailList.add(model);
        }
        return appraisalContactDetailList;
    }

    public AppraisalContactDetailView transformToView(final List<AppraisalContactDetail> appraisalContactDetailList, final Appraisal appraisal , final User user){
        log.debug("-- transform List<AppraisalContactDetail> to AppraisalContactDetailView(Size of list is {})", ""+appraisalContactDetailList.size());
        appraisalContactDetailView = new AppraisalContactDetailView();
        int size = 0;
        size = appraisalContactDetailList.size();
        AppraisalContactDetail appraisalContactDetail;
        appraisalContactDetailView.setId(appraisal.getId());
        if(size == 1){
            appraisalContactDetail = appraisalContactDetailList.get(0);
            appraisalContactDetailView.setContractId1(appraisalContactDetail.getId());
            appraisalContactDetailView.setCustomerName1(appraisalContactDetail.getCustomerName());
            appraisalContactDetailView.setContactNo1(appraisalContactDetail.getContactNo());
        } else if(size == 2){
            appraisalContactDetail = appraisalContactDetailList.get(0);
            appraisalContactDetailView.setContractId1(appraisalContactDetail.getId());
            appraisalContactDetailView.setCustomerName1(appraisalContactDetail.getCustomerName());
            appraisalContactDetailView.setContactNo1(appraisalContactDetail.getContactNo());
            appraisalContactDetail = appraisalContactDetailList.get(1);
            appraisalContactDetailView.setContractId2(appraisalContactDetail.getId());
            appraisalContactDetailView.setCustomerName2(appraisalContactDetail.getCustomerName());
            appraisalContactDetailView.setContactNo2(appraisalContactDetail.getContactNo());
        } else if(size == 3){
            appraisalContactDetail = appraisalContactDetailList.get(0);
            appraisalContactDetailView.setContractId1(appraisalContactDetail.getId());
            appraisalContactDetailView.setCustomerName1(appraisalContactDetail.getCustomerName());
            appraisalContactDetailView.setContactNo1(appraisalContactDetail.getContactNo());
            appraisalContactDetail = appraisalContactDetailList.get(1);
            appraisalContactDetailView.setContractId2(appraisalContactDetail.getId());
            appraisalContactDetailView.setCustomerName2(appraisalContactDetail.getCustomerName());
            appraisalContactDetailView.setContactNo2(appraisalContactDetail.getContactNo());
            appraisalContactDetail = appraisalContactDetailList.get(2);
            appraisalContactDetailView.setContractId3(appraisalContactDetail.getId());
            appraisalContactDetailView.setCustomerName3(appraisalContactDetail.getCustomerName());
            appraisalContactDetailView.setContactNo3(appraisalContactDetail.getContactNo());
        }
        appraisalContactDetailView.setModifyBy(user);
        appraisalContactDetailView.setModifyDate(DateTime.now().toDate());
        log.debug("-- appraisalContactDetailView = {}", appraisalContactDetailView.toString());
        return appraisalContactDetailView;
    }
}
