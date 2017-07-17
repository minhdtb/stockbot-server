package com.minhdtb.lib;

import com.google.common.io.LittleEndianDataInputStream;
import lombok.Data;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Data
public final class MetaStockEMaster {

    private MetaStockEMasterHeader header;
    private List<MetaStockEMasterRecord> records = new ArrayList<>();

    public MetaStockEMaster() {

    }

    public MetaStockEMaster(LittleEndianDataInputStream is) {
        try {
            header = new MetaStockEMasterHeader(is);
            for (int i = 0; i < header.getTotalFiles(); i++) {
                MetaStockEMasterRecord data = new MetaStockEMasterRecord(is);
                records.add(data);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
