package com.clevel.selos.businesscontrol;

import com.clevel.selos.dao.master.RoleDAO;
import com.clevel.selos.dao.master.UserDAO;
import com.clevel.selos.dao.working.WorkCaseDAO;
import com.clevel.selos.dao.working.WorkCaseOwnerDAO;
import com.clevel.selos.dao.working.WorkCasePrescreenDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.working.WorkCase;
import com.clevel.selos.model.db.working.WorkCaseOwner;
import com.clevel.selos.model.db.working.WorkCasePrescreen;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.Iterator;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Prashanth Reddy B
 * Date: 4/30/14
 * Time: 11:05 AM
 * To change this template use File | Settings | File Templates.
 */
@Stateless
public class ChangeOwnerControl extends BusinessControl {
    @Inject
    @SELOS
    private Logger log;

    @Inject
    WorkCaseOwnerDAO workCaseOwnerDAO;

    @Inject
    WorkCaseDAO workCaseDAO;

    @Inject
    WorkCasePrescreenDAO workCasePrescreenDAO;

    @Inject
    UserDAO userDAO;

    @Inject
    RoleDAO roleDAO;

    @Inject
    public ChangeOwnerControl(){

    }

    public void updateUserName(String[] wobNumbers, String changeUser, String currentUser){

        log.info("in ChangeOwnerControl class, updateUserName(), WOBNums List :{}, ChangeUser : {}, CurrentUser : {}", wobNumbers.length, changeUser, currentUser);

        try
        {

            User user = userDAO.findByUserName(changeUser);

            int newRoleId = user.getRole().getId();

            User user1 = userDAO.findByUserName(currentUser);

            int currentRoleId = user1.getRole().getId();

            log.info("New role : {}, Current Role : {}", newRoleId, currentRoleId);

            for(int i=0; i<wobNumbers.length; i++)
            {

                WorkCase workCase = workCaseDAO.findByWobNumber(wobNumbers[i]);

                if(workCase !=  null)
                {

                    List<WorkCaseOwner> workCaseOwnerList = workCaseOwnerDAO.findByWorkCaseId(workCase.getId(), currentUser, currentRoleId);

                    log.info("WorkCase List obtained : {}, WobNumber : {}",workCaseOwnerList.size(), wobNumbers[i]);

                    Iterator it = workCaseOwnerList.iterator();

                    while (it.hasNext())
                    {

                        WorkCaseOwner workCaseOwner =   (WorkCaseOwner)it.next();

                        workCaseOwner.setUser(userDAO.findById(changeUser));

                        workCaseOwner.setRole(roleDAO.findById(newRoleId));

                        workCaseOwnerDAO.persist(workCaseOwner);

                    }

                }

                WorkCasePrescreen workCasePrescreen = workCasePrescreenDAO.findByWobNumber(wobNumbers[i]);

                if(workCasePrescreen!=null)
                {

                    List<WorkCaseOwner> workCaseOwnerList = workCaseOwnerDAO.findByWorkCasePreScreenId(workCasePrescreen.getId(), currentUser, currentRoleId);

                    log.info("WorkCasePreScreen List obtained : {}, WobNumber : {}",workCaseOwnerList.size(), wobNumbers[i]);

                    Iterator it = workCaseOwnerList.iterator();

                    while (it.hasNext())
                    {

                        WorkCaseOwner workCaseOwner =   (WorkCaseOwner)it.next();

                        log.info("in while workcase owner prescreen : {}, prescreen id : {}",workCaseOwner.getId(),workCaseOwner.getWorkCasePrescreen().getId());

                        workCaseOwner.setUser(userDAO.findById(changeUser));

                        workCaseOwner.setRole(roleDAO.findById(newRoleId));

                        log.info("Work Case updated , New User : {}, Role:{}",changeUser, newRoleId);
                        log.info("WorkCaseOwner : {}",workCaseOwner);

                        workCaseOwnerDAO.persist(workCaseOwner);

                    }
                }

                else
                {
                    if(workCase!=null)
                    {
                        workCasePrescreen = workCasePrescreenDAO.findByAppNumber(workCase.getAppNumber());
                    }

                    if(workCasePrescreen !=null)
                    {
                        List<WorkCaseOwner> workCaseOwnerList = workCaseOwnerDAO.findByWorkCasePreScreenId(workCasePrescreen.getId(), currentUser, currentRoleId);

                        log.info("WorkCasePreScreen List obtained : {}, WobNumber : {}",workCaseOwnerList.size(), wobNumbers[i]);

                        Iterator it = workCaseOwnerList.iterator();

                        while (it.hasNext())
                        {

                            WorkCaseOwner workCaseOwner =   (WorkCaseOwner)it.next();

                            log.info("in while workcase owner prescreen : {}, prescreen id : {}",workCaseOwner.getId(),workCaseOwner.getWorkCasePrescreen().getId());

                            workCaseOwner.setUser(userDAO.findById(changeUser));

                            workCaseOwner.setRole(roleDAO.findById(newRoleId));

                            workCaseOwnerDAO.persist(workCaseOwner);

                        }
                    }
                }

            }
        }
        catch (Exception e)
        {
            log.error("Error in ChangeControl updateUserName : {}",e);
        }
    }
}
