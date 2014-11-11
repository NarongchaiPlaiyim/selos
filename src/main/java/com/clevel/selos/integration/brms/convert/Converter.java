package com.clevel.selos.integration.brms.convert;

import com.clevel.selos.integration.BRMS;
import com.clevel.selos.integration.brms.model.BRMSFieldAttributes;
import com.clevel.selos.integration.brms.model.request.BRMSCustomerInfo;
import com.clevel.selos.integration.brms.model.request.BRMSNCBAccountInfo;
import com.clevel.selos.integration.brms.model.request.BRMSTMBAccountInfo;
import com.clevel.selos.integration.brms.model.response.DocumentDetail;
import com.clevel.selos.integration.brms.model.response.UWRulesResponse;
import com.clevel.selos.integration.brms.model.response.UWRulesResult;
import com.clevel.selos.model.BRMSYesNo;
import com.clevel.selos.model.DocLevel;
import com.clevel.selos.model.TMBTDRFlag;
import com.clevel.selos.model.UWRuleType;
import com.clevel.selos.util.Util;
import com.ilog.rules.decisionservice.DecisionServiceResponse;
import com.ilog.rules.param.UnderwritingRequest;
import com.ilog.rules.param.UnderwritingResult;
import com.tmbbank.enterprise.model.*;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.xml.datatype.DatatypeFactory;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

public class Converter implements Serializable {
    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");

    @Inject
    @BRMS
    Logger logger;

    @Inject
    public Converter() {
    }

    protected String getDecisionID(String applicationNo, String statusCode){
        logger.debug("getDecisionID with applicationNo {} and statusCode: {}", applicationNo, statusCode);
        String decisionID = new StringBuilder("SELOS").append(applicationNo).append(statusCode == null ? "" : statusCode).append("_").append(simpleDateFormat.format(Calendar.getInstance().getTime())).toString();
        logger.debug("return decisionID", decisionID);
        return decisionID;
    }

    protected AttributeType getAttributeType(BRMSFieldAttributes field, Date value){
        AttributeType attributeType = new AttributeType();
        try{
            attributeType.setName(field.value());
            GregorianCalendar gregorianCalendar = new GregorianCalendar();
            if(value == null){
                value = Calendar.getInstance().getTime();
            }
            gregorianCalendar.setTime(value);
            attributeType.setDateTimeValue(DatatypeFactory.newInstance().newXMLGregorianCalendar(gregorianCalendar));
        } catch (Exception ex){
            logger.warn("Cannot convert Date {}",value);
        }
        return attributeType;
    }

    protected AttributeType getAttributeType(BRMSFieldAttributes field, String value){
        AttributeType attributeType = new AttributeType();
        if(value == null){
            logger.debug("attribute value {} is null", field.value());
            value = "";
        }
        attributeType.setName(field.value());
        attributeType.setStringValue(value);
        return attributeType;
    }

    protected AttributeType getAttributeType(BRMSFieldAttributes field, BigDecimal value){
        AttributeType attributeType = new AttributeType();
        if(value == null){
            logger.debug("attribute value {} is null", field.value());
            value = BigDecimal.ZERO;
        }
        attributeType.setName(field.value());
        attributeType.setNumericValue(value);
        return attributeType;
    }

    protected AttributeType getAttributeType(BRMSFieldAttributes field, boolean value){
        AttributeType attributeType = new AttributeType();
        attributeType.setName(field.value());
        attributeType.setStringValue(BRMSYesNo.lookup(value).value());

        return attributeType;
    }

    protected BigDecimal getValueForInterface(BigDecimal value){
        if(value == null)
            return BigDecimal.ZERO;
        return value;
    }

    protected Date getValueForInterface(Date value){
        if(value == null)
            return Calendar.getInstance().getTime();
        return value;
    }

    protected String getValueForInterface(String value){
        if(value == null)
            return "";
        return value;
    }

    protected Integer getValueForInterface(Integer value){
        if(value == null)
            return 0;
        return value;
    }

    protected Boolean getValueForInterface(Boolean value){
        if(value == null)
            return false;
        return value;
    }

