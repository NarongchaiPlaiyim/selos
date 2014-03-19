package com.clevel.selos.transform;

import com.clevel.selos.dao.history.ReturnInfoHistoryDAO;
import com.clevel.selos.dao.master.StepDAO;
import com.clevel.selos.dao.master.UserDAO;
import com.clevel.selos.dao.working.ReturnInfoDAO;
import com.clevel.selos.model.db.history.ReturnInfoHistory;
import com.clevel.selos.model.db.master.*;
import com.clevel.selos.model.db.working.MandateDoc;
import com.clevel.selos.model.db.working.ReturnInfo;
import com.clevel.selos.model.db.working.WorkCase;
import com.clevel.selos.model.view.ReturnInfoView;

import javax.inject.Inject;
import java.util.Date;

public class ReturnInfoTransform extends Transform {
    @Inject
    private UserTransform userTransform;
    @Inject
    private StepTransform stepTransform;
    @Inject
    private ReturnInfoDAO returnInfoDAO;
    @Inject
    private ReturnInfoHistoryDAO returnInfoHistoryDAO;
    @Inject
    private UserDAO userDAO;
    @Inject
    private StepDAO stepDAO;

    @Inject
    public ReturnInfoTransform() {
    }

    public ReturnInfo transformToModel(ReturnInfoView returnInfoView, WorkCase workCase, User user){
        log.info("Start - transformToModel ::: returnInfoView : {}, workCase : {}, user : {}", returnInfoView,workCase,user);
        ReturnInfo returnInfo = new ReturnInfo();

        if(returnInfoView.getId() != 0){
            returnInfo = returnInfoDAO.findById(returnInfoView.getId());
        } else {
            returnInfo.setCreateDate(new Date());
            returnInfo.setCreateBy(user);
            returnInfo.setWorkCase(workCase);
        }
        returnInfo.setModifyDate(new Date());
        returnInfo.setModifyBy(user);

        returnInfo.setDateOfReturn(returnInfoView.getDateOfReturn());
        if(returnInfoView.getReturnFromUser()!=null && !returnInfoView.getReturnFromUser().getId().equalsIgnoreCase("0")){
            returnInfo.setReturnFromUser(userDAO.findById(returnInfoView.getReturnFromUser().getId()));
        }
        if(returnInfoView.getReturnFromStep()!=null && returnInfoView.getReturnFromStep().getId()!=0){
            returnInfo.setReturnFromStep(stepDAO.findById(returnInfoView.getReturnFromStep().getId()));
        }
        returnInfo.setDateOfReply(returnInfoView.getDateOfReply());
        returnInfo.setReturnCode(returnInfoView.getReturnCode());
        returnInfo.setDescription(returnInfoView.getDescription());
        returnInfo.setReason(returnInfoView.getReason());
        returnInfo.setReasonDetail(returnInfoView.getReasonDetail());
        returnInfo.setChallenge(returnInfoView.getChallenge());
        returnInfo.setAcceptChallenge(returnInfoView.getAcceptChallenge());
        returnInfo.setReplyDetail(returnInfoView.getReplyDetail());
        if(returnInfoView.isCanEdit()){
            returnInfo.setCanEdit(1);
        }
        returnInfo.setReasonId(returnInfoView.getReasonId());


        log.info("End - transformToModel ::: returnInfo : {}", returnInfo);
        return returnInfo;
    }

