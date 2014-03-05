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
import com.clevel.selos.model.RoleValue;
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
import org.primefaces.context.RequestContext;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
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
    private Date currentDate;
    private String currentDateDDMMYY;

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
    //private String userId;

    private boolean bankAccTypeSelectRequired;
    private boolean roleUW;
    private boolean clickSave;

    public BankStatementDetail() {
    }

    @PostConstruct
    public void onCreation() {
        preRender();
        initViewFormAndSelectItems();
        checkRequiredBankAccTypeSelected();
        clickSave = false;
    }

    private void preRender() {
        log.info("preRender ::: setSession ");
        /*HttpSession session = FacesUtil.getSession(false);
        session.setAttribute("workCaseId", new Long(2));
        session.setAttribute("stepId", 1006);
        session.setAttribute("userId", 10001);*/

        HttpSession session = FacesUtil.getSession(true);
        if (session.getAttribute("workCaseId") != null) {
            workCaseId = Long.parseLong(session.getAttribute("workCaseId").toString());
            stepId = Long.parseLong(session.getAttribute("stepId").toString());
            //userId = session.getAttribute("userId").toString();
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

        if (FacesUtil.getSessionMapValue("bankStmtSumView") == null
            || FacesUtil.getSessionMapValue("isTmbBank") == null
            || FacesUtil.getSessionMapValue("lastMonthDate") == null
            || FacesUtil.getSessionMapValue("numberOfMonths") == null) {

            log.error("Some necessary parameters from Bank statement summary is null!");
            FacesUtil.redirect("/site/bankStatementSummary.jsf");
            return;
        } else {
            summaryView = (BankStmtSummaryView) FacesUtil.getSessionMapValue("bankStmtSumView");
            isTmbBank = (Boolean) FacesUtil.getSessionMapValue("isTmbBank");
            lastMonthDate = (Date) FacesUtil.getSessionMapValue("lastMonthDate");
            numberOfMonths = (Integer) FacesUtil.getSessionMapValue("numberOfMonths");
            bankStmtView = (BankStmtView) FacesUtil.getSessionMapValue("selectedBankStmtView");

            log.debug("Passed parameters from Bank statement summary ::: bankStmtSumParams:{isTmbBank: {}, lastMonthDate: {}, numberOfMonths: {}, selectedBankStmtView is null: {}}",
                    isTmbBank, lastMonthDate, numberOfMonths, null == bankStmtView);

            if (numberOfMonths == 0) {
                log.error("Number of months from Bank statement summary is zero(0)!, Can not be generate Bank statement detail table.");
                FacesUtil.redirect("/site/bankStatementSummary.jsf");
                return;
            }
        }

        // Check Role
        int roleId = bankStmtControl.getUserRoleId();
        if (RoleValue.UW.id() == roleId) {
            roleUW = true;
        }
    }

    private void initViewFormAndSelectItems() {
        if (bankStmtView == null) {
            // ADD NEW - Click Add New from Summary page
            bankStmtView = new BankStmtView();
            bankStmtView.setBankStmtDetailViewList(generateBankStmtDetail());
        }
        else {
            // Click Edit from Summary page
            // - Edit already exist Bank statement from Database
            // - Edit Bank statement from DWH (NOT EXIST from Database) will be generate detail from
            if (bankStmtView.getBankStmtDetailViewList() != null && bankStmtView.getBankStmtDetailViewList().size() > 0) {
                numberOfMonths = Util.safetyList(bankStmtView.getBankStmtDetailViewList()).size();
            } else {
                bankStmtView.setBankStmtDetailViewList(generateBankStmtDetail());
            }
        }

        bankStmtControl.sortAsOfDateBankStmtDetails(bankStmtView.getBankStmtDetailViewList(), SortOrder.ASCENDING);

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
        bankStmtDetailViewList = new ArrayList<BankStmtDetailView>();
        Date date;
        for (int i = 0; i < numberOfMonths; i++) {
            BankStmtDetailView bankStmtDetailView = new BankStmtDetailView();
            date = DateTimeUtil.getOnlyDatePlusMonth(lastMonthDate, -i);
            bankStmtDetailView.setAsOfDate(date);
            bankStmtDetailViewList.add(bankStmtDetailView);
        }
        return bankStmtDetailViewList;
    }

    public void onSave() {
        log.debug("onSave()");
        log.debug("-> summaryView: {}", summaryView);
        log.debug("-> bankStmtView: {}", bankStmtView);
        // calculate Bank statement and detail
        bankStmtControl.bankStmtDetailCalculation(bankStmtView, summaryView.getSeasonal());

        if (bankStmtView.getId() == 0) {
            // Add New Bank statement
            bankStmtView = bankStmtControl.saveBankStmt(bankStmtView);

            if (isTmbBank) {
                if (summaryView.getTmbBankStmtViewList() != null) {
                    summaryView.getTmbBankStmtViewList().add(bankStmtView);
                } else {
                    List<BankStmtView> tmbBankStmtViewList = new ArrayList<BankStmtView>();
                    tmbBankStmtViewList.add(bankStmtView);
                    summaryView.setTmbBankStmtViewList(tmbBankStmtViewList);
                }
            } else {
                if (summaryView.getOthBankStmtViewList() != null) {
                    summaryView.getOthBankStmtViewList().add(bankStmtView);
                } else {
                    List<BankStmtView> othBankStmtViewList = new ArrayList<BankStmtView>();
                    othBankStmtViewList.add(bankStmtView);
                    summaryView.setOthBankStmtViewList(othBankStmtViewList);
                }
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

        clickSave = true;

        // update Main account and Highest inflow
        bankStmtControl.updateMainAccAndHighestInflow(summaryView);
        // re-calculate Total & Grand total summary
        bankStmtControl.bankStmtSumTotalCalculation(summaryView, false);

        try {
            summaryView = bankStmtControl.saveBankStmtSummary(summaryView, workCaseId, 0);
            // update related parts
            dbrControl.updateValueOfDBR(workCaseId);
            exSummaryControl.calForBankStmtSummary(workCaseId);
            bizInfoSummaryControl.calByBankStatement(workCaseId);

            messageHeader = msg.get("app.messageHeader.info");
            message = "Save Bank Statement Detail data success.";
            severity = MessageDialogSeverity.INFO.severity();
        }
        catch (Exception e) {
            messageHeader = msg.get("app.messageHeader.error");
            severity = MessageDialogSeverity.ALERT.severity();
            if (e.getCause() != null) {
                message = "Save Bank Statement Detail data failed. Cause : " + e.getCause().toString();
            } else {
                message = "Save Bank Statement Detail data failed. Cause : " + e.getMessage();
            }
        }
        RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
    }

    public void onCancel() {
        log.debug("onCancel()");
        if (clickSave) {
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

    public void setCurrentDate(Date currentDate) {
        this.currentDate = currentDate;
    }

    public String getCurrentDateDDMMYY() {
        return currentDateDDMMYY = DateTimeUtil.convertToStringDDMMYYYY(getCurrentDate());
    }

    public void setCurrentDateDDMMYY(String currentDateDDMMYY) {
        this.currentDateDDMMYY = currentDateDDMMYY;
    }

    public boolean isBankAccTypeSelectRequired() {
        return bankAccTypeSelectRequired;
    }

    public void setBankAccTypeSelectRequired(boolean bankAccTypeSelectRequired) {
        this.bankAccTypeSelectRequired = bankAccTypeSelectRequired;
    }

    public boolean isRoleUW() {
        return roleUW;
    }

    public void setRoleUW(boolean roleUW) {
        this.roleUW = roleUW;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }
}
