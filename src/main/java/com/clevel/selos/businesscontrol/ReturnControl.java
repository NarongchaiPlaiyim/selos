package com.clevel.selos.businesscontrol;

import com.clevel.selos.businesscontrol.util.bpm.BPMExecutor;
import com.clevel.selos.dao.history.ReturnInfoHistoryDAO;
import com.clevel.selos.dao.master.*;
import com.clevel.selos.dao.working.*;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.ActionCode;
import com.clevel.selos.model.PricingDOAValue;
import com.clevel.selos.model.RoleValue;
import com.clevel.selos.model.db.history.ReturnInfoHistory;
import com.clevel.selos.model.db.master.*;
import com.clevel.selos.model.db.working.*;
import com.clevel.selos.model.view.ReturnInfoView;
import com.clevel.selos.transform.ReturnInfoTransform;
import com.clevel.selos.transform.StepTransform;
import com.clevel.selos.transform.UserTransform;
import com.clevel.selos.util.Util;
import org.joda.time.DateTime;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.*;

@Stateless
public class ReturnControl extends BusinessControl {
    @Inject
    @SELOS
    Logger log;

    @Inject
    WorkCaseDAO workCaseDAO;
    @Inject
    ReasonDAO reasonDAO;
    @Inject
    ReturnInfoDAO returnInfoDAO;
    @Inject
    ReturnInfoHistoryDAO returnInfoHistoryDAO;
    @Inject
    ReturnInfoTransform returnInfoTransform;
    @Inject
    UserTransform userTransform;
    @Inject
    StepTransform stepTransform;
    @Inject
    StepDAO stepDAO;
    @Inject
    MandateDocDAO mandateDocDAO;

    @Inject
    BPMExecutor bpmExecutor;

    public List<Reason> getReturnReasonList(){
        List<Reason> reasons = reasonDAO.getReasonList();
        if(reasons == null){
            reasons = new ArrayList<Reason>();
        }

        return reasons;
    }

    public List<ReturnInfoView> getReturnInfoViewList(long workCaseId){
        List<ReturnInfoView> returnInfoViews = new ArrayList<ReturnInfoView>();
        if(workCaseId!=0){
            List<ReturnInfo> returnInfoList = returnInfoDAO.findReturnList(workCaseId);
            for(ReturnInfo returnInfo: returnInfoList){
                ReturnInfoView returnInfoView = returnInfoTransform.transformToView(returnInfo);
                returnInfoViews.add(returnInfoView);
            }
        }

        return returnInfoViews;
    }

    public List<ReturnInfoView> getReturnInfoHistoryViewList(long workCaseId, int maxResult){
        List<ReturnInfoView> returnInfoViews = new ArrayList<ReturnInfoView>();
        if(workCaseId!=0){
            List<ReturnInfoHistory> returnInfoList = new ArrayList<ReturnInfoHistory>();
            if(maxResult>0){
                returnInfoList = returnInfoHistoryDAO.findReturnHistoryLimitList(workCaseId,maxResult);
            } else {
                returnInfoList = returnInfoHistoryDAO.findReturnHistoryList(workCaseId);
            }

            for(ReturnInfoHistory returnInfo: returnInfoList){
                ReturnInfoView returnInfoView = returnInfoTransform.transformToView(returnInfo);
                returnInfoViews.add(returnInfoView);
            }
        }

        return returnInfoViews;
    }

