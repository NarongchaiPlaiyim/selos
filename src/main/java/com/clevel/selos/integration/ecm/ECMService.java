package com.clevel.selos.integration.ecm;

import com.clevel.selos.exception.ECMInterfaceException;
import com.clevel.selos.integration.NCB;
import com.clevel.selos.integration.ecm.db.ECMCAPShare;
import com.clevel.selos.integration.ecm.db.ECMDetail;
import com.clevel.selos.integration.ecm.module.DBExecute;
import com.clevel.selos.system.message.ExceptionMapping;
import com.clevel.selos.system.message.ExceptionMessage;
import com.clevel.selos.system.message.Message;
import com.clevel.selos.util.Util;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.io.Serializable;
import java.util.List;

public class ECMService implements Serializable {
    @Inject
    @NCB
    private Logger log;
    @Inject
    private DBExecute dbExecute;
    @Inject
    @ExceptionMessage
    Message msg;
    @Inject
    public ECMService() {

    }

    public List<ECMDetail> getECMData(final String caNumber) throws Exception{
        List<ECMDetail> ecmDetailList = null;
        try {
            if(!Util.isNull(caNumber) && !Util.isZero(caNumber.length())){
                ecmDetailList = Util.safetyList(dbExecute.findByCANumber(caNumber));
                log.debug("-- EcmDetailList.size[{}]", ecmDetailList.size());
                if(Util.isZero(ecmDetailList.size())){
                    log.debug("Data Not Found!");
                    throw new ECMInterfaceException(new Exception(msg.get(ExceptionMapping.ECM_DATA_NOT_FOUND)),ExceptionMapping.ECM_DATA_NOT_FOUND, msg.get(ExceptionMapping.ECM_DATA_NOT_FOUND));
                }
            }
            return ecmDetailList;
        } catch (ECMInterfaceException e){
            log.error("ECMInterfaceException while get ECM data!",e);
            throw e;
        } catch (Exception e) {
            log.error("Exception while get ECM data!",e);
            throw new Exception(e.getMessage());
        }
    }

    public boolean update(final ECMCAPShare ecmcapShare) throws Exception{
        if(Util.isNull(ecmcapShare)){
            log.error("Exception while update ECM! ECMCAPShare is {}", ecmcapShare);
            throw new Exception("ECMCAPShare is null");
        } else {
            if(Util.isNull(ecmcapShare.getCrsUKCANumber())){
                log.error("Exception while update ECM! ECMCAPShare is {}", ecmcapShare.getCrsUKCANumber());
                throw new Exception("ECMCAPShare.CRSUKCANumber[null]");
            }
        }
        try {
            if(dbExecute.updateECM(ecmcapShare)){
                log.debug("AppNumber[{}] updated", ecmcapShare.getCrsUKCANumber());
                return true;
            } else {
                log.debug("AppNumber[{}] canceled", ecmcapShare.getCrsUKCANumber());
                return false;
            }
        } catch (ECMInterfaceException e){
            log.error("ECMInterfaceException while update ECM!",e);
            throw e;
        } catch (Exception e) {
            log.error("Exception while update ECM!",e);
            throw new Exception(e.getMessage());
        }
    }

    public boolean insert(final ECMCAPShare ecmcapShare) throws Exception{
        if(Util.isNull(ecmcapShare)){
            log.error("Exception while insert ECM! ECMCAPShare is {}", ecmcapShare);
            throw new Exception("ECMCAPShare is null");
        } else {
            if(Util.isNull(ecmcapShare.getCrsUKCANumber())){
                log.error("Exception while insert ECM! ECMCAPShare is {}", ecmcapShare.getCrsUKCANumber());
                throw new Exception("ECMCAPShare.CRSUKCANumber[null]");
            }

            if(Util.isNull(ecmcapShare.getCrsCustName())){
                log.error("Exception while insert ECM! ECMCAPShare is {}", ecmcapShare.getCrsCustName());
                throw new Exception("ECMCAPShare.CRSCustName[null]");
            }
        }
        try {
            if(dbExecute.insertIntoECM(ecmcapShare)){
                log.debug("AppNumber[{}] inserted", ecmcapShare.getCrsUKCANumber());
                return true;
            } else {
                log.debug("AppNumber[{}] canceled", ecmcapShare.getCrsUKCANumber());
                return false;
            }
        } catch (ECMInterfaceException e){
            log.error("ECMInterfaceException while insert into ECM!",e);
            throw e;
        } catch (Exception e) {
            log.error("Exception while insert into ECM!",e);
            throw new Exception(e.getMessage());
        }
    }

}
