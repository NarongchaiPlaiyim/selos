package com.clevel.selos.businesscontrol;

import com.clevel.selos.dao.master.UserDAO;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.security.UserDetail;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.inject.Inject;
import java.io.Serializable;
import java.math.BigDecimal;

public abstract class BusinessControl implements Serializable {
    @Inject
    protected UserDAO userDAO;

    protected String getCurrentUserID() {
        UserDetail userDetail = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (userDetail != null)
            return userDetail.getUserName();
        return null;
    }

    protected User getCurrentUser() {
        try {
            return userDAO.findById(getCurrentUserID());
        } catch (Exception ex) {
            return null;
        }
    }

    protected BigDecimal getMinBigDecimal(BigDecimal value1, BigDecimal value2){
        if(value1 != null && value2 != null){
            if(value1.compareTo(value2) < 0){
                return value1;
            } else {
                return value2;
            }
        } else {
            return null;
        }
    }

    protected BigDecimal getMinBigDecimal(BigDecimal value1, BigDecimal value2, BigDecimal value3){
        if(value1 != null && value2 != null && value3 != null){
            if(value1.compareTo(value2) < 0){
                if(value1.compareTo(value3) < 0){
                    return value1;
                } else {
                    return value3;
                }
            } else {
                if(value2.compareTo(value3) < 0){
                    return value2;
                } else {
                    return value3;
                }
            }
        } else {
            return null;
        }
    }
}
