package com.clevel.selos.transform;

import com.clevel.selos.integration.NCB;
import com.clevel.selos.model.db.working.MandateDocCust;
import org.slf4j.Logger;

import javax.inject.Inject;

public class CheckMandateDocCustTransform extends Transform {
    @Inject
    @NCB
    private Logger log;
    private MandateDocCust mandateDocCust;
    @Inject
    public CheckMandateDocCustTransform() {

    }
}
