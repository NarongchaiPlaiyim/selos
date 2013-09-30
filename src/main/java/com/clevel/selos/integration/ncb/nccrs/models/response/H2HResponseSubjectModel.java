package com.clevel.selos.integration.ncb.nccrs.models.response;

public class H2HResponseSubjectModel {
    private HeadingModel heading;
    private ProfileModel profile;
    private ShareHoldersModel shareholders;
    private CollateralsModel collaterals; 
    private DisPuteModel dispute;
    private CreditScoreModel creditscore;
    private ExecutiveSummaryModel executivesummary;
    private CreditSummaryModel creditsummary;
    private ActiveAccountsModel activeaccounts;
    private ClosedAccountsModel closedaccounts;
    private InquiryHistoriesModel inquiryhistories;
    private ContactMessageModel contactmessage;
    private HistoricalBalanceReportModel historicalbalancereport;
    
    public HeadingModel getHeading() {
        return heading;
    }

    public ProfileModel getProfile() {
        return profile;
    }

    public ShareHoldersModel getShareholders() {
        return shareholders;
    }

    public CollateralsModel getCollaterals() {
        return collaterals;
    }

    public ExecutiveSummaryModel getExecutivesummary() {
        return executivesummary;
    }

    public CreditSummaryModel getCreditsummary() {
        return creditsummary;
    }

    public ActiveAccountsModel getActiveaccounts() {
        return activeaccounts;
    }

    public ClosedAccountsModel getClosedaccounts() {
        return closedaccounts;
    }

    public InquiryHistoriesModel getInquiryhistories() {
        return inquiryhistories;
    }

    public ContactMessageModel getContactmessage() {
        return contactmessage;
    }

    public HistoricalBalanceReportModel getHistoricalbalancereport() {
        return historicalbalancereport;
    }

    public DisPuteModel getDispute() {
        return dispute;
    }

    public CreditScoreModel getCreditscore() {
        return creditscore;
    }
}
