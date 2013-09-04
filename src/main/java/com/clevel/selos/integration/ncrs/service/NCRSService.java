package com.clevel.selos.integration.ncrs.service;


//import com.clevel.selos.controller.TestNCRS;
import com.clevel.selos.integration.Integration;
import com.clevel.selos.integration.ncrs.models.request.TUEFEnquiryIdModel;
import com.clevel.selos.integration.ncrs.models.request.TUEFEnquiryNameModel;
import com.clevel.selos.integration.ncrs.models.response.Ncrsresponse;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.io.Serializable;
import java.util.ArrayList;

public class NCRSService implements Serializable {
    @Inject
    @Integration(Integration.System.NCB)
    Logger log;

    @Inject
    public NCRSService() {

    }

    public void process(){
        //!!!!! TEST !!!!!
        //log.debug("========================================= process().");
        System.out.println("========================================= process().");
        NCRSModel ncrsModel = new NCRSModel();

        String url = "http://10.175.230.112/ncrs/servlet/xmladapter";
        System.out.println("URL : "+url);

        TUEFEnquiryNameModel nameModel = new TUEFEnquiryNameModel("aa", "bb", null);
        ArrayList<TUEFEnquiryNameModel> name = new ArrayList<TUEFEnquiryNameModel>();
        name.add(nameModel);

        TUEFEnquiryIdModel idModel = new TUEFEnquiryIdModel("01", "3111111111115", null);
        ArrayList<TUEFEnquiryIdModel> id = new ArrayList<TUEFEnquiryIdModel>();
        id.add(idModel);


        try {
            ncrsModel.setId("SLOSTEST");
            ncrsModel.setPass("SLOSTEST12");
            ncrsModel.setMemberref("123456789");
            ncrsModel.setEnqpurpose("01");
            ncrsModel.setEnqamount(null);
            ncrsModel.setConsent("Y");
            ncrsModel.setDisputeenquiry(null);

            ncrsModel.setIdList(id);
            ncrsModel.setNameList(name);
            ncrsModel.setUrl(url);

            System.out.println("========================================= Model : "+ncrsModel.toString());

            NCRSImp ncrs = new NCRSImp();

            Ncrsresponse response =  ncrs.requestOnline(ncrsModel);

            if(null!=response){
//                TestNCRS testNCRS = new TestNCRS();
//                testNCRS.setResult("========================================= DONE!!!");
//                System.out.println("========================================= User : "+response.getHeaderModel().getUser());
//                System.out.println("========================================= Pass : "+response.getHeaderModel().getPassword());
//                System.out.println("========================================= Command : "+response.getHeaderModel().getCommand());
            }else {
                System.out.println("========================================= Response is null");
            }


        } catch (Exception e) {
            System.out.println("Exception : "+e);
        }
    }

}
