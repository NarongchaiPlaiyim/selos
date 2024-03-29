package com.clevel.selos.integration.coms;

import com.clevel.selos.dao.ext.coms.AgreementAppIndexDAO;
import com.clevel.selos.dao.master.AADDecisionDAO;
import com.clevel.selos.dao.master.UserDAO;
import com.clevel.selos.dao.working.WorkCaseDAO;
import com.clevel.selos.exception.COMSInterfaceException;
import com.clevel.selos.integration.COMS;
import com.clevel.selos.integration.coms.db.CollateralDecisionDetail;
import com.clevel.selos.integration.coms.db.CollateralJobLevel;
import com.clevel.selos.integration.coms.db.HeadCollateral;
import com.clevel.selos.integration.coms.db.SubCollateral;
import com.clevel.selos.integration.coms.model.AddressType;
import com.clevel.selos.integration.coms.model.AppraisalData;
import com.clevel.selos.integration.coms.model.HeadCollateralData;
import com.clevel.selos.integration.coms.model.SubCollateralData;
import com.clevel.selos.integration.coms.module.AddressTypeMapping;
import com.clevel.selos.integration.coms.module.DBExecute;
import com.clevel.selos.model.db.ext.coms.AgreementAppIndex;
import com.clevel.selos.model.db.master.AADDecision;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.working.WorkCase;
import com.clevel.selos.system.Config;
import com.clevel.selos.system.message.ExceptionMapping;
import com.clevel.selos.system.message.ExceptionMessage;
import com.clevel.selos.system.message.Message;
import com.clevel.selos.util.Util;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;

@Stateless
public class COMSService implements Serializable {
    @Inject
    @COMS
    Logger log;

    @Inject
    DBExecute dbExecute;

    @Inject
    AddressTypeMapping addressTypeMapping;

    @Inject
    AgreementAppIndexDAO agreementAppIndexDAO;

    @Inject
    WorkCaseDAO workCaseDAO;

    @Inject
    UserDAO userDAO;

    @Inject
    AADDecisionDAO aadDecisionDAO;

    @Inject
    @Config(name = "interface.coms.label.district.th")
    String districtLabel;
    @Inject
    @Config(name = "interface.coms.label.subdistrict.th")
    String subDistrictLabel;
    @Inject
    @Config(name = "interface.coms.label.province.th")
    String provinceLabel;
    @Inject
    @Config(name = "interface.coms.label.country.th")
    String countryLabel;

    @Inject
    @ExceptionMessage
    Message msg;

    private boolean isMATI;

    @Inject
    public COMSService() {

    }

    private final String SPACE = " ";

