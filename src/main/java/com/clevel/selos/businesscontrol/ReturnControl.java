package com.clevel.selos.businesscontrol;

import com.clevel.selos.businesscontrol.util.bpm.BPMExecutor;
import com.clevel.selos.dao.history.ReturnInfoHistoryDAO;
import com.clevel.selos.dao.master.ReasonDAO;
import com.clevel.selos.dao.master.StepDAO;
import com.clevel.selos.dao.working.MandateDocDAO;
import com.clevel.selos.dao.working.ReturnInfoDAO;
import com.clevel.selos.dao.working.WorkCaseDAO;
import com.clevel.selos.dao.working.WorkCasePrescreenDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.ActionCode;
import com.clevel.selos.model.db.history.ReturnInfoHistory;
import com.clevel.selos.model.db.master.Reason;
import com.clevel.selos.model.db.master.Step;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.working.MandateDoc;
import com.clevel.selos.model.db.working.ReturnInfo;
import com.clevel.selos.model.db.working.WorkCase;
import com.clevel.selos.model.db.working.WorkCasePrescreen;
import com.clevel.selos.model.view.ReturnInfoView;
import com.clevel.selos.transform.ReturnInfoTransform;
import com.clevel.selos.transform.StepTransform;
import com.clevel.selos.transform.UserTransform;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.*;

@Stateless
public class ReturnControl extends BusinessControl {
    @Inject
    @SELOS
    Logger log;

    @Inject
    WorkCaseDAO workCaseDAO;
    @Inject
    WorkCasePrescreenDAO workCasePrescreenDAO;
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

    public List<ReturnInfoView> getReturnInfoViewList(long workCaseId, long workCasePrescreenId){
        log.debug("getReturnInfoViewList (workCaseId: {}, workCasePrescreenId: {})",workCaseId,workCasePrescreenId);
        List<ReturnInfoView> returnInfoViews = new ArrayList<ReturnInfoView>();
        if(workCaseId!=0 || workCasePrescreenId!=0){
            List<ReturnInfo> returnInfoList;
            if(workCaseId!=0){
                returnInfoList = returnInfoDAO.findReturnList(workCaseId);
            } else {
                returnInfoList = returnInfoDAO.findReturnListPrescreen(workCasePrescreenId);
            }

            for(ReturnInfo returnInfo: returnInfoList){
                ReturnInfoView returnInfoView = returnInfoTransform.transformToView(returnInfo);
                returnInfoViews.add(returnInfoView);
            }
        }

        return returnInfoViews;
    }

    public List<ReturnInfoView> getReturnInfoHistoryViewList(long workCaseId, long workCasePrescreenId, int maxResult){
        List<ReturnInfoView> returnInfoViews = new ArrayList<ReturnInfoView>();
        if(workCaseId!=0 || workCasePrescreenId!=0){
            List<ReturnInfoHistory> returnInfoList;
            if(maxResult>0){
                if(workCaseId!=0){
                    returnInfoList = returnInfoHistoryDAO.findReturnHistoryLimitList(workCaseId,maxResult);
                } else {
                    returnInfoList = returnInfoHistoryDAO.findReturnHistoryLimitListPrescreen(workCasePrescreenId, maxResult);
                }
            } else {
                if(workCaseId!=0){
                    returnInfoList = returnInfoHistoryDAO.findReturnHistoryList(workCaseId);
                } else {
                    returnInfoList = returnInfoHistoryDAO.findReturnHistoryListPrescreen(workCasePrescreenId);
                }
            }

            for(ReturnInfoHistory returnInfo: returnInfoList){
                ReturnInfoView returnInfoView = returnInfoTransform.transformToView(returnInfo);
                returnInfoViews.add(returnInfoView);
            }
        }

        return returnInfoViews;
    }

