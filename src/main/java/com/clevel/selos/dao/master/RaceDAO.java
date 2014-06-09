package com.clevel.selos.dao.master;


import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.Race;
import org.slf4j.Logger;

import javax.inject.Inject;

public class RaceDAO extends GenericDAO<Race, Integer> {

    private static final long serialVersionUID = -6839989464764992717L;
	@Inject
    @SELOS
    Logger log;
  
	public RaceDAO() {
    }
    
   
}
