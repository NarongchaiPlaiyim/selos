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
import com.clevel.selos.model.db.working.*;
import com.clevel.selos.system.message.ExceptionMapping;
import com.clevel.selos.system.message.ExceptionMessage;
import com.clevel.selos.system.message.Message;
import com.clevel.selos.util.Util;
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

    private final static String SPACE_BAR = " ";

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
                String addressLine1 = "-";
                String addressLine2 = "-";
                String addressLine3 = "-";
                String addressLine4 = "-";
                String province = "";
                String zipCode = "";

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

                            List<Address> addressesList = customer.getAddressesList();
                            for(Address address: addressesList){
                                if(address.getAddressType() == customer.getMailingAddressType()){
                                    String addrNo = Util.getStringNotNull(address.getAddressNo());
                                    String building = Util.getStringNotNull(address.getBuilding());
                                    String road = Util.getStringNotNull(address.getRoad());
                                    String moo = Util.getStringNotNull(address.getMoo());
                                    String subDistrict = Util.getStringNotNull(address.getSubDistrict().getName());
                                    String district = Util.getStringNotNull(address.getDistrict().getName());
                                    province = Util.getStringNotNull(address.getProvince().getName());
                                    zipCode = Util.getStringNotNull(address.getPostalCode());

                                    if(!addrNo.trim().equalsIgnoreCase("") && !addrNo.trim().equalsIgnoreCase("-")){
                                        addressLine1 = addrNo;
                                        if(!building.trim().equalsIgnoreCase("") && !building.trim().equalsIgnoreCase("-")){
                                            addressLine1 = addressLine1+SPACE_BAR+building;
                                            if(!road.trim().equalsIgnoreCase("") && !road.trim().equalsIgnoreCase("-")){
                                                addressLine1 = addressLine1+SPACE_BAR+road;
                                                if(!moo.trim().equalsIgnoreCase("") && !moo.trim().equalsIgnoreCase("-")){
                                                    addressLine1 = addressLine1+SPACE_BAR+moo;
                                                }
                                            } else {
                                                if(!moo.trim().equalsIgnoreCase("") && !moo.trim().equalsIgnoreCase("-")){
                                                    addressLine1 = addressLine1+SPACE_BAR+moo;
                                                }
                                            }
                                        } else {
                                            if(!road.trim().equalsIgnoreCase("") && !road.trim().equalsIgnoreCase("-")){
                                                addressLine1 = addressLine1+SPACE_BAR+road;
                                                if(!moo.trim().equalsIgnoreCase("") && !moo.trim().equalsIgnoreCase("-")){
                                                    addressLine1 = addressLine1+SPACE_BAR+moo;
                                                }
                                            } else {
                                                if(!moo.trim().equalsIgnoreCase("") && !moo.trim().equalsIgnoreCase("-")){
                                                    addressLine1 = addressLine1+SPACE_BAR+moo;
                                                }
                                            }
                                        }
                                    } else {
                                        if(!building.trim().equalsIgnoreCase("") && !building.trim().equalsIgnoreCase("-")){
                                            addressLine1 = building;
                                        } else {
                                            if(!road.trim().equalsIgnoreCase("") && !road.trim().equalsIgnoreCase("-")){
                                                addressLine1 = road;
                                            } else {
                                                if(!moo.trim().equalsIgnoreCase("") && !moo.trim().equalsIgnoreCase("-")){
                                                    addressLine1 = moo;
                                                }
                                            }
                                        }
                                    }

                                    addressLine2 = district+SPACE_BAR+subDistrict;

                                    rejectedLetter.setAddrLine1(addressLine1);
                                    rejectedLetter.setAddrLine2(addressLine2);
                                    rejectedLetter.setAddrLine3(addressLine3);
                                    rejectedLetter.setAddrLine4(addressLine4);
                                    rejectedLetter.setProvince(province);
                                    rejectedLetter.setZipcode(zipCode);
                                }
                            }
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
