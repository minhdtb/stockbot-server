package com.minhdtb.lib;


import com.google.common.io.LittleEndianDataInputStream;
import com.google.common.io.LittleEndianDataOutputStream;
import lombok.Data;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Data
public final class MetaStockData {

    private LittleEndianDataOutputStream os;
    private MetaStockDataHeader header;
    private List<MetaStockDataRecord> records = new ArrayList<>();

    public MetaStockData() {

    }

    public MetaStockData(LittleEndianDataInputStream is) {
        try {
            header = new MetaStockDataHeader(is);
            for (int i = 0; i < header.getLastRecord(); i++) {
                MetaStockDataRecord data = new MetaStockDataRecord(is);
                records.add(data);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void save(LittleEndianDataOutputStream os) throws IOException {
        byte[] buffer;
        int len;

        if (header == null) {
            header = new MetaStockDataHeader((short) records.size(), (short) records.size());
        }

        if (os == null)
            return;

        buffer = new byte[255];
        len = header.encode(buffer);
        os.write(buffer, 0, len);

        for (MetaStockDataRecord record : records) {
            buffer = new byte[255];
            len = record.encode(buffer);
            os.write(buffer, 0, len);
        }
    }

    public void append(MetaStockDataRecord record) {
        records.add(record);
    }

    public void remove(int index) {
        records.remove(index);
    }
}
