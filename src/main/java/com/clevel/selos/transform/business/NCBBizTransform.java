package com.clevel.selos.transform.business;

import com.clevel.selos.dao.master.AccountStatusDAO;
import com.clevel.selos.dao.master.AccountTypeDAO;
import com.clevel.selos.dao.master.SettlementStatusDAO;
import com.clevel.selos.integration.NCB;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.integration.ncb.nccrs.models.response.*;
import com.clevel.selos.integration.ncb.nccrs.nccrsmodel.NCCRSModel;
import com.clevel.selos.integration.ncb.nccrs.nccrsmodel.NCCRSOutputModel;
import com.clevel.selos.integration.ncb.ncrs.models.response.*;
import com.clevel.selos.integration.ncb.ncrs.ncrsmodel.NCRSModel;
import com.clevel.selos.integration.ncb.ncrs.ncrsmodel.NCRSOutputModel;
import com.clevel.selos.model.*;
import com.clevel.selos.model.db.master.AccountStatus;
import com.clevel.selos.model.db.master.AccountType;
import com.clevel.selos.model.db.master.SettlementStatus;
import com.clevel.selos.model.view.NCBDetailView;
import com.clevel.selos.model.view.NCBInfoView;
import com.clevel.selos.model.view.NcbView;
import com.clevel.selos.system.Config;
import com.clevel.selos.util.DateTimeUtil;
import com.clevel.selos.util.Util;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.*;

public class NCBBizTransform extends BusinessTransform {
    @Inject
    @NCB
    Logger log;
    @Inject
    AccountTypeDAO accountTypeDAO;
    @Inject
    AccountStatusDAO accountStatusDAO;
    @Inject
    SettlementStatusDAO settlementStatusDAO;

    private final int SIX_MONTH = 6;
    private final int TWELVE_MONTH = 12;

    private final String TMB_BANK = "TMB";
    private final String ACCOUNT_TYPE_OD_IND = "04";
    private final String ENQ_PURPOSE_IND = "01";

    @Inject
    @Config(name = "ncb.nccrs.bank.tmb")
    String TMB_BANK_THAI;
    @Inject
    @Config(name = "ncb.nccrs.purpose.type")
    String ENQ_PURPOSE_JUR;
    @Inject
    @Config(name = "ncb.nccrs.account.type")
    String ACCOUNT_TYPE_OD_JUR;

