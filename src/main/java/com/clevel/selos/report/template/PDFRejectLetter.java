package com.clevel.selos.report.template;


import com.clevel.selos.businesscontrol.CustomerInfoControl;
import com.clevel.selos.dao.master.ReasonDAO;
import com.clevel.selos.dao.master.UserDAO;
import com.clevel.selos.dao.master.UserTeamDAO;
import com.clevel.selos.dao.working.*;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.*;
import com.clevel.selos.model.db.master.Reason;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.master.UserTeam;
import com.clevel.selos.model.db.working.*;
import com.clevel.selos.model.report.RejectLetterReport;
import com.clevel.selos.model.view.AppBorrowerHeaderView;
import com.clevel.selos.model.view.AppHeaderView;
import com.clevel.selos.system.message.Message;
import com.clevel.selos.system.message.NormalMessage;
import com.clevel.selos.util.FacesUtil;
import com.clevel.selos.util.Util;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@ViewScoped
@ManagedBean(name = "rejectLetter")
public class PDFRejectLetter implements Serializable {
    @Inject
    @SELOS
    Logger log;

    long workCaseId;
    private long workCasePreScreenId;

    @Inject
    @NormalMessage
    Message msg;

    @Inject CustomerDAO customerDAO;
    @Inject private WorkCaseDAO workCaseDAO;
    @Inject private WorkCasePrescreenDAO workCasePrescreenDAO;
    @Inject private AddressDAO addressDAO;
    @Inject private CustomerInfoControl customerInfoControl;
    @Inject private UWRuleResultSummaryDAO uwRuleResultSummaryDAO;
    @Inject private UWRuleResultDetailDAO uwRuleResultDetailDAO;
    @Inject private UserDAO userDAO;
    @Inject private UserTeamDAO userTeamDAO;
    @Inject private UserTeam userTeam;
    @Inject CancelRejectInfoDAO cancelRejectInfoDAO;
    @Inject ExSummaryDAO exSummaryDAO;
    @Inject ExSumDeviateDAO exSumDeviateDAO;
    @Inject ReasonDAO reasonDAO;

    private List<Customer> customers;
    private AppHeaderView appHeaderView;
    private List<UWRuleResultDetail> uwRuleResultDetails;
    private User userView;
    private UWRuleResultSummary uwRuleResultSummary;
    private long statusId;
    private WorkCase workCase;
    private WorkCasePrescreen workCasePrescreen;
    private CancelRejectInfo cancelRejectInfo;
    private int typeReject;
    private ExSummary exSummary;
    private List<ExSumDeviate> exSumDeviate;
    private Reason reason;
    private RejectLetterReport rejectLetterReport;
    private HttpSession session;
    private final String SPACE = " ";
    private int typeNCB;
    private int typePolicy;
    private int typeIncome;

    public PDFRejectLetter() {
    }

    public boolean checkSession(HttpSession session){
        boolean checkSession = false;
        if(( (Long)session.getAttribute("workCaseId") != 0 || (Long)session.getAttribute("workCasePreScreenId") != 0 ) &&
                (Long)session.getAttribute("stepId") != 0){
            checkSession = true;
        }

        return checkSession;
    }

