package com.clevel.selos.dao.relation;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.Relation;
import com.clevel.selos.model.db.master.UserTeam;
import com.clevel.selos.model.db.relation.RelTeamUserDetails;
import com.clevel.selos.model.db.relation.RelationCustomer;
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

public class RelTeamUserDetailsDAO extends GenericDAO<RelTeamUserDetails, Integer> {
    @Inject
    @SELOS
    Logger log;
    @Inject
    RelTeamUserDetailsDAO() {
    }

    @SuppressWarnings("unchecked")
    public List<UserTeam> getTeamHeadLeadByTeamId(int teamId) {
        log.info("getTeamHeadLeadByTeamId (teamId : {})", teamId);
        List<UserTeam> userTeamList = null;

        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("team_Id", teamId));
        criteria.add(Restrictions.eq("teamFlag", "Y"));

        List<RelTeamUserDetails> relTeamUserDetailsList = criteria.list();

        log.debug("relTeamUserDetailsList size : {}",relTeamUserDetailsList.size());

        if(relTeamUserDetailsList!=null && relTeamUserDetailsList.size()>0){
            List<Integer> teamIdList = new ArrayList<Integer>();
            for(RelTeamUserDetails relTeamUserDetails: relTeamUserDetailsList){
                teamIdList.add(relTeamUserDetails.getTlThId());
            }

            Criteria criteria2 = getSession().createCriteria(UserTeam.class);
            criteria2.add(Restrictions.in("id", teamIdList));
            userTeamList = criteria2.list();
        }

        log.info("userTeamList size : {}", userTeamList.size());

        return userTeamList;
    }
}
