/*
 * Copyright 2011 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.util.array;

/**
 * An ordered collection of {@code double}s.
 *
 * @author Gabriele Carcassi
 */
public abstract class ListDouble implements ListNumber, CollectionDouble {

    @Override
    public IteratorDouble iterator() {
        return new IteratorDouble() {
            
            private int index;

            @Override
            public boolean hasNext() {
                return index < size();
            }

            @Override
            public double nextDouble() {
                return getDouble(index++);
            }
        };
    }

    @Override
    public float getFloat(int index) {
        return (float) getDouble(index);
    }

    @Override
    public long getLong(int index) {
        return (long) getDouble(index);
    }

    @Override
    public int getInt(int index) {
        return (int) getDouble(index);
    }

    @Override
    public short getShort(int index) {
        return (short) getDouble(index);
    }

    @Override
    public byte getByte(int index) {
        return (byte) getDouble(index);
    }

    @Override
    public void setDouble(int index, double value) {
        throw new UnsupportedOperationException("Read only list.");
    }

    @Override
    public void setFloat(int index, float value) {
        setDouble(index, (double) value);
    }

    @Override
    public void setLong(int index, long value) {
        setDouble(index, (double) value);
    }

    @Override
    public void setInt(int index, int value) {
        setDouble(index, (double) value);
    }

    @Override
    public void setShort(int index, short value) {
        setDouble(index, (double) value);
    }

    @Override
    public void setByte(int index, byte value) {
        setDouble(index, (double) value);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        
        if (obj instanceof ListNumber) {
            ListNumber other = (ListNumber) obj;

            if (size() != other.size())
                return false;

            for (int i = 0; i < size(); i++) {
                if (getDouble(i) != other.getDouble(i))
                    return false;
            }

            return true;
        }
        
        return false;
    }

    @Override
    public int hashCode() {
        int result = 1;
        for (int i = 0; i < size(); i++) {
            // The preferred solution would be to use
            // long bits = Double.doubleToLongBits(getDouble(i));
            // result = 31 * result + (int)(bits ^ (bits >>> 32));
            // which is the same logic than Arrays.hashCode(double[])
            // The problem: it's not going to be the same value
            // for equals arrays of integer types.
            
            // We use the long representation instead
            long element = getLong(i);
            int elementHash = (int)(element ^ (element >>> 32));
            result = 31 * result + elementHash;
        }
        return result;
    }

}
