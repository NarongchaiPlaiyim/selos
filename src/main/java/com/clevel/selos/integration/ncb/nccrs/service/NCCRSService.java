package com.clevel.selos.integration.ncb.nccrs.service;

import com.clevel.selos.exception.NCBInterfaceException;
import com.clevel.selos.integration.NCB;
import com.clevel.selos.integration.ncb.exportncbi.NCBIExportImp;
import com.clevel.selos.integration.ncb.ncbresult.NCBResultImp;
import com.clevel.selos.integration.ncb.nccrs.nccrsmodel.NCCRSInputModel;
import com.clevel.selos.integration.ncb.nccrs.nccrsmodel.NCCRSModel;
import com.clevel.selos.integration.ncb.nccrs.nccrsmodel.NCCRSOutputModel;
import com.clevel.selos.system.message.ExceptionMapping;
import com.clevel.selos.system.message.ExceptionMessage;
import com.clevel.selos.system.message.Message;
import com.clevel.selos.system.message.ValidationMessage;
import com.clevel.selos.util.Util;
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
    @ExceptionMessage
    Message message;

    @Inject
    @NCB
    NCBIExportImp exportImp;

    @Inject
    @NCB
    NCBResultImp resultImp;

    private final String exception = ExceptionMapping.NCB_EXCEPTION;

    @Inject
    public NCCRSService() {
    }

    public ArrayList<NCCRSOutputModel> process(NCCRSInputModel inputModel)throws Exception{
        ArrayList<NCCRSOutputModel> responseModelArrayList = null;
        try {
            log.debug("NCCRS process()");
            boolean flag = resultImp.isChecked(inputModel.getAppRefNumber());
            log.debug("NCCRS flag is {}", flag);
            if (!flag){
                ArrayList<NCCRSModel> nccrsModelArrayList = inputModel.getNccrsModelArrayList();
                NCCRSModel nccrsModel = null;
                for(int i = 0; i<nccrsModelArrayList.size(); i++){
                    nccrsModel = nccrsModelArrayList.get(i);
                    nccrsModel.setMemberRef(Util.setRequestNo(inputModel.getAppRefNumber(), i));
                    log.debug("NCRS MemberRef = {}", nccrsModel.getMemberRef());
                }
                responseModelArrayList = nccrsImp.requestOnline(inputModel);
                return responseModelArrayList;
            } else {
                responseModelArrayList = nccrsImp.requestOffline(inputModel);
                return responseModelArrayList;
            }
        } catch (Exception e) {
            String resultDesc = "NCCRS Exception : "+ e.getMessage();
            log.error("NCCRS Exception : {}", e.getMessage());
            throw new NCBInterfaceException(new Exception(resultDesc), exception,message.get(exception, resultDesc));
//            throw new Exception("NCCRS Exception : "+e.getMessage());
        }
    }
}
