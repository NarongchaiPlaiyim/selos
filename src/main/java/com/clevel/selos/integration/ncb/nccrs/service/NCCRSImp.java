package com.clevel.selos.integration.ncb.nccrs.service;

import com.clevel.selos.dao.master.UserDAO;
import com.clevel.selos.exception.NCBInterfaceException;
import com.clevel.selos.integration.NCB;
import com.clevel.selos.integration.ncb.exportncbi.NCBIExportImp;
import com.clevel.selos.integration.ncb.exportncbi.NCBIExportModel;
import com.clevel.selos.integration.ncb.httppost.Post;
import com.clevel.selos.integration.ncb.ncbresult.NCBResultImp;
import com.clevel.selos.integration.ncb.nccrs.models.request.*;
import com.clevel.selos.integration.ncb.nccrs.models.response.ActiveAccountsModel;
import com.clevel.selos.integration.ncb.nccrs.models.response.ClosedAccountsModel;
import com.clevel.selos.integration.ncb.nccrs.models.response.NCCRSResponseModel;
import com.clevel.selos.integration.ncb.nccrs.nccrsmodel.NCCRSInputModel;
import com.clevel.selos.integration.ncb.nccrs.nccrsmodel.NCCRSModel;
import com.clevel.selos.integration.ncb.nccrs.nccrsmodel.NCCRSOutputModel;
import com.clevel.selos.model.ActionResult;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.security.encryption.EncryptionService;
import com.clevel.selos.system.Config;
import com.clevel.selos.system.audit.SystemAuditor;
import com.clevel.selos.system.audit.UserAuditor;
import com.clevel.selos.system.message.ExceptionMapping;
import com.clevel.selos.system.message.ExceptionMessage;
import com.clevel.selos.system.message.Message;
import com.clevel.selos.util.Util;
import com.thoughtworks.xstream.XStream;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.HttpHostConnectException;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class NCCRSImp implements NCCRS, Serializable {
    @Inject
    @NCB
    Logger log;

    @Inject
    Post post;

    @Inject
    @ExceptionMessage
    Message message;

    @Inject
    @Config(name = "interface.ncb.nccrs.username")
    private String id;

    @Inject
    @Config(name = "interface.ncb.nccrs.password")
    private String pass;

    @Inject
    @Config(name = "interface.ncb.nccrs.address")
    private String url;

    @Inject
    @Config(name = "interface.ncb.nccrs.timeOut")
    private String timeOut;


    @Inject
    @Config(name = "system.encryption.enable")
    String encryptionEnable;


    @Inject
    EncryptionService encryptionService;

    @Inject
    UserAuditor userAuditor;

    @Inject
    @NCB
    SystemAuditor ncbAuditor;

    @Inject
    @NCB
    NCBIExportImp exportImp;

    @Inject
    @NCB
    NCBResultImp resultImp;

    @Inject
    UserDAO userDAO;

    private String appRefNumber = null;
    private String userId = null;
    private String CANumber = null;
    private String referenceTel = null;
    private String registType = null;
    private String registId = null;
    private String memberRef = null;
    private String companyName = null;
    private String juristicType = null;
    private String passwordEncrypt;
    private final String action = "NCCRS";
    private final String ONLINE = "BB01001";
    private final String FIND = "TS01001";
    private final String READ = "TS01002";
    private final String ERROR = "ER01001";
    private final String exception = ExceptionMapping.NCB_EXCEPTION;
    private final String failed = ExceptionMapping.NCB_FAILED;
    private final String httpHostException = ExceptionMapping.NCB_HTTPHOSTCONNECTEXCEPTION;
    private final String timeOutException = ExceptionMapping.NCB_CONNECTTIMEOUTEXCEPTION;

    @Inject
    public NCCRSImp() {
    }

    @PostConstruct
    public void onCreate() {
        if (Util.isTrue(encryptionEnable)) {
            passwordEncrypt = encryptionService.decrypt(Base64.decodeBase64(pass));
        } else {
            passwordEncrypt = pass;
        }
    }

    @Override
    public ArrayList<NCCRSOutputModel> requestOnline(NCCRSInputModel inputModel) throws Exception {
        log.debug("NCCRS Call : requestOnline()");
        NCCRSResponseModel responseModel = null;

        ArrayList<NCCRSModel> nccrsModelArrayList = null;
        appRefNumber = inputModel.getAppRefNumber();
        userId = inputModel.getUserId();
        CANumber = inputModel.getCANumber();
        referenceTel = inputModel.getReferenceTel();
        String reason = null;
        Date inquiryDate = null;
        ArrayList<NCCRSOutputModel> outputModelArrayList = new ArrayList<NCCRSOutputModel>();
        nccrsModelArrayList = inputModel.getNccrsModelArrayList();
        log.debug("NCCRS Check ncb online {} personals", nccrsModelArrayList.size());
        for (NCCRSModel nccrsModel : nccrsModelArrayList) {
            registType = nccrsModel.getRegistType();
            registId = nccrsModel.getRegistId();
            companyName = nccrsModel.getCompanyName();
            memberRef = nccrsModel.getMemberRef();
            juristicType = nccrsModel.getJuristicType();
            try {
                inquiryDate = new Date();
                resultImp.add(appRefNumber, registType, registId, inquiryDate, ActionResult.SENDING, "", memberRef);
                responseModel = callOnline(nccrsModel);
                reason = "";
                if (!Util.isNull(responseModel.getBody().getTransaction().getTrackingid())) {
                    reason = responseModel.getBody().getTransaction().getTrackingid();
                    log.debug("NCCRS Tracking Id is {}", reason);
                }
                resultImp.updateSUCCEED(appRefNumber, registId, reason);
                outputModelArrayList.add(new NCCRSOutputModel(appRefNumber, ActionResult.SUCCESS, reason, registId, responseModel, nccrsModel));
            } catch (HttpHostConnectException e) {
                reason = e.getMessage();
                inquiryDate = new Date();
                log.error("NCCRS FAILED ", e);
                resultImp.updateStatus(appRefNumber,registId,reason,ActionResult.FAILED);
                outputModelArrayList.add(new NCCRSOutputModel(appRefNumber, ActionResult.FAILED, reason, registId, responseModel, nccrsModel));
            } catch (ConnectTimeoutException e) {
                reason = e.getMessage();
                inquiryDate = new Date();
                log.error("NCCRS FAILED ", e);
                resultImp.updateStatus(appRefNumber,registId,reason,ActionResult.FAILED);
                outputModelArrayList.add(new NCCRSOutputModel(appRefNumber, ActionResult.FAILED, reason, registId, responseModel, nccrsModel));
            } catch (Exception e) {
                reason = e.getMessage();
                inquiryDate = new Date();
                log.error("NCCRS EXCEPTION ", e);
                resultImp.updateStatus(appRefNumber,registId,reason,ActionResult.EXCEPTION);
                outputModelArrayList.add(new NCCRSOutputModel(appRefNumber, ActionResult.EXCEPTION, reason, registId, responseModel, nccrsModel));
            }
        }
        return outputModelArrayList;
    }

    @Override
    public ArrayList<NCCRSOutputModel> requestOffline(NCCRSInputModel inputModel) throws Exception {
        log.debug("NCRS Call : requestOffline (inputModel: {})", inputModel);
        NCCRSResponseModel responseModel = null;

        ArrayList<NCCRSModel> nccrsModelArrayList = null;
        appRefNumber = inputModel.getAppRefNumber();
        userId = inputModel.getUserId();
        CANumber = inputModel.getCANumber();
        referenceTel = inputModel.getReferenceTel();
        String reason = null;
        ArrayList<NCCRSOutputModel> outputModelArrayList = new ArrayList<NCCRSOutputModel>();
        nccrsModelArrayList = inputModel.getNccrsModelArrayList();
        log.debug("NCCRS Check ncb offline {} personals", nccrsModelArrayList.size());
        for (NCCRSModel nccrsModel : nccrsModelArrayList) {
            registType = nccrsModel.getRegistType();
            registId = nccrsModel.getRegistId();
            nccrsModel.setMemberRef(resultImp.getRequestNo(appRefNumber, registId));
            memberRef = nccrsModel.getMemberRef();
            companyName = nccrsModel.getCompanyName();
            juristicType = nccrsModel.getJuristicType();
            if (memberRef != null && !Util.isEmpty(memberRef)) {
                try {
                    responseModel = callOffline(nccrsModel);
                    reason = "";
                    if (!Util.isNull(responseModel.getBody().getTransaction().getTrackingid())) {
                        reason = responseModel.getBody().getTransaction().getTrackingid();
                        log.debug("NCCRS Tracking Id is {}", reason);
                    }
                    outputModelArrayList.add(new NCCRSOutputModel(appRefNumber, ActionResult.SUCCESS, reason, registId, responseModel, nccrsModel));
                } catch (HttpHostConnectException e) {
                    reason = e.getMessage();
                    log.error("NCCRS FAILED ", e);
                    outputModelArrayList.add(new NCCRSOutputModel(appRefNumber, ActionResult.FAILED, reason, registId, responseModel, nccrsModel));
                } catch (ConnectTimeoutException e) {
                    reason = e.getMessage();
                    log.error("NCCRS FAILED ", e);
                    outputModelArrayList.add(new NCCRSOutputModel(appRefNumber, ActionResult.FAILED, reason, registId, responseModel, nccrsModel));
                } catch (Exception e) {
                    reason = e.getMessage();
                    log.error("NCCRS EXCEPTION ", e);
                    outputModelArrayList.add(new NCCRSOutputModel(appRefNumber, ActionResult.EXCEPTION, reason, registId, responseModel, nccrsModel));
                }
            } else {
                //1.get size where appnumber
                //2.gen new memberRef
                //3requestOnline
                //4.update result, audit
            }
        }
        return outputModelArrayList;
    }

    private NCCRSResponseModel callOnline(NCCRSModel nccrsModel) throws Exception {
        log.debug("NCCRS Call : callOnline()");

        StringBuilder stringBuilder = null;
        String actionDesc = null;
        Date actionDate = null;
        Date resultDate = null;
        String resultDesc = null;
        String linkKey = null;
        NCCRSResponseModel responseModel = null;
        linkKey = Util.getLinkKey(userId);
        //TO Add office code when call NCB
        String officeCode = "";
        if(!Util.isEmpty(userId)) {
            User user = userDAO.findById(userId);
            if(!Util.isNull(user))
                officeCode = !Util.isNull(user.getTeam())?user.getTeam().getTeam_code():"";
        }
        stringBuilder = new StringBuilder();
        stringBuilder.append("companyname = ").append(nccrsModel.getCompanyName());
        stringBuilder.append(" registtype = ").append(nccrsModel.getRegistType());
        stringBuilder.append(" registid = ").append(nccrsModel.getRegistId());
        actionDesc = null != stringBuilder ? stringBuilder.toString() : "";
        log.debug("[{}] NCCRS requestOnline(NCCRSModel : {})", linkKey, actionDesc);
        actionDate = new Date();
        String action = this.action + " " + ONLINE;
        try {
            actionDate = new Date();
            responseModel = checkOnlineResponseModel(request(nccrsModel, ONLINE), officeCode);
            resultDate = new Date();
            log.debug("[{}] NCCRS Online audit userId {} action {} actionDesc {} actionDate {} actionResult {} resultDesc {} resultDate {} linkKey {}",
                    linkKey, userId, action, actionDesc, actionDate, ActionResult.SUCCESS, resultDesc, resultDate, linkKey);
            ncbAuditor.add(userId, action, actionDesc, actionDate, ActionResult.SUCCESS, resultDesc, resultDate, linkKey);
            saveNCBI(responseModel, officeCode);
            return responseModel;
        } catch (HttpHostConnectException e) {
            resultDesc = e.getMessage();
            resultDate = new Date();
            log.debug("[{}] NCCRS Online audit userId {} action {} actionDesc {} actionDate {} actionResult {} resultDesc {} resultDate {} linkKey {}",
                    linkKey, userId, action, actionDesc, actionDate, ActionResult.FAILED, resultDesc, resultDate, linkKey);
            ncbAuditor.add(userId, action, actionDesc, actionDate, ActionResult.FAILED, resultDesc, resultDate, linkKey);
            throw new NCBInterfaceException(e, httpHostException, message.get(httpHostException, resultDesc));
//            throw new HttpHostConnectException(new HttpHost(url), new ConnectException());
        } catch (ConnectTimeoutException e) {
            resultDesc = e.getMessage();
            resultDate = new Date();
            log.debug("[{}] NCCRS Online audit userId {} action {} actionDesc {} actionDate {} actionResult {} resultDesc {} resultDate {} linkKey {}",
                    linkKey, userId, action, actionDesc, actionDate, ActionResult.FAILED, resultDesc, resultDate, linkKey);
            ncbAuditor.add(userId, action, actionDesc, actionDate, ActionResult.FAILED, resultDesc, resultDate, linkKey);
            throw new NCBInterfaceException(e, timeOutException, message.get(timeOutException, resultDesc));
//            throw new ConnectTimeoutException(e.getMessage());
        } catch (NCBInterfaceException e) {
            resultDesc = e.getMessage();
            resultDate = new Date();
            log.debug("[{}] NCCRS Online audit userId {} action {} actionDesc {} actionDate {} actionResult {} resultDesc {} resultDate {} linkKey {}",
                    linkKey, userId, action, actionDesc, actionDate, ActionResult.EXCEPTION, resultDesc, resultDate, linkKey);
            ncbAuditor.add(userId, action, actionDesc, actionDate, ActionResult.EXCEPTION, resultDesc, resultDate, linkKey);
            throw new NCBInterfaceException(e, exception, resultDesc);
        } catch (Exception e) {
            resultDesc = e.getMessage();
            resultDate = new Date();
            log.debug("[{}] NCCRS Online audit userId {} action {} actionDesc {} actionDate {} actionResult {} resultDesc {} resultDate {} linkKey {}",
                    linkKey, userId, action, actionDesc, actionDate, ActionResult.EXCEPTION, resultDesc, resultDate, linkKey);
            ncbAuditor.add(userId, action, actionDesc, actionDate, ActionResult.EXCEPTION, resultDesc, resultDate, linkKey);
            if(resultDesc==null){
                throw new NCBInterfaceException(e, exception, message.get(exception));
            } else {
                throw new NCBInterfaceException(e, exception, message.get(exception, resultDesc));
            }
        }
    }

    private NCCRSResponseModel callOffline(NCCRSModel nccrsModel) throws Exception {
        log.debug("NCCRS Call : callOffline()");
        StringBuilder stringBuilder = null;
        String actionDesc = null;
        Date actionDate = null;
        Date resultDate = null;
        String resultDesc = null;
        String linkKey = null;
        NCCRSResponseModel responseModel = null;
        linkKey = Util.getLinkKey(userId);
        //TO Add office code when call NCB
        String officeCode = "";
        if(!Util.isEmpty(userId)) {
            User user = userDAO.findById(userId);
            if(!Util.isNull(user))
                officeCode = !Util.isNull(user.getTeam())?user.getTeam().getTeam_code():"";
        }
        stringBuilder = new StringBuilder();
        stringBuilder.append("companyname = ").append(nccrsModel.getCompanyName());
        stringBuilder.append(" registtype = ").append(nccrsModel.getRegistType());
        stringBuilder.append(" registid = ").append(nccrsModel.getRegistId());
        actionDesc = null != stringBuilder ? stringBuilder.toString() : "";
        log.debug("[{}] NCCRS requestOffline(NCCRSModel : {})", linkKey, actionDesc);
        actionDate = new Date();
        String action = this.action + " " + FIND;
        try {
            actionDate = new Date();
            responseModel = checkOfflineResponseModelTracking(request(nccrsModel, FIND));
            resultDate = new Date();
            log.debug("[{}] NCCRS Offline audit userId {} action {} actionDesc {} actionDate {} actionResult {} resultDesc {} resultDate {} linkKey {}",
                    linkKey, userId, action, actionDesc, actionDate, ActionResult.SUCCESS, resultDesc, resultDate, linkKey);
            ncbAuditor.add(userId, action, actionDesc, actionDate, ActionResult.SUCCESS, resultDesc, resultDate, linkKey);
            if (null != responseModel.getBody().getTrackingid()) {
                ArrayList<String> arrayList = responseModel.getBody().getTrackingid();
                if (arrayList.size() > 0) {
                    log.debug("NCCRS The List Tracking ID {}", arrayList);
                    String trackingId = arrayList.get(arrayList.size() - 1);
                    log.debug("NCCRS The maximum value of list Tracking ID is {}", trackingId);
                    nccrsModel.setTrackingId(trackingId);
                    actionDate = new Date();
                    actionDesc = trackingId;
                    action = this.action + " " + READ;
                    responseModel = checkOfflineResponseModel(request(nccrsModel, READ), officeCode);
                    if (null != responseModel.getBody().getTransaction().getUser()) {
                        log.debug("NCCRS get response offline");
                        String registId = nccrsModel.getRegistId();
                        //responseModel = checkOnlineResponseModel(responseModel);
                        resultDate = new Date();
                        log.debug("[{}] NCCRS Offline audit userId {} action {} actionDesc {} actionDate {} actionResult {} resultDesc {} resultDate {} linkKey {}",
                                linkKey, userId, action, actionDesc, actionDate, ActionResult.SUCCESS, resultDesc, resultDate, linkKey);
                        ncbAuditor.add(userId, action, actionDesc, actionDate, ActionResult.SUCCESS, resultDesc, resultDate, linkKey);
                        resultImp.updateSUCCEED(appRefNumber, registId, trackingId);
                        saveNCBI(responseModel, officeCode); //TODO: to confirm, need to reconcile or not?
                        return responseModel;
                    } else {
                        resultDesc = "NCCRS NCB Exception Transaction is null";
                        log.error("NCCRS NCB Exception Transaction is null");
                        throw new NCBInterfaceException(new Exception(resultDesc), exception, message.get(exception, resultDesc));
                    }
                } else {
                    responseModel = callOnline(nccrsModel);
                    String reason = "";
                    if (!Util.isNull(responseModel.getBody().getTransaction().getTrackingid())) {
                        reason = responseModel.getBody().getTransaction().getTrackingid();
                        log.debug("NCCRS Tracking Id is {}", reason);
                    }
                    resultImp.updateSUCCEED(appRefNumber, registId, reason);
                    return responseModel;
                }
            } else {
                responseModel = callOnline(nccrsModel);
                String reason = "";
                if (!Util.isNull(responseModel.getBody().getTransaction().getTrackingid())) {
                    reason = responseModel.getBody().getTransaction().getTrackingid();
                    log.debug("NCCRS Tracking Id is {}", reason);
                }
                resultImp.updateSUCCEED(appRefNumber, registId, reason);
                return responseModel;
            }
        } catch (HttpHostConnectException e) {
            resultDesc = e.getMessage();
            resultDate = new Date();
            log.debug("[{}] NCCRS Offline audit userId {} action {} actionDesc {} actionDate {} actionResult {} resultDesc {} resultDate {} linkKey {}",
                    linkKey, userId, action, actionDesc, actionDate, ActionResult.FAILED, resultDesc, resultDate, linkKey);
            ncbAuditor.add(userId, action, actionDesc, actionDate, ActionResult.FAILED, resultDesc, resultDate, linkKey);
            throw new NCBInterfaceException(e, httpHostException, message.get(httpHostException, resultDesc));
        } catch (ConnectTimeoutException e) {
            resultDesc = e.getMessage();
            resultDate = new Date();
            log.debug("[{}] NCCRS Offline audit userId {} action {} actionDesc {} actionDate {} actionResult {} resultDesc {} resultDate {} linkKey {}",
                    linkKey, userId, action, actionDesc, actionDate, ActionResult.FAILED, resultDesc, resultDate, linkKey);
            ncbAuditor.add(userId, action, actionDesc, actionDate, ActionResult.FAILED, resultDesc, resultDate, linkKey);
            throw new NCBInterfaceException(e, timeOutException, message.get(timeOutException, resultDesc));
        } catch (NCBInterfaceException e) {
            resultDesc = e.getMessage();
            resultDate = new Date();
            log.debug("[{}] NCCRS Offline audit userId {} action {} actionDesc {} actionDate {} actionResult {} resultDesc {} resultDate {} linkKey {}",
                    linkKey, userId, action, actionDesc, actionDate, ActionResult.EXCEPTION, resultDesc, resultDate, linkKey);
            ncbAuditor.add(userId, action, actionDesc, actionDate, ActionResult.EXCEPTION, resultDesc, resultDate, linkKey);
            throw new NCBInterfaceException(e, exception, resultDesc);
        } catch (Exception e) {
            resultDesc = e.getMessage();
            resultDate = new Date();
            log.debug("[{}] NCCRS Offline audit userId {} action {} actionDesc {} actionDate {} actionResult {} resultDesc {} resultDate {} linkKey {}",
                    linkKey, userId, action, actionDesc, actionDate, ActionResult.EXCEPTION, resultDesc, resultDate, linkKey);
            ncbAuditor.add(userId, action, actionDesc, actionDate, ActionResult.EXCEPTION, resultDesc, resultDate, linkKey);
            if(resultDesc==null){
                throw new NCBInterfaceException(e, exception, message.get(exception));
            } else {
                throw new NCBInterfaceException(e, exception, message.get(exception, resultDesc));
            }
        }
    }

    private NCCRSResponseModel checkOnlineResponseModel(NCCRSResponseModel responseModel, String officeCode) throws Exception {
        log.debug("NCCRS Call : checkOnlineResponseModel()");
        if (null != responseModel) {
            String resultDesc = responseModel.getHeader().getCommand();
            log.debug("NCCRS Result desc {}", resultDesc);
            if (!ERROR.equals(resultDesc)) {
                if(responseModel.getBody().getErrormsg()!=null){
                    //Link down
                    resultDesc = responseModel.getBody().getErrormsg();
                    log.error("NCCRS NCB Exception {}", responseModel.getBody().getErrormsg());
                    throw new NCBInterfaceException(new Exception(resultDesc), this.exception, resultDesc);
                } else {
                    if (null != responseModel.getBody().getTransaction().getH2herror()) { //error
                        resultDesc = responseModel.getBody().getTransaction().getH2herror().getErrormsg();
                        log.error("NCCRS NCB Exception TUEFERROR {}", resultDesc);
                        log.debug("save data to NCBI Export");
                        saveNCBI(responseModel, officeCode);
                        throw new NCBInterfaceException(new Exception(resultDesc), this.exception, resultDesc);
                    } else if (null != responseModel.getBody().getTransaction().getH2hresponse()){
                        return responseModel;
                    } else {
                        //unexpected result;
                        resultDesc = "Unexpected Result From NCCRS";
                        log.error("NCCRS NCB Exception {}", resultDesc);
                        throw new NCBInterfaceException(new Exception(resultDesc), this.exception, resultDesc);
                    }
                }
            } else {
                resultDesc = responseModel.getBody().getErrormsg();
                log.error("NCCRS NCB Exception {}", responseModel.getBody().getErrormsg());
                throw new NCBInterfaceException(new Exception(resultDesc), exception, resultDesc);
            }
        } else {
            String resultDesc = "NCCRS Response model is null";
            log.error("NCCRS Response model is null");
            throw new NCBInterfaceException(new Exception(resultDesc), exception, resultDesc);
        }
    }

    private NCCRSResponseModel checkOfflineResponseModel(NCCRSResponseModel responseModel, String officeCode) throws Exception {
        log.debug("NCCRS Call : checkOfflineResponseModel()");
        if (null != responseModel) {
            String resultDesc = responseModel.getHeader().getCommand();
            log.debug("NCCRS Result desc {}", resultDesc);
            if (!ERROR.equals(resultDesc)) {
                if(responseModel.getBody().getErrormsg()!=null){
                    //Link down
                    resultDesc = responseModel.getBody().getErrormsg();
                    log.error("NCCRS NCB Exception {}", responseModel.getBody().getErrormsg());
                    throw new NCBInterfaceException(new Exception(resultDesc), this.exception, resultDesc);
                } else {
                    if (null != responseModel.getBody().getTransaction().getH2herror()) { //error
                        resultDesc = responseModel.getBody().getTransaction().getH2herror().getErrormsg();
                        log.error("NCCRS NCB Exception TUEFERROR {}", resultDesc);
                        log.debug("save data to NCBI Export");
                        saveNCBI(responseModel, officeCode);
                        throw new NCBInterfaceException(new Exception(resultDesc), this.exception, resultDesc);
                    } else if (null != responseModel.getBody().getTransaction().getH2hresponse()){
                        return responseModel;
                    } else {
                        //unexpected result;
                        resultDesc = "Unexpected Result From NCCRS";
                        log.error("NCCRS NCB Exception {}", resultDesc);
                        throw new NCBInterfaceException(new Exception(resultDesc), this.exception, resultDesc);
                    }
                }
            } else {
                resultDesc = responseModel.getBody().getErrormsg();
                log.error("NCCRS NCB Exception {}", responseModel.getBody().getErrormsg());
                throw new NCBInterfaceException(new Exception(resultDesc), exception, resultDesc);
            }
        } else {
            String resultDesc = "NCCRS Response model is null";
            log.error("NCCRS Response model is null");
            throw new NCBInterfaceException(new Exception(resultDesc), exception, resultDesc);
        }
    }

    private NCCRSResponseModel checkOfflineResponseModelTracking(NCCRSResponseModel responseModel) throws Exception {
        log.debug("NCCRS Call : checkOfflineResponseModelTracking()");
        if (responseModel != null) {
            String resultDesc = responseModel.getHeader().getCommand();
            log.debug("NCCRS Result desc {}", resultDesc);
            if (!ERROR.equals(responseModel.getHeader().getCommand())) {
                if(responseModel.getBody().getErrormsg()!=null){
                    //Link down
                    resultDesc = responseModel.getBody().getErrormsg();
                    log.error("NCCRS NCB Exception {}", responseModel.getBody().getErrormsg());
                    throw new NCBInterfaceException(new Exception(resultDesc), this.exception, resultDesc);
                } else {
                    return responseModel;
                }
            } else {
                resultDesc = responseModel.getBody().getErrormsg();
                log.error("NCCRS NCB Exception {}", responseModel.getBody().getErrormsg());
                throw new NCBInterfaceException(new Exception(resultDesc), this.exception, resultDesc);
            }
        } else {
            String resultDesc = "NCCRS Response model is null";
            log.error("NCCRS Response model is null");
            throw new NCBInterfaceException(new Exception(resultDesc), exception, resultDesc);
        }
    }

    private void saveNCBI(NCCRSResponseModel responseModel, String officeCode) throws Exception {
        NCBIExportModel exportModel = new NCBIExportModel();

        exportModel.setOfficeCode(officeCode);

        exportModel.setRequestNo(memberRef);
        exportModel.setStaffId(userId);

        exportModel.setInquiryType("01");
        exportModel.setCustomerType("02");
        exportModel.setCustomerDocumentType("05");//
        exportModel.setJuristicType(juristicType);
        exportModel.setCustomerId(registId);

        exportModel.setCountryCode("TH");

        exportModel.setTitleCode(null);
        exportModel.setFirstName(null);
        exportModel.setLastName(null);
        exportModel.setJuristicName(companyName);
        exportModel.setAppRefNumber(appRefNumber);
        exportModel.setCaution(null);
        exportModel.setReferenceTel(referenceTel);

        try {
            ActiveAccountsModel activeaccounts = responseModel.getBody().getTransaction().getH2hresponse().getSubject().getActiveaccounts();
            ClosedAccountsModel closedAccountsModel = responseModel.getBody().getTransaction().getH2hresponse().getSubject().getClosedaccounts();
            if (null != activeaccounts || null != closedAccountsModel) {
                exportModel.setInquiryStatus("01");
            } else {
                exportModel.setInquiryStatus("02");
            }
        } catch (Exception e) {
            exportModel.setInquiryStatus("02");
        }

        try {
            String date = responseModel.getBody().getTransaction().getRequestdate();
            exportModel.setInquiryDate(Util.getDateOrTime(date, true));
            exportModel.setInquiryTime(Util.getDateOrTime(date, false));
        } catch (Exception e) {
            Date date = new Date();
            exportModel.setInquiryDate(Util.createDateString(date));
            exportModel.setInquiryTime(Util.createDateString(date, "HH:mm:ss"));
        }
        exportImp.add(exportModel);
    }

    private NCCRSRequestModel createOnlineModel(NCCRSModel nccrsModel, String command) {
        log.debug("NCCRS Call : createOnlineModel()");
        String inqPurose = nccrsModel.getInqPurose();
        String productType = nccrsModel.getProductType();
        String confirmConsent = nccrsModel.getConfirmConsent();
        String language = nccrsModel.getLanguage();
        String historicalBalanceReport = nccrsModel.getHistoricalBalanceReport();

        return new NCCRSRequestModel(
                new HeaderModel(id, passwordEncrypt, command),
                new BodyModel(
                        new H2HRequestModel(registType, registId, companyName,
                                inqPurose, productType, memberRef,
                                confirmConsent, language),
                        new AttributeModel(historicalBalanceReport)));
    }

    private NCCRSRequestModel createFindModel(NCCRSModel nccrsModel, String command) {
        log.debug("NCCRS Call : createFindModel()");
        return new NCCRSRequestModel(
                new HeaderModel(id, passwordEncrypt, command),
                new BodyModel(
                        //enquirydatefrom must be default value 19000101
                        new CriteriaModel(/*Util.createDateString(new Date(), "yyyyMMdd")*/ id, nccrsModel.getRegistId(), memberRef)));

    }

    private NCCRSRequestModel createReadModel(String trackingId, String command) {
        log.debug("NCCRS Call : createReadModel()");
        return new NCCRSRequestModel(
                new HeaderModel(id, passwordEncrypt, command),
                new BodyModel(trackingId));
    }

    private NCCRSResponseModel request(NCCRSModel nccrsModel, String command) throws Exception {
        log.debug("NCCRS Call : request()");
        log.debug("NCCRS Command code : {}", command);
        if (ONLINE.equals(command)) {
            return sendToHTTP(createOnlineModel(nccrsModel, command));
        } else if (FIND.equals(command)) {
            return sendToHTTP(createFindModel(nccrsModel, command));
        } else {
            return sendToHTTP(createReadModel(nccrsModel.getTrackingId(), command));
        }
    }

    private NCCRSResponseModel sendToHTTP(NCCRSRequestModel nccrsRequest) throws Exception {
        log.debug("NCCRS Call : sendToHTTP()");
        NCCRSResponseModel nccrsResponse = null;
        String xml = null;
        String result = null;
        XStream xStream = null;

        xStream = new XStream();
        xStream.processAnnotations(NCCRSRequestModel.class);
        xml = new String(xStream.toXML(nccrsRequest).getBytes("UTF-8"));
        log.debug("NCCRS Request : \n{}", xml);
        int nTimeOut = 60; //sec.
        try {
            nTimeOut = Integer.parseInt(timeOut);
        } catch (Exception ex) {
            log.debug("can not convert time out to integer (Default is 60 second)");
        }
        result = new String(post.sendPost(xml, url, nTimeOut).getBytes("ISO-8859-1"), "UTF-8");
        String res = "<ncrsresponse>";
        int pointer = result.indexOf(res);
        result = result.replace(result.substring(0, pointer), "");
        if (!"".equals(result)) {
            xStream.processAnnotations(NCCRSResponseModel.class);
            nccrsResponse = (NCCRSResponseModel) xStream.fromXML(result);
            log.debug("NCCRS Response : \n{}", xStream.toXML(nccrsResponse));
            return nccrsResponse;
        } else {
            String resultDesc = "NCCRS XML response error : " + result;
            log.error("NCCRS XML response error : {}", result);
            throw new NCBInterfaceException(new Exception(resultDesc), exception, message.get(exception, resultDesc));
        }
    }
}
