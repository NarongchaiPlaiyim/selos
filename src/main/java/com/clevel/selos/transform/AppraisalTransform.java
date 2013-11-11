package com.clevel.selos.transform;

import com.clevel.selos.model.db.working.Appraisal;
import com.clevel.selos.model.view.AppraisalView;
import org.joda.time.DateTime;

public class AppraisalTransform extends Transform {

    public Appraisal transformToModel(AppraisalView appraisalView){

        Appraisal appraisal = new Appraisal();
        if(appraisalView.getId()==0){
            appraisal.setCreateBy(appraisalView.getCreateBy());
            appraisal.setCreateDate(DateTime.now().toDate());
        }else{
            appraisal.setId(appraisalView.getId());
        }

        appraisal.setAppointmentCusName(appraisalView.getAppointmentCusName());
        appraisal.setAADAdminRemark(appraisalView.getAADAdminRemark());
        appraisal.setAppointmentDate(appraisalView.getAppointmentDate());
        appraisal.setAppointmentTime(appraisalView.getAppointmentTime());
        appraisal.setAppraisalCompany(appraisalView.getAppraisalCompany());
        appraisal.setAppraisalDate(appraisalView.getAppraisalDate());
        appraisal.setAppraisalDivision(appraisalView.getAppraisalDivision());
        appraisal.setAppraisalName(appraisalView.getAppraisalName());
        appraisal.setAppraisalType(appraisalView.getAppraisalType());
        appraisal.setBdmRemark(appraisalView.getBdmRemark());
        appraisal.setCancelAppointment(appraisalView.getCancelAppointment());
        appraisal.setAppointmentRemark(appraisalView.getAppointmentRemark());
        appraisal.setDueDate(appraisalView.getDueDate());
        appraisal.setLocationOfProperty(appraisalView.getLocationOfProperty());
        appraisal.setZoneLocation(appraisalView.getZoneLocation());
        appraisal.setProvinceOfProperty(appraisalView.getProvinceOfProperty());
        appraisal.setReceivedTaskDate(appraisalView.getReceivedTaskDate());

        appraisal.setModifyBy(appraisalView.getModifyBy());
        appraisal.setModifyDate(DateTime.now().toDate());

        return appraisal;
    }

    public AppraisalView transformToView(Appraisal appraisal){

        AppraisalView appraisalView = new AppraisalView();
        appraisalView.setAppointmentCusName(appraisal.getAppointmentCusName());
        appraisalView.setAADAdminRemark(appraisal.getAADAdminRemark());
        appraisalView.setAppointmentDate(appraisal.getAppointmentDate());
        appraisalView.setAppointmentTime(appraisal.getAppointmentTime());
        appraisalView.setAppraisalCompany(appraisal.getAppraisalCompany());
        appraisalView.setAppraisalDate(appraisal.getAppraisalDate());
        appraisalView.setAppraisalDivision(appraisal.getAppraisalDivision());
        appraisalView.setAppraisalName(appraisal.getAppraisalName());
        appraisalView.setAppraisalType(appraisal.getAppraisalType());
        appraisalView.setBdmRemark(appraisal.getBdmRemark());
        appraisalView.setCancelAppointment(appraisal.getCancelAppointment());
        appraisalView.setAppointmentRemark(appraisal.getAppointmentRemark());
        appraisalView.setDueDate(appraisal.getDueDate());
        appraisalView.setLocationOfProperty(appraisal.getLocationOfProperty());
        appraisalView.setZoneLocation(appraisal.getZoneLocation());
        appraisalView.setProvinceOfProperty(appraisal.getProvinceOfProperty());
        appraisalView.setReceivedTaskDate(appraisal.getReceivedTaskDate());

        appraisalView.setId(appraisal.getId());
        appraisalView.setCreateBy(appraisal.getCreateBy());
        appraisalView.setCreateDate(appraisal.getCreateDate());
        appraisalView.setModifyBy(appraisal.getModifyBy());
        appraisalView.setModifyDate(appraisal.getModifyDate());

        return appraisalView;
    }
}
