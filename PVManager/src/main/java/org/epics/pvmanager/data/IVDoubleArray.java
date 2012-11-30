/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager.data;

import java.util.List;
import org.epics.util.array.ListDouble;

/**
 *
 * @author carcassi
 */
class IVDoubleArray extends IVNumeric implements VDoubleArray {

    private final double[] array;
    private final ListDouble data;
    private final List<Integer> sizes;

    public IVDoubleArray(ListDouble data, List<Integer> sizes,
            Alarm alarm, Time time, Display display) {
        super(alarm.getAlarmSeverity(), alarm.getAlarmName(), time.getTimestamp(), time.getTimeUserTag(), time.isTimeValid(),
                display.getLowerDisplayLimit(), display.getLowerCtrlLimit(), display.getLowerAlarmLimit(), display.getLowerWarningLimit(),
                display.getUnits(), display.getFormat(),
                display.getUpperWarningLimit(), display.getUpperAlarmLimit(), display.getUpperCtrlLimit(), display.getUpperDisplayLimit());
        this.array = null;
        this.sizes = sizes;
        this.data = data;
    }

    @Override
    public double[] getArray() {
        if (array == null) {
            double[] temp = new double[data.size()];
            for (int i = 0; i < data.size(); i++) {
                temp[i] = data.getDouble(i);
            }
            return temp;
        }
        
        return array;
    }

    @Override
    public List<Integer> getSizes() {
        return sizes;
    }

    @Override
    public ListDouble getData() {
        return data;
    }

}
