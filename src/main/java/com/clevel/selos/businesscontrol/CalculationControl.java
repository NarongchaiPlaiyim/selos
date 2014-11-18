package com.clevel.selos.businesscontrol;

import com.clevel.selos.dao.master.ProductFormulaDAO;
import com.clevel.selos.dao.relation.PotentialColToTCGColDAO;
import com.clevel.selos.dao.relation.PrdProgramToCreditTypeDAO;
import com.clevel.selos.dao.working.*;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.*;
import com.clevel.selos.model.db.master.*;
import com.clevel.selos.model.db.relation.PotentialColToTCGCol;
import com.clevel.selos.model.db.relation.PrdProgramToCreditType;
import com.clevel.selos.model.db.working.*;
import com.clevel.selos.model.view.*;
import com.clevel.selos.transform.CustomerTransform;
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
public class CalculationControl extends BusinessControl{
    @Inject
    @SELOS
    Logger log;

    @Inject
    private DBRControl dbrControl;
    @Inject
    private NCBInfoControl ncbInfoControl;
    @Inject
    private BizInfoSummaryControl bizInfoSummaryControl;
    @Inject
    private CustomerInfoControl customerInfoControl;
    @Inject
    private QualitativeControl qualitativeControl;
    @Inject
    private ExSummaryControl exSummaryControl;

    @Inject
    private ProposeLineDAO proposeLineDAO;
    @Inject
    private ExSummaryDAO exSummaryDAO;
    @Inject
    private BankStatementSummaryDAO bankStatementSummaryDAO;
    @Inject
    private BasicInfoDAO basicInfoDAO;
    @Inject
    private DBRDAO dbrDAO;
    @Inject
    private TCGDAO tcgDAO;
    @Inject
    private PrdProgramToCreditTypeDAO prdProgramToCreditTypeDAO;
    @Inject
    private ProductFormulaDAO productFormulaDAO;
    @Inject
    private DecisionDAO decisionDAO;
    @Inject
    private CustomerDAO customerDAO;
    @Inject
    private ExistingCreditFacilityDAO existingCreditFacilityDAO;
    @Inject
    private WorkCaseDAO workCaseDAO;
    @Inject
    private PotentialColToTCGColDAO potentialColToTCGColDAO;
    @Inject
    private ExistingCreditDetailDAO existingCreditDetailDAO;

    @Inject
    private CustomerTransform customerTransform;

    @Inject
    public CalculationControl() {
    }

    //TODO : Method Call For Page
    public void calForProposeLine(long workCaseId){
        calIncomeBorrowerCharacteristic(workCaseId);
        calRecommendedWCNeedBorrowerCharacteristic(workCaseId);
        calGroupExposureBorrowerCharacteristic(workCaseId);
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
        calIncomeFactor(workCaseId);
    }

    public void calForCustomerInfo(long workCaseId){
        calGroupSaleBorrowerCharacteristic(workCaseId);
    }

    public void calForBasicInfo(long workCaseId) {
        calIncomeBorrowerCharacteristic(workCaseId);
        calRecommendedWCNeedBorrowerCharacteristic(workCaseId);
        calGroupSaleBorrowerCharacteristic(workCaseId);
    }

    public void calForTCG(long workCaseId) {
        calIncomeBorrowerCharacteristic(workCaseId);
    }

    public void calForWC(long workCaseId) {
        calIncomeBorrowerCharacteristic(workCaseId);
        calRecommendedWCNeedBorrowerCharacteristic(workCaseId);
    }

    // ----------------------------------------------------------------------------------------------------------------------------------------------- //
    // ----------------------------------------------------------------------------------------------------------------------------------------------- //
    // ---------------------------------------------------          Calculation Function          ---------------------------------------------------- //
    // ----------------------------------------------------------------------------------------------------------------------------------------------- //
    // ----------------------------------------------------------------------------------------------------------------------------------------------- //

