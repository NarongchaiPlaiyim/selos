package com.clevel.selos.integration.ncrs.service;


import com.clevel.selos.controller.TestNCRS;
import com.clevel.selos.integration.Integration;
import com.clevel.selos.integration.NCB;
import com.clevel.selos.integration.ncrs.commands.Command;
import com.clevel.selos.integration.ncrs.exception.ValidationException;
import com.clevel.selos.integration.ncrs.models.request.TUEFEnquiryIdModel;
import com.clevel.selos.integration.ncrs.models.request.TUEFEnquiryNameModel;
import com.clevel.selos.integration.ncrs.models.response.Ncrsresponse;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.io.Serializable;
import java.util.ArrayList;

public class NCRSService implements Serializable {
    @Inject
    @NCB
    Logger log;

    @Inject
    public NCRSService() {

    }

    public void process(TestNCRS testNCRS){
        //!!!!! TEST !!!!!
        //!!!!! TEST !!!!!
        //!!!!! TEST !!!!!
        //!!!!! TEST !!!!!
        //!!!!! TEST !!!!!
        //!!!!! TEST !!!!!
        //!!!!! TEST !!!!!
        //!!!!! TEST !!!!!
        //!!!!! TEST !!!!!
        //!!!!! TEST !!!!!
        //log.debug("========================================= process().");
        System.out.println("=========================================process().");
        NCRSModel ncrsModel = new NCRSModel();

        String url = "http://10.175.230.112/ncrs/servlet/xmladapter";
        System.out.println("URL : "+url);

        TUEFEnquiryNameModel nameModel = new TUEFEnquiryNameModel("aa", "bb", null);
        ArrayList<TUEFEnquiryNameModel> name = new ArrayList<TUEFEnquiryNameModel>();
        name.add(nameModel);

        TUEFEnquiryIdModel idModel = new TUEFEnquiryIdModel("01", "3111111111115", null);
        ArrayList<TUEFEnquiryIdModel> id = new ArrayList<TUEFEnquiryIdModel>();
        id.add(idModel);


        Command command = new Command();

        try {
            ncrsModel.setId(testNCRS.getId());
            ncrsModel.setPass(testNCRS.getPass());
            ncrsModel.setMemberref(testNCRS.getMemberref());
            ncrsModel.setEnqpurpose(testNCRS.getEnqpurpose());
            ncrsModel.setEnqamount(testNCRS.getEnqamount());
            ncrsModel.setConsent(testNCRS.getConsent());
            ncrsModel.setDisputeenquiry(testNCRS.getDisputeenquiry());

            ncrsModel.setIdList(id);
            ncrsModel.setNameList(name);
            ncrsModel.setUrl(url);

            System.out.println("=========================================process() Model : "+ncrsModel.toString());
            ncrsModel.validation();
            NCRSImp ncrs = new NCRSImp();

            Ncrsresponse response =  null;//ncrs.requestOnline(ncrsModel);
            System.out.println("=========================================process() Call  : requestOnline(NCRSModel)");
            if(null!=response){
                if(!command.ERROR.equals(response.getHeaderModel().getCommand())){
                    //The response (Online) has succeeded
                    //The response will be return (XML Transaction record)
                }else {
                    //Exception NCB
                    //if you want to know Error message
                    //response.getBodyModel().getErrormsg();
                    //throw new ValidationException("Exception : NCB");
                }
            }else {
                System.out.println("=========================================process() Response form requestOnline is null");
                System.out.println("=========================================process() Call  : requestOffline(NCRSModel)");
                response =  null;//ncrs.requestOffline(ncrsModel);
                if(!command.ERROR.equals(response.getHeaderModel().getCommand())){
                    //The response (Offline) has succeeded
                    //The response will be return (trackingid and result)

                    //code
                    //response.getBodyModel().getsTrackingid();
                    //response.getBodyModel().getsResult();

                }else {
                    //if you want to know Error message
                    //response.getBodyModel().getErrormsg();
                    // I don't know...
                }

            }


        } catch (Exception e) {
            System.out.println("=========================================process() Exception : "+e);
        }
    }

}
