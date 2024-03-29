package com.clevel.selos.integration.dwh.bankstatement;

import com.clevel.selos.dao.ext.dwh.*;
import com.clevel.selos.dao.system.SystemParameterDAO;
import com.clevel.selos.integration.DWH;
import com.clevel.selos.integration.dwh.bankstatement.model.DWHBankStatement;
import com.clevel.selos.integration.dwh.bankstatement.model.DWHBankStatementResult;
import com.clevel.selos.model.ActionResult;
import com.clevel.selos.model.db.ext.bankstatement.*;
import com.clevel.selos.model.db.system.SystemParameter;
import com.clevel.selos.system.Config;
import com.clevel.selos.system.message.ExceptionMapping;
import com.clevel.selos.system.message.ExceptionMessage;
import com.clevel.selos.system.message.Message;
import com.clevel.selos.util.DateTimeUtil;
import com.clevel.selos.util.Util;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BankStatementService implements Serializable {
    @Inject
    @DWH
    Logger log;

    @Inject
    @Config(name = "interface.dwh.bank.last.month.sysparam")
    String lastMonth;
    @Inject
    @Config(name = "interface.dwh.bank.m1.table.sysparam")
    String month1;
    @Inject
    @Config(name = "interface.dwh.bank.m2.table.sysparam")
    String month2;
    @Inject
    @Config(name = "interface.dwh.bank.m3.table.sysparam")
    String month3;
    @Inject
    @Config(name = "interface.dwh.bank.m4.table.sysparam")
    String month4;
    @Inject
    @Config(name = "interface.dwh.bank.m5.table.sysparam")
    String month5;
    @Inject
    @Config(name = "interface.dwh.bank.m6.table.sysparam")
    String month6;
    @Inject
    @Config(name = "interface.dwh.bank.m7.table.sysparam")
    String month7;
    @Inject
    @Config(name = "interface.dwh.bank.m8.table.sysparam")
    String month8;
    @Inject
    @Config(name = "interface.dwh.bank.m9.table.sysparam")
    String month9;
    @Inject
    @Config(name = "interface.dwh.bank.m10.table.sysparam")
    String month10;
    @Inject
    @Config(name = "interface.dwh.bank.m11.table.sysparam")
    String month11;
    @Inject
    @Config(name = "interface.dwh.bank.m12.table.sysparam")
    String month12;
    @Inject
    @Config(name = "interface.dwh.bank.m13.table.sysparam")
    String month13;
    @Inject
    @Config(name = "interface.dwh.bank.tmp.table.sysparam")
    String monthTmp;

    @Inject
    SystemParameterDAO systemParameterDAO;
    @Inject
    BankStatement1DAO bankStatement1DAO;
    @Inject
    BankStatement2DAO bankStatement2DAO;
    @Inject
    BankStatement3DAO bankStatement3DAO;
    @Inject
    BankStatement4DAO bankStatement4DAO;
    @Inject
    BankStatement5DAO bankStatement5DAO;
    @Inject
    BankStatement6DAO bankStatement6DAO;
    @Inject
    BankStatement7DAO bankStatement7DAO;
    @Inject
    BankStatement8DAO bankStatement8DAO;
    @Inject
    BankStatement9DAO bankStatement9DAO;
    @Inject
    BankStatement10DAO bankStatement10DAO;
    @Inject
    BankStatement11DAO bankStatement11DAO;
    @Inject
    BankStatement12DAO bankStatement12DAO;
    @Inject
    BankStatement13DAO bankStatement13DAO;
    @Inject
    BankStatement14DAO bankStatement14DAO;

    @Inject
    @ExceptionMessage
    Message exceptionMsg;

    private static final String FORMAT_MONTH_YEAR = "MMyyyy";


    @Inject
    public BankStatementService() {

    }

    public DWHBankStatementResult getBankStatementData(String accountNumber, Date fromDate, int numberOfMonth) {
        DWHBankStatementResult bankStatementResult = new DWHBankStatementResult();
        if (fromDate != null && numberOfMonth > 0 && numberOfMonth < 13 && !Util.isEmpty(accountNumber)) {
            //find last month on system param
            SystemParameter systemParameter = systemParameterDAO.findByParameterName(lastMonth);
            if (systemParameter != null) {
                Date lastMonth = Util.strToDateFormat(systemParameter.getValue(), FORMAT_MONTH_YEAR);
                int numOfDifMonth = DateTimeUtil.monthBetween2DatesWithNoDate(fromDate, lastMonth);
                String firstMonth = "";
                if (numOfDifMonth == 0) {
                    firstMonth = month1;
                } else if (numOfDifMonth == 1) {
                    firstMonth = month2;
                } else if (numOfDifMonth == 2) {
                    firstMonth = month3;
                } else if (numOfDifMonth == 3 && numberOfMonth < 12) {
                    firstMonth = month4;
                } else if (numOfDifMonth == 4 && numberOfMonth < 11) {
                    firstMonth = month5;
                } else if (numOfDifMonth == 5 && numberOfMonth < 10) {
                    firstMonth = month6;
                } else if (numOfDifMonth == 6 && numberOfMonth < 9) {
                    firstMonth = month7;
                } else if (numOfDifMonth == 7 && numberOfMonth < 8) {
                    firstMonth = month8;
                } else if (numOfDifMonth == 8 && numberOfMonth < 7) {
                    firstMonth = month9;
                } else if (numOfDifMonth == 9 && numberOfMonth < 6) {
                    firstMonth = month10;
                } else if (numOfDifMonth == 10 && numberOfMonth < 5) {
                    firstMonth = month11;
                } else if (numOfDifMonth == 11 && numberOfMonth < 4) {
                    firstMonth = month12;
                } else if (numOfDifMonth == 12 && numberOfMonth < 3) {
                    firstMonth = month13;
                } else if (numOfDifMonth == 13 && numberOfMonth < 2) {
                    firstMonth = monthTmp;
                } else {
                    log.debug("Action result is failed!");
                    log.debug("Data in DWH is not enough.");
                    bankStatementResult.setActionResult(ActionResult.FAILED);
                    //bankStatementResult.setReason(exceptionMsg.get(ExceptionMapping.DWH_DATA_NOT_ENOUGH));
                    bankStatementResult.setReason("Data in Data Warehouse is not enough.");
                    bankStatementResult.setBankStatementList(new ArrayList<DWHBankStatement>());
                }

                //get first table
                if (!Util.isEmpty(firstMonth)) {
                    systemParameter = systemParameterDAO.findByParameterName(firstMonth);
                    if (systemParameter != null) {
                        List<DWHBankStatement> bankStatementList = getList(accountNumber, systemParameter.getValue(), numberOfMonth);
                        if (bankStatementList != null && bankStatementList.size() > 0) {
                            log.debug("Action result is success.");
                            bankStatementResult.setActionResult(ActionResult.SUCCESS);
                            bankStatementResult.setBankStatementList(bankStatementList);
                        } else {
                            log.debug("Action result is failed!");
                            log.debug("DWH data not found.");
                            bankStatementResult.setActionResult(ActionResult.FAILED);
                            bankStatementResult.setReason(exceptionMsg.get(ExceptionMapping.DWH_DATA_NOT_FOUND));
                            bankStatementResult.setBankStatementList(new ArrayList<DWHBankStatement>());
                        }
                    } else {
                        log.debug("Action result is failed!");
                        log.debug("Not found system parameter by first month.");
                        bankStatementResult.setActionResult(ActionResult.FAILED);
                        bankStatementResult.setReason(exceptionMsg.get(ExceptionMapping.NOT_FOUND_SYSTEM_PARAM));
                        bankStatementResult.setBankStatementList(new ArrayList<DWHBankStatement>());
                    }
                }
            } else {
                log.debug("Action result is failed!");
                log.debug("Not found system parameter by last month.");
                bankStatementResult.setActionResult(ActionResult.FAILED);
                bankStatementResult.setReason(exceptionMsg.get(ExceptionMapping.NOT_FOUND_SYSTEM_PARAM));
                bankStatementResult.setBankStatementList(new ArrayList<DWHBankStatement>());
            }
        } else {
            log.debug("Action result is failed!");
            log.debug("DWH invalid input.");
            bankStatementResult.setActionResult(ActionResult.FAILED);
            bankStatementResult.setReason(exceptionMsg.get(ExceptionMapping.DWH_INVALID_INPUT));
            bankStatementResult.setBankStatementList(new ArrayList<DWHBankStatement>());
        }
        return bankStatementResult;
    }

    public List<DWHBankStatement> getList(String accountNumber, String tableNumber, int numberOfMonth) {
        List<DWHBankStatement> bankStatementList = new ArrayList<DWHBankStatement>();
        if (numberOfMonth <= 12) {
            if (tableNumber.equalsIgnoreCase("1")) {
                if (numberOfMonth > 0) { //get from table month1
                    BankStatement1 bankStatement1 = bankStatement1DAO.getByAccountNumber(accountNumber);
                    DWHBankStatement bankStatement = transform(bankStatement1);
                    bankStatementList.add(bankStatement);
                }
                if (numberOfMonth > 1) { //get from table month2
                    BankStatement2 bankStatement2 = bankStatement2DAO.getByAccountNumber(accountNumber);
                    DWHBankStatement bankStatement = transform(bankStatement2);
                    bankStatementList.add(bankStatement);
                }
                if (numberOfMonth > 2) { //get from table month3
                    BankStatement3 bankStatement3 = bankStatement3DAO.getByAccountNumber(accountNumber);
                    DWHBankStatement bankStatement = transform(bankStatement3);
                    bankStatementList.add(bankStatement);
                }
                if (numberOfMonth > 3) { //get from table month4
                    BankStatement4 bankStatement4 = bankStatement4DAO.getByAccountNumber(accountNumber);
                    DWHBankStatement bankStatement = transform(bankStatement4);
                    bankStatementList.add(bankStatement);
                }
                if (numberOfMonth > 4) { //get from table month5
                    BankStatement5 bankStatement5 = bankStatement5DAO.getByAccountNumber(accountNumber);
                    DWHBankStatement bankStatement = transform(bankStatement5);
                    bankStatementList.add(bankStatement);
                }
                if (numberOfMonth > 5) { //get from table month6
                    BankStatement6 bankStatement6 = bankStatement6DAO.getByAccountNumber(accountNumber);
                    DWHBankStatement bankStatement = transform(bankStatement6);
                    bankStatementList.add(bankStatement);
                }
                if (numberOfMonth > 6) { //get from table month7
                    BankStatement7 bankStatement7 = bankStatement7DAO.getByAccountNumber(accountNumber);
                    DWHBankStatement bankStatement = transform(bankStatement7);
                    bankStatementList.add(bankStatement);
                }
                if (numberOfMonth > 7) { //get from table month8
                    BankStatement8 bankStatement8 = bankStatement8DAO.getByAccountNumber(accountNumber);
                    DWHBankStatement bankStatement = transform(bankStatement8);
                    bankStatementList.add(bankStatement);
                }
                if (numberOfMonth > 8) { //get from table month9
                    BankStatement9 bankStatement9 = bankStatement9DAO.getByAccountNumber(accountNumber);
                    DWHBankStatement bankStatement = transform(bankStatement9);
                    bankStatementList.add(bankStatement);
                }
                if (numberOfMonth > 9) { //get from table month10
                    BankStatement10 bankStatement10 = bankStatement10DAO.getByAccountNumber(accountNumber);
                    DWHBankStatement bankStatement = transform(bankStatement10);
                    bankStatementList.add(bankStatement);
                }
                if (numberOfMonth > 10) { //get from table month11
                    BankStatement11 bankStatement11 = bankStatement11DAO.getByAccountNumber(accountNumber);
                    DWHBankStatement bankStatement = transform(bankStatement11);
                    bankStatementList.add(bankStatement);
                }
                if (numberOfMonth > 11) { //get from table month12
                    BankStatement12 bankStatement12 = bankStatement12DAO.getByAccountNumber(accountNumber);
                    DWHBankStatement bankStatement = transform(bankStatement12);
                    bankStatementList.add(bankStatement);
                }
            } else if (tableNumber.equalsIgnoreCase("2")) {
                if (numberOfMonth > 0) { //get from table month2
                    BankStatement2 bankStatement2 = bankStatement2DAO.getByAccountNumber(accountNumber);
                    DWHBankStatement bankStatement = transform(bankStatement2);
                    bankStatementList.add(bankStatement);
                }
                if (numberOfMonth > 1) { //get from table month3
                    BankStatement3 bankStatement3 = bankStatement3DAO.getByAccountNumber(accountNumber);
                    DWHBankStatement bankStatement = transform(bankStatement3);
                    bankStatementList.add(bankStatement);
                }
                if (numberOfMonth > 2) { //get from table month4
                    BankStatement4 bankStatement4 = bankStatement4DAO.getByAccountNumber(accountNumber);
                    DWHBankStatement bankStatement = transform(bankStatement4);
                    bankStatementList.add(bankStatement);
                }
                if (numberOfMonth > 3) { //get from table month5
                    BankStatement5 bankStatement5 = bankStatement5DAO.getByAccountNumber(accountNumber);
                    DWHBankStatement bankStatement = transform(bankStatement5);
                    bankStatementList.add(bankStatement);
                }
                if (numberOfMonth > 4) { //get from table month6
                    BankStatement6 bankStatement6 = bankStatement6DAO.getByAccountNumber(accountNumber);
                    DWHBankStatement bankStatement = transform(bankStatement6);
                    bankStatementList.add(bankStatement);
                }
                if (numberOfMonth > 5) { //get from table month7
                    BankStatement7 bankStatement7 = bankStatement7DAO.getByAccountNumber(accountNumber);
                    DWHBankStatement bankStatement = transform(bankStatement7);
                    bankStatementList.add(bankStatement);
                }
                if (numberOfMonth > 6) { //get from table month8
                    BankStatement8 bankStatement8 = bankStatement8DAO.getByAccountNumber(accountNumber);
                    DWHBankStatement bankStatement = transform(bankStatement8);
                    bankStatementList.add(bankStatement);
                }
                if (numberOfMonth > 7) { //get from table month9
                    BankStatement9 bankStatement9 = bankStatement9DAO.getByAccountNumber(accountNumber);
                    DWHBankStatement bankStatement = transform(bankStatement9);
                    bankStatementList.add(bankStatement);
                }
                if (numberOfMonth > 8) { //get from table month10
                    BankStatement10 bankStatement10 = bankStatement10DAO.getByAccountNumber(accountNumber);
                    DWHBankStatement bankStatement = transform(bankStatement10);
                    bankStatementList.add(bankStatement);
                }
                if (numberOfMonth > 9) { //get from table month11
                    BankStatement11 bankStatement11 = bankStatement11DAO.getByAccountNumber(accountNumber);
                    DWHBankStatement bankStatement = transform(bankStatement11);
                    bankStatementList.add(bankStatement);
                }
                if (numberOfMonth > 10) { //get from table month12
                    BankStatement12 bankStatement12 = bankStatement12DAO.getByAccountNumber(accountNumber);
                    DWHBankStatement bankStatement = transform(bankStatement12);
                    bankStatementList.add(bankStatement);
                }
                if (numberOfMonth > 11) { //get from table month13
                    BankStatement13 bankStatement13 = bankStatement13DAO.getByAccountNumber(accountNumber);
                    DWHBankStatement bankStatement = transform(bankStatement13);
                    bankStatementList.add(bankStatement);
                }
            } else if (tableNumber.equalsIgnoreCase("3")) {
                if (numberOfMonth > 0) { //get from table month3
                    BankStatement3 bankStatement3 = bankStatement3DAO.getByAccountNumber(accountNumber);
                    DWHBankStatement bankStatement = transform(bankStatement3);
                    bankStatementList.add(bankStatement);
                }
                if (numberOfMonth > 1) { //get from table month4
                    BankStatement4 bankStatement4 = bankStatement4DAO.getByAccountNumber(accountNumber);
                    DWHBankStatement bankStatement = transform(bankStatement4);
                    bankStatementList.add(bankStatement);
                }
                if (numberOfMonth > 2) { //get from table month5
                    BankStatement5 bankStatement5 = bankStatement5DAO.getByAccountNumber(accountNumber);
                    DWHBankStatement bankStatement = transform(bankStatement5);
                    bankStatementList.add(bankStatement);
                }
                if (numberOfMonth > 3) { //get from table month6
                    BankStatement6 bankStatement6 = bankStatement6DAO.getByAccountNumber(accountNumber);
                    DWHBankStatement bankStatement = transform(bankStatement6);
                    bankStatementList.add(bankStatement);
                }
                if (numberOfMonth > 4) { //get from table month7
                    BankStatement7 bankStatement7 = bankStatement7DAO.getByAccountNumber(accountNumber);
                    DWHBankStatement bankStatement = transform(bankStatement7);
                    bankStatementList.add(bankStatement);
                }
                if (numberOfMonth > 5) { //get from table month8
                    BankStatement8 bankStatement8 = bankStatement8DAO.getByAccountNumber(accountNumber);
                    DWHBankStatement bankStatement = transform(bankStatement8);
                    bankStatementList.add(bankStatement);
                }
                if (numberOfMonth > 6) { //get from table month9
                    BankStatement9 bankStatement9 = bankStatement9DAO.getByAccountNumber(accountNumber);
                    DWHBankStatement bankStatement = transform(bankStatement9);
                    bankStatementList.add(bankStatement);
                }
                if (numberOfMonth > 7) { //get from table month10
                    BankStatement10 bankStatement10 = bankStatement10DAO.getByAccountNumber(accountNumber);
                    DWHBankStatement bankStatement = transform(bankStatement10);
                    bankStatementList.add(bankStatement);
                }
                if (numberOfMonth > 8) { //get from table month11
                    BankStatement11 bankStatement11 = bankStatement11DAO.getByAccountNumber(accountNumber);
                    DWHBankStatement bankStatement = transform(bankStatement11);
                    bankStatementList.add(bankStatement);
                }
                if (numberOfMonth > 9) { //get from table month12
                    BankStatement12 bankStatement12 = bankStatement12DAO.getByAccountNumber(accountNumber);
                    DWHBankStatement bankStatement = transform(bankStatement12);
                    bankStatementList.add(bankStatement);
                }
                if (numberOfMonth > 10) { //get from table month13
                    BankStatement13 bankStatement13 = bankStatement13DAO.getByAccountNumber(accountNumber);
                    DWHBankStatement bankStatement = transform(bankStatement13);
                    bankStatementList.add(bankStatement);
                }
                if (numberOfMonth > 11) { //get from table month14
                    BankStatement14 bankStatement14 = bankStatement14DAO.getByAccountNumber(accountNumber);
                    DWHBankStatement bankStatement = transform(bankStatement14);
                    bankStatementList.add(bankStatement);
                }
            } else if (tableNumber.equalsIgnoreCase("4")) {
                if (numberOfMonth > 0) { //get from table month4
                    BankStatement4 bankStatement4 = bankStatement4DAO.getByAccountNumber(accountNumber);
                    DWHBankStatement bankStatement = transform(bankStatement4);
                    bankStatementList.add(bankStatement);
                }
                if (numberOfMonth > 1) { //get from table month5
                    BankStatement5 bankStatement5 = bankStatement5DAO.getByAccountNumber(accountNumber);
                    DWHBankStatement bankStatement = transform(bankStatement5);
                    bankStatementList.add(bankStatement);
                }
                if (numberOfMonth > 2) { //get from table month6
                    BankStatement6 bankStatement6 = bankStatement6DAO.getByAccountNumber(accountNumber);
                    DWHBankStatement bankStatement = transform(bankStatement6);
                    bankStatementList.add(bankStatement);
                }
                if (numberOfMonth > 3) { //get from table month7
                    BankStatement7 bankStatement7 = bankStatement7DAO.getByAccountNumber(accountNumber);
                    DWHBankStatement bankStatement = transform(bankStatement7);
                    bankStatementList.add(bankStatement);
                }
                if (numberOfMonth > 4) { //get from table month8
                    BankStatement8 bankStatement8 = bankStatement8DAO.getByAccountNumber(accountNumber);
                    DWHBankStatement bankStatement = transform(bankStatement8);
                    bankStatementList.add(bankStatement);
                }
                if (numberOfMonth > 5) { //get from table month9
                    BankStatement9 bankStatement9 = bankStatement9DAO.getByAccountNumber(accountNumber);
                    DWHBankStatement bankStatement = transform(bankStatement9);
                    bankStatementList.add(bankStatement);
                }
                if (numberOfMonth > 6) { //get from table month10
                    BankStatement10 bankStatement10 = bankStatement10DAO.getByAccountNumber(accountNumber);
                    DWHBankStatement bankStatement = transform(bankStatement10);
                    bankStatementList.add(bankStatement);
                }
                if (numberOfMonth > 7) { //get from table month11
                    BankStatement11 bankStatement11 = bankStatement11DAO.getByAccountNumber(accountNumber);
                    DWHBankStatement bankStatement = transform(bankStatement11);
                    bankStatementList.add(bankStatement);
                }
                if (numberOfMonth > 8) { //get from table month12
                    BankStatement12 bankStatement12 = bankStatement12DAO.getByAccountNumber(accountNumber);
                    DWHBankStatement bankStatement = transform(bankStatement12);
                    bankStatementList.add(bankStatement);
                }
                if (numberOfMonth > 9) { //get from table month13
                    BankStatement13 bankStatement13 = bankStatement13DAO.getByAccountNumber(accountNumber);
                    DWHBankStatement bankStatement = transform(bankStatement13);
                    bankStatementList.add(bankStatement);
                }
                if (numberOfMonth > 10) { //get from table month14
                    BankStatement14 bankStatement14 = bankStatement14DAO.getByAccountNumber(accountNumber);
                    DWHBankStatement bankStatement = transform(bankStatement14);
                    bankStatementList.add(bankStatement);
                }
                if (numberOfMonth > 11) { //get from table month1
                    BankStatement1 bankStatement1 = bankStatement1DAO.getByAccountNumber(accountNumber);
                    DWHBankStatement bankStatement = transform(bankStatement1);
                    bankStatementList.add(bankStatement);
                }
            } else if (tableNumber.equalsIgnoreCase("5")) {
                if (numberOfMonth > 0) { //get from table month5
                    BankStatement5 bankStatement5 = bankStatement5DAO.getByAccountNumber(accountNumber);
                    DWHBankStatement bankStatement = transform(bankStatement5);
                    bankStatementList.add(bankStatement);
                }
                if (numberOfMonth > 1) { //get from table month6
                    BankStatement6 bankStatement6 = bankStatement6DAO.getByAccountNumber(accountNumber);
                    DWHBankStatement bankStatement = transform(bankStatement6);
                    bankStatementList.add(bankStatement);
                }
                if (numberOfMonth > 2) { //get from table month7
                    BankStatement7 bankStatement7 = bankStatement7DAO.getByAccountNumber(accountNumber);
                    DWHBankStatement bankStatement = transform(bankStatement7);
                    bankStatementList.add(bankStatement);
                }
                if (numberOfMonth > 3) { //get from table month8
                    BankStatement8 bankStatement8 = bankStatement8DAO.getByAccountNumber(accountNumber);
                    DWHBankStatement bankStatement = transform(bankStatement8);
                    bankStatementList.add(bankStatement);
                }
                if (numberOfMonth > 4) { //get from table month9
                    BankStatement9 bankStatement9 = bankStatement9DAO.getByAccountNumber(accountNumber);
                    DWHBankStatement bankStatement = transform(bankStatement9);
                    bankStatementList.add(bankStatement);
                }
                if (numberOfMonth > 5) { //get from table month10
                    BankStatement10 bankStatement10 = bankStatement10DAO.getByAccountNumber(accountNumber);
                    DWHBankStatement bankStatement = transform(bankStatement10);
                    bankStatementList.add(bankStatement);
                }
                if (numberOfMonth > 6) { //get from table month11
                    BankStatement11 bankStatement11 = bankStatement11DAO.getByAccountNumber(accountNumber);
                    DWHBankStatement bankStatement = transform(bankStatement11);
                    bankStatementList.add(bankStatement);
                }
                if (numberOfMonth > 7) { //get from table month12
                    BankStatement12 bankStatement12 = bankStatement12DAO.getByAccountNumber(accountNumber);
                    DWHBankStatement bankStatement = transform(bankStatement12);
                    bankStatementList.add(bankStatement);
                }
                if (numberOfMonth > 8) { //get from table month13
                    BankStatement13 bankStatement13 = bankStatement13DAO.getByAccountNumber(accountNumber);
                    DWHBankStatement bankStatement = transform(bankStatement13);
                    bankStatementList.add(bankStatement);
                }
                if (numberOfMonth > 9) { //get from table month14
                    BankStatement14 bankStatement14 = bankStatement14DAO.getByAccountNumber(accountNumber);
                    DWHBankStatement bankStatement = transform(bankStatement14);
                    bankStatementList.add(bankStatement);
                }
                if (numberOfMonth > 10) { //get from table month1
                    BankStatement1 bankStatement1 = bankStatement1DAO.getByAccountNumber(accountNumber);
                    DWHBankStatement bankStatement = transform(bankStatement1);
                    bankStatementList.add(bankStatement);
                }
                if (numberOfMonth > 11) { //get from table month2
                    BankStatement2 bankStatement2 = bankStatement2DAO.getByAccountNumber(accountNumber);
                    DWHBankStatement bankStatement = transform(bankStatement2);
                    bankStatementList.add(bankStatement);
                }
            } else if (tableNumber.equalsIgnoreCase("6")) {
                if (numberOfMonth > 0) { //get from table month6
                    BankStatement6 bankStatement6 = bankStatement6DAO.getByAccountNumber(accountNumber);
                    DWHBankStatement bankStatement = transform(bankStatement6);
                    bankStatementList.add(bankStatement);
                }
                if (numberOfMonth > 1) { //get from table month7
                    BankStatement7 bankStatement7 = bankStatement7DAO.getByAccountNumber(accountNumber);
                    DWHBankStatement bankStatement = transform(bankStatement7);
                    bankStatementList.add(bankStatement);
                }
                if (numberOfMonth > 2) { //get from table month8
                    BankStatement8 bankStatement8 = bankStatement8DAO.getByAccountNumber(accountNumber);
                    DWHBankStatement bankStatement = transform(bankStatement8);
                    bankStatementList.add(bankStatement);
                }
                if (numberOfMonth > 3) { //get from table month9
                    BankStatement9 bankStatement9 = bankStatement9DAO.getByAccountNumber(accountNumber);
                    DWHBankStatement bankStatement = transform(bankStatement9);
                    bankStatementList.add(bankStatement);
                }
                if (numberOfMonth > 4) { //get from table month10
                    BankStatement10 bankStatement10 = bankStatement10DAO.getByAccountNumber(accountNumber);
                    DWHBankStatement bankStatement = transform(bankStatement10);
                    bankStatementList.add(bankStatement);
                }
                if (numberOfMonth > 5) { //get from table month11
                    BankStatement11 bankStatement11 = bankStatement11DAO.getByAccountNumber(accountNumber);
                    DWHBankStatement bankStatement = transform(bankStatement11);
                    bankStatementList.add(bankStatement);
                }
                if (numberOfMonth > 6) { //get from table month12
                    BankStatement12 bankStatement12 = bankStatement12DAO.getByAccountNumber(accountNumber);
                    DWHBankStatement bankStatement = transform(bankStatement12);
                    bankStatementList.add(bankStatement);
                }
                if (numberOfMonth > 7) { //get from table month13
                    BankStatement13 bankStatement13 = bankStatement13DAO.getByAccountNumber(accountNumber);
                    DWHBankStatement bankStatement = transform(bankStatement13);
                    bankStatementList.add(bankStatement);
                }
                if (numberOfMonth > 8) { //get from table month14
                    BankStatement14 bankStatement14 = bankStatement14DAO.getByAccountNumber(accountNumber);
                    DWHBankStatement bankStatement = transform(bankStatement14);
                    bankStatementList.add(bankStatement);
                }
                if (numberOfMonth > 9) { //get from table month1
                    BankStatement1 bankStatement1 = bankStatement1DAO.getByAccountNumber(accountNumber);
                    DWHBankStatement bankStatement = transform(bankStatement1);
                    bankStatementList.add(bankStatement);
                }
                if (numberOfMonth > 10) { //get from table month2
                    BankStatement2 bankStatement2 = bankStatement2DAO.getByAccountNumber(accountNumber);
                    DWHBankStatement bankStatement = transform(bankStatement2);
                    bankStatementList.add(bankStatement);
                }
                if (numberOfMonth > 11) { //get from table month3
                    BankStatement3 bankStatement3 = bankStatement3DAO.getByAccountNumber(accountNumber);
                    DWHBankStatement bankStatement = transform(bankStatement3);
                    bankStatementList.add(bankStatement);
                }
            } else if (tableNumber.equalsIgnoreCase("7")) {
                if (numberOfMonth > 0) { //get from table month7
                    BankStatement7 bankStatement7 = bankStatement7DAO.getByAccountNumber(accountNumber);
                    DWHBankStatement bankStatement = transform(bankStatement7);
                    bankStatementList.add(bankStatement);
                }
                if (numberOfMonth > 1) { //get from table month8
                    BankStatement8 bankStatement8 = bankStatement8DAO.getByAccountNumber(accountNumber);
                    DWHBankStatement bankStatement = transform(bankStatement8);
                    bankStatementList.add(bankStatement);
                }
                if (numberOfMonth > 2) { //get from table month9
                    BankStatement9 bankStatement9 = bankStatement9DAO.getByAccountNumber(accountNumber);
                    DWHBankStatement bankStatement = transform(bankStatement9);
                    bankStatementList.add(bankStatement);
                }
                if (numberOfMonth > 3) { //get from table month10
                    BankStatement10 bankStatement10 = bankStatement10DAO.getByAccountNumber(accountNumber);
                    DWHBankStatement bankStatement = transform(bankStatement10);
                    bankStatementList.add(bankStatement);
                }
                if (numberOfMonth > 4) { //get from table month11
                    BankStatement11 bankStatement11 = bankStatement11DAO.getByAccountNumber(accountNumber);
                    DWHBankStatement bankStatement = transform(bankStatement11);
                    bankStatementList.add(bankStatement);
                }
                if (numberOfMonth > 5) { //get from table month12
                    BankStatement12 bankStatement12 = bankStatement12DAO.getByAccountNumber(accountNumber);
                    DWHBankStatement bankStatement = transform(bankStatement12);
                    bankStatementList.add(bankStatement);
                }
                if (numberOfMonth > 6) { //get from table month13
                    BankStatement13 bankStatement13 = bankStatement13DAO.getByAccountNumber(accountNumber);
                    DWHBankStatement bankStatement = transform(bankStatement13);
                    bankStatementList.add(bankStatement);
                }
                if (numberOfMonth > 7) { //get from table month14
                    BankStatement14 bankStatement14 = bankStatement14DAO.getByAccountNumber(accountNumber);
                    DWHBankStatement bankStatement = transform(bankStatement14);
                    bankStatementList.add(bankStatement);
                }
                if (numberOfMonth > 8) { //get from table month1
                    BankStatement1 bankStatement1 = bankStatement1DAO.getByAccountNumber(accountNumber);
                    DWHBankStatement bankStatement = transform(bankStatement1);
                    bankStatementList.add(bankStatement);
                }
                if (numberOfMonth > 9) { //get from table month2
                    BankStatement2 bankStatement2 = bankStatement2DAO.getByAccountNumber(accountNumber);
                    DWHBankStatement bankStatement = transform(bankStatement2);
                    bankStatementList.add(bankStatement);
                }
                if (numberOfMonth > 10) { //get from table month3
                    BankStatement3 bankStatement3 = bankStatement3DAO.getByAccountNumber(accountNumber);
                    DWHBankStatement bankStatement = transform(bankStatement3);
                    bankStatementList.add(bankStatement);
                }
                if (numberOfMonth > 11) { //get from table month4
                    BankStatement4 bankStatement4 = bankStatement4DAO.getByAccountNumber(accountNumber);
                    DWHBankStatement bankStatement = transform(bankStatement4);
                    bankStatementList.add(bankStatement);
                }
            } else if (tableNumber.equalsIgnoreCase("8")) {
                if (numberOfMonth > 0) { //get from table month8
                    BankStatement8 bankStatement8 = bankStatement8DAO.getByAccountNumber(accountNumber);
                    DWHBankStatement bankStatement = transform(bankStatement8);
                    bankStatementList.add(bankStatement);
                }
                if (numberOfMonth > 1) { //get from table month9
                    BankStatement9 bankStatement9 = bankStatement9DAO.getByAccountNumber(accountNumber);
                    DWHBankStatement bankStatement = transform(bankStatement9);
                    bankStatementList.add(bankStatement);
                }
                if (numberOfMonth > 2) { //get from table month10
                    BankStatement10 bankStatement10 = bankStatement10DAO.getByAccountNumber(accountNumber);
                    DWHBankStatement bankStatement = transform(bankStatement10);
                    bankStatementList.add(bankStatement);
                }
                if (numberOfMonth > 3) { //get from table month11
                    BankStatement11 bankStatement11 = bankStatement11DAO.getByAccountNumber(accountNumber);
                    DWHBankStatement bankStatement = transform(bankStatement11);
                    bankStatementList.add(bankStatement);
                }
                if (numberOfMonth > 4) { //get from table month12
                    BankStatement12 bankStatement12 = bankStatement12DAO.getByAccountNumber(accountNumber);
                    DWHBankStatement bankStatement = transform(bankStatement12);
                    bankStatementList.add(bankStatement);
                }
                if (numberOfMonth > 5) { //get from table month13
                    BankStatement13 bankStatement13 = bankStatement13DAO.getByAccountNumber(accountNumber);
                    DWHBankStatement bankStatement = transform(bankStatement13);
                    bankStatementList.add(bankStatement);
                }
                if (numberOfMonth > 6) { //get from table month14
                    BankStatement14 bankStatement14 = bankStatement14DAO.getByAccountNumber(accountNumber);
                    DWHBankStatement bankStatement = transform(bankStatement14);
                    bankStatementList.add(bankStatement);
                }
                if (numberOfMonth > 7) { //get from table month1
                    BankStatement1 bankStatement1 = bankStatement1DAO.getByAccountNumber(accountNumber);
                    DWHBankStatement bankStatement = transform(bankStatement1);
                    bankStatementList.add(bankStatement);
                }
                if (numberOfMonth > 8) { //get from table month2
                    BankStatement2 bankStatement2 = bankStatement2DAO.getByAccountNumber(accountNumber);
                    DWHBankStatement bankStatement = transform(bankStatement2);
                    bankStatementList.add(bankStatement);
                }
                if (numberOfMonth > 9) { //get from table month3
                    BankStatement3 bankStatement3 = bankStatement3DAO.getByAccountNumber(accountNumber);
                    DWHBankStatement bankStatement = transform(bankStatement3);
                    bankStatementList.add(bankStatement);
                }
                if (numberOfMonth > 10) { //get from table month4
                    BankStatement4 bankStatement4 = bankStatement4DAO.getByAccountNumber(accountNumber);
                    DWHBankStatement bankStatement = transform(bankStatement4);
                    bankStatementList.add(bankStatement);
                }
                if (numberOfMonth > 11) { //get from table month5
                    BankStatement5 bankStatement5 = bankStatement5DAO.getByAccountNumber(accountNumber);
                    DWHBankStatement bankStatement = transform(bankStatement5);
                    bankStatementList.add(bankStatement);
                }
            } else if (tableNumber.equalsIgnoreCase("9")) {
                if (numberOfMonth > 0) { //get from table month9
                    BankStatement9 bankStatement9 = bankStatement9DAO.getByAccountNumber(accountNumber);
                    DWHBankStatement bankStatement = transform(bankStatement9);
                    bankStatementList.add(bankStatement);
                }
                if (numberOfMonth > 1) { //get from table month10
                    BankStatement10 bankStatement10 = bankStatement10DAO.getByAccountNumber(accountNumber);
                    DWHBankStatement bankStatement = transform(bankStatement10);
                    bankStatementList.add(bankStatement);
                }
                if (numberOfMonth > 2) { //get from table month11
                    BankStatement11 bankStatement11 = bankStatement11DAO.getByAccountNumber(accountNumber);
                    DWHBankStatement bankStatement = transform(bankStatement11);
                    bankStatementList.add(bankStatement);
                }
                if (numberOfMonth > 3) { //get from table month12
                    BankStatement12 bankStatement12 = bankStatement12DAO.getByAccountNumber(accountNumber);
                    DWHBankStatement bankStatement = transform(bankStatement12);
                    bankStatementList.add(bankStatement);
                }
                if (numberOfMonth > 4) { //get from table month13
                    BankStatement13 bankStatement13 = bankStatement13DAO.getByAccountNumber(accountNumber);
                    DWHBankStatement bankStatement = transform(bankStatement13);
                    bankStatementList.add(bankStatement);
                }
                if (numberOfMonth > 5) { //get from table month14
                    BankStatement14 bankStatement14 = bankStatement14DAO.getByAccountNumber(accountNumber);
                    DWHBankStatement bankStatement = transform(bankStatement14);
                    bankStatementList.add(bankStatement);
                }
                if (numberOfMonth > 6) { //get from table month1
                    BankStatement1 bankStatement1 = bankStatement1DAO.getByAccountNumber(accountNumber);
                    DWHBankStatement bankStatement = transform(bankStatement1);
                    bankStatementList.add(bankStatement);
                }
                if (numberOfMonth > 7) { //get from table month2
                    BankStatement2 bankStatement2 = bankStatement2DAO.getByAccountNumber(accountNumber);
                    DWHBankStatement bankStatement = transform(bankStatement2);
                    bankStatementList.add(bankStatement);
                }
                if (numberOfMonth > 8) { //get from table month3
                    BankStatement3 bankStatement3 = bankStatement3DAO.getByAccountNumber(accountNumber);
                    DWHBankStatement bankStatement = transform(bankStatement3);
                    bankStatementList.add(bankStatement);
                }
                if (numberOfMonth > 9) { //get from table month4
                    BankStatement4 bankStatement4 = bankStatement4DAO.getByAccountNumber(accountNumber);
                    DWHBankStatement bankStatement = transform(bankStatement4);
                    bankStatementList.add(bankStatement);
                }
                if (numberOfMonth > 10) { //get from table month5
                    BankStatement5 bankStatement5 = bankStatement5DAO.getByAccountNumber(accountNumber);
                    DWHBankStatement bankStatement = transform(bankStatement5);
                    bankStatementList.add(bankStatement);
                }
                if (numberOfMonth > 11) { //get from table month6
                    BankStatement6 bankStatement6 = bankStatement6DAO.getByAccountNumber(accountNumber);
                    DWHBankStatement bankStatement = transform(bankStatement6);
                    bankStatementList.add(bankStatement);
                }
            } else if (tableNumber.equalsIgnoreCase("10")) {
                if (numberOfMonth > 0) { //get from table month10
                    BankStatement10 bankStatement10 = bankStatement10DAO.getByAccountNumber(accountNumber);
                    DWHBankStatement bankStatement = transform(bankStatement10);
                    bankStatementList.add(bankStatement);
                }
                if (numberOfMonth > 1) { //get from table month11
                    BankStatement11 bankStatement11 = bankStatement11DAO.getByAccountNumber(accountNumber);
                    DWHBankStatement bankStatement = transform(bankStatement11);
                    bankStatementList.add(bankStatement);
                }
                if (numberOfMonth > 2) { //get from table month12
                    BankStatement12 bankStatement12 = bankStatement12DAO.getByAccountNumber(accountNumber);
                    DWHBankStatement bankStatement = transform(bankStatement12);
                    bankStatementList.add(bankStatement);
                }
                if (numberOfMonth > 3) { //get from table month13
                    BankStatement13 bankStatement13 = bankStatement13DAO.getByAccountNumber(accountNumber);
                    DWHBankStatement bankStatement = transform(bankStatement13);
                    bankStatementList.add(bankStatement);
                }
                if (numberOfMonth > 4) { //get from table month14
                    BankStatement14 bankStatement14 = bankStatement14DAO.getByAccountNumber(accountNumber);
                    DWHBankStatement bankStatement = transform(bankStatement14);
                    bankStatementList.add(bankStatement);
                }
                if (numberOfMonth > 5) { //get from table month1
                    BankStatement1 bankStatement1 = bankStatement1DAO.getByAccountNumber(accountNumber);
                    DWHBankStatement bankStatement = transform(bankStatement1);
                    bankStatementList.add(bankStatement);
                }
                if (numberOfMonth > 6) { //get from table month2
                    BankStatement2 bankStatement2 = bankStatement2DAO.getByAccountNumber(accountNumber);
                    DWHBankStatement bankStatement = transform(bankStatement2);
                    bankStatementList.add(bankStatement);
                }
                if (numberOfMonth > 7) { //get from table month3
                    BankStatement3 bankStatement3 = bankStatement3DAO.getByAccountNumber(accountNumber);
                    DWHBankStatement bankStatement = transform(bankStatement3);
                    bankStatementList.add(bankStatement);
                }
                if (numberOfMonth > 8) { //get from table month4
                    BankStatement4 bankStatement4 = bankStatement4DAO.getByAccountNumber(accountNumber);
                    DWHBankStatement bankStatement = transform(bankStatement4);
                    bankStatementList.add(bankStatement);
                }
                if (numberOfMonth > 9) { //get from table month5
                    BankStatement5 bankStatement5 = bankStatement5DAO.getByAccountNumber(accountNumber);
                    DWHBankStatement bankStatement = transform(bankStatement5);
                    bankStatementList.add(bankStatement);
                }
                if (numberOfMonth > 10) { //get from table month6
                    BankStatement6 bankStatement6 = bankStatement6DAO.getByAccountNumber(accountNumber);
                    DWHBankStatement bankStatement = transform(bankStatement6);
                    bankStatementList.add(bankStatement);
                }
                if (numberOfMonth > 11) { //get from table month7
                    BankStatement7 bankStatement7 = bankStatement7DAO.getByAccountNumber(accountNumber);
                    DWHBankStatement bankStatement = transform(bankStatement7);
                    bankStatementList.add(bankStatement);
                }
            } else if (tableNumber.equalsIgnoreCase("11")) {
                if (numberOfMonth > 0) { //get from table month11
                    BankStatement11 bankStatement11 = bankStatement11DAO.getByAccountNumber(accountNumber);
                    DWHBankStatement bankStatement = transform(bankStatement11);
                    bankStatementList.add(bankStatement);
                }
                if (numberOfMonth > 1) { //get from table month12
                    BankStatement12 bankStatement12 = bankStatement12DAO.getByAccountNumber(accountNumber);
                    DWHBankStatement bankStatement = transform(bankStatement12);
                    bankStatementList.add(bankStatement);
                }
                if (numberOfMonth > 2) { //get from table month13
                    BankStatement13 bankStatement13 = bankStatement13DAO.getByAccountNumber(accountNumber);
                    DWHBankStatement bankStatement = transform(bankStatement13);
                    bankStatementList.add(bankStatement);
                }
                if (numberOfMonth > 3) { //get from table month14
                    BankStatement14 bankStatement14 = bankStatement14DAO.getByAccountNumber(accountNumber);
                    DWHBankStatement bankStatement = transform(bankStatement14);
                    bankStatementList.add(bankStatement);
                }
                if (numberOfMonth > 4) { //get from table month1
                    BankStatement1 bankStatement1 = bankStatement1DAO.getByAccountNumber(accountNumber);
                    DWHBankStatement bankStatement = transform(bankStatement1);
                    bankStatementList.add(bankStatement);
                }
                if (numberOfMonth > 5) { //get from table month2
                    BankStatement2 bankStatement2 = bankStatement2DAO.getByAccountNumber(accountNumber);
                    DWHBankStatement bankStatement = transform(bankStatement2);
                    bankStatementList.add(bankStatement);
                }
                if (numberOfMonth > 6) { //get from table month3
                    BankStatement3 bankStatement3 = bankStatement3DAO.getByAccountNumber(accountNumber);
                    DWHBankStatement bankStatement = transform(bankStatement3);
                    bankStatementList.add(bankStatement);
                }
                if (numberOfMonth > 7) { //get from table month4
                    BankStatement4 bankStatement4 = bankStatement4DAO.getByAccountNumber(accountNumber);
                    DWHBankStatement bankStatement = transform(bankStatement4);
                    bankStatementList.add(bankStatement);
                }
                if (numberOfMonth > 8) { //get from table month5
                    BankStatement5 bankStatement5 = bankStatement5DAO.getByAccountNumber(accountNumber);
                    DWHBankStatement bankStatement = transform(bankStatement5);
                    bankStatementList.add(bankStatement);
                }
                if (numberOfMonth > 9) { //get from table month6
                    BankStatement6 bankStatement6 = bankStatement6DAO.getByAccountNumber(accountNumber);
                    DWHBankStatement bankStatement = transform(bankStatement6);
                    bankStatementList.add(bankStatement);
                }
                if (numberOfMonth > 10) { //get from table month7
                    BankStatement7 bankStatement7 = bankStatement7DAO.getByAccountNumber(accountNumber);
                    DWHBankStatement bankStatement = transform(bankStatement7);
                    bankStatementList.add(bankStatement);
                }
                if (numberOfMonth > 11) { //get from table month8
                    BankStatement8 bankStatement8 = bankStatement8DAO.getByAccountNumber(accountNumber);
                    DWHBankStatement bankStatement = transform(bankStatement8);
                    bankStatementList.add(bankStatement);
                }
            } else if (tableNumber.equalsIgnoreCase("12")) {
                if (numberOfMonth > 0) { //get from table month12
                    BankStatement12 bankStatement12 = bankStatement12DAO.getByAccountNumber(accountNumber);
                    DWHBankStatement bankStatement = transform(bankStatement12);
                    bankStatementList.add(bankStatement);
                }
                if (numberOfMonth > 1) { //get from table month13
                    BankStatement13 bankStatement13 = bankStatement13DAO.getByAccountNumber(accountNumber);
                    DWHBankStatement bankStatement = transform(bankStatement13);
                    bankStatementList.add(bankStatement);
                }
                if (numberOfMonth > 2) { //get from table month14
                    BankStatement14 bankStatement14 = bankStatement14DAO.getByAccountNumber(accountNumber);
                    DWHBankStatement bankStatement = transform(bankStatement14);
                    bankStatementList.add(bankStatement);
                }
                if (numberOfMonth > 3) { //get from table month1
                    BankStatement1 bankStatement1 = bankStatement1DAO.getByAccountNumber(accountNumber);
                    DWHBankStatement bankStatement = transform(bankStatement1);
                    bankStatementList.add(bankStatement);
                }
                if (numberOfMonth > 4) { //get from table month2
                    BankStatement2 bankStatement2 = bankStatement2DAO.getByAccountNumber(accountNumber);
                    DWHBankStatement bankStatement = transform(bankStatement2);
                    bankStatementList.add(bankStatement);
                }
                if (numberOfMonth > 5) { //get from table month3
                    BankStatement3 bankStatement3 = bankStatement3DAO.getByAccountNumber(accountNumber);
                    DWHBankStatement bankStatement = transform(bankStatement3);
                    bankStatementList.add(bankStatement);
                }
                if (numberOfMonth > 6) { //get from table month4
                    BankStatement4 bankStatement4 = bankStatement4DAO.getByAccountNumber(accountNumber);
                    DWHBankStatement bankStatement = transform(bankStatement4);
                    bankStatementList.add(bankStatement);
                }
                if (numberOfMonth > 7) { //get from table month5
                    BankStatement5 bankStatement5 = bankStatement5DAO.getByAccountNumber(accountNumber);
                    DWHBankStatement bankStatement = transform(bankStatement5);
                    bankStatementList.add(bankStatement);
                }
                if (numberOfMonth > 8) { //get from table month6
                    BankStatement6 bankStatement6 = bankStatement6DAO.getByAccountNumber(accountNumber);
                    DWHBankStatement bankStatement = transform(bankStatement6);
                    bankStatementList.add(bankStatement);
                }
                if (numberOfMonth > 9) { //get from table month7
                    BankStatement7 bankStatement7 = bankStatement7DAO.getByAccountNumber(accountNumber);
                    DWHBankStatement bankStatement = transform(bankStatement7);
                    bankStatementList.add(bankStatement);
                }
                if (numberOfMonth > 10) { //get from table month8
                    BankStatement8 bankStatement8 = bankStatement8DAO.getByAccountNumber(accountNumber);
                    DWHBankStatement bankStatement = transform(bankStatement8);
                    bankStatementList.add(bankStatement);
                }
                if (numberOfMonth > 11) { //get from table month9
                    BankStatement9 bankStatement9 = bankStatement9DAO.getByAccountNumber(accountNumber);
                    DWHBankStatement bankStatement = transform(bankStatement9);
                    bankStatementList.add(bankStatement);
                }
            } else if (tableNumber.equalsIgnoreCase("13")) {
                if (numberOfMonth > 0) { //get from table month13
                    BankStatement13 bankStatement13 = bankStatement13DAO.getByAccountNumber(accountNumber);
                    DWHBankStatement bankStatement = transform(bankStatement13);
                    bankStatementList.add(bankStatement);
                }
                if (numberOfMonth > 1) { //get from table month14
                    BankStatement14 bankStatement14 = bankStatement14DAO.getByAccountNumber(accountNumber);
                    DWHBankStatement bankStatement = transform(bankStatement14);
                    bankStatementList.add(bankStatement);
                }
                if (numberOfMonth > 2) { //get from table month1
                    BankStatement1 bankStatement1 = bankStatement1DAO.getByAccountNumber(accountNumber);
                    DWHBankStatement bankStatement = transform(bankStatement1);
                    bankStatementList.add(bankStatement);
                }
                if (numberOfMonth > 3) { //get from table month2
                    BankStatement2 bankStatement2 = bankStatement2DAO.getByAccountNumber(accountNumber);
                    DWHBankStatement bankStatement = transform(bankStatement2);
                    bankStatementList.add(bankStatement);
                }
                if (numberOfMonth > 4) { //get from table month3
                    BankStatement3 bankStatement3 = bankStatement3DAO.getByAccountNumber(accountNumber);
                    DWHBankStatement bankStatement = transform(bankStatement3);
                    bankStatementList.add(bankStatement);
                }
                if (numberOfMonth > 5) { //get from table month4
                    BankStatement4 bankStatement4 = bankStatement4DAO.getByAccountNumber(accountNumber);
                    DWHBankStatement bankStatement = transform(bankStatement4);
                    bankStatementList.add(bankStatement);
                }
                if (numberOfMonth > 6) { //get from table month5
                    BankStatement5 bankStatement5 = bankStatement5DAO.getByAccountNumber(accountNumber);
                    DWHBankStatement bankStatement = transform(bankStatement5);
                    bankStatementList.add(bankStatement);
                }
                if (numberOfMonth > 7) { //get from table month6
                    BankStatement6 bankStatement6 = bankStatement6DAO.getByAccountNumber(accountNumber);
                    DWHBankStatement bankStatement = transform(bankStatement6);
                    bankStatementList.add(bankStatement);
                }
                if (numberOfMonth > 8) { //get from table month7
                    BankStatement7 bankStatement7 = bankStatement7DAO.getByAccountNumber(accountNumber);
                    DWHBankStatement bankStatement = transform(bankStatement7);
                    bankStatementList.add(bankStatement);
                }
                if (numberOfMonth > 9) { //get from table month8
                    BankStatement8 bankStatement8 = bankStatement8DAO.getByAccountNumber(accountNumber);
                    DWHBankStatement bankStatement = transform(bankStatement8);
                    bankStatementList.add(bankStatement);
                }
                if (numberOfMonth > 10) { //get from table month9
                    BankStatement9 bankStatement9 = bankStatement9DAO.getByAccountNumber(accountNumber);
                    DWHBankStatement bankStatement = transform(bankStatement9);
                    bankStatementList.add(bankStatement);
                }
                if (numberOfMonth > 11) { //get from table month10
                    BankStatement10 bankStatement10 = bankStatement10DAO.getByAccountNumber(accountNumber);
                    DWHBankStatement bankStatement = transform(bankStatement10);
                    bankStatementList.add(bankStatement);
                }
            } else if (tableNumber.equalsIgnoreCase("14")) {
                if (numberOfMonth > 0) { //get from table month14
                    BankStatement14 bankStatement14 = bankStatement14DAO.getByAccountNumber(accountNumber);
                    DWHBankStatement bankStatement = transform(bankStatement14);
                    bankStatementList.add(bankStatement);
                }
                if (numberOfMonth > 1) { //get from table month1
                    BankStatement1 bankStatement1 = bankStatement1DAO.getByAccountNumber(accountNumber);
                    DWHBankStatement bankStatement = transform(bankStatement1);
                    bankStatementList.add(bankStatement);
                }
                if (numberOfMonth > 2) { //get from table month2
                    BankStatement2 bankStatement2 = bankStatement2DAO.getByAccountNumber(accountNumber);
                    DWHBankStatement bankStatement = transform(bankStatement2);
                    bankStatementList.add(bankStatement);
                }
                if (numberOfMonth > 3) { //get from table month3
                    BankStatement3 bankStatement3 = bankStatement3DAO.getByAccountNumber(accountNumber);
                    DWHBankStatement bankStatement = transform(bankStatement3);
                    bankStatementList.add(bankStatement);
                }
                if (numberOfMonth > 4) { //get from table month4
                    BankStatement4 bankStatement4 = bankStatement4DAO.getByAccountNumber(accountNumber);
                    DWHBankStatement bankStatement = transform(bankStatement4);
                    bankStatementList.add(bankStatement);
                }
                if (numberOfMonth > 5) { //get from table month5
                    BankStatement5 bankStatement5 = bankStatement5DAO.getByAccountNumber(accountNumber);
                    DWHBankStatement bankStatement = transform(bankStatement5);
                    bankStatementList.add(bankStatement);
                }
                if (numberOfMonth > 6) { //get from table month6
                    BankStatement6 bankStatement6 = bankStatement6DAO.getByAccountNumber(accountNumber);
                    DWHBankStatement bankStatement = transform(bankStatement6);
                    bankStatementList.add(bankStatement);
                }
                if (numberOfMonth > 7) { //get from table month7
                    BankStatement7 bankStatement7 = bankStatement7DAO.getByAccountNumber(accountNumber);
                    DWHBankStatement bankStatement = transform(bankStatement7);
                    bankStatementList.add(bankStatement);
                }
                if (numberOfMonth > 8) { //get from table month8
                    BankStatement8 bankStatement8 = bankStatement8DAO.getByAccountNumber(accountNumber);
                    DWHBankStatement bankStatement = transform(bankStatement8);
                    bankStatementList.add(bankStatement);
                }
                if (numberOfMonth > 9) { //get from table month9
                    BankStatement9 bankStatement9 = bankStatement9DAO.getByAccountNumber(accountNumber);
                    DWHBankStatement bankStatement = transform(bankStatement9);
                    bankStatementList.add(bankStatement);
                }
                if (numberOfMonth > 10) { //get from table month10
                    BankStatement10 bankStatement10 = bankStatement10DAO.getByAccountNumber(accountNumber);
                    DWHBankStatement bankStatement = transform(bankStatement10);
                    bankStatementList.add(bankStatement);
                }
                if (numberOfMonth > 11) { //get from table month11
                    BankStatement11 bankStatement11 = bankStatement11DAO.getByAccountNumber(accountNumber);
                    DWHBankStatement bankStatement = transform(bankStatement11);
                    bankStatementList.add(bankStatement);
                }
            }
        }
        return bankStatementList;
    }

    public DWHBankStatement transform(BankStatement1 bankStatement) {
        log.debug("transform() BankStatement: {}", bankStatement);
        DWHBankStatement bankStatementResult = new DWHBankStatement();
        if (bankStatement != null) {
            bankStatementResult.setAccountStatus(bankStatement.getAccountStatus());
            bankStatementResult.setAccountOpenDate(bankStatement.getAccountOpenDate());
            bankStatementResult.setBranchCode(bankStatement.getBranchCode());
            bankStatementResult.setDataSource(bankStatement.getDataSource());
            bankStatementResult.setAccountNumber(bankStatement.getAccountNumber());
            bankStatementResult.setAccountName(bankStatement.getAccountName());
            bankStatementResult.setOverLimitAmount(bankStatement.getOdLimit());
            bankStatementResult.setGrossCreditBalance(bankStatement.getGrossCreditBalance());
            bankStatementResult.setNumberOfCreditTxn(bankStatement.getCreditTXNNumber());
            bankStatementResult.setDebitAmount(bankStatement.getGrossDebitBalance());
            bankStatementResult.setNumberOfDebitTxn(bankStatement.getDebitTXNNumber());
            bankStatementResult.setHighestBalanceDate(bankStatement.getHighestBalanceDate());
            bankStatementResult.setHighestBalance(bankStatement.getHighestBalance());
            bankStatementResult.setLowestBalanceDate(bankStatement.getLowestBalanceDate());
            bankStatementResult.setLowestBalance(bankStatement.getLowestBalance());
            bankStatementResult.setMonthEndBalance(bankStatement.getMonthEndBalance());
            bankStatementResult.setNumberOfChequeReturn(bankStatement.getCheckReturnNumber());
            bankStatementResult.setChequeReturnAmount(bankStatement.getCheckReturnAmount());
            bankStatementResult.setNumberOfTimesOD(bankStatement.getOdLimitNumber());
            bankStatementResult.setStartODDate(bankStatement.getStartOdDate());
            bankStatementResult.setEndODDate(bankStatement.getEndOdDate());
            bankStatementResult.setAsOfDate(bankStatement.getAsOfDate());
        }
        return bankStatementResult;
    }

    public DWHBankStatement transform(BankStatement2 bankStatement) {
        log.debug("transform() BankStatement: {}", bankStatement);
        DWHBankStatement bankStatementResult = new DWHBankStatement();
        if (bankStatement != null) {
            bankStatementResult.setAccountStatus(bankStatement.getAccountStatus());
            bankStatementResult.setAccountOpenDate(bankStatement.getAccountOpenDate());
            bankStatementResult.setBranchCode(bankStatement.getBranchCode());
            bankStatementResult.setDataSource(bankStatement.getDataSource());
            bankStatementResult.setAccountNumber(bankStatement.getAccountNumber());
            bankStatementResult.setAccountName(bankStatement.getAccountName());
            bankStatementResult.setOverLimitAmount(bankStatement.getOdLimit());
            bankStatementResult.setGrossCreditBalance(bankStatement.getGrossCreditBalance());
            bankStatementResult.setNumberOfCreditTxn(bankStatement.getCreditTXNNumber());
            bankStatementResult.setDebitAmount(bankStatement.getGrossDebitBalance());
            bankStatementResult.setNumberOfDebitTxn(bankStatement.getDebitTXNNumber());
            bankStatementResult.setHighestBalanceDate(bankStatement.getHighestBalanceDate());
            bankStatementResult.setHighestBalance(bankStatement.getHighestBalance());
            bankStatementResult.setLowestBalanceDate(bankStatement.getLowestBalanceDate());
            bankStatementResult.setLowestBalance(bankStatement.getLowestBalance());
            bankStatementResult.setMonthEndBalance(bankStatement.getMonthEndBalance());
            bankStatementResult.setNumberOfChequeReturn(bankStatement.getCheckReturnNumber());
            bankStatementResult.setChequeReturnAmount(bankStatement.getCheckReturnAmount());
            bankStatementResult.setNumberOfTimesOD(bankStatement.getOdLimitNumber());
            bankStatementResult.setStartODDate(bankStatement.getStartOdDate());
            bankStatementResult.setEndODDate(bankStatement.getEndOdDate());
            bankStatementResult.setAsOfDate(bankStatement.getAsOfDate());
        }
        return bankStatementResult;
    }

    public DWHBankStatement transform(BankStatement3 bankStatement) {
        log.debug("transform() BankStatement: {}", bankStatement);
        DWHBankStatement bankStatementResult = new DWHBankStatement();
        if (bankStatement != null) {
            bankStatementResult.setAccountStatus(bankStatement.getAccountStatus());
            bankStatementResult.setAccountOpenDate(bankStatement.getAccountOpenDate());
            bankStatementResult.setBranchCode(bankStatement.getBranchCode());
            bankStatementResult.setDataSource(bankStatement.getDataSource());
            bankStatementResult.setAccountNumber(bankStatement.getAccountNumber());
            bankStatementResult.setAccountName(bankStatement.getAccountName());
            bankStatementResult.setOverLimitAmount(bankStatement.getOdLimit());
            bankStatementResult.setGrossCreditBalance(bankStatement.getGrossCreditBalance());
            bankStatementResult.setNumberOfCreditTxn(bankStatement.getCreditTXNNumber());
            bankStatementResult.setDebitAmount(bankStatement.getGrossDebitBalance());
            bankStatementResult.setNumberOfDebitTxn(bankStatement.getDebitTXNNumber());
            bankStatementResult.setHighestBalanceDate(bankStatement.getHighestBalanceDate());
            bankStatementResult.setHighestBalance(bankStatement.getHighestBalance());
            bankStatementResult.setLowestBalanceDate(bankStatement.getLowestBalanceDate());
            bankStatementResult.setLowestBalance(bankStatement.getLowestBalance());
            bankStatementResult.setMonthEndBalance(bankStatement.getMonthEndBalance());
            bankStatementResult.setNumberOfChequeReturn(bankStatement.getCheckReturnNumber());
            bankStatementResult.setChequeReturnAmount(bankStatement.getCheckReturnAmount());
            bankStatementResult.setNumberOfTimesOD(bankStatement.getOdLimitNumber());
            bankStatementResult.setStartODDate(bankStatement.getStartOdDate());
            bankStatementResult.setEndODDate(bankStatement.getEndOdDate());
            bankStatementResult.setAsOfDate(bankStatement.getAsOfDate());
        }
        return bankStatementResult;
    }

    public DWHBankStatement transform(BankStatement4 bankStatement) {
        log.debug("transform() BankStatement: {}", bankStatement);
        DWHBankStatement bankStatementResult = new DWHBankStatement();
        if (bankStatement != null) {
            bankStatementResult.setAccountStatus(bankStatement.getAccountStatus());
            bankStatementResult.setAccountOpenDate(bankStatement.getAccountOpenDate());
            bankStatementResult.setBranchCode(bankStatement.getBranchCode());
            bankStatementResult.setDataSource(bankStatement.getDataSource());
            bankStatementResult.setAccountNumber(bankStatement.getAccountNumber());
            bankStatementResult.setAccountName(bankStatement.getAccountName());
            bankStatementResult.setOverLimitAmount(bankStatement.getOdLimit());
            bankStatementResult.setGrossCreditBalance(bankStatement.getGrossCreditBalance());
            bankStatementResult.setNumberOfCreditTxn(bankStatement.getCreditTXNNumber());
            bankStatementResult.setDebitAmount(bankStatement.getGrossDebitBalance());
            bankStatementResult.setNumberOfDebitTxn(bankStatement.getDebitTXNNumber());
            bankStatementResult.setHighestBalanceDate(bankStatement.getHighestBalanceDate());
            bankStatementResult.setHighestBalance(bankStatement.getHighestBalance());
            bankStatementResult.setLowestBalanceDate(bankStatement.getLowestBalanceDate());
            bankStatementResult.setLowestBalance(bankStatement.getLowestBalance());
            bankStatementResult.setMonthEndBalance(bankStatement.getMonthEndBalance());
            bankStatementResult.setNumberOfChequeReturn(bankStatement.getCheckReturnNumber());
            bankStatementResult.setChequeReturnAmount(bankStatement.getCheckReturnAmount());
            bankStatementResult.setNumberOfTimesOD(bankStatement.getOdLimitNumber());
            bankStatementResult.setStartODDate(bankStatement.getStartOdDate());
            bankStatementResult.setEndODDate(bankStatement.getEndOdDate());
            bankStatementResult.setAsOfDate(bankStatement.getAsOfDate());
        }
        return bankStatementResult;
    }

    public DWHBankStatement transform(BankStatement5 bankStatement) {
        log.debug("transform() BankStatement: {}", bankStatement);
        DWHBankStatement bankStatementResult = new DWHBankStatement();
        if (bankStatement != null) {
            bankStatementResult.setAccountStatus(bankStatement.getAccountStatus());
            bankStatementResult.setAccountOpenDate(bankStatement.getAccountOpenDate());
            bankStatementResult.setBranchCode(bankStatement.getBranchCode());
            bankStatementResult.setDataSource(bankStatement.getDataSource());
            bankStatementResult.setAccountNumber(bankStatement.getAccountNumber());
            bankStatementResult.setAccountName(bankStatement.getAccountName());
            bankStatementResult.setOverLimitAmount(bankStatement.getOdLimit());
            bankStatementResult.setGrossCreditBalance(bankStatement.getGrossCreditBalance());
            bankStatementResult.setNumberOfCreditTxn(bankStatement.getCreditTXNNumber());
            bankStatementResult.setDebitAmount(bankStatement.getGrossDebitBalance());
            bankStatementResult.setNumberOfDebitTxn(bankStatement.getDebitTXNNumber());
            bankStatementResult.setHighestBalanceDate(bankStatement.getHighestBalanceDate());
            bankStatementResult.setHighestBalance(bankStatement.getHighestBalance());
            bankStatementResult.setLowestBalanceDate(bankStatement.getLowestBalanceDate());
            bankStatementResult.setLowestBalance(bankStatement.getLowestBalance());
            bankStatementResult.setMonthEndBalance(bankStatement.getMonthEndBalance());
            bankStatementResult.setNumberOfChequeReturn(bankStatement.getCheckReturnNumber());
            bankStatementResult.setChequeReturnAmount(bankStatement.getCheckReturnAmount());
            bankStatementResult.setNumberOfTimesOD(bankStatement.getOdLimitNumber());
            bankStatementResult.setStartODDate(bankStatement.getStartOdDate());
            bankStatementResult.setEndODDate(bankStatement.getEndOdDate());
            bankStatementResult.setAsOfDate(bankStatement.getAsOfDate());
        }
        return bankStatementResult;
    }

    public DWHBankStatement transform(BankStatement6 bankStatement) {
        log.debug("transform() BankStatement: {}", bankStatement);
        DWHBankStatement bankStatementResult = new DWHBankStatement();
        if (bankStatement != null) {
            bankStatementResult.setAccountStatus(bankStatement.getAccountStatus());
            bankStatementResult.setAccountOpenDate(bankStatement.getAccountOpenDate());
            bankStatementResult.setBranchCode(bankStatement.getBranchCode());
            bankStatementResult.setDataSource(bankStatement.getDataSource());
            bankStatementResult.setAccountNumber(bankStatement.getAccountNumber());
            bankStatementResult.setAccountName(bankStatement.getAccountName());
            bankStatementResult.setOverLimitAmount(bankStatement.getOdLimit());
            bankStatementResult.setGrossCreditBalance(bankStatement.getGrossCreditBalance());
            bankStatementResult.setNumberOfCreditTxn(bankStatement.getCreditTXNNumber());
            bankStatementResult.setDebitAmount(bankStatement.getGrossDebitBalance());
            bankStatementResult.setNumberOfDebitTxn(bankStatement.getDebitTXNNumber());
            bankStatementResult.setHighestBalanceDate(bankStatement.getHighestBalanceDate());
            bankStatementResult.setHighestBalance(bankStatement.getHighestBalance());
            bankStatementResult.setLowestBalanceDate(bankStatement.getLowestBalanceDate());
            bankStatementResult.setLowestBalance(bankStatement.getLowestBalance());
            bankStatementResult.setMonthEndBalance(bankStatement.getMonthEndBalance());
            bankStatementResult.setNumberOfChequeReturn(bankStatement.getCheckReturnNumber());
            bankStatementResult.setChequeReturnAmount(bankStatement.getCheckReturnAmount());
            bankStatementResult.setNumberOfTimesOD(bankStatement.getOdLimitNumber());
            bankStatementResult.setStartODDate(bankStatement.getStartOdDate());
            bankStatementResult.setEndODDate(bankStatement.getEndOdDate());
            bankStatementResult.setAsOfDate(bankStatement.getAsOfDate());
        }
        return bankStatementResult;
    }

    public DWHBankStatement transform(BankStatement7 bankStatement) {
        log.debug("transform() BankStatement: {}", bankStatement);
        DWHBankStatement bankStatementResult = new DWHBankStatement();
        if (bankStatement != null) {
            bankStatementResult.setAccountStatus(bankStatement.getAccountStatus());
            bankStatementResult.setAccountOpenDate(bankStatement.getAccountOpenDate());
            bankStatementResult.setBranchCode(bankStatement.getBranchCode());
            bankStatementResult.setDataSource(bankStatement.getDataSource());
            bankStatementResult.setAccountNumber(bankStatement.getAccountNumber());
            bankStatementResult.setAccountName(bankStatement.getAccountName());
            bankStatementResult.setOverLimitAmount(bankStatement.getOdLimit());
            bankStatementResult.setGrossCreditBalance(bankStatement.getGrossCreditBalance());
            bankStatementResult.setNumberOfCreditTxn(bankStatement.getCreditTXNNumber());
            bankStatementResult.setDebitAmount(bankStatement.getGrossDebitBalance());
            bankStatementResult.setNumberOfDebitTxn(bankStatement.getDebitTXNNumber());
            bankStatementResult.setHighestBalanceDate(bankStatement.getHighestBalanceDate());
            bankStatementResult.setHighestBalance(bankStatement.getHighestBalance());
            bankStatementResult.setLowestBalanceDate(bankStatement.getLowestBalanceDate());
            bankStatementResult.setLowestBalance(bankStatement.getLowestBalance());
            bankStatementResult.setMonthEndBalance(bankStatement.getMonthEndBalance());
            bankStatementResult.setNumberOfChequeReturn(bankStatement.getCheckReturnNumber());
            bankStatementResult.setChequeReturnAmount(bankStatement.getCheckReturnAmount());
            bankStatementResult.setNumberOfTimesOD(bankStatement.getOdLimitNumber());
            bankStatementResult.setStartODDate(bankStatement.getStartOdDate());
            bankStatementResult.setEndODDate(bankStatement.getEndOdDate());
            bankStatementResult.setAsOfDate(bankStatement.getAsOfDate());
        }
        return bankStatementResult;
    }

    public DWHBankStatement transform(BankStatement8 bankStatement) {
        log.debug("transform() BankStatement: {}", bankStatement);
        DWHBankStatement bankStatementResult = new DWHBankStatement();
        if (bankStatement != null) {
            bankStatementResult.setAccountStatus(bankStatement.getAccountStatus());
            bankStatementResult.setAccountOpenDate(bankStatement.getAccountOpenDate());
            bankStatementResult.setBranchCode(bankStatement.getBranchCode());
            bankStatementResult.setDataSource(bankStatement.getDataSource());
            bankStatementResult.setAccountNumber(bankStatement.getAccountNumber());
            bankStatementResult.setAccountName(bankStatement.getAccountName());
            bankStatementResult.setOverLimitAmount(bankStatement.getOdLimit());
            bankStatementResult.setGrossCreditBalance(bankStatement.getGrossCreditBalance());
            bankStatementResult.setNumberOfCreditTxn(bankStatement.getCreditTXNNumber());
            bankStatementResult.setDebitAmount(bankStatement.getGrossDebitBalance());
            bankStatementResult.setNumberOfDebitTxn(bankStatement.getDebitTXNNumber());
            bankStatementResult.setHighestBalanceDate(bankStatement.getHighestBalanceDate());
            bankStatementResult.setHighestBalance(bankStatement.getHighestBalance());
            bankStatementResult.setLowestBalanceDate(bankStatement.getLowestBalanceDate());
            bankStatementResult.setLowestBalance(bankStatement.getLowestBalance());
            bankStatementResult.setMonthEndBalance(bankStatement.getMonthEndBalance());
            bankStatementResult.setNumberOfChequeReturn(bankStatement.getCheckReturnNumber());
            bankStatementResult.setChequeReturnAmount(bankStatement.getCheckReturnAmount());
            bankStatementResult.setNumberOfTimesOD(bankStatement.getOdLimitNumber());
            bankStatementResult.setStartODDate(bankStatement.getStartOdDate());
            bankStatementResult.setEndODDate(bankStatement.getEndOdDate());
            bankStatementResult.setAsOfDate(bankStatement.getAsOfDate());
        }
        return bankStatementResult;
    }

    public DWHBankStatement transform(BankStatement9 bankStatement) {
        log.debug("transform() BankStatement: {}", bankStatement);
        DWHBankStatement bankStatementResult = new DWHBankStatement();
        if (bankStatement != null) {
            bankStatementResult.setAccountStatus(bankStatement.getAccountStatus());
            bankStatementResult.setAccountOpenDate(bankStatement.getAccountOpenDate());
            bankStatementResult.setBranchCode(bankStatement.getBranchCode());
            bankStatementResult.setDataSource(bankStatement.getDataSource());
            bankStatementResult.setAccountNumber(bankStatement.getAccountNumber());
            bankStatementResult.setAccountName(bankStatement.getAccountName());
            bankStatementResult.setOverLimitAmount(bankStatement.getOdLimit());
            bankStatementResult.setGrossCreditBalance(bankStatement.getGrossCreditBalance());
            bankStatementResult.setNumberOfCreditTxn(bankStatement.getCreditTXNNumber());
            bankStatementResult.setDebitAmount(bankStatement.getGrossDebitBalance());
            bankStatementResult.setNumberOfDebitTxn(bankStatement.getDebitTXNNumber());
            bankStatementResult.setHighestBalanceDate(bankStatement.getHighestBalanceDate());
            bankStatementResult.setHighestBalance(bankStatement.getHighestBalance());
            bankStatementResult.setLowestBalanceDate(bankStatement.getLowestBalanceDate());
            bankStatementResult.setLowestBalance(bankStatement.getLowestBalance());
            bankStatementResult.setMonthEndBalance(bankStatement.getMonthEndBalance());
            bankStatementResult.setNumberOfChequeReturn(bankStatement.getCheckReturnNumber());
            bankStatementResult.setChequeReturnAmount(bankStatement.getCheckReturnAmount());
            bankStatementResult.setNumberOfTimesOD(bankStatement.getOdLimitNumber());
            bankStatementResult.setStartODDate(bankStatement.getStartOdDate());
            bankStatementResult.setEndODDate(bankStatement.getEndOdDate());
            bankStatementResult.setAsOfDate(bankStatement.getAsOfDate());
        }
        return bankStatementResult;
    }

    public DWHBankStatement transform(BankStatement10 bankStatement) {
        log.debug("transform() BankStatement: {}", bankStatement);
        DWHBankStatement bankStatementResult = new DWHBankStatement();
        if (bankStatement != null) {
            bankStatementResult.setAccountStatus(bankStatement.getAccountStatus());
            bankStatementResult.setAccountOpenDate(bankStatement.getAccountOpenDate());
            bankStatementResult.setBranchCode(bankStatement.getBranchCode());
            bankStatementResult.setDataSource(bankStatement.getDataSource());
            bankStatementResult.setAccountNumber(bankStatement.getAccountNumber());
            bankStatementResult.setAccountName(bankStatement.getAccountName());
            bankStatementResult.setOverLimitAmount(bankStatement.getOdLimit());
            bankStatementResult.setGrossCreditBalance(bankStatement.getGrossCreditBalance());
            bankStatementResult.setNumberOfCreditTxn(bankStatement.getCreditTXNNumber());
            bankStatementResult.setDebitAmount(bankStatement.getGrossDebitBalance());
            bankStatementResult.setNumberOfDebitTxn(bankStatement.getDebitTXNNumber());
            bankStatementResult.setHighestBalanceDate(bankStatement.getHighestBalanceDate());
            bankStatementResult.setHighestBalance(bankStatement.getHighestBalance());
            bankStatementResult.setLowestBalanceDate(bankStatement.getLowestBalanceDate());
            bankStatementResult.setLowestBalance(bankStatement.getLowestBalance());
            bankStatementResult.setMonthEndBalance(bankStatement.getMonthEndBalance());
            bankStatementResult.setNumberOfChequeReturn(bankStatement.getCheckReturnNumber());
            bankStatementResult.setChequeReturnAmount(bankStatement.getCheckReturnAmount());
            bankStatementResult.setNumberOfTimesOD(bankStatement.getOdLimitNumber());
            bankStatementResult.setStartODDate(bankStatement.getStartOdDate());
            bankStatementResult.setEndODDate(bankStatement.getEndOdDate());
            bankStatementResult.setAsOfDate(bankStatement.getAsOfDate());
        }
        return bankStatementResult;
    }

    public DWHBankStatement transform(BankStatement11 bankStatement) {
        log.debug("transform() BankStatement: {}", bankStatement);
        DWHBankStatement bankStatementResult = new DWHBankStatement();
        if (bankStatement != null) {
            bankStatementResult.setAccountStatus(bankStatement.getAccountStatus());
            bankStatementResult.setAccountOpenDate(bankStatement.getAccountOpenDate());
            bankStatementResult.setBranchCode(bankStatement.getBranchCode());
            bankStatementResult.setDataSource(bankStatement.getDataSource());
            bankStatementResult.setAccountNumber(bankStatement.getAccountNumber());
            bankStatementResult.setAccountName(bankStatement.getAccountName());
            bankStatementResult.setOverLimitAmount(bankStatement.getOdLimit());
            bankStatementResult.setGrossCreditBalance(bankStatement.getGrossCreditBalance());
            bankStatementResult.setNumberOfCreditTxn(bankStatement.getCreditTXNNumber());
            bankStatementResult.setDebitAmount(bankStatement.getGrossDebitBalance());
            bankStatementResult.setNumberOfDebitTxn(bankStatement.getDebitTXNNumber());
            bankStatementResult.setHighestBalanceDate(bankStatement.getHighestBalanceDate());
            bankStatementResult.setHighestBalance(bankStatement.getHighestBalance());
            bankStatementResult.setLowestBalanceDate(bankStatement.getLowestBalanceDate());
            bankStatementResult.setLowestBalance(bankStatement.getLowestBalance());
            bankStatementResult.setMonthEndBalance(bankStatement.getMonthEndBalance());
            bankStatementResult.setNumberOfChequeReturn(bankStatement.getCheckReturnNumber());
            bankStatementResult.setChequeReturnAmount(bankStatement.getCheckReturnAmount());
            bankStatementResult.setNumberOfTimesOD(bankStatement.getOdLimitNumber());
            bankStatementResult.setStartODDate(bankStatement.getStartOdDate());
            bankStatementResult.setEndODDate(bankStatement.getEndOdDate());
            bankStatementResult.setAsOfDate(bankStatement.getAsOfDate());
        }
        return bankStatementResult;
    }

    public DWHBankStatement transform(BankStatement12 bankStatement) {
        log.debug("transform() BankStatement: {}", bankStatement);
        DWHBankStatement bankStatementResult = new DWHBankStatement();
        if (bankStatement != null) {
            bankStatementResult.setAccountStatus(bankStatement.getAccountStatus());
            bankStatementResult.setAccountOpenDate(bankStatement.getAccountOpenDate());
            bankStatementResult.setBranchCode(bankStatement.getBranchCode());
            bankStatementResult.setDataSource(bankStatement.getDataSource());
            bankStatementResult.setAccountNumber(bankStatement.getAccountNumber());
            bankStatementResult.setAccountName(bankStatement.getAccountName());
            bankStatementResult.setOverLimitAmount(bankStatement.getOdLimit());
            bankStatementResult.setGrossCreditBalance(bankStatement.getGrossCreditBalance());
            bankStatementResult.setNumberOfCreditTxn(bankStatement.getCreditTXNNumber());
            bankStatementResult.setDebitAmount(bankStatement.getGrossDebitBalance());
            bankStatementResult.setNumberOfDebitTxn(bankStatement.getDebitTXNNumber());
            bankStatementResult.setHighestBalanceDate(bankStatement.getHighestBalanceDate());
            bankStatementResult.setHighestBalance(bankStatement.getHighestBalance());
            bankStatementResult.setLowestBalanceDate(bankStatement.getLowestBalanceDate());
            bankStatementResult.setLowestBalance(bankStatement.getLowestBalance());
            bankStatementResult.setMonthEndBalance(bankStatement.getMonthEndBalance());
            bankStatementResult.setNumberOfChequeReturn(bankStatement.getCheckReturnNumber());
            bankStatementResult.setChequeReturnAmount(bankStatement.getCheckReturnAmount());
            bankStatementResult.setNumberOfTimesOD(bankStatement.getOdLimitNumber());
            bankStatementResult.setStartODDate(bankStatement.getStartOdDate());
            bankStatementResult.setEndODDate(bankStatement.getEndOdDate());
            bankStatementResult.setAsOfDate(bankStatement.getAsOfDate());
        }
        return bankStatementResult;
    }

    public DWHBankStatement transform(BankStatement13 bankStatement) {
        log.debug("transform() BankStatement: {}", bankStatement);
        DWHBankStatement bankStatementResult = new DWHBankStatement();
        if (bankStatement != null) {
            bankStatementResult.setAccountStatus(bankStatement.getAccountStatus());
            bankStatementResult.setAccountOpenDate(bankStatement.getAccountOpenDate());
            bankStatementResult.setBranchCode(bankStatement.getBranchCode());
            bankStatementResult.setDataSource(bankStatement.getDataSource());
            bankStatementResult.setAccountNumber(bankStatement.getAccountNumber());
            bankStatementResult.setAccountName(bankStatement.getAccountName());
            bankStatementResult.setOverLimitAmount(bankStatement.getOdLimit());
            bankStatementResult.setGrossCreditBalance(bankStatement.getGrossCreditBalance());
            bankStatementResult.setNumberOfCreditTxn(bankStatement.getCreditTXNNumber());
            bankStatementResult.setDebitAmount(bankStatement.getGrossDebitBalance());
            bankStatementResult.setNumberOfDebitTxn(bankStatement.getDebitTXNNumber());
            bankStatementResult.setHighestBalanceDate(bankStatement.getHighestBalanceDate());
            bankStatementResult.setHighestBalance(bankStatement.getHighestBalance());
            bankStatementResult.setLowestBalanceDate(bankStatement.getLowestBalanceDate());
            bankStatementResult.setLowestBalance(bankStatement.getLowestBalance());
            bankStatementResult.setMonthEndBalance(bankStatement.getMonthEndBalance());
            bankStatementResult.setNumberOfChequeReturn(bankStatement.getCheckReturnNumber());
            bankStatementResult.setChequeReturnAmount(bankStatement.getCheckReturnAmount());
            bankStatementResult.setNumberOfTimesOD(bankStatement.getOdLimitNumber());
            bankStatementResult.setStartODDate(bankStatement.getStartOdDate());
            bankStatementResult.setEndODDate(bankStatement.getEndOdDate());
            bankStatementResult.setAsOfDate(bankStatement.getAsOfDate());
        }
        return bankStatementResult;
    }

    public DWHBankStatement transform(BankStatement14 bankStatement) {
        log.debug("transform() BankStatement: {}", bankStatement);
        DWHBankStatement bankStatementResult = new DWHBankStatement();
        if (bankStatement != null) {
            bankStatementResult.setAccountStatus(bankStatement.getAccountStatus());
            bankStatementResult.setAccountOpenDate(bankStatement.getAccountOpenDate());
            bankStatementResult.setBranchCode(bankStatement.getBranchCode());
            bankStatementResult.setDataSource(bankStatement.getDataSource());
            bankStatementResult.setAccountNumber(bankStatement.getAccountNumber());
            bankStatementResult.setAccountName(bankStatement.getAccountName());
            bankStatementResult.setOverLimitAmount(bankStatement.getOdLimit());
            bankStatementResult.setGrossCreditBalance(bankStatement.getGrossCreditBalance());
            bankStatementResult.setNumberOfCreditTxn(bankStatement.getCreditTXNNumber());
            bankStatementResult.setDebitAmount(bankStatement.getGrossDebitBalance());
            bankStatementResult.setNumberOfDebitTxn(bankStatement.getDebitTXNNumber());
            bankStatementResult.setHighestBalanceDate(bankStatement.getHighestBalanceDate());
            bankStatementResult.setHighestBalance(bankStatement.getHighestBalance());
            bankStatementResult.setLowestBalanceDate(bankStatement.getLowestBalanceDate());
            bankStatementResult.setLowestBalance(bankStatement.getLowestBalance());
            bankStatementResult.setMonthEndBalance(bankStatement.getMonthEndBalance());
            bankStatementResult.setNumberOfChequeReturn(bankStatement.getCheckReturnNumber());
            bankStatementResult.setChequeReturnAmount(bankStatement.getCheckReturnAmount());
            bankStatementResult.setNumberOfTimesOD(bankStatement.getOdLimitNumber());
            bankStatementResult.setStartODDate(bankStatement.getStartOdDate());
            bankStatementResult.setEndODDate(bankStatement.getEndOdDate());
            bankStatementResult.setAsOfDate(bankStatement.getAsOfDate());
        }
        return bankStatementResult;
    }
}
