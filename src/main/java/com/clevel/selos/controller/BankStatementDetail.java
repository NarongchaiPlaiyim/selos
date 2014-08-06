package com.clevel.selos.controller;

import com.clevel.selos.businesscontrol.BankStmtControl;
import com.clevel.selos.businesscontrol.BizInfoSummaryControl;
import com.clevel.selos.businesscontrol.DBRControl;
import com.clevel.selos.businesscontrol.ExSummaryControl;
import com.clevel.selos.dao.master.AccountStatusDAO;
import com.clevel.selos.dao.master.BankAccountTypeDAO;
import com.clevel.selos.dao.master.BankDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.MessageDialogSeverity;
import com.clevel.selos.model.Screen;
import com.clevel.selos.model.view.*;
import com.clevel.selos.system.message.ExceptionMessage;
import com.clevel.selos.system.message.Message;
import com.clevel.selos.system.message.NormalMessage;
import com.clevel.selos.system.message.ValidationMessage;
import com.clevel.selos.transform.AccountStatusTransform;
import com.clevel.selos.transform.BankAccountStatusTransform;
import com.clevel.selos.transform.BankAccountTypeTransform;
import com.clevel.selos.transform.BankTransform;
import com.clevel.selos.util.FacesUtil;
import com.clevel.selos.util.Util;
import org.joda.time.DateTime;
import org.primefaces.context.RequestContext;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import javax.swing.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@ViewScoped
@ManagedBean(name = "bankStatementDetail")
public class BankStatementDetail extends BaseController {
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
    @Inject
    DBRControl dbrControl;
    @Inject
    ExSummaryControl exSummaryControl;
    @Inject
    BizInfoSummaryControl bizInfoSummaryControl;

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
    private String severity;

    //Session
    private long workCaseId;
    private long stepId;

    private boolean bankAccTypeSelectRequired;
    private boolean clickSaveSuccess;

    public BankStatementDetail() {
    }

    public void preRender() {
        log.info("preRender");
        HttpSession session = FacesUtil.getSession(false);

        if (checkSession(session)) {
            log.debug("preRender ::: Found session for case.");

            if (FacesUtil.getSessionMapValue("bankStmtSumView") == null ||
                FacesUtil.getSessionMapValue("isTmbBank") == null ||
                FacesUtil.getSessionMapValue("lastMonthDate") == null ||
                FacesUtil.getSessionMapValue("numberOfMonths") == null) {

                log.error("Some necessary parameters from Bank statement summary is null!");
                FacesUtil.redirect("/site/bankStatementSummary.jsf");
            }
            else if ((Integer) FacesUtil.getSessionMapValue("numberOfMonths") == 0) {
                log.error("Number of months from Bank statement summary = 0!");
                FacesUtil.redirect("/site/bankStatementSummary.jsf");
            }

        }
        else {
            log.debug("preRender ::: No session for case found. Redirect to Inbox");
            FacesUtil.redirect("/site/inbox.jsf");
        }
    }

    @PostConstruct
    public void onCreation() {
        log.debug("onCreation");
        HttpSession session = FacesUtil.getSession(false);

        if (checkSession(session)) {
            workCaseId = Util.parseLong(session.getAttribute("workCaseId"), 0);
            stepId = Util.parseLong(session.getAttribute("stepId"), 0);
            String ownerCaseUserId = Util.parseString(session.getAttribute("caseOwner"), "");

            loadFieldControl(workCaseId, Screen.BANK_STATEMENT_DETAIL, ownerCaseUserId);

            if (FacesUtil.getSessionMapValue("bankStmtSumView") != null &&
                FacesUtil.getSessionMapValue("isTmbBank") != null &&
                FacesUtil.getSessionMapValue("lastMonthDate") != null &&
                FacesUtil.getSessionMapValue("numberOfMonths") != null &&
                (Integer) FacesUtil.getSessionMapValue("numberOfMonths") != 0) {

                summaryView = (BankStmtSummaryView) FacesUtil.getSessionMapValue("bankStmtSumView");
                isTmbBank = (Boolean) FacesUtil.getSessionMapValue("isTmbBank");
                lastMonthDate = (Date) FacesUtil.getSessionMapValue("lastMonthDate");
                numberOfMonths = (Integer) FacesUtil.getSessionMapValue("numberOfMonths");
                bankStmtView = (BankStmtView) FacesUtil.getSessionMapValue("selectedBankStmtView");

                log.debug("Passed parameters from Bank statement summary ::: bankStmtSumParams:{isTmbBank: {}, lastMonthDate: {}, numberOfMonths: {}, selectedBankStmtView is null: {}}",
                        isTmbBank, lastMonthDate, numberOfMonths, null == bankStmtView);
                initViewFormAndSelectItems();
                checkRequiredBankAccTypeSelected();
                clickSaveSuccess = false;
            }
        }
    }

