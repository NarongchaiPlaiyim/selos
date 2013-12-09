package com.clevel.selos.businesscontrol;

import com.clevel.selos.controller.Decision;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.User;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;

@Stateless
public class DecisionControl extends BusinessControl {
    @Inject
    @SELOS
    private Logger log;

    //DAO

    //Transform

    public void saveDecision() {
        // todo: saveDecision()
    }

    public User getCurrentUser() {
        return getCurrentUser();
    }
}
