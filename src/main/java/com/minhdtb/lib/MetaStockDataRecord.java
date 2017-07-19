package com.minhdtb.lib;

import com.google.common.io.LittleEndianDataInputStream;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
public final class MetaStockDataRecord extends MetaStockElement {

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
        super(is);
    }

    @Override
    int encode(byte[] buffer) {
        int len = 0;

        byte[] tmpBuffer = getFloatArray(FloatToMBF(DateToInt(date, true)));
        len += copyBuffer(tmpBuffer, buffer, len);

        tmpBuffer = getFloatArray(FloatToMBF(open));
        len += copyBuffer(tmpBuffer, buffer, len);

        tmpBuffer = getFloatArray(FloatToMBF(high));
        len += copyBuffer(tmpBuffer, buffer, len);

        tmpBuffer = getFloatArray(FloatToMBF(low));
        len += copyBuffer(tmpBuffer, buffer, len);

        tmpBuffer = getFloatArray(FloatToMBF(close));
        len += copyBuffer(tmpBuffer, buffer, len);

        tmpBuffer = getFloatArray(FloatToMBF(volume));
        len += copyBuffer(tmpBuffer, buffer, len);

        tmpBuffer = getFloatArray(FloatToMBF(openInterest));
        len += copyBuffer(tmpBuffer, buffer, len);

        return len;
    }

    @Override
    void parse() throws IOException {
        date = readMBFDate();
        open = readMBFFloat();
        high = readMBFFloat();
        low = readMBFFloat();
        close = readMBFFloat();
        volume = readMBFFloat();
        openInterest = readMBFFloat();
    }
}
