package com.clevel.selos.model;

public enum PotentialCollateralValue {
    CASH_COLLATERAL(1),
    CORE_ASSET(2),
    NONE_CORE_ASSET(3);

    int id;

    PotentialCollateralValue(int id) {
        this.id = id;
    }

    public int id() {
        return this.id;
    }
}
