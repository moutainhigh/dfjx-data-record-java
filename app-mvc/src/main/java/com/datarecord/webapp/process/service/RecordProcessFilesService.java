package com.datarecord.webapp.process.service;

import java.io.File;
import java.io.IOException;

public interface RecordProcessFilesService {
    String getImportTemplate(String jobId) throws IOException;

    void importRecordData(String jobId, String reportId, File importFile) throws IOException;

    void overrideImport(String jobId, String reportId, File importFile) throws IOException;

    void extendImport(String jobId, String reportId, File importFile) throws IOException;
}
