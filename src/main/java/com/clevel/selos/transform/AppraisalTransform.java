package com.clevel.selos.transform;

import com.clevel.selos.dao.working.AppraisalDAO;
import com.clevel.selos.model.db.master.*;
import com.clevel.selos.model.db.working.Appraisal;
import com.clevel.selos.model.db.working.WorkCase;
import com.clevel.selos.model.view.AppraisalView;
import org.joda.time.DateTime;

import javax.inject.Inject;
import java.util.Date;

public class AppraisalTransform extends Transform {
    @Inject
    private NewCollateralTransform collateralDetailTransform;
    @Inject
    private AppraisalDAO appraisalDAO;
    private AppraisalView appraisalView;
    private Appraisal appraisal;

    @Inject
    public AppraisalTransform() {

    }

    public Appraisal transformToModel(final AppraisalView appraisalView, final WorkCase workCase, final User user){
        appraisal = new Appraisal();
        if(appraisalView.getId()!=0){
            appraisal = appraisalDAO.findById(appraisalView.getId());
        }else{
            appraisal.setWorkCase(workCase);
            appraisal.setCreateBy(user);
            appraisal.setCreateDate(DateTime.now().toDate());
        }
        appraisal.setAppraisalType(appraisalView.getAppraisalType());
        appraisal.setAppraisalCompany(appraisalView.getAppraisalCompany());
        appraisal.setAppraisalDivision(appraisalView.getAppraisalDivision());
        appraisal.setAppraisalName(appraisalView.getAppraisalName());
        appraisal.setReceivedTaskDate(appraisalView.getReceivedTaskDate());
        appraisal.setLocationOfProperty(appraisalView.getLocationOfProperty());
        appraisal.setProvinceOfProperty(appraisalView.getProvinceOfProperty());
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
        appraisalView = new AppraisalView();

        appraisalView.setId(appraisal.getId());
        appraisalView.setAppointmentCusName(appraisal.getAppointmentCusName());
        appraisalView.setAADAdminRemark(appraisal.getAADAdminRemark());
        appraisalView.setAppointmentDate(appraisal.getAppointmentDate());
        appraisalView.setAppointmentTime(appraisal.getAppointmentTime());

        if(appraisal.getAppraisalCompany()!=null && appraisal.getAppraisalCompany().getId()!=0){
            appraisalView.setAppraisalCompany(appraisal.getAppraisalCompany());
        }else{
            appraisalView.setAppraisalCompany(new AppraisalCompany());
        }

        appraisalView.setAppraisalDate(appraisal.getAppraisalDate());
        if(appraisal.getAppraisalCompany()!=null && appraisal.getAppraisalDivision().getId()!=0){
            appraisalView.setAppraisalDivision(appraisal.getAppraisalDivision());
        }else{
            appraisalView.setAppraisalDivision(new AppraisalDivision());
        }

        if(appraisal.getLocationOfProperty()!=null && appraisal.getLocationOfProperty().getId()!=0){
            appraisalView.setLocationOfProperty(appraisal.getLocationOfProperty());
        }else{
            appraisalView.setLocationOfProperty(new LocationProperty());
        }

        if(appraisal.getProvinceOfProperty()!=null && appraisal.getProvinceOfProperty().getCode()!=0){
            appraisalView.setProvinceOfProperty(appraisal.getProvinceOfProperty());
        }else{
            appraisalView.setProvinceOfProperty(new Province());
        }

        appraisalView.setAppraisalName(appraisal.getAppraisalName());
        appraisalView.setAppraisalType(appraisal.getAppraisalType());
        appraisalView.setBdmRemark(appraisal.getBdmRemark());
        appraisalView.setCancelAppointment(appraisal.getCancelAppointment());
        appraisalView.setAppointmentRemark(appraisal.getAppointmentRemark());
        appraisalView.setDueDate(appraisal.getDueDate());
        appraisalView.setZoneLocation(appraisal.getZoneLocation());
        appraisalView.setReceivedTaskDate(appraisal.getReceivedTaskDate());
        appraisalView.setId(appraisal.getId());
        appraisalView.setCreateBy(appraisal.getCreateBy());
        appraisalView.setCreateDate(appraisal.getCreateDate());
        appraisalView.setModifyBy(appraisal.getModifyBy());
        appraisalView.setModifyDate(appraisal.getModifyDate());
        return appraisalView;
    }
}
