package com.clevel.selos.integration.ncrs.service;


import com.clevel.selos.exception.ValidationException;
import com.clevel.selos.integration.NCB;
import com.clevel.selos.integration.ncrs.httppost.Post;
import com.clevel.selos.integration.ncrs.models.request.*;
import com.clevel.selos.integration.ncrs.models.response.NCRSResponse;


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

    //Config
    private static String id = "SLOSTEST";
    private static String pass = "SLOSTEST12";
    private static String url = "http://10.175.230.112/ncrs/servlet/xmladapter";

    public final String CPUTOCPU_ENQUIRY = "BB01001";
    public final String BATCHOFFLINE_ENQUIRY_ENTRY = "FF01001";

    @Inject
    public NCRSImp() {
    }

    @Override
    public NCRSResponse requestOnline(NCRSModel ncrsModel) throws Exception {
        if (null!=ncrsModel) throw new ValidationException("The NCRSModel is null");
        log.debug("========================================= requestOnline(NCRSModel : {})",ncrsModel.toString());
        return request(ncrsModel, CPUTOCPU_ENQUIRY);
    }

    @Override
    public NCRSResponse requestOffline(NCRSModel ncrsModel) throws Exception {
        if (null!=ncrsModel) throw new ValidationException("The NCRSModel is null");
        log.debug("========================================= requestOffline(NCRSModel : {})",ncrsModel.toString());
        return request(ncrsModel, BATCHOFFLINE_ENQUIRY_ENTRY);
    }

    private NCRSResponse request(NCRSModel ncrsModel, String command)throws Exception{
        log.debug("========================================= NCRSModel : {}",ncrsModel.toString());
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
        NCRSResponse ncrsResponse = null;
        xStream = new XStream();
        xStream.processAnnotations(NCRSRequest.class);
        log.debug("========================================= Command code : {}",command);
        ncrsRequest = new NCRSRequest(
                new HeaderModel(id, pass, command),
                new BodyModel(
                        new TUEFEnquiryModel(
                                new TUEFEnquiryHeaderModel(memberRef, enqPurpose, enqAmount,consent, disPuteenQuiry),
                                nameList, idList)));
        xml = xStream.toXML(ncrsRequest);
        log.debug("========================================= Request : {}",xml);
        result = post.sendPost(xml,url);
        if(!"".equals(result)){
            log.debug("========================================= Response : {}",result);
            xStream.processAnnotations(NCRSResponse.class);
            ncrsResponse = (NCRSResponse)xStream.fromXML(result);
            return ncrsResponse;
        }else{
            log.debug("========================================= Response : {}",result);
            return ncrsResponse;
        }
    }
}
