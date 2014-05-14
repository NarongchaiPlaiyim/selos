package com.clevel.selos.transform;

import com.clevel.selos.dao.master.UWDeviationFlagDAO;
import com.clevel.selos.dao.master.UWRejectGroupDAO;
import com.clevel.selos.dao.master.UWRuleGroupDAO;
import com.clevel.selos.dao.master.UWRuleNameDAO;
import com.clevel.selos.dao.working.*;
import com.clevel.selos.exception.BRMSInterfaceException;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.integration.brms.model.response.UWRulesResult;
import com.clevel.selos.model.UWResultColor;
import com.clevel.selos.model.UWRuleType;
import com.clevel.selos.model.db.master.UWDeviationFlag;
import com.clevel.selos.model.db.master.UWRejectGroup;
import com.clevel.selos.model.db.master.UWRuleGroup;
import com.clevel.selos.model.db.master.UWRuleName;
import com.clevel.selos.model.db.working.*;
import com.clevel.selos.model.view.*;
import com.clevel.selos.system.message.ExceptionMapping;
import com.clevel.selos.system.message.ExceptionMessage;
import com.clevel.selos.system.message.Message;
import com.clevel.selos.util.Util;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class UWRuleResultTransform extends Transform{

    @Inject
    @SELOS
    private Logger logger;

    @Inject
    private CustomerTransform customerTransform;

    @Inject
    private UWRuleNameDAO uwRuleNameDAO;
    @Inject
    private UWRuleResultSummaryDAO uwRuleResultSummaryDAO;
    @Inject
    private UWRuleResultDetailDAO uwRuleResultDetailDAO;
    @Inject
    private UWDeviationFlagDAO uwDeviationFlagDAO;
    @Inject
    private UWRejectGroupDAO uwRejectGroupDAO;
    @Inject
    private UWRuleGroupDAO uwRuleGroupDAO;
    @Inject
    private CustomerDAO customerDAO;
    @Inject
    private WorkCaseDAO workCaseDAO;
    @Inject
    private WorkCasePrescreenDAO workCasePrescreenDAO;

    @Inject
    @ExceptionMessage
    Message exceptionMsg;

    @Inject
    public UWRuleResultTransform(){}

    public UWRuleResultSummaryView transformToView(Map<String, UWRulesResult> uwRulesResultMap, List<Customer> customerList){
        logger.debug("-- begin transformToView(Map<String, UWRulesResult>) {}", uwRulesResultMap);
        if(uwRulesResultMap == null || uwRulesResultMap.size() == 0)
            return null;

        UWRuleResultSummaryView uwRuleResultSummaryView = new UWRuleResultSummaryView();
        Map<String, UWRuleResultDetailView> uwRuleResultDetailViewMap = new TreeMap<String, UWRuleResultDetailView>();
        for(UWRulesResult uwRulesResult : uwRulesResultMap.values()){
            logger.debug("transform uwRuleResult: {}", uwRulesResult);
            UWRuleName uwRuleName = null;
            UWDeviationFlag uwDeviationFlag = null;
            UWRejectGroup uwRejectGroup = null;
            UWResultColor uwResultColor = UWResultColor.lookup(uwRulesResult.getColor());
            if(uwResultColor == null){
                logger.debug("Cannot Find uwResultColor - '{}' uwResultColor was not found.", uwRulesResult.getColor());
                throw new BRMSInterfaceException(null, ExceptionMapping.BRMS_INVALID_RETURN_DATA, exceptionMsg.get(ExceptionMapping.BRMS_INVALID_RETURN_DATA, "uwResultColor was not found " + uwRulesResult.getColor()));
            }

            try{
                uwRuleName = uwRuleNameDAO.findByBRMSCode(uwRulesResult.getRuleName());
            } catch (Exception ex){
                logger.debug("Cannot Find uwRuleName - '{}' uwRuleName was not found", uwRulesResult.getRuleName());
                throw new BRMSInterfaceException(null, ExceptionMapping.BRMS_INVALID_RETURN_DATA, exceptionMsg.get(ExceptionMapping.BRMS_INVALID_RETURN_DATA, "UW Rule Name was not found " + uwRulesResult.getRuleName()));
            }

            if(uwRuleName == null){
                logger.debug("Cannot Find uwRuleName - '{}' uwRuleName was not found", uwRulesResult.getRuleName());
                throw new BRMSInterfaceException(null, ExceptionMapping.BRMS_INVALID_RETURN_DATA, exceptionMsg.get(ExceptionMapping.BRMS_INVALID_RETURN_DATA, "UW Rule Name was not found " + uwRulesResult.getRuleName()));
            }

            if(uwResultColor.equals(UWResultColor.RED)){
                if(Util.isEmpty(uwRulesResult.getDeviationFlag())){
                    logger.debug("Cannot Find uwDeviationFlag - '{}' uw Deviation Flag was not found when UWResultColor is RED", uwRulesResult.getDeviationFlag());
                    throw new BRMSInterfaceException(null, ExceptionMapping.BRMS_INVALID_RETURN_DATA, exceptionMsg.get(ExceptionMapping.BRMS_INVALID_RETURN_DATA, " uw Deviation Flag was not found when UWResultColor is RED " + uwRulesResult.getDeviationFlag()));
                }
            }
            if(!Util.isEmpty(uwRulesResult.getDeviationFlag())) {
                try{
                    uwDeviationFlag = uwDeviationFlagDAO.findByBRMSCode(uwRulesResult.getDeviationFlag());
                }catch (Exception ex){
                    logger.debug("Cannot Find uwDeviationFlag - '{}' uwDeviationFlag was not found", uwRulesResult.getDeviationFlag());
                    throw new BRMSInterfaceException(null, ExceptionMapping.BRMS_INVALID_RETURN_DATA, exceptionMsg.get(ExceptionMapping.BRMS_INVALID_RETURN_DATA, "uwDeviationFlag was not found " + uwRulesResult.getDeviationFlag()));
                }
            }

            if(!Util.isEmpty(uwRulesResult.getRejectGroupCode())) {
                try{
                    uwRejectGroup = uwRejectGroupDAO.findByBRMSCode(uwRulesResult.getRejectGroupCode());
                }catch (Exception ex){
                    logger.debug("Cannot Find uwRejectGroup - '{}' uwRejectGroup was not found", uwRulesResult.getRejectGroupCode());
                    throw new BRMSInterfaceException(null, ExceptionMapping.BRMS_INVALID_RETURN_DATA, exceptionMsg.get(ExceptionMapping.BRMS_INVALID_RETURN_DATA, "uwRejectGroup was not found " + uwRulesResult.getRejectGroupCode()));
                }
            }

            if(!uwRuleName.isFinalRateFlag()){
                logger.debug("{} is not a final Result.", uwRuleName);

                UWRuleResultDetailView uwRuleResultDetailView = new UWRuleResultDetailView();
                uwRuleResultDetailView.setUwRuleNameView(transformToView(uwRuleName));
                uwRuleResultDetailView.setRuleColorResult(uwResultColor);
                uwRuleResultDetailView.setRejectGroupCode(transformToView(uwRejectGroup));
                uwRuleResultDetailView.setDeviationFlag(transformToView(uwDeviationFlag));
                uwRuleResultDetailView.setUwRuleType(uwRulesResult.getType());

                if(!Util.isEmpty(uwRulesResult.getRuleOrder())){
                    logger.debug("transform Rule Order {}", uwRulesResult.getRuleOrder());
                    try{
                        uwRuleResultDetailView.setRuleOrder(Integer.parseInt(uwRulesResult.getRuleOrder()));
                    }catch (Exception ex){
                        logger.debug("Cannot Find ruleOrder - '{}' ruleOrder was not found", uwRulesResult.getRuleOrder());
                        throw new BRMSInterfaceException(null, ExceptionMapping.BRMS_INVALID_RETURN_DATA, exceptionMsg.get(ExceptionMapping.BRMS_INVALID_RETURN_DATA, "ruleOrder was not found " + uwRulesResult.getRuleOrder()));
                    }
                }

                if(!uwRulesResult.getType().equals(UWRuleType.GROUP_LEVEL)){
                    logger.debug("found Customer Level Rule {}", uwRulesResult);
                    if(!Util.isEmpty(uwRulesResult.getPersonalID())){
                        logger.debug("find customer for Personal ID {}", uwRulesResult.getPersonalID());
                        Customer _matchedCustomer = null;
                        for(Customer customer : customerList){
                            String _personalId = null;
                            if(customer.getIndividual() != null){
                                Individual individual = customer.getIndividual();
                                _personalId = individual.getCitizenId();
                            } else if(customer.getJuristic() != null){
                                Juristic juristic = customer.getJuristic();
                                _personalId = juristic.getRegistrationId();
                            }
                            if(uwRulesResult.getPersonalID().equals(_personalId)){
                                _matchedCustomer = customer;
                                break;
                            }
                        }

                        logger.debug("Matched Customer {}", _matchedCustomer);
                        if(_matchedCustomer == null){
                            logger.debug("Cannot Find customer - '{}' customer was not found", uwRulesResult.getPersonalID());
                            throw new BRMSInterfaceException(null, ExceptionMapping.BRMS_INVALID_RETURN_DATA, exceptionMsg.get(ExceptionMapping.BRMS_INVALID_RETURN_DATA, "customer was not found " + uwRulesResult.getPersonalID()));
                        }

                        uwRuleResultDetailView.setCustomerInfoSimpleView(customerTransform.transformToSimpleView(_matchedCustomer));
                    } else {
                        logger.debug("Cannot Find customer - '{}' customer was not return", uwRulesResult.getPersonalID());
                        throw new BRMSInterfaceException(null, ExceptionMapping.BRMS_INVALID_RETURN_DATA, exceptionMsg.get(ExceptionMapping.BRMS_INVALID_RETURN_DATA, "customer was not return " + uwRulesResult.getPersonalID()));
                    }
                }

                if(uwRulesResult.getType() == UWRuleType.CUS_LEVEL){
                    String keyMap = uwRuleResultDetailView.getRuleOrder()+""+uwRulesResult.getPersonalID();
                    uwRuleResultDetailViewMap.put(keyMap, uwRuleResultDetailView);
                } else {
                    uwRuleResultDetailViewMap.put(uwRuleResultDetailView.getRuleOrder()+"", uwRuleResultDetailView);
                }
            } else {
                logger.debug("Found UW Final Result {}", uwRuleName);
                uwRuleResultSummaryView.setUwDeviationFlagView(transformToView(uwDeviationFlag));
                uwRuleResultSummaryView.setUwResultColor(uwResultColor);
                uwRuleResultSummaryView.setRejectGroupView(transformToView(uwRejectGroup));
                uwRuleResultSummaryView.setUwRuleNameView(transformToView(uwRuleName));
            }
        }

        uwRuleResultSummaryView.setUwRuleResultDetailViewMap(uwRuleResultDetailViewMap);
        logger.debug("-- end transformToView return uwRuleResultSummaryView: {}", uwRuleResultSummaryView);
        return uwRuleResultSummaryView;
    }

    public UWRuleResultSummary transformToModel(UWRuleResultSummaryView uwRuleResultSummaryView){
        logger.debug("-- begin transformToMode UWRuleResultSummaryView {}", uwRuleResultSummaryView);
        if(uwRuleResultSummaryView == null)
            return null;

        UWRuleResultSummary uwRuleResultSummary = new UWRuleResultSummary();
        if(uwRuleResultSummaryView.getWorkCasePrescreenId() != 0){
            uwRuleResultSummary.setWorkCasePrescreen(workCasePrescreenDAO.findById(uwRuleResultSummaryView.getWorkCasePrescreenId()));
            logger.debug("set UWResult for Prescreen Result");
        }
        else {
            uwRuleResultSummary.setWorkCase(workCaseDAO.findById(uwRuleResultSummaryView.getWorkCaseId()));
            logger.debug("set UWResult for Full App Result");
        }

        uwRuleResultSummary.setUwRuleName(transformToModel(uwRuleResultSummaryView.getUwRuleNameView()));
        uwRuleResultSummary.setUwResultColor(uwRuleResultSummaryView.getUwResultColor());
        uwRuleResultSummary.setRejectGroup(transformToModel(uwRuleResultSummaryView.getRejectGroupView()));
        uwRuleResultSummary.setUwDeviationFlag(transformToModel(uwRuleResultSummaryView.getUwDeviationFlagView()));

        List<UWRuleResultDetail> uwRuleResultDetailList = new ArrayList<UWRuleResultDetail>();
        Map<String, UWRuleResultDetailView> uwRuleResultDetailViewMap = uwRuleResultSummaryView.getUwRuleResultDetailViewMap();
        for(UWRuleResultDetailView uwRuleResultDetailView : uwRuleResultDetailViewMap.values()){
            UWRuleResultDetail uwRuleResultDetail = transformToModel(uwRuleResultDetailView);
            if(uwRuleResultDetail != null)
                uwRuleResultDetail.setUwRuleResultSummary(uwRuleResultSummary);
                uwRuleResultDetailList.add(uwRuleResultDetail);
        }

        uwRuleResultSummary.setUwRuleResultDetailList(uwRuleResultDetailList);
        logger.debug("-- end transformToModel return {}", uwRuleResultSummary);
        return uwRuleResultSummary;
    }

    public UWRuleResultDetail transformToModel(UWRuleResultDetailView uwRuleResultDetailView){
        logger.debug("-- begin transformToModel UWRuleResultDetailView", uwRuleResultDetailView);
        if(uwRuleResultDetailView == null)
            return null;

        UWRuleResultDetail uwRuleResultDetail = new UWRuleResultDetail();

        if(uwRuleResultDetailView.getCustomerInfoSimpleView() != null && uwRuleResultDetailView.getCustomerInfoSimpleView().getId() != 0){
            Customer customer = customerDAO.findById(uwRuleResultDetailView.getCustomerInfoSimpleView().getId());
            uwRuleResultDetail.setCustomer(customer);
        }

        uwRuleResultDetail.setUwDeviationFlag(transformToModel(uwRuleResultDetailView.getDeviationFlag()));
        uwRuleResultDetail.setRejectGroup(transformToModel(uwRuleResultDetailView.getRejectGroupCode()));
        uwRuleResultDetail.setUwResultColor(uwRuleResultDetailView.getRuleColorResult());
        uwRuleResultDetail.setUwRuleType(uwRuleResultDetailView.getUwRuleType());
        uwRuleResultDetail.setUwRuleName(transformToModel(uwRuleResultDetailView.getUwRuleNameView()));
        uwRuleResultDetail.setRuleOrder(uwRuleResultDetailView.getRuleOrder());
        logger.debug("-- end transformToModel UWRuleResultDetailView {}", uwRuleResultDetailView);
        return uwRuleResultDetail;
    }

    public UWRuleName transformToModel(UWRuleNameView uwRuleNameView){
        logger.debug("transformToModel uwRuleNameView{}", uwRuleNameView);
        if(uwRuleNameView == null || uwRuleNameView.getId() == 0)
            return null;

        UWRuleName uwRuleName = null;
        try{
            uwRuleName = uwRuleNameDAO.findById(uwRuleNameView.getId());
        } catch (Exception ex){
            logger.debug("Cannot find UWRuleName {}", uwRuleNameView.getId());
            uwRuleName = new UWRuleName();
            uwRuleName.setId(uwRuleName.getId());
        }

        if(!Util.isEmpty(uwRuleNameView.getBrmsCode()))
            uwRuleName.setBrmsCode(uwRuleNameView.getBrmsCode());
        if(!Util.isEmpty(uwRuleNameView.getDescription()))
            uwRuleName.setName(uwRuleNameView.getName());
        uwRuleName.setDescription(uwRuleNameView.getDescription());
        if(uwRuleNameView.getUwRuleGroupView() != null)
            uwRuleName.setRuleGroup(transformToModel(uwRuleNameView.getUwRuleGroupView()));
        uwRuleName.setFinalRateFlag(uwRuleNameView.isFinalRateFlag());
        logger.debug("return from transformToModel {}", uwRuleName);
        return uwRuleName;
    }

    public UWRuleGroup transformToModel(UWRuleGroupView uwRuleGroupView){
        logger.debug("transformToMode UWRuleGroupView {}", uwRuleGroupView);
        if(uwRuleGroupView == null || uwRuleGroupView.getId() == 0)
            return null;
        UWRuleGroup uwRuleGroup = null;
        try{
            uwRuleGroup = uwRuleGroupDAO.findById(uwRuleGroupView.getId());
        }catch (Exception ex){
            logger.debug("cannot found UWRuleGroup {}", uwRuleGroupView.getId());
            uwRuleGroup = new UWRuleGroup();
            uwRuleGroup.setId(uwRuleGroupView.getId());
        }
        if(!Util.isEmpty(uwRuleGroupView.getName()))
            uwRuleGroup.setName(uwRuleGroupView.getName());
        uwRuleGroup.setDescription(uwRuleGroupView.getDescription());
        logger.debug("transformToMode return {}", uwRuleGroup);
        return uwRuleGroup;
    }

    public UWRejectGroup transformToModel(UWRejectGroupView uwRejectGroupView){
        logger.debug("transformToModel UWRejectGroupView {}", uwRejectGroupView);
        if(uwRejectGroupView == null || uwRejectGroupView.getId() == 0)
            return null;
        UWRejectGroup uwRejectGroup = null;
        try{
            uwRejectGroup = uwRejectGroupDAO.findById(uwRejectGroupView.getId());
        }catch (Exception ex){
            logger.debug("Cannot find uwRejectGroup {}", uwRejectGroupView.getId());
            uwRejectGroup = new UWRejectGroup();
            uwRejectGroup.setId(uwRejectGroupView.getId());
        }
        if(!Util.isEmpty(uwRejectGroupView.getBrmsCode()))
            uwRejectGroup.setBrmsCode(uwRejectGroupView.getBrmsCode());
        if(!Util.isEmpty(uwRejectGroupView.getName()))
            uwRejectGroup.setName(uwRejectGroupView.getName());

        uwRejectGroup.setDescription(uwRejectGroupView.getDescription());
        logger.debug("transformToModel return {}", uwRejectGroup);
        return uwRejectGroup;
    }

    public UWDeviationFlag transformToModel(UWDeviationFlagView uwDeviationFlagView){
        logger.debug("transformToModel UWDeviationFlagView {}", uwDeviationFlagView);
        if(uwDeviationFlagView == null)
            return null;
        UWDeviationFlag uwDeviationFlag = null;
        try{
            uwDeviationFlag = uwDeviationFlagDAO.findById(uwDeviationFlagView.getId());
        }catch (Exception ex){
            logger.debug("Cannot Find uwDeviationFlag {}", uwDeviationFlagView.getId());
            uwDeviationFlag = new UWDeviationFlag();
            uwDeviationFlag.setId(uwDeviationFlagView.getId());
        }
        if(!Util.isEmpty(uwDeviationFlagView.getBrmsCode()))
            uwDeviationFlag.setBrmsCode(uwDeviationFlagView.getBrmsCode());
        if(!Util.isEmpty(uwDeviationFlagView.getName()))
            uwDeviationFlag.setName(uwDeviationFlagView.getName());
        uwDeviationFlag.setDescription(uwDeviationFlagView.getDescription());
        logger.debug("transformToModel return {}", uwDeviationFlag);
        return uwDeviationFlag;
    }

    public UWRuleResultSummaryView transformToView(UWRuleResultSummary uwRuleResultSummary){
        logger.debug("transformToView uwRuleResultSummary {}", uwRuleResultSummary);
        if(uwRuleResultSummary == null)
            return null;

        UWRuleResultSummaryView uwRuleResultSummaryView = new UWRuleResultSummaryView();
        uwRuleResultSummaryView.setId(uwRuleResultSummary.getId());
        uwRuleResultSummaryView.setUwRuleNameView(transformToView(uwRuleResultSummary.getUwRuleName()));
        uwRuleResultSummaryView.setUwDeviationFlagView(transformToView(uwRuleResultSummary.getUwDeviationFlag()));
        uwRuleResultSummaryView.setRejectGroupView(transformToView(uwRuleResultSummary.getRejectGroup()));
        if(uwRuleResultSummary.getWorkCasePrescreen() != null)
            uwRuleResultSummaryView.setWorkCasePrescreenId(uwRuleResultSummary.getWorkCasePrescreen().getId());
        if(uwRuleResultSummary.getWorkCase() != null)
            uwRuleResultSummaryView.setWorkCaseId(uwRuleResultSummary.getWorkCase().getId());
        uwRuleResultSummaryView.setUwResultColor(uwRuleResultSummary.getUwResultColor());

        Map<String, UWRuleResultDetailView> uwRuleResultDetailViewMap = new TreeMap<String, UWRuleResultDetailView>();
        List<UWRuleResultDetail> uwRuleResultDetailList = uwRuleResultSummary.getUwRuleResultDetailList();
        for(UWRuleResultDetail uwRuleResultDetail : uwRuleResultDetailList){
            UWRuleResultDetailView uwRuleResultDetailView = transformToView(uwRuleResultDetail);
            if(uwRuleResultDetailView != null)
                if(uwRuleResultDetail.getUwRuleType() == UWRuleType.CUS_LEVEL){
                    String keyMap = uwRuleResultDetailView.getRuleOrder()+""+uwRuleResultDetailView.getCustomerInfoSimpleView().getCitizenId();
                    uwRuleResultDetailViewMap.put(keyMap, uwRuleResultDetailView);
                } else {
                    uwRuleResultDetailViewMap.put(uwRuleResultDetailView.getRuleOrder()+"", uwRuleResultDetailView);
                }
        }
        uwRuleResultSummaryView.setUwRuleResultDetailViewMap(uwRuleResultDetailViewMap);
        logger.debug("transformToView return uwRuleResultSummaryView {}", uwRuleResultSummaryView);
        return uwRuleResultSummaryView;
    }

    public UWRuleResultDetailView transformToView(UWRuleResultDetail uwRuleResultDetail){

        if(uwRuleResultDetail == null)
            return null;

        UWRuleResultDetailView uwRuleResultDetailView = new UWRuleResultDetailView();
        uwRuleResultDetailView.setId(uwRuleResultDetail.getId());
        uwRuleResultDetailView.setDeviationFlag(transformToView(uwRuleResultDetail.getUwDeviationFlag()));
        uwRuleResultDetailView.setRuleOrder(uwRuleResultDetail.getRuleOrder());
        uwRuleResultDetailView.setUwRuleNameView(transformToView(uwRuleResultDetail.getUwRuleName()));
        uwRuleResultDetailView.setRejectGroupCode(transformToView(uwRuleResultDetail.getRejectGroup()));
        uwRuleResultDetailView.setUwRuleType(uwRuleResultDetail.getUwRuleType());
        uwRuleResultDetailView.setRuleColorResult(uwRuleResultDetail.getUwResultColor());
        uwRuleResultDetailView.setReason(uwRuleResultDetail.getReason());
        if(uwRuleResultDetail.getCustomer() != null){
            CustomerInfoSimpleView customerInfoSimpleView = customerTransform.transformToSimpleView(uwRuleResultDetail.getCustomer());
            uwRuleResultDetailView.setCustomerInfoSimpleView(customerInfoSimpleView);
        }
        return uwRuleResultDetailView;
    }

    public UWRejectGroupView transformToView(UWRejectGroup uwRejectGroup){
        if(uwRejectGroup == null)
            return null;

        UWRejectGroupView uwRejectGroupView = new UWRejectGroupView();
        uwRejectGroupView.setId(uwRejectGroup.getId());
        uwRejectGroupView.setBrmsCode(uwRejectGroup.getBrmsCode());
        uwRejectGroupView.setDescription(uwRejectGroup.getDescription());
        uwRejectGroupView.setName(uwRejectGroup.getName());
        return uwRejectGroupView;
    }

    public UWRuleGroupView transformToView(UWRuleGroup uwRuleGroup){
        if(uwRuleGroup == null)
            return null;

        UWRuleGroupView uwRuleGroupView = new UWRuleGroupView();
        uwRuleGroupView.setId(uwRuleGroup.getId());
        uwRuleGroupView.setName(uwRuleGroup.getName());
        uwRuleGroupView.setDescription(uwRuleGroup.getDescription());
        return uwRuleGroupView;
    }

    public UWDeviationFlagView transformToView(UWDeviationFlag uwDeviationFlag){
        if(uwDeviationFlag == null)
            return null;

        UWDeviationFlagView uwDeviationFlagView = new UWDeviationFlagView();
        uwDeviationFlagView.setId(uwDeviationFlag.getId());
        uwDeviationFlagView.setBrmsCode(uwDeviationFlag.getBrmsCode());
        uwDeviationFlagView.setDescription(uwDeviationFlag.getDescription());
        uwDeviationFlagView.setName(uwDeviationFlag.getName());
        return uwDeviationFlagView;
    }

    public UWRuleNameView transformToView(UWRuleName uwRuleName){
        if(uwRuleName == null)
            return null;

        UWRuleNameView uwRuleNameView = new UWRuleNameView();
        uwRuleNameView.setId(uwRuleName.getId());
        uwRuleNameView.setName(uwRuleName.getName());
        uwRuleNameView.setDescription(uwRuleName.getDescription());
        uwRuleNameView.setBrmsCode(uwRuleName.getBrmsCode());
        uwRuleNameView.setUwRuleGroupView(transformToView(uwRuleName.getRuleGroup()));
        uwRuleNameView.setFinalRateFlag(uwRuleName.isFinalRateFlag());
        return uwRuleNameView;
    }
}
