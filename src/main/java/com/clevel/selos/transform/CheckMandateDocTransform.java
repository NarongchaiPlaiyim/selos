package com.clevel.selos.transform;

import com.clevel.selos.integration.NCB;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.integration.ecm.db.ECMDetail;
import com.clevel.selos.model.db.working.MandateDoc;
import com.clevel.selos.model.view.*;
import com.clevel.selos.system.Config;
import com.clevel.selos.util.Util;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CheckMandateDocTransform extends Transform {
    @Inject
    @NCB
    private Logger log;
    @Inject
    @Config(name = "interface.workplace.address")
    private String address;
    @Inject
    @Config(name = "interface.mandate.doc.objectStore")
    private String objectStore;
    private CheckMandateDocView checkMandateDocView;
    private MandateDoc mandateDoc;

    private CheckMandatoryDocView checkMandatoryDocView;
    private CheckOptionalDocView checkOptionalDocView;
    private CheckOtherDocView checkOtherDocView;

    @Inject
    public CheckMandateDocTransform() {
        init();
    }

    private void init(){
        log.debug("-- init()");
        checkMandateDocView = null;
    }

    public CheckMandateDocView transformToView(final MandateDoc mandateDoc){
        checkMandateDocView = new CheckMandateDocView();
        return checkMandateDocView;
    }

    public MandateDoc transformToModel(final CheckMandateDocView checkMandateDocView){
        mandateDoc = new MandateDoc();
        return mandateDoc;
    }

    //BRMS and ECM
    public CheckMandatoryDocView transformToCheckMandatoryDocView(final MandateDocView mandateDocView, final List<ECMDetail> ecmDetailList, final int complete, final String token){
        checkMandatoryDocView = new CheckMandatoryDocView();
        boolean flag = false;
        //Checking Document Type
        if(!Util.isNull(mandateDocView.getEcmDocTypeDesc()) && !Util.isZero(mandateDocView.getEcmDocTypeDesc().length())){
            log.debug("-- MandateDocView.EcmDocTypeDesc is not null");
            log.debug("-- Document Type is {}", mandateDocView.getEcmDocTypeDesc());
            checkMandatoryDocView.setDocumentType(mandateDocView.getEcmDocTypeDesc());
        } else {
            flag = true;
        }

        //Checking BRMS Document Type
        List<String> brmsDescList = Util.safetyList(mandateDocView.getBrmsDescList());
        checkMandatoryDocView.setBRMSDocumentTypeList(brmsDescList);

        //Checking Owners
        List<CustomerInfoSimpleView> customerInfoSimpleViewList = Util.safetyList(mandateDocView.getCustomerInfoSimpleViewList());
        List<String> ownersList  = new ArrayList<String>();
        for(CustomerInfoSimpleView customerInfoSimpleView : customerInfoSimpleViewList){
            ownersList.add(customerInfoSimpleView.getCustomerName());
        }
        checkMandatoryDocView.setOwnewList(ownersList);

        //File Name
        List<CheckMandatoryDocFileNameView> fileNameViewList = new ArrayList<CheckMandatoryDocFileNameView>();
        CheckMandatoryDocFileNameView checkMandatoryDocFileNameView = null;
        for(ECMDetail ecmDetail : ecmDetailList){
            if(flag){
                if(!Util.isNull(ecmDetail.getTypeNameTH()) && !Util.isZero(ecmDetail.getTypeNameTH().length())){
                    log.debug("-- EcmDetail.TypeNameTH is not null");
                    log.debug("-- Document Type is {}", ecmDetail.getTypeNameTH());
                    checkMandatoryDocView.setDocumentType(ecmDetail.getTypeNameTH());
                    flag = false;
                } else {
                    log.debug("-- EcmDetail.TypeNameTH is null");
                    log.debug("-- Document Type is {}", "Empty");
                    checkMandatoryDocView.setDocumentType("");
                    flag = true;
                }
            }

            checkMandatoryDocFileNameView = new CheckMandatoryDocFileNameView();
            checkMandatoryDocFileNameView.setFileName(ecmDetail.getOrgFileName());
            checkMandatoryDocFileNameView.setUrl(getURLByFNId(ecmDetail.getFnDocId(), token));
            fileNameViewList.add(checkMandatoryDocFileNameView);
        }
        checkMandatoryDocView.setFileNameViewList(fileNameViewList);

        //is Complete
        checkMandatoryDocView.setComplete(complete);



        return checkMandatoryDocView;
    }
    public CheckOptionalDocView transformToCheckOptionalDocView(final MandateDocView mandateDocView, final List<ECMDetail> ecmDetailList, final int complete, final String token ){
        checkOptionalDocView = new CheckOptionalDocView();
        return checkOptionalDocView;
    }
    public CheckOtherDocView transformToCheckOtherDocView(final MandateDocView mandateDocView, final List<ECMDetail> ecmDetailList, final int complete, final String token ){
        checkOtherDocView = new CheckOtherDocView();
        return checkOtherDocView;
    }

    //BRMS
    public CheckMandatoryDocView transformToCheckMandatoryDocView(final MandateDocView mandateDocView, final int complete){
        checkMandatoryDocView = new CheckMandatoryDocView();
        //Checking Document Type
        if(!Util.isNull(mandateDocView.getEcmDocTypeDesc()) && !Util.isZero(mandateDocView.getEcmDocTypeDesc().length())){
            log.debug("-- MandateDocView.EcmDocTypeDesc is not null");
            log.debug("-- Document Type is {}", mandateDocView.getEcmDocTypeDesc());
            checkMandatoryDocView.setDocumentType(mandateDocView.getEcmDocTypeDesc());
        } else {
            checkMandatoryDocView.setDocumentType("");
        }

        //Checking BRMS Document Type
        List<String> brmsDescList = Util.safetyList(mandateDocView.getBrmsDescList());
        checkMandatoryDocView.setBRMSDocumentTypeList(brmsDescList);

        //Checking Owners
        List<CustomerInfoSimpleView> customerInfoSimpleViewList = Util.safetyList(mandateDocView.getCustomerInfoSimpleViewList());
        List<String> ownersList  = new ArrayList<String>();
        for(CustomerInfoSimpleView customerInfoSimpleView : customerInfoSimpleViewList){
            ownersList.add(customerInfoSimpleView.getCustomerName());
        }
        checkMandatoryDocView.setOwnewList(ownersList);

        //is Complete
        checkMandatoryDocView.setComplete(complete);
        return checkMandatoryDocView;
    }
    public CheckOptionalDocView transformToCheckOptionalDocView(final MandateDocView mandateDocView, final int complete){
        checkOptionalDocView = new CheckOptionalDocView();
        //Checking Document Type
        if(!Util.isNull(mandateDocView.getEcmDocTypeDesc()) && !Util.isZero(mandateDocView.getEcmDocTypeDesc().length())){
            log.debug("-- MandateDocView.EcmDocTypeDesc is not null");
            log.debug("-- Document Type is {}", mandateDocView.getEcmDocTypeDesc());
            checkOptionalDocView.setDocumentType(mandateDocView.getEcmDocTypeDesc());
        } else {
            checkOptionalDocView.setDocumentType("");
        }

        //Checking BRMS Document Type
        List<String> brmsDescList = Util.safetyList(mandateDocView.getBrmsDescList());
        checkOptionalDocView.setBRMSDocumentTypeList(brmsDescList);

        //Checking Owners
        List<CustomerInfoSimpleView> customerInfoSimpleViewList = Util.safetyList(mandateDocView.getCustomerInfoSimpleViewList());
        List<String> ownersList  = new ArrayList<String>();
        for(CustomerInfoSimpleView customerInfoSimpleView : customerInfoSimpleViewList){
            ownersList.add(customerInfoSimpleView.getCustomerName());
        }
        checkOptionalDocView.setOwnewList(ownersList);

        //is Complete
        checkOptionalDocView.setComplete(complete);
        return checkOptionalDocView;
    }
    public CheckOtherDocView transformToCheckOtherDocView(final MandateDocView mandateDocView, final int complete){
        checkOtherDocView = new CheckOtherDocView();
        //Checking Document Type
        if(!Util.isNull(mandateDocView.getEcmDocTypeDesc()) && !Util.isZero(mandateDocView.getEcmDocTypeDesc().length())){
            log.debug("-- MandateDocView.EcmDocTypeDesc is not null");
            log.debug("-- Document Type is {}", mandateDocView.getEcmDocTypeDesc());
            checkOtherDocView.setDocumentType(mandateDocView.getEcmDocTypeDesc());
        } else {
            checkOtherDocView.setDocumentType("");
        }

        //Checking BRMS Document Type
        List<String> brmsDescList = Util.safetyList(mandateDocView.getBrmsDescList());
        checkOtherDocView.setBRMSDocumentTypeList(brmsDescList);

        //Checking Owners
        List<CustomerInfoSimpleView> customerInfoSimpleViewList = Util.safetyList(mandateDocView.getCustomerInfoSimpleViewList());
        List<String> ownersList  = new ArrayList<String>();
        for(CustomerInfoSimpleView customerInfoSimpleView : customerInfoSimpleViewList){
            ownersList.add(customerInfoSimpleView.getCustomerName());
        }
        checkOtherDocView.setOwnewList(ownersList);

        //is Complete
        checkOtherDocView.setComplete(complete);
        return checkOtherDocView;
    }

    //ECM
    public CheckOtherDocView transformToCheckOtherDocView(final List<ECMDetail> ecmDetailList, final int complete, final String token ){
        checkOtherDocView = new CheckOtherDocView();
//        //Checking Owners
//        List<CustomerInfoSimpleView> customerInfoSimpleViewList = Util.safetyList(mandateDocView.getCustomerInfoSimpleViewList());
//        List<String> ownersList  = new ArrayList<String>();
//        for(CustomerInfoSimpleView customerInfoSimpleView : customerInfoSimpleViewList){
//            ownersList.add(customerInfoSimpleView.getCustomerName());
//        }
//        checkMandatoryDocView.setOwnewList(ownersList);

        List<CheckMandatoryDocFileNameView> fileNameViewList = new ArrayList<CheckMandatoryDocFileNameView>();
        CheckMandatoryDocFileNameView checkMandatoryDocFileNameView = null;
        boolean flag = true;
        for(ECMDetail ecmDetail : ecmDetailList){
            //Checking Document Type
            if(flag){
                if(!Util.isNull(ecmDetail.getTypeNameTH()) && !Util.isZero(ecmDetail.getTypeNameTH().length())){
                    log.debug("-- EcmDetail.TypeNameTH is not null");
                    log.debug("-- Document Type is {}", ecmDetail.getTypeNameTH());
                    checkOtherDocView.setDocumentType(ecmDetail.getTypeNameTH());
                } else {
                    log.debug("-- EcmDetail.TypeNameTH is null");
                    log.debug("-- Document Type is {}", "Empty");
                    checkOtherDocView.setDocumentType("");
                }
                flag = false;
            }

            //File Name
            checkMandatoryDocFileNameView = new CheckMandatoryDocFileNameView();
            checkMandatoryDocFileNameView.setFileName(ecmDetail.getOrgFileName());
            checkMandatoryDocFileNameView.setUrl(getURLByFNId(ecmDetail.getFnDocId(), token));
            fileNameViewList.add(checkMandatoryDocFileNameView);
        }
        checkMandatoryDocView.setFileNameViewList(fileNameViewList);

        //is Complete
        checkOtherDocView.setComplete(complete);
        return checkOtherDocView;
    }

    //URL
    private String getURLByFNId(final String FNId, final String token){
        return address+"/getContent?objectStoreName="+objectStore+"&id="+FNId+"&objectType=document&ut=" + token;
    }
}
