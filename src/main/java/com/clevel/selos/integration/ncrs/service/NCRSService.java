package com.clevel.selos.integration.ncrs.service;


import com.clevel.selos.integration.NCB;
import com.clevel.selos.integration.ncrs.models.response.NCRSResponse;
import com.clevel.selos.integration.ncrs.vaildation.ValidationImp;
import com.clevel.selos.system.message.Message;
import com.clevel.selos.system.message.ValidationMessage;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.io.Serializable;

public class NCRSService implements Serializable {

    @Inject
    @NCB
    Logger log;

    @Inject
    NCRSImp ncrs;

    @Inject
    @ValidationMessage
    Message message;

    @Inject
    ValidationImp validationImp;

    @Inject
    DataForTEST dataForTEST;

    public final String ERROR = "ER01001";

    @Inject
    public NCRSService() {

    }

    public void process(NCRSModel ncrsModel){
        /*try {
            log.debug("========================================= process.");

            NCRSResponse ncrsResponse = dataForTEST.request(ncrsModel);

            NameModel nameModel = ncrsResponse.getBodyModel().getTransaction().getName();

            log.debug("========================================= TrackingID. {}",ncrsResponse.getBodyModel().getTransaction().getTrackingid());

            nameModel.getDateofbirth();

            IdModel idModel = ncrsResponse.getBodyModel().getTransaction().getId();
            idModel.getIdnumber();

        } catch (Exception e) {
            log.error("========================================= Exception : {}", e);
        }  */

        log.debug("========================================= process.");
        try {
            validationImp.validation(ncrsModel);

            log.debug("=========================================process. Call  : requestOnline(NCRSModel)");
            NCRSResponse ncrsResponse =  ncrs.requestOnline(ncrsModel);

            if(null!=ncrsResponse){
                if(!ERROR.equals(ncrsResponse.getHeaderModel().getCommand())){
                    //The response (Online) has succeeded
                    log.debug("========================================= The response (Online) has succeeded");
                    //The response will be return (XML Transaction record)
                    log.debug("========================================= User id is ",ncrsResponse.getHeaderModel().getUser());
                    log.debug("========================================= Password id is ",ncrsResponse.getHeaderModel().getPassword());
                    log.debug("========================================= Command id is ",ncrsResponse.getHeaderModel().getCommand());

                }else {

                    //Exception NCB
                    //if you want to know Error message
                    //response.getBodyModel().getErrormsg();
                    //throw new ValidationException("Exception : NCB");
                }
            }else {
                System.out.println("=========================================process. Response form requestOnline is null");
                System.out.println("=========================================process. Call  : requestOffline(NCRSModel)");
                ncrsResponse =  ncrs.requestOffline(ncrsModel);
                if(!ERROR.equals(ncrsResponse.getHeaderModel().getCommand())){
                    //The response (Offline) has succeeded
                    log.debug("The response (Offline) has succeeded");

                    log.debug("Tracking id is ",ncrsResponse.getBodyModel().getsTrackingid());
                    log.debug("Result id is ",ncrsResponse.getBodyModel().getsResult());
                    ncrsResponse.getBodyModel().getsResult();
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
            log.error("========================================= Exception : {}", e);
        }
    }

}
