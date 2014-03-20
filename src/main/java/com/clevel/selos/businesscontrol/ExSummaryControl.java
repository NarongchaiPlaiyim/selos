package com.clevel.selos.businesscontrol;

import com.clevel.selos.dao.master.RiskTypeDAO;
import com.clevel.selos.dao.working.*;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.*;
import com.clevel.selos.model.db.master.RiskType;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.working.*;
import com.clevel.selos.model.view.*;
import com.clevel.selos.system.message.Message;
import com.clevel.selos.system.message.NormalMessage;
import com.clevel.selos.transform.ExSummaryTransform;
import com.clevel.selos.util.DateTimeUtil;
import com.clevel.selos.util.FacesUtil;
import com.clevel.selos.util.Util;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.*;

@Stateless
public class ExSummaryControl extends BusinessControl {
    @Inject
    @SELOS
    Logger log;
    @Inject
    @NormalMessage
    Message msg;

    @Inject
    private ExSummaryDAO exSummaryDAO;
    @Inject
    private CustomerDAO customerDAO;
    @Inject
    private WorkCaseDAO workCaseDAO;
    @Inject
    private ExSumDeviateDAO exSumDeviateDAO;
    @Inject
    private BasicInfoDAO basicInfoDAO;
    @Inject
    private BankStatementSummaryDAO bankStatementSummaryDAO;
    @Inject
    private DBRDAO dbrDAO;
    @Inject
    private NewCreditFacilityDAO newCreditFacilityDAO;
    @Inject
    private RiskTypeDAO riskTypeDAO;
    @Inject
    private DecisionDAO decisionDAO;

    @Inject
    private ExSummaryTransform exSummaryTransform;

    @Inject
    private CustomerInfoControl customerInfoControl;
    @Inject
    private NCBInfoControl ncbInfoControl;
    @Inject
    private BizInfoSummaryControl bizInfoSummaryControl;
    @Inject
    private QualitativeControl qualitativeControl;
    @Inject
    private CreditFacProposeControl creditFacProposeControl;

