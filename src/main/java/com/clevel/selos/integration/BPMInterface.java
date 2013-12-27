package com.clevel.selos.integration;

import com.clevel.selos.filenet.bpm.services.dto.CaseDTO;
import com.clevel.selos.integration.bpm.model.BPMInbox;
import com.clevel.selos.integration.bpm.model.FieldName;
import com.clevel.selos.integration.bpm.model.OrderType;
import com.clevel.selos.model.db.history.CaseCreationHistory;

import java.util.HashMap;
import java.util.List;

public interface BPMInterface {
    public boolean createCase(CaseCreationHistory caseCreationHistory);

    public void authenticate(String userName, String password);

    public List<CaseDTO> getInboxList();

    public void dispatchCase(String queueName, String wobNumber, HashMap<String, String> fields);

    public void lockCase(String queueName, String wobNumber);

    public void unLockCase(String queueName, String wobNumber);

    public List<BPMInbox> getMyBoxList(String userId, FieldName fieldName, OrderType orderType, int recPerPage, int pageNo);

    public List<BPMInbox> getReturnBoxList(String userId, FieldName fieldName, OrderType orderType, int recPerPage, int pageNo);

    public List<BPMInbox> getBDMUWBoxList(String userId, FieldName fieldName, OrderType orderType, int recPerPage, int pageNo);
}
