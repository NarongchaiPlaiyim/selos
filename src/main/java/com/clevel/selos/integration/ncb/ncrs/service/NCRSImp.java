package com.clevel.selos.integration.ncb.ncrs.service;


import com.clevel.selos.dao.master.UserDAO;
import com.clevel.selos.exception.NCBInterfaceException;
import com.clevel.selos.integration.NCB;
import com.clevel.selos.integration.ncb.exportncbi.NCBIExportImp;
import com.clevel.selos.integration.ncb.exportncbi.NCBIExportModel;
import com.clevel.selos.integration.ncb.httppost.Post;
import com.clevel.selos.integration.ncb.ncbresult.NCBResultImp;
import com.clevel.selos.integration.ncb.ncrs.models.request.*;
import com.clevel.selos.integration.ncb.ncrs.models.response.ErrorModel;
import com.clevel.selos.integration.ncb.ncrs.models.response.NCRSResponseModel;
import com.clevel.selos.integration.ncb.ncrs.models.response.SubjectModel;
import com.clevel.selos.integration.ncb.ncrs.models.response.TUEFErrorError;
import com.clevel.selos.integration.ncb.ncrs.ncrsmodel.NCRSInputModel;
import com.clevel.selos.integration.ncb.ncrs.ncrsmodel.NCRSModel;
import com.clevel.selos.integration.ncb.ncrs.ncrsmodel.NCRSOutputModel;
import com.clevel.selos.integration.ncb.vaildation.ValidationImp;
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

public class NCRSImp implements NCRS, Serializable {
    @Inject
    @NCB
    Logger log;

    @Inject
    Post post;

    @Inject
    @ExceptionMessage
    Message message;

    @Inject
    ValidationImp validationImp;

    @Inject
    @Config(name = "interface.ncb.ncrs.username")
    private String id;

    @Inject
    @Config(name = "interface.ncb.ncrs.password")
    private String pass;

    @Inject
    @Config(name = "interface.ncb.ncrs.address")
    private String url;

    @Inject
    @Config(name = "interface.ncb.ncrs.timeOut")
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

    ArrayList<TUEFEnquiryIdModel> idModelArrayList = null;
    ArrayList<TUEFEnquiryNameModel> nameModelArrayList = null;

    private String appRefNumber = null;
    private String userId = null;
    private String CANumber = null;
    private String referenceTel = null;
    private String customerType = null;
    private String customerId = null;
    private String customerDocmentType = null;
    private String titleNameCode = null;
    private String memberref = null;
    private String countryCode = null;
    private String firstName = null;
    private String lastName = null;
    private String passwordEncrypt;
    private final String action = "NCRS";
    private final String ONLINE = "BB01001";
    private final String FIND = "TS01001";
    private final String READ = "TS01002";
    private final String ERROR = "ER01001";
    private final String exception = ExceptionMapping.NCB_EXCEPTION;
    private final String failed = ExceptionMapping.NCB_FAILED;
    private final String httpHostException = ExceptionMapping.NCB_HTTPHOSTCONNECTEXCEPTION;
    private final String timeOutException = ExceptionMapping.NCB_CONNECTTIMEOUTEXCEPTION;

