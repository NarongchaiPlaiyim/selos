package com.clevel.selos.integration.ncbi.service;

import com.clevel.selos.integration.NCB;
import com.clevel.selos.integration.ncb.exportncbi.NCBIExportImp;
import com.clevel.selos.integration.ncbi.model.NcbiRequestModel;
import com.clevel.selos.integration.ncbi.model.RequestModel;
import com.clevel.selos.model.db.export.NCBIExport;
import com.thoughtworks.xstream.XStream;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class NCBIImp implements NCBI, Serializable{
    @Inject
    @NCB
    Logger log;

    @Inject
    @NCB
    NCBIExportImp exportImp;

    @Inject
    public NCBIImp() {
    }

    private String nameFile = null;
    private final String path = "D:\\";
    private final String xmlFile = ".xml";
    @Override
    public ArrayList<String> XMLFormatRequestTransaction(List<NCBIExport> ncbiExportList) throws Exception {
        NcbiRequestModel requestModel = null;
        ArrayList<String> path = new ArrayList<String>();
        ArrayList<RequestModel> modelArrayList = new ArrayList<RequestModel>();

        for(NCBIExport ncbiExport : ncbiExportList){
            nameFile = ncbiExport.getRequestNo();
            if("01".equals(ncbiExport.getCustomerType())){
                path.add(generateXML(setNCRSRequest(ncbiExport)));
            } else {
                path.add(generateXML(setNCCRSRequest(ncbiExport)));
            }
        }
        return path;
    }

    private RequestModel setNCRSRequest(NCBIExport ncbiExport){
        log.debug("NCBI call setNCRSRequest()");
        log.debug("NCBI NCBIExport : {}", ncbiExport.toString());
        String staffId =              ncbiExport.getStaffId();
        String requestNo =            ncbiExport.getRequestNo();
        String inquiryType =          ncbiExport.getInquiryType();
        String customerType =         ncbiExport.getCustomerType();
        String customerDocumentType = ncbiExport.getCustomerDocumentType();
        String customerId =           ncbiExport.getCustomerId();
        String countryCode =          ncbiExport.getCountryCode();
        String titleCode =            ncbiExport.getTitleCode();
        String firstName =            ncbiExport.getFirstName();
        String lastName =             ncbiExport.getLastName();
        String caNumber =             ncbiExport.getCaNumber();
        String caution =              ncbiExport.getCaution();
        String referenceTel =         ncbiExport.getReferenceTel();
        String inquiryStatus =        ncbiExport.getInquiryStatus();
        String inquiryDate =          ncbiExport.getInquiryDate();
        String inquiryTime =          ncbiExport.getInquiryTime();
        String officeCode =           ncbiExport.getOfficeCode();

        return new RequestModel(
                staffId,
                requestNo,
                inquiryType,
                customerType,
                customerDocumentType,
                customerId,
                countryCode,
                titleCode,
                firstName,
                lastName,
                caNumber,
                caution,
                referenceTel,
                inquiryStatus,
                inquiryDate,
                inquiryTime,
                officeCode);
    }
    private RequestModel setNCCRSRequest(NCBIExport ncbiExport){
        log.debug("NCBI call setNCCRSRequest()");
        log.debug("NCBI NCBIExport : {}", ncbiExport.toString());
        String staffId =              ncbiExport.getStaffId();
        String requestNo =            ncbiExport.getRequestNo();
        String inquiryType =          ncbiExport.getInquiryType();
        String customerType =         ncbiExport.getCustomerType();
        String customerDocumentType = ncbiExport.getCustomerDocumentType();
        String juristicType =         ncbiExport.getJuristicType();
        String customerId =           ncbiExport.getCustomerId();
        String countryCode =          ncbiExport.getCountryCode();
        String juristicName =         ncbiExport.getJuristicName();
        String caNumber =             ncbiExport.getCaNumber();
        String caution =              ncbiExport.getCaution();
        String referenceTel =         ncbiExport.getReferenceTel();
        String inquiryStatus =        ncbiExport.getInquiryStatus();
        String inquiryDate =          ncbiExport.getInquiryDate();
        String inquiryTime =          ncbiExport.getInquiryTime();
        String officeCode =           ncbiExport.getOfficeCode();

        return new RequestModel(
                staffId,
                requestNo,
                inquiryType,
                customerType,
                customerDocumentType,
                juristicType,
                customerId,
                countryCode,
                juristicName,
                caNumber,
                caution,
                referenceTel,
                inquiryStatus,
                inquiryDate,
                inquiryTime,
                officeCode);
    }
    private String generateXML(RequestModel requestModel){
        log.debug("NCRS Call : generateXML()");
        XStream xStream = null;
        String xml = null;
        FileWriter fstream = null;
        BufferedWriter out = null;
        NcbiRequestModel ncbiRequestModel = new NcbiRequestModel(requestModel);
        String path = this.path+"\\"+nameFile+xmlFile;
        try {
            xStream = new XStream();
            xStream.processAnnotations(NcbiRequestModel.class);
            xml = xStream.toXML(ncbiRequestModel);
            fstream = new FileWriter(path);
            out = new BufferedWriter(fstream);
            out.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            System.out.println(xml.replace("__", "_"));
            out.write(xml.replace("__", "_"));
        } catch (IOException e) {
            log.error("NCBI IOException : {}", e.getMessage());
        } finally {
            try {
                if(null!=out){
                    out.close();
                }
            } catch (IOException e) {
                log.debug("NCBI IOException : {}", e.getMessage());
            }
        }
        return path;
    }

}
