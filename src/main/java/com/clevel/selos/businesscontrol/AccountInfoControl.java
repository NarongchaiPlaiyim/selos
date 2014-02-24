package com.clevel.selos.businesscontrol;

import java.io.Serializable;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.slf4j.Logger;

import com.clevel.selos.dao.working.OpenAccountCreditDAO;
import com.clevel.selos.dao.working.OpenAccountDAO;
import com.clevel.selos.dao.working.OpenAccountNameDAO;
import com.clevel.selos.dao.working.OpenAccountPurposeDAO;
import com.clevel.selos.dao.working.WorkCaseDAO;
import com.clevel.selos.integration.SELOS;

@Stateless
public class AccountInfoControl extends BusinessControl implements Serializable {
    private static final long serialVersionUID = 7094364991471157346L;

	@Inject @SELOS
    private Logger log;
    
    @Inject private WorkCaseDAO workCaseDAO;
    @Inject private OpenAccountDAO openAccountDAO;
    @Inject private OpenAccountNameDAO openAccountNameDAO;
    @Inject private OpenAccountCreditDAO openAccountCreditDAO;
    @Inject private OpenAccountPurposeDAO openAccPurposeDAO;
    
}
