package com.minhdtb.lib.base;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class MetaStock<T extends MetaStockElement> {

    private List<T> records = new ArrayList<>();

    protected abstract void save(File file) throws IOException;

    protected abstract void load() throws IOException;

    protected abstract void addRecord(T record);

    public List<T> getRecords() {
        return records;
    }
}
