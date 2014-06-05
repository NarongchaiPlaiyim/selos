package com.clevel.selos.integration.brms.service;

import com.clevel.selos.integration.BRMS;
import com.clevel.selos.system.Config;
import com.ilog.rules.decisionservice.*;
import com.sun.xml.internal.ws.client.BindingProviderProperties;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import java.io.File;
import java.io.FileWriter;
import java.io.Serializable;
import java.net.URL;

public class EndPoint implements Serializable {
    @Inject
    @BRMS
    Logger logger;

    private static final String WSDL = "?wsdl";

    @Inject
    @Config(name = "interface.brms.request.timeout")
    private String brmsRequestTimeout;

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
        logger.debug("-- begin Sending Request to callPrescreenUnderwritingRulesService()");
        logger.debug("Service URL : {}", brmsServiceURL);
        logger.debug("Service Name : {}", prescreenServiceName);
        logger.debug("Service Address : {}", prescreenAddress);
        DecisionServiceSEPrescreenUWSFlow_Service service = null;
        DecisionServiceSEPrescreenUWSFlow port = null;
        DecisionServiceResponse response = null;
        try {
            String requestFile = "/home/slosdev/clevel/brms/prescreenUnderwritingRulesRequest.xml";
            String responseFile = "/home/slosdev/clevel/brms/prescreenUnderwritingRulesResponse.xml";
            JAXBContext requestContext = JAXBContext.newInstance(DecisionServiceRequest.class);
            Marshaller mRequest = requestContext.createMarshaller();
            mRequest.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            mRequest.marshal(request, new FileWriter(requestFile, true));

            URL url = new URL(prescreenAddress + WSDL);
            QName qname = new QName(brmsServiceURL, prescreenServiceName);

            service = new DecisionServiceSEPrescreenUWSFlow_Service(url, qname);
            port = service.getDecisionServiceSOAPstmbrmsred1();
            int timeout = 60000;
            try{
                timeout=Integer.parseInt(brmsRequestTimeout)*1000;
            }catch (Exception e){
                logger.debug("request Service request_timeout must be a number! {Default : 60sec}");
            }
            ((BindingProvider) port).getRequestContext().put(BindingProviderProperties.REQUEST_TIMEOUT,timeout);
            ((BindingProvider) port).getRequestContext().put(BindingProviderProperties.CONNECT_TIMEOUT,timeout);
            ((BindingProvider) port).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, prescreenAddress);
            logger.debug("callPrescreenUnderwritingRulesService() Calling...{}", request);
            response = port.executeDecisionService(request);
            logger.debug("callPrescreenUnderwritingRulesService() Done...{}", response);

            JAXBContext responseContext = JAXBContext.newInstance(DecisionServiceResponse.class);
            Marshaller mResponse = responseContext.createMarshaller();
            mResponse.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            mResponse.marshal(response, new FileWriter(responseFile, true));