    public ReturnInfoHistory transformToModelHistory(ReturnInfoView returnInfoView, WorkCase workCase){
        log.info("Start - transformToModelHistory ::: returnInfoView : {}, workCase : {}", returnInfoView,workCase);
        ReturnInfoHistory returnInfo = new ReturnInfoHistory();

        returnInfo.setCreateDate(returnInfoView.getCreateDate());
        returnInfo.setCreateBy(returnInfoView.getCreateBy());
        returnInfo.setWorkCase(workCase);
        returnInfo.setModifyDate(returnInfoView.getModifyDate());
        returnInfo.setModifyBy(returnInfoView.getModifyBy());

        returnInfo.setDateOfReturn(returnInfoView.getDateOfReturn());
        if(returnInfoView.getReturnFromUser()!=null && !returnInfoView.getReturnFromUser().getId().equalsIgnoreCase("0")){
            returnInfo.setReturnFromUser(userDAO.findById(returnInfoView.getReturnFromUser().getId()));
        }
        if(returnInfoView.getReturnFromStep()!=null && returnInfoView.getReturnFromStep().getId()!=0){
            returnInfo.setReturnFromStep(stepDAO.findById(returnInfoView.getReturnFromStep().getId()));
        }
        returnInfo.setDateOfReply(returnInfoView.getDateOfReply());
        returnInfo.setReturnCode(returnInfoView.getReturnCode());
        returnInfo.setDescription(returnInfoView.getDescription());
        returnInfo.setReason(returnInfoView.getReason());
        returnInfo.setReasonDetail(returnInfoView.getReasonDetail());
        returnInfo.setChallenge(returnInfoView.getChallenge());
        returnInfo.setAcceptChallenge(returnInfoView.getAcceptChallenge());
        returnInfo.setReplyDetail(returnInfoView.getReplyDetail());
        if(returnInfoView.isCanEdit()){
            returnInfo.setCanEdit(1);
        }
        returnInfo.setReasonId(returnInfoView.getReasonId());


        log.info("End - transformToModelHistory ::: returnInfo : {}", returnInfo);
        return returnInfo;
    }

    public ReturnInfoView transformToView(ReturnInfo returnInfo){
        log.info("Start - transformToView ::: returnInfo : {}", returnInfo);
        ReturnInfoView returnInfoView = new ReturnInfoView();

        returnInfoView.setId(returnInfo.getId());
        returnInfoView.setDateOfReturn(returnInfo.getDateOfReturn());
        returnInfoView.setReturnFromUser(userTransform.transformToView(returnInfo.getReturnFromUser()));
        returnInfoView.setReturnFromStep(stepTransform.transformToView(returnInfo.getReturnFromStep()));
        returnInfoView.setDateOfReply(returnInfo.getDateOfReply());
        returnInfoView.setReturnCode(returnInfo.getReturnCode());
        returnInfoView.setDescription(returnInfo.getDescription());
        returnInfoView.setReason(returnInfo.getReason());
        returnInfoView.setReasonDetail(returnInfo.getReasonDetail());
        returnInfoView.setChallenge(returnInfo.getChallenge());
        returnInfoView.setAcceptChallenge(returnInfo.getAcceptChallenge());
        returnInfoView.setReplyDetail(returnInfo.getReplyDetail());
        if(returnInfo.getCanEdit()==1){
            returnInfoView.setCanEdit(true);
        }
        returnInfoView.setReasonId(returnInfo.getReasonId());
        returnInfoView.setCreateBy(returnInfo.getCreateBy());
        returnInfoView.setCreateDate(returnInfo.getCreateDate());
        returnInfoView.setModifyBy(returnInfo.getModifyBy());
        returnInfoView.setModifyDate(returnInfo.getModifyDate());

        log.info("End - transformToView ::: returnInfoView : {}", returnInfoView);
        return returnInfoView;
    }

    public ReturnInfoView transformToNewView(ReturnInfo returnInfo){
        log.info("Start - transformToView ::: returnInfo : {}", returnInfo);
        ReturnInfoView returnInfoView = new ReturnInfoView();

        returnInfoView.setReturnCode(returnInfo.getReturnCode());
        returnInfoView.setDescription(returnInfo.getDescription());
        returnInfoView.setReason(returnInfo.getReason());
        returnInfoView.setReasonDetail(returnInfo.getReasonDetail());
        if(returnInfo.getCanEdit()==1){
            returnInfoView.setCanEdit(true);
        }
        returnInfoView.setReasonId(returnInfo.getReasonId());

        log.info("End - transformToView ::: returnInfoView : {}", returnInfoView);
        return returnInfoView;
    }

