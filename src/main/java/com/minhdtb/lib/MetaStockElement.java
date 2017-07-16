package com.minhdtb.lib;

import com.google.common.io.LittleEndianDataInputStream;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

abstract class MetaStockElement {

    protected LittleEndianDataInputStream is;

    abstract int encode(byte[] buffer, int i);

    abstract void parse() throws IOException;

    private static long getUnsignedInt(int x) {
        return x & 0x00000000ffffffffL;
    }

    private Date DateFromSingle(float s) {
        int si = (int) s;
        int d = si % 100;
        si = si / 100;
        int m = si % 100;
        si = si / 100;
        int y = si + 1900;

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, y);
        cal.set(Calendar.MONTH, m - 1);
        cal.set(Calendar.DAY_OF_MONTH, d);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        return cal.getTime();
    }

    private float MBFToFloat(int value) {
        long tempValue = getUnsignedInt(value);
        if (tempValue == 0)
            return 0.0f;
        tempValue = (((tempValue - 0x02000000) & 0xFF000000) >> 1) |
                ((tempValue & 0x00800000) << 8) |
                (tempValue & 0x007FFFFF);

        return Float.intBitsToFloat((int) tempValue);
    }

    private float FloatToMBF(float value) {
        return 0;
    }

    void Skip(int len) throws IOException {
        is.skip(len);
    }

    String readString(int len) throws IOException {
        byte[] buffer = new byte[len];
        is.read(buffer);
        return new String(buffer).split("\0")[0];
    }

    Date readDate() throws IOException {
        return DateFromSingle(is.readFloat());
    }

    Date readMBFDate() throws IOException {
        return DateFromSingle(MBFToFloat(is.readInt()));
    }

    int readUnsignedByte() throws IOException {
        return is.readUnsignedByte();
    }

    int readUnsignedShort() throws IOException {
        return is.readUnsignedShort();
    }

    float readMBFFloat() throws IOException {
        return MBFToFloat(is.readInt());
    }
}
