package com.clevel.selos.transform;

import com.clevel.selos.dao.master.AccountStatusDAO;
import com.clevel.selos.dao.master.BankAccountTypeDAO;
import com.clevel.selos.dao.master.BankDAO;
import com.clevel.selos.dao.master.DWHBankDataSourceDAO;
import com.clevel.selos.dao.working.BankStatementDAO;
import com.clevel.selos.dao.working.BankStatementDetailDAO;
import com.clevel.selos.dao.working.BankStatementSummaryDAO;
import com.clevel.selos.dao.working.BankStmtSrcOfCollateralProofDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.integration.dwh.bankstatement.model.DWHBankStatement;
import com.clevel.selos.model.BankType;
import com.clevel.selos.model.db.master.DWHBankDataSource;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.working.BankStatement;
import com.clevel.selos.model.db.working.BankStatementDetail;
import com.clevel.selos.model.db.working.BankStatementSummary;
import com.clevel.selos.model.db.working.BankStmtSrcOfCollateralProof;
import com.clevel.selos.model.view.*;
import com.clevel.selos.model.view.master.BankAccountTypeView;
import com.clevel.selos.transform.master.BankAccountTypeTransform;
import com.clevel.selos.util.DateTimeUtil;
import com.clevel.selos.util.Util;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.*;

public class BankStmtTransform extends Transform {
    @SELOS
    @Inject
    private Logger log;

    //DAO
    @Inject
    private BankDAO bankDAO;
    @Inject
    private BankStatementSummaryDAO bankStatementSummaryDAO;
    @Inject
    private BankStatementDAO bankStatementDAO;
    @Inject
    private BankStatementDetailDAO bankStatementDetailDAO;
    @Inject
    private BankStmtSrcOfCollateralProofDAO srcOfCollateralProofDAO;
    @Inject
    private BankAccountTypeDAO bankAccountTypeDAO;
    @Inject
    private DWHBankDataSourceDAO dwhBankDataSourceDAO;
    @Inject
    private AccountStatusDAO accountStatusDAO;

    //Transform
    @Inject
    AccountStatusTransform accountStatusTransform;
    @Inject
    BankTransform bankTransform;
    @Inject
    BankAccountTypeTransform bankAccountTypeTransform;

    @Inject
    public BankStmtTransform() {
    }

    //================================================== Get View ==================================================//
    public BankStmtView getBankStmtView(DWHBankStatement dwhBankStatement) {
        log.info("Transform BankStmtView with DWHBankStatement : {}", dwhBankStatement);
        BankStmtView bankStmtView = new BankStmtView();
        if (dwhBankStatement != null) {
            bankStmtView.setAccountNumber(dwhBankStatement.getAccountNumber());
            bankStmtView.setAccountName(dwhBankStatement.getAccountName());

            DWHBankDataSource dwhBankDataSource = dwhBankDataSourceDAO.findByDataSource(dwhBankStatement.getDataSource());
            if(dwhBankDataSource != null){
                bankStmtView.setBankAccountTypeView(bankAccountTypeTransform.getBankAccountTypeView(dwhBankDataSource.getBankAccountType()));
                bankStmtView.setBankAccountTypeId(bankStmtView.getBankAccountTypeView().getId());
            } else {
                bankStmtView.setBankAccountTypeView(new BankAccountTypeView());
            }

            AccountTypeView accountTypeView = new AccountTypeView();
            accountTypeView.setAccountType(dwhBankStatement.getDataSource()); //TODO: transform data source to account type

            BankView bankView = bankTransform.transformToView(bankDAO.getTMBBank());
            bankStmtView.setBankView(bankView);
            bankStmtView.setBranchName(dwhBankStatement.getBranchCode());
        }
        log.info("Return BankStmtView : {}", bankStmtView);
        return bankStmtView;
    }

    public BankStmtDetailView getBankStmtDetailView(DWHBankStatement dwhBankStatement) {
        log.info("Transform BankStmtDetailView with DWHBankStatement : {}", dwhBankStatement);
        BankStmtDetailView bankStmtDetailView = new BankStmtDetailView();
        if (dwhBankStatement != null) {
            bankStmtDetailView.setOverLimitAmount(dwhBankStatement.getOverLimitAmount());
            bankStmtDetailView.setGrossCreditBalance(dwhBankStatement.getGrossCreditBalance());
            bankStmtDetailView.setNumberOfCreditTxn(dwhBankStatement.getNumberOfCreditTxn());
            bankStmtDetailView.setDebitAmount(dwhBankStatement.getDebitAmount());
            bankStmtDetailView.setNumberOfDebitTxn(dwhBankStatement.getNumberOfDebitTxn());
            bankStmtDetailView.setDateOfMaxBalance(dwhBankStatement.getHighestBalanceDate());
            bankStmtDetailView.setMaxBalance(dwhBankStatement.getHighestBalance());
            bankStmtDetailView.setDateOfMinBalance(dwhBankStatement.getLowestBalanceDate());
            bankStmtDetailView.setMinBalance(dwhBankStatement.getLowestBalance());
            bankStmtDetailView.setNumberOfChequeReturn(dwhBankStatement.getNumberOfChequeReturn());
            bankStmtDetailView.setChequeReturnAmount(dwhBankStatement.getChequeReturnAmount());
            bankStmtDetailView.setAsOfDate(dwhBankStatement.getAsOfDate());
            bankStmtDetailView.setOverLimitDays(DateTimeUtil.daysBetween2Dates(dwhBankStatement.getStartODDate(), dwhBankStatement.getEndODDate()));
            bankStmtDetailView.setOverLimitTimes(dwhBankStatement.getNumberOfTimesOD());
        }
        log.info("Return BankStmtDetailView : {}", bankStmtDetailView);
        return bankStmtDetailView;
    }

