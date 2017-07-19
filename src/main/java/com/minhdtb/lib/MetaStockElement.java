package com.minhdtb.lib;

import com.google.common.io.LittleEndianDataInputStream;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Calendar;
import java.util.Date;

abstract class MetaStockElement {

    abstract int encode(byte[] buffer, int i);

    abstract void parse() throws IOException;

    private LittleEndianDataInputStream is;

    MetaStockElement() {

    }

    MetaStockElement(LittleEndianDataInputStream is) throws IOException {
        this.is = is;
        this.parse();
    }

    private Date getDate(int y, int m, int d) {
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

    private Date getDate(int date) {
        int si = date;
        int d = si % 100;
        si = si / 100;
        int m = si % 100;
        si = si / 100;
        int y = si + 1900;

        return getDate(y, m, d);
    }

    private float MBFToFloat(float value) {
        byte[] msbin = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putFloat(value).array();
        byte[] ieee = new byte[4];
        int sign = msbin[2] & 0x80;
        int ieeeExp;

        if (msbin[3] == 0) return 0;

        ieee[3] |= sign;
        ieeeExp = (msbin[3] & 0xFF) - 2;
        ieee[3] |= ieeeExp >> 1;

        ieee[2] |= ieeeExp << 7;
        ieee[2] |= msbin[2] & 0x7f;

        ieee[1] = msbin[1];
        ieee[0] = msbin[0];

        return ByteBuffer.wrap(ieee).order(ByteOrder.LITTLE_ENDIAN).getFloat();
    }

    private float FloatToMBF(float value) {
        byte[] ieee = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putFloat(value).array();
        byte[] msbin = new byte[4];
        int msbinExp = 0x00;
        int sign = ieee[3] & 0x80;

        msbinExp |= ieee[3] << 1;
        msbinExp |= (ieee[2] & 0xFF) >> 7;

        if (msbinExp == 0xfe)
            return 1;

        msbinExp += 2;

        msbin[3] = (byte) msbinExp;

        msbin[2] |= sign;
        msbin[2] |= ieee[2] & 0x7f;

        msbin[1] = ieee[1];
        msbin[0] = ieee[0];

        return ByteBuffer.wrap(msbin).order(ByteOrder.LITTLE_ENDIAN).getFloat();
    }

    void Skip(int len) throws IOException {
        is.skip(len);
    }

    String readString(int len) throws IOException {
        byte[] buffer = new byte[len];
        is.read(buffer);
        return new String(buffer).split("\0")[0];
    }

    Date readFloatDate() throws IOException {
        return getDate((int) is.readFloat());
    }

    Date readIntDate() throws IOException {
        return getDate(is.readInt());
    }

    Date readMBFDate() throws IOException {
        return getDate((int) readMBFFloat());
    }

    int readUnsignedByte() throws IOException {
        return is.readUnsignedByte();
    }

    int readUnsignedShort() throws IOException {
        return is.readUnsignedShort();
    }

    float readMBFFloat() throws IOException {
        return MBFToFloat(is.readFloat());
    }
}
