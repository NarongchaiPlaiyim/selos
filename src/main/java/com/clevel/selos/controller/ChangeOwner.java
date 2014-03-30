package com.clevel.selos.controller;

import com.clevel.selos.businesscontrol.PEDBExecute;
import com.clevel.selos.dao.master.ActionDAO;
import com.clevel.selos.dao.master.UserDAO;
import com.clevel.selos.filenet.bpm.services.BPMService;
import com.clevel.selos.filenet.bpm.services.impl.BPMServiceImpl;
import com.clevel.selos.filenet.bpm.util.constants.BPMConstants;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.integration.bpm.BPMInterfaceImpl;
import com.clevel.selos.model.view.ChangeOwnerView;
import com.clevel.selos.model.view.PERoster;
import com.clevel.selos.security.UserDetail;
import com.clevel.selos.system.Config;
import org.slf4j.Logger;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlSelectBooleanCheckbox;
import javax.faces.component.html.HtmlSelectOneMenu;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Sreenu
 * Date: 3/20/14
 * Time: 11:36 PM
 * To change this template use File | Settings | File Templates.
 */
@ManagedBean(name = "change")
@ViewScoped
public class ChangeOwner implements Serializable {
    @Inject
    @SELOS
    Logger log;
    @Inject
    UserDAO userDAO;
    @Inject
    PEDBExecute pedbExecute;
    @Inject
    ActionDAO actionDAO;

    public UserDAO getUserDAO() {
        return userDAO;
    }

    public void setUserDAO(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public PEDBExecute getPedbExecute() {
        return pedbExecute;
    }

    public void setPedbExecute(PEDBExecute pedbExecute) {
        this.pedbExecute = pedbExecute;
    }

    public ActionDAO getActionDAO() {
        return actionDAO;
    }

    public void setActionDAO(ActionDAO actionDAO) {
        this.actionDAO = actionDAO;
    }

    public UserDetail getUserDetail() {
        return userDetail;
    }

    public void setUserDetail(UserDetail userDetail) {
        this.userDetail = userDetail;
    }

    public int getTeamId() {
        return teamId;
    }

    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }

    public void setRolSet(List<ChangeOwnerView> rolSet) {
        this.rolSet = rolSet;
    }

    public String getChangeOwnerQueryList() {
        return changeOwnerQueryList;
    }

    public void setChangeOwnerQueryList(String changeOwnerQueryList) {
        this.changeOwnerQueryList = changeOwnerQueryList;
    }

    public String getRosterName() {
        return rosterName;
    }

    public void setRosterName(String rosterName) {
        this.rosterName = rosterName;
    }

    public int getSelectedTeamId() {
        return selectedTeamId;
    }

    public void setSelectedTeamId(int selectedTeamId) {
        this.selectedTeamId = selectedTeamId;
    }

    public Map.Entry getEntry() {
        return entry;
    }

    public void setEntry(Map.Entry entry) {
        this.entry = entry;
    }

    public String getPeRosterName() {
        return peRosterName;
    }

    public void setPeRosterName(String peRosterName) {
        this.peRosterName = peRosterName;
    }

    public BPMInterfaceImpl getBpmInterfaceImpl() {
        return bpmInterfaceImpl;
    }

    public void setBpmInterfaceImpl(BPMInterfaceImpl bpmInterfaceImpl) {
        this.bpmInterfaceImpl = bpmInterfaceImpl;
    }

    UserDetail userDetail;
    private int teamId;
   // private Set<ChangeOwnerView> rolSet = null;
    private List<ChangeOwnerView> rolSet = null;
    List<PERoster> changeOwerViewList = null;
    String  changeOwnerQueryList = null;
    private List userList1 = null;
    List<ChangeOwnerView> teamTypeName;
    private String selectTeam;
    private String selectRole;
    private String selectuser;
    List<String> userNamesForChangeOwnerList = null;
    private String selectUserChangeOwner;
    private String remark;
    List<ChangeOwnerView>teamNamesForChangeOwerTo =null;
    private Map<String, Boolean> checked = new HashMap<String, Boolean>();
    String rosterName = null;
    String roleName;

    public String getRoleNumber() {
        return roleNumber;
    }

