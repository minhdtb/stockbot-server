package com.minhdtb.lib.base;

import com.google.common.io.LittleEndianDataOutputStream;
import com.minhdtb.lib.data.MetaStockDataRecord;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class MetaStock<T extends MetaStockElement> {
    private List<T> records = new ArrayList<>();

    protected abstract void save(File file) throws IOException;

    protected void write(MetaStockElement header, LittleEndianDataOutputStream os) throws IOException {
        byte[] buffer;
        int len;

        int BUFFER_SIZE = 1024;
        
        buffer = new byte[BUFFER_SIZE];
        len = header.encode(buffer);
        os.write(buffer, 0, len);

        for (T record : getRecords()) {
            buffer = new byte[BUFFER_SIZE];
            len = record.encode(buffer);
            os.write(buffer, 0, len);
        }
    }

    public void append(T record) {
        records.add(record);
    }

    public void remove(int index) {
        records.remove(index);
    }

    public List<T> getRecords() {
        return records;
    }

}
