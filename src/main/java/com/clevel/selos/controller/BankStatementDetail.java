package com.clevel.selos.controller;

import com.clevel.selos.businesscontrol.BankStmtControl;
import com.clevel.selos.dao.master.AccountStatusDAO;
import com.clevel.selos.dao.master.BankAccountTypeDAO;
import com.clevel.selos.dao.master.BankDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.view.*;
import com.clevel.selos.system.message.ExceptionMessage;
import com.clevel.selos.system.message.Message;
import com.clevel.selos.system.message.NormalMessage;
import com.clevel.selos.system.message.ValidationMessage;
import com.clevel.selos.transform.AccountStatusTransform;
import com.clevel.selos.transform.BankAccountStatusTransform;
import com.clevel.selos.transform.BankAccountTypeTransform;
import com.clevel.selos.transform.BankTransform;
import com.clevel.selos.util.DateTimeUtil;
import com.clevel.selos.util.FacesUtil;
import com.clevel.selos.util.Util;
import org.joda.time.DateTime;
import org.primefaces.component.selectonemenu.SelectOneMenu;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.Flash;
import javax.faces.event.ValueChangeListener;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import javax.swing.*;
import java.io.Serializable;
import java.util.*;

@ViewScoped
@ManagedBean(name = "bankStatementDetail")
public class BankStatementDetail implements Serializable {
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

    //Business logic
    @Inject
    BankStmtControl bankStmtControl;

    //DAO
    @Inject
    BankDAO bankDAO;
    @Inject
    BankAccountTypeDAO bankAccountTypeDAO;
    @Inject
    AccountStatusDAO accountStatusDAO;

    //Transform
    @Inject
    BankTransform bankTransform;
    @Inject
    BankAccountTypeTransform bankAccTypeTransform;
    @Inject
    BankAccountStatusTransform bankAccStatusTransform;
    @Inject
    AccountStatusTransform accountStatusTransform;

    //Parameters from Bank statement summary
    private BankStmtSummaryView summaryView;
    private boolean isTmbBank;
    private Date lastMonthDate;
    private int numberOfMonths;

    //View form
    private BankStmtView bankStmtView;
    private Date currentDate;

    enum ModeForButton { ADD, EDIT }
    private ModeForButton modeForButton;

    //Select items list
    private List<BankView> bankViewList;
    private List<BankAccountTypeView> bankAccTypeViewList;
    private List<BankAccountTypeView> othBankAccTypeViewList;
    private List<AccountStatusView> accStatusViewList;

    //Messages Dialog
    private String messageHeader;
    private String message;

    //Session
    private long workCaseId;
    private long workCasePrescreenId;
    private long stepId;
    private String userId;

    private boolean bankAccTypeSelectRequired;

    public BankStatementDetail() {
    }

    @PostConstruct
    public void onCreation() {
        preRender();
        initViewFormAndSelectItems();
        checkRequiredBankAccTypeSelected();
    }

    private void preRender() {
        log.info("preRender ::: setSession ");
        HttpSession session = FacesUtil.getSession(false);
        session.setAttribute("workCaseId", 2);
        session.setAttribute("stepId", 1006);
        session.setAttribute("userId", 10001);

        session = FacesUtil.getSession(true);
        if (session.getAttribute("workCaseId") != null) {
            workCaseId = Long.parseLong(session.getAttribute("workCaseId").toString());
            workCasePrescreenId = 21;
            stepId = Long.parseLong(session.getAttribute("stepId").toString());
            userId = session.getAttribute("userId").toString();
        } else {
            //TODO return to inbox
            log.info("preRender ::: workCaseId is null.");
            try {
                FacesUtil.redirect("/site/inbox.jsf");
                return;
            } catch (Exception e) {
                log.info("Exception :: {}", e);
            }
        }

        //Parameters passed from Bank statement summary page
        Flash flash = FacesUtil.getFlash();
        Map<String, Object> map = (Map<String, Object>) flash.get("bankStmtSumParams");
        if (map != null) {
            summaryView = (BankStmtSummaryView) map.get("bankStmtSumView");
            isTmbBank = (Boolean) map.get("isTmbBank");
            lastMonthDate = (Date) map.get("lastMonthDate");
            numberOfMonths = (Integer) map.get("numberOfMonths");
            bankStmtView = (BankStmtView) map.get("selectedBankStmtView");

            log.debug("onCreation() bankStmtSumParams:{isTmbBank: {}, lastMonthDate: {}, numberOfMonths: {}, selectedBankStmtView is null: {}}",
                    isTmbBank, lastMonthDate, numberOfMonths, null == bankStmtView);

            // if(add new bank statement)
            // User must be click Refresh for retrieve 'lastMonthDate' and 'numberOfMonths' first
            if (null == bankStmtView && null == lastMonthDate && numberOfMonths == 0) {
                FacesUtil.redirect("/site/bankStatementSummary.jsf");
                return;
            }
        } else {
            //Return to Bank statement summary if parameter is null
            FacesUtil.redirect("/site/bankStatementSummary.jsf");
            return;
        }
    }

