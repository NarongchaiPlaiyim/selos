package com.clevel.selos.dao.working;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.model.db.working.OpenAccount;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.List;

public class OpenAccountDAO extends GenericDAO<OpenAccount, Long> {
    @Inject
    private Logger log;

    @Inject
    public OpenAccountDAO(){
    }

    public List<OpenAccount> findByBasicInfoId(long basicInfoId){
        log.info("findByBasicInfoId : {}", basicInfoId);
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("basicInfo.id", basicInfoId));
        List<OpenAccount> openAccountList = criteria.list();

        return openAccountList;
    }
}
