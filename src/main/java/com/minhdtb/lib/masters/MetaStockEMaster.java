package com.minhdtb.lib.masters;

import com.google.common.io.LittleEndianDataInputStream;
import com.google.common.io.LittleEndianDataOutputStream;
import com.minhdtb.lib.base.MetaStockMasterBase;

import java.io.*;
import java.util.Objects;
import java.util.OptionalInt;
import java.util.stream.IntStream;

public final class MetaStockEMaster extends MetaStockMasterBase<MetaStockEMasterRecord> {

    private LittleEndianDataInputStream inputStream;
    private File file;

    public MetaStockEMaster(File file) {
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
    protected void load() throws IOException {
        getRecords().clear();

        header = new MetaStockEMasterHeader(inputStream);
        for (int i = 0; i < header.count(); i++) {
            MetaStockEMasterRecord data = new MetaStockEMasterRecord(inputStream);
            getRecords().add(data);
        }
    }

    @Override
    public void addRecord(MetaStockEMasterRecord record) {
        try {
            getRecords().add(record);
            RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
            header = new MetaStockEMasterHeader((short) getRecords().size(), (short) getRecords().size());
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
        header = new MetaStockEMasterHeader((short) getRecords().size(), (short) getRecords().size());
        header.write(outputStream);
        for (MetaStockEMasterRecord record : getRecords()) {
            record.write(outputStream);
        }
    }

    @Override
    public void updateRecord(MetaStockEMasterRecord record) {
        OptionalInt found = IntStream.range(0, getRecords().size() - 1).filter(value ->
                Objects.equals(getRecords().get(value).getSymbol(), record.getSymbol())).findFirst();
        if (found.isPresent()) {
            updateAt(file, found.getAsInt(), record);
        }
    }
}