    @Inject
    public NCRSImp() {
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
    public ArrayList<NCRSOutputModel> requestOnline(NCRSInputModel inputModel) throws Exception {
        log.debug("NCRS Call : requestOnline()");
        NCRSResponseModel responseModel = null;

        ArrayList<NCRSModel> ncrsModelArrayList = null;
        TUEFEnquiryIdModel idModel = null;
        TUEFEnquiryNameModel nameModel = null;
        appRefNumber = inputModel.getAppRefNumber();
        userId = inputModel.getUserId();
        CANumber = inputModel.getCANumber();
        referenceTel = inputModel.getReferenceTel();
        String reason = null;
        Date inquiryDate = null;
        ArrayList<NCRSOutputModel> outputModelArrayList = new ArrayList<NCRSOutputModel>();
        ncrsModelArrayList = inputModel.getNcrsModelArrayList();
        log.debug("NCRS Check ncb online {} personals", ncrsModelArrayList.size());
        for (NCRSModel ncrsModel : ncrsModelArrayList) {
            customerId = ncrsModel.getCitizenId();
            customerType = ncrsModel.getIdType();
            customerDocmentType = ncrsModel.getCustomerDocmentType();
            titleNameCode = ncrsModel.getTitleNameCode();
            countryCode = ncrsModel.getCountryCode();
            memberref = ncrsModel.getMemberref();
            firstName = ncrsModel.getFirstName();
            lastName = ncrsModel.getLastName();
            idModelArrayList = new ArrayList<TUEFEnquiryIdModel>();
            if ("07".equals(customerType)) {
                idModel = new TUEFEnquiryIdModel(customerType, customerId, countryCode);
            } else {
                idModel = new TUEFEnquiryIdModel(customerType, customerId);
            }
            idModelArrayList.add(idModel);
            nameModelArrayList = new ArrayList<TUEFEnquiryNameModel>();
            nameModel = new TUEFEnquiryNameModel(ncrsModel.getLastName(), ncrsModel.getFirstName());
            nameModelArrayList.add(nameModel);
            try {
                inquiryDate = new Date();
                log.debug("add logging before call NCB...");
                resultImp.add(appRefNumber, customerType, customerId, inquiryDate, ActionResult.SENDING, "", memberref);
                responseModel = callOnline(ncrsModel);
                reason = "";
                if (!Util.isNull(responseModel.getBodyModel().getTransaction().getTrackingid())) {
                    reason = responseModel.getBodyModel().getTransaction().getTrackingid();
                    log.debug("NCRS Tracking Id is {}", reason);
                }
                resultImp.updateSUCCEED(appRefNumber, customerId, reason);
                outputModelArrayList.add(new NCRSOutputModel(appRefNumber, ActionResult.SUCCESS, reason, customerId, responseModel, ncrsModel));
            } catch (HttpHostConnectException e) {
                reason = e.getMessage();
                inquiryDate = new Date();
                log.error("NCRS FAILED {}", reason);
                resultImp.updateStatus(appRefNumber,customerId,reason,ActionResult.FAILED);
                outputModelArrayList.add(new NCRSOutputModel(appRefNumber, ActionResult.FAILED, reason, customerId, responseModel, ncrsModel));
            } catch (ConnectTimeoutException e) {
                reason = e.getMessage();
                inquiryDate = new Date();
                log.error("NCRS FAILED {}", reason);
                resultImp.updateStatus(appRefNumber,customerId,reason,ActionResult.FAILED);
                outputModelArrayList.add(new NCRSOutputModel(appRefNumber, ActionResult.FAILED, reason, customerId, responseModel, ncrsModel));
            } catch (Exception e) {
                reason = e.getMessage();
                inquiryDate = new Date();
                log.error("NCRS EXCEPTION {}", reason);
                resultImp.updateStatus(appRefNumber,customerId,reason,ActionResult.EXCEPTION);
                outputModelArrayList.add(new NCRSOutputModel(appRefNumber, ActionResult.EXCEPTION, reason, customerId, responseModel, ncrsModel));
            }
        }
        return outputModelArrayList;
    }

    @Override
    public ArrayList<NCRSOutputModel> requestOffline(NCRSInputModel inputModel) throws Exception {
        log.debug("NCRS Call : requestOffline()");
        NCRSResponseModel responseModel = null;

        ArrayList<NCRSModel> ncrsModelArrayList = null;
        TUEFEnquiryIdModel idModel = null;
        TUEFEnquiryNameModel nameModel = null;
        appRefNumber = inputModel.getAppRefNumber();
        userId = inputModel.getUserId();
        CANumber = inputModel.getCANumber();
        referenceTel = inputModel.getReferenceTel();
        String reason = null;
        ArrayList<NCRSOutputModel> responseModelArrayList = new ArrayList<NCRSOutputModel>();
        ncrsModelArrayList = inputModel.getNcrsModelArrayList();
        log.debug("NCRS Check ncb offline {} personals", ncrsModelArrayList.size());

        for (NCRSModel ncrsModel : ncrsModelArrayList) {
            customerId = ncrsModel.getCitizenId();
            customerType = ncrsModel.getIdType();
            customerDocmentType = ncrsModel.getCustomerDocmentType();
            titleNameCode = ncrsModel.getTitleNameCode();
            countryCode = ncrsModel.getCountryCode();
            ncrsModel.setMemberref(resultImp.getRequestNo(appRefNumber, customerId));
            memberref = ncrsModel.getMemberref();
            firstName = ncrsModel.getFirstName();
            lastName = ncrsModel.getLastName();

            idModelArrayList = new ArrayList<TUEFEnquiryIdModel>();
            idModel = new TUEFEnquiryIdModel(customerType, customerId);
            idModelArrayList.add(idModel);
            nameModelArrayList = new ArrayList<TUEFEnquiryNameModel>();
            nameModel = new TUEFEnquiryNameModel(ncrsModel.getLastName(), ncrsModel.getFirstName());
            nameModelArrayList.add(nameModel);
            try {
                responseModel = callOffline(ncrsModel);
                reason = "";
                if (!Util.isNull(responseModel.getBodyModel().getTransaction().getTrackingid())) {
                    reason = responseModel.getBodyModel().getTransaction().getTrackingid();
                    log.debug("NCRS Tracking Id is {}", reason);
                }
                responseModelArrayList.add(new NCRSOutputModel(appRefNumber, ActionResult.SUCCESS, reason, customerId, responseModel, ncrsModel));
            } catch (HttpHostConnectException e) {
                reason = e.getMessage();
                log.error("NCRS FAILED {}", e);
                responseModelArrayList.add(new NCRSOutputModel(appRefNumber, ActionResult.FAILED, reason, customerId, responseModel, ncrsModel));
            } catch (ConnectTimeoutException e) {
                reason = e.getMessage();
                log.error("NCRS FAILED {}", e);
                responseModelArrayList.add(new NCRSOutputModel(appRefNumber, ActionResult.FAILED, reason, customerId, responseModel, ncrsModel));
            } catch (Exception e) {
                reason = e.getMessage();
                log.error("NCRS EXCEPTION {}", e);
                responseModelArrayList.add(new NCRSOutputModel(appRefNumber, ActionResult.EXCEPTION, reason, customerId, responseModel, ncrsModel));
            }
        }
        return responseModelArrayList;
    }


    private NCRSResponseModel callOnline(NCRSModel ncrsModel) throws Exception {
        log.debug("NCRS Call : callOnline()");

        StringBuilder stringBuilder = null;
        String actionDesc = null;
        Date actionDate = null;
        Date resultDate = null;
        String resultDesc = null;
        String linkKey = null;
        NCRSResponseModel responseModel = null;
        linkKey = Util.getLinkKey(userId);
        //TO Add office code when call NCB
        String officeCode = "";
        if(!Util.isEmpty(userId)) {
            User user = userDAO.findById(userId);
            if(!Util.isNull(user))
                officeCode = !Util.isNull(user.getTeam())?user.getTeam().getTeam_code():"";
        }

        stringBuilder = new StringBuilder();
        for (TUEFEnquiryNameModel nameModel : nameModelArrayList) {
            stringBuilder.append(" FirstName = ");
            stringBuilder.append(nameModel.getFirstname());
            stringBuilder.append(" FamilyName = ");
            stringBuilder.append(nameModel.getFamilyname());
        }
        for (TUEFEnquiryIdModel idModel : idModelArrayList) {
            stringBuilder.append(" IdType = ");
            stringBuilder.append(idModel.getIdtype());
            stringBuilder.append(" IdNumber = ");
            stringBuilder.append(idModel.getIdnumber());
        }
        actionDesc = null != stringBuilder ? stringBuilder.toString() : "";
        log.debug("[{}] NCRS requestOnline(NCRSModel : {})", linkKey, actionDesc);
        actionDate = new Date();
        String action = this.action + " " + ONLINE;
        try {
            actionDate = new Date();
            responseModel = checkOnlineResponseModel(request(ncrsModel, ONLINE), officeCode);
            resultDate = new Date();
            log.debug("[{}] NCRS Online audit userId {} action {} actionDesc {} actionDate {} actionResult {} resultDesc {} resultDate {} linkKey {}",
                    linkKey, userId, action, actionDesc, actionDate, ActionResult.SUCCESS, resultDesc, resultDate, linkKey);
            ncbAuditor.add(userId, action, actionDesc, actionDate, ActionResult.SUCCESS, resultDesc, resultDate, linkKey);
            saveNCBI(responseModel, officeCode);
            return responseModel;
        } catch (HttpHostConnectException e) {
            resultDesc = e.getMessage();
            resultDate = new Date();
            log.debug("Exception while call online!",e);
            log.debug("[{}] NCRS Online audit userId {} action {} actionDesc {} actionDate {} actionResult {} resultDesc {} resultDate {} linkKey {}",
                    linkKey, userId, action, actionDesc, actionDate, ActionResult.FAILED, resultDesc, resultDate, linkKey);
            ncbAuditor.add(userId, action, actionDesc, actionDate, ActionResult.FAILED, resultDesc, resultDate, linkKey);
            throw new NCBInterfaceException(e, httpHostException, message.get(httpHostException, resultDesc));
        } catch (ConnectTimeoutException e) {
            resultDesc = e.getMessage();
            resultDate = new Date();
            log.debug("Exception while call online!",e);
            log.debug("[{}] NCRS Online audit userId {} action {} actionDesc {} actionDate {} actionResult {} resultDesc {} resultDate {} linkKey {}",
                    linkKey, userId, action, actionDesc, actionDate, ActionResult.FAILED, resultDesc, resultDate, linkKey);
            ncbAuditor.add(userId, action, actionDesc, actionDate, ActionResult.FAILED, resultDesc, resultDate, linkKey);
            throw new NCBInterfaceException(e, timeOutException, message.get(timeOutException, resultDesc));
        } catch (NCBInterfaceException e) {
            resultDesc = e.getMessage();
            resultDate = new Date();
            log.debug("Exception while call online!",e);
            log.debug("[{}] NCRS Online audit userId {} action {} actionDesc {} actionDate {} actionResult {} resultDesc {} resultDate {} linkKey {}",
                    linkKey, userId, action, actionDesc, actionDate, ActionResult.EXCEPTION, resultDesc, resultDate, linkKey);
            ncbAuditor.add(userId, action, actionDesc, actionDate, ActionResult.EXCEPTION, resultDesc, resultDate, linkKey);
            throw new NCBInterfaceException(e, exception, resultDesc);
        } catch (Exception e) {
            resultDesc = e.getMessage();
            resultDate = new Date();
            log.debug("Exception while call online!",e);
            log.debug("[{}] NCRS Online audit userId {} action {} actionDesc {} actionDate {} actionResult {} resultDesc {} resultDate {} linkKey {}",
                    linkKey, userId, action, actionDesc, actionDate, ActionResult.EXCEPTION, resultDesc, resultDate, linkKey);
            ncbAuditor.add(userId, action, actionDesc, actionDate, ActionResult.EXCEPTION, resultDesc, resultDate, linkKey);
            if(resultDesc==null){
                throw new NCBInterfaceException(e, exception, message.get(exception));
            } else {
                throw new NCBInterfaceException(e, exception, message.get(exception, resultDesc));
            }
        }
    }

    private NCRSResponseModel callOffline(NCRSModel ncrsModel) throws Exception {
        log.debug("NCRS Call : callOffline()");
        StringBuilder stringBuilder = null;
        String actionDesc = null;
        Date actionDate = null;
        Date resultDate = null;
        String resultDesc = null;
        String linkKey = null;
        NCRSResponseModel responseModel = null;
        linkKey = Util.getLinkKey(userId);
        //TO Add office code when call NCB
        String officeCode = "";
        if(!Util.isEmpty(userId)) {
            User user = userDAO.findById(userId);
            if(!Util.isNull(user))
                officeCode = !Util.isNull(user.getTeam())?user.getTeam().getTeam_code():"";
        }

        stringBuilder = new StringBuilder();
        for (TUEFEnquiryNameModel nameModel : nameModelArrayList) {
            stringBuilder.append(" FirstName = ");
            stringBuilder.append(nameModel.getFirstname());
            stringBuilder.append(" FamilyName = ");
            stringBuilder.append(nameModel.getFamilyname());
        }
        for (TUEFEnquiryIdModel idModel : idModelArrayList) {
            stringBuilder.append(" IdType = ");
            stringBuilder.append(idModel.getIdtype());
            stringBuilder.append(" IdNumber = ");
            stringBuilder.append(idModel.getIdnumber());
        }
        actionDesc = null != stringBuilder ? stringBuilder.toString() : "";
        log.debug("[{}] NCRS requestOffline(NCRSModel : {})", linkKey, actionDesc);
        String action = this.action + " " + FIND;
        try {
            actionDate = new Date();
            responseModel = checkOfflineResponseModelTracking(request(ncrsModel, FIND));
            resultDate = new Date();
            log.debug("[{}] NCRS Offline audit userId {} action {} actionDesc {} actionDate {} actionResult {} resultDesc {} resultDate {} linkKey {}",
                    linkKey, userId, action, actionDesc, actionDate, ActionResult.SUCCESS, resultDesc, resultDate, linkKey);
            ncbAuditor.add(userId, action, actionDesc, actionDate, ActionResult.SUCCESS, resultDesc, resultDate, linkKey);
            if (null != responseModel.getBodyModel().getTrackingid()) {
                ArrayList<String> arrayList = responseModel.getBodyModel().getTrackingid();
                if (1 <= arrayList.size()) {
                    log.debug("NCRS The List Tracking ID {}", arrayList);
                    String trackingId = arrayList.get(arrayList.size() - 1);
                    log.debug("NCRS The maximum value of list Tracking ID is {}", trackingId);
                    ncrsModel.setTrackingId(trackingId);
                    actionDate = new Date();
                    actionDesc = trackingId;
                    action = this.action + " " + READ;
                    responseModel = checkOfflineResponseModel(request(ncrsModel, READ), officeCode);
                    if (null != responseModel.getBodyModel().getTransaction().getUser()) {
                        log.debug("NCRS get response offline");
                        String customerId = "";
                        if (idModelArrayList.size() > 0) {
                            TUEFEnquiryIdModel idModel = idModelArrayList.get(idModelArrayList.size()-1);
                            customerId = idModel.getIdnumber();
                        }
                        //responseModel = checkOnlineResponseModel(responseModel);
                        resultDate = new Date();
                        log.debug("[{}] NCRS Offline audit userId {} action {} actionDesc {} actionDate {} actionResult {} resultDesc {} resultDate {} linkKey {}",
                                linkKey, userId, action, actionDesc, actionDate, ActionResult.SUCCESS, resultDesc, resultDate, linkKey);
                        ncbAuditor.add(userId, action, actionDesc, actionDate, ActionResult.SUCCESS, resultDesc, resultDate, linkKey);
                        resultImp.updateSUCCEED(appRefNumber, customerId, trackingId);
                        saveNCBI(responseModel, officeCode); //TODO: to confirm, need to reconcile or not?
                        return responseModel;
                    } else {
                        resultDesc = "NCRS NCB Exception Transaction is null";
                        log.error("NCRS NCB Exception Transaction is null");
                        throw new NCBInterfaceException(new Exception(resultDesc), exception, message.get(exception, resultDesc));
                    }
                } else {
                    log.debug("Tracking did not found");
                    responseModel = callOnline(ncrsModel);
                    String reason = "";
                    if (!Util.isNull(responseModel.getBodyModel().getTransaction().getTrackingid())) {
                        reason = responseModel.getBodyModel().getTransaction().getTrackingid();
                        log.debug("NCRS Tracking Id is {}", reason);
                    }
                    resultImp.updateSUCCEED(appRefNumber, customerId, reason);
                    return responseModel;
                }
            } else {
                log.debug("Tracking did not found");
                responseModel = callOnline(ncrsModel);
                String reason = "";
                if (!Util.isNull(responseModel.getBodyModel().getTransaction().getTrackingid())) {
                    reason = responseModel.getBodyModel().getTransaction().getTrackingid();
                    log.debug("NCRS Tracking Id is {}", reason);
                }
                resultImp.updateSUCCEED(appRefNumber, customerId, reason);
                return responseModel;
            }
        } catch (HttpHostConnectException e) {
            resultDesc = e.getMessage();
            resultDate = new Date();
            log.debug("Exception while call offline!",e);
            log.debug("[{}] NCRS Offline audit userId {} action {} actionDesc {} actionDate {} actionResult {} resultDesc {} resultDate {} linkKey {}",
                    linkKey, userId, action, actionDesc, actionDate, ActionResult.FAILED, resultDesc, resultDate, linkKey);
            ncbAuditor.add(userId, action, actionDesc, actionDate, ActionResult.FAILED, resultDesc, resultDate, linkKey);
            if(resultDesc==null){
                throw new NCBInterfaceException(e, httpHostException, message.get(httpHostException));
            } else {
                throw new NCBInterfaceException(e, httpHostException, message.get(httpHostException, resultDesc));
            }
        } catch (ConnectTimeoutException e) {
            resultDesc = e.getMessage();
            resultDate = new Date();
            log.debug("Exception while call offline!",e);
            log.debug("[{}] NCRS Offline audit userId {} action {} actionDesc {} actionDate {} actionResult {} resultDesc {} resultDate {} linkKey {}",
                    linkKey, userId, action, actionDesc, actionDate, ActionResult.FAILED, resultDesc, resultDate, linkKey);
            ncbAuditor.add(userId, action, actionDesc, actionDate, ActionResult.FAILED, resultDesc, resultDate, linkKey);
            if(resultDesc==null){
                throw new NCBInterfaceException(e, timeOutException, message.get(timeOutException));
            } else {
                throw new NCBInterfaceException(e, timeOutException, message.get(timeOutException, resultDesc));
            }
        } catch (NCBInterfaceException e) {
            resultDesc = e.getMessage();
            resultDate = new Date();
            log.debug("Exception while call offline!",e);
            log.debug("[{}] NCRS Offline audit userId {} action {} actionDesc {} actionDate {} actionResult {} resultDesc {} resultDate {} linkKey {}",
                    linkKey, userId, action, actionDesc, actionDate, ActionResult.EXCEPTION, resultDesc, resultDate, linkKey);
            ncbAuditor.add(userId, action, actionDesc, actionDate, ActionResult.EXCEPTION, resultDesc, resultDate, linkKey);
            if(resultDesc==null){
                throw new NCBInterfaceException(e, exception, resultDesc);
            } else {
                throw new NCBInterfaceException(e, exception, message.get(exception, resultDesc));
            }
        } catch (Exception e) {
            resultDesc = e.getMessage();
            resultDate = new Date();
            log.debug("Exception while call offline!",e);
            log.debug("[{}] NCRS Offline audit userId {} action {} actionDesc {} actionDate {} actionResult {} resultDesc {} resultDate {} linkKey {}",
                    linkKey, userId, action, actionDesc, actionDate, ActionResult.EXCEPTION, resultDesc, resultDate, linkKey);
            ncbAuditor.add(userId, action, actionDesc, actionDate, ActionResult.EXCEPTION, resultDesc, resultDate, linkKey);
            if(resultDesc==null){
                throw new NCBInterfaceException(e, exception, message.get(exception));
            } else {
                throw new NCBInterfaceException(e, exception, message.get(exception, resultDesc));
            }
        }
    }

    private NCRSResponseModel checkOnlineResponseModel(NCRSResponseModel responseModel, String officeCode) throws Exception {
        log.debug("NCRS Call : checkOnlineResponseModel()");
        if (responseModel != null) {
            String resultDesc = responseModel.getHeaderModel().getCommand();
            log.debug("NCRS Result desc {}", resultDesc);
            if (!ERROR.equals(resultDesc)) {
                if(responseModel.getBodyModel().getErrormsg()!=null){
                    //Link down
                    resultDesc = responseModel.getBodyModel().getErrormsg();
                    log.error("NCRS NCB Exception {}", responseModel.getBodyModel().getErrormsg());
                    throw new NCBInterfaceException(new Exception(resultDesc), this.exception, resultDesc);
                } else {
                    if (null != responseModel.getBodyModel().getTransaction().getTueferror()) { //Error
                        StringBuilder exception = new StringBuilder("TUEF Error");
                        TUEFErrorError error = responseModel.getBodyModel().getTransaction().getTueferror().getError();
                        ArrayList<ErrorModel> arrayList = error.getError();
                        for (int i = 0; i < arrayList.size(); i++) {
                            ErrorModel errorModel = arrayList.get(i);
                            exception.append((i + 1)).append(" ").append(errorModel.getDescription()).append(" ");
                        }
                        resultDesc = exception.toString();
                        log.error("NCRS NCB Exception TUEFERROR {}", resultDesc);
                        log.debug("save data to NCBI Export");
                        saveNCBI(responseModel, officeCode);
                        throw new NCBInterfaceException(new Exception(resultDesc), this.exception, resultDesc);
                    } else if (null != responseModel.getBodyModel().getTransaction().getTuefresponse()){
                        return responseModel;
                    } else {
                        //unexpected result;
                        resultDesc = "Unexpected Result From NCRS";
                        log.error("NCRS NCB Exception {}", resultDesc);
                        throw new NCBInterfaceException(new Exception(resultDesc), this.exception, resultDesc);
                    }
                }
            } else {
                resultDesc = responseModel.getBodyModel().getErrormsg();
                log.error("NCRS NCB Exception {}", responseModel.getBodyModel().getErrormsg());
                throw new NCBInterfaceException(new Exception(resultDesc), this.exception, resultDesc);
            }
        } else {
            String resultDesc = "NCRS Response model is null";
            log.error("NCRS Response model is null");
            throw new NCBInterfaceException(new Exception(resultDesc), this.exception, resultDesc);
        }
    }

    private NCRSResponseModel checkOfflineResponseModel(NCRSResponseModel responseModel, String officeCode) throws Exception {
        log.debug("NCRS Call : checkOfflineFindResponseModel()");
        if (responseModel != null) {
            String resultDesc = responseModel.getHeaderModel().getCommand();
            log.debug("NCRS Result desc {}", resultDesc);
            if (!ERROR.equals(resultDesc)) {
                if(responseModel.getBodyModel().getErrormsg()!=null){
                    //Link down
                    resultDesc = responseModel.getBodyModel().getErrormsg();
                    log.error("NCRS NCB Exception {}", responseModel.getBodyModel().getErrormsg());
                    throw new NCBInterfaceException(new Exception(resultDesc), this.exception, resultDesc);
                } else {
                    if (null != responseModel.getBodyModel().getTransaction().getTueferror()) { //Error
                        StringBuilder exception = new StringBuilder("TUEF Error");
                        TUEFErrorError error = responseModel.getBodyModel().getTransaction().getTueferror().getError();
                        ArrayList<ErrorModel> arrayList = error.getError();
                        for (int i = 0; i < arrayList.size(); i++) {
                            ErrorModel errorModel = arrayList.get(i);
                            exception.append((i + 1)).append(" ").append(errorModel.getDescription()).append(" ");
                        }
                        resultDesc = exception.toString();
                        log.error("NCRS NCB Exception TUEFERROR {}", resultDesc);
                        log.debug("save data to NCBI Export");
                        saveNCBI(responseModel, officeCode); //TODO: to confirm, need to reconcile or not?
                        throw new NCBInterfaceException(new Exception(resultDesc), this.exception, resultDesc);
                    } else if (null != responseModel.getBodyModel().getTransaction().getTuefresponse()){
                        return responseModel;
                    } else {
                        //unexpected result;
                        resultDesc = "Unexpected Result From NCRS";
                        log.error("NCRS NCB Exception {}", resultDesc);
                        throw new NCBInterfaceException(new Exception(resultDesc), this.exception, resultDesc);
                    }
                }
            } else {
                resultDesc = responseModel.getBodyModel().getErrormsg();
                log.error("NCRS NCB Exception {}", responseModel.getBodyModel().getErrormsg());
                throw new NCBInterfaceException(new Exception(resultDesc), this.exception, resultDesc);
            }
        } else {
            String resultDesc = "NCRS Response model is null";
            log.error("NCRS Response model is null");
            throw new NCBInterfaceException(new Exception(resultDesc), this.exception, resultDesc);
        }
    }

    private NCRSResponseModel checkOfflineResponseModelTracking(NCRSResponseModel responseModel) throws Exception {
        log.debug("NCRS Call : checkOfflineResponseModelTracking()");
        if (responseModel != null) {
            String resultDesc = responseModel.getHeaderModel().getCommand();
            log.debug("NCRS Result desc {}", resultDesc);
            if (!ERROR.equals(responseModel.getHeaderModel().getCommand())) {
                if(responseModel.getBodyModel().getErrormsg()!=null){
                    //Link down
                    resultDesc = responseModel.getBodyModel().getErrormsg();
                    log.error("NCRS NCB Exception {}", responseModel.getBodyModel().getErrormsg());
                    throw new NCBInterfaceException(new Exception(resultDesc), this.exception, resultDesc);
                } else {
                    return responseModel;
                }
            } else {
                resultDesc = responseModel.getBodyModel().getErrormsg();
                log.error("NCRS NCB Exception {}", responseModel.getBodyModel().getErrormsg());
                throw new NCBInterfaceException(new Exception(resultDesc), this.exception, resultDesc);
            }
        } else {
            String resultDesc = "NCRS Response model is null";
            log.error("NCRS Response model is null");
            throw new NCBInterfaceException(new Exception(resultDesc), this.exception, resultDesc);
        }
    }

    private void saveNCBI(NCRSResponseModel responseModel, String officeCode) throws Exception {
        NCBIExportModel exportModel = new NCBIExportModel();

        exportModel.setOfficeCode(officeCode);  //todo XXX

        exportModel.setRequestNo(memberref);
        exportModel.setStaffId(userId);
        exportModel.setInquiryType("01");//01
        exportModel.setCustomerType("01");//01
        exportModel.setJuristicType(null);
        exportModel.setCustomerDocumentType(customerDocmentType);

        if (!"01".equals(customerDocmentType)) {
            exportModel.setCountryCode(countryCode);
        } else {
            exportModel.setCountryCode("TH");
        }

        exportModel.setTitleCode(titleNameCode);
        exportModel.setCustomerId(customerId);
        exportModel.setFirstName(firstName);
        exportModel.setLastName(lastName);
        exportModel.setJuristicName(null);
        exportModel.setAppRefNumber(appRefNumber);
        exportModel.setCaution(null);
        exportModel.setReferenceTel(referenceTel);
        try {
            exportModel.setInquiryStatus(checkInquiryStatus(responseModel));
        } catch (Exception e) {
            exportModel.setInquiryStatus("02");
        }
        try {
            String date = responseModel.getBodyModel().getTransaction().getEnquirydate();
            exportModel.setInquiryDate(Util.getDateOrTime(date, true));
            exportModel.setInquiryTime(Util.getDateOrTime(date, false));
        } catch (Exception e) {
            Date date = new Date();
            exportModel.setInquiryDate(Util.createDateString(date));
            exportModel.setInquiryTime(Util.createDateString(date, "HH:mm:ss"));
        }
        exportImp.add(exportModel);
    }

    private NCRSRequestModel createOnlineModel(NCRSModel ncrsModel, String command) {
        log.debug("NCRS Call : createOnlineModel()");
        String memberRef = ncrsModel.getMemberref();
        String enqPurpose = ncrsModel.getEnqpurpose();
        String enqAmount = ncrsModel.getEnqamount();
        String consent = ncrsModel.getConsent();
        return new NCRSRequestModel(
                new HeaderModel(id, passwordEncrypt, command),
                new BodyModel(
                        new TUEFEnquiryModel(
                                new TUEFEnquiryHeaderModel(memberRef, enqPurpose, enqAmount, consent),
                                nameModelArrayList, idModelArrayList)));
    }

    private NCRSRequestModel createFindModel(NCRSModel ncrsModel, String command) {
        log.debug("NCRS Call : createFindModel()");
        TUEFEnquiryIdModel listModel = idModelArrayList.get(0);
        return new NCRSRequestModel(
                new HeaderModel(id, passwordEncrypt, command),
                new BodyModel(
                        new CriteriaModel(/*Util.createDateString(new Date(), "yyyyMMdd")*/"", listModel.getIdtype(), listModel.getIdnumber(), id, memberref)));
    }

    private NCRSRequestModel createReadModel(String trackingId, String command) {
        log.debug("NCRS Call : createReadModel()");
        return new NCRSRequestModel(
                new HeaderModel(id, passwordEncrypt, command),
                new BodyModel(trackingId));
    }

    private NCRSResponseModel request(NCRSModel ncrsModel, String command) throws Exception {
        log.debug("NCRS Call : request()");
        log.debug("NCRS Command code : {}", command);
        if (ONLINE.equals(command)) {
            return sendToHTTP(createOnlineModel(ncrsModel, command));
        } else if (FIND.equals(command)) {
            return sendToHTTP(createFindModel(ncrsModel, command));
        } else {
            return sendToHTTP(createReadModel(ncrsModel.getTrackingId(), command));
        }
    }

    private NCRSResponseModel sendToHTTP(NCRSRequestModel ncrsRequest) throws Exception {
        log.debug("NCRS Call : sendToHTTP()");
        NCRSResponseModel ncrsResponse = null;
        String xml = null;
        String result = null;
        XStream xStream = null;

        xStream = new XStream();
        xStream.processAnnotations(NCRSRequestModel.class);
        xml = new String(xStream.toXML(ncrsRequest).getBytes("UTF-8"));
        log.debug("NCRS Request : \n{}", xml);
        int nTimeOut = 60; //sec.
        try {
            nTimeOut = Integer.parseInt(timeOut);
        } catch (Exception ex) {
            log.debug("can not convert time out to integer (Default is 60 second)");
        }

        result = new String(post.sendPost(xml, url, nTimeOut).getBytes("ISO-8859-1"), "UTF-8");
        if (!"".equals(result)) {
            xStream.processAnnotations(NCRSResponseModel.class);
            ncrsResponse = (NCRSResponseModel) xStream.fromXML(result);
            log.debug("NCRS Response : \n{}", xStream.toXML(ncrsResponse));
            return ncrsResponse;
        } else {
            String resultDesc = "NCRS XML response error : " + result;
            log.error("NCRS XML response error : {}", result);
            throw new NCBInterfaceException(new Exception(resultDesc), this.exception, message.get(this.exception, resultDesc));
        }
    }

    private String checkInquiryStatus(NCRSResponseModel responseModel) throws Exception {
        String inquiryStatus = "02";
        try {
            ArrayList<SubjectModel> subjectModelArrayList = responseModel.getBodyModel().getTransaction().getTuefresponse().getSubject();
            for (SubjectModel subjectModel : subjectModelArrayList) {
                if (0 < subjectModel.getAccount().size() && null != subjectModel) {
                    inquiryStatus = "01";
                    return inquiryStatus;
                }
            }
            return inquiryStatus;
        } catch (Exception e) {
            return inquiryStatus;
        }
    }
}
