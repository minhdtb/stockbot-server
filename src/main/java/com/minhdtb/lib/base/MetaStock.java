package com.minhdtb.lib.base;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class MetaStock<T extends MetaStockElement> {

    private List<T> records = new ArrayList<>();

    protected abstract void save() throws IOException;

    protected abstract void load() throws IOException;

    protected abstract void addRecord(T record);


    protected void write(MetaStockElement header) throws IOException {
        header.write();
        for (T record : getRecords()) {
            record.write();
        }
    }

    public List<T> getRecords() {
        return records;
    }

}
