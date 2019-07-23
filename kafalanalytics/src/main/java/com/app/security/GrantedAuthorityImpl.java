package com.app.security;

import java.io.Serializable;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.Assert;

public class GrantedAuthorityImpl implements GrantedAuthority, Serializable {
    //~ Instance fields ================================================================================================

    private static final long serialVersionUID = 1L;
    private String role;

    //~ Constructors ===================================================================================================

    public GrantedAuthorityImpl(String role) {
        Assert.hasText(role, "A granted authority textual representation is required");
        this.role = role;
    }

    //~ Methods ========================================================================================================

    public boolean equals(Object obj) {
        if (obj instanceof String) {
            return obj.equals(this.role);
        }

        if (obj instanceof GrantedAuthority) {
            GrantedAuthority attr = (GrantedAuthority) obj;

            return this.role.equals(attr.getAuthority());
        }

        return false;
    }

    public String getAuthority() {
        return this.role;
    }

    public int hashCode() {
        return this.role.hashCode();
    }

    public String toString() {
        return this.role;
    }

    public int compareTo(GrantedAuthority ga) {
        if (ga != null) {
            String rhsRole = ga.getAuthority();

            if (rhsRole == null) {
                return -1;
            }

            return role.compareTo(rhsRole);
        }
        return -1;
    }
}
