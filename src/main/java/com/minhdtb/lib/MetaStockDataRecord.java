package com.minhdtb.lib;

import com.google.common.io.LittleEndianDataInputStream;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
public class MetaStockDataRecord extends MetaStockElement {

    private Date date;
    private float open;
    private float high;
    private float low;
    private float close;
    private float volume;
    private float openInterest;

    public MetaStockDataRecord(Date date, float open, float high, float low, float close, float volume, float openInterest) {
        this.date = date;
        this.open = open;
        this.high = high;
        this.low = low;
        this.close = close;
        this.volume = volume;
        this.openInterest = openInterest;
    }

    MetaStockDataRecord(LittleEndianDataInputStream is) throws IOException {
        date = DateFromSingle(MBFToFloat(is.readInt()));
        open = MBFToFloat(is.readInt());
        high = MBFToFloat(is.readInt());
        low = MBFToFloat(is.readInt());
        close = MBFToFloat(is.readInt());
        volume = MBFToFloat(is.readInt());
        openInterest = MBFToFloat(is.readInt());
    }

    @Override
    int encode(byte[] buffer, int i) {
        return 0;
    }

    @Override
    public String toString() {
        DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String sdt = df.format(date);
        return String.format("MetaStockDataRecord (date = %s, open = %f, high = %f, low = %f, close = %f, volume = %f, " +
                "open interest = %f)", sdt, open, high, low, close, volume, openInterest);
    }
}
