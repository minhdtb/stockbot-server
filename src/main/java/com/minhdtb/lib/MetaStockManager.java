package com.minhdtb.lib;

import com.minhdtb.lib.data.MetaStockData;
import com.minhdtb.lib.data.MetaStockDataRecord;
import com.minhdtb.lib.master.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.io.File;
import java.io.IOException;
import java.util.*;

@ToString
@AllArgsConstructor
@Data
class MetaStockHistory {
    private String symbol;
    private Date startDate;
    private Date endDate;
    private int fileNumber;
    private File file;
}

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
        this.path = path.endsWith("\\") ? path : path + "\\";

        new File(path).mkdirs();

        File masterFile = new File(this.path + "MASTER");
        if (masterFile.exists()) {
            MetaStockMaster master = new MetaStockMaster(masterFile);
            for (MetaStockMasterRecord record : master.getRecords()) {
                File masterDataFile = new File(this.path + "F" + record.getFileNumber() + ".DAT");
                if (masterDataFile.exists()) {
                    MetaStockHistory history = new MetaStockHistory(record.getSymbol(), record.getStartDate(),
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
                        MetaStockHistory history = new MetaStockHistory(record.getSymbol(), record.getStartDate(),
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
            MetaStockData data = new MetaStockData(history.getFile());
            data.getRecords().add(new MetaStockDataRecord(historyData.getDate(), historyData.getOpen(), historyData.getHigh(),
                    historyData.getLow(), historyData.getClose(), historyData.getVolume(), 0));
            data.save(history.getFile());
        } else {
            Optional<MetaStockHistory> max = historyList.stream().max(Comparator.comparingInt(MetaStockHistory::getFileNumber));
            int fileNumber = max.map(metaStockHistory -> metaStockHistory.getFileNumber() + 1).orElse(1);

            if (fileNumber <= 255) {
                File masterFile = new File(this.path + "MASTER");
                if (masterFile.exists()) {
                    MetaStockMaster master = new MetaStockMaster(masterFile);
                    MetaStockMasterRecord masterRecord = new MetaStockMasterRecord(symbol, symbol, period, fileNumber,
                            "e", 7, null, new Date(), historyData.getDate());
                    master.getRecords().add(masterRecord);
                    master.save(masterFile);
                } else {
                    MetaStockMaster master = new MetaStockMaster();
                    MetaStockMasterRecord masterRecord = new MetaStockMasterRecord(symbol, symbol, period, fileNumber,
                            "e", 7, null, new Date(), historyData.getDate());
                    master.getRecords().add(masterRecord);
                    master.save(masterFile);
                }

                File eMasterFile = new File(this.path + "EMASTER");
                if (eMasterFile.exists()) {
                    MetaStockEMaster master = new MetaStockEMaster(eMasterFile);
                    MetaStockEMasterRecord masterRecord = new MetaStockEMasterRecord(symbol, symbol, period, fileNumber,
                            7, new Date(), historyData.getDate());
                    master.getRecords().add(masterRecord);
                    master.save(eMasterFile);
                } else {
                    MetaStockEMaster master = new MetaStockEMaster();
                    MetaStockEMasterRecord masterRecord = new MetaStockEMasterRecord(symbol, symbol, period, fileNumber,
                            7, new Date(), historyData.getDate());
                    master.getRecords().add(masterRecord);
                    master.save(eMasterFile);
                }

                File dataFile = new File(this.path + "F" + fileNumber + ".DAT");
                MetaStockData data = new MetaStockData();
                data.getRecords().add(new MetaStockDataRecord(historyData.getDate(), historyData.getOpen(),
                        historyData.getHigh(), historyData.getLow(), historyData.getClose(), historyData.getVolume(), 0));
                data.save(dataFile);
            } else {
                File xMasterFile = new File(this.path + "XMASTER");
                if (xMasterFile.exists()) {
                    MetaStockXMaster master = new MetaStockXMaster(xMasterFile);
                    MetaStockXMasterRecord masterRecord = new MetaStockXMasterRecord(symbol, symbol, period, fileNumber,
                            new Date(), historyData.getDate(), new Date(), new Date());
                    master.getRecords().add(masterRecord);
                    master.save(xMasterFile);
                } else {
                    MetaStockXMaster master = new MetaStockXMaster();
                    MetaStockXMasterRecord masterRecord = new MetaStockXMasterRecord(symbol, symbol, period, fileNumber,
                            new Date(), historyData.getDate(), new Date(), new Date());
                    master.getRecords().add(masterRecord);
                    master.save(xMasterFile);
                }

                File dataFile = new File(this.path + "F" + fileNumber + ".MWD");
                MetaStockData data = new MetaStockData();
                data.getRecords().add(new MetaStockDataRecord(historyData.getDate(), historyData.getOpen(),
                        historyData.getHigh(), historyData.getLow(), historyData.getClose(), historyData.getVolume(), 0));
                data.save(dataFile);
            }
        }
    }
}
