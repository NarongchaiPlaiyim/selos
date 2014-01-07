package com.clevel.selos.integration.bpm.service;

import com.clevel.selos.exception.BPMInterfaceException;
import com.clevel.selos.integration.BPM;
import com.clevel.selos.integration.bpm.model.*;
import com.clevel.selos.integration.bpm.module.DBExecute;
import com.clevel.selos.system.message.ExceptionMapping;
import com.clevel.selos.system.message.ExceptionMessage;
import com.clevel.selos.system.message.Message;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class InboxService implements Serializable {
    @Inject
    @BPM
    Logger log;

    @Inject
    DBExecute dbExecute;

    @Inject
    @ExceptionMessage
    Message msg;

    @Inject
    public InboxService() {

    }

    public List<BPMInbox> getMyBox(String userId, FieldName fieldName, OrderType orderType, int recPerPage, int pageNo) throws Exception {
        log.debug("getMyBox. (userId: {}, fieldName: {}, orderType: {}, recPerPage: {}, pageNo: {})",userId,fieldName,orderType,recPerPage,pageNo);
        List<BPMInbox> bpmInboxList = new ArrayList<BPMInbox>();
        try {
            int count = dbExecute.getMyBoxCount(userId);
            if(count!=0){
                //find total page
                int startRecord = 0;
                int endRecord = 0;

                BigDecimal pageReqNo = new BigDecimal(pageNo);
                BigDecimal totalPage = new BigDecimal(count).divide(new BigDecimal(recPerPage), RoundingMode.UP);
                if(pageReqNo.compareTo(totalPage) < 0){
                    startRecord = ((pageNo-1)*recPerPage)+1;
                    endRecord = pageNo*recPerPage;
                } else if(pageReqNo.compareTo(totalPage) == 0){
                    startRecord = ((pageNo-1)*recPerPage)+1;
                    endRecord = 0;
                }

                bpmInboxList = dbExecute.getMyBox(userId,fieldName,orderType,startRecord,endRecord);
            }/* else {
                throw new BPMInterfaceException(new Exception(), ExceptionMapping.BPM_GET_INBOX_DATA_NOT_FOUND, msg.get(ExceptionMapping.BPM_GET_INBOX_DATA_NOT_FOUND));
            }*/
        } catch (Exception ex) {
            throw ex;
        }
        return bpmInboxList;
    }

    public List<BPMInbox> getReturnBox(String userId, FieldName fieldName, OrderType orderType, int recPerPage, int pageNo) throws Exception {
        log.debug("getReturnBox. (userId: {}, fieldName: {}, orderType: {}, recPerPage: {}, pageNo: {})",userId,fieldName,orderType,recPerPage,pageNo);
        List<BPMInbox> bpmInboxList = new ArrayList<BPMInbox>();
        try {
            int count = dbExecute.getReturnBoxCount(userId);
            if(count!=0){
                //find total page
                int startRecord = 0;
                int endRecord = 0;

                BigDecimal pageReqNo = new BigDecimal(pageNo);
                BigDecimal totalPage = new BigDecimal(count).divide(new BigDecimal(recPerPage), RoundingMode.UP);
                if(pageReqNo.compareTo(totalPage) < 0){
                    startRecord = ((pageNo-1)*recPerPage)+1;
                    endRecord = pageNo*recPerPage;
                } else if(pageReqNo.compareTo(totalPage) == 0){
                    startRecord = ((pageNo-1)*recPerPage)+1;
                    endRecord = 0;
                }

                bpmInboxList = dbExecute.getReturnBox(userId, fieldName, orderType, startRecord, endRecord);
            }/* else {
                throw new BPMInterfaceException(new Exception(), ExceptionMapping.BPM_GET_INBOX_DATA_NOT_FOUND, msg.get(ExceptionMapping.BPM_GET_INBOX_DATA_NOT_FOUND));
            }*/
        } catch (Exception ex) {
            throw ex;
        }
        return bpmInboxList;
    }

    public List<BPMInbox> getBDMUWBox(String userId, FieldName fieldName, OrderType orderType, int recPerPage, int pageNo) throws Exception {
        log.debug("getReturnBox. (userId: {}, fieldName: {}, orderType: {}, recPerPage: {}, pageNo: {})",userId,fieldName,orderType,recPerPage,pageNo);
        List<BPMInbox> bpmInboxList = new ArrayList<BPMInbox>();
        try {
            int count = dbExecute.getBDMUWBoxCount(userId);
            if(count!=0){
                //find total page
                int startRecord = 0;
                int endRecord = 0;

                BigDecimal pageReqNo = new BigDecimal(pageNo);
                BigDecimal totalPage = new BigDecimal(count).divide(new BigDecimal(recPerPage), RoundingMode.UP);
                if(pageReqNo.compareTo(totalPage) < 0){
                    startRecord = ((pageNo-1)*recPerPage)+1;
                    endRecord = pageNo*recPerPage;
                } else if(pageReqNo.compareTo(totalPage) == 0){
                    startRecord = ((pageNo-1)*recPerPage)+1;
                    endRecord = 0;
                }

                bpmInboxList = dbExecute.getBDMUWBox(userId, fieldName, orderType, startRecord, endRecord);
            }/* else {
                throw new BPMInterfaceException(new Exception(), ExceptionMapping.BPM_GET_INBOX_DATA_NOT_FOUND, msg.get(ExceptionMapping.BPM_GET_INBOX_DATA_NOT_FOUND));
            }*/
        } catch (Exception ex) {
            throw ex;
        }
        return bpmInboxList;
    }

    public List<BPMInbox> getPoolBox(String userId, RoleName roleName, FieldName fieldName, OrderType orderType, int recPerPage, int pageNo) throws Exception {

        return null;
    }

    public BPMInboxRecord getInboxRecord(String userId) throws Exception {
        BPMInboxRecord bpmInboxRecord = new BPMInboxRecord();
        try {
            int countMybox = dbExecute.getMyBoxCount(userId);
            int countReturnBox = dbExecute.getReturnBoxCount(userId);
            int countBDMUWBox = dbExecute.getBDMUWBoxCount(userId);
            bpmInboxRecord.setMyboxRecord(countMybox);
            bpmInboxRecord.setReturnBoxRecord(countReturnBox);
            bpmInboxRecord.setBdmUwBoxRecord(countBDMUWBox);
        } catch (Exception ex) {
            throw ex;
        }
        return bpmInboxRecord;
    }
}
