package com.clevel.selos.transform;

import com.clevel.selos.dao.master.RoleDAO;
import com.clevel.selos.dao.master.RoleTypeDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.Role;
import com.clevel.selos.model.db.master.RoleType;
import com.clevel.selos.model.view.RoleTypeView;
import com.clevel.selos.model.view.RoleView;
import org.slf4j.Logger;

import javax.inject.Inject;

public class RoleTransform extends Transform {
    @Inject
    @SELOS
    private Logger log;

    @Inject
    private RoleDAO roleDAO;
    @Inject
    private RoleTypeDAO roleTypeDAO;

    @Inject
    public RoleTransform() {
    }

    public Role transformRoleToModel(RoleView roleView) {
        if (roleView == null) {
            return null;
        }

        Role role;
        if (roleView.getId() != 0) {
            role = roleDAO.findById(roleView.getId());
        } else {
            role = new Role();
        }

        role.setActive(roleView.getActive());
        role.setName(roleView.getName());
        role.setDescription(roleView.getDescription());
        role.setSystemName(roleView.getSystemName());
        role.setRoleType(transformRoleTypeToModel(roleView.getRoleTypeView()));
        return role;
    }

    public RoleView transformRoleToView(Role role) {
        RoleView roleView = new RoleView();
        if (role == null) {
            return roleView;
        }
        roleView.setId(role.getId());
        roleView.setActive(role.getActive());
        roleView.setName(role.getName());
        roleView.setDescription(role.getDescription());
        roleView.setSystemName(role.getSystemName());
        roleView.setRoleTypeView(transformRoleTypeToView(role.getRoleType()));
        return roleView;
    }

    public RoleType transformRoleTypeToModel(RoleTypeView roleTypeView) {
        if (roleTypeView == null) {
            return null;
        }

        RoleType roleType;
        if (roleTypeView.getId() != 0) {
            roleType = roleTypeDAO.findById(roleTypeView.getId());
        } else {
            roleType = new RoleType();
        }

        roleType.setRoleTypeName(roleTypeView.getRoleTypeName());
        roleType.setDescription(roleTypeView.getDescription());
        roleType.setActive(roleTypeView.getActive());
        return roleType;
    }

    public RoleTypeView transformRoleTypeToView(RoleType roleType) {
        RoleTypeView roleTypeView = new RoleTypeView();
        if (roleType == null) {
            return roleTypeView;
        }
        roleTypeView.setId(roleType.getId());
        roleTypeView.setActive(roleType.getActive());
        roleTypeView.setDescription(roleType.getDescription());
        roleTypeView.setRoleTypeName(roleType.getRoleTypeName());
        return roleTypeView;
    }
}
