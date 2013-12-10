package com.clevel.selos.businesscontrol;

import com.clevel.selos.controller.Decision;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.RoleUser;
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

    public User getApprover() {
        return getCurrentUser();
    }

    public boolean isRoleUW() {
        User currentUser = getCurrentUser();
        if (RoleUser.UW.getValue() == currentUser.getRole().getId())
            return true;
        else
            return false;
    }
}
