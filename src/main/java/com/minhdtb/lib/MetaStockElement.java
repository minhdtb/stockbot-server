package com.minhdtb.lib;

import java.util.Calendar;
import java.util.Date;

abstract class MetaStockElement {

    abstract int encode(byte[] buffer, int i);

    private static long getUnsignedInt(int x) {
        return x & 0x00000000ffffffffL;
    }

    Date DateFromSingle(float s) {
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

    float MBFToFloat(int value) {
        long tempValue = getUnsignedInt(value);
        if (tempValue == 0)
            return 0.0f;
        tempValue = (((tempValue - 0x02000000) & 0xFF000000) >> 1) |
                ((tempValue & 0x00800000) << 8) |
                (tempValue & 0x007FFFFF);

        return Float.intBitsToFloat((int) tempValue);
    }

    float FloatToMBF(float value) {
        return 0;
    }
}
