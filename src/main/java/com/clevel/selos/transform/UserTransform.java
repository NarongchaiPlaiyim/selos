package com.clevel.selos.transform;

import com.clevel.selos.dao.master.UserDAO;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.view.UserView;

import javax.inject.Inject;

public class UserTransform extends Transform {
    @Inject
    private UserDAO userDAO;

    public UserView transformToView(User user) {
        UserView userView = new UserView();
        if (user == null) {
            return userView;
        }
        userView.setId(user.getId());
        userView.setUserName(user.getUserName());
        userView.setRoleDescription(user.getRole() != null ? user.getRole().getDescription() : "");
        userView.setTitleName(user.getTitle() != null ? user.getTitle().getName() : "");
        return userView;
    }
}
