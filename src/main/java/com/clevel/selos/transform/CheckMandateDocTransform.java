package com.clevel.selos.transform;

import com.clevel.selos.dao.master.CollateralTypeDAO;
import com.clevel.selos.dao.working.MandateDocDAO;
import com.clevel.selos.dao.working.WorkCaseDAO;
import com.clevel.selos.integration.NCB;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.integration.ecm.db.ECMDetail;
import com.clevel.selos.model.DocMandateType;
import com.clevel.selos.model.db.master.CollateralType;
import com.clevel.selos.model.db.master.Role;
import com.clevel.selos.model.db.working.MandateDoc;
import com.clevel.selos.model.db.working.WorkCase;
import com.clevel.selos.model.view.*;
import com.clevel.selos.system.Config;
import com.clevel.selos.util.Util;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class CheckMandateDocTransform extends Transform {
//    @Inject
//    @SELOS
//    private Logger log;
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
        init();
    }

    private void init(){
//        log.debug("-- init()");
        checkMandateDocView = null;
        mandatoryDocumentsList = null;
        optionalDocumentsList = null;
        otherDocumentsList = null;
    }

    public CheckMandateDocView transformToView(final MandateDoc mandateDoc){
        checkMandateDocView = new CheckMandateDocView();
        return checkMandateDocView;
    }

    public MandateDoc transformToModel(final CheckMandateDocView checkMandateDocView){
        mandateDoc = new MandateDoc();




        return mandateDoc;
    }

    public List<MandateDoc> transformToModel(final CheckMandateDocView checkMandateDocView, final long workCaseId, final Role role){
        List<MandateDoc> mandateDocList = new ArrayList<MandateDoc>();
        MandateDoc model = null;
        workCase = workCaseDAO.findById(workCaseId);

        mandatoryDocumentsList = Util.safetyList(checkMandateDocView.getMandatoryDocumentsList());
        for(CheckMandatoryDocView view : mandatoryDocumentsList){
            if(!Util.isZero(view.getId())){
                model = mandateDocDAO.findById(view.getId());
            } else {
                model = new MandateDoc();
            }
            model.setWorkCase(workCase);
            model.setRole(role);
            model.setEcmDocType(view.getDocumentType());
            model.setEcmDocTypeDesc("");//todo
            model.setMandateType(DocMandateType.MANDATE.value());
            model.setCompleted(view.getComplete());
            model.setRemark(view.getRemark());
            model.setReasonIncomplete(Util.isTrue(view.isIncomplete()));
            model.setReasonIndistinct(Util.isTrue(view.isIndistinct()));
            model.setReasonIncorrect(Util.isTrue(view.isIncorrect()));
            model.setReasonExpire(Util.isTrue(view.isExpire()));
            model.setMandateDocBRMSList(checkMandateDocBRMSTransform.transformToModel(view.getBRMSDocumentTypeList()));
            model.setMandateDocCustList(checkMandateDocCustTransform.transformToModel(view.getOwnewList()));
            model.setMandateDocFileNameList(checkMandateDocFileNameTransform.transformToModel(view.getFileNameViewList()));
            mandateDocList.add(model);
        }

        optionalDocumentsList = Util.safetyList(checkMandateDocView.getOptionalDocumentsList());
        for(CheckOptionalDocView view : optionalDocumentsList){
            if(!Util.isZero(view.getId())){
                model = mandateDocDAO.findById(view.getId());
            } else {
                model = new MandateDoc();
            }
            model.setWorkCase(workCase);
            model.setRole(role);
            model.setEcmDocType(view.getDocumentType());
            model.setEcmDocTypeDesc("");//todo
            model.setMandateType(DocMandateType.OPTIONAL.value());
            model.setCompleted(view.getComplete());
            model.setRemark(view.getRemark());
            model.setReasonIncomplete(Util.isTrue(view.isIncomplete()));
            model.setReasonIndistinct(Util.isTrue(view.isIndistinct()));
            model.setReasonIncorrect(Util.isTrue(view.isIncorrect()));
            model.setReasonExpire(Util.isTrue(view.isExpire()));
            model.setMandateDocBRMSList(checkMandateDocBRMSTransform.transformToModel(view.getBRMSDocumentTypeList()));
            model.setMandateDocCustList(checkMandateDocCustTransform.transformToModel(view.getOwnewList()));
            model.setMandateDocFileNameList(checkMandateDocFileNameTransform.transformToModel(view.getFileNameViewList()));
            mandateDocList.add(model);
        }

        otherDocumentsList = Util.safetyList(checkMandateDocView.getOtherDocumentsList());
        for(CheckOtherDocView view : otherDocumentsList){
            if(!Util.isZero(view.getId())){
                model = mandateDocDAO.findById(view.getId());
            } else {
                model = new MandateDoc();
            }
            model.setWorkCase(workCase);
            model.setRole(role);
            model.setEcmDocType(view.getDocumentType());
            model.setEcmDocTypeDesc("");//todo
            model.setMandateType(DocMandateType.OTHER.value());
            model.setCompleted(view.getComplete());
            model.setRemark(view.getRemark());
            model.setReasonIncomplete(Util.isTrue(view.isIncomplete()));
            model.setReasonIndistinct(Util.isTrue(view.isIndistinct()));
            model.setReasonIncorrect(Util.isTrue(view.isIncorrect()));
            model.setReasonExpire(Util.isTrue(view.isExpire()));
            model.setMandateDocBRMSList(checkMandateDocBRMSTransform.transformToModel(view.getBRMSDocumentTypeList()));
            model.setMandateDocCustList(checkMandateDocCustTransform.transformToModel(view.getOwnewList()));
            model.setMandateDocFileNameList(checkMandateDocFileNameTransform.transformToModel(view.getFileNameViewList()));
            mandateDocList.add(model);
        }


        return mandateDocList;
    }

     //TODO : Creating for ...ROLE[Select from the database]


    //BRMS and ECM
    public CheckMandatoryDocView transformToCheckMandatoryDocView(final MandateDocView mandateDocView, final List<ECMDetail> ecmDetailList, final int complete, final String userToken){
        checkMandatoryDocView = new CheckMandatoryDocView();
        boolean flag = false;
        //Checking Document Type
        if(!Util.isNull(mandateDocView.getEcmDocTypeDesc()) && !Util.isZero(mandateDocView.getEcmDocTypeDesc().length())){
//            log.debug("-- MandateDocView.EcmDocTypeDesc is not null");
//            log.debug("-- Document Type is {}", mandateDocView.getEcmDocTypeDesc());
            checkMandatoryDocView.setDocumentType(mandateDocView.getEcmDocTypeDesc());
        } else {
            flag = true;
        }

        //Checking BRMS Document Type
        List<MandateDocBRMSView> BRMSDocumentTypeList = Util.safetyList(checkMandateDocBRMSTransform.transformStringListToView(mandateDocView.getBrmsDescList()));
        checkMandatoryDocView.setBRMSDocumentTypeList(BRMSDocumentTypeList);

        //Checking Owners
        List<MandateDocCustView> ownewList = checkMandateDocCustTransform.transformCustomerListToView(Util.safetyList(mandateDocView.getCustomerInfoSimpleViewList()));
        checkMandatoryDocView.setOwnewList(ownewList);


        List<MandateDocFileNameView> fileNameViewList = new ArrayList<MandateDocFileNameView>();
        MandateDocFileNameView checkMandatoryDocFileNameView = null;
        for(ECMDetail ecmDetail : ecmDetailList){
            if(flag){
                if(!Util.isNull(ecmDetail.getTypeNameTH()) && !Util.isZero(ecmDetail.getTypeNameTH().length())){
//                    log.debug("-- EcmDetail.TypeNameTH is not null");
//                    log.debug("-- Document Type is {}", ecmDetail.getTypeNameTH());
                    checkMandatoryDocView.setDocumentType(ecmDetail.getTypeNameTH());
                    flag = false;
                } else {
//                    log.debug("-- EcmDetail.TypeNameTH is null");
//                    log.debug("-- Document Type is {}", "Empty");
                    checkMandatoryDocView.setDocumentType("");
                    flag = true;
                }
            }

            //File Name
            checkMandatoryDocFileNameView = checkMandateDocFileNameTransform.transformToView(ecmDetail.getOrgFileName(), getURLByFNId(ecmDetail.getFnDocId(), userToken));
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
//            log.debug("-- MandateDocView.EcmDocTypeDesc is not null");
//            log.debug("-- Document Type is {}", mandateDocView.getEcmDocTypeDesc());
            checkMandatoryDocView.setDocumentType(mandateDocView.getEcmDocTypeDesc());
        } else {
            checkMandatoryDocView.setDocumentType("");
        }

        //Checking BRMS Document Type
        List<MandateDocBRMSView> BRMSDocumentTypeList = Util.safetyList(checkMandateDocBRMSTransform.transformStringListToView(mandateDocView.getBrmsDescList()));
        checkMandatoryDocView.setBRMSDocumentTypeList(BRMSDocumentTypeList);

        //Checking Owners
        List<MandateDocCustView> ownewList = checkMandateDocCustTransform.transformCustomerListToView(Util.safetyList(mandateDocView.getCustomerInfoSimpleViewList()));
        checkMandatoryDocView.setOwnewList(ownewList);

        //is Complete
        checkMandatoryDocView.setComplete(complete);
        return checkMandatoryDocView;
    }
    public CheckOptionalDocView transformToCheckOptionalDocView(final MandateDocView mandateDocView, final int complete){
        checkOptionalDocView = new CheckOptionalDocView();
        //Checking Document Type
        if(!Util.isNull(mandateDocView.getEcmDocTypeDesc()) && !Util.isZero(mandateDocView.getEcmDocTypeDesc().length())){
//            log.debug("-- MandateDocView.EcmDocTypeDesc is not null");
//            log.debug("-- Document Type is {}", mandateDocView.getEcmDocTypeDesc());
            checkOptionalDocView.setDocumentType(mandateDocView.getEcmDocTypeDesc());
        } else {
            checkOptionalDocView.setDocumentType("");
        }

        //Checking BRMS Document Type
        List<MandateDocBRMSView> BRMSDocumentTypeList = Util.safetyList(checkMandateDocBRMSTransform.transformStringListToView(mandateDocView.getBrmsDescList()));
        checkMandatoryDocView.setBRMSDocumentTypeList(BRMSDocumentTypeList);

        //Checking Owners
        List<MandateDocCustView> ownewList = checkMandateDocCustTransform.transformCustomerListToView(Util.safetyList(mandateDocView.getCustomerInfoSimpleViewList()));
        checkMandatoryDocView.setOwnewList(ownewList);

        //is Complete
        checkOptionalDocView.setComplete(complete);
        return checkOptionalDocView;
    }
    public CheckOtherDocView transformToCheckOtherDocView(final MandateDocView mandateDocView, final int complete){
        checkOtherDocView = new CheckOtherDocView();
        //Checking Document Type
        if(!Util.isNull(mandateDocView.getEcmDocTypeDesc()) && !Util.isZero(mandateDocView.getEcmDocTypeDesc().length())){
//            log.debug("-- MandateDocView.EcmDocTypeDesc is not null");
//            log.debug("-- Document Type is {}", mandateDocView.getEcmDocTypeDesc());
            checkOtherDocView.setDocumentType(mandateDocView.getEcmDocTypeDesc());
        } else {
            checkOtherDocView.setDocumentType("");
        }

        //Checking BRMS Document Type
        List<MandateDocBRMSView> BRMSDocumentTypeList = Util.safetyList(checkMandateDocBRMSTransform.transformStringListToView(mandateDocView.getBrmsDescList()));
        checkMandatoryDocView.setBRMSDocumentTypeList(BRMSDocumentTypeList);

        //Checking Owners
        List<MandateDocCustView> ownewList = checkMandateDocCustTransform.transformCustomerListToView(Util.safetyList(mandateDocView.getCustomerInfoSimpleViewList()));
        checkMandatoryDocView.setOwnewList(ownewList);

        //is Complete
        checkOtherDocView.setComplete(complete);
        return checkOtherDocView;
    }

    //ECM
    public CheckOtherDocView transformToCheckOtherDocView(final List<ECMDetail> ecmDetailList, final int complete, final String userToken ){
        checkOtherDocView = new CheckOtherDocView();

        List<MandateDocFileNameView> fileNameViewList = new ArrayList<MandateDocFileNameView>();
        MandateDocFileNameView checkMandatoryDocFileNameView = null;
        boolean flag = true;
        for(ECMDetail ecmDetail : ecmDetailList){
            //Checking Document Type
            if(flag){
                if(!Util.isNull(ecmDetail.getTypeNameTH()) && !Util.isZero(ecmDetail.getTypeNameTH().length())){
//                    log.debug("-- EcmDetail.TypeNameTH is not null");
//                    log.debug("-- Document Type is {}", ecmDetail.getTypeNameTH());
                    checkOtherDocView.setDocumentType(ecmDetail.getTypeNameTH());
                } else {
//                    log.debug("-- EcmDetail.TypeNameTH is null");
//                    log.debug("-- Document Type is {}", "Empty");
                    checkOtherDocView.setDocumentType("");
                }
                flag = false;
            }

            //File Name
            checkMandatoryDocFileNameView = checkMandateDocFileNameTransform.transformToView(ecmDetail.getOrgFileName(), getURLByFNId(ecmDetail.getFnDocId(), userToken));
            fileNameViewList.add(checkMandatoryDocFileNameView);
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