    public BankStmtSummaryView getBankStmtSummaryView(BankStatementSummary bankStatementSummary) {
        BankStmtSummaryView bankStmtSummaryView = new BankStmtSummaryView();
        List<BankStmtView> tmbBankStmtViewList = new ArrayList<BankStmtView>();
        List<BankStmtView> othBankStmtViewList = new ArrayList<BankStmtView>();

        if (bankStatementSummary == null) {
            log.debug("getBankStmtSummaryView() bankStatementSummary is null!");
            bankStmtSummaryView.setTmbBankStmtViewList(tmbBankStmtViewList);
            bankStmtSummaryView.setOthBankStmtViewList(othBankStmtViewList);
            return bankStmtSummaryView;
        }
        bankStmtSummaryView.setId(bankStatementSummary.getId());

        bankStmtSummaryView.setModifyBy(bankStatementSummary.getModifyBy());
        bankStmtSummaryView.setModifyDate(bankStatementSummary.getModifyDate());

        bankStmtSummaryView.setSeasonal(bankStatementSummary.getSeasonal());
        bankStmtSummaryView.setExpectedSubmitDate(bankStatementSummary.getExpectedSubmitDate());
        bankStmtSummaryView.setCountRefresh(bankStatementSummary.getCountRefresh());
        bankStmtSummaryView.setTMBTotalIncomeGross(bankStatementSummary.getTMBTotalIncomeGross());
        bankStmtSummaryView.setTMBTotalIncomeNetBDM(bankStatementSummary.getTMBTotalIncomeNetBDM());
        bankStmtSummaryView.setTMBTotalIncomeNetUW(bankStatementSummary.getTMBTotalIncomeNetUW());

        bankStmtSummaryView.setOthTotalIncomeGross(bankStatementSummary.getOthTotalIncomeGross());
        bankStmtSummaryView.setOthTotalIncomeNetBDM(bankStatementSummary.getOthTotalIncomeNetBDM());
        bankStmtSummaryView.setOthTotalIncomeNetUW(bankStatementSummary.getOthTotalIncomeNetUW());

        bankStmtSummaryView.setGrdTotalIncomeGross(bankStatementSummary.getGrdTotalIncomeGross());
        bankStmtSummaryView.setGrdTotalIncomeNetBDM(bankStatementSummary.getGrdTotalIncomeNetBDM());
        bankStmtSummaryView.setGrdTotalIncomeNetUW(bankStatementSummary.getGrdTotalIncomeNetUW());
        bankStmtSummaryView.setGrdTotalTDChqRetAmount(bankStatementSummary.getGrdTotalTDChqRetAmount());
        bankStmtSummaryView.setGrdTotalTDChqRetPercent(bankStatementSummary.getGrdTotalTDChqRetPercent());
        bankStmtSummaryView.setGrdTotalBorrowerIncomeGross(bankStatementSummary.getGrdTotalBorrowerIncomeGross());
        bankStmtSummaryView.setGrdTotalBorrowerIncomeNetBDM(bankStatementSummary.getGrdTotalBorrowerIncomeNetBDM());
        bankStmtSummaryView.setGrdTotalBorrowerIncomeNetUW(bankStatementSummary.getGrdTotalBorrowerIncomeNetUW());
        bankStmtSummaryView.setGrdTotalAvgOSBalanceAmount(bankStatementSummary.getGrdTotalAvgOSBalanceAmount());

//        Bank tmbBank = bankDAO.getTMBBank();
        for (BankStatement bankStmt : Util.safetyList(bankStatementSummary.getBankStmtList())) {
            log.info("bankStmt.id = {}", bankStmt.getId());
            if (BankType.TMB.shortName().equalsIgnoreCase(bankStmt.getBank().getShortName())) {
                tmbBankStmtViewList.add(getBankStmtView(bankStmt));
            } else {
                othBankStmtViewList.add(getBankStmtView(bankStmt));
            }
        }

        //Order Bank statement By Id ASC
        Collections.sort(tmbBankStmtViewList, new Comparator<BankStmtView>() {
            public int compare(BankStmtView o1, BankStmtView o2) {
                return (int) (o1.getId() - o2.getId());
            }
        });

        Collections.sort(othBankStmtViewList, new Comparator<BankStmtView>() {
            public int compare(BankStmtView o1, BankStmtView o2) {
                return (int) (o1.getId() - o2.getId());
            }
        });

        bankStmtSummaryView.setTmbBankStmtViewList(tmbBankStmtViewList);
        bankStmtSummaryView.setOthBankStmtViewList(othBankStmtViewList);
        return bankStmtSummaryView;
    }