    public List<ReturnInfoView> getReturnInfoViewListFromMandateDocAndNoAccept(long workCaseId, long workCasePrescreenId){
        List<ReturnInfoView> returnInfoViews = new ArrayList<ReturnInfoView>();
        List<MandateDoc> mandateDocList;
        User user = getCurrentUser();
        Map<String, ReturnInfoView> returnInfoViewMap = new HashMap<String, ReturnInfoView>();
        if((workCaseId!=0 || workCasePrescreenId!=0) && user!=null){
            List<ReturnInfo> returnInfoList = new ArrayList<ReturnInfo>();
            if(workCaseId!=0) {
                mandateDocList = mandateDocDAO.findByWorkCaseIdAndRoleForReturn(workCaseId, user.getRole().getId());
                returnInfoList = returnInfoDAO.findByNotAcceptList(workCaseId);
            } else {
                mandateDocList = mandateDocDAO.findByWorkCasePrescreenIdAndRoleForReturn(workCasePrescreenId, user.getRole().getId());
                returnInfoList = returnInfoDAO.findByNotAcceptListPreScreen(workCasePrescreenId);
            }

            if(mandateDocList!=null && mandateDocList.size()>0){
                for(MandateDoc mandateDoc: mandateDocList){
                    ReturnInfoView returnInfoView = returnInfoTransform.transformToNewView(mandateDoc);
                    returnInfoViewMap.put(returnInfoView.getReturnCode(), returnInfoView);
                }
            }

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
                SortedSet<String> keys = new TreeSet<String>(returnInfoViewMap.keySet());
                for (String key : keys) {
                    returnInfoViews.add(returnInfoViewMap.get(key));
                }
            }
        }

