/*
 * Copyright 2010 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */

package gov.bnl.pvmanager.epics;

/**
 * Scalar double with alarm, timestamp, display and control information.
 * Auto-unboxing makes the extra method for the primitive type
 * unnecessary.
 * 
 * @author carcassi
 */
public interface EDouble extends Scalar<Double>, Alarm, Time, Display<Double> {
}
