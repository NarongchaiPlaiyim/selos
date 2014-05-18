package com.clevel.selos.controller;


import com.clevel.selos.businesscontrol.BasicInfoControl;
import com.clevel.selos.businesscontrol.CustomerAcceptanceControl;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.ApproveResult;
import com.clevel.selos.model.ApproveType;
import com.clevel.selos.model.db.master.Reason;
import com.clevel.selos.model.db.master.Status;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.view.BasicInfoView;
import com.clevel.selos.model.view.ContactRecordDetailView;
import com.clevel.selos.model.view.CustomerAcceptanceView;
import com.clevel.selos.model.view.TCGInfoView;
import com.clevel.selos.util.FacesUtil;
import com.clevel.selos.util.Util;
import org.primefaces.context.RequestContext;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@ViewScoped
@ManagedBean(name = "customerAcceptancePre")
public class CustomerAcceptancePre extends BaseController {

    @SELOS
    @Inject
    private Logger log;

    @Inject
    private CustomerAcceptanceControl customerAcceptanceControl;

    @Inject
    private BasicInfoControl basicInfoControl;

    //Private variable
    private boolean preRenderCheck = false;
    private long workCaseId = -1;
    private long stepId = -1;
    private long stageId = -1;
    private User user;
    private Status workCaseStatus;
    private List<ContactRecordDetailView> deleteList;
    private BasicInfoView basicInfoView;

    //Property
    private TCGInfoView tcgInfoView;
    private CustomerAcceptanceView customerAcceptanceView;
    private List<ContactRecordDetailView> contactRecordDetailViews;
    private ContactRecordDetailView contactRecord;
    private int deletedRowId;
    private List<Reason> reasons;
    private boolean addDialog;

    public CustomerAcceptancePre() {
    }

    /*
     * Action
     */
    @PostConstruct
    private void init() {
        log.info("Construct");
        HttpSession session = FacesUtil.getSession(false);
        if (session != null) {
            workCaseId = Util.parseLong(session.getAttribute("workCaseId"), -1);
            stepId = Util.parseLong(session.getAttribute("stepId"), -1);
            stageId = Util.parseLong(session.getAttribute("stageId"), -1);
            user = (User) session.getAttribute("user");
        }
        _loadInitData();
    }

    public void preRender() {
        log.info("preRender workCase Id = " + workCaseId);
        HttpSession session = FacesUtil.getSession(true);
        if(checkSession(session)){
            //Check valid step
            stepId = getCurrentStep(session);
            if(stepId != 2011 && stepId != 2012){
                FacesUtil.redirect("/selos/inbox.jsf");
            }
        }
    }

    public void onOpenAddContactRecordDialog() {
        contactRecord = new ContactRecordDetailView();
        contactRecord.setId(0);
        contactRecord.setCallingDate(new Date());
        contactRecord.setCreateBy(user);
        contactRecord.setStatus(workCaseStatus);
        if (reasons == null) {
            reasons = customerAcceptanceControl.getContactRecordReasons();
        }
        addDialog = true;
    }

    public void onOpenUpdateContactRecordDialog() {
        if (reasons == null) {
            reasons = customerAcceptanceControl.getContactRecordReasons();
        }
        addDialog = false;
    }

    public void onAddContactRecord() {
        Reason reason = _retrieveReasonFromId(contactRecord.getUpdReasonId());
        contactRecord.setReason(reason);
        contactRecordDetailViews.add(contactRecord);
        contactRecord = null;

        RequestContext.getCurrentInstance().addCallbackParam("functionComplete", true);
    }

    public void onUpdateContactRecord() {
        Reason reason = _retrieveReasonFromId(contactRecord.getUpdReasonId());
        contactRecord.setReason(reason);
        contactRecord.setNeedUpdate(true);
        contactRecord = null;

        RequestContext.getCurrentInstance().addCallbackParam("functionComplete", true);
    }

