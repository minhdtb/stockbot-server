package com.minhdtb.lib.master;

import com.google.common.io.LittleEndianDataInputStream;
import com.google.common.io.LittleEndianDataOutputStream;
import com.minhdtb.lib.base.MetaStock;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.IOException;

@EqualsAndHashCode(callSuper = true)
@Data
public final class MetaStockXMaster extends MetaStock<MetaStockXMasterRecord> {

    private MetaStockXMasterHeader header;

    public MetaStockXMaster() {

    }

    public MetaStockXMaster(LittleEndianDataInputStream is) {
        try {
            header = new MetaStockXMasterHeader(is);
            for (int i = 0; i < header.count() - 1; i++) {
                MetaStockXMasterRecord data = new MetaStockXMasterRecord(is);
                getRecords().add(data);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void save(LittleEndianDataOutputStream os) throws IOException {

    }
}
