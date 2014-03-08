package com.clevel.selos.integration.brms.service;

import com.clevel.selos.integration.BRMS;
import com.clevel.selos.system.Config;
import com.ilog.rules.decisionservice.*;
import com.sun.xml.internal.ws.client.BindingProviderProperties;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import java.io.Serializable;
import java.net.URL;

public class EndPoint implements Serializable {
    @Inject
    @BRMS
    Logger log;

    @Inject
    @Config(name = "interface.brms.request.timeout")
    private String brmsRequestTimeout;

    @Inject
    @Config(name = "interface.brms.wsdl.url")
    private String brmsWSDLURL;

    @Inject
    @Config(name = "interface.brms.service.url")
    private String brmsServiceURL;

    @Inject
    @Config(name = "interface.brms.prescreen.service.name")
    private String prescreenServiceName;

    @Inject
    @Config(name = "interface.brms.fullapp.service.name")
    private String fullAppServiceName;

    @Inject
    @Config(name = "interface.brms.standard.paricing.interest.service.name")
    private String interestServiceName;

    @Inject
    @Config(name = "interface.brms.standard.paricing.fee.service.name")
    private String feeServiceName;

    @Inject
    @Config(name = "interface.brms.customer.service.name")
    private String customerServiceName;

    @Inject
    @Config(name = "interface.brms.appraisal.service.name")
    private String appraisalServiceName;

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

