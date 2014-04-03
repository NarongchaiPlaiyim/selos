package com.clevel.selos.integration;

import com.clevel.selos.filenet.bpm.services.dto.CaseDTO;
import com.clevel.selos.integration.bpm.model.BPMInbox;
import com.clevel.selos.integration.bpm.model.BPMInboxRecord;
import com.clevel.selos.integration.bpm.model.FieldName;
import com.clevel.selos.integration.bpm.model.OrderType;
import com.clevel.selos.model.db.history.CaseCreationHistory;

import java.util.HashMap;
import java.util.List;

public interface BPMInterface {
    public boolean createCase(CaseCreationHistory caseCreationHistory);

    public boolean createParallelCase(String appNumber, String borrowerName, String productGroup, int requestType, String bdmUserName);

    public void authenticate(String userName, String password);

    public List<CaseDTO> getInboxList();

    public List<CaseDTO> getInboxPoolList(String queueName);

    public void dispatchCase(String queueName, String wobNumber, HashMap<String, String> fields);

    public void lockCase(String queueName, String wobNumber);

    public void unLockCase(String queueName, String wobNumber);

    public List<BPMInbox> getMyBoxList(String userId, FieldName fieldName, OrderType orderType, int recPerPage, int pageNo);

    public List<BPMInbox> getReturnBoxList(String userId, FieldName fieldName, OrderType orderType, int recPerPage, int pageNo);

    public List<BPMInbox> getBDMUWBoxList(String userId, FieldName fieldName, OrderType orderType, int recPerPage, int pageNo);

    public BPMInboxRecord getInboxRecord(String userId);

    public void batchDispatchCaseFromRoster(String s, String[] strings, HashMap<String, String> stringStringHashMap);

    public void updateCase(String queueName, String wobNumber, HashMap<String, String> fields);


}
