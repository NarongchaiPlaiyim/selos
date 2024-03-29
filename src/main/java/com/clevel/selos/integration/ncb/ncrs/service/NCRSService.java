package com.clevel.selos.integration.ncb.ncrs.service;

import com.clevel.selos.exception.NCBInterfaceException;
import com.clevel.selos.integration.NCB;
import com.clevel.selos.integration.ncb.exportncbi.NCBIExportImp;
import com.clevel.selos.integration.ncb.ncbresult.NCBResultImp;
import com.clevel.selos.integration.ncb.ncrs.ncrsmodel.NCRSInputModel;
import com.clevel.selos.integration.ncb.ncrs.ncrsmodel.NCRSModel;
import com.clevel.selos.integration.ncb.ncrs.ncrsmodel.NCRSOutputModel;
import com.clevel.selos.integration.ncb.vaildation.ValidationImp;
import com.clevel.selos.integration.test.NCBInterfaceImpTest;
import com.clevel.selos.system.message.ExceptionMapping;
import com.clevel.selos.system.message.ExceptionMessage;
import com.clevel.selos.system.message.Message;
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
    @ExceptionMessage
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

    public ArrayList<NCRSOutputModel> process(NCRSInputModel inputModel) throws Exception {
        ArrayList<NCRSOutputModel> responseModelArrayList = null;
        NCRSInputModel inputModelForNewCustomers = null;
        ArrayList<NCRSModel> ncrsModelArrayListForNewCustomers = null;
        try {
            log.debug("NCRS process()");
            boolean flag = resultImp.isChecked(inputModel.getAppRefNumber());
            log.debug("NCRS flag is {}", flag);
            if (!flag) {
                ArrayList<NCRSModel> ncrsModelArrayList = inputModel.getNcrsModelArrayList();
                NCRSModel ncrsModel = null;
                for (int i = 0; i < ncrsModelArrayList.size(); i++) {
                    ncrsModel = ncrsModelArrayList.get(i);
                    ncrsModel.setMemberref(Util.setRequestNo(inputModel.getAppRefNumber(), i));
                    log.debug("NCRS MemberRef = {}", ncrsModel.getMemberref());
                }
                responseModelArrayList = ncrsImp.requestOnline(inputModel);
                return responseModelArrayList;
            } else {
                ArrayList<NCRSModel> ncrsModelArrayList = inputModel.getNcrsModelArrayList();
                NCRSModel ncrsModel = null;
                int resultOfSize = 0;
                boolean result = false;
                boolean flagCheckNewCustomer = false;
                resultOfSize = resultImp.getSizeFromAppNumber(inputModel.getAppRefNumber());
                ncrsModelArrayListForNewCustomers = new ArrayList<NCRSModel>();
                pointer:
                for (int i = 0; i < ncrsModelArrayList.size(); i++) {
                    ncrsModel = ncrsModelArrayList.get(i);
                    result = resultImp.isOldCustomer(inputModel.getAppRefNumber(), ncrsModel.getCitizenId());
                    if (!result) {
                        flagCheckNewCustomer = true;
                        if (0 != resultOfSize) {
                            ncrsModel.setMemberref(Util.setRequestNo(inputModel.getAppRefNumber(), resultOfSize));
                            resultOfSize++;
                            log.debug("NCRS MemberRef = {}", ncrsModel.getMemberref());
                            ncrsModelArrayListForNewCustomers.add(ncrsModel);
                            ncrsModelArrayList.remove(i);
                            i--;
                        } else {
                            continue pointer;
                        }
                    }
                }

                if (flagCheckNewCustomer) {
                    log.debug("Request offline and online");
                    inputModelForNewCustomers = new NCRSInputModel(inputModel.getUserId(), inputModel.getAppRefNumber(), inputModel.getCANumber(), inputModel.getReferenceTel(), ncrsModelArrayListForNewCustomers);
                    responseModelArrayList = ncrsImp.requestOffline(inputModel);
                    responseModelArrayList.addAll(ncrsImp.requestOnline(inputModelForNewCustomers));
                    return responseModelArrayList;
                } else {
                    log.debug("Request offline");
                    responseModelArrayList = ncrsImp.requestOffline(inputModel);
                    return responseModelArrayList;
                }
            }
        } catch (Exception e) {
            String resultDesc = "NCCRS Exception : " + e.getMessage();
            log.error("NCRS Exception", e);
            throw new NCBInterfaceException(e, exception, message.get(exception, resultDesc));
        }
    }

}
