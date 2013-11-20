package com.clevel.selos.model.view;

import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.PotentialCollateral;
import com.clevel.selos.system.message.Message;
import com.clevel.selos.system.message.NormalMessage;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.io.Serializable;

public class PrescreenCollateralView implements Serializable {
    @Inject
    @SELOS
    Logger log;
    @Inject
    @NormalMessage
    Message msg;

    private long id;
    private PotentialCollateral potentialCollateral;

    public PrescreenCollateralView() {
    }

    public void reset(){
        this.id = 0;
        this.potentialCollateral = new PotentialCollateral();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public PotentialCollateral getPotentialCollateral() {
        return potentialCollateral;
    }

    public void setPotentialCollateral(PotentialCollateral potentialCollateral) {
        this.potentialCollateral = potentialCollateral;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("msg", msg)
                .append("id", id)
                .append("potentialCollateral", potentialCollateral)
                .toString();
    }
}
