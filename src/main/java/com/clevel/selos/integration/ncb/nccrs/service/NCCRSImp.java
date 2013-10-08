package com.clevel.selos.integration.ncb.nccrs.service;

import com.clevel.selos.exception.ValidationException;
import com.clevel.selos.integration.NCB;
import com.clevel.selos.integration.ncb.exportncbi.NCBIExportImp;
import com.clevel.selos.integration.ncb.exportncbi.NCBIExportModel;
import com.clevel.selos.integration.ncb.ncbresult.NCBResultImp;
import com.clevel.selos.integration.ncb.nccrs.models.request.*;
import com.clevel.selos.integration.ncb.nccrs.models.response.ActiveAccountsModel;
import com.clevel.selos.integration.ncb.nccrs.models.response.ClosedAccountsModel;
import com.clevel.selos.integration.ncb.nccrs.models.response.NCCRSResponseModel;
import com.clevel.selos.integration.ncb.httppost.Post;
import com.clevel.selos.integration.ncb.nccrs.nccrsmodel.NCCRSInputModel;
import com.clevel.selos.integration.ncb.nccrs.nccrsmodel.NCCRSModel;
import com.clevel.selos.integration.ncb.nccrs.nccrsmodel.NCCRSOutputModel;
import com.clevel.selos.model.ActionResult;
import com.clevel.selos.system.Config;
import com.clevel.selos.system.audit.SystemAuditor;
import com.clevel.selos.system.audit.UserAuditor;
import com.clevel.selos.system.message.Message;
import com.clevel.selos.system.message.ValidationMessage;
import com.clevel.selos.util.Util;
import com.thoughtworks.xstream.XStream;
import org.apache.http.HttpHost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.protocol.HTTP;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.io.Serializable;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.Date;

public class NCCRSImp implements NCCRS, Serializable {
    @Inject
    @NCB
    Logger log;

    @Inject
    Post post;

    @Inject
    @ValidationMessage
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

    private String appRefNumber = null;
    private String userId = null;
    private String CANumber = null;
    private String referenceTel  = null;
    private String registType = null;
    private String registId  = null;
    private String memberRef = null;
    private String companyName = null;
    private String juristicType = null;
    private final String action= "NCCRS";
    private final String ONLINE = "BB01001";
    private final String FIND = "TS01001";
    private final String READ = "TS01002";
    private final String ERROR = "ER01001";

    @Inject
    public NCCRSImp() {
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
        log.debug("NCCRS Check ncb online {} personals",nccrsModelArrayList.size());
        for(NCCRSModel nccrsModel : nccrsModelArrayList){
            registType = nccrsModel.getRegistType();
            registId = nccrsModel.getRegistId();
            companyName = nccrsModel.getCompanyName();
            memberRef = nccrsModel.getMemberRef();
            juristicType = nccrsModel.getJuristicType();
            try {
                responseModel = callOnline(nccrsModel);
                inquiryDate = new Date();
                reason = "";
                if(!Util.isNull(responseModel.getBody().getTransaction().getTrackingid())){
                    reason = responseModel.getBody().getTransaction().getTrackingid();
                    log.debug("NCCRS Tracking Id is {}", reason);
                }
                resultImp.add(appRefNumber, registType, registId, inquiryDate, ActionResult.SUCCEED, reason, memberRef);
                outputModelArrayList.add(new NCCRSOutputModel(appRefNumber, ActionResult.SUCCEED, reason, registId, responseModel));
            } catch (HttpHostConnectException e) {
                reason = e.getMessage();
                inquiryDate = new Date();
                log.error("NCCRS FAILED {}", reason);
                resultImp.add(appRefNumber, registType, registId, inquiryDate, ActionResult.FAILED, reason, memberRef);
                outputModelArrayList.add(new NCCRSOutputModel(appRefNumber, ActionResult.FAILED, reason, registId, responseModel));
            } catch (ConnectTimeoutException e) {
                reason = e.getMessage();
                inquiryDate = new Date();
                log.error("NCCRS FAILED {}", reason);
                resultImp.add(appRefNumber, registType, registId, inquiryDate, ActionResult.FAILED, reason, memberRef);
                outputModelArrayList.add(new NCCRSOutputModel(appRefNumber, ActionResult.FAILED, reason, registId, responseModel));
            } catch (Exception e) {
                reason = e.getMessage();
                inquiryDate = new Date();
                log.error("NCCRS EXCEPTION {}", reason);
                resultImp.add(appRefNumber, registType, registId, inquiryDate, ActionResult.EXCEPTION, reason, memberRef);
                outputModelArrayList.add(new NCCRSOutputModel(appRefNumber, ActionResult.EXCEPTION, reason, registId, responseModel));
            }
        }
        return outputModelArrayList;
    }

