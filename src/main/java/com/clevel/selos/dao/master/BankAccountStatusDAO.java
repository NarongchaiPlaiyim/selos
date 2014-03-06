package com.clevel.selos.dao.master;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.BankAccountStatus;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.List;

public class BankAccountStatusDAO extends GenericDAO<BankAccountStatus, Integer> {
    @Inject
    @SELOS
    Logger log;
    @Inject
    BankAccountStatusDAO(){
    }

    public BankAccountStatus findByCodeAndDataSource(String code, String dataSource){
        if(code!=null && dataSource!=null){
            log.info("findByCodeAndType. (code: {}, dataSource: {})", code.trim(), dataSource);
            Criteria criteria = createCriteria();
            criteria.add(Restrictions.eq("code", code.trim()));
            criteria.add(Restrictions.eq("dataSource", dataSource));
            BankAccountStatus bankAccountStatus = (BankAccountStatus)criteria.uniqueResult();

            log.info("findByCodeAndType. bankAccountStatus : {}", bankAccountStatus);

            return bankAccountStatus;
        }
        return null;
    }

    public List<BankAccountStatus> findAllExceptDataSource(List<String> dataSources){
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.not(Restrictions.in("dataSource",dataSources)));
        List<BankAccountStatus> bankAccountStatusList =  criteria.list();
        return bankAccountStatusList;
    }

}