    public void init(){
        log.debug("--on init()");
        session = FacesUtil.getSession(true);
        rejectLetterReport = new RejectLetterReport();

        if(checkSession(session)){
            if((Long)session.getAttribute("workCaseId") != 0){
                workCaseId = (Long)session.getAttribute("workCaseId");
            } else if ((Long)session.getAttribute("workCasePreScreenId") != 0){
                workCasePreScreenId = (Long)session.getAttribute("workCasePreScreenId");
            } else {
                log.debug("workCaseId is null.");
                try{
                    FacesUtil.redirect("/site/inbox.jsf");
                }catch (Exception ex){
                    log.error("Exception :: {}",ex);
                }
            }
        }

        if (!Util.isZero(workCaseId)){
            customers = new ArrayList<Customer>();
            customers = customerDAO.findBorrowerByWorkCaseId(workCaseId);
            uwRuleResultSummary = uwRuleResultSummaryDAO.findByWorkCaseId(workCaseId);
            cancelRejectInfo = cancelRejectInfoDAO.findByWorkCaseId(workCaseId);
            exSummary = exSummaryDAO.findByWorkCaseId(workCaseId);
            if (!Util.isNull(exSummary)){
                exSumDeviate = exSumDeviateDAO.findByExSumId(exSummary.getId());
                log.debug("--exSumDeviate. {}",exSumDeviate.size());
            }
            workCase = workCaseDAO.findById(workCaseId);
                if (!Util.isNull(workCase)){
                    statusId = workCase.getStatus().getId();
                }
        } else if (!Util.isZero(workCasePreScreenId)){
            customers = customerDAO.findBorrowerByWorkCasePreScreenId(workCasePreScreenId);
            uwRuleResultSummary = uwRuleResultSummaryDAO.findByWorkcasePrescreenId(workCasePreScreenId);
            cancelRejectInfo = cancelRejectInfoDAO.findByWorkCasePreScreenId(workCasePreScreenId);
            workCasePrescreen = workCasePrescreenDAO.findById(workCasePreScreenId);
                if (!Util.isNull(workCasePrescreen)){
                    statusId = workCasePrescreen.getStatus().getId();
                }
        }
        findCancelCodeByUWResult();
        findCancelCodeByExSum();
        onCheckLogic();
    }

    public int getColorByUwRleResultSummary(){
        log.debug("--On getColorByUwRleResultSummary.");
        int colorCode = 3;
        if (!Util.isNull(uwRuleResultSummary)){
           if ((UWResultColor.RED).equals(uwRuleResultSummary.getUwResultColor())){
               colorCode = 0;
           } else if ((UWResultColor.GREEN).equals(uwRuleResultSummary.getUwResultColor())){
               colorCode = 1;
           } else if ((UWResultColor.YELLOW).equals(uwRuleResultSummary.getUwResultColor())){
               colorCode = 2;
           }
        } else {
            colorCode = 4;
        }
        log.debug("--Color is value. {}",colorCode);
        return colorCode;
    }

    public void findCancelCodeByUWResult(){
        log.debug("--On findCancelCodeByUWResult.");
        uwRuleResultDetails = new ArrayList<UWRuleResultDetail>();

        if (!Util.isNull(uwRuleResultSummary)){
            if (!Util.isZero(uwRuleResultSummary.getId())){
                uwRuleResultDetails = uwRuleResultDetailDAO.findByUWRuleSummaryId(uwRuleResultSummary.getId());
                if (!Util.isNull(uwRuleResultDetails)){
                    for (UWRuleResultDetail ruleResultDetail : uwRuleResultDetails){
                        if (!Util.isNull(ruleResultDetail.getRejectGroup())){
                            if (ruleResultDetail.getRejectGroup().getId() == 1){
                                typeNCB = ruleResultDetail.getRejectGroup().getId();
                            } else if (ruleResultDetail.getRejectGroup().getId() == 2){
                               typeIncome = ruleResultDetail.getRejectGroup().getId();
                            } else if (ruleResultDetail.getRejectGroup().getId() == 3){
                                typePolicy = ruleResultDetail.getRejectGroup().getId();
                            }
                        }
                    }
                }
            }
        }
    }

    public void findCancelCodeByExSum(){
        log.debug("--On findCancelCodeByExSum.");

        reason = new Reason();
        if (!Util.isNull(exSummary)){
            if (Util.isSafetyList(exSumDeviate)){
                for (ExSumDeviate sumDeviate : exSumDeviate){
                    reason = reasonDAO.findById(sumDeviate.getDeviateCode().getId());
                    if (!Util.isNull(reason.getUwRejectGroup())){
                        if (reason.getUwRejectGroup().getId() == 1) {
                            typeNCB = reason.getUwRejectGroup().getId();
                        } else if (reason.getUwRejectGroup().getId() == 2){
                            typeIncome = reason.getUwRejectGroup().getId();
                        } else if (reason.getUwRejectGroup().getId() == 3){
                            typePolicy = reason.getUwRejectGroup().getId();
                        }
                    }
                }
            }
        }
    }

    public Map cancelCode(){
        String[][] cancelCode = {{"C034", "C034"},
                {"C035", "C035"},
                {"C036", "C036"},
                {"C037", "C037"},
                {"C038", "C038"},
                {"C039", "C039"},
                {"C040", "C040"}};
        return ArrayUtils.toMap(cancelCode);
    }

