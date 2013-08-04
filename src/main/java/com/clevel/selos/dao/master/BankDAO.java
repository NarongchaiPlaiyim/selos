package com.clevel.selos.dao.master;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.model.db.master.Bank;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;

@Stateless
public class BankDAO extends GenericDAO<Bank,Integer> {
    @Inject
    private Logger log;

    @Inject
    public BankDAO() {
    }

    public List<Bank> getListExcludeTMB() {
        //todo: change later
        return findAll();
    }

}