    public AppraisalData getAppraisalData(String userId, String jobNo) throws Exception {
        log.debug("getAppraisalData userId: {}, jobNo: {}", userId, jobNo);
        AppraisalData appraisalData = null;
        try {
            appraisalData = new AppraisalData();
            //Get Collateral Job Level
            if(jobNo!=null && !jobNo.trim().equals("")){
                CollateralJobLevel collateralJobLevel = dbExecute.getCollateralJobLevel(jobNo.trim());
                if(collateralJobLevel!=null){
                    appraisalData.setJobId(collateralJobLevel.getJobNo());
                    appraisalData.setAppraisalDate(collateralJobLevel.getCurApprDate());
                    appraisalData.setMATI(collateralJobLevel.getMATI());
                    appraisalData.setAadDecision(collateralJobLevel.getDecision());
                    appraisalData.setUsages(collateralJobLevel.getUsages());
                    if(collateralJobLevel.getMATI()!=null && collateralJobLevel.getMATI().equalsIgnoreCase("Y")) {
                        isMATI = true;
                    } else {
                        isMATI = false;
                    }

                    //Get Decision
                    CollateralDecisionDetail collateralDecisionDetail = dbExecute.getCollateralDecisionDetail(jobNo.trim());
                    if(collateralDecisionDetail!=null){
                        appraisalData.setAadDecisionReason(collateralDecisionDetail.getReasonCondNo());
                        appraisalData.setAadDecisionReasonDetail(collateralDecisionDetail.getReasonCondRemark());
                        appraisalData.setMortgageCondition(collateralDecisionDetail.getCondNo());
                        appraisalData.setMortgageConditionDetail(collateralDecisionDetail.getCondRemark());
                    }
                    //Get Head Collateral
                    List<HeadCollateralData> headCollateralDataList = new ArrayList<HeadCollateralData>();
                    List<HeadCollateral> headCollateralList = dbExecute.getHeadCollateral(jobNo.trim());
                    if(headCollateralList!=null && headCollateralList.size()>0){
                        for(HeadCollateral headCollateral: headCollateralList){
                            HeadCollateralData headCollateralData = new HeadCollateralData();
                            headCollateralData.setCollId(headCollateral.getColId());
                            headCollateralData.setTitleDeed(headCollateral.getColNo());
                            String location = subDistrictLabel.concat(Util.getStringNotNullOrEmpty(headCollateral.getAddDistrict())).concat(SPACE)
                                    .concat(districtLabel).concat(Util.getStringNotNullOrEmpty(headCollateral.getCity())).concat(SPACE).concat(Util.getStringNotNull(headCollateral.getCityExpand())).concat(SPACE)
                                    .concat(provinceLabel).concat(Util.getStringNotNullOrEmpty(headCollateral.getProvName())).concat(SPACE).concat(Util.getStringNotNull(headCollateral.getProvExpand())).concat(SPACE)
                                    .concat(countryLabel).concat(Util.getStringNotNull(headCollateral.getCountryNameThai()));

                            /*if(headCollateral.getAddDistrict()!=null){
                                location = location.concat(headCollateral.getAddDistrict()).concat(SPACE);
                            }
                            if(headCollateral.getCity()!=null){
                                location = location.concat(headCollateral.getCity()).concat(SPACE);
                            }
                            if(headCollateral.getCityExpand()!=null){
                                location = location.concat(headCollateral.getCityExpand()).concat(SPACE);
                            }
                            if(headCollateral.getProvName()!=null){
                                location = location.concat(headCollateral.getProvName()).concat(SPACE);
                            }
                            if(headCollateral.getProvExpand()!=null){
                                location = location.concat(headCollateral.getProvExpand()).concat(SPACE);
                            }
                            if(headCollateral.getCountryNameThai()!=null){
                                location = location.concat(headCollateral.getCountryNameThai());
                            }*/

                            headCollateralData.setCollateralLocation(location);
                            BigDecimal headAppraisalValue = BigDecimal.ZERO;
                            if(isMATI){
                                headAppraisalValue = headCollateral.getMatiPrice();
                            } else {
                                headAppraisalValue = headCollateral.getcPrice();
                            }
                            headCollateralData.setAppraisalValue(headAppraisalValue);
                            headCollateralData.setHeadCollType(headCollateral.getColType());
                            headCollateralData.setSubCollType(headCollateral.getColSubType());
                            ////Get Sub Collateral
                            List<SubCollateralData> subCollateralDataList = new ArrayList<SubCollateralData>();
                            List<SubCollateral> subCollateralList = dbExecute.getSubCollateral(jobNo.trim(),headCollateral.getColId());
                            Map<String,SubCollateralData> subCollateralDataMap = new HashMap<String, SubCollateralData>();
                            if(subCollateralList!=null && subCollateralList.size()>0){
                                for(SubCollateral subCollateral: subCollateralList){
                                    String typeOfUsage = "-";
                                    SubCollateralData subCollateralData = new SubCollateralData();
                                    subCollateralData.setCollId(subCollateral.getColId());
                                    subCollateralData.setHeadCollId(subCollateral.getHeadColId());
                                    subCollateralData.setRunningNumber(subCollateral.getLineNo());
                                    subCollateralData.setHeadCollType(subCollateral.getColType());
                                    subCollateralData.setSubCollType(subCollateral.getColSubType());
                                    subCollateralData.setTitleDeed(subCollateral.getOwnDocNo());
                                    subCollateralData.setCollateralOwnerId(subCollateral.getCusId());
                                    String collateralOwner = "";
                                    if(subCollateral.getPreName()!=null){
                                        collateralOwner = collateralOwner.concat(subCollateral.getPreName()).concat(SPACE);
                                    }
                                    if(subCollateral.getName()!=null){
                                        collateralOwner = collateralOwner.concat(subCollateral.getName()).concat(SPACE);
                                    }
                                    if(subCollateral.getSirName()!=null){
                                        collateralOwner = collateralOwner.concat(subCollateral.getSirName());
                                    }
                                    subCollateralData.setCollateralOwner(collateralOwner);
                                    BigDecimal subAppraisalValue = BigDecimal.ZERO;
                                    if(isMATI){
                                        subAppraisalValue = subCollateral.getMatiPrice();
                                    } else {
                                        subAppraisalValue = subCollateral.getcPrice();
                                    }
                                    subCollateralData.setAppraisalValue(subAppraisalValue);

                                    //HashMap<String,String> usageMap = new HashMap<String, String>();

                                    //Get Address
                                    String address = "";
                                    AddressType addressType = addressTypeMapping.getAddressType(subCollateralData.getHeadCollType(), subCollateralData.getSubCollType(), subCollateral.getOnlType());
                                    if(addressType!=null){
                                        switch (addressType){
                                            case TYPE1:
                                                address = dbExecute.getAddressType1(subCollateralData.getCollId(), subCollateralData.getHeadCollId());
                                                break;
                                            case TYPE2:
                                                address = dbExecute.getAddressType2(subCollateralData.getCollId(), subCollateralData.getHeadCollId());
                                                break;
                                            case TYPE3:
                                                address = dbExecute.getAddressType3(subCollateralData.getCollId(), subCollateralData.getHeadCollId());
                                                break;
                                            case TYPE4:
                                                address = dbExecute.getAddressType4(subCollateralData.getCollId(), subCollateralData.getHeadCollId());
                                                break;
                                            case TYPE5: //Building
                                                Map<String,String> addrMap = new HashMap<String, String>();
                                                addrMap = dbExecute.getAddressType5(subCollateralData.getCollId(), subCollateralData.getHeadCollId());
                                                address = addrMap.get("address");
                                                typeOfUsage = addrMap.get("usageType");
                                                break;
                                            case TYPE6:
                                                address = dbExecute.getAddressType6(subCollateralData.getCollId(), subCollateralData.getHeadCollId());
                                                break;
                                            case TYPE7:
                                                address = dbExecute.getAddressType7(subCollateralData.getCollId(), subCollateralData.getHeadCollId());
                                                break;
                                            case TYPE8:
                                                address = dbExecute.getAddressType8(subCollateralData.getCollId(), subCollateralData.getHeadCollId());
                                                break;
                                            case TYPE9:
                                                address = dbExecute.getAddressType9(subCollateralData.getCollId(), subCollateralData.getHeadCollId());
                                                break;
                                            case TYPE10:
                                                address = dbExecute.getAddressType10(subCollateralData.getCollId(), subCollateralData.getHeadCollId());
                                                break;
                                            case TYPE11:
                                                address = dbExecute.getAddressType11(subCollateralData.getCollId(), subCollateralData.getHeadCollId());
                                                break;
                                            case TYPE12:
                                                address = dbExecute.getAddressType12(subCollateralData.getCollId(), subCollateralData.getHeadCollId());
                                                break;
                                            default:
                                                break;
                                        }
                                    }
                                    subCollateralData.setAddress(address);
                                    subCollateralData.setTypeOfUsage(typeOfUsage);

                                    if(subCollateralDataMap.containsKey(subCollateral.getColId())){
                                        SubCollateralData subCollateralDataTmp = subCollateralDataMap.get(subCollateral.getColId());
                                        String collateralOwnerStr = subCollateralDataTmp.getCollateralOwner();
                                        if(collateralOwnerStr!=null && !collateralOwnerStr.trim().equalsIgnoreCase("")){
                                            collateralOwnerStr = collateralOwnerStr.concat(", ").concat(subCollateralData.getCollateralOwner());
                                        } else {
                                            collateralOwnerStr = subCollateralData.getCollateralOwner();
                                        }
                                        subCollateralDataTmp.setCollateralOwner(collateralOwnerStr);
                                        subCollateralDataMap.put(subCollateral.getColId(),subCollateralDataTmp);
                                    } else {
                                        subCollateralDataMap.put(subCollateral.getColId(),subCollateralData);
                                    }

                                    //subCollateralDataList.add(subCollateralData);

                                    /*if(usageMap.containsKey("usages")){
                                        subCollateralData.setUsage(usageMap.get("usages"));
                                    }

                                    if(usageMap.containsKey("usageType")){
                                        subCollateralData.setTypeOfUsage(usageMap.get("usageType"));
                                    }*/
                                }
                            }

                            if(subCollateralDataMap!=null && subCollateralDataMap.size()>0){
                                subCollateralDataList = new ArrayList<SubCollateralData>(subCollateralDataMap.values());
                            }

                            headCollateralData.setSubCollateralDataList(subCollateralDataList);
                            headCollateralDataList.add(headCollateralData);
                        }
                    }
                    appraisalData.setHeadCollateralDataList(headCollateralDataList);
                } else {
                    log.debug("Data Not Found!");
                    throw new COMSInterfaceException(new Exception(msg.get(ExceptionMapping.COMS_DATA_NOT_FOUND)),ExceptionMapping.COMS_DATA_NOT_FOUND, msg.get(ExceptionMapping.COMS_DATA_NOT_FOUND));
                }
            }
        } catch (COMSInterfaceException e){
            log.error("Exception while get appraisal data!",e);
            throw e;
        } catch (Exception e) {
            log.error("Exception while get appraisal data!",e);
            throw new Exception(e.getMessage());
        }
        return appraisalData;
    }