    public void onDeleteContactRecord() {
        if (deletedRowId < 0 || deletedRowId > contactRecordDetailViews.size())
            return;
        ContactRecordDetailView delete = contactRecordDetailViews.remove(deletedRowId);
        if (delete != null && delete.getId() > 0) {
            deleteList.add(delete);
        }
        deletedRowId = -1;

        RequestContext.getCurrentInstance().addCallbackParam("functionComplete", true);
    }

    public boolean isContactUpdatable(ContactRecordDetailView detail) {
        if (detail == null)
            return false;
        return Util.equals(user.getId(), detail.getCreateBy().getId()) && workCaseStatus.getId() == detail.getStatus().getId();
    }

    public void onSaveCustomerAcceptance() {
        customerAcceptanceControl.saveCustomerContactRecords(workCaseId, customerAcceptanceView, tcgInfoView, contactRecordDetailViews, deleteList);
        _loadInitData();
        RequestContext.getCurrentInstance().addCallbackParam("functionComplete", true);
    }

    public void onCancelCustomerAcceptance() {
        _loadInitData();
        RequestContext.getCurrentInstance().addCallbackParam("functionComplete", true);
    }

    /*
     * Private method
     */
    private Reason _retrieveReasonFromId(int id) {
        for (Reason reason : reasons) {
            if (reason.getId() == id)
                return reason;
        }
        return null;
    }

    private void _loadInitData() {
        if (workCaseId > 0) {
            basicInfoView = basicInfoControl.getBasicInfo(workCaseId);
        }

        tcgInfoView = customerAcceptanceControl.getTCGInfoView(workCaseId);
        workCaseStatus = customerAcceptanceControl.getWorkCaseStatus(workCaseId);
        customerAcceptanceView = customerAcceptanceControl.getCustomerAcceptanceView(workCaseId);
        contactRecordDetailViews = new ArrayList<ContactRecordDetailView>(customerAcceptanceControl.getContactRecordDetails(customerAcceptanceView.getId()));
        contactRecord = null;
        deleteList = new ArrayList<ContactRecordDetailView>();
        deletedRowId = -1;
        preRenderCheck = false;
    }

    public CustomerAcceptanceView getCustomerAcceptanceView() {
        return customerAcceptanceView;
    }

    public Date getLastUpdateDateTime() {
        return customerAcceptanceView.getModifyDate();
    }

    public String getLastUpdateBy() {
        User modUser = customerAcceptanceView.getModifyBy();
        if (modUser == null)
            return "";
        return modUser.getDisplayName();
    }

    public String getMinTCGPayinSlipSendDate() {
        SimpleDateFormat dFmt = new SimpleDateFormat("dd/MM/yyyy", new Locale("th", "TH"));
        return dFmt.format(new Date());
    }

    public ApproveResult getApproveResult() {
        if (basicInfoView == null)
            return ApproveResult.NA;
        else
            return basicInfoView.getApproveResult();
    }

    public ApproveType getApproveType() {
        if (basicInfoView == null)
            return ApproveType.NA;
        else
            return basicInfoView.getApproveType();
    }

    public void setApproveType(ApproveType type) {
        //DO NOTHING
    }

    public String getMinDate() {
        SimpleDateFormat dFmt = new SimpleDateFormat("dd/MM/yyyy", new Locale("th", "TH"));
        return dFmt.format(new Date());
    }

    public List<ContactRecordDetailView> getContactRecordDetailViews() {
        return contactRecordDetailViews;
    }

    public ContactRecordDetailView getContactRecord() {
        return contactRecord;
    }

    public void setContactRecord(ContactRecordDetailView contactRecord) {
        this.contactRecord = contactRecord;
    }

    public List<Reason> getReasons() {
        return reasons;
    }

    public int getDeletedRowId() {
        return deletedRowId;
    }

    public void setDeletedRowId(int deletedRowId) {
        this.deletedRowId = deletedRowId;
    }

    public TCGInfoView getTcgInfoView() {
        return tcgInfoView;
    }

    public boolean isAddDialog() {
        return addDialog;
    }
}
