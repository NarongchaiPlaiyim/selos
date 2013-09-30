package com.clevel.selos.integration.ncb.exportncbi;

import com.clevel.selos.dao.export.NCBIExportDAO;
import com.clevel.selos.integration.NCB;
import com.clevel.selos.model.db.export.NCBIExport;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.Date;

@Stateless
@NCB
public class NCBIExportImp implements Serializable {
    @Inject
    Logger log;

    @Inject
    NCBIExportDAO ncbiExportDAO;

    @Inject
    public NCBIExportImp() {
    }

    public void add(NCBIExportModel exportModel){

        String staffId =                exportModel.getStaffId();
        String requestNo =              exportModel.getRequestNo();
        String inquiryType =            exportModel.getInquiryType();
        String customerType =           exportModel.getCustomerType();
        String customerDocumentType =   exportModel.getCustomerDocumentType();
        String juristicType =           exportModel.getJuristicType();
        String customerId =             exportModel.getCustomerId();
        String countryCode =            exportModel.getCountryCode();
        String titleCode  =             exportModel.getTitleCode();
        String firstName =              exportModel.getFirstName();
        String lastName =               exportModel.getLastName();
        String juristicName =           exportModel.getJuristicName();
        String caNumber =               exportModel.getCaNumber();
        String caution =                exportModel.getCaution();
        String referenceTel =           exportModel.getReferenceTel();
        String inquiryStatus =          exportModel.getInquiryStatus();
        Date inquiryDate  =             exportModel.getInquiryDate();
        String officeCode =             exportModel.getOfficeCode();

        ncbiExportDAO.persist(new NCBIExport(
                staffId, requestNo, inquiryType, customerType, customerDocumentType,
                juristicType, customerId, countryCode, titleCode, firstName,
                lastName, juristicName, caNumber, caution, referenceTel,
                inquiryStatus, inquiryDate, officeCode
                ));
    }
}