    protected void convertTMBAccountInfo(List<AccountType> accountTypeList, List<BRMSTMBAccountInfo> tmbAccountInfoList){

        if(tmbAccountInfoList != null && tmbAccountInfoList.size() > 0) {
            for(BRMSTMBAccountInfo tmbAccountInfo : tmbAccountInfoList){
                AccountType accountType = new AccountType();
                List<AttributeType> tmbAccAttributeList = accountType.getAttribute();
                tmbAccAttributeList.add(getAttributeType(BRMSFieldAttributes.ACCOUNT_ACTIVE_FLAG, tmbAccountInfo.isActiveFlag()));
                tmbAccAttributeList.add(getAttributeType(BRMSFieldAttributes.DATA_SOURCE, tmbAccountInfo.getDataSource()));
                tmbAccAttributeList.add(getAttributeType(BRMSFieldAttributes.ACCOUNT_REFERENCE, tmbAccountInfo.getAccountRef()));
                tmbAccAttributeList.add(getAttributeType(BRMSFieldAttributes.CUST_TO_ACCOUNT_RELATIONSHIP, tmbAccountInfo.getCustToAccountRelationCD()));
                tmbAccAttributeList.add(getAttributeType(BRMSFieldAttributes.TMB_TDR_FLAG, tmbAccountInfo.getTmbTDRFlag()));
                tmbAccAttributeList.add(getAttributeType(BRMSFieldAttributes.NUM_OF_MONTH_PRINCIPAL_AND_INTEREST_PAST_DUE, tmbAccountInfo.getNumMonthIntPastDue()));
                tmbAccAttributeList.add(getAttributeType(BRMSFieldAttributes.NUM_OF_MONTH_PRINCIPAL_AND_INTEREST_PAST_DUE_OF_TDR_ACCOUNT, tmbAccountInfo.getNumMonthIntPastDueTDRAcc()));
                tmbAccAttributeList.add(getAttributeType(BRMSFieldAttributes.NUM_OF_DAY_PRINCIPAL_PAST_DUE, tmbAccountInfo.getTmbDelPriDay()));
                tmbAccAttributeList.add(getAttributeType(BRMSFieldAttributes.NUM_OF_DAY_INTEREST_PAST_DUE, tmbAccountInfo.getTmbDelIntDay()));
                tmbAccAttributeList.add(getAttributeType(BRMSFieldAttributes.CARD_BLOCK_CODE, tmbAccountInfo.getTmbBlockCode()));
                accountTypeList.add(accountType);
            }
        } else {
            AccountType accountType = new AccountType();
            List<AttributeType> tmbAccAttributeList = accountType.getAttribute();
            tmbAccAttributeList.add(getAttributeType(BRMSFieldAttributes.ACCOUNT_ACTIVE_FLAG, Boolean.FALSE));
            tmbAccAttributeList.add(getAttributeType(BRMSFieldAttributes.DATA_SOURCE, (String)null));
            tmbAccAttributeList.add(getAttributeType(BRMSFieldAttributes.ACCOUNT_REFERENCE, (String)null));
            tmbAccAttributeList.add(getAttributeType(BRMSFieldAttributes.CUST_TO_ACCOUNT_RELATIONSHIP, (String)null));
            tmbAccAttributeList.add(getAttributeType(BRMSFieldAttributes.TMB_TDR_FLAG, TMBTDRFlag.NORMAL.value()));
            tmbAccAttributeList.add(getAttributeType(BRMSFieldAttributes.NUM_OF_MONTH_PRINCIPAL_AND_INTEREST_PAST_DUE, BigDecimal.ZERO));
            tmbAccAttributeList.add(getAttributeType(BRMSFieldAttributes.NUM_OF_MONTH_PRINCIPAL_AND_INTEREST_PAST_DUE_OF_TDR_ACCOUNT, BigDecimal.ZERO));
            tmbAccAttributeList.add(getAttributeType(BRMSFieldAttributes.NUM_OF_DAY_PRINCIPAL_PAST_DUE, BigDecimal.ZERO));
            tmbAccAttributeList.add(getAttributeType(BRMSFieldAttributes.NUM_OF_DAY_INTEREST_PAST_DUE, BigDecimal.ZERO));
            tmbAccAttributeList.add(getAttributeType(BRMSFieldAttributes.CARD_BLOCK_CODE, (String)null));
            accountTypeList.add(accountType);
        }

    }

