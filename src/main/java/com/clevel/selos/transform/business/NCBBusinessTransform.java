package com.clevel.selos.transform.business;

import com.clevel.selos.dao.master.AccountStatusDAO;
import com.clevel.selos.dao.master.AccountTypeDAO;
import com.clevel.selos.dao.master.SettlementStatusDAO;
import com.clevel.selos.integration.ncb.ncrs.models.response.*;
import com.clevel.selos.integration.ncb.ncrs.ncrsmodel.NCRSOutputModel;
import com.clevel.selos.model.ActionResult;
import com.clevel.selos.model.NCBPaymentCode;
import com.clevel.selos.model.db.master.AccountStatus;
import com.clevel.selos.model.db.master.AccountType;
import com.clevel.selos.model.db.master.SettlementStatus;
import com.clevel.selos.model.view.NCBDetailView;
import com.clevel.selos.model.view.NCBSummaryView;
import com.clevel.selos.model.view.NcbView;
import com.clevel.selos.util.Util;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.*;

public class NCBBusinessTransform extends BusinessTransform {
    @Inject
    AccountTypeDAO accountTypeDAO;
    @Inject
    AccountStatusDAO accountStatusDAO;
    @Inject
    SettlementStatusDAO settlementStatusDAO;

    private static final String TMB_BANK = "TMB";
    private static final String SUCCESS = "SUCCESS";

