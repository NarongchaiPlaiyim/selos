package com.clevel.selos.controller;


import com.clevel.selos.businesscontrol.NCBInfoControl;
import com.clevel.selos.dao.working.CustomerDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.ActionAudit;
import com.clevel.selos.model.ActionResult;
import com.clevel.selos.model.Screen;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.working.Customer;
import com.clevel.selos.model.view.NCBInfoView;
import com.clevel.selos.system.audit.SLOSAuditor;
import com.clevel.selos.system.message.ExceptionMessage;
import com.clevel.selos.system.message.Message;
import com.clevel.selos.system.message.NormalMessage;
import com.clevel.selos.system.message.ValidationMessage;
import com.clevel.selos.transform.NCBTransform;
import com.clevel.selos.util.FacesUtil;
import com.clevel.selos.util.Util;
import org.joda.time.DateTime;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@ViewScoped
@ManagedBean(name = "ncbSummary")
public class NCBSummary extends BaseController {
    @Inject
    @SELOS
    Logger log;
    @Inject
    @NormalMessage
    Message msg;

    @Inject
    @ValidationMessage
    Message validationMsg;

    @Inject
    @ExceptionMessage
    Message exceptionMsg;

    @Inject
    private SLOSAuditor slosAuditor;

    @Inject
    private CustomerDAO customerDAO;
    @Inject
    private NCBTransform ncbTransform;

    @Inject
    private NCBInfoControl ncbInfoControl;

    private long workCaseId;
    private List<NCBInfoView> ncbSumViewList;
    private NCBInfoView ncbView;
    private List<Customer> customerView;
    private NCBInfoView ncbSummaryViewItem;
    //private User user;
    private Date date;
    private String messageHeader;
    private String message;
    private boolean messageErr;

    private String userId;

    public NCBSummary() {}

    public boolean checkSession(HttpSession session){
        boolean checkSession = false;
        if( (Long)session.getAttribute("workCaseId") != 0 && (Long)session.getAttribute("stepId") != 0){
            checkSession = true;
        }

        return checkSession;
    }

    public void preRender(){
        log.debug("preRender");
        HttpSession session = FacesUtil.getSession(false);

        if(checkSession(session)){
            //TODO Check valid stepId
            log.debug("preRender ::: Check valid stepId");
        }else{
            log.debug("preRender ::: No session for case found. Redirect to Inbox");
            FacesUtil.redirect("/site/inbox.jsf");
        }
    }

    @PostConstruct
    public void onCreation() {
        log.info("onCreation");
        Date date = new Date();

        HttpSession session = FacesUtil.getSession(false);

        User user = getCurrentUser();
        if(!Util.isNull(user)) {
            userId = user.getId();
        } else {
            userId = "Null";
        }

        if(checkSession(session)){
            workCaseId = (Long)session.getAttribute("workCaseId");

            date = DateTime.now().toDate();
            ncbSumViewList = ncbInfoControl.getNCBInfoViewByWorkCaseId(workCaseId);
            log.info("onCreation ::: ncbSumViewList : {}", ncbSumViewList);
            if (customerView == null) {
                customerView = new ArrayList<Customer>();
            }

            if (ncbSumViewList == null) {
                ncbSumViewList = new ArrayList<NCBInfoView>();
            }
            String ownerCaseUserId = Util.parseString(session.getAttribute("caseOwner"), "");
            loadFieldControl(workCaseId, Screen.NCB_SUMMARY, ownerCaseUserId);

            slosAuditor.add(Screen.NCB_SUMMARY.value(), userId, ActionAudit.ON_CREATION, "", date, ActionResult.SUCCESS, "");
        } else {
            slosAuditor.add(Screen.NCB_SUMMARY.value(), userId, ActionAudit.ON_CREATION, "", date, ActionResult.FAILED, "Invalid Session");

            log.debug("No session for case found. Redirect to Inbox");
            FacesUtil.redirect("/site/inbox.jsf");
        }
    }

    public void onOpenNCBInfo() {
        log.info("openNCBInfo ::: ");

        if (ncbSummaryViewItem != null) {
            try {
                log.info("openNCBInfo ::: ncbSummaryViewItem :: {}",ncbSummaryViewItem);
                HttpSession session = FacesUtil.getSession(true);

                session.setAttribute("customerId", ncbSummaryViewItem.getCustomerId());    // set customerId to NCB information
                session.setAttribute("workCaseId", workCaseId);

//                slosAuditor.add(Screen.NCB_SUMMARY.value(), userId, ActionAudit.ON_EDIT, "On Edit NCB Detail - Customer ID :: " + ncbSummaryViewItem.getCustomerId(), date, ActionResult.SUCCESS, "");

                FacesUtil.redirect("/site/NCBInfo.jsf");
            } catch (Exception e) {
                log.error("onOpenNCBInfo Exception : {}", Util.getMessageException(e));
//                slosAuditor.add(Screen.NCB_SUMMARY.value(), userId, ActionAudit.ON_EDIT, "On Edit NCB Detail - Customer ID :: " + ncbSummaryViewItem.getCustomerId(), date, ActionResult.FAILED, Util.getMessageException(e));
            }
        } else {
//            slosAuditor.add(Screen.NCB_SUMMARY.value(), userId, ActionAudit.ON_EDIT, "On Edit NCB Detail", date, ActionResult.FAILED, "NCB Summary View Item is Null");
        }
    }

    public NCBInfoView getNcbView() {
        return ncbView;
    }

    public void setNcbView(NCBInfoView ncbView) {
        this.ncbView = ncbView;
    }

    public List<NCBInfoView> getNcbSumViewList() {
        return ncbSumViewList;
    }

    public void setNcbSumViewList(List<NCBInfoView> ncbSumViewList) {
        this.ncbSumViewList = ncbSumViewList;
    }

    public List<Customer> getCustomerView() {
        return customerView;
    }

    public void setCustomerView(List<Customer> customerView) {
        this.customerView = customerView;
    }

    public NCBInfoView getNcbSummaryViewItem() {
        return ncbSummaryViewItem;
    }

    public void setNcbSummaryViewItem(NCBInfoView ncbSummaryViewItem) {
        this.ncbSummaryViewItem = ncbSummaryViewItem;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean isMessageErr() {
        return messageErr;
    }

    public void setMessageErr(boolean messageErr) {
        this.messageErr = messageErr;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessageHeader() {
        return messageHeader;
    }

    public void setMessageHeader(String messageHeader) {
        this.messageHeader = messageHeader;
    }
}