    protected NCBReportType getNCBReportType(BRMSCustomerInfo customerInfo){
        NCBReportType ncbReportType = new NCBReportType();
        List<AttributeType> ncbReportAttrList = ncbReportType.getAttribute();
        ncbReportAttrList.add(getAttributeType(BRMSFieldAttributes.NCB_FLAG, customerInfo.isNcbFlag()));

        List<BRMSNCBAccountInfo> brmsNCBAccountInfoList = customerInfo.getNcbAccountInfoList();
        List<NCBAccountType> ncbAccountList = ncbReportType.getNcbAccount();
        for(BRMSNCBAccountInfo brmsNCBAccountInfo : brmsNCBAccountInfoList){
            NCBAccountType ncbAccount = new NCBAccountType();
            ncbAccount.setNcbAccountStatus(getValueForInterface(brmsNCBAccountInfo.getLoanAccountStatus()));
            ncbAccount.setAccountType(getValueForInterface(brmsNCBAccountInfo.getLoanAccountType()));
            ncbAccount.setTdrFlag(getValueForInterface(brmsNCBAccountInfo.isTdrFlag()));
            ncbAccount.setOverdue31DTo60DCount(getValueForInterface(brmsNCBAccountInfo.getNumberOfOverDue()));
            ncbAccount.setOverLimitLast6MthsCount(getValueForInterface(brmsNCBAccountInfo.getNumberOfOverLimit()));

            List<AttributeType> ncbAccountAttrList = ncbAccount.getAttribute();
            ncbAccountAttrList.add(getAttributeType(BRMSFieldAttributes.TMB_BANK_FLAG, brmsNCBAccountInfo.isTmbFlag()));
            ncbAccountAttrList.add(getAttributeType(BRMSFieldAttributes.NCB_NPL_FLAG, brmsNCBAccountInfo.isNplFlag()));
            ncbAccountAttrList.add(getAttributeType(BRMSFieldAttributes.NCB_TDR_FLAG, brmsNCBAccountInfo.isTdrFlag()));
            ncbAccountAttrList.add(getAttributeType(BRMSFieldAttributes.CREDIT_AMOUNT_AT_FIRST_NPL_DATE, brmsNCBAccountInfo.getCreditAmtAtNPLDate()));
            if(customerInfo.isIndividual()){
                ncbAccountAttrList.add(getAttributeType(BRMSFieldAttributes.CURRENT_PAYMENT_PATTERN_INDV, brmsNCBAccountInfo.getCurrentPaymentType()));
                ncbAccountAttrList.add(getAttributeType(BRMSFieldAttributes.SIX_MONTHS_PAYMENT_PATTERN_INDV, brmsNCBAccountInfo.getSixMonthPaymentType()));
                ncbAccountAttrList.add(getAttributeType(BRMSFieldAttributes.TWELVE_MONTHS_PAYMENT_PATTERN_INDV, brmsNCBAccountInfo.getTwelveMonthPaymentType()));
            } else {
                ncbAccountAttrList.add(getAttributeType(BRMSFieldAttributes.CURRENT_PAYMENT_PATTERN_JURIS, brmsNCBAccountInfo.getCurrentPaymentType()));
                ncbAccountAttrList.add(getAttributeType(BRMSFieldAttributes.SIX_MONTHS_PAYMENT_PATTERN_JURIS, brmsNCBAccountInfo.getSixMonthPaymentType()));
                ncbAccountAttrList.add(getAttributeType(BRMSFieldAttributes.TWELVE_MONTHS_PAYMENT_PATTERN_JURIS, brmsNCBAccountInfo.getTwelveMonthPaymentType()));
            }
            ncbAccountAttrList.add(getAttributeType(BRMSFieldAttributes.NUM_OF_MONTH_ACCOUNT_CLOSE_DATE, brmsNCBAccountInfo.getAccountCloseDateMonths()));

            ncbAccountList.add(ncbAccount);
        }
        //Set NCB Enquiry info//
        List<NCBEnquiryType> enquiryTypeList = ncbReportType.getNcbEnquiry();
        NCBEnquiryType ncbEnquiryType = new NCBEnquiryType();
        ncbEnquiryType.setNumSearchesLast6Mths(getValueForInterface(customerInfo.getNumberOfNCBCheckIn6Months()));
        List<AttributeType> ncbEnqAttrList = ncbEnquiryType.getAttribute();
        ncbEnqAttrList.add(getAttributeType(BRMSFieldAttributes.NUM_OF_DAYS_NCB_CHECK, customerInfo.getNumberOfDayLastNCBCheck()));
        enquiryTypeList.add(ncbEnquiryType);

        return ncbReportType;
    }

    protected List<DocumentDetail> getDocumentDetail(List<DocumentSetType> documentSetTypeList, String owner, DocLevel docLevel){
        logger.debug("-- begin getDocumentDetail from List<DocumentSetType>{}", documentSetTypeList);

        List<DocumentDetail> documentDetailList = new ArrayList<DocumentDetail>();

        for(DocumentSetType documentSetType : documentSetTypeList){
            List<DocumentType> documentTypeList = documentSetType.getDocument();
            for(DocumentType documentType : documentTypeList){
                logger.debug("transform document type {}", documentType);
                DocumentDetail documentDetail = new DocumentDetail();
                documentDetail.setId(documentType.getID());
                documentDetail.setDescription(documentType.getDescription());
                documentDetail.setMandateFlag(documentType.isMandatoryFlag());

                List<AttributeType> attributeTypeList = documentType.getAttribute();
                for(AttributeType attributeType : attributeTypeList){
                    if(BRMSFieldAttributes.DOCUMENT_GROUP.value().equals(attributeType.getName())){
                        documentDetail.setDocumentGroup(attributeType.getStringValue());
                    } else if(BRMSFieldAttributes.CONDITION.value().endsWith(attributeType.getName())){
                        documentDetail.setCondition(attributeType.getStringValue());
                    } else if(BRMSFieldAttributes.STEP.value().equals(attributeType.getName())){
                        documentDetail.setStep(attributeType.getStringValue());
                    } else if(BRMSFieldAttributes.SHOW_FLAG.value().equals(attributeType.getName())){
                        documentDetail.setShowFlag(attributeType.getStringValue());
                    } else if(BRMSFieldAttributes.OPER_STEP.value().equals(attributeType.getName())){
                        documentDetail.setOperStep(attributeType.getStringValue());
                    } else if(BRMSFieldAttributes.OPER_SHOW_FLAG.value().equals(attributeType.getName())){
                        documentDetail.setOperShowFlag(attributeType.getStringValue());
                    } else if(BRMSFieldAttributes.OPER_MANDATORY_Flag.value().equals(attributeType.getName())){
                        documentDetail.setOperMandateFlag(attributeType.isBooleanValue());
                    }
                }
                documentDetail.setDocLevel(docLevel);
                documentDetail.setDocOwner(owner);
                logger.debug("to DocumentDetail {}", documentDetail);
                documentDetailList.add(documentDetail);
            }
        }

        logger.debug("-- end getDocumentDetail return {}", documentDetailList);
        return documentDetailList;
    }

