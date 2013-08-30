package se.skltp.mt.core.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import se.vgregion.dao.domain.patterns.entity.AbstractEntity;

/**
 * Authority entity, username and the authority 
 *
 */
@Entity
@Table(name = "AUTHORITIES")
public class Authority extends AbstractEntity<Long> {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private long id;

    @Column(name = "AUTHORITY", nullable = false)
    @Enumerated(EnumType.STRING)
    private AuthorityRole authority;
    
    @ManyToOne
    @JoinColumn(name = "USERNAME")
    private User user;

    public Authority() {
    }

    public Authority(User user, AuthorityRole authority) {
        this.user = user;
        this.authority = authority;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AuthorityRole getAuthority() {
        return authority;
    }

    public void setAuthority(AuthorityRole authority) {
        this.authority = authority;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
