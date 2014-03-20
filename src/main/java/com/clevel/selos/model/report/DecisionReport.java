package com.clevel.selos.model.report;

import com.clevel.selos.model.view.ExistingCreditTierDetailView;
import com.clevel.selos.model.view.ExistingSplitLineDetailView;
import com.clevel.selos.report.ReportModel;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;


public class DecisionReport extends ReportModel{

    private BigDecimal extBorrowerTotalComLimit;
    private BigDecimal extBorrowerTotalRetailLimit;
    private BigDecimal extBorrowerTotalAppInRLOSLimit;
    private BigDecimal extBorrowerTotalCommercial;
    private BigDecimal extBorrowerTotalComAndOBOD;
    private BigDecimal extBorrowerTotalExposure;
    private BigDecimal extRelatedTotalComLimit;
    private BigDecimal extRelatedTotalRetailLimit;
    private BigDecimal extRelatedTotalAppInRLOSLimit;
    private BigDecimal extRelatedTotalCommercial;
    private BigDecimal extRelatedTotalComAndOBOD;
    private BigDecimal extRelatedTotalExposure;
    private BigDecimal extGroupTotalCommercial;
    private BigDecimal extGroupTotalComAndOBOD;
    private BigDecimal extGroupTotalExposure;
    private BigDecimal extBorrowerTotalAppraisalValue;
    private BigDecimal extBorrowerTotalMortgageValue;
    private BigDecimal extRelatedTotalAppraisalValue;
    private BigDecimal extRelatedTotalMortgageValue;

    //extGuarantor
    private BigDecimal exttotalLimitGuaranteeAmount;

    private BigDecimal extTotalGuaranteeAmount;
    private BigDecimal proposeTotalCreditLimit;

    private BigDecimal approveTotalCreditLimit;
    private BigDecimal approveBrwTotalCommercial;
    private BigDecimal approveBrwTotalComAndOBOD;
    private BigDecimal approveTotalExposure;

    //guarantor
    private BigDecimal totalLimitGuaranteeAmount;



    public DecisionReport() {
    }
}
