package com.clevel.selos.transform;

import com.clevel.selos.dao.working.AppraisalDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.*;
import com.clevel.selos.model.db.working.Appraisal;
import com.clevel.selos.model.db.working.WorkCase;
import com.clevel.selos.model.view.AppraisalView;
import com.clevel.selos.util.Util;
import org.joda.time.DateTime;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.Date;

public class AppraisalTransform extends Transform {
    @Inject
    @SELOS
    Logger log;
    @Inject
    private AppraisalDAO appraisalDAO;
    private AppraisalView appraisalView;
    private Appraisal appraisal;

    @Inject
    public AppraisalTransform() {

    }

    public Appraisal transformToModel(final AppraisalView appraisalView, final WorkCase workCase, final User user){
        log.debug("-- transform AppraisalView to Appraisal");
        appraisal = new Appraisal();
        long id = appraisalView.getId();
        if(id != 0){
            appraisal = appraisalDAO.findById(id);
        }else{
            appraisal.setWorkCase(workCase);
            appraisal.setCreateBy(user);
            appraisal.setCreateDate(DateTime.now().toDate());
        }
        appraisal.setAppraisalType(appraisalView.getAppraisalType());

        if(checkNullObject(appraisalView.getAppraisalDivision()) && checkId0(appraisalView.getAppraisalDivision().getId())){
            appraisal.setAppraisalDivision(appraisalView.getAppraisalDivision());
        } else {
            appraisal.setAppraisalDivision(null);
        }
        if(checkNullObject(appraisalView.getAppraisalCompany()) && checkId0(appraisalView.getAppraisalCompany().getId())){
            appraisal.setAppraisalCompany(appraisalView.getAppraisalCompany());
        } else {
            appraisal.setAppraisalCompany(null);
        }
        if(checkNullObject(appraisalView.getLocationOfProperty()) && checkId0(appraisalView.getLocationOfProperty().getId())){
            appraisal.setLocationOfProperty(appraisalView.getLocationOfProperty());
        } else {
            appraisal.setLocationOfProperty(null);
        }
        if(checkNullObject(appraisalView.getProvinceOfProperty()) && checkId0(appraisalView.getProvinceOfProperty().getCode())){
            appraisal.setProvinceOfProperty(appraisalView.getProvinceOfProperty());
        } else {
            appraisal.setProvinceOfProperty(null);
        }

        appraisal.setAppraisalName(appraisalView.getAppraisalName());
        appraisal.setReceivedTaskDate(appraisalView.getReceivedTaskDate());
        appraisal.setAppraisalDate(appraisalView.getAppraisalDate());
        appraisal.setDueDate(appraisalView.getDueDate());
        appraisal.setAADAdminRemark(appraisalView.getAADAdminRemark());
        appraisal.setAppointmentCusName(appraisalView.getAppointmentCusName());
        appraisal.setAppointmentDate(appraisalView.getAppointmentDate());
        appraisal.setAppointmentTime(appraisalView.getAppointmentTime());
        appraisal.setBdmRemark(appraisalView.getBdmRemark());
        appraisal.setCancelAppointment(appraisalView.getCancelAppointment());
        appraisal.setAppointmentRemark(appraisalView.getAppointmentRemark());
        appraisal.setZoneLocation(appraisalView.getZoneLocation());
        appraisal.setModifyDate(DateTime.now().toDate());
        appraisal.setModifyBy(user);
        return appraisal;
    }

    public AppraisalView transformToView(Appraisal appraisal){
        log.debug("-- transform Appraisal to AppraisalView");
        appraisalView = new AppraisalView();

        appraisalView.setId(appraisal.getId());
        appraisalView.setAppointmentCusName(appraisal.getAppointmentCusName());
        appraisalView.setAADAdminRemark(appraisal.getAADAdminRemark());
        appraisalView.setAppointmentDate(appraisal.getAppointmentDate());
        appraisalView.setAppointmentTime(appraisal.getAppointmentTime());

        if(checkNullObject(appraisal.getAppraisalDivision()) && checkId0(appraisal.getAppraisalDivision().getId())){
            appraisalView.setAppraisalDivision(appraisalView.getAppraisalDivision());
        } else {
            appraisalView.setAppraisalDivision(new AppraisalDivision());
        }
        if(checkNullObject(appraisal.getAppraisalCompany()) && checkId0(appraisal.getAppraisalCompany().getId())){
            appraisalView.setAppraisalCompany(appraisalView.getAppraisalCompany());
        } else {
            appraisalView.setAppraisalCompany(new AppraisalCompany());
        }
        if(checkNullObject(appraisal.getLocationOfProperty()) && checkId0(appraisal.getLocationOfProperty().getId())){
            appraisalView.setLocationOfProperty(appraisalView.getLocationOfProperty());
        } else {
            appraisalView.setLocationOfProperty(new LocationProperty());
        }
        if(checkNullObject(appraisal.getProvinceOfProperty()) && checkId0(appraisal.getProvinceOfProperty().getCode())){
            appraisalView.setProvinceOfProperty(appraisalView.getProvinceOfProperty());
        } else {
            appraisalView.setProvinceOfProperty(new Province());
        }

        appraisalView.setAppraisalDate(appraisal.getAppraisalDate());
        appraisalView.setAppraisalName(appraisal.getAppraisalName());
        appraisalView.setAppraisalType(appraisal.getAppraisalType());
        appraisalView.setBdmRemark(appraisal.getBdmRemark());
        appraisalView.setCancelAppointment(appraisal.getCancelAppointment());
        appraisalView.setAppointmentRemark(appraisal.getAppointmentRemark());
        appraisalView.setDueDate(appraisal.getDueDate());
        appraisalView.setZoneLocation(appraisal.getZoneLocation());
        appraisalView.setReceivedTaskDate(appraisal.getReceivedTaskDate());
        appraisalView.setCreateBy(appraisal.getCreateBy());
        appraisalView.setCreateDate(appraisal.getCreateDate());
        appraisalView.setModifyBy(appraisal.getModifyBy());
        appraisalView.setModifyDate(appraisal.getModifyDate());
        return appraisalView;
    }

    private<T> boolean checkNullObject(T object){
        return !Util.isNull(object);
    }

    private boolean checkId0(int id){
        return !Util.isZero(id);
    }
}