    public BankStmtView getBankStmtView(BankStatement bankStatement) {
        BankStmtView bankStmtView = new BankStmtView();
        if (bankStatement == null) {
            log.debug("getBankStmtView() bankStatement is null!");
            return bankStmtView;
        }
        bankStmtView.setId(bankStatement.getId());

        bankStmtView.setModifyBy(bankStatement.getModifyBy());
        bankStmtView.setModifyDate(bankStatement.getModifyDate());

        bankStmtView.setNotCountIncome(bankStatement.getNotCountIncome());
        bankStmtView.setBankView(bankTransform.transformToView(bankStatement.getBank()));
        bankStmtView.setBranchName(bankStatement.getBranch());
        if (bankStatement.getBankAccountType() != null) {
            bankStmtView.setBankAccountTypeView(bankAccountTypeTransform.getBankAccountTypeView(bankStatement.getBankAccountType()));
            bankStmtView.setBankAccountTypeId(bankStmtView.getBankAccountTypeView().getId());
        }
        bankStmtView.setOtherAccountType(bankStatement.getOtherAccountType());
        bankStmtView.setAccountNumber(bankStatement.getAccountNo());
        bankStmtView.setAccountName(bankStatement.getAccountName());
        bankStmtView.setAccountStatusView(accountStatusTransform.transformToView(bankStatement.getAccountStatus()));
        bankStmtView.setMainAccount(bankStatement.getMainAccount());
        bankStmtView.setAccountCharacteristic(bankStatement.getAccountCharacteristic());
        bankStmtView.setTMB(bankStatement.getTMB());
        bankStmtView.setHighestInflow(bankStatement.getHighestInflow());
        bankStmtView.setAvgLimit(bankStatement.getAvgLimit());
        bankStmtView.setAvgIncomeGross(bankStatement.getAvgIncomeGross());
        bankStmtView.setAvgIncomeNetBDM(bankStatement.getAvgIncomeNetBDM());
        bankStmtView.setAvgIncomeNetUW(bankStatement.getAvgIncomeNetUW());
        bankStmtView.setAvgWithDrawAmount(bankStatement.getAvgDrawAmount());
        bankStmtView.setAvgSwingPercent(bankStatement.getAvgSwingPercent());
        bankStmtView.setAvgUtilizationPercent(bankStatement.getAvgUtilizationPercent());
        bankStmtView.setAvgGrossInflowPerLimit(bankStatement.getAvgGrossInflowPerLimit());
        bankStmtView.setChequeReturn(bankStatement.getChequeReturn());
        bankStmtView.setTrdChequeReturnAmount(bankStatement.getTdChequeReturnAmount());
        bankStmtView.setTrdChequeReturnPercent(bankStatement.getTdChequeReturnPercent());
        bankStmtView.setOverLimitTimes(bankStatement.getOverLimitTimes());
        bankStmtView.setOverLimitDays(bankStatement.getOverLimitDays());
        bankStmtView.setRemark(bankStatement.getRemark());
        bankStmtView.setAvgOSBalanceAmount(bankStatement.getAvgOSBalanceAmount());

        List<BankStmtDetailView> bankStmtDetailViewList = new ArrayList<BankStmtDetailView>();
        for (BankStatementDetail detail : Util.safetyList(bankStatement.getBankStatementDetailList())) {
            bankStmtDetailViewList.add(getBankStmtDetailView(detail));
        }
        bankStmtView.setBankStmtDetailViewList(bankStmtDetailViewList);

        List<BankStmtSrcOfCollateralProofView> srcCollateralProofList = new ArrayList<BankStmtSrcOfCollateralProofView>();
        for (BankStmtSrcOfCollateralProof srcOfCollateralProof : Util.safetyList(bankStatement.getSrcOfCollateralProofList())) {
            srcCollateralProofList.add(getSrcOfCollateralProofView(srcOfCollateralProof));
        }
        bankStmtView.setNetIncomeLastSix(bankStatement.getNetIncomeLastSix());
        bankStmtView.setSrcOfCollateralProofViewList(srcCollateralProofList);
        bankStmtView.setTotalTransaction(bankStatement.getTotalTransaction());
        return bankStmtView;
    }

    public BankStmtDetailView getBankStmtDetailView(BankStatementDetail bankStatementDetail) {
        BankStmtDetailView bankStmtDetailView = new BankStmtDetailView();
        if (bankStatementDetail == null) {
            log.debug("getBankStmtDetailView() bankStatementDetail is null!");
            return bankStmtDetailView;
        }
        bankStmtDetailView.setId(bankStatementDetail.getId());
        bankStmtDetailView.setOverLimitAmount(bankStatementDetail.getOverLimitAmount());
        bankStmtDetailView.setGrossCreditBalance(bankStatementDetail.getGrossCreditBalance());
        bankStmtDetailView.setNumberOfCreditTxn(bankStatementDetail.getNumberOfCreditTxn());
        bankStmtDetailView.setExcludeListBDM(bankStatementDetail.getExcludeListBDM());
        bankStmtDetailView.setExcludeListUW(bankStatementDetail.getExcludeListUW());
        bankStmtDetailView.setCreditAmountBDM(bankStatementDetail.getCreditAmountBDM());
        bankStmtDetailView.setCreditAmountUW(bankStatementDetail.getCreditAmountUW());
        bankStmtDetailView.setTimesOfAvgCreditBDM(bankStatementDetail.getTimesOfAverageCreditBDM());
        bankStmtDetailView.setTimesOfAvgCreditUW(bankStatementDetail.getTimesOfAverageCreditUW());
        bankStmtDetailView.setDebitAmount(bankStatementDetail.getDebitAmount());
        bankStmtDetailView.setNumberOfDebitTxn(bankStatementDetail.getNumberOfDebitTxn());
        bankStmtDetailView.setDateOfMaxBalance(bankStatementDetail.getDateOfMaxBalance());
        bankStmtDetailView.setMaxBalance(bankStatementDetail.getMaxBalance());
        bankStmtDetailView.setDateOfMinBalance(bankStatementDetail.getDateOfMinBalance());
        bankStmtDetailView.setMinBalance(bankStatementDetail.getMinBalance());
        bankStmtDetailView.setMonthBalance(bankStatementDetail.getMonthBalance());
        bankStmtDetailView.setNumberOfChequeReturn(bankStatementDetail.getNumberOfChequeReturn());
        bankStmtDetailView.setChequeReturnAmount(bankStatementDetail.getChequeReturnAmount());
        bankStmtDetailView.setOverLimitTimes(bankStatementDetail.getOverLimitTimes());
        bankStmtDetailView.setOverLimitDays(bankStatementDetail.getOverLimitDays());
        bankStmtDetailView.setSwingPercent(bankStatementDetail.getSwingPercent());
        bankStmtDetailView.setUtilizationPercent(bankStatementDetail.getUtilizationPercent());
        bankStmtDetailView.setGrossInflowPerLimit(bankStatementDetail.getGrossInflowPerLimit());
        bankStmtDetailView.setTotalTransaction(bankStatementDetail.getTotalTransaction());
        bankStmtDetailView.setAsOfDate(bankStatementDetail.getAsOfDate());
        return bankStmtDetailView;
    }

