package com.minhdtb.lib;


import com.google.common.io.LittleEndianDataInputStream;
import lombok.Data;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Data
public final class MetaStockXMaster {

    private MetaStockXMasterHeader header;
    private List<MetaStockXMasterRecord> records = new ArrayList<>();

    public MetaStockXMaster() {

    }

    public MetaStockXMaster(LittleEndianDataInputStream is) {
        try {
            header = new MetaStockXMasterHeader(is);
            for (int i = 0; i < header.getLastFileNumber(); i++) {
                MetaStockXMasterRecord data = new MetaStockXMasterRecord(is);
                records.add(data);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
