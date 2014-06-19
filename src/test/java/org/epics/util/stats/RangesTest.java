/**
 * Copyright (C) 2012-14 epics-util developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.epics.util.stats;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;
import static org.epics.util.stats.Ranges.*;

/**
 *
 * @author carcassi
 */
public class RangesTest {
    
    public RangesTest() {
    }
    
    @Test
    public void range1() throws Exception {
        Range range = Ranges.range(0.0, 10.0);
        assertThat(range.getMinimum(), equalTo((Number) 0.0));
        assertThat(range.getMaximum(), equalTo((Number) 10.0));
    }
    
    public void range2() throws Exception {
        Range range = Ranges.range(0.0, 0.0);
        assertThat(range.getMinimum(), equalTo((Number) 0.0));
        assertThat(range.getMaximum(), equalTo((Number) 0.0));
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void range3() throws Exception {
        Range range = Ranges.range(10.0, 0.0);
    }
    
    public void absRange1() {
        Range range = absRange(range(1, 3));
        assertThat(range.getMinimum(), equalTo((Number) 1.0));
        assertThat(range.getMaximum(), equalTo((Number) 3.0));
    }
    
    public void absRange2() {
        Range range = absRange(range(0, 3));
        assertThat(range.getMinimum(), equalTo((Number) 0.0));
        assertThat(range.getMaximum(), equalTo((Number) 3.0));
    }
    
    public void absRange3() {
        Range range = absRange(range(-2, 3));
        assertThat(range.getMinimum(), equalTo((Number) 0.0));
        assertThat(range.getMaximum(), equalTo((Number) 3.0));
    }
    
    public void absRange4() {
        Range range = absRange(range(-4, 2));
        assertThat(range.getMinimum(), equalTo((Number) 0.0));
        assertThat(range.getMaximum(), equalTo((Number) 4.0));
    }
    
    public void absRange5() {
        Range range = absRange(range(-4, -2));
        assertThat(range.getMinimum(), equalTo((Number) 2.0));
        assertThat(range.getMaximum(), equalTo((Number) 4.0));
    }
    
    @Test
    public void subrange1() {
        assertThat(Ranges.contains(Ranges.range(0.0, 1.0), Ranges.range(0.5, 0.75)), equalTo(true));
        assertThat(Ranges.contains(Ranges.range(0.0, 1.0), Ranges.range(0.5, 1.0)), equalTo(true));
        assertThat(Ranges.contains(Ranges.range(0.0, 1.0), Ranges.range(0.0, 0.75)), equalTo(true));
        assertThat(Ranges.contains(Ranges.range(0.0, 1.0), Ranges.range(-1.0, 0.75)), equalTo(false));
        assertThat(Ranges.contains(Ranges.range(0.0, 1.0), Ranges.range(0.0, 1.75)), equalTo(false));
    }
    
    @Test
    public void sum1() {
        Range range1 = Ranges.range(0.0, 5.0);
        Range range2 = Ranges.range(1.0, 2.0);
        assertThat(Ranges.sum(range1, range2), sameInstance(range1));
        assertThat(Ranges.sum(range2, range1), sameInstance(range1));
    }
    
    @Test
    public void sum2() {
        Range range1 = Ranges.range(0.0, 5.0);
        Range range2 = Ranges.range(1.0, 6.0);
        Range range = Ranges.sum(range1, range2);
        assertThat(range.getMinimum(), equalTo((Number) 0.0));
        assertThat(range.getMaximum(), equalTo((Number) 6.0));
        range = Ranges.sum(range2, range1);
        assertThat(range.getMinimum(), equalTo((Number) 0.0));
        assertThat(range.getMaximum(), equalTo((Number) 6.0));
    }
    
    @Test
    public void sum3() {
        Range range1 = Ranges.range(0.0, 3.0);
        Range range2 = Ranges.range(4.0, 6.0);
        Range range = Ranges.sum(range1, range2);
        assertThat(range.getMinimum(), equalTo((Number) 0.0));
        assertThat(range.getMaximum(), equalTo((Number) 6.0));
        range = Ranges.sum(range2, range1);
        assertThat(range.getMinimum(), equalTo((Number) 0.0));
        assertThat(range.getMaximum(), equalTo((Number) 6.0));
    }
    
    @Test
    public void sum4() {
        Range range1 = Ranges.range(0.0, 3.0);
        Range range2 = Ranges.range(0.0, 6.0);
        Range range = Ranges.sum(range1, range2);
        assertThat(range.getMinimum(), equalTo((Number) 0.0));
        assertThat(range.getMaximum(), equalTo((Number) 6.0));
        range = Ranges.sum(range2, range1);
        assertThat(range.getMinimum(), equalTo((Number) 0.0));
        assertThat(range.getMaximum(), equalTo((Number) 6.0));
    }
    
    @Test
    public void sum5() {
        Range range1 = Ranges.range(0.0, 6.0);
        Range range2 = Ranges.range(3.0, 6.0);
        Range range = Ranges.sum(range1, range2);
        assertThat(range.getMinimum(), equalTo((Number) 0.0));
        assertThat(range.getMaximum(), equalTo((Number) 6.0));
        range = Ranges.sum(range2, range1);
        assertThat(range.getMinimum(), equalTo((Number) 0.0));
        assertThat(range.getMaximum(), equalTo((Number) 6.0));
    }
    
    @Test
    public void overlap1() {
        Range range1 = Ranges.range(0.0, 6.0);
        Range range2 = Ranges.range(3.0, 6.0);
        assertThat(Ranges.overlap(range1, range2), equalTo(0.5));
    }
    
    @Test
    public void overlap2() {
        Range range1 = Ranges.range(0.0, 8.0);
        Range range2 = Ranges.range(2.0, 4.0);
        assertThat(Ranges.overlap(range1, range2), equalTo(0.25));
    }
    
    @Test
    public void overlap3() {
        Range range1 = Ranges.range(0.0, 8.0);
        Range range2 = Ranges.range(2.0, 4.0);
        assertThat(Ranges.overlap(range1, range2), equalTo(0.25));
    }
    
    @Test
    public void overlap4() {
        Range range1 = Ranges.range(0.0, 8.0);
        Range range2 = Ranges.range(0.0, 4.0);
        assertThat(Ranges.overlap(range1, range2), equalTo(0.5));
    }
    
    @Test
    public void overlap5() {
        Range range1 = Ranges.range(0.0, 8.0);
        Range range2 = Ranges.range(-1.0, 4.0);
        assertThat(Ranges.overlap(range1, range2), equalTo(0.5));
    }
    
    @Test
    public void overlap6() {
        Range range1 = Ranges.range(0.0, 8.0);
        Range range2 = Ranges.range(-1.0, 14.0);
        assertThat(Ranges.overlap(range1, range2), equalTo(1.0));
    }
    
    @Test
    public void overlap7() {
        Range range1 = Ranges.range(0.0, 8.0);
        Range range2 = Ranges.range(2.0, 14.0);
        assertThat(Ranges.overlap(range1, range2), equalTo(0.75));
    }
    
    @Test
    public void isValid1() {
        Range range1 = Ranges.range(0.0, 8.0);
        assertThat(Ranges.isValid(range1), equalTo(true));
    }
    
    @Test
    public void isValid2() {
        Range range1 = Ranges.range(5.0, 5.0);
        assertThat(Ranges.isValid(range1), equalTo(false));
    }
    
    @Test
    public void isValid3() {
        Range range1 = Ranges.range(Double.NaN, 8.0);
        assertThat(Ranges.isValid(range1), equalTo(false));
    }
    
    @Test
    public void isValid4() {
        Range range1 = Ranges.range(Double.NEGATIVE_INFINITY, 8.0);
        assertThat(Ranges.isValid(range1), equalTo(false));
    }
    
    @Test
    public void isValid5() {
        Range range1 = Ranges.range(0.0, Double.NaN);
        assertThat(Ranges.isValid(range1), equalTo(false));
    }
    
    @Test
    public void isValid6() {
        Range range1 = Ranges.range(0.0, Double.POSITIVE_INFINITY);
        assertThat(Ranges.isValid(range1), equalTo(false));
    }
    
    @Test
    public void isValid7() {
        assertThat(Ranges.isValid(null), equalTo(false));
    }
}
