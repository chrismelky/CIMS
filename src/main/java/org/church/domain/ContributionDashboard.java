package org.church.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;

@Entity
public class ContributionDashboard {

    @Id
    private String name;
    private BigDecimal promise;
    private BigDecimal collection;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPromise() {
        return promise;
    }

    public void setPromise(BigDecimal promise) {
        this.promise = promise;
    }

    public BigDecimal getCollection() {
        return collection;
    }

    public void setCollection(BigDecimal collection) {
        this.collection = collection;
    }
}
