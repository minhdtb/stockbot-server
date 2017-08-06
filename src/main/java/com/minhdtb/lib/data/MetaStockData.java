package com.minhdtb.lib.data;

import com.google.common.io.LittleEndianDataInputStream;
import com.google.common.io.LittleEndianDataOutputStream;
import com.minhdtb.lib.base.MetaStock;

import java.io.*;

public final class MetaStockData extends MetaStock<MetaStockDataRecord> {

    private LittleEndianDataInputStream inputStream;
    private File file;

    public MetaStockData(File file) {
        try {
            this.file = file;
            if (file.exists()) {
                this.inputStream = new LittleEndianDataInputStream(new FileInputStream(file));
                load();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void load() throws IOException {
        getRecords().clear();

        header = new MetaStockDataHeader(inputStream);
        for (int i = 0; i < header.count(); i++) {
            MetaStockDataRecord data = new MetaStockDataRecord(inputStream);
            getRecords().add(data);
        }
    }

    @Override
    public void addRecord(MetaStockDataRecord record) {
        try {
            getRecords().add(record);
            RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
            header = new MetaStockDataHeader((short) getRecords().size(), (short) (getRecords().size() + 1));
            randomAccessFile.seek(0);
            randomAccessFile.write(header.toByteArray());
            randomAccessFile.seek(randomAccessFile.length());
            randomAccessFile.write(record.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void save() throws IOException {
        LittleEndianDataOutputStream outputStream = new LittleEndianDataOutputStream(new FileOutputStream(file));
        header = new MetaStockDataHeader((short) getRecords().size(), (short) (getRecords().size() + 1));
        header.write(outputStream);
        for (MetaStockDataRecord record : getRecords()) {
            record.write(outputStream);
        }
    }
}