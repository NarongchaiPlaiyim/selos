package com.clevel.selos.dao.master;


import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.InboxTableName;
import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.Iterator;
import java.util.List;


public class InboxTableNameDAO extends GenericDAO<InboxTableName,Integer>
{
    @Inject
    @SELOS
    Logger log;

    List<InboxTableName> inboxnamelist = null;

    int inboxid;

    public InboxTableNameDAO()
    {

    }

    public int getInboxId(String inboxname)
    {
        Criteria criteria = getSession().createCriteria(InboxTableName.class).setProjection(Projections.projectionList().add(Projections.property("id"),"id") )
                .add(Restrictions.eq("inboxname",inboxname)).setResultTransformer(Transformers.aliasToBean(InboxTableName.class));

        inboxnamelist = criteria.list();

        Iterator iterator = inboxnamelist.iterator();

        while(iterator.hasNext() == true)
        {
            InboxTableName inboxTableName = new InboxTableName();

            inboxTableName = (InboxTableName)iterator.next();

            inboxid = inboxTableName.getId();


        }
        return inboxid;

    }
}
