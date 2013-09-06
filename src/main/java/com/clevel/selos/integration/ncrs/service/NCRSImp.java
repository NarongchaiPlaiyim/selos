package com.clevel.selos.integration.ncrs.service;


import com.clevel.selos.exception.ValidationException;
import com.clevel.selos.integration.NCB;
import com.clevel.selos.integration.ncrs.commands.Command;
import com.clevel.selos.integration.ncrs.httppost.Post;
import com.clevel.selos.integration.ncrs.models.request.*;
import com.clevel.selos.integration.ncrs.models.response.NCRSResponse;

import com.clevel.selos.util.Util;
import com.thoughtworks.xstream.XStream;
import javax.inject.Inject;
import java.util.ArrayList;

import org.slf4j.Logger;

public class NCRSImp implements NCRS {
    @Inject
    @NCB
    Logger log;

    @Inject
    Post post;

    //Config
    private static String id = "SLOSTEST";
    private static String pass = "SLOSTEST12";
    private static String url = "http://10.175.230.112/ncrs/servlet/xmladapter";

    @Inject
    public NCRSImp() {

    }

    @Override
    public NCRSResponse requestOnline(NCRSModel ncrsModel) throws Exception {

        if(null!=ncrsModel){
            Util util = new Util();
            log.debug("=========================================requestOnline(NCRSModel : {})",ncrsModel.toString());

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
            Command command = null;
            NCRSResponse ncrsResponse = null;

            xStream = new XStream();
            xStream.processAnnotations(NCRSRequest.class);
            command = new Command();
            log.debug("=========================================requestOnline. Command code : {}",command.CPUTOCPU_ENQUIRY);
            ncrsRequest = new NCRSRequest(
            new HeaderModel(id, pass, command.CPUTOCPU_ENQUIRY),
            new BodyModel(
                new TUEFEnquiryModel(
                new TUEFEnquiryHeaderModel(memberRef, enqPurpose, enqAmount,consent, disPuteenQuiry),
                    nameList, idList)));
            xml = xStream.toXML(ncrsRequest);
            log.debug("=========================================requestOnline. Request : {}",xml);
            result = post.sendPost(xml,url);

            if(!"".equals(result)){
                log.debug("=========================================requestOnline. Response : {}",result);
                xStream.processAnnotations(NCRSResponse.class);
                ncrsResponse = (NCRSResponse)xStream.fromXML(result);
                return ncrsResponse;
            }else{
                log.debug("=========================================requestOnline. Response : {}", result);
                return ncrsResponse;
            }

       }else {
            throw new ValidationException("The NCRSModel is null");
        }
    }

    @Override
    public NCRSResponse requestOffline(NCRSModel ncrsModel) throws Exception {
        if(null!=ncrsModel){
            Util util = new Util();
            log.debug("=========================================requestOffline(NCRSModel : {})",ncrsModel.toString());

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
            Command command = null;
            NCRSResponse ncrsResponse = null;

            xStream = new XStream();
            xStream.processAnnotations(NCRSRequest.class);
            command = new Command();
            log.debug("=========================================requestOffline. Command code : {}",command.BATCHOFFLINE_ENQUIRY_ENTRY);
            ncrsRequest = new NCRSRequest(
                    new HeaderModel(id, pass, command.BATCHOFFLINE_ENQUIRY_ENTRY),
                    new BodyModel(
                            new TUEFEnquiryModel(
                                    new TUEFEnquiryHeaderModel(memberRef, enqPurpose, enqAmount,consent, disPuteenQuiry),
                                    nameList, idList)));
            xml = xStream.toXML(ncrsRequest);
            log.debug("=========================================requestOffline. Request : {}",xml);
            result = post.sendPost(xml,url);

            if(!"".equals(result)){
                log.debug("=========================================requestOffline. Response : {}",result);
                xStream.processAnnotations(NCRSResponse.class);
                ncrsResponse = (NCRSResponse)xStream.fromXML(result);
                return ncrsResponse;
            }else{
                log.debug("=========================================requestOffline. Response : {}",result);
                return ncrsResponse;
            }

        }else {
            throw new ValidationException("The NCRSModel is null");
        }
    }
}
