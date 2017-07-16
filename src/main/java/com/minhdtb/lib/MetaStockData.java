package com.minhdtb.lib;


import com.google.common.io.LittleEndianDataInputStream;
import lombok.Data;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Data
public final class MetaStockData {

    private MetaStockDataHeader header;
    private List<MetaStockDataRecord> records = new ArrayList<>();

    public MetaStockData(LittleEndianDataInputStream is) {
        try {
            header = new MetaStockDataHeader(is);
            for (int i = 1; i < header.getLastRecord(); i++) {
                MetaStockDataRecord data = new MetaStockDataRecord(is);
                records.add(data);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void save() {

    }
}
