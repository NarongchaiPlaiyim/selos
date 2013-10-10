package com.clevel.selos.model.view;

import java.io.Serializable;
import java.math.BigDecimal;

public class ExistingCreditView implements Serializable {
    private long id;
    private String customerName;
    private String productProgram;
    private String creditTypeName;
    private BigDecimal limit;
    private BigDecimal outstanding;
    private int tenor;
}