    public UWRulesResponse getUWRulesResponse(DecisionServiceResponse decisionServiceResponse){
        logger.debug("-- start convert getUWRulesResponse from DecisionServiceResponse {}", decisionServiceResponse);

        UWRulesResponse uwRulesResponse = new UWRulesResponse();

        if(decisionServiceResponse != null){
            uwRulesResponse.setDecisionID(decisionServiceResponse.getDecisionID());

            UnderwritingRequest underwritingRequest = decisionServiceResponse.getUnderwritingRequest();
            UnderwritingApprovalRequestType underwritingApprovalRequestType = underwritingRequest.getUnderwritingApprovalRequest();

            ApplicationType applicationType = underwritingApprovalRequestType.getApplication();
            uwRulesResponse.setApplicationNo(applicationType.getApplicationNumber());

            UnderwritingResult underwritingResult = decisionServiceResponse.getUnderwritingResult();
            UnderwritingApprovalResultType underwritingApprovalResultType = underwritingResult.getUnderwritingApprovalResult();

            Map<String, UWRulesResult> uwRulesResultMap = new TreeMap<String, UWRulesResult>();
            List<ResultType> resultTypeList = underwritingApprovalResultType.getResult();
            for(ResultType resultType : resultTypeList){
                UWRulesResult uwRulesResult = new UWRulesResult();
                uwRulesResult.setRuleName(resultType.getRuleName());
                //Find if it is Group Level//
                UWRuleType _ruleType = null;
                //if(("Group_Result").equals(resultType.getType()))
                if(("Group_Result").equals(resultType.getStringValue()))
                    _ruleType = UWRuleType.GROUP_LEVEL;

                uwRulesResult.setColor(resultType.getColor());
                uwRulesResult.setDeviationFlag(resultType.getDeviationFlag());
                uwRulesResult.setRejectGroupCode(resultType.getRejectGroupCode());

                List<AttributeType> uwAttributeTypeList = resultType.getAttribute();
                for(AttributeType attributeType : uwAttributeTypeList){
                    if(attributeType.getName() != null){
                        if(attributeType.getName().equals(BRMSFieldAttributes.UW_RULE_ORDER.value())){
                            uwRulesResult.setRuleOrder(attributeType.getStringValue());
                        }
                        if(attributeType.getName().equals(BRMSFieldAttributes.UW_PERSONAL_ID.value())){
                            String _attrValue = attributeType.getStringValue();
                            if(_attrValue != null){
                                uwRulesResult.setPersonalID(_attrValue);
                                _ruleType = UWRuleType.CUS_LEVEL;
                            }else {
                                //if UW_PERSONAL_ID == null, then it is Group Level//
                                _ruleType = UWRuleType.GROUP_LEVEL;
                            }
                        }
                    }
                }

                uwRulesResult.setType(_ruleType);
                if(Util.isEmpty(uwRulesResult.getRuleOrder())){
                    uwRulesResult.setRuleOrder("0001");
                }
                logger.debug("uwRulesResult : {}", uwRulesResult);
                if(_ruleType == UWRuleType.CUS_LEVEL){
                    String keyMap = uwRulesResult.getRuleOrder().concat(uwRulesResult.getPersonalID());
                    uwRulesResultMap.put(keyMap, uwRulesResult);
                } else {
                    uwRulesResultMap.put(uwRulesResult.getRuleOrder(), uwRulesResult);
                }
            }
            uwRulesResponse.setUwRulesResultMap(uwRulesResultMap);
        }

        logger.debug("-- end convert getUWRulesResponse from DecisionServiceResponse return {}", uwRulesResponse);
        return uwRulesResponse;
    }
}