            return response;
        } catch (Exception e) {
            logger.error("callPrescreenUnderwritingRulesService() Error :", e);
            throw e;
        }
    }

    public DecisionServiceResponse callFullApplicationUnderwritingRulesService(DecisionServiceRequest request) throws Exception {
        logger.debug("-- begin Sending Request to callFullApplicationUnderwritingRulesService()");
        logger.debug("Service URL : {}", brmsServiceURL);
        logger.debug("Service Name : {}", fullAppServiceName);
        logger.debug("Service Address : {}", fullAppAddress);
        DecisionServiceSEFullApplicationUWSFlow_Service service = null;
        DecisionServiceSEFullApplicationUWSFlow port = null;
        DecisionServiceResponse response = null;
        try {
            String requestFile = "/home/slosdev/clevel/brms/fullApplicationUnderwritingRulesRequest.xml";
            String responseFile = "/home/slosdev/clevel/brms/fullApplicationUnderwritingRulesResponse.xml";
            JAXBContext requestContext = JAXBContext.newInstance(DecisionServiceRequest.class);
            Marshaller mRequest = requestContext.createMarshaller();
            mRequest.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            mRequest.marshal(request, new FileWriter(requestFile, true));

            URL url = new URL(fullAppAddress + WSDL);
            QName qname = new QName(brmsServiceURL, fullAppServiceName);
            service = new DecisionServiceSEFullApplicationUWSFlow_Service(url, qname);

            port = service.getDecisionServiceSOAPstmbrmsred1();
            int timeout = 60000;
            try{
                timeout=Integer.parseInt(brmsRequestTimeout)*1000;
            }catch (Exception e){
                logger.debug("request Service request_timeout must be a number! {Default : 60sec}");
            }
            ((BindingProvider) port).getRequestContext().put(BindingProviderProperties.REQUEST_TIMEOUT,timeout);
            ((BindingProvider) port).getRequestContext().put(BindingProviderProperties.CONNECT_TIMEOUT,timeout);
            ((BindingProvider) port).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, fullAppAddress);
            logger.debug("callFullApplicationUnderwritingRulesService() Calling...{}", request);
            response = port.executeDecisionService(request);

            JAXBContext responseContext = JAXBContext.newInstance(DecisionServiceResponse.class);
            Marshaller mResponse = responseContext.createMarshaller();
            mResponse.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            mResponse.marshal(response, new FileWriter(responseFile, true));

            logger.debug("callFullApplicationUnderwritingRulesService() Done...{}", response);
            return response;
        } catch (Exception e) {
            logger.error("callFullApplicationUnderwritingRulesService() Error : {}", e);
            throw e;
        }
    }

    public DecisionServiceResponse callStandardPricingInterestRulesService(DecisionServiceRequest request) throws Exception {
        logger.debug("-- begin Sending Request to callStandardPricingInterestRulesService()");
        logger.debug("Service URL : {}", brmsServiceURL);
        logger.debug("Service Name : {}", interestServiceName);
        logger.debug("Service Address : {}", interestAddress);DecisionServiceSEStandardPricingInterestFlow_Service service = null;
        DecisionServiceSEStandardPricingInterestFlow port = null;
        DecisionServiceResponse response = null;
        try {

            String requestFile = "/home/slosdev/clevel/brms/standardPricingInterestRulesRequest.xml";
            String responseFile = "/home/slosdev/clevel/brms/standardPricingInterestRulesResponse.xml";
            JAXBContext requestContext = JAXBContext.newInstance(DecisionServiceRequest.class);
            Marshaller mRequest = requestContext.createMarshaller();
            mRequest.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            mRequest.marshal(request, new FileWriter(requestFile, true));

            URL url = new URL(interestAddress + WSDL);
            QName qname = new QName(brmsServiceURL, interestServiceName);

            service = new DecisionServiceSEStandardPricingInterestFlow_Service(url, qname);
            port = service.getDecisionServiceSOAPstmbrmsred1();
            int timeout = 60000;
            try{
                timeout=Integer.parseInt(brmsRequestTimeout)*1000;
            }catch (Exception e){
                logger.debug("request Service request_timeout must be a number! {Default : 60sec}");
            }
            ((BindingProvider) port).getRequestContext().put(BindingProviderProperties.REQUEST_TIMEOUT,timeout);
            ((BindingProvider) port).getRequestContext().put(BindingProviderProperties.CONNECT_TIMEOUT,timeout);
            ((BindingProvider) port).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, interestAddress);
            logger.debug("callStandardPricingInterestRulesService() Calling...{}", request);
            response = port.executeDecisionService(request);

            JAXBContext responseContext = JAXBContext.newInstance(DecisionServiceResponse.class);
            Marshaller mResponse = responseContext.createMarshaller();
            mResponse.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            mResponse.marshal(response, new FileWriter(responseFile, true));

            logger.debug("callStandardPricingFeeRulesService() Done...{}", response);
            return response;
        } catch (Exception e) {
            logger.error("callStandardPricingInterestRulesService() Error : {}", e);
            throw e;
        }
    }

    public DecisionServiceResponse callStandardPricingFeeRulesService(DecisionServiceRequest request) throws Exception {
        logger.debug("-- begin Sending Request to callStandardPricingFeeRulesService()");
        logger.debug("Service URL : {}", brmsServiceURL);
        logger.debug("Service Name : {}", feeServiceName);
        logger.debug("Service Address : {}", feeAddress);
        DecisionServiceSEStandardPricingFeeFlow_Service service = null;
        DecisionServiceSEStandardPricingFeeFlow port = null;
        DecisionServiceResponse response = null;
        try {
            String requestFile = "/home/slosdev/clevel/brms/standardPricingFeeRulesRequest.xml";
            String responseFile = "/home/slosdev/clevel/brms/standardPricingFeeRulesResponse.xml";
            JAXBContext requestContext = JAXBContext.newInstance(DecisionServiceRequest.class);
            Marshaller mRequest = requestContext.createMarshaller();
            mRequest.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            mRequest.marshal(request, new FileWriter(requestFile, true));

            URL wsdlUrl = new URL(feeAddress + this.WSDL);
            QName qname = new QName(brmsServiceURL, feeServiceName);

            service = new DecisionServiceSEStandardPricingFeeFlow_Service(wsdlUrl, qname);
            port = service.getDecisionServiceSOAPstmbrmsred1();
            int timeout = 60000;
            try{
                timeout=Integer.parseInt(brmsRequestTimeout)*1000;
            }catch (Exception e){
                logger.debug("request Service request_timeout must be a number! {Default : 60sec}");
            }
            ((BindingProvider) port).getRequestContext().put(BindingProviderProperties.REQUEST_TIMEOUT,timeout);
            ((BindingProvider) port).getRequestContext().put(BindingProviderProperties.CONNECT_TIMEOUT,timeout);
            ((BindingProvider) port).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, feeAddress);
            logger.debug("callStandardPricingFeeRulesService() Calling...{}", request);
            response = port.executeDecisionService(request);

            JAXBContext responseContext = JAXBContext.newInstance(DecisionServiceResponse.class);
            Marshaller mResponse = responseContext.createMarshaller();
            mResponse.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            mResponse.marshal(response, new FileWriter(responseFile, true));

            logger.debug("callStandardPricingFeeRulesService() Done... {}", response);
            return response;
        } catch (Exception e) {
            logger.error("callStandardPricingFeeRulesService() Error : {}", e);
            throw e;
        }
    }

    public DecisionServiceResponse callDocumentCustomerRulesService(DecisionServiceRequest request) throws Exception {
        logger.debug("-- begin Sending Request to callDocumentCustomerRulesService()");
        logger.debug("Service URL : {}", brmsServiceURL);
        logger.debug("Service Name : {}", customerServiceName);
        logger.debug("Service Address : {}", customerAddress);
        DecisionServiceSEDocumentCustomerFlow_Service service = null;
        DecisionServiceSEDocumentCustomerFlow port = null;
        DecisionServiceResponse response = null;
        try {
            String requestFile = "/home/slosdev/clevel/brms/documentCustomerRulesRequest.xml";
            String responseFile = "/home/slosdev/clevel/brms/documentCustomerRulesResponse.xml";
            JAXBContext requestContext = JAXBContext.newInstance(DecisionServiceRequest.class);
            Marshaller mRequest = requestContext.createMarshaller();
            mRequest.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            mRequest.marshal(request, new FileWriter(requestFile, true));

            URL wsdlUrl = new URL(customerAddress + this.WSDL);
            QName qname = new QName(brmsServiceURL, customerServiceName);
            service = new DecisionServiceSEDocumentCustomerFlow_Service(wsdlUrl, qname);
            port = service.getDecisionServiceSOAPstmbrmsred1();
            int timeout = 60000;
            try{
                timeout=Integer.parseInt(brmsRequestTimeout)*1000;
            }catch (Exception e){
                logger.debug("request Service request_timeout must be a number! {Default : 60sec}");
            }
            ((BindingProvider) port).getRequestContext().put(BindingProviderProperties.REQUEST_TIMEOUT,timeout);
            ((BindingProvider) port).getRequestContext().put(BindingProviderProperties.CONNECT_TIMEOUT,timeout);
            ((BindingProvider) port).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, customerAddress);
            logger.debug("callDocumentCustomerRulesService() Calling...{}", request);
            response = port.executeDecisionService(request);

            JAXBContext responseContext = JAXBContext.newInstance(DecisionServiceResponse.class);
            Marshaller mResponse = responseContext.createMarshaller();
            mResponse.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            mResponse.marshal(response, new FileWriter(responseFile, true));

            logger.debug("callStandardPricingFeeRulesService() Done... {}", response);
            return response;
        } catch (Exception e) {
            logger.error("callDocumentCustomerRulesService() Error : {}", e);
            throw e;
        }
    }

    public DecisionServiceResponse callDocumentAppraisalRulesService(DecisionServiceRequest request) throws Exception {
        logger.debug("-- begin Sending Request to callDocumentAppraisalRulesService()");
        logger.debug("Service URL : {}", brmsServiceURL);
        logger.debug("Service Name : {}", appraisalServiceName);
        logger.debug("Service Address : {}", appraisalAddress);
        DecisionServiceSEDocumentAppraisalFlow_Service service = null;
        DecisionServiceSEDocumentAppraisalFlow port = null;
        DecisionServiceResponse response = null;
        try {
            String requestFile = "/home/slosdev/clevel/brms/documentAppraisalRulesRequest.xml";
            String responseFile = "/home/slosdev/clevel/brms/documentAppraisalRulesResponse.xml";
            JAXBContext requestContext = JAXBContext.newInstance(DecisionServiceRequest.class);
            Marshaller mRequest = requestContext.createMarshaller();
            mRequest.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            mRequest.marshal(request, new FileWriter(requestFile, true));

            URL wsdlUrl = new URL(appraisalAddress + this.WSDL);
            QName qname = new QName(brmsServiceURL, appraisalServiceName);
            service = new DecisionServiceSEDocumentAppraisalFlow_Service(wsdlUrl, qname);
            port = service.getDecisionServiceSOAPstmbrmsred1();
            int timeout = 60000;
            try{
                timeout=Integer.parseInt(brmsRequestTimeout)*1000;
            }catch (Exception e){
                logger.debug("request Service request_timeout must be a number! {Default : 60sec}");
            }
            ((BindingProvider) port).getRequestContext().put(BindingProviderProperties.REQUEST_TIMEOUT,timeout);
            ((BindingProvider) port).getRequestContext().put(BindingProviderProperties.CONNECT_TIMEOUT,timeout);
            ((BindingProvider) port).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, appraisalAddress);
            logger.debug("callDocumentAppraisalRulesService() Calling...{}", request);
            response = port.executeDecisionService(request);

            JAXBContext responseContext = JAXBContext.newInstance(DecisionServiceResponse.class);
            Marshaller mResponse = responseContext.createMarshaller();
            mResponse.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            mResponse.marshal(response, new FileWriter(responseFile, true));

            logger.debug("callStandardPricingFeeRulesService() Done... {}", response);
            return response;
        } catch (Exception e) {
            logger.error("callDocumentAppraisalRulesService() Error : {}", e);
            throw e;
        }
    }
}