        return returnInfoViews;
    }

    public List<ReturnInfoView> getReturnReplyInfoViewListForSaveHistory(long workCaseId, long workCasePrescreenId){
        log.debug("getReturnReplyInfoViewListForSaveHistory (workCaseId: {}, workCasePrescreenId: {})",workCaseId,workCasePrescreenId);
        List<ReturnInfoView> returnInfoViews = new ArrayList<ReturnInfoView>();
        if(workCaseId!=0 || workCasePrescreenId!=0){
            List<ReturnInfo> returnInfoList;
            if(workCaseId!=0){
                returnInfoList = returnInfoDAO.findReturnReplyList(workCaseId);
            } else {
                returnInfoList = returnInfoDAO.findReturnReplyListPrescreen(workCasePrescreenId);
            }

            for(ReturnInfo returnInfo: returnInfoList){
                ReturnInfoView returnInfoView = returnInfoTransform.transformToView(returnInfo);
                returnInfoViews.add(returnInfoView);
            }
        }

        log.debug("getReturnReplyInfoViewListForSaveHistory (returnInfoViews size: {})",returnInfoViews.size());
        return returnInfoViews;
    }

    public List<ReturnInfoView> getReturnReplyInfoViewListForSaveHistoryForRestart(long workCaseId, long workCasePrescreenId){
        log.debug("getReturnReplyInfoViewListForSaveHistory (workCaseId: {}, workCasePrescreenId: {})",workCaseId,workCasePrescreenId);
        List<ReturnInfoView> returnInfoViews = new ArrayList<ReturnInfoView>();
        if(workCaseId!=0 || workCasePrescreenId!=0){
            List<ReturnInfo> returnInfoList;
            if(workCaseId!=0){
                returnInfoList = returnInfoDAO.findReturnList(workCaseId);
            } else {
                returnInfoList = returnInfoDAO.findReturnListPrescreen(workCasePrescreenId);
            }

            for(ReturnInfo returnInfo: returnInfoList){
                ReturnInfoView returnInfoView = returnInfoTransform.transformToView(returnInfo);
                returnInfoViews.add(returnInfoView);
            }
        }

        log.debug("getReturnReplyInfoViewListForSaveHistory (returnInfoViews size: {})",returnInfoViews.size());
        return returnInfoViews;
    }

    public List<ReturnInfoView> getReturnNoReplyList(long workCaseId, long workCasePrescreenId){
        List<ReturnInfoView> returnInfoViews = new ArrayList<ReturnInfoView>();
        if(workCaseId!=0 || workCasePrescreenId!=0){
            List<ReturnInfo> returnInfoList;
            if(workCaseId!=0){
                returnInfoList = returnInfoDAO.findByNotReplyList(workCaseId);
            } else {
                returnInfoList = returnInfoDAO.findByNotReplyListPrescreen(workCasePrescreenId);
            }

            for(ReturnInfo returnInfo: returnInfoList){
                ReturnInfoView returnInfoView = returnInfoTransform.transformToNewView(returnInfo);
                returnInfoViews.add(returnInfoView);
            }
        }

        return returnInfoViews;
    }

    public List<ReturnInfoView> getReturnNoReviewList(long workCaseId, long workCasePrescreenId){
        List<ReturnInfoView> returnInfoViews = new ArrayList<ReturnInfoView>();
        if(workCaseId!=0 || workCasePrescreenId!=0){
            List<ReturnInfo> returnInfoList;
            if(workCaseId!=0){
                returnInfoList = returnInfoDAO.findByNotReviewList(workCaseId);
            } else {
                returnInfoList = returnInfoDAO.findByNotReviewListPrescreen(workCasePrescreenId);
            }

            for(ReturnInfo returnInfo: returnInfoList){
                ReturnInfoView returnInfoView = returnInfoTransform.transformToNewView(returnInfo);
                returnInfoViews.add(returnInfoView);
            }
        }

        return returnInfoViews;
    }

    public void saveReturnHistory(long workCaseId, long workCasePrescreenId) throws Exception{
        log.debug("saveReturnHistory (workCaseId: {}, workCasePrescreenId: {})",workCaseId,workCasePrescreenId);
        User user = getCurrentUser();
        List<ReturnInfoView> returnInfoViews = getReturnReplyInfoViewListForSaveHistory(workCaseId,workCasePrescreenId);
        if(returnInfoViews!=null && returnInfoViews.size()>0){
            List<ReturnInfoView> returnInfoViewHistoryList = transformReturnInfoToHistoryView(returnInfoViews);
            List<ReturnInfoHistory> returnInfoHistoryList = new ArrayList<ReturnInfoHistory>();
            List<ReturnInfo> returnInfoList = new ArrayList<ReturnInfo>();

            if(workCaseId!=0){
                WorkCase workCase = workCaseDAO.findById(workCaseId);
                if(returnInfoViewHistoryList!=null && returnInfoViewHistoryList.size()>0){
                    for(ReturnInfoView returnInfoView: returnInfoViewHistoryList){
                        ReturnInfoHistory returnInfoHistory = returnInfoTransform.transformToModelHistory(returnInfoView, workCase, null);
                        returnInfoHistoryList.add(returnInfoHistory);
                    }
                    returnInfoHistoryDAO.persist(returnInfoHistoryList);
                }

                for(ReturnInfoView returnInfoView: returnInfoViews){
                    ReturnInfo returnInfo = returnInfoTransform.transformToModel(returnInfoView,workCase,null,user);
                    returnInfoList.add(returnInfo);
                }

                returnInfoDAO.delete(returnInfoList);
            } else {
                WorkCasePrescreen workCasePrescreen = workCasePrescreenDAO.findById(workCasePrescreenId);
                if(returnInfoViewHistoryList!=null && returnInfoViewHistoryList.size()>0){
                    for(ReturnInfoView returnInfoView: returnInfoViewHistoryList){
                        ReturnInfoHistory returnInfoHistory = returnInfoTransform.transformToModelHistory(returnInfoView, null, workCasePrescreen);
                        returnInfoHistoryList.add(returnInfoHistory);
                    }
                    returnInfoHistoryDAO.persist(returnInfoHistoryList);
                }

                for(ReturnInfoView returnInfoView: returnInfoViews){
                    ReturnInfo returnInfo = returnInfoTransform.transformToModel(returnInfoView,null,workCasePrescreen,user);
                    returnInfoList.add(returnInfo);
                }

                returnInfoDAO.delete(returnInfoList);
            }
        }
    }

    public void saveReturnHistoryForRestart(long workCaseId, long workCasePrescreenId) throws Exception{
        log.debug("saveReturnHistory (workCaseId: {}, workCasePrescreenId: {})",workCaseId,workCasePrescreenId);
        User user = getCurrentUser();
        List<ReturnInfoView> returnInfoViews = getReturnReplyInfoViewListForSaveHistoryForRestart(workCaseId,workCasePrescreenId);
        if(returnInfoViews!=null && returnInfoViews.size()>0){
            List<ReturnInfoView> returnInfoViewHistoryList = transformReturnInfoToHistoryView(returnInfoViews);
            List<ReturnInfoHistory> returnInfoHistoryList = new ArrayList<ReturnInfoHistory>();
            List<ReturnInfo> returnInfoList = new ArrayList<ReturnInfo>();

            if(workCaseId!=0){
                WorkCase workCase = workCaseDAO.findById(workCaseId);
                if(returnInfoViewHistoryList!=null && returnInfoViewHistoryList.size()>0){
                    for(ReturnInfoView returnInfoView: returnInfoViewHistoryList){
                        ReturnInfoHistory returnInfoHistory = returnInfoTransform.transformToModelHistory(returnInfoView, workCase, null);
                        returnInfoHistoryList.add(returnInfoHistory);
                    }
                    returnInfoHistoryDAO.persist(returnInfoHistoryList);
                }

                for(ReturnInfoView returnInfoView: returnInfoViews){
                    ReturnInfo returnInfo = returnInfoTransform.transformToModel(returnInfoView,workCase,null,user);
                    returnInfoList.add(returnInfo);
                }

                returnInfoDAO.delete(returnInfoList);
            } else {
                WorkCasePrescreen workCasePrescreen = workCasePrescreenDAO.findById(workCasePrescreenId);
                if(returnInfoViewHistoryList!=null && returnInfoViewHistoryList.size()>0){
                    for(ReturnInfoView returnInfoView: returnInfoViewHistoryList){
                        ReturnInfoHistory returnInfoHistory = returnInfoTransform.transformToModelHistory(returnInfoView, null, workCasePrescreen);
                        returnInfoHistoryList.add(returnInfoHistory);
                    }
                    returnInfoHistoryDAO.persist(returnInfoHistoryList);
                }

                for(ReturnInfoView returnInfoView: returnInfoViews){
                    ReturnInfo returnInfo = returnInfoTransform.transformToModel(returnInfoView,null,workCasePrescreen,user);
                    returnInfoList.add(returnInfo);
                }

                returnInfoDAO.delete(returnInfoList);
            }
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

    public void submitReturnBDM(long workCaseId, long workCasePrescreenId, String queueName, User user, long stepId, List<ReturnInfoView> returnInfoViewList, String wobNumber) throws Exception {
        if(returnInfoViewList!=null && returnInfoViewList.size()>0){
            boolean hasRG001 = false;

            //Move Return Info in Working to History
            saveReturnHistory(workCaseId,workCasePrescreenId);

            //Save new to Return Info working
            WorkCase workCase = null;
            WorkCasePrescreen workCasePrescreen = null;
            if(workCaseId!=0){
                workCase = workCaseDAO.findById(workCaseId);
            } else {
                workCasePrescreen = workCasePrescreenDAO.findById(workCasePrescreenId);
            }
            Step step = stepDAO.findById(stepId);
            List<ReturnInfo> returnInfoList = new ArrayList<ReturnInfo>();
            Date returnDate = new Date();
            String reason = "";
            String remark = "";
            for(ReturnInfoView returnInfoView: returnInfoViewList){
                //Set Return User and Return Step
                returnInfoView.setReturnFromUser(userTransform.transformToView(user));
                returnInfoView.setReturnFromStep(stepTransform.transformToView(step));
                returnInfoView.setDateOfReturn(returnDate);
                returnInfoView.setChallenge(0); //not selected
                returnInfoView.setAcceptChallenge(0); //not selected
                if(returnInfoView.getReturnCode()!=null && returnInfoView.getReturnCode().equalsIgnoreCase("RG001")){
                    hasRG001 = true;
                }
                ReturnInfo returnInfo = returnInfoTransform.transformToModel(returnInfoView, workCase, workCasePrescreen, user);
                returnInfoList.add(returnInfo);

                if(reason.trim().equalsIgnoreCase("")){
                    reason = returnInfoView.getReason();
                    if(reason==null){
                        reason = "";
                    }
                } else {
                    if(returnInfoView.getReason()!=null){
                        reason = reason+","+returnInfoView.getReason();
                    }
                }

                if(remark.trim().equalsIgnoreCase("")){
                    remark = returnInfoView.getReasonDetail();
                    if(remark==null){
                        remark = "";
                    }
                } else {
                    if(returnInfoView.getReasonDetail()!=null){
                        remark = remark+","+returnInfoView.getReasonDetail();
                    }
                }

            }

            try{
                log.debug("persist returnInfoList.");
                returnInfoDAO.persist(returnInfoList);
                log.debug("persist returnInfoList success");
            } catch (Exception e) {
                log.debug("persist returnInfoList error");
                throw e;
            }

            log.debug("execute bpm (dispatch)");
            if((step!=null && step.getId()==2003) || (step!=null && step.getId()==2016)){
                bpmExecutor.returnBDM(workCaseId, queueName, ActionCode.RETURN_TO_BDM.getVal(),hasRG001);
            } else {
                bpmExecutor.returnCase(queueName,wobNumber,remark,reason,ActionCode.RETURN_TO_BDM.getVal());
            }
        }
    }

    public void submitReturnAADAdmin(long workCaseId, long workCasePrescreenId, String queueName, User user, long stepId, List<ReturnInfoView> returnInfoViewList, String wobNumber) throws Exception {
        if(returnInfoViewList!=null && returnInfoViewList.size()>0){
            boolean hasRG001 = false;

            //Move Return Info in Working to History
            saveReturnHistory(workCaseId,workCasePrescreenId);

            //Save new to Return Info working
            WorkCase workCase = null;
            WorkCasePrescreen workCasePrescreen = null;
            if(workCaseId!=0){
                workCase = workCaseDAO.findById(workCaseId);
            } else {
                workCasePrescreen = workCasePrescreenDAO.findById(workCasePrescreenId);
            }
            Step step = stepDAO.findById(stepId);
            List<ReturnInfo> returnInfoList = new ArrayList<ReturnInfo>();
            Date returnDate = new Date();
            String reason = "";
            String remark = "";
            for(ReturnInfoView returnInfoView: returnInfoViewList){
                //Set Return User and Return Step
                returnInfoView.setReturnFromUser(userTransform.transformToView(user));
                returnInfoView.setReturnFromStep(stepTransform.transformToView(step));
                returnInfoView.setDateOfReturn(returnDate);
                returnInfoView.setChallenge(0); //not selected
                returnInfoView.setAcceptChallenge(0); //not selected
                if(returnInfoView.getReturnCode()!=null && returnInfoView.getReturnCode().equalsIgnoreCase("RG001")){
                    hasRG001 = true;
                }
                ReturnInfo returnInfo = returnInfoTransform.transformToModel(returnInfoView, workCase, workCasePrescreen, user);
                returnInfoList.add(returnInfo);

                if(reason.trim().equalsIgnoreCase("")){
                    reason = returnInfoView.getReason();
                    if(reason==null){
                        reason = "";
                    }
                } else {
                    if(returnInfoView.getReason()!=null){
                        reason = reason+","+returnInfoView.getReason();
                    }
                }

                if(remark.trim().equalsIgnoreCase("")){
                    remark = returnInfoView.getReasonDetail();
                    if(remark==null){
                        remark = "";
                    }
                } else {
                    if(returnInfoView.getReasonDetail()!=null){
                        remark = remark+","+returnInfoView.getReasonDetail();
                    }
                }

            }

            try{
                log.debug("persist returnInfoList.");
                returnInfoDAO.persist(returnInfoList);
                log.debug("persist returnInfoList success");
            } catch (Exception e) {
                log.debug("persist returnInfoList error");
                throw e;
            }

            log.debug("execute bpm (dispatch)");
            bpmExecutor.returnCase(queueName,wobNumber,remark,reason,ActionCode.RETURN_TO_AAD_ADMIN.getVal());
        }
    }

    public void submitReturnBU(long workCaseId, long workCasePrescreenId, String queueName, User user, long stepId, List<ReturnInfoView> returnInfoViewList, String wobNumber) throws Exception {
        if(returnInfoViewList!=null && returnInfoViewList.size()>0){
            boolean hasRG001 = false;

            //Move Return Info in Working to History
            saveReturnHistory(workCaseId,workCasePrescreenId);

            //Save new to Return Info working
            WorkCase workCase = null;
            WorkCasePrescreen workCasePrescreen = null;
            if(workCaseId!=0){
                workCase = workCaseDAO.findById(workCaseId);
            } else {
                workCasePrescreen = workCasePrescreenDAO.findById(workCasePrescreenId);
            }
            Step step = stepDAO.findById(stepId);
            List<ReturnInfo> returnInfoList = new ArrayList<ReturnInfo>();
            Date returnDate = new Date();
            String reason = "";
            String remark = "";
            for(ReturnInfoView returnInfoView: returnInfoViewList){
                //Set Return User and Return Step
                returnInfoView.setReturnFromUser(userTransform.transformToView(user));
                returnInfoView.setReturnFromStep(stepTransform.transformToView(step));
                returnInfoView.setDateOfReturn(returnDate);
                returnInfoView.setChallenge(0); //not selected
                returnInfoView.setAcceptChallenge(0); //not selected
                if(returnInfoView.getReturnCode()!=null && returnInfoView.getReturnCode().equalsIgnoreCase("RG001")){
                    hasRG001 = true;
                }
                ReturnInfo returnInfo = returnInfoTransform.transformToModel(returnInfoView, workCase, workCasePrescreen, user);
                returnInfoList.add(returnInfo);

                if(reason.trim().equalsIgnoreCase("")){
                    reason = returnInfoView.getReason();
                    if(reason==null){
                        reason = "";
                    }
                } else {
                    if(returnInfoView.getReason()!=null){
                        reason = reason+","+returnInfoView.getReason();
                    }
                }

                if(remark.trim().equalsIgnoreCase("")){
                    remark = returnInfoView.getReasonDetail();
                    if(remark==null){
                        remark = "";
                    }
                } else {
                    if(returnInfoView.getReasonDetail()!=null){
                        remark = remark+","+returnInfoView.getReasonDetail();
                    }
                }

            }

            try{
                log.debug("persist returnInfoList.");
                returnInfoDAO.persist(returnInfoList);
                log.debug("persist returnInfoList success");
            } catch (Exception e) {
                log.debug("persist returnInfoList error");
                throw e;
            }

            log.debug("execute bpm (dispatch)");
            bpmExecutor.returnCase(queueName,wobNumber,remark,reason,ActionCode.REVISE_CA.getVal());
        }
    }

    public void saveReturnInformation(long workCaseId, long workCasePrescreenId, String queueName, User user, List<ReturnInfoView> returnInfoViewList) throws Exception {
        if(returnInfoViewList!=null && returnInfoViewList.size()>0){
            WorkCase workCase = null;
            WorkCasePrescreen workCasePrescreen = null;
            if(workCaseId!=0){
                workCase = workCaseDAO.findById(workCaseId);
            } else {
                workCasePrescreen = workCasePrescreenDAO.findById(workCasePrescreenId);
            }
            List<ReturnInfo> returnInfoList = new ArrayList<ReturnInfo>();
            for(ReturnInfoView returnInfoView: returnInfoViewList){
                ReturnInfo returnInfo = returnInfoTransform.transformToModel(returnInfoView, workCase, workCasePrescreen, user);
                returnInfoList.add(returnInfo);
            }

            returnInfoDAO.persist(returnInfoList);
        }
    }

    public void submitReply(long workCaseId, long workCasePrescreenId, String queueName) throws Exception {
        List<ReturnInfo> returnInfoList;
        if(workCaseId!=0){
            returnInfoList = returnInfoDAO.findReturnList(workCaseId);
        } else {
            returnInfoList = returnInfoDAO.findReturnListPrescreen(workCasePrescreenId);
        }

        if(returnInfoList!=null && returnInfoList.size()>0){
            Date replyDate = new Date();
            for(int i=0; i<returnInfoList.size(); i++){
                returnInfoList.get(i).setDateOfReply(replyDate);
            }

            returnInfoDAO.persist(returnInfoList);
        }

        String wobNumber = "";
        WorkCase workCase = null;
        WorkCasePrescreen workCasePrescreen = null;
        if(workCaseId!=0){
            workCase = workCaseDAO.findById(workCaseId);
            wobNumber = workCase.getWobNumber();
        } else {
            workCasePrescreen = workCasePrescreenDAO.findById(workCasePrescreenId);
            wobNumber = workCasePrescreen.getWobNumber();
        }

        bpmExecutor.submitCase(queueName, wobNumber, ActionCode.SUBMIT_CA.getVal());
    }
}