    public BankStmtSrcOfCollateralProofView getSrcOfCollateralProofView(BankStmtSrcOfCollateralProof srcOfCollateralProof) {
        BankStmtSrcOfCollateralProofView srcOfCollateralProofView = new BankStmtSrcOfCollateralProofView();
        if (srcOfCollateralProof == null) {
            log.debug("getSrcOfCollateralProofView() srcOfCollateralProof is null!");
            return srcOfCollateralProofView;
        }
        srcOfCollateralProofView.setId(srcOfCollateralProof.getId());
        srcOfCollateralProofView.setDateOfMaxBalance(srcOfCollateralProof.getDateOfMaxBalance());
        srcOfCollateralProofView.setMaxBalance(srcOfCollateralProof.getMaxBalance());
        return srcOfCollateralProofView;
    }

    //================================================== Get Model ==================================================//
    public BankStatementSummary getBankStatementSummary(BankStmtSummaryView bankStmtSummaryView, User user) {
        BankStatementSummary bankStatementSummary = null;
        Date now = new Date();
        if (bankStmtSummaryView != null) {
            if (bankStmtSummaryView.getId() != 0) {
                bankStatementSummary = bankStatementSummaryDAO.findById(bankStmtSummaryView.getId());
            } else {
                bankStatementSummary = new BankStatementSummary();
                bankStatementSummary.setCreateBy(user);
                bankStatementSummary.setCreateDate(now);
            }
            bankStatementSummary.setModifyBy(user);
            bankStatementSummary.setModifyDate(now);

            bankStatementSummary.setSeasonal(bankStmtSummaryView.getSeasonal());
            bankStatementSummary.setExpectedSubmitDate(bankStmtSummaryView.getExpectedSubmitDate());
            bankStatementSummary.setCountRefresh(bankStmtSummaryView.getCountRefresh());

            bankStatementSummary.setTMBTotalIncomeGross(bankStmtSummaryView.getTMBTotalIncomeGross());
            bankStatementSummary.setTMBTotalIncomeNetBDM(bankStmtSummaryView.getTMBTotalIncomeNetBDM());
            bankStatementSummary.setTMBTotalIncomeNetUW(bankStmtSummaryView.getTMBTotalIncomeNetUW());

            bankStatementSummary.setOthTotalIncomeGross(bankStmtSummaryView.getOthTotalIncomeGross());
            bankStatementSummary.setOthTotalIncomeNetBDM(bankStmtSummaryView.getOthTotalIncomeNetBDM());
            bankStatementSummary.setOthTotalIncomeNetUW(bankStmtSummaryView.getOthTotalIncomeNetUW());

            bankStatementSummary.setGrdTotalIncomeGross(bankStmtSummaryView.getGrdTotalIncomeGross());
            bankStatementSummary.setGrdTotalIncomeNetBDM(bankStmtSummaryView.getGrdTotalIncomeNetBDM());
            bankStatementSummary.setGrdTotalIncomeNetUW(bankStmtSummaryView.getGrdTotalIncomeNetUW());
            bankStatementSummary.setGrdTotalTDChqRetAmount(bankStmtSummaryView.getGrdTotalTDChqRetAmount());
            bankStatementSummary.setGrdTotalTDChqRetPercent(bankStmtSummaryView.getGrdTotalTDChqRetPercent());
            bankStatementSummary.setGrdTotalBorrowerIncomeGross(bankStmtSummaryView.getGrdTotalBorrowerIncomeGross());
            bankStatementSummary.setGrdTotalBorrowerIncomeNetBDM(bankStmtSummaryView.getGrdTotalBorrowerIncomeNetBDM());
            bankStatementSummary.setGrdTotalBorrowerIncomeNetUW(bankStmtSummaryView.getGrdTotalBorrowerIncomeNetUW());
            bankStatementSummary.setGrdTotalAvgOSBalanceAmount(bankStmtSummaryView.getGrdTotalAvgOSBalanceAmount());

            List<BankStatement> bankStatementList = new ArrayList<BankStatement>();
            for (BankStmtView tmbBankStmtView : Util.safetyList(bankStmtSummaryView.getTmbBankStmtViewList())) {
                bankStatementList.add(getBankStatement(tmbBankStmtView, bankStatementSummary, user));
            }
            for (BankStmtView othBankStmtView : Util.safetyList(bankStmtSummaryView.getOthBankStmtViewList())) {
                bankStatementList.add(getBankStatement(othBankStmtView, bankStatementSummary, user));
            }
            bankStatementSummary.setBankStmtList(bankStatementList);
        }
        return bankStatementSummary;
    }

