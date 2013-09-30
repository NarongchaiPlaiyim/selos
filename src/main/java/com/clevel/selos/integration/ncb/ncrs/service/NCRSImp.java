package com.clevel.selos.integration.ncb.ncrs.service;


import com.clevel.selos.integration.NCB;
import com.clevel.selos.integration.ncb.ncbresult.NCBResultImp;
import com.clevel.selos.integration.ncb.exportncbi.NCBIExportImp;
import com.clevel.selos.integration.ncb.exportncbi.NCBIExportModel;
import com.clevel.selos.integration.ncb.httppost.Post;
import com.clevel.selos.integration.ncb.ncrs.models.request.*;
import com.clevel.selos.integration.ncb.ncrs.models.request.BodyModel;
import com.clevel.selos.integration.ncb.ncrs.models.request.HeaderModel;
import com.clevel.selos.integration.ncb.ncrs.models.request.TUEFEnquiryHeaderModel;
import com.clevel.selos.integration.ncb.ncrs.models.request.TUEFEnquiryIdModel;
import com.clevel.selos.integration.ncb.ncrs.models.request.TUEFEnquiryModel;
import com.clevel.selos.integration.ncb.ncrs.models.request.TUEFEnquiryNameModel;
import com.clevel.selos.integration.ncb.ncrs.models.response.*;

import com.clevel.selos.integration.ncb.ncrs.ncrsmodel.NCRSInputModel;
import com.clevel.selos.integration.ncb.ncrs.ncrsmodel.NCRSModel;
import com.clevel.selos.integration.ncb.ncrs.ncrsmodel.NCRSOutputModel;
import org.apache.http.HttpHost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.HttpHostConnectException;
import com.clevel.selos.integration.ncb.vaildation.ValidationImp;
import com.clevel.selos.model.ActionResult;
import com.clevel.selos.system.Config;
import com.clevel.selos.system.audit.SystemAuditor;
import com.clevel.selos.system.audit.UserAuditor;
import com.clevel.selos.system.message.Message;
import com.clevel.selos.system.message.ValidationMessage;
import com.clevel.selos.util.Util;
import com.thoughtworks.xstream.XStream;
import javax.inject.Inject;
import java.io.Serializable;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.Date;

import org.apache.http.protocol.HTTP;
import org.slf4j.Logger;

public class NCRSImp implements NCRS, Serializable{
    @Inject
    @NCB
    Logger log;

    @Inject
    Post post;

    @Inject
    @ValidationMessage
    Message message;

    @Inject
    ValidationImp validationImp;

    @Inject
    @Config(name = "interface.ncb.ncrs.user")
    private String id;

    @Inject
    @Config(name = "interface.ncb.ncrs.pass")
    private String pass;

    @Inject
    @Config(name = "interface.ncb.ncrs.address")
    private String url;

    @Inject
    @Config(name = "interface.ncb.ncrs.timeOut")
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

    ArrayList<TUEFEnquiryIdModel> idModelArrayList = null;
    ArrayList<TUEFEnquiryNameModel> nameModelArrayList = null;

