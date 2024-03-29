package com.clevel.selos.dao.master;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.ManageUserActive;
import com.clevel.selos.model.UserStatus;
import com.clevel.selos.model.db.master.Role;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.master.UserTeam;
import com.clevel.selos.model.db.relation.RelTeamUserDetails;
import com.clevel.selos.model.db.working.WorkCase;
import com.clevel.selos.model.db.working.WorkCaseOwner;
import com.clevel.selos.model.db.working.WorkCasePrescreen;
import com.clevel.selos.model.view.ChangeOwnerView;
import com.clevel.selos.model.view.isa.IsaSearchView;
import com.clevel.selos.util.Util;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.joda.time.DateTime;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public class UserDAO extends GenericDAO<User,String> {
    @Inject
    @SELOS
    private Logger log;

    public User user;

    public UserTeam userTeam;

    Long workCaseId;

    long workCasePrescreenId;

    @Inject
    public UserDAO() {

    }

    public boolean isExistId(final String id){
        log.debug("-- isExistId(id : {})", id);
        return isRecordExist(Restrictions.eq("id", id));
    }

//    public boolean isExistIdAndActivityAndStatus(final String id, final int activity, final String status){
//        log.debug("-- isExistId(id : {}, activity : {}, status : {})", id, activity, status);
//        return isRecordExist(Restrictions.eq("id", id),Restrictions.eq("active", activity),Restrictions.eq("status", status));
//    }
//
//    public User fingByIdAndActivityAndStatus(final String id, final int activity, final String status){
//        user = new User();
//        Criteria criteria = createCriteria();
//        criteria.add(Restrictions.eq("id", id));
//        criteria.add(Restrictions.eq("active", activity));
//        criteria.add(Restrictions.eq("status", status));
//        criteria.addOrder(Order.asc("id"));
//        user = (User) criteria.uniqueResult();
//
//        return user;
//    }

    public boolean isExistUserName(final String userName){
        log.debug("-- isExistUserName(userName : {})", userName);
        return isRecordExist(Restrictions.eq("userName", userName));
    }

    public User findById(final String id){
        log.debug("-- findById. [{}]",id);
        user = new User();
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("id", id));
        criteria.addOrder(Order.asc("id"));
        user = (User) criteria.uniqueResult();

        return user;
    }

    public void updateActiveOrInactive(final User model, final int value, final User user){
        log.debug("-- updateActiveOrInactive(User.id[{}], value : {})",model.getId(), value);
        if(!Util.isNull(model)){
            model.setActive(value);
            model.setModifyBy(user);
            model.setModifyDate(DateTime.now().toDate());
            persist(model);
            log.debug("-- User.id[{}] updated", model.getId());
        }
    }

    public List<User> findByUserStatusNORMAL(){
        log.debug("-- findByUserStatusNORMAL()");
        List<User> userList = null;
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("userStatus", UserStatus.NORMAL));
        criteria.addOrder(Order.asc("id"));
        userList = Util.safetyList((List<User>) criteria.list());
        log.debug("-- UserList.size()[{}]", userList.size());
        return userList;
    }

    public void createNewUserByISA(final User user){
        log.debug("-- createNewUserByISA(). {}",user);
        if(!Util.isNull(user)){
            user.setUserStatus(UserStatus.NORMAL);
            persist(user);
        }
    }

    public void deleteUserByISA(final String id, final User user) throws Exception{
        log.debug("-- deleteUserByISA(Id : {})", id);
        User model = findById(id);
        if(!Util.isNull(model)){
            model.setActive(ManageUserActive.INACTIVE.getValue());
            model.setUserStatus(UserStatus.MARK_AS_DELETED);
            model.setModifyBy(user);
            model.setModifyDate(DateTime.now().toDate());
            persist(model);
        }
    }

    public List<User> findByISA(final IsaSearchView isaSearchView) throws Exception{
        log.debug("-- findByISA(ISASearchView : {})", isaSearchView);
        List<User> userList = null;
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("userStatus", UserStatus.NORMAL));
        if (!Util.isNull(isaSearchView.getId()) && !Util.isZero(isaSearchView.getId().length())) {
            criteria.add(Restrictions.like("id", "%" + isaSearchView.getId() + "%"));
        }
        if (!Util.isNull(isaSearchView.getUsername()) && !Util.isZero(isaSearchView.getUsername().length())) {
            criteria.add(Restrictions.like("userName", "%" + isaSearchView.getUsername() + "%"));
        }
        if (!Util.isZero(isaSearchView.getRoleId().getId())) {
            criteria.add(Restrictions.eq("role", isaSearchView.getRoleId()));
        }
        if (!Util.isZero(isaSearchView.getDepartmentId().getId())) {
            criteria.add(Restrictions.eq("department", isaSearchView.getDepartmentId()));
        }
        if (!Util.isZero(isaSearchView.getDivisionId().getId())) {
            criteria.add(Restrictions.eq("division", isaSearchView.getDivisionId()));
        }
        if (!Util.isZero(isaSearchView.getRegionId().getId())) {
            criteria.add(Restrictions.eq("region", isaSearchView.getRegionId()));
        }
        if (!Util.isZero(isaSearchView.getTeamId().getId())) {
            criteria.add(Restrictions.eq("team", isaSearchView.getTeamId()));
        }
        if (!Util.isZero(isaSearchView.getTitleId().getId())) {
            criteria.add(Restrictions.eq("title", isaSearchView.getTitleId()));
        }
        userList = Util.safetyList((List<User>) criteria.list());
        log.debug("-- UserList.size()[{}]", userList.size());
        return userList;
    }

    public User findUserByID(String userId)
    {
        Criteria criteria1 = getSession().createCriteria(User.class);

        criteria1.add(Restrictions.eq("id",userId).ignoreCase());

        List<User> usersList = criteria1.list();

        User user = new User();

        Iterator<User> iterator1 = usersList.iterator();

        while(iterator1.hasNext())
        {
            user = iterator1.next();
        }

        return user;
    }

    public String getUserNameById(String userId)
    {

        Criteria criteria1 = getSession().createCriteria(User.class);
        criteria1.setProjection(Projections.projectionList().add(Projections.property("userName"), "userName"));
        criteria1.add(Restrictions.eq("id",userId).ignoreCase()).setResultTransformer(Transformers.aliasToBean(User.class));
        List usernamebasedteamid = criteria1.list();
        Iterator iterator1 = usernamebasedteamid.iterator();
        String username = null;
        while(iterator1.hasNext() == true)
        {
            User user = new User();
            user = (User)iterator1.next();
            username = user.getUserName();
            user = null;
        }

        username = userId+" - "+username;

        return username;

    }

    /*public String getUserIdByName(String userName)
    {

        Criteria criteria1 = getSession().createCriteria(User.class);
        criteria1.setProjection(Projections.projectionList().add(Projections.property("id"), "id"));
        criteria1.add(Restrictions.eq("userName",userName).ignoreCase()).setResultTransformer(Transformers.aliasToBean(User.class));
        List usernamebasedteamid = criteria1.list();
        Iterator iterator1 = usernamebasedteamid.iterator();
        String userId = null;
        while(iterator1.hasNext() == true)
        {
            User user = new User();
            user = (User)iterator1.next();
            userId = user.getId();
            user = null;
        }

        //userName = userId+" - "+userName;

        return userId;

    }*/

    public User findByUserName(String userName) {

        //log.debug("findByUserName. (userName: {})",userName);
        return findOneByCriteria(Restrictions.eq("userName",userName));
    }

    public List<User> findBDMChecker(User user){
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.ne("id", user.getId()));
        criteria.add(Restrictions.eq("role", user.getRole()));
        criteria.add(Restrictions.eq("team", user.getTeam()));
        criteria.addOrder(Order.asc("id"));
        List<User> userList = criteria.list();

        return userList;
    }

    public List<User> findABDMList(User user, Role abdmRole){
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.ne("id", user.getId()));
        criteria.add(Restrictions.eq("role", abdmRole));
        criteria.add(Restrictions.eq("team", user.getTeam()));
        criteria.addOrder(Order.asc("id"));

        List<User> userList = criteria.list();

        return userList;

    }

    public List<User> findUserListByRoleId(User user, int roleId){
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.ne("id", user.getId()));
        criteria.add(Restrictions.eq("role.id", roleId));
        criteria.addOrder(Order.asc("id"));

        List<User> userList = criteria.list();

        return userList;
    }

    public List<User> findUserList(List<UserTeam> userTeams){
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.in("team", userTeams));
        criteria.addOrder(Order.asc("userName"));

        List<User> userList = criteria.list();

        return userList;
    }

    // to get teamNames based on team id
    public List<ChangeOwnerView> getTeamLeadUsers(int teamId)
    {
        log.info("getTeamLeadUsers method::::");
        List teamTypeValue = null;
        teamTypeValue = new ArrayList();
        String teamTypeValues = null;
        String stringOfTeamTypeValues = null;
        List<Integer> teamList = null;
        teamList = new ArrayList<Integer>();
        List<Integer> teamListForGH = null;
        teamListForGH = new ArrayList<Integer>();
        String teamTypeName = null;
        List<ChangeOwnerView>  stringTeamTypeList = null;
        stringTeamTypeList = new ArrayList<ChangeOwnerView>();
        try
        {

            Criteria crTeamType = getSession().createCriteria(UserTeam.class)
                    .setProjection(Projections.projectionList()
                            .add(Projections.property("team_type"), "team_type"))
                    .add(Restrictions.eq("id", teamId))
                    .setResultTransformer(Transformers.aliasToBean(UserTeam.class));
            List teamTypeList = crTeamType.list();
            Iterator iterator1 = teamTypeList.iterator();

            while (iterator1.hasNext() == true)
            {
                UserTeam teamType = new UserTeam();
                teamType = (UserTeam) iterator1.next();
                int teamTypeId = teamType.getTeam_type();
                teamTypeValue.add(teamTypeId);
                teamType = null;
            }

            stringOfTeamTypeValues = teamTypeValue.toString();
            teamTypeValues = stringOfTeamTypeValues.replace("[", "").replace("]", "");
            int teamValue = Integer.parseInt(teamTypeValues);
            log.info("teamList .... {}", teamTypeValue);
            Criteria cr = getSession().createCriteria(RelTeamUserDetails.class)
                    .setProjection(Projections.projectionList()
                            .add(Projections.property("team_Id"), "team_Id").add(Projections.property("raSearchCase"), "raSearchCase"))
                    .add(Restrictions.eq("tlThId", teamId))
                    .setResultTransformer(Transformers.aliasToBean(RelTeamUserDetails.class));
            List teamUserList = cr.list();
            log.info("TeamUserList .... {}", teamUserList);
            Iterator iterator2 = teamUserList.iterator();

            while (iterator2.hasNext() == true)
            {
                RelTeamUserDetails relTeamUserDetails1 = new RelTeamUserDetails();
                relTeamUserDetails1 = (RelTeamUserDetails) iterator2.next();
                int teamLeadId = relTeamUserDetails1.getTeam_Id();
                String raSearchCase = relTeamUserDetails1.getRaSearchCase();
                // log.info("raSearchCase______________" + raSearchCase);
                if (raSearchCase.equalsIgnoreCase("Y"))
                {
                    teamList.add(teamLeadId);
                    teamListForGH.add(teamLeadId);
                    raSearchCase= null;
                }
                relTeamUserDetails1 = null;
            }

            log.info("teamHeadValue teamList .... {}", teamList);
            if (teamValue == 3) {

                Criteria cr2 = getSession().createCriteria(RelTeamUserDetails.class)
                        .setProjection(Projections.projectionList()
                                .add(Projections.property("team_Id"), "team_Id").add(Projections.property("raSearchCase"), "raSearchCase"))
                        .add(Restrictions.in("tlThId", teamList))
                        .setResultTransformer(Transformers.aliasToBean(RelTeamUserDetails.class));
                List teamHeadUserList2 = cr2.list();
                //log.info("teamHeadUserList2 .... {}", teamHeadUserList2);
                teamList.clear();
                Iterator iterator = teamHeadUserList2.iterator();

                while (iterator.hasNext() == true)
                {
                    RelTeamUserDetails relTeamUserDetails1 = new RelTeamUserDetails();
                    relTeamUserDetails1 = (RelTeamUserDetails) iterator.next();
                    int teamHeadId = relTeamUserDetails1.getTeam_Id();
                    String raSearchCase12 = relTeamUserDetails1.getRaSearchCase();
                    //log.info("raSearchCase:::in condition 11"+raSearchCase12);
                    String raSearchCase = relTeamUserDetails1.getRaSearchCase();
                    //log.info("raSearchCase:::in condition 33"+raSearchCase);
                    // log.info("teamLeadId .... {}", teamHeadId);
                    if(raSearchCase.equalsIgnoreCase("Y"))
                    {
                        teamList.add(teamHeadId);
                        raSearchCase = null;
                    }
                    relTeamUserDetails1 = null;
                }

            }

            if (teamValue == 4) {

                log.info("group .... {}", teamValue);
                Criteria cr3 = getSession().createCriteria(RelTeamUserDetails.class)
                        .setProjection(Projections.projectionList()
                                .add(Projections.property("team_Id"), "team_Id").add(Projections.property("raSearchCase"), "raSearchCase"))
                        .add(Restrictions.in("tlThId", teamList))
                        .setResultTransformer(Transformers.aliasToBean(RelTeamUserDetails.class));

                List teamHeadUserList3 = cr3.list();
                log.info("teamHeadUserList2 .... {}", teamHeadUserList3);
                teamList.clear();
                Iterator iterator3 = teamHeadUserList3.iterator();

                while (iterator3.hasNext() == true)
                {
                    RelTeamUserDetails relTeamUserDetails2 = new RelTeamUserDetails();
                    relTeamUserDetails2 = (RelTeamUserDetails) iterator3.next();
                    String raSearchCase = relTeamUserDetails2.getRaSearchCase();
                    int teamHeadId1 = relTeamUserDetails2.getTeam_Id();
                    log.info("teamLeadId .... {}", teamHeadId1);
                    if(raSearchCase.equalsIgnoreCase("Y"))
                    {
                        teamList.add(teamHeadId1);
                        raSearchCase = null;
                    }
                    relTeamUserDetails2 = null;
                }

                Criteria cr4 = getSession().createCriteria(RelTeamUserDetails.class)
                        .setProjection(Projections.projectionList()
                                .add(Projections.property("team_Id"), "team_Id").add(Projections.property("raSearchCase"), "raSearchCase"))
                        .add(Restrictions.in("tlThId", teamList))
                        .setResultTransformer(Transformers.aliasToBean(RelTeamUserDetails.class));

                List teamHeadUserList4 = cr4.list();
                log.info("teamHeadUserList4 .... {}", teamHeadUserList4);
                teamList.clear();
                Iterator iterator = teamHeadUserList4.iterator();

                while (iterator.hasNext() == true)
                {
                    RelTeamUserDetails relTeamUserDetails1 = new RelTeamUserDetails();
                    relTeamUserDetails1 = (RelTeamUserDetails) iterator.next();
                    String raSearchCase = relTeamUserDetails1.getRaSearchCase();
                    int teamHeadId2 = relTeamUserDetails1.getTeam_Id();
                    log.info("teamLeadId .... {}", teamHeadId2);
                    if(raSearchCase.equalsIgnoreCase("Y"))
                    {
                        teamList.add(teamHeadId2);
                        teamListForGH.add(teamHeadId2);
                        raSearchCase = null;
                    }
                    relTeamUserDetails1 = null;
                }

            }
            log.info("teamList.... {}", teamList);
            Criteria criteria2 = getSession().createCriteria(UserTeam.class);
            if (teamValue == 4)
            {
                criteria2.setProjection(Projections.projectionList().add(Projections.property("team_name"), "team_name").add(Projections.property("id"), "id")).add(Restrictions.in("id", teamListForGH)).setResultTransformer(Transformers.aliasToBean(UserTeam.class));
            }
            else
            {

                criteria2.setProjection(Projections.projectionList().add(Projections.property("team_name"), "team_name").add(Projections.property("id"), "id")).add(Restrictions.in("id", teamList)).setResultTransformer(Transformers.aliasToBean(UserTeam.class));

            }
            teamTypeList = criteria2.list();
            log.info("teamTypeList ..{}", teamTypeList);
            Iterator iterator3 = teamTypeList.iterator();

            while (iterator3.hasNext() == true) {
                ChangeOwnerView changeOwnerView = new ChangeOwnerView();
                UserTeam teamType1 = new UserTeam();
                teamType1 = (UserTeam) iterator3.next();
                teamTypeName = teamType1.getTeam_name();
                String id = String.valueOf(teamType1.getId());
                log.info("team in modified id is:::::::::::::::"+id);
                changeOwnerView.setId(id);
                changeOwnerView.setUser(teamTypeName);
                stringTeamTypeList.add(changeOwnerView);
                changeOwnerView= null;
                teamType1 = null;
            }


            log.info("inboxTypeList .... {}", stringTeamTypeList);

            stringTeamTypeList = new ArrayList<ChangeOwnerView>(new HashSet<ChangeOwnerView>(stringTeamTypeList));
        }
        catch (Exception e)
        {
            log.error("",e);
        }
        finally {
            teamTypeValue = null;
            teamTypeValues = null;
            stringOfTeamTypeValues = null;
            teamList = null;
            teamListForGH = null;
            teamTypeName = null;
        }
        return stringTeamTypeList;

    }

    public List<ChangeOwnerView> getRoleList(int seletedTeamId, int teamId)
    {
        log.info("::getRoleList method::");
        /*Set<ChangeOwnerView> set = null;
        set = new TreeSet<ChangeOwnerView>();*/

        List<ChangeOwnerView> set = new ArrayList<ChangeOwnerView>();
        List<String> roleList = new ArrayList<String>();
        List<User>roleIdList = new ArrayList<User>();
        List<Integer>roleIdsIntList = new ArrayList<Integer>();

        try {

            Criteria cr2 = getSession().createCriteria(User.class, "user").createAlias("user.team", "team")
                    .setProjection(Projections.projectionList()
                            .add(Projections.property("role"), "role"))
                            // .add(Restrictions.eq("team.id", teamCodeDetails))
                    .add(Restrictions.eq("team.id", seletedTeamId))
                    .setResultTransformer(Transformers.aliasToBean(User.class));
            roleIdList = cr2.list();
            Iterator iterator1 = roleIdList.iterator();
            while (iterator1.hasNext()) {
                User user3 = new User();
                user3 = (User) iterator1.next();
                int roleId = user3.getRole().getId();
                log.info("role id :::" + roleId);
                roleIdsIntList.add(roleId);
                user3 = null;
            }

            log.info("list:::::" + roleIdsIntList);
            Criteria criteria2 = getSession().createCriteria(Role.class);
            criteria2.setProjection(Projections.projectionList().add(Projections.property("name"), "name").add(Projections.property("id"), "id")).add(Restrictions.in("id", roleIdsIntList)).setResultTransformer(Transformers.aliasToBean(Role.class));
            roleList = criteria2.list();
            log.info("roleList::::::::::::::::" + roleList);
            Iterator it = roleList.iterator();
            String roleName = null;
            while (it.hasNext()) {
                ChangeOwnerView changeOwnerView=new ChangeOwnerView();
                Role role = new Role();
                role = (Role) it.next();
                roleName = role.getName();
                log.info("roel name: ",roleName);
                String roleId = String.valueOf(role.getId());
                changeOwnerView.setRoleName(roleName);
                changeOwnerView.setRoleNumber(roleId);
                set.add(changeOwnerView) ;
               // role = null;
                //changeOwnerView = null;
            }

        } catch (Exception e) {
            log.error("",e);
        }
        finally {
            roleList = null;
            roleIdList = null;
        }
        log.info("set::::::::::::::::::{}",  set);
        return set;
    }

    public List<String> getUserNames(int teamId, int selectedTeamId, String selectedRoleId)
    {

        List<String>listOfAllUsers = null;
        listOfAllUsers = new ArrayList<String>();
        int selectRoleId = Integer.parseInt(selectedRoleId);

        try {
            log.debug("In getUserNames: team id:{},roleid:{}",selectedTeamId,selectedRoleId);

            Criteria criteria1 = getSession().createCriteria(User.class);
            criteria1.setProjection(Projections.projectionList().add(Projections.property("id"), "id"));
            criteria1.add(Restrictions.eq("team.id", selectedTeamId)).add(Restrictions.eq("role.id",selectRoleId)).setResultTransformer(Transformers.aliasToBean(User.class));
            List usernamebasedteamid = criteria1.list();
            Iterator iterator1 = usernamebasedteamid.iterator();
            while(iterator1.hasNext() == true)
            {
                User user = new User();
                user = (User)iterator1.next();
                String userId = user.getId();
                listOfAllUsers.add(userId);
                user = null;
            }


            Criteria criteria2 = getSession().createCriteria(RelTeamUserDetails.class);
            criteria2.setProjection(Projections.projectionList().add(Projections.property("tlThId"),"tlThId"));
            criteria2.add(Restrictions.eq("team_Id",selectedTeamId)).setResultTransformer(Transformers.aliasToBean(RelTeamUserDetails.class));
            List teamleadusernamesidlist = criteria2.list();
            Iterator iterator2 = teamleadusernamesidlist.iterator();
            while(iterator2.hasNext() == true)
            {
                RelTeamUserDetails relTeamUserDetails = new RelTeamUserDetails();
                relTeamUserDetails = (RelTeamUserDetails)iterator2.next();
                int teamleadid = relTeamUserDetails.getTlThId();
                Criteria criteria3 = getSession().createCriteria(User.class);
                criteria3.setProjection(Projections.projectionList().add(Projections.property("id"),"id")) ;
                criteria3.add(Restrictions.eq("team.id",teamleadid)).add(Restrictions.eq("role.id", selectRoleId)).setResultTransformer(Transformers.aliasToBean(User.class));
                List teamLeadUsersList = criteria3.list();

                Iterator iterator3 = teamLeadUsersList.iterator();
                while(iterator3.hasNext() == true)
                {
                    User user1 = new User();
                    user1 = (User)iterator3.next();
                    String teamleadname = user1.getId();
                    listOfAllUsers.add(teamleadname);
                    user1 = null;
                }
                relTeamUserDetails= null;
            }
            userTeam = null;
            log.debug("All Users :{}",listOfAllUsers.toString());
        }
        catch (Exception exeception)
        {
            exeception.printStackTrace();
        }
        finally
        {

        }
        return listOfAllUsers;
    }


    public String getChangeOwnerWorkItems(String teamLeadName, String selectedRole, String selectUserName, List<String> userList)
    {
        log.info("getChangeOwnerWorkItems=");

        List<String> usersIdsList =null;
        usersIdsList  = new ArrayList<String>();
        List<String> workCaseIdList = null;
        workCaseIdList = new ArrayList<String>();
        List<String> appNumberList = null;
        appNumberList = new ArrayList<String>();
        List<String>uIdsList = null;
        uIdsList = new ArrayList<String>();
        List<Long> WorkCaseList = null;
        List<Long> WorkCasePrescreenList = null;
        WorkCaseList = new ArrayList<Long>();
        WorkCasePrescreenList = new ArrayList<Long>();
        List<String> applicationNoList = null;
        applicationNoList = new ArrayList<String>();
        String changeOwnerList = null;
        String userId = null;
        String appNumber = null;
        String modifiedAppNo = null;
        String stringapplicationNoList = null;
        int selectedRoleId =Integer.parseInt(selectedRole);

        try {


            if (selectUserName.equalsIgnoreCase("All") && userList != null)
            {
                uIdsList.addAll(userList);
            }

            else
            {
                uIdsList.add(selectUserName);
            }

            Criteria criteriaForWorkCaseId = getSession().createCriteria(WorkCaseOwner.class);
            // criteriaForWorkCaseId.setProjection(Projections.projectionList().add(Projections.property("workCaseId"), "workCaseId")).add(Restrictions.eq("roleid",roleId)).add(Restrictions.in("userid", uIdsList)).setResultTransformer(Transformers.aliasToBean(WorkCaseOwner.class));
            if(uIdsList != null )
            {
                criteriaForWorkCaseId.setProjection(Projections.projectionList().add(Projections.property("workCase"), "workCase").add(Projections.property("workCasePrescreen"), "workCasePrescreen")).add(Restrictions.eq("role.id", selectedRoleId)).add(Restrictions.in("user.id", uIdsList)).setResultTransformer(Transformers.aliasToBean(WorkCaseOwner.class));

                log.debug("Criteria to get change owner workitems :{}",criteriaForWorkCaseId.toString());

                workCaseIdList = criteriaForWorkCaseId.list();
            }

            Iterator iterator1 = workCaseIdList.iterator();
            while (iterator1.hasNext()) {
                WorkCaseOwner workCaseOwner = new WorkCaseOwner();
                workCaseOwner = (WorkCaseOwner) iterator1.next();
                if(workCaseOwner.getWorkCase()!=null)
                {

                    workCaseId = (long) workCaseOwner.getWorkCase().getId();
                    log.info("workCaseId is ::::::{}" , workCaseId);
                    WorkCaseList.add(workCaseId);

                }

                if(workCaseOwner.getWorkCasePrescreen()!=null)
                {
                    workCasePrescreenId = (long)workCaseOwner.getWorkCasePrescreen().getId();

                    log.info("workCasePrescreenId is ::::::{}" , workCasePrescreenId);

                    WorkCasePrescreenList.add(workCasePrescreenId);
                }

                workCaseOwner = null;
            }
            log.info("WorkCaseList is ::::::" + WorkCaseList.size());

            Iterator iterator;

            Criteria criteriaForAppNumber = getSession().createCriteria(WorkCase.class);

            // criteriaForWorkCaseId.setProjection(Projections.projectionList().add(Projections.property("workCaseId"), "workCaseId")).add(Restrictions.eq("roleid",roleId)).add(Restrictions.in("roleId", roleIdlist)).setResultTransformer(Transformers.aliasToBean(WorkCaseOwner.class));
            if (WorkCaseList.size() !=0)
            {
                criteriaForAppNumber.setProjection(Projections.projectionList().add(Projections.property("appNumber"), "appNumber")).add(Restrictions.in("id", WorkCaseList)).add(Restrictions.eq("bpmActive", 1)).setResultTransformer(Transformers.aliasToBean(WorkCase.class));
                appNumberList = criteriaForAppNumber.list();

                iterator = appNumberList.iterator();
                while (iterator.hasNext()) {
                    WorkCase workCase = new WorkCase();
                    workCase = (WorkCase) iterator.next();
                    appNumber = workCase.getAppNumber();
                    modifiedAppNo = "'" + appNumber + "'";
                    applicationNoList.add(modifiedAppNo);
                    workCase = null;
                }

            }

            log.info("WorkCasePrescreenList is ::::::" + WorkCasePrescreenList.size());

            criteriaForAppNumber = getSession().createCriteria(WorkCasePrescreen.class);

            // criteriaForWorkCaseId.setProjection(Projections.projectionList().add(Projections.property("workCaseId"), "workCaseId")).add(Restrictions.eq("roleid",roleId)).add(Restrictions.in("roleId", roleIdlist)).setResultTransformer(Transformers.aliasToBean(WorkCaseOwner.class));
            if (WorkCasePrescreenList.size() !=0) {
                criteriaForAppNumber.setProjection(Projections.projectionList().add(Projections.property("appNumber"), "appNumber")).add(Restrictions.in("id", WorkCasePrescreenList)).add(Restrictions.eq("bpmActive", 1)).setResultTransformer(Transformers.aliasToBean(WorkCasePrescreen.class));
                appNumberList = criteriaForAppNumber.list();

                iterator = appNumberList.iterator();
                while (iterator.hasNext()) {
                    WorkCasePrescreen workCasePrescreen = new WorkCasePrescreen();
                    workCasePrescreen = (WorkCasePrescreen) iterator.next();
                    appNumber = workCasePrescreen.getAppNumber();
                    modifiedAppNo = "'" + appNumber + "'";
                    applicationNoList.add(modifiedAppNo);
                    workCasePrescreen = null;
                }

            }

            log.info("App Number :"+applicationNoList.size());
            stringapplicationNoList = applicationNoList.toString();
            log.info("APp numbers : "+stringapplicationNoList);
            changeOwnerList = stringapplicationNoList.replace("[", "").replace("]", "");

        }
        catch (Exception e)
        {
            log.error("Error:",e);
        }
        finally
        {
            usersIdsList =null;
            workCaseIdList = null;
            appNumberList = null;
            uIdsList = null;
            WorkCaseList = null;
            applicationNoList = null;
            stringapplicationNoList = null;

        }
        return changeOwnerList;


    }
    public List<ChangeOwnerView> getTeamUserForChangeOwner(int teamId)
    {
        List teamTypeValue = new ArrayList();
        String teamTypeValues = null;
        String stringOfTeamTypeValues = null;
        List<Integer> teamList = new ArrayList<Integer>();
        List<Integer> teamListForGH = new ArrayList<Integer>();
        String teamTypeName = null;
        List<ChangeOwnerView> stringTeamTypeList = new ArrayList<ChangeOwnerView>();
        try {


            Criteria crTeamType = getSession().createCriteria(UserTeam.class)
                    .setProjection(Projections.projectionList()
                            .add(Projections.property("team_type"), "team_type"))
                    .add(Restrictions.eq("id", teamId))
                    .setResultTransformer(Transformers.aliasToBean(UserTeam.class));
            List teamTypeList = crTeamType.list();
            Iterator iterator1 = teamTypeList.iterator();

            while (iterator1.hasNext() == true) {
                UserTeam teamType = new UserTeam();

                teamType = (UserTeam) iterator1.next();

                int teamTypeId = teamType.getTeam_type();
                teamTypeValue.add(teamTypeId);
                teamType = null;
            }

            stringOfTeamTypeValues = teamTypeValue.toString();
            teamTypeValues = stringOfTeamTypeValues.replace("[", "").replace("]", "");
            int teamValue = Integer.parseInt(teamTypeValues);
            Criteria cr = getSession().createCriteria(RelTeamUserDetails.class)
                    .setProjection(Projections.projectionList()
                            .add(Projections.property("team_Id"), "team_Id").add(Projections.property("raSearchUser"), "raSearchUser"))
                    .add(Restrictions.eq("tlThId", teamId))
                    .setResultTransformer(Transformers.aliasToBean(RelTeamUserDetails.class));
            List teamUserList = cr.list();
            Iterator iterator2 = teamUserList.iterator();

            while (iterator2.hasNext() == true)
            {
                RelTeamUserDetails relTeamUserDetails1 = new RelTeamUserDetails();
                relTeamUserDetails1 = (RelTeamUserDetails) iterator2.next();
                int teamLeadId = relTeamUserDetails1.getTeam_Id();
                String raSearchCaseuser = relTeamUserDetails1.getRaSearchUser();
                if (raSearchCaseuser.equalsIgnoreCase("Y"))
                {
                    teamList.add(teamLeadId);
                    teamListForGH.add(teamLeadId);
                    raSearchCaseuser= null;
                }
                relTeamUserDetails1 = null;
            }

            if (teamValue == 3) {
                Criteria cr2 = getSession().createCriteria(RelTeamUserDetails.class)
                        .setProjection(Projections.projectionList()
                                .add(Projections.property("team_Id"), "team_Id").add(Projections.property("raSearchUser"), "raSearchUser"))
                        .add(Restrictions.in("tlThId", teamList))
                        .setResultTransformer(Transformers.aliasToBean(RelTeamUserDetails.class));
                List teamHeadUserList2 = cr2.list();
                teamList.clear();
                Iterator iterator = teamHeadUserList2.iterator();

                while (iterator.hasNext() == true)
                {
                    RelTeamUserDetails relTeamUserDetails1 = new RelTeamUserDetails();
                    relTeamUserDetails1 = (RelTeamUserDetails) iterator.next();
                    int teamHeadId = relTeamUserDetails1.getTeam_Id();
                    String raSearchCaseuser = relTeamUserDetails1.getRaSearchUser();
                    if(raSearchCaseuser.equalsIgnoreCase("Y"))
                    {
                        teamList.add(teamHeadId);
                        raSearchCaseuser = null;
                    }
                    relTeamUserDetails1 = null;
                }

            }

            if (teamValue == 4) {
                Criteria cr3 = getSession().createCriteria(RelTeamUserDetails.class)
                        .setProjection(Projections.projectionList()
                                .add(Projections.property("team_Id"), "team_Id").add(Projections.property("raSearchUser"), "raSearchUser"))
                        .add(Restrictions.in("tlThId", teamList))
                        .setResultTransformer(Transformers.aliasToBean(RelTeamUserDetails.class));

                List teamHeadUserList3 = cr3.list();
                teamList.clear();
                Iterator iterator3 = teamHeadUserList3.iterator();

                while (iterator3.hasNext() == true)
                {
                    RelTeamUserDetails relTeamUserDetails2 = new RelTeamUserDetails();
                    relTeamUserDetails2 = (RelTeamUserDetails) iterator3.next();
                    String raSearchCaseuser = relTeamUserDetails2.getRaSearchUser();
                    int teamHeadId1 = relTeamUserDetails2.getTeam_Id();
                    if(raSearchCaseuser.equalsIgnoreCase("Y"))
                    {
                        teamList.add(teamHeadId1);
                        raSearchCaseuser = null;
                    }
                    relTeamUserDetails2 = null;
                }

                Criteria cr4 = getSession().createCriteria(RelTeamUserDetails.class)
                        .setProjection(Projections.projectionList()
                                .add(Projections.property("team_Id"), "team_Id").add(Projections.property("raSearchUser"), "raSearchUser"))
                        .add(Restrictions.in("tlThId", teamList))
                        .setResultTransformer(Transformers.aliasToBean(RelTeamUserDetails.class));

                List teamHeadUserList4 = cr4.list();
                teamList.clear();
                Iterator iterator = teamHeadUserList4.iterator();

                while (iterator.hasNext() == true)
                {
                    RelTeamUserDetails relTeamUserDetails1 = new RelTeamUserDetails();
                    relTeamUserDetails1 = (RelTeamUserDetails) iterator.next();
                    String raSearchCaseuser = relTeamUserDetails1.getRaSearchUser();
                    int teamHeadId2 = relTeamUserDetails1.getTeam_Id();
                    if(raSearchCaseuser.equalsIgnoreCase("Y"))
                    {
                        teamList.add(teamHeadId2);
                        teamListForGH.add(teamHeadId2);
                        raSearchCaseuser = null;
                    }
                    relTeamUserDetails1 = null;
                }

            }
            Criteria criteria2 = getSession().createCriteria(UserTeam.class);
            if (teamValue == 4)
            {
                criteria2.setProjection(Projections.projectionList().add(Projections.property("team_name"), "team_name").add(Projections.property("id"), "id")).add(Restrictions.in("id", teamListForGH)).setResultTransformer(Transformers.aliasToBean(UserTeam.class));
            }
            else
            {
                criteria2.setProjection(Projections.projectionList().add(Projections.property("team_name"), "team_name").add(Projections.property("id"), "id")).add(Restrictions.in("id", teamList)).setResultTransformer(Transformers.aliasToBean(UserTeam.class));
            }
            teamTypeList = criteria2.list();
            Iterator iterator3 = teamTypeList.iterator();

            while (iterator3.hasNext() == true) {
                ChangeOwnerView changeOwnerView = new ChangeOwnerView();
                UserTeam teamType1 = new UserTeam();
                teamType1 = (UserTeam) iterator3.next();
                teamTypeName = teamType1.getTeam_name();
                String id = String.valueOf(teamType1.getId());
                changeOwnerView.setId(id);
                changeOwnerView.setUser(teamTypeName);
                stringTeamTypeList.add(changeOwnerView);
                changeOwnerView= null;
                teamType1 = null;
            }
        }
        catch (Exception e)
        {
            log.error("",e);
        }
        finally {
            teamTypeValue = null;
            teamTypeValues = null;
            stringOfTeamTypeValues = null;
            teamList = null;
            teamListForGH = null;
        }
        log.info("inboxTypeList .... {}", stringTeamTypeList);
        return stringTeamTypeList;
    }

    public String getRoleNameFromRoleTable(int roleid)
    {
        List<String> roleIdsList = null;
        roleIdsList= new ArrayList<String>();
        String roleName = null;
        try
        {

            Criteria criteriaForRole = getSession().createCriteria(Role.class)
                    .setProjection(Projections.projectionList()
                            .add(Projections.property("name"), "name"))
                    .add(Restrictions.eq("id", roleid))
                    .setResultTransformer(Transformers.aliasToBean(Role.class));
            roleIdsList = criteriaForRole.list();
            Iterator it = roleIdsList.iterator();
            while (it.hasNext()) {
                Role role = new Role();
                role = (Role) it.next();
                roleName = role.getName();
                role = null;

            }

        }
        catch (Exception e)
        {
            log.error("",e);
        }
        finally {

            roleIdsList = null;
        }
        return  roleName;
    }
}
