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
        NCCRSInputModel inputModelForNewCustomers = null;
        ArrayList<NCCRSModel> ncrsModelArrayListForNewCustomers = null;
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
                ArrayList<NCCRSModel> nccrsModelArrayList = inputModel.getNccrsModelArrayList();
                NCCRSModel nccrsModel = null;
                int resultOfSize = 0;
                boolean result = false;
                boolean flagCheckNewCustomer = false;
                resultOfSize = resultImp.getSizeFromAppNumber(inputModel.getAppRefNumber());
                ncrsModelArrayListForNewCustomers = new ArrayList<NCCRSModel>();
                pointer : for(int i = 0; i<nccrsModelArrayList.size(); i++){
                    nccrsModel = nccrsModelArrayList.get(i);
                    result = resultImp.isOldCustomer(inputModel.getAppRefNumber(), nccrsModel.getRegistId());
                    if (!result) {
                        flagCheckNewCustomer = true;
                        if (0!=resultOfSize){
                            nccrsModel.setMemberRef(Util.setRequestNo(inputModel.getAppRefNumber(), resultOfSize));
                            resultOfSize++;
                            log.debug("NCRS MemberRef = {}", nccrsModel.getMemberRef());
                            ncrsModelArrayListForNewCustomers.add(nccrsModel);
                            nccrsModelArrayList.remove(i);
                            i--;
                        } else {
                            continue pointer;
                        }
                    }
                }

                if (flagCheckNewCustomer) {
                    log.debug("Request offline and online");
                    inputModelForNewCustomers = new NCCRSInputModel(inputModel.getUserId(), inputModel.getAppRefNumber(), inputModel.getCANumber(), inputModel.getReferenceTel(), ncrsModelArrayListForNewCustomers);
                    responseModelArrayList =  nccrsImp.requestOffline(inputModel);
                    responseModelArrayList.addAll(nccrsImp.requestOnline(inputModelForNewCustomers));
                    return responseModelArrayList;
                } else {
                    log.debug("Request offline");
                    responseModelArrayList =  nccrsImp.requestOffline(inputModel);
                    return responseModelArrayList;
                }
            }
        } catch (Exception e) {
            String resultDesc = "NCCRS Exception : "+ e.getMessage();
            log.error("NCCRS Exception", e);
            throw new NCBInterfaceException(e, exception,message.get(exception, resultDesc));
//            throw new Exception("NCCRS Exception : "+e.getMessage());
        }
    }
}