    public List<NcbView> transform(List<NCRSOutputModel> responseNCRSModels){
        List<NcbView> ncbViews = null;
        List<NCBDetailView> ncbDetailViews = null;
        NCBSummaryView ncbSummaryView = null;
        if(responseNCRSModels!=null && responseNCRSModels.size()>0){
            ncbViews = new ArrayList<NcbView>();
            for(NCRSOutputModel responseNCRSModel: responseNCRSModels){
                NcbView ncbView = new NcbView();
                ncbView.setIdNumber(responseNCRSModel.getIdNumber());
                if(responseNCRSModel.getActionResult().equals(SUCCESS)){
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
                                    //todo: get name, last name, address, marital status to list in summary

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

                                    for(SubjectAccountModel subjectAccountModel : subjectAccountModelResults){
                                        NCBDetailView ncbDetailView = new NCBDetailView();
                                        //set accountType
                                        AccountType accountType = accountTypeDAO.getIndividualByCode(subjectAccountModel.getAccounttype());
                                        ncbDetailView.setAccountType(accountType);
                                        //set tmb account
                                        ncbDetailView.setTMBAccount(false);
                                        if(subjectAccountModel.getShortname().equals(TMB_BANK)){ //todo: change to master
                                            ncbDetailView.setTMBAccount(true);
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
                                        ncbDetailView.setDateOfDebtRestructuring(Util.strYYYYMMDDtoDateFormat(subjectAccountModel.getLastrestructureddate())); //todo: check if null
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
                                            if(isInMonthPeriod(subjectAccountModel.getPaymtdate01(),12)){
                                                if(isOverLimit(subjectAccountModel.getPaymt01())){
                                                    numberOfOverLimit++;
                                                }
                                                if(isInMonthPeriod(subjectAccountModel.getPaymtdate01(),6)){
                                                    worstCode = subjectAccountModel.getPaymt01();
                                                    if(isOutStandingPayment(subjectAccountModel.getPaymt01())){
                                                        numberOfOutStandingPayment++;
                                                    }
                                                }
                                                if(isInMonthPeriod(subjectAccountModel.getPaymtdate02(),12)){
                                                    if(isOverLimit(subjectAccountModel.getPaymt02())){
                                                        numberOfOverLimit++;
                                                    }
                                                    if(isInMonthPeriod(subjectAccountModel.getPaymtdate02(), 6)){
                                                        worstCode = getWorstCode(subjectAccountModel.getPaymt02(), worstCode);
                                                        if(isOutStandingPayment(subjectAccountModel.getPaymt02())){
                                                            numberOfOutStandingPayment++;
                                                        }
                                                    }
                                                    if(isInMonthPeriod(subjectAccountModel.getPaymtdate03(),12)){
                                                        if(isOverLimit(subjectAccountModel.getPaymt03())){
                                                            numberOfOverLimit++;
                                                        }
                                                        if(isInMonthPeriod(subjectAccountModel.getPaymtdate03(), 6)){
                                                            worstCode = getWorstCode(subjectAccountModel.getPaymt03(), worstCode);
                                                            if(isOutStandingPayment(subjectAccountModel.getPaymt03())){
                                                                numberOfOutStandingPayment++;
                                                            }
                                                        }
                                                        if(isInMonthPeriod(subjectAccountModel.getPaymtdate04(),12)){
                                                            if(isOverLimit(subjectAccountModel.getPaymt04())){
                                                                numberOfOverLimit++;
                                                            }
                                                            if(isInMonthPeriod(subjectAccountModel.getPaymtdate04(), 6)){
                                                                worstCode = getWorstCode(subjectAccountModel.getPaymt04(), worstCode);
                                                                if(isOutStandingPayment(subjectAccountModel.getPaymt04())){
                                                                    numberOfOutStandingPayment++;
                                                                }
                                                            }
                                                            if(isInMonthPeriod(subjectAccountModel.getPaymtdate05(),12)){
                                                                if(isOverLimit(subjectAccountModel.getPaymt05())){
                                                                    numberOfOverLimit++;
                                                                }
                                                                if(isInMonthPeriod(subjectAccountModel.getPaymtdate05(), 6)){
                                                                    worstCode = getWorstCode(subjectAccountModel.getPaymt05(), worstCode);
                                                                    if(isOutStandingPayment(subjectAccountModel.getPaymt05())){
                                                                        numberOfOutStandingPayment++;
                                                                    }
                                                                }
                                                                if(isInMonthPeriod(subjectAccountModel.getPaymtdate06(),12)){
                                                                    if(isOverLimit(subjectAccountModel.getPaymt06())){
                                                                        numberOfOverLimit++;
                                                                    }
                                                                    if(isInMonthPeriod(subjectAccountModel.getPaymtdate06(), 6)){
                                                                        worstCode = getWorstCode(subjectAccountModel.getPaymt06(), worstCode);
                                                                        if(isOutStandingPayment(subjectAccountModel.getPaymt06())){
                                                                            numberOfOutStandingPayment++;
                                                                        }
                                                                    }
                                                                    if(isInMonthPeriod(subjectAccountModel.getPaymtdate07(),12)){
                                                                        if(isOverLimit(subjectAccountModel.getPaymt07())){
                                                                            numberOfOverLimit++;
                                                                        }
                                                                        if(isInMonthPeriod(subjectAccountModel.getPaymtdate07(), 6)){
                                                                            worstCode = getWorstCode(subjectAccountModel.getPaymt07(), worstCode);
                                                                            if(isOutStandingPayment(subjectAccountModel.getPaymt07())){
                                                                                numberOfOutStandingPayment++;
                                                                            }
                                                                        }
                                                                        if(isInMonthPeriod(subjectAccountModel.getPaymtdate08(),12)){
                                                                            if(isOverLimit(subjectAccountModel.getPaymt08())){
                                                                                numberOfOverLimit++;
                                                                            }
                                                                            if(isInMonthPeriod(subjectAccountModel.getPaymtdate08(), 6)){
                                                                                worstCode = getWorstCode(subjectAccountModel.getPaymt08(), worstCode);
                                                                                if(isOutStandingPayment(subjectAccountModel.getPaymt08())){
                                                                                    numberOfOutStandingPayment++;
                                                                                }
                                                                            }
                                                                            if(isInMonthPeriod(subjectAccountModel.getPaymtdate09(),12)){
                                                                                if(isOverLimit(subjectAccountModel.getPaymt09())){
                                                                                    numberOfOverLimit++;
                                                                                }
                                                                                if(isInMonthPeriod(subjectAccountModel.getPaymtdate09(), 6)){
                                                                                    worstCode = getWorstCode(subjectAccountModel.getPaymt09(), worstCode);
                                                                                    if(isOutStandingPayment(subjectAccountModel.getPaymt09())){
                                                                                        numberOfOutStandingPayment++;
                                                                                    }
                                                                                }
                                                                                if(isInMonthPeriod(subjectAccountModel.getPaymtdate10(),12)){
                                                                                    if(isOverLimit(subjectAccountModel.getPaymt10())){
                                                                                        numberOfOverLimit++;
                                                                                    }
                                                                                    if(isInMonthPeriod(subjectAccountModel.getPaymtdate10(), 6)){
                                                                                        worstCode = getWorstCode(subjectAccountModel.getPaymt10(), worstCode);
                                                                                        if(isOutStandingPayment(subjectAccountModel.getPaymt10())){
                                                                                            numberOfOutStandingPayment++;
                                                                                        }
                                                                                    }
                                                                                    if(isInMonthPeriod(subjectAccountModel.getPaymtdate11(),12)){
                                                                                        if(isOverLimit(subjectAccountModel.getPaymt11())){
                                                                                            numberOfOverLimit++;
                                                                                        }
                                                                                        if(isInMonthPeriod(subjectAccountModel.getPaymtdate11(), 6)){
                                                                                            worstCode = getWorstCode(subjectAccountModel.getPaymt11(), worstCode);
                                                                                            if(isOutStandingPayment(subjectAccountModel.getPaymt11())){
                                                                                                numberOfOutStandingPayment++;
                                                                                            }
                                                                                        }
                                                                                        if(isInMonthPeriod(subjectAccountModel.getPaymtdate12(),12)){
                                                                                            if(isOverLimit(subjectAccountModel.getPaymt12())){
                                                                                                numberOfOverLimit++;
                                                                                            }
                                                                                            if(isInMonthPeriod(subjectAccountModel.getPaymtdate12(), 6)){
                                                                                                worstCode = getWorstCode(subjectAccountModel.getPaymt12(), worstCode);
                                                                                                if(isOutStandingPayment(subjectAccountModel.getPaymt12())){
                                                                                                    numberOfOutStandingPayment++;
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
                                            if(isInMonthPeriod(subjectAccountModel.getPaymtdate01(),12)){
                                                worstCode = subjectAccountModel.getPaymt01();
                                                if(isOutStandingPayment(subjectAccountModel.getPaymt01())){
                                                    numberOfOutStandingPayment++;
                                                }
                                                if(isOverLimit(subjectAccountModel.getPaymt01())){
                                                    numberOfOverLimit++;
                                                }
                                                if(isInMonthPeriod(subjectAccountModel.getPaymtdate02(),12)){
                                                    worstCode = getWorstCode(subjectAccountModel.getPaymt02(),worstCode);
                                                    if(isOutStandingPayment(subjectAccountModel.getPaymt02())){
                                                        numberOfOutStandingPayment++;
                                                    }
                                                    if(isOverLimit(subjectAccountModel.getPaymt02())){
                                                        numberOfOverLimit++;
                                                    }
                                                    if(isInMonthPeriod(subjectAccountModel.getPaymtdate03(),12)){
                                                        worstCode = getWorstCode(subjectAccountModel.getPaymt03(),worstCode);
                                                        if(isOutStandingPayment(subjectAccountModel.getPaymt03())){
                                                            numberOfOutStandingPayment++;
                                                        }
                                                        if(isOverLimit(subjectAccountModel.getPaymt03())){
                                                            numberOfOverLimit++;
                                                        }
                                                        if(isInMonthPeriod(subjectAccountModel.getPaymtdate04(),12)){
                                                            worstCode = getWorstCode(subjectAccountModel.getPaymt04(),worstCode);
                                                            if(isOutStandingPayment(subjectAccountModel.getPaymt04())){
                                                                numberOfOutStandingPayment++;
                                                            }
                                                            if(isOverLimit(subjectAccountModel.getPaymt04())){
                                                                numberOfOverLimit++;
                                                            }
                                                            if(isInMonthPeriod(subjectAccountModel.getPaymtdate05(),12)){
                                                                worstCode = getWorstCode(subjectAccountModel.getPaymt05(),worstCode);
                                                                if(isOutStandingPayment(subjectAccountModel.getPaymt05())){
                                                                    numberOfOutStandingPayment++;
                                                                }
                                                                if(isOverLimit(subjectAccountModel.getPaymt05())){
                                                                    numberOfOverLimit++;
                                                                }
                                                                if(isInMonthPeriod(subjectAccountModel.getPaymtdate06(),12)){
                                                                    worstCode = getWorstCode(subjectAccountModel.getPaymt06(),worstCode);
                                                                    if(isOutStandingPayment(subjectAccountModel.getPaymt06())){
                                                                        numberOfOutStandingPayment++;
                                                                    }
                                                                    if(isOverLimit(subjectAccountModel.getPaymt06())){
                                                                        numberOfOverLimit++;
                                                                    }
                                                                    if(isInMonthPeriod(subjectAccountModel.getPaymtdate07(),12)){
                                                                        worstCode = getWorstCode(subjectAccountModel.getPaymt07(),worstCode);
                                                                        if(isOutStandingPayment(subjectAccountModel.getPaymt07())){
                                                                            numberOfOutStandingPayment++;
                                                                        }
                                                                        if(isOverLimit(subjectAccountModel.getPaymt07())){
                                                                            numberOfOverLimit++;
                                                                        }
                                                                        if(isInMonthPeriod(subjectAccountModel.getPaymtdate08(),12)){
                                                                            worstCode = getWorstCode(subjectAccountModel.getPaymt08(),worstCode);
                                                                            if(isOutStandingPayment(subjectAccountModel.getPaymt08())){
                                                                                numberOfOutStandingPayment++;
                                                                            }
                                                                            if(isOverLimit(subjectAccountModel.getPaymt08())){
                                                                                numberOfOverLimit++;
                                                                            }
                                                                            if(isInMonthPeriod(subjectAccountModel.getPaymtdate09(),12)){
                                                                                worstCode = getWorstCode(subjectAccountModel.getPaymt09(),worstCode);
                                                                                if(isOutStandingPayment(subjectAccountModel.getPaymt09())){
                                                                                    numberOfOutStandingPayment++;
                                                                                }
                                                                                if(isOverLimit(subjectAccountModel.getPaymt09())){
                                                                                    numberOfOverLimit++;
                                                                                }
                                                                                if(isInMonthPeriod(subjectAccountModel.getPaymtdate10(),12)){
                                                                                    worstCode = getWorstCode(subjectAccountModel.getPaymt10(),worstCode);
                                                                                    if(isOutStandingPayment(subjectAccountModel.getPaymt10())){
                                                                                        numberOfOutStandingPayment++;
                                                                                    }
                                                                                    if(isOverLimit(subjectAccountModel.getPaymt10())){
                                                                                        numberOfOverLimit++;
                                                                                    }
                                                                                    if(isInMonthPeriod(subjectAccountModel.getPaymtdate11(),12)){
                                                                                        worstCode = getWorstCode(subjectAccountModel.getPaymt11(),worstCode);
                                                                                        if(isOutStandingPayment(subjectAccountModel.getPaymt11())){
                                                                                            numberOfOutStandingPayment++;
                                                                                        }
                                                                                        if(isOverLimit(subjectAccountModel.getPaymt11())){
                                                                                            numberOfOverLimit++;
                                                                                        }
                                                                                        if(isInMonthPeriod(subjectAccountModel.getPaymtdate12(),12)){
                                                                                            worstCode = getWorstCode(subjectAccountModel.getPaymt12(),worstCode);
                                                                                            if(isOutStandingPayment(subjectAccountModel.getPaymt12())){
                                                                                                numberOfOutStandingPayment++;
                                                                                            }
                                                                                            if(isOverLimit(subjectAccountModel.getPaymt12())){
                                                                                                numberOfOverLimit++;
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
                                    ncbSummaryView.setTypeOfCurrentPayment(currentWorstPaymentStatus);
                                    ncbSummaryView.setTypeOfHistoryPayment(worstPaymentStatus);
                                } else {
                                    //no ncb detail
                                }

                                //get enquiry amount
                                if(enquiryModelResults.size() > 0){

                                }
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

    private String getWorstCode(String code, String worstCode){
        int value1 = NCBPaymentCode.getValue(worstCode).value();
        int value2 = NCBPaymentCode.getValue(code).value();
        if(value2>value1){
            return code;
        }
        return worstCode;
    }

    private boolean isInMonthPeriod(String paymentDateStr, int numberMonth){
        if(paymentDateStr!=null && !paymentDateStr.trim().equals("")){
            Date paymentDate = Util.strYYYYMMDDtoDateFormat(paymentDateStr);
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
}