    public List<NcbView> transformIndividual(List<NCRSOutputModel> responseNCRSModels) throws Exception{
        List<NcbView> ncbViews = null;
        List<NCBDetailView> ncbDetailViews = null;
        //NCBSummaryView ncbSummaryView = null;
        if (responseNCRSModels != null && responseNCRSModels.size() > 0) {
            ncbViews = new ArrayList<NcbView>();
            for (NCRSOutputModel responseNCRSModel : responseNCRSModels) {
                NcbView ncbView = new NcbView();
                NCBInfoView ncbInfoView = new NCBInfoView();
                List<AccountInfoName> accountInfoNameList = new ArrayList<AccountInfoName>();
                List<AccountInfoId> accountInfoIdList = new ArrayList<AccountInfoId>();

                //get account info from NCRSModel
                NCRSModel ncrsModel = responseNCRSModel.getNcrsModel();
                AccountInfoName accountInfoName = new AccountInfoName();
                AccountInfoId accountInfoId = new AccountInfoId();

                accountInfoName.setNameTh(ncrsModel.getFirstName());
                accountInfoName.setSurnameTh(ncrsModel.getLastName());
                accountInfoId.setIdNumber(ncrsModel.getCitizenId());
                if (ncrsModel.getIdType().equalsIgnoreCase("01")) {
                    accountInfoId.setDocumentType(DocumentType.CITIZEN_ID);
                } else {
                    accountInfoId.setDocumentType(DocumentType.PASSPORT);
                }
                accountInfoNameList.add(accountInfoName);
                accountInfoIdList.add(accountInfoId);
                ncbView.setIdNumber(responseNCRSModel.getIdNumber());
                if (responseNCRSModel.getActionResult() == ActionResult.SUCCESS) {
                    ncbView.setResult(ActionResult.SUCCESS);

                    //Transform NCB Account Logic
                    if (responseNCRSModel.getResponseModel() != null) {
                        NCRSResponseModel ncrsResponseModel = responseNCRSModel.getResponseModel();
                        if (ncrsResponseModel.getBodyModel() != null && ncrsResponseModel.getBodyModel().getTransaction() != null) {
                            TUEFResponseModel tuefResponseModel = null;
                            if (ncrsResponseModel.getBodyModel().getTransaction().getTuefresponse() != null) {
                                tuefResponseModel = ncrsResponseModel.getBodyModel().getTransaction().getTuefresponse();
                            }
                            if (tuefResponseModel != null && tuefResponseModel.getSubject() != null && tuefResponseModel.getSubject().size() > 0) {
                                List<SubjectAccountModel> subjectAccountModelResults = new ArrayList<SubjectAccountModel>();
                                List<EnquiryModel> enquiryModelResults = new ArrayList<EnquiryModel>();

                                for (SubjectModel subjectModel : tuefResponseModel.getSubject()) {

                                    //get Name and Id information for send csi
                                    if (subjectModel.getName() != null && subjectModel.getName().size() > 0) {
                                        for (SubjectNameModel subjectNameModel : subjectModel.getName()) {
                                            AccountInfoName ncbAccountInfoName = new AccountInfoName();
                                            ncbAccountInfoName.setNameTh(subjectNameModel.getFirstname());
                                            ncbAccountInfoName.setSurnameTh(subjectNameModel.getFamilyname());
                                            accountInfoNameList.add(ncbAccountInfoName);
                                        }
                                    }

                                    if (subjectModel.getId() != null && subjectModel.getId().size() > 0) {
                                        for (SubjectIdModel subjectIdModel : subjectModel.getId()) {
                                            if (subjectIdModel.getIdtype().equalsIgnoreCase("01") && subjectIdModel.getIdtype().equalsIgnoreCase("07")) {
                                                AccountInfoId ncbAccountInfoId = new AccountInfoId();
                                                if (subjectIdModel.getIdtype().equalsIgnoreCase("01")) {
                                                    ncbAccountInfoId.setDocumentType(DocumentType.CITIZEN_ID);
                                                } else {
                                                    ncbAccountInfoId.setDocumentType(DocumentType.PASSPORT);
                                                }
                                                ncbAccountInfoId.setIdNumber(subjectIdModel.getIdnumber());
                                                accountInfoIdList.add(ncbAccountInfoId);
                                            }
                                        }
                                    }

                                    //get Account for all subject
                                    List<SubjectAccountModel> subjectAccountModels = subjectModel.getAccount();
                                    if (subjectAccountModels != null && subjectAccountModels.size() > 0) {
                                        for (SubjectAccountModel subjectAccountModel : subjectAccountModels) {
                                            subjectAccountModelResults.add(subjectAccountModel);
                                        }
                                    }

                                    //get Enquiry for all subject
                                    List<EnquiryModel> enquiryModels = subjectModel.getEnquiry();
                                    if (enquiryModels != null && enquiryModels.size() > 0) {
                                        for (EnquiryModel enquiryModel : enquiryModels) {
                                            if (enquiryModel != null && enquiryModel.getEnqpurpose() != null && enquiryModel.getEnqpurpose().equals(ENQ_PURPOSE_IND)) {
                                                enquiryModelResults.add(enquiryModel);
                                            }
                                        }
                                    }
                                }

                                if (subjectAccountModelResults.size() > 0) {
                                    // transform all account from all subject to ncb detail and summary
                                    ncbDetailViews = new ArrayList<NCBDetailView>();

                                    //declare field for summary data
                                    String currentWorstPaymentStatus = null;
                                    String worstPaymentStatus = null;
                                    boolean isNPLTMB = false;
                                    boolean isNPLOther = false;
                                    String lastNPLDateTMB = null;
                                    String lastNPLDateOther = null;
                                    boolean isTDRTMB = false;
                                    boolean isTDROther = false;
                                    String lastTDRDateTMB = null;
                                    String lastTDRDateOther = null;
                                    boolean isValidPayment = true;
                                    String lastAsOfDate = null;

                                    //Check lastAsOfDate
                                    for(SubjectAccountModel subjectAccountModel : subjectAccountModelResults){
                                        if(Util.isNull(lastAsOfDate)){
                                            lastAsOfDate = subjectAccountModel.getAsofdate() != null ? subjectAccountModel.getAsofdate() : null;
                                        }else{
                                            if(subjectAccountModel.getAsofdate() != null){
                                                if(compareDateYYYMMDD(lastAsOfDate, subjectAccountModel.getAsofdate())){
                                                    lastAsOfDate = subjectAccountModel.getAsofdate();
                                                }
                                            }
                                        }
                                    }

                                    for(SubjectAccountModel subjectAccountModel : subjectAccountModelResults){
                                        isValidPayment = isValidPaymentPatternIndividual(subjectAccountModel, lastAsOfDate);
                                        log.debug("isValidPayment {}",isValidPayment);
                                        if(!isValidPayment){
                                            break;
                                        }
                                    }

                                    if(isValidPayment){
                                        for (SubjectAccountModel subjectAccountModel : subjectAccountModelResults) {
                                            //check asOfDate < 12 Month?
                                            if (!isInMonthPeriodYYYYMMDD(subjectAccountModel.getPaymtdate01(), lastAsOfDate, TWELVE_MONTH)) {
                                                continue;
                                            }

                                            boolean isTMBAccount = false;
                                            NCBDetailView ncbDetailView = new NCBDetailView();
                                            //set accountType
                                            AccountType accountType = accountTypeDAO.getIndividualByCode(subjectAccountModel.getAccounttype());
                                            ncbDetailView.setAccountType(accountType);
                                            //set tmb account
                                            ncbDetailView.setTMBAccount(0);
                                            if (subjectAccountModel.getShortname().equals(TMB_BANK)) { //todo: change to master
                                                ncbDetailView.setTMBAccount(1);
                                                isTMBAccount = true;
                                            }
                                            //set account status
                                            AccountStatus accountStatus = accountStatusDAO.getIndividualByCode(subjectAccountModel.getAccountstatus());
                                            ncbDetailView.setAccountStatus(accountStatus);
                                            //set date of info
                                            ncbDetailView.setDateOfInfo(Util.strYYYYMMDDtoDateFormat(subjectAccountModel.getAsofdate()));
                                            //set open date
                                            ncbDetailView.setAccountOpenDate(Util.strYYYYMMDDtoDateFormat(subjectAccountModel.getOpendate()));
                                            //set credit limit
                                            if (!Util.isEmpty(subjectAccountModel.getCreditlimit())) {
                                                ncbDetailView.setLimit(new BigDecimal(subjectAccountModel.getCreditlimit()));
                                            }
                                            //set outstanding amount
                                            if (!Util.isEmpty(subjectAccountModel.getAmountowed())) {
                                                ncbDetailView.setOutstanding(new BigDecimal(subjectAccountModel.getAmountowed()));
                                            }
                                            //set installment
                                            if (!Util.isEmpty(subjectAccountModel.getInstallmentamount())) {
                                                ncbDetailView.setInstallment(new BigDecimal(subjectAccountModel.getInstallmentamount()));
                                            }
                                            //set restructure date
                                            log.debug("subjectAccountModel.getLastrestructureddate() : {}", subjectAccountModel.getLastrestructureddate());
                                            if (!Util.isEmpty(subjectAccountModel.getLastrestructureddate())) {
                                                ncbDetailView.setDateOfDebtRestructuring(Util.strYYYYMMDDtoDateFormat(subjectAccountModel.getLastrestructureddate()));
                                                //get TDR last date
                                                if (isTMBAccount) {
                                                    isTDRTMB = true;
                                                    if (!Util.isEmpty(lastTDRDateTMB)) {
                                                        lastTDRDateTMB = subjectAccountModel.getCloseddate();
                                                    } else {
                                                        lastTDRDateTMB = getLastDateYYYYMMDD(lastTDRDateTMB, subjectAccountModel.getCloseddate());
                                                    }
                                                } else {
                                                    isTDROther = true;
                                                    if (!Util.isEmpty(lastTDRDateOther)) {
                                                        lastTDRDateOther = subjectAccountModel.getCloseddate();
                                                    } else {
                                                        lastTDRDateOther = getLastDateYYYYMMDD(lastTDRDateOther, subjectAccountModel.getCloseddate());
                                                    }
                                                }
                                                log.debug("isTDRTMB : {}, isTDROther : {}, lastTDRDateTMB : {}, lastTDRDateOther : {}", isTDRTMB, isTDROther, lastTDRDateTMB, lastTDRDateOther);
                                            } else {
                                                ncbDetailView.setDateOfDebtRestructuring(null);
                                            }
                                            //set current payment
                                            SettlementStatus settlementStatus = new SettlementStatus();
                                            if (!Util.isEmpty(subjectAccountModel.getPaymt01())) {
                                                settlementStatus = settlementStatusDAO.getIndividualByCode(subjectAccountModel.getPaymt01());
                                            }
                                            ncbDetailView.setCurrentPayment(settlementStatus);
                                            if (!Util.isEmpty(currentWorstPaymentStatus)) {
                                                currentWorstPaymentStatus = getWorstCode(subjectAccountModel.getPaymt01(), currentWorstPaymentStatus);
                                            } else {
                                                if(!isIgnoreCode(subjectAccountModel.getPaymt01()))
                                                    currentWorstPaymentStatus = subjectAccountModel.getPaymt01();
                                            }

                                            //set history payment
                                            ncbDetailView.setHistoryPayment(settlementStatus);

                                            //check for last 6,12 months for get worst payment, calculate number of outstanding and number of over limit
                                            String worstCode = null;
                                            int numberOfOutStandingPayment = 0;
                                            int numberOfOverLimit = 0;
                                            String startAccountDate = subjectAccountModel.getPaymt01() != null ? subjectAccountModel.getAsofdate() : "";
                                            if (!Util.isEmpty(subjectAccountModel.getAccounttype()) && subjectAccountModel.getAccounttype().equals(ACCOUNT_TYPE_OD_IND)) {
                                                if (isInMonthPeriodYYYYMMDD(subjectAccountModel.getPaymtdate01(), lastAsOfDate, TWELVE_MONTH)) {
                                                    if (isOverLimit(subjectAccountModel.getPaymt01())) {
                                                        numberOfOverLimit++;
                                                    }
                                                    if (isInMonthPeriodYYYYMMDD(subjectAccountModel.getPaymtdate01(), lastAsOfDate, SIX_MONTH)) {
                                                        if(!isIgnoreCode(subjectAccountModel.getPaymt01())){
                                                            worstCode = subjectAccountModel.getPaymt01();
                                                        }
                                                        if (isOutStandingPayment(subjectAccountModel.getPaymt01())) {
                                                            numberOfOutStandingPayment++;
                                                        }
                                                        if (isNPLIndividual(subjectAccountModel.getPaymt01())) {
                                                            if (isTMBAccount) {
                                                                isNPLTMB = true;
                                                                if (Util.isEmpty(lastNPLDateTMB)) {
                                                                    lastNPLDateTMB = subjectAccountModel.getPaymtdate01();
                                                                } else {
                                                                    lastNPLDateTMB = getLastDateYYYYMMDD(lastNPLDateTMB, subjectAccountModel.getPaymtdate01());
                                                                }
                                                            } else {
                                                                isNPLOther = true;
                                                                if (Util.isEmpty(lastNPLDateOther)) {
                                                                    lastNPLDateOther = subjectAccountModel.getPaymtdate01();
                                                                } else {
                                                                    lastNPLDateOther = getLastDateYYYYMMDD(lastNPLDateOther, subjectAccountModel.getPaymtdate01());
                                                                }
                                                            }
                                                        }
                                                    }
                                                    if (isInMonthPeriodYYYYMMDD(subjectAccountModel.getPaymtdate02(), lastAsOfDate, TWELVE_MONTH)) {
                                                        if (isOverLimit(subjectAccountModel.getPaymt02())) {
                                                            numberOfOverLimit++;
                                                        }
                                                        if (isInMonthPeriodYYYYMMDD(subjectAccountModel.getPaymtdate02(), lastAsOfDate, SIX_MONTH)) {
                                                            if(!Util.isEmpty(worstCode)){
                                                                worstCode = getWorstCode(subjectAccountModel.getPaymt02(), worstCode);
                                                            } else {
                                                                if(!isIgnoreCode(subjectAccountModel.getPaymt02())){
                                                                    worstCode = subjectAccountModel.getPaymt02();
                                                                }
                                                            }

                                                            if (isOutStandingPayment(subjectAccountModel.getPaymt02())) {
                                                                numberOfOutStandingPayment++;
                                                            }
                                                            if (isNPLIndividual(subjectAccountModel.getPaymt02())) {
                                                                if (isTMBAccount) {
                                                                    isNPLTMB = true;
                                                                    if (Util.isEmpty(lastNPLDateTMB)) {
                                                                        lastNPLDateTMB = subjectAccountModel.getPaymtdate02();
                                                                    } else {
                                                                        lastNPLDateTMB = getLastDateYYYYMMDD(lastNPLDateTMB, subjectAccountModel.getPaymtdate02());
                                                                    }
                                                                } else {
                                                                    isNPLOther = true;
                                                                    if (Util.isEmpty(lastNPLDateOther)) {
                                                                        lastNPLDateOther = subjectAccountModel.getPaymtdate02();
                                                                    } else {
                                                                        lastNPLDateOther = getLastDateYYYYMMDD(lastNPLDateOther, subjectAccountModel.getPaymtdate02());
                                                                    }
                                                                }
                                                            }
                                                        }
                                                        if (isInMonthPeriodYYYYMMDD(subjectAccountModel.getPaymtdate03(), lastAsOfDate, TWELVE_MONTH)) {
                                                            if (isOverLimit(subjectAccountModel.getPaymt03())) {
                                                                numberOfOverLimit++;
                                                            }
                                                            if (isInMonthPeriodYYYYMMDD(subjectAccountModel.getPaymtdate03(), lastAsOfDate, SIX_MONTH)) {
                                                                if(!Util.isEmpty(worstCode)){
                                                                    worstCode = getWorstCode(subjectAccountModel.getPaymt03(), worstCode);
                                                                } else {
                                                                    if(!isIgnoreCode(subjectAccountModel.getPaymt03())){
                                                                        worstCode = subjectAccountModel.getPaymt03();
                                                                    }
                                                                }

                                                                if (isOutStandingPayment(subjectAccountModel.getPaymt03())) {
                                                                    numberOfOutStandingPayment++;
                                                                }
                                                                if (isNPLIndividual(subjectAccountModel.getPaymt03())) {
                                                                    if (isTMBAccount) {
                                                                        isNPLTMB = true;
                                                                        if (Util.isEmpty(lastNPLDateTMB)) {
                                                                            lastNPLDateTMB = subjectAccountModel.getPaymtdate03();
                                                                        } else {
                                                                            lastNPLDateTMB = getLastDateYYYYMMDD(lastNPLDateTMB, subjectAccountModel.getPaymtdate03());
                                                                        }
                                                                    } else {
                                                                        isNPLOther = true;
                                                                        if (Util.isEmpty(lastNPLDateOther)) {
                                                                            lastNPLDateOther = subjectAccountModel.getPaymtdate03();
                                                                        } else {
                                                                            lastNPLDateOther = getLastDateYYYYMMDD(lastNPLDateOther, subjectAccountModel.getPaymtdate03());
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                            if (isInMonthPeriodYYYYMMDD(subjectAccountModel.getPaymtdate04(), lastAsOfDate, TWELVE_MONTH)) {
                                                                if (isOverLimit(subjectAccountModel.getPaymt04())) {
                                                                    numberOfOverLimit++;
                                                                }
                                                                if (isInMonthPeriodYYYYMMDD(subjectAccountModel.getPaymtdate04(), lastAsOfDate, SIX_MONTH)) {
                                                                    if(!Util.isEmpty(worstCode)){
                                                                        worstCode = getWorstCode(subjectAccountModel.getPaymt04(), worstCode);
                                                                    } else {
                                                                        if(!isIgnoreCode(subjectAccountModel.getPaymt04())){
                                                                            worstCode = subjectAccountModel.getPaymt04();
                                                                        }
                                                                    }
                                                                    if (isOutStandingPayment(subjectAccountModel.getPaymt04())) {
                                                                        numberOfOutStandingPayment++;
                                                                    }
                                                                    if (isNPLIndividual(subjectAccountModel.getPaymt04())) {
                                                                        if (isTMBAccount) {
                                                                            isNPLTMB = true;
                                                                            if (Util.isEmpty(lastNPLDateTMB)) {
                                                                                lastNPLDateTMB = subjectAccountModel.getPaymtdate04();
                                                                            } else {
                                                                                lastNPLDateTMB = getLastDateYYYYMMDD(lastNPLDateTMB, subjectAccountModel.getPaymtdate04());
                                                                            }
                                                                        } else {
                                                                            isNPLOther = true;
                                                                            if (Util.isEmpty(lastNPLDateOther)) {
                                                                                lastNPLDateOther = subjectAccountModel.getPaymtdate04();
                                                                            } else {
                                                                                lastNPLDateOther = getLastDateYYYYMMDD(lastNPLDateOther, subjectAccountModel.getPaymtdate04());
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                                if (isInMonthPeriodYYYYMMDD(subjectAccountModel.getPaymtdate05(), lastAsOfDate, TWELVE_MONTH)) {
                                                                    if (isOverLimit(subjectAccountModel.getPaymt05())) {
                                                                        numberOfOverLimit++;
                                                                    }
                                                                    if (isInMonthPeriodYYYYMMDD(subjectAccountModel.getPaymtdate05(), lastAsOfDate, SIX_MONTH)) {
                                                                        if(!Util.isEmpty(worstCode)){
                                                                            worstCode = getWorstCode(subjectAccountModel.getPaymt05(), worstCode);
                                                                        } else {
                                                                            if(!isIgnoreCode(subjectAccountModel.getPaymt05())){
                                                                                worstCode = subjectAccountModel.getPaymt05();
                                                                            }
                                                                        }
                                                                        if (isOutStandingPayment(subjectAccountModel.getPaymt05())) {
                                                                            numberOfOutStandingPayment++;
                                                                        }
                                                                        if (isNPLIndividual(subjectAccountModel.getPaymt05())) {
                                                                            if (isTMBAccount) {
                                                                                isNPLTMB = true;
                                                                                if (Util.isEmpty(lastNPLDateTMB)) {
                                                                                    lastNPLDateTMB = subjectAccountModel.getPaymtdate05();
                                                                                } else {
                                                                                    lastNPLDateTMB = getLastDateYYYYMMDD(lastNPLDateTMB, subjectAccountModel.getPaymtdate05());
                                                                                }
                                                                            } else {
                                                                                isNPLOther = true;
                                                                                if (Util.isEmpty(lastNPLDateOther)) {
                                                                                    lastNPLDateOther = subjectAccountModel.getPaymtdate05();
                                                                                } else {
                                                                                    lastNPLDateOther = getLastDateYYYYMMDD(lastNPLDateOther, subjectAccountModel.getPaymtdate05());
                                                                                }
                                                                            }
                                                                        }
                                                                    }
                                                                    if (isInMonthPeriodYYYYMMDD(subjectAccountModel.getPaymtdate06(), lastAsOfDate, TWELVE_MONTH)) {
                                                                        if (isOverLimit(subjectAccountModel.getPaymt06())) {
                                                                            numberOfOverLimit++;
                                                                        }
                                                                        if (isInMonthPeriodYYYYMMDD(subjectAccountModel.getPaymtdate06(), lastAsOfDate, SIX_MONTH)) {
                                                                            if(!Util.isEmpty(worstCode)){
                                                                                worstCode = getWorstCode(subjectAccountModel.getPaymt06(), worstCode);
                                                                            } else {
                                                                                if(!isIgnoreCode(subjectAccountModel.getPaymt06())){
                                                                                    worstCode = subjectAccountModel.getPaymt06();
                                                                                }
                                                                            }
                                                                            if (isOutStandingPayment(subjectAccountModel.getPaymt06())) {
                                                                                numberOfOutStandingPayment++;
                                                                            }
                                                                            if (isNPLIndividual(subjectAccountModel.getPaymt06())) {
                                                                                if (isTMBAccount) {
                                                                                    isNPLTMB = true;
                                                                                    if (Util.isEmpty(lastNPLDateTMB)) {
                                                                                        lastNPLDateTMB = subjectAccountModel.getPaymtdate06();
                                                                                    } else {
                                                                                        lastNPLDateTMB = getLastDateYYYYMMDD(lastNPLDateTMB, subjectAccountModel.getPaymtdate06());
                                                                                    }
                                                                                } else {
                                                                                    isNPLOther = true;
                                                                                    if (Util.isEmpty(lastNPLDateOther)) {
                                                                                        lastNPLDateOther = subjectAccountModel.getPaymtdate06();
                                                                                    } else {
                                                                                        lastNPLDateOther = getLastDateYYYYMMDD(lastNPLDateOther, subjectAccountModel.getPaymtdate06());
                                                                                    }
                                                                                }
                                                                            }
                                                                        }
                                                                        if (isInMonthPeriodYYYYMMDD(subjectAccountModel.getPaymtdate07(), lastAsOfDate, TWELVE_MONTH)) {
                                                                            if (isOverLimit(subjectAccountModel.getPaymt07())) {
                                                                                numberOfOverLimit++;
                                                                            }
                                                                            if (isInMonthPeriodYYYYMMDD(subjectAccountModel.getPaymtdate07(), lastAsOfDate, SIX_MONTH)) {
                                                                                if(!Util.isEmpty(worstCode)){
                                                                                    worstCode = getWorstCode(subjectAccountModel.getPaymt07(), worstCode);
                                                                                } else {
                                                                                    if(!isIgnoreCode(subjectAccountModel.getPaymt07())){
                                                                                        worstCode = subjectAccountModel.getPaymt07();
                                                                                    }
                                                                                }
                                                                                if (isOutStandingPayment(subjectAccountModel.getPaymt07())) {
                                                                                    numberOfOutStandingPayment++;
                                                                                }
                                                                                if (isNPLIndividual(subjectAccountModel.getPaymt07())) {
                                                                                    if (isTMBAccount) {
                                                                                        isNPLTMB = true;
                                                                                        if (Util.isEmpty(lastNPLDateTMB)) {
                                                                                            lastNPLDateTMB = subjectAccountModel.getPaymtdate07();
                                                                                        } else {
                                                                                            lastNPLDateTMB = getLastDateYYYYMMDD(lastNPLDateTMB, subjectAccountModel.getPaymtdate07());
                                                                                        }
                                                                                    } else {
                                                                                        isNPLOther = true;
                                                                                        if (Util.isEmpty(lastNPLDateOther)) {
                                                                                            lastNPLDateOther = subjectAccountModel.getPaymtdate07();
                                                                                        } else {
                                                                                            lastNPLDateOther = getLastDateYYYYMMDD(lastNPLDateOther, subjectAccountModel.getPaymtdate07());
                                                                                        }
                                                                                    }
                                                                                }
                                                                            }
                                                                            if (isInMonthPeriodYYYYMMDD(subjectAccountModel.getPaymtdate08(), lastAsOfDate, TWELVE_MONTH)) {
                                                                                if (isOverLimit(subjectAccountModel.getPaymt08())) {
                                                                                    numberOfOverLimit++;
                                                                                }
                                                                                if (isInMonthPeriodYYYYMMDD(subjectAccountModel.getPaymtdate08(), lastAsOfDate, SIX_MONTH)) {
                                                                                    if(!Util.isEmpty(worstCode)){
                                                                                        worstCode = getWorstCode(subjectAccountModel.getPaymt08(), worstCode);
                                                                                    } else {
                                                                                        if(!isIgnoreCode(subjectAccountModel.getPaymt08())){
                                                                                            worstCode = subjectAccountModel.getPaymt08();
                                                                                        }
                                                                                    }
                                                                                    if (isOutStandingPayment(subjectAccountModel.getPaymt08())) {
                                                                                        numberOfOutStandingPayment++;
                                                                                    }
                                                                                    if (isNPLIndividual(subjectAccountModel.getPaymt08())) {
                                                                                        if (isTMBAccount) {
                                                                                            isNPLTMB = true;
                                                                                            if (Util.isEmpty(lastNPLDateTMB)) {
                                                                                                lastNPLDateTMB = subjectAccountModel.getPaymtdate08();
                                                                                            } else {
                                                                                                lastNPLDateTMB = getLastDateYYYYMMDD(lastNPLDateTMB, subjectAccountModel.getPaymtdate08());
                                                                                            }
                                                                                        } else {
                                                                                            isNPLOther = true;
                                                                                            if (Util.isEmpty(lastNPLDateOther)) {
                                                                                                lastNPLDateOther = subjectAccountModel.getPaymtdate08();
                                                                                            } else {
                                                                                                lastNPLDateOther = getLastDateYYYYMMDD(lastNPLDateOther, subjectAccountModel.getPaymtdate08());
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                }
                                                                                if (isInMonthPeriodYYYYMMDD(subjectAccountModel.getPaymtdate09(), lastAsOfDate, TWELVE_MONTH)) {
                                                                                    if (isOverLimit(subjectAccountModel.getPaymt09())) {
                                                                                        numberOfOverLimit++;
                                                                                    }
                                                                                    if (isInMonthPeriodYYYYMMDD(subjectAccountModel.getPaymtdate09(), lastAsOfDate, SIX_MONTH)) {
                                                                                        if(!Util.isEmpty(worstCode)){
                                                                                            worstCode = getWorstCode(subjectAccountModel.getPaymt09(), worstCode);
                                                                                        } else {
                                                                                            if(!isIgnoreCode(subjectAccountModel.getPaymt09())){
                                                                                                worstCode = subjectAccountModel.getPaymt09();
                                                                                            }
                                                                                        }
                                                                                        if (isOutStandingPayment(subjectAccountModel.getPaymt09())) {
                                                                                            numberOfOutStandingPayment++;
                                                                                        }
                                                                                        if (isNPLIndividual(subjectAccountModel.getPaymt09())) {
                                                                                            if (isTMBAccount) {
                                                                                                isNPLTMB = true;
                                                                                                if (Util.isEmpty(lastNPLDateTMB)) {
                                                                                                    lastNPLDateTMB = subjectAccountModel.getPaymtdate09();
                                                                                                } else {
                                                                                                    lastNPLDateTMB = getLastDateYYYYMMDD(lastNPLDateTMB, subjectAccountModel.getPaymtdate09());
                                                                                                }
                                                                                            } else {
                                                                                                isNPLOther = true;
                                                                                                if (Util.isEmpty(lastNPLDateOther)) {
                                                                                                    lastNPLDateOther = subjectAccountModel.getPaymtdate09();
                                                                                                } else {
                                                                                                    lastNPLDateOther = getLastDateYYYYMMDD(lastNPLDateOther, subjectAccountModel.getPaymtdate09());
                                                                                                }
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                    if (isInMonthPeriodYYYYMMDD(subjectAccountModel.getPaymtdate10(), lastAsOfDate, TWELVE_MONTH)) {
                                                                                        if (isOverLimit(subjectAccountModel.getPaymt10())) {
                                                                                            numberOfOverLimit++;
                                                                                        }
                                                                                        if (isInMonthPeriodYYYYMMDD(subjectAccountModel.getPaymtdate10(), lastAsOfDate, SIX_MONTH)) {
                                                                                            if(!Util.isEmpty(worstCode)){
                                                                                                worstCode = getWorstCode(subjectAccountModel.getPaymt10(), worstCode);
                                                                                            } else {
                                                                                                if(!isIgnoreCode(subjectAccountModel.getPaymt10())){
                                                                                                    worstCode = subjectAccountModel.getPaymt10();
                                                                                                }
                                                                                            }
                                                                                            if (isOutStandingPayment(subjectAccountModel.getPaymt10())) {
                                                                                                numberOfOutStandingPayment++;
                                                                                            }
                                                                                            if (isNPLIndividual(subjectAccountModel.getPaymt10())) {
                                                                                                if (isTMBAccount) {
                                                                                                    isNPLTMB = true;
                                                                                                    if (Util.isEmpty(lastNPLDateTMB)) {
                                                                                                        lastNPLDateTMB = subjectAccountModel.getPaymtdate10();
                                                                                                    } else {
                                                                                                        lastNPLDateTMB = getLastDateYYYYMMDD(lastNPLDateTMB, subjectAccountModel.getPaymtdate10());
                                                                                                    }
                                                                                                } else {
                                                                                                    isNPLOther = true;
                                                                                                    if (Util.isEmpty(lastNPLDateOther)) {
                                                                                                        lastNPLDateOther = subjectAccountModel.getPaymtdate10();
                                                                                                    } else {
                                                                                                        lastNPLDateOther = getLastDateYYYYMMDD(lastNPLDateOther, subjectAccountModel.getPaymtdate10());
                                                                                                    }
                                                                                                }
                                                                                            }
                                                                                        }
                                                                                        if (isInMonthPeriodYYYYMMDD(subjectAccountModel.getPaymtdate11(), lastAsOfDate, TWELVE_MONTH)) {
                                                                                            if (isOverLimit(subjectAccountModel.getPaymt11())) {
                                                                                                numberOfOverLimit++;
                                                                                            }
                                                                                            if (isInMonthPeriodYYYYMMDD(subjectAccountModel.getPaymtdate11(), lastAsOfDate, SIX_MONTH)) {
                                                                                                if(!Util.isEmpty(worstCode)){
                                                                                                    worstCode = getWorstCode(subjectAccountModel.getPaymt11(), worstCode);
                                                                                                } else {
                                                                                                    if(!isIgnoreCode(subjectAccountModel.getPaymt11())){
                                                                                                        worstCode = subjectAccountModel.getPaymt11();
                                                                                                    }
                                                                                                }
                                                                                                if (isOutStandingPayment(subjectAccountModel.getPaymt11())) {
                                                                                                    numberOfOutStandingPayment++;
                                                                                                }
                                                                                                if (isNPLIndividual(subjectAccountModel.getPaymt11())) {
                                                                                                    if (isTMBAccount) {
                                                                                                        isNPLTMB = true;
                                                                                                        if (Util.isEmpty(lastNPLDateTMB)) {
                                                                                                            lastNPLDateTMB = subjectAccountModel.getPaymtdate11();
                                                                                                        } else {
                                                                                                            lastNPLDateTMB = getLastDateYYYYMMDD(lastNPLDateTMB, subjectAccountModel.getPaymtdate11());
                                                                                                        }
                                                                                                    } else {
                                                                                                        isNPLOther = true;
                                                                                                        if (Util.isEmpty(lastNPLDateOther)) {
                                                                                                            lastNPLDateOther = subjectAccountModel.getPaymtdate11();
                                                                                                        } else {
                                                                                                            lastNPLDateOther = getLastDateYYYYMMDD(lastNPLDateOther, subjectAccountModel.getPaymtdate11());
                                                                                                        }
                                                                                                    }
                                                                                                }
                                                                                            }
                                                                                            if (isInMonthPeriodYYYYMMDD(subjectAccountModel.getPaymtdate12(), lastAsOfDate, TWELVE_MONTH)) {
                                                                                                if (isOverLimit(subjectAccountModel.getPaymt12())) {
                                                                                                    numberOfOverLimit++;
                                                                                                }
                                                                                                if (isInMonthPeriodYYYYMMDD(subjectAccountModel.getPaymtdate12(), lastAsOfDate, SIX_MONTH)) {
                                                                                                    if(!Util.isEmpty(worstCode)){
                                                                                                        worstCode = getWorstCode(subjectAccountModel.getPaymt12(), worstCode);
                                                                                                    } else {
                                                                                                        if(!isIgnoreCode(subjectAccountModel.getPaymt12())){
                                                                                                            worstCode = subjectAccountModel.getPaymt12();
                                                                                                        }
                                                                                                    }
                                                                                                    if (isOutStandingPayment(subjectAccountModel.getPaymt12())) {
                                                                                                        numberOfOutStandingPayment++;
                                                                                                    }
                                                                                                    if (isNPLIndividual(subjectAccountModel.getPaymt12())) {
                                                                                                        if (isTMBAccount) {
                                                                                                            isNPLTMB = true;
                                                                                                            if (Util.isEmpty(lastNPLDateTMB)) {
                                                                                                                lastNPLDateTMB = subjectAccountModel.getPaymtdate12();
                                                                                                            } else {
                                                                                                                lastNPLDateTMB = getLastDateYYYYMMDD(lastNPLDateTMB, subjectAccountModel.getPaymtdate12());
                                                                                                            }
                                                                                                        } else {
                                                                                                            isNPLOther = true;
                                                                                                            if (Util.isEmpty(lastNPLDateOther)) {
                                                                                                                lastNPLDateOther = subjectAccountModel.getPaymtdate12();
                                                                                                            } else {
                                                                                                                lastNPLDateOther = getLastDateYYYYMMDD(lastNPLDateOther, subjectAccountModel.getPaymtdate12());
                                                                                                            }
                                                                                                        }
                                                                                                    }
                                                                                                }
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                }
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            } else {
                                                if (isInMonthPeriodYYYYMMDD(subjectAccountModel.getPaymtdate01(), lastAsOfDate, TWELVE_MONTH)) {
                                                    if(!isIgnoreCode(subjectAccountModel.getPaymt01())){
                                                        worstCode = subjectAccountModel.getPaymt01();
                                                    }
                                                    if (isOutStandingPayment(subjectAccountModel.getPaymt01())) {
                                                        numberOfOutStandingPayment++;
                                                    }
                                                    if (isOverLimit(subjectAccountModel.getPaymt01())) {
                                                        numberOfOverLimit++;
                                                    }
                                                    if (isNPLIndividual(subjectAccountModel.getPaymt01())) {
                                                        if (isTMBAccount) {
                                                            isNPLTMB = true;
                                                            if (Util.isEmpty(lastNPLDateTMB)) {
                                                                lastNPLDateTMB = subjectAccountModel.getPaymtdate01();
                                                            } else {
                                                                lastNPLDateTMB = getLastDateYYYYMMDD(lastNPLDateTMB, subjectAccountModel.getPaymtdate01());
                                                            }
                                                        } else {
                                                            isNPLOther = true;
                                                            if (Util.isEmpty(lastNPLDateOther)) {
                                                                lastNPLDateOther = subjectAccountModel.getPaymtdate01();
                                                            } else {
                                                                lastNPLDateOther = getLastDateYYYYMMDD(lastNPLDateOther, subjectAccountModel.getPaymtdate01());
                                                            }
                                                        }
                                                    }
                                                    if (isInMonthPeriodYYYYMMDD(subjectAccountModel.getPaymtdate02(), lastAsOfDate, TWELVE_MONTH)) {
                                                        if(!Util.isEmpty(worstCode)){
                                                            worstCode = getWorstCode(subjectAccountModel.getPaymt02(), worstCode);
                                                        } else {
                                                            if(!isIgnoreCode(subjectAccountModel.getPaymt02())){
                                                                worstCode = subjectAccountModel.getPaymt02();
                                                            }
                                                        }
                                                        if (isOutStandingPayment(subjectAccountModel.getPaymt02())) {
                                                            numberOfOutStandingPayment++;
                                                        }
                                                        if (isOverLimit(subjectAccountModel.getPaymt02())) {
                                                            numberOfOverLimit++;
                                                        }
                                                        if (isNPLIndividual(subjectAccountModel.getPaymt02())) {
                                                            if (isTMBAccount) {
                                                                isNPLTMB = true;
                                                                if (Util.isEmpty(lastNPLDateTMB)) {
                                                                    lastNPLDateTMB = subjectAccountModel.getPaymtdate02();
                                                                } else {
                                                                    lastNPLDateTMB = getLastDateYYYYMMDD(lastNPLDateTMB, subjectAccountModel.getPaymtdate02());
                                                                }
                                                            } else {
                                                                isNPLOther = true;
                                                                if (Util.isEmpty(lastNPLDateOther)) {
                                                                    lastNPLDateOther = subjectAccountModel.getPaymtdate02();
                                                                } else {
                                                                    lastNPLDateOther = getLastDateYYYYMMDD(lastNPLDateOther, subjectAccountModel.getPaymtdate02());
                                                                }
                                                            }
                                                        }
                                                        if (isInMonthPeriodYYYYMMDD(subjectAccountModel.getPaymtdate03(), lastAsOfDate, TWELVE_MONTH)) {
                                                            if(!Util.isEmpty(worstCode)){
                                                                worstCode = getWorstCode(subjectAccountModel.getPaymt03(), worstCode);
                                                            } else {
                                                                if(!isIgnoreCode(subjectAccountModel.getPaymt03())){
                                                                    worstCode = subjectAccountModel.getPaymt03();
                                                                }
                                                            }
                                                            if (isOutStandingPayment(subjectAccountModel.getPaymt03())) {
                                                                numberOfOutStandingPayment++;
                                                            }
                                                            if (isOverLimit(subjectAccountModel.getPaymt03())) {
                                                                numberOfOverLimit++;
                                                            }
                                                            if (isNPLIndividual(subjectAccountModel.getPaymt03())) {
                                                                if (isTMBAccount) {
                                                                    isNPLTMB = true;
                                                                    if (Util.isEmpty(lastNPLDateTMB)) {
                                                                        lastNPLDateTMB = subjectAccountModel.getPaymtdate03();
                                                                    } else {
                                                                        lastNPLDateTMB = getLastDateYYYYMMDD(lastNPLDateTMB, subjectAccountModel.getPaymtdate03());
                                                                    }
                                                                } else {
                                                                    isNPLOther = true;
                                                                    if (Util.isEmpty(lastNPLDateOther)) {
                                                                        lastNPLDateOther = subjectAccountModel.getPaymtdate03();
                                                                    } else {
                                                                        lastNPLDateOther = getLastDateYYYYMMDD(lastNPLDateOther, subjectAccountModel.getPaymtdate03());
                                                                    }
                                                                }
                                                            }
                                                            if (isInMonthPeriodYYYYMMDD(subjectAccountModel.getPaymtdate04(), lastAsOfDate, TWELVE_MONTH)) {
                                                                if(!Util.isEmpty(worstCode)){
                                                                    worstCode = getWorstCode(subjectAccountModel.getPaymt04(), worstCode);
                                                                } else {
                                                                    if(!isIgnoreCode(subjectAccountModel.getPaymt04())){
                                                                        worstCode = subjectAccountModel.getPaymt04();
                                                                    }
                                                                }
                                                                if (isOutStandingPayment(subjectAccountModel.getPaymt04())) {
                                                                    numberOfOutStandingPayment++;
                                                                }
                                                                if (isOverLimit(subjectAccountModel.getPaymt04())) {
                                                                    numberOfOverLimit++;
                                                                }
                                                                if (isNPLIndividual(subjectAccountModel.getPaymt04())) {
                                                                    if (isTMBAccount) {
                                                                        isNPLTMB = true;
                                                                        if (Util.isEmpty(lastNPLDateTMB)) {
                                                                            lastNPLDateTMB = subjectAccountModel.getPaymtdate04();
                                                                        } else {
                                                                            lastNPLDateTMB = getLastDateYYYYMMDD(lastNPLDateTMB, subjectAccountModel.getPaymtdate04());
                                                                        }
                                                                    } else {
                                                                        isNPLOther = true;
                                                                        if (Util.isEmpty(lastNPLDateOther)) {
                                                                            lastNPLDateOther = subjectAccountModel.getPaymtdate04();
                                                                        } else {
                                                                            lastNPLDateOther = getLastDateYYYYMMDD(lastNPLDateOther, subjectAccountModel.getPaymtdate04());
                                                                        }
                                                                    }
                                                                }
                                                                if (isInMonthPeriodYYYYMMDD(subjectAccountModel.getPaymtdate05(), lastAsOfDate, TWELVE_MONTH)) {
                                                                    if(!Util.isEmpty(worstCode)){
                                                                        worstCode = getWorstCode(subjectAccountModel.getPaymt05(), worstCode);
                                                                    } else {
                                                                        if(!isIgnoreCode(subjectAccountModel.getPaymt05())){
                                                                            worstCode = subjectAccountModel.getPaymt05();
                                                                        }
                                                                    }
                                                                    if (isOutStandingPayment(subjectAccountModel.getPaymt05())) {
                                                                        numberOfOutStandingPayment++;
                                                                    }
                                                                    if (isOverLimit(subjectAccountModel.getPaymt05())) {
                                                                        numberOfOverLimit++;
                                                                    }
                                                                    if (isNPLIndividual(subjectAccountModel.getPaymt05())) {
                                                                        if (isTMBAccount) {
                                                                            isNPLTMB = true;
                                                                            if (Util.isEmpty(lastNPLDateTMB)) {
                                                                                lastNPLDateTMB = subjectAccountModel.getPaymtdate05();
                                                                            } else {
                                                                                lastNPLDateTMB = getLastDateYYYYMMDD(lastNPLDateTMB, subjectAccountModel.getPaymtdate05());
                                                                            }
                                                                        } else {
                                                                            isNPLOther = true;
                                                                            if (Util.isEmpty(lastNPLDateOther)) {
                                                                                lastNPLDateOther = subjectAccountModel.getPaymtdate05();
                                                                            } else {
                                                                                lastNPLDateOther = getLastDateYYYYMMDD(lastNPLDateOther, subjectAccountModel.getPaymtdate05());
                                                                            }
                                                                        }
                                                                    }
                                                                    if (isInMonthPeriodYYYYMMDD(subjectAccountModel.getPaymtdate06(), lastAsOfDate, TWELVE_MONTH)) {
                                                                        if(!Util.isEmpty(worstCode)){
                                                                            worstCode = getWorstCode(subjectAccountModel.getPaymt06(), worstCode);
                                                                        } else {
                                                                            if(!isIgnoreCode(subjectAccountModel.getPaymt06())){
                                                                                worstCode = subjectAccountModel.getPaymt06();
                                                                            }
                                                                        }
                                                                        if (isOutStandingPayment(subjectAccountModel.getPaymt06())) {
                                                                            numberOfOutStandingPayment++;
                                                                        }
                                                                        if (isOverLimit(subjectAccountModel.getPaymt06())) {
                                                                            numberOfOverLimit++;
                                                                        }
                                                                        if (isNPLIndividual(subjectAccountModel.getPaymt06())) {
                                                                            if (isTMBAccount) {
                                                                                isNPLTMB = true;
                                                                                if (Util.isEmpty(lastNPLDateTMB)) {
                                                                                    lastNPLDateTMB = subjectAccountModel.getPaymtdate06();
                                                                                } else {
                                                                                    lastNPLDateTMB = getLastDateYYYYMMDD(lastNPLDateTMB, subjectAccountModel.getPaymtdate06());
                                                                                }
                                                                            } else {
                                                                                isNPLOther = true;
                                                                                if (Util.isEmpty(lastNPLDateOther)) {
                                                                                    lastNPLDateOther = subjectAccountModel.getPaymtdate06();
                                                                                } else {
                                                                                    lastNPLDateOther = getLastDateYYYYMMDD(lastNPLDateOther, subjectAccountModel.getPaymtdate06());
                                                                                }
                                                                            }
                                                                        }
                                                                        if (isInMonthPeriodYYYYMMDD(subjectAccountModel.getPaymtdate07(), lastAsOfDate, TWELVE_MONTH)) {
                                                                            if(!Util.isEmpty(worstCode)){
                                                                                worstCode = getWorstCode(subjectAccountModel.getPaymt07(), worstCode);
                                                                            } else {
                                                                                if(!isIgnoreCode(subjectAccountModel.getPaymt07())){
                                                                                    worstCode = subjectAccountModel.getPaymt07();
                                                                                }
                                                                            }
                                                                            if (isOutStandingPayment(subjectAccountModel.getPaymt07())) {
                                                                                numberOfOutStandingPayment++;
                                                                            }
                                                                            if (isOverLimit(subjectAccountModel.getPaymt07())) {
                                                                                numberOfOverLimit++;
                                                                            }
                                                                            if (isNPLIndividual(subjectAccountModel.getPaymt07())) {
                                                                                if (isTMBAccount) {
                                                                                    isNPLTMB = true;
                                                                                    if (Util.isEmpty(lastNPLDateTMB)) {
                                                                                        lastNPLDateTMB = subjectAccountModel.getPaymtdate07();
                                                                                    } else {
                                                                                        lastNPLDateTMB = getLastDateYYYYMMDD(lastNPLDateTMB, subjectAccountModel.getPaymtdate07());
                                                                                    }
                                                                                } else {
                                                                                    isNPLOther = true;
                                                                                    if (Util.isEmpty(lastNPLDateOther)) {
                                                                                        lastNPLDateOther = subjectAccountModel.getPaymtdate07();
                                                                                    } else {
                                                                                        lastNPLDateOther = getLastDateYYYYMMDD(lastNPLDateOther, subjectAccountModel.getPaymtdate07());
                                                                                    }
                                                                                }
                                                                            }
                                                                            if (isInMonthPeriodYYYYMMDD(subjectAccountModel.getPaymtdate08(), lastAsOfDate, TWELVE_MONTH)) {
                                                                                if(!Util.isEmpty(worstCode)){
                                                                                    worstCode = getWorstCode(subjectAccountModel.getPaymt08(), worstCode);
                                                                                } else {
                                                                                    if(!isIgnoreCode(subjectAccountModel.getPaymt08())){
                                                                                        worstCode = subjectAccountModel.getPaymt08();
                                                                                    }
                                                                                }
                                                                                if (isOutStandingPayment(subjectAccountModel.getPaymt08())) {
                                                                                    numberOfOutStandingPayment++;
                                                                                }
                                                                                if (isOverLimit(subjectAccountModel.getPaymt08())) {
                                                                                    numberOfOverLimit++;
                                                                                }
                                                                                if (isNPLIndividual(subjectAccountModel.getPaymt08())) {
                                                                                    if (isTMBAccount) {
                                                                                        isNPLTMB = true;
                                                                                        if (Util.isEmpty(lastNPLDateTMB)) {
                                                                                            lastNPLDateTMB = subjectAccountModel.getPaymtdate08();
                                                                                        } else {
                                                                                            lastNPLDateTMB = getLastDateYYYYMMDD(lastNPLDateTMB, subjectAccountModel.getPaymtdate08());
                                                                                        }
                                                                                    } else {
                                                                                        isNPLOther = true;
                                                                                        if (Util.isEmpty(lastNPLDateOther)) {
                                                                                            lastNPLDateOther = subjectAccountModel.getPaymtdate08();
                                                                                        } else {
                                                                                            lastNPLDateOther = getLastDateYYYYMMDD(lastNPLDateOther, subjectAccountModel.getPaymtdate08());
                                                                                        }
                                                                                    }
                                                                                }
                                                                                if (isInMonthPeriodYYYYMMDD(subjectAccountModel.getPaymtdate09(), lastAsOfDate, TWELVE_MONTH)) {
                                                                                    if(!Util.isEmpty(worstCode)){
                                                                                        worstCode = getWorstCode(subjectAccountModel.getPaymt09(), worstCode);
                                                                                    } else {
                                                                                        if(!isIgnoreCode(subjectAccountModel.getPaymt09())){
                                                                                            worstCode = subjectAccountModel.getPaymt09();
                                                                                        }
                                                                                    }
                                                                                    if (isOutStandingPayment(subjectAccountModel.getPaymt09())) {
                                                                                        numberOfOutStandingPayment++;
                                                                                    }
                                                                                    if (isOverLimit(subjectAccountModel.getPaymt09())) {
                                                                                        numberOfOverLimit++;
                                                                                    }
                                                                                    if (isNPLIndividual(subjectAccountModel.getPaymt09())) {
                                                                                        if (isTMBAccount) {
                                                                                            isNPLTMB = true;
                                                                                            if (Util.isEmpty(lastNPLDateTMB)) {
                                                                                                lastNPLDateTMB = subjectAccountModel.getPaymtdate09();
                                                                                            } else {
                                                                                                lastNPLDateTMB = getLastDateYYYYMMDD(lastNPLDateTMB, subjectAccountModel.getPaymtdate09());
                                                                                            }
                                                                                        } else {
                                                                                            isNPLOther = true;
                                                                                            if (Util.isEmpty(lastNPLDateOther)) {
                                                                                                lastNPLDateOther = subjectAccountModel.getPaymtdate09();
                                                                                            } else {
                                                                                                lastNPLDateOther = getLastDateYYYYMMDD(lastNPLDateOther, subjectAccountModel.getPaymtdate09());
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                    if (isInMonthPeriodYYYYMMDD(subjectAccountModel.getPaymtdate10(), lastAsOfDate, TWELVE_MONTH)) {
                                                                                        if(!Util.isEmpty(worstCode)){
                                                                                            worstCode = getWorstCode(subjectAccountModel.getPaymt10(), worstCode);
                                                                                        } else {
                                                                                            if(!isIgnoreCode(subjectAccountModel.getPaymt10())){
                                                                                                worstCode = subjectAccountModel.getPaymt10();
                                                                                            }
                                                                                        }
                                                                                        if (isOutStandingPayment(subjectAccountModel.getPaymt10())) {
                                                                                            numberOfOutStandingPayment++;
                                                                                        }
                                                                                        if (isOverLimit(subjectAccountModel.getPaymt10())) {
                                                                                            numberOfOverLimit++;
                                                                                        }
                                                                                        if (isNPLIndividual(subjectAccountModel.getPaymt10())) {
                                                                                            if (isTMBAccount) {
                                                                                                isNPLTMB = true;
                                                                                                if (Util.isEmpty(lastNPLDateTMB)) {
                                                                                                    lastNPLDateTMB = subjectAccountModel.getPaymtdate10();
                                                                                                } else {
                                                                                                    lastNPLDateTMB = getLastDateYYYYMMDD(lastNPLDateTMB, subjectAccountModel.getPaymtdate10());
                                                                                                }
                                                                                            } else {
                                                                                                isNPLOther = true;
                                                                                                if (Util.isEmpty(lastNPLDateOther)) {
                                                                                                    lastNPLDateOther = subjectAccountModel.getPaymtdate10();
                                                                                                } else {
                                                                                                    lastNPLDateOther = getLastDateYYYYMMDD(lastNPLDateOther, subjectAccountModel.getPaymtdate10());
                                                                                                }
                                                                                            }
                                                                                        }
                                                                                        if (isInMonthPeriodYYYYMMDD(subjectAccountModel.getPaymtdate11(), lastAsOfDate, TWELVE_MONTH)) {
                                                                                            if(!Util.isEmpty(worstCode)){
                                                                                                worstCode = getWorstCode(subjectAccountModel.getPaymt11(), worstCode);
                                                                                            } else {
                                                                                                if(!isIgnoreCode(subjectAccountModel.getPaymt11())){
                                                                                                    worstCode = subjectAccountModel.getPaymt11();
                                                                                                }
                                                                                            }
                                                                                            if (isOutStandingPayment(subjectAccountModel.getPaymt11())) {
                                                                                                numberOfOutStandingPayment++;
                                                                                            }
                                                                                            if (isOverLimit(subjectAccountModel.getPaymt11())) {
                                                                                                numberOfOverLimit++;
                                                                                            }
                                                                                            if (isNPLIndividual(subjectAccountModel.getPaymt11())) {
                                                                                                if (isTMBAccount) {
                                                                                                    isNPLTMB = true;
                                                                                                    if (Util.isEmpty(lastNPLDateTMB)) {
                                                                                                        lastNPLDateTMB = subjectAccountModel.getPaymtdate11();
                                                                                                    } else {
                                                                                                        lastNPLDateTMB = getLastDateYYYYMMDD(lastNPLDateTMB, subjectAccountModel.getPaymtdate11());
                                                                                                    }
                                                                                                } else {
                                                                                                    isNPLOther = true;
                                                                                                    if (Util.isEmpty(lastNPLDateOther)) {
                                                                                                        lastNPLDateOther = subjectAccountModel.getPaymtdate11();
                                                                                                    } else {
                                                                                                        lastNPLDateOther = getLastDateYYYYMMDD(lastNPLDateOther, subjectAccountModel.getPaymtdate11());
                                                                                                    }
                                                                                                }
                                                                                            }
                                                                                            if (isInMonthPeriodYYYYMMDD(subjectAccountModel.getPaymtdate12(), lastAsOfDate, 12)) {
                                                                                                if(!Util.isEmpty(worstCode)){
                                                                                                    worstCode = getWorstCode(subjectAccountModel.getPaymt12(), worstCode);
                                                                                                } else {
                                                                                                    if(!isIgnoreCode(subjectAccountModel.getPaymt12())){
                                                                                                        worstCode = subjectAccountModel.getPaymt12();
                                                                                                    }
                                                                                                }
                                                                                                if (isOutStandingPayment(subjectAccountModel.getPaymt12())) {
                                                                                                    numberOfOutStandingPayment++;
                                                                                                }
                                                                                                if (isOverLimit(subjectAccountModel.getPaymt12())) {
                                                                                                    numberOfOverLimit++;
                                                                                                }
                                                                                                if (isNPLIndividual(subjectAccountModel.getPaymt12())) {
                                                                                                    if (isTMBAccount) {
                                                                                                        isNPLTMB = true;
                                                                                                        if (Util.isEmpty(lastNPLDateTMB)) {
                                                                                                            lastNPLDateTMB = subjectAccountModel.getPaymtdate12();
                                                                                                        } else {
                                                                                                            lastNPLDateTMB = getLastDateYYYYMMDD(lastNPLDateTMB, subjectAccountModel.getPaymtdate12());
                                                                                                        }
                                                                                                    } else {
                                                                                                        isNPLOther = true;
                                                                                                        if (Util.isEmpty(lastNPLDateOther)) {
                                                                                                            lastNPLDateOther = subjectAccountModel.getPaymtdate12();
                                                                                                        } else {
                                                                                                            lastNPLDateOther = getLastDateYYYYMMDD(lastNPLDateOther, subjectAccountModel.getPaymtdate12());
                                                                                                        }
                                                                                                    }
                                                                                                }
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                }
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                            //set worst payment status
                                            log.debug("Account : {} worstCode : {}",ncbDetailView.getAccountName(), worstCode);
                                            SettlementStatus historySettlementStatus = new SettlementStatus();
                                            if (!Util.isEmpty(worstCode)) {
                                                historySettlementStatus = settlementStatusDAO.getIndividualByCode(worstCode);
                                            }
                                            ncbDetailView.setHistoryPayment(historySettlementStatus);
                                            if (!Util.isEmpty(worstPaymentStatus)) {
                                                worstPaymentStatus = getWorstCode(subjectAccountModel.getPaymt01(), worstPaymentStatus);
                                            } else {
                                                if(!isIgnoreCode(subjectAccountModel.getPaymt01())){
                                                    worstPaymentStatus = subjectAccountModel.getPaymt01();
                                                }
                                            }
                                            //set number of outstanding payment
                                            ncbDetailView.setNoOfOutstandingPaymentIn12months(numberOfOutStandingPayment);
                                            //set number of over limit
                                            ncbDetailView.setNoOfOverLimit(numberOfOverLimit);

                                            //add ncbDetailView to ncbDetailViewList
                                            log.debug("Add ncbDetailView to list : {}", ncbDetailView);
                                            ncbDetailViews.add(ncbDetailView);
                                        }
                                    }

                                    if(!isValidPayment){
                                        log.debug("unexpected value getting from NCRS : settlement status");
                                        String reason = "Unexpected value getting from NCRS : settlement status";
                                        ncbView.setResult(ActionResult.FAILED);
                                        ncbView.setReason(reason);
                                        ncbViews.add(ncbView);
                                        continue;
                                    }

                                    //set NCBInfoView
                                    if (!Util.isEmpty(currentWorstPaymentStatus)) {
                                        SettlementStatus currentWorstSettlementStatus = settlementStatusDAO.getIndividualByCode(currentWorstPaymentStatus);
                                        if (currentWorstSettlementStatus != null) {
                                            ncbInfoView.setCurrentPaymentType(currentWorstSettlementStatus.getName());
                                        }
                                    }
                                    if (!Util.isEmpty(worstPaymentStatus)) {
                                        SettlementStatus worstSettlementStatus = settlementStatusDAO.getIndividualByCode(worstPaymentStatus);
                                        if (worstSettlementStatus != null) {
                                            ncbInfoView.setHistoryPaymentType(worstSettlementStatus.getName());
                                        }
                                    }

                                    if (isNPLTMB || isNPLOther) {
                                        ncbInfoView.setNplFlag(RadioValue.YES.value()); //true
                                        if (isNPLTMB) {
                                            ncbInfoView.setNplTMBFlag(true);
                                            if(!Util.isEmpty(lastNPLDateTMB)){
                                                if(lastNPLDateTMB.length()>=6){
                                                    int month = Integer.parseInt(lastNPLDateTMB.substring(4,6));
                                                    int year = Integer.parseInt(lastNPLDateTMB.substring(0,4));
                                                    ncbInfoView.setNplTMBMonth(month);
                                                    ncbInfoView.setNplTMBYear(year);
                                                }
                                            }
                                        }
                                        if (isNPLOther) {
                                            ncbInfoView.setNplOtherFlag(true);
                                            if(!Util.isEmpty(lastNPLDateOther)){
                                                if(lastNPLDateOther.length()>=6){
                                                    int month = Integer.parseInt(lastNPLDateOther.substring(4,6));
                                                    int year = Integer.parseInt(lastNPLDateOther.substring(0,4));
                                                    ncbInfoView.setNplOtherMonth(month);
                                                    ncbInfoView.setNplOtherYear(year);
                                                }
                                            }
                                        }
                                    } else {
                                        ncbInfoView.setNplFlag(RadioValue.NO.value());
                                    }

                                    if (isTDRTMB || isTDROther) {
                                        ncbInfoView.setTdrFlag(RadioValue.YES.value()); //true
                                        if (isTDRTMB) {
                                            ncbInfoView.setTdrTMBFlag(true);
                                            if(!Util.isEmpty(lastTDRDateTMB)){
                                                if(lastTDRDateTMB.length()>=6){
                                                    int month = Integer.parseInt(lastTDRDateTMB.substring(4,6));
                                                    int year = Integer.parseInt(lastTDRDateTMB.substring(0,4));
                                                    ncbInfoView.setTdrTMBMonth(month);
                                                    ncbInfoView.setTdrTMBYear(year);
                                                }
                                            }
                                        }
                                        if (isTDROther) {
                                            ncbInfoView.setTdrOtherFlag(true);
                                            if(!Util.isEmpty(lastTDRDateOther)){
                                                if(lastTDRDateOther.length()>=6){
                                                    int month = Integer.parseInt(lastTDRDateOther.substring(4,6));
                                                    int year = Integer.parseInt(lastTDRDateOther.substring(0,4));
                                                    ncbInfoView.setTdrOtherMonth(month);
                                                    ncbInfoView.setTdrOtherYear(year);
                                                }
                                            }
                                        }
                                    } else {
                                        ncbInfoView.setTdrFlag(RadioValue.NO.value());
                                    }
                                } else {
                                    //no ncb detail
                                }

                                //get enquiry amount
                                int enquiryTime = 0;
                                if (enquiryModelResults.size() > 0) {
                                    Map<String, String> enquiryMap = new HashMap<String, String>();  //for check duplicate enquiry time
                                    //get number of enquiry in last 6 months
                                    for (EnquiryModel enquiryModel : enquiryModelResults) {
                                        if (isInMonthPeriodYYYYMMDD(enquiryModel.getEnqdate(), SIX_MONTH)) {
                                            enquiryMap.put(enquiryModel.getEnqdate().concat(enquiryModel.getEnqtime()), enquiryModel.getEnqamount());
                                        }
                                    }

                                    enquiryTime = enquiryMap.size();
                                }
                                ncbInfoView.setCheckIn6Month(enquiryTime);
                            }

                            //TODO: add more data (hidden field) for NCBInfoView (name, address, marital status, enquiry date, last as of date, tracking id) here
                            //ncbInfoView
                        }
                    }
                    ncbView.setNcbInfoView(ncbInfoView);
                    ncbView.setNCBDetailViews(ncbDetailViews);
                    ncbView.setAccountInfoIdList(accountInfoIdList);
                    ncbView.setAccountInfoNameList(accountInfoNameList);
                } else {
                    ncbView.setResult(ActionResult.FAILED);
                    ncbView.setReason(responseNCRSModel.getReason());
                }
                ncbViews.add(ncbView);
            }
        }
        return ncbViews;
    }

    public List<NcbView> transformJuristic(List<NCCRSOutputModel> responseNCCRSModels) throws Exception {
        log.debug("NCBBizTransform : transformJuristic ::: responseNCCRSModels : {}");
        List<NcbView> ncbViews = null;
        List<NCBDetailView> ncbDetailViews = null;
        if (responseNCCRSModels != null && responseNCCRSModels.size() > 0) {
            ncbViews = new ArrayList<NcbView>();
            for (NCCRSOutputModel responseNCCRSModel : responseNCCRSModels) {
                log.debug("NCCRSOutputModel : {}", responseNCCRSModel);
                NcbView ncbView = new NcbView();
                NCBInfoView ncbInfoView = new NCBInfoView();
                List<AccountInfoName> accountInfoNameList = new ArrayList<AccountInfoName>();
                List<AccountInfoId> accountInfoIdList = new ArrayList<AccountInfoId>();

                //get account info from NCRSModel
                NCCRSModel nccrsModel = responseNCCRSModel.getNccrsModel();
                AccountInfoName accountInfoName = new AccountInfoName();
                AccountInfoId accountInfoId = new AccountInfoId();

                accountInfoName.setNameTh(nccrsModel.getCompanyName());
                accountInfoId.setIdNumber(nccrsModel.getRegistId());
                accountInfoId.setDocumentType(DocumentType.CORPORATE_ID);
                accountInfoNameList.add(accountInfoName);
                accountInfoIdList.add(accountInfoId);
                ncbView.setIdNumber(responseNCCRSModel.getIdNumber());
                if (responseNCCRSModel.getActionResult() == ActionResult.SUCCESS) {
                    ncbView.setResult(ActionResult.SUCCESS);
                    //Transform NCB Account Logic
                    if (responseNCCRSModel.getResponseModel() != null) {
                        NCCRSResponseModel nccrsResponseModel = responseNCCRSModel.getResponseModel();
                        if (nccrsResponseModel.getBody() != null && nccrsResponseModel.getBody().getTransaction() != null) {
                            H2HResponseModel h2HResponseModel = null;
                            if (nccrsResponseModel.getBody().getTransaction().getH2hresponse() != null) {
                                h2HResponseModel = nccrsResponseModel.getBody().getTransaction().getH2hresponse();
                            }
                            if (h2HResponseModel != null && h2HResponseModel.getSubject() != null) {
                                H2HResponseSubjectModel h2HResponseSubjectModel = h2HResponseModel.getSubject();
                                List<InqHistModel> enquiryModelResults = new ArrayList<InqHistModel>();

                                //get Name and Id information for send csi
                                if (h2HResponseSubjectModel != null && h2HResponseSubjectModel.getProfile() != null) {
                                    ProfileModel profileModel = h2HResponseSubjectModel.getProfile();
                                    AccountInfoName accountInfoName2 = new AccountInfoName();
                                    AccountInfoId accountInfoId2 = new AccountInfoId();

                                    accountInfoName2.setNameTh(profileModel.getThainame());
                                    accountInfoName2.setNameTh(profileModel.getEngname());
                                    accountInfoId2.setIdNumber(profileModel.getRegistid());
                                    accountInfoId2.setDocumentType(DocumentType.CORPORATE_ID);
                                    accountInfoNameList.add(accountInfoName2);
                                    accountInfoIdList.add(accountInfoId2);

                                    if (h2HResponseSubjectModel.getProfile().getAdditional() != null) {
                                        AdditionalModel additionalModel = h2HResponseSubjectModel.getProfile().getAdditional();
                                        if (additionalModel.getName() != null && additionalModel.getName().size() > 0) {
                                            for (ProfileNameModel profileNameModel : additionalModel.getName()) {
                                                AccountInfoName ncbAccountInfoName = new AccountInfoName();
                                                ncbAccountInfoName.setNameTh(profileNameModel.getThainame());
                                                ncbAccountInfoName.setSurnameTh(profileNameModel.getEngname());
                                                accountInfoNameList.add(ncbAccountInfoName);
                                            }
                                        }
                                    }
                                }

                                //get Enquiry for all subject
                                if (h2HResponseSubjectModel != null
                                        && h2HResponseSubjectModel.getInquiryhistories() != null
                                        && h2HResponseSubjectModel.getInquiryhistories().getInqhist() != null
                                        && h2HResponseSubjectModel.getInquiryhistories().getInqhist().size() > 0) {
                                    List<InqHistModel> inqHistModels = h2HResponseSubjectModel.getInquiryhistories().getInqhist();
                                    for (InqHistModel inqHistModel : inqHistModels) {
                                        if (inqHistModel != null && inqHistModel.getInqpurpose() != null && inqHistModel.getInqpurpose().equals(ENQ_PURPOSE_JUR)) {
                                            enquiryModelResults.add(inqHistModel);
                                        }
                                    }
                                }

                                //get list Account for active,closed
                                List<AccountModel> accountModels = new ArrayList<AccountModel>();
                                boolean haveActiveAccountData = false;
                                boolean haveClosedAccountData = false;
                                if (h2HResponseSubjectModel.getActiveaccounts() != null) {
                                    if (h2HResponseSubjectModel.getActiveaccounts().getAccount() != null && h2HResponseSubjectModel.getActiveaccounts().getAccount().size() > 0) {
                                        haveActiveAccountData = true;
                                    }
                                }
                                if (h2HResponseSubjectModel.getClosedaccounts() != null) {
                                    if (h2HResponseSubjectModel.getClosedaccounts().getAccount() != null && h2HResponseSubjectModel.getClosedaccounts().getAccount().size() > 0) {
                                        haveClosedAccountData = true;
                                    }
                                }

                                if (haveActiveAccountData || haveClosedAccountData) {
                                    // transform all account from all subject to ncb detail and summary
                                    ncbDetailViews = new ArrayList<NCBDetailView>();

                                    //declare field for summary data
                                    String currentWorstPaymentStatus = null;
                                    String worstPaymentStatus = null;
                                    boolean isNPLTMB = false;
                                    boolean isNPLOther = false;
                                    String lastNPLDateTMB = null;
                                    String lastNPLDateOther = null;
                                    boolean isTDRTMB = false;
                                    boolean isTDROther = false;
                                    String lastTDRDateTMB = null;
                                    String lastTDRDateOther = null;
                                    boolean isValidPayment = true;
                                    //check for active account
                                    if (haveActiveAccountData) {
                                        for (AccountModel accountModel : h2HResponseSubjectModel.getActiveaccounts().getAccount()) {
                                            boolean isTMBAccount = false;
                                            NCBDetailView ncbDetailView = new NCBDetailView();
                                            if (accountModel.getCreditinfo() != null) {
                                                CreditInfoModel creditInfoModel = accountModel.getCreditinfo();
                                                //set accountType
                                                AccountType accountType = accountTypeDAO.getJuristicByName(creditInfoModel.getCredittype());
                                                ncbDetailView.setAccountType(accountType);
                                                //set tmb account
                                                ncbDetailView.setTMBAccount(0);
                                                if (creditInfoModel.getCreditortype() != null && creditInfoModel.getCreditortype().trim().equals(TMB_BANK_THAI)) {
                                                    ncbDetailView.setTMBAccount(1);
                                                    isTMBAccount = true;
                                                }
                                                //set account status
                                                AccountStatus accountStatus = accountStatusDAO.getJuristicByCode(creditInfoModel.getAccountstatus());
                                                ncbDetailView.setAccountStatus(accountStatus);
                                                //set date of info
                                                ncbDetailView.setDateOfInfo(DateTimeUtil.getLastDayOfMonth(Util.strYYYYMMtoDateFormat(creditInfoModel.getAsofdate())));
                                                //set open date
                                                String[] openDate = Util.splitSpace(creditInfoModel.getOpeneddate());
                                                if (openDate != null && openDate.length > 0) {
                                                    ncbDetailView.setAccountOpenDate(Util.strYYYYMMDDtoDateFormat(openDate[0]));
                                                }
                                                //set credit limit
                                                if (!Util.isEmpty(creditInfoModel.getCreditlimit())) {
                                                    ncbDetailView.setLimit(new BigDecimal(creditInfoModel.getCreditlimit()));
                                                }
                                                //set outstanding amount
                                                if (!Util.isEmpty(creditInfoModel.getOutstanding())) {
                                                    ncbDetailView.setOutstanding(new BigDecimal(creditInfoModel.getOutstanding()));
                                                }
                                                //set installment
                                                if (!Util.isEmpty(creditInfoModel.getInstallmentamount())) {
                                                    ncbDetailView.setInstallment(new BigDecimal(creditInfoModel.getInstallmentamount()));
                                                }
                                                //for calculate brms rules,, add npl flag and tdr flag
                                                ncbDetailView.setNplFlag(RadioValue.NO.value());
                                                //set restructure date
                                                if (!Util.isEmpty(creditInfoModel.getRestructuredate())) {
                                                    String[] reStructureDate = Util.splitSpace(creditInfoModel.getRestructuredate());
                                                    if (reStructureDate != null && reStructureDate.length > 0) {
                                                        ncbDetailView.setDateOfDebtRestructuring(Util.strYYYYMMDDtoDateFormat(reStructureDate[0]));
                                                    }
                                                    //get TDR last date
                                                    if (isTMBAccount) {
                                                        isTDRTMB = true;
                                                        if (!Util.isEmpty(lastTDRDateTMB)) {
                                                            lastTDRDateTMB = creditInfoModel.getCloseddate();
                                                        } else {
                                                            lastTDRDateTMB = getLastDateYYYYMMDD(lastTDRDateTMB, creditInfoModel.getCloseddate());
                                                        }
                                                    } else {
                                                        isTDROther = true;
                                                        if (!Util.isEmpty(lastTDRDateOther)) {
                                                            lastTDRDateOther = creditInfoModel.getCloseddate();
                                                        } else {
                                                            lastTDRDateOther = getLastDateYYYYMMDD(lastTDRDateOther, creditInfoModel.getCloseddate());
                                                        }
                                                    }
                                                }
                                            }

                                            String worstCode = null;
                                            int numberOfOutStandingPayment = 0;
                                            int numberOfOverLimit = 0;
                                            String creditType = "";
                                            if (accountModel.getCredithistories() != null && accountModel.getCredithistories().getCredithist() != null && accountModel.getCredithistories().getCredithist().size() > 0) {
                                                List<CreditHistModel> creditHistModelList = accountModel.getCredithistories().getCredithist();
                                                //set current payment
                                                SettlementStatus settlementStatus = new SettlementStatus();
                                                if (creditHistModelList.get(0) != null && !Util.isEmpty(creditHistModelList.get(0).getDaypastdue())) {
                                                    isValidPayment = isValidPaymentPatternJuristic(creditHistModelList.get(0));
                                                    if(!isValidPayment) {
                                                        break;
                                                    }
                                                    settlementStatus = settlementStatusDAO.getJuristicByCode(creditHistModelList.get(0).getDaypastdue());
                                                }
                                                ncbDetailView.setCurrentPayment(settlementStatus);
                                                if (!Util.isEmpty(currentWorstPaymentStatus)) {
                                                    currentWorstPaymentStatus = getWorstCode(creditHistModelList.get(0).getDaypastdue(), currentWorstPaymentStatus);
                                                } else {
                                                    currentWorstPaymentStatus = creditHistModelList.get(0).getDaypastdue();
                                                }

                                                //check for last 6,12 months for get worst payment, calculate number of outstanding and number of over limit
                                                if (accountModel.getCreditinfo() != null && !Util.isEmpty(accountModel.getCreditinfo().getCredittype())) {
                                                    creditType = accountModel.getCreditinfo().getCredittype();
                                                }
                                                if (!Util.isEmpty(creditType) && creditType.equals(ACCOUNT_TYPE_OD_JUR)) {
                                                    if (isInMonthPeriodYYYYMM(creditHistModelList.get(0).getAsofdate(), TWELVE_MONTH)) {
                                                        for (CreditHistModel creditHistModel : creditHistModelList) {
                                                            if(isOverLimit(creditHistModel.getDaypastdue())){
                                                                numberOfOverLimit++;
                                                            }

                                                            //get worstCode
                                                            if (isInMonthPeriodYYYYMM(creditHistModel.getAsofdate(), SIX_MONTH)) {
                                                                isValidPayment = isValidPaymentPatternJuristic(creditHistModel);
                                                                log.debug("DayPastDue : {}, trim : {}",creditHistModel.getDaypastdue(),creditHistModel.getDaypastdue().trim());
                                                                if(!isValidPayment) {
                                                                    break;
                                                                }
                                                                if (Util.isEmpty(worstCode)) {
                                                                    if(!isIgnoreCode(creditHistModel.getDaypastdue())){
                                                                        worstCode = creditHistModel.getDaypastdue();
                                                                    }
                                                                } else {
                                                                    worstCode = getWorstCode(creditHistModel.getDaypastdue(), worstCode);
                                                                }

                                                                if (isOutStandingPayment(creditHistModel.getDaypastdue())) {
                                                                    numberOfOutStandingPayment++;
                                                                }
                                                                if (isNPLIndividual(creditHistModel.getDaypastdue())) {
                                                                    if (isTMBAccount) {
                                                                        isNPLTMB = true;
                                                                        if (Util.isEmpty(lastNPLDateTMB)) {
                                                                            lastNPLDateTMB = creditHistModel.getAsofdate();
                                                                        } else {
                                                                            lastNPLDateTMB = getLastDateYYYYMM(lastNPLDateTMB, creditHistModel.getAsofdate());
                                                                        }
                                                                    } else {
                                                                        isNPLOther = true;
                                                                        if (Util.isEmpty(lastNPLDateOther)) {
                                                                            lastNPLDateOther = creditHistModel.getAsofdate();
                                                                        } else {
                                                                            lastNPLDateOther = getLastDateYYYYMM(lastNPLDateOther, creditHistModel.getAsofdate());
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }

                                                        if(!isValidPayment) {
                                                            break;
                                                        }
                                                    }
                                                } else {
                                                    if (isInMonthPeriodYYYYMM(creditHistModelList.get(0).getAsofdate(), TWELVE_MONTH)) {
                                                        for (CreditHistModel creditHistModel : creditHistModelList) {
                                                            if (isInMonthPeriodYYYYMM(creditHistModel.getAsofdate(), TWELVE_MONTH)) {
                                                                if(isOverLimit(creditHistModel.getDaypastdue())){
                                                                    numberOfOverLimit++;
                                                                }

                                                                isValidPayment = isValidPaymentPatternJuristic(creditHistModel);
                                                                log.debug("DayPastDue : {}, trim : {}",creditHistModel.getDaypastdue(),creditHistModel.getDaypastdue().trim());
                                                                if(!isValidPayment) {
                                                                    break;
                                                                }

                                                                //get worstCode
                                                                if (Util.isEmpty(worstCode)) {
                                                                    if(!isIgnoreCode(creditHistModel.getDaypastdue())){
                                                                        worstCode = creditHistModel.getDaypastdue();
                                                                    }

                                                                } else {
                                                                    worstCode = getWorstCode(creditHistModel.getDaypastdue(), worstCode);
                                                                }

                                                                if (isOutStandingPayment(creditHistModel.getDaypastdue())) {
                                                                    numberOfOutStandingPayment++;
                                                                }
                                                                if (isNPLIndividual(creditHistModel.getDaypastdue())) {
                                                                    if (isTMBAccount) {
                                                                        isNPLTMB = true;
                                                                        if (Util.isEmpty(lastNPLDateTMB)) {
                                                                            lastNPLDateTMB = creditHistModel.getAsofdate();
                                                                        } else {
                                                                            lastNPLDateTMB = getLastDateYYYYMM(lastNPLDateTMB, creditHistModel.getAsofdate());
                                                                        }
                                                                    } else {
                                                                        isNPLOther = true;
                                                                        if (Util.isEmpty(lastNPLDateOther)) {
                                                                            lastNPLDateOther = creditHistModel.getAsofdate();
                                                                        } else {
                                                                            lastNPLDateOther = getLastDateYYYYMM(lastNPLDateOther, creditHistModel.getAsofdate());
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }

                                                        if(!isValidPayment) {
                                                            break;
                                                        }
                                                    }
                                                }
                                                //set worst payment status
                                                SettlementStatus historySettlementStatus = new SettlementStatus();
                                                if (!Util.isEmpty(worstCode)) {
                                                    historySettlementStatus = settlementStatusDAO.getJuristicByCode(worstCode);
                                                }
                                                ncbDetailView.setHistoryPayment(historySettlementStatus);
                                                if (!Util.isEmpty(worstPaymentStatus)) {
                                                    worstPaymentStatus = getWorstCode(creditHistModelList.get(0).getDaypastdue(), worstPaymentStatus);
                                                } else {
                                                    worstPaymentStatus = creditHistModelList.get(0).getDaypastdue();
                                                }
                                            }
                                            //set number of outstanding payment
                                            ncbDetailView.setNoOfOutstandingPaymentIn12months(numberOfOutStandingPayment);
                                            //set number of over limit
                                            ncbDetailView.setNoOfOverLimit(numberOfOverLimit);

                                            //add ncbDetailView to ncbDetailViewList
                                            log.debug("Add ncbDetailView to list : {}", ncbDetailView);
                                            ncbDetailViews.add(ncbDetailView);
                                        }
                                    } else {
                                        for (AccountModel accountModel : h2HResponseSubjectModel.getActiveaccounts().getAccount()) {
                                            boolean isTMBAccount = false;
                                            NCBDetailView ncbDetailView = new NCBDetailView();
                                            if (accountModel.getCreditinfo() != null) {
                                                CreditInfoModel creditInfoModel = accountModel.getCreditinfo();
                                                //set accountType
                                                AccountType accountType = accountTypeDAO.getJuristicByName(creditInfoModel.getCredittype());
                                                ncbDetailView.setAccountType(accountType);
                                                //set tmb account
                                                ncbDetailView.setTMBAccount(0);
                                                if (creditInfoModel.getCreditortype() != null && creditInfoModel.getCreditortype().trim().equals(TMB_BANK_THAI)) {
                                                    ncbDetailView.setTMBAccount(1);
                                                    isTMBAccount = true;
                                                }
                                                //set account status
                                                AccountStatus accountStatus = accountStatusDAO.getJuristicByCode(creditInfoModel.getAccountstatus());
                                                ncbDetailView.setAccountStatus(accountStatus);
                                                //set date of info
                                                ncbDetailView.setDateOfInfo(DateTimeUtil.getLastDayOfMonth(Util.strYYYYMMtoDateFormat(creditInfoModel.getAsofdate())));
                                                //set open date
                                                String[] openDate = Util.splitSpace(creditInfoModel.getOpeneddate());
                                                if (openDate != null && openDate.length > 0) {
                                                    ncbDetailView.setAccountOpenDate(Util.strYYYYMMDDtoDateFormat(openDate[0]));
                                                }
                                                //set credit limit
                                                if (!Util.isEmpty(creditInfoModel.getCreditlimit())) {
                                                    ncbDetailView.setLimit(new BigDecimal(creditInfoModel.getCreditlimit()));
                                                }
                                                //set outstanding amount
                                                if (!Util.isEmpty(creditInfoModel.getOutstanding())) {
                                                    ncbDetailView.setOutstanding(new BigDecimal(creditInfoModel.getOutstanding()));
                                                }
                                                //set installment
                                                if (!Util.isEmpty(creditInfoModel.getInstallmentamount())) {
                                                    ncbDetailView.setInstallment(new BigDecimal(creditInfoModel.getInstallmentamount()));
                                                }
                                                //set restructure date
                                                log.debug("creditInfoModel.getRestructuredate() : {}", creditInfoModel.getRestructuredate());
                                                if (!Util.isEmpty(creditInfoModel.getRestructuredate())) {
                                                    String[] reStructureDate = Util.splitSpace(creditInfoModel.getRestructuredate());
                                                    if (reStructureDate != null && reStructureDate.length > 0) {
                                                        ncbDetailView.setDateOfDebtRestructuring(Util.strYYYYMMDDtoDateFormat(reStructureDate[0]));
                                                    }
                                                    //get TDR last date
                                                    if (isTMBAccount) {
                                                        isTDRTMB = true;
                                                        if (!Util.isEmpty(lastTDRDateTMB)) {
                                                            lastTDRDateTMB = creditInfoModel.getCloseddate();
                                                        } else {
                                                            lastTDRDateTMB = getLastDateYYYYMMDD(lastTDRDateTMB, creditInfoModel.getCloseddate());
                                                        }
                                                    } else {
                                                        isTDROther = true;
                                                        if (!Util.isEmpty(lastTDRDateOther)) {
                                                            lastTDRDateOther = creditInfoModel.getCloseddate();
                                                        } else {
                                                            lastTDRDateOther = getLastDateYYYYMMDD(lastTDRDateOther, creditInfoModel.getCloseddate());
                                                        }
                                                    }
                                                    log.debug("isTDRTMB : {}, isTDROther : {}, lastTDRDateTMB : {}", isTDRTMB, isTDROther, lastTDRDateTMB, lastTDRDateOther);
                                                } else {
                                                    ncbDetailView.setDateOfDebtRestructuring(null);
                                                }
                                            }

                                            String worstCode = null;
                                            int numberOfOutStandingPayment = 0;
                                            int numberOfOverLimit = 0;
                                            String creditType = "";
                                            if (accountModel.getCredithistories() != null && accountModel.getCredithistories().getCredithist() != null && accountModel.getCredithistories().getCredithist().size() > 0) {
                                                List<CreditHistModel> creditHistModelList = accountModel.getCredithistories().getCredithist();
                                                //set current payment
                                                SettlementStatus settlementStatus = new SettlementStatus();
                                                if (creditHistModelList.get(0) != null && !Util.isEmpty(creditHistModelList.get(0).getDaypastdue())) {
                                                    isValidPayment = isValidPaymentPatternJuristic(creditHistModelList.get(0));
                                                    if(!isValidPayment) {
                                                        break;
                                                    }
                                                    settlementStatus = settlementStatusDAO.getJuristicByCode(creditHistModelList.get(0).getDaypastdue());
                                                }
                                                ncbDetailView.setCurrentPayment(settlementStatus);
                                                if (!Util.isEmpty(currentWorstPaymentStatus)) {
                                                    currentWorstPaymentStatus = getWorstCode(creditHistModelList.get(0).getDaypastdue(), currentWorstPaymentStatus);
                                                } else {
                                                    currentWorstPaymentStatus = creditHistModelList.get(0).getDaypastdue();
                                                }

                                                //check for last 6,12 months for get worst payment, calculate number of outstanding and number of over limit
                                                if (accountModel.getCreditinfo() != null && !Util.isEmpty(accountModel.getCreditinfo().getCredittype())) {
                                                    creditType = accountModel.getCreditinfo().getCredittype();
                                                }
                                                if (!Util.isEmpty(creditType) && creditType.equals(ACCOUNT_TYPE_OD_JUR)) {
                                                    if (isInMonthPeriodYYYYMM(creditHistModelList.get(0).getAsofdate(), TWELVE_MONTH)) {
                                                        for (CreditHistModel creditHistModel : creditHistModelList) {
                                                            if(isOverLimit(creditHistModel.getDaypastdue())){
                                                                numberOfOverLimit++;
                                                            }

                                                            //get worstCode
                                                            if (isInMonthPeriodYYYYMM(creditHistModel.getAsofdate(), SIX_MONTH)) {
                                                                isValidPayment = isValidPaymentPatternJuristic(creditHistModel);
                                                                log.debug("DayPastDue : {}, trim : {}",creditHistModel.getDaypastdue(),creditHistModel.getDaypastdue().trim());
                                                                if(!isValidPayment) {
                                                                    break;
                                                                }
                                                                if (Util.isEmpty(worstCode)) {
                                                                    if(!isIgnoreCode(creditHistModel.getDaypastdue())){
                                                                        worstCode = creditHistModel.getDaypastdue();
                                                                    }
                                                                } else {
                                                                    worstCode = getWorstCode(creditHistModel.getDaypastdue(), worstCode);
                                                                }

                                                                if (isOutStandingPayment(creditHistModel.getDaypastdue())) {
                                                                    numberOfOutStandingPayment++;
                                                                }
                                                                if (isNPLIndividual(creditHistModel.getDaypastdue())) {
                                                                    if (isTMBAccount) {
                                                                        isNPLTMB = true;
                                                                        if (Util.isEmpty(lastNPLDateTMB)) {
                                                                            lastNPLDateTMB = creditHistModel.getAsofdate();
                                                                        } else {
                                                                            lastNPLDateTMB = getLastDateYYYYMM(lastNPLDateTMB, creditHistModel.getAsofdate());
                                                                        }
                                                                    } else {
                                                                        isNPLOther = true;
                                                                        if (Util.isEmpty(lastNPLDateOther)) {
                                                                            lastNPLDateOther = creditHistModel.getDaypastdue();
                                                                        } else {
                                                                            lastNPLDateOther = getLastDateYYYYMM(lastNPLDateOther, creditHistModel.getAsofdate());
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }

                                                        if(!isValidPayment) {
                                                            break;
                                                        }
                                                    }
                                                } else {
                                                    if (isInMonthPeriodYYYYMM(creditHistModelList.get(0).getAsofdate(), TWELVE_MONTH)) {
                                                        for (CreditHistModel creditHistModel : creditHistModelList) {
                                                            if (isInMonthPeriodYYYYMM(creditHistModel.getAsofdate(), TWELVE_MONTH)) {
                                                                if(isOverLimit(creditHistModel.getDaypastdue())){
                                                                    numberOfOverLimit++;
                                                                }
                                                                isValidPayment = isValidPaymentPatternJuristic(creditHistModel);
                                                                log.debug("DayPastDue : {}, trim : {}",creditHistModel.getDaypastdue(),creditHistModel.getDaypastdue().trim());
                                                                if(!isValidPayment) {
                                                                    break;
                                                                }

                                                                //get worstCode
                                                                if (Util.isEmpty(worstCode)) {
                                                                    if(!isIgnoreCode(creditHistModel.getDaypastdue())){
                                                                        worstCode = creditHistModel.getDaypastdue();
                                                                    }
                                                                } else {
                                                                    worstCode = getWorstCode(creditHistModel.getDaypastdue(), worstCode);
                                                                }

                                                                if (isOutStandingPayment(creditHistModel.getDaypastdue())) {
                                                                    numberOfOutStandingPayment++;
                                                                }
                                                                if (isNPLIndividual(creditHistModel.getDaypastdue())) {
                                                                    if (isTMBAccount) {
                                                                        isNPLTMB = true;
                                                                        if (Util.isEmpty(lastNPLDateTMB)) {
                                                                            lastNPLDateTMB = creditHistModel.getAsofdate();
                                                                        } else {
                                                                            lastNPLDateTMB = getLastDateYYYYMM(lastNPLDateTMB, creditHistModel.getAsofdate());
                                                                        }
                                                                    } else {
                                                                        isNPLOther = true;
                                                                        if (Util.isEmpty(lastNPLDateOther)) {
                                                                            lastNPLDateOther = creditHistModel.getAsofdate();
                                                                        } else {
                                                                            lastNPLDateOther = getLastDateYYYYMM(lastNPLDateOther, creditHistModel.getAsofdate());
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }

                                                        if(!isValidPayment) {
                                                            break;
                                                        }
                                                    }
                                                }
                                                //set worst payment status
                                                SettlementStatus historySettlementStatus = new SettlementStatus();
                                                if (!Util.isEmpty(worstCode)) {
                                                    historySettlementStatus = settlementStatusDAO.getJuristicByCode(worstCode);
                                                }
                                                ncbDetailView.setHistoryPayment(historySettlementStatus);
                                                if (!Util.isEmpty(worstPaymentStatus)) {
                                                    worstPaymentStatus = getWorstCode(creditHistModelList.get(0).getDaypastdue(), worstPaymentStatus);
                                                } else {
                                                    worstPaymentStatus = creditHistModelList.get(0).getDaypastdue();
                                                }
                                            }
                                            //set number of outstanding payment
                                            ncbDetailView.setNoOfOutstandingPaymentIn12months(numberOfOutStandingPayment);
                                            //set number of over limit
                                            ncbDetailView.setNoOfOverLimit(numberOfOverLimit);

                                            //add ncbDetailView to ncbDetailViewList
                                            ncbDetailViews.add(ncbDetailView);
                                        }
                                    }

                                    if(!isValidPayment){
                                        log.debug("unexpected value getting from NCCRS : settlement status");
                                        String reason = "Unexpected value getting from NCCRS : settlement status";
                                        ncbView.setResult(ActionResult.FAILED);
                                        ncbView.setReason(reason);
                                        ncbViews.add(ncbView);
                                        continue;
                                    }

                                    if (!Util.isEmpty(currentWorstPaymentStatus)) {
                                        SettlementStatus currentWorstSettlementStatus = settlementStatusDAO.getJuristicByCode(currentWorstPaymentStatus);
                                        if (currentWorstSettlementStatus != null) {
                                            ncbInfoView.setCurrentPaymentType(currentWorstSettlementStatus.getName());
                                        }
                                    }
                                    if (!Util.isEmpty(worstPaymentStatus)) {
                                        SettlementStatus worstSettlementStatus = settlementStatusDAO.getJuristicByCode(worstPaymentStatus);
                                        if (worstSettlementStatus != null) {
                                            ncbInfoView.setHistoryPaymentType(worstSettlementStatus.getName());
                                        }
                                    }

                                    if (isNPLTMB || isNPLOther) {
                                        ncbInfoView.setNplFlag(RadioValue.YES.value()); //true
                                        if (isNPLTMB) {
                                            ncbInfoView.setNplTMBFlag(true);
                                            if(!Util.isEmpty(lastNPLDateTMB)){
                                                if(lastNPLDateTMB.length()>=6){
                                                    int month = Integer.parseInt(lastNPLDateTMB.substring(4,6));
                                                    int year = Integer.parseInt(lastNPLDateTMB.substring(0,4));
                                                    ncbInfoView.setNplTMBMonth(month);
                                                    ncbInfoView.setNplTMBYear(year);
                                                }
                                            }
                                        }
                                        if (isNPLOther) {
                                            ncbInfoView.setNplOtherFlag(true);
                                            if(!Util.isEmpty(lastNPLDateOther)){
                                                if(lastNPLDateOther.length()>=6){
                                                    int month = Integer.parseInt(lastNPLDateOther.substring(4,6));
                                                    int year = Integer.parseInt(lastNPLDateOther.substring(0,4));
                                                    ncbInfoView.setNplOtherMonth(month);
                                                    ncbInfoView.setNplOtherYear(year);
                                                }
                                            }
                                        }
                                    } else {
                                        ncbInfoView.setNplFlag(RadioValue.NO.value()); //false
                                    }

                                    if (isTDRTMB || isTDROther) {
                                        ncbInfoView.setTdrFlag(RadioValue.YES.value()); //true
                                        if (isTDRTMB) {
                                            ncbInfoView.setTdrTMBFlag(true);
                                            if(!Util.isEmpty(lastTDRDateTMB)){
                                                if(lastTDRDateTMB.length()>=6){
                                                    int month = Integer.parseInt(lastTDRDateTMB.substring(4,6));
                                                    int year = Integer.parseInt(lastTDRDateTMB.substring(0,4));
                                                    ncbInfoView.setTdrTMBMonth(month);
                                                    ncbInfoView.setTdrTMBYear(year);
                                                }
                                            }
                                        }
                                        if (isTDROther) {
                                            ncbInfoView.setTdrOtherFlag(true);
                                            if(!Util.isEmpty(lastTDRDateOther)){
                                                if(lastTDRDateOther.length()>=6){
                                                    int month = Integer.parseInt(lastTDRDateOther.substring(4,6));
                                                    int year = Integer.parseInt(lastTDRDateOther.substring(0,4));
                                                    ncbInfoView.setTdrOtherMonth(month);
                                                    ncbInfoView.setTdrOtherYear(year);
                                                }
                                            }
                                        }
                                    } else {
                                        ncbInfoView.setTdrFlag(RadioValue.NO.value()); //false
                                    }
                                } else {
                                    //no account detail data
                                }

                                //get enquiry amount
                                int enquiryTime = 0;
                                if (enquiryModelResults.size() > 0) {
                                    Map<String, String> enquiryMap = new HashMap<String, String>();  //for check duplicate enquiry time
                                    //get number of enquiry in last 6 months
                                    for (InqHistModel enquiryModel : enquiryModelResults) {
                                        String[] inqDate = Util.splitSpace(enquiryModel.getInqdate());
                                        if (inqDate != null && inqDate.length > 0) {
                                            if (isInMonthPeriodYYYYMMDD(inqDate[0], SIX_MONTH)) {
                                                enquiryMap.put(enquiryModel.getInqdate(), enquiryModel.getInqdate());
                                            }
                                        }
                                    }
                                    enquiryTime = enquiryMap.size();
                                }

                                if (ncbInfoView != null) {
                                    ncbInfoView.setCheckIn6Month(enquiryTime);
                                }
                            }

                            //TODO: add more data (hidden field) for NCBInfoView (name, address, marital status, enquiry date, last as of date, tracking id) here
                            //ncbInfoView

                        }
                    }
                    ncbView.setNcbInfoView(ncbInfoView);
                    ncbView.setNCBDetailViews(ncbDetailViews);
                    ncbView.setAccountInfoIdList(accountInfoIdList);
                    ncbView.setAccountInfoNameList(accountInfoNameList);
                } else {
                    log.debug("NCCRSTransformJuristic : Failed");
                    ncbView.setResult(ActionResult.FAILED);
                    ncbView.setReason(responseNCCRSModel.getReason());
                }
                ncbViews.add(ncbView);
            }
        }
        return ncbViews;
    }

    private boolean isValidPaymentPatternIndividual(SubjectAccountModel subjectAccountModel, String lastAsOfDate){
        if(!Util.isEmpty(subjectAccountModel.getPaymtdate01()) && isInMonthPeriodYYYYMMDD(subjectAccountModel.getPaymtdate01(), lastAsOfDate, TWELVE_MONTH)){
            log.debug("subjectAccountModel.getPaymt01() : {}",subjectAccountModel.getPaymt01());
            if(NCBPaymentCode.getValue(subjectAccountModel.getPaymt01()) != null){
                if(!Util.isEmpty(subjectAccountModel.getPaymtdate02()) && isInMonthPeriodYYYYMMDD(subjectAccountModel.getPaymtdate02(), lastAsOfDate, TWELVE_MONTH)){
                    log.debug("subjectAccountModel.getPaymt02() : {}",subjectAccountModel.getPaymt02());
                    if(NCBPaymentCode.getValue(subjectAccountModel.getPaymt02()) != null){
                        if(!Util.isEmpty(subjectAccountModel.getPaymtdate03()) && isInMonthPeriodYYYYMMDD(subjectAccountModel.getPaymtdate03(), lastAsOfDate, TWELVE_MONTH)){
                            log.debug("subjectAccountModel.getPaymt03() : {}",subjectAccountModel.getPaymt03());
                            if(NCBPaymentCode.getValue(subjectAccountModel.getPaymt03()) != null){
                                if(!Util.isEmpty(subjectAccountModel.getPaymtdate04()) && isInMonthPeriodYYYYMMDD(subjectAccountModel.getPaymtdate04(), lastAsOfDate, TWELVE_MONTH)){
                                    log.debug("subjectAccountModel.getPaymt04() : {}",subjectAccountModel.getPaymt04());
                                    if(NCBPaymentCode.getValue(subjectAccountModel.getPaymt04()) != null){
                                        if(!Util.isEmpty(subjectAccountModel.getPaymtdate05()) && isInMonthPeriodYYYYMMDD(subjectAccountModel.getPaymtdate05(), lastAsOfDate, TWELVE_MONTH)){
                                            log.debug("subjectAccountModel.getPaymt05() : {}",subjectAccountModel.getPaymt05());
                                            if(NCBPaymentCode.getValue(subjectAccountModel.getPaymt05()) != null){
                                                if(!Util.isEmpty(subjectAccountModel.getPaymtdate06()) && isInMonthPeriodYYYYMMDD(subjectAccountModel.getPaymtdate06(), lastAsOfDate, TWELVE_MONTH)){
                                                    log.debug("subjectAccountModel.getPaymt06() : {}",subjectAccountModel.getPaymt06());
                                                    if(NCBPaymentCode.getValue(subjectAccountModel.getPaymt06()) != null){
                                                        if(!Util.isEmpty(subjectAccountModel.getPaymtdate07()) && isInMonthPeriodYYYYMMDD(subjectAccountModel.getPaymtdate07(), lastAsOfDate, TWELVE_MONTH)){
                                                            log.debug("subjectAccountModel.getPaymt07() : {}",subjectAccountModel.getPaymt07());
                                                            if(NCBPaymentCode.getValue(subjectAccountModel.getPaymt07()) != null){
                                                                if(!Util.isEmpty(subjectAccountModel.getPaymtdate08()) && isInMonthPeriodYYYYMMDD(subjectAccountModel.getPaymtdate08(), lastAsOfDate, TWELVE_MONTH)){
                                                                    log.debug("subjectAccountModel.getPaymt08() : {}",subjectAccountModel.getPaymt08());
                                                                    if(NCBPaymentCode.getValue(subjectAccountModel.getPaymt08()) != null){
                                                                        if(!Util.isEmpty(subjectAccountModel.getPaymtdate09()) && isInMonthPeriodYYYYMMDD(subjectAccountModel.getPaymtdate09(), lastAsOfDate, TWELVE_MONTH)){
                                                                            log.debug("subjectAccountModel.getPaymt09() : {}",subjectAccountModel.getPaymt09());
                                                                            if(NCBPaymentCode.getValue(subjectAccountModel.getPaymt09()) != null){
                                                                                if(!Util.isEmpty(subjectAccountModel.getPaymtdate10()) && isInMonthPeriodYYYYMMDD(subjectAccountModel.getPaymtdate10(), lastAsOfDate, TWELVE_MONTH)){
                                                                                    log.debug("subjectAccountModel.getPaymt10() : {}",subjectAccountModel.getPaymt10());
                                                                                    if(NCBPaymentCode.getValue(subjectAccountModel.getPaymt10()) != null){
                                                                                        if(!Util.isEmpty(subjectAccountModel.getPaymtdate11()) && isInMonthPeriodYYYYMMDD(subjectAccountModel.getPaymtdate11(), lastAsOfDate, TWELVE_MONTH)){
                                                                                            log.debug("subjectAccountModel.getPaymt11() : {}",subjectAccountModel.getPaymt11());
                                                                                            if(NCBPaymentCode.getValue(subjectAccountModel.getPaymt11()) != null){
                                                                                                if(!Util.isEmpty(subjectAccountModel.getPaymtdate12()) && isInMonthPeriodYYYYMMDD(subjectAccountModel.getPaymtdate12(), lastAsOfDate, TWELVE_MONTH)){
                                                                                                    log.debug("subjectAccountModel.getPaymt12() : {}",subjectAccountModel.getPaymt12());
                                                                                                    if(NCBPaymentCode.getValue(subjectAccountModel.getPaymt12()) != null){
                                                                                                            return true;
                                                                                                    } else {
                                                                                                        return false;
                                                                                                    }
                                                                                                } else {
                                                                                                    return true;
                                                                                                }
                                                                                            } else {
                                                                                                return false;
                                                                                            }
                                                                                        } else {
                                                                                            return true;
                                                                                        }
                                                                                    } else {
                                                                                        return false;
                                                                                    }
                                                                                } else {
                                                                                    return true;
                                                                                }
                                                                            } else {
                                                                                return false;
                                                                            }
                                                                        } else {
                                                                            return true;
                                                                        }
                                                                    } else {
                                                                        return false;
                                                                    }
                                                                } else {
                                                                    return true;
                                                                }
                                                            } else {
                                                                return false;
                                                            }
                                                        } else {
                                                            return true;
                                                        }
                                                    } else {
                                                        return false;
                                                    }
                                                } else {
                                                    return true;
                                                }
                                            } else {
                                                return false;
                                            }
                                        } else {
                                            return true;
                                        }
                                    } else {
                                        return false;
                                    }
                                } else {
                                    return true;
                                }
                            } else {
                                return false;
                            }
                        } else {
                            return true;
                        }
                    } else {
                        return false;
                    }
                } else {
                    return true;
                }
            } else {
                return false;
            }
        } else {
            if(!Util.isEmpty(subjectAccountModel.getPaymt01())){
                log.debug("subjectAccountModel.getPaymt01() : {}",subjectAccountModel.getPaymt01());
                if(NCBPaymentCode.getValue(subjectAccountModel.getPaymt01()) != null){
                    return true;
                } else {
                    return false;
                }
            }
            return false;
        }
    }

    private boolean isValidPaymentPatternJuristic(CreditHistModel creditHistModel){
        if(NCBPaymentCode.getValue(creditHistModel.getDaypastdue()) != null){
            return true;
        }
        return false;
    }

    private String getWorstCode(String code, String worstCode) {
        int value1 = NCBPaymentCode.getValue(worstCode).value();
        int value2 = NCBPaymentCode.getValue(code).value();
        if(isIgnoreCode(code)) { //ignore code;
            return worstCode;
        }
        if (value2 > value1) {
            return code;
        }
        return worstCode;
    }

    private boolean isIgnoreCode(String code){
        if(!Util.isEmpty(code)
                && !code.equalsIgnoreCase(NCBPaymentCode.CODE_XXX.toString())
                && !code.equalsIgnoreCase(NCBPaymentCode.CODE_999.toString())) {
            return false;
        }
        return true;
    }

    private boolean compareDateYYYMMDD(String currentDateStr, String compareDateStr){
        if (!Util.isNull(currentDateStr) && !Util.isEmpty(currentDateStr) && !Util.isNull(compareDateStr) && !Util.isEmpty(compareDateStr)) {
            Date currentDate = Util.strYYYYMMDDtoDateFormat(currentDateStr);
            Date compareDate = Util.strYYYYMMDDtoDateFormat(compareDateStr);

            int compareResult = DateTimeUtil.compareDate(currentDate, compareDate);

            if(compareResult < 0){      //if currentDate < compareDate
                return true;
            }
        }
        return false;
    }

    private boolean isInMonthPeriodYYYYMMDD(String dateStr, int numberMonth) {
        if (dateStr != null && !dateStr.trim().equals("")) {
            Date paymentDate = Util.strYYYYMMDDtoDateFormat(dateStr);
            Date currentDate = new Date();
            Calendar startCalendar = new GregorianCalendar();
            startCalendar.setTime(paymentDate);
            Calendar endCalendar = new GregorianCalendar();
            endCalendar.setTime(currentDate);
            int diffYear = endCalendar.get(Calendar.YEAR) - startCalendar.get(Calendar.YEAR);
            int diffMonth = diffYear * 12 + endCalendar.get(Calendar.MONTH) - startCalendar.get(Calendar.MONTH);
            if (diffMonth <= numberMonth) {
                return true;
            }
        }
        return false;
    }

    private boolean isInMonthPeriodYYYYMMDD(String dateStr, String compareStr, int numberMonth) {
        log.debug("isInMonthPeriodYYYYMMDD (dateStr: {}, compareStr: {}, numberMonth: {}",dateStr,compareStr,numberMonth);
        if (!Util.isEmpty(dateStr) && !Util.isEmpty(compareStr)) {
            Date paymentDate = Util.strYYYYMMDDtoDateFormat(dateStr);
            Date compareDate = Util.strYYYYMMDDtoDateFormat(compareStr);
            Calendar startCalendar = new GregorianCalendar();
            startCalendar.setTime(paymentDate);
            Calendar endCalendar = new GregorianCalendar();
            endCalendar.setTime(compareDate);
            int diffYear = endCalendar.get(Calendar.YEAR) - startCalendar.get(Calendar.YEAR);
            int diffMonth = diffYear * 12 + endCalendar.get(Calendar.MONTH) - startCalendar.get(Calendar.MONTH);
            if (diffMonth <= numberMonth) {
                log.debug("return : true");
                return true;
            }
        }
        log.debug("return : false");
        return false;
    }

    private boolean isInMonthPeriodYYYYMM(String dateStr, int numberMonth) {
        if (dateStr != null && !dateStr.trim().equals("")) {
            Date paymentDate = Util.strYYYYMMtoDateFormat(dateStr);
            Date currentDate = new Date();
            Calendar startCalendar = new GregorianCalendar();
            startCalendar.setTime(paymentDate);
            Calendar endCalendar = new GregorianCalendar();
            endCalendar.setTime(currentDate);
            int diffYear = endCalendar.get(Calendar.YEAR) - startCalendar.get(Calendar.YEAR);
            int diffMonth = diffYear * 12 + endCalendar.get(Calendar.MONTH) - startCalendar.get(Calendar.MONTH);
            if (diffMonth <= numberMonth) {
                return true;
            }
        }
        return false;
    }

    private boolean isOutStandingPayment(String paymentCode) {
        if (!Util.isEmpty(paymentCode)) {
            int value = NCBPaymentCode.getValue(paymentCode).value();
            if (value >= 4) { //code >= 004
                return true;
            }
        }
        return false;
    }

    private boolean isOverLimit(String paymentCode) {
        if (!Util.isEmpty(paymentCode)) {
            int value = NCBPaymentCode.getValue(paymentCode).value();
            if (value == 3) { //code == __Y
                return true;
            }
        }
        return false;
    }

    private boolean isNPLIndividual(String paymentCode) {
        if (!Util.isEmpty(paymentCode)) {
            int value = NCBPaymentCode.getValue(paymentCode).value();
            if (value >= 3) {
                return true;
            }
        }
        return false;
    }

    private boolean isNPLJuristic(String paymentCode) {
        if (!Util.isEmpty(paymentCode)) {
            int value = NCBPaymentCode.getValue(paymentCode).value();
            if (value >= 4) {
                return true;
            }
        }
        return false;
    }

    private String getLastDateYYYYMMDD(String dateStr1, String dateStr2) {
        Date date1 = Util.strYYYYMMDDtoDateFormat(dateStr1);
        Date date2 = Util.strYYYYMMDDtoDateFormat(dateStr2);
        if (date1 != null && date2 != null) {
            if (date1.compareTo(date2) > 0) {
                return Util.createDateString(date1, "yyyyMMdd");
            } else if (date1.compareTo(date2) < 0) {
                return Util.createDateString(date2, "yyyyMMdd");
            }
        } else {
            if (date1 == null) {
                return dateStr2;
            } else {
                return dateStr1;
            }
        }
        return dateStr1;
    }

    private String getLastDateYYYYMM(String dateStr1, String dateStr2) {
        Date date1 = DateTimeUtil.getLastDayOfMonth(Util.strYYYYMMtoDateFormat(dateStr1));
        Date date2 = DateTimeUtil.getLastDayOfMonth(Util.strYYYYMMtoDateFormat(dateStr2));
        if (date1 != null && date2 != null) {
            if (date1.compareTo(date2) > 0) {
                return Util.createDateString(date1, "yyyyMMdd");
            } else if (date1.compareTo(date2) < 0) {
                return Util.createDateString(date2, "yyyyMMdd");
            }
        } else {
            if (date1 == null) {
                return dateStr2;
            } else {
                return dateStr1;
            }
        }
        return dateStr1;
    }
}
