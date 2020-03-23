package org.church.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
public class MemberContributionDashboard {

    @Id
    private String name;
    private String phone_number;
    private BigDecimal promise;
    private BigDecimal contribution;
    private String due_date;
    private Boolean over_due;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public BigDecimal getPromise() {
        return promise;
    }

    public void setPromise(BigDecimal promise) {
        this.promise = promise;
    }

    public BigDecimal getContribution() {
        return contribution;
    }

    public void setContribution(BigDecimal contribution) {
        this.contribution = contribution;
    }

    public String getDue_date() {
        return due_date;
    }

    public void setDue_date(String due_date) {
        this.due_date = due_date;
    }

    public Boolean getOver_due() {
        return over_due;
    }

    public void setOver_due(Boolean over_due) {
        this.over_due = over_due;
    }
}
