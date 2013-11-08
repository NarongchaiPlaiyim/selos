package com.clevel.selos.dao.master;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.model.db.master.FieldsControl;
import com.clevel.selos.model.db.master.Role;
import com.clevel.selos.model.db.master.Status;
import com.clevel.selos.model.db.master.Step;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.List;

public class FieldsControlDAO extends GenericDAO<FieldsControl, Long> {
    @Inject
    private Logger log;

    @Inject
    public FieldsControlDAO() {
    }

    public List<FieldsControl> findFieldControlByScreenRoleStepStatus(int screenId,Role role,Status status) {
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("screenId", screenId));
        criteria.add(Restrictions.eq("role", role));
        criteria.add(Restrictions.eq("status", status));
        List<FieldsControl> fieldsControlList = criteria.list();

        return fieldsControlList;
    }
}