    public DecisionServiceResponse callPrescreenUnderwritingRulesService(DecisionServiceRequest request) throws Exception {
        log.debug("callPrescreenUnderwritingRulesService()");
        log.debug("Address : {}", prescreenAddress);
        DecisionServiceSEPrescreenUWSFlow_Service service = null;
        DecisionServiceSEPrescreenUWSFlow port = null;
        DecisionServiceResponse response = null;
        try {
            URL url = new URL("http://stmbrmsred1:9080/DecisionService/ws/SE_Prescreen_UWS_RuleApp/1.0/SE_Prescreen_UWS_Flow?wsdl");
            QName qname = new QName("http://www.ilog.com/rules/DecisionService", "DecisionServiceSE_Prescreen_UWS_Flow");

            service = new DecisionServiceSEPrescreenUWSFlow_Service(url, qname);
            port = service.getDecisionServiceSOAPstmbrmsred1();
            int timeout = 60000;
            try{
                timeout=Integer.parseInt(brmsRequestTimeout)*1000;
            }catch (Exception e){
                log.debug("request Service request_timeout must be a number! {Default : 60sec}");
            }
            ((BindingProvider) port).getRequestContext().put(BindingProviderProperties.REQUEST_TIMEOUT,timeout);
            ((BindingProvider) port).getRequestContext().put(BindingProviderProperties.CONNECT_TIMEOUT,timeout);
            ((BindingProvider) port).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, prescreenAddress);
            log.debug("callPrescreenUnderwritingRulesService() Calling...{}", request);
            response = port.executeDecisionService(request);
            log.debug("callPrescreenUnderwritingRulesService() Done...{}", response);
            return response;
        } catch (Exception e) {
            log.error("callPrescreenUnderwritingRulesService() Error : {}", e);
            throw e;
        }
    }

    public DecisionServiceResponse callFullApplicationUnderwritingRulesService(DecisionServiceRequest request) throws Exception {
        log.debug("callFullApplicationUnderwritingRulesService()");
        log.debug("Address : {}", fullAppAddress);
        DecisionServiceSEFullApplicationUWSFlow_Service service = null;
        DecisionServiceSEFullApplicationUWSFlow port = null;
        DecisionServiceResponse response = null;
        try {
            service = new DecisionServiceSEFullApplicationUWSFlow_Service();
            port = service.getDecisionServiceSOAPstmbrmsred1();
            int timeout = 60000;
            try{
                timeout=Integer.parseInt(brmsRequestTimeout)*1000;
            }catch (Exception e){
                log.debug("request Service request_timeout must be a number! {Default : 60sec}");
            }
            ((BindingProvider) port).getRequestContext().put(BindingProviderProperties.REQUEST_TIMEOUT,timeout);
            ((BindingProvider) port).getRequestContext().put(BindingProviderProperties.CONNECT_TIMEOUT,timeout);
            ((BindingProvider) port).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, fullAppAddress);
            log.debug("callFullApplicationUnderwritingRulesService() Calling...{}", request);
            response = port.executeDecisionService(request);
            log.debug("callFullApplicationUnderwritingRulesService() Done...{}", response);
            return response;
        } catch (Exception e) {
            log.error("callFullApplicationUnderwritingRulesService() Error : {}", e);
            throw e;
        }
    }

    public DecisionServiceResponse callStandardPricingInterestRulesService(DecisionServiceRequest request) throws Exception {
        log.debug("callStandardPricingInterestRulesService()");
        log.debug("Address : {}", interestAddress);
        DecisionServiceSEStandardPricingInterestFlow_Service service = null;
        DecisionServiceSEStandardPricingInterestFlow port = null;
        DecisionServiceResponse response = null;
        try {
            URL url = new URL("http://stmbrmsred1:9080/DecisionService/ws/SE_Standard_Pricing_Interest_RuleApp/1.0/SE_Standard_Pricing_Interest_Flow?wsdl");
            QName qname = new QName("http://www.ilog.com/rules/DecisionService", "DecisionServiceSE_Standard_Pricing_Interest_Flow");

            service = new DecisionServiceSEStandardPricingInterestFlow_Service(url, qname);
            port = service.getDecisionServiceSOAPstmbrmsred1();
            int timeout = 60000;
            try{
                timeout=Integer.parseInt(brmsRequestTimeout)*1000;
            }catch (Exception e){
                log.debug("request Service request_timeout must be a number! {Default : 60sec}");
            }
            ((BindingProvider) port).getRequestContext().put(BindingProviderProperties.REQUEST_TIMEOUT,timeout);
            ((BindingProvider) port).getRequestContext().put(BindingProviderProperties.CONNECT_TIMEOUT,timeout);
            ((BindingProvider) port).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, interestAddress);
            log.debug("callStandardPricingInterestRulesService() Calling...{}", request);
            response = port.executeDecisionService(request);
            log.debug("callStandardPricingFeeRulesService() Done...{}", response);
            return response;
        } catch (Exception e) {
            log.error("callStandardPricingInterestRulesService() Error : {}", e);
            throw e;
        }
    }

    public DecisionServiceResponse callStandardPricingFeeRulesService(DecisionServiceRequest request) throws Exception {
        log.debug("callStandardPricingFeeRulesService()");
        log.debug("Address : {}", feeAddress);
        DecisionServiceSEStandardPricingFeeFlow_Service service = null;
        DecisionServiceSEStandardPricingFeeFlow port = null;
        DecisionServiceResponse response = null;
        try {
            URL url = new URL("http://stmbrmsred1:9080/DecisionService/ws/SE_Standard_Pricing_Fee_RuleApp/1.0/SE_Standard_Pricing_Fee_Flow?wsdl");
            QName qname = new QName("http://www.ilog.com/rules/DecisionService", "DecisionServiceSE_Standard_Pricing_Fee_Flow");

            service = new DecisionServiceSEStandardPricingFeeFlow_Service(url, qname);
            port = service.getDecisionServiceSOAPstmbrmsred1();
            int timeout = 60000;
            try{
                timeout=Integer.parseInt(brmsRequestTimeout)*1000;
            }catch (Exception e){
                log.debug("request Service request_timeout must be a number! {Default : 60sec}");
            }
            ((BindingProvider) port).getRequestContext().put(BindingProviderProperties.REQUEST_TIMEOUT,timeout);
            ((BindingProvider) port).getRequestContext().put(BindingProviderProperties.CONNECT_TIMEOUT,timeout);
            ((BindingProvider) port).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, feeAddress);
            log.debug("callStandardPricingFeeRulesService() Calling...{}", request);
            response = port.executeDecisionService(request);
            log.debug("callStandardPricingFeeRulesService() Done... {}", response);
            return response;
        } catch (Exception e) {
            log.error("callStandardPricingFeeRulesService() Error : {}", e);
            throw e;
        }
    }

    public DecisionServiceResponse callDocumentCustomerRulesService(DecisionServiceRequest request) throws Exception {
        log.debug("callDocumentCustomerRulesService()");
        log.debug("Address : {}", customerAddress);
        DecisionServiceSEDocumentCustomerFlow_Service service = null;
        DecisionServiceSEDocumentCustomerFlow port = null;
        DecisionServiceResponse response = null;
        try {
            service = new DecisionServiceSEDocumentCustomerFlow_Service();
            port = service.getDecisionServiceSOAPstmbrmsred1();
            int timeout = 60000;
            try{
                timeout=Integer.parseInt(brmsRequestTimeout)*1000;
            }catch (Exception e){
                log.debug("request Service request_timeout must be a number! {Default : 60sec}");
            }
            ((BindingProvider) port).getRequestContext().put(BindingProviderProperties.REQUEST_TIMEOUT,timeout);
            ((BindingProvider) port).getRequestContext().put(BindingProviderProperties.CONNECT_TIMEOUT,timeout);
            ((BindingProvider) port).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, customerAddress);
            log.debug("callDocumentCustomerRulesService() Calling...{}", request);
            response = port.executeDecisionService(request);
            log.debug("callStandardPricingFeeRulesService() Done... {}", response);
            return response;
        } catch (Exception e) {
            log.error("callDocumentCustomerRulesService() Error : {}", e);
            throw e;
        }
    }

    public DecisionServiceResponse callDocumentAppraisalRulesService(DecisionServiceRequest request) throws Exception {
        log.debug("callDocumentAppraisalRulesService()");
        log.debug("Address : {}", appraisalAddress);
        DecisionServiceSEDocumentAppraisalFlow_Service service = null;
        DecisionServiceSEDocumentAppraisalFlow port = null;
        DecisionServiceResponse response = null;
        try {
            service = new DecisionServiceSEDocumentAppraisalFlow_Service();
            port = service.getDecisionServiceSOAPstmbrmsred1();
            int timeout = 60000;
            try{
                timeout=Integer.parseInt(brmsRequestTimeout)*1000;
            }catch (Exception e){
                log.debug("request Service request_timeout must be a number! {Default : 60sec}");
            }
            ((BindingProvider) port).getRequestContext().put(BindingProviderProperties.REQUEST_TIMEOUT,timeout);
            ((BindingProvider) port).getRequestContext().put(BindingProviderProperties.CONNECT_TIMEOUT,timeout);
            ((BindingProvider) port).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, appraisalAddress);
            log.debug("callDocumentAppraisalRulesService() Calling...{}", request);
            response = port.executeDecisionService(request);
            log.debug("callStandardPricingFeeRulesService() Done... {}", response);
            return response;
        } catch (Exception e) {
            log.error("callDocumentAppraisalRulesService() Error : {}", e);
            throw e;
        }
    }
}
