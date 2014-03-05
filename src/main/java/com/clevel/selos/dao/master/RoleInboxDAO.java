package com.clevel.selos.dao.master;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.Action;
import com.clevel.selos.model.db.master.InboxType;
import com.clevel.selos.model.db.master.Role;
import com.clevel.selos.model.db.relation.RelRoleBasedInbox;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Prathyusha
 * Date: 2/26/14
 * Time: 3:31 PM
 * To change this template use File | Settings | File Templates.
 */
public class RoleInboxDAO extends GenericDAO<InboxType, Long> {

    @Inject
    @SELOS
    Logger log;

    @Inject
    Role role ;

    Iterator iterator = null;

    @Inject
    InboxType inboxType;

    List inboxList = new ArrayList();
    List<InboxType> inboxTypeList = new ArrayList<InboxType>();
    List<RelRoleBasedInbox> roleBasedInboxList = new ArrayList<RelRoleBasedInbox>();
//    List<Integer> inboxList = new ArrayList<Integer>();
    List<String> stringInboxList = new ArrayList<String>();
    int inboxRoleId;
    long inboxId;
    String inboxName = null;
    String modifiedInboxName = null;
    String stringOfInboxValues = null;
    long inboxValues ;

    public List<String> getUserBasedRole(int inboxRoleId,RelRoleBasedInbox relRoleBasedInbox,InboxType inboxType){

        Criteria cr = getSession().createCriteria(RelRoleBasedInbox.class)
                .setProjection(Projections.projectionList()

                .add(Projections.property("inboxId"), "inboxId")) .add(Restrictions.eq("roleId",inboxRoleId))

                .setResultTransformer(Transformers.aliasToBean(RelRoleBasedInbox.class));

        List userList = cr.list();
        log.info("userList .... {}", userList);

        Iterator iterator = userList.iterator();

        while(iterator.hasNext() == true)
        {
            RelRoleBasedInbox inboxTableName = new RelRoleBasedInbox();

            inboxTableName = (RelRoleBasedInbox)iterator.next();

            inboxRoleId = inboxTableName.getInboxId();
            inboxList.add(inboxRoleId);
        }
        Criteria criteria2 = getSession().createCriteria(InboxType.class);

        criteria2.setProjection(Projections.projectionList().add(Projections.property("inbox_name"), "inbox_name")).add(Restrictions.in("id", inboxList)).setResultTransformer(Transformers.aliasToBean(InboxType.class));
        inboxTypeList = criteria2.list();

        iterator = inboxTypeList.iterator();

        while (iterator.hasNext() == true)
        {
           InboxType inboxType1 = new InboxType();
            inboxType1 = (InboxType) iterator.next();
            inboxName = inboxType1.getInbox_name();
          //  modifiedInboxName = "'" + inboxName + "'";
            log.info("inbox Name ..{}",inboxName);
            stringInboxList.add(inboxName);
        }
        log.info("inboxTypeList .... {}", stringInboxList);

        return stringInboxList;


    }

}
