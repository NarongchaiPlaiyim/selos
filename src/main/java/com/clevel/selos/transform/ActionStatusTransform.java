package com.clevel.selos.transform;

import com.clevel.selos.model.ActionResult;
import com.clevel.selos.model.view.ActionStatusView;

public class ActionStatusTransform extends Transform {

    public ActionStatusView getActionStatusView(ActionResult actionResult, String desc) {
        ActionStatusView actionStatusView = new ActionStatusView();
        actionStatusView.setStatusCode(actionResult);
        actionStatusView.setStatusDesc(desc);
        return actionStatusView;
    }
}
