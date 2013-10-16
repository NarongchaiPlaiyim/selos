package com.clevel.selos.businesscontrol;

import com.clevel.selos.dao.working.DBRDAO;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;

@Stateless
public class DBRControl {
    @Inject
    Logger log;

    @Inject
    DBRDAO dbrdao;

    public DBRControl(){

    }





}