    public ExSummaryView getExSummaryViewByWorkCaseId(long workCaseId) {
        log.info("getExSummaryView ::: workCaseId : {}", workCaseId);
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
        //Account Movement
        BankStatementSummary bankStatementSummary = bankStatementSummaryDAO.findByWorkCaseId(workCaseId);
        exSummaryView.setExSumAccMovementViewList(new ArrayList<ExSumAccountMovementView>());
        if(bankStatementSummary != null && bankStatementSummary.getId() != 0){
            ExSumAccountMovementView mainBank = null;
            ExSumAccountMovementView otherBank = null;
            if(bankStatementSummary.getBankStmtList() != null && bankStatementSummary.getBankStmtList().size() > 0 ){
                for(BankStatement bs : bankStatementSummary.getBankStmtList()){
                    if(bs.getMainAccount() == 2){ // 2 = main account , 1 = not main account
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

        List<BizInfoDetailView> bizInfoDetailViewList = new ArrayList<BizInfoDetailView>();
        if(bizInfoSummaryView != null && bizInfoSummaryView.getId() != 0){
            bizInfoDetailViewList = bizInfoSummaryControl.onGetBizInfoDetailViewByBizInfoSummary(bizInfoSummaryView.getId());
        }

        if(bizInfoSummaryView != null && bizInfoSummaryView.getId() != 0){
            if(basicInfo.getBorrowerType().getId() == BorrowerType.INDIVIDUAL.value()){ // id = 1 use bank stmt
                if(bankStatementSummary != null && bankStatementSummary.getGrdTotalIncomeGross() != null){
                    bizSize = bankStatementSummary.getGrdTotalIncomeGross();
                }
            } else { // use customer
                if(cusListView != null && cusListView.size() > 0){
                    for(CustomerInfoView cus : cusListView){
                        if(cus.getCustomerEntity().getId() == BorrowerType.JURISTIC.value()){
                            if(cus.getRelation().getId() == RelationValue.BORROWER.value()){ // Borrower
                                bizSize = Util.add(bizSize , cus.getSalesFromFinancialStmt());
                            }
                        }
                    }
                }
            }

            ExSumBusinessInfoView exSumBusinessInfoView = exSummaryTransform.transformBizInfoSumToExSumBizView(bizInfoSummaryView,qualitativeView,bizSize);
            exSummaryView.setExSumBusinessInfoView(exSumBusinessInfoView);

            exSummaryView.setBusinessLocationName(bizInfoSummaryView.getBizLocationName());

            StringBuilder addressTH = new StringBuilder();
            addressTH = addressTH.append(msg.get("app.bizInfoSummary.label.addressNo").concat(" "));
            addressTH = addressTH.append((bizInfoSummaryView.getAddressNo() != null ? bizInfoSummaryView.getAddressNo() : "-").concat(" "));
            addressTH = addressTH.append(msg.get("app.bizInfoSummary.label.addressMoo").concat(" "));
            addressTH = addressTH.append((bizInfoSummaryView.getAddressMoo() != null ? bizInfoSummaryView.getAddressMoo() : "-").concat(" "));
            addressTH = addressTH.append(msg.get("app.bizInfoSummary.label.addressBuilding").concat(" "));
            addressTH = addressTH.append((bizInfoSummaryView.getAddressBuilding() != null ? bizInfoSummaryView.getAddressBuilding() : "-").concat(" "));
            addressTH = addressTH.append(msg.get("app.bizInfoSummary.label.addressStreet").concat(" "));
            addressTH = addressTH.append((bizInfoSummaryView.getAddressStreet() != null ? bizInfoSummaryView.getAddressStreet() : "-").concat(" "));
            addressTH = addressTH.append(msg.get("app.bizInfoSummary.label.subdistrict").concat(" "));
            addressTH = addressTH.append((bizInfoSummaryView.getSubDistrict() != null ? bizInfoSummaryView.getSubDistrict().getCode() != 0 ? bizInfoSummaryView.getSubDistrict().getName() : "-" : "-").concat(" "));
            addressTH = addressTH.append(msg.get("app.bizInfoSummary.label.district").concat(" "));
            addressTH = addressTH.append((bizInfoSummaryView.getDistrict() != null ? bizInfoSummaryView.getDistrict().getId() != 0 ? bizInfoSummaryView.getDistrict().getName() : "-" : "-").concat(" "));
            addressTH = addressTH.append(msg.get("app.bizInfoSummary.label.province").concat(" "));
            addressTH = addressTH.append((bizInfoSummaryView.getProvince() != null ? bizInfoSummaryView.getProvince().getCode() != 0 ? bizInfoSummaryView.getProvince().getName() : "-" : "-").concat(" "));
            addressTH = addressTH.append(msg.get("app.bizInfoSummary.label.postCode").concat(" "));
            addressTH = addressTH.append((bizInfoSummaryView.getPostCode() != null ? bizInfoSummaryView.getPostCode() : "-").concat(" "));
            addressTH = addressTH.append(msg.get("app.bizInfoSummary.label.country").concat(" "));
            addressTH = addressTH.append((bizInfoSummaryView.getCountry() != null ? bizInfoSummaryView.getCountry().getId() != 0 ? bizInfoSummaryView.getCountry().getName() : "-" : "-").concat(" "));

            exSummaryView.setBusinessLocationAddress(addressTH.toString());
            exSummaryView.setBusinessLocationAddressEN(bizInfoSummaryView.getAddressEng());
            //if isRental = N, display ownerName. If isRental = Y, display expiryDate
            if(bizInfoSummaryView.getRental() == 1) { // 1 is yes??
                if(bizInfoSummaryView.getExpiryDate() != null){
                    exSummaryView.setOwner("เช่า วันที่หมดอายุ : "+DateTimeUtil.convertToStringDDMMYYYY(bizInfoSummaryView.getExpiryDate() , new Locale("th", "TH")));
                }
            } else {
                exSummaryView.setOwner(bizInfoSummaryView.getOwnerName()); //owner name
            }

            //For footer borrower
            StringBuilder bizPermission = new StringBuilder();
            List<String> bizPermissionList = new ArrayList<String>();
            Date tmpHighestDate = new Date();
            int tmpIndexHighestExpire = 0;
            if(bizInfoDetailViewList != null && bizInfoDetailViewList.size() > 0){
                for(int i = 0 ; i < bizInfoDetailViewList.size() ; i++){
                    if(bizInfoDetailViewList.get(i).getBizPermission().equalsIgnoreCase("Y")){
                        bizPermissionList.add(bizInfoDetailViewList.get(i).getBizDocPermission());

                        Date currentDate = bizInfoDetailViewList.get(i).getBizDocExpiryDate();
                        if(DateTimeUtil.compareDate(tmpHighestDate,currentDate) > 0){
                            tmpHighestDate = currentDate;
                            tmpIndexHighestExpire = i;
                        }
                    }
                    bizInfoDetailViewList.get(i).getPercentBiz();
                }
            }
//            แสดง Business Permission จากทุกๆ ธุรกิจ โดยมีเครื่องหมายจุลภาค คั่น
            if(bizPermissionList != null && bizPermissionList.size() > 0){
                for(int i = 0 ; i < bizPermissionList.size() ; i++){
                    if((bizPermissionList.size()-1) == i){
                        bizPermission = bizPermission.append(bizPermissionList.get(i));
                    } else {
                        bizPermission = bizPermission.append(bizPermissionList.get(i).concat(", "));
                    }
                }
                exSummaryView.setBusinessPermission(bizPermission.toString());
                exSummaryView.setExpiryDate(bizInfoDetailViewList.get(tmpIndexHighestExpire).getBizDocExpiryDate());
            }
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

//        "กรณีผู้กู้ = Individual
//        ใช้ ""วันจดทะเบียนพาณิชย์"" หรือ ""วันก่อตั้งกิจการ/อ้างอิงประสบการณ์"" Whichever is longer
//                กรณีผู้กู้ = Juristic
//        ใช้ ""วันจดทะเบียนนิติบุคคล"" หรือ วันก่อตั้งกิจการ/อ้างอิงประสบการณ์"" Whichever is longer"
        if(bizInfoSummaryView != null && bizInfoSummaryView.getId() != 0){
            if(DateTimeUtil.compareDate(bizInfoSummaryView.getRegistrationDate(),bizInfoSummaryView.getEstablishDate()) > 0){
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
            } else if(newCreditFacilityView.getCreditCustomerType() == 2){
                exSumCharacteristicView.setCustomer("Prime");
            } else {
                exSumCharacteristicView.setCustomer("-");
            }
        } else {
            exSumCharacteristicView.setCustomer("-");
        }

        exSumCharacteristicView.setIncome(exSummary.getIncome());
        exSumCharacteristicView.setRecommendedWCNeed(exSummary.getRecommendedWCNeed());
        exSumCharacteristicView.setActualWC(exSummary.getActualWC());
        exSumCharacteristicView.setYearInBusiness(exSummary.getYearInBusiness());

        exSummaryView.setExSumCharacteristicView(exSumCharacteristicView);

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //Collateral

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //Credit Risk Type
        ExSumCreditRiskInfoView exSumCreditRiskInfoView = new ExSumCreditRiskInfoView();
        if(basicInfo != null && basicInfo.getRiskCustomerType() != null && basicInfo.getRiskCustomerType().getId() != 0){
            RiskType riskType = riskTypeDAO.findById(basicInfo.getRiskCustomerType().getId());
            if(riskType != null){
                exSumCreditRiskInfoView.setRiskCusType(riskType.getDescription());
            }
        }

        if(basicInfo != null && basicInfo.getExistingSMECustomer() == RadioValue.NO.value()){ //new customer
            if(qualitativeView != null && qualitativeView.getId() != 0){
                //todo: BOT Class
                exSumCreditRiskInfoView.setBotClass(qualitativeView.getQualityLevel().getDescription());
                if(qualitativeView.getReason() != null){
                    exSumCreditRiskInfoView.setReason(qualitativeView.getReason());
                } else {
                    exSumCreditRiskInfoView.setReason("-");
                }
            }
        }

        //find highest percent biz
//            แสดงประเภทการค้าขายของธุรกิจที่มีสัดส่วนมากที่สุด กรณีมีธุรกิจที่มีสัดส่วนมากที่สุดเท่ากันมากว่า 1 ธุรกิจให้แสดงธุรกิจแรก
//            exSummaryView.setBusinessOperationActivity();
//            แสดงวันที่ Expiration Date ของ Business Permission ที่ Update ที่สุด (หมดอายุ ช้าที่สุด)
//            exSummaryView.setExpiryDate();
        if(bizInfoDetailViewList != null && bizInfoDetailViewList.size() > 1){
            int tmpIndexHighestProportion = 0;
            BigDecimal tmpHighestProportion = BigDecimal.ZERO;
            for (int i=0 ; i < bizInfoDetailViewList.size() ; i++){ // find highest business proportion
                BigDecimal currentProportion;
                currentProportion = bizInfoDetailViewList.get(i).getPercentBiz();
                if(currentProportion.compareTo(tmpHighestProportion) > 0){
                    tmpHighestProportion = currentProportion;
                    tmpIndexHighestProportion = i;
                }
            }

            exSumCreditRiskInfoView.setIndirectCountryName(bizInfoDetailViewList.get(tmpIndexHighestProportion).getExpIndCountryName());
            exSumCreditRiskInfoView.setPercentExport(bizInfoDetailViewList.get(tmpIndexHighestProportion).getPercentExpIndCountryName());
            exSummaryView.setBusinessOperationActivity(bizInfoDetailViewList.get(tmpIndexHighestProportion).getBizActivity().getDescription());

//            ให้เอาที่ Business ที่มีสัดส่วนธุรกิจ ที่มีค่าสูงสุดมาแสดงผล
//            แต่กรณีที่มีสัดส่วนธุรกิจ 50 - 50 จะให้เอาข้อมูล Record แรกมาแสดงแทน
            if(exSummaryView.getExSumBusinessInfoView() != null){
                exSummaryView.getExSumBusinessInfoView().setBizType(bizInfoDetailViewList.get(tmpIndexHighestProportion).getBizType().getDescription());
                exSummaryView.getExSumBusinessInfoView().setBizGroup(bizInfoDetailViewList.get(tmpIndexHighestProportion).getBizGroup().getDescription());
                exSummaryView.getExSumBusinessInfoView().setBizCode(bizInfoDetailViewList.get(tmpIndexHighestProportion).getBizCode());
                exSummaryView.getExSumBusinessInfoView().setBizDesc(bizInfoDetailViewList.get(tmpIndexHighestProportion).getBizDesc().getName());
            }
        } else if(bizInfoDetailViewList != null && bizInfoDetailViewList.size() == 1){
            exSumCreditRiskInfoView.setIndirectCountryName(bizInfoDetailViewList.get(0).getExpIndCountryName());
            exSumCreditRiskInfoView.setPercentExport(bizInfoDetailViewList.get(0).getPercentExpIndCountryName());
            exSummaryView.setBusinessOperationActivity(bizInfoDetailViewList.get(0).getBizActivity().getDescription());

            if(exSummaryView.getExSumBusinessInfoView() != null){
                exSummaryView.getExSumBusinessInfoView().setBizType(bizInfoDetailViewList.get(0).getBizType().getDescription());
                exSummaryView.getExSumBusinessInfoView().setBizGroup(bizInfoDetailViewList.get(0).getBizGroup().getDescription());
                exSummaryView.getExSumBusinessInfoView().setBizCode(bizInfoDetailViewList.get(0).getBizCode());
                exSummaryView.getExSumBusinessInfoView().setBizDesc(bizInfoDetailViewList.get(0).getBizDesc().getName());
            }
        }

        if(exSummary != null && exSummary.getLastReviewDate() != null){
            exSumCreditRiskInfoView.setLastReviewDate(exSummary.getLastReviewDate());
        } else {
            exSumCreditRiskInfoView.setLastReviewDate(new Date());
        }

        if(exSummary != null && exSummary.getNextReviewDate() != null){
            exSumCreditRiskInfoView.setNextReviewDate(exSummary.getNextReviewDate());
        } else {
            if(exSummary != null && exSummary.getLastReviewDate() != null){
                exSumCreditRiskInfoView.setNextReviewDate(DateTimeUtil.getFirstDayOfMonthDatePlusOneYear(exSummary.getLastReviewDate()));
            } else {
                exSumCreditRiskInfoView.setNextReviewDate(DateTimeUtil.getFirstDayOfMonthDatePlusOneYear(new Date()));
            }
        }

        exSumCreditRiskInfoView.setExtendedReviewDate(null); //Always '-'

        exSummaryView.setExSumCreditRiskInfoView(exSumCreditRiskInfoView);
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

        //Delete All Deviate
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
        calAppraisalValue(workCaseId);
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

    public void calForCustomerInfoJuristic(long workCaseId){
        calGroupSaleBorrowerCharacteristic(workCaseId);
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
        NewCreditFacility newCreditFacility = newCreditFacilityDAO.findByWorkCaseId(workCaseId);

        BigDecimal totalWCTMB = BigDecimal.ZERO;
        BigDecimal odLimit = BigDecimal.ZERO;
        BigDecimal loanCoreWC = BigDecimal.ZERO;
        BigDecimal adjusted = BigDecimal.ZERO;
        BigDecimal twelve = BigDecimal.valueOf(12);

        if(newCreditFacility != null && newCreditFacility.getId() != 0){
            totalWCTMB = newCreditFacility.getTotalWcTmb();
            odLimit = newCreditFacility.getTotalCommercialAndOBOD();
            loanCoreWC = newCreditFacility.getTotalCommercial();
        }
        if(dbr != null && dbr.getId() != 0){
            adjusted = dbr.getMonthlyIncomeAdjust();
        }

        BigDecimal income = Util.divide(Util.add(Util.add(totalWCTMB,odLimit),loanCoreWC),Util.multiply(adjusted,twelve));

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

        if(basicInfo != null && basicInfo.getId() != 0 && newCreditFacility != null && newCreditFacility.getId() != 0){
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
        log.debug("calActualWCBorrowerCharacteristic :: workCaseId : {}",workCaseId);
//        NewCreditFacility newCreditFacility = newCreditFacilityDAO.findByWorkCaseId(workCaseId);
//        BigDecimal actualWC = newCreditFacility.getTotalApproveCredit();
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
//    groupSaleBDM - กรณีผู้กู้ = Juristic (รายได้ตามงบการเงิน จาก Cust Info Detail (Juristic) + รายได้ของผู้ค้ำฯ / ผู้เกี่ยวข้องทุกคนที่ Flag Group Income = Y) * 12
//    groupSaleBDM - กรณีผู้กู้ = Individual (Grand Total Income Gross จากหน้า Bank Statement Summary + รายได้ของผู้ค้ำฯ / ผู้เกี่ยวข้องทุกคนที่ Flag Group Income = Y)*12
//    Fix ค่าของ BDM เมื่อส่งมายัง UW และ UW มีการแก้ไขข้อมูล
//    groupSaleUW - กรณีผู้กู้ = Juristic (รายได้ตามงบการเงิน จาก Cust Info Detail (Juristic) + รายได้ของผู้ค้ำฯ / ผู้เกี่ยวข้องทุกคนที่ Flag Group Income = Y) * 12
//    groupSaleUW - กรณีผู้กู้ = Individual (Grand Total Income Gross จากหน้า Bank Statement Summary + รายได้ของผู้ค้ำฯ / ผู้เกี่ยวข้องทุกคนที่ Flag Group Income = Y) * 12
    public void calGroupSaleBorrowerCharacteristic(long workCaseId){ //TODO: BankStatementSummary & Customer Info Juristic , Pls Call me !!
        log.debug("calGroupSaleBorrowerCharacteristic :: workCaseId : {}",workCaseId);
        WorkCase workCase = workCaseDAO.findById(workCaseId);
        BasicInfo basicInfo = basicInfoDAO.findByWorkCaseId(workCaseId);
        List<CustomerInfoView> cusListView = customerInfoControl.getAllCustomerByWorkCase(workCaseId);
        User user = getCurrentUser();

//    Fix ค่าของ BDM เมื่อส่งมายัง UW และ UW มีการแก้ไขข้อมูล
        BigDecimal groupSaleBDM = BigDecimal.ZERO;
        BigDecimal groupSaleUW = BigDecimal.ZERO;
        BigDecimal twelve = BigDecimal.valueOf(12);

        long stepId = 0;

        HttpSession session = FacesUtil.getSession(true);
        if(session.getAttribute("stepId") != null){
            stepId = Long.parseLong(session.getAttribute("stepId").toString());
        }

        log.debug("stepId : {}",stepId);

        ExSummary exSummary = exSummaryDAO.findByWorkCaseId(workCaseId);
        if(exSummary == null){
            exSummary = new ExSummary();
            exSummary.setWorkCase(workCase);
        }

        if(stepId == StepValue.FULLAPP_BDM_SSO_ABDM.value() && user.getRole().getId() == RoleValue.BDM.id()){ // BDM //update groupSaleBDM && groupSaleUW
            if(basicInfo.getBorrowerType().getId() == BorrowerType.INDIVIDUAL.value()){ // use bank statement
//    groupSaleBDM - กรณีผู้กู้ = Individual (Grand Total Income Gross จากหน้า Bank Statement Summary + รายได้ของผู้ค้ำฯ / ผู้เกี่ยวข้องทุกคนที่ Flag Group Income = Y)*12 //
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
                            }
                        }
                    }
                }
                groupSaleBDM = Util.multiply(Util.add(grdTotalIncomeGross,approxIncome),twelve);
                groupSaleUW = Util.multiply(Util.add(grdTotalIncomeGross,approxIncome),twelve);
            } else { // use customer
//    groupSaleBDM - กรณีผู้กู้ = Juristic (รายได้ตามงบการเงิน จาก Cust Info Detail (Juristic) + รายได้ของผู้ค้ำฯ / ผู้เกี่ยวข้องทุกคนที่ Flag Group Income = Y) * 12
                BigDecimal saleFromFinStmt = BigDecimal.ZERO;
                BigDecimal approxIncome = BigDecimal.ZERO;
                if(cusListView != null && cusListView.size() > 0){
                    for(CustomerInfoView cus : cusListView){
                        if(cus.getRelation().getId() == RelationValue.BORROWER.value()){
                            if(cus.getCustomerEntity().getId() == BorrowerType.JURISTIC.value()){
                                saleFromFinStmt = Util.add(saleFromFinStmt,cus.getSalesFromFinancialStmt());
                            }
                        } else {
                            if(cus.getCustomerEntity().getId() == BorrowerType.INDIVIDUAL.value()){
                                if(cus.getReference() != null && cus.getReference().getGroupIncome() == 1){
                                    approxIncome = Util.add(approxIncome,cus.getApproxIncome());
                                }
                            }
                        }
                    }
                }
                groupSaleBDM = Util.multiply(Util.add(saleFromFinStmt,approxIncome),twelve);
                groupSaleUW = Util.multiply(Util.add(saleFromFinStmt,approxIncome),twelve);
            }
        } else if(stepId == StepValue.CREDIT_DECISION_UW1.value() && user.getRole().getId() == RoleValue.UW.id()){ //UW //update only groupSaleUW
            if(basicInfo.getBorrowerType().getId() == BorrowerType.INDIVIDUAL.value()){ // use bank statement
//    groupSaleBDM - กรณีผู้กู้ = Individual (Grand Total Income Gross จากหน้า Bank Statement Summary + รายได้ของผู้ค้ำฯ / ผู้เกี่ยวข้องทุกคนที่ Flag Group Income = Y)*12
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
                            }
                        }
                    }
                }
                groupSaleUW = Util.multiply(Util.add(grdTotalIncomeGross,approxIncome),twelve);
            } else { // use customer
//    groupSaleBDM - กรณีผู้กู้ = Juristic (รายได้ตามงบการเงิน จาก Cust Info Detail (Juristic) + รายได้ของผู้ค้ำฯ / ผู้เกี่ยวข้องทุกคนที่ Flag Group Income = Y) * 12
                BigDecimal saleFromFinStmt = BigDecimal.ZERO;
                BigDecimal approxIncome = BigDecimal.ZERO;
                if(cusListView != null && cusListView.size() > 0){
                    for(CustomerInfoView cus : cusListView){
                        if(cus.getRelation().getId() == RelationValue.BORROWER.value()){
                            if(cus.getCustomerEntity().getId() == BorrowerType.JURISTIC.value()){
                                saleFromFinStmt = Util.add(saleFromFinStmt,cus.getSalesFromFinancialStmt());
                            }
                        } else {
                            if(cus.getCustomerEntity().getId() == BorrowerType.INDIVIDUAL.value()){
                                if(cus.getReference() != null && cus.getReference().getGroupIncome() == 1){
                                    approxIncome = Util.add(approxIncome,cus.getApproxIncome());
                                }
                            }
                        }
                    }
                }
                groupSaleUW = Util.multiply(Util.add(saleFromFinStmt,approxIncome),twelve);
            }
            //for do not update group sale bdm in step uw
            groupSaleBDM = exSummary.getGroupSaleBDM();
        }

        if(user.getRole().getId() == RoleValue.UW.id()){
            exSummary.setGroupSaleUW(groupSaleUW);
        } else {
            exSummary.setGroupSaleBDM(groupSaleBDM);
        }

        exSummaryDAO.persist(exSummary);
    }

