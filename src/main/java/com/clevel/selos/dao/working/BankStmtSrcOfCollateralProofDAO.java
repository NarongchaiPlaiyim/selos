package com.clevel.selos.dao.working;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.model.db.working.BankStmtSrcOfCollateralProof;
import org.slf4j.Logger;

import javax.inject.Inject;

public class BankStmtSrcOfCollateralProofDAO extends GenericDAO<BankStmtSrcOfCollateralProof, Long> {
    @Inject
    private Logger log;

    @Inject
    public BankStmtSrcOfCollateralProofDAO() {

    }
}
