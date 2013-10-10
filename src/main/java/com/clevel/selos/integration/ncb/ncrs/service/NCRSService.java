package com.clevel.selos.integration.ncb.ncrs.service;

import com.clevel.selos.exception.NCBInterfaceException;
import com.clevel.selos.integration.NCB;
import com.clevel.selos.integration.ncb.ncbresult.NCBResultImp;
import com.clevel.selos.integration.ncb.exportncbi.NCBIExportImp;
import com.clevel.selos.integration.ncb.ncrs.ncrsmodel.NCRSInputModel;
import com.clevel.selos.integration.ncb.ncrs.ncrsmodel.NCRSModel;
import com.clevel.selos.integration.ncb.ncrs.ncrsmodel.NCRSOutputModel;
import com.clevel.selos.integration.ncb.vaildation.ValidationImp;
import com.clevel.selos.integration.test.NCBInterfaceImpTest;
import com.clevel.selos.system.message.ExceptionMapping;
import com.clevel.selos.system.message.Message;
import com.clevel.selos.system.message.ValidationMessage;
import com.clevel.selos.util.Util;
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

    private final String exception = ExceptionMapping.NCB_EXCEPTION;
    @Inject
    public NCRSService() {
    }

    public ArrayList<NCRSOutputModel> process(NCRSInputModel inputModel) throws Exception{
        ArrayList<NCRSOutputModel> responseModelArrayList = null;
        try {
            log.debug("NCRS process()");
            boolean flag = resultImp.isChecked(inputModel.getAppRefNumber());
            log.debug("NCRS flag is {}", flag);
            if (!flag){
                ArrayList<NCRSModel> ncrsModelArrayList = inputModel.getNcrsModelArrayList();
                NCRSModel ncrsModel = null;
                for(int i = 0; i<ncrsModelArrayList.size(); i++){
                    ncrsModel = ncrsModelArrayList.get(i);
                    ncrsModel.setMemberref(Util.setRequestNo(inputModel.getAppRefNumber(), i));
                    log.debug("NCRS MemberRef = {}", ncrsModel.getMemberref());
                }
                responseModelArrayList = ncrsImp.requestOnline(inputModel);
                return responseModelArrayList;
            } else {
                responseModelArrayList =  ncrsImp.requestOffline(inputModel);
                return responseModelArrayList;
            }

        } catch (Exception e) {
            String resultDesc = "NCCRS Exception : "+ e.getMessage();
            log.error("NCRS Exception : {}", e.getMessage());
            throw new NCBInterfaceException(new Exception(resultDesc), exception,message.get(exception, resultDesc));
//            throw new Exception("NCRS Exception : "+e.getMessage());
        }
    }

}
