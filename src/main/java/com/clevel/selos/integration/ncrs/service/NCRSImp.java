package com.clevel.selos.integration.ncrs.service;


import com.clevel.selos.exception.ValidationException;
import com.clevel.selos.integration.NCB;
import com.clevel.selos.integration.nccrs.service.NCBIExportImp;
import com.clevel.selos.integration.nccrs.service.NCBIExportModel;
import com.clevel.selos.integration.ncrs.httppost.Post;
import com.clevel.selos.integration.ncrs.models.request.*;
import com.clevel.selos.integration.ncrs.models.request.BodyModel;
import com.clevel.selos.integration.ncrs.models.request.HeaderModel;
import com.clevel.selos.integration.ncrs.models.request.TUEFEnquiryHeaderModel;
import com.clevel.selos.integration.ncrs.models.request.TUEFEnquiryIdModel;
import com.clevel.selos.integration.ncrs.models.request.TUEFEnquiryModel;
import com.clevel.selos.integration.ncrs.models.request.TUEFEnquiryNameModel;
import com.clevel.selos.integration.ncrs.models.response.*;

import com.clevel.selos.integration.ncrs.ncrsmodel.NCRSInputModel;
import com.clevel.selos.integration.ncrs.ncrsmodel.NCRSModel;
import com.clevel.selos.integration.ncrs.ncrsmodel.ResponseNCRSModel;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.HttpHostConnectException;
import com.clevel.selos.integration.ncrs.vaildation.ValidationImp;
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
import java.util.ArrayList;
import java.util.Date;

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
    private final String action= "NCRS";
    private final String ONLINE = "BB01001";
    private final String FIND = "TS01001";
    private final String READ = "TS01002";
    private final String ERROR = "ER01001";
    private final String SUCCEED = "SUCCEED";
    private final String FAILED = "FAILED";