    public int onCheckLogic(){
        log.debug("OnCeckLogic.");

        if (!Util.isZero(workCaseId) || !Util.isZero(workCasePreScreenId)){

            //##### status 90001
            if (statusId == StatusValue.CANCEL_CA.value()){
                if (Util.isNull(uwRuleResultSummary) || (UWResultColor.GREEN).equals(uwRuleResultSummary.getUwResultColor()) || (UWResultColor.YELLOW).equals(uwRuleResultSummary.getUwResultColor())){
                    if (!Util.isNull(cancelRejectInfo)){
                        if (cancelCode().containsKey(cancelRejectInfo.getReason().getCode())){
                            typeReject = 2;
                        }
                    }
                } else {
                    typeReject = 1;
                }
            }

            //##### status 90002/90007
            if (statusId == StatusValue.REJECT_UW1.value() || statusId == StatusValue.REJECT_UW2.value()){
                typeReject = 1;
            }

            //##### status 90004
            if (statusId == StatusValue.REJECT_CA.value()){
                if ((UWResultColor.GREEN).equals(uwRuleResultSummary.getUwResultColor()) || (UWResultColor.YELLOW).equals(uwRuleResultSummary.getUwResultColor())){
                    typeReject = 2;
                } else if ((UWResultColor.RED).equals(uwRuleResultSummary.getUwResultColor())){
                    typeReject = 1;
                }
            }
        }

        log.debug("--typeReject. {}",typeReject);
        return typeReject;
    }

    public List<RejectLetterReport> fillAllNameReject (){
        log.debug("fillAllNameReject. {}");
        List<RejectLetterReport> reportList = new ArrayList<RejectLetterReport>();
        HttpSession session = FacesUtil.getSession(false);
        appHeaderView = (AppHeaderView) session.getAttribute("appHeaderInfo");

        if(Util.safetyList(appHeaderView.getBorrowerHeaderViewList()).size() > 0){
            for (AppBorrowerHeaderView view : appHeaderView.getBorrowerHeaderViewList()){
                RejectLetterReport report = new RejectLetterReport();
                report.setName(view.getBorrowerName());
                reportList.add(report);
            }
        } else {
            RejectLetterReport report = new RejectLetterReport();
            reportList.add(report);
            log.debug("appHeaderView.getBorrowerHeaderViewList is Null. {}");
        }

        return reportList;
    }