    @Override
    public ArrayList<NCCRSOutputModel> requestOffline(NCCRSInputModel inputModel) throws Exception {
        log.debug("NCRS Call : requestOffline()");
        NCCRSResponseModel responseModel = null;

        ArrayList<NCCRSModel> nccrsModelArrayList = null;
        appRefNumber = inputModel.getAppRefNumber();
        userId = inputModel.getUserId();
        CANumber = inputModel.getCANumber();
        referenceTel = inputModel.getReferenceTel();
        String reason = null;
        ArrayList<NCCRSOutputModel> outputModelArrayList = new ArrayList<NCCRSOutputModel>();
        nccrsModelArrayList = inputModel.getNccrsModelArrayList();
        log.debug("NCCRS Check ncb offline {} personals",nccrsModelArrayList.size());
        for(NCCRSModel nccrsModel : nccrsModelArrayList){
            registType = nccrsModel.getRegistType();
            registId = nccrsModel.getRegistId();
            nccrsModel.setMemberRef(resultImp.getRequestNo(appRefNumber, registId));
            memberRef = nccrsModel.getMemberRef();
            companyName = nccrsModel.getCompanyName();
            juristicType = nccrsModel.getJuristicType();
            try {
                if(resultImp.isFAILED(appRefNumber, registId)){
                    responseModel = callOffline(nccrsModel);
                    reason = "";
                    if(!Util.isNull(responseModel.getBody().getTransaction().getTrackingid())){
                        reason = responseModel.getBody().getTransaction().getTrackingid();
                        log.debug("NCCRS Tracking Id is {}", reason);
                    }
                    outputModelArrayList.add(new NCCRSOutputModel(appRefNumber, ActionResult.SUCCEED, reason, registId, responseModel));
                } else if(resultImp.isEXCEPTION(appRefNumber, registId)) {
                    responseModel = callOnline(nccrsModel);
                    reason = "";
                    if(!Util.isNull(responseModel.getBody().getTransaction().getTrackingid())){
                        reason = responseModel.getBody().getTransaction().getTrackingid();
                        log.debug("NCCRS Tracking Id is {}", reason);
                    }
                    resultImp.updateSUCCEED(appRefNumber, registId, reason);
                    outputModelArrayList.add(new NCCRSOutputModel(appRefNumber, ActionResult.SUCCEED, reason, registId, responseModel));
                }
            } catch (HttpHostConnectException e) {
                reason = e.getMessage();
                log.error("NCCRS FAILED {}", reason);
                outputModelArrayList.add(new NCCRSOutputModel(appRefNumber, ActionResult.FAILED, reason, registId, responseModel));
            } catch (ConnectTimeoutException e) {
                reason = e.getMessage();
                log.error("NCCRS FAILED {}", reason);
                outputModelArrayList.add(new NCCRSOutputModel(appRefNumber, ActionResult.FAILED, reason, registId, responseModel));
            } catch (Exception e) {
                reason = e.getMessage();
                log.error("NCCRS EXCEPTION {}", reason);
                outputModelArrayList.add(new NCCRSOutputModel(appRefNumber, ActionResult.EXCEPTION, reason, registId, responseModel));
            }
        }
        return outputModelArrayList;
    }

