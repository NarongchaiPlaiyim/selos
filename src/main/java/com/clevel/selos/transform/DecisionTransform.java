package com.clevel.selos.transform;

import com.clevel.selos.model.db.working.Decision;
import com.clevel.selos.model.view.DecisionView;

public class DecisionTransform extends Transform {

    public Decision getDecisionModel(DecisionView decisionView) {
        Decision decision = new Decision();
        return decision;
    }

    public DecisionView getDecisionView(Decision decision) {
        DecisionView decisionView = new DecisionView();
        return decisionView;
    }
}
