package com.clevel.selos.dao.master;


import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.slf4j.Logger;
import com.clevel.selos.model.db.master.QueueNameId;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class QueueNameIdDAO   extends GenericDAO<QueueNameId,Integer>
{
    @Inject
    @SELOS
    Logger log;



    public QueueNameIdDAO()
    {

    }


    public List<QueueNameId> getQueueNameIdFilterCondition(int inboxnameid, int roleid)
    {

        log.info("controler comes to getQueueNameIdFilterCondition method ");

        List<QueueNameId> queuenameidfilterconditionlist = new ArrayList<QueueNameId>();

        Criteria criteria = getSession().createCriteria(QueueNameId.class);

        criteria.setProjection( Projections.projectionList().add(Projections.property("queueid"),"queueid").add(Projections.property("filtercondition"), "filtercondition"));

        criteria.add(Restrictions.eq("inboxid", inboxnameid));

        criteria.add(Restrictions.eq("roleid", roleid)).setResultTransformer(Transformers.aliasToBean(QueueNameId.class));

        queuenameidfilterconditionlist = criteria.list();

        log.info("queue id size is : {}",queuenameidfilterconditionlist.size());

        Iterator iterator = queuenameidfilterconditionlist.iterator();

        int queueid;
        String filterstring;

        while(iterator.hasNext() == true)
        {
            QueueNameId queueNameId = new QueueNameId();

            queueNameId = (QueueNameId)iterator.next();

            queueid = queueNameId.getQueueid();

            filterstring = queueNameId.getFiltercondition();

            log.info("queue id obtained is ::::::::::{}",queueid);

            log.info("filterstring is :::::::::::::: {}",filterstring);
        }





        return queuenameidfilterconditionlist;

    }

}
