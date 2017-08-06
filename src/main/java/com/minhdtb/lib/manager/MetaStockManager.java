package com.minhdtb.lib.manager;

import com.minhdtb.lib.data.MetaStockData;
import com.minhdtb.lib.data.MetaStockDataRecord;
import com.minhdtb.lib.masters.*;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class MetaStockManager {

    private String path;
    private List<MetaStockHistory> historyList = new ArrayList<>();

    private List<MetaStockHistoryData> openData(File dataFile) {
        List<MetaStockHistoryData> dataList = new ArrayList<>();
        MetaStockData data = new MetaStockData(dataFile);
        for (MetaStockDataRecord dataRecord : data.getRecords()) {
            MetaStockHistoryData historyData = new MetaStockHistoryData(dataRecord.getDate(),
                    dataRecord.getOpen(), dataRecord.getHigh(), dataRecord.getLow(), dataRecord.getClose(), dataRecord.getVolume());
            dataList.add(historyData);
        }

        return dataList;
    }

    public void open(String path) {
        historyList.clear();

        this.path = path.endsWith("\\") ? path : path + "\\";

        new File(path).mkdirs();

        File masterFile = new File(this.path + "MASTER");
        if (masterFile.exists()) {
            MetaStockMaster master = new MetaStockMaster(masterFile);
            for (MetaStockMasterRecord record : master.getRecords()) {
                File masterDataFile = new File(this.path + "F" + record.getFileNumber() + ".DAT");
                if (masterDataFile.exists()) {
                    MetaStockHistory history = new MetaStockHistory(record.getSymbol(), record.getPeriod(), record.getStartDate(),
                            record.getEndDate(), record.getFileNumber(), masterDataFile);
                    historyList.add(history);
                }
            }
        }

        masterFile = new File(this.path + "XMASTER");
        if (masterFile.exists()) {
            MetaStockXMaster eMaster = new MetaStockXMaster(masterFile);
            for (MetaStockXMasterRecord record : eMaster.getRecords()) {
                File masterDataFile = new File(this.path + "F" + record.getFileNumber() + ".MWD");
                if (masterDataFile.exists()) {
                    if (masterDataFile.exists()) {
                        MetaStockHistory history = new MetaStockHistory(record.getSymbol(), record.getPeriod(), record.getStartDate(),
                                record.getEndDate(), record.getFileNumber(), masterDataFile);
                        historyList.add(history);
                    }
                }
            }
        }
    }

    public List<MetaStockHistory> getHistoryList() {
        return historyList;
    }

    public List<MetaStockHistoryData> getHistory(String name) {
        Optional<MetaStockHistory> found = historyList.stream().filter(data -> Objects.equals(data.getSymbol(), name)).findFirst();
        if (found.isPresent()) {
            return openData(found.get().getFile());
        }

        return new ArrayList<>();
    }

    public void addHistory(String symbol, String period, MetaStockHistoryData historyData) throws IOException {
        Optional<MetaStockHistory> found = historyList.stream().filter(data -> Objects.equals(data.getSymbol(), symbol)).findFirst();
        if (found.isPresent()) {
            MetaStockHistory history = found.get();
            addExisting(history, historyData);
        } else {
            Optional<MetaStockHistory> max = historyList.stream().max(Comparator.comparingInt(MetaStockHistory::getFileNumber));
            int fileNumber = max.map(metaStockHistory -> metaStockHistory.getFileNumber() + 1).orElse(1);

            File file;
            if (fileNumber <= 255) {
                file = new File(path + "F" + fileNumber + ".DAT");
            } else {
                file = new File(path + "F" + fileNumber + ".MWD");
            }

            MetaStockHistory history = new MetaStockHistory(symbol, period, historyData.getDate(),
                    historyData.getDate(), fileNumber, file);
            addNew(history, historyData);
        }

        open(path);
    }

    private void addExisting(MetaStockHistory history, MetaStockHistoryData historyData) {
        // add to data
        MetaStockData data = new MetaStockData(history.getFile());
        MetaStockDataRecord dataRecord = new MetaStockDataRecord(historyData.getDate(), historyData.getOpen(),
                historyData.getHigh(), historyData.getLow(), historyData.getClose(), historyData.getVolume(), 0);
        data.addRecord(dataRecord);
        // update master
        int fileNumber = history.getFileNumber();
        if (fileNumber <= 255) {
            // update master file
            MetaStockMaster master = new MetaStockMaster(new File(path + "MASTER"));
            Optional<MetaStockMasterRecord> optional1 = master.getRecords().stream()
                    .filter(value -> Objects.equals(value.getSymbol(), history.getSymbol())).findFirst();
            if (optional1.isPresent()) {
                MetaStockMasterRecord oldRecord = optional1.get();
                MetaStockMasterRecord newRecord = new MetaStockMasterRecord(
                        oldRecord.getSymbol(),
                        oldRecord.getDescription(),
                        oldRecord.getPeriod(),
                        oldRecord.getFileNumber(),
                        oldRecord.getFileType(),
                        oldRecord.getNumberOfFields(),
                        oldRecord.getVersion(),
                        oldRecord.getStartDate(),
                        historyData.getDate()
                );
                master.updateRecord(newRecord);
            }
            // update e master file
            MetaStockEMaster eMaster = new MetaStockEMaster(new File(path + "EMASTER"));
            Optional<MetaStockEMasterRecord> optional2 = eMaster.getRecords().stream()
                    .filter(value -> Objects.equals(value.getSymbol(), history.getSymbol())).findFirst();
            if (optional2.isPresent()) {
                MetaStockEMasterRecord oldRecord = optional2.get();
                MetaStockEMasterRecord newRecord = new MetaStockEMasterRecord(
                        oldRecord.getSymbol(),
                        oldRecord.getDescription(),
                        oldRecord.getPeriod(),
                        oldRecord.getFileNumber(),
                        oldRecord.getTotalFields(),
                        oldRecord.getStartDate(),
                        historyData.getDate()
                );
                eMaster.updateRecord(newRecord);
            }
        } else {
            // update x master file
            MetaStockXMaster xMaster = new MetaStockXMaster(new File(path + "XMASTER"));
            Optional<MetaStockXMasterRecord> optional = xMaster.getRecords().stream()
                    .filter(value -> Objects.equals(value.getSymbol(), history.getSymbol())).findFirst();
            if (optional.isPresent()) {
                MetaStockXMasterRecord oldRecord = optional.get();
                MetaStockXMasterRecord newRecord = new MetaStockXMasterRecord(
                        oldRecord.getSymbol(),
                        oldRecord.getDescription(),
                        oldRecord.getPeriod(),
                        oldRecord.getFileNumber(),
                        oldRecord.getStartDate(),
                        historyData.getDate(),
                        oldRecord.getFirstDate(),
                        historyData.getDate()
                );
                xMaster.updateRecord(newRecord);
            }
        }
    }

    private void addNew(MetaStockHistory history, MetaStockHistoryData historyData) {
        // add to data
        MetaStockData data = new MetaStockData(history.getFile());
        MetaStockDataRecord dataRecord = new MetaStockDataRecord(historyData.getDate(), historyData.getOpen(),
                historyData.getHigh(), historyData.getLow(), historyData.getClose(), historyData.getVolume(), 0);
        data.addRecord(dataRecord);
        // add to master
        int fileNumber = history.getFileNumber();
        if (fileNumber <= 255) {
            // add to master file
            MetaStockMaster master = new MetaStockMaster(new File(path + "MASTER"));
            MetaStockMasterRecord newRecord1 = new MetaStockMasterRecord(
                    history.getSymbol(),
                    history.getSymbol(),
                    history.getPeriod(),
                    history.getFileNumber(),
                    "e", 7, null, historyData.getDate(), historyData.getDate());
            master.addRecord(newRecord1);
            // add to e master file
            MetaStockEMaster eMaster = new MetaStockEMaster(new File(path + "EMASTER"));
            MetaStockEMasterRecord newRecord2 = new MetaStockEMasterRecord(
                    history.getSymbol(),
                    history.getSymbol(),
                    history.getPeriod(),
                    history.getFileNumber(),
                    7, historyData.getDate(), historyData.getDate());
            eMaster.addRecord(newRecord2);
        } else {
            // add to x master file
            MetaStockXMaster xMaster = new MetaStockXMaster(new File(path + "XMASTER"));
            MetaStockXMasterRecord record = new MetaStockXMasterRecord(
                    history.getSymbol(),
                    history.getSymbol(),
                    history.getPeriod(),
                    history.getFileNumber(),
                    historyData.getDate(),
                    historyData.getDate(),
                    historyData.getDate(),
                    historyData.getDate());
            xMaster.addRecord(record);
        }
    }
}