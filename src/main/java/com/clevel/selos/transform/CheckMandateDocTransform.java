package com.clevel.selos.transform;

import com.clevel.selos.businesscontrol.master.StepControl;
import com.clevel.selos.dao.master.ReasonDAO;
import com.clevel.selos.dao.master.StageDAO;
import com.clevel.selos.dao.master.StepDAO;
import com.clevel.selos.dao.working.*;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.integration.ecm.db.ECMDetail;
import com.clevel.selos.model.DocLevel;
import com.clevel.selos.model.DocMandateType;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.working.*;
import com.clevel.selos.model.view.*;
import com.clevel.selos.system.Config;
import com.clevel.selos.system.message.Message;
import com.clevel.selos.system.message.NormalMessage;
import com.clevel.selos.util.Util;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

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
    @NormalMessage
    private Message msg;

    @Inject
    private MandateDocDetailDAO mandateDocDetailDAO;
    @Inject
    private WorkCaseDAO workCaseDAO;
    @Inject
    private WorkCasePrescreenDAO workCasePrescreenDAO;
    @Inject
    private MandateDocSummaryDAO mandateDocSummaryDAO;
    @Inject
    private CustomerDAO customerDAO;
    @Inject
    private StepDAO stepDAO;
    @Inject
    private ReasonDAO reasonDAO;
    @Inject
    private StageDAO stageDAO;

    @Inject
    private CustomerTransform customerTransform;

    private String fileNamePrefix;

    private static SimpleDateFormat viewDateFormatWT = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.UK);

    @Inject
    public CheckMandateDocTransform() {
    }

    @PostConstruct
    private void init(){
        log.debug("-- init()");
        fileNamePrefix = msg.get("app.checkmandatedoc.filename.prefix");
    }

    public CheckMandateDocView transformToView(MandateDocSummary mandateDocSummary, String userToken){

        CheckMandateDocView checkMandateDocView = new CheckMandateDocView();
        if(mandateDocSummary == null)
            return checkMandateDocView;

        checkMandateDocView.setId(mandateDocSummary.getId());
        checkMandateDocView.setStepId(mandateDocSummary.getStep().getId());
        checkMandateDocView.setForStageId(mandateDocSummary.getStage().getId());
        checkMandateDocView.setRoleId(mandateDocSummary.getRole().getId());
        if(mandateDocSummary.getWorkCase() != null)
            checkMandateDocView.setWorkCaseId(mandateDocSummary.getWorkCase().getId());
        if(mandateDocSummary.getWorkCasePrescreen() != null)
            checkMandateDocView.setWorkCasePrescreenId(mandateDocSummary.getWorkCasePrescreen().getId());


        Map<String, MandateDocView> mandateDocViewMap = new ConcurrentHashMap<String, MandateDocView>();
        Map<String, MandateDocView> optionalDocViewMap = new ConcurrentHashMap<String, MandateDocView>();
        Map<String, MandateDocView> otherDocViewMap = new ConcurrentHashMap<String, MandateDocView>();

        for(MandateDocDetail mandateDocDetail : mandateDocSummary.getMandateDocDetailList()){
            MandateDocView mandateDocView = new MandateDocView();
            mandateDocView.setId(mandateDocDetail.getId());
            mandateDocView.setEcmDocTypeId(mandateDocDetail.getEcmDocType());
            mandateDocView.setEcmDocTypeDesc(mandateDocDetail.getEcmDocTypeDesc());
            mandateDocView.setCompletedFlag(mandateDocDetail.getCompletedFlag());
            mandateDocView.setDisplay(mandateDocDetail.isDisplay());
            mandateDocView.setDocMandateType(mandateDocDetail.getMandateType());
            mandateDocView.setRemark(mandateDocDetail.getRemark());
            if(mandateDocDetail.getMandateDocCustList() != null && mandateDocDetail.getMandateDocCustList().size() > 0)
                mandateDocView.setDocLevel(DocLevel.CUS_LEVEL);
            else {
                if(mandateDocDetail.getMandateType().equals(DocMandateType.MANDATE) || mandateDocDetail.getMandateType().equals(DocMandateType.OPTIONAL))
                    mandateDocView.setDocLevel(DocLevel.APP_LEVEL);
                else
                    mandateDocView.setDocLevel(DocLevel.NA);
            }

            List<CustomerInfoSimpleView> customerInfoSimpleViewList = new ArrayList<CustomerInfoSimpleView>();
            if(mandateDocDetail.getMandateDocCustList() != null && mandateDocDetail.getMandateDocCustList().size() > 0){
                for(MandateDocCust mandateDocCust : mandateDocDetail.getMandateDocCustList()){
                    CustomerInfoSimpleView customerInfoSimpleView = customerTransform.transformToSimpleView(mandateDocCust.getCustomer());
                    customerInfoSimpleViewList.add(customerInfoSimpleView);
                }
            }
            mandateDocView.setCustomerInfoSimpleViewList(customerInfoSimpleViewList);

            List<String> brmsDocList = new ArrayList<String>();
            if(mandateDocDetail.getMandateDocBRMSList() != null && mandateDocDetail.getMandateDocBRMSList().size() > 0){
                for(MandateDocBRMS mandateDocBRMS : mandateDocDetail.getMandateDocBRMSList()){
                    brmsDocList.add(mandateDocBRMS.getBRMSDocType());
                }
            }
            mandateDocView.setBrmsDescList(brmsDocList);

            List<MandateDocFileNameView> mandateDocFileNameViewList = new ArrayList<MandateDocFileNameView>();
            if(mandateDocDetail.getMandateDocFileNameList() != null && mandateDocDetail.getMandateDocFileNameList().size() > 0){
                for(MandateDocFileName mandateDocFileName : mandateDocDetail.getMandateDocFileNameList()){
                    MandateDocFileNameView mandateDocFileNameView = new MandateDocFileNameView();
                    mandateDocFileNameView.setId(mandateDocFileName.getId());
                    mandateDocFileNameView.setEcmDocId(mandateDocDetail.getEcmDocType());
                    mandateDocFileNameView.setFnDocId(mandateDocFileName.getFnDocId());
                    mandateDocFileNameView.setUrl(getURLByFNId(mandateDocFileName.getFnDocId(), userToken));
                    mandateDocFileNameViewList.add(mandateDocFileNameView);
                }
            }
            mandateDocView.setMandateDocFileNameViewList(mandateDocFileNameViewList);
            mandateDocView.setNumberOfDoc(mandateDocFileNameViewList.size());

            List<String> reasonList = new ArrayList<String>();
            if(mandateDocDetail.getMandateDocReasonList() != null && mandateDocDetail.getMandateDocReasonList().size() > 0){
                for(MandateDocReason mandateDocReason : mandateDocDetail.getMandateDocReasonList()){
                    reasonList.add(Integer.toString(mandateDocReason.getReason().getId()));
                }
            }
            mandateDocView.setSelectedReasonList(reasonList);

            if(DocMandateType.MANDATE.equals(mandateDocDetail.getMandateType())){
                mandateDocViewMap.put(mandateDocView.getEcmDocTypeId(), mandateDocView);
            } else if(DocMandateType.OPTIONAL.equals(mandateDocDetail.getMandateType())){
                optionalDocViewMap.put(mandateDocView.getEcmDocTypeId(), mandateDocView);
            } else {
                otherDocViewMap.put(mandateDocView.getEcmDocTypeId(), mandateDocView);
            }
        }

        checkMandateDocView.setMandatoryDocumentsMap(mandateDocViewMap);
        checkMandateDocView.setOptionalDocumentsMap(optionalDocViewMap);
        checkMandateDocView.setOtherDocumentsMap(otherDocViewMap);
        return checkMandateDocView;
    }


    public MandateDocFileNameView transformToView(ECMDetail ecmDetail, String userToken){
        MandateDocFileNameView mandateDocFileNameView = new MandateDocFileNameView();
        if(ecmDetail == null)
            return mandateDocFileNameView;

        String fileName = null;
        if(!Util.isNull(ecmDetail.getImportDate())){
            fileName = fileNamePrefix + "_" + viewDateFormatWT.format(ecmDetail.getImportDate());

        } else {
            fileName = fileNamePrefix + "_" + viewDateFormatWT.format(ecmDetail.getCreateDate());
        }
        mandateDocFileNameView.setFileName(fileName);
        mandateDocFileNameView.setFnDocId(ecmDetail.getFnDocId());
        mandateDocFileNameView.setUrl(getURLByFNId(ecmDetail.getFnDocId(), userToken));
        return mandateDocFileNameView;
    }


    //URL
    private String getURLByFNId(final String FNId, final String userToken){
        return address+"/getContent?objectStoreName="+objectStore+"&id="+FNId+"&objectType=document&ut=" + userToken;
    }

    private String updateToken(final String oldUrl, String userToken){
        String newUrl = null;
        final String PARAM = "document&ut=";
        if(!Util.isNull(oldUrl) && !Util.isZero(oldUrl.length())){
            newUrl = oldUrl.substring(0, oldUrl.indexOf(PARAM))+PARAM+userToken;
            log.debug("-- [BEFORE] {}", oldUrl);
            log.debug("-- [AFTER] {}", newUrl);
            return newUrl;
        } else {
            return oldUrl;
        }
    }

    public MandateDocSummary transformToModel(CheckMandateDocView checkMandateDocView, User user){
        if(checkMandateDocView == null)
            return null;

        Date now = Calendar.getInstance().getTime();
        MandateDocSummary mandateDocSummary = null;
        if(checkMandateDocView.getId() > 0) {
            mandateDocSummary = mandateDocSummaryDAO.findById(checkMandateDocView.getId());
        } else {
            if(checkMandateDocView.getWorkCasePrescreenId() > 0)
                mandateDocSummary = mandateDocSummaryDAO.findByWorkCasePrescreenIdForStepRole(checkMandateDocView.getWorkCasePrescreenId(), checkMandateDocView.getStepId(), checkMandateDocView.getRoleId());
            if(checkMandateDocView.getWorkCaseId() > 0)
                mandateDocSummary = mandateDocSummaryDAO.findByWorkCaseIdForStepRole(checkMandateDocView.getWorkCaseId(), checkMandateDocView.getStepId(), checkMandateDocView.getRoleId());
        }

        if(mandateDocSummary == null)
            mandateDocSummary = new MandateDocSummary();

        if(mandateDocSummary.getCreateDate() == null){
            mandateDocSummary.setCreateDate(now);
            mandateDocSummary.setCreateBy(user);
        }
        mandateDocSummary.setModifyDate(now);
        mandateDocSummary.setModifyBy(user);
        mandateDocSummary.setRole(user.getRole());
        mandateDocSummary.setStep(stepDAO.findById(checkMandateDocView.getStepId()));
        mandateDocSummary.setStage(stageDAO.findById(checkMandateDocView.getForStageId()));

        if(checkMandateDocView.getWorkCaseId() > 0)
            mandateDocSummary.setWorkCase(workCaseDAO.findById(checkMandateDocView.getWorkCaseId()));
        else
            mandateDocSummary.setWorkCase(null);

        if(checkMandateDocView.getWorkCasePrescreenId() > 0)
            mandateDocSummary.setWorkCasePrescreen(workCasePrescreenDAO.findById(checkMandateDocView.getWorkCasePrescreenId()));
        else
            mandateDocSummary.setWorkCasePrescreen(null);

        List<MandateDocDetail> mandateDocDetailList = new ArrayList<MandateDocDetail>();
        //Add Mandate Doc View
        for(MandateDocView mandateDocVew : checkMandateDocView.getMandateDocumentsList()){
            mandateDocDetailList.add(transformToModel(mandateDocVew, mandateDocSummary));
        }
        //Add Optional Doc View
        for(MandateDocView mandateDocView : checkMandateDocView.getOptionalDocumentsList()){
            mandateDocDetailList.add(transformToModel(mandateDocView, mandateDocSummary));
        }
        //Add Other Doc View
        for(MandateDocView mandateDocView : checkMandateDocView.getOtherDocumentsList()){
            mandateDocDetailList.add(transformToModel(mandateDocView, mandateDocSummary));
        }

        mandateDocSummary.setMandateDocDetailList(mandateDocDetailList);

        return mandateDocSummary;
    }

    public MandateDocDetail transformToModel(MandateDocView mandateDocView, MandateDocSummary mandateDocSummary){
        if(mandateDocView == null)
            return null;
        MandateDocDetail mandateDocDetail = new MandateDocDetail();
        mandateDocDetail.setCompletedFlag(mandateDocView.getCompletedFlag());
        mandateDocDetail.setEcmDocType(mandateDocView.getEcmDocTypeId());
        mandateDocDetail.setEcmDocTypeDesc(mandateDocView.getEcmDocTypeDesc());
        mandateDocDetail.setMandateType(mandateDocView.getDocMandateType());
        mandateDocDetail.setDisplay(mandateDocView.isDisplay());
        mandateDocDetail.setRemark(mandateDocView.getRemark());

        if(mandateDocView.getBrmsDescList() != null && mandateDocView.getBrmsDescList().size() > 0) {
            List<MandateDocBRMS> mandateDocBRMSList = new ArrayList<MandateDocBRMS>();
            for(String brmsDoc : mandateDocView.getBrmsDescList()){
                MandateDocBRMS mandateDocBRMS = new MandateDocBRMS();
                mandateDocBRMS.setBRMSDocType(brmsDoc);
                mandateDocBRMS.setMandateDocDetail(mandateDocDetail);
                mandateDocBRMSList.add(mandateDocBRMS);
            }
            mandateDocDetail.setMandateDocBRMSList(mandateDocBRMSList);
        }

        if(mandateDocView.getMandateDocFileNameViewList() != null && mandateDocView.getMandateDocFileNameViewList().size() > 0) {
            List<MandateDocFileName> mandateDocFileNameList = new ArrayList<MandateDocFileName>();
            for(MandateDocFileNameView fileNameView : mandateDocView.getMandateDocFileNameViewList()){
                MandateDocFileName mandateDocFileName = new MandateDocFileName();
                mandateDocFileName.setId(fileNameView.getId());
                mandateDocFileName.setMandateDocDetail(mandateDocDetail);
                mandateDocFileName.setFileName(fileNameView.getFileName());
                mandateDocFileName.setFnDocId(fileNameView.getFnDocId());
                mandateDocFileName.setEcmDocId(fileNameView.getEcmDocId());
                mandateDocFileNameList.add(mandateDocFileName);
            }
            mandateDocDetail.setMandateDocFileNameList(mandateDocFileNameList);
        }

        if(mandateDocView.getCustomerInfoSimpleViewList() != null && mandateDocView.getCustomerInfoSimpleViewList().size() > 0) {
            List<MandateDocCust> mandateDocCustList = new ArrayList<MandateDocCust>();
            for(CustomerInfoSimpleView customerInfoSimpleView : mandateDocView.getCustomerInfoSimpleViewList()){
                MandateDocCust mandateDocCust = new MandateDocCust();
                mandateDocCust.setCustomer(customerDAO.findById(customerInfoSimpleView.getId()));
                mandateDocCust.setMandateDocDetail(mandateDocDetail);
                mandateDocCustList.add(mandateDocCust);
            }
            mandateDocDetail.setMandateDocCustList(mandateDocCustList);
        }

        if(mandateDocView.getSelectedReasonList() != null && mandateDocView.getSelectedReasonList().size() > 0) {
            List<MandateDocReason> mandateDocReasonList = new ArrayList<MandateDocReason>();
            for(String reasonIdStr : mandateDocView.getSelectedReasonList()){
                int reasonId = Integer.parseInt(reasonIdStr);
                MandateDocReason mandateDocReason = new MandateDocReason();
                mandateDocReason.setReason(reasonDAO.findById(reasonId));
                mandateDocReason.setMandateDocDetail(mandateDocDetail);
                mandateDocReasonList.add(mandateDocReason);
            }
            mandateDocDetail.setMandateDocReasonList(mandateDocReasonList);
        }

        mandateDocDetail.setMandateDocSummary(mandateDocSummary);
        return mandateDocDetail;
    }

}
