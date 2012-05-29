/*
 * Copyright 2011 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.util.array;

/**
 * An ordered collection of {@code float}s.
 *
 * @author Gabriele Carcassi
 */
public abstract class ListFloat implements ListNumber, CollectionFloat {

    @Override
    public IteratorFloat iterator() {
        return new IteratorFloat() {
            
            private int index;

            @Override
            public boolean hasNext() {
                return index < size();
            }

            @Override
            public float nextFloat() {
                return getFloat(index++);
            }
        };
    }

    @Override
    public double getDouble(int index) {
        return (double) getFloat(index);
    }

    @Override
    public long getLong(int index) {
        return (long) getFloat(index);
    }

    @Override
    public int getInt(int index) {
        return (int) getFloat(index);
    }

    @Override
    public short getShort(int index) {
        return (short) getFloat(index);
    }

    @Override
    public byte getByte(int index) {
        return (byte) getFloat(index);
    }
    
    @Override
    public void setDouble(int index, double value) {
        setFloat(index, (float) value);
    }

    @Override
    public void setFloat(int index, float value) {
        throw new UnsupportedOperationException("Read only list.");
    }

    @Override
    public void setLong(int index, long value) {
        setFloat(index, (float) value);
    }

    @Override
    public void setInt(int index, int value) {
        setFloat(index, (float) value);
    }

    @Override
    public void setShort(int index, short value) {
        setFloat(index, (float) value);
    }

    @Override
    public void setByte(int index, byte value) {
        setFloat(index, (float) value);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        
        // Should compare to the higher precision if needed
        if (obj instanceof ListDouble) {
            return obj.equals(this);
        }
        
        if (obj instanceof ListNumber) {
            ListNumber other = (ListNumber) obj;

            if (size() != other.size())
                return false;

            for (int i = 0; i < size(); i++) {
                if (getFloat(i) != other.getFloat(i))
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
            // result = 31 * result + Float.floatToIntBits(getFloat(i));
            // which is the same logic than Arrays.hashCode(float[])
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
