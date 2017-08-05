package com.minhdtb.lib;

import com.minhdtb.lib.data.MetaStockData;
import com.minhdtb.lib.data.MetaStockDataRecord;
import com.minhdtb.lib.master.MetaStockMaster;
import com.minhdtb.lib.master.MetaStockMasterRecord;
import com.minhdtb.lib.master.MetaStockXMaster;
import com.minhdtb.lib.master.MetaStockXMasterRecord;

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

        } else {
            Optional<MetaStockHistory> max = historyList.stream().max(Comparator.comparingInt(MetaStockHistory::getFileNumber));
            int fileNumber = max.map(metaStockHistory -> metaStockHistory.getFileNumber() + 1).orElse(1);

            if (fileNumber <= 255) {

            } else {

            }
        }
    }
}