    public void setRoleNumber(String roleNumber) {
        this.roleNumber = roleNumber;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    String roleNumber;
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    private String id;
    private String user;
    int selectedTeamId;
    Map.Entry entry;
    @Inject
    @Config(name = "interface.pe.rosterName")
    String peRosterName;
    @Inject
    BPMInterfaceImpl bpmInterfaceImpl;

    public List<ChangeOwnerView> getTeamTypeName() {
        return teamTypeName;
    }

    public void setTeamTypeName(List<ChangeOwnerView> teamTypeName) {
        this.teamTypeName = teamTypeName;
    }
    public String getRemark() {
        return remark;
    }
    public void setRemark(String remark) {
        this.remark = remark;
    }
    public String getSelectUserChangeOwner() {
        return selectUserChangeOwner;
    }
    public void setSelectUserChangeOwner(String selectUserChangeOwner) {
        this.selectUserChangeOwner = selectUserChangeOwner;
    }
    public List<String> getUserNamesForChangeOwnerList() {
        return userNamesForChangeOwnerList;
    }
    public void setUserNamesForChangeOwnerList(List<String> userNamesForChangeOwnerList) {
        this.userNamesForChangeOwnerList = userNamesForChangeOwnerList;
    }
    public String getSelectTeamNameChangeOwner() {
        return selectTeamNameChangeOwner;
    }
    public void setSelectTeamNameChangeOwner(String selectTeamNameChangeOwner) {
        this.selectTeamNameChangeOwner = selectTeamNameChangeOwner;
    }
    private String selectTeamNameChangeOwner;
    public List<ChangeOwnerView> getTeamNamesForChangeOwerTo() {
        return teamNamesForChangeOwerTo;
    }
    public void setTeamNamesForChangeOwerTo(List<ChangeOwnerView> teamNamesForChangeOwerTo) {
        this.teamNamesForChangeOwerTo = teamNamesForChangeOwerTo;
    }
    public List<PERoster> getChangeOwerViewList() {
        return changeOwerViewList;
    }
    public void setChangeOwerViewList(List<PERoster> changeOwerViewList) {
        this.changeOwerViewList = changeOwerViewList;
    }
    public List getUserList1() {
        return userList1;
    }
    public void setUserList1(List userList1) {
        this.userList1 = userList1;
    }
    public String getSelectRole() {
        return selectRole;
    }
    public void setSelectRole(String selectRole) {
        this.selectRole = selectRole;
    }
    public String getSelectTeam() {
        return selectTeam;
    }
    public void setSelectTeam(String selectTeam) {
        this.selectTeam = selectTeam;
    }

    public String getSelectuser() {
        return selectuser;
    }
    public void setSelectuser(String selectuser) {
        this.selectuser = selectuser;
    }
    public List<ChangeOwnerView> getRolSet() {
        return rolSet;
    }
    /*public void setRolSet(Set rolSet) {
        this.rolSet = rolSet;
    }*/
    public Map<String, Boolean> getChecked() {
        return checked;
    }
    public void setChecked(Map<String, Boolean> checked) {
        this.checked = checked;
    }

    @PostConstruct
    public void init()
    {

        userDetail = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        teamId = userDetail.getTeamid();
        log.info("teamIdList::::::::" + teamId);
        teamTypeName = new ArrayList<ChangeOwnerView>();
        teamTypeName = userDAO.getTeamLeadUsers(teamId);
        teamNamesForChangeOwerTo =new ArrayList<ChangeOwnerView>();
        teamNamesForChangeOwerTo = userDAO.getTeamUserForChangeOwner(teamId);

        log.info("teamTypeName::::" + teamTypeName);


    }

    public List<ChangeOwnerView> getRoleNameForSelectedTeam(ValueChangeEvent event)
    {

        rolSet = null;
        //rolSet = new TreeSet<ChangeOwnerView>();
        rolSet = new ArrayList<ChangeOwnerView>();
        selectTeam = event.getNewValue().toString();

        if(selectTeam != null && selectTeam.trim().length()>0)
        {
        selectedTeamId = Integer.parseInt(selectTeam);
        }
        if(selectTeam != null)
        {
            rolSet = userDAO.getRoleList(selectedTeamId, teamId);
        }

        userList1 = null;
        return rolSet;

    }


    public List<String> getAllUserNames(ValueChangeEvent event)
    {
        try
        {
            userList1 = null;
            userList1 = new ArrayList();
            selectRole = event.getNewValue().toString();

            if(selectRole != null && selectRole.trim().length()>0 && selectTeam != null)
            {
                selectedTeamId = Integer.parseInt(selectTeam);
                userList1 = userDAO.getUserNames(teamId, selectedTeamId,selectRole);
                log.info("userList::::" + userList1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }

       /* String all = "ALL";
        if(userList1.size() > 0)
        {
            userList1.add(all);
        }*/

        return userList1;
    }


    public void changOwnerSearchQuery()
    {

        changeOwerViewList = new ArrayList<PERoster>();

        log.info("in Change Owner :{},{},{},{}",selectTeam,selectRole,selectuser);

        if( selectTeam !=null  && selectTeam.trim().length() > 0 && selectRole != null && selectRole.trim().length() > 0  && selectuser != null && selectuser.trim().length() > 0 )
        {
            changeOwnerQueryList =  userDAO.getChangeOwnerWorkItems(selectTeam,selectRole,selectuser,userList1);

            log.info("in Change Owner if 1 ");
        }
        else
        {
            changeOwnerQueryList = null;

            log.info("in Change Owner if else 1 ");
        }
        if(changeOwnerQueryList != null && changeOwnerQueryList.length() > 0)
        {
            changeOwerViewList = pedbExecute.queryForChangeOwner(changeOwnerQueryList);

            log.info("in Change Owner if 2 ");

        }



    }

    public List<String> userNamesForChangeTo(AjaxBehaviorEvent ajaxBehaviorEvent)
    {
        log.info("this is userNamesForChangeTo method");
        UIComponent source = (UIComponent)ajaxBehaviorEvent.getSource();
        log.info("Value:" + ((HtmlSelectOneMenu) source).getValue());
        String selectTeamName= ((HtmlSelectOneMenu)source).getValue().toString();

        if(selectTeamName != null && selectRole != null)
        {
            selectedTeamId = Integer.parseInt(selectTeamName);
            userNamesForChangeOwnerList = userDAO.getUserNames(teamId, selectedTeamId,selectRole);
            log.info("userNamesForChangeOwnerList:::::::::::"+userNamesForChangeOwnerList);
        }
        return  userNamesForChangeOwnerList;

    }



    public List<PERoster> onReassignBulk()
    {
        log.info("controller cmes to onReassignBulk method ");

        HashMap<String,String> fieldsMap = new HashMap<String, String>();
        List<String>wobNumbersList = new ArrayList<String>();
        Object[] arryOfObjectWobNOs = null;
        String[] stringArrayOfWobNos = null;
        String roleName = null;

        if(selectRole != null && selectRole.trim().length()>0)
        {
            int   selectedRoleId = Integer.parseInt(selectRole);

            roleName = userDAO.getRoleNameFromRoleTable(selectedRoleId);
            log.info("roleName:::::::"+roleName);

        }
        if(selectUserChangeOwner != null && selectUserChangeOwner.trim().length() > 0 && selectuser != null && roleName != null)
        {
            log.info("cntroler in not null condition");
            fieldsMap.put(BPMConstants.BPM_FIELD_ACTION_NAME, "Change Owner");
            fieldsMap.put("NewUser",selectUserChangeOwner);
            fieldsMap.put("LastUser",selectuser);
            fieldsMap.put("CO_Role",roleName);
            fieldsMap.put("Remarks",remark);
        }


        log.info("stringStringHashMap length is:::::"+fieldsMap.size());

        for ( Map.Entry<String, Boolean> entry : checked.entrySet()) {

            if (entry.getValue()==true)
            {
                String wobNo =entry.getKey().toString();
                //log.info("wobno:::::::::::::"+wobNo);
                wobNumbersList.add(wobNo);

            }

        }
        arryOfObjectWobNOs =wobNumbersList.toArray();
        stringArrayOfWobNos = Arrays.copyOf(arryOfObjectWobNOs, arryOfObjectWobNOs.length, String[].class);
        log.info("stringArray length:::::::::::::::::::::::"+stringArrayOfWobNos.length);


        try {
            if(peRosterName != null && stringArrayOfWobNos.length !=0 && fieldsMap.size() !=0)
            {

                bpmInterfaceImpl.batchDispatchCaseFromRoster(peRosterName,stringArrayOfWobNos,fieldsMap);
                log.info("batchDispatch successful.... ");
                changOwnerSearchQuery();
            }

        }
        catch (Exception e)
        {
            wobNumbersList = null;
            arryOfObjectWobNOs = null;
            stringArrayOfWobNos = null;
            roleName = null;
            fieldsMap = null;
        }
        return null;
    }
}

