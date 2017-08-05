package com.minhdtb.lib.base;

import com.google.common.io.LittleEndianDataInputStream;
import com.google.common.io.LittleEndianDataOutputStream;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public abstract class MetaStock<T extends MetaStockElement> {

    private List<T> records = new ArrayList<>();

    protected abstract void save() throws IOException;

    protected abstract void load() throws IOException;

    protected LittleEndianDataOutputStream os;
    protected LittleEndianDataInputStream is;

    public MetaStock(File file) {
        try {
            if (file.exists()) {
                os = new LittleEndianDataOutputStream(new FileOutputStream(file, true));
            } else {
                os = new LittleEndianDataOutputStream(new FileOutputStream(file));
            }
            
            is = new LittleEndianDataInputStream(new FileInputStream(file));
            load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void write(MetaStockElement header, LittleEndianDataOutputStream os) throws IOException {
        header.write(os);
        for (T record : getRecords()) {
            record.write(os);
        }
    }

    public List<T> getRecords() {
        return records;
    }

}
