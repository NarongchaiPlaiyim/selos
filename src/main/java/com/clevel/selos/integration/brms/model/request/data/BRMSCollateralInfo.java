package com.clevel.selos.integration.brms.model.request.data;

import java.io.Serializable;
import java.math.BigDecimal;

public class BRMSCollateralInfo implements Serializable{
    private String collateralType;
    private String subCollateralType;
    private boolean appraisalFlag;
    private String aadDecision;
    private BigDecimal percentOwnerBuildingAppr;
    private BigDecimal numberOfMonthsApprDate;
    private String collId;

}
