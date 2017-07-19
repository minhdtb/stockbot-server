package com.minhdtb.lib;

import com.google.common.io.LittleEndianDataInputStream;
import com.google.common.io.LittleEndianDataOutputStream;
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

    public void save(LittleEndianDataOutputStream os) throws IOException {
        byte[] buffer;
        int len;

        if (header == null) {
            header = new MetaStockEMasterHeader((short) records.size(), (short) records.size());
        }

        if (os == null)
            return;

        buffer = new byte[255];
        len = header.encode(buffer);
        os.write(buffer, 0, len);

        for (MetaStockEMasterRecord record : records) {
            buffer = new byte[255];
            len = record.encode(buffer);
            os.write(buffer, 0, len);
        }
    }

    public void append(MetaStockEMasterRecord record) {
        records.add(record);
    }

    public void remove(int index) {
        records.remove(index);
    }
}
