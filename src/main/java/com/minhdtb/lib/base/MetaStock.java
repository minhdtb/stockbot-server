package com.minhdtb.lib.base;

import com.google.common.io.LittleEndianDataOutputStream;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class MetaStock<T extends MetaStockElement> {
    private List<T> records = new ArrayList<>();

    protected abstract void save(File file) throws IOException;

    protected void write(MetaStockElement header, LittleEndianDataOutputStream os) throws IOException {
        header.write(os);
        for (T record : getRecords()) {
            record.write(os);
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
