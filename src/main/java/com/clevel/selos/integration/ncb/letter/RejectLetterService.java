package com.clevel.selos.integration.ncb.letter;

import com.clevel.selos.dao.master.UserDAO;
import com.clevel.selos.dao.report.RejectedLetterDAO;
import com.clevel.selos.dao.working.CustomerDAO;
import com.clevel.selos.dao.working.IndividualDAO;
import com.clevel.selos.dao.working.JuristicDAO;
import com.clevel.selos.dao.working.WorkCaseDAO;
import com.clevel.selos.exception.NCBInterfaceException;
import com.clevel.selos.integration.NCB;
import com.clevel.selos.model.BorrowerType;
import com.clevel.selos.model.db.ext.coms.AgreementAppIndex;
import com.clevel.selos.model.db.master.CustomerEntity;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.report.RejectedLetter;
import com.clevel.selos.model.db.working.Customer;
import com.clevel.selos.model.db.working.Individual;
import com.clevel.selos.model.db.working.Juristic;
import com.clevel.selos.model.db.working.WorkCase;
import com.clevel.selos.system.message.ExceptionMapping;
import com.clevel.selos.system.message.ExceptionMessage;
import com.clevel.selos.system.message.Message;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Stateless
public class RejectLetterService implements Serializable {
    @Inject
    @NCB
    Logger log;

    @Inject
    WorkCaseDAO workCaseDAO;

    @Inject
    UserDAO userDAO;

    @Inject
    CustomerDAO customerDAO;

    @Inject
    IndividualDAO individualDAO;

    @Inject
    JuristicDAO juristicDAO;

    @Inject
    RejectedLetterDAO rejectedLetterDAO;

    @Inject
    @ExceptionMessage
    Message msg;

    @Inject
    public RejectLetterService() {

    }

    public boolean extractRejectedLetterData(String userId, long workCaseId) throws Exception{
        WorkCase workCase = workCaseDAO.findById(workCaseId);
        if(workCase!=null && workCase.getId()>0){
            RejectedLetter rejectedLetter = new RejectedLetter();
            User user = userDAO.findUserByID(userId);
            List<Customer> customerBorrowerList = customerDAO.findBorrowerByWorkCaseId(workCaseId);
            if(customerBorrowerList!=null && customerBorrowerList.size()>0){
                int numberOfBr = 0;
                String citizenId = "";
                String title = null;
                String nameTh = null;
                String lastNameTh = null;
                String borrowerName = "";

                rejectedLetter.setWorkCase(workCase);
                rejectedLetter.setAppNumber(workCase.getAppNumber());
                rejectedLetter.setHubCode(user.getBuCode()); //TODO: verify data
                rejectedLetter.setZoneOfficePhone(user.getPhoneNumber()); //TODO: verify data
                for(Customer customer: customerBorrowerList) {
                    if(customer.getTitle()!=null && customer.getTitle().getTitleTh()!=null)
                        title = customer.getTitle().getTitleTh();
                    nameTh = customer.getNameTh();
                    lastNameTh = customer.getLastNameTh();

                    if(customer.getCustomerEntity().getId() == BorrowerType.INDIVIDUAL.value()){
                        Individual individual = customer.getIndividual();
                        if(individual!=null)
                            citizenId = individual.getCitizenId();
                    } else {
                        Juristic juristic = customer.getJuristic();
                        if(juristic!=null)
                            citizenId = juristic.getRegistrationId();
                    }
                    if(title!=null)
                        borrowerName = title;
                    borrowerName = borrowerName.concat(nameTh);
                    if(lastNameTh!=null && !lastNameTh.equalsIgnoreCase(""))
                        borrowerName = borrowerName.concat(" ").concat(lastNameTh);

                    numberOfBr = numberOfBr+1;

                    switch (numberOfBr){
                        case 1:
                            rejectedLetter.setBorrower1(borrowerName);
                            rejectedLetter.setCitizenId1(citizenId);
                            break;
                        case 2:
                            rejectedLetter.setBorrower2(borrowerName);
                            rejectedLetter.setCitizenId2(citizenId);
                            break;
                        case 3:
                            rejectedLetter.setBorrower3(borrowerName);
                            rejectedLetter.setCitizenId3(citizenId);
                            break;
                        case 4:
                            rejectedLetter.setBorrower4(borrowerName);
                            rejectedLetter.setCitizenId4(citizenId);
                            break;
                        default:
                            break;
                    }
                }
            }

            rejectedLetterDAO.save(rejectedLetter);
            return true;
        } else {
            throw new NCBInterfaceException(new Exception(msg.get(ExceptionMapping.NCB_WORKCASE_NOT_FOUND, workCaseId+"")),ExceptionMapping.NCB_WORKCASE_NOT_FOUND, msg.get(ExceptionMapping.NCB_WORKCASE_NOT_FOUND, workCaseId+""));
        }
    }
}
