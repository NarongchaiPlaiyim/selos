package com.clevel.selos.model.view;

import java.io.Serializable;
import java.math.BigDecimal;

public class ExistingCollateral implements Serializable {
    private long id;
    private String typeOfCollateral;
    private BigDecimal value;
    private BigDecimal pledge;
}