//    private int retry = 0;
    @Inject
    public NCRSImp() {
    }

    @Override
    public ArrayList<ResponseNCRSModel> requestOnline(NCRSModel ncrsModel) throws Exception {
        log.debug("NCRS Call : requestOnline()");
        NCRSResponseModel responseModel = null;
        ArrayList<NCRSInputModel> inputModel = null;
        TUEFEnquiryIdModel idModel = null;
        TUEFEnquiryNameModel nameModel = null;

        String appRefnumber = ncrsModel.getAppRefNumber();
        String customerType = null;
        String customerId = null;
        String result = null;
        String reason = null;
        Date inquiryDate = null;
        ArrayList<ResponseNCRSModel> responseModelArrayList = new ArrayList<ResponseNCRSModel>();
        inputModel = ncrsModel.getInputModel();
        log.debug("NCRS Check ncb online {} personals",inputModel.size());

        for(NCRSInputModel model : inputModel ){
            customerType = model.getIdType();
            customerId = model.getCitizenId();

            idModelArrayList = new ArrayList<TUEFEnquiryIdModel>();
            idModel = new TUEFEnquiryIdModel(customerType, customerId);
            idModelArrayList.add(idModel);
            nameModelArrayList = new ArrayList<TUEFEnquiryNameModel>();
            nameModel = new TUEFEnquiryNameModel(model.getLastName(), model.getFirstName());
            nameModelArrayList.add(nameModel);

            try{
                responseModel = callOnline(ncrsModel);
                result = SUCCEED;
                inquiryDate = new Date();
                reason = "";
                log.debug("___________________________________________________________555");
                log.debug("NCRS appRefnumber : {} customerType : {} customerId : {} inquiryDate : {}  result : {}",
                        appRefnumber, customerType, customerId, inquiryDate, result);
                resultImp.add(appRefnumber, customerType, customerId, new Date(), result, reason);
                responseModelArrayList.add(new ResponseNCRSModel(appRefnumber, result, reason, customerId, responseModel));
            } catch (HttpHostConnectException e) {
                result = FAILED;
                reason = e.getMessage();
                log.debug("___________________________________________________________666");
                responseModelArrayList.add(new ResponseNCRSModel(appRefnumber, result, reason, customerId, responseModel));
                log.error("NCRS error {}", reason);
                throw new ValidationException(e.getMessage());
            } catch (Exception e) {
                result = FAILED;
                reason = e.getMessage();
                responseModelArrayList.add(new ResponseNCRSModel(appRefnumber, result, reason, customerId, responseModel));
                log.error("NCRS error {}", reason);
                inquiryDate = new Date();
                log.debug("___________________________________________________________777");
                log.debug("NCRS appRefnumber : {} customerType : {} customerId : {} inquiryDate : {}  result : {}",
                        appRefnumber, customerType, customerId, inquiryDate, result);
                resultImp.add(appRefnumber, customerType, customerId, new Date(), result, reason);
            }
        }
        return responseModelArrayList;
    }

    @Override
    public ArrayList<ResponseNCRSModel> requestOffline(NCRSModel ncrsModel) throws Exception {
        log.debug("NCRS Call : requestOffline()");
        NCRSResponseModel responseModel = null;
        ArrayList<ResponseNCRSModel> responseModelArrayList = new ArrayList<ResponseNCRSModel>();

        return responseModelArrayList;
    }

    private NCRSResponseModel callOnline(NCRSModel ncrsModel) throws Exception{
        log.debug("NCRS Call : callOnline()");

        String userId = ncrsModel.getUserId();
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
        try {
                responseModel = request(ncrsModel, ONLINE);
                log.debug("___________________________________________________________000");
                if(responseModel != null){
                    resultDesc = responseModel.getHeaderModel().getCommand();
                    log.debug("___________________________________________________________111");
                    log.debug("NCRS requestOnline {}", resultDesc);
                    if(!ERROR.equals(resultDesc)){
                        log.debug("___________________________________________________________222");
                        resultDate = new Date();
                        if(null==responseModel.getBodyModel().getTransaction().getTueferror()){
                            log.debug("___________________________________________________________333");
                            log.debug("NCRS audit userId {} action {} actionDesc {} actionDate {} actionResult {} resultDesc {} resultDate {} linkKey {}"  ,
                                    userId, action, actionDesc, actionDate, ActionResult.EXCEPTION, resultDesc, resultDate, linkKey);
                            ncbAuditor.add(
                                    userId,
                                    action,
                                    actionDesc,
                                    actionDate,
                                    ActionResult.SUCCEED,
                                    resultDesc,
                                    resultDate,
                                    linkKey );
                            log.debug("___________________________________________________________444");
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
                            ArrayList<com.clevel.selos.integration.ncrs.models.response.TUEFEnquiryNameModel> test =  responseModel.getBodyModel().getTransaction().getTuefenquiry().getName();
                            com.clevel.selos.integration.ncrs.models.response.TUEFEnquiryNameModel model = test.get(0);
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
                            return responseModel;
                        } else {
                            StringBuilder exception = new StringBuilder("TUEF Error");
                            TUEFErrorError error =  responseModel.getBodyModel().getTransaction().getTueferror().getError();
                            ArrayList<ErrorModel> arrayList =  error.getError();

                            for (int i = 0; i<arrayList.size();i++){
                                ErrorModel errorModel = arrayList.get(i);
                                exception.append((i+1)).append(" ").append(errorModel.getDescription()).append(" ");
                            }

//                            for(ErrorModel errorModel : arrayList){
//                                log.error("NCRS requestOnline Tuef Error value : {} description : {} code : {}", errorModel.getValue(), errorModel.getDescription(), errorModel.getCode());
//                            }
//                            log.error("NCRS requestOnline Responsedata : {}", responseModel.getBodyModel().getTransaction().getTueferror().getResponsedata().getTexttuefresponse());

                            resultDesc = null!=exception?exception.toString():"";
                            resultDate = new Date();
                            log.debug("NCRS audit userId {} action {} actionDesc {} actionDate {} actionResult {} resultDesc {} resultDate {} linkKey {}"  ,
                                    userId, action, actionDesc, actionDate, ActionResult.EXCEPTION, resultDesc, resultDate, linkKey);
                            ncbAuditor.add(
                                    userId,
                                    action,
                                    actionDesc,
                                    actionDate,
                                    ActionResult.EXCEPTION,
                                    resultDesc,
                                    resultDate,
                                    linkKey );
                            throw new Exception(null!=exception?exception.toString():"");

                        }
                    } else {
                        log.debug("NCRS requestOnline Command = {}"  ,resultDesc);
                        resultDesc = responseModel.getBodyModel().getErrormsg();
                        resultDesc = ( (resultDesc != null) ? (resultDate + "") : "" );
                        resultDate = new Date();
                        log.debug("NCRS audit userId {} action {} actionDesc {} actionDate {} actionResult {} resultDesc {} resultDate {} linkKey {}"  ,
                                userId, action, actionDesc, actionDate, ActionResult.EXCEPTION, resultDesc, resultDate, linkKey);
                        ncbAuditor.add(
                                userId,
                                action,
                                actionDesc,
                                actionDate,
                                ActionResult.EXCEPTION,
                                resultDesc,
                                resultDate,
                                linkKey );
                        log.error("NCRS requestOnline {}" ,resultDesc);
                        throw new Exception("NCB Exception "+resultDesc);
                    }
                } else {
                    //responseModel  is null
                }
        } catch (HttpHostConnectException e) {
            resultDesc = e.getMessage();
            resultDate = new Date();
            log.debug("NCRS audit userId {} action {} actionDesc {} actionDate {} actionResult {} resultDesc {} resultDate {} linkKey {}"  ,
                    userId, action, actionDesc, actionDate, ActionResult.EXCEPTION, resultDesc, resultDate, linkKey);
            ncbAuditor.add(
                    userId,
                    action,
                    actionDesc,
                    actionDate,
                    ActionResult.FAILED,
                    resultDesc,
                    resultDate,
                    linkKey );
            throw new ValidationException(e.getMessage());
        } catch (ConnectTimeoutException e) {
            callOffline(ncrsModel);
        }
        return responseModel;
    }
    private NCRSResponseModel callOffline(NCRSModel ncrsModel) throws Exception{
        log.debug("NCRS Call : callOffline()");

        NCRSResponseModel responseModel = null;
        responseModel = request(ncrsModel, FIND);
        if(responseModel != null){
            if(!ERROR.equals(responseModel.getHeaderModel().getCommand())){
                if(null!=responseModel.getBodyModel().getTrackingid()){
                    ArrayList<String> arrayList = responseModel.getBodyModel().getTrackingid();
                    if(1<=arrayList.size()){
                        log.debug("NCRS The List Tracking ID {}", arrayList);
                        String trackingId = arrayList.get(arrayList.size()-1);
                        log.debug("NCRS The maximum value of list Tracking ID is {}", trackingId);
                        ncrsModel.setTrackingId(trackingId);
                        responseModel = request(ncrsModel, READ);
                        if(responseModel != null){
                            if(!ERROR.equals(responseModel.getHeaderModel().getCommand())){
                                if(null != responseModel.getBodyModel().getTransaction().getUser()){
                                    return responseModel;
                                } else {
                                    log.debug("NCRS Please click check NCB again");
                                    throw new ValidationException("Please click check NCB again");
                                }
                            } else {
                                log.error("NCB Exception {}" ,responseModel.getBodyModel().getErrormsg());
                                throw new Exception("NCB Exception {}"+responseModel.getBodyModel().getErrormsg());
                            }
                        } else {
                            return responseModel;
                        }
                    } else {
                        throw new ValidationException("TrackingId < 1");
                    }
                } else {
                        throw new ValidationException("Matched transaction did not found");
                }
            } else {
                log.error("NCB Exception {}" ,responseModel.getBodyModel().getErrormsg());
                throw new Exception("NCB Exception {}"+responseModel.getBodyModel().getErrormsg());
            }
        } else {
            return responseModel;
        }
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
        String xml = null;
        String result = null;
        XStream xStream = null;

        xStream = new XStream();
        xStream.processAnnotations(NCRSRequestModel.class);
        xml = xStream.toXML(ncrsRequest);
        log.debug("\nNCRS Request : \n{}",xml);
        result = post.sendPost(xml, url, Integer.parseInt(timeOut));
        if(!"".equals(result)){
            log.debug("NCRS Response : {}",result);
            xStream.processAnnotations(NCRSResponseModel.class);
            ncrsResponse = (NCRSResponseModel)xStream.fromXML(result);
            return ncrsResponse;
        }else{
            log.debug("NCRS Response : {}",result);
            return ncrsResponse;
        }
    }
}