    public boolean extractAgreementAppIndex(String userId, long workCaseId) throws Exception{
        WorkCase workCase = workCaseDAO.findById(workCaseId);
        if(workCase!=null && workCase.getId()>0){
            AgreementAppIndex agreementAppIndex = agreementAppIndexDAO.findByWorkCaseId(workCaseId);
            User user = userDAO.findUserByID(userId);
            if(agreementAppIndex!=null){
                //update extract type
                agreementAppIndex.setExtractType("U");
                agreementAppIndex.setModifyBy(user);
                agreementAppIndex.setModifyDate(new Date());
            } else {
                //insert new
                agreementAppIndex = new AgreementAppIndex();
                agreementAppIndex.setExtractType("N");
                agreementAppIndex.setExtractDate(new Date());
                agreementAppIndex.setModifyBy(user);
                agreementAppIndex.setModifyDate(new Date());
                agreementAppIndex.setAppNumber(workCase.getAppNumber());
                agreementAppIndex.setWorkCase(workCase);
            }
            agreementAppIndexDAO.save(agreementAppIndex);
            return true;
        } else {
            throw new COMSInterfaceException(new Exception(msg.get(ExceptionMapping.COMS_WORKCASE_NOT_FOUND, workCaseId+"")),ExceptionMapping.COMS_WORKCASE_NOT_FOUND, msg.get(ExceptionMapping.COMS_WORKCASE_NOT_FOUND, workCaseId+""));
        }
    }
}
