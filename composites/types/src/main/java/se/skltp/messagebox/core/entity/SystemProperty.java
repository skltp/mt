package se.skltp.messagebox.core.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

import se.vgregion.dao.domain.patterns.entity.AbstractEntity;

/**
 * A system property.
 * <p/>
 * A simple name/value tuples for system values.
 * <p/>
 *
 * @author mats.olsson@callistaenterprise.se
 */
@Entity
public class SystemProperty extends AbstractEntity<String> {

    @Id
    private String name;
    private String value;

    public SystemProperty(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public SystemProperty() {
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String getId() {
        return name;
    }
}

