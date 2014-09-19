package com.clevel.selos.businesscontrol;

import com.clevel.selos.dao.master.ProductFormulaDAO;
import com.clevel.selos.dao.master.RiskTypeDAO;
import com.clevel.selos.dao.relation.PrdProgramToCreditTypeDAO;
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
    private UWRuleResultDetailDAO uwRuleResultDetailDAO;
    @Inject
    private ExistingCreditFacilityDAO existingCreditFacilityDAO;
    @Inject
    private TCGDAO tcgDAO;
    @Inject
    private ProductFormulaDAO productFormulaDAO;
    @Inject
    private PrdProgramToCreditTypeDAO prdProgramToCreditTypeDAO;

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
        ProposeLineView proposeLineView = proposeLineControl.findProposeLineViewByWorkCaseId(workCaseId);
        if(proposeLineView != null && proposeLineView.getId() != 0){
            exSummaryView.setTradeFinance(proposeLineView);
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

        if(proposeLineView != null && proposeLineView.getId() != 0){
            if(proposeLineView.getCreditCustomerType() == CreditCustomerType.NORMAL){ // normal 1, prime 2
                exSumCharacteristicView.setCustomer("Normal");
            } else if(proposeLineView.getCreditCustomerType() == CreditCustomerType.PRIME){
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
        User user = getCurrentUser();
        DecisionView decisionView = decisionControl.findDecisionViewByWorkCaseId(workCaseId);
        BigDecimal tmpCashColl = null;
        BigDecimal tmpCoreAsset = null;
        BigDecimal tmpNonCore = null;
        if(!Util.isNull(user) && !Util.isNull(user.getRole()) && user.getRole().getId() == RoleValue.BDM.id()) {
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
        } else {
            if(!Util.isNull(decisionView)){
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
            }
        }

        ExSumCollateralView exSumCollateralView = new ExSumCollateralView();
        exSumCollateralView.setCashCollateralValue(tmpCashColl);
        exSumCollateralView.setCoreAssetValue(tmpCoreAsset);
        exSumCollateralView.setNoneCoreAssetValue(tmpNonCore);

//        Sum of (Propose/PreApprove/Approve Limit)
        if(decisionView != null && decisionView.getApproveTotalCreditLimit()!=null){
            exSumCollateralView.setLimitApprove(decisionView.getApproveTotalCreditLimit());
        } else if(proposeLineView!=null && proposeLineView.getTotalPropose()!=null) {
            exSumCollateralView.setLimitApprove(proposeLineView.getTotalPropose());
        } else {
            exSumCollateralView.setLimitApprove(BigDecimal.ZERO);
        }

        //Todo: Percent LTV
//        (limitApprove + Sum(วงเงิน/ภาระสินเชื่อเดิม)) หาร (cashCollateralValue + coreAssetValue + noneCoreAssetValue)
        BigDecimal existingSMELimit = null;
        if(proposeLineView != null){
            existingSMELimit = proposeLineView.getExistingSMELimit();
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
        if(Util.isSafetyList(exSummaryView.getDeviateCode()) && exSummaryView.getDeviateCode().size() > 0) {
            List<ExSumDeviate> exSumDeviateList = exSummaryTransform.transformDeviateToModel(exSummaryView.getDeviateCode(), exSummary.getId());
            exSumDeviateDAO.persist(exSumDeviateList);
        }
    }

    //(Qualitative Class = P,SM,SS,D,DL) DL is the worst.
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
                    tmpWorstCase = calWorstCaseBotClass(tmpWorstCase, customer.getCustomerOblInfo() != null ? customer.getCustomerOblInfo().getAdjustClass() : "");
                }
            }

            if(qualitativeView != null && qualitativeView.getId() != 0){
                botClassReason = qualitativeView.getQualityLevel() != null ? qualitativeView.getQualityLevel().getDescription() : "";
                if(qualitativeView.getQualityResult() != null && !qualitativeView.getQualityResult().trim().equalsIgnoreCase("")){
                    botClass = calWorstCaseBotClass(tmpWorstCase, qualitativeView.getQualityResult());
                }
            }
        }
        exSummary.setCreditRiskReason(botClassReason);
        exSummary.setCreditRiskBOTClass(botClass);
        exSummaryDAO.persist(exSummary);
    }
}