package com.clevel.selos.ws;


import com.clevel.selos.businesscontrol.BRMSControl;
import com.clevel.selos.businesscontrol.UWRuleResultControl;
import com.clevel.selos.dao.master.UWDeviationFlagDAO;
import com.clevel.selos.dao.working.WorkCaseDAO;
import com.clevel.selos.model.ActionResult;
import com.clevel.selos.model.UWResultColor;
import com.clevel.selos.model.db.working.WorkCase;
import com.clevel.selos.model.view.UWRuleResponseView;
import com.clevel.selos.model.view.UWRuleResultDetailView;
import com.clevel.selos.model.view.UWRuleResultSummaryView;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import java.util.Map;

@WebService
public class CheckCriteria {

    @Inject
    @WS
    Logger log;

    @Inject
    private WorkCaseDAO workCaseDAO;
    @Inject
    private BRMSControl brmsControl;
    @Inject
    private UWRuleResultControl uwRuleResultControl;
    @Inject
    private UWDeviationFlagDAO uwDeviationFlagDAO;

    @Inject
    public CheckCriteria() {

    }

    @WebMethod
    public CheckCriteriaResponse caseCriteria(@WebParam(name = "appId") String appId) throws Exception{
    log.debug("caseCriteriaService()");
        CheckCriteriaResponse criteriaResponse = new CheckCriteriaResponse();
        try {
            WorkCase workCase = workCaseDAO.findByAppNumber(appId);
            if(workCase!=null){
                try{
                    UWRuleResponseView uwRuleResponseView = brmsControl.getFullApplicationResult(workCase.getId());
                    log.info("onCheckCriteria uwRulesResponse : {}", uwRuleResponseView);
                    if(uwRuleResponseView != null){
                        if(uwRuleResponseView.getActionResult().equals(ActionResult.SUCCESS)){
                            UWRuleResultSummaryView uwRuleResultSummaryView = uwRuleResponseView.getUwRuleResultSummaryView();
                            uwRuleResultSummaryView.setWorkCaseId(workCase.getId());
                            uwRuleResultControl.saveNewUWRuleResult(uwRuleResultSummaryView);

                            criteriaResponse.setValue(WSBRMSResponse.APPROVE, appId);

                            Map<String, UWRuleResultDetailView> uwRuleResultDetailViewMap = uwRuleResultSummaryView.getUwRuleResultDetailViewMap();
                            for(UWRuleResultDetailView uwRuleResultDetailView : uwRuleResultDetailViewMap.values()){
                                if(uwRuleResultDetailView.getRuleColorResult() == UWResultColor.RED) {
                                    if(uwRuleResultDetailView.getDeviationFlag().getId() == 3){ //Red, with no deviation
                                        criteriaResponse.setValue(WSBRMSResponse.REJECT, appId);
                                        break;
                                    }
                                }
                            }
                        } else {
                            log.error("Error!! Calling BRMS for FullApplication UWRule is not success");
                            throw new Exception("Calling BRMS for FullApplication UWRule is not success");
                        }
                    } else {
                        log.error("Error!! Response is null of calling BRMS FullApplication UWRule");
                        throw new Exception("Error!! Response is null of calling BRMS FullApplication UWRule");
                    }
                } catch (Exception ex){
                    log.error("Exception while caseCriteria : ", ex);
                    throw ex;
                }
            } else {
                log.error("Error!! AppId is not found");
                throw new Exception("AppId is not found in database!!");
            }
        } catch (Exception e) {
            log.error("Error while check criteria : ",e);
            throw e;
        }

        return criteriaResponse;
    }


}
