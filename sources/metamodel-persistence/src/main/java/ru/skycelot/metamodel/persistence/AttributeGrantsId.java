package ru.skycelot.metamodel.persistence;

import java.io.Serializable;

/**
 *
 */
public class AttributeGrantsId implements Serializable {

    private long attribute;
    private long status;

    public long getAttribute() {
        return attribute;
    }

    public void setAttribute(long attribute) {
        this.attribute = attribute;
    }

    public long getStatus() {
        return status;
    }

    public void setStatus(long status) {
        this.status = status;
    }

    @Override
    public int hashCode() {
        return Long.hashCode(attribute) + Long.hashCode(status);
    }

    @Override
    public boolean equals(Object obj) {
        boolean equality = false;
        if (obj instanceof AttributeGrantsId) {
            AttributeGrantsId other = (AttributeGrantsId) obj;
            if (attribute == other.attribute && status == other.status) {
                equality = true;
            }
        }
        return equality;
    }
}
