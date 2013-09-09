package com.clevel.selos.integration.nccrs.service;

import com.clevel.selos.exception.ValidationException;
import com.clevel.selos.integration.NCB;
import com.clevel.selos.integration.nccrs.models.request.*;
import com.clevel.selos.integration.nccrs.models.response.NCCRSResponseModel;
import com.clevel.selos.integration.ncrs.httppost.Post;
import com.clevel.selos.system.message.Message;
import com.clevel.selos.system.message.ValidationMessage;
import com.thoughtworks.xstream.XStream;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.io.Serializable;

public class NCCRSImp implements NCCRS, Serializable {
    @Inject
    @NCB
    Logger log;

    @Inject
    Post post;

    @Inject
    @ValidationMessage
    Message message;

    //Config
    private String id = "NCBINQCSIT";
    private String pass = "Sit12345";
    private String url = "http://10.175.230.112/ncrs/servlet/xmladapter";

    @Inject
    public NCCRSImp() {
    }

    @Override
    public NCCRSResponseModel requestOnline(NCCRSModel model) throws Exception {
        if (null==model) throw new ValidationException(message.get("validation.101"));
        log.debug("=========================================NCCRS requestOnline(NCCRSModel : {})",model.toString());
        final String CPUTOCPU_ENQUIRY = "BB01001";
        return request(model,CPUTOCPU_ENQUIRY);
    }

    @Override
    public NCCRSResponseModel requestOffline(NCCRSModel model) throws Exception {
        if (null==model) throw new ValidationException(message.get("validation.101"));
        log.debug("=========================================NCCRS requestOffline(NCCRSModel : {})",model.toString());
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
        log.debug("=========================================NCCRS Command code : {}",command);
        nccrsRequest = new NCCRSRequestModel(
                           new HeaderModel(id,pass,command),
                           new BodyModel(
                               new H2HRequestModel(registType, registId, companyName,
                                                   inqPurose, productType, memberRef,
                                                   confirmConsent, language),
                               new AttributeModel(historicalBalanceReport)
                           ));
        xml = xStream.toXML(nccrsRequest);
        log.debug("=========================================NCCRS Request : {}",xml);
        result = post.sendPost(xml,url);
        if(!"".equals(result)){
            log.debug("=========================================NCCRS Response : {}",result);
            xStream.processAnnotations(NCCRSResponseModel.class);
            nccrsResponse = (NCCRSResponseModel)xStream.fromXML(result);
            return nccrsResponse;
        }else{
            log.debug("=========================================NCCRS Response : {}",result);
            return nccrsResponse;
        }
    }
}
