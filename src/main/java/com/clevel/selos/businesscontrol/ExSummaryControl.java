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
import com.clevel.selos.transform.CustomerTransform;
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
import java.util.Locale;

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
    private ProposeLineDAO proposeLineDAO;
    @Inject
    private RiskTypeDAO riskTypeDAO;
    @Inject
    private DecisionDAO decisionDAO;
    @Inject
    UWRuleResultDetailDAO uwRuleResultDetailDAO;
    @Inject
    ExistingCreditFacilityDAO existingCreditFacilityDAO;

    @Inject
    private ExSummaryTransform exSummaryTransform;
    @Inject
    private CustomerTransform customerTransform;

    @Inject
    private CustomerInfoControl customerInfoControl;
    @Inject
    private NCBInfoControl ncbInfoControl;
    @Inject
    private BizInfoSummaryControl bizInfoSummaryControl;
    @Inject
    private QualitativeControl qualitativeControl;
    @Inject
    private ProposeLineControl proposeLineControl;
    @Inject
    private DecisionControl decisionControl;
    @Inject
    private UWRuleResultControl uwRuleResultControl;

    public ExSummaryView getExSummaryViewByWorkCaseId(long workCaseId, long statusId) {
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
        ProposeLineView newCreditFacilityView = proposeLineControl.findProposeLineViewByWorkCaseId(workCaseId);
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
            List<ExSumAccountMovementView> exSumAccountMovementViewList = new ArrayList<ExSumAccountMovementView>();
            if(mainBank != null || otherBank != null) {
                if(mainBank != null) {
                    exSumAccountMovementViewList.add(mainBank);
                    //exSummaryView.getExSumAccMovementViewList().add(mainBank);
                }
                if(otherBank != null) {
                    exSumAccountMovementViewList.add(otherBank);
                    //exSummaryView.getExSumAccMovementViewList().add(otherBank);
                }
                exSummaryView.setExSumAccMovementViewList(exSumAccountMovementViewList);
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

            if(basicInfo.getBorrowerType().getId() == BorrowerType.INDIVIDUAL.value()){ // id = 1 use bank stmt
                if(bankStatementSummary != null && bankStatementSummary.getGrdTotalIncomeGross() != null){
                    bizSize = Util.multiply(bankStatementSummary.getGrdTotalIncomeGross(),BigDecimal.valueOf(12));
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

            ExSumBusinessInfoView exSumBusinessInfoView = exSummaryTransform.transformBizInfoSumToExSumBizView(bizInfoSummaryView,qualitativeView,bizSize,exSummary);
            if(statusId < StatusValue.REVIEW_CA.value()){
                exSumBusinessInfoView.setUW(null);
            }
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
                    exSummaryView.setOwner(msg.get("app.exSummary.business.location.label.ownerExpired")
                                            .concat(" ")
                                            .concat(DateTimeUtil.convertToStringDDMMYYYY(bizInfoSummaryView.getExpiryDate() , new Locale("th", "TH"))));
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
                        if(DateTimeUtil.compareDate(tmpHighestDate,currentDate) < 0){
                            tmpHighestDate = currentDate;
                            tmpIndexHighestExpire = i;
                        } else {
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
                        bizPermission = bizPermission.append(Util.parseString(bizPermissionList.get(i), "").concat(", "));
                    }
                }
                exSummaryView.setBusinessPermission(bizPermission.toString());
                exSummaryView.setExpiryDate(bizInfoDetailViewList.get(tmpIndexHighestExpire).getBizDocExpiryDate());
            } else {
                exSummaryView.setBusinessPermission("-");
                exSummaryView.setExpiryDate(null);
            }
        } else {
            exSummaryView.setExSumBusinessInfoView(null);
        }

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        //Borrower
        ExSumCharacteristicView exSumCharacteristicView = new ExSumCharacteristicView();
        DBR dbr = dbrDAO.findByWorkCaseId(workCaseId);
        if(dbr != null && dbr.getId() != 0){
            exSumCharacteristicView.setCurrentDBR(dbr.getDbrBeforeRequest());
            exSumCharacteristicView.setFinalDBR(dbr.getFinalDBR());
        }

//        "กรณีผู้กู้ = Individual
//        ใช้ ""วันจดทะเบียนพาณิชย์"" หรือ ""วันก่อตั้งกิจการ/อ้างอิงประสบการณ์"" Whichever is longer
//                กรณีผู้กู้ = Juristic
//        ใช้ ""วันจดทะเบียนนิติบุคคล"" หรือ วันก่อตั้งกิจการ/อ้างอิงประสบการณ์"" Whichever is longer"
        if(bizInfoSummaryView != null && bizInfoSummaryView.getId() != 0){
            if(DateTimeUtil.compareDate(bizInfoSummaryView.getRegistrationDate(),bizInfoSummaryView.getEstablishDate()) < 0){
                exSumCharacteristicView.setStartBusinessDate(bizInfoSummaryView.getRegistrationDate());
            } else {
                exSumCharacteristicView.setStartBusinessDate(bizInfoSummaryView.getEstablishDate());
            }
        }

        exSumCharacteristicView.setSalePerYearBDM(exSummary.getSalePerYearBDM());
        exSumCharacteristicView.setGroupSaleBDM(exSummary.getGroupSaleBDM());
        exSumCharacteristicView.setGroupExposureBDM(exSummary.getGroupExposureBDM());

        if(statusId >= StatusValue.REVIEW_CA.value()){
            exSumCharacteristicView.setSalePerYearUW(exSummary.getSalePerYearUW());
            exSumCharacteristicView.setGroupSaleUW(exSummary.getGroupSaleUW());
            exSumCharacteristicView.setGroupExposureUW(exSummary.getGroupExposureUW());
        } else {
            exSumCharacteristicView.setSalePerYearUW(null);
            exSumCharacteristicView.setGroupSaleUW(null);
            exSumCharacteristicView.setGroupExposureUW(null);
        }

        if(newCreditFacilityView != null && newCreditFacilityView.getId() != 0){
            if(newCreditFacilityView.getCreditCustomerType() == CreditCustomerType.NORMAL){ // normal 1, prime 2
                exSumCharacteristicView.setCustomer("Normal");
            } else if(newCreditFacilityView.getCreditCustomerType() == CreditCustomerType.PRIME){
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
        DecisionView decisionView = decisionControl.findDecisionViewByWorkCaseId(workCaseId);
        BigDecimal tmpCashColl = null;
        BigDecimal tmpCoreAsset = null;
        BigDecimal tmpNonCore = null;
        if(decisionView != null){
            if(decisionView.getApproveCollateralList() != null && decisionView.getApproveCollateralList().size() > 0){
                for(ProposeCollateralInfoView acl : decisionView.getApproveCollateralList()){
                    if(acl.getProposeCollateralInfoHeadViewList() != null && acl.getProposeCollateralInfoHeadViewList().size() > 0){
                        for(ProposeCollateralInfoHeadView nch : acl.getProposeCollateralInfoHeadViewList()){
                            if(nch != null && nch.getPotentialCollateral() != null){
                                if(nch.getPotentialCollateral().getId() == 1){ // Cash Collateral / BE
                                    tmpCashColl = Util.add(tmpCashColl,nch.getAppraisalValue());
                                } else if(nch.getPotentialCollateral().getId() == 2){ // Core Asset
                                    tmpCoreAsset = Util.add(tmpCoreAsset,nch.getAppraisalValue());
                                } else if(nch.getPotentialCollateral().getId() == 3){ // Non - Core Asset
                                    tmpNonCore = Util.add(tmpNonCore,nch.getAppraisalValue());
                                }
                            }
                        }
                    }
                }
            }
        } else {
            ProposeLineView proposeLineView = proposeLineControl.findProposeLineViewByWorkCaseId(workCaseId);
            if(!Util.isNull(proposeLineView)) {
                if(proposeLineView.getProposeCollateralInfoViewList() != null && proposeLineView.getProposeCollateralInfoViewList().size() > 0){
                    for(ProposeCollateralInfoView pcl : proposeLineView.getProposeCollateralInfoViewList()){
                        if(pcl.getProposeCollateralInfoHeadViewList() != null && pcl.getProposeCollateralInfoHeadViewList().size() > 0){
                            for(ProposeCollateralInfoHeadView nch : pcl.getProposeCollateralInfoHeadViewList()){
                                if(nch != null && nch.getPotentialCollateral() != null){
                                    if(nch.getPotentialCollateral().getId() == 1){ // Cash Collateral / BE
                                        tmpCashColl = Util.add(tmpCashColl,nch.getAppraisalValue());
                                    } else if(nch.getPotentialCollateral().getId() == 2){ // Core Asset
                                        tmpCoreAsset = Util.add(tmpCoreAsset,nch.getAppraisalValue());
                                    } else if(nch.getPotentialCollateral().getId() == 3){ // Non - Core Asset
                                        tmpNonCore = Util.add(tmpNonCore,nch.getAppraisalValue());
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        ExSumCollateralView exSumCollateralView = new ExSumCollateralView();
        exSumCollateralView.setCashCollateralValue(tmpCashColl);
        exSumCollateralView.setCoreAssetValue(tmpCoreAsset);
        exSumCollateralView.setNoneCoreAssetValue(tmpNonCore);

//        Sum of (Propose/PreApprove/Approve Limit)
        if(decisionView != null && decisionView.getApproveTotalCreditLimit()!=null){
            exSumCollateralView.setLimitApprove(decisionView.getApproveTotalCreditLimit());
        } else if(newCreditFacilityView!=null && newCreditFacilityView.getTotalPropose()!=null) {
            exSumCollateralView.setLimitApprove(newCreditFacilityView.getTotalPropose());
        } else {
            exSumCollateralView.setLimitApprove(BigDecimal.ZERO);
        }

        //Todo: Percent LTV
//        (limitApprove + Sum(วงเงิน/ภาระสินเชื่อเดิม)) หาร (cashCollateralValue + coreAssetValue + noneCoreAssetValue)
        BigDecimal existingSMELimit = null;
        if(newCreditFacilityView != null){
            existingSMELimit = newCreditFacilityView.getExistingSMELimit();
        }
        exSumCollateralView.setPercentLTV(Util.multiply(Util.divide(Util.add(exSumCollateralView.getLimitApprove(),existingSMELimit),Util.add(Util.add(tmpCashColl,tmpCoreAsset),tmpNonCore)),BigDecimal.valueOf(100)));

        exSummaryView.setExSumCollateralView(exSumCollateralView);

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //Credit Risk Type
        ExSumCreditRiskInfoView exSumCreditRiskInfoView = new ExSumCreditRiskInfoView();
        if(basicInfo != null && basicInfo.getRiskCustomerType() != null && basicInfo.getRiskCustomerType().getId() != 0){
            RiskType riskType = riskTypeDAO.findById(basicInfo.getRiskCustomerType().getId());
            if(riskType != null){
                exSumCreditRiskInfoView.setRiskCusType(riskType.getDescription());
            } else {
                exSumCreditRiskInfoView.setRiskCusType("-");
            }
        } else {
            exSumCreditRiskInfoView.setRiskCusType("-");
        }

        if(basicInfo != null && basicInfo.getExistingSMECustomer() == RadioValue.NO.value()){ //new customer
            if(qualitativeView != null && qualitativeView.getId() != 0){
                if(qualitativeView.getQualityLevel().getDescription() != null){
                    exSumCreditRiskInfoView.setReason(qualitativeView.getQualityLevel().getDescription());
                } else {
                    exSumCreditRiskInfoView.setReason("-");
                }

                if(qualitativeView.getQualityResult() != null && !qualitativeView.getQualityResult().trim().equalsIgnoreCase("")){
                    exSumCreditRiskInfoView.setBotClass(qualitativeView.getQualityResult());
                } else {
                    exSumCreditRiskInfoView.setBotClass("-");
                }
            }
        } else { // (Bot Class = P,SM,SS,D,DL) DL is the worst.
            String tmpWorstCase = "";
            String worstCase = "";
            if(cusListView != null && cusListView.size() > 0){
                for(int i = 0; i < cusListView.size() ; i++){
                    if(cusListView.get(i).getRelation().getId() == RelationValue.BORROWER.value()){
                        tmpWorstCase = calWorstCaseBotClass(tmpWorstCase,cusListView.get(i).getAdjustClass());
                    }
                }
            }

            if(qualitativeView != null && qualitativeView.getId() != 0){
                if(qualitativeView.getQualityLevel().getDescription() != null){
                    exSumCreditRiskInfoView.setReason(qualitativeView.getQualityLevel().getDescription());
                } else {
                    exSumCreditRiskInfoView.setReason("-");
                }

                if(qualitativeView.getQualityResult() != null && !qualitativeView.getQualityResult().trim().equalsIgnoreCase("")){
                    worstCase = calWorstCaseBotClass(tmpWorstCase,qualitativeView.getQualityResult());
                    if(worstCase.trim().equalsIgnoreCase("")){
                        exSumCreditRiskInfoView.setBotClass("-");
                    } else {
                        exSumCreditRiskInfoView.setBotClass(worstCase);
                    }
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
        UWRuleResultSummaryView uwRuleResultSummaryView = uwRuleResultControl.getUWRuleResultByWorkCaseId(workCaseId);
        if(uwRuleResultSummaryView != null && uwRuleResultSummaryView.getId() != 0){
            if(uwRuleResultSummaryView.getUwDeviationFlagView() != null){
                exSummaryView.setApplicationResult(uwRuleResultSummaryView.getUwDeviationFlagView().getName());
            }
            exSummaryView.setApplicationColorResult(uwRuleResultSummaryView.getUwResultColor());
            exSummaryView.setUwRuleSummaryId(uwRuleResultSummaryView.getId());

            List<ExSumDecisionView> exSumDecisionViewList = exSummaryTransform.transformUWRuleToExSumDecision(uwRuleResultSummaryView);

            exSummaryView.setExSumDecisionListView(exSumDecisionViewList);
        } else {
            exSummaryView.setExSumDecisionListView(new ArrayList<ExSumDecisionView>());
        }

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

        //Delete By Tmp DeviateDecision
        if(exSummaryView.getDeleteTmpList() != null && exSummaryView.getDeleteTmpList().size() > 0){
            for(Long tmpId : exSummaryView.getDeleteTmpList()){
                if(tmpId != null && tmpId != 0){
                    UWRuleResultDetail uwRuleResultDetail = uwRuleResultDetailDAO.findById(tmpId);
                    uwRuleResultDetailDAO.delete(uwRuleResultDetail);
                }
            }
        }
        //Save Deviate
        log.debug("workCaseId :: {}, UwRuleSummaryId :: {} , ExSumDecisionListView :: {}",workCaseId, exSummaryView.getUwRuleSummaryId(), exSummaryView.getExSumDecisionListView());
        List<UWRuleResultDetail> uwRuleResultDetails = exSummaryTransform.transformExSumDecisionToUWRuleResultDetailModelList(workCaseId, exSummaryView.getUwRuleSummaryId(), exSummaryView.getExSumDecisionListView());
        int tmpMostOrder = 90000;
        for(UWRuleResultDetail uw : uwRuleResultDetails){
            if(uw.getUwResultColor() == null){ // add new only not from BRMS
                if(uw.getRuleOrder() == 0){ //for find most order
                    for(int i = 0 ; i < uwRuleResultDetails.size() ; i++){
                        if(uwRuleResultDetails.get(i).getRuleOrder() > 90000){
                            if(uwRuleResultDetails.get(i).getRuleOrder() > tmpMostOrder){
                                tmpMostOrder = uwRuleResultDetails.get(i).getRuleOrder();
                            }
                        }
                    }
                    tmpMostOrder = tmpMostOrder + 1;
                    uw.setRuleOrder(tmpMostOrder);
                }
            }
        }
        uwRuleResultDetailDAO.persist(uwRuleResultDetails);

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
        calGroupExposureBorrowerCharacteristic(workCaseId);
    }

    public void calForDecision(long workCaseId){
        calIncomeBorrowerCharacteristic(workCaseId);
        calActualWCBorrowerCharacteristic(workCaseId);
        calGroupExposureBorrowerCharacteristic(workCaseId);
    }

    public void calForBankStmtSummary(long workCaseId, long stepId){
        calSalePerYearBorrowerCharacteristic(workCaseId);
        calGroupSaleBorrowerCharacteristic(workCaseId, stepId);
    }

    public void calForDBR(long workCaseId){
        calIncomeBorrowerCharacteristic(workCaseId);
    }

    public void calForBizInfoSummary(long workCaseId){
        calYearInBusinessBorrowerCharacteristic(workCaseId);
        calIncomeFactor(workCaseId);
    }

    public void calForCustomerInfoJuristic(long workCaseId, long stepId){
        calGroupSaleBorrowerCharacteristic(workCaseId, stepId);
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
        Decision decision = decisionDAO.findByWorkCaseId(workCaseId);

        BigDecimal totalWCTMB = BigDecimal.ZERO;
        BigDecimal odLimit = BigDecimal.ZERO;
        BigDecimal loanCoreWC = BigDecimal.ZERO;
        BigDecimal adjusted = BigDecimal.ZERO;
        BigDecimal twelve = BigDecimal.valueOf(12);

        User user = getCurrentUser();

        if(!Util.isNull(proposeLine) && !Util.isZero(proposeLine.getId())){
            totalWCTMB = proposeLine.getTotalWCTmb();
            if(user.getRole().getId() != RoleValue.UW.id()) {
                odLimit = proposeLine.getTotalCommercialAndOBOD();
                loanCoreWC = proposeLine.getTotalCommercial();
            }
        }

        if(!Util.isNull(decision) && !Util.isZero(decision.getId()) && user.getRole().getId() == RoleValue.UW.id()){
            odLimit = decision.getTotalApproveComAndOBOD();
            loanCoreWC = decision.getTotalApproveCommercial();
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
        ProposeLine newCreditFacility = proposeLineDAO.findByWorkCaseId(workCaseId);
        BasicInfo basicInfo = basicInfoDAO.findByWorkCaseId(workCaseId);

        BigDecimal recommendedWCNeed = BigDecimal.ZERO;
        BigDecimal value1 = BigDecimal.ZERO;
        BigDecimal value2 = BigDecimal.ZERO;
        BigDecimal value3 = BigDecimal.ZERO;
        BigDecimal value6 = BigDecimal.ZERO;
        if(newCreditFacility != null && newCreditFacility.getId() != 0){
            if(newCreditFacility.getCase1WCLimit() != null){
                value1 = newCreditFacility.getCase1WCLimit();
            }
            if(newCreditFacility.getCase2WCLimit() != null){
                value2 = newCreditFacility.getCase2WCLimit();
            }
            if(newCreditFacility.getCase3WCLimit() != null){
                value3 = newCreditFacility.getCase3WCLimit();
            }
            value6 = Util.subtract(newCreditFacility.getWcNeed(), newCreditFacility.getTotalWCTmb());
        }

        if(basicInfo != null && basicInfo.getId() != 0 && newCreditFacility != null && newCreditFacility.getId() != 0){
            if(basicInfo.getRefinanceIN() == RadioValue.YES.value()){
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
    public void calGroupSaleBorrowerCharacteristic(long workCaseId, long stepId){ //TODO: BankStatementSummary & Customer Info Juristic , Pls Call me !!
        log.debug("calGroupSaleBorrowerCharacteristic :: workCaseId : {}",workCaseId);
        WorkCase workCase = workCaseDAO.findById(workCaseId);
        BasicInfo basicInfo = basicInfoDAO.findByWorkCaseId(workCaseId);
        List<CustomerInfoView> cusListView = customerInfoControl.getAllCustomerByWorkCase(workCaseId);
        User user = getCurrentUser();

//    Fix ค่าของ BDM เมื่อส่งมายัง UW และ UW มีการแก้ไขข้อมูล
        BigDecimal groupSaleBDM = BigDecimal.ZERO;
        BigDecimal groupSaleUW = BigDecimal.ZERO;
        BigDecimal twelve = BigDecimal.valueOf(12);

        log.debug("calGroupSaleBorrowerCharacteristic ::: stepId : {}", stepId);

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
                        if(cus.getRelation().getId() != RelationValue.BORROWER.value()){
                            if(cus.getCustomerEntity().getId() == BorrowerType.INDIVIDUAL.value()){
                                if(cus.getReference() != null && cus.getReference().getGroupIncome() == 1){
                                    approxIncome = Util.add(approxIncome,cus.getApproxIncome());
                                }
                            } else if(cus.getCustomerEntity().getId() == BorrowerType.JURISTIC.value()){
                                saleFromFinStmt = Util.add(saleFromFinStmt,cus.getSalesFromFinancialStmt());
                            }
                        } else if(cus.getCustomerEntity().getId() == BorrowerType.JURISTIC.value()){
                            saleFromFinStmt = Util.add(saleFromFinStmt,cus.getSalesFromFinancialStmt());
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
                        if(cus.getRelation().getId() != RelationValue.BORROWER.value()){
                            if(cus.getCustomerEntity().getId() == BorrowerType.INDIVIDUAL.value()){
                                if(cus.getReference() != null && cus.getReference().getGroupIncome() == 1){
                                    approxIncome = Util.add(approxIncome,cus.getApproxIncome());
                                }
                            } else if(cus.getCustomerEntity().getId() == BorrowerType.JURISTIC.value()){
                                saleFromFinStmt = Util.add(saleFromFinStmt,cus.getSalesFromFinancialStmt());
                            }
                        } else if(cus.getCustomerEntity().getId() == BorrowerType.JURISTIC.value()){
                            saleFromFinStmt = Util.add(saleFromFinStmt,cus.getSalesFromFinancialStmt());
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
        ProposeLine newCreditFacility = proposeLineDAO.findByWorkCaseId(workCaseId);
        Decision decision = decisionDAO.findByWorkCaseId(workCaseId);
        BigDecimal groupExposureBDM = null;
        BigDecimal groupExposureUW = null;
        if(!Util.isNull(newCreditFacility) && !Util.isZero(newCreditFacility.getId())){
            groupExposureBDM = Util.add(newCreditFacility.getTotalExposure(), newCreditFacility.getTotalPropose());
            if(!Util.isNull(decision) && !Util.isZero(decision.getId())){
                groupExposureUW = Util.add(newCreditFacility.getTotalExposure(), decision.getTotalApproveCredit());
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

//        (Qualitative Class = P,SM,SS,D,DL) DL is the worst.
    public String calWorstCaseBotClass(String a, String b){
        if(a != null && b != null){
            if(a.trim().equalsIgnoreCase("") && b.trim().equalsIgnoreCase("")){
                return "";
            } else if(a.trim().equalsIgnoreCase("")){
                return b;
            } else if(b.trim().equalsIgnoreCase("")){
                return a;
            }
        } else if(a != null){
            return a;
        } else if(b != null){
            return b;
        } else {
            return "";
        }

        int aInt;
        int bInt;

        if(a.trim().equalsIgnoreCase("P")){
            aInt = 1;
        } else if(a.trim().equalsIgnoreCase("SM")){
            aInt = 2;
        } else if(a.trim().equalsIgnoreCase("SS")){
            aInt = 3;
        } else if(a.trim().equalsIgnoreCase("D")){
            aInt = 4;
        } else if(a.trim().equalsIgnoreCase("DL")){
            aInt = 5;
        } else {
            aInt = 0;
        }

        if(b.trim().equalsIgnoreCase("P")){
            bInt = 1;
        } else if(b.trim().equalsIgnoreCase("SM")){
            bInt = 2;
        } else if(b.trim().equalsIgnoreCase("SS")){
            bInt = 3;
        } else if(b.trim().equalsIgnoreCase("D")){
            bInt = 4;
        } else if(b.trim().equalsIgnoreCase("DL")){
            bInt = 5;
        } else {
            bInt = 0;
        }

        if(aInt > bInt){
            return a;
        } else if (aInt < bInt){
            return b;
        } else { //equal
            return a;
        }
    }

    public List<CustomerInfoView> getCustomerList(long workCaseId){
        log.info("getCustomerList ::: workCaseId : {}", workCaseId);
        List<CustomerInfoView> customerInfoViewList = customerTransform.transformToSelectList(customerDAO.findByWorkCaseId(workCaseId));
        return customerInfoViewList;
    }
}