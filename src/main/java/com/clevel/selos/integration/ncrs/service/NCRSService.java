package com.clevel.selos.integration.ncrs.service;

import com.clevel.selos.integration.NCB;
import com.clevel.selos.integration.ncrs.models.response.NCRSResponseModel;
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
    NCRSImp ncrsImp;

    @Inject
    @ValidationMessage
    Message message;

    @Inject
    ValidationImp validationImp;

    public final String ERROR = "ER01001";

    @Inject
    public NCRSService() {
    }

    public void process(NCRSModel ncrsModel){

        /*try {
            log.debug("=========================================NCRS process.");
            NCRSResponse ncrsResponse = ncrsInterfaceImpTest.request(ncrsModel);
            NameModel nameModel = ncrsResponse.getBodyModel().getTransaction().getName();
            log.debug("=========================================NCRS TrackingID. {}",ncrsResponse.getBodyModel().getTransaction().getTrackingid());
            log.debug("=========================================NCRS DateOfBirth. {}",nameModel.getDateofbirth());
            IdModel idModel = ncrsResponse.getBodyModel().getTransaction().getId();
            idModel.getIdnumber();

        } catch (Exception e) {
            log.error("=========================================NCRS Exception : {}", e);
        }  */

        log.debug("=========================================NCRS process.");
        try {
            //validationImp.validation(ncrsModel);

            log.debug("=========================================NCRS process. Call  : requestOnline(NCRSModel)");
            NCRSResponseModel ncrsResponse =  ncrsImp.requestOnline(ncrsModel);

            if(null!=ncrsResponse){
                if(!ERROR.equals(ncrsResponse.getHeaderModel().getCommand())){
                    //The response (Online) has succeeded
                    log.debug("=========================================NCRS The response (Online) has succeeded");
                    //The response will be return (XML Transaction record)
                    log.debug("=========================================NCRS User id is {}",ncrsResponse.getHeaderModel().getUser());
                    log.debug("=========================================NCRS Password id is {}",ncrsResponse.getHeaderModel().getPassword());
                    log.debug("=========================================NCRS Command id is {}",ncrsResponse.getHeaderModel().getCommand());

                }else {
                    log.debug("=========================================NCRS The response (Online) has failed");
                    log.debug("=========================================NCRS The error message is {}",ncrsResponse.getBodyModel().getErrormsg());
                    //Exception NCB
                    //if you want to know Error message
                    //response.getBodyModel().getErrormsg();
                    //throw new ValidationException("Exception : NCB");
                }
            }else {
                log.debug("=========================================NCRS process. Response form requestOnline is null");
                log.debug("=========================================NCRS process. Call  : requestOffline(NCRSModel)");
                ncrsResponse =  ncrsImp.requestOffline(ncrsModel);
                if(!ERROR.equals(ncrsResponse.getHeaderModel().getCommand())){
                    //The response (Offline) has succeeded
                    log.debug("=========================================NCRS The response (Offline) has succeeded");

                    log.debug("=========================================NCRS Tracking id is {}",ncrsResponse.getBodyModel().getsTrackingid());
                    log.debug("=========================================NCRS Result id is {}",ncrsResponse.getBodyModel().getsResult());
                    ncrsResponse.getBodyModel().getsResult();
                    //The response will be return (trackingid and result)

                    //code
                    //response.getBodyModel().getsTrackingid();
                    //response.getBodyModel().getsResult();

                }else {
                    log.debug("=========================================NCRS The response (Offline) has failed");
                    log.debug("=========================================NCRS The error message is {}",ncrsResponse.getBodyModel().getErrormsg());
                    //if you want to know Error message
                    //response.getBodyModel().getErrormsg();
                    // I don't know...
                }

            }
        } catch (Exception e) {
            log.error("=========================================NCRS Exception : ", e);
        }
    }

}
