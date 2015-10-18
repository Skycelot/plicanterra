package ru.skycelot.plicanterra.security.core;

import java.util.Date;

/**
 *
 */
public class Challenge {

    public String nonce;
    public String login;
    public Date issued;

    @Override
    public int hashCode() {
        return (this.nonce != null ? this.nonce.hashCode() : super.hashCode());
    }

    @Override
    public boolean equals(Object obj) {
        boolean equality = false;
        if (obj instanceof Challenge) {
            final Challenge other = (Challenge) obj;
            if (this.nonce != null) {
                equality = this.nonce.equals(other.nonce);
            } else {
                equality = super.equals(obj);
            }
        }
        return equality;
    }

    @Override
    public String toString() {
        return "Challenge{" + "nonce=" + nonce + ", login=" + login + ", issued=" + issued + '}';
    }
}