    public BankStatement getBankStatement(BankStmtView bankStmtView, BankStatementSummary bankStatementSummary, User user) {
        BankStatement bankStatement = null;
        if (bankStmtView != null) {
            Date now = new Date();
            if (bankStmtView.getId() != 0) {
                bankStatement = bankStatementDAO.findById(bankStmtView.getId());
            } else {
                bankStatement = new BankStatement();
                bankStatement.setCreateBy(user);
                bankStatement.setCreateDate(now);
            }
            bankStatement.setModifyBy(user);
            bankStatement.setModifyDate(now);
            bankStatement.setNotCountIncome(bankStmtView.getNotCountIncome());
            if (bankStmtView.getBankView().getCode() != 0 ) {
                bankStatement.setBank(bankDAO.findById(bankStmtView.getBankView().getCode()));
            } else {
                bankStatement.setBank(null);
            }
            bankStatement.setBranch(bankStmtView.getBranchName());
//            if(bankStmtView.getBankAccountTypeView() != null
//                    && !Util.isNull(Integer.toString(bankStmtView.getBankAccountTypeView().getId()))
//                    && bankStmtView.getBankAccountTypeView().getId() != 0){
//                bankStatement.setBankAccountType(bankAccountTypeDAO.findById(bankStmtView.getBankAccountTypeView().getId()));
//            } else {
//                bankStatement.setBankAccountType(null);
//            }
            if (bankStmtView.getBankAccountTypeId() != 0) {
                bankStatement.setBankAccountType(bankAccountTypeDAO.findById(bankStmtView.getBankAccountTypeId()));
            } else {
                bankStatement.setBankAccountType(null);
            }
            bankStatement.setAccountNo(Util.removeNonDigit(bankStmtView.getAccountNumber()));
            bankStatement.setAccountName(bankStmtView.getAccountName());
            bankStatement.setOtherAccountType(bankStmtView.getOtherAccountType());
            if(bankStmtView.getAccountStatusView() != null
                    && !Util.isEmpty(bankStmtView.getAccountStatusView().getId())){
                bankStatement.setAccountStatus(accountStatusDAO.findById(Integer.parseInt(bankStmtView.getAccountStatusView().getId())));
            } else {
                bankStatement.setAccountStatus(null);
            }
            bankStatement.setMainAccount(bankStmtView.getMainAccount());
            bankStatement.setAccountCharacteristic(bankStmtView.getAccountCharacteristic());
            bankStatement.setTMB(bankStmtView.getTMB());
            bankStatement.setHighestInflow(bankStmtView.getHighestInflow());
            bankStatement.setAvgLimit(bankStmtView.getAvgLimit());
            bankStatement.setAvgIncomeGross(bankStmtView.getAvgIncomeGross());
            bankStatement.setAvgIncomeNetBDM(bankStmtView.getAvgIncomeNetBDM());
            bankStatement.setAvgIncomeNetUW(bankStmtView.getAvgIncomeNetUW());
            bankStatement.setAvgDrawAmount(bankStmtView.getAvgWithDrawAmount());
            bankStatement.setAvgSwingPercent(bankStmtView.getAvgSwingPercent());
            bankStatement.setAvgUtilizationPercent(bankStmtView.getAvgUtilizationPercent());
            bankStatement.setAvgGrossInflowPerLimit(bankStmtView.getAvgGrossInflowPerLimit());
            bankStatement.setChequeReturn(bankStmtView.getChequeReturn());
            bankStatement.setTdChequeReturnAmount(bankStmtView.getTrdChequeReturnAmount());
            bankStatement.setTdChequeReturnPercent(bankStmtView.getTrdChequeReturnPercent());
            bankStatement.setOverLimitTimes(bankStmtView.getOverLimitTimes());
            bankStatement.setOverLimitDays(bankStmtView.getOverLimitDays());
            bankStatement.setRemark(bankStmtView.getRemark());
            bankStatement.setAvgOSBalanceAmount(bankStmtView.getAvgOSBalanceAmount());
            //set bank statement detail list
            List<BankStatementDetail> bankStatementDetailList = new ArrayList<BankStatementDetail>();
            for (BankStmtDetailView detailView : Util.safetyList(bankStmtView.getBankStmtDetailViewList())) {
                bankStatementDetailList.add(getBankStatementDetail(detailView, bankStatement));
            }
            bankStatement.setBankStatementDetailList(bankStatementDetailList);
            //set new source of collateral proof from view to model
            /*List<BankStmtSrcOfCollateralProof> srcOfCollateralProofList = new ArrayList<BankStmtSrcOfCollateralProof>();
            for (BankStmtSrcOfCollateralProofView srcOfCollateralProofView : Util.safetyList(bankStmtView.getSrcOfCollateralProofViewList())) {
                srcOfCollateralProofList.add(getSrcOfCollateralProof(srcOfCollateralProofView, bankStatement));
            }
            bankStatement.setSrcOfCollateralProofList(srcOfCollateralProofList);*/
            //set parent
            bankStatement.setBankStatementSummary(bankStatementSummary);
            bankStatement.setTotalTransaction(bankStmtView.getTotalTransaction());
        }
        return bankStatement;
    }

