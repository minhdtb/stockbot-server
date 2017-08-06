package com.minhdtb.lib.base;

import com.minhdtb.lib.annotations.DataField;
import com.minhdtb.lib.annotations.DataType;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.nio.ByteOrder;
import java.security.InvalidParameterException;
import java.util.Calendar;
import java.util.Date;

import static java.nio.ByteBuffer.allocate;
import static java.nio.ByteBuffer.wrap;

public abstract class MetaStockElement {

    private InputStream inputStream;

    protected MetaStockElement() {

    }

    protected MetaStockElement(InputStream inputStream) throws IOException {
        this.inputStream = inputStream;
        this.read();
    }

    public void write(OutputStream outputStream) {
        writeBuffer(outputStream);
    }

    private void read() {
        readBuffer(inputStream);
    }

    public byte[] toByteArray() {
        ByteArrayOutputStream os = new ByteArrayOutputStream(getSize());
        writeBuffer(os);

        return os.toByteArray();
    }

    private int getSize() {
        int size = 0;

        Class<? extends MetaStockElement> clazz = this.getClass();

        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(DataField.class)) {
                DataField dataField = field.getAnnotation(DataField.class);
                size += dataField.length();
            }
        }

        return size;
    }

    private void writeBuffer(OutputStream os) {
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

    private void readBuffer(InputStream is) {
        Class<? extends MetaStockElement> clazz = this.getClass();

        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(DataField.class)) {
                DataField dataField = field.getAnnotation(DataField.class);
                try {
                    if (field.getName().toLowerCase().contains("spare")) {
                        is.skip(dataField.length());
                    } else {
                        field.setAccessible(true);
                        byte[] buffer = new byte[dataField.length()];
                        is.read(buffer);

                        if (field.getType() == byte[].class) {
                            field.set(this, buffer);
                        } else if (field.getType() == int.class && dataField.length() == 2) {
                            field.set(this, getShort(buffer));
                        } else if (field.getType() == int.class && dataField.length() == 1) {
                            field.set(this, getUnsignedByte(buffer));
                        } else if (field.getType() == float.class && dataField.length() == 4) {
                            field.set(this, MBFToFloat(getFloat(buffer)));
                        } else if (field.getType() == String.class) {
                            field.set(this, getString(buffer));
                        } else if (field.getType() == Date.class && dataField.length() == 4) {
                            if (dataField.type() == DataType.MBF) {
                                field.set(this, IntToDate((int) MBFToFloat(getFloat(buffer)), true));
                            } else if (dataField.type() == DataType.FLOAT) {
                                field.set(this, IntToDate((int) getFloat(buffer), true));
                            } else {
                                field.set(this, IntToDate(getInt(buffer), false));
                            }
                        } else {
                            throw new InvalidParameterException("Invalid data field structure.");
                        }
                    }
                } catch (IllegalAccessException | IOException e) {
                    e.printStackTrace();
                }
            }
        }
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
        byte[] msbin = allocate(4).order(ByteOrder.LITTLE_ENDIAN).putFloat(value).array();
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

        return wrap(ieee).order(ByteOrder.LITTLE_ENDIAN).getFloat();
    }

    private float FloatToMBF(float value) {
        byte[] ieee = allocate(4).order(ByteOrder.LITTLE_ENDIAN).putFloat(value).array();
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

        return wrap(msbin).order(ByteOrder.LITTLE_ENDIAN).getFloat();
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

    private short getShort(byte[] buffer) {
        return allocate(2).order(ByteOrder.LITTLE_ENDIAN).put(buffer).getShort(0);
    }

    private int getUnsignedByte(byte[] buffer) {
        return buffer[0] & 0xFF;
    }

    private float getFloat(byte[] buffer) {
        return allocate(4).order(ByteOrder.LITTLE_ENDIAN).put(buffer).getFloat(0);
    }

    private int getInt(byte[] buffer) {
        return allocate(4).order(ByteOrder.LITTLE_ENDIAN).put(buffer).getInt(0);
    }

    private String getString(byte[] buffer) {
        String[] str = new String(buffer).split("\0");
        return str.length > 0 ? str[0].trim() : null;
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
        return allocate(2)
                .order(ByteOrder.LITTLE_ENDIAN)
                .putShort(value)
                .array();
    }

    private byte[] getByteArray(byte value) {
        return new byte[]{value};
    }

    private byte[] getFloatArray(float value) {
        return allocate(4)
                .order(ByteOrder.LITTLE_ENDIAN)
                .putFloat(value)
                .array();
    }

    private byte[] getIntArray(int value) {
        return allocate(4)
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
