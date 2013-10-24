package com.clevel.selos.integration.brms.service;

public interface EndPoint {
    public com.clevel.selos.integration.brms.service.prescreenunderwritingrules.DecisionServiceResponse callPrescreenUnderwritingRulesService(com.clevel.selos.integration.brms.service.prescreenunderwritingrules.DecisionServiceRequest request) throws Exception;
    public com.clevel.selos.integration.brms.service.fullapplicationUnderwritingrules.DecisionServiceResponse callFullApplicationUnderwritingRulesService(com.clevel.selos.integration.brms.service.fullapplicationUnderwritingrules.DecisionServiceRequest request) throws Exception;
    public com.clevel.selos.integration.brms.service.standardpricing.interestrules.DecisionServiceResponse callStandardPricingInterestRulesService(com.clevel.selos.integration.brms.service.standardpricing.interestrules.DecisionServiceRequest request) throws Exception;
    public com.clevel.selos.integration.brms.service.standardpricing.feerules.DecisionServiceResponse callStandardPricingFeeRulesService(com.clevel.selos.integration.brms.service.standardpricing.feerules.DecisionServiceRequest request) throws Exception;
    public com.clevel.selos.integration.brms.service.document.customerrules.DecisionServiceResponse callDocumentCustomerRulesService(com.clevel.selos.integration.brms.service.document.customerrules.DecisionServiceRequest request) throws Exception;
    public com.clevel.selos.integration.brms.service.document.apprisalrules.DecisionServiceResponse callDocumentAppraisalRulesService(com.clevel.selos.integration.brms.service.document.apprisalrules.DecisionServiceRequest request) throws Exception;
}
