package com.clevel.selos.transform;

import com.clevel.selos.integration.brms.model.request.PreScreenRequest;
import com.clevel.selos.integration.brms.model.response.PreScreenResponse;
import com.clevel.selos.model.view.CustomerInfoView;
import com.clevel.selos.model.view.PreScreenResponseView;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class PreScreenResultTransform extends Transform {

    @Inject
    public PreScreenResultTransform(){

    }

    public PreScreenRequest transformToRequest(List<CustomerInfoView> customerInfoViewList){
        PreScreenRequest preScreenRequest = new PreScreenRequest();
        //TODO Transform to preScreenRequest
        return preScreenRequest;
    }

    public List<PreScreenResponseView> transformResponseToView(List<PreScreenResponse> presCreenResponses){
        List<PreScreenResponseView> preScreenResponseList = new ArrayList<PreScreenResponseView>();
        //TODO Transform to preScreenResponse
        return preScreenResponseList;
    }

    public List<PreScreenResponseView> tranformToCustomerResponse(List<PreScreenResponseView> preScreenResponseViews){
        List<PreScreenResponseView> preScreenResponseViewList = new ArrayList<PreScreenResponseView>();
        for(PreScreenResponseView item : preScreenResponseViews){
            PreScreenResponseView preScreenResponseView = new PreScreenResponseView();
            if(item.getPersonalId() != null){
                preScreenResponseView.setRuleName(item.getRuleName());
                preScreenResponseView.setRuleOrder(item.getRuleOrder());
                preScreenResponseView.setType(item.getType());
                preScreenResponseView.setPersonalId(item.getPersonalId());
                preScreenResponseView.setColor(item.getColor());
                preScreenResponseView.setDeviationFlag(item.getDeviationFlag());
                preScreenResponseView.setRejectGroupCode(item.getRejectGroupCode());

                preScreenResponseViewList.add(preScreenResponseView);
            }
        }
        return preScreenResponseViewList;
    }

    public List<PreScreenResponseView> tranformToGroupResponse(List<PreScreenResponseView> preScreenResponseViews){
        List<PreScreenResponseView> preScreenResponseViewList = new ArrayList<PreScreenResponseView>();
        for(PreScreenResponseView item : preScreenResponseViews){
            PreScreenResponseView preScreenResponseView = new PreScreenResponseView();
            if(item.getPersonalId() == null){
                preScreenResponseView.setRuleName(item.getRuleName());
                preScreenResponseView.setRuleOrder(item.getRuleOrder());
                preScreenResponseView.setType(item.getType());
                preScreenResponseView.setPersonalId(item.getPersonalId());
                preScreenResponseView.setColor(item.getColor());
                preScreenResponseView.setDeviationFlag(item.getDeviationFlag());
                preScreenResponseView.setRejectGroupCode(item.getRejectGroupCode());

                preScreenResponseViewList.add(preScreenResponseView);
            }
        }
        return preScreenResponseViewList;
    }
}
