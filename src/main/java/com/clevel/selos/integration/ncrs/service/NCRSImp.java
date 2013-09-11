package com.clevel.selos.integration.ncrs.service;


import com.clevel.selos.exception.ValidationException;
import com.clevel.selos.integration.NCB;
import com.clevel.selos.integration.ncrs.httppost.Post;
import com.clevel.selos.integration.ncrs.models.request.*;
import com.clevel.selos.integration.ncrs.models.response.NCRSResponseModel;


import com.clevel.selos.system.Config;
import com.clevel.selos.system.message.Message;
import com.clevel.selos.system.message.ValidationMessage;
import com.thoughtworks.xstream.XStream;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.ArrayList;

import org.slf4j.Logger;

public class NCRSImp implements NCRS, Serializable{
    @Inject
    @NCB
    Logger log;

    @Inject
    Post post;

    @Inject
    public NCRSImp() {
    }

    @Inject
    @ValidationMessage
    Message message;
    //Config

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

    @Override
    public NCRSResponseModel requestOnline(NCRSModel ncrsModel) throws Exception {
        if (null==ncrsModel) throw new ValidationException(message.get("validation.101"));
        log.debug("=========================================NCRS requestOnline(NCRSModel : {})",ncrsModel.toString());
        String CPUTOCPU_ENQUIRY = "BB01001";
        return request(ncrsModel, CPUTOCPU_ENQUIRY);
    }

    @Override
    public NCRSResponseModel requestOffline(NCRSModel ncrsModel) throws Exception {
        if (null==ncrsModel) throw new ValidationException(message.get("validation.101"));
        log.debug("=========================================NCRS requestOffline(NCRSModel : {})",ncrsModel.toString());
        String BATCHOFFLINE_ENQUIRY_ENTRY = "FF01001";
        return request(ncrsModel, BATCHOFFLINE_ENQUIRY_ENTRY);
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
        log.debug("=========================================NCRS Command code : {}",command);
        ncrsRequest = new NCRSRequest(
                new HeaderModel(id, pass, command),
                new BodyModel(
                        new TUEFEnquiryModel(
                                new TUEFEnquiryHeaderModel(memberRef, enqPurpose, enqAmount,consent, disPuteenQuiry),
                                nameList, idList)));
        xml = xStream.toXML(ncrsRequest);
        log.debug("=========================================NCRS Request : {}",xml);

        result = post.sendPost(xml, url, Integer.parseInt(timeOut));
        if(!"".equals(result)){
//            log.debug("=========================================NCRS Response : {}",result);
            xStream.processAnnotations(NCRSResponseModel.class);
            ncrsResponse = (NCRSResponseModel)xStream.fromXML(result);
            return ncrsResponse;
        }else{
//            log.debug("=========================================NCRS Response : {}",result);
            return ncrsResponse;
        }
    }
}