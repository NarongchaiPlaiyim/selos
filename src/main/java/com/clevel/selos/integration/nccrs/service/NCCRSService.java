package com.clevel.selos.integration.nccrs.service;

import com.clevel.selos.integration.NCB;
import com.clevel.selos.integration.nccrs.models.response.NCCRSResponseModel;
import com.clevel.selos.system.message.Message;
import com.clevel.selos.system.message.ValidationMessage;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.io.Serializable;

public class NCCRSService implements Serializable {

    @Inject
    @NCB
    Logger log;

    @Inject
    NCCRSImp nccrsImp;

    @Inject
    @ValidationMessage
    Message message;

    public final String ERROR = "ER01001";

    @Inject
    public NCCRSService() {
    }

    public void process(NCCRSModel nccrsModel){
        /*try {
            log.debug("=========================================NCCRS process.");
            NCCRSResponseModel responseModel = nccrsInterfaceImpTest.request(nccrsModel);

            log.debug("=========================================NCCRS User. {}",responseModel.getHeader().getUser());

        } catch (Exception e) {
            log.error("=========================================NCCRS Exception : {}", e);
        }  */

        log.debug("=========================================NCCRS process.");
        try {

            log.debug("=========================================NCCRS process. Call  : requestOnline(NCRSModel)");

            NCCRSResponseModel responseModel = nccrsImp.requestOnline(nccrsModel);

            if(null!=responseModel){
                if(!ERROR.equals(responseModel.getHeader().getCommand())){
                    //The response (Online) has succeeded
                    log.debug("=========================================NCCRS The response (Online) has succeeded");
                    log.debug("=========================================NCCRS User id is {}",responseModel.getHeader().getUser());
                    log.debug("=========================================NCCRS Password id is {}",responseModel.getHeader().getPassword());
                    log.debug("=========================================NCCRS Command id is {}",responseModel.getHeader().getCommand());
                    //The response will be return (XML Transaction record)

                }else {
                    log.debug("=========================================NCCRS The response (Online) has failed");
                    log.debug("=========================================NCCRS The error message is {}",responseModel.getBody().getErrormsg());
                    //Exception NCB
                    //if you want to know Error message
                    //response.getBodyModel().getErrormsg();
                    //throw new ValidationException("Exception : NCB");
                }
            }else {
                log.debug("=========================================NCCRS process. Response form requestOnline is null");
                log.debug("=========================================NCCRS process. Call  : requestOffline(NCCRSModel)");

                responseModel = nccrsImp.requestOnline(nccrsModel);
                if(!ERROR.equals(responseModel.getHeader().getCommand())){
                    //The response (Offline) has succeeded
                    log.debug("=========================================NCCRS The response (Offline) has succeeded");
                    log.debug("=========================================NCCRS Tracking id is {}",responseModel.getBody().getTrackingid());
                    log.debug("=========================================NCCRS Result id is {}",responseModel.getBody().getResult());
                    //The response will be return (trackingid and result)

                    //code
                    //response.getBodyModel().getsTrackingid();
                    //response.getBodyModel().getsResult();

                }else {
                    log.debug("=========================================NCCRS The response (Offline) has failed");
                    log.debug("=========================================NCCRS The error message is {}",responseModel.getBody().getErrormsg());
                    //if you want to know Error message
                    //response.getBodyModel().getErrormsg();
                    // I don't know...
                }

            }
        } catch (Exception e) {
            log.error("=========================================NCCRS Exception : {}", e);
        }

    }
}