    public List<ReturnInfoView> getReturnInfoViewListFromMandateDocAndNoAccept(long workCaseId){
        List<ReturnInfoView> returnInfoViews = new ArrayList<ReturnInfoView>();
        List<MandateDoc> mandateDocList;
        User user = getCurrentUser();
        Map<String, ReturnInfoView> returnInfoViewMap = new HashMap<String, ReturnInfoView>();
        if(workCaseId!=0 && user!=null){
            mandateDocList = mandateDocDAO.findByWorkCaseIdAndRoleForReturn(workCaseId, user.getRole().getId());
            if(mandateDocList!=null && mandateDocList.size()>0){
                for(MandateDoc mandateDoc: mandateDocList){
                    ReturnInfoView returnInfoView = returnInfoTransform.transformToNewView(mandateDoc);
                    returnInfoViewMap.put(returnInfoView.getReturnCode(), returnInfoView);
                }
            }

            List<ReturnInfo> returnInfoList = returnInfoDAO.findByNotAcceptList(workCaseId);
            for(ReturnInfo returnInfo: returnInfoList){
                ReturnInfoView returnInfoView;
                if(returnInfoViewMap.containsKey(returnInfo.getReturnCode())){
                    returnInfoView = returnInfoViewMap.get(returnInfo.getReturnCode());
                    returnInfoViewMap.remove(returnInfo.getReturnCode());
                } else {
                    returnInfoView = returnInfoTransform.transformToNewView(returnInfo);
                }
                returnInfoViews.add(returnInfoView);
            }

            if(returnInfoViewMap!=null && returnInfoViewMap.size()>0){
                Iterator it = returnInfoViewMap.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry pairs = (Map.Entry)it.next();
                    returnInfoViews.add((ReturnInfoView) pairs.getValue());
                    it.remove(); // avoids a ConcurrentModificationException
                }
            }
        }

