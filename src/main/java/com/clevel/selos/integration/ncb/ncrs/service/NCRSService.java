package com.clevel.selos.integration.ncb.ncrs.service;

import com.clevel.selos.integration.NCB;
import com.clevel.selos.integration.ncb.ncbresult.NCBResultImp;
import com.clevel.selos.integration.ncb.exportncbi.NCBIExportImp;
import com.clevel.selos.integration.ncb.ncrs.ncrsmodel.NCRSInputModel;
import com.clevel.selos.integration.ncb.ncrs.ncrsmodel.NCRSOutputModel;
import com.clevel.selos.integration.ncb.vaildation.ValidationImp;
import com.clevel.selos.integration.test.NCBInterfaceImpTest;
import com.clevel.selos.system.message.Message;
import com.clevel.selos.system.message.ValidationMessage;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.io.Serializable;
import java.util.ArrayList;

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

    public void process(NCRSInputModel inputModel){

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

        ArrayList<NCRSOutputModel> responseModelArrayList = null;
        if(true){//
        try {
            log.debug("NCRS process()");
            boolean flag = resultImp.isChecked(inputModel.getAppRefNumber());
            log.debug("NCRS flag is {}", flag);
            if (!flag){
                responseModelArrayList = ncrsImp.requestOnline(inputModel);
                for (NCRSOutputModel outputModel : responseModelArrayList){
                    if("FAILED".equals(outputModel.getActionResult())){
                        log.debug("NCRS Online check ncb id is {}, resutl is {} and reason is {}",outputModel.getIdNumber(), outputModel.getActionResult(), outputModel.getReason());
                    } else {
                        log.debug("NCRS Online check ncb id is {}, resutl is {} and reason is {}",outputModel.getIdNumber(), outputModel.getActionResult(), outputModel.getReason());
                    }
                }
            } else {
                responseModelArrayList =  ncrsImp.requestOffline(inputModel);
                for (NCRSOutputModel outputModel : responseModelArrayList){
                    if("FAILED".equals(outputModel.getActionResult())){
                        log.debug("NCRS Offline check ncb id is {}, resutl is {} and reason is {}",outputModel.getIdNumber(), outputModel.getActionResult(), outputModel.getReason());
                    } else {
                        log.debug("NCRS Offline check ncb id is {}, resutl is {} and reason is {}",outputModel.getIdNumber(), outputModel.getActionResult(), outputModel.getReason());
                    }
                }
            }

        } catch (Exception e) {
            log.error("NCRS Exception : {}", e.getMessage());
        }
        }//

    }

}
