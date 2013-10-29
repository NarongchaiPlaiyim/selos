package com.clevel.selos.system.audit;

import com.clevel.selos.model.ActionResult;

import java.util.Date;

public interface SystemAuditor {
    public void add(String userId, String action, String actionDesc, Date actionDate, ActionResult actionResult, String resultDesc, Date resultDate, String linkKey);

    public void add(String userId, String action, String actionDesc, Date actionDate, ActionResult actionResult, String resultDesc, String linkKey);
}
