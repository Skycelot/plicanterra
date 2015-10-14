package ru.petrosoft.erratum.security.core;

import java.util.Date;

/**
 *
 */
public class Token {

    public String token;
    public String login;
    public Date issued;

    @Override
    public int hashCode() {
        return (this.token != null ? this.token.hashCode() : super.hashCode());
    }

    @Override
    public boolean equals(Object obj) {
        boolean equality = false;
        if (obj instanceof Token) {
            final Token other = (Token) obj;
            if (this.token != null) {
                equality = this.token.equals(other.token);
            } else {
                equality = super.equals(obj);
            }
        }
        return equality;
    }

    @Override
    public String toString() {
        return "Token{" + "token=" + token + ", login=" + login + ", issued=" + issued + '}';
    }
}
