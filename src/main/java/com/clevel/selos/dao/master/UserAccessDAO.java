package com.clevel.selos.dao.master;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.UserAccess;
import com.clevel.selos.util.Util;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class UserAccessDAO extends GenericDAO<UserAccess,Integer> {
    @Inject
    @SELOS
    private Logger log;

    @Inject
    public UserAccessDAO() {
    }

    public List<UserAccess> getUserAccess(long stepId, int screenId, int roleId){
        List<UserAccess> userAccessList = new ArrayList<UserAccess>();
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("step.id", stepId));
        criteria.add(Restrictions.eq("screenId", screenId));
        criteria.add(Restrictions.eq("role.id", roleId));
        criteria.add(Restrictions.eq("active", 1));

        userAccessList = criteria.list();

        if(Util.isNull(userAccessList)){
            userAccessList = new ArrayList<UserAccess>();
        }

        return userAccessList;
    }
}