    //Borrower Characteristic - groupExposureBDM , groupExposureUW ( Line 58-59 )
    //Decision
//    groupExposureBDM - Group Total Exposure + Total Propose Credit
//    groupExposureUW - Group Total Exposure + Total Approved Credit
    public void calGroupExposureBorrowerCharacteristic(long workCaseId){ //TODO: Decision , Credit Facility-Propose , Pls Call me !!
        log.debug("calGroupExposureBorrowerCharacteristic :: workCaseId : {}",workCaseId);
        NewCreditFacility newCreditFacility = newCreditFacilityDAO.findByWorkCaseId(workCaseId);
        Decision decision = decisionDAO.findByWorkCaseId(workCaseId);
        BigDecimal groupExposureBDM = BigDecimal.ZERO;
        BigDecimal groupExposureUW = BigDecimal.ZERO;
        if((newCreditFacility != null && newCreditFacility.getId() != 0) && (decision != null && decision.getId() != 0)){
            groupExposureBDM = Util.add(newCreditFacility.getTotalExposure(), newCreditFacility.getTotalPropose());
            groupExposureUW = Util.add(newCreditFacility.getTotalExposure(), decision.getTotalApproveCredit());
        }

        ExSummary exSummary = exSummaryDAO.findByWorkCaseId(workCaseId);
        if(exSummary == null){
            exSummary = new ExSummary();
            WorkCase workCase = new WorkCase();
            workCase.setId(workCaseId);
            exSummary.setWorkCase(workCase);
        }

        User user = getCurrentUser();
        if(user.getRole().getId() == RoleValue.UW.id()){
            exSummary.setGroupExposureUW(groupExposureUW);
        } else {
            exSummary.setGroupExposureBDM(groupExposureBDM);
        }

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
            //todo:yearInBizMonth To send to BRMS
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

    public void calAppraisalValue(long workCaseId){ //todo: decision pls call me !? or other !?
        BigDecimal cashColl = BigDecimal.ZERO;
        BigDecimal coreColl = BigDecimal.ZERO;
        BigDecimal noneCoreColl = BigDecimal.ZERO;
        NewCreditFacility newCreditFacility = newCreditFacilityDAO.findByWorkCaseId(workCaseId);
        if(newCreditFacility != null && newCreditFacility.getNewCollateralDetailList() != null && newCreditFacility.getNewCollateralDetailList().size() > 0){
            for(NewCollateral newCollateral : newCreditFacility.getNewCollateralDetailList()){
                if (newCollateral.getProposeType() != null && newCollateral.getProposeType().equals("A")){
                    if(newCollateral.getNewCollateralHeadList() != null && newCollateral.getNewCollateralHeadList().size() > 0){
                        for (NewCollateralHead newCollateralHead : newCollateral.getNewCollateralHeadList()){
                            //todo:check this !? or not
//                            if (newCollateralHead.getProposeType() != null && newCollateralHead.getProposeType().equals("A")){
                                if(newCollateralHead.getPotential().getId() == PotentialCollateralValue.CASH_COLLATERAL.id()){
                                    cashColl = Util.add(cashColl,newCollateralHead.getAppraisalValue());
                                } else if(newCollateralHead.getPotential().getId() == PotentialCollateralValue.CORE_ASSET.id()){
                                    coreColl = Util.add(coreColl,newCollateralHead.getAppraisalValue());
                                } else if(newCollateralHead.getPotential().getId() == PotentialCollateralValue.NONE_CORE_ASSET.id()){
                                    noneCoreColl = Util.add(noneCoreColl,newCollateralHead.getAppraisalValue());
                                }
//                            }
                        }
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
        exSummary.setCashCollateralValue(cashColl);
        exSummary.setCoreAssetValue(coreColl);
        exSummary.setNoneCoreAssetValue(noneCoreColl);
//        exSummary.setLimitApprove();
//        exSummary.setPercentLTV();

        exSummaryDAO.persist(exSummary);
    }
}