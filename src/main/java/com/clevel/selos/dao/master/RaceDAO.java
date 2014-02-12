package com.clevel.selos.dao.master;


import javax.inject.Inject;

import org.slf4j.Logger;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.Race;

public class RaceDAO extends GenericDAO<Race, Integer> {

    private static final long serialVersionUID = -6839989464764992717L;
	@Inject
    @SELOS
    Logger log;
  
	public RaceDAO() {
    }
    
   
}
