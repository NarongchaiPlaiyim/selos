package com.clevel.selos.report;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public abstract class ReportModel implements Serializable {
    protected static final String DEFAULT_STRING = "-";
    protected static final int DEFAULT_INTEGER = 0;
    protected static final BigDecimal DEFAULT_BIG_DECIMAL = new BigDecimal("0.00");
    protected static final Date DEFAULT_DATE = new Date();
    protected static final boolean DEFAULT_BOOLEAN = false;

    protected String getDefaultString() {
        return DEFAULT_STRING;
    }

    protected int getDefaultInteger() {
        return DEFAULT_INTEGER;
    }

    protected BigDecimal getDefaultBigDecimal() {
        return DEFAULT_BIG_DECIMAL;
    }

    protected Date getDefaultDate() {
        return DEFAULT_DATE;
    }

    protected boolean getDefaultBoolean() {
        return DEFAULT_BOOLEAN;
    }
}
