package com.minhdtb.lib;


import com.google.common.io.LittleEndianDataInputStream;
import com.google.common.io.LittleEndianDataOutputStream;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.IOException;

@EqualsAndHashCode(callSuper = true)
@Data
public final class MetaStockData extends MetaStock<MetaStockDataRecord> {

    private MetaStockDataHeader header;

    public MetaStockData() {

    }

    public MetaStockData(LittleEndianDataInputStream is) {
        try {
            header = new MetaStockDataHeader(is);
            for (int i = 0; i < header.getLastRecord() - 1; i++) {
                MetaStockDataRecord data = new MetaStockDataRecord(is);
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
            header = new MetaStockDataHeader((short) getRecords().size(), (short) getRecords().size());
        }

        if (os == null)
            return;

        buffer = new byte[255];
        len = header.encode(buffer);
        os.write(buffer, 0, len);

        for (MetaStockDataRecord record : getRecords()) {
            buffer = new byte[255];
            len = record.encode(buffer);
            os.write(buffer, 0, len);
        }
    }
}
