package com.clevel.selos.dao.master;


import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.DoaPriorityUserNames;
import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.transform.Transformers;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DoaPriorityUserNamesDAO extends GenericDAO<DoaPriorityUserNames, String>
{
    @Inject
    @SELOS
    Logger log;

    public DoaPriorityUserNamesDAO()
    {

    }

    public List<DoaPriorityUserNames> getDoaPriorityUserNames()
    {
        List<DoaPriorityUserNames> doaPriorityUserNames;

        Criteria criteria = createCriteria();

        criteria.setProjection(Projections.projectionList().add(Projections.property("userid"),"userid"));
        criteria.setResultTransformer(Transformers.aliasToBean(DoaPriorityUserNames.class));

        doaPriorityUserNames = criteria.list();

        log.debug("doaPriorityUserNames list size is : {}", doaPriorityUserNames.size());

        return doaPriorityUserNames;
    }

}
