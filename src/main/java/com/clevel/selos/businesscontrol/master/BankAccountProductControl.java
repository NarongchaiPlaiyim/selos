package com.clevel.selos.businesscontrol.master;

import com.clevel.selos.businesscontrol.BusinessControl;
import com.clevel.selos.dao.master.BankAccountProductDAO;

import javax.inject.Inject;

public class BankAccountProductControl extends BusinessControl{
    @Inject
    private ApplicationCacheLoader cacheLoader;
    @Inject
    private BankAccountProductDAO bankAccountProductDAO;





}