    public RejectLetterReport fillRejectLetter(){
        log.debug("fillRejectLetter. {}");

        if (!Util.isNull(workCase)){
            userView = userDAO.findByUserName(workCase.getCreateBy().getUserName());
        } else if (!Util.isNull(workCasePrescreen)){
            userView = userDAO.findByUserName(workCasePrescreen.getCreateBy().getUserName());
        }

        if (!Util.isNull(userView.getTeam())){
            userTeam = userTeamDAO.findByID(userView.getTeam().getId());
        } else {
            userTeam = new UserTeam();
        }

        RejectLetterReport letterReport = new RejectLetterReport();
        String date = Util.createDateTh(new Date());
        String[] spDate = date.split("/");
        int month = Integer.valueOf(spDate[1]);
        String setMonth;
        StringBuilder addressTH = null;

        if(!Util.isZero(workCaseId) || !Util.isZero(workCasePreScreenId)){
            if (!Util.isNull(workCase)){
                letterReport.setAppNumber(workCase.getAppNumber());
            } else {
                letterReport.setAppNumber(workCasePrescreen.getAppNumber());
            }

            for (Customer view : customers){
                Customer customer = customerDAO.findById(view.getId());

                if(!Util.isNull(customer.getAddressesList()) && customer.getAddressesList().size() > 0){
                    addressTH = new StringBuilder();
                    Address allAddress = customer.getAddressesList().get(0);
                    addressTH = addressTH.append(msg.get("app.bizInfoSummary.label.addressNo").concat(SPACE))
                        .append((allAddress.getAddressNo() != null ? allAddress.getAddressNo() : "-").concat(SPACE))
                        .append(msg.get("app.bizInfoSummary.label.addressMoo").concat(SPACE))
                        .append((allAddress.getMoo() != null ? allAddress.getMoo() : "-").concat(SPACE))
                        .append(msg.get("app.bizInfoSummary.label.addressBuilding").concat(SPACE))
                        .append((allAddress.getBuilding() != null ? allAddress.getBuilding() : "-").concat(SPACE))
                        .append(msg.get("app.bizInfoSummary.label.addressStreet").concat(SPACE))
                        .append((allAddress.getRoad() != null ? allAddress.getRoad() : "-").concat(SPACE))
                        .append(msg.get("app.bizInfoSummary.label.subdistrict").concat(SPACE))
                        .append((allAddress.getSubDistrict() != null ? allAddress.getSubDistrict().getCode() != 0 ? allAddress.getSubDistrict().getName() : "-" : "-").concat(SPACE))
                        .append(msg.get("app.bizInfoSummary.label.district").concat(SPACE))
                        .append((allAddress.getDistrict() != null ? allAddress.getDistrict().getId() != 0 ? allAddress.getDistrict().getName() : "-" : "-").concat(SPACE))
                        .append(msg.get("app.bizInfoSummary.label.province").concat(SPACE))
                        .append((allAddress.getProvince() != null ? allAddress.getProvince().getCode() != 0 ? allAddress.getProvince().getName() : "-" : "-").concat(SPACE))
                        .append(msg.get("app.bizInfoSummary.label.postCode").concat(SPACE))
                        .append((allAddress.getPostalCode() != null ? allAddress.getPostalCode() : "-").concat(SPACE))
                        .append(msg.get("app.bizInfoSummary.label.country").concat(SPACE))
                        .append((allAddress.getCountry() != null ? allAddress.getCountry().getId() != 0 ? allAddress.getCountry().getName() : "-" : "-").concat(SPACE));
                    break;
                } else {
                    addressTH = addressTH.append(SPACE);
                }
            }
            if(!Util.isNull(addressTH)){
                letterReport.setAddress(addressTH.toString());
            } else {
                letterReport.setAddress(SPACE);
            }


            switch (month){
                case 1: setMonth = msg.get("app.report.month.january"); break;
                case 2: setMonth = msg.get("app.report.month.february"); break;
                case 3: setMonth = msg.get("app.report.month.march"); break;
                case 4: setMonth = msg.get("app.report.month.april"); break;
                case 5: setMonth = msg.get("app.report.month.may"); break;
                case 6: setMonth = msg.get("app.report.month.june"); break;
                case 7: setMonth = msg.get("app.report.month.july"); break;
                case 8: setMonth = msg.get("app.report.month.august"); break;
                case 9: setMonth = msg.get("app.report.month.september"); break;
                case 10: setMonth = msg.get("app.report.month.october"); break;
                case 11: setMonth = msg.get("app.report.month.november"); break;
                case 12: setMonth = msg.get("app.report.month.december"); break;
                default:setMonth = SPACE;

            }

            StringBuilder stringBuilder =new StringBuilder();
            if(!Util.isNull(spDate) && spDate.length == 3){
                stringBuilder = stringBuilder.append(Integer.parseInt(spDate[0])).append(SPACE);
                stringBuilder = stringBuilder.append(setMonth).append(SPACE);
                stringBuilder =  stringBuilder.append(spDate[2]);
                letterReport.setDate(stringBuilder.toString());
            } else {
                letterReport.setDate(SPACE);
            }

            //PHONE_TEAM From NCB Reject Letter
            letterReport.setTeam_phone(!Util.isNull(userTeam.getTeam_phone()) ? userTeam.getTeam_phone() : "-");
        }
        return letterReport;
    }

    public int getTypeReject() {
        return typeReject;
    }

    public void setTypeReject(int typeReject) {
        this.typeReject = typeReject;
    }

    public int getTypeIncome() {
        return typeIncome;
    }

    public void setTypeIncome(int typeIncome) {
        this.typeIncome = typeIncome;
    }

    public int getTypePolicy() {
        return typePolicy;
    }

    public void setTypePolicy(int typePolicy) {
        this.typePolicy = typePolicy;
    }

    public int getTypeNCB() {
        return typeNCB;
    }

    public void setTypeNCB(int typeNCB) {
        this.typeNCB = typeNCB;
    }
}
