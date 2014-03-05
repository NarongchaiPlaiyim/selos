package com.clevel.selos.dao.master;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.Action;
import com.clevel.selos.model.db.master.Relretunactions;
import com.clevel.selos.model.db.master.RosterTableName;
import com.clevel.selos.model.db.master.User;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ActionDAO extends GenericDAO<Action, Long> {
    @Inject
    @SELOS
    Logger log;
    List<Long> actionList = new ArrayList<Long>();
    List<Action> decriptionList = new ArrayList<Action>();
    List<String> stringDiscriptionList = new ArrayList<String>();
    String stringOfDescriptionValues = null;
    String descriptionValues = null;
    String rosterName = null;
    Relretunactions relretunactions1 = null;
    long actionCode;
    RosterTableName rosterTableName = null;
    Iterator iterator = null;
    List<RosterTableName> rosterTableNames = null;
    Action action1 = null;
    String description = null;
    String modifiedDescription = null;
    @Inject
    public ActionDAO() {
    }

    public String getDescripationFromAction(Relretunactions relretunactions, Action action)
    {
        relretunactions = new Relretunactions();
        action = new Action();
        Criteria cr = getSession().createCriteria(Relretunactions.class)
                .setProjection(Projections.projectionList()

                        .add(Projections.property("actionCode"), "actionCode"))
                .setResultTransformer(Transformers.aliasToBean(Relretunactions.class));

        List<Relretunactions> list = cr.list();
        iterator = list.iterator();
        while (iterator.hasNext() == true)
        {
            relretunactions1 = new Relretunactions();
            relretunactions1 = (Relretunactions) iterator.next();
            actionCode = relretunactions1.getActionCode();
            actionList.add(actionCode);

            relretunactions1 = null;

        }
        log.info("actionCode list :::" + actionList);
        Criteria criteria2 = getSession().createCriteria(Action.class);

        criteria2.setProjection(Projections.projectionList().add(Projections.property("description"), "description")).add(Restrictions.in("id", actionList)).setResultTransformer(Transformers.aliasToBean(Action.class));
        decriptionList = criteria2.list();
        iterator = decriptionList.iterator();

        while (iterator.hasNext() == true)
        {
            action1 = new Action();
            action1 = (Action) iterator.next();
            description = action1.getDescription();
            modifiedDescription = "'" + description + "'";
            stringDiscriptionList.add(modifiedDescription);
            action1 = null;
        }
        stringOfDescriptionValues = stringDiscriptionList.toString();
        descriptionValues = stringOfDescriptionValues.replace("[", "").replace("]", "");
        return descriptionValues;
    }

    public String getRosterTableName()
    {
        try {


            rosterTableNames = new ArrayList<RosterTableName>();
            Criteria cr = getSession().createCriteria(RosterTableName.class)
                    .setProjection(Projections.projectionList()

                            .add(Projections.property("rosterName"), "rosterName"))
                    .setResultTransformer(Transformers.aliasToBean(RosterTableName.class));

            rosterTableNames = cr.list();
            iterator = rosterTableNames.iterator();

            while (iterator.hasNext() == true)
            {
                rosterTableName = new RosterTableName();
                rosterTableName = (RosterTableName) iterator.next();
                rosterName = rosterTableName.getRosterName();
                log.info("roster table name is::::" + rosterName);

            }

        }
        catch (Exception exception)
        {
            exception.printStackTrace();
        }
        finally {
            actionList = null;
            decriptionList = null ;
            stringDiscriptionList =null;
            stringOfDescriptionValues = null;
            descriptionValues = null;

            relretunactions1 = null;
            long actionCode;
            rosterTableName = null;
            iterator = null;
            rosterTableNames = null;
            action1 = null;
            description = null;
            modifiedDescription = null;
        }
        return rosterName;
    }

}