    public BankStatement getBankStmtForPersist(BankStmtView bankStmtView, User user) {
        BankStatement bankStatement = null;
        if (bankStmtView != null) {
            Date now = new Date();
            if (bankStmtView.getId() != 0) {
                bankStatement = bankStatementDAO.findById(bankStmtView.getId());
            } else {
                bankStatement = new BankStatement();
                bankStatement.setCreateBy(user);
                bankStatement.setCreateDate(now);
            }
            bankStatement.setModifyBy(user);
            bankStatement.setModifyDate(now);
            bankStatement.setNotCountIncome(bankStmtView.getNotCountIncome());
            bankStatement.setBank(bankStmtView.getBankView().getCode() != 0 ? bankDAO.findById(bankStmtView.getBankView().getCode()) : null);
            bankStatement.setBranch(bankStmtView.getBranchName());
//            if(bankStmtView.getBankAccountTypeView() != null
//                    && !Util.isNull(Integer.toString(bankStmtView.getBankAccountTypeView().getId()))
//                    && bankStmtView.getBankAccountTypeView().getId() != 0){
//                bankStatement.setBankAccountType(bankAccountTypeDAO.findById(bankStmtView.getBankAccountTypeView().getId()));
//            } else {
//                bankStatement.setBankAccountType(null);
//            }
            if (bankStmtView.getBankAccountTypeId() != 0) {
                bankStatement.setBankAccountType(bankAccountTypeDAO.findById(bankStmtView.getBankAccountTypeId()));
            } else {
                bankStatement.setBankAccountType(null);
            }
            bankStatement.setAccountNo(Util.removeNonDigit(bankStmtView.getAccountNumber()));
            bankStatement.setAccountName(bankStmtView.getAccountName());
            bankStatement.setOtherAccountType(bankStmtView.getOtherAccountType());
            if(bankStmtView.getAccountStatusView() != null
                    && !Util.isEmpty(bankStmtView.getAccountStatusView().getId())){
                bankStatement.setAccountStatus(accountStatusDAO.findById(Integer.parseInt(bankStmtView.getAccountStatusView().getId())));
            } else {
                bankStatement.setAccountStatus(null);
            }
            bankStatement.setMainAccount(bankStmtView.getMainAccount());
            bankStatement.setAccountCharacteristic(bankStmtView.getAccountCharacteristic());
            bankStatement.setTMB(bankStmtView.getTMB());

            bankStatement.setHighestInflow(bankStmtView.getHighestInflow());
            bankStatement.setAvgLimit(bankStmtView.getAvgLimit());
            bankStatement.setAvgIncomeGross(bankStmtView.getAvgIncomeGross());
            bankStatement.setAvgIncomeNetBDM(bankStmtView.getAvgIncomeNetBDM());
            bankStatement.setAvgIncomeNetUW(bankStmtView.getAvgIncomeNetUW());
            bankStatement.setAvgDrawAmount(bankStmtView.getAvgWithDrawAmount());
            bankStatement.setAvgSwingPercent(bankStmtView.getAvgSwingPercent());
            bankStatement.setAvgUtilizationPercent(bankStmtView.getAvgUtilizationPercent());
            bankStatement.setAvgGrossInflowPerLimit(bankStmtView.getAvgGrossInflowPerLimit());
            bankStatement.setChequeReturn(bankStmtView.getChequeReturn());
            bankStatement.setTdChequeReturnAmount(bankStmtView.getTrdChequeReturnAmount());
            bankStatement.setTdChequeReturnPercent(bankStmtView.getTrdChequeReturnPercent());
            bankStatement.setOverLimitTimes(bankStmtView.getOverLimitTimes());
            bankStatement.setOverLimitDays(bankStmtView.getOverLimitDays());
            bankStatement.setRemark(bankStmtView.getRemark());
            bankStatement.setAvgOSBalanceAmount(bankStmtView.getAvgOSBalanceAmount());
            bankStatement.setNetIncomeLastSix(bankStmtView.getNetIncomeLastSix());
            //set bank statement detail list
            List<BankStatementDetail> bankStatementDetailList = new ArrayList<BankStatementDetail>();
            for (BankStmtDetailView detailView : Util.safetyList(bankStmtView.getBankStmtDetailViewList())) {
                bankStatementDetailList.add(getBankStatementDetail(detailView, bankStatement));
            }
            bankStatement.setBankStatementDetailList(bankStatementDetailList);
            //set new source of collateral proof from view to model
            List<BankStmtSrcOfCollateralProof> srcOfCollateralProofList = new ArrayList<BankStmtSrcOfCollateralProof>();
            for (BankStmtSrcOfCollateralProofView srcOfCollateralProofView : Util.safetyList(bankStmtView.getSrcOfCollateralProofViewList())) {
                srcOfCollateralProofList.add(getSrcOfCollateralProof(srcOfCollateralProofView, bankStatement));
            }
            bankStatement.setSrcOfCollateralProofList(srcOfCollateralProofList);
            bankStatement.setTotalTransaction(bankStmtView.getTotalTransaction());
        }
        return bankStatement;
    }