    private void initViewFormAndSelectItems() {
        if (bankStmtView == null) {
            // add new Bank statement
            bankStmtView = new BankStmtView();
            bankStmtView.setBankStmtDetailViewList(generateBankStmtDetail());
            modeForButton = ModeForButton.ADD;
        } else {
            // edit Bank statement
            numberOfMonths = Util.safetyList(bankStmtView.getBankStmtDetailViewList()).size();
            modeForButton = ModeForButton.EDIT;
        }

        // select items
        bankViewList = new ArrayList<BankView>();
        if (isTmbBank) {
            bankViewList.add(bankTransform.getBankView(bankDAO.getTMBBank()));
        } else {
            bankViewList = bankTransform.getBankViewList(bankDAO.getListExcludeTMB());
        }

        bankAccTypeViewList = bankAccTypeTransform.getBankAccountTypeView(bankAccountTypeDAO.getBankAccountTypeList());
        othBankAccTypeViewList = bankAccTypeTransform.getBankAccountTypeView(bankAccountTypeDAO.getOtherAccountTypeList());
        accStatusViewList = accountStatusTransform.transformToViewList(accountStatusDAO.findAll());
    }


    private List<BankStmtDetailView> generateBankStmtDetail() {
        List<BankStmtDetailView> bankStmtDetailViewList;
        bankStmtDetailViewList = new ArrayList<BankStmtDetailView>(numberOfMonths);
        Date date;
        for (int i = 0; i < numberOfMonths; i++) {
            BankStmtDetailView bankStmtDetailView = new BankStmtDetailView();
            date = DateTimeUtil.getOnlyDatePlusMonth(lastMonthDate, -i);
            bankStmtDetailView.setAsOfDate(date);
            bankStmtDetailViewList.add(bankStmtDetailView);
        }
        bankStmtControl.sortAsOfDateBankStmtDetails(bankStmtDetailViewList, SortOrder.ASCENDING);
        return bankStmtDetailViewList;
    }

    public void onSave() {
        log.debug("onSave() bankStmtView: {}", bankStmtView);
        // calculate Bank statement and detail
        bankStmtControl.bankStmtDetailCalculation(bankStmtView, summaryView.getSeasonal());

        if (bankStmtView.getId() == 0) {
            // add new Bank statement
            if (isTmbBank) {
                summaryView.getTmbBankStmtViewList().add(bankStmtView);
            } else {
                summaryView.getOthBankStmtViewList().add(bankStmtView);
            }
        }
        else {
            // edit exist Bank statement
            boolean foundBankStmt = false;
            // find from TMB Bank statement list
            for (int i=0; i<summaryView.getTmbBankStmtViewList().size(); i++) {
                BankStmtView tmbBankStmtView = summaryView.getTmbBankStmtViewList().get(i);
                if (bankStmtView.getId() == tmbBankStmtView.getId()) {
                    // replace edited Bank statement to old
                    summaryView.getTmbBankStmtViewList().set(i, bankStmtView);
                    foundBankStmt = true;
                    break;
                }
            }

            if (!foundBankStmt) {
                // find from Other Bank statement list
                for (int i=0; i<summaryView.getOthBankStmtViewList().size(); i++) {
                    BankStmtView othBankStmtView = summaryView.getOthBankStmtViewList().get(i);
                    if (bankStmtView.getId() == othBankStmtView.getId()) {
                        // replace edited Bank statement to old
                        summaryView.getOthBankStmtViewList().set(i, bankStmtView);
                        break;
                    }
                }
            }
        }

        try {
            // recalculate and save Bank statement summary
            bankStmtControl.bankStmtSumTotalCalculation(summaryView);
            bankStmtControl.saveBankStmtSummary(summaryView, workCaseId, workCasePrescreenId, userId);

            messageHeader = "Save Bank Statement Detail Success.";
            message = "Save Bank Statement Detail data success.";
            RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");

        } catch (Exception e) {
            messageHeader = "Save Bank Statement Detail Failed.";
            if (e.getCause() != null) {
                message = "Save Bank Statement Detail data failed. Cause : " + e.getCause().toString();
            } else {
                message = "Save Bank Statement Detail data failed. Cause : " + e.getMessage();
            }
            RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
        }
    }

