package com.clevel.selos.transform;

import com.clevel.selos.dao.working.MandateDocDAO;
import com.clevel.selos.dao.working.WorkCaseDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.integration.ecm.db.ECMDetail;
import com.clevel.selos.model.DocMandateType;
import com.clevel.selos.model.db.master.Role;
import com.clevel.selos.model.db.working.*;
import com.clevel.selos.model.view.*;
import com.clevel.selos.system.Config;
import com.clevel.selos.util.Util;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class CheckMandateDocTransform extends Transform {
    @Inject
    @SELOS
    private Logger log;
    @Inject
    @Config(name = "interface.workplace.address")
    private String address;
    @Inject
    @Config(name = "interface.mandate.doc.objectStore")
    private String objectStore;
    @Inject
    private MandateDocDAO mandateDocDAO;
    @Inject
    private WorkCaseDAO workCaseDAO;
    private WorkCase workCase;
    private CheckMandateDocView checkMandateDocView;
    private MandateDoc mandateDoc;
    @Inject
    private CheckMandateDocBRMSTransform checkMandateDocBRMSTransform;
    @Inject
    private CheckMandateDocCustTransform checkMandateDocCustTransform;
    @Inject
    private CheckMandateDocFileNameTransform checkMandateDocFileNameTransform;
    private List<CheckMandatoryDocView> mandatoryDocumentsList;
    private CheckMandatoryDocView checkMandatoryDocView;
    private List<CheckOptionalDocView> optionalDocumentsList;
    private CheckOptionalDocView checkOptionalDocView;
    private List<CheckOtherDocView> otherDocumentsList;
    private CheckOtherDocView checkOtherDocView;

    @Inject
    public CheckMandateDocTransform() {
//        init();
    }

    @PostConstruct
    private void init(){
        log.debug("-- init()");
        checkMandateDocView = null;
        mandatoryDocumentsList = null;
        optionalDocumentsList = null;
        otherDocumentsList = null;
    }

    public CheckMandateDocView transformToView(final List<MandateDoc> mandateDocList){
        log.debug("-- transformToView(MandateDocList.size()[{}])", mandateDocList.size());
        CheckMandateDocView view = new CheckMandateDocView();
        List<CheckMandateDocView> viewList = new ArrayList<CheckMandateDocView>();
        List<CheckMandatoryDocView> mandatoryDocumentsList = new ArrayList<CheckMandatoryDocView>();
        CheckMandatoryDocView mandatoryDocView = null;
        List<CheckOptionalDocView> optionalDocumentsList = new ArrayList<CheckOptionalDocView>();
        CheckOptionalDocView optionalDocView = null;
        List<CheckOtherDocView> otherDocumentsList = new ArrayList<CheckOtherDocView>();
        CheckOtherDocView otherDocView = null;

        for (MandateDoc model : mandateDocList){
            if(DocMandateType.MANDATE.value() == model.getMandateType()){
                mandatoryDocView = new CheckMandatoryDocView();
                mandatoryDocView.setKey(model.getEcmDocType());
                mandatoryDocView.setDocumentType(model.getEcmDocTypeDesc());
                mandatoryDocView.setComplete(model.getCompleted());
                mandatoryDocView.setIncomplete(Util.isTrue(model.getReasonIncomplete()));
                mandatoryDocView.setIncomplete(Util.isTrue(model.getReasonIndistinct()));
                mandatoryDocView.setIncorrect(Util.isTrue(model.getReasonIncorrect()));
                mandatoryDocView.setExpire(Util.isTrue(model.getReasonExpire()));
                mandatoryDocView.setRemark(model.getRemark());
                mandatoryDocView.setBRMSDocumentTypeList(checkMandateDocBRMSTransform.transformToView(model.getMandateDocBRMSList()));
                mandatoryDocView.setOwnewList(checkMandateDocCustTransform.transformToView(model.getMandateDocCustList()));
                mandatoryDocView.setFileNameViewList(checkMandateDocFileNameTransform.transformToView(model.getMandateDocFileNameList()));
                mandatoryDocumentsList.add(mandatoryDocView);
            } else if(DocMandateType.OPTIONAL.value() == model.getMandateType()){
                optionalDocView = new CheckOptionalDocView();
                optionalDocView.setKey(model.getEcmDocType());
                optionalDocView.setDocumentType(model.getEcmDocTypeDesc());
                optionalDocView.setComplete(model.getCompleted());
                optionalDocView.setIncomplete(Util.isTrue(model.getReasonIncomplete()));
                optionalDocView.setIncomplete(Util.isTrue(model.getReasonIndistinct()));
                optionalDocView.setIncorrect(Util.isTrue(model.getReasonIncorrect()));
                optionalDocView.setExpire(Util.isTrue(model.getReasonExpire()));
                optionalDocView.setRemark(model.getRemark());
                optionalDocView.setBRMSDocumentTypeList(checkMandateDocBRMSTransform.transformToView(model.getMandateDocBRMSList()));
                optionalDocView.setOwnewList(checkMandateDocCustTransform.transformToView(model.getMandateDocCustList()));
                optionalDocView.setFileNameViewList(checkMandateDocFileNameTransform.transformToView(model.getMandateDocFileNameList()));
                optionalDocumentsList.add(optionalDocView);
            } else {
                otherDocView = new CheckOtherDocView();
                otherDocView.setKey(model.getEcmDocType());
                otherDocView.setDocumentType(model.getEcmDocTypeDesc());
                otherDocView.setComplete(model.getCompleted());
                otherDocView.setIncomplete(Util.isTrue(model.getReasonIncomplete()));
                otherDocView.setIncomplete(Util.isTrue(model.getReasonIndistinct()));
                otherDocView.setIncorrect(Util.isTrue(model.getReasonIncorrect()));
                otherDocView.setExpire(Util.isTrue(model.getReasonExpire()));
                otherDocView.setRemark(model.getRemark());
                otherDocView.setBRMSDocumentTypeList(checkMandateDocBRMSTransform.transformToView(model.getMandateDocBRMSList()));
                otherDocView.setOwnewList(checkMandateDocCustTransform.transformToView(model.getMandateDocCustList()));
                otherDocView.setFileNameViewList(checkMandateDocFileNameTransform.transformToView(model.getMandateDocFileNameList()));
                otherDocumentsList.add(otherDocView);
            }
        }

        view.setMandatoryDocumentsList(mandatoryDocumentsList);
        log.debug("-- MandatoryDocumentsList.size()[{}]", view.getMandatoryDocumentsList().size());
        view.setOptionalDocumentsList(optionalDocumentsList);
        log.debug("-- OptionalDocumentsList.size()[{}]", view.getOptionalDocumentsList().size());
        view.setOtherDocumentsList(otherDocumentsList);
        log.debug("-- OtherDocumentsList.size()[{}]", view.getOtherDocumentsList().size());
        return view;
    }
    public List<MandateDoc> transformToModel(final CheckMandateDocView checkMandateDocView, final WorkCase workCase, final WorkCasePrescreen workCasePrescreen, final Role role){
        List<MandateDoc> mandateDocList = new ArrayList<MandateDoc>();
        MandateDoc model = null;

        mandatoryDocumentsList = Util.safetyList(checkMandateDocView.getMandatoryDocumentsList());
        for(CheckMandatoryDocView view : mandatoryDocumentsList){
            model = new MandateDoc();
            log.debug("-- [NEW]CheckMandatoryDocView Created");
            model.setWorkCase(workCase);
            model.setWorkCasePrescreen(workCasePrescreen);
            model.setRole(role);
            model.setEcmDocType(view.getKey());
            model.setEcmDocTypeDesc(view.getDocumentType());
            model.setMandateType(DocMandateType.MANDATE.value());
            model.setCompleted(view.getComplete());
            model.setRemark(view.getRemark());
            model.setReasonIncomplete(Util.isTrue(view.isIncomplete()));
            model.setReasonIndistinct(Util.isTrue(view.isIndistinct()));
            model.setReasonIncorrect(Util.isTrue(view.isIncorrect()));
            model.setReasonExpire(Util.isTrue(view.isExpire()));
            model.setMandateDocBRMSList(checkMandateDocBRMSTransform.transformToModel(model,  Util.safetyList(view.getBRMSDocumentTypeList())));
            model.setMandateDocCustList(checkMandateDocCustTransform.transformToModel(model, Util.safetyList(view.getOwnewList())));
            model.setMandateDocFileNameList(checkMandateDocFileNameTransform.transformToModel(model, Util.safetyList(view.getFileNameViewList())));
            mandateDocList.add(model);
        }

        optionalDocumentsList = Util.safetyList(checkMandateDocView.getOptionalDocumentsList());
        for(CheckOptionalDocView view : optionalDocumentsList){
            model = new MandateDoc();
            log.debug("-- [NEW]CheckOptionalDocView Created");
            model.setWorkCase(workCase);
            model.setRole(role);
            model.setEcmDocType(view.getKey());
            model.setEcmDocTypeDesc(view.getDocumentType());
            model.setMandateType(DocMandateType.OPTIONAL.value());
            model.setCompleted(view.getComplete());
            model.setRemark(view.getRemark());
            model.setReasonIncomplete(Util.isTrue(view.isIncomplete()));
            model.setReasonIndistinct(Util.isTrue(view.isIndistinct()));
            model.setReasonIncorrect(Util.isTrue(view.isIncorrect()));
            model.setReasonExpire(Util.isTrue(view.isExpire()));
            model.setMandateDocBRMSList(checkMandateDocBRMSTransform.transformToModel(model, Util.safetyList(view.getBRMSDocumentTypeList())));
            model.setMandateDocCustList(checkMandateDocCustTransform.transformToModel(model, Util.safetyList(view.getOwnewList())));
            model.setMandateDocFileNameList(checkMandateDocFileNameTransform.transformToModel(model, Util.safetyList(view.getFileNameViewList())));
            mandateDocList.add(model);
        }

        otherDocumentsList = Util.safetyList(checkMandateDocView.getOtherDocumentsList());
        for(CheckOtherDocView view : otherDocumentsList){
            model = new MandateDoc();
            log.debug("-- [NEW]CheckOtherDocView Created");
            model.setWorkCase(workCase);
            model.setRole(role);
            model.setEcmDocType(view.getKey());
            model.setEcmDocTypeDesc(view.getDocumentType());
            model.setMandateType(DocMandateType.OTHER.value());
            model.setCompleted(view.getComplete());
            model.setRemark(view.getRemark());
            model.setReasonIncomplete(Util.isTrue(view.isIncomplete()));
            model.setReasonIndistinct(Util.isTrue(view.isIndistinct()));
            model.setReasonIncorrect(Util.isTrue(view.isIncorrect()));
            model.setReasonExpire(Util.isTrue(view.isExpire()));
            model.setMandateDocBRMSList(checkMandateDocBRMSTransform.transformToModel(model, Util.safetyList(view.getBRMSDocumentTypeList())));
            model.setMandateDocCustList(checkMandateDocCustTransform.transformToModel(model, Util.safetyList(view.getOwnewList())));
            model.setMandateDocFileNameList(checkMandateDocFileNameTransform.transformToModel(model, Util.safetyList(view.getFileNameViewList())));
            mandateDocList.add(model);
        }
        return mandateDocList;
    }

    //BRMS and ECM
    public CheckMandatoryDocView transformToCheckMandatoryDocView(final String key, final MandateDocView mandateDocView, final List<ECMDetail> ecmDetailList, final int complete, final String userToken){
        log.debug("-- transformToCheckMandatoryDocView(MandateDocView.EcmDocTypeId[{}], complete[{}])", mandateDocView.getEcmDocTypeId(), complete);
        checkMandatoryDocView = new CheckMandatoryDocView();
        boolean flag = false;

        checkMandatoryDocView.setKey(key);

        //Checking Document Type
        if(!Util.isNull(mandateDocView.getEcmDocTypeDesc()) && !Util.isZero(mandateDocView.getEcmDocTypeDesc().length())){
            log.debug("-- MandateDocView.EcmDocTypeDesc is not null");
            log.debug("-- Document Type is {}", mandateDocView.getEcmDocTypeDesc());
            checkMandatoryDocView.setDocumentType(mandateDocView.getEcmDocTypeDesc());
        } else {
            flag = true;
        }
        log.debug("-- CheckMandatoryDocView.DocumentType[{}]", checkMandatoryDocView.getDocumentType());

        //Checking BRMS Document Type
        List<MandateDocBRMSView> BRMSDocumentTypeList = Util.safetyList(checkMandateDocBRMSTransform.transformStringListToView(mandateDocView.getBrmsDescList()));
        for(MandateDocBRMSView mandateDocBRMSView : BRMSDocumentTypeList){
            log.debug("-- MandateDocBRMSView.BRMSDocType[{}]", mandateDocBRMSView.getBRMSDocType());
        }
        checkMandatoryDocView.setBRMSDocumentTypeList(BRMSDocumentTypeList);

        //Checking Owners
        List<MandateDocCustView> ownewList = checkMandateDocCustTransform.transformCustomerListToView(Util.safetyList(mandateDocView.getCustomerInfoSimpleViewList()));
        for(MandateDocCustView mandateDocCustView : ownewList){
            log.debug("-- MandateDocCustView.CustName[{}]", mandateDocCustView.getCustName());
        }
        checkMandatoryDocView.setOwnewList(ownewList);


        List<MandateDocFileNameView> fileNameViewList = new ArrayList<MandateDocFileNameView>();
        MandateDocFileNameView checkMandatoryDocFileNameView = null;
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

            //File Name
            checkMandatoryDocFileNameView = checkMandateDocFileNameTransform.transformToView(ecmDetail.getOrgFileName(), getURLByFNId(ecmDetail.getFnDocId(), userToken));
            fileNameViewList.add(checkMandatoryDocFileNameView);
        }
        for(MandateDocFileNameView mandateDocFileNameView : fileNameViewList){
            log.debug("-- MandateDocFileNameView.FileName[{}]", mandateDocFileNameView.getFileName());
            log.debug("-- MandateDocFileNameView.URL[{}]", mandateDocFileNameView.getUrl());
        }
        checkMandatoryDocView.setFileNameViewList(fileNameViewList);

        //is Complete
        checkMandatoryDocView.setComplete(complete);
        log.debug("-- CheckOtherDocView.Complete[{}]", checkMandatoryDocView.getComplete());
        return checkMandatoryDocView;
    }
    public CheckOptionalDocView transformToCheckOptionalDocView(final String key, final MandateDocView mandateDocView, final List<ECMDetail> ecmDetailList, final int complete, final String userToken ){
        log.debug("-- transformToCheckOptionalDocView(MandateDocView.EcmDocTypeId[{}], complete[{}])", mandateDocView.getEcmDocTypeId(), complete);
        checkOptionalDocView = new CheckOptionalDocView();
        boolean flag = false;

        checkOptionalDocView.setKey(key);

        //Checking Document Type
        if(!Util.isNull(mandateDocView.getEcmDocTypeDesc()) && !Util.isZero(mandateDocView.getEcmDocTypeDesc().length())){
            log.debug("-- MandateDocView.EcmDocTypeDesc is not null");
            log.debug("-- Document Type is {}", mandateDocView.getEcmDocTypeDesc());
            checkOptionalDocView.setDocumentType(mandateDocView.getEcmDocTypeDesc());
        } else {
            flag = true;
        }
        log.debug("-- CheckMandatoryDocView.DocumentType[{}]", checkOptionalDocView.getDocumentType());

        //Checking BRMS Document Type
        List<MandateDocBRMSView> BRMSDocumentTypeList = Util.safetyList(checkMandateDocBRMSTransform.transformStringListToView(mandateDocView.getBrmsDescList()));
        for(MandateDocBRMSView mandateDocBRMSView : BRMSDocumentTypeList){
            log.debug("-- MandateDocBRMSView.BRMSDocType[{}]", mandateDocBRMSView.getBRMSDocType());
        }
        checkOptionalDocView.setBRMSDocumentTypeList(BRMSDocumentTypeList);

        //Checking Owners
        List<MandateDocCustView> ownewList = checkMandateDocCustTransform.transformCustomerListToView(Util.safetyList(mandateDocView.getCustomerInfoSimpleViewList()));
        for(MandateDocCustView mandateDocCustView : ownewList){
            log.debug("-- MandateDocCustView.CustName[{}]", mandateDocCustView.getCustName());
        }
        checkOptionalDocView.setOwnewList(ownewList);


        List<MandateDocFileNameView> fileNameViewList = new ArrayList<MandateDocFileNameView>();
        MandateDocFileNameView checkMandatoryDocFileNameView = null;
        for(ECMDetail ecmDetail : ecmDetailList){
            if(flag){
                if(!Util.isNull(ecmDetail.getTypeNameTH()) && !Util.isZero(ecmDetail.getTypeNameTH().length())){
                    log.debug("-- EcmDetail.TypeNameTH is not null");
                    log.debug("-- Document Type is {}", ecmDetail.getTypeNameTH());
                    checkOptionalDocView.setDocumentType(ecmDetail.getTypeNameTH());
                    flag = false;
                } else {
                    log.debug("-- EcmDetail.TypeNameTH is null");
                    log.debug("-- Document Type is {}", "Empty");
                    checkOptionalDocView.setDocumentType("");
                    flag = true;
                }
            }

            //File Name
            checkMandatoryDocFileNameView = checkMandateDocFileNameTransform.transformToView(ecmDetail.getOrgFileName(), getURLByFNId(ecmDetail.getFnDocId(), userToken));
            fileNameViewList.add(checkMandatoryDocFileNameView);
        }
        for(MandateDocFileNameView mandateDocFileNameView : fileNameViewList){
            log.debug("-- MandateDocFileNameView.FileName[{}]", mandateDocFileNameView.getFileName());
            log.debug("-- MandateDocFileNameView.URL[{}]", mandateDocFileNameView.getUrl());
        }
        checkOptionalDocView.setFileNameViewList(fileNameViewList);

        //is Complete
        checkOptionalDocView.setComplete(complete);
        log.debug("-- CheckOtherDocView.Complete[{}]", checkOptionalDocView.getComplete());
        return checkOptionalDocView;
    }
    public CheckOtherDocView transformToCheckOtherDocView(final String key, final MandateDocView mandateDocView, final List<ECMDetail> ecmDetailList, final int complete, final String userToken ){
        log.debug("-- transformToCheckOtherDocView(MandateDocView.EcmDocTypeId[{}], complete[{}])", mandateDocView.getEcmDocTypeId(), complete);
        checkOtherDocView = new CheckOtherDocView();
        boolean flag = false;

        checkOtherDocView.setKey(key);

        //Checking Document Type
        if(!Util.isNull(mandateDocView.getEcmDocTypeDesc()) && !Util.isZero(mandateDocView.getEcmDocTypeDesc().length())){
            log.debug("-- MandateDocView.EcmDocTypeDesc is not null");
            log.debug("-- Document Type is {}", mandateDocView.getEcmDocTypeDesc());
            checkOtherDocView.setDocumentType(mandateDocView.getEcmDocTypeDesc());
        } else {
            flag = true;
        }
        log.debug("-- CheckMandatoryDocView.DocumentType[{}]", checkOtherDocView.getDocumentType());

        //Checking BRMS Document Type
        List<MandateDocBRMSView> BRMSDocumentTypeList = Util.safetyList(checkMandateDocBRMSTransform.transformStringListToView(mandateDocView.getBrmsDescList()));
        for(MandateDocBRMSView mandateDocBRMSView : BRMSDocumentTypeList){
            log.debug("-- MandateDocBRMSView.BRMSDocType[{}]", mandateDocBRMSView.getBRMSDocType());
        }
        checkOtherDocView.setBRMSDocumentTypeList(BRMSDocumentTypeList);

        //Checking Owners
        List<MandateDocCustView> ownewList = checkMandateDocCustTransform.transformCustomerListToView(Util.safetyList(mandateDocView.getCustomerInfoSimpleViewList()));
        for(MandateDocCustView mandateDocCustView : ownewList){
            log.debug("-- MandateDocCustView.CustName[{}]", mandateDocCustView.getCustName());
        }
        checkOtherDocView.setOwnewList(ownewList);


        List<MandateDocFileNameView> fileNameViewList = new ArrayList<MandateDocFileNameView>();
        MandateDocFileNameView checkMandatoryDocFileNameView = null;
        for(ECMDetail ecmDetail : ecmDetailList){
            if(flag){
                if(!Util.isNull(ecmDetail.getTypeNameTH()) && !Util.isZero(ecmDetail.getTypeNameTH().length())){
                    log.debug("-- EcmDetail.TypeNameTH is not null");
                    log.debug("-- Document Type is {}", ecmDetail.getTypeNameTH());
                    checkOtherDocView.setDocumentType(ecmDetail.getTypeNameTH());
                    flag = false;
                } else {
                    log.debug("-- EcmDetail.TypeNameTH is null");
                    log.debug("-- Document Type is {}", "Empty");
                    checkOtherDocView.setDocumentType("");
                    flag = true;
                }
            }

            //File Name
            checkMandatoryDocFileNameView = checkMandateDocFileNameTransform.transformToView(ecmDetail.getOrgFileName(), getURLByFNId(ecmDetail.getFnDocId(), userToken));
            fileNameViewList.add(checkMandatoryDocFileNameView);
        }
        for(MandateDocFileNameView mandateDocFileNameView : fileNameViewList){
            log.debug("-- MandateDocFileNameView.FileName[{}]", mandateDocFileNameView.getFileName());
            log.debug("-- MandateDocFileNameView.URL[{}]", mandateDocFileNameView.getUrl());
        }
        checkOtherDocView.setFileNameViewList(fileNameViewList);

        //is Complete
        checkOtherDocView.setComplete(complete);
        log.debug("-- CheckOtherDocView.Complete[{}]", checkOtherDocView.getComplete());
        return checkOtherDocView;
    }

    //BRMS
    public CheckMandatoryDocView transformToCheckMandatoryDocView(final String key, final MandateDocView mandateDocView, final int complete){
        log.debug("-- transformToCheckMandatoryDocView(MandateDocView.EcmDocTypeId[{}], complete[{}])", mandateDocView.getEcmDocTypeId(), complete);
        checkMandatoryDocView = new CheckMandatoryDocView();

        checkMandatoryDocView.setKey(key);

        //Checking Document Type
        if(!Util.isNull(mandateDocView.getEcmDocTypeDesc()) && !Util.isZero(mandateDocView.getEcmDocTypeDesc().length())){
            log.debug("-- MandateDocView.EcmDocTypeDesc is not null");
            log.debug("-- Document Type is {}", mandateDocView.getEcmDocTypeDesc());
            checkMandatoryDocView.setDocumentType(mandateDocView.getEcmDocTypeDesc());
        } else {
            checkMandatoryDocView.setDocumentType("");
        }
        log.debug("-- CheckMandatoryDocView.DocumentType[{}]", checkMandatoryDocView.getDocumentType());

        //Checking BRMS Document Type
        List<MandateDocBRMSView> BRMSDocumentTypeList = Util.safetyList(checkMandateDocBRMSTransform.transformStringListToView(mandateDocView.getBrmsDescList()));
        for(MandateDocBRMSView mandateDocBRMSView : BRMSDocumentTypeList){
            log.debug("-- MandateDocBRMSView.BRMSDocType[{}]", mandateDocBRMSView.getBRMSDocType());
        }
        checkMandatoryDocView.setBRMSDocumentTypeList(BRMSDocumentTypeList);

        //Checking Owners
        List<MandateDocCustView> ownewList = checkMandateDocCustTransform.transformCustomerListToView(Util.safetyList(mandateDocView.getCustomerInfoSimpleViewList()));
        for(MandateDocCustView mandateDocCustView : ownewList){
            log.debug("-- MandateDocCustView.CustName[{}]", mandateDocCustView.getCustName());
        }
        checkMandatoryDocView.setOwnewList(ownewList);

        //is Complete
        checkMandatoryDocView.setComplete(complete);
        log.debug("-- CheckOtherDocView.Complete[{}]", checkMandatoryDocView.getComplete());
        return checkMandatoryDocView;
    }
    public CheckOptionalDocView transformToCheckOptionalDocView(final String key, final MandateDocView mandateDocView, final int complete){
        log.debug("-- transformToCheckOptionalDocView(MandateDocView.EcmDocTypeId[{}], complete[{}])", mandateDocView.getEcmDocTypeId(), complete);
        checkOptionalDocView = new CheckOptionalDocView();
        log.debug("-- [New]CheckOtherDocView Created");

        checkOptionalDocView.setKey(key);

        //Checking Document Type
        if(!Util.isNull(mandateDocView.getEcmDocTypeDesc()) && !Util.isZero(mandateDocView.getEcmDocTypeDesc().length())){
            log.debug("-- MandateDocView.EcmDocTypeDesc is not null");
            log.debug("-- Document Type is {}", mandateDocView.getEcmDocTypeDesc());
            checkOptionalDocView.setDocumentType(mandateDocView.getEcmDocTypeDesc());
        } else {
            checkOptionalDocView.setDocumentType("");
        }
        log.debug("-- CheckOtherDocView.DocumentType[{}]", checkOptionalDocView.getDocumentType());

        //Checking BRMS Document Type
        List<MandateDocBRMSView> BRMSDocumentTypeList = Util.safetyList(checkMandateDocBRMSTransform.transformStringListToView(mandateDocView.getBrmsDescList()));
        for(MandateDocBRMSView mandateDocBRMSView : BRMSDocumentTypeList){
            log.debug("-- MandateDocBRMSView.BRMSDocType[{}]", mandateDocBRMSView.getBRMSDocType());
        }
        checkOptionalDocView.setBRMSDocumentTypeList(BRMSDocumentTypeList);

        //Checking Owners
        List<MandateDocCustView> ownewList = checkMandateDocCustTransform.transformCustomerListToView(Util.safetyList(mandateDocView.getCustomerInfoSimpleViewList()));
        for(MandateDocCustView mandateDocCustView : ownewList){
            log.debug("-- MandateDocCustView.CustName[{}]", mandateDocCustView.getCustName());
        }
        checkOptionalDocView.setOwnewList(ownewList);

        //is Complete
        checkOptionalDocView.setComplete(complete);
        log.debug("-- CheckOtherDocView.Complete[{}]", checkOptionalDocView.getComplete());

        return checkOptionalDocView;
    }
    public CheckOtherDocView transformToCheckOtherDocView(final String key, final MandateDocView mandateDocView, final int complete){
        log.debug("-- transformToCheckOtherDocView(MandateDocView.EcmDocTypeId[{}], complete[{}])", mandateDocView.getEcmDocTypeId(), complete);
        checkOtherDocView = new CheckOtherDocView();
        log.debug("-- [New]CheckOtherDocView Created");

        checkOtherDocView.setKey(key);

        //Checking Document Type
        if(!Util.isNull(mandateDocView.getEcmDocTypeDesc()) && !Util.isZero(mandateDocView.getEcmDocTypeDesc().length())){
            log.debug("-- MandateDocView.EcmDocTypeDesc is not null");
            log.debug("-- Document Type is {}", mandateDocView.getEcmDocTypeDesc());
            checkOtherDocView.setDocumentType(mandateDocView.getEcmDocTypeDesc());
        } else {
            checkOtherDocView.setDocumentType("");
        }
        log.debug("-- CheckOtherDocView.DocumentType[{}]", checkOtherDocView.getDocumentType());

        //Checking BRMS Document Type
        List<MandateDocBRMSView> BRMSDocumentTypeList = Util.safetyList(checkMandateDocBRMSTransform.transformStringListToView(mandateDocView.getBrmsDescList()));
        for(MandateDocBRMSView mandateDocBRMSView : BRMSDocumentTypeList){
            log.debug("-- MandateDocBRMSView.BRMSDocType[{}]", mandateDocBRMSView.getBRMSDocType());
        }
        checkOtherDocView.setBRMSDocumentTypeList(BRMSDocumentTypeList);

        //Checking Owners
        List<MandateDocCustView> ownewList = checkMandateDocCustTransform.transformCustomerListToView(Util.safetyList(mandateDocView.getCustomerInfoSimpleViewList()));
        for(MandateDocCustView mandateDocCustView : ownewList){
            log.debug("-- MandateDocCustView.CustName[{}]", mandateDocCustView.getCustName());
        }
        checkOtherDocView.setOwnewList(ownewList);

        //is Complete
        checkOtherDocView.setComplete(complete);
        log.debug("-- CheckOtherDocView.Complete[{}]", checkOtherDocView.getComplete());

        return checkOtherDocView;
    }

    //ECM
    public CheckOtherDocView transformToCheckOtherDocView(final String key, final List<ECMDetail> ecmDetailList, final int complete, final String userToken ){
        checkOtherDocView = new CheckOtherDocView();

        checkOtherDocView.setKey(key);

        List<MandateDocFileNameView> fileNameViewList = new ArrayList<MandateDocFileNameView>();
        MandateDocFileNameView checkMandatoryDocFileNameView = null;
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
            log.debug("-- CheckOtherDocView.DocumentType[{}]", checkOtherDocView.getDocumentType());

            //File Name
            checkMandatoryDocFileNameView = checkMandateDocFileNameTransform.transformToView(ecmDetail.getOrgFileName(), getURLByFNId(ecmDetail.getFnDocId(), userToken));
            fileNameViewList.add(checkMandatoryDocFileNameView);
        }
        for(MandateDocFileNameView mandateDocFileNameView : fileNameViewList){
            log.debug("-- MandateDocFileNameView.FileName[{}]", mandateDocFileNameView.getFileName());
            log.debug("-- MandateDocFileNameView.URL[{}]", mandateDocFileNameView.getUrl());
        }
        checkOtherDocView.setFileNameViewList(fileNameViewList);

        //is Complete
        checkOtherDocView.setComplete(complete);
        log.debug("-- CheckOtherDocView.Complete[{}]", checkOtherDocView.getComplete());
        return checkOtherDocView;
    }

    //ECM AND MANDATEDOC
    public CheckMandatoryDocView transformToCheckMandatoryDocView(final String key, final MandateDoc mandateDoc, final List<ECMDetail> ecmDetailList, final int complete, final String userToken ){
        checkMandatoryDocView = new CheckMandatoryDocView();

        checkMandatoryDocView.setKey(key);

        List<MandateDocFileNameView> fileNameViewList = new ArrayList<MandateDocFileNameView>();
        MandateDocFileNameView checkMandatoryDocFileNameView = null;
        boolean flag = true;
        for(ECMDetail ecmDetail : ecmDetailList){
            //Checking Document Type
            if(flag){
                if(!Util.isNull(ecmDetail.getTypeNameTH()) && !Util.isZero(ecmDetail.getTypeNameTH().length())){
                    log.debug("-- EcmDetail.TypeNameTH is not null");
                    log.debug("-- Document Type is {}", ecmDetail.getTypeNameTH());
                    checkMandatoryDocView.setDocumentType(ecmDetail.getTypeNameTH());
                } else {
                    log.debug("-- EcmDetail.TypeNameTH is null");
                    log.debug("-- Document Type is {}", "Empty");
                    checkMandatoryDocView.setDocumentType("");
                }
                flag = false;
            }
            log.debug("-- CheckOtherDocView.DocumentType[{}]", checkMandatoryDocView.getDocumentType());

            //File Name
            checkMandatoryDocFileNameView = checkMandateDocFileNameTransform.transformToView(ecmDetail.getOrgFileName(), getURLByFNId(ecmDetail.getFnDocId(), userToken));
            fileNameViewList.add(checkMandatoryDocFileNameView);
        }

        List<MandateDocBRMSView> BRMSDocumentTypeList = new ArrayList<MandateDocBRMSView>();
        MandateDocBRMSView mandateDocBRMSView = null;
        List<MandateDocBRMS> mandateDocBRMSList = Util.safetyList(mandateDoc.getMandateDocBRMSList());
        for(MandateDocBRMS mandateDocBRMS : mandateDocBRMSList){
            mandateDocBRMSView = new MandateDocBRMSView();
            mandateDocBRMSView.setId(mandateDocBRMS.getId());
            mandateDocBRMSView.setBRMSDocType(mandateDocBRMS.getBRMSDocType());
            BRMSDocumentTypeList.add(mandateDocBRMSView);
        }
        log.debug("-- BRMSDocumentTypeList.size()[{}]", BRMSDocumentTypeList.size());
        checkMandatoryDocView.setBRMSDocumentTypeList(BRMSDocumentTypeList);

        List<MandateDocCustView> ownerList = new ArrayList<MandateDocCustView>();
        MandateDocCustView mandateDocCustView = null;
        List<MandateDocCust> mandateDocCustList = Util.safetyList(mandateDoc.getMandateDocCustList());
        for(MandateDocCust mandateDocCust : mandateDocCustList){
            mandateDocCustView = new MandateDocCustView();
            mandateDocCustView.setId(mandateDocCust.getId());
            mandateDocCustView.setCustName(mandateDocCust.getCustName());
            ownerList.add(mandateDocCustView);
        }
        log.debug("-- OwnerList.size()[{}]", ownerList.size());
        checkMandatoryDocView.setOwnewList(ownerList);

        for(MandateDocFileNameView mandateDocFileNameView : fileNameViewList){
            log.debug("-- MandateDocFileNameView.FileName[{}]", mandateDocFileNameView.getFileName());
            log.debug("-- MandateDocFileNameView.URL[{}]", mandateDocFileNameView.getUrl());
        }
        checkMandatoryDocView.setFileNameViewList(fileNameViewList);

        if(complete == 1){
            checkMandatoryDocView.readOnly();
            checkMandatoryDocView.readOnlyRemarkAndReason();
        }

        checkMandatoryDocView.setIncomplete(Util.isTrue(mandateDoc.getReasonIncomplete()));
        checkMandatoryDocView.setIndistinct(Util.isTrue(mandateDoc.getReasonIndistinct()));
        checkMandatoryDocView.setIncorrect(Util.isTrue(mandateDoc.getReasonIncorrect()));
        checkMandatoryDocView.setExpire(Util.isTrue(mandateDoc.getReasonExpire()));
        checkMandatoryDocView.setRemark(mandateDoc.getRemark());

        //is Complete
        checkMandatoryDocView.setComplete(complete);
        log.debug("-- CheckMandatoryDocView.Complete[{}]", checkMandatoryDocView.getComplete());
        return checkMandatoryDocView;
    }
    public CheckOptionalDocView transformToCheckOptionalDocView(final String key, final MandateDoc mandateDoc, final List<ECMDetail> ecmDetailList, final int complete, final String userToken ){
        checkOptionalDocView = new CheckOptionalDocView();

        checkOptionalDocView.setKey(key);

        List<MandateDocFileNameView> fileNameViewList = new ArrayList<MandateDocFileNameView>();
        MandateDocFileNameView checkMandatoryDocFileNameView = null;
        boolean flag = true;
        for(ECMDetail ecmDetail : ecmDetailList){
            //Checking Document Type
            if(flag){
                if(!Util.isNull(ecmDetail.getTypeNameTH()) && !Util.isZero(ecmDetail.getTypeNameTH().length())){
                    log.debug("-- EcmDetail.TypeNameTH is not null");
                    log.debug("-- Document Type is {}", ecmDetail.getTypeNameTH());
                    checkOptionalDocView.setDocumentType(ecmDetail.getTypeNameTH());
                } else {
                    log.debug("-- EcmDetail.TypeNameTH is null");
                    log.debug("-- Document Type is {}", "Empty");
                    checkOptionalDocView.setDocumentType("");
                }
                flag = false;
            }
            log.debug("-- CheckOtherDocView.DocumentType[{}]", checkOptionalDocView.getDocumentType());

            //File Name
            checkMandatoryDocFileNameView = checkMandateDocFileNameTransform.transformToView(ecmDetail.getOrgFileName(), getURLByFNId(ecmDetail.getFnDocId(), userToken));
            fileNameViewList.add(checkMandatoryDocFileNameView);
        }

        List<MandateDocBRMSView> BRMSDocumentTypeList = new ArrayList<MandateDocBRMSView>();
        MandateDocBRMSView mandateDocBRMSView = null;
        List<MandateDocBRMS> mandateDocBRMSList = Util.safetyList(mandateDoc.getMandateDocBRMSList());
        for(MandateDocBRMS mandateDocBRMS : mandateDocBRMSList){
            mandateDocBRMSView = new MandateDocBRMSView();
            mandateDocBRMSView.setId(mandateDocBRMS.getId());
            mandateDocBRMSView.setBRMSDocType(mandateDocBRMS.getBRMSDocType());
            BRMSDocumentTypeList.add(mandateDocBRMSView);
        }
        log.debug("-- BRMSDocumentTypeList.size()[{}]", BRMSDocumentTypeList.size());
        checkOptionalDocView.setBRMSDocumentTypeList(BRMSDocumentTypeList);

        List<MandateDocCustView> ownerList = new ArrayList<MandateDocCustView>();
        MandateDocCustView mandateDocCustView = null;
        List<MandateDocCust> mandateDocCustList = Util.safetyList(mandateDoc.getMandateDocCustList());
        for(MandateDocCust mandateDocCust : mandateDocCustList){
            mandateDocCustView = new MandateDocCustView();
            mandateDocCustView.setId(mandateDocCust.getId());
            mandateDocCustView.setCustName(mandateDocCust.getCustName());
            ownerList.add(mandateDocCustView);
        }
        log.debug("-- OwnerList.size()[{}]", ownerList.size());
        checkOptionalDocView.setOwnewList(ownerList);

        for(MandateDocFileNameView mandateDocFileNameView : fileNameViewList){
            log.debug("-- MandateDocFileNameView.FileName[{}]", mandateDocFileNameView.getFileName());
            log.debug("-- MandateDocFileNameView.URL[{}]", mandateDocFileNameView.getUrl());
        }
        checkOptionalDocView.setFileNameViewList(fileNameViewList);

        if(complete == 1){
            checkOptionalDocView.readOnly();
            checkOptionalDocView.readOnlyRemarkAndReason();
        }

        checkOptionalDocView.setIncomplete(Util.isTrue(mandateDoc.getReasonIncomplete()));
        checkOptionalDocView.setIndistinct(Util.isTrue(mandateDoc.getReasonIndistinct()));
        checkOptionalDocView.setIncorrect(Util.isTrue(mandateDoc.getReasonIncorrect()));
        checkOptionalDocView.setExpire(Util.isTrue(mandateDoc.getReasonExpire()));
        checkOptionalDocView.setRemark(mandateDoc.getRemark());

        //is Complete
        checkOptionalDocView.setComplete(complete);
        log.debug("-- CheckOptionalDocView.Complete[{}]", checkOptionalDocView.getComplete());
        return checkOptionalDocView;
    }
    public CheckOtherDocView transformToCheckOtherDocView(final String key, final MandateDoc mandateDoc, final List<ECMDetail> ecmDetailList, final int complete, final String userToken ){
        checkOtherDocView = new CheckOtherDocView();

        checkOtherDocView.setKey(key);

        List<MandateDocFileNameView> fileNameViewList = new ArrayList<MandateDocFileNameView>();
        MandateDocFileNameView checkMandatoryDocFileNameView = null;
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
            log.debug("-- CheckOtherDocView.DocumentType[{}]", checkOtherDocView.getDocumentType());

            //File Name
            checkMandatoryDocFileNameView = checkMandateDocFileNameTransform.transformToView(ecmDetail.getOrgFileName(), getURLByFNId(ecmDetail.getFnDocId(), userToken));
            fileNameViewList.add(checkMandatoryDocFileNameView);
        }

        List<MandateDocBRMSView> BRMSDocumentTypeList = new ArrayList<MandateDocBRMSView>();
        MandateDocBRMSView mandateDocBRMSView = null;
        List<MandateDocBRMS> mandateDocBRMSList = Util.safetyList(mandateDoc.getMandateDocBRMSList());
        for(MandateDocBRMS mandateDocBRMS : mandateDocBRMSList){
            mandateDocBRMSView = new MandateDocBRMSView();
            mandateDocBRMSView.setId(mandateDocBRMS.getId());
            mandateDocBRMSView.setBRMSDocType(mandateDocBRMS.getBRMSDocType());
            BRMSDocumentTypeList.add(mandateDocBRMSView);
        }
        log.debug("-- BRMSDocumentTypeList.size()[{}]", BRMSDocumentTypeList.size());
        checkOtherDocView.setBRMSDocumentTypeList(BRMSDocumentTypeList);

        List<MandateDocCustView> ownerList = new ArrayList<MandateDocCustView>();
        MandateDocCustView mandateDocCustView = null;
        List<MandateDocCust> mandateDocCustList = Util.safetyList(mandateDoc.getMandateDocCustList());
        for(MandateDocCust mandateDocCust : mandateDocCustList){
            mandateDocCustView = new MandateDocCustView();
            mandateDocCustView.setId(mandateDocCust.getId());
            mandateDocCustView.setCustName(mandateDocCust.getCustName());
            ownerList.add(mandateDocCustView);
        }
        log.debug("-- OwnerList.size()[{}]", ownerList.size());
        checkOtherDocView.setOwnewList(ownerList);

        for(MandateDocFileNameView mandateDocFileNameView : fileNameViewList){
            log.debug("-- MandateDocFileNameView.FileName[{}]", mandateDocFileNameView.getFileName());
            log.debug("-- MandateDocFileNameView.URL[{}]", mandateDocFileNameView.getUrl());
        }
        checkOtherDocView.setFileNameViewList(fileNameViewList);

        //is Complete
        checkOtherDocView.setComplete(complete);

        if(complete == 1){
            checkOtherDocView.readOnly();
            checkOtherDocView.readOnlyRemarkAndReason();
        }

        checkOtherDocView.setIncomplete(Util.isTrue(mandateDoc.getReasonIncomplete()));
        checkOtherDocView.setIndistinct(Util.isTrue(mandateDoc.getReasonIndistinct()));
        checkOtherDocView.setIncorrect(Util.isTrue(mandateDoc.getReasonIncorrect()));
        checkOtherDocView.setExpire(Util.isTrue(mandateDoc.getReasonExpire()));
        checkOtherDocView.setRemark(mandateDoc.getRemark());

        log.debug("-- CheckOtherDocView.Complete[{}]", checkOtherDocView.getComplete());
        return checkOtherDocView;
    }




    //URL
    private String getURLByFNId(final String FNId, final String token){
        return address+"/getContent?objectStoreName="+objectStore+"&id="+FNId+"&objectType=document&ut=" + token;
    }

}