    public BankStatementDetail getBankStatementDetail(BankStmtDetailView bankStmtDetailView, BankStatement bankStatement) {
        BankStatementDetail bankStatementDetail = null;
        if (bankStmtDetailView != null) {
            if (bankStmtDetailView.getId() != 0) {
                bankStatementDetail = bankStatementDetailDAO.findById(bankStmtDetailView.getId());
            } else {
                bankStatementDetail = new BankStatementDetail();
            }
            bankStatementDetail.setOverLimitAmount(bankStmtDetailView.getOverLimitAmount());
            bankStatementDetail.setGrossCreditBalance(bankStmtDetailView.getGrossCreditBalance());
            bankStatementDetail.setNumberOfCreditTxn(bankStmtDetailView.getNumberOfCreditTxn());
            bankStatementDetail.setExcludeListBDM(bankStmtDetailView.getExcludeListBDM());
            bankStatementDetail.setExcludeListUW(bankStmtDetailView.getExcludeListUW());
            bankStatementDetail.setCreditAmountBDM(bankStmtDetailView.getCreditAmountBDM());
            bankStatementDetail.setCreditAmountUW(bankStmtDetailView.getCreditAmountUW());
            bankStatementDetail.setTimesOfAverageCreditBDM(bankStmtDetailView.getTimesOfAvgCreditBDM());
            bankStatementDetail.setTimesOfAverageCreditUW(bankStmtDetailView.getTimesOfAvgCreditUW());
            bankStatementDetail.setDebitAmount(bankStmtDetailView.getDebitAmount());
            bankStatementDetail.setNumberOfDebitTxn(bankStmtDetailView.getNumberOfDebitTxn());
            bankStatementDetail.setDateOfMaxBalance(bankStmtDetailView.getDateOfMaxBalance());
            bankStatementDetail.setMaxBalance(bankStmtDetailView.getMaxBalance());
            bankStatementDetail.setDateOfMinBalance(bankStmtDetailView.getDateOfMinBalance());
            bankStatementDetail.setMinBalance(bankStmtDetailView.getMinBalance());
            bankStatementDetail.setMonthBalance(bankStmtDetailView.getMonthBalance());
            bankStatementDetail.setNumberOfChequeReturn(bankStmtDetailView.getNumberOfChequeReturn());
            bankStatementDetail.setChequeReturnAmount(bankStmtDetailView.getChequeReturnAmount());
            bankStatementDetail.setOverLimitTimes(bankStmtDetailView.getOverLimitTimes());
            bankStatementDetail.setOverLimitDays(bankStmtDetailView.getOverLimitDays());
            bankStatementDetail.setSwingPercent(bankStmtDetailView.getSwingPercent());
            bankStatementDetail.setUtilizationPercent(bankStmtDetailView.getUtilizationPercent());
            bankStatementDetail.setGrossInflowPerLimit(bankStmtDetailView.getGrossInflowPerLimit());
            bankStatementDetail.setTotalTransaction(bankStmtDetailView.getTotalTransaction());
            bankStatementDetail.setAsOfDate(bankStmtDetailView.getAsOfDate());
            //set parent
            bankStatementDetail.setBankStatement(bankStatement);
        }
        return bankStatementDetail;
    }

    public BankStmtSrcOfCollateralProof getSrcOfCollateralProof(BankStmtSrcOfCollateralProofView bankStmtSrcOfCollateralProofView, BankStatement bankStatement) {
        BankStmtSrcOfCollateralProof srcOfCollateralProof = null;
        if (bankStmtSrcOfCollateralProofView != null) {
            if (bankStmtSrcOfCollateralProofView.getId() != 0) {
                srcOfCollateralProof = srcOfCollateralProofDAO.findById(bankStmtSrcOfCollateralProofView.getId());
            } else {
                srcOfCollateralProof = new BankStmtSrcOfCollateralProof();
            }
            srcOfCollateralProof.setDateOfMaxBalance(bankStmtSrcOfCollateralProofView.getDateOfMaxBalance());
            srcOfCollateralProof.setMaxBalance(bankStmtSrcOfCollateralProofView.getMaxBalance());
            //set parent
            srcOfCollateralProof.setBankStatement(bankStatement);
        }
        return srcOfCollateralProof;
    }

    public BankStmtView copyToNewBankStmtView(BankStmtView originalBankStmtView) {
        BankStmtView newBankStmtView = new BankStmtView();
        if (originalBankStmtView == null) {
            return newBankStmtView;
        }

        newBankStmtView.setId(originalBankStmtView.getId());
        newBankStmtView.setNotCountIncome(originalBankStmtView.getNotCountIncome());
        newBankStmtView.setBankView(originalBankStmtView.getBankView());
        newBankStmtView.setBranchName(originalBankStmtView.getBranchName());
        newBankStmtView.setBankAccountTypeId(originalBankStmtView.getBankAccountTypeId());
        newBankStmtView.setBankAccountTypeView(originalBankStmtView.getBankAccountTypeView());
        newBankStmtView.setOtherAccountType(originalBankStmtView.getOtherAccountType());
        newBankStmtView.setAccountNumber(originalBankStmtView.getAccountNumber());
        newBankStmtView.setAccountName(originalBankStmtView.getAccountName());
        newBankStmtView.setAccountStatusView(originalBankStmtView.getAccountStatusView());
        newBankStmtView.setMainAccount(originalBankStmtView.getMainAccount());
        newBankStmtView.setAccountCharacteristic(originalBankStmtView.getAccountCharacteristic());
        newBankStmtView.setTMB(originalBankStmtView.getTMB());
        newBankStmtView.setHighestInflow(originalBankStmtView.getHighestInflow());
        newBankStmtView.setAvgLimit(originalBankStmtView.getAvgLimit());
        newBankStmtView.setAvgIncomeGross(originalBankStmtView.getAvgIncomeGross());
        newBankStmtView.setAvgIncomeNetBDM(originalBankStmtView.getAvgIncomeNetBDM());
        newBankStmtView.setAvgIncomeNetUW(originalBankStmtView.getAvgIncomeNetUW());
        newBankStmtView.setAvgWithDrawAmount(originalBankStmtView.getAvgWithDrawAmount());
        newBankStmtView.setAvgSwingPercent(originalBankStmtView.getAvgSwingPercent());
        newBankStmtView.setAvgUtilizationPercent(originalBankStmtView.getAvgUtilizationPercent());
        newBankStmtView.setAvgGrossInflowPerLimit(originalBankStmtView.getAvgGrossInflowPerLimit());
        newBankStmtView.setChequeReturn(originalBankStmtView.getChequeReturn());
        newBankStmtView.setTrdChequeReturnAmount(originalBankStmtView.getTrdChequeReturnAmount());
        newBankStmtView.setTrdChequeReturnPercent(originalBankStmtView.getTrdChequeReturnPercent());
        newBankStmtView.setOverLimitTimes(originalBankStmtView.getOverLimitTimes());
        newBankStmtView.setOverLimitDays(originalBankStmtView.getOverLimitDays());
        newBankStmtView.setRemark(originalBankStmtView.getRemark());
        newBankStmtView.setAvgOSBalanceAmount(originalBankStmtView.getAvgOSBalanceAmount());

        List<BankStmtDetailView> bankStmtDetailViewList = new ArrayList<BankStmtDetailView>();
        for (BankStmtDetailView detail : Util.safetyList(originalBankStmtView.getBankStmtDetailViewList())) {
            bankStmtDetailViewList.add(copyToNewBankStmtDetail(detail));
        }
        newBankStmtView.setBankStmtDetailViewList(bankStmtDetailViewList);

        List<BankStmtSrcOfCollateralProofView> srcCollateralProofList = new ArrayList<BankStmtSrcOfCollateralProofView>();
        for (BankStmtSrcOfCollateralProofView srcOfCollProofView : Util.safetyList(originalBankStmtView.getSrcOfCollateralProofViewList())) {
            srcCollateralProofList.add(copyToNewSrcOfCollProofView(srcOfCollProofView));
        }
        newBankStmtView.setSrcOfCollateralProofViewList(srcCollateralProofList);
        return newBankStmtView;
    }

