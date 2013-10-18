package com.clevel.selos.transform.business;

import com.clevel.selos.dao.master.AccountStatusDAO;
import com.clevel.selos.dao.master.AccountTypeDAO;
import com.clevel.selos.dao.master.SettlementStatusDAO;
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
import com.clevel.selos.model.view.NCBSummaryView;
import com.clevel.selos.model.view.NcbView;
import com.clevel.selos.util.Util;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.*;

public class NCBBizTransform extends BusinessTransform {
    @Inject
    Logger log;
    @Inject
    AccountTypeDAO accountTypeDAO;
    @Inject
    AccountStatusDAO accountStatusDAO;
    @Inject
    SettlementStatusDAO settlementStatusDAO;

    private final String TMB_BANK = "TMB";
    private final int SIX_MONTH = 6;
    private final int TWELVE_MONTH = 12;

    public List<NcbView> transformIndividual(List<NCRSOutputModel> responseNCRSModels){
        List<NcbView> ncbViews = null;
        List<NCBDetailView> ncbDetailViews = null;
        NCBInfoView ncbInfoView = null;
        //NCBSummaryView ncbSummaryView = null;
        if(responseNCRSModels!=null && responseNCRSModels.size()>0){
            ncbViews = new ArrayList<NcbView>();
            for(NCRSOutputModel responseNCRSModel: responseNCRSModels){
                NcbView ncbView = new NcbView();
                List<AccountInfoName> accountInfoNameList = new ArrayList<AccountInfoName>();
                List<AccountInfoId> accountInfoIdList = new ArrayList<AccountInfoId>();

                //get account info from NCRSModel
                NCRSModel ncrsModel = responseNCRSModel.getNcrsModel();
                AccountInfoName accountInfoName = new AccountInfoName();
                AccountInfoId accountInfoId = new AccountInfoId();

                accountInfoName.setNameTh(ncrsModel.getFirstName());
                accountInfoName.setSurnameTh(ncrsModel.getLastName());
                accountInfoId.setIdNumber(ncrsModel.getCitizenId());
                if(ncrsModel.getIdType().equalsIgnoreCase("01")){
                    accountInfoId.setDocumentType(DocumentType.CITIZEN_ID);
                } else {
                    accountInfoId.setDocumentType(DocumentType.PASSPORT);
                }
                accountInfoNameList.add(accountInfoName);
                accountInfoIdList.add(accountInfoId);
                ncbView.setIdNumber(responseNCRSModel.getIdNumber());
                if(responseNCRSModel.getActionResult() == ActionResult.SUCCEED){
                    ncbView.setResult(ActionResult.SUCCEED);

                    //Transform NCB Account Logic
                    if(responseNCRSModel.getResponseModel()!=null){
                        NCRSResponseModel ncrsResponseModel = responseNCRSModel.getResponseModel();
                        if(ncrsResponseModel.getBodyModel()!=null){
                            TUEFResponseModel tuefResponseModel = ncrsResponseModel.getBodyModel().getTransaction().getTuefresponse();
                            if(tuefResponseModel.getSubject()!=null && tuefResponseModel.getSubject().size()>0){
                                List<SubjectAccountModel> subjectAccountModelResults = new ArrayList<SubjectAccountModel>();
                                List<EnquiryModel> enquiryModelResults = new ArrayList<EnquiryModel>();

                                for(SubjectModel subjectModel: tuefResponseModel.getSubject()){

                                    //get Name and Id information for send csi
                                    if(subjectModel.getName()!=null && subjectModel.getName().size()>0) {
                                        for(SubjectNameModel subjectNameModel: subjectModel.getName()){
                                            AccountInfoName ncbAccountInfoName = new AccountInfoName();
                                            ncbAccountInfoName.setNameTh(subjectNameModel.getFirstname());
                                            ncbAccountInfoName.setSurnameTh(subjectNameModel.getFamilyname());
                                            accountInfoNameList.add(ncbAccountInfoName);
                                        }
                                    }

                                    if(subjectModel.getId()!=null && subjectModel.getId().size()>0){
                                        for(SubjectIdModel subjectIdModel: subjectModel.getId()){
                                            if(subjectIdModel.getIdtype().equalsIgnoreCase("01") && subjectIdModel.getIdtype().equalsIgnoreCase("07")){
                                                AccountInfoId ncbAccountInfoId = new AccountInfoId();
                                                if(subjectIdModel.getIdtype().equalsIgnoreCase("01")){
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
                                    if(subjectAccountModels != null && subjectAccountModels.size() > 0){
                                        for(SubjectAccountModel subjectAccountModel : subjectAccountModels){
                                            subjectAccountModelResults.add(subjectAccountModel);
                                        }
                                    }

                                    //get Enquiry for all subject
                                    List<EnquiryModel> enquiryModels = subjectModel.getEnquiry();
                                    if(enquiryModels != null && enquiryModels.size() > 0){
                                        for(EnquiryModel enquiryModel : enquiryModels){
                                            enquiryModelResults.add(enquiryModel);
                                        }
                                    }
                                }

                                if(subjectAccountModelResults.size() > 0){
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
                                    for(SubjectAccountModel subjectAccountModel : subjectAccountModelResults){
                                        boolean isTMBAccount = false;
                                        NCBDetailView ncbDetailView = new NCBDetailView();
                                        //set accountType
                                        AccountType accountType = accountTypeDAO.getIndividualByCode(subjectAccountModel.getAccounttype());
                                        ncbDetailView.setAccountType(accountType);
                                        //set tmb account
                                        ncbDetailView.setTMBAccount(0);
                                        if(subjectAccountModel.getShortname().equals(TMB_BANK)){ //todo: change to master
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
                                        if(!Util.isEmpty(subjectAccountModel.getCreditlimit())){
                                            ncbDetailView.setLimit(new BigDecimal(subjectAccountModel.getCreditlimit()));
                                        }
                                        //set outstanding amount
                                        if(!Util.isEmpty(subjectAccountModel.getAmountowed())){
                                            ncbDetailView.setOutstanding(new BigDecimal(subjectAccountModel.getAmountowed()));
                                        }
                                        //set installment
                                        if(!Util.isEmpty(subjectAccountModel.getInstallmentamount())){
                                            ncbDetailView.setInstallment(new BigDecimal(subjectAccountModel.getInstallmentamount()));
                                        }
                                        //set restructure date
                                        if(!Util.isEmpty(subjectAccountModel.getLastrestructureddate())){
                                            ncbDetailView.setDateOfDebtRestructuring(Util.strYYYYMMDDtoDateFormat(subjectAccountModel.getLastrestructureddate()));
                                            //get TDR last date
                                            if(isTMBAccount){
                                                isTDRTMB = true;
                                                if(!Util.isEmpty(lastTDRDateTMB)){
                                                    lastTDRDateTMB = subjectAccountModel.getCloseddate();
                                                } else {
                                                    lastTDRDateTMB = getLastDateYYYYMMDD(lastTDRDateTMB,subjectAccountModel.getCloseddate());
                                                }
                                            } else {
                                                isTDROther = true;
                                                if(!Util.isEmpty(lastTDRDateOther)){
                                                    lastTDRDateOther = subjectAccountModel.getCloseddate();
                                                } else {
                                                    lastTDRDateOther = getLastDateYYYYMMDD(lastTDRDateOther,subjectAccountModel.getCloseddate());
                                                }
                                            }
                                        }
                                        //set current payment
                                        SettlementStatus settlementStatus = new SettlementStatus();
                                        if(!Util.isEmpty(subjectAccountModel.getPaymt01())){
                                            settlementStatus = settlementStatusDAO.getIndividualByCode(subjectAccountModel.getPaymt01());
                                        }
                                        ncbDetailView.setCurrentPayment(settlementStatus);
                                        if(!Util.isEmpty(currentWorstPaymentStatus)){
                                            currentWorstPaymentStatus = getWorstCode(subjectAccountModel.getPaymt01(),currentWorstPaymentStatus);
                                        } else {
                                            currentWorstPaymentStatus = subjectAccountModel.getPaymt01();
                                        }

                                        //check for last 6,12 months for get worst payment, calculate number of outstanding and number of over limit
                                        String worstCode = null;
                                        int numberOfOutStandingPayment = 0;
                                        int numberOfOverLimit = 0;
                                        if(!Util.isEmpty(subjectAccountModel.getAccountstatus()) && subjectAccountModel.getAccountstatus().equals("04")){
                                            if(isInMonthPeriodYYYYMMDD(subjectAccountModel.getPaymtdate01(), TWELVE_MONTH)){
                                                if(isOverLimit(subjectAccountModel.getPaymt01())){
                                                    numberOfOverLimit++;
                                                }
                                                if(isInMonthPeriodYYYYMMDD(subjectAccountModel.getPaymtdate01(), SIX_MONTH)){
                                                    worstCode = subjectAccountModel.getPaymt01();
                                                    if(isOutStandingPayment(subjectAccountModel.getPaymt01())){
                                                        numberOfOutStandingPayment++;
                                                    }
                                                    if(isNPLIndividual(subjectAccountModel.getPaymt01())){
                                                        if(isTMBAccount){
                                                            isNPLTMB = true;
                                                            if(Util.isEmpty(lastNPLDateTMB)){
                                                                lastNPLDateTMB = subjectAccountModel.getPaymtdate01();
                                                            } else {
                                                                lastNPLDateTMB = getLastDateYYYYMMDD(lastNPLDateTMB,subjectAccountModel.getPaymtdate01());
                                                            }
                                                        } else {
                                                            isNPLOther = true;
                                                            if(Util.isEmpty(lastNPLDateOther)){
                                                                lastNPLDateOther = subjectAccountModel.getPaymtdate01();
                                                            } else {
                                                                lastNPLDateOther = getLastDateYYYYMMDD(lastNPLDateOther,subjectAccountModel.getPaymtdate01());
                                                            }
                                                        }
                                                    }
                                                }
                                                if(isInMonthPeriodYYYYMMDD(subjectAccountModel.getPaymtdate02(), TWELVE_MONTH)){
                                                    if(isOverLimit(subjectAccountModel.getPaymt02())){
                                                        numberOfOverLimit++;
                                                    }
                                                    if(isInMonthPeriodYYYYMMDD(subjectAccountModel.getPaymtdate02(), SIX_MONTH)){
                                                        worstCode = getWorstCode(subjectAccountModel.getPaymt02(), worstCode);
                                                        if(isOutStandingPayment(subjectAccountModel.getPaymt02())){
                                                            numberOfOutStandingPayment++;
                                                        }
                                                        if(isNPLIndividual(subjectAccountModel.getPaymt02())){
                                                            if(isTMBAccount){
                                                                isNPLTMB = true;
                                                                if(Util.isEmpty(lastNPLDateTMB)){
                                                                    lastNPLDateTMB = subjectAccountModel.getPaymtdate02();
                                                                } else {
                                                                    lastNPLDateTMB = getLastDateYYYYMMDD(lastNPLDateTMB,subjectAccountModel.getPaymtdate02());
                                                                }
                                                            } else {
                                                                isNPLOther = true;
                                                                if(Util.isEmpty(lastNPLDateOther)){
                                                                    lastNPLDateOther = subjectAccountModel.getPaymtdate02();
                                                                } else {
                                                                    lastNPLDateOther = getLastDateYYYYMMDD(lastNPLDateOther,subjectAccountModel.getPaymtdate02());
                                                                }
                                                            }
                                                        }
                                                    }
                                                    if(isInMonthPeriodYYYYMMDD(subjectAccountModel.getPaymtdate03(), TWELVE_MONTH)){
                                                        if(isOverLimit(subjectAccountModel.getPaymt03())){
                                                            numberOfOverLimit++;
                                                        }
                                                        if(isInMonthPeriodYYYYMMDD(subjectAccountModel.getPaymtdate03(), SIX_MONTH)){
                                                            worstCode = getWorstCode(subjectAccountModel.getPaymt03(), worstCode);
                                                            if(isOutStandingPayment(subjectAccountModel.getPaymt03())){
                                                                numberOfOutStandingPayment++;
                                                            }
                                                            if(isNPLIndividual(subjectAccountModel.getPaymt03())){
                                                                if(isTMBAccount){
                                                                    isNPLTMB = true;
                                                                    if(Util.isEmpty(lastNPLDateTMB)){
                                                                        lastNPLDateTMB = subjectAccountModel.getPaymtdate03();
                                                                    } else {
                                                                        lastNPLDateTMB = getLastDateYYYYMMDD(lastNPLDateTMB,subjectAccountModel.getPaymtdate03());
                                                                    }
                                                                } else {
                                                                    isNPLOther = true;
                                                                    if(Util.isEmpty(lastNPLDateOther)){
                                                                        lastNPLDateOther = subjectAccountModel.getPaymtdate03();
                                                                    } else {
                                                                        lastNPLDateOther = getLastDateYYYYMMDD(lastNPLDateOther,subjectAccountModel.getPaymtdate03());
                                                                    }
                                                                }
                                                            }
                                                        }
                                                        if(isInMonthPeriodYYYYMMDD(subjectAccountModel.getPaymtdate04(), TWELVE_MONTH)){
                                                            if(isOverLimit(subjectAccountModel.getPaymt04())){
                                                                numberOfOverLimit++;
                                                            }
                                                            if(isInMonthPeriodYYYYMMDD(subjectAccountModel.getPaymtdate04(), SIX_MONTH)){
                                                                worstCode = getWorstCode(subjectAccountModel.getPaymt04(), worstCode);
                                                                if(isOutStandingPayment(subjectAccountModel.getPaymt04())){
                                                                    numberOfOutStandingPayment++;
                                                                }
                                                                if(isNPLIndividual(subjectAccountModel.getPaymt04())){
                                                                    if(isTMBAccount){
                                                                        isNPLTMB = true;
                                                                        if(Util.isEmpty(lastNPLDateTMB)){
                                                                            lastNPLDateTMB = subjectAccountModel.getPaymtdate04();
                                                                        } else {
                                                                            lastNPLDateTMB = getLastDateYYYYMMDD(lastNPLDateTMB,subjectAccountModel.getPaymtdate04());
                                                                        }
                                                                    } else {
                                                                        isNPLOther = true;
                                                                        if(Util.isEmpty(lastNPLDateOther)){
                                                                            lastNPLDateOther = subjectAccountModel.getPaymtdate04();
                                                                        } else {
                                                                            lastNPLDateOther = getLastDateYYYYMMDD(lastNPLDateOther,subjectAccountModel.getPaymtdate04());
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                            if(isInMonthPeriodYYYYMMDD(subjectAccountModel.getPaymtdate05(), TWELVE_MONTH)){
                                                                if(isOverLimit(subjectAccountModel.getPaymt05())){
                                                                    numberOfOverLimit++;
                                                                }
                                                                if(isInMonthPeriodYYYYMMDD(subjectAccountModel.getPaymtdate05(), SIX_MONTH)){
                                                                    worstCode = getWorstCode(subjectAccountModel.getPaymt05(), worstCode);
                                                                    if(isOutStandingPayment(subjectAccountModel.getPaymt05())){
                                                                        numberOfOutStandingPayment++;
                                                                    }
                                                                    if(isNPLIndividual(subjectAccountModel.getPaymt05())){
                                                                        if(isTMBAccount){
                                                                            isNPLTMB = true;
                                                                            if(Util.isEmpty(lastNPLDateTMB)){
                                                                                lastNPLDateTMB = subjectAccountModel.getPaymtdate05();
                                                                            } else {
                                                                                lastNPLDateTMB = getLastDateYYYYMMDD(lastNPLDateTMB,subjectAccountModel.getPaymtdate05());
                                                                            }
                                                                        } else {
                                                                            isNPLOther = true;
                                                                            if(Util.isEmpty(lastNPLDateOther)){
                                                                                lastNPLDateOther = subjectAccountModel.getPaymtdate05();
                                                                            } else {
                                                                                lastNPLDateOther = getLastDateYYYYMMDD(lastNPLDateOther,subjectAccountModel.getPaymtdate05());
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                                if(isInMonthPeriodYYYYMMDD(subjectAccountModel.getPaymtdate06(), TWELVE_MONTH)){
                                                                    if(isOverLimit(subjectAccountModel.getPaymt06())){
                                                                        numberOfOverLimit++;
                                                                    }
                                                                    if(isInMonthPeriodYYYYMMDD(subjectAccountModel.getPaymtdate06(), SIX_MONTH)){
                                                                        worstCode = getWorstCode(subjectAccountModel.getPaymt06(), worstCode);
                                                                        if(isOutStandingPayment(subjectAccountModel.getPaymt06())){
                                                                            numberOfOutStandingPayment++;
                                                                        }
                                                                        if(isNPLIndividual(subjectAccountModel.getPaymt06())){
                                                                            if(isTMBAccount){
                                                                                isNPLTMB = true;
                                                                                if(Util.isEmpty(lastNPLDateTMB)){
                                                                                    lastNPLDateTMB = subjectAccountModel.getPaymtdate06();
                                                                                } else {
                                                                                    lastNPLDateTMB = getLastDateYYYYMMDD(lastNPLDateTMB,subjectAccountModel.getPaymtdate06());
                                                                                }
                                                                            } else {
                                                                                isNPLOther = true;
                                                                                if(Util.isEmpty(lastNPLDateOther)){
                                                                                    lastNPLDateOther = subjectAccountModel.getPaymtdate06();
                                                                                } else {
                                                                                    lastNPLDateOther = getLastDateYYYYMMDD(lastNPLDateOther,subjectAccountModel.getPaymtdate06());
                                                                                }
                                                                            }
                                                                        }
                                                                    }
                                                                    if(isInMonthPeriodYYYYMMDD(subjectAccountModel.getPaymtdate07(), TWELVE_MONTH)){
                                                                        if(isOverLimit(subjectAccountModel.getPaymt07())){
                                                                            numberOfOverLimit++;
                                                                        }
                                                                        if(isInMonthPeriodYYYYMMDD(subjectAccountModel.getPaymtdate07(), SIX_MONTH)){
                                                                            worstCode = getWorstCode(subjectAccountModel.getPaymt07(), worstCode);
                                                                            if(isOutStandingPayment(subjectAccountModel.getPaymt07())){
                                                                                numberOfOutStandingPayment++;
                                                                            }
                                                                            if(isNPLIndividual(subjectAccountModel.getPaymt07())){
                                                                                if(isTMBAccount){
                                                                                    isNPLTMB = true;
                                                                                    if(Util.isEmpty(lastNPLDateTMB)){
                                                                                        lastNPLDateTMB = subjectAccountModel.getPaymtdate07();
                                                                                    } else {
                                                                                        lastNPLDateTMB = getLastDateYYYYMMDD(lastNPLDateTMB,subjectAccountModel.getPaymtdate07());
                                                                                    }
                                                                                } else {
                                                                                    isNPLOther = true;
                                                                                    if(Util.isEmpty(lastNPLDateOther)){
                                                                                        lastNPLDateOther = subjectAccountModel.getPaymtdate07();
                                                                                    } else {
                                                                                        lastNPLDateOther = getLastDateYYYYMMDD(lastNPLDateOther,subjectAccountModel.getPaymtdate07());
                                                                                    }
                                                                                }
                                                                            }
                                                                        }
                                                                        if(isInMonthPeriodYYYYMMDD(subjectAccountModel.getPaymtdate08(), TWELVE_MONTH)){
                                                                            if(isOverLimit(subjectAccountModel.getPaymt08())){
                                                                                numberOfOverLimit++;
                                                                            }
                                                                            if(isInMonthPeriodYYYYMMDD(subjectAccountModel.getPaymtdate08(), SIX_MONTH)){
                                                                                worstCode = getWorstCode(subjectAccountModel.getPaymt08(), worstCode);
                                                                                if(isOutStandingPayment(subjectAccountModel.getPaymt08())){
                                                                                    numberOfOutStandingPayment++;
                                                                                }
                                                                                if(isNPLIndividual(subjectAccountModel.getPaymt08())){
                                                                                    if(isTMBAccount){
                                                                                        isNPLTMB = true;
                                                                                        if(Util.isEmpty(lastNPLDateTMB)){
                                                                                            lastNPLDateTMB = subjectAccountModel.getPaymtdate08();
                                                                                        } else {
                                                                                            lastNPLDateTMB = getLastDateYYYYMMDD(lastNPLDateTMB,subjectAccountModel.getPaymtdate08());
                                                                                        }
                                                                                    } else {
                                                                                        isNPLOther = true;
                                                                                        if(Util.isEmpty(lastNPLDateOther)){
                                                                                            lastNPLDateOther = subjectAccountModel.getPaymtdate08();
                                                                                        } else {
                                                                                            lastNPLDateOther = getLastDateYYYYMMDD(lastNPLDateOther,subjectAccountModel.getPaymtdate08());
                                                                                        }
                                                                                    }
                                                                                }
                                                                            }
                                                                            if(isInMonthPeriodYYYYMMDD(subjectAccountModel.getPaymtdate09(), TWELVE_MONTH)){
                                                                                if(isOverLimit(subjectAccountModel.getPaymt09())){
                                                                                    numberOfOverLimit++;
                                                                                }
                                                                                if(isInMonthPeriodYYYYMMDD(subjectAccountModel.getPaymtdate09(), SIX_MONTH)){
                                                                                    worstCode = getWorstCode(subjectAccountModel.getPaymt09(), worstCode);
                                                                                    if(isOutStandingPayment(subjectAccountModel.getPaymt09())){
                                                                                        numberOfOutStandingPayment++;
                                                                                    }
                                                                                    if(isNPLIndividual(subjectAccountModel.getPaymt09())){
                                                                                        if(isTMBAccount){
                                                                                            isNPLTMB = true;
                                                                                            if(Util.isEmpty(lastNPLDateTMB)){
                                                                                                lastNPLDateTMB = subjectAccountModel.getPaymtdate09();
                                                                                            } else {
                                                                                                lastNPLDateTMB = getLastDateYYYYMMDD(lastNPLDateTMB,subjectAccountModel.getPaymtdate09());
                                                                                            }
                                                                                        } else {
                                                                                            isNPLOther = true;
                                                                                            if(Util.isEmpty(lastNPLDateOther)){
                                                                                                lastNPLDateOther = subjectAccountModel.getPaymtdate09();
                                                                                            } else {
                                                                                                lastNPLDateOther = getLastDateYYYYMMDD(lastNPLDateOther,subjectAccountModel.getPaymtdate09());
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                }
                                                                                if(isInMonthPeriodYYYYMMDD(subjectAccountModel.getPaymtdate10(), TWELVE_MONTH)){
                                                                                    if(isOverLimit(subjectAccountModel.getPaymt10())){
                                                                                        numberOfOverLimit++;
                                                                                    }
                                                                                    if(isInMonthPeriodYYYYMMDD(subjectAccountModel.getPaymtdate10(), SIX_MONTH)){
                                                                                        worstCode = getWorstCode(subjectAccountModel.getPaymt10(), worstCode);
                                                                                        if(isOutStandingPayment(subjectAccountModel.getPaymt10())){
                                                                                            numberOfOutStandingPayment++;
                                                                                        }
                                                                                        if(isNPLIndividual(subjectAccountModel.getPaymt10())){
                                                                                            if(isTMBAccount){
                                                                                                isNPLTMB = true;
                                                                                                if(Util.isEmpty(lastNPLDateTMB)){
                                                                                                    lastNPLDateTMB = subjectAccountModel.getPaymtdate10();
                                                                                                } else {
                                                                                                    lastNPLDateTMB = getLastDateYYYYMMDD(lastNPLDateTMB,subjectAccountModel.getPaymtdate10());
                                                                                                }
                                                                                            } else {
                                                                                                isNPLOther = true;
                                                                                                if(Util.isEmpty(lastNPLDateOther)){
                                                                                                    lastNPLDateOther = subjectAccountModel.getPaymtdate10();
                                                                                                } else {
                                                                                                    lastNPLDateOther = getLastDateYYYYMMDD(lastNPLDateOther,subjectAccountModel.getPaymtdate10());
                                                                                                }
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                    if(isInMonthPeriodYYYYMMDD(subjectAccountModel.getPaymtdate11(), TWELVE_MONTH)){
                                                                                        if(isOverLimit(subjectAccountModel.getPaymt11())){
                                                                                            numberOfOverLimit++;
                                                                                        }
                                                                                        if(isInMonthPeriodYYYYMMDD(subjectAccountModel.getPaymtdate11(), SIX_MONTH)){
                                                                                            worstCode = getWorstCode(subjectAccountModel.getPaymt11(), worstCode);
                                                                                            if(isOutStandingPayment(subjectAccountModel.getPaymt11())){
                                                                                                numberOfOutStandingPayment++;
                                                                                            }
                                                                                            if(isNPLIndividual(subjectAccountModel.getPaymt11())){
                                                                                                if(isTMBAccount){
                                                                                                    isNPLTMB = true;
                                                                                                    if(Util.isEmpty(lastNPLDateTMB)){
                                                                                                        lastNPLDateTMB = subjectAccountModel.getPaymtdate11();
                                                                                                    } else {
                                                                                                        lastNPLDateTMB = getLastDateYYYYMMDD(lastNPLDateTMB,subjectAccountModel.getPaymtdate11());
                                                                                                    }
                                                                                                } else {
                                                                                                    isNPLOther = true;
                                                                                                    if(Util.isEmpty(lastNPLDateOther)){
                                                                                                        lastNPLDateOther = subjectAccountModel.getPaymtdate11();
                                                                                                    } else {
                                                                                                        lastNPLDateOther = getLastDateYYYYMMDD(lastNPLDateOther,subjectAccountModel.getPaymtdate11());
                                                                                                    }
                                                                                                }
                                                                                            }
                                                                                        }
                                                                                        if(isInMonthPeriodYYYYMMDD(subjectAccountModel.getPaymtdate12(), TWELVE_MONTH)){
                                                                                            if(isOverLimit(subjectAccountModel.getPaymt12())){
                                                                                                numberOfOverLimit++;
                                                                                            }
                                                                                            if(isInMonthPeriodYYYYMMDD(subjectAccountModel.getPaymtdate12(), SIX_MONTH)){
                                                                                                worstCode = getWorstCode(subjectAccountModel.getPaymt12(), worstCode);
                                                                                                if(isOutStandingPayment(subjectAccountModel.getPaymt12())){
                                                                                                    numberOfOutStandingPayment++;
                                                                                                }
                                                                                                if(isNPLIndividual(subjectAccountModel.getPaymt12())){
                                                                                                    if(isTMBAccount){
                                                                                                        isNPLTMB = true;
                                                                                                        if(Util.isEmpty(lastNPLDateTMB)){
                                                                                                            lastNPLDateTMB = subjectAccountModel.getPaymtdate12();
                                                                                                        } else {
                                                                                                            lastNPLDateTMB = getLastDateYYYYMMDD(lastNPLDateTMB,subjectAccountModel.getPaymtdate12());
                                                                                                        }
                                                                                                    } else {
                                                                                                        isNPLOther = true;
                                                                                                        if(Util.isEmpty(lastNPLDateOther)){
                                                                                                            lastNPLDateOther = subjectAccountModel.getPaymtdate12();
                                                                                                        } else {
                                                                                                            lastNPLDateOther = getLastDateYYYYMMDD(lastNPLDateOther,subjectAccountModel.getPaymtdate12());
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
                                            if(isInMonthPeriodYYYYMMDD(subjectAccountModel.getPaymtdate01(), TWELVE_MONTH)){
                                                worstCode = subjectAccountModel.getPaymt01();
                                                if(isOutStandingPayment(subjectAccountModel.getPaymt01())){
                                                    numberOfOutStandingPayment++;
                                                }
                                                if(isOverLimit(subjectAccountModel.getPaymt01())){
                                                    numberOfOverLimit++;
                                                }
                                                if(isNPLIndividual(subjectAccountModel.getPaymt01())){
                                                    if(isTMBAccount){
                                                        isNPLTMB = true;
                                                        if(Util.isEmpty(lastNPLDateTMB)){
                                                            lastNPLDateTMB = subjectAccountModel.getPaymtdate01();
                                                        } else {
                                                            lastNPLDateTMB = getLastDateYYYYMMDD(lastNPLDateTMB,subjectAccountModel.getPaymtdate01());
                                                        }
                                                    } else {
                                                        isNPLOther = true;
                                                        if(Util.isEmpty(lastNPLDateOther)){
                                                            lastNPLDateOther = subjectAccountModel.getPaymtdate01();
                                                        } else {
                                                            lastNPLDateOther = getLastDateYYYYMMDD(lastNPLDateOther,subjectAccountModel.getPaymtdate01());
                                                        }
                                                    }
                                                }
                                                if(isInMonthPeriodYYYYMMDD(subjectAccountModel.getPaymtdate02(), TWELVE_MONTH)){
                                                    worstCode = getWorstCode(subjectAccountModel.getPaymt02(),worstCode);
                                                    if(isOutStandingPayment(subjectAccountModel.getPaymt02())){
                                                        numberOfOutStandingPayment++;
                                                    }
                                                    if(isOverLimit(subjectAccountModel.getPaymt02())){
                                                        numberOfOverLimit++;
                                                    }
                                                    if(isNPLIndividual(subjectAccountModel.getPaymt02())){
                                                        if(isTMBAccount){
                                                            isNPLTMB = true;
                                                            if(Util.isEmpty(lastNPLDateTMB)){
                                                                lastNPLDateTMB = subjectAccountModel.getPaymtdate02();
                                                            } else {
                                                                lastNPLDateTMB = getLastDateYYYYMMDD(lastNPLDateTMB,subjectAccountModel.getPaymtdate02());
                                                            }
                                                        } else {
                                                            isNPLOther = true;
                                                            if(Util.isEmpty(lastNPLDateOther)){
                                                                lastNPLDateOther = subjectAccountModel.getPaymtdate02();
                                                            } else {
                                                                lastNPLDateOther = getLastDateYYYYMMDD(lastNPLDateOther,subjectAccountModel.getPaymtdate02());
                                                            }
                                                        }
                                                    }
                                                    if(isInMonthPeriodYYYYMMDD(subjectAccountModel.getPaymtdate03(), TWELVE_MONTH)){
                                                        worstCode = getWorstCode(subjectAccountModel.getPaymt03(),worstCode);
                                                        if(isOutStandingPayment(subjectAccountModel.getPaymt03())){
                                                            numberOfOutStandingPayment++;
                                                        }
                                                        if(isOverLimit(subjectAccountModel.getPaymt03())){
                                                            numberOfOverLimit++;
                                                        }
                                                        if(isNPLIndividual(subjectAccountModel.getPaymt03())){
                                                            if(isTMBAccount){
                                                                isNPLTMB = true;
                                                                if(Util.isEmpty(lastNPLDateTMB)){
                                                                    lastNPLDateTMB = subjectAccountModel.getPaymtdate03();
                                                                } else {
                                                                    lastNPLDateTMB = getLastDateYYYYMMDD(lastNPLDateTMB,subjectAccountModel.getPaymtdate03());
                                                                }
                                                            } else {
                                                                isNPLOther = true;
                                                                if(Util.isEmpty(lastNPLDateOther)){
                                                                    lastNPLDateOther = subjectAccountModel.getPaymtdate03();
                                                                } else {
                                                                    lastNPLDateOther = getLastDateYYYYMMDD(lastNPLDateOther,subjectAccountModel.getPaymtdate03());
                                                                }
                                                            }
                                                        }
                                                        if(isInMonthPeriodYYYYMMDD(subjectAccountModel.getPaymtdate04(), TWELVE_MONTH)){
                                                            worstCode = getWorstCode(subjectAccountModel.getPaymt04(),worstCode);
                                                            if(isOutStandingPayment(subjectAccountModel.getPaymt04())){
                                                                numberOfOutStandingPayment++;
                                                            }
                                                            if(isOverLimit(subjectAccountModel.getPaymt04())){
                                                                numberOfOverLimit++;
                                                            }
                                                            if(isNPLIndividual(subjectAccountModel.getPaymt04())){
                                                                if(isTMBAccount){
                                                                    isNPLTMB = true;
                                                                    if(Util.isEmpty(lastNPLDateTMB)){
                                                                        lastNPLDateTMB = subjectAccountModel.getPaymtdate04();
                                                                    } else {
                                                                        lastNPLDateTMB = getLastDateYYYYMMDD(lastNPLDateTMB,subjectAccountModel.getPaymtdate04());
                                                                    }
                                                                } else {
                                                                    isNPLOther = true;
                                                                    if(Util.isEmpty(lastNPLDateOther)){
                                                                        lastNPLDateOther = subjectAccountModel.getPaymtdate04();
                                                                    } else {
                                                                        lastNPLDateOther = getLastDateYYYYMMDD(lastNPLDateOther,subjectAccountModel.getPaymtdate04());
                                                                    }
                                                                }
                                                            }
                                                            if(isInMonthPeriodYYYYMMDD(subjectAccountModel.getPaymtdate05(), TWELVE_MONTH)){
                                                                worstCode = getWorstCode(subjectAccountModel.getPaymt05(),worstCode);
                                                                if(isOutStandingPayment(subjectAccountModel.getPaymt05())){
                                                                    numberOfOutStandingPayment++;
                                                                }
                                                                if(isOverLimit(subjectAccountModel.getPaymt05())){
                                                                    numberOfOverLimit++;
                                                                }
                                                                if(isNPLIndividual(subjectAccountModel.getPaymt05())){
                                                                    if(isTMBAccount){
                                                                        isNPLTMB = true;
                                                                        if(Util.isEmpty(lastNPLDateTMB)){
                                                                            lastNPLDateTMB = subjectAccountModel.getPaymtdate05();
                                                                        } else {
                                                                            lastNPLDateTMB = getLastDateYYYYMMDD(lastNPLDateTMB,subjectAccountModel.getPaymtdate05());
                                                                        }
                                                                    } else {
                                                                        isNPLOther = true;
                                                                        if(Util.isEmpty(lastNPLDateOther)){
                                                                            lastNPLDateOther = subjectAccountModel.getPaymtdate05();
                                                                        } else {
                                                                            lastNPLDateOther = getLastDateYYYYMMDD(lastNPLDateOther,subjectAccountModel.getPaymtdate05());
                                                                        }
                                                                    }
                                                                }
                                                                if(isInMonthPeriodYYYYMMDD(subjectAccountModel.getPaymtdate06(), TWELVE_MONTH)){
                                                                    worstCode = getWorstCode(subjectAccountModel.getPaymt06(),worstCode);
                                                                    if(isOutStandingPayment(subjectAccountModel.getPaymt06())){
                                                                        numberOfOutStandingPayment++;
                                                                    }
                                                                    if(isOverLimit(subjectAccountModel.getPaymt06())){
                                                                        numberOfOverLimit++;
                                                                    }
                                                                    if(isNPLIndividual(subjectAccountModel.getPaymt06())){
                                                                        if(isTMBAccount){
                                                                            isNPLTMB = true;
                                                                            if(Util.isEmpty(lastNPLDateTMB)){
                                                                                lastNPLDateTMB = subjectAccountModel.getPaymtdate06();
                                                                            } else {
                                                                                lastNPLDateTMB = getLastDateYYYYMMDD(lastNPLDateTMB,subjectAccountModel.getPaymtdate06());
                                                                            }
                                                                        } else {
                                                                            isNPLOther = true;
                                                                            if(Util.isEmpty(lastNPLDateOther)){
                                                                                lastNPLDateOther = subjectAccountModel.getPaymtdate06();
                                                                            } else {
                                                                                lastNPLDateOther = getLastDateYYYYMMDD(lastNPLDateOther,subjectAccountModel.getPaymtdate06());
                                                                            }
                                                                        }
                                                                    }
                                                                    if(isInMonthPeriodYYYYMMDD(subjectAccountModel.getPaymtdate07(), TWELVE_MONTH)){
                                                                        worstCode = getWorstCode(subjectAccountModel.getPaymt07(),worstCode);
                                                                        if(isOutStandingPayment(subjectAccountModel.getPaymt07())){
                                                                            numberOfOutStandingPayment++;
                                                                        }
                                                                        if(isOverLimit(subjectAccountModel.getPaymt07())){
                                                                            numberOfOverLimit++;
                                                                        }
                                                                        if(isNPLIndividual(subjectAccountModel.getPaymt07())){
                                                                            if(isTMBAccount){
                                                                                isNPLTMB = true;
                                                                                if(Util.isEmpty(lastNPLDateTMB)){
                                                                                    lastNPLDateTMB = subjectAccountModel.getPaymtdate07();
                                                                                } else {
                                                                                    lastNPLDateTMB = getLastDateYYYYMMDD(lastNPLDateTMB,subjectAccountModel.getPaymtdate07());
                                                                                }
                                                                            } else {
                                                                                isNPLOther = true;
                                                                                if(Util.isEmpty(lastNPLDateOther)){
                                                                                    lastNPLDateOther = subjectAccountModel.getPaymtdate07();
                                                                                } else {
                                                                                    lastNPLDateOther = getLastDateYYYYMMDD(lastNPLDateOther,subjectAccountModel.getPaymtdate07());
                                                                                }
                                                                            }
                                                                        }
                                                                        if(isInMonthPeriodYYYYMMDD(subjectAccountModel.getPaymtdate08(), TWELVE_MONTH)){
                                                                            worstCode = getWorstCode(subjectAccountModel.getPaymt08(),worstCode);
                                                                            if(isOutStandingPayment(subjectAccountModel.getPaymt08())){
                                                                                numberOfOutStandingPayment++;
                                                                            }
                                                                            if(isOverLimit(subjectAccountModel.getPaymt08())){
                                                                                numberOfOverLimit++;
                                                                            }
                                                                            if(isNPLIndividual(subjectAccountModel.getPaymt08())){
                                                                                if(isTMBAccount){
                                                                                    isNPLTMB = true;
                                                                                    if(Util.isEmpty(lastNPLDateTMB)){
                                                                                        lastNPLDateTMB = subjectAccountModel.getPaymtdate08();
                                                                                    } else {
                                                                                        lastNPLDateTMB = getLastDateYYYYMMDD(lastNPLDateTMB,subjectAccountModel.getPaymtdate08());
                                                                                    }
                                                                                } else {
                                                                                    isNPLOther = true;
                                                                                    if(Util.isEmpty(lastNPLDateOther)){
                                                                                        lastNPLDateOther = subjectAccountModel.getPaymtdate08();
                                                                                    } else {
                                                                                        lastNPLDateOther = getLastDateYYYYMMDD(lastNPLDateOther,subjectAccountModel.getPaymtdate08());
                                                                                    }
                                                                                }
                                                                            }
                                                                            if(isInMonthPeriodYYYYMMDD(subjectAccountModel.getPaymtdate09(), TWELVE_MONTH)){
                                                                                worstCode = getWorstCode(subjectAccountModel.getPaymt09(),worstCode);
                                                                                if(isOutStandingPayment(subjectAccountModel.getPaymt09())){
                                                                                    numberOfOutStandingPayment++;
                                                                                }
                                                                                if(isOverLimit(subjectAccountModel.getPaymt09())){
                                                                                    numberOfOverLimit++;
                                                                                }
                                                                                if(isNPLIndividual(subjectAccountModel.getPaymt09())){
                                                                                    if(isTMBAccount){
                                                                                        isNPLTMB = true;
                                                                                        if(Util.isEmpty(lastNPLDateTMB)){
                                                                                            lastNPLDateTMB = subjectAccountModel.getPaymtdate09();
                                                                                        } else {
                                                                                            lastNPLDateTMB = getLastDateYYYYMMDD(lastNPLDateTMB,subjectAccountModel.getPaymtdate09());
                                                                                        }
                                                                                    } else {
                                                                                        isNPLOther = true;
                                                                                        if(Util.isEmpty(lastNPLDateOther)){
                                                                                            lastNPLDateOther = subjectAccountModel.getPaymtdate09();
                                                                                        } else {
                                                                                            lastNPLDateOther = getLastDateYYYYMMDD(lastNPLDateOther,subjectAccountModel.getPaymtdate09());
                                                                                        }
                                                                                    }
                                                                                }
                                                                                if(isInMonthPeriodYYYYMMDD(subjectAccountModel.getPaymtdate10(), TWELVE_MONTH)){
                                                                                    worstCode = getWorstCode(subjectAccountModel.getPaymt10(),worstCode);
                                                                                    if(isOutStandingPayment(subjectAccountModel.getPaymt10())){
                                                                                        numberOfOutStandingPayment++;
                                                                                    }
                                                                                    if(isOverLimit(subjectAccountModel.getPaymt10())){
                                                                                        numberOfOverLimit++;
                                                                                    }
                                                                                    if(isNPLIndividual(subjectAccountModel.getPaymt10())){
                                                                                        if(isTMBAccount){
                                                                                            isNPLTMB = true;
                                                                                            if(Util.isEmpty(lastNPLDateTMB)){
                                                                                                lastNPLDateTMB = subjectAccountModel.getPaymtdate10();
                                                                                            } else {
                                                                                                lastNPLDateTMB = getLastDateYYYYMMDD(lastNPLDateTMB,subjectAccountModel.getPaymtdate10());
                                                                                            }
                                                                                        } else {
                                                                                            isNPLOther = true;
                                                                                            if(Util.isEmpty(lastNPLDateOther)){
                                                                                                lastNPLDateOther = subjectAccountModel.getPaymtdate10();
                                                                                            } else {
                                                                                                lastNPLDateOther = getLastDateYYYYMMDD(lastNPLDateOther,subjectAccountModel.getPaymtdate10());
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                    if(isInMonthPeriodYYYYMMDD(subjectAccountModel.getPaymtdate11(), TWELVE_MONTH)){
                                                                                        worstCode = getWorstCode(subjectAccountModel.getPaymt11(),worstCode);
                                                                                        if(isOutStandingPayment(subjectAccountModel.getPaymt11())){
                                                                                            numberOfOutStandingPayment++;
                                                                                        }
                                                                                        if(isOverLimit(subjectAccountModel.getPaymt11())){
                                                                                            numberOfOverLimit++;
                                                                                        }
                                                                                        if(isNPLIndividual(subjectAccountModel.getPaymt11())){
                                                                                            if(isTMBAccount){
                                                                                                isNPLTMB = true;
                                                                                                if(Util.isEmpty(lastNPLDateTMB)){
                                                                                                    lastNPLDateTMB = subjectAccountModel.getPaymtdate11();
                                                                                                } else {
                                                                                                    lastNPLDateTMB = getLastDateYYYYMMDD(lastNPLDateTMB,subjectAccountModel.getPaymtdate11());
                                                                                                }
                                                                                            } else {
                                                                                                isNPLOther = true;
                                                                                                if(Util.isEmpty(lastNPLDateOther)){
                                                                                                    lastNPLDateOther = subjectAccountModel.getPaymtdate11();
                                                                                                } else {
                                                                                                    lastNPLDateOther = getLastDateYYYYMMDD(lastNPLDateOther,subjectAccountModel.getPaymtdate11());
                                                                                                }
                                                                                            }
                                                                                        }
                                                                                        if(isInMonthPeriodYYYYMMDD(subjectAccountModel.getPaymtdate12(), 12)){
                                                                                            worstCode = getWorstCode(subjectAccountModel.getPaymt12(),worstCode);
                                                                                            if(isOutStandingPayment(subjectAccountModel.getPaymt12())){
                                                                                                numberOfOutStandingPayment++;
                                                                                            }
                                                                                            if(isOverLimit(subjectAccountModel.getPaymt12())){
                                                                                                numberOfOverLimit++;
                                                                                            }
                                                                                            if(isNPLIndividual(subjectAccountModel.getPaymt12())){
                                                                                                if(isTMBAccount){
                                                                                                    isNPLTMB = true;
                                                                                                    if(Util.isEmpty(lastNPLDateTMB)){
                                                                                                        lastNPLDateTMB = subjectAccountModel.getPaymtdate12();
                                                                                                    } else {
                                                                                                        lastNPLDateTMB = getLastDateYYYYMMDD(lastNPLDateTMB,subjectAccountModel.getPaymtdate12());
                                                                                                    }
                                                                                                } else {
                                                                                                    isNPLOther = true;
                                                                                                    if(Util.isEmpty(lastNPLDateOther)){
                                                                                                        lastNPLDateOther = subjectAccountModel.getPaymtdate12();
                                                                                                    } else {
                                                                                                        lastNPLDateOther = getLastDateYYYYMMDD(lastNPLDateOther,subjectAccountModel.getPaymtdate12());
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
                                        SettlementStatus historySettlementStatus = new SettlementStatus();
                                        if(!Util.isEmpty(worstCode)){
                                            historySettlementStatus = settlementStatusDAO.getIndividualByCode(worstCode);
                                        }
                                        ncbDetailView.setHistoryPayment(historySettlementStatus);
                                        if(!Util.isEmpty(worstPaymentStatus)){
                                            worstPaymentStatus = getWorstCode(subjectAccountModel.getPaymt01(),worstPaymentStatus);
                                        } else {
                                            worstPaymentStatus = subjectAccountModel.getPaymt01();
                                        }
                                        //set number of outstanding payment
                                        ncbDetailView.setNoOfOutstandingPaymentIn12months(new BigDecimal(numberOfOutStandingPayment));
                                        //set number of over limit
                                        ncbDetailView.setNoOfOverLimit(numberOfOverLimit);

                                        //add ncbDetailView to ncbDetailViewList
                                        ncbDetailViews.add(ncbDetailView);
                                    }
                                    ncbInfoView = new NCBInfoView();
                                    //ncbSummaryView = new NCBSummaryView();
                                    ncbInfoView.setCurrentPaymentType(currentWorstPaymentStatus);
                                    ncbInfoView.setHistoryPaymentType(worstPaymentStatus);

                                    //ncbSummaryView.setTypeOfCurrentPayment(currentWorstPaymentStatus); //todo: change view field to SettlementType
                                    //ncbSummaryView.setTypeOfHistoryPayment(worstPaymentStatus); //todo: change view field to SettlementType
                                    //todo: set NPL flag and NPL date for TMB and other
                                    //todo: set TDR flag and TDR date for TMB and other
                                } else {
                                    //no ncb detail
                                }

                                //get enquiry amount
                                int enquiryTime = 0;
                                if(enquiryModelResults.size() > 0){
                                    Map<String, String> enquiryMap = new HashMap<String, String>();  //for check duplicate enquiry time
                                    //get number of enquiry in last 6 months
                                    for(EnquiryModel enquiryModel: enquiryModelResults){
                                        if(isInMonthPeriodYYYYMMDD(enquiryModel.getEnqdate(),SIX_MONTH)){
                                            enquiryMap.put(enquiryModel.getEnqdate().concat(enquiryModel.getEnqtime()),enquiryModel.getEnqamount());
                                        }
                                    }

                                    enquiryTime = enquiryMap.size();
                                }

                                if(ncbInfoView != null){
                                    ncbInfoView.setCheckIn6Month(enquiryTime);
                                }

                                ncbView.setNCBDetailViews(ncbDetailViews);
                                ncbView.setNCBInfoView(ncbInfoView);
                                ncbView.setAccountInfoIdList(accountInfoIdList);
                                ncbView.setAccountInfoNameList(accountInfoNameList);
                            }
                        }
                    }

                } else {
                    ncbView.setResult(ActionResult.FAILED);
                    ncbView.setReason(responseNCRSModel.getReason());
                }
                ncbViews.add(ncbView);
            }
        }
        return ncbViews;
    }

    public List<NcbView> transformJuristic(List<NCCRSOutputModel> responseNCCRSModels){
        log.debug("NCBBizTransform : transformJuristic ::: responseNCCRSModels : {}");
        List<NcbView> ncbViews = null;
        List<NCBDetailView> ncbDetailViews = null;
        NCBSummaryView ncbSummaryView = null;
        if(responseNCCRSModels!=null && responseNCCRSModels.size()>0){
            ncbViews = new ArrayList<NcbView>();
            for(NCCRSOutputModel responseNCCRSModel: responseNCCRSModels){
                log.debug("NCCRSOutputModel : {}", responseNCCRSModel);
                NcbView ncbView = new NcbView();
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
                if(responseNCCRSModel.getActionResult() == ActionResult.SUCCEED){
                    ncbView.setResult(ActionResult.SUCCEED);
                    //Transform NCB Account Logic
                    if(responseNCCRSModel.getResponseModel()!=null){
                        NCCRSResponseModel nccrsResponseModel = responseNCCRSModel.getResponseModel();
                        if(nccrsResponseModel.getBody()!=null){
                            H2HResponseModel h2HResponseModel = nccrsResponseModel.getBody().getTransaction().getH2hresponse();
                            if(h2HResponseModel.getSubject()!=null){
                                H2HResponseSubjectModel h2HResponseSubjectModel = h2HResponseModel.getSubject();

                                //get Name and Id information for send csi
                                if(h2HResponseSubjectModel!=null && h2HResponseSubjectModel.getProfile()!=null) {
                                    ProfileModel profileModel = h2HResponseSubjectModel.getProfile();
                                    AccountInfoName accountInfoName2 = new AccountInfoName();
                                    AccountInfoId accountInfoId2 = new AccountInfoId();

                                    accountInfoName2.setNameTh(profileModel.getThainame());
                                    accountInfoName2.setNameTh(profileModel.getEngname());
                                    accountInfoId2.setIdNumber(profileModel.getRegistid());
                                    accountInfoId2.setDocumentType(DocumentType.CORPORATE_ID);
                                    accountInfoNameList.add(accountInfoName2);
                                    accountInfoIdList.add(accountInfoId2);

                                    if(h2HResponseSubjectModel.getProfile().getAdditional()!=null){
                                        AdditionalModel additionalModel = h2HResponseSubjectModel.getProfile().getAdditional();
                                        if(additionalModel.getName()!=null && additionalModel.getName().size()>0){
                                            for(ProfileNameModel profileNameModel: additionalModel.getName()){
                                                AccountInfoName ncbAccountInfoName = new AccountInfoName();
                                                ncbAccountInfoName.setNameTh(profileNameModel.getThainame());
                                                ncbAccountInfoName.setSurnameTh(profileNameModel.getEngname());
                                                accountInfoNameList.add(ncbAccountInfoName);
                                            }
                                        }
                                    }
                                }

                                //get list Account for active,closed
                                List<AccountModel> accountModels = new ArrayList<AccountModel>();
                                boolean haveActiveAccountData = false;
                                boolean haveClosedAccountData = false;
                                if(h2HResponseSubjectModel.getActiveaccounts()!=null){
                                    if(h2HResponseSubjectModel.getActiveaccounts().getAccount()!=null && h2HResponseSubjectModel.getActiveaccounts().getAccount().size()>0){
                                        haveActiveAccountData = true;
                                    }
                                }
                                if(h2HResponseSubjectModel.getClosedaccounts()!=null){
                                    if(h2HResponseSubjectModel.getClosedaccounts().getAccount()!=null && h2HResponseSubjectModel.getClosedaccounts().getAccount().size()>0){
                                        haveClosedAccountData = true;
                                    }
                                }

                                if(haveActiveAccountData || haveClosedAccountData){
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

                                    //check for active account
                                    if(haveActiveAccountData){
                                        for(AccountModel accountModel : h2HResponseSubjectModel.getActiveaccounts().getAccount()){
                                            boolean isTMBAccount = false;

                                        }
                                    }
                                } else {
                                    //no account detail data
                                }

                                /*if(subjectAccountModelResults.size() > 0){
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
                                    for(SubjectAccountModel subjectAccountModel : subjectAccountModelResults){
                                        boolean isTMBAccount = false;
                                        NCBDetailView ncbDetailView = new NCBDetailView();
                                        //set accountType
                                        AccountType accountType = accountTypeDAO.getIndividualByCode(subjectAccountModel.getAccounttype());
                                        ncbDetailView.setAccountType(accountType);
                                        //set tmb account
                                        ncbDetailView.setTMBAccount(false);
                                        if(subjectAccountModel.getShortname().equals(TMB_BANK)){ //todo: change to master
                                            ncbDetailView.setTMBAccount(true);
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
                                        ncbDetailView.setLimit(new BigDecimal(subjectAccountModel.getCreditlimit())); //todo: check if null
                                        //set outstanding amount
                                        ncbDetailView.setOutstanding(new BigDecimal(subjectAccountModel.getAmountowed())); //todo: check if null
                                        //set installment
                                        ncbDetailView.setInstallment(new BigDecimal(subjectAccountModel.getInstallmentamount())); //todo: check if null
                                        //set restructure date
                                        if(!Util.isEmpty(subjectAccountModel.getLastrestructureddate())){
                                            ncbDetailView.setDateOfDebtRestructuring(Util.strYYYYMMDDtoDateFormat(subjectAccountModel.getLastrestructureddate()));
                                            //get TDR last date
                                            if(isTMBAccount){
                                                isTDRTMB = true;
                                                if(!Util.isEmpty(lastTDRDateTMB)){
                                                    lastTDRDateTMB = subjectAccountModel.getCloseddate();
                                                } else {
                                                    lastTDRDateTMB = getLastDateYYYYMMDD(lastTDRDateTMB,subjectAccountModel.getCloseddate());
                                                }
                                            } else {
                                                isTDROther = true;
                                                if(!Util.isEmpty(lastTDRDateOther)){
                                                    lastTDRDateOther = subjectAccountModel.getCloseddate();
                                                } else {
                                                    lastTDRDateOther = getLastDateYYYYMMDD(lastTDRDateOther,subjectAccountModel.getCloseddate());
                                                }
                                            }
                                        }
                                        //set current payment
                                        SettlementStatus settlementStatus = new SettlementStatus();
                                        if(!Util.isEmpty(subjectAccountModel.getPaymt01())){
                                            settlementStatus = settlementStatusDAO.getIndividualByCode(subjectAccountModel.getPaymt01());
                                        }
                                        ncbDetailView.setCurrentPayment(settlementStatus);
                                        if(!Util.isEmpty(currentWorstPaymentStatus)){
                                            currentWorstPaymentStatus = getWorstCode(subjectAccountModel.getPaymt01(),currentWorstPaymentStatus);
                                        } else {
                                            currentWorstPaymentStatus = subjectAccountModel.getPaymt01();
                                        }

                                        //check for last 6,12 months for get worst payment, calculate number of outstanding and number of over limit
                                        String worstCode = null;
                                        int numberOfOutStandingPayment = 0;
                                        int numberOfOverLimit = 0;
                                        if(!Util.isEmpty(subjectAccountModel.getAccountstatus()) && subjectAccountModel.getAccountstatus().equals("04")){
                                            if(isInMonthPeriodYYYYMMDD(subjectAccountModel.getPaymtdate01(), TWELVE_MONTH)){
                                                if(isOverLimit(subjectAccountModel.getPaymt01())){
                                                    numberOfOverLimit++;
                                                }
                                                if(isInMonthPeriodYYYYMMDD(subjectAccountModel.getPaymtdate01(), SIX_MONTH)){
                                                    worstCode = subjectAccountModel.getPaymt01();
                                                    if(isOutStandingPayment(subjectAccountModel.getPaymt01())){
                                                        numberOfOutStandingPayment++;
                                                    }
                                                    if(isNPLIndividual(subjectAccountModel.getPaymt01())){
                                                        if(isTMBAccount){
                                                            isNPLTMB = true;
                                                            if(Util.isEmpty(lastNPLDateTMB)){
                                                                lastNPLDateTMB = subjectAccountModel.getPaymtdate01();
                                                            } else {
                                                                lastNPLDateTMB = getLastDateYYYYMMDD(lastNPLDateTMB,subjectAccountModel.getPaymtdate01());
                                                            }
                                                        } else {
                                                            isNPLOther = true;
                                                            if(Util.isEmpty(lastNPLDateOther)){
                                                                lastNPLDateOther = subjectAccountModel.getPaymtdate01();
                                                            } else {
                                                                lastNPLDateOther = getLastDateYYYYMMDD(lastNPLDateOther,subjectAccountModel.getPaymtdate01());
                                                            }
                                                        }
                                                    }
                                                }
                                                if(isInMonthPeriodYYYYMMDD(subjectAccountModel.getPaymtdate02(), TWELVE_MONTH)){
                                                    if(isOverLimit(subjectAccountModel.getPaymt02())){
                                                        numberOfOverLimit++;
                                                    }
                                                    if(isInMonthPeriodYYYYMMDD(subjectAccountModel.getPaymtdate02(), SIX_MONTH)){
                                                        worstCode = getWorstCode(subjectAccountModel.getPaymt02(), worstCode);
                                                        if(isOutStandingPayment(subjectAccountModel.getPaymt02())){
                                                            numberOfOutStandingPayment++;
                                                        }
                                                        if(isNPLIndividual(subjectAccountModel.getPaymt02())){
                                                            if(isTMBAccount){
                                                                isNPLTMB = true;
                                                                if(Util.isEmpty(lastNPLDateTMB)){
                                                                    lastNPLDateTMB = subjectAccountModel.getPaymtdate02();
                                                                } else {
                                                                    lastNPLDateTMB = getLastDateYYYYMMDD(lastNPLDateTMB,subjectAccountModel.getPaymtdate02());
                                                                }
                                                            } else {
                                                                isNPLOther = true;
                                                                if(Util.isEmpty(lastNPLDateOther)){
                                                                    lastNPLDateOther = subjectAccountModel.getPaymtdate02();
                                                                } else {
                                                                    lastNPLDateOther = getLastDateYYYYMMDD(lastNPLDateOther,subjectAccountModel.getPaymtdate02());
                                                                }
                                                            }
                                                        }
                                                    }
                                                    if(isInMonthPeriodYYYYMMDD(subjectAccountModel.getPaymtdate03(), TWELVE_MONTH)){
                                                        if(isOverLimit(subjectAccountModel.getPaymt03())){
                                                            numberOfOverLimit++;
                                                        }
                                                        if(isInMonthPeriodYYYYMMDD(subjectAccountModel.getPaymtdate03(), SIX_MONTH)){
                                                            worstCode = getWorstCode(subjectAccountModel.getPaymt03(), worstCode);
                                                            if(isOutStandingPayment(subjectAccountModel.getPaymt03())){
                                                                numberOfOutStandingPayment++;
                                                            }
                                                            if(isNPLIndividual(subjectAccountModel.getPaymt03())){
                                                                if(isTMBAccount){
                                                                    isNPLTMB = true;
                                                                    if(Util.isEmpty(lastNPLDateTMB)){
                                                                        lastNPLDateTMB = subjectAccountModel.getPaymtdate03();
                                                                    } else {
                                                                        lastNPLDateTMB = getLastDateYYYYMMDD(lastNPLDateTMB,subjectAccountModel.getPaymtdate03());
                                                                    }
                                                                } else {
                                                                    isNPLOther = true;
                                                                    if(Util.isEmpty(lastNPLDateOther)){
                                                                        lastNPLDateOther = subjectAccountModel.getPaymtdate03();
                                                                    } else {
                                                                        lastNPLDateOther = getLastDateYYYYMMDD(lastNPLDateOther,subjectAccountModel.getPaymtdate03());
                                                                    }
                                                                }
                                                            }
                                                        }
                                                        if(isInMonthPeriodYYYYMMDD(subjectAccountModel.getPaymtdate04(), TWELVE_MONTH)){
                                                            if(isOverLimit(subjectAccountModel.getPaymt04())){
                                                                numberOfOverLimit++;
                                                            }
                                                            if(isInMonthPeriodYYYYMMDD(subjectAccountModel.getPaymtdate04(), SIX_MONTH)){
                                                                worstCode = getWorstCode(subjectAccountModel.getPaymt04(), worstCode);
                                                                if(isOutStandingPayment(subjectAccountModel.getPaymt04())){
                                                                    numberOfOutStandingPayment++;
                                                                }
                                                                if(isNPLIndividual(subjectAccountModel.getPaymt04())){
                                                                    if(isTMBAccount){
                                                                        isNPLTMB = true;
                                                                        if(Util.isEmpty(lastNPLDateTMB)){
                                                                            lastNPLDateTMB = subjectAccountModel.getPaymtdate04();
                                                                        } else {
                                                                            lastNPLDateTMB = getLastDateYYYYMMDD(lastNPLDateTMB,subjectAccountModel.getPaymtdate04());
                                                                        }
                                                                    } else {
                                                                        isNPLOther = true;
                                                                        if(Util.isEmpty(lastNPLDateOther)){
                                                                            lastNPLDateOther = subjectAccountModel.getPaymtdate04();
                                                                        } else {
                                                                            lastNPLDateOther = getLastDateYYYYMMDD(lastNPLDateOther,subjectAccountModel.getPaymtdate04());
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                            if(isInMonthPeriodYYYYMMDD(subjectAccountModel.getPaymtdate05(), TWELVE_MONTH)){
                                                                if(isOverLimit(subjectAccountModel.getPaymt05())){
                                                                    numberOfOverLimit++;
                                                                }
                                                                if(isInMonthPeriodYYYYMMDD(subjectAccountModel.getPaymtdate05(), SIX_MONTH)){
                                                                    worstCode = getWorstCode(subjectAccountModel.getPaymt05(), worstCode);
                                                                    if(isOutStandingPayment(subjectAccountModel.getPaymt05())){
                                                                        numberOfOutStandingPayment++;
                                                                    }
                                                                    if(isNPLIndividual(subjectAccountModel.getPaymt05())){
                                                                        if(isTMBAccount){
                                                                            isNPLTMB = true;
                                                                            if(Util.isEmpty(lastNPLDateTMB)){
                                                                                lastNPLDateTMB = subjectAccountModel.getPaymtdate05();
                                                                            } else {
                                                                                lastNPLDateTMB = getLastDateYYYYMMDD(lastNPLDateTMB,subjectAccountModel.getPaymtdate05());
                                                                            }
                                                                        } else {
                                                                            isNPLOther = true;
                                                                            if(Util.isEmpty(lastNPLDateOther)){
                                                                                lastNPLDateOther = subjectAccountModel.getPaymtdate05();
                                                                            } else {
                                                                                lastNPLDateOther = getLastDateYYYYMMDD(lastNPLDateOther,subjectAccountModel.getPaymtdate05());
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                                if(isInMonthPeriodYYYYMMDD(subjectAccountModel.getPaymtdate06(), TWELVE_MONTH)){
                                                                    if(isOverLimit(subjectAccountModel.getPaymt06())){
                                                                        numberOfOverLimit++;
                                                                    }
                                                                    if(isInMonthPeriodYYYYMMDD(subjectAccountModel.getPaymtdate06(), SIX_MONTH)){
                                                                        worstCode = getWorstCode(subjectAccountModel.getPaymt06(), worstCode);
                                                                        if(isOutStandingPayment(subjectAccountModel.getPaymt06())){
                                                                            numberOfOutStandingPayment++;
                                                                        }
                                                                        if(isNPLIndividual(subjectAccountModel.getPaymt06())){
                                                                            if(isTMBAccount){
                                                                                isNPLTMB = true;
                                                                                if(Util.isEmpty(lastNPLDateTMB)){
                                                                                    lastNPLDateTMB = subjectAccountModel.getPaymtdate06();
                                                                                } else {
                                                                                    lastNPLDateTMB = getLastDateYYYYMMDD(lastNPLDateTMB,subjectAccountModel.getPaymtdate06());
                                                                                }
                                                                            } else {
                                                                                isNPLOther = true;
                                                                                if(Util.isEmpty(lastNPLDateOther)){
                                                                                    lastNPLDateOther = subjectAccountModel.getPaymtdate06();
                                                                                } else {
                                                                                    lastNPLDateOther = getLastDateYYYYMMDD(lastNPLDateOther,subjectAccountModel.getPaymtdate06());
                                                                                }
                                                                            }
                                                                        }
                                                                    }
                                                                    if(isInMonthPeriodYYYYMMDD(subjectAccountModel.getPaymtdate07(), TWELVE_MONTH)){
                                                                        if(isOverLimit(subjectAccountModel.getPaymt07())){
                                                                            numberOfOverLimit++;
                                                                        }
                                                                        if(isInMonthPeriodYYYYMMDD(subjectAccountModel.getPaymtdate07(), SIX_MONTH)){
                                                                            worstCode = getWorstCode(subjectAccountModel.getPaymt07(), worstCode);
                                                                            if(isOutStandingPayment(subjectAccountModel.getPaymt07())){
                                                                                numberOfOutStandingPayment++;
                                                                            }
                                                                            if(isNPLIndividual(subjectAccountModel.getPaymt07())){
                                                                                if(isTMBAccount){
                                                                                    isNPLTMB = true;
                                                                                    if(Util.isEmpty(lastNPLDateTMB)){
                                                                                        lastNPLDateTMB = subjectAccountModel.getPaymtdate07();
                                                                                    } else {
                                                                                        lastNPLDateTMB = getLastDateYYYYMMDD(lastNPLDateTMB,subjectAccountModel.getPaymtdate07());
                                                                                    }
                                                                                } else {
                                                                                    isNPLOther = true;
                                                                                    if(Util.isEmpty(lastNPLDateOther)){
                                                                                        lastNPLDateOther = subjectAccountModel.getPaymtdate07();
                                                                                    } else {
                                                                                        lastNPLDateOther = getLastDateYYYYMMDD(lastNPLDateOther,subjectAccountModel.getPaymtdate07());
                                                                                    }
                                                                                }
                                                                            }
                                                                        }
                                                                        if(isInMonthPeriodYYYYMMDD(subjectAccountModel.getPaymtdate08(), TWELVE_MONTH)){
                                                                            if(isOverLimit(subjectAccountModel.getPaymt08())){
                                                                                numberOfOverLimit++;
                                                                            }
                                                                            if(isInMonthPeriodYYYYMMDD(subjectAccountModel.getPaymtdate08(), SIX_MONTH)){
                                                                                worstCode = getWorstCode(subjectAccountModel.getPaymt08(), worstCode);
                                                                                if(isOutStandingPayment(subjectAccountModel.getPaymt08())){
                                                                                    numberOfOutStandingPayment++;
                                                                                }
                                                                                if(isNPLIndividual(subjectAccountModel.getPaymt08())){
                                                                                    if(isTMBAccount){
                                                                                        isNPLTMB = true;
                                                                                        if(Util.isEmpty(lastNPLDateTMB)){
                                                                                            lastNPLDateTMB = subjectAccountModel.getPaymtdate08();
                                                                                        } else {
                                                                                            lastNPLDateTMB = getLastDateYYYYMMDD(lastNPLDateTMB,subjectAccountModel.getPaymtdate08());
                                                                                        }
                                                                                    } else {
                                                                                        isNPLOther = true;
                                                                                        if(Util.isEmpty(lastNPLDateOther)){
                                                                                            lastNPLDateOther = subjectAccountModel.getPaymtdate08();
                                                                                        } else {
                                                                                            lastNPLDateOther = getLastDateYYYYMMDD(lastNPLDateOther,subjectAccountModel.getPaymtdate08());
                                                                                        }
                                                                                    }
                                                                                }
                                                                            }
                                                                            if(isInMonthPeriodYYYYMMDD(subjectAccountModel.getPaymtdate09(), TWELVE_MONTH)){
                                                                                if(isOverLimit(subjectAccountModel.getPaymt09())){
                                                                                    numberOfOverLimit++;
                                                                                }
                                                                                if(isInMonthPeriodYYYYMMDD(subjectAccountModel.getPaymtdate09(), SIX_MONTH)){
                                                                                    worstCode = getWorstCode(subjectAccountModel.getPaymt09(), worstCode);
                                                                                    if(isOutStandingPayment(subjectAccountModel.getPaymt09())){
                                                                                        numberOfOutStandingPayment++;
                                                                                    }
                                                                                    if(isNPLIndividual(subjectAccountModel.getPaymt09())){
                                                                                        if(isTMBAccount){
                                                                                            isNPLTMB = true;
                                                                                            if(Util.isEmpty(lastNPLDateTMB)){
                                                                                                lastNPLDateTMB = subjectAccountModel.getPaymtdate09();
                                                                                            } else {
                                                                                                lastNPLDateTMB = getLastDateYYYYMMDD(lastNPLDateTMB,subjectAccountModel.getPaymtdate09());
                                                                                            }
                                                                                        } else {
                                                                                            isNPLOther = true;
                                                                                            if(Util.isEmpty(lastNPLDateOther)){
                                                                                                lastNPLDateOther = subjectAccountModel.getPaymtdate09();
                                                                                            } else {
                                                                                                lastNPLDateOther = getLastDateYYYYMMDD(lastNPLDateOther,subjectAccountModel.getPaymtdate09());
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                }
                                                                                if(isInMonthPeriodYYYYMMDD(subjectAccountModel.getPaymtdate10(), TWELVE_MONTH)){
                                                                                    if(isOverLimit(subjectAccountModel.getPaymt10())){
                                                                                        numberOfOverLimit++;
                                                                                    }
                                                                                    if(isInMonthPeriodYYYYMMDD(subjectAccountModel.getPaymtdate10(), SIX_MONTH)){
                                                                                        worstCode = getWorstCode(subjectAccountModel.getPaymt10(), worstCode);
                                                                                        if(isOutStandingPayment(subjectAccountModel.getPaymt10())){
                                                                                            numberOfOutStandingPayment++;
                                                                                        }
                                                                                        if(isNPLIndividual(subjectAccountModel.getPaymt10())){
                                                                                            if(isTMBAccount){
                                                                                                isNPLTMB = true;
                                                                                                if(Util.isEmpty(lastNPLDateTMB)){
                                                                                                    lastNPLDateTMB = subjectAccountModel.getPaymtdate10();
                                                                                                } else {
                                                                                                    lastNPLDateTMB = getLastDateYYYYMMDD(lastNPLDateTMB,subjectAccountModel.getPaymtdate10());
                                                                                                }
                                                                                            } else {
                                                                                                isNPLOther = true;
                                                                                                if(Util.isEmpty(lastNPLDateOther)){
                                                                                                    lastNPLDateOther = subjectAccountModel.getPaymtdate10();
                                                                                                } else {
                                                                                                    lastNPLDateOther = getLastDateYYYYMMDD(lastNPLDateOther,subjectAccountModel.getPaymtdate10());
                                                                                                }
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                    if(isInMonthPeriodYYYYMMDD(subjectAccountModel.getPaymtdate11(), TWELVE_MONTH)){
                                                                                        if(isOverLimit(subjectAccountModel.getPaymt11())){
                                                                                            numberOfOverLimit++;
                                                                                        }
                                                                                        if(isInMonthPeriodYYYYMMDD(subjectAccountModel.getPaymtdate11(), SIX_MONTH)){
                                                                                            worstCode = getWorstCode(subjectAccountModel.getPaymt11(), worstCode);
                                                                                            if(isOutStandingPayment(subjectAccountModel.getPaymt11())){
                                                                                                numberOfOutStandingPayment++;
                                                                                            }
                                                                                            if(isNPLIndividual(subjectAccountModel.getPaymt11())){
                                                                                                if(isTMBAccount){
                                                                                                    isNPLTMB = true;
                                                                                                    if(Util.isEmpty(lastNPLDateTMB)){
                                                                                                        lastNPLDateTMB = subjectAccountModel.getPaymtdate11();
                                                                                                    } else {
                                                                                                        lastNPLDateTMB = getLastDateYYYYMMDD(lastNPLDateTMB,subjectAccountModel.getPaymtdate11());
                                                                                                    }
                                                                                                } else {
                                                                                                    isNPLOther = true;
                                                                                                    if(Util.isEmpty(lastNPLDateOther)){
                                                                                                        lastNPLDateOther = subjectAccountModel.getPaymtdate11();
                                                                                                    } else {
                                                                                                        lastNPLDateOther = getLastDateYYYYMMDD(lastNPLDateOther,subjectAccountModel.getPaymtdate11());
                                                                                                    }
                                                                                                }
                                                                                            }
                                                                                        }
                                                                                        if(isInMonthPeriodYYYYMMDD(subjectAccountModel.getPaymtdate12(), TWELVE_MONTH)){
                                                                                            if(isOverLimit(subjectAccountModel.getPaymt12())){
                                                                                                numberOfOverLimit++;
                                                                                            }
                                                                                            if(isInMonthPeriodYYYYMMDD(subjectAccountModel.getPaymtdate12(), SIX_MONTH)){
                                                                                                worstCode = getWorstCode(subjectAccountModel.getPaymt12(), worstCode);
                                                                                                if(isOutStandingPayment(subjectAccountModel.getPaymt12())){
                                                                                                    numberOfOutStandingPayment++;
                                                                                                }
                                                                                                if(isNPLIndividual(subjectAccountModel.getPaymt12())){
                                                                                                    if(isTMBAccount){
                                                                                                        isNPLTMB = true;
                                                                                                        if(Util.isEmpty(lastNPLDateTMB)){
                                                                                                            lastNPLDateTMB = subjectAccountModel.getPaymtdate12();
                                                                                                        } else {
                                                                                                            lastNPLDateTMB = getLastDateYYYYMMDD(lastNPLDateTMB,subjectAccountModel.getPaymtdate12());
                                                                                                        }
                                                                                                    } else {
                                                                                                        isNPLOther = true;
                                                                                                        if(Util.isEmpty(lastNPLDateOther)){
                                                                                                            lastNPLDateOther = subjectAccountModel.getPaymtdate12();
                                                                                                        } else {
                                                                                                            lastNPLDateOther = getLastDateYYYYMMDD(lastNPLDateOther,subjectAccountModel.getPaymtdate12());
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
                                            if(isInMonthPeriodYYYYMMDD(subjectAccountModel.getPaymtdate01(), TWELVE_MONTH)){
                                                worstCode = subjectAccountModel.getPaymt01();
                                                if(isOutStandingPayment(subjectAccountModel.getPaymt01())){
                                                    numberOfOutStandingPayment++;
                                                }
                                                if(isOverLimit(subjectAccountModel.getPaymt01())){
                                                    numberOfOverLimit++;
                                                }
                                                if(isNPLIndividual(subjectAccountModel.getPaymt01())){
                                                    if(isTMBAccount){
                                                        isNPLTMB = true;
                                                        if(Util.isEmpty(lastNPLDateTMB)){
                                                            lastNPLDateTMB = subjectAccountModel.getPaymtdate01();
                                                        } else {
                                                            lastNPLDateTMB = getLastDateYYYYMMDD(lastNPLDateTMB,subjectAccountModel.getPaymtdate01());
                                                        }
                                                    } else {
                                                        isNPLOther = true;
                                                        if(Util.isEmpty(lastNPLDateOther)){
                                                            lastNPLDateOther = subjectAccountModel.getPaymtdate01();
                                                        } else {
                                                            lastNPLDateOther = getLastDateYYYYMMDD(lastNPLDateOther,subjectAccountModel.getPaymtdate01());
                                                        }
                                                    }
                                                }
                                                if(isInMonthPeriodYYYYMMDD(subjectAccountModel.getPaymtdate02(), TWELVE_MONTH)){
                                                    worstCode = getWorstCode(subjectAccountModel.getPaymt02(),worstCode);
                                                    if(isOutStandingPayment(subjectAccountModel.getPaymt02())){
                                                        numberOfOutStandingPayment++;
                                                    }
                                                    if(isOverLimit(subjectAccountModel.getPaymt02())){
                                                        numberOfOverLimit++;
                                                    }
                                                    if(isNPLIndividual(subjectAccountModel.getPaymt02())){
                                                        if(isTMBAccount){
                                                            isNPLTMB = true;
                                                            if(Util.isEmpty(lastNPLDateTMB)){
                                                                lastNPLDateTMB = subjectAccountModel.getPaymtdate02();
                                                            } else {
                                                                lastNPLDateTMB = getLastDateYYYYMMDD(lastNPLDateTMB,subjectAccountModel.getPaymtdate02());
                                                            }
                                                        } else {
                                                            isNPLOther = true;
                                                            if(Util.isEmpty(lastNPLDateOther)){
                                                                lastNPLDateOther = subjectAccountModel.getPaymtdate02();
                                                            } else {
                                                                lastNPLDateOther = getLastDateYYYYMMDD(lastNPLDateOther,subjectAccountModel.getPaymtdate02());
                                                            }
                                                        }
                                                    }
                                                    if(isInMonthPeriodYYYYMMDD(subjectAccountModel.getPaymtdate03(), TWELVE_MONTH)){
                                                        worstCode = getWorstCode(subjectAccountModel.getPaymt03(),worstCode);
                                                        if(isOutStandingPayment(subjectAccountModel.getPaymt03())){
                                                            numberOfOutStandingPayment++;
                                                        }
                                                        if(isOverLimit(subjectAccountModel.getPaymt03())){
                                                            numberOfOverLimit++;
                                                        }
                                                        if(isNPLIndividual(subjectAccountModel.getPaymt03())){
                                                            if(isTMBAccount){
                                                                isNPLTMB = true;
                                                                if(Util.isEmpty(lastNPLDateTMB)){
                                                                    lastNPLDateTMB = subjectAccountModel.getPaymtdate03();
                                                                } else {
                                                                    lastNPLDateTMB = getLastDateYYYYMMDD(lastNPLDateTMB,subjectAccountModel.getPaymtdate03());
                                                                }
                                                            } else {
                                                                isNPLOther = true;
                                                                if(Util.isEmpty(lastNPLDateOther)){
                                                                    lastNPLDateOther = subjectAccountModel.getPaymtdate03();
                                                                } else {
                                                                    lastNPLDateOther = getLastDateYYYYMMDD(lastNPLDateOther,subjectAccountModel.getPaymtdate03());
                                                                }
                                                            }
                                                        }
                                                        if(isInMonthPeriodYYYYMMDD(subjectAccountModel.getPaymtdate04(), TWELVE_MONTH)){
                                                            worstCode = getWorstCode(subjectAccountModel.getPaymt04(),worstCode);
                                                            if(isOutStandingPayment(subjectAccountModel.getPaymt04())){
                                                                numberOfOutStandingPayment++;
                                                            }
                                                            if(isOverLimit(subjectAccountModel.getPaymt04())){
                                                                numberOfOverLimit++;
                                                            }
                                                            if(isNPLIndividual(subjectAccountModel.getPaymt04())){
                                                                if(isTMBAccount){
                                                                    isNPLTMB = true;
                                                                    if(Util.isEmpty(lastNPLDateTMB)){
                                                                        lastNPLDateTMB = subjectAccountModel.getPaymtdate04();
                                                                    } else {
                                                                        lastNPLDateTMB = getLastDateYYYYMMDD(lastNPLDateTMB,subjectAccountModel.getPaymtdate04());
                                                                    }
                                                                } else {
                                                                    isNPLOther = true;
                                                                    if(Util.isEmpty(lastNPLDateOther)){
                                                                        lastNPLDateOther = subjectAccountModel.getPaymtdate04();
                                                                    } else {
                                                                        lastNPLDateOther = getLastDateYYYYMMDD(lastNPLDateOther,subjectAccountModel.getPaymtdate04());
                                                                    }
                                                                }
                                                            }
                                                            if(isInMonthPeriodYYYYMMDD(subjectAccountModel.getPaymtdate05(), TWELVE_MONTH)){
                                                                worstCode = getWorstCode(subjectAccountModel.getPaymt05(),worstCode);
                                                                if(isOutStandingPayment(subjectAccountModel.getPaymt05())){
                                                                    numberOfOutStandingPayment++;
                                                                }
                                                                if(isOverLimit(subjectAccountModel.getPaymt05())){
                                                                    numberOfOverLimit++;
                                                                }
                                                                if(isNPLIndividual(subjectAccountModel.getPaymt05())){
                                                                    if(isTMBAccount){
                                                                        isNPLTMB = true;
                                                                        if(Util.isEmpty(lastNPLDateTMB)){
                                                                            lastNPLDateTMB = subjectAccountModel.getPaymtdate05();
                                                                        } else {
                                                                            lastNPLDateTMB = getLastDateYYYYMMDD(lastNPLDateTMB,subjectAccountModel.getPaymtdate05());
                                                                        }
                                                                    } else {
                                                                        isNPLOther = true;
                                                                        if(Util.isEmpty(lastNPLDateOther)){
                                                                            lastNPLDateOther = subjectAccountModel.getPaymtdate05();
                                                                        } else {
                                                                            lastNPLDateOther = getLastDateYYYYMMDD(lastNPLDateOther,subjectAccountModel.getPaymtdate05());
                                                                        }
                                                                    }
                                                                }
                                                                if(isInMonthPeriodYYYYMMDD(subjectAccountModel.getPaymtdate06(), TWELVE_MONTH)){
                                                                    worstCode = getWorstCode(subjectAccountModel.getPaymt06(),worstCode);
                                                                    if(isOutStandingPayment(subjectAccountModel.getPaymt06())){
                                                                        numberOfOutStandingPayment++;
                                                                    }
                                                                    if(isOverLimit(subjectAccountModel.getPaymt06())){
                                                                        numberOfOverLimit++;
                                                                    }
                                                                    if(isNPLIndividual(subjectAccountModel.getPaymt06())){
                                                                        if(isTMBAccount){
                                                                            isNPLTMB = true;
                                                                            if(Util.isEmpty(lastNPLDateTMB)){
                                                                                lastNPLDateTMB = subjectAccountModel.getPaymtdate06();
                                                                            } else {
                                                                                lastNPLDateTMB = getLastDateYYYYMMDD(lastNPLDateTMB,subjectAccountModel.getPaymtdate06());
                                                                            }
                                                                        } else {
                                                                            isNPLOther = true;
                                                                            if(Util.isEmpty(lastNPLDateOther)){
                                                                                lastNPLDateOther = subjectAccountModel.getPaymtdate06();
                                                                            } else {
                                                                                lastNPLDateOther = getLastDateYYYYMMDD(lastNPLDateOther,subjectAccountModel.getPaymtdate06());
                                                                            }
                                                                        }
                                                                    }
                                                                    if(isInMonthPeriodYYYYMMDD(subjectAccountModel.getPaymtdate07(), TWELVE_MONTH)){
                                                                        worstCode = getWorstCode(subjectAccountModel.getPaymt07(),worstCode);
                                                                        if(isOutStandingPayment(subjectAccountModel.getPaymt07())){
                                                                            numberOfOutStandingPayment++;
                                                                        }
                                                                        if(isOverLimit(subjectAccountModel.getPaymt07())){
                                                                            numberOfOverLimit++;
                                                                        }
                                                                        if(isNPLIndividual(subjectAccountModel.getPaymt07())){
                                                                            if(isTMBAccount){
                                                                                isNPLTMB = true;
                                                                                if(Util.isEmpty(lastNPLDateTMB)){
                                                                                    lastNPLDateTMB = subjectAccountModel.getPaymtdate07();
                                                                                } else {
                                                                                    lastNPLDateTMB = getLastDateYYYYMMDD(lastNPLDateTMB,subjectAccountModel.getPaymtdate07());
                                                                                }
                                                                            } else {
                                                                                isNPLOther = true;
                                                                                if(Util.isEmpty(lastNPLDateOther)){
                                                                                    lastNPLDateOther = subjectAccountModel.getPaymtdate07();
                                                                                } else {
                                                                                    lastNPLDateOther = getLastDateYYYYMMDD(lastNPLDateOther,subjectAccountModel.getPaymtdate07());
                                                                                }
                                                                            }
                                                                        }
                                                                        if(isInMonthPeriodYYYYMMDD(subjectAccountModel.getPaymtdate08(), TWELVE_MONTH)){
                                                                            worstCode = getWorstCode(subjectAccountModel.getPaymt08(),worstCode);
                                                                            if(isOutStandingPayment(subjectAccountModel.getPaymt08())){
                                                                                numberOfOutStandingPayment++;
                                                                            }
                                                                            if(isOverLimit(subjectAccountModel.getPaymt08())){
                                                                                numberOfOverLimit++;
                                                                            }
                                                                            if(isNPLIndividual(subjectAccountModel.getPaymt08())){
                                                                                if(isTMBAccount){
                                                                                    isNPLTMB = true;
                                                                                    if(Util.isEmpty(lastNPLDateTMB)){
                                                                                        lastNPLDateTMB = subjectAccountModel.getPaymtdate08();
                                                                                    } else {
                                                                                        lastNPLDateTMB = getLastDateYYYYMMDD(lastNPLDateTMB,subjectAccountModel.getPaymtdate08());
                                                                                    }
                                                                                } else {
                                                                                    isNPLOther = true;
                                                                                    if(Util.isEmpty(lastNPLDateOther)){
                                                                                        lastNPLDateOther = subjectAccountModel.getPaymtdate08();
                                                                                    } else {
                                                                                        lastNPLDateOther = getLastDateYYYYMMDD(lastNPLDateOther,subjectAccountModel.getPaymtdate08());
                                                                                    }
                                                                                }
                                                                            }
                                                                            if(isInMonthPeriodYYYYMMDD(subjectAccountModel.getPaymtdate09(), TWELVE_MONTH)){
                                                                                worstCode = getWorstCode(subjectAccountModel.getPaymt09(),worstCode);
                                                                                if(isOutStandingPayment(subjectAccountModel.getPaymt09())){
                                                                                    numberOfOutStandingPayment++;
                                                                                }
                                                                                if(isOverLimit(subjectAccountModel.getPaymt09())){
                                                                                    numberOfOverLimit++;
                                                                                }
                                                                                if(isNPLIndividual(subjectAccountModel.getPaymt09())){
                                                                                    if(isTMBAccount){
                                                                                        isNPLTMB = true;
                                                                                        if(Util.isEmpty(lastNPLDateTMB)){
                                                                                            lastNPLDateTMB = subjectAccountModel.getPaymtdate09();
                                                                                        } else {
                                                                                            lastNPLDateTMB = getLastDateYYYYMMDD(lastNPLDateTMB,subjectAccountModel.getPaymtdate09());
                                                                                        }
                                                                                    } else {
                                                                                        isNPLOther = true;
                                                                                        if(Util.isEmpty(lastNPLDateOther)){
                                                                                            lastNPLDateOther = subjectAccountModel.getPaymtdate09();
                                                                                        } else {
                                                                                            lastNPLDateOther = getLastDateYYYYMMDD(lastNPLDateOther,subjectAccountModel.getPaymtdate09());
                                                                                        }
                                                                                    }
                                                                                }
                                                                                if(isInMonthPeriodYYYYMMDD(subjectAccountModel.getPaymtdate10(), TWELVE_MONTH)){
                                                                                    worstCode = getWorstCode(subjectAccountModel.getPaymt10(),worstCode);
                                                                                    if(isOutStandingPayment(subjectAccountModel.getPaymt10())){
                                                                                        numberOfOutStandingPayment++;
                                                                                    }
                                                                                    if(isOverLimit(subjectAccountModel.getPaymt10())){
                                                                                        numberOfOverLimit++;
                                                                                    }
                                                                                    if(isNPLIndividual(subjectAccountModel.getPaymt10())){
                                                                                        if(isTMBAccount){
                                                                                            isNPLTMB = true;
                                                                                            if(Util.isEmpty(lastNPLDateTMB)){
                                                                                                lastNPLDateTMB = subjectAccountModel.getPaymtdate10();
                                                                                            } else {
                                                                                                lastNPLDateTMB = getLastDateYYYYMMDD(lastNPLDateTMB,subjectAccountModel.getPaymtdate10());
                                                                                            }
                                                                                        } else {
                                                                                            isNPLOther = true;
                                                                                            if(Util.isEmpty(lastNPLDateOther)){
                                                                                                lastNPLDateOther = subjectAccountModel.getPaymtdate10();
                                                                                            } else {
                                                                                                lastNPLDateOther = getLastDateYYYYMMDD(lastNPLDateOther,subjectAccountModel.getPaymtdate10());
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                    if(isInMonthPeriodYYYYMMDD(subjectAccountModel.getPaymtdate11(), TWELVE_MONTH)){
                                                                                        worstCode = getWorstCode(subjectAccountModel.getPaymt11(),worstCode);
                                                                                        if(isOutStandingPayment(subjectAccountModel.getPaymt11())){
                                                                                            numberOfOutStandingPayment++;
                                                                                        }
                                                                                        if(isOverLimit(subjectAccountModel.getPaymt11())){
                                                                                            numberOfOverLimit++;
                                                                                        }
                                                                                        if(isNPLIndividual(subjectAccountModel.getPaymt11())){
                                                                                            if(isTMBAccount){
                                                                                                isNPLTMB = true;
                                                                                                if(Util.isEmpty(lastNPLDateTMB)){
                                                                                                    lastNPLDateTMB = subjectAccountModel.getPaymtdate11();
                                                                                                } else {
                                                                                                    lastNPLDateTMB = getLastDateYYYYMMDD(lastNPLDateTMB,subjectAccountModel.getPaymtdate11());
                                                                                                }
                                                                                            } else {
                                                                                                isNPLOther = true;
                                                                                                if(Util.isEmpty(lastNPLDateOther)){
                                                                                                    lastNPLDateOther = subjectAccountModel.getPaymtdate11();
                                                                                                } else {
                                                                                                    lastNPLDateOther = getLastDateYYYYMMDD(lastNPLDateOther,subjectAccountModel.getPaymtdate11());
                                                                                                }
                                                                                            }
                                                                                        }
                                                                                        if(isInMonthPeriodYYYYMMDD(subjectAccountModel.getPaymtdate12(), 12)){
                                                                                            worstCode = getWorstCode(subjectAccountModel.getPaymt12(),worstCode);
                                                                                            if(isOutStandingPayment(subjectAccountModel.getPaymt12())){
                                                                                                numberOfOutStandingPayment++;
                                                                                            }
                                                                                            if(isOverLimit(subjectAccountModel.getPaymt12())){
                                                                                                numberOfOverLimit++;
                                                                                            }
                                                                                            if(isNPLIndividual(subjectAccountModel.getPaymt12())){
                                                                                                if(isTMBAccount){
                                                                                                    isNPLTMB = true;
                                                                                                    if(Util.isEmpty(lastNPLDateTMB)){
                                                                                                        lastNPLDateTMB = subjectAccountModel.getPaymtdate12();
                                                                                                    } else {
                                                                                                        lastNPLDateTMB = getLastDateYYYYMMDD(lastNPLDateTMB,subjectAccountModel.getPaymtdate12());
                                                                                                    }
                                                                                                } else {
                                                                                                    isNPLOther = true;
                                                                                                    if(Util.isEmpty(lastNPLDateOther)){
                                                                                                        lastNPLDateOther = subjectAccountModel.getPaymtdate12();
                                                                                                    } else {
                                                                                                        lastNPLDateOther = getLastDateYYYYMMDD(lastNPLDateOther,subjectAccountModel.getPaymtdate12());
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
                                        SettlementStatus historySettlementStatus = new SettlementStatus();
                                        if(!Util.isEmpty(worstCode)){
                                            historySettlementStatus = settlementStatusDAO.getIndividualByCode(worstCode);
                                        }
                                        ncbDetailView.setHistoryPayment(historySettlementStatus);
                                        if(!Util.isEmpty(worstPaymentStatus)){
                                            worstPaymentStatus = getWorstCode(subjectAccountModel.getPaymt01(),worstPaymentStatus);
                                        } else {
                                            worstPaymentStatus = subjectAccountModel.getPaymt01();
                                        }
                                        //set number of outstanding payment
                                        ncbDetailView.setNoOfOutstandingPaymentIn12months(new BigDecimal(numberOfOutStandingPayment));
                                        //set number of over limit
                                        ncbDetailView.setNoOfOverLimit(numberOfOverLimit);

                                        //add ncbDetailView to ncbDetailViewList
                                        ncbDetailViews.add(ncbDetailView);
                                    }

                                    ncbSummaryView = new NCBSummaryView();
                                    ncbSummaryView.setTypeOfCurrentPayment(currentWorstPaymentStatus); //todo: change view field to SettlementType
                                    ncbSummaryView.setTypeOfHistoryPayment(worstPaymentStatus); //todo: change view field to SettlementType
                                    //todo: set NPL flag and NPL date for TMB and other
                                    //todo: set TDR flag and TDR date for TMB and other
                                } else {
                                    //no ncb detail
                                }

                                //get enquiry amount
                                int enquiryTime = 0;
                                if(enquiryModelResults.size() > 0){
                                    Map<String, String> enquiryMap = new HashMap<String, String>();  //for check duplicate enquiry time
                                    //get number of enquiry in last 6 months
                                    for(EnquiryModel enquiryModel: enquiryModelResults){
                                        if(isInMonthPeriodYYYYMMDD(enquiryModel.getEnqdate(),SIX_MONTH)){
                                            enquiryMap.put(enquiryModel.getEnqdate().concat(enquiryModel.getEnqtime()),enquiryModel.getEnqamount());
                                        }
                                    }

                                    enquiryTime = enquiryMap.size();
                                }

                                if(ncbSummaryView != null){
                                    ncbSummaryView.setNoOfNCBCheckIn6months(enquiryTime+"");
                                }*/

                                ncbView.setAccountInfoIdList(accountInfoIdList);
                                ncbView.setAccountInfoNameList(accountInfoNameList);
                            }
                        }
                    }
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

    private String getWorstCode(String code, String worstCode){
        int value1 = NCBPaymentCode.getValue(worstCode).value();
        int value2 = NCBPaymentCode.getValue(code).value();
        if(value2>value1){
            return code;
        }
        return worstCode;
    }

    private boolean isInMonthPeriodYYYYMMDD(String dateStr, int numberMonth){
        if(dateStr!=null && !dateStr.trim().equals("")){
            Date paymentDate = Util.strYYYYMMDDtoDateFormat(dateStr);
            Date currentDate = new Date();
            Calendar startCalendar = new GregorianCalendar();
            startCalendar.setTime(paymentDate);
            Calendar endCalendar = new GregorianCalendar();
            endCalendar.setTime(currentDate);
            int diffYear = endCalendar.get(Calendar.YEAR) - startCalendar.get(Calendar.YEAR);
            int diffMonth = diffYear * 12 + endCalendar.get(Calendar.MONTH) - startCalendar.get(Calendar.MONTH);
            if(diffMonth <= numberMonth){
                return true;
            }
        }
        return false;
    }

    private boolean isOutStandingPayment(String paymentCode){
        if(!Util.isEmpty(paymentCode)){
            int value = NCBPaymentCode.getValue(paymentCode).value();
            if(value >= 4){ //code >= 004
                return true;
            }
        }
        return false;
    }

    private boolean isOverLimit(String paymentCode){
        if(!Util.isEmpty(paymentCode)){
            int value = NCBPaymentCode.getValue(paymentCode).value();
            if(value == 3){ //code == __Y
                return true;
            }
        }
        return false;
    }

    private boolean isNPLIndividual(String paymentCode){
        if(!Util.isEmpty(paymentCode)){
            int value = NCBPaymentCode.getValue(paymentCode).value();
            if(value >= 3){
                return true;
            }
        }
        return false;
    }

    private boolean isNPLJuristic(String paymentCode){
        if(!Util.isEmpty(paymentCode)){
            int value = NCBPaymentCode.getValue(paymentCode).value();
            if(value >= 4){
                return true;
            }
        }
        return false;
    }

    private String getLastDateYYYYMMDD(String dateStr1, String dateStr2){
        Date date1 = Util.strYYYYMMDDtoDateFormat(dateStr1);
        Date date2 = Util.strYYYYMMDDtoDateFormat(dateStr2);
        if(date1 != null && date2 != null){
            if(date1.compareTo(date2) > 0){
                return Util.createDateString(date1,"yyyyMMdd");
            } else if(date1.compareTo(date2) < 0){
                return Util.createDateString(date2,"yyyyMMdd");
            }
        } else {
            if(date1 == null) {
                return dateStr2;
            } else {
                return dateStr1;
            }
        }
        return dateStr1;
    }
}