    public void onCancel() {
        log.debug("onCancel()");
        //initViewFormAndSelectItems();
    }

    private void checkRequiredBankAccTypeSelected() {
        int bankAccTypeId = bankStmtView.getBankAccountTypeView().getId();
        int otherAccType = bankStmtView.getOtherAccountType();

        if (bankAccTypeId == 0 && otherAccType == 0)
            bankAccTypeSelectRequired = true;
        else
            bankAccTypeSelectRequired = false;
    }

    public void onChangeBankAccTypeSelected() {
        if (bankStmtView.getBankAccountTypeView().getId() != 0) {
            bankStmtView.setOtherAccountType(0);
        }
        checkRequiredBankAccTypeSelected();
    }

    public void onChangeOthAccTypeSelected() {
        if (bankStmtView.getOtherAccountType() != 0) {
            bankStmtView.getBankAccountTypeView().setId(0);
        }
        checkRequiredBankAccTypeSelected();
    }

    //-------------------- Getter/Setter --------------------
    public BankStmtView getBankStmtView() {
        return bankStmtView;
    }

    public void setBankStmtView(BankStmtView bankStmtView) {
        this.bankStmtView = bankStmtView;
    }

    public int getNumberOfMonths() {
        return numberOfMonths;
    }

    public void setNumberOfMonths(int numberOfMonths) {
        this.numberOfMonths = numberOfMonths;
    }

    public List<BankView> getBankViewList() {
        return bankViewList;
    }

    public void setBankViewList(List<BankView> bankViewList) {
        this.bankViewList = bankViewList;
    }

    public List<BankAccountTypeView> getBankAccTypeViewList() {
        return bankAccTypeViewList;
    }

    public void setBankAccTypeViewList(List<BankAccountTypeView> bankAccTypeViewList) {
        this.bankAccTypeViewList = bankAccTypeViewList;
    }

    public List<BankAccountTypeView> getOthBankAccTypeViewList() {
        return othBankAccTypeViewList;
    }

    public void setOthBankAccTypeViewList(List<BankAccountTypeView> othBankAccTypeViewList) {
        this.othBankAccTypeViewList = othBankAccTypeViewList;
    }

    public List<AccountStatusView> getAccStatusViewList() {
        return accStatusViewList;
    }

    public void setAccStatusViewList(List<AccountStatusView> accStatusViewList) {
        this.accStatusViewList = accStatusViewList;
    }

    public String getMessageHeader() {
        return messageHeader;
    }

    public void setMessageHeader(String messageHeader) {
        this.messageHeader = messageHeader;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getCurrentDate() {
        return DateTime.now().toDate();
    }

    public void setCurrentDate(Date currentDate) {
        this.currentDate = currentDate;
    }

    public boolean isBankAccTypeSelectRequired() {
        return bankAccTypeSelectRequired;
    }

    public void setBankAccTypeSelectRequired(boolean bankAccTypeSelectRequired) {
        this.bankAccTypeSelectRequired = bankAccTypeSelectRequired;
    }
}
