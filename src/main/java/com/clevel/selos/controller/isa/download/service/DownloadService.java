package com.clevel.selos.controller.isa.download.service;


import com.clevel.selos.integration.SELOS;
import org.primefaces.model.DefaultStreamedContent;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.io.File;
import java.io.FileInputStream;

public class DownloadService {
    @Inject
    @SELOS
    private Logger log;
    private static final String CHARSET = "TIS-620";
    private static final String UTF_8 = "UTF-8";
    private static final String CONTENT_TYPE = "text/csv";
    @Inject
    public DownloadService() {

    }

    public DefaultStreamedContent process(final String fullPath) throws Exception{
        final File file = new File(fullPath);
        return new DefaultStreamedContent(new FileInputStream(file), CONTENT_TYPE, file.getName(), UTF_8);
    }
}
