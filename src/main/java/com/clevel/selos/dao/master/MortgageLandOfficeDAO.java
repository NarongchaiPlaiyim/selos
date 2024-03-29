package com.clevel.selos.dao.master;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.MortgageLandOffice;
import org.slf4j.Logger;

import javax.inject.Inject;

public class MortgageLandOfficeDAO extends GenericDAO<MortgageLandOffice, Integer> {
	
	private static final long serialVersionUID = -7476597515734086699L;
	@Inject @SELOS
	private Logger logger;
	
	public MortgageLandOfficeDAO() {
	}

}
