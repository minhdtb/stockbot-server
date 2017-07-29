package com.minhdtb.lib.master;

import com.google.common.io.LittleEndianDataInputStream;
import com.google.common.io.LittleEndianDataOutputStream;
import com.minhdtb.lib.base.MetaStock;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

@EqualsAndHashCode(callSuper = true)
@Data
public final class MetaStockXMaster extends MetaStock<MetaStockXMasterRecord> {

    private MetaStockXMasterHeader header;

    public MetaStockXMaster() {

    }

    public MetaStockXMaster(String filename) {
        try {
            LittleEndianDataInputStream is = new LittleEndianDataInputStream(new FileInputStream(filename));
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
    public void save(String filename) throws IOException {
        if (header == null) {
            header = new MetaStockXMasterHeader((short) getRecords().size(), (short) getRecords().size(), (short) getRecords().size());
        }

        write(header, new LittleEndianDataOutputStream(new FileOutputStream(filename)));
    }
}