    public ReturnInfoView transformToView(ReturnInfoHistory returnInfo){
        log.info("Start - transformToView ::: returnInfo : {}", returnInfo);
        ReturnInfoView returnInfoView = new ReturnInfoView();

        returnInfoView.setId(returnInfo.getId());
        returnInfoView.setDateOfReturn(returnInfo.getDateOfReturn());
        returnInfoView.setReturnFromUser(userTransform.transformToView(returnInfo.getReturnFromUser()));
        returnInfoView.setReturnFromStep(stepTransform.transformToView(returnInfo.getReturnFromStep()));
        returnInfoView.setDateOfReply(returnInfo.getDateOfReply());
        returnInfoView.setReturnCode(returnInfo.getReturnCode());
        returnInfoView.setDescription(returnInfo.getDescription());
        returnInfoView.setReason(returnInfo.getReason());
        returnInfoView.setReasonDetail(returnInfo.getReasonDetail());
        returnInfoView.setChallenge(returnInfo.getChallenge());
        returnInfoView.setAcceptChallenge(returnInfo.getAcceptChallenge());
        returnInfoView.setReplyDetail(returnInfo.getReplyDetail());
        if(returnInfo.getCanEdit()==1){
            returnInfoView.setCanEdit(true);
        }
        returnInfoView.setReasonId(returnInfo.getReasonId());
        returnInfoView.setCreateBy(returnInfo.getCreateBy());
        returnInfoView.setCreateDate(returnInfo.getCreateDate());
        returnInfoView.setModifyBy(returnInfo.getModifyBy());
        returnInfoView.setModifyDate(returnInfo.getModifyDate());

        log.info("End - transformToView ::: returnInfoView : {}", returnInfoView);
        return returnInfoView;
    }

    public ReturnInfoView transformToNewViewForHistory(ReturnInfoView returnInfo){
        log.info("Start - transformToView ::: returnInfo : {}", returnInfo);
        ReturnInfoView returnInfoView = new ReturnInfoView();
        returnInfoView.setDateOfReturn(returnInfo.getDateOfReturn());
        returnInfoView.setReturnFromUser(returnInfo.getReturnFromUser());
        returnInfoView.setReturnFromStep(returnInfo.getReturnFromStep());
        returnInfoView.setDateOfReply(returnInfo.getDateOfReply());
        returnInfoView.setReturnCode(returnInfo.getReturnCode());
        returnInfoView.setDescription(returnInfo.getDescription());
        returnInfoView.setReason(returnInfo.getReason());
        returnInfoView.setReasonDetail(returnInfo.getReasonDetail());
        returnInfoView.setChallenge(returnInfo.getChallenge());
        returnInfoView.setAcceptChallenge(returnInfo.getAcceptChallenge());
        returnInfoView.setReplyDetail(returnInfo.getReplyDetail());
        returnInfoView.setCanEdit(returnInfo.isCanEdit());
        returnInfoView.setReasonId(returnInfo.getReasonId());
        returnInfoView.setCreateBy(returnInfo.getCreateBy());
        returnInfoView.setCreateDate(returnInfo.getCreateDate());
        returnInfoView.setModifyBy(returnInfo.getModifyBy());
        returnInfoView.setModifyDate(returnInfo.getModifyDate());

        log.info("End - transformToView ::: returnInfoView : {}", returnInfoView);
        return returnInfoView;
    }

    public ReturnInfoView transformToNewView(MandateDoc mandateDoc){
        log.info("Start - transformToView ::: mandateDoc : {}", mandateDoc);
        ReturnInfoView returnInfoView = new ReturnInfoView();

        returnInfoView.setReturnCode(mandateDoc.getEcmDocType());
        returnInfoView.setDescription(mandateDoc.getEcmDocTypeDesc());
        //returnInfoView.setReason(mandateDoc.getReason());
        returnInfoView.setReasonDetail(mandateDoc.getRemark());
        returnInfoView.setCanEdit(false);

        log.info("End - transformToView ::: returnInfoView : {}", returnInfoView);
        return returnInfoView;
    }

}
