package com.clevel.selos.businesscontrol;

import com.clevel.selos.dao.working.*;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.*;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.working.*;
import com.clevel.selos.model.view.*;
import com.clevel.selos.transform.ExSummaryTransform;
import com.clevel.selos.util.DateTimeUtil;
import com.clevel.selos.util.Util;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
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
    DBRDAO dbrDAO;
    @Inject
    DecisionDAO decisionDAO;
    @Inject
    NewCreditFacilityDAO newCreditFacilityDAO;

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
    @Inject
    CreditFacProposeControl creditFacProposeControl;

    public ExSummaryView getExSummaryViewByWorkCaseId(long workCaseId) {
        log.info("getExSummaryView ::: workCaseId : {}", workCaseId);
        WorkCase workCase = workCaseDAO.findById(workCaseId);

        ExSummary exSummary = exSummaryDAO.findByWorkCaseId(workCaseId);

        BasicInfo basicInfo = basicInfoDAO.findByWorkCaseId(workCaseId);
        int qualitative = 0;
        if(basicInfo != null && basicInfo.getId() != 0){
            qualitative = basicInfo.getQualitativeType(); // A = 1 , B = 2
        }

        QualitativeView qualitativeView;
        if(qualitative == 1){ //todo: enum
            qualitativeView = qualitativeControl.getQualitativeA(workCaseId);
        } else if (qualitative == 2) {
            qualitativeView = qualitativeControl.getQualitativeB(workCaseId);
        } else {
            qualitativeView = null;
        }

        if (exSummary == null) {
            exSummary = new ExSummary();
        }

        ExSummaryView exSummaryView = exSummaryTransform.transformToView(exSummary);

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        //Customer Information
        List<CustomerInfoView> cusListView = customerInfoControl.getAllCustomerByWorkCase(workCaseId);
        if(cusListView != null && cusListView.size() > 0){
            exSummaryView.setBorrowerListView(cusListView);
        } else {
            exSummaryView.setBorrowerListView(null);
        }

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        //Trade Finance
        NewCreditFacilityView newCreditFacilityView = creditFacProposeControl.findNewCreditFacilityByWorkCase(workCaseId);
        if(newCreditFacilityView != null && newCreditFacilityView.getId() != 0){
            exSummaryView.setTradeFinance(newCreditFacilityView);
        } else {
            exSummaryView.setTradeFinance(null);
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

            ExSumBusinessInfoView exSumBusinessInfoView = exSummaryTransform.transformBizInfoSumToExSumBizView(bizInfoSummaryView,qualitativeView,bizSize);
            exSummaryView.setExSumBusinessInfoView(exSumBusinessInfoView);

            exSummaryView.setBusinessLocationName(bizInfoSummaryView.getBizLocationName());

            String addressTH = bizInfoSummaryView.getAddressNo()+" "+bizInfoSummaryView.getAddressMoo()+" "+
                    bizInfoSummaryView.getAddressBuilding()+" "+bizInfoSummaryView.getAddressStreet()+" "+
                    bizInfoSummaryView.getProvince().getName()+" "+bizInfoSummaryView.getDistrict().getName()+" "+
                    bizInfoSummaryView.getSubDistrict().getName()+" "+bizInfoSummaryView.getCountry().getName();

            exSummaryView.setBusinessLocationAddress(addressTH);
            exSummaryView.setBusinessLocationAddressEN(bizInfoSummaryView.getAddressEng());
            //if isRental = N, display ownerName. If isRental = Y, display expiryDate
            if(bizInfoSummaryView.getRental() == 1) { // 1 is yes??
                exSummaryView.setOwner(bizInfoSummaryView.getExpiryDate().toString());
            } else {
                exSummaryView.setOwner(bizInfoSummaryView.getOwnerName()); //owner name
            }

            //For footer borrower
            //todo: this
            StringBuilder bizPermission = new StringBuilder();
            if(bizInfoSummaryView.getBizInfoDetailViewList() != null && bizInfoSummaryView.getBizInfoDetailViewList().size() > 0){
                for(BizInfoDetailView bizInfoDetailView : bizInfoSummaryView.getBizInfoDetailViewList()){
                    bizPermission = bizPermission.append(bizInfoDetailView.getBizPermission()+", "); //todo: this
                }
            }
//            แสดงประเภทการค้าขายของธุรกิจที่มีสัดส่วนมากที่สุด กรณีมีธุรกิจที่มีสัดส่วนมากที่สุดเท่ากันมากว่า 1 ธุรกิจให้แสดงธุรกิจแรก
            exSummaryView.setBusinessOperationActivity("");
//            แสดง Business Permission จากทุกๆ ธุรกิจ โดยมีเครื่องหมายจุลภาค คั่น
            exSummaryView.setBusinessPermission(bizPermission.toString());
//            แสดงวันที่ Expiration Date ของ Business Permission ที่ Update ที่สุด (หมดอายุ ช้าที่สุด)
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
        exSumCharacteristicView.setSalePerYearBDM(exSummary.getSalePerYearBDM());
        exSumCharacteristicView.setSalePerYearUW(exSummary.getSalePerYearUW());
        exSumCharacteristicView.setGroupSaleBDM(exSummary.getGroupSaleBDM());
        exSumCharacteristicView.setGroupSaleUW(exSummary.getGroupSaleUW());
        exSumCharacteristicView.setGroupExposureBDM(exSummary.getGroupExposureBDM());
        exSumCharacteristicView.setGroupExposureUW(exSummary.getGroupExposureUW());

        if(newCreditFacilityView != null && newCreditFacilityView.getId() != 0){
            if(newCreditFacilityView.getCreditCustomerType() == 1){ // normal 1, prime 2
                exSumCharacteristicView.setCustomer("Normal");
            } else {
                exSumCharacteristicView.setCustomer("Prime");
            }
        }

        exSumCharacteristicView.setIncome(exSummary.getIncome());
        exSumCharacteristicView.setRecommendedWCNeed(exSummary.getRecommendedWCNeed());
        exSumCharacteristicView.setActualWC(exSummary.getActualWC());
        exSumCharacteristicView.setYearInBusiness(exSummary.getYearInBusiness());

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

        //todo: test this , pls delete after test
        calYearInBusinessBorrowerCharacteristic(workCaseId);

        //Delete Deviate
        List<ExSumDeviate> esdList = exSumDeviateDAO.findByExSumId(exSummary.getId());
        exSumDeviateDAO.delete(esdList);
        //Save Deviate
        List<ExSumDeviate> exSumDeviateList = exSummaryTransform.transformDeviateToModel(exSummaryView.getDeviateCode(),exSummary.getId());
        exSumDeviateDAO.persist(exSumDeviateList);
    }

    //TODO : Method Call For Page
    public void calForCreditFacility(long workCaseId){
        calIncomeBorrowerCharacteristic(workCaseId);
        calRecommendedWCNeedBorrowerCharacteristic(workCaseId);
    }

    public void calForDecision(long workCaseId){
        calIncomeBorrowerCharacteristic(workCaseId);
        calActualWCBorrowerCharacteristic(workCaseId);
        calGroupExposureBorrowerCharacteristic(workCaseId);
    }

    public void calForBankStmtSummary(long workCaseId){
        calSalePerYearBorrowerCharacteristic(workCaseId);
        calGroupSaleBorrowerCharacteristic(workCaseId);
    }

    public void calForDBR(long workCaseId){
        calIncomeBorrowerCharacteristic(workCaseId);
    }

    public void calForBizInfoSummary(long workCaseId){
        calYearInBusinessBorrowerCharacteristic(workCaseId);
    }

    //TODO : Business login here
    //Borrower Characteristic - income ( Line 45 )
    //Credit Facility-Propose + DBR + Decision
    //[สินเชื่อหมุนเวียนที่มีอยู่กับ TMB + OD Limit ที่อนุมัติ + Loan Core WC ที่อนุมัติ] / (รายได้ต่อเดือน Adjusted หน้า DBR *12)
    public void calIncomeBorrowerCharacteristic(long workCaseId){ //TODO : Credit Facility-Propose & DBR & Decision , Pls Call me !!
        DBR dbr = dbrDAO.findByWorkCaseId(workCaseId);
        NewCreditFacility newCreditFacility = newCreditFacilityDAO.findByWorkCaseId(workCaseId);

        BigDecimal totalWCTMB = newCreditFacility.getTotalWcTmb();
        BigDecimal odLimit = newCreditFacility.getTotalCommercialAndOBOD();
        BigDecimal loanCoreWC = newCreditFacility.getTotalCommercial();
        BigDecimal adjusted = dbr.getMonthlyIncomeAdjust();

        BigDecimal income = (totalWCTMB.add(odLimit).add(loanCoreWC)).divide((adjusted.multiply(new BigDecimal(12))));

        ExSummary exSummary = exSummaryDAO.findByWorkCaseId(workCaseId);
        if(exSummary == null){
            exSummary = new ExSummary();
            WorkCase workCase = new WorkCase();
            workCase.setId(workCaseId);
            exSummary.setWorkCase(workCase);
        }
        exSummary.setIncome(income);

        exSummaryDAO.persist(exSummary);
    }

    //Borrower Characteristic - recommendedWCNeed ( Line 46 )
    //Credit Facility-Propose หัวข้อ WC Requirement
    //Refinance from Basic Info
//    กรณี Refinance In Flag = Yes + Prime
//    Min [สินเชื่อหมุนเวียนที่สามารถพิจารณาให้ได้จากกรณีที่ 2 : คำนวณจาก 1.5 เท่าของ WC, สินเชื่อหมุนเวียนที่สามารถพิจารณาให้ได้จากกรณีที่ 3 : คำนวณจาก 35% ของรายได้]
//    กรณี Refinance In Flag = Yes + Normal
//    Min [สินเชื่อหมุนเวียนที่สามารถพิจารณาให้ได้จากกรณีที่ 1 : คำนวณจาก 1.25 เท่าของ WC, สินเชื่อหมุนเวียนที่สามารถพิจารณาให้ได้จากกรณีที่ 3 : คำนวณจาก 35% ของรายได้]
//    กรณี Refinance In Flag = No + Prime
//    Min [(ความต้องการเงินทุนหมุนเวียน - รวมวงเงินสินเชื่อหมุนเวียนของ TMB) , สินเชื่อหมุนเวียนที่สามารถพิจารณาให้ได้จากกรณีที่ 2 : คำนวณจาก 1.5 เท่าของ WC, สินเชื่อหมุนเวียนที่สามารถพิจารณาให้ได้จากกรณีที่ 3 : คำนวณจาก 35% ของรายได้]
//    กรณี Refinance In Flag = No + Normal
//    Min [(ความต้องการเงินทุนหมุนเวียน - รวมวงเงินสินเชื่อหมุนเวียนของ TMB) , สินเชื่อหมุนเวียนที่สามารถพิจารณาให้ได้จากกรณีที่ 1 : คำนวณจาก 1.25 เท่าของ WC, สินเชื่อหมุนเวียนที่สามารถพิจารณาให้ได้จากกรณีที่ 3 : คำนวณจาก 35% ของรายได้]
    public void calRecommendedWCNeedBorrowerCharacteristic(long workCaseId){ //TODO : Credit Facility-Propose , Pls Call me !!
        NewCreditFacility newCreditFacility = newCreditFacilityDAO.findByWorkCaseId(workCaseId);
        BasicInfo basicInfo = basicInfoDAO.findByWorkCaseId(workCaseId);

        BigDecimal recommendedWCNeed = BigDecimal.ZERO;
        BigDecimal value1 = BigDecimal.ZERO;
        BigDecimal value2 = BigDecimal.ZERO;
        BigDecimal value3 = BigDecimal.ZERO;
        BigDecimal value6 = BigDecimal.ZERO;
        if(newCreditFacility != null && newCreditFacility.getId() != 0){
            if(newCreditFacility.getCase1WcLimit() != null){
                value1 = newCreditFacility.getCase1WcLimit();
            }
            if(newCreditFacility.getCase2WcLimit() != null){
                value2 = newCreditFacility.getCase2WcLimit();
            }
            if(newCreditFacility.getCase3WcLimit() != null){
                value3 = newCreditFacility.getCase3WcLimit();
            }
            value6 = Util.subtract(newCreditFacility.getWcNeed(),newCreditFacility.getTotalWcTmb());
        }

        if(basicInfo != null && basicInfo.getId() != 0 && newCreditFacility != null){
            if(basicInfo.getRefinanceIN() == 1){
                if(newCreditFacility.getCreditCustomerType() == CreditCustomerType.PRIME.value()){
                    recommendedWCNeed = getMinBigDecimal(value2,value3);
                } else {
                    recommendedWCNeed = getMinBigDecimal(value1,value3);
                }
            } else {
                if(newCreditFacility.getCreditCustomerType() == CreditCustomerType.PRIME.value()){
                    recommendedWCNeed = getMinBigDecimal(value2,value3,value6);
                } else {
                    recommendedWCNeed = getMinBigDecimal(value1,value3,value6);
                }
            }
        }

        ExSummary exSummary = exSummaryDAO.findByWorkCaseId(workCaseId);
        if(exSummary == null){
            exSummary = new ExSummary();
            WorkCase workCase = new WorkCase();
            workCase.setId(workCaseId);
            exSummary.setWorkCase(workCase);
        }
        exSummary.setRecommendedWCNeed(recommendedWCNeed);

        exSummaryDAO.persist(exSummary);
    }

    //Borrower Characteristic - actualWC ( Line 47 )
    //Decision หัวข้อ Approve Credit
//    Sum( วงเงินสินเชื่อหมุนเวียนที่อนุมัต)
    public void calActualWCBorrowerCharacteristic(long workCaseId){ //TODO : Decision , Pls Call me !!
        NewCreditFacility newCreditFacility = newCreditFacilityDAO.findByWorkCaseId(workCaseId);
        BigDecimal actualWC = BigDecimal.ZERO;
        if(newCreditFacility != null && newCreditFacility.getNewCreditDetailList() != null) {
            for(NewCreditDetail n : newCreditFacility.getNewCreditDetailList()){
                if(n.getType() == 1){ // 0 = propose , 1 = approve // TODO: enum or not
    //                actualWC = actualWC.add(n.get);
                }
            }
        }
        ExSummary exSummary = exSummaryDAO.findByWorkCaseId(workCaseId);
        if(exSummary == null){
            exSummary = new ExSummary();
            WorkCase workCase = new WorkCase();
            workCase.setId(workCaseId);
            exSummary.setWorkCase(workCase);
        }
        exSummary.setActualWC(actualWC);

        exSummaryDAO.persist(exSummary);
    }

    //Borrower Characteristic - salePerYearBDM , salePerYearUW ( Line 52-53 )
    //Bank Statement Summary
//    Grand Total Income Net BDM จากหน้า Bank Statement Summary * 12
//    Grand Total Income Net UW จากหน้า Bank Statement Summary * 12
    public void calSalePerYearBorrowerCharacteristic(long workCaseId){ //TODO: BankStatementSummary , Pls Call me !!
        BankStatementSummary bankStatementSummary = bankStatementSummaryDAO.findByWorkCaseId(workCaseId);
        if(bankStatementSummary != null && bankStatementSummary.getId() != 0){
            ExSummary exSummary = exSummaryDAO.findByWorkCaseId(workCaseId);
            if(exSummary == null){
                exSummary = new ExSummary();
                WorkCase workCase = new WorkCase();
                workCase.setId(workCaseId);
                exSummary.setWorkCase(workCase);
            }
            if(bankStatementSummary.getGrdTotalIncomeNetBDM() != null){
                exSummary.setSalePerYearBDM(bankStatementSummary.getGrdTotalIncomeNetBDM().multiply(new BigDecimal(12)));
            }
            if(bankStatementSummary.getGrdTotalIncomeNetUW() != null){
                exSummary.setSalePerYearUW(bankStatementSummary.getGrdTotalIncomeNetUW().multiply(new BigDecimal(12)));
            }
            exSummaryDAO.persist(exSummary);

        }
    }

    //Borrower Characteristic - groupSaleBDM , groupSaleUW ( Line 55-56 )
    //Customer Info Detail , Bank Statement Summary
//    groupSaleBDM - กรณีผู้กู้ = Juristic (รายได้ตามงบการเงิน จาก Cust Info Detail (Juristic) + รายได้ของผู้ค้ำฯ / ผู้เกี่ยวข้องทุกคนที่ Flag Group Income = Y) * 12
//    groupSaleBDM - กรณีผู้กู้ = Individual (Grand Total Income Gross จากหน้า Bank Statement Summary + รายได้ของผู้ค้ำฯ / ผู้เกี่ยวข้องทุกคนที่ Flag Group Income = Y)*12
//    Fix ค่าของ BDM เมื่อส่งมายัง UW และ UW มีการแก้ไขข้อมูล
//    groupSaleUW - กรณีผู้กู้ = Juristic (รายได้ตามงบการเงิน จาก Cust Info Detail (Juristic) + รายได้ของผู้ค้ำฯ / ผู้เกี่ยวข้องทุกคนที่ Flag Group Income = Y) * 12
//    groupSaleUW - กรณีผู้กู้ = Individual (Grand Total Income Gross จากหน้า Bank Statement Summary + รายได้ของผู้ค้ำฯ / ผู้เกี่ยวข้องทุกคนที่ Flag Group Income = Y) * 12
    public void calGroupSaleBorrowerCharacteristic(long workCaseId){ //TODO: BankStatementSummary & Customer Info Juristic , Pls Call me !!
        WorkCase workCase = workCaseDAO.findById(workCaseId);
        List<CustomerInfoView> cusListView = customerInfoControl.getAllCustomerByWorkCase(workCaseId);
        User user = getCurrentUser();
        ExSummary exSummary = exSummaryDAO.findByWorkCaseId(workCaseId);
        if(exSummary == null){
            exSummary = new ExSummary();
            exSummary.setWorkCase(workCase);
        }
        if(workCase.getBorrowerType().getId() == BorrowerType.INDIVIDUAL.value()){ // use bank statement
            if(user.getRole().getId() != RoleValue.UW.id()){//Fix ค่าของ BDM เมื่อส่งมายัง UW และ UW มีการแก้ไขข้อมูล
//    groupSaleBDM - กรณีผู้กู้ = Individual (Grand Total Income Gross จากหน้า Bank Statement Summary + รายได้ของผู้ค้ำฯ / ผู้เกี่ยวข้องทุกคนที่ Flag Group Income = Y)*12
//            exSumCharacteristicView.setGroupSaleBDM(bankStatementSummary.getGrdTotalIncomeGross());
            }

//    groupSaleUW - กรณีผู้กู้ = Individual (Grand Total Income Gross จากหน้า Bank Statement Summary + รายได้ของผู้ค้ำฯ / ผู้เกี่ยวข้องทุกคนที่ Flag Group Income = Y) * 12
//            exSumCharacteristicView.setGroupSaleUW(cus.getSalesFromFinancialStmt());
        } else { // use customer
            if(cusListView != null && cusListView.size() > 0){
                for(CustomerInfoView cus : cusListView){
                    if(cus.getCustomerEntity().getId() == BorrowerType.JURISTIC.value()){
                        if(cus.getRelation().getId() == RelationValue.BORROWER.value()){ // Borrower
                        }
                    }
                }
            }

            if(user.getRole().getId() != RoleValue.UW.id()){//Fix ค่าของ BDM เมื่อส่งมายัง UW และ UW มีการแก้ไขข้อมูล
//    groupSaleBDM - กรณีผู้กู้ = Juristic (รายได้ตามงบการเงิน จาก Cust Info Detail (Juristic) + รายได้ของผู้ค้ำฯ / ผู้เกี่ยวข้องทุกคนที่ Flag Group Income = Y) * 12
//            exSumCharacteristicView.setGroupSaleBDM();
            }

//    groupSaleUW - กรณีผู้กู้ = Juristic (รายได้ตามงบการเงิน จาก Cust Info Detail (Juristic) + รายได้ของผู้ค้ำฯ / ผู้เกี่ยวข้องทุกคนที่ Flag Group Income = Y) * 12
//            exSumCharacteristicView.setGroupSaleUW();
        }
    }

    //Borrower Characteristic - groupExposureBDM , groupExposureUW ( Line 58-59 )
    //Decision
//    groupExposureBDM - Group Total Exposure + Total Propose Credit
//    groupExposureUW - Group Total Exposure + Total Approved Credit
    public void calGroupExposureBorrowerCharacteristic(long workCaseId){ //TODO: Decision , Credit Facility-Propose , Pls Call me !!
        NewCreditFacility newCreditFacility = newCreditFacilityDAO.findByWorkCaseId(workCaseId);
        BigDecimal groupExposureBDM = newCreditFacility.getTotalExposure().add(newCreditFacility.getTotalPropose());
        BigDecimal groupExposureUW = newCreditFacility.getTotalExposure().add(newCreditFacility.getTotalApproveCredit());

        ExSummary exSummary = exSummaryDAO.findByWorkCaseId(workCaseId);
        if(exSummary == null){
            exSummary = new ExSummary();
            WorkCase workCase = new WorkCase();
            workCase.setId(workCaseId);
            exSummary.setWorkCase(workCase);
        }
        exSummary.setGroupExposureBDM(groupExposureBDM);
        exSummary.setGroupExposureUW(groupExposureUW);

        exSummaryDAO.persist(exSummary);
    }

    //Borrower Characteristic - yearInBusiness , yearInBusinessMonth ( Line 49-50 )
    //Business Info Summary
//    Max of (วันก่อตั้ง หรือ วันจดทะเบียนพาณิชย์ in businessInfoSummary)
//    calculate months from yearInBusiness fields.
    public void calYearInBusinessBorrowerCharacteristic(long workCaseId){ //TODO: Business Info Summary , Pls Call me !!
        BizInfoSummaryView bizInfoSummaryView = bizInfoSummaryControl.onGetBizInfoSummaryByWorkCase(workCaseId);
        Date yearInBiz = DateTimeUtil.getMaxOfDate(bizInfoSummaryView.getRegistrationDate(), bizInfoSummaryView.getEstablishDate());
        String year = DateTimeUtil.calYearMonth(yearInBiz);
        //todo:yearInBizMonth To send to BRMS
        int month = DateTimeUtil.calMonth(yearInBiz);

        ExSummary exSummary = exSummaryDAO.findByWorkCaseId(workCaseId);
        if(exSummary == null){
            exSummary = new ExSummary();
            WorkCase workCase = new WorkCase();
            workCase.setId(workCaseId);
            exSummary.setWorkCase(workCase);
        }
        exSummary.setYearInBusiness(year);

        exSummaryDAO.persist(exSummary);
    }
}