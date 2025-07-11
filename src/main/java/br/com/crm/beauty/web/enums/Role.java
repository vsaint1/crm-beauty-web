package br.com.crm.beauty.web.enums;

public enum Role {
    
    USER("ROLE_USER"),
    ADMIN("ROLE_ADMIN");
    
    private String role;
    
    Role(String role) {
        this.role = role;
    }

    /**
     * @return String return the role
     */
    public String getRole() {
        return role;
    }

    /**
     * @param role the role to set
     */
    public void setRole(String role) {
        this.role = role;
    }

}
