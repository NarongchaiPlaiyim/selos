package com.clevel.selos.integration.ncrs.service;

import com.clevel.selos.integration.NCB;
import com.clevel.selos.integration.nccrs.service.NCBIExportImp;
import com.clevel.selos.integration.ncrs.models.response.NCRSResponseModel;
import com.clevel.selos.integration.ncrs.ncrsmodel.NCRSModel;
import com.clevel.selos.integration.ncrs.ncrsmodel.ResponseNCRSModel;
import com.clevel.selos.integration.ncrs.vaildation.ValidationImp;
import com.clevel.selos.integration.test.NCBInterfaceImpTest;
import com.clevel.selos.model.db.ext.ncb.NCBResult;
import com.clevel.selos.system.message.Message;
import com.clevel.selos.system.message.ValidationMessage;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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

    @Inject
    NCBInterfaceImpTest ncbInterfaceImpTest;

    @Inject
    @NCB
    NCBIExportImp exportImp;

    @Inject
    @NCB
    NCBResultImp resultImp;

    public final String ERROR = "ER01001";

    @Inject
    public NCRSService() {
    }

    public void process(NCRSModel ncrsModel){

        /*try {
            log.debug("NCRS process.");
            NCRSResponseModel ncrsResponse = ncbInterfaceImpTest.request(ncrsModel);
            NameModel nameModel = ncrsResponse.getBodyModel().getTransaction().getName();
            log.debug("NCRS TrackingID. {}",ncrsResponse.getBodyModel().getTransaction().getTrackingid());
            log.debug("NCRS DateOfBirth. {}",nameModel.getDateofbirth());
            log.debug("NCRS Test {}",ncrsResponse.getBodyModel().getErrormsg());
            String test = ncrsResponse.getBodyModel().getErrormsg();

            if ("null".equals(test)){
                log.debug("NCRS Test {}","String is null");
            }

            if (null == test){
                log.debug("NCRS Test {}","String is null pointer");
            }

            String user = ncrsResponse.getHeaderModel().getUser();

            if (user == null)log.debug("NCRS User is null {}", user);
            if ("".equals(user))log.debug("NCRS User is emtry {}", user);

            IdModel idModel = ncrsResponse.getBodyModel().getTransaction().getId();
            idModel.getIdnumber();

        } catch (Exception e) {
            log.error("NCRS Exception : {}", e);
        }*/

        ArrayList<ResponseNCRSModel> responseModelArrayList = null;
        if(true){//
        try {
            log.debug("NCRS process()");
            boolean flag = resultImp.isChecked(ncrsModel.getAppRefNumber());
            log.debug("NCRS flag is {}", flag);
            if (!flag){
                responseModelArrayList = ncrsImp.requestOnline(ncrsModel);
                for (ResponseNCRSModel responseModel : responseModelArrayList){
                    if("FAILED".equals(responseModel.getActionResult())){
                        log.debug("NCRS Online check ncb id is {}, resutl is {} and reason is {}",responseModel.getIdNumber(), responseModel.getActionResult(), responseModel.getReason());
                    } else {
                        log.debug("NCRS Online check ncb id is {}, resutl is {} and reason is {}",responseModel.getIdNumber(), responseModel.getActionResult(), responseModel.getReason());
                    }
                }
            } else {
                responseModelArrayList =  ncrsImp.requestOffline(ncrsModel);
                for (ResponseNCRSModel responseModel : responseModelArrayList){
                    if("FAILED".equals(responseModel.getActionResult())){
                        log.debug("NCRS Offline check ncb id is {}, resutl is {} and reason is {}",responseModel.getIdNumber(), responseModel.getActionResult(), responseModel.getReason());
                    } else {
                        log.debug("NCRS Offline check ncb id is {}, resutl is {} and reason is {}",responseModel.getIdNumber(), responseModel.getActionResult(), responseModel.getReason());
                    }
                }
            }

        } catch (Exception e) {
            log.error("NCRS Exception : {}", e.getMessage());
        }
        }//

    }

}
