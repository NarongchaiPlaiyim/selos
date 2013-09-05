package com.clevel.selos.integration.ncrs.service;


import com.clevel.selos.integration.NCB;
import com.clevel.selos.integration.ncrs.commands.Command;
import com.clevel.selos.integration.ncrs.exception.ValidationException;
import com.clevel.selos.integration.ncrs.httppost.Post;
import com.clevel.selos.integration.ncrs.models.request.*;
import com.clevel.selos.integration.ncrs.models.response.Ncrsresponse;

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
    public NCRSImp() {

    }


    @Override
    public Ncrsresponse requestOnline(NCRSModel ncrsModel) throws Exception {
//        System.out.println("=========================================requestOnline()");
        Util util = new Util();
        log.debug("requestOnline.(NCRSModel : {} )",ncrsModel.toString());

//        System.out.println("=========================================requestOnline() Model : "+ncrsModel.toString());

        if(null!=ncrsModel){
//            System.out.println("=========================================requestOnline() NCRSModel is not null");

            String id = ncrsModel.getId();
//            System.out.println("=========================================requestOnline() ID : "+id);
            log.debug("requestOnline. (NCRSModel : id {})",ncrsModel.getId());

            String pass = ncrsModel.getPass();
//            System.out.println("=========================================requestOnline() Pass : "+pass);
            log.debug("requestOnline. (NCRSModel : pass {})",ncrsModel.getPass());

            String memberref = ncrsModel.getMemberref();
//            System.out.println("=========================================requestOnline() Memberfef : "+memberref);
            log.debug("requestOnline. (NCRSModel : memberref {})",ncrsModel.getMemberref());

            String enqpurpose = ncrsModel.getEnqpurpose();
//            System.out.println("=========================================requestOnline() Enqpurpose : "+enqpurpose);
            log.debug("requestOnline. (NCRSModel : enqpurpose {})",ncrsModel.getEnqpurpose());

            String enqamount = ncrsModel.getEnqamount();
//            System.out.println("=========================================requestOnline() Enqamount : "+enqamount);
            log.debug("requestOnline. (NCRSModel : enqamount {})",ncrsModel.getEnqamount());

            String consent = ncrsModel.getConsent();
//            System.out.println("=========================================requestOnline() Consent : "+consent);
            log.debug("requestOnline. (NCRSModel : consent {})",ncrsModel.getConsent());

            String disputeenquiry = ncrsModel.getDisputeenquiry();
//            System.out.println("=========================================requestOnline() Disputeenquiry : "+disputeenquiry);
            log.debug("requestOnline. (NCRSModel : disputeenquiry {})",ncrsModel.getDisputeenquiry());

            ArrayList<TUEFEnquiryNameModel> nameList = ncrsModel.getNameList();
            log.debug("requestOnline. (NCRSModel : nameList {})",ncrsModel.getNameList().size());

            ArrayList<TUEFEnquiryIdModel> idList = ncrsModel.getIdList();
            log.debug("requestOnline. (NCRSModel : idList {})",ncrsModel.getIdList().size());

            String url = ncrsModel.getUrl();
//            System.out.println("=========================================requestOnline() URL : "+url);
            log.debug("requestOnline. (NCRSModel : {})",ncrsModel.toString());


            if(util.isNotNullString(id)&&util.isNotNullString(pass))throw new ValidationException("The NCRSModel is null");




            /*if(util.isNotNullString(id)&&util.isNotNullString(pass)){
                if(util.isNotNullString(memberref)&&util.checkLength(memberref,25)&&util.isNotNullString(enqpurpose)&&util.checkLength(enqpurpose,2)){
                    if (!util.isNotNullString(enqamount)||util.checkLength(enqamount,9)){
                       if(util.isNotNullString(consent)&&util.checkLength(consent,1)){
                           if(util.checkSize(ncrsModel.getIdList(),1)&&util.checkSize(ncrsModel.getNameList(),1)){
                               if(util.isNotNullString(url)){    */

                                   String xml = null;
                                   String result = null;
                                   XStream xStream = null;
                                   Ncrsrequest ncrsrequest = null;
                                   Post post = null;
                                   Command command = null;


                                   xStream = new XStream();
                                   xStream.processAnnotations(Ncrsrequest.class);
                                   command = new Command();
                                   log.debug("requestOnline. (Commands : {})",command.CPUTOCPU_ENQUIRY);
                                   ncrsrequest = new Ncrsrequest(
                                           new HeaderModel(id, pass, command.CPUTOCPU_ENQUIRY),
                                           new BodyModel(
                                                   new TUEFEnquiryModel(
                                                           new TUEFEnquiryHeaderModel(memberref, enqpurpose, util.convertNullToBlank(enqamount),consent, util.convertNullToBlank(disputeenquiry)),
                                                           nameList,
                                                           idList)));
                                   xml = xStream.toXML(ncrsrequest);

//                                   System.out.println("=========================================requestOnline() XML : "+xml);

                                   log.debug("requestOnline. (Request : {})",xml);
                                   post =  Post.getInstance();
                                   result = post.sendPost(xml,url);
//                                   System.out.println("=========================================requestOnline() Result : "+result);


                                   Ncrsresponse ncrsresponse = null;
                                   if(!"".equals(result)){
                                   log.debug("requestOnline. (Response : {})",result);
                                       xStream.processAnnotations(Ncrsresponse.class);
                                       ncrsresponse = (Ncrsresponse)xStream.fromXML(result);
                                       return ncrsresponse;
                                   }else{
                                   log.warn("requestOnline. (Response : {})",result);
                                       return ncrsresponse;
                                   }
                               /*}else{
                                   throw new ValidationException("NCRSModel : url is null");
                               }
                           }else{
                               throw new ValidationException("NCRSModel : name is null or size < 1 and id is null or size < 1");
                           }
                       }else{
                           throw new ValidationException("NCRSModel : consent is consent or length > 1 ");
                       }
                    }else {
                        throw new ValidationException("NCRSModel : length of enqamount > 9");
                    }
                }else{
                    throw new ValidationException("NCRSModel : memberref is null or length > 25 and enqpurpose is null or length > 2");
                }
            }else{
                throw new ValidationException("NCRSModel : ID is null or Pass is null");
            } */
        }else {
            throw new ValidationException("The NCRSModel is null");
        }
    }

    @Override
    public Ncrsresponse requestOffline(NCRSModel ncrsModel) throws Exception {
        System.out.println("=========================================requestOffline()");
        Util util = new Util();
        //log.debug("requestOffline.(NCRSModel : {} )",ncrsModel.toString());

        System.out.println("=========================================requestOffline() Model : "+ncrsModel.toString());

        if(null!=ncrsModel){
            System.out.println("=========================================requestOffline() NCRSModel is not null");

            String id = ncrsModel.getId();
            System.out.println("=========================================requestOffline() ID : "+id);
//            log.debug("requestOffline. (NCRSModel : id {})",ncrsModel.getId());

            String pass = ncrsModel.getPass();
            System.out.println("=========================================requestOffline() Pass : "+pass);
//            log.debug("requestOffline. (NCRSModel : pass {})",ncrsModel.getPass());

            String memberref = ncrsModel.getMemberref();
            System.out.println("=========================================requestOffline() Memberfef : "+memberref);
//            log.debug("requestOffline. (NCRSModel : memberref {})",ncrsModel.getMemberref());

            String enqpurpose = ncrsModel.getEnqpurpose();
            System.out.println("=========================================requestOffline() Enqpurpose : "+enqpurpose);
//            log.debug("requestOffline. (NCRSModel : enqpurpose {})",ncrsModel.getEnqpurpose());

            String enqamount = ncrsModel.getEnqamount();
            System.out.println("=========================================requestOffline() Enqamount : "+enqamount);
//            log.debug("requestOffline. (NCRSModel : enqamount {})",ncrsModel.getEnqamount());

            String consent = ncrsModel.getConsent();
            System.out.println("=========================================requestOffline() Consent : "+consent);
//            log.debug("requestOffline. (NCRSModel : consent {})",ncrsModel.getConsent());

            String disputeenquiry = ncrsModel.getDisputeenquiry();
            System.out.println("=========================================requestOffline() Disputeenquiry : "+disputeenquiry);
//            log.debug("requestOffline. (NCRSModel : disputeenquiry {})",ncrsModel.getDisputeenquiry());

            ArrayList<TUEFEnquiryNameModel> nameList = ncrsModel.getNameList();
//            log.debug("requestOffline. (NCRSModel : nameList {})",ncrsModel.getNameList().size());

            ArrayList<TUEFEnquiryIdModel> idList = ncrsModel.getIdList();
//            log.debug("requestOffline. (NCRSModel : idList {})",ncrsModel.getIdList().size());

            String url = ncrsModel.getUrl();
            System.out.println("=========================================requestOffline() URL : "+url);
//            log.debug("requestOffline. (NCRSModel : {})",ncrsModel.toString());

            if(util.isNotNullString(id)&&util.isNotNullString(pass)){
                if(util.isNotNullString(memberref)&&util.checkLength(memberref,25)&&util.isNotNullString(enqpurpose)&&util.checkLength(enqpurpose,2)){
                    if (!util.isNotNullString(enqamount)||util.checkLength(enqamount,9)){
                        if(util.isNotNullString(consent)&&util.checkLength(consent,1)){
                            if(util.checkSize(ncrsModel.getIdList(),1)&&util.checkSize(ncrsModel.getNameList(),1)){
                                if(util.isNotNullString(url)){

                                    String xml = null;
                                    String result = null;
                                    XStream xStream = null;
                                    Ncrsrequest ncrsrequest = null;
                                    Post post = null;
                                    Command command = null;


                                    xStream = new XStream();
                                    xStream.processAnnotations(Ncrsrequest.class);
                                    command = new Command();
//                                   log.debug("requestOffline is not null. (Commands : {})",command.BATCHOFFLINE_ENQUIRY_ENTRY);
                                    ncrsrequest = new Ncrsrequest(
                                            new HeaderModel(id, pass, command.BATCHOFFLINE_ENQUIRY_ENTRY),
                                            new BodyModel(
                                                    new TUEFEnquiryModel(
                                                            new TUEFEnquiryHeaderModel(memberref, enqpurpose, util.convertNullToBlank(enqamount),consent, util.convertNullToBlank(disputeenquiry)),
                                                            nameList,
                                                            idList)));
                                    xml = xStream.toXML(ncrsrequest);

                                    System.out.println("=========================================requestOffline() XML : "+xml);

//                                   log.debug("requestOffline. (Request : {})",xml);
                                    post =  Post.getInstance();
                                    result = post.sendPost(xml,url);
                                    System.out.println("=========================================requestOffline() Result : "+result);


                                    Ncrsresponse ncrsresponse = null;
                                    if(!"".equals(result)){
//                                   log.debug("requestOffline. (Response : {})",result);
                                        xStream.processAnnotations(Ncrsresponse.class);
                                        ncrsresponse = (Ncrsresponse)xStream.fromXML(result);
                                        return ncrsresponse;
                                    }else{
//                                   log.debug("requestOffline. (Response : {})",result);
                                        return ncrsresponse;
                                    }
                                }else{
                                    throw new ValidationException("NCRSModel : url is null");
                                }
                            }else{
                                throw new ValidationException("NCRSModel : name is null or size < 1 and id is null or size < 1");
                            }
                        }else{
                            throw new ValidationException("NCRSModel : consent is consent or length > 1 ");
                        }
                    }else {
                        throw new ValidationException("NCRSModel : length of enqamount > 9");
                    }
                }else{
                    throw new ValidationException("NCRSModel : memberref is null or length > 25 and enqpurpose is null or length > 2");
                }
            }else{
                throw new ValidationException("NCRSModel : ID is null or Pass is null");
            }
        }else {
            throw new ValidationException("The NCRSModel is null");
        }
    }
}
