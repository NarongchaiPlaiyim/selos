package com.clevel.selos.integration.nccrs.service;

import com.clevel.selos.exception.ValidationException;
import com.clevel.selos.integration.NCB;
import com.clevel.selos.integration.nccrs.models.request.*;
import com.clevel.selos.integration.nccrs.models.response.NCCRSResponseModel;
import com.clevel.selos.integration.nccrs.httppost.Post;
import com.clevel.selos.integration.ncrs.models.request.CriteriaModel;
import com.clevel.selos.model.ActionResult;
import com.clevel.selos.system.Config;
import com.clevel.selos.system.audit.SystemAuditor;
import com.clevel.selos.system.audit.UserAuditor;
import com.clevel.selos.system.message.Message;
import com.clevel.selos.system.message.ValidationMessage;
import com.clevel.selos.util.Util;
import com.thoughtworks.xstream.XStream;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.io.Serializable;
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
    @Config(name = "interface.ncb.nccrs.user")
    private String id;

    @Inject
    @Config(name = "interface.ncb.nccrs.pass")
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

    private final String action= "NCCRS";
    private final String ERROR = "ER01001";

    @Inject
    public NCCRSImp() {
    }

    @Override
    public NCCRSResponseModel requestOnline(NCCRSModel model) throws Exception {
        if (null==model) throw new ValidationException(message.get("validation.101"));

        String userId = "userId";
        StringBuilder stringBuilder = null;
        String actionDesc = null;
        NCCRSResponseModel responseModel = null;
        Date actionDate = null;
        Date resultDate = null;
        String resultDesc = null;
        String linkKey = null;

        linkKey = Util.getLinkKey(userId);
        final String CPUTOCPU_ENQUIRY = "BB01001";

        log.debug("{}NCCRS requestOnline(NCCRSModel : {})", linkKey, model.toString());
        actionDesc = model.toString();
        actionDate = new Date();
        try {
            responseModel = request(model, CPUTOCPU_ENQUIRY);

            if(null!=responseModel){
                resultDesc = responseModel.getHeader().getCommand();
                if(!ERROR.equals(resultDesc)){
                    resultDate = new Date();
                    ncbAuditor.add( userId,
                            action,
                            actionDesc,
                            actionDate,
                            ActionResult.SUCCEED,
                            resultDesc,
                            resultDate,
                            linkKey );




                    return responseModel;


                    //Save to NCBI table
                } else {
                    resultDesc = responseModel.getBody().getErrormsg();
                    resultDate = new Date();
                    ncbAuditor.add( userId,
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
            ncbAuditor.add( userId,
                    action,
                    actionDesc,
                    actionDate,
                    ActionResult.FAILED,
                    resultDesc,
                    resultDate,
                    linkKey );
            throw new ValidationException(e.getMessage());
        }
    }

    @Override
    public NCCRSResponseModel requestOffline(NCCRSModel model) throws Exception {
        if (null==model) throw new ValidationException(message.get("validation.101"));
        log.debug("NCCRS requestOffline(NCCRSModel : {})",model.toString());
        final String BATCHOFFLINE_ENQUIRY_ENTRY = "FF01001";
        return request(model, BATCHOFFLINE_ENQUIRY_ENTRY);
    }

    private NCCRSResponseModel request(NCCRSModel model, String command)throws Exception{
        String registType = model.getRegistType();
        String registId = model.getRegistId();
        String companyName = model.getCompanyName();
        String inqPurose  = model.getInqPurose();
        String productType = model.getProductType();
        String memberRef = model.getMemberRef();
        String confirmConsent = model.getConfirmConsent();
        String language = model.getLanguage();
        String historicalBalanceReport  = model.getHistoricalBalanceReport();

        String xml = null;
        String result = null;
        XStream xStream = null;
        NCCRSRequestModel nccrsRequest = null;
        NCCRSResponseModel nccrsResponse = null;
        xStream = new XStream();
        xStream.processAnnotations(NCCRSRequestModel.class);
        log.debug("NCCRS Command code : {}",command);
        nccrsRequest = new NCCRSRequestModel(
                           new HeaderModel(id,pass,command),
                           new BodyModel(
                               new H2HRequestModel(registType, registId, companyName,
                                                   inqPurose, productType, memberRef,
                                                   confirmConsent, language),
                               new AttributeModel(historicalBalanceReport)
                           ));
        xml = xStream.toXML(nccrsRequest);
        log.debug("NCCRS Request : \n{}",xml);
        result = post.sendPost(xml, url, Integer.parseInt(timeOut));
        if(!"".equals(result)){
            log.debug("NCCRS Response : {}",result);
            xStream.processAnnotations(NCCRSResponseModel.class);
            nccrsResponse = (NCCRSResponseModel)xStream.fromXML(result);
            return nccrsResponse;
        }else{
            log.debug("NCCRS Response : {}",result);
            return nccrsResponse;
        }
    }



    private NCCRSRequestModel createOnlineModel(NCCRSModel model, String command){
        NCCRSRequestModel nccrsRequest = null;
        return nccrsRequest;
    }
    private NCCRSRequestModel createFindModel(NCCRSModel model, String command){
       NCCRSRequestModel nccrsRequest = null;

//       nccrsRequest = new NCCRSRequestModel(
//               new HeaderModel(id, pass, command),
//               new BodyModel(
                       //new CriteriaModel(Util.createDateString(new Date(),"YYYYMMdd"), model.getRegistId(), id)));
       return nccrsRequest;
   }
    private NCCRSRequestModel createReadModel(String trackingId, String command){
        NCCRSRequestModel nccrsRequest = null;
        nccrsRequest = new NCCRSRequestModel(
                new HeaderModel(id, pass, command),
                new BodyModel(trackingId));
        return nccrsRequest;
    }
    private NCCRSResponseModel sendToHTTP(NCCRSRequestModel nccrsRequest) throws Exception {
        NCCRSResponseModel nccrsResponse = null;
        String xml = null;
        String result = null;
        XStream xStream = null;

        xStream = new XStream();
        xStream.processAnnotations(NCCRSRequestModel.class);
        xml = xStream.toXML(nccrsRequest);
        log.debug("NCCRS Request : \n{}",xml);
        result = post.sendPost(xml, url, Integer.parseInt(timeOut));
        if(!"".equals(result)){
            log.debug("NCCRS Response : {}",result);
            xStream.processAnnotations(NCCRSResponseModel.class);
            nccrsResponse = (NCCRSResponseModel)xStream.fromXML(result);
            return nccrsResponse;
        }else{
            log.debug("NCCRS Response : {}",result);
            return nccrsResponse;
        }
    }
}