    private String appRefNumber = null;
    private String userId = null;
    private final String action= "NCRS";
    private final String ONLINE = "BB01001";
    private final String FIND = "TS01001";
    private final String READ = "TS01002";
    private final String ERROR = "ER01001";
//    private int retry = 0;
    @Inject
    public NCRSImp() {
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
        String customerType = null;
        String customerId = null;
        String reason = null;
        Date inquiryDate = null;
        ArrayList<NCRSOutputModel> outputModelArrayList = new ArrayList<NCRSOutputModel>();
        ncrsModelArrayList = inputModel.getNcrsModelArrayList();
        log.debug("NCRS Check ncb online {} personals",ncrsModelArrayList.size());
        for(NCRSModel ncrsModel : ncrsModelArrayList ){
            customerType = ncrsModel.getIdType();
            customerId = ncrsModel.getCitizenId();
            idModelArrayList = new ArrayList<TUEFEnquiryIdModel>();
            idModel = new TUEFEnquiryIdModel(customerType, customerId);
            idModelArrayList.add(idModel);
            nameModelArrayList = new ArrayList<TUEFEnquiryNameModel>();
            nameModel = new TUEFEnquiryNameModel(ncrsModel.getLastName(), ncrsModel.getFirstName());
            nameModelArrayList.add(nameModel);
            try{
                responseModel = callOnline(ncrsModel);
                inquiryDate = new Date();
                reason = "";
                if(!Util.isNull(responseModel.getBodyModel().getTransaction().getTrackingid())){
                    reason = responseModel.getBodyModel().getTransaction().getTrackingid();
                    log.debug("NCRS Tracking Id is {}", reason);
                }
                resultImp.add(appRefNumber, customerType, customerId, inquiryDate, ActionResult.SUCCEED, reason);
                outputModelArrayList.add(new NCRSOutputModel(appRefNumber, ActionResult.SUCCEED, reason, customerId, responseModel));
            } catch (HttpHostConnectException e) {
                reason = e.getMessage();
                inquiryDate = new Date();
                log.error("NCRS FAILED {}", reason);
                resultImp.add(appRefNumber, customerType, customerId, inquiryDate, ActionResult.FAILED, reason);
                outputModelArrayList.add(new NCRSOutputModel(appRefNumber, ActionResult.FAILED, reason, customerId, responseModel));
            } catch (ConnectTimeoutException e) {
                reason = e.getMessage();
                inquiryDate = new Date();
                log.error("NCRS FAILED {}", reason);
                resultImp.add(appRefNumber, customerType, customerId, inquiryDate, ActionResult.FAILED, reason);
                outputModelArrayList.add(new NCRSOutputModel(appRefNumber, ActionResult.FAILED, reason, customerId, responseModel));

            } catch (Exception e) {
                reason = e.getMessage();
                inquiryDate = new Date();
                log.error("NCRS EXCEPTION {}", reason);
                resultImp.add(appRefNumber, customerType, customerId, inquiryDate, ActionResult.EXCEPTION, reason);
                outputModelArrayList.add(new NCRSOutputModel(appRefNumber, ActionResult.EXCEPTION, reason, customerId, responseModel));
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
        String customerType = null;
        String customerId = null;
        String reason = null;
        ArrayList<NCRSOutputModel> responseModelArrayList = new ArrayList<NCRSOutputModel>();
        ncrsModelArrayList = inputModel.getNcrsModelArrayList();
        log.debug("NCRS Check ncb offline {} personals",ncrsModelArrayList.size());

        for(NCRSModel ncrsModel : ncrsModelArrayList ){
            customerId = ncrsModel.getCitizenId();
            customerType = ncrsModel.getIdType();
            idModelArrayList = new ArrayList<TUEFEnquiryIdModel>();
            idModel = new TUEFEnquiryIdModel(customerType, customerId);
            idModelArrayList.add(idModel);
            nameModelArrayList = new ArrayList<TUEFEnquiryNameModel>();
            nameModel = new TUEFEnquiryNameModel(ncrsModel.getLastName(), ncrsModel.getFirstName());
            nameModelArrayList.add(nameModel);

            try {
                if(resultImp.isFAILED(appRefNumber, customerId)){
                    responseModel = callOffline(ncrsModel);
                    reason = "";
                    if(!Util.isNull(responseModel.getBodyModel().getTransaction().getTrackingid())){
                        reason = responseModel.getBodyModel().getTransaction().getTrackingid();
                        log.debug("NCRS Tracking Id is {}", reason);
                    }
                    responseModelArrayList.add(new NCRSOutputModel(appRefNumber, ActionResult.SUCCEED, reason, customerId, responseModel));
                } else if(resultImp.isEXCEPTION(appRefNumber, customerId)) {
                    responseModel = callOnline(ncrsModel);
                    reason = "";
                    if(!Util.isNull(responseModel.getBodyModel().getTransaction().getTrackingid())){
                        reason = responseModel.getBodyModel().getTransaction().getTrackingid();
                        log.debug("NCRS Tracking Id is {}", reason);
                    }
                    log.debug("_________________________________100");
                    responseModelArrayList.add(new NCRSOutputModel(appRefNumber, ActionResult.SUCCEED, reason, customerId, responseModel));
                }
            } catch (HttpHostConnectException e) {
                reason = e.getMessage();
                log.error("NCCRS FAILED {}", reason);
                responseModelArrayList.add(new NCRSOutputModel(appRefNumber, ActionResult.FAILED, reason, customerId, responseModel));
            } catch (ConnectTimeoutException e) {
                reason = e.getMessage();
                log.error("NCCRS FAILED {}", reason);
                responseModelArrayList.add(new NCRSOutputModel(appRefNumber, ActionResult.FAILED, reason, customerId, responseModel));
            } catch (Exception e) {
                reason = e.getMessage();
                log.error("NCCRS EXCEPTION {}", reason);
                responseModelArrayList.add(new NCRSOutputModel(appRefNumber, ActionResult.EXCEPTION, reason, customerId, responseModel));
            }
        }
        return responseModelArrayList;
    }



    private NCRSResponseModel callOnline(NCRSModel ncrsModel) throws Exception{
        log.debug("NCRS Call : callOnline()");

        StringBuilder stringBuilder = null;
        String actionDesc = null;
        Date actionDate = null;
        Date resultDate = null;
        String resultDesc = null;
        String linkKey = null;
        NCRSResponseModel responseModel = null;
        linkKey = Util.getLinkKey(userId);

        stringBuilder = new StringBuilder();
        for(TUEFEnquiryNameModel nameModel : nameModelArrayList){
            stringBuilder.append(" FirstName = ");
            stringBuilder.append(nameModel.getFirstname());
            stringBuilder.append(" FamilyName = ");
            stringBuilder.append(nameModel.getFamilyname());
        }
        for(TUEFEnquiryIdModel idModel : idModelArrayList){
            stringBuilder.append(" IdType = ");
            stringBuilder.append(idModel.getIdtype());
            stringBuilder.append(" IdNumber = ");
            stringBuilder.append(idModel.getIdnumber());
        }
        actionDesc = null!=stringBuilder?stringBuilder.toString():"";
        log.debug("{} NCRS requestOnline(NCRSModel : {})",linkKey ,actionDesc);
        actionDate = new Date();
        String action = this.action + " "+ ONLINE;
        try {
            actionDate = new Date();
            responseModel = checkOnlineResponseModel(request(ncrsModel, ONLINE));
            resultDate = new Date();
            log.debug("NCRS Online audit userId {} action {} actionDesc {} actionDate {} actionResult {} resultDesc {} resultDate {} linkKey {}"  ,
                           userId, action, actionDesc, actionDate, ActionResult.SUCCEED, resultDesc, resultDate, linkKey);
            ncbAuditor.add(userId, action, actionDesc, actionDate, ActionResult.SUCCEED, resultDesc, resultDate, linkKey );
            saveNCBI(responseModel);
            return responseModel;
        } catch (HttpHostConnectException e) {
            resultDesc = e.getMessage();
            resultDate = new Date();
            log.debug("NCRS Online audit userId {} action {} actionDesc {} actionDate {} actionResult {} resultDesc {} resultDate {} linkKey {}"  ,
                           userId, action, actionDesc, actionDate, ActionResult.FAILED, resultDesc, resultDate, linkKey);
            ncbAuditor.add(userId, action, actionDesc, actionDate, ActionResult.FAILED, resultDesc, resultDate, linkKey);
            throw new HttpHostConnectException(new HttpHost(url), new ConnectException());
        } catch (ConnectTimeoutException e) {
            log.debug("NCRS Online audit userId {} action {} actionDesc {} actionDate {} actionResult {} resultDesc {} resultDate {} linkKey {}"  ,
                           userId, action, actionDesc, actionDate, ActionResult.FAILED, resultDesc, resultDate, linkKey);
            ncbAuditor.add(userId, action, actionDesc, actionDate, ActionResult.FAILED, resultDesc, resultDate, linkKey);
            throw new ConnectTimeoutException(e.getMessage());
        } catch (Exception e) {
            log.debug("NCRS Online audit userId {} action {} actionDesc {} actionDate {} actionResult {} resultDesc {} resultDate {} linkKey {}"  ,
                           userId, action, actionDesc, actionDate, ActionResult.EXCEPTION, resultDesc, resultDate, linkKey);
            ncbAuditor.add(userId, action, actionDesc, actionDate, ActionResult.EXCEPTION, resultDesc, resultDate, linkKey);
            throw new Exception(e.getMessage());
        }
    }
    private NCRSResponseModel callOffline(NCRSModel ncrsModel) throws Exception{
        log.debug("NCRS Call : callOffline()");
        StringBuilder stringBuilder = null;
        String actionDesc = null;
        Date actionDate = null;
        Date resultDate = null;
        String resultDesc = null;
        String linkKey = null;
        NCRSResponseModel responseModel = null;
        linkKey = Util.getLinkKey(userId);

        stringBuilder = new StringBuilder();
        for(TUEFEnquiryNameModel nameModel : nameModelArrayList){
            stringBuilder.append(" FirstName = ");
            stringBuilder.append(nameModel.getFirstname());
            stringBuilder.append(" FamilyName = ");
            stringBuilder.append(nameModel.getFamilyname());
        }
        for(TUEFEnquiryIdModel idModel : idModelArrayList){
            stringBuilder.append(" IdType = ");
            stringBuilder.append(idModel.getIdtype());
            stringBuilder.append(" IdNumber = ");
            stringBuilder.append(idModel.getIdnumber());
        }
        actionDesc = null!=stringBuilder?stringBuilder.toString():"";
        log.debug("{} NCRS requestOffline(NCRSModel : {})",linkKey ,actionDesc);
        String action = this.action + " "+ FIND;
        try {
            actionDate = new Date();
            responseModel = checkOfflineResponseModel(request(ncrsModel, FIND));
            resultDate = new Date();
            log.debug("NCRS Offline audit userId {} action {} actionDesc {} actionDate {} actionResult {} resultDesc {} resultDate {} linkKey {}"  ,
                           userId, action, actionDesc, actionDate, ActionResult.SUCCEED, resultDesc, resultDate, linkKey);
            ncbAuditor.add(userId, action, actionDesc, actionDate, ActionResult.SUCCEED, resultDesc, resultDate, linkKey);
            if(null!=responseModel.getBodyModel().getTrackingid()){
                ArrayList<String> arrayList = responseModel.getBodyModel().getTrackingid();
                if(1<=arrayList.size()){
                    log.debug("NCRS The List Tracking ID {}", arrayList);
                    String trackingId = arrayList.get(arrayList.size()-1);
                    log.debug("NCRS The maximum value of list Tracking ID is {}", trackingId);
                    ncrsModel.setTrackingId(trackingId);
                    actionDate = new Date();
                    actionDesc = trackingId;
                    action = this.action + " "+ READ;
                    responseModel = checkOfflineResponseModel(request(ncrsModel, READ));
                    if(null != responseModel.getBodyModel().getTransaction().getUser()){
                        log.debug("NCRS get response offline");
                        String customerId = "";
                        if(1<=idModelArrayList.size()){
                            TUEFEnquiryIdModel idModel = idModelArrayList.get(0);
                            customerId = idModel.getIdnumber();
                        }
                        responseModel = checkOnlineResponseModel(responseModel);
                        resultDate = new Date();
                        log.debug("NCRS Offline audit userId {} action {} actionDesc {} actionDate {} actionResult {} resultDesc {} resultDate {} linkKey {}"  ,
                                userId, action, actionDesc, actionDate, ActionResult.SUCCEED, resultDesc, resultDate, linkKey);
                        ncbAuditor.add(userId, action, actionDesc, actionDate, ActionResult.SUCCEED, resultDesc, resultDate, linkKey);
                        resultImp.updateSUCCEED(appRefNumber, customerId, trackingId);
                        saveNCBI(responseModel);
                        return responseModel;
                    } else {
                        log.error("NCRS NCB Exception Transaction is null");
                        throw new Exception("NCRS NCB Exception Transaction is null");
                    }
                } else {
                    return checkOnlineResponseModel(callOnline(ncrsModel));
                }
            } else {
                log.error("Matched transaction did not found");
                throw new Exception("Matched transaction did not found");
            }
        } catch (HttpHostConnectException e) {
            resultDesc = e.getMessage();
            resultDate = new Date();
            log.debug("NCRS Offline audit userId {} action {} actionDesc {} actionDate {} actionResult {} resultDesc {} resultDate {} linkKey {}"  ,
                           userId, action, actionDesc, actionDate, ActionResult.FAILED, resultDesc, resultDate, linkKey);
            ncbAuditor.add(userId, action, actionDesc, actionDate, ActionResult.FAILED, resultDesc, resultDate, linkKey);
            throw new HttpHostConnectException(new HttpHost(url), new ConnectException());
        } catch (ConnectTimeoutException e) {
            resultDesc = e.getMessage();
            resultDate = new Date();
            log.debug("NCRS Offline audit userId {} action {} actionDesc {} actionDate {} actionResult {} resultDesc {} resultDate {} linkKey {}"  ,
                           userId, action, actionDesc, actionDate, ActionResult.FAILED, resultDesc, resultDate, linkKey);
            ncbAuditor.add(userId, action, actionDesc, actionDate, ActionResult.FAILED, resultDesc, resultDate, linkKey);
            throw new ConnectTimeoutException(e.getMessage());
        } catch (Exception e) {
            resultDesc = e.getMessage();
            resultDate = new Date();
            log.debug("NCRS Offline audit userId {} action {} actionDesc {} actionDate {} actionResult {} resultDesc {} resultDate {} linkKey {}"  ,
                           userId, action, actionDesc, actionDate, ActionResult.EXCEPTION, resultDesc, resultDate, linkKey);
            ncbAuditor.add(userId, action, actionDesc, actionDate, ActionResult.EXCEPTION, resultDesc, resultDate, linkKey);
            throw new Exception(e.getMessage());
        }
    }
    private NCRSResponseModel checkOnlineResponseModel(NCRSResponseModel responseModel) throws Exception{
        log.debug("NCRS Call : checkOnlineResponseModel()");
        if(responseModel != null){
            String resultDesc = responseModel.getHeaderModel().getCommand();
            log.debug("NCRS Result desc {}", resultDesc);
            if(!ERROR.equals(resultDesc)){
                if(null==responseModel.getBodyModel().getTransaction().getTueferror()){
                    return responseModel;
                } else {
                    StringBuilder exception = new StringBuilder("TUEF Error");
                    TUEFErrorError error =  responseModel.getBodyModel().getTransaction().getTueferror().getError();
                    ArrayList<ErrorModel> arrayList =  error.getError();
                    for (int i = 0; i<arrayList.size();i++){
                        ErrorModel errorModel = arrayList.get(i);
                        exception.append((i+1)).append(" ").append(errorModel.getDescription()).append(" ");
                    }
                    log.error("NCRS NCB Exception TUEFERROR {}",null!=exception?exception.toString():"");
                    throw new Exception(null!=exception?exception.toString():"");
                }
            } else {
                log.error("NCRS NCB Exception {}" ,responseModel.getBodyModel().getErrormsg());
                throw new Exception("NCRS NCB Exception "+responseModel.getBodyModel().getErrormsg());
            }
        } else {
                log.error("NCRS Response model is null");
                throw new Exception("NCRS Response model is null");
        }
    }
    private NCRSResponseModel checkOfflineResponseModel(NCRSResponseModel responseModel) throws Exception{
        log.debug("NCRS Call : checkOfflineFindResponseModel()");
        if(responseModel != null){
            if(!ERROR.equals(responseModel.getHeaderModel().getCommand())){
                return responseModel;
            } else {
                log.error("NCRS NCB Exception {}" ,responseModel.getBodyModel().getErrormsg());
                throw new Exception("NCRS NCB Exception {}"+responseModel.getBodyModel().getErrormsg());
            }
        } else {
            log.error("NCRS Response model is null");
            throw new Exception("NCRS Response model is null");
        }
    }
    private void saveNCBI(NCRSResponseModel responseModel) throws Exception{
        //NCBI
        NCBIExportModel exportModel = new NCBIExportModel();
        exportModel.setStaffId("01");
        exportModel.setRequestNo("01");
        exportModel.setInquiryType("01");
        exportModel.setCustomerType("01");
        exportModel.setCustomerDocumentType("01");
        exportModel.setJuristicType("01");
        exportModel.setCustomerId("01");
        exportModel.setCountryCode("01");
        exportModel.setTitleCode("01");
        ArrayList<com.clevel.selos.integration.ncb.ncrs.models.response.TUEFEnquiryNameModel> test =  responseModel.getBodyModel().getTransaction().getTuefenquiry().getName();
        com.clevel.selos.integration.ncb.ncrs.models.response.TUEFEnquiryNameModel model = test.get(0);
        exportModel.setFirstName(model.getFirstname());
        exportModel.setLastName("01");
        exportModel.setJuristicName("01");
        exportModel.setCaNumber("01");
        exportModel.setCaution("01");
        exportModel.setReferenceTel("01");
        exportModel.setInquiryStatus("01");
        exportModel.setInquiryDate(new Date( ));
        exportModel.setOfficeCode("010");
        exportImp.add(exportModel);
    }
    private NCRSRequestModel createOnlineModel(NCRSModel ncrsModel, String command){
        log.debug("NCRS Call : createOnlineModel()");
        String memberRef = ncrsModel.getMemberref();
        String enqPurpose = ncrsModel.getEnqpurpose();
        String enqAmount = ncrsModel.getEnqamount();
        String consent = ncrsModel.getConsent();
        return new NCRSRequestModel(
                new HeaderModel(id, pass, command),
                new BodyModel(
                        new TUEFEnquiryModel(
                                new TUEFEnquiryHeaderModel(memberRef, enqPurpose, enqAmount,consent),
                                nameModelArrayList, idModelArrayList)));
    }
    private NCRSRequestModel createFindModel(NCRSModel ncrsModel, String command){
        log.debug("NCRS Call : createFindModel()");
        TUEFEnquiryIdModel listModel = idModelArrayList.get(0);
        return new NCRSRequestModel(
                new HeaderModel(id, pass, command),
                new BodyModel(
                        new CriteriaModel(Util.createDateString(new Date(),"yyyyMMdd"), listModel.getIdtype(), listModel.getIdnumber(), id)));
    }
    private NCRSRequestModel createReadModel(String trackingId, String command){
        log.debug("NCRS Call : createReadModel()");
        return new NCRSRequestModel(
                new HeaderModel(id, pass, command),
                new BodyModel(trackingId));
    }
    private NCRSResponseModel request(NCRSModel ncrsModel, String command)throws Exception{
        log.debug("NCRS Call : request()");
        log.debug("NCRS Command code : {}",command);
        if(ONLINE.equals(command)){
            return sendToHTTP(createOnlineModel(ncrsModel, command));
        } else if(FIND.equals(command)) {
            return sendToHTTP(createFindModel(ncrsModel, command));
        } else {
            return sendToHTTP(createReadModel(ncrsModel.getTrackingId(), command));
        }
    }
    private NCRSResponseModel sendToHTTP(NCRSRequestModel ncrsRequest) throws Exception {
        log.debug("NCRS Call : sendToHTTP()");
        NCRSResponseModel ncrsResponse = null;
        String xml = "\n<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
        String result = null;
        XStream xStream = null;

        xStream = new XStream();
        xStream.processAnnotations(NCRSRequestModel.class);
        xml = new String(xml.getBytes(HTTP.UTF_8));
        xml += xStream.toXML(ncrsRequest);
        log.debug("NCRS Request : {}",xml);
        result = post.sendPost(xml, url, Integer.parseInt(timeOut));


        result = post.sendPost(xml, url, Integer.parseInt(timeOut));
        xStream.processAnnotations(NCRSResponseModel.class);
        result = new String(result.getBytes(HTTP.UTF_8));
        ncrsResponse = (NCRSResponseModel)xStream.fromXML(result);
        log.debug("NCRS Response 111: {}\n",xStream.toXML(ncrsResponse));

        result = post.sendPost(callGetString(), url, Integer.parseInt(timeOut));
        xStream.processAnnotations(NCRSResponseModel.class);
        result = new String(result.getBytes(HTTP.UTF_8));
        ncrsResponse = (NCRSResponseModel)xStream.fromXML(result);
        log.debug("NCRS Response 222: {}\n",xStream.toXML(ncrsResponse));

        if(!"".equals(result)){
            xStream.processAnnotations(NCRSResponseModel.class);
            result = new String(result.getBytes(HTTP.UTF_8));
            ncrsResponse = (NCRSResponseModel)xStream.fromXML(result);
            log.debug("NCRS Response : {}\n",xStream.toXML(ncrsResponse));
            return ncrsResponse;
        }else{
            log.error("NCRS XML response error : {}",result);
            throw new Exception("XML response error");
        }
    }


    private static String callGetString(){

        StringBuilder builder = new StringBuilder();
        builder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        builder.append("<ncrsrequest>");
        builder.append("<header>");
        builder.append("<user>SLOSTEST</user>");
        builder.append("<password>SLOSTEST12</password>");
        builder.append("<command>BB01001</command>");
        builder.append("</header>");
        builder.append("<body>");
        builder.append("<tuefenquiry>");
        builder.append("<header>");
        builder.append("<memberref>123456789</memberref>");
        builder.append("<enqpurpose>01</enqpurpose>");
        builder.append("<enqamount>01</enqamount>");
        builder.append("<consent>Y</consent>");
        builder.append("</header>");
        builder.append("<name>");
        builder.append("<familyname>พสุธร</familyname>");
        builder.append("<firstname>กุญชร</firstname>");
        builder.append("</name>");
        builder.append("<id>");
        builder.append("<idtype>01</idtype>");
        builder.append("<idnumber>3100300390029</idnumber>");
        builder.append("</id>");
        builder.append("</tuefenquiry>");
        builder.append("</body>");
        builder.append("</ncrsrequest>");
        return builder.toString();
    }
}
