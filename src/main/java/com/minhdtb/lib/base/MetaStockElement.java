package com.minhdtb.lib.base;

import com.google.common.io.LittleEndianDataInputStream;
import com.google.common.io.LittleEndianDataOutputStream;
import com.minhdtb.lib.annotations.DataField;
import com.minhdtb.lib.annotations.DataType;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.security.InvalidParameterException;
import java.util.Calendar;
import java.util.Date;

public abstract class MetaStockElement {

    private LittleEndianDataInputStream is;

    protected MetaStockElement() {

    }

    void write(LittleEndianDataOutputStream os) {
        Class<? extends MetaStockElement> clazz = this.getClass();

        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(DataField.class)) {
                DataField dataField = field.getAnnotation(DataField.class);
                try {
                    field.setAccessible(true);

                    if (field.getName().toLowerCase().contains("spare")) {
                        os.write(new byte[dataField.length()]);
                    } else if (field.getType() == byte[].class) {
                        byte[] value = (byte[]) field.get(this);
                        value = value != null ? value : new byte[dataField.length()];
                        os.write(value);
                    } else if (field.getType() == int.class && dataField.length() == 2) {
                        int value = (int) field.get(this);
                        os.write(getShortArray((short) value));
                    } else if (field.getType() == int.class && dataField.length() == 1) {
                        int value = (int) field.get(this);
                        os.write(getByteArray((byte) value));
                    } else if (field.getType() == float.class && dataField.length() == 4) {
                        os.write(getFloatArray(FloatToMBF((float) field.get(this))));
                    } else if (field.getType() == String.class) {
                        os.write(getStringArray((String) field.get(this), dataField.length()));
                    } else if (field.getType() == Date.class) {
                        if (dataField.type() == DataType.MBF) {
                            os.write(getFloatArray(FloatToMBF(DateToInt((Date) field.get(this), true))));
                        } else if (dataField.type() == DataType.FLOAT) {
                            os.write(getFloatArray(DateToInt((Date) field.get(this), true)));
                        } else {
                            os.write(getIntArray(DateToInt((Date) field.get(this), false)));
                        }
                    }
                } catch (IOException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void read() {
        Class<? extends MetaStockElement> clazz = this.getClass();

        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(DataField.class)) {
                DataField dataField = field.getAnnotation(DataField.class);
                try {
                    field.setAccessible(true);

                    if (field.getName().toLowerCase().contains("spare")) {
                        Skip(dataField.length());
                    } else if (field.getType() == byte[].class) {
                        field.set(this, readByteArray(dataField.length()));
                    } else if (field.getType() == int.class && dataField.length() == 2) {
                        field.set(this, readUnsignedShort());
                    } else if (field.getType() == int.class && dataField.length() == 1) {
                        field.set(this, readUnsignedByte());
                    } else if (field.getType() == float.class && dataField.length() == 4) {
                        field.set(this, readMBFFloat());
                    } else if (field.getType() == String.class) {
                        field.set(this, readString(dataField.length()));
                    } else if (field.getType() == Date.class) {
                        if (dataField.type() == DataType.MBF) {
                            field.set(this, readMBFDate());
                        } else if (dataField.type() == DataType.FLOAT) {
                            field.set(this, readFloatDate());
                        } else {
                            field.set(this, readIntDate());
                        }
                    } else {
                        throw new InvalidParameterException("Invalid data field structure.");
                    }
                } catch (IllegalAccessException | IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    protected MetaStockElement(LittleEndianDataInputStream is) throws IOException {
        this.is = is;
        this.read();
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

    private Date IntToDate(int date, boolean flag) {
        int si = date;
        int d = si % 100;
        si = si / 100;
        int m = si % 100;
        si = si / 100;
        int y = si + (flag ? 1900 : 0);

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

    private void Skip(int len) throws IOException {
        is.skip(len);
    }

    private byte[] readByteArray(int len) throws IOException {
        byte[] buffer = new byte[len];
        is.read(buffer);
        return buffer;
    }

    private String readString(int len) throws IOException {
        byte[] buffer = new byte[len];
        is.read(buffer);
        String[] str = new String(buffer).split("\0");
        return str.length > 0 ? str[0].trim() : null;
    }

    private Date readFloatDate() throws IOException {
        return IntToDate((int) is.readFloat(), true);
    }

    private Date readIntDate() throws IOException {
        return IntToDate(is.readInt(), false);
    }

    private Date readMBFDate() throws IOException {
        return IntToDate((int) readMBFFloat(), true);
    }

    private int readUnsignedByte() throws IOException {
        return is.readUnsignedByte();
    }

    private int readUnsignedShort() throws IOException {
        return is.readUnsignedShort();
    }

    private float readMBFFloat() throws IOException {
        return MBFToFloat(is.readFloat());
    }

    private int DateToInt(Date date, boolean flag) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        int year = cal.get(Calendar.YEAR) - (flag ? 1900 : 0);
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int ret = year;
        ret = ret * 100 + month;
        ret = ret * 100 + day;

        return ret;
    }

    private byte[] getShortArray(short value) {
        return ByteBuffer.allocate(2)
                .order(ByteOrder.LITTLE_ENDIAN)
                .putShort(value)
                .array();
    }

    private byte[] getByteArray(byte value) {
        return new byte[]{value};
    }

    private byte[] getFloatArray(float value) {
        return ByteBuffer.allocate(4)
                .order(ByteOrder.LITTLE_ENDIAN)
                .putFloat(value)
                .array();
    }

    private byte[] getIntArray(int value) {
        return ByteBuffer.allocate(4)
                .order(ByteOrder.LITTLE_ENDIAN)
                .putInt(value)
                .array();
    }

    private byte[] getStringArray(String value, int length) {
        byte[] buffer = new byte[length];
        if (value != null) {
            value = value.length() > length ? value.substring(0, length - 1) : value;
            System.arraycopy(value.getBytes(), 0, buffer, 0, value.length());
        }

        return buffer;
    }
}
