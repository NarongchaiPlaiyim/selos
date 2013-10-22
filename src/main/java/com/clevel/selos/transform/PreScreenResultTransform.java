package com.clevel.selos.transform;

import com.clevel.selos.integration.brms.model.RuleColorResult;
import com.clevel.selos.integration.brms.model.request.PreScreenRequest;
import com.clevel.selos.integration.brms.model.response.PreScreenResponse;
import com.clevel.selos.model.db.master.Step;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.working.BRMSResult;
import com.clevel.selos.model.db.working.WorkCase;
import com.clevel.selos.model.db.working.WorkCasePrescreen;
import com.clevel.selos.model.view.CustomerInfoView;
import com.clevel.selos.model.view.PreScreenResponseView;
import org.joda.time.DateTime;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Date;
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
                preScreenResponseView.setId(item.getId());
                preScreenResponseView.setRuleName(item.getRuleName());
                preScreenResponseView.setRuleOrder(item.getRuleOrder());
                preScreenResponseView.setRuleType(item.getRuleType());
                preScreenResponseView.setPersonalId(item.getPersonalId());
                preScreenResponseView.setRuleColor(item.getRuleColor());
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
                preScreenResponseView.setId(item.getId());
                preScreenResponseView.setRuleName(item.getRuleName());
                preScreenResponseView.setRuleOrder(item.getRuleOrder());
                preScreenResponseView.setRuleType(item.getRuleType());
                preScreenResponseView.setPersonalId(item.getPersonalId());
                preScreenResponseView.setRuleColor(item.getRuleColor());
                preScreenResponseView.setDeviationFlag(item.getDeviationFlag());
                preScreenResponseView.setRejectGroupCode(item.getRejectGroupCode());

                preScreenResponseViewList.add(preScreenResponseView);
            }
        }
        return preScreenResponseViewList;
    }

    public List<BRMSResult> transformResultToModel(List<PreScreenResponseView> preScreenResponseViews, WorkCasePrescreen workCasePrescreen, WorkCase workCase, Step step, User user){
        List<BRMSResult> brmsResultList = new ArrayList<BRMSResult>();
        Date createDate = DateTime.now().toDate();
        Date modifyDate = DateTime.now().toDate();
        for(PreScreenResponseView item : preScreenResponseViews){
            BRMSResult brmsResult = new BRMSResult();
            brmsResult.setId(item.getId());
            brmsResult.setWorkCase(workCase);
            brmsResult.setWorkCasePreScreen(workCasePrescreen);
            brmsResult.setStep(step);
            brmsResult.setRuleName(item.getRuleName());
            brmsResult.setRuleOrder(item.getRuleOrder());
            brmsResult.setRuleType(item.getRuleType());
            brmsResult.setRuleColor(item.getRuleColor().persistValue());
            brmsResult.setPersonalId(item.getPersonalId());
            brmsResult.setDeviationFlag(item.getDeviationFlag());
            brmsResult.setRejectGroupCode(item.getRejectGroupCode());
            if(brmsResult.getId() == 0){
                brmsResult.setCreateDate(createDate);
                brmsResult.setCreateBy(user);
            }
            brmsResult.setModifyDate(modifyDate);
            brmsResult.setModifyBy(user);

            brmsResultList.add(brmsResult);
        }
        return brmsResultList;
    }

    public List<PreScreenResponseView> transformResultToView(List<BRMSResult> brmsResults){
        List<PreScreenResponseView> preScreenResponseViewList = new ArrayList<PreScreenResponseView>();
        for(BRMSResult item : brmsResults){
            PreScreenResponseView preScreenResponseView = new PreScreenResponseView();
            preScreenResponseView.setId(item.getId());
            preScreenResponseView.setRuleName(item.getRuleName());
            preScreenResponseView.setRuleOrder(item.getRuleOrder());
            preScreenResponseView.setRuleType(item.getRuleType());
            preScreenResponseView.setRuleColor(RuleColorResult.valueOf(item.getRuleColor()));
            preScreenResponseView.setPersonalId(item.getPersonalId());
            preScreenResponseView.setDeviationFlag(item.getDeviationFlag());
            preScreenResponseView.setCreateBy(item.getCreateBy());
            preScreenResponseView.setCreateDate(item.getCreateDate());
            preScreenResponseView.setModifyBy(item.getModifyBy());
            preScreenResponseView.setModifyDate(item.getModifyDate());

            preScreenResponseViewList.add(preScreenResponseView);
        }
        return preScreenResponseViewList;
    }


}
