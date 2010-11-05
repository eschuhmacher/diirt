/*
 * Copyright 2010 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */

package org.epics.pvmanager.replay;

import javax.xml.bind.annotation.XmlAttribute;
import org.epics.pvmanager.data.VDouble;

/**
 *
 * @author carcassi
 */
class XmlVDouble extends XmlVNumberMetaData implements VDouble {

    @XmlAttribute
    Double value;

    @Override
    public Double getValue() {
        return value;
    }

}
