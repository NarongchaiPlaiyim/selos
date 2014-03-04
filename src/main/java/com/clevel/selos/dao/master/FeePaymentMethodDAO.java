package com.clevel.selos.dao.master;

import javax.inject.Inject;

import org.slf4j.Logger;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.FeePaymentMethod;

public class FeePaymentMethodDAO extends GenericDAO<FeePaymentMethod,Integer> {
	private static final long serialVersionUID = -9092627334977116360L;
	@Inject @SELOS
    private Logger log;
}
