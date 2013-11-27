package com.clevel.selos.businesscontrol;

import com.clevel.selos.dao.working.*;
import com.clevel.selos.model.BankType;
import com.clevel.selos.model.BorrowerType;
import com.clevel.selos.model.RelationValue;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.working.*;
import com.clevel.selos.model.view.*;
import com.clevel.selos.transform.ExSummaryTransform;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.List;

@Stateless
public class ExSummaryControl extends BusinessControl {
    @Inject
    ExSummaryDAO exSummaryDAO;
    @Inject
    CustomerDAO customerDAO;
    @Inject
    WorkCaseDAO workCaseDAO;
    @Inject
    ExSumDeviateDAO exSumDeviateDAO;
    @Inject
    BasicInfoDAO basicInfoDAO;
    @Inject
    BankStatementSummaryDAO bankStatementSummaryDAO;

    @Inject
    ExSummaryTransform exSummaryTransform;

    @Inject
    CustomerInfoControl customerInfoControl;
    @Inject
    NCBInfoControl ncbInfoControl;
//    @Inject
//    BankStmtControl bankStmtControl;
    @Inject
    BizInfoSummaryControl bizInfoSummaryControl;
    @Inject
    QualitativeControl qualitativeControl;

    public ExSummaryView getExSummaryViewByWorkCaseId(long workCaseId) {
        log.info("getExSummaryView ::: workCaseId : {}", workCaseId);
        WorkCase workCase = workCaseDAO.findById(workCaseId);

        ExSummary exSummary = exSummaryDAO.findByWorkCaseId(workCaseId);

        BasicInfo basicInfo = basicInfoDAO.findByWorkCaseId(workCaseId);
        int qualitative = 0;
        if(basicInfo != null && basicInfo.getId() != 0){
            qualitative = basicInfo.getQualitativeType(); // A = 1 , B = 2
        }
        QualitativeView qualitativeView = new QualitativeView();

        if(qualitative == 1){ //todo: enum
            qualitativeView = qualitativeControl.getQualitativeA(workCaseId);
        } else if (qualitative == 2) {
            qualitativeView = qualitativeControl.getQualitativeB(workCaseId);
        }

        if (exSummary == null) {
            exSummary = new ExSummary();
        }

        ExSummaryView exSummaryView = exSummaryTransform.transformToView(exSummary);

        //Customer Information
        List<CustomerInfoView> cusListView = customerInfoControl.getAllCustomerByWorkCase(workCaseId);
        if(cusListView != null && cusListView.size() > 0){
            exSummaryView.setBorrowerListView(cusListView);
        } else {
            exSummaryView.setBorrowerListView(null);
        }
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        //NCB Information
        List<NCBInfoView> ncbInfoViewList = ncbInfoControl.getNCBInfoViewByWorkCaseId(workCaseId);
        if(ncbInfoViewList != null && ncbInfoViewList.size() > 0){
            exSummaryView.setNcbInfoListView(ncbInfoViewList);
        } else {
            exSummaryView.setNcbInfoListView(null);
        }
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        //Bank Statement Summary
        BankStatementSummary bankStatementSummary = bankStatementSummaryDAO.getByWorkcase(workCase);
        if(bankStatementSummary != null && bankStatementSummary.getId() != 0){
            ExSumAccountMovementView mainBank = new ExSumAccountMovementView();
            ExSumAccountMovementView otherBank = new ExSumAccountMovementView();
            if(bankStatementSummary.getBankStmtList() != null && bankStatementSummary.getBankStmtList().size() > 0 ){
                for(BankStatement bs : bankStatementSummary.getBankStmtList()){
                    if(bs.getMainAccount() == 1){
                        if(bs.getBank().getCode() == BankType.TMB.value()){ // TMB Bank
                            mainBank = exSummaryTransform.transformBankStmtToExSumBizView(bs);
                        } else { // Other Bank
                            otherBank = exSummaryTransform.transformBankStmtToExSumBizView(bs);
                        }
                    }
                }
            }
            exSummaryView.getExSumAccMovementViewList().add(mainBank);
            exSummaryView.getExSumAccMovementViewList().add(otherBank);
        } else {
            exSummaryView.setExSumAccMovementViewList(null);
        }

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        //Business Information
        BizInfoSummaryView bizInfoSummaryView = bizInfoSummaryControl.onGetBizInfoSummaryByWorkCase(workCaseId);
        BigDecimal bizSize = BigDecimal.ZERO;
        if(bizInfoSummaryView != null && bizInfoSummaryView.getId() != 0){
            if(workCase.getBorrowerType().getId() == BorrowerType.INDIVIDUAL.value()){ // id = 1 use bank stmt
                bizSize = bankStatementSummary.getGrdTotalIncomeGross();
            } else { // use customer
                List<Customer> customer = customerDAO.findByWorkCaseId(workCaseId);
                if(customer != null && customer.size() > 0){
                    for(Customer cus : customer){
                        if(cus.getCustomerEntity().getId() == BorrowerType.JURISTIC.value()){
                            if(cus.getRelation().getId() == RelationValue.BORROWER.value()){ // Borrower
                                bizSize = cus.getJuristic().getSalesFromFinancialStmt();
                            }
                        }
                    }
                }
            }
            ExSumBusinessInfoView exSumBusinessInfoView = exSummaryTransform.transformBizInfoSumToExSumBizView(bizInfoSummaryView,qualitativeView.getQualityResult(),bizSize);
            exSummaryView.setExSumBusinessInfoView(exSumBusinessInfoView);
        } else {
            exSummaryView.setExSumBusinessInfoView(null);
        }
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        log.info("getExSummaryView ::: exSummaryView : {}", exSummaryView);

        return exSummaryView;
    }

    public void saveExSummary(ExSummaryView exSummaryView, long workCaseId, User user) {
        log.info("saveExSummary ::: exSummaryView : {}", exSummaryView);

        WorkCase workCase = workCaseDAO.findById(workCaseId);

        ExSummary exSummary = exSummaryTransform.transformToModel(exSummaryView, workCase, user);
        exSummaryDAO.persist(exSummary);

        //Delete Deviate
        List<ExSumDeviate> esdList = exSumDeviateDAO.findByExSumId(exSummary.getId());
        exSumDeviateDAO.delete(esdList);
        //Save Deviate
        List<ExSumDeviate> exSumDeviateList = exSummaryTransform.transformDeviateToModel(exSummaryView.getDeviateCode(),exSummary.getId());
        exSumDeviateDAO.persist(exSumDeviateList);
    }
}