/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.vtype;

import java.util.Arrays;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import static org.epics.vtype.ValueFactory.*;
import static org.epics.vtype.ValueUtil.*;
import org.epics.util.text.NumberFormats;
import org.epics.util.array.ArrayDouble;
import org.epics.util.array.ArrayFloat;
import org.epics.util.array.ArrayInt;
import org.epics.util.array.ListNumber;
import org.epics.util.text.NumberFormats;

/**
 *
 * @author carcassi
 */
public class ValueUtilTest {

    public ValueUtilTest() {
    }

    @Test
    public void testTypeOf() {
        assertThat(ValueUtil.typeOf(newVString(null, alarmNone(), timeNow())),
                equalTo((Class) VString.class));
        assertThat(ValueUtil.typeOf(newVDouble(Double.NaN, alarmNone(), timeNow(), displayNone())),
                equalTo((Class) VDouble.class));
    }
    
    @Test
    public void displayEquals1() {
        assertThat(ValueUtil.displayEquals(displayNone(), displayNone()), equalTo(true));
        Display display1 = newDisplay(0.0, 1.0, 2.0, "", NumberFormats.toStringFormat(), 8.0, 9.0, 10.0, 0.0, 10.0);
        assertThat(ValueUtil.displayEquals(display1, displayNone()), equalTo(false));
        Display display2 = newDisplay(0.0, 1.0, 2.0, "", NumberFormats.toStringFormat(), 8.0, 9.0, 10.0, 0.0, 10.0);
        assertThat(ValueUtil.displayEquals(display1, display2), equalTo(true));
        display2 = newDisplay(0.1, 1.0, 2.0, "", NumberFormats.toStringFormat(), 8.0, 9.0, 10.0, 0.0, 10.0);
        assertThat(ValueUtil.displayEquals(display1, display2), equalTo(false));
        display2 = newDisplay(0.0, 1.1, 2.0, "", NumberFormats.toStringFormat(), 8.0, 9.0, 10.0, 0.0, 10.0);
        assertThat(ValueUtil.displayEquals(display1, display2), equalTo(false));
        display2 = newDisplay(0.0, 1.0, 2.1, "", NumberFormats.toStringFormat(), 8.0, 9.0, 10.0, 0.0, 10.0);
        assertThat(ValueUtil.displayEquals(display1, display2), equalTo(false));
        display2 = newDisplay(0.0, 1.0, 2.0, "a", NumberFormats.toStringFormat(), 8.0, 9.0, 10.0, 0.0, 10.0);
        assertThat(ValueUtil.displayEquals(display1, display2), equalTo(false));
        display2 = newDisplay(0.0, 1.0, 2.0, "", NumberFormats.format(2), 8.0, 9.0, 10.0, 0.0, 10.0);
        assertThat(ValueUtil.displayEquals(display1, display2), equalTo(false));
        display2 = newDisplay(0.0, 1.0, 2.0, "", NumberFormats.toStringFormat(), 8.1, 9.0, 10.0, 0.0, 10.0);
        assertThat(ValueUtil.displayEquals(display1, display2), equalTo(false));
        display2 = newDisplay(0.0, 1.0, 2.0, "", NumberFormats.toStringFormat(), 8.0, 9.1, 10.0, 0.0, 10.0);
        assertThat(ValueUtil.displayEquals(display1, display2), equalTo(false));
        display2 = newDisplay(0.0, 1.0, 2.0, "", NumberFormats.toStringFormat(), 8.0, 9.0, 10.1, 0.0, 10.0);
        assertThat(ValueUtil.displayEquals(display1, display2), equalTo(false));
        display2 = newDisplay(0.0, 1.0, 2.0, "", NumberFormats.toStringFormat(), 8.0, 9.0, 10.0, 0.1, 10.0);
        assertThat(ValueUtil.displayEquals(display1, display2), equalTo(false));
        display2 = newDisplay(0.0, 1.0, 2.0, "", NumberFormats.toStringFormat(), 8.0, 9.0, 10.0, 0.0, 10.1);
        assertThat(ValueUtil.displayEquals(display1, display2), equalTo(false));
    }
    
    @Test
    public void numericValueOf1() {
        assertThat(numericValueOf(newVDouble(1.0)), equalTo(1.0));
        assertThat(numericValueOf(newVInt(1, alarmNone(), timeNow(), displayNone())), equalTo(1.0));
        assertThat(numericValueOf(newVEnum(1, Arrays.asList("ONE", "TWO", "THREE"), alarmNone(), timeNow())), equalTo(1.0));
        assertThat(numericValueOf(newVDoubleArray(new ArrayDouble(1.0), alarmNone(), timeNow(), displayNone())), equalTo(1.0));
        assertThat(numericValueOf(newVFloatArray(new ArrayFloat(1.0f), alarmNone(), timeNow(), displayNone())), equalTo(1.0));
        assertThat(numericValueOf(newVIntArray(new ArrayInt(1), alarmNone(), timeNow(), displayNone())), equalTo(1.0));
        assertThat(numericValueOf(newVEnumArray(new ArrayInt(1,0,2), Arrays.asList("ONE", "TWO", "THREE"), alarmNone(), timeNow())), equalTo(1.0));
    }
    
    @Test
    public void displayHasValidDisplayLimits1() {
        assertThat(displayHasValidDisplayLimits(displayNone()), equalTo(false));
    }
    
    @Test
    public void displayHasValidDisplayLimits2() {
        Display display1 = newDisplay(0.0, 1.0, 2.0, "", NumberFormats.toStringFormat(), 8.0, 9.0, 10.0, 0.0, 10.0);
        assertThat(displayHasValidDisplayLimits(display1), equalTo(true));
    }
    
    @Test
    public void numericColumnOf1() {
        VTable data = ValueFactory.newVTable(Arrays.<Class<?>>asList(double.class, double.class),
                Arrays.asList("x", "y"), Arrays.<Object>asList(new ArrayDouble(1,2,3), new ArrayDouble(5,4,6)));
        assertThat(ValueUtil.numericColumnOf(data, null), equalTo(null));
        assertThat(ValueUtil.numericColumnOf(data, "x"), equalTo((ListNumber) new ArrayDouble(1,2,3)));
        assertThat(ValueUtil.numericColumnOf(data, "y"), equalTo((ListNumber) new ArrayDouble(5,4,6)));
        assertThat(ValueUtil.numericColumnOf(data, "z"), equalTo(null));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void numericColumnOf2() {
        VTable data = ValueFactory.newVTable(Arrays.<Class<?>>asList(double.class, String.class),
                Arrays.asList("x", "y"), Arrays.<Object>asList(new ArrayDouble(1,2,3), Arrays.asList("a", "b", "c")));
        assertThat(ValueUtil.numericColumnOf(data, null), equalTo(null));
        assertThat(ValueUtil.numericColumnOf(data, "x"), equalTo((ListNumber) new ArrayDouble(1,2,3)));
        ValueUtil.numericColumnOf(data, "y");
    }

}