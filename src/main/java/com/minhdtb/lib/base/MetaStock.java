package com.minhdtb.lib.base;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

public abstract class MetaStock<T extends MetaStockElement> {

    protected MetaStockHeader header;

    private List<T> records = new ArrayList<>();

    protected abstract void save() throws IOException;

    protected abstract void load() throws IOException;

    protected abstract void addRecord(T record);

    public List<T> getRecords() {
        return records;
    }

    public MetaStockHeader getHeader() {
        return header;
    }

    protected void updateAt(File file, int index, T record) {
        try {
            RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
            randomAccessFile.seek(header.getSize() + record.getSize() * index);
            randomAccessFile.write(record.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
