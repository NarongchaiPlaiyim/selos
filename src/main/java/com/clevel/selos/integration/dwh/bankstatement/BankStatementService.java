package com.clevel.selos.integration.dwh.bankstatement;

import com.clevel.selos.dao.system.SystemParameterDAO;
import com.clevel.selos.integration.DWH;
import com.clevel.selos.integration.dwh.obligation.model.Obligation;
import com.clevel.selos.system.Config;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.io.Serializable;

public class BankStatementService implements Serializable {
    @Inject
    @DWH
    Logger log;

    @Inject
    @Config(name = "interface.dwh.obligation.table.sysparam")
    String sysParam;

    @Inject
    SystemParameterDAO systemParameterDAO;

    @Inject
    public BankStatementService() {

    }


}
