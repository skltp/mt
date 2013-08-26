package se.inera.ifv.casebox.core.entity;

public enum AuthorityRole {
    
    ROLE_USER("ROLE_USER");

    private String nice;

    private AuthorityRole(String nice) {
        this.nice = nice;
    }

    @Override
    public String toString() {
        return nice;
    }
}