    public BankStmtDetailView copyToNewBankStmtDetail(BankStmtDetailView originalBankStmtDetailView) {
        BankStmtDetailView newBankStmtDetailView = new BankStmtDetailView();
        if (originalBankStmtDetailView == null) {
            return newBankStmtDetailView;
        }

        newBankStmtDetailView.setId(originalBankStmtDetailView.getId());
        newBankStmtDetailView.setOverLimitAmount(originalBankStmtDetailView.getOverLimitAmount());
        newBankStmtDetailView.setGrossCreditBalance(originalBankStmtDetailView.getGrossCreditBalance());
        newBankStmtDetailView.setNumberOfCreditTxn(originalBankStmtDetailView.getNumberOfCreditTxn());
        newBankStmtDetailView.setExcludeListBDM(originalBankStmtDetailView.getExcludeListBDM());
        newBankStmtDetailView.setExcludeListUW(originalBankStmtDetailView.getExcludeListUW());
        newBankStmtDetailView.setCreditAmountBDM(originalBankStmtDetailView.getCreditAmountBDM());
        newBankStmtDetailView.setCreditAmountUW(originalBankStmtDetailView.getCreditAmountUW());
        newBankStmtDetailView.setTimesOfAvgCreditBDM(originalBankStmtDetailView.getTimesOfAvgCreditBDM());
        newBankStmtDetailView.setTimesOfAvgCreditUW(originalBankStmtDetailView.getTimesOfAvgCreditUW());
        newBankStmtDetailView.setDebitAmount(originalBankStmtDetailView.getDebitAmount());
        newBankStmtDetailView.setNumberOfDebitTxn(originalBankStmtDetailView.getNumberOfDebitTxn());
        newBankStmtDetailView.setDateOfMaxBalance(originalBankStmtDetailView.getDateOfMaxBalance());
        newBankStmtDetailView.setMaxBalance(originalBankStmtDetailView.getMaxBalance());
        newBankStmtDetailView.setDateOfMinBalance(originalBankStmtDetailView.getDateOfMinBalance());
        newBankStmtDetailView.setMinBalance(originalBankStmtDetailView.getMinBalance());
        newBankStmtDetailView.setMonthBalance(originalBankStmtDetailView.getMonthBalance());
        newBankStmtDetailView.setNumberOfChequeReturn(originalBankStmtDetailView.getNumberOfChequeReturn());
        newBankStmtDetailView.setChequeReturnAmount(originalBankStmtDetailView.getChequeReturnAmount());
        newBankStmtDetailView.setOverLimitTimes(originalBankStmtDetailView.getOverLimitTimes());
        newBankStmtDetailView.setOverLimitDays(originalBankStmtDetailView.getOverLimitDays());
        newBankStmtDetailView.setSwingPercent(originalBankStmtDetailView.getSwingPercent());
        newBankStmtDetailView.setUtilizationPercent(originalBankStmtDetailView.getUtilizationPercent());
        newBankStmtDetailView.setGrossInflowPerLimit(originalBankStmtDetailView.getGrossInflowPerLimit());
        newBankStmtDetailView.setTotalTransaction(originalBankStmtDetailView.getTotalTransaction());
        newBankStmtDetailView.setAsOfDate(originalBankStmtDetailView.getAsOfDate());
        return newBankStmtDetailView;
    }

    public BankStmtSrcOfCollateralProofView copyToNewSrcOfCollProofView(BankStmtSrcOfCollateralProofView originalSrcOfCollProofView) {
        BankStmtSrcOfCollateralProofView newSrcOfCollProofView = new BankStmtSrcOfCollateralProofView();
        if (originalSrcOfCollProofView == null) {
            return newSrcOfCollProofView;
        }

        newSrcOfCollProofView.setId(originalSrcOfCollProofView.getId());
        newSrcOfCollProofView.setDateOfMaxBalance(originalSrcOfCollProofView.getDateOfMaxBalance());
        newSrcOfCollProofView.setMaxBalance(originalSrcOfCollProofView.getMaxBalance());
        return newSrcOfCollProofView;
    }
}
