package com.clevel.selos.dao.working;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.working.AccountInfoDetailPurpose;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.List;

public class AccountInfoDetailPurposeDAO extends GenericDAO<AccountInfoDetailPurpose, Long> {
    @Inject
    @SELOS
    Logger log;
    @Inject
    public AccountInfoDetailPurposeDAO() {

    }

    public List<AccountInfoDetailPurpose> findByAccountInfoDetailPurposeId(long accountInfoDetailPurposeId) {
        log.info("findByAccountInfoDetailPurposeId : {}", accountInfoDetailPurposeId);
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("accountInfoDetailPurpose.id", accountInfoDetailPurposeId));
        List<AccountInfoDetailPurpose> purposeList = criteria.list();

        return purposeList;
    }
}
