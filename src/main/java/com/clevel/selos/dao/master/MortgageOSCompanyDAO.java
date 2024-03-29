package com.clevel.selos.dao.master;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.MortgageOSCompany;
import org.slf4j.Logger;

import javax.inject.Inject;

public class MortgageOSCompanyDAO extends GenericDAO<MortgageOSCompany, Integer> {
	private static final long serialVersionUID = 427467999924515202L;
	
	@Inject
    @SELOS
    private Logger log;
    
    public MortgageOSCompanyDAO() {
    }
}
