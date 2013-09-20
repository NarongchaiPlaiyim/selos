package com.clevel.selos.integration.ncrs.service;


import com.clevel.selos.dao.export.NCBIExportDAO;
import com.clevel.selos.exception.ValidationException;
import com.clevel.selos.integration.NCB;
import com.clevel.selos.integration.nccrs.service.NCBIExportImp;
import com.clevel.selos.integration.nccrs.service.NCBIExportModel;
import com.clevel.selos.integration.ncrs.httppost.Post;
import com.clevel.selos.integration.ncrs.models.request.*;
import com.clevel.selos.integration.ncrs.models.response.NCRSResponseModel;


import com.clevel.selos.integration.ncrs.vaildation.ValidationImp;
import com.clevel.selos.model.ActionResult;
import com.clevel.selos.model.db.export.NCBIExport;
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

    private final String action= "NCRS";
    private final String ERROR = "ER01001";

    @Inject
    @NCB
    NCBIExportImp exportImp;

    @Inject
    public NCRSImp() {
    }

    @Override
    public NCRSResponseModel requestOnline(NCRSModel ncrsModel) throws Exception {
        if (null==ncrsModel) throw new ValidationException(message.get("101"));

        String userId = "userId";
        StringBuilder stringBuilder = null;
        String actionDesc = null;
        Date actionDate = null;
        Date resultDate = null;
        String resultDesc = null;
        String linkKey = null;
        NCRSResponseModel responseModel = null;


        linkKey = Util.getLinkKey(userId);
        final String CPUTOCPU_ENQUIRY = "BB01001";

        log.debug("{}NCRS requestOnline(NCRSModel : {})",linkKey ,ncrsModel.toString());

        ArrayList<TUEFEnquiryIdModel> idModelArrayList =  ncrsModel.getIdList();
        ArrayList<TUEFEnquiryNameModel> nameModelArrayList = ncrsModel.getNameList();
        stringBuilder = new StringBuilder();

        for(TUEFEnquiryNameModel nameModel : nameModelArrayList){
            stringBuilder.append(" FirstName=");
            stringBuilder.append(nameModel.getFirstname());
            stringBuilder.append(" FamilyName=");
            stringBuilder.append(nameModel.getFamilyname());
        }
        for(TUEFEnquiryIdModel idModel : idModelArrayList){
            stringBuilder.append(" IdType=");
            stringBuilder.append(idModel.getIdtype());
            stringBuilder.append(" IdNumber=");
            stringBuilder.append(idModel.getIdnumber());

        }
        actionDesc = null!=stringBuilder?stringBuilder.toString():"";

        linkKey = Util.getLinkKey(userId);
        actionDate = new Date();

        try {
            responseModel = request(ncrsModel, CPUTOCPU_ENQUIRY);

            if(null!=responseModel){
                resultDesc = responseModel.getHeaderModel().getCommand();
                if(!ERROR.equals(resultDesc)){
                    resultDate = new Date();
                    ncbAuditor.add(
                            userId,
                            action,
                            actionDesc,
                            actionDate,
                            ActionResult.SUCCEED,
                            resultDesc,
                            resultDate,
                            linkKey );
                    //Save to EXP_NCBI
                    return responseModel;
                } else {
                    resultDesc = responseModel.getBodyModel().getErrormsg();
                    resultDate = new Date();
                    ncbAuditor.add(
                            userId,
                            action,
                            actionDesc,
                            actionDate,
                            ActionResult.EXCEPTION,
                            resultDesc,
                            resultDate,
                            linkKey );
                    throw new Exception("NCB Exception "+resultDesc);
                }
            } else {
                return responseModel;
            }
        }catch (Exception e){
            resultDesc = e.getMessage();
            resultDate = new Date();
            ncbAuditor.add(
                    userId,
                    action,
                    actionDesc,
                    actionDate,
                    ActionResult.FAILED,
                    resultDesc,
                    resultDate,
                    linkKey );

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
            exportModel.setFirstName("01");
            exportModel.setLastName("01");
            exportModel.setJuristicName("01");
            exportModel.setCaNumber("01");
            exportModel.setCaution("01");
            exportModel.setReferenceTel("01");
            exportModel.setInquiryStatus("01");
            exportModel.setInquiryDate(new Date( ));
            exportModel.setOfficeCode("010");
            exportImp.add(exportModel);

            throw new ValidationException(e.getMessage());
        }
    }

    @Override
    public NCRSResponseModel requestOffline(NCRSModel ncrsModel) throws Exception {
        if (null==ncrsModel) throw new ValidationException(message.get("validation.101"));

        NCRSResponseModel responseModel = null;
        String userId = "Example";
        String linkKey = null;
        linkKey = Util.getLinkKey(userId);
        log.debug("{}NCRS requestOffline(NCRSModel : {})",linkKey ,ncrsModel.toString());
        String BATCHOFFLINE_ENQUIRY_ENTRY = "FF01001";
        //ts

        Date actionDate = new Date();

        //ncbAuditor.add(userId, action, null, actionDate, ActionResult.SUCCEED, null, new Date(), linkKey);

        responseModel = request(ncrsModel, BATCHOFFLINE_ENQUIRY_ENTRY);
        if(null!=responseModel){
            if(!ERROR.equals(responseModel.getHeaderModel().getCommand())){
                /*ncbAuditor.add( userId,
                                action,
                                null,
                                actionDate,
                                ActionResult.SUCCEED,
                                null,
                                new Date(),
                                linkKey );
                return responseModel;    */
                //Save to NCBI table
            } else {
                /*ncbAuditor.add( userId,
                                action,
                                null,
                                actionDate,
                                ActionResult.EXCEPTION,
                                responseModel.getBodyModel().getErrormsg(),
                                new Date(),
                                linkKey );    */
                throw new Exception(responseModel.getBodyModel().getErrormsg());
            }
        } else {
            return responseModel;
        }
        return responseModel;
    }

    private NCRSResponseModel request(NCRSModel ncrsModel, String command)throws Exception{
        String memberRef = ncrsModel.getMemberref();
        String enqPurpose = ncrsModel.getEnqpurpose();
        String enqAmount = ncrsModel.getEnqamount();
        String consent = ncrsModel.getConsent();
        String disPuteenQuiry = ncrsModel.getDisputeenquiry();
        ArrayList<TUEFEnquiryNameModel> nameList = ncrsModel.getNameList();
        ArrayList<TUEFEnquiryIdModel> idList = ncrsModel.getIdList();

        String xml = null;
        String result = null;
        XStream xStream = null;
        NCRSRequest ncrsRequest = null;
        NCRSResponseModel ncrsResponse = null;
        xStream = new XStream();
        xStream.processAnnotations(NCRSRequest.class);
        log.debug("NCRS Command code : {}",command);
        ncrsRequest = new NCRSRequest(
                new HeaderModel(id, pass, command),
                new BodyModel(
                        new TUEFEnquiryModel(
                                new TUEFEnquiryHeaderModel(memberRef, enqPurpose, enqAmount,consent, disPuteenQuiry),
                                nameList, idList)));
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
