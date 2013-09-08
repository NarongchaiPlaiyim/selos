package com.clevel.selos.integration.nccrs.models.response;

public class H2HResponseModel {
    private H2HResponseHeaderModel header;
    private H2HResponseSubjectModel subject;
    private H2HErrorModel h2herror;

    public H2HResponseHeaderModel getHeader() {
        return header;
    }

    public H2HResponseSubjectModel getSubject() {
        return subject;
    }

    public H2HErrorModel getH2herror() {
        return h2herror;
    }
    
}