    //TODO : Business login here
    //Borrower Characteristic - income ( Line 45 )
    //Credit Facility-Propose + DBR + Decision
    //[สินเชื่อหมุนเวียนที่มีอยู่กับ TMB + OD Limit ที่อนุมัติ + Loan Core WC ที่อนุมัติ] / (รายได้ต่อเดือน Adjusted หน้า DBR *12)
    public void calIncomeBorrowerCharacteristic(long workCaseId){ //TODO : Credit Facility-Propose & DBR & Decision , Pls Call me !!
        log.debug("calIncomeBorrowerCharacteristic :: workCaseId : {}",workCaseId);
        DBR dbr = dbrDAO.findByWorkCaseId(workCaseId);
        ProposeLine proposeLine = proposeLineDAO.findByWorkCaseId(workCaseId);
        BasicInfo basicInfo = basicInfoDAO.findByWorkCaseId(workCaseId);
        TCG tcg = tcgDAO.findByWorkCaseId(workCaseId);

        BigDecimal totalWCTMB = BigDecimal.ZERO;
        BigDecimal limitBDM = BigDecimal.ZERO;
        BigDecimal limitUW = BigDecimal.ZERO;
        BigDecimal adjusted = BigDecimal.ZERO;
        BigDecimal twelve = BigDecimal.valueOf(12);

        if(!Util.isNull(proposeLine) && !Util.isZero(proposeLine.getId())) {
            totalWCTMB = proposeLine.getTotalWCTmb();
            if(!Util.isNull(proposeLine.getProposeCreditInfoList()) && !Util.isZero(proposeLine.getProposeCreditInfoList().size())) {
                if(!Util.isNull(basicInfo) && !Util.isNull(tcg)) {
                    int tcgFlag = tcg.getTcgFlag();
                    int specialProgramId = 0;
                    int marketTableFlag = 0;
                    if(basicInfo.getApplySpecialProgram() == 1) {
                        if(!Util.isNull(basicInfo.getSpecialProgram()) && !Util.isZero(basicInfo.getSpecialProgram().getId())) {
                            specialProgramId = basicInfo.getSpecialProgram().getId();
                        }
                    }
                    if(!Util.isNull(dbr)) {
                        marketTableFlag = dbr.getMarketableFlag();
                    }
                    for(ProposeCreditInfo creditInfo : proposeLine.getProposeCreditInfoList()) {
                        if(!Util.isNull(creditInfo) && !Util.isNull(creditInfo.getCreditType()) && !Util.isNull(creditInfo.getProductProgram())) {
                            PrdProgramToCreditType prdProgramToCreditType = prdProgramToCreditTypeDAO.getPrdProgramToCreditType(creditInfo.getCreditType(), creditInfo.getProductProgram());
                            if(!Util.isNull(proposeLine.getCreditCustomerType()) && !Util.isNull(prdProgramToCreditType)) {
                                ProductFormula productFormula;
                                if(creditInfo.getCreditType().getCreditGroup() == CreditTypeGroup.OD.value()) {
                                    log.debug("Credit Group == OD");
                                    productFormula = productFormulaDAO.findProductFormulaPropose(prdProgramToCreditType, proposeLine.getCreditCustomerType(), specialProgramId, tcgFlag, marketTableFlag);
                                } else {
                                    log.debug("Credit Group != OD ");
                                    productFormula = productFormulaDAO.findProductFormulaPropose(prdProgramToCreditType, proposeLine.getCreditCustomerType(), specialProgramId, tcgFlag);
                                }
                                if(!Util.isNull(productFormula) && !Util.isNull(productFormula.getWcCalculate())) {
                                    if(productFormula.getWcCalculate() == 2) {
                                        if(creditInfo.getProposeType() == ProposeType.P) {
                                            limitBDM = Util.add(limitBDM, creditInfo.getLimit());
                                        } else if(creditInfo.getProposeType() == ProposeType.A && creditInfo.getUwDecision().value() == DecisionType.APPROVED.value()) {
                                            limitUW = Util.add(limitUW, creditInfo.getLimit());
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        if(dbr != null && dbr.getId() != 0){
            adjusted = dbr.getMonthlyIncomeAdjust();
        }

        User user = getCurrentUser();
        BigDecimal income = BigDecimal.ZERO;
        if(!Util.isNull(user) && !Util.isNull(user.getRole()) && user.getRole().getId() == RoleValue.UW.id()) { // USE BDM
            income = Util.divide(Util.add(totalWCTMB,limitBDM),Util.multiply(adjusted,twelve));
        } else if (!Util.isNull(user) && !Util.isNull(user.getRole()) && user.getRole().getId() != RoleValue.UW.id()) { // USE UW
            income = Util.divide(Util.add(totalWCTMB,limitUW),Util.multiply(adjusted,twelve));
        }

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
        log.debug("calRecommendedWCNeedBorrowerCharacteristic :: workCaseId : {}",workCaseId);
        ProposeLine proposeLine = proposeLineDAO.findByWorkCaseId(workCaseId);
        BasicInfo basicInfo = basicInfoDAO.findByWorkCaseId(workCaseId);

        BigDecimal recommendedWCNeed = BigDecimal.ZERO;

        if(!Util.isNull(proposeLine) && !Util.isZero(proposeLine.getId())) {
            BigDecimal value1 = BigDecimal.ZERO;
            BigDecimal value2 = BigDecimal.ZERO;
            BigDecimal value3 = BigDecimal.ZERO;

            if(!Util.isNull(proposeLine.getCase1WCLimit())){
                value1 = proposeLine.getCase1WCLimit();
            }
            if(!Util.isNull(proposeLine.getCase2WCLimit())){
                value2 = proposeLine.getCase2WCLimit();
            }
            if(!Util.isNull(proposeLine.getCase3WCLimit())){
                value3 = proposeLine.getCase3WCLimit();
            }

            BigDecimal value6 = Util.subtract(proposeLine.getWcNeed(), proposeLine.getTotalWCTmb());

            if(!Util.isNull(basicInfo) && !Util.isZero(basicInfo.getId())) {
                if(basicInfo.getRefinanceIN() == RadioValue.YES.value()){
                    if(proposeLine.getCreditCustomerType() == CreditCustomerType.PRIME.value()){
                        recommendedWCNeed = getMinBigDecimal(value2,value3);
                    } else {
                        recommendedWCNeed = getMinBigDecimal(value1,value3);
                    }
                } else {
                    if(proposeLine.getCreditCustomerType() == CreditCustomerType.PRIME.value()){
                        recommendedWCNeed = getMinBigDecimal(value2,value3,value6);
                    } else {
                        recommendedWCNeed = getMinBigDecimal(value1,value3,value6);
                    }
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
        log.debug("calActualWCBorrowerCharacteristic :: workCaseId : {}",workCaseId);
        Decision decision = decisionDAO.findByWorkCaseId(workCaseId);
        BigDecimal actualWC = null;
        if (decision != null) {
            actualWC = decision.getTotalApproveCredit();
        }

        ExSummary exSummary = exSummaryDAO.findByWorkCaseId(workCaseId);
        if(exSummary == null){
            exSummary = new ExSummary();
            WorkCase workCase = new WorkCase();
            workCase.setId(workCaseId);
            exSummary.setWorkCase(workCase);
        }

        if(actualWC != null){
            exSummary.setActualWC(actualWC);
            exSummaryDAO.persist(exSummary);
        }
    }

    //Borrower Characteristic - salePerYearBDM , salePerYearUW ( Line 52-53 )
    //Bank Statement Summary
//    Grand Total Income Net BDM จากหน้า Bank Statement Summary * 12
//    Grand Total Income Net UW จากหน้า Bank Statement Summary * 12
    public void calSalePerYearBorrowerCharacteristic(long workCaseId){ //TODO: BankStatementSummary , Pls Call me !!
        log.debug("calSalePerYearBorrowerCharacteristic :: workCaseId : {}",workCaseId);
        BigDecimal twelve = BigDecimal.valueOf(12);
        BankStatementSummary bankStatementSummary = bankStatementSummaryDAO.findByWorkCaseId(workCaseId);
        if(bankStatementSummary != null && bankStatementSummary.getId() != 0){
            ExSummary exSummary = exSummaryDAO.findByWorkCaseId(workCaseId);
            if(exSummary == null){
                exSummary = new ExSummary();
                WorkCase workCase = new WorkCase();
                workCase.setId(workCaseId);
                exSummary.setWorkCase(workCase);
            }

            User user = getCurrentUser();

            if(user.getRole().getId() != RoleValue.UW.id() && bankStatementSummary.getGrdTotalIncomeNetBDM() != null){
                exSummary.setSalePerYearBDM(Util.multiply(bankStatementSummary.getGrdTotalIncomeNetBDM(),twelve));
            }
            if(user.getRole().getId() == RoleValue.UW.id() && bankStatementSummary.getGrdTotalIncomeNetUW() != null){
                exSummary.setSalePerYearUW(Util.multiply(bankStatementSummary.getGrdTotalIncomeNetUW(),twelve));
            }

            exSummaryDAO.persist(exSummary);
        }
    }

    //Borrower Characteristic - groupSaleBDM , groupSaleUW ( Line 55-56 )
    //Customer Info Detail , Bank Statement Summary
//    - กรณีผู้กู้เป็น Juristic
//    - ใช้ รายได้ตามงบการเงิน จาก Customer ( Juristic ) ทั้งหมด ( ไม่ว่า Relation จะเป็นอะไร ) บวกกับ Approx. Income จาก Customer ( Individual ) ที่ Relation = Guarantor , Related ที่มี Flag Group Sale ใน Reference เป็น Y
//    - กรณีผู้กู้เป็น Individual
//    - ใช้ Grand Total Income Gross จากหน้า Bank Statement คูณด้วย 12 แล้ว บวกกับ รายได้ตามงบการเงิน จากหน้า Customer ( Juristic ) ที่ Relation = Guarantor , Related ที่มี Flag Group Sale ใน Reference เป็น Y บวกกับ Approx. Income จาก Customer ( Individual ) ที่ Relation = Guarantor , Related ที่มี Flag Group Sale ใน Reference เป็น Y
    public void calGroupSaleBorrowerCharacteristic(long workCaseId){ //TODO: BankStatementSummary & Customer Info Juristic , Pls Call me !!
        log.debug("calGroupSaleBorrowerCharacteristic :: workCaseId : {}",workCaseId);
        BasicInfo basicInfo = basicInfoDAO.findByWorkCaseId(workCaseId);
        List<CustomerInfoView> cusListView = customerInfoControl.getAllCustomerByWorkCase(workCaseId);
        User user = getCurrentUser();

//    Fix ค่าของ BDM เมื่อส่งมายัง UW และ UW มีการแก้ไขข้อมูล
        BigDecimal groupSaleBDM;
        BigDecimal groupSaleUW;
        BigDecimal twelve = BigDecimal.valueOf(12);

        ExSummary exSummary = exSummaryDAO.findByWorkCaseId(workCaseId);
        if(exSummary == null){
            exSummary = new ExSummary();
            WorkCase workCase = new WorkCase();
            workCase.setId(workCaseId);
            exSummary.setWorkCase(workCase);
        }

        if(basicInfo.getBorrowerType().getId() == BorrowerType.INDIVIDUAL.value()){ // use bank statement
            BankStatementSummary bankStatementSummary = bankStatementSummaryDAO.findByWorkCaseId(workCaseId);
            BigDecimal grdTotalIncomeGross = BigDecimal.ZERO;
            BigDecimal approxIncome = BigDecimal.ZERO;
            if(bankStatementSummary != null){
                grdTotalIncomeGross = bankStatementSummary.getGrdTotalIncomeGross();
            }
            if(cusListView != null && cusListView.size() > 0){
                for(CustomerInfoView cus : cusListView){
                    if(cus.getRelation().getId() != RelationValue.BORROWER.value()){
                        if(cus.getCustomerEntity().getId() == BorrowerType.INDIVIDUAL.value()){
                            if(cus.getReference() != null && cus.getReference().getGroupIncome() == 1){
                                approxIncome = Util.add(approxIncome,cus.getApproxIncome());
                            }
                        } else if(cus.getCustomerEntity().getId() == BorrowerType.JURISTIC.value()){
                            if(cus.getReference() != null && cus.getReference().getGroupIncome() == 1){
                                approxIncome = Util.add(approxIncome,cus.getSalesFromFinancialStmt());
                            }
                        }
                    }
                }
            }
            groupSaleBDM = Util.add(Util.multiply(grdTotalIncomeGross,twelve),approxIncome);
            groupSaleUW = Util.add(Util.multiply(grdTotalIncomeGross,twelve),approxIncome);
        } else { // use customer
            BigDecimal saleFromFinStmt = BigDecimal.ZERO;
            BigDecimal approxIncome = BigDecimal.ZERO;
            if(cusListView != null && cusListView.size() > 0){
                for(CustomerInfoView cus : cusListView){
                    if(cus.getRelation().getId() != RelationValue.BORROWER.value()){
                        if(cus.getCustomerEntity().getId() == BorrowerType.INDIVIDUAL.value()){
                            if(cus.getReference() != null && cus.getReference().getGroupIncome() == 1){
                                approxIncome = Util.add(approxIncome,cus.getApproxIncome());
                            }
                        }
                    }
                    if(cus.getCustomerEntity().getId() == BorrowerType.JURISTIC.value()){
                        saleFromFinStmt = Util.add(saleFromFinStmt,cus.getSalesFromFinancialStmt());
                    }
                }
            }
            groupSaleBDM = Util.add(saleFromFinStmt,approxIncome);
            groupSaleUW = Util.add(saleFromFinStmt,approxIncome);
        }

        if(user.getRole().getId() == RoleValue.UW.id()){
            exSummary.setGroupSaleUW(groupSaleUW);
        } else {
            exSummary.setGroupSaleUW(groupSaleUW);
            exSummary.setGroupSaleBDM(groupSaleBDM);
        }

        exSummaryDAO.persist(exSummary);
    }

    //Borrower Characteristic - groupExposureBDM , groupExposureUW ( Line 58-59 )
    //Decision
//    groupExposureBDM - Group Total Exposure + Total Propose Credit > Change to Get Total Group Exposure on Propose Page ( Total Group Exposure = Total Group Exposure ( Existing ) + Total Propose Credit )
//    groupExposureUW - Group Total Exposure + Total Approved Credit > Change to Get Total Group Exposure on Decision Page ( Total Group Exposure = Total Group Exposure ( Existing ) + Total Approve Credit )
    public void calGroupExposureBorrowerCharacteristic(long workCaseId){ //TODO: Decision , Credit Facility-Propose , Pls Call me !!
        log.debug("calGroupExposureBorrowerCharacteristic :: workCaseId : {}",workCaseId);
        ProposeLine proposeLine = proposeLineDAO.findByWorkCaseId(workCaseId);
        Decision decision = decisionDAO.findByWorkCaseId(workCaseId);
        BigDecimal groupExposureBDM = null;
        BigDecimal groupExposureUW = null;
        if(!Util.isNull(proposeLine) && !Util.isZero(proposeLine.getId())){
            groupExposureBDM = proposeLine.getTotalExposure();
            if(!Util.isNull(decision) && !Util.isZero(decision.getId())){
                groupExposureUW = decision.getTotalApproveExposure();
            } else {
                groupExposureUW = proposeLine.getTotalExposure();
            }
        }

        ExSummary exSummary = exSummaryDAO.findByWorkCaseId(workCaseId);
        if(exSummary == null){
            exSummary = new ExSummary();
            WorkCase workCase = new WorkCase();
            workCase.setId(workCaseId);
            exSummary.setWorkCase(workCase);
        }

        exSummary.setGroupExposureUW(groupExposureUW);
        exSummary.setGroupExposureBDM(groupExposureBDM);

        exSummaryDAO.persist(exSummary);
    }

    //Borrower Characteristic - yearInBusiness , yearInBusinessMonth ( Line 49-50 )
    //Business Info Summary
//    Max of (วันก่อตั้ง หรือ วันจดทะเบียนพาณิชย์ in businessInfoSummary)
//    calculate months from yearInBusiness fields.
    public void calYearInBusinessBorrowerCharacteristic(long workCaseId){ //TODO: Business Info Summary , Pls Call me !!
        log.debug("calYearInBusinessBorrowerCharacteristic :: workCaseId : {}",workCaseId);
        BizInfoSummaryView bizInfoSummaryView = bizInfoSummaryControl.onGetBizInfoSummaryByWorkCase(workCaseId);
        Date yearInBiz = DateTimeUtil.getMaxOfDate(bizInfoSummaryView.getRegistrationDate(), bizInfoSummaryView.getEstablishDate());
        String year = "";
        int month = 0;
        if(yearInBiz != null){
            year = DateTimeUtil.calYearMonth(yearInBiz);
            month = DateTimeUtil.calMonth(yearInBiz);
        }

        ExSummary exSummary = exSummaryDAO.findByWorkCaseId(workCaseId);
        if(exSummary == null){
            exSummary = new ExSummary();
            WorkCase workCase = new WorkCase();
            workCase.setId(workCaseId);
            exSummary.setWorkCase(workCase);
        }
        exSummary.setYearInBusiness(year);
        exSummary.setYearInBusinessMonth(month);

        exSummaryDAO.persist(exSummary);
    }

    public void calReviewDate(long workCaseId){ // todo:submit button
//        lastReviewDate = Current Date until click submit, display submit date.
//        nextReviewDate = (1st day of approve month + 12 Months)
        ExSummary exSummary = exSummaryDAO.findByWorkCaseId(workCaseId);
        if(exSummary == null){
            exSummary = new ExSummary();
            WorkCase workCase = new WorkCase();
            workCase.setId(workCaseId);
            exSummary.setWorkCase(workCase);
        }
//        exSummary.setLastReviewDate();
//        exSummary.setNextReviewDate(DateTimeUtil.getFirstDayOfMonthDatePlusOneYear(exSummary.getLastReviewDate()));

        exSummaryDAO.persist(exSummary);
    }

    public void calIncomeFactor(long workCaseId){ //TODO: Business Info Summary , Pls Call me !!
        BizInfoSummaryView bizInfoSummaryView = bizInfoSummaryControl.onGetBizInfoSummaryByWorkCase(workCaseId);

        ExSummary exSummary = exSummaryDAO.findByWorkCaseId(workCaseId);
        if(exSummary == null){
            exSummary = new ExSummary();
            WorkCase workCase = new WorkCase();
            workCase.setId(workCaseId);
            exSummary.setWorkCase(workCase);
        }

        User user = getCurrentUser();

        if(user.getRole().getId() == RoleValue.UW.id()){
            exSummary.setIncomeFactorUW(bizInfoSummaryView.getWeightIncomeFactor());
        } else {
            exSummary.setIncomeFactorBDM(bizInfoSummaryView.getWeightIncomeFactor());
        }

        exSummaryDAO.persist(exSummary);
    }

    public void calWC(long workCaseId) {
        log.debug("calWC ::: workCaseId : {}", workCaseId);
        BigDecimal dayOfYear = BigDecimal.valueOf(365);
        BigDecimal monthOfYear = BigDecimal.valueOf(12);
        BigDecimal onePointTwoFive = BigDecimal.valueOf(1.25);
        BigDecimal onePointFive = BigDecimal.valueOf(1.50);
        BigDecimal thirtyFive = BigDecimal.valueOf(35);
        BigDecimal oneHundred = BigDecimal.valueOf(100);
        BigDecimal two = BigDecimal.valueOf(2);

        // ยอดขาย/รายได้
        BigDecimal adjustDBR = BigDecimal.ZERO;
        // วงเงินสินเชื่อหมุนเวียนจากหน้า NCB
        BigDecimal revolvingCreditNCB = BigDecimal.ZERO;
        // วงเงินสินเชื่อหมุนเวียนส่วนผู้เกี่ยวข้องในหน้า DBR
        BigDecimal revolvingCreditDBR = BigDecimal.ZERO;
        // ภาระสินเชื่อประเภทอื่นๆ จากหน้า NCB ที่มี flag W/C = Yes
        BigDecimal loanBurdenWCFlag = BigDecimal.ZERO;
        // วงเงินสินเชื่อหมุนเวียนใน NCB ที่ flag เป็น TMB
        BigDecimal revolvingCreditNCBTMBFlag = BigDecimal.ZERO;
        // ภาระสินเชื่อประเภทอื่น ที่ flag TMB และ flag W/C
        BigDecimal loanBurdenTMBFlag = BigDecimal.ZERO;

        //how to check role and get ap ar inv !?
        BigDecimal weightAP = BigDecimal.ZERO;
        BigDecimal weightAR = BigDecimal.ZERO;
        BigDecimal weightINV = BigDecimal.ZERO;
        // Sum(weight cost of goods sold * businessProportion)
        // cost of goods = business desc ( column COG )
        // business proportion = สัดส่วนธุรกิจ ในแต่ละ business < %Income >
        //CR >>
        //AAAValue = ยอดขาย/รายได้ คูณ weighted Standard Cost of Good Sold
        //ยอดขาย/รายได้ = รายได้ต่อเดือน (adjusted) [DBR] คูณ 12
        BigDecimal aaaValue = BigDecimal.ZERO;

        //table 1
        BigDecimal wcNeed;
        BigDecimal totalWcDebit;
        BigDecimal totalWcTmb;
        BigDecimal wcNeedDiffer;

        //table 2
        BigDecimal case1WcLimit;
        BigDecimal case1WcMinLimit;
        BigDecimal case1Wc50CoreWc;
        BigDecimal case1WcDebitCoreWc;

        //table 3
        BigDecimal case2WcLimit;
        BigDecimal case2WcMinLimit;
        BigDecimal case2Wc50CoreWc;
        BigDecimal case2WcDebitCoreWc;

        //table 4
        BigDecimal case3WcLimit;
        BigDecimal case3WcMinLimit;
        BigDecimal case3Wc50CoreWc;
        BigDecimal case3WcDebitCoreWc;

        ////////////////////////////////////////////////////

        DBRView dbrView = dbrControl.getDBRByWorkCase(workCaseId);
        if (dbrView != null) {
            adjustDBR = dbrView.getMonthlyIncomeAdjust();
            revolvingCreditDBR = dbrView.getTotalMonthDebtRelatedWc();
        }

        List<NCB> ncbList = ncbInfoControl.getNCBByWorkCaseId(workCaseId);
        if (ncbList != null && ncbList.size() > 0) {
            for (NCB ncb : ncbList) {
                revolvingCreditNCB = Util.add(revolvingCreditNCB, ncb.getLoanCreditNCB());
                loanBurdenWCFlag = Util.add(loanBurdenWCFlag, ncb.getLoanCreditWC());
                revolvingCreditNCBTMBFlag = Util.add(revolvingCreditNCBTMBFlag, ncb.getLoanCreditTMB());
                loanBurdenTMBFlag = Util.add(loanBurdenTMBFlag, ncb.getLoanCreditWCTMB());
            }
        }

        //*** ยอดขาย/รายได้  = รายได้ต่อเดือน (adjusted) [DBR] * 12
        BigDecimal salesIncome = Util.multiply(adjustDBR, monthOfYear);

        BizInfoSummaryView bizInfoSummaryView = bizInfoSummaryControl.onGetBizInfoSummaryByWorkCase(workCaseId);
        if (bizInfoSummaryView != null) {
            weightAR = bizInfoSummaryView.getSumWeightAR();
            weightAP = bizInfoSummaryView.getSumWeightAP();
            weightINV = bizInfoSummaryView.getSumWeightINV();
            // Sum(weight cost of goods sold * businessProportion)
            List<BizInfoDetailView> bizInfoDetailViewList = new ArrayList<BizInfoDetailView>();
            if (bizInfoSummaryView.getId() != 0) {
                bizInfoDetailViewList = bizInfoSummaryControl.onGetBizInfoDetailViewByBizInfoSummary(bizInfoSummaryView.getId());
                if (bizInfoDetailViewList != null && bizInfoDetailViewList.size() > 0) {
                    for (BizInfoDetailView bidv : bizInfoDetailViewList) {
                        BigDecimal cog = BigDecimal.ZERO;
                        if (bidv.getBizDesc() != null) {
                            cog = bidv.getBizDesc().getCog();
                        }
                        aaaValue = Util.add(aaaValue, Util.multiply(cog, salesIncome));
                    }
                }
            }
        }

        //calculation
        //(ยอดขาย/รายได้ หาร 365 คูณ Weighted AR) + (AAAValue หาร 365 คูณ Weighted INV) - ((AAAValue หาร 365 คูณ Weighted AP)
        wcNeed = Util.subtract((Util.add(Util.multiply(Util.divide(salesIncome, dayOfYear), weightAR), Util.multiply(Util.divide(aaaValue, dayOfYear), weightINV))), (Util.multiply(Util.divide(aaaValue, dayOfYear), weightAP)));
        //Sum (วงเงินสินเชื่อหมุนเวียนจากหน้า NCB และ ส่วนผู้เกี่ยวข้องในหน้า DBR + ภาระสินเชื่อประเภทอื่นๆ จากหน้า NCB ที่มี flag W/C = Yes )
        totalWcDebit = Util.add(Util.add(revolvingCreditNCB, revolvingCreditDBR), loanBurdenWCFlag);
        //วงเงินสินเชื่อหมุนเวียนใน NCB ที่ flag เป็น TMB + ภาระสินเชื่อประเภทอื่น ที่ flag TMB และ flag W/C
        totalWcTmb = Util.add(revolvingCreditNCBTMBFlag, loanBurdenTMBFlag);
        //wcNeed - totalWcDebit
        wcNeedDiffer = Util.subtract(wcNeed, totalWcDebit);

        log.debug("Value ::: wcNeed : {}, totalWcDebit : {}, totalWcTmb : {}, wcNeedDiffer : {}", wcNeed, totalWcDebit, totalWcTmb, wcNeedDiffer);


        //1.25 x wcNeed
        case1WcLimit = Util.multiply(wcNeed, onePointTwoFive);
        //case1WcLimit - totalWcDebit
        case1WcMinLimit = Util.subtract(case1WcLimit, totalWcDebit);
        //ไม่เกิน 50% ของ case1WcLimit และไม่เกิน case1WcMinLimit แล้วแต่ตัวไหนจะต่ำกว่า
        case1Wc50CoreWc = Util.compareToFindLower(Util.divide(case1WcLimit, two), case1WcMinLimit);
        //case1WcMinLimit - case1Wc50CoreWc
        case1WcDebitCoreWc = Util.subtract(case1WcMinLimit, case1Wc50CoreWc);

        log.debug("Value ::: case1WcLimit : {}, case1WcMinLimit : {}, case1Wc50CoreWc : {}, case1WcDebitCoreWc : {}", case1WcLimit, case1WcMinLimit, case1Wc50CoreWc, case1WcDebitCoreWc);

        //1.5 x wcNeed
        case2WcLimit = Util.multiply(wcNeed, onePointFive);
        //case2WcLimit - totalWcDebit
        case2WcMinLimit = Util.subtract(case2WcLimit, totalWcDebit);
        //ไม่เกิน 50% ของ case2WcLimit และไม่เกิน case2WcMinLimit แล้วแต่ตัวไหนจะต่ำกว่า
        case2Wc50CoreWc = Util.compareToFindLower(Util.divide(case2WcLimit, two), case2WcMinLimit);
        //case2WcMinLimit - case2Wc50CoreWc
        case2WcDebitCoreWc = Util.subtract(case2WcMinLimit, case2Wc50CoreWc);

        log.debug("Value ::: case2WcLimit : {}, case2WcMinLimit : {}, case2Wc50CoreWc : {}, case2WcDebitCoreWc : {}", case2WcLimit, case2WcMinLimit, case2Wc50CoreWc, case2WcDebitCoreWc);

        //ยอดขาย/รายได้ หาร 12 คูณ 35% -> Change To : ยอดขาย/รายได้ คูณ 35%
        case3WcLimit = Util.divide(Util.multiply(salesIncome, thirtyFive), oneHundred);
        //case3WcLimit - totalWcDebit
        case3WcMinLimit = Util.subtract(case2WcLimit, totalWcDebit);
        //ไม่เกิน 50% ของ case3WcLimit และไม่เกิน case3WcMinLimit แล้วแต่ตัวไหนจะต่ำกว่า
        case3Wc50CoreWc = Util.compareToFindLower(Util.divide(case3WcLimit, two), case3WcMinLimit);
        //case3WcMinLimit - case3Wc50CoreWc
        case3WcDebitCoreWc = Util.subtract(case3WcMinLimit, case3Wc50CoreWc);

        log.debug("Value ::: case3WcLimit : {}, case3WcMinLimit : {}, case3Wc50CoreWc : {}, case3WcDebitCoreWc : {}", case3WcLimit, case3WcMinLimit, case3Wc50CoreWc, case3WcDebitCoreWc);

        ProposeLine proposeLine = proposeLineDAO.findByWorkCaseId(workCaseId);
        if(proposeLine == null){
            proposeLine = new ProposeLine();
            WorkCase workCase = new WorkCase();
            workCase.setId(workCaseId);
            proposeLine.setWorkCase(workCase);
        }

        proposeLine.setWcNeed(wcNeed);
        proposeLine.setTotalWCDebit(totalWcDebit);
        proposeLine.setTotalWCTmb(totalWcTmb);
        proposeLine.setWcNeedDiffer(wcNeedDiffer);
        proposeLine.setCase1WCLimit(case1WcLimit);
        proposeLine.setCase1WCMinLimit(case1WcMinLimit);
        proposeLine.setCase1WC50CoreWC(case1Wc50CoreWc);
        proposeLine.setCase1WCDebitCoreWC(case1WcDebitCoreWc);
        proposeLine.setCase2WCLimit(case2WcLimit);
        proposeLine.setCase2WCMinLimit(case2WcMinLimit);
        proposeLine.setCase2WC50CoreWC(case2Wc50CoreWc);
        proposeLine.setCase2WCDebitCoreWC(case2WcDebitCoreWc);
        proposeLine.setCase3WCLimit(case3WcLimit);
        proposeLine.setCase3WCMinLimit(case3WcMinLimit);
        proposeLine.setCase3WC50CoreWC(case3Wc50CoreWc);
        proposeLine.setCase3WCDebitCoreWC(case3WcDebitCoreWc);
        proposeLineDAO.persist(proposeLine);

        //Update for cal new WC to cal another method
        calForWC(workCaseId);
    }

    public void calculateBOTClass(long workCaseId){
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

        String botClassReason = "";
        String botClass = "";
        ExSummary exSummary = exSummaryDAO.findByWorkCaseId(workCaseId);
        if(Util.isNull(exSummary)){
            exSummary = new ExSummary();
        }

        if(basicInfo != null && basicInfo.getExistingSMECustomer() == RadioValue.NO.value()){ //new customer
            if(qualitativeView != null && qualitativeView.getId() != 0){
                botClassReason = qualitativeView.getQualityLevel() != null ? qualitativeView.getQualityLevel().getDescription() : "";
                botClass = qualitativeView.getQualityResult() != null && !qualitativeView.getQualityResult().trim().equalsIgnoreCase("") ? qualitativeView.getQualityResult() : "";
            }
        } else { // (Bot Class = P,SM,SS,D,DL) DL is the worst.
            String tmpWorstCase = "";
            String worstCase = "";
            List<Customer> customerList = customerDAO.findBorrowerByWorkCaseId(workCaseId);
            if(customerList != null && customerList.size() > 0){
                for(Customer customer : customerList){
                    tmpWorstCase = exSummaryControl.calWorstCaseBotClass(tmpWorstCase, customer.getCustomerOblInfo() != null ? customer.getCustomerOblInfo().getAdjustClass() : "");
                }
            }

            if(qualitativeView != null && qualitativeView.getId() != 0){
                botClassReason = qualitativeView.getQualityLevel() != null ? qualitativeView.getQualityLevel().getDescription() : "";
                if(qualitativeView.getQualityResult() != null && !qualitativeView.getQualityResult().trim().equalsIgnoreCase("")){
                    botClass = exSummaryControl.calWorstCaseBotClass(tmpWorstCase, qualitativeView.getQualityResult());
                }
            }
        }
        exSummary.setCreditRiskReason(botClassReason);
        exSummary.setCreditRiskBOTClass(botClass);
        exSummaryDAO.persist(exSummary);
    }

    public void calculateFinalDBR(long workCaseId){
        DBR dbr = dbrDAO.findByWorkCaseId(workCaseId);
        if(Util.isNull(dbr)){
            dbr = new DBR();
        }

        BigDecimal totalMonthDebtBorrowerFinal = dbr.getTotalMonthDebtBorrowerFinal();
        BigDecimal totalMonthDebtRelated = dbr.getTotalMonthDebtRelated();
        BigDecimal netMonthlyIncome = dbr.getNetMonthlyIncome();

        ProposeLine proposeLine = proposeLineDAO.findByWorkCaseId(workCaseId);
        BigDecimal totalProposeLoanDBR = BigDecimal.ZERO;
        if(!Util.isNull(proposeLine)) {
            totalProposeLoanDBR = proposeLine.getTotalProposeLoanDBR();
        }

        dbr.setFinalDBR(Util.multiply(Util.divide(Util.add(Util.add(totalMonthDebtBorrowerFinal, totalMonthDebtRelated),totalProposeLoanDBR), netMonthlyIncome), BigDecimal.valueOf(100)));

        dbrDAO.persist(dbr);
    }

    public void calculateTotalProposeAmount(long workCaseId) {
        log.debug("calculateTotalProposeAmount :: workCaseId :: {}", workCaseId);
        ProposeLine proposeLine = proposeLineDAO.findByWorkCaseId(workCaseId);
        User user = getCurrentUser();
        log.debug("calculateTotalProposeAmount :: user :: {}", user);
        if (!Util.isNull(proposeLine)) {
            log.debug("calculateTotalProposeAmount :: proposeLine ID :: {}", proposeLine.getId());
            BigDecimal sumTotalOBOD = BigDecimal.ZERO;         // OBOD of Propose
            BigDecimal sumTotalCommercial = BigDecimal.ZERO;   // Commercial of Propose
            BigDecimal sumTotalPropose = BigDecimal.ZERO;      // All Propose
            BigDecimal sumTotalBorrowerCommercial = BigDecimal.ZERO;   // Without : OBOD  Propose and Existing
            BigDecimal sumTotalBorrowerCommercialAndOBOD = BigDecimal.ZERO;  //All Propose and Existing
            BigDecimal sumTotalGroupExposure = BigDecimal.ZERO;
            BigDecimal sumTotalLoanDbr = BigDecimal.ZERO;
            BigDecimal sumTotalApproveLoanDbr = BigDecimal.ZERO;
            BigDecimal sumTotalNonLoanDbr = BigDecimal.ZERO;
            BigDecimal borrowerComOBOD = BigDecimal.ZERO;
            BigDecimal borrowerCom = BigDecimal.ZERO;
            BigDecimal groupExposure = BigDecimal.ZERO;
            BigDecimal sumTotalNewCredit = BigDecimal.ZERO;

            ExistingCreditFacility existingCreditFacility = existingCreditFacilityDAO.findByWorkCaseId(workCaseId);
            if (!Util.isNull(existingCreditFacility)) {
                log.debug("calculateTotalProposeAmount :: existingCreditFacility ID :: {}", existingCreditFacility.getId());
                borrowerComOBOD = existingCreditFacility.getTotalBorrowerComOBOD();
                borrowerCom = existingCreditFacility.getTotalBorrowerCom();
                groupExposure = existingCreditFacility.getTotalGroupExposure();
            }

            BasicInfo basicInfo = basicInfoDAO.findByWorkCaseId(workCaseId);
            TCG tcg = tcgDAO.findByWorkCaseId(workCaseId);

            if(!Util.isNull(basicInfo) && !Util.isNull(tcg)) {
                log.debug("calculateTotalProposeAmount :: basicInfo ID :: {}", basicInfo.getId());
                log.debug("calculateTotalProposeAmount :: tcg ID :: {}", tcg.getId());
                int tcgFlag = tcg != null ? tcg.getTcgFlag() : 0;
                int specialProgramId = 0;
                if(basicInfo.getApplySpecialProgram() == 1) {
                    if(!Util.isNull(basicInfo.getSpecialProgram()) && !Util.isZero(basicInfo.getSpecialProgram().getId())) {
                        specialProgramId = basicInfo.getSpecialProgram().getId();
                    }
                }
                log.debug("calculateTotalProposeAmount :: tcgFlag :: {}", tcgFlag);
                log.debug("calculateTotalProposeAmount :: specialProgramId :: {}", specialProgramId);
                List<ProposeCreditInfo> proposeCreditInfoList = proposeLine.getProposeCreditInfoList();
                if (!Util.isNull(proposeCreditInfoList) && !Util.isZero(proposeCreditInfoList.size())) {
                    log.debug("calculateTotalProposeAmount :: proposeCreditInfoList Size :: {}", proposeCreditInfoList.size());
                    DBR dbr = dbrDAO.findByWorkCaseId(workCaseId);
                    for (ProposeCreditInfo creditInfo : proposeCreditInfoList) {
                        if (!Util.isNull(creditInfo) && !Util.isNull(creditInfo.getProductProgram()) && !Util.isZero(creditInfo.getProductProgram().getId()) && !Util.isNull(creditInfo.getCreditType()) && !Util.isZero(creditInfo.getCreditType().getId())) {
                            PrdProgramToCreditType prdProgramToCreditType = prdProgramToCreditTypeDAO.getPrdProgramToCreditType(creditInfo.getCreditType(), creditInfo.getProductProgram());
                            if(!Util.isNull(prdProgramToCreditType)) {
                                int marketTableFlag = RadioValue.NO.value();
                                if(!Util.isNull(dbr)) {
                                    log.debug("calculateTotalProposeAmount :: dbr ID :: {}", dbr.getId());
                                    log.debug("calculateTotalProposeAmount :: marketTableFlag :: {}", dbr.getMarketableFlag());
                                    marketTableFlag = dbr.getMarketableFlag();
                                }

                                ProductFormula productFormula;
                                if(creditInfo.getCreditType().getCreditGroup() == CreditTypeGroup.OD.value()) {
                                    log.debug("calculateTotalProposeAmount :: Credit Group == OD");
                                    productFormula = productFormulaDAO.findProductFormulaPropose(prdProgramToCreditType, proposeLine.getCreditCustomerType(), specialProgramId, tcgFlag, marketTableFlag);
                                } else {
                                    log.debug("calculateTotalProposeAmount :: Credit Group != OD ");
                                    productFormula = productFormulaDAO.findProductFormulaPropose(prdProgramToCreditType, proposeLine.getCreditCustomerType(), specialProgramId, tcgFlag);
                                }
                                if (!Util.isNull(productFormula)) {
                                    if(creditInfo.getProposeType() == ProposeType.A) {
                                        //For DBR  sumTotalLoanDbr and sumTotalNonLoanDbr
                                        if(creditInfo.getRequestType() == RequestTypes.NEW.value() && creditInfo.getUwDecision() == DecisionType.APPROVED) {
                                            if (productFormula.getDbrCalculate() == 2) {// Yes
                                                if (productFormula.getDbrMethod() == DBRMethod.NOT_CALCULATE.value()) {// not calculate
                                                    sumTotalApproveLoanDbr = sumTotalApproveLoanDbr.add(BigDecimal.ZERO);
                                                } else if (productFormula.getDbrMethod() == DBRMethod.INSTALLMENT.value()) { //Installment
                                                    sumTotalApproveLoanDbr = sumTotalApproveLoanDbr.add(creditInfo.getInstallment());
                                                } else if (productFormula.getDbrMethod() == DBRMethod.INT_YEAR.value()) { //(Limit*((?????????????+ Spread)/100))/12
                                                    sumTotalApproveLoanDbr = sumTotalApproveLoanDbr.add(calTotalProposeLoanDBRForIntYear(creditInfo, productFormula.getDbrSpread()));
                                                }
                                            }
                                        }
                                    } else {
                                        if (CreditTypeGroup.CASH_IN.value() == (productFormula.getProgramToCreditType().getCreditType().getCreditGroup())) { //OBOD or CASH_IN
                                            //ExposureMethod for check to use limit or limit*PCE%
                                            if (productFormula.getExposureMethod() == ExposureMethod.NOT_CALCULATE.value()) { //ไม่คำนวณ
                                                sumTotalOBOD = sumTotalOBOD.add(BigDecimal.ZERO);
                                            } else if (productFormula.getExposureMethod() == ExposureMethod.LIMIT.value()) { //limit
                                                sumTotalOBOD = sumTotalOBOD.add(creditInfo.getLimit());
                                            } else if (productFormula.getExposureMethod() == ExposureMethod.PCE_LIMIT.value()) { //(limit * %PCE)/100
                                                sumTotalOBOD = sumTotalOBOD.add(Util.divide(Util.multiply(creditInfo.getLimit(),creditInfo.getPcePercent()),100));
                                            }
                                        } else {
                                            //ExposureMethod for check to use limit or limit*PCE%
                                            if (productFormula.getExposureMethod() == ExposureMethod.NOT_CALCULATE.value()) { //ไม่คำนวณ
                                                sumTotalCommercial = sumTotalCommercial.add(BigDecimal.ZERO);
                                            } else if (productFormula.getExposureMethod() == ExposureMethod.LIMIT.value()) { //limit
                                                sumTotalCommercial = sumTotalCommercial.add(creditInfo.getLimit());
                                            } else if (productFormula.getExposureMethod() == ExposureMethod.PCE_LIMIT.value()) {    //(limit * %PCE)/100
                                                sumTotalCommercial = sumTotalCommercial.add(Util.divide(Util.multiply(creditInfo.getLimit(),creditInfo.getPcePercent()),100));
                                            }
                                        }
                                        sumTotalPropose = Util.add(sumTotalCommercial, sumTotalOBOD);// Commercial + OBOD  All Credit

                                        if(creditInfo.getRequestType() == RequestTypes.NEW.value()) {
                                            if (productFormula.getDbrCalculate() == 1) {// No
                                                sumTotalNonLoanDbr = BigDecimal.ZERO;
                                            } else if (productFormula.getDbrCalculate() == 2) {// Yes
                                                if (productFormula.getDbrMethod() == DBRMethod.NOT_CALCULATE.value()) {// not calculate
                                                    sumTotalLoanDbr = sumTotalLoanDbr.add(BigDecimal.ZERO);
                                                } else if (productFormula.getDbrMethod() == DBRMethod.INSTALLMENT.value()) { //Installment
                                                    sumTotalLoanDbr = sumTotalLoanDbr.add(creditInfo.getInstallment());
                                                } else if (productFormula.getDbrMethod() == DBRMethod.INT_YEAR.value()) { //(Limit*((?????????????+ Spread)/100))/12
                                                    sumTotalLoanDbr = sumTotalLoanDbr.add(calTotalProposeLoanDBRForIntYear(creditInfo, productFormula.getDbrSpread()));
                                                }
                                            }

                                            if (productFormula.getExposureMethod() == ExposureMethod.NOT_CALCULATE.value()) { //ไม่คำนวณ
                                                sumTotalNewCredit = sumTotalNewCredit.add(BigDecimal.ZERO);
                                            } else if (productFormula.getExposureMethod() == ExposureMethod.LIMIT.value()) { //limit
                                                sumTotalNewCredit = sumTotalNewCredit.add(creditInfo.getLimit());
                                            } else if (productFormula.getExposureMethod() == ExposureMethod.PCE_LIMIT.value()) {    //(limit * %PCE)/100
                                                sumTotalNewCredit = sumTotalNewCredit.add(Util.divide(Util.multiply(creditInfo.getLimit(),creditInfo.getPcePercent()),100));
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    sumTotalBorrowerCommercialAndOBOD = Util.add(borrowerComOBOD, sumTotalPropose); // Total Commercial&OBOD  ของ Borrower (จาก Existing credit) +Total Propose Credit
                    sumTotalBorrowerCommercial = Util.add(borrowerCom, sumTotalCommercial); //Total Commercial  ของ Borrower (จาก Existing credit) + *Commercial ของ propose
                    sumTotalGroupExposure = Util.add(groupExposure, sumTotalPropose); //ให้เปลี่ยนเป็น Total Exposure ของ Group  (จาก Existing credit) +  Total Propose Credit
                }
            }

            log.debug("calculateTotalProposeAmount :: Total Propose :: {}", sumTotalPropose);
            log.debug("calculateTotalProposeAmount :: Total Propose Loan DBR ( Propose ) :: {}", sumTotalLoanDbr);
            log.debug("calculateTotalProposeAmount :: Total Propose Loan DBR ( Approve ) :: {}", sumTotalApproveLoanDbr);
            log.debug("calculateTotalProposeAmount :: Total Propose Non Loan DBR :: {}", sumTotalNonLoanDbr);
            log.debug("calculateTotalProposeAmount :: Total Commercial :: {}", sumTotalBorrowerCommercial);
            log.debug("calculateTotalProposeAmount :: Total Commercial And OBOD :: {}", sumTotalBorrowerCommercialAndOBOD);
            log.debug("calculateTotalProposeAmount :: Total Exposure :: {}", sumTotalGroupExposure);
            log.debug("calculateTotalProposeAmount :: Total New Credit :: {}", sumTotalNewCredit);

            if(!Util.isNull(user) && !Util.isNull(user.getRole()) && user.getRole().getId() == RoleValue.UW.id()) { // If UW Save will update loan dbr
                proposeLine.setTotalProposeLoanDBR(sumTotalApproveLoanDbr);                 //sumTotalLoanDbr
                proposeLine.setTotalPropose(sumTotalPropose);                               //sumTotalPropose All Credit in this case
                proposeLine.setTotalProposeNonLoanDBR(sumTotalNonLoanDbr);                  //sumTotalNonLoanDbr
                proposeLine.setTotalCommercial(sumTotalBorrowerCommercial);                 //sum Commercial of Existing and Propose
                proposeLine.setTotalCommercialAndOBOD(sumTotalBorrowerCommercialAndOBOD);   //sum Commercial and OBOD of Existing and Propose
                proposeLine.setTotalExposure(sumTotalGroupExposure);                        //sumTotalGroupExposure
                proposeLine.setTotalNewCredit(sumTotalNewCredit);                           //sumTotalNewCredit
            } else {
                proposeLine.setTotalProposeLoanDBR(sumTotalLoanDbr);                        //sumTotalLoanDbr
                proposeLine.setTotalPropose(sumTotalPropose);                               //sumTotalPropose All Credit in this case
                proposeLine.setTotalProposeNonLoanDBR(sumTotalNonLoanDbr);                  //sumTotalNonLoanDbr
                proposeLine.setTotalCommercial(sumTotalBorrowerCommercial);                 //sum Commercial of Existing and Propose
                proposeLine.setTotalCommercialAndOBOD(sumTotalBorrowerCommercialAndOBOD);   //sum Commercial and OBOD of Existing and Propose
                proposeLine.setTotalExposure(sumTotalGroupExposure);                        //sumTotalGroupExposure
                proposeLine.setTotalNewCredit(sumTotalNewCredit);                           //sumTotalNewCredit
            }
            proposeLineDAO.persist(proposeLine);
        }
    }

    public BigDecimal calTotalProposeLoanDBRForIntYear(ProposeCreditInfo proposeCreditInfo, BigDecimal dbrSpread) {
        BigDecimal sumTotalLoanDbr = BigDecimal.ZERO;
        if (!Util.isNull(proposeCreditInfo) && !Util.isNull(proposeCreditInfo.getProposeCreditInfoTierDetailList()) && !Util.isZero(proposeCreditInfo.getProposeCreditInfoTierDetailList().size())) {
            BigDecimal oneHundred = BigDecimal.valueOf(100);
            BigDecimal twelve = BigDecimal.valueOf(12);
            for (ProposeCreditInfoTierDetail proposeCreditInfoTierDetail : proposeCreditInfo.getProposeCreditInfoTierDetailList()) { //(Limit*((อัตราดอกเบี้ย+ Spread)/100))/12
                if (!Util.isNull(proposeCreditInfoTierDetail)) {
                    sumTotalLoanDbr = Util.add(sumTotalLoanDbr, Util.divide(Util.multiply(Util.divide(Util.add(Util.add(proposeCreditInfoTierDetail.getFinalBasePrice().getValue(), proposeCreditInfoTierDetail.getFinalInterest()), dbrSpread), oneHundred), proposeCreditInfo.getLimit()), twelve));
                }
            }
        }
        return sumTotalLoanDbr;
    }

    public void calculateMaximumSMELimit(long workCaseId) {
        BigDecimal maximumSMELimit = BigDecimal.ZERO;
        ProposeLine proposeLine = proposeLineDAO.findByWorkCaseId(workCaseId);
        TCG tcg = tcgDAO.findByWorkCaseId(workCaseId);
        BasicInfo basicInfo = basicInfoDAO.findByWorkCaseId(workCaseId);
        WorkCase workCase = workCaseDAO.findById(workCaseId);
        ExistingCreditFacility existingCreditFacility = existingCreditFacilityDAO.findByWorkCaseId(workCaseId);
        if (!Util.isNull(proposeLine)) {
            if(!Util.isNull(existingCreditFacility)) {
                proposeLine.setExistingSMELimit(existingCreditFacility.getTotalGroupComOBOD());
            } else {
                proposeLine.setExistingSMELimit(BigDecimal.ZERO);
            }
            // ***** Collateral ***** //
            BigDecimal thirtyPercent = BigDecimal.valueOf(0.30);
            BigDecimal fiftyPercent = BigDecimal.valueOf(0.50);

            BigDecimal num1 = BigDecimal.valueOf(10000000);  //10,000,000
            BigDecimal num2 = BigDecimal.valueOf(23000000);  //23,000,000
            BigDecimal num3 = BigDecimal.valueOf(3000000);   //3,000,000
            BigDecimal num4 = BigDecimal.valueOf(20000000); //20,000,000

            BigDecimal summaryOne = BigDecimal.ZERO;
            BigDecimal summaryTwo;
            BigDecimal potentialCollValue = BigDecimal.ZERO;

            BankStatementSummary bankStatementSummary = bankStatementSummaryDAO.findByWorkCaseId(workCaseId);

            if (!Util.isNull(workCase) && !Util.isNull(workCase.getProductGroup()) && !Util.isNull(bankStatementSummary) && !Util.isNull(tcg) && !Util.isNull(basicInfo)) {
                ProductGroup productGroup = workCase.getProductGroup();
                //********************************************** TCG is YES****************************************//
                if (tcg.getTcgFlag() == RadioValue.YES.value()) {
                    //********** ProductGroup เป็น TMB_SME_SMART_BIZ หรือ RETENTION*********//
                    if ((ProductGroupValue.TMB_SME_SMART_BIZ.id() == productGroup.getId()) || (ProductGroupValue.RETENTION.id() == productGroup.getId())) {
                        List<ProposeCollateralInfo> proposeCollateralInfoList = proposeLine.getProposeCollateralInfoList();
                        if (!Util.isNull(proposeCollateralInfoList) && !Util.isZero(proposeCollateralInfoList.size())) {
                            for (ProposeCollateralInfo collateral : proposeCollateralInfoList) {
                                if(collateral.getProposeType() == ProposeType.P) {
                                    List<ProposeCollateralInfoHead> collHeadList = collateral.getProposeCollateralInfoHeadList();
                                    if (!Util.isNull(collHeadList) && !Util.isZero(collHeadList.size())) {
                                        for (ProposeCollateralInfoHead collHead : collHeadList) {
                                            PotentialCollateral potentialCollateral = collHead.getPotentialCollateral();
                                         /* Sum of [(HeadCollateral-Appraisal of coreAsset/30%)-ภาระสินเชื่อเดิม(collHeadView.getExistingCredit())] +
                                            Sum of [(HeadCollateral-Appraisal of nonCoreAsset/50%)-ภาระสินเชื่อเดิม(collHeadView.getExistingCredit())] +
                                            Sum of [(HeadCollateral-Appraisal of cashCollateral/BE(คือฟิลล์ไหน ???)/30%)-ภาระสินเชื่อเดิม(collHeadView.getExistingCredit())] */
                                            if (!Util.isNull(potentialCollateral) && !Util.isZero(potentialCollateral.getId())) {
                                                if (PotentialCollateralValue.CORE_ASSET.id() == potentialCollateral.getId()) {
                                                    potentialCollValue = Util.subtract((Util.divide(collHead.getAppraisalValue(), thirtyPercent)), collHead.getExistingCredit());
                                                } else if (PotentialCollateralValue.NONE_CORE_ASSET.id() == potentialCollateral.getId()) {
                                                    potentialCollValue = Util.subtract((Util.divide(collHead.getAppraisalValue(), fiftyPercent)), collHead.getExistingCredit());
                                                } else if (PotentialCollateralValue.CASH_COLLATERAL.id() == potentialCollateral.getId()) {
                                                    potentialCollValue = Util.subtract((Util.divide(collHead.getAppraisalValue(), thirtyPercent)), collHead.getExistingCredit());
                                                }
                                            }
                                            summaryOne = summaryOne.add(potentialCollValue);
                                        }
                                    }
                                }
                            }
                        }

                        summaryTwo = calSum2ForCompareSum1(proposeLine, bankStatementSummary, basicInfo);
                        //เอาผลลัพธ์ที่น้อยกว่าเสมอ
                        if (summaryOne.doubleValue() < summaryTwo.doubleValue()) {
                            maximumSMELimit = summaryOne;
                        } else {
                            maximumSMELimit = summaryTwo;
                        }
                        //********** ProductGroup เป็น F_CASH *********//
                    } else if (ProductGroupValue.F_CASH.id() == productGroup.getId()) {
                        //Sum of [(HeadCollateral-Appraisal of cashCollateral/BE(คือฟิลล์ไหน ???))-ภาระสินเชื่อเดิม(collHeadView.getExistingCredit())]
                        List<ProposeCollateralInfo> proposeCollateralInfoList = proposeLine.getProposeCollateralInfoList();
                        if (!Util.isNull(proposeCollateralInfoList) && !Util.isZero(proposeCollateralInfoList.size())) {
                            for (ProposeCollateralInfo collateral : proposeCollateralInfoList) {
                                if(collateral.getProposeType() == ProposeType.P) {
                                    List<ProposeCollateralInfoHead> collHeadList = collateral.getProposeCollateralInfoHeadList();
                                    if (!Util.isNull(collHeadList) && !Util.isZero(collHeadList.size())) {
                                        for (ProposeCollateralInfoHead collHead : collHeadList) {
                                            PotentialCollateral potentialCollateral = collHead.getPotentialCollateral();
                                            if (!Util.isNull(potentialCollateral) && !Util.isZero(potentialCollateral.getId())) {
                                                if (PotentialCollateralValue.CASH_COLLATERAL.id() == potentialCollateral.getId()) {
                                                    potentialCollValue = Util.subtract(collHead.getAppraisalValue(), collHead.getExistingCredit());
                                                }
                                            }
                                            summaryOne = summaryOne.add(potentialCollValue);   // Sum of [Head Coll - Appraisal of Cash Collateral / BE - ภาระสินเชื่อเดิม]
                                        }
                                    }
                                }
                            }
                        }

                        //20,000,000 - วงเงิน/ภาระสินเชื่อ SME เดิม (รวมกลุ่ม/กิจการในเครือ)
                        summaryTwo = Util.subtract(num4, proposeLine.getExistingSMELimit());
                        //เอาผลลัพธ์ที่น้อยกว่าเสมอ
                        if(summaryOne.compareTo(summaryTwo) < 0){
                            maximumSMELimit = summaryOne;
                        } else {
                            maximumSMELimit = summaryTwo;
                        }

                    } else if (ProductGroupValue.OD_NO_ASSET.id() == productGroup.getId()) {//********** ProductGroup เป็น OD_NO_ASSET *********//
                        maximumSMELimit = Util.subtract(num1, proposeLine.getExistingSMELimit()); // 10 ล้าน - วงเงิน/ภาระสินเชื่อ SME เดิม (รวกลุ่ม/กิจการในเครือ)
                    } else if (ProductGroupValue.QUICK_LOAN.id() == productGroup.getId()) {   //********** ProductGroup เป็น QUICK_LOAN *********//
                        summaryOne = num3;  // 3 ล้าน

                        if (proposeLine.getExistingSMELimit().compareTo(num4) < 0) {  // if วงเงิน/ภาระสินเชื่อ SME เดิม (รวกลุ่ม/กิจการในเครือ) น้อยกว่า 20 ล้าน
                            summaryTwo = Util.subtract(num2, proposeLine.getExistingSMELimit()); // 23 ล้าน - วงเงิน/ภาระสินเชื่อ SME เดิม (รวกลุ่ม/กิจการในเครือ)
                        } else {
                            summaryTwo = BigDecimal.ZERO;
                        }

                        //เอาผลลัพธ์ที่น้อยกว่าเสมอ
                        if (summaryOne.compareTo(summaryTwo) < 0) {
                            maximumSMELimit = summaryOne;
                        } else {
                            maximumSMELimit = summaryTwo;
                        }

                    }
                    //********************************************** TCG is NO ****************************************//
                } else if (tcg.getTcgFlag() == RadioValue.NO.value()) {
                    //********** ProductGroup เป็น TMB_SME_SMART_BIZ หรือ RETENTION หรือ F_CASH*********//
                    if ((ProductGroupValue.TMB_SME_SMART_BIZ.id() == productGroup.getId()) ||
                            (ProductGroupValue.RETENTION.id() == productGroup.getId()) ||
                            (ProductGroupValue.F_CASH.id() == productGroup.getId())) {

                        // sum of[(ราคาประเมิน (collHeadView.getAppraisalValue())*LTVPercent) - ภาระสินเชื่อเดิม]
                        List<ProposeCollateralInfo> proposeCollateralInfoList = proposeLine.getProposeCollateralInfoList();
                        if (!Util.isNull(proposeCollateralInfoList) && !Util.isZero(proposeCollateralInfoList.size())) {
                            for (ProposeCollateralInfo collateral : proposeCollateralInfoList) {
                                if(collateral.getProposeType() == ProposeType.P) {
                                    List<ProposeCollateralInfoHead> collHeadList = collateral.getProposeCollateralInfoHeadList();
                                    if (!Util.isNull(collHeadList) && !Util.isZero(collHeadList.size())) {
                                        BigDecimal ltvValue;
                                        BigDecimal percentLTV;
                                        for (ProposeCollateralInfoHead collHead : collHeadList) {
                                            percentLTV = findLTVPercent(collHead, basicInfo, workCase);
                                            ltvValue = Util.multiply(collHead.getAppraisalValue(), percentLTV);
                                            summaryOne = Util.add(summaryOne, (Util.subtract(ltvValue, collHead.getExistingCredit())));
                                        }
                                    }
                                }
                            }
                        }

                        summaryTwo = calSum2ForCompareSum1(proposeLine, bankStatementSummary, basicInfo);

                        //เอาผลลัพธ์ที่น้อยกว่าเสมอ
                        if (summaryOne.doubleValue() < summaryTwo.doubleValue()) {
                            maximumSMELimit = summaryOne;
                        } else {
                            maximumSMELimit = summaryTwo;
                        }

                    } else if (ProductGroupValue.OD_NO_ASSET.id() == productGroup.getId()) {   //********** ProductGroup เป็น OD_NO_ASSET *********//
                        maximumSMELimit = Util.subtract(num1, proposeLine.getExistingSMELimit()); // 10 ล้าน - วงเงิน/ภาระสินเชื่อ SME เดิม (รวกลุ่ม/กิจการในเครือ)
                    } else if (ProductGroupValue.QUICK_LOAN.id() == productGroup.getId()) {   //********** ProductGroup เป็น QUICK_LOAN *********//
                        summaryOne = num3;  // 3 ล้าน

                        if (proposeLine.getExistingSMELimit().compareTo(num4) < 0) {  // if วงเงิน/ภาระสินเชื่อ SME เดิม (รวกลุ่ม/กิจการในเครือ) น้อยกว่า 20 ล้าน
                            summaryTwo = Util.subtract(num2, proposeLine.getExistingSMELimit()); // 23 ล้าน - วงเงิน/ภาระสินเชื่อ SME เดิม (รวกลุ่ม/กิจการในเครือ)
                        } else {
                            summaryTwo = BigDecimal.ZERO;
                        }

                        //เอาผลลัพธ์ที่น้อยกว่าเสมอ
                        if (summaryOne.doubleValue() < summaryTwo.doubleValue()) {
                            maximumSMELimit = summaryOne;
                        } else {
                            maximumSMELimit = summaryTwo;
                        }
                    }
                }
            }

            if (maximumSMELimit.compareTo(BigDecimal.ZERO) < 0) {
                maximumSMELimit = BigDecimal.ZERO;
            }

            proposeLine.setMaximumSMELimit(maximumSMELimit);

            proposeLineDAO.persist(proposeLine);
        }
    }

    public BigDecimal calSum2ForCompareSum1(ProposeLine proposeLine, BankStatementSummary bankStatementSummary, BasicInfo basicInfo) {
        BigDecimal num1 = BigDecimal.valueOf(20000000);      //20,000,000
        BigDecimal num2 = BigDecimal.valueOf(35000000);      //35,000,000
        BigDecimal numBank = BigDecimal.valueOf(100000000);  //100,000,000
        BigDecimal sumBank = BigDecimal.ZERO;
        BigDecimal summary = BigDecimal.ZERO;
        boolean flagForCoreAsset = false;
        /*
        1. Customer Type = Individual
        2. มี Core Asset ใน Proposed หรือ Approved Collateral
        3. SCF <= 13
        4. [Sum of (Income Gross_TMB Bank Statement Summary)+Sum of (Income Gross_Other Bank Statement Summary)] x 12 >= 100,000,000
        5. ใช้สินเชื่อทางตรงกับ TMB อย่างน้อย 1 ปี (ในหน้า Basic Info) = Yes
        */
        if (!Util.isNull(bankStatementSummary)) {
            sumBank = Util.multiply(Util.add(bankStatementSummary.getTMBTotalIncomeGross(), bankStatementSummary.getOthTotalIncomeGross()), BigDecimal.valueOf(12));
        }

        if(!Util.isNull(proposeLine) && !Util.isNull(proposeLine.getProposeCollateralInfoList())) {
            List<ProposeCollateralInfo> proposeCollateralInfoList = proposeLine.getProposeCollateralInfoList();
            if (!Util.isNull(proposeCollateralInfoList) && !Util.isZero(proposeCollateralInfoList.size())) {
                for (ProposeCollateralInfo collateral : proposeCollateralInfoList) {
                    if(!Util.isNull(collateral) && collateral.getProposeType() == ProposeType.P) {
                        if(!Util.isNull(collateral.getProposeCollateralInfoHeadList())) {
                            List<ProposeCollateralInfoHead> collHeadList = collateral.getProposeCollateralInfoHeadList();
                            if (!Util.isNull(collHeadList) && !Util.isZero(collHeadList.size())) {
                                for (ProposeCollateralInfoHead collHead : collHeadList) {
                                    if(!Util.isNull(collHead) && collHead.getPotentialCollateral() != null){
                                        PotentialCollateral potentialCollateral = collHead.getPotentialCollateral();
                                        if (potentialCollateral.getId() != 0) {
                                            if (PotentialCollateralValue.CORE_ASSET.id() == potentialCollateral.getId()) {
                                                flagForCoreAsset = true;
                                                break;
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

        if (!Util.isNull(basicInfo)) {
            if (((!Util.isNull(basicInfo.getBorrowerType())) && (basicInfo.getBorrowerType().getId() == BorrowerType.INDIVIDUAL.value())) &&
                    ((!Util.isNull(basicInfo.getSbfScore())) && (basicInfo.getSbfScore().getScore() <= 13)) &&
                    ((!Util.isNull(basicInfo.getHaveLoanInOneYear())) && (basicInfo.getHaveLoanInOneYear() == RadioValue.YES.value())) &&
                    (sumBank.compareTo(numBank) >= 0) && (flagForCoreAsset)) {
                summary = Util.subtract(num2, proposeLine.getExistingSMELimit());   //35 ล้าน - วงเงิน/ภาระสินเชื่อ SME เดิม (รวมกลุ่มกิจการในเครื่อ)
            } else {
                summary = Util.subtract(num1, proposeLine.getExistingSMELimit());   //20 ล้าน - วงเงิน/ภาระสินเชื่อ SME เดิม (รวมกลุ่มกิจการในเครื่อ)
            }
        }
        return summary;
    }

    public BigDecimal findLTVPercent(ProposeCollateralInfoHead proposeCollateralInfoHead, BasicInfo basicInfo, WorkCase workCase) {
        BigDecimal ltvPercentBig = BigDecimal.ZERO;
        if(!Util.isNull(proposeCollateralInfoHead)){
            if(!Util.isNull(proposeCollateralInfoHead.getPotentialCollateral()) && !Util.isZero(proposeCollateralInfoHead.getPotentialCollateral().getId())
                    && !Util.isNull(proposeCollateralInfoHead.getCollateralType()) && !Util.isZero(proposeCollateralInfoHead.getCollateralType().getId())){
                PotentialCollateral potentialCollateral = proposeCollateralInfoHead.getPotentialCollateral();
                TCGCollateralType tcgCollateralType = proposeCollateralInfoHead.getCollateralType();
                if (!Util.isNull(potentialCollateral) && !Util.isNull(tcgCollateralType)) {
                    PotentialColToTCGCol potentialColToTCGCol = potentialColToTCGColDAO.getPotentialColToTCGCol(potentialCollateral, tcgCollateralType);
                    if (potentialColToTCGCol != null) {
                        if (basicInfo != null && workCase != null) {
                            if (workCase.getProductGroup() != null && Util.isTrue(workCase.getProductGroup().getSpecialLTV())) {
                                if (potentialColToTCGCol.getRetentionLTV() != null) {
                                    ltvPercentBig = potentialColToTCGCol.getRetentionLTV();
                                } else {
                                    if (Util.isRadioTrue(basicInfo.getExistingSMECustomer()) &&
                                            Util.isRadioTrue(basicInfo.getPassAnnualReview()) &&
                                            Util.isRadioTrue(basicInfo.getRequestLoanWithSameName()) &&
                                            Util.isRadioTrue(basicInfo.getHaveLoanInOneYear()) &&
                                            (basicInfo.getSbfScore() != null && basicInfo.getSbfScore().getScore() <= 15)) {
                                        ltvPercentBig = potentialColToTCGCol.getTenPercentLTV();
                                    } else {
                                        ltvPercentBig = potentialColToTCGCol.getPercentLTV();
                                    }
                                }
                            } else if (Util.isRadioTrue(basicInfo.getExistingSMECustomer()) &&
                                    Util.isRadioTrue(basicInfo.getPassAnnualReview()) &&
                                    Util.isRadioTrue(basicInfo.getRequestLoanWithSameName()) &&
                                    Util.isRadioTrue(basicInfo.getHaveLoanInOneYear()) &&
                                    (basicInfo.getSbfScore() != null && basicInfo.getSbfScore().getScore() <= 15)) {
                                ltvPercentBig = potentialColToTCGCol.getTenPercentLTV();
                            } else {
                                ltvPercentBig = potentialColToTCGCol.getPercentLTV();
                            }
                        }
                    }
                }
            }
        }
        return ltvPercentBig;
    }

    public void calculateBasicInfo(long workCaseId){
        log.debug("calculateBasicInfo ::: Start...");
        BasicInfo basicInfo = basicInfoDAO.findByWorkCaseId(workCaseId);
        log.info("start calBasicInfo for basicInfo {} :: ", basicInfo);
        BigDecimal totalUnpaidFeeInsurance = BigDecimal.ZERO;
        BigDecimal totalPendingClaimLG = BigDecimal.ZERO;
        Date lastReviewDate = null;
        Date extendedReviewDate = null;
        SBFScore sbfScore = new SBFScore();
        int countNonExistingSME = 0;
        int countExistingSME = 0;

        if(!Util.isNull(basicInfo)){
            List<Customer> customerList = customerDAO.findByWorkCaseId(workCaseId);

            for(Customer customer : customerList){
                if(customer.getTmbCustomerId() != null && !"".equals(customer.getTmbCustomerId())){
                    CustomerOblInfo customerOblInfo = customer.getCustomerOblInfo();
                    if(customerOblInfo != null){
                        if(customer.getReference() != null && Util.isTrue(customer.getReference().getPendingClaimLG()))
                            totalPendingClaimLG = totalPendingClaimLG.add(customerOblInfo.getPendingClaimLG());

                        if(customer.getReference() != null && Util.isTrue(customer.getReference().getUnpaidInsurance()))
                            totalUnpaidFeeInsurance = totalUnpaidFeeInsurance.add(customerOblInfo.getUnpaidFeeInsurance());

                        if(customer.getRelation() != null && customer.getRelation().getId() == RelationValue.BORROWER.value()) {
                            if(customerOblInfo.getLastReviewDate() != null){
                                if(lastReviewDate == null || customerOblInfo.getLastReviewDate().after(lastReviewDate)){
                                    lastReviewDate = customerOblInfo.getLastReviewDate();
                                }
                            }

                            if(customerOblInfo.getExtendedReviewDate() != null){
                                if(extendedReviewDate == null || customerOblInfo.getExtendedReviewDate().before(extendedReviewDate)){
                                    extendedReviewDate = customerOblInfo.getExtendedReviewDate();
                                }
                            }

                            //SCFScore, get worst score (Max is the worst) of final rate
                            if(customerOblInfo.getRatingFinal() != null && customerOblInfo.getRatingFinal().getId() != 0){
                                if(sbfScore.getScore() < customerOblInfo.getRatingFinal().getScore()){
                                    sbfScore = customerOblInfo.getRatingFinal();
                                }
                            }

                            if(customerOblInfo.getServiceSegment() != null && Util.isTrue(customerOblInfo.getServiceSegment().getExistingSME())){
                                countExistingSME++;
                                log.debug("plus countExistingSME", countExistingSME);
                            }
                        }

                        if(customer.getReference() != null && Util.isTrue(customer.getReference().getSll())){
                            if(customerOblInfo.getServiceSegment() != null && Util.isTrue(customerOblInfo.getServiceSegment().getNonExistingSME())){
                                countNonExistingSME++;
                                log.debug("plus countNonExistingSME", countNonExistingSME);
                            }
                        }
                    }
                }
            }

            log.info("value :: totalUnpaidFeeInsurance : {}, totalPendingClaimLG {}", totalUnpaidFeeInsurance, totalPendingClaimLG);

            basicInfo.setNoUnpaidFeeInsurance(totalUnpaidFeeInsurance.compareTo(BigDecimal.ZERO) == 0? RadioValue.YES.value() : RadioValue.NO.value());
            basicInfo.setNoPendingClaimLG(totalPendingClaimLG.compareTo(BigDecimal.ZERO) == 0? RadioValue.YES.value(): RadioValue.NO.value());
            basicInfo.setLastReviewDate(lastReviewDate);

            if(lastReviewDate == null)
                basicInfo.setPassAnnualReview(RadioValue.NO.value());
            else
                basicInfo.setPassAnnualReview(RadioValue.YES.value());

            if(sbfScore != null && sbfScore.getId() == 0){
                sbfScore = null;
            }
            basicInfo.setSbfScore(sbfScore);

            log.info("countExistingSME {}", countExistingSME);
            log.info("countNonExistingSME {}", countNonExistingSME);

            if(countExistingSME > 0){
                basicInfo.setRequestLoanWithSameName(RadioValue.YES.value());
            } else {
                basicInfo.setRequestLoanWithSameName(RadioValue.NO.value());
            }

            if(countExistingSME > 0 && countNonExistingSME == 0){
                basicInfo.setExistingSMECustomer(RadioValue.YES.value());
            } else {
                basicInfo.setExistingSMECustomer(RadioValue.NO.value());
            }

            basicInfo.setExtendedReviewDate(extendedReviewDate);

            basicInfo.setRetrievedFlag(1);

            //set existing loan in one year flag
            basicInfo.setHaveLoanInOneYear(1);
            ExistingCreditFacility existingCreditFacility = existingCreditFacilityDAO.findByWorkCaseId(basicInfo.getWorkCase().getId());
            if(existingCreditFacility!=null) {
                List<ExistingCreditDetail> borrowerComExistingCreditList = existingCreditDetailDAO.findByExistingCreditFacilityByTypeAndCategory(existingCreditFacility,RelationValue.BORROWER.value(), CreditCategory.COMMERCIAL);
                if(borrowerComExistingCreditList!=null && borrowerComExistingCreditList.size()>0){
                    for(ExistingCreditDetail existingCreditDetail : borrowerComExistingCreditList){
                        if(existingCreditDetail.getExistProductSegment()!=null){
                            if(existingCreditDetail.getExistProductSegment().getId() == 2 || existingCreditDetail.getExistProductSegment().getId() == 6){
                                basicInfo.setHaveLoanInOneYear(2);
                                break;
                            }
                        }
                    }
                }
            }
            log.info("haveLoanInOneYear {}", Util.isRadioTrue(basicInfo.getHaveLoanInOneYear()));

            log.info("calBasicInfo :: basicInfo {}", basicInfo);

            basicInfoDAO.persist(basicInfo);
        }
    }
}
