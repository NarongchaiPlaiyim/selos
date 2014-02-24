package com.clevel.selos.integration.brms.model.request;

import java.io.Serializable;
import java.math.BigDecimal;

public class BRMSTMBAccountInfo implements Serializable{

    private String adjustClass;
    private boolean activeFlag;
    private String dataSource;
    private String accountRef;
    private String custToAccountRelationCD;
    private boolean tmbTDRFlag;
    private BigDecimal deletePrinIntMonth;
    private BigDecimal tmbMonthClass;
    private BigDecimal tmbDelPriDay;
    private BigDecimal tmbDelIntDay;
    private String tmbBlockCode;


}
