package com.clevel.selos.integration.ncb.nccrs.service;

import com.clevel.selos.integration.NCB;
import com.clevel.selos.integration.ncb.exportncbi.NCBIExportImp;
import com.clevel.selos.integration.ncb.ncbresult.NCBResultImp;
import com.clevel.selos.integration.ncb.nccrs.nccrsmodel.NCCRSInputModel;
import com.clevel.selos.integration.ncb.nccrs.nccrsmodel.NCCRSOutputModel;
import com.clevel.selos.system.message.Message;
import com.clevel.selos.system.message.ValidationMessage;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.io.Serializable;
import java.util.ArrayList;

public class NCCRSService implements Serializable {

    @Inject
    @NCB
    Logger log;

    @Inject
    NCCRSImp nccrsImp;

    @Inject
    @ValidationMessage
    Message message;

    @Inject
    @NCB
    NCBIExportImp exportImp;

    @Inject
    @NCB
    NCBResultImp resultImp;

    @Inject
    public NCCRSService() {
    }

    public void process(NCCRSInputModel inputModel){
        /*try {
            log.debug("NCCRS process.");
            NCCRSResponseModel responseModel = nccrsInterfaceImpTest.request(nccrsModel);

            log.debug("NCCRS User. {}",responseModel.getHeader().getUser());

        } catch (Exception e) {
            log.error("NCCRS Exception : {}", e);
        }  */

        ArrayList<NCCRSOutputModel> responseModelArrayList = null;
        if(true){
        try {
            log.debug("NCCRS process()");
            boolean flag = resultImp.isChecked(inputModel.getAppRefNumber());
            log.debug("NCCRS flag is {}", flag);
            if (!flag){
                responseModelArrayList = nccrsImp.requestOnline(inputModel);
                for (NCCRSOutputModel outputModel : responseModelArrayList){
                    if("FAILED".equals(outputModel.getActionResult())){
                        log.debug("NCCRS Online check ncb id is {}, resutl is {} and reason is {}",outputModel.getIdNumber(), outputModel.getActionResult(), outputModel.getReason());
                    } else {
                        log.debug("NCCRS Online check ncb id is {}, resutl is {} and reason is {}",outputModel.getIdNumber(), outputModel.getActionResult(), outputModel.getReason());
                    }
                }
            } else {
                responseModelArrayList = nccrsImp.requestOffline(inputModel);
                for (NCCRSOutputModel outputModel : responseModelArrayList){
                    if("FAILED".equals(outputModel.getActionResult())){
                        log.debug("NCCRS Online check ncb id is {}, resutl is {} and reason is {}",outputModel.getIdNumber(), outputModel.getActionResult(), outputModel.getReason());
                    } else {
                        log.debug("NCCRS Online check ncb id is {}, resutl is {} and reason is {}",outputModel.getIdNumber(), outputModel.getActionResult(), outputModel.getReason());
                    }
                }
            }
        } catch (Exception e) {
            log.error("NCCRS Exception : {}", e.getMessage());
        }
        }



    }
}
