package com.clevel.selos.businesscontrol;

import com.clevel.selos.dao.master.ProductFormulaDAO;
import com.clevel.selos.dao.relation.PrdProgramToCreditTypeDAO;
import com.clevel.selos.dao.working.*;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.*;
import com.clevel.selos.model.db.master.ProductFormula;
import com.clevel.selos.model.db.master.User;
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
                for(ProposeCreditInfo creditInfo : proposeLine.getProposeCreditInfoList()) {
                    if(!Util.isNull(creditInfo) && !Util.isNull(creditInfo.getCreditType()) && !Util.isNull(creditInfo.getProductProgram())) {
                        PrdProgramToCreditType prdProgramToCreditType = prdProgramToCreditTypeDAO.getPrdProgramToCreditType(creditInfo.getCreditType(), creditInfo.getProductProgram());
                        if(!Util.isNull(basicInfo) && !Util.isNull(basicInfo.getSpecialProgram()) && !Util.isNull(tcg) && !Util.isNull(tcg.getTcgFlag()) && !Util.isNull(proposeLine.getCreditCustomerType())) {
                            ProductFormula productFormula = productFormulaDAO.findProductFormulaPropose(prdProgramToCreditType, proposeLine.getCreditCustomerType(), basicInfo.getSpecialProgram(), tcg.getTcgFlag());
                            if(!Util.isNull(productFormula) && !Util.isNull(productFormula.getWcCalculate())) {
                                if(productFormula.getWcCalculate() == 2) {
                                    if(creditInfo.getProposeType() == ProposeType.P) {
                                        limitBDM = Util.add(limitBDM, creditInfo.getLimit());
                                    } else {
                                        limitUW = Util.add(limitUW, creditInfo.getLimit());
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
        //for submit button
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
//        exSummary.setNextReviewDate();

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
                        aaaValue = Util.add(aaaValue, Util.divide(Util.multiply(cog, bidv.getPercentBiz()), oneHundred));
                    }
                }
            }
        }

        //*** ยอดขาย/รายได้  = รายได้ต่อเดือน (adjusted) [DBR] * 12
        BigDecimal salesIncome = Util.multiply(adjustDBR, monthOfYear);
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
}
