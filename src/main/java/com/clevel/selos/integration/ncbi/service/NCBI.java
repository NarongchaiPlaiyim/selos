package com.clevel.selos.integration.ncbi.service;

import com.clevel.selos.model.db.export.NCBIExport;

import java.util.ArrayList;
import java.util.List;

public interface NCBI {
    public ArrayList<String> XMLFormatRequestTransaction(List<NCBIExport> ncbiExportList) throws Exception;
}