    private void initViewFormAndSelectItems() {
        if (bankStmtView != null) {
            log.debug("Edit Bank statement.");
            if (bankStmtView.getBankStmtDetailViewList() != null && bankStmtView.getBankStmtDetailViewList().size() > 0) {
                bankStmtView.setBankStmtDetailViewList(bankStmtControl.generateBankStmtDetail(numberOfMonths, lastMonthDate));
//                numberOfMonths = bankStmtView.getBankStmtDetailViewList().size();
//                Date tmpDate;
//                for (int i=(numberOfMonths-1), j=0; i>=0; i--, j++) {
//                    BankStmtDetailView bankStmtDetailView = bankStmtView.getBankStmtDetailViewList().get(j);
//                    if (Util.isNull(bankStmtDetailView.getAsOfDate())) {
//                        tmpDate = DateTimeUtil.getOnlyDatePlusMonth(lastMonthDate, -i);
//                        bankStmtDetailView.setAsOfDate(tmpDate);
//                        bankStmtDetailView.setDateOfMaxBalance(DateTimeUtil.getFirstDayOfMonth(tmpDate));
//                        bankStmtDetailView.setDateOfMinBalance(DateTimeUtil.getFirstDayOfMonth(tmpDate));
//                    }
//                }
            }
            else {
                bankStmtView.setBankStmtDetailViewList(bankStmtControl.generateBankStmtDetail(numberOfMonths, lastMonthDate));
            }
        }
        else {
            log.debug("Create new Bank statement.");
            bankStmtView = new BankStmtView();
            bankStmtView.setBankStmtDetailViewList(bankStmtControl.generateBankStmtDetail(numberOfMonths, lastMonthDate));
        }

        bankStmtControl.sortAsOfDateBankStmtDetails(bankStmtView.getBankStmtDetailViewList(), SortOrder.ASCENDING);

        log.debug("Load drop-down items from database.");
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

    public void onSave() {
        log.debug("onSave()");
        log.debug("-> summaryView: {}", summaryView);
        log.debug("-> bankStmtView: {}", bankStmtView);

        try {

            bankStmtControl.bankStmtDetailCalculation(bankStmtView, summaryView.getSeasonal());

            if (bankStmtView.getId() == 0) {
                // Add New Bank statement
                bankStmtView = bankStmtControl.saveBankStmt(bankStmtView);

                if (isTmbBank) {
                    if (summaryView.getTmbBankStmtViewList() == null) {
                        summaryView.setTmbBankStmtViewList(new ArrayList<BankStmtView>());
                    }
                    summaryView.getTmbBankStmtViewList().add(bankStmtView);
                }
                else {
                    if (summaryView.getOthBankStmtViewList() == null) {
                        summaryView.setOthBankStmtViewList(new ArrayList<BankStmtView>());
                    }
                    summaryView.getOthBankStmtViewList().add(bankStmtView);
                }

            } else {
                // Edit exist Bank statement
                bankStmtView = bankStmtControl.saveBankStmt(bankStmtView);

                boolean foundBankStmt = false;
                // TMB
                if (summaryView.getTmbBankStmtViewList() != null) {
                    for (int i=0; i<summaryView.getTmbBankStmtViewList().size(); i++) {
                        BankStmtView tmbBankStmtView = summaryView.getTmbBankStmtViewList().get(i);
                        if (bankStmtView.getId() == tmbBankStmtView.getId()) {
                            // replace edited Bank statement to old
                            summaryView.getTmbBankStmtViewList().set(i, bankStmtView);
                            foundBankStmt = true;
                            break;
                        }
                    }
                }

                if (!foundBankStmt) {
                    // OTHER
                    if (summaryView.getOthBankStmtViewList() != null) {
                        for (int i=0; i<summaryView.getOthBankStmtViewList().size(); i++) {
                            BankStmtView othBankStmtView = summaryView.getOthBankStmtViewList().get(i);
                            if (bankStmtView.getId() == othBankStmtView.getId()) {
                                summaryView.getOthBankStmtViewList().set(i, bankStmtView);
                                break;
                            }
                        }
                    }
                }
            }

            clickSaveSuccess = true;

            // update Main account and Highest inflow
            bankStmtControl.updateMainAccAndHighestInflow(summaryView);
            // re-calculate Total & Grand total summary
            bankStmtControl.bankStmtSumTotalCalculation(summaryView, false);

            BankStmtSummaryView resultBankStmtSumView = bankStmtControl.saveBankStmtSumFullApp(summaryView, workCaseId);
            if (resultBankStmtSumView != null) {
                summaryView = resultBankStmtSumView;
            }

            // update related parts
            dbrControl.updateValueOfDBR(workCaseId);
            exSummaryControl.calForBankStmtSummary(workCaseId, stepId);
            bizInfoSummaryControl.calByBankStatement(workCaseId, stepId);

            messageHeader = msg.get("app.messageHeader.info");
            message = "Save Bank Statement Detail data success.";
            severity = MessageDialogSeverity.INFO.severity();
        }
        catch (Exception e) {
            messageHeader = msg.get("app.messageHeader.error");
            severity = MessageDialogSeverity.ALERT.severity();
            message = "Save Bank Statement Detail data failed. Cause : ";
            message += e.getCause() != null ? e.getCause().toString() : e.getMessage();
        }

        RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
    }

    public void onCancel() {
        log.debug("onCancel()");
        if (clickSaveSuccess) {
            initViewFormAndSelectItems();
            checkRequiredBankAccTypeSelected();
        } else {
            onCreation();
        }
    }

    private void checkRequiredBankAccTypeSelected() {
        int bankAccTypeId = bankStmtView.getBankAccountTypeId();
        int otherAccType = bankStmtView.getOtherAccountType();

        bankAccTypeSelectRequired = (bankAccTypeId == 0 && otherAccType == 0);
        log.debug("checkRequiredBankAccTypeSelected() bankAccTypeSelectRequired = {}", bankAccTypeSelectRequired);
    }

    public void onChangeBankAccTypeSelected() {
        if (bankStmtView.getBankAccountTypeId() != 0) {
            bankStmtView.setOtherAccountType(0);
        }
        checkRequiredBankAccTypeSelected();
    }

    public void onChangeOthAccTypeSelected() {
        if (bankStmtView.getOtherAccountType() != 0) {
            bankStmtView.setBankAccountTypeId(0);
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

    public boolean isBankAccTypeSelectRequired() {
        return bankAccTypeSelectRequired;
    }

    public void setBankAccTypeSelectRequired(boolean bankAccTypeSelectRequired) {
        this.bankAccTypeSelectRequired = bankAccTypeSelectRequired;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }
}
