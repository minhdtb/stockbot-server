package com.minhdtb.lib.master;

import com.google.common.io.LittleEndianDataInputStream;
import com.google.common.io.LittleEndianDataOutputStream;
import com.minhdtb.lib.base.MetaStock;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.IOException;

@EqualsAndHashCode(callSuper = true)
@Data
public final class MetaStockEMaster extends MetaStock<MetaStockEMasterRecord> {

    private MetaStockEMasterHeader header;

    public MetaStockEMaster() {

    }

    public MetaStockEMaster(LittleEndianDataInputStream is) {
        try {
            header = new MetaStockEMasterHeader(is);
            for (int i = 0; i < header.getTotalFiles(); i++) {
                MetaStockEMasterRecord data = new MetaStockEMasterRecord(is);
                getRecords().add(data);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void save(LittleEndianDataOutputStream os) throws IOException {
        byte[] buffer;
        int len;

        if (header == null) {
            header = new MetaStockEMasterHeader((short) getRecords().size(), (short) getRecords().size());
        }

        if (os == null)
            return;

        buffer = new byte[BUFFER_SIZE];
        len = header.encode(buffer);
        os.write(buffer, 0, len);

        for (MetaStockEMasterRecord record : getRecords()) {
            buffer = new byte[BUFFER_SIZE];
            len = record.encode(buffer);
            os.write(buffer, 0, len);
        }
    }
}
