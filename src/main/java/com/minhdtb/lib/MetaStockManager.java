package com.minhdtb.lib;

import com.minhdtb.lib.data.MetaStockData;
import com.minhdtb.lib.data.MetaStockDataRecord;
import com.minhdtb.lib.master.MetaStockMaster;
import com.minhdtb.lib.master.MetaStockMasterRecord;
import com.minhdtb.lib.master.MetaStockXMaster;
import com.minhdtb.lib.master.MetaStockXMasterRecord;
import lombok.AllArgsConstructor;
import lombok.ToString;

import java.io.File;
import java.util.*;

@ToString
@AllArgsConstructor
class MetaStockHistory {
    public String name;
    public Date startDate;
    public Date endDate;
    public File file;
}

@ToString
@AllArgsConstructor
class MetaStockHistoryData {
    public Date date;
    public float open;
    public float high;
    public float low;
    public float close;
    public float volume;
}

public class MetaStockManager {

    private List<MetaStockHistory> historyList = new ArrayList<>();

    private List<MetaStockHistoryData> openData(File dataFile) {
        List<MetaStockHistoryData> dataList = new ArrayList<>();
        MetaStockData data = new MetaStockData(dataFile.getAbsolutePath());
        for (MetaStockDataRecord dataRecord : data.getRecords()) {
            MetaStockHistoryData historyData = new MetaStockHistoryData(dataRecord.getDate(),
                    dataRecord.getOpen(), dataRecord.getHigh(), dataRecord.getLow(), dataRecord.getClose(), dataRecord.getVolume());
            dataList.add(historyData);
        }

        return dataList;
    }

    public void open(String path) {
        path = path.endsWith("\\") ? path : path + "\\";

        File masterFile = new File(path + "MASTER");
        if (masterFile.exists()) {
            MetaStockMaster master = new MetaStockMaster(masterFile.getAbsolutePath());
            for (MetaStockMasterRecord record : master.getRecords()) {
                File masterDataFile = new File(path + "F" + record.getFileNumber() + ".DAT");
                if (masterDataFile.exists()) {
                    MetaStockHistory history = new MetaStockHistory(record.getSymbol(), record.getStartDate(),
                            record.getEndDate(), masterDataFile);
                    historyList.add(history);
                }
            }
        }

        masterFile = new File(path + "XMASTER");
        if (masterFile.exists()) {
            MetaStockXMaster eMaster = new MetaStockXMaster(masterFile.getAbsolutePath());
            for (MetaStockXMasterRecord record : eMaster.getRecords()) {
                File masterDataFile = new File(path + "F" + record.getFileNumber() + ".MWD");
                if (masterDataFile.exists()) {
                    if (masterDataFile.exists()) {
                        MetaStockHistory history = new MetaStockHistory(record.getSymbol(), record.getStartDate(),
                                record.getEndDate(), masterDataFile);
                        historyList.add(history);
                    }
                }
            }
        }
    }

    public List<MetaStockHistoryData> getHistory(String name) {
        Optional<MetaStockHistory> found = historyList.stream().filter(data -> Objects.equals(data.name, name)).findFirst();
        if (found.isPresent()) {
            return openData(found.get().file);
        }

        return null;
    }
}
