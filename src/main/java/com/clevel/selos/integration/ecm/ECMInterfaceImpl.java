package com.clevel.selos.integration.ecm;

import com.clevel.selos.exception.ECMInterfaceException;
import com.clevel.selos.integration.ECM;
import com.clevel.selos.integration.ECMInterface;
import com.clevel.selos.integration.ecm.db.ECMCAPShare;
import com.clevel.selos.integration.ecm.model.ECMDataResult;
import com.clevel.selos.model.ActionResult;
import com.clevel.selos.system.message.ExceptionMapping;
import com.clevel.selos.system.message.ExceptionMessage;
import com.clevel.selos.system.message.Message;
import com.clevel.selos.util.Util;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.io.Serializable;
import java.sql.Date;
import java.util.List;

public class ECMInterfaceImpl implements ECMInterface, Serializable {
    @Inject
    @ECM
    private Logger log;
    @Inject
    @ExceptionMessage
    private Message msg;
    @Inject
    private ECMService ecmService;
    @Inject
    public ECMInterfaceImpl() {

    }

    @Override
    public ECMDataResult getECMDataResult(final String caNumber) {
        ECMDataResult ecmDataResult = null;
        try {
            ecmDataResult = new ECMDataResult();
            ecmDataResult.setEcmDetailList(ecmService.getECMData(caNumber));
            ecmDataResult.setActionResult(ActionResult.SUCCESS);
            return ecmDataResult;
        } catch (ECMInterfaceException e){
            log.error("Exception while get ECM data!", e);
            throw e;
        } catch (Exception e) {
            log.error("Exception while get ECM data!", e);
            String exceptionMessage = msg.get(ExceptionMapping.ECM_EXCEPTION);
            if(!Util.isEmpty(e.getMessage()) && !e.getMessage().trim().equalsIgnoreCase("null")){
                exceptionMessage = e.getMessage();
            }
            throw new ECMInterfaceException(e, ExceptionMapping.ECM_EXCEPTION, exceptionMessage);
        }
    }

    @Override
    public ECMDataResult getECMTypeName(final List<String> ecmDocId) {
        ECMDataResult ecmDataResult = null;
        try {
            ecmDataResult = new ECMDataResult();
            ecmDataResult.setEcmTypeNameMap(ecmService.getECMTypeName(ecmDocId));
            ecmDataResult.setActionResult(ActionResult.SUCCESS);
            return ecmDataResult;
        } catch (ECMInterfaceException e){
            log.error("Exception while get ECM Type Name!", e);
            throw e;
        } catch (Exception e) {
            log.error("Exception while get ECM Type Name!", e);
            String exceptionMessage = msg.get(ExceptionMapping.ECM_EXCEPTION);
            if(!Util.isEmpty(e.getMessage()) && !e.getMessage().trim().equalsIgnoreCase("null")){
                exceptionMessage = e.getMessage();
            }
            throw new ECMInterfaceException(e, ExceptionMapping.ECM_EXCEPTION, exceptionMessage);
        }
    }

    @Override
    public boolean update(final ECMCAPShare ecmcapShare) {
        try {
            ecmcapShare.setCrsLastUpdate(new Date(new java.util.Date().getTime()));
            return ecmService.update(ecmcapShare);
        } catch (Exception e) {
            log.error("Exception while update ECM data!", e);
            return false;
//            throw new ECMInterfaceException(e, ExceptionMapping.ECM_UPDATEDATA_ERROR, e.getMessage());
        }
    }

    @Override
    public boolean insert(final ECMCAPShare ecmcapShare) {
        try {
            ecmcapShare.setCrsCreateDate(new Date(new java.util.Date().getTime()));
            ecmcapShare.setCrsLastUpdate(new Date(new java.util.Date().getTime()));
            return ecmService.insert(ecmcapShare);
        } catch (Exception e) {
            log.error("Exception while insert into ECM data!", e);
            return false;
//            throw new ECMInterfaceException(e, ExceptionMapping.ECM_INSERTDATA_ERROR, e.getMessage());
        }
    }
}
