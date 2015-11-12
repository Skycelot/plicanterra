package ru.skycelot.metamodel.persistence;

import java.io.Serializable;

/**
 *
 */
public class LinkGrantsId implements Serializable {

    private long link;
    private long status;

    public long getLink() {
        return link;
    }

    public void setLink(long link) {
        this.link = link;
    }

    public long getStatus() {
        return status;
    }

    public void setStatus(long status) {
        this.status = status;
    }

    @Override
    public int hashCode() {
        return Long.hashCode(link) + Long.hashCode(status);
    }

    @Override
    public boolean equals(Object obj) {
        boolean equality = false;
        if (obj instanceof LinkGrantsId) {
            LinkGrantsId other = (LinkGrantsId) obj;
            if (link == other.link && status == other.status) {
                equality = true;
            }
        }
        return equality;
    }
}
