package com.minhdtb.lib;

import com.google.common.io.LittleEndianDataOutputStream;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class MetaStock<T> {
    private List<T> records = new ArrayList<>();

    final int BUFFER_SIZE = 1024;

    public void append(T record) {
        records.add(record);
    }

    public void remove(int index) {
        records.remove(index);
    }

    public List<T> getRecords() {
        return records;
    }

    abstract void save(LittleEndianDataOutputStream os) throws IOException;
}
