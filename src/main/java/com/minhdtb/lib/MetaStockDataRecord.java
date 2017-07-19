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

        byte[] tmpBuffer = ByteBuffer.allocate(4)
                .order(ByteOrder.LITTLE_ENDIAN)
                .putFloat(FloatToMBF(DateToInt(date, true)))
                .array();
        System.arraycopy(tmpBuffer, 0, buffer, len, tmpBuffer.length);
        len += tmpBuffer.length;

        tmpBuffer = ByteBuffer.allocate(4)
                .order(ByteOrder.LITTLE_ENDIAN)
                .putFloat(FloatToMBF(open))
                .array();
        System.arraycopy(tmpBuffer, 0, buffer, len, tmpBuffer.length);
        len += tmpBuffer.length;

        tmpBuffer = ByteBuffer.allocate(4)
                .order(ByteOrder.LITTLE_ENDIAN)
                .putFloat(FloatToMBF(high))
                .array();
        System.arraycopy(tmpBuffer, 0, buffer, len, tmpBuffer.length);
        len += tmpBuffer.length;

        tmpBuffer = ByteBuffer.allocate(4)
                .order(ByteOrder.LITTLE_ENDIAN)
                .putFloat(FloatToMBF(low))
                .array();
        System.arraycopy(tmpBuffer, 0, buffer, len, tmpBuffer.length);
        len += tmpBuffer.length;

        tmpBuffer = ByteBuffer.allocate(4)
                .order(ByteOrder.LITTLE_ENDIAN)
                .putFloat(FloatToMBF(close))
                .array();
        System.arraycopy(tmpBuffer, 0, buffer, len, tmpBuffer.length);
        len += tmpBuffer.length;

        tmpBuffer = ByteBuffer.allocate(4)
                .order(ByteOrder.LITTLE_ENDIAN)
                .putFloat(FloatToMBF(volume))
                .array();
        System.arraycopy(tmpBuffer, 0, buffer, len, tmpBuffer.length);
        len += tmpBuffer.length;

        tmpBuffer = ByteBuffer.allocate(4)
                .order(ByteOrder.LITTLE_ENDIAN)
                .putFloat(FloatToMBF(openInterest))
                .array();
        System.arraycopy(tmpBuffer, 0, buffer, len, tmpBuffer.length);
        len += tmpBuffer.length;

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