    private NCCRSResponseModel callOnline(NCCRSModel nccrsModel) throws Exception{
        log.debug("NCCRS Call : callOnline()");

        StringBuilder stringBuilder = null;
        String actionDesc = null;
        Date actionDate = null;
        Date resultDate = null;
        String resultDesc = null;
        String linkKey = null;
        NCCRSResponseModel responseModel = null;
        linkKey = Util.getLinkKey(userId);
        stringBuilder = new StringBuilder();
        stringBuilder.append("companyname = ").append(nccrsModel.getCompanyName());
        stringBuilder.append(" registtype = ").append(nccrsModel.getRegistType());
        stringBuilder.append(" registid = ").append(nccrsModel.getRegistId());
        actionDesc = null!=stringBuilder?stringBuilder.toString():"";
        log.debug("{} NCCRS requestOnline(NCCRSModel : {})",linkKey ,actionDesc);
        actionDate = new Date();
        String action = this.action + " "+ ONLINE;
        try {
            actionDate = new Date();
            responseModel = checkOnlineResponseModel(request(nccrsModel, ONLINE));
            resultDate = new Date();
            log.debug("NCCRS Online audit userId {} action {} actionDesc {} actionDate {} actionResult {} resultDesc {} resultDate {} linkKey {}"  ,
                           userId, action, actionDesc, actionDate, ActionResult.SUCCEED, resultDesc, resultDate, linkKey);
            ncbAuditor.add(userId, action, actionDesc, actionDate, ActionResult.SUCCEED, resultDesc, resultDate, linkKey );
            saveNCBI(responseModel);
            return responseModel;
        } catch (HttpHostConnectException e) {
            resultDesc = e.getMessage();
            resultDate = new Date();
            log.debug("NCCRS Online audit userId {} action {} actionDesc {} actionDate {} actionResult {} resultDesc {} resultDate {} linkKey {}"  ,
                           userId, action, actionDesc, actionDate, ActionResult.FAILED, resultDesc, resultDate, linkKey);
            ncbAuditor.add(userId, action, actionDesc, actionDate, ActionResult.FAILED, resultDesc, resultDate, linkKey);
            throw new HttpHostConnectException(new HttpHost(url), new ConnectException());
        } catch (ConnectTimeoutException e) {
            resultDesc = e.getMessage();
            resultDate = new Date();
            log.debug("NCCRS Online audit userId {} action {} actionDesc {} actionDate {} actionResult {} resultDesc {} resultDate {} linkKey {}"  ,
                           userId, action, actionDesc, actionDate, ActionResult.FAILED, resultDesc, resultDate, linkKey);
            ncbAuditor.add(userId, action, actionDesc, actionDate, ActionResult.FAILED, resultDesc, resultDate, linkKey);
            throw new ConnectTimeoutException(e.getMessage());
        } catch (Exception e) {
            resultDesc = e.getMessage();
            resultDate = new Date();
            log.debug("NCCRS Online audit userId {} action {} actionDesc {} actionDate {} actionResult {} resultDesc {} resultDate {} linkKey {}"  ,
                           userId, action, actionDesc, actionDate, ActionResult.EXCEPTION, resultDesc, resultDate, linkKey);
            ncbAuditor.add(userId, action, actionDesc, actionDate, ActionResult.EXCEPTION, resultDesc, resultDate, linkKey);
            throw new Exception(e.getMessage());
        }
    }
    private NCCRSResponseModel callOffline(NCCRSModel nccrsModel) throws Exception{
        log.debug("NCCRS Call : callOffline()");
        StringBuilder stringBuilder = null;
        String actionDesc = null;
        Date actionDate = null;
        Date resultDate = null;
        String resultDesc = null;
        String linkKey = null;
        NCCRSResponseModel responseModel = null;
        linkKey = Util.getLinkKey(userId);
        stringBuilder = new StringBuilder();
        stringBuilder.append("companyname = ").append(nccrsModel.getCompanyName());
        stringBuilder.append(" registtype = ").append(nccrsModel.getRegistType());
        stringBuilder.append(" registid = ").append(nccrsModel.getRegistId());
        actionDesc = null!=stringBuilder?stringBuilder.toString():"";
        log.debug("{} NCCRS requestOffline(NCCRSModel : {})",linkKey ,actionDesc);
        actionDate = new Date();
        String action = this.action + " "+ FIND;
        try {
            actionDate = new Date();
            responseModel = checkOfflineResponseModel(request(nccrsModel, FIND));
            resultDate = new Date();
            log.debug("NCCRS Offline audit userId {} action {} actionDesc {} actionDate {} actionResult {} resultDesc {} resultDate {} linkKey {}"  ,
                           userId, action, actionDesc, actionDate, ActionResult.SUCCEED, resultDesc, resultDate, linkKey);
            ncbAuditor.add(userId, action, actionDesc, actionDate, ActionResult.SUCCEED, resultDesc, resultDate, linkKey);
            if(null!=responseModel.getBody().getTrackingid()){
                ArrayList<String> arrayList = responseModel.getBody().getTrackingid();
                if(1<=arrayList.size()){
                    log.debug("NCCRS The List Tracking ID {}", arrayList);
                    String trackingId = arrayList.get(arrayList.size()-1);
                    log.debug("NCCRS The maximum value of list Tracking ID is {}", trackingId);
                    nccrsModel.setTrackingId(trackingId);
                    actionDate = new Date();
                    actionDesc = trackingId;
                    action = this.action + " "+ READ;
                    responseModel = checkOfflineResponseModel(request(nccrsModel, READ));
                    if(null != responseModel.getBody().getTransaction().getUser()){
                        log.debug("NCCRS get response offline");
                        String registId = nccrsModel.getRegistId();
                        responseModel = checkOnlineResponseModel(responseModel);
                        resultDate = new Date();
                        log.debug("NCCRS Offline audit userId {} action {} actionDesc {} actionDate {} actionResult {} resultDesc {} resultDate {} linkKey {}"  ,
                                       userId, action, actionDesc, actionDate, ActionResult.SUCCEED, resultDesc, resultDate, linkKey);
                        ncbAuditor.add(userId, action, actionDesc, actionDate, ActionResult.SUCCEED, resultDesc, resultDate, linkKey);
                        resultImp.updateSUCCEED(appRefNumber, registId, trackingId);
                        saveNCBI(responseModel);
                        return responseModel;
                    } else {
                        log.error("NCCRS NCB Exception Transaction is null");
                        throw new Exception("NCCRS NCB Exception Transaction is null");
                    }
                } else {
                    return checkOnlineResponseModel(callOnline(nccrsModel));
                }
            } else {
                log.error("Matched transaction did not found");
                throw new Exception("Matched transaction did not found");
            }
        } catch (HttpHostConnectException e) {
            resultDesc = e.getMessage();
            resultDate = new Date();
            log.debug("NCCRS Offline audit userId {} action {} actionDesc {} actionDate {} actionResult {} resultDesc {} resultDate {} linkKey {}"  ,
                           userId, action, actionDesc, actionDate, ActionResult.FAILED, resultDesc, resultDate, linkKey);
            ncbAuditor.add(userId, action, actionDesc, actionDate, ActionResult.FAILED, resultDesc, resultDate, linkKey);
            throw new HttpHostConnectException(new HttpHost(url), new ConnectException());
        } catch (ConnectTimeoutException e) {
            resultDesc = e.getMessage();
            resultDate = new Date();
            log.debug("NCCRS Offline audit userId {} action {} actionDesc {} actionDate {} actionResult {} resultDesc {} resultDate {} linkKey {}"  ,
                           userId, action, actionDesc, actionDate, ActionResult.FAILED, resultDesc, resultDate, linkKey);
            ncbAuditor.add(userId, action, actionDesc, actionDate, ActionResult.FAILED, resultDesc, resultDate, linkKey);
            throw new ConnectTimeoutException(e.getMessage());
        } catch (Exception e) {
            resultDesc = e.getMessage();
            resultDate = new Date();
            log.debug("NCCRS Offline audit userId {} action {} actionDesc {} actionDate {} actionResult {} resultDesc {} resultDate {} linkKey {}"  ,
                           userId, action, actionDesc, actionDate, ActionResult.EXCEPTION, resultDesc, resultDate, linkKey);
            ncbAuditor.add(userId, action, actionDesc, actionDate, ActionResult.EXCEPTION, resultDesc, resultDate, linkKey);
            throw new Exception(e.getMessage());
        }


    }
    private NCCRSResponseModel checkOnlineResponseModel(NCCRSResponseModel responseModel) throws Exception{
        log.debug("NCCRS Call : checkOnlineResponseModel()");
        if (null != responseModel){
            String resultDesc = responseModel.getHeader().getCommand();
            log.debug("NCCRS Result desc {}", resultDesc);
            if (!ERROR.equals(resultDesc)){
                if(null == responseModel.getBody().getTransaction().getH2herror()/*.getErrormsg()*/){
                    return responseModel;
                } else {
                    log.error("NCCRS NCB Exception H2HERROR {}",responseModel.getBody().getTransaction().getH2herror().getErrormsg());
                    throw new Exception(responseModel.getBody().getTransaction().getH2herror().getErrormsg());
                }
            } else {
                log.error("NCCRS NCB Exception {}" ,responseModel.getBody().getErrormsg());
                throw new Exception("NCCRS NCB Exception "+ responseModel.getBody().getErrormsg());
            }
        } else {
            log.error("NCCRS Response model is null");
            throw new Exception("NCCRS Response model is null");
        }
    }
    private NCCRSResponseModel checkOfflineResponseModel(NCCRSResponseModel responseModel) throws Exception{
        log.debug("NCCRS Call : checkOnlineResponseModel()");
        if(responseModel != null){
            if(!ERROR.equals(responseModel.getHeader().getCommand())){
                return responseModel;
            } else {
                log.error("NCCRS NCB Exception {}" ,responseModel.getBody().getErrormsg());
                throw new Exception("NCCRS NCB Exception {}"+responseModel.getBody().getErrormsg());
            }
        } else {
            log.error("NCCRS Response model is null");
            throw new Exception("NCCRS Response model is null");
        }
    }
    private void saveNCBI(NCCRSResponseModel responseModel) throws Exception{
        NCBIExportModel exportModel = new NCBIExportModel();

        exportModel.setOfficeCode("010");

        exportModel.setRequestNo(memberRef);
        exportModel.setStaffId(userId);

        exportModel.setInquiryType("01");
        exportModel.setCustomerType("02");
        exportModel.setCustomerDocumentType("05");//
        exportModel.setJuristicType(juristicType);
        exportModel.setCustomerId(registId);
        exportModel.setCountryCode("01");
        exportModel.setTitleCode(null);
        exportModel.setFirstName(null);
        exportModel.setLastName(null);
        exportModel.setJuristicName(companyName);
        exportModel.setCaNumber(CANumber);
        exportModel.setCaution(null);
        exportModel.setReferenceTel(referenceTel);

        try {
            ActiveAccountsModel activeaccounts = responseModel.getBody().getTransaction().getH2hresponse().getSubject().getActiveaccounts();
            ClosedAccountsModel closedAccountsModel = responseModel.getBody().getTransaction().getH2hresponse().getSubject().getClosedaccounts();
            if (null != activeaccounts || null != closedAccountsModel){
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
    private NCCRSRequestModel createOnlineModel(NCCRSModel nccrsModel, String command){
        log.debug("NCCRS Call : createOnlineModel()");
        String inqPurose  = nccrsModel.getInqPurose();
        String productType = nccrsModel.getProductType();
        String confirmConsent = nccrsModel.getConfirmConsent();
        String language = nccrsModel.getLanguage();
        String historicalBalanceReport  = nccrsModel.getHistoricalBalanceReport();

        return new NCCRSRequestModel(
                new HeaderModel(id,pass,command),
                new BodyModel(
                        new H2HRequestModel(registType, registId, companyName,
                                inqPurose, productType, memberRef,
                                confirmConsent, language),
                        new AttributeModel(historicalBalanceReport)));
    }
    private NCCRSRequestModel createFindModel(NCCRSModel nccrsModel, String command){
        log.debug("NCCRS Call : createFindModel()");
        return new NCCRSRequestModel(
               new HeaderModel(id, pass, command),
               new BodyModel(
                       new CriteriaModel(Util.createDateString(new Date(),"YYYYMMdd"), nccrsModel.getRegistId(), id)));
    }
    private NCCRSRequestModel createReadModel(String trackingId, String command){
        log.debug("NCCRS Call : createReadModel()");
        return new NCCRSRequestModel(
                new HeaderModel(id, pass, command),
                new BodyModel(trackingId));
    }
    private NCCRSResponseModel request(NCCRSModel nccrsModel, String command)throws Exception{
        log.debug("NCCRS Call : request()");
        log.debug("NCCRS Command code : {}",command);
        if(ONLINE.equals(command)){
            return sendToHTTP(createOnlineModel(nccrsModel, command));
        } else if(FIND.equals(command)) {
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
        xml = new String(xStream.toXML(nccrsRequest).getBytes(HTTP.UTF_8));
        log.debug("NCCRS Request : \n{}",xml);
        result = new String(post.sendPost(xml, url, Integer.parseInt(timeOut)).getBytes(HTTP.ISO_8859_1), HTTP.UTF_8);
        String res = "<ncrsresponse>";
        int pointer = result.indexOf(res);
        result = result.replace(result.substring(0, pointer), "");
        if(!"".equals(result)){
            xStream.processAnnotations(NCCRSResponseModel.class);
            nccrsResponse = (NCCRSResponseModel)xStream.fromXML(result);
            log.debug("NCCRS Response : \n{}",xStream.toXML(nccrsResponse));
            return nccrsResponse;
        }else{
            log.error("NCCRS XML response error : {}", result);
            throw new Exception("XML response error");
        }
    }
}
