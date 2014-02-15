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
        log.debug("-- transform AppraisalContactDetailView to List<AppraisalContactDetail> {}", ""+view.toString());
        long contractId = 0;
        AppraisalContactDetail model = null;
        String contractForCheck;
        appraisalContactDetailList = new ArrayList<AppraisalContactDetail>();
        log.debug("-- new ArrayList<AppraisalContactDetail>() created");

        contractForCheck = view.getContactNo1();
        log.debug("-- Contract = {}", contractForCheck);
        if(!Util.isLengthZero(contractForCheck)){
            contractId =  view.getContractId1();
            model = new AppraisalContactDetail();
//            model.setId(contractId);
            log.debug("-- new AppraisalContactDetail[1] created");
            model.setCreateBy(user);
//            log.debug("---- AppraisalContactDetail.createBy[{}]", model.getCreateBy());
            model.setCreateDate(DateTime.now().toDate());
//            log.debug("---- AppraisalContactDetail.createDate[{}]", model.getCreateDate());
            model.setContactNo(view.getContactNo1());
//            log.debug("---- AppraisalContactDetail.contactNo[{}]", model.getContactNo());
            model.setCustomerName(view.getCustomerName1());
//            log.debug("---- AppraisalContactDetail.customerName[{}]", model.getCustomerName());
            model.setModifyBy(user);
//            log.debug("---- AppraisalContactDetail.modifyBy[{}]", model.getModifyBy());
            model.setModifyDate(DateTime.now().toDate());
//            log.debug("---- AppraisalContactDetail.modifyDate[{}]", model.getModifyDate());
            appraisalContactDetailList.add(model);
            log.debug("-- AppraisalContactDetail added to appraisalContactDetailList[{}]", appraisalContactDetailList.size());
        }

        contractForCheck = view.getContactNo2();
        log.debug("-- Contract = {}", contractForCheck);
        if(!Util.isLengthZero(contractForCheck)){
            contractId =  view.getContractId2();
            model = new AppraisalContactDetail();
//            model.setId(contractId);
            log.debug("-- new AppraisalContactDetail[2] created");
            model.setCreateBy(user);
//            log.debug("---- AppraisalContactDetail.createBy[{}]", model.getCreateBy());
            model.setCreateDate(DateTime.now().toDate());
//            log.debug("---- AppraisalContactDetail.createDate[{}]", model.getCreateDate());
            model.setContactNo(view.getContactNo2());
//            log.debug("---- AppraisalContactDetail.contactNo[{}]", model.getContactNo());
            model.setCustomerName(view.getCustomerName2());
//            log.debug("---- AppraisalContactDetail.customerName[{}]", model.getCustomerName());
            model.setModifyBy(user);
//            log.debug("---- AppraisalContactDetail.modifyBy[{}]", model.getModifyBy());
            model.setModifyDate(DateTime.now().toDate());
//            log.debug("---- AppraisalContactDetail.modifyDate[{}]", model.getModifyDate());
            appraisalContactDetailList.add(model);
        }

        contractForCheck = view.getContactNo3();
        log.debug("-- Contract = {}", contractForCheck);
        if(!Util.isLengthZero(contractForCheck)){
            contractId =  view.getContractId3();
            model = new AppraisalContactDetail();
//            model.setId(contractId);
            log.debug("-- new AppraisalContactDetail[3] created");
            model.setCreateBy(user);
//            log.debug("---- AppraisalContactDetail.createBy[{}]", model.getCreateBy());
            model.setCreateDate(DateTime.now().toDate());
//            log.debug("---- AppraisalContactDetail.createDate[{}]", model.getCreateDate());
            model.setContactNo(view.getContactNo3());
//            log.debug("---- AppraisalContactDetail.contactNo[{}]", model.getContactNo());
            model.setCustomerName(view.getCustomerName3());
//            log.debug("---- AppraisalContactDetail.customerName[{}]", model.getCustomerName());
            model.setModifyBy(user);
//            log.debug("---- AppraisalContactDetail.modifyBy[{}]", model.getModifyBy());
            model.setModifyDate(DateTime.now().toDate());
//            log.debug("---- AppraisalContactDetail.modifyDate[{}]", model.getModifyDate());
            appraisalContactDetailList.add(model);
            log.debug("-- AppraisalContactDetail added to appraisalContactDetailList[{}]", appraisalContactDetailList.size());
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
//            appraisalContactDetailView.setNo1(appraisalContactDetail.getId());
            appraisalContactDetailView.setContractId1(appraisalContactDetail.getId());
            appraisalContactDetailView.setCustomerName1(appraisalContactDetail.getCustomerName());
            appraisalContactDetailView.setContactNo1(appraisalContactDetail.getContactNo());
        } else if(size == 2){
            appraisalContactDetail = appraisalContactDetailList.get(0);
//            appraisalContactDetailView.setNo1(appraisalContactDetail.getId());
            appraisalContactDetailView.setContractId1(appraisalContactDetail.getId());
            appraisalContactDetailView.setCustomerName1(appraisalContactDetail.getCustomerName());
            appraisalContactDetailView.setContactNo1(appraisalContactDetail.getContactNo());
            appraisalContactDetail = appraisalContactDetailList.get(1);
//            appraisalContactDetailView.setNo2(appraisalContactDetail.getId());
            appraisalContactDetailView.setContractId2(appraisalContactDetail.getId());
            appraisalContactDetailView.setCustomerName2(appraisalContactDetail.getCustomerName());
            appraisalContactDetailView.setContactNo2(appraisalContactDetail.getContactNo());
        } else if(size == 3){
            appraisalContactDetail = appraisalContactDetailList.get(0);
//            appraisalContactDetailView.setNo1(appraisalContactDetail.getId());
            appraisalContactDetailView.setContractId1(appraisalContactDetail.getId());
            appraisalContactDetailView.setCustomerName1(appraisalContactDetail.getCustomerName());
            appraisalContactDetailView.setContactNo1(appraisalContactDetail.getContactNo());
            appraisalContactDetail = appraisalContactDetailList.get(1);
//            appraisalContactDetailView.setNo2(appraisalContactDetail.getId());
            appraisalContactDetailView.setContractId2(appraisalContactDetail.getId());
            appraisalContactDetailView.setCustomerName2(appraisalContactDetail.getCustomerName());
            appraisalContactDetailView.setContactNo2(appraisalContactDetail.getContactNo());
            appraisalContactDetail = appraisalContactDetailList.get(2);
//            appraisalContactDetailView.setNo3(appraisalContactDetail.getId());
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
