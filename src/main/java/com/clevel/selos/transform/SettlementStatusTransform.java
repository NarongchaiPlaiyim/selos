package com.clevel.selos.transform;

import com.clevel.selos.dao.master.SettlementStatusDAO;
import com.clevel.selos.model.db.master.SettlementStatus;
import com.clevel.selos.model.view.SettlementStatusView;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class SettlementStatusTransform extends Transform {
    @Inject
    private SettlementStatusDAO settlementStatusDAO;

    @Inject
    public SettlementStatusTransform() {
    }

    public SettlementStatus transformToModel(SettlementStatusView settlementStatusView) {
        if (settlementStatusView == null) {
            return null;
        }

        SettlementStatus settlementStatus = null;
        if (settlementStatusView.getId() != 0) {
            settlementStatus = settlementStatusDAO.findById(settlementStatusView.getId());
        }

        /*settlementStatus.setId(settlementStatusView.getId());
        settlementStatus.setActive(settlementStatusView.getActive());
        settlementStatus.setName(settlementStatusView.getName());
        settlementStatus.setNcbCode(settlementStatusView.getNcbCode());*/

        return settlementStatus;
    }

    public List<SettlementStatus> transformToModel(List<SettlementStatusView> settlementStatusViewList) {
        List<SettlementStatus> settlementStatusList = new ArrayList<SettlementStatus>();
        if (settlementStatusViewList == null) {
            return settlementStatusList;
        }
        for (SettlementStatusView settlementStatusView : settlementStatusViewList) {
            settlementStatusList.add(transformToModel(settlementStatusView));
        }
        return settlementStatusList;
    }

    public SettlementStatusView transformToView(SettlementStatus settlementStatus) {
        SettlementStatusView settlementStatusView = new SettlementStatusView();
        if (settlementStatus == null) {
            return settlementStatusView;
        }
        settlementStatusView.setId(settlementStatus.getId());
        settlementStatusView.setActive(settlementStatus.getActive());
        settlementStatusView.setName(settlementStatus.getName());
        settlementStatusView.setNcbCode(settlementStatus.getNcbCode());
        return settlementStatusView;
    }

    public List<SettlementStatusView> transformToView(List<SettlementStatus> settlementStatusList) {
        List<SettlementStatusView> settlementStatusViewList = new ArrayList<SettlementStatusView>();
        if (settlementStatusList == null) {
            return settlementStatusViewList;
        }
        for (SettlementStatus settlementStatus : settlementStatusList) {
            settlementStatusViewList.add(transformToView(settlementStatus));
        }
        return settlementStatusViewList;
    }
}
