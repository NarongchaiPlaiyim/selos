package com.clevel.selos.businesscontrol;

import com.clevel.selos.dao.working.*;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.BankType;
import com.clevel.selos.model.BorrowerType;
import com.clevel.selos.model.RelationValue;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.working.*;
import com.clevel.selos.model.view.*;
import com.clevel.selos.transform.ExSummaryTransform;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class ExSummaryControl extends BusinessControl {
    @Inject
    @SELOS
    Logger log;

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
    NewCreditFacilityDAO newCreditFacilityDAO;
    @Inject
    DBRDAO dbrDAO;

    @Inject
    ExSummaryTransform exSummaryTransform;

    @Inject
    CustomerInfoControl customerInfoControl;
    @Inject
    NCBInfoControl ncbInfoControl;
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

        if(qualitativeView == null) { // todo:check this
            qualitativeView = new QualitativeView();
            qualitativeView.setQualityResult("qualitativeClass");
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

        //Trade Finance
        NewCreditFacility newCreditFacility = newCreditFacilityDAO.findByWorkCaseId(workCaseId);


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
        BankStatementSummary bankStatementSummary = bankStatementSummaryDAO.findByWorkCaseId(workCaseId);
        exSummaryView.setExSumAccMovementViewList(new ArrayList<ExSumAccountMovementView>());
        if(bankStatementSummary != null && bankStatementSummary.getId() != 0){
            ExSumAccountMovementView mainBank = null;
            ExSumAccountMovementView otherBank = null;
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
            if(mainBank != null || otherBank != null) {
                if(mainBank != null) {
                    exSummaryView.getExSumAccMovementViewList().add(mainBank);
                }
                if(otherBank != null) {
                    exSummaryView.getExSumAccMovementViewList().add(otherBank);
                }
            } else {
                exSummaryView.setExSumAccMovementViewList(null);
            }
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
//                List<Customer> customer = customerDAO.findByWorkCaseId(workCaseId);
                if(cusListView != null && cusListView.size() > 0){
                    for(CustomerInfoView cus : cusListView){
                        if(cus.getCustomerEntity().getId() == BorrowerType.JURISTIC.value()){
                            if(cus.getRelation().getId() == RelationValue.BORROWER.value()){ // Borrower
                                bizSize = cus.getSalesFromFinancialStmt();
                            }
                        }
                    }
                }
            }

            ExSumBusinessInfoView exSumBusinessInfoView = exSummaryTransform.transformBizInfoSumToExSumBizView(bizInfoSummaryView,qualitativeView.getQualityResult(),bizSize);
            exSummaryView.setExSumBusinessInfoView(exSumBusinessInfoView);

            exSummaryView.setBusinessLocationName(bizInfoSummaryView.getBizLocationName());
            exSummaryView.setBusinessLocationAddress(bizInfoSummaryView.getAddressBuilding()); //todo: change this or not?
            exSummaryView.setBusinessLocationAddressEN(bizInfoSummaryView.getAddressEng());
            //todo: if isRental = N, display ownerName. If isRental = Y, display expiryDate
            if(bizInfoSummaryView.getRental() == 1) { // 1 is yes??
                exSummaryView.setOwner(bizInfoSummaryView.getExpiryDate().toString());
            } else {
//                exSummaryView.setOwner();
            }

            //For footer borrower
            //todo: this
            exSummaryView.setBusinessOperationActivity("");
            exSummaryView.setBusinessPermission("");
            exSummaryView.setExpiryDate(bizInfoSummaryView.getExpiryDate());
        } else {
            exSummaryView.setExSumBusinessInfoView(null);
        }

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        //Borrower
        ExSumCharacteristicView exSumCharacteristicView = new ExSumCharacteristicView();
        DBR dbr = dbrDAO.findByWorkCaseId(workCaseId);
        if(dbr != null && dbr.getId() != 0){
            exSumCharacteristicView.setCurrentDBR(dbr.getCurrentDBR());
            exSumCharacteristicView.setFinalDBR(dbr.getFinalDBR());
        }
        if(bizInfoSummaryView != null && bizInfoSummaryView.getId() != 0){
            if(workCase.getBorrowerType().getId() == BorrowerType.INDIVIDUAL.value()){
                exSumCharacteristicView.setStartBusinessDate(bizInfoSummaryView.getRegistrationDate());
            } else {
                exSumCharacteristicView.setStartBusinessDate(bizInfoSummaryView.getEstablishDate());
            }
        }
        // todo: business logic here
        if(bankStatementSummary != null && bankStatementSummary.getId() != 0){
//            Grand Total Income Net BDM จากหน้า Bank Statement Summary * 12
            exSumCharacteristicView.setSalePerYearBDM(bankStatementSummary.getGrdTotalIncomeNetBDM().multiply(new BigDecimal(12)));
//            Grand Total Income Net UW จากหน้า Bank Statement Summary * 12
            exSumCharacteristicView.setGroupSaleUW(bankStatementSummary.getGrdTotalIncomeNetUW().multiply(new BigDecimal(12)));
        }

        // todo: business logic here
        if(workCase.getBorrowerType().getId() == BorrowerType.INDIVIDUAL.value()){ // use bank statement
//            กรณีผู้กู้ = Individual (Grand Total Income Gross จากหน้า Bank Statement Summary + รายได้ของผู้ค้ำฯ / ผู้เกี่ยวข้องทุกคนที่ Flag Group Income = Y)*12
//            exSumCharacteristicView.setGroupSaleBDM(bankStatementSummary.getGrdTotalIncomeGross());

//            กรณีผู้กู้ = Individual (Grand Total Income Gross จากหน้า Bank Statement Summary + รายได้ของผู้ค้ำฯ / ผู้เกี่ยวข้องทุกคนที่ Flag Group Income = Y) * 12
            if(cusListView != null && cusListView.size() > 0){
                for(CustomerInfoView cus : cusListView){
                    if(cus.getCustomerEntity().getId() == BorrowerType.JURISTIC.value()){
                        if(cus.getRelation().getId() == RelationValue.BORROWER.value()){ // Borrower
//                            exSumCharacteristicView.setGroupSaleUW(cus.getSalesFromFinancialStmt());
                        }
                    }
                }
            }
        } else { // use customer
//            กรณีผู้กู้ = Juristic (รายได้ตามงบการเงิน จาก Cust Info Detail (Juristic) + รายได้ของผู้ค้ำฯ / ผู้เกี่ยวข้องทุกคนที่ Flag Group Income = Y) * 12
//            exSumCharacteristicView.setGroupSaleBDM();

//            กรณีผู้กู้ = Juristic (รายได้ตามงบการเงิน จาก Cust Info Detail (Juristic) + รายได้ของผู้ค้ำฯ / ผู้เกี่ยวข้องทุกคนที่ Flag Group Income = Y) * 12
//            exSumCharacteristicView.setGroupSaleUW();
        }

        exSummaryView.setExSumCharacteristicView(exSumCharacteristicView);

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        log.info("getExSummaryView ::: exSummaryView : {}", exSummaryView);

        return exSummaryView;
    }

    public void saveExSummary(ExSummaryView exSummaryView, long workCaseId) {
        log.info("saveExSummary ::: exSummaryView : {}", exSummaryView);

        User user = getCurrentUser();

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