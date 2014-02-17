package com.clevel.selos.integration.brms.service;

import com.clevel.selos.integration.BRMS;
import com.clevel.selos.integration.brms.service.document.apprisalrules.DecisionServiceSEDocumentAppraisalFlow;
import com.clevel.selos.integration.brms.service.document.apprisalrules.DecisionServiceSEDocumentAppraisalFlow_Service;
import com.clevel.selos.integration.brms.service.document.customerrules.DecisionServiceSEDocumentCustomerFlow;
import com.clevel.selos.integration.brms.service.document.customerrules.DecisionServiceSEDocumentCustomerFlow_Service;
import com.clevel.selos.integration.brms.service.fullapplicationUnderwritingrules.DecisionServiceSEFullApplicationUWSFlow;
import com.clevel.selos.integration.brms.service.fullapplicationUnderwritingrules.DecisionServiceSEFullApplicationUWSFlow_Service;
import com.clevel.selos.integration.brms.service.prescreenunderwritingrules.DecisionServiceSEPrescreenUWSFlow;
import com.clevel.selos.integration.brms.service.prescreenunderwritingrules.DecisionServiceSEPrescreenUWSFlow_Service;
import com.clevel.selos.integration.brms.service.standardpricing.feerules.DecisionServiceSEStandardPricingFeeFlow;
import com.clevel.selos.integration.brms.service.standardpricing.feerules.DecisionServiceSEStandardPricingFeeFlow_Service;
import com.clevel.selos.integration.brms.service.standardpricing.interestrules.DecisionServiceSEStandardPricingInterestFlow;
import com.clevel.selos.integration.brms.service.standardpricing.interestrules.DecisionServiceSEStandardPricingInterestFlow_Service;
import com.clevel.selos.system.Config;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.io.Serializable;
import java.net.URL;

public class EndPoint implements Serializable {
    @Inject
    @BRMS
    Logger log;

    @Inject
    @Config(name = "interface.brms.prescreen.address")
    private String prescreenAddress;

    @Inject
    @Config(name = "interface.brms.fullapp.address")
    private String fullAppAddress;

    @Inject
    @Config(name = "interface.brms.standard.paricing.interest.address")
    private String interestAddress;

    @Inject
    @Config(name = "interface.brms.standard.paricing.fee.address")
    private String feeAddress;

    @Inject
    @Config(name = "interface.brms.document.customer.address")
    private String customerAddress;

    @Inject
    @Config(name = "interface.brms.document.appraisal.address")
    private String appraisalAddress;

    @Inject
    public EndPoint() {
    }

    public com.clevel.selos.integration.brms.service.prescreenunderwritingrules.DecisionServiceResponse callPrescreenUnderwritingRulesService(com.clevel.selos.integration.brms.service.prescreenunderwritingrules.DecisionServiceRequest request) throws Exception {
        log.debug("callPrescreenUnderwritingRulesService()");
        log.debug("Address : {}", prescreenAddress);
        DecisionServiceSEPrescreenUWSFlow_Service service = null;
        DecisionServiceSEPrescreenUWSFlow port = null;
        com.clevel.selos.integration.brms.service.prescreenunderwritingrules.DecisionServiceResponse response = null;
        try {
            service = new DecisionServiceSEPrescreenUWSFlow_Service(new URL(prescreenAddress));
            port = service.getDecisionServiceSOAPstmbrmsred1();
            log.debug("callPrescreenUnderwritingRulesService() Calling...");
            response = port.executeDecisionService(request);
            log.debug("callPrescreenUnderwritingRulesService() Done...");
            return response;
        } catch (Exception e) {
            log.error("callPrescreenUnderwritingRulesService() Error : {}", e);
            throw e;
        }
    }

    public com.clevel.selos.integration.brms.service.fullapplicationUnderwritingrules.DecisionServiceResponse callFullApplicationUnderwritingRulesService(com.clevel.selos.integration.brms.service.fullapplicationUnderwritingrules.DecisionServiceRequest request) throws Exception {
        log.debug("callFullApplicationUnderwritingRulesService()");
        log.debug("Address : {}", fullAppAddress);
        DecisionServiceSEFullApplicationUWSFlow_Service service = null;
        DecisionServiceSEFullApplicationUWSFlow port = null;
        com.clevel.selos.integration.brms.service.fullapplicationUnderwritingrules.DecisionServiceResponse response = null;
        try {
            service = new DecisionServiceSEFullApplicationUWSFlow_Service(new URL(fullAppAddress));
            port = service.getDecisionServiceSOAPstmbrmsred1();
            log.debug("callFullApplicationUnderwritingRulesService() Calling...");
            response = port.executeDecisionService(request);
            log.debug("callFullApplicationUnderwritingRulesService() Done...");
            return response;
        } catch (Exception e) {
            log.error("callFullApplicationUnderwritingRulesService() Error : {}", e);
            throw e;
        }
    }

    public com.clevel.selos.integration.brms.service.standardpricing.interestrules.DecisionServiceResponse callStandardPricingInterestRulesService(com.clevel.selos.integration.brms.service.standardpricing.interestrules.DecisionServiceRequest request) throws Exception {
        log.debug("callStandardPricingInterestRulesService()");
        log.debug("Address : {}", interestAddress);
        DecisionServiceSEStandardPricingInterestFlow_Service service = null;
        DecisionServiceSEStandardPricingInterestFlow port = null;
        com.clevel.selos.integration.brms.service.standardpricing.interestrules.DecisionServiceResponse response = null;
        try {
            service = new DecisionServiceSEStandardPricingInterestFlow_Service(new URL(interestAddress));
            port = service.getDecisionServiceSOAPstmbrmsred1();
            log.debug("callStandardPricingInterestRulesService() Calling...");
            response = port.executeDecisionService(request);
            log.debug("callStandardPricingInterestRulesService() Done...");
            return response;
        } catch (Exception e) {
            log.error("callStandardPricingInterestRulesService() Error : {}", e);
            throw e;
        }
    }

    public com.clevel.selos.integration.brms.service.standardpricing.feerules.DecisionServiceResponse callStandardPricingFeeRulesService(com.clevel.selos.integration.brms.service.standardpricing.feerules.DecisionServiceRequest request) throws Exception {
        log.debug("callStandardPricingFeeRulesService()");
        log.debug("Address : {}", feeAddress);
        DecisionServiceSEStandardPricingFeeFlow_Service service = null;
        DecisionServiceSEStandardPricingFeeFlow port = null;
        com.clevel.selos.integration.brms.service.standardpricing.feerules.DecisionServiceResponse response = null;
        try {
            service = new DecisionServiceSEStandardPricingFeeFlow_Service(new URL(feeAddress));
            port = service.getDecisionServiceSOAPstmbrmsred1();
            log.debug("callStandardPricingFeeRulesService() Calling...");
            response = port.executeDecisionService(request);
            log.debug("callStandardPricingFeeRulesService() Done...");
            return response;
        } catch (Exception e) {
            log.error("callStandardPricingFeeRulesService() Error : {}", e);
            throw e;
        }
    }

    public com.clevel.selos.integration.brms.service.document.customerrules.DecisionServiceResponse callDocumentCustomerRulesService(com.clevel.selos.integration.brms.service.document.customerrules.DecisionServiceRequest request) throws Exception {
        log.debug("callDocumentCustomerRulesService()");
        log.debug("Address : {}", customerAddress);
        DecisionServiceSEDocumentCustomerFlow_Service service = null;
        DecisionServiceSEDocumentCustomerFlow port = null;
        com.clevel.selos.integration.brms.service.document.customerrules.DecisionServiceResponse response = null;
        try {
            service = new DecisionServiceSEDocumentCustomerFlow_Service(new URL(customerAddress));
            port = service.getDecisionServiceSOAPstmbrmsred1();
            log.debug("callDocumentCustomerRulesService() Calling...");
            response = port.executeDecisionService(request);
            log.debug("callDocumentCustomerRulesService() Done...");
            return response;
        } catch (Exception e) {
            log.error("callDocumentCustomerRulesService() Error : {}", e);
            throw e;
        }
    }

    public com.clevel.selos.integration.brms.service.document.apprisalrules.DecisionServiceResponse callDocumentAppraisalRulesService(com.clevel.selos.integration.brms.service.document.apprisalrules.DecisionServiceRequest request) throws Exception {
        log.debug("callDocumentAppraisalRulesService()");
        log.debug("Address : {}", appraisalAddress);
        DecisionServiceSEDocumentAppraisalFlow_Service service = null;
        DecisionServiceSEDocumentAppraisalFlow port = null;
        com.clevel.selos.integration.brms.service.document.apprisalrules.DecisionServiceResponse response = null;
        try {
            service = new DecisionServiceSEDocumentAppraisalFlow_Service(new URL(appraisalAddress));
            port = service.getDecisionServiceSOAPstmbrmsred1();
            log.debug("callDocumentAppraisalRulesService() Calling...");
            response = port.executeDecisionService(request);
            log.debug("callDocumentAppraisalRulesService() Done...");
            return response;
        } catch (Exception e) {
            log.error("callDocumentAppraisalRulesService() Error : {}", e);
            throw e;
        }
    }
}