        return returnInfoViews;
    }

    public List<ReturnInfoView> getReturnReplyInfoViewListForSaveHistory(long workCaseId){
        List<ReturnInfoView> returnInfoViews = new ArrayList<ReturnInfoView>();
        if(workCaseId!=0){
            List<ReturnInfo> returnInfoList = returnInfoDAO.findReturnReplyList(workCaseId);
            for(ReturnInfo returnInfo: returnInfoList){
                ReturnInfoView returnInfoView = returnInfoTransform.transformToView(returnInfo);
                returnInfoViews.add(returnInfoView);
            }
        }

        return returnInfoViews;
    }

    public List<ReturnInfoView> getReturnNoReplyList(long workCaseId){
        List<ReturnInfoView> returnInfoViews = new ArrayList<ReturnInfoView>();
        if(workCaseId!=0){
            List<ReturnInfo> returnInfoList = returnInfoDAO.findByNotReplyList(workCaseId);
            for(ReturnInfo returnInfo: returnInfoList){
                ReturnInfoView returnInfoView = returnInfoTransform.transformToNewView(returnInfo);
                returnInfoViews.add(returnInfoView);
            }
        }

        return returnInfoViews;
    }

    public List<ReturnInfoView> getReturnNoReviewList(long workCaseId){
        List<ReturnInfoView> returnInfoViews = new ArrayList<ReturnInfoView>();
        if(workCaseId!=0){
            List<ReturnInfo> returnInfoList = returnInfoDAO.findByNotReviewList(workCaseId);
            for(ReturnInfo returnInfo: returnInfoList){
                ReturnInfoView returnInfoView = returnInfoTransform.transformToNewView(returnInfo);
                returnInfoViews.add(returnInfoView);
            }
        }

        return returnInfoViews;
    }

    public void saveReturnHistory(long workCaseId, User user) throws Exception{
        List<ReturnInfoView> returnInfoViews = getReturnReplyInfoViewListForSaveHistory(workCaseId);
        if(returnInfoViews!=null && returnInfoViews.size()>0){
            List<ReturnInfoView> returnInfoViewHistoryList = transformReturnInfoToHistoryView(returnInfoViews);
            List<ReturnInfoHistory> returnInfoHistoryList = new ArrayList<ReturnInfoHistory>();
            List<ReturnInfo> returnInfoList = new ArrayList<ReturnInfo>();

            WorkCase workCase = workCaseDAO.findById(workCaseId);
            if(returnInfoViewHistoryList!=null && returnInfoViewHistoryList.size()>0){
                for(ReturnInfoView returnInfoView: returnInfoViewHistoryList){
                    ReturnInfoHistory returnInfoHistory = returnInfoTransform.transformToModelHistory(returnInfoView, workCase);
                    returnInfoHistoryList.add(returnInfoHistory);
                }
                returnInfoHistoryDAO.persist(returnInfoHistoryList);
            }

            for(ReturnInfoView returnInfoView: returnInfoViews){
                ReturnInfo returnInfo = returnInfoTransform.transformToModel(returnInfoView,workCase,user);
                returnInfoList.add(returnInfo);
            }

            returnInfoDAO.delete(returnInfoList);
        }
    }

    public List<ReturnInfoView> transformReturnInfoToHistoryView(List<ReturnInfoView> returnInfoViewList){
        List<ReturnInfoView> returnInfoViews = new ArrayList<ReturnInfoView>();
        if(returnInfoViewList!=null && returnInfoViewList.size()>0){
            for(ReturnInfoView returnInfoView: returnInfoViewList) {
                ReturnInfoView returnInfoViewNew =  returnInfoTransform.transformToNewViewForHistory(returnInfoView);
                returnInfoViews.add(returnInfoViewNew);
            }
        }
        return returnInfoViews;
    }

    public void submitReturnBDM(long workCaseId, String queueName, User user, long stepId, List<ReturnInfoView> returnInfoViewList) throws Exception {
        if(returnInfoViewList!=null && returnInfoViewList.size()>0){
            //Move Return Info in Working to History
            saveReturnHistory(workCaseId,user);

            //Save new to Return Info working
            WorkCase workCase = workCaseDAO.findById(workCaseId);
            Step step = stepDAO.findById(stepId);
            List<ReturnInfo> returnInfoList = new ArrayList<ReturnInfo>();
            Date returnDate = new Date();
            for(ReturnInfoView returnInfoView: returnInfoViewList){
                //Set Return User and Return Step
                returnInfoView.setReturnFromUser(userTransform.transformToView(user));
                returnInfoView.setReturnFromStep(stepTransform.transformToView(step));
                returnInfoView.setDateOfReturn(returnDate);
                returnInfoView.setChallenge(0); //not selected
                returnInfoView.setAcceptChallenge(0); //not selected
                ReturnInfo returnInfo = returnInfoTransform.transformToModel(returnInfoView, workCase, user);
                returnInfoList.add(returnInfo);
            }

            returnInfoDAO.persist(returnInfoList);
            //bpmExecutor.returnBDM(workCaseId, queueName, ActionCode.RETURN_TO_BDM_FULLAPP.getVal());
        }
    }

    public void saveReturnInformation(long workCaseId, String queueName, User user, List<ReturnInfoView> returnInfoViewList) throws Exception {
        if(returnInfoViewList!=null && returnInfoViewList.size()>0){
            WorkCase workCase = workCaseDAO.findById(workCaseId);
            List<ReturnInfo> returnInfoList = new ArrayList<ReturnInfo>();
            for(ReturnInfoView returnInfoView: returnInfoViewList){
                ReturnInfo returnInfo = returnInfoTransform.transformToModel(returnInfoView, workCase, user);
                returnInfoList.add(returnInfo);
            }

            returnInfoDAO.persist(returnInfoList);
        }
    }

    public void submitReturnUW1(long workCaseId, String queueName) throws Exception {
        List<ReturnInfo> returnInfoList = returnInfoDAO.findReturnList(workCaseId);
        if(returnInfoList!=null && returnInfoList.size()>0){
            Date replyDate = new Date();
            for(int i=0; i<returnInfoList.size(); i++){
                returnInfoList.get(i).setDateOfReply(replyDate);
            }

            returnInfoDAO.persist(returnInfoList);
        }

        //bpmExecutor.returnBDM(workCaseId, queueName, ActionCode.RETURN_TO_BDM_FULLAPP.getVal());
    }
}
