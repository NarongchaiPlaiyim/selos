package com.clevel.selos.transform.business;

import com.clevel.selos.dao.master.AccountStatusDAO;
import com.clevel.selos.dao.master.AccountTypeDAO;
import com.clevel.selos.dao.master.SettlementStatusDAO;
import com.clevel.selos.integration.ncrs.models.response.*;
import com.clevel.selos.integration.ncrs.ncrsmodel.ResponseNCRSModel;
import com.clevel.selos.model.ActionResult;
import com.clevel.selos.model.db.master.AccountStatus;
import com.clevel.selos.model.db.master.AccountType;
import com.clevel.selos.model.db.master.SettlementStatus;
import com.clevel.selos.model.view.NcbRecordView;
import com.clevel.selos.model.view.NcbView;
import com.clevel.selos.util.Util;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class NCBBusinessTransform extends BusinessTransform {
    @Inject
    AccountTypeDAO accountTypeDAO;
    @Inject
    AccountStatusDAO accountStatusDAO;
    @Inject
    SettlementStatusDAO settlementStatusDAO;

    private static final String TMB_BANK = "TMB";
    private static final String SUCCESS = "SUCCESS";

    public List<NcbView> transform(List<ResponseNCRSModel> responseNCRSModels){
        List<NcbView> ncbViews = null;
        if(responseNCRSModels!=null && responseNCRSModels.size()>0){
            ncbViews = new ArrayList<NcbView>();
            for(ResponseNCRSModel responseNCRSModel: responseNCRSModels){
                NcbView ncbView = new NcbView();
                ncbView.setIdNumber(responseNCRSModel.getIdNumber());
                if(responseNCRSModel.getResult().equals(SUCCESS)){
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

                                // transform all account to ncb detail and summary
                                if(subjectAccountModelResults.size() > 0){
                                    // transform all account to ncb detail and summary
                                    for(SubjectAccountModel subjectAccountModel : subjectAccountModelResults){
                                        NcbRecordView ncbRecordView = new NcbRecordView();
                                        //set accountType
                                        AccountType accountType = accountTypeDAO.getIndividualByCode(subjectAccountModel.getAccounttype());
                                        ncbRecordView.setAccountType(accountType);
                                        //set tmb account
                                        ncbRecordView.setTMBAccount(false);
                                        if(subjectAccountModel.getShortname().equals(TMB_BANK)){ //todo: change to master
                                            ncbRecordView.setTMBAccount(true);
                                        }
                                        //set account status
                                        AccountStatus accountStatus = accountStatusDAO.getIndividualByCode(subjectAccountModel.getAccountstatus());
                                        ncbRecordView.setAccountStatus(accountStatus);
                                        //set date of info
                                        ncbRecordView.setDateOfInfo(Util.strYYYYMMDDtoDateFormat(subjectAccountModel.getAsofdate()));
                                        //set open date
                                        ncbRecordView.setAccountOpenDate(Util.strYYYYMMDDtoDateFormat(subjectAccountModel.getOpendate()));
                                        //set credit limit
                                        ncbRecordView.setLimit(new BigDecimal(subjectAccountModel.getCreditlimit())); //todo: check if null
                                        //set outstanding amount
                                        ncbRecordView.setOutstanding(new BigDecimal(subjectAccountModel.getAmountowed())); //todo: check if null
                                        //set installment
                                        ncbRecordView.setInstallment(new BigDecimal(subjectAccountModel.getInstallmentamount())); //todo: check if null
                                        //set restructure date
                                        ncbRecordView.setDateOfDebtRestructuring(Util.strYYYYMMDDtoDateFormat(subjectAccountModel.getLastrestructureddate())); //todo: check if null
                                        //set current payment
                                        SettlementStatus settlementStatus = new SettlementStatus();
                                        if(subjectAccountModel.getPaymt01()!=null){
                                            settlementStatus = settlementStatusDAO.getIndividualByCode(subjectAccountModel.getPaymt01());
                                        }
                                        ncbRecordView.setCurrentPayment(settlementStatus);
                                        //set history payment
                                    }

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
}
