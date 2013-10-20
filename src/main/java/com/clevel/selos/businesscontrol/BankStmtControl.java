package com.clevel.selos.businesscontrol;

import com.clevel.selos.integration.DWHInterface;
import com.clevel.selos.integration.RMInterface;
import com.clevel.selos.integration.corebanking.model.customeraccount.CustomerAccountListModel;
import com.clevel.selos.integration.corebanking.model.customeraccount.CustomerAccountResult;
import com.clevel.selos.integration.dwh.bankstatement.model.DWHBankStatement;
import com.clevel.selos.integration.dwh.bankstatement.model.DWHBankStatementResult;
import com.clevel.selos.model.ActionResult;
import com.clevel.selos.model.db.working.BankStatementSummary;
import com.clevel.selos.model.view.*;
import com.clevel.selos.transform.ActionStatusTransform;
import com.clevel.selos.transform.BankStmtTransform;
import com.clevel.selos.util.DateTimeUtil;
import com.clevel.selos.util.Util;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Stateless
public class BankStmtControl extends BusinessControl{

    @Inject
    private RMInterface rmInterface;

    @Inject
    DWHInterface dwhInterface;

    @Inject
    ActionStatusTransform actionStatusTransform;

    @Inject
    BankStmtTransform bankStmtTransform;

    public BankStmtSummaryView retreiveBankStmtInterface(List<CustomerInfoView> customerInfoViewList, Date expectedSumitDate){
        return retreiveBankStmtInterface(customerInfoViewList, expectedSumitDate, 0);
    }

    public BankStmtSummaryView retreiveBankStmtInterface(List<CustomerInfoView> customerInfoViewList, Date expectedSumitDate, int seasonal){
        log.info("Start retreiveBankStmtInterface with {}", customerInfoViewList);
        Date startBankStmtDate = getStartBankStmtDate(expectedSumitDate);
        int numberOfMonthBankStmt = getRetrieveMonthBankStmt(seasonal);

        BankStmtSummaryView bankStmtSummaryView = new BankStmtSummaryView();
        List<ActionStatusView> actionStatusViewList = new ArrayList<ActionStatusView>();
        List<BankStmtView> bankStmtViewList = new ArrayList<BankStmtView>();

        for(CustomerInfoView customerInfoView : customerInfoViewList){
            if(customerInfoView.getRelation().getId() == 1){
                if(!Util.isEmpty(customerInfoView.getTmbCustomerId())){
                    log.info("Finding Account Number List for TMB Cus ID: {}", customerInfoView.getTmbCustomerId());
                    CustomerAccountResult customerAccountResult = rmInterface.getCustomerAccountInfo(getCurrentUserID(), customerInfoView.getTmbCustomerId());
                    //CustomerAccountResult customerAccountResult = getBankAccountList(customerInfoView.getTmbCustomerId());
                    if(customerAccountResult.getActionResult().equals(ActionResult.SUCCESS)){
                        List<CustomerAccountListModel> accountListModelList = customerAccountResult.getAccountListModels();
                        log.info("Finding account {}", accountListModelList);
                        for(CustomerAccountListModel customerAccountListModel : accountListModelList){
                            DWHBankStatementResult dwhBankStatementResult = dwhInterface.getBankStatementData(getCurrentUserID(), customerAccountListModel.getAccountNo(), startBankStmtDate, numberOfMonthBankStmt);

                            if(dwhBankStatementResult.getActionResult().equals(ActionResult.SUCCESS)){
                                List<DWHBankStatement> dwhBankStatementList = dwhBankStatementResult.getBankStatementList();
                                BankStmtView bankStmtView = null;
                                List<BankStmtDetailView> bankStmtDetailViewList = new ArrayList<BankStmtDetailView>();
                                for(DWHBankStatement dwhBankStatement : dwhBankStatementList){

                                    BankStmtDetailView bankStmtDetailView = bankStmtTransform.getBankStmtDetailView(dwhBankStatement);
                                    if(bankStmtView == null){
                                        bankStmtView = bankStmtTransform.getBankStmtView(dwhBankStatement);
                                    }
                                    bankStmtDetailViewList.add(bankStmtDetailView);
                                }
                                bankStmtView.setBankStmtDetailViewList(bankStmtDetailViewList);
                                bankStmtViewList.add(bankStmtView);
                            } else {
                                actionStatusViewList.add(actionStatusTransform.getActionStatusView(dwhBankStatementResult.getActionResult(), dwhBankStatementResult.getReason()));
                            }
                        }
                    } else {
                        actionStatusViewList.add(actionStatusTransform.getActionStatusView(customerAccountResult.getActionResult(), customerAccountResult.getReason()));

                    }
                }
            }
        }
        bankStmtSummaryView.setActionStatusViewList(actionStatusViewList);
        bankStmtSummaryView.setBankStmtViewList(bankStmtViewList);
        return bankStmtSummaryView;
    }

    public BankStatementSummary getBankStmt(){

        return null;
    }

    /**
     * To get starting date of retrieving bank account
     * If expected submission date less than 15 get current month -2 (T-2), If more than 15 get current month -1 (T-1)
     * Ex. expectedSubmissionDate: 15/10/2013 -> (T-1) -> start previous month at 'September'
     * @param expectedSubmissionDate
     * @return Start previous date by bank account condition
     */
    public Date getStartBankStmtDate(Date expectedSubmissionDate) {
        if (expectedSubmissionDate != null) {
            int days = Util.getDayOfDate(expectedSubmissionDate);
            int retrieveMonth = days < 15 ? 2 : 1;
            return DateTimeUtil.getOnlyDatePlusMonth(expectedSubmissionDate, -retrieveMonth);
        }
        return null;
    }

    public int getRetrieveMonthBankStmt(int seasonalFlag) {
        return seasonalFlag == 1 ? 12 : 6;
    }

    private CustomerAccountResult getBankAccountList(String tmbCusId){
        List<CustomerAccountListModel> accountListModelList = new ArrayList<CustomerAccountListModel>();
        if(tmbCusId.equals("001100000000000000000006106302")){
            CustomerAccountListModel customerAccountListModel1 = new CustomerAccountListModel();
            customerAccountListModel1.setAccountNo("3042582720");

            CustomerAccountListModel customerAccountListModel2 = new CustomerAccountListModel();
            customerAccountListModel2.setAccountNo("3042886758");

            CustomerAccountListModel customerAccountListModel3 = new CustomerAccountListModel();
            customerAccountListModel3.setAccountNo("3042843353");

            CustomerAccountListModel customerAccountListModel4 = new CustomerAccountListModel();
            customerAccountListModel4.setAccountNo("3052116807");

            accountListModelList.add(customerAccountListModel1);
            accountListModelList.add(customerAccountListModel2);
            accountListModelList.add(customerAccountListModel3);
            accountListModelList.add(customerAccountListModel4);


        }

        CustomerAccountResult customerAccountResult = new CustomerAccountResult();
        customerAccountResult.setActionResult(ActionResult.SUCCESS);
        customerAccountResult.setCustomerId(tmbCusId);
        customerAccountResult.setAccountListModels(accountListModelList);
        return customerAccountResult;
    }
}
