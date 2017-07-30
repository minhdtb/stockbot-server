package com.minhdtb.lib;

import com.minhdtb.lib.data.MetaStockData;
import com.minhdtb.lib.data.MetaStockDataRecord;
import com.minhdtb.lib.master.MetaStockMaster;
import com.minhdtb.lib.master.MetaStockMasterRecord;
import com.minhdtb.lib.master.MetaStockXMaster;
import com.minhdtb.lib.master.MetaStockXMasterRecord;
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
    private String name;
    private Date startDate;
    private Date endDate;
    private int fileNumber;
    private File file;
}

@ToString
@AllArgsConstructor
@Data
class MetaStockHistoryData {
    private Date date;
    private float open;
    private float high;
    private float low;
    private float close;
    private float volume;
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
        Optional<MetaStockHistory> found = historyList.stream().filter(data -> Objects.equals(data.getName(), name)).findFirst();
        if (found.isPresent()) {
            return openData(found.get().getFile());
        }

        return new ArrayList<>();
    }

    public void addHistory(String name, MetaStockHistoryData historyData) {
        Optional<MetaStockHistory> found = historyList.stream().filter(data -> Objects.equals(data.getName(), name)).findFirst();
        if (found.isPresent()) {
            MetaStockHistory history = found.get();
            MetaStockData data = new MetaStockData(history.getFile());
            data.getRecords().add(new MetaStockDataRecord(historyData.getDate(), historyData.getOpen(), historyData.getHigh(),
                    historyData.getLow(), historyData.getClose(), historyData.getVolume(), 0));
            try {
                data.save(history.getFile());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Optional<MetaStockHistory> max = historyList.stream().max(Comparator.comparingInt(MetaStockHistory::getFileNumber));
            if (max.isPresent()) {
                int maxFileNumber = max.get().getFileNumber();
                if (maxFileNumber > 255) {

                } else {

                }
            }
        }
    }
}
