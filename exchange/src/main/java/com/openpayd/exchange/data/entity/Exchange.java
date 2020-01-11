package com.openpayd.exchange.data.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Currency;

@Entity
@Table(name = "exchange", indexes = {
        @Index(columnList = "exchange_date desc", name = "idx_exchange___exchange_date_desc")
})
public class Exchange {
    private long id;

    private ZonedDateTime exchangeDate;

    private Currency sourceCurrency;
    private Currency targetCurrency;

    private BigDecimal rate;
    private BigDecimal calculated;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "exchange_date")
    public ZonedDateTime getExchangeDate() {
        return exchangeDate;
    }

    public void setExchangeDate(ZonedDateTime exchangeDate) {
        this.exchangeDate = exchangeDate;
    }

    @Basic
    @Column(name = "source_currency")
    public Currency getSourceCurrency() {
        return sourceCurrency;
    }

    public void setSourceCurrency(Currency sourceCurrency) {
        this.sourceCurrency = sourceCurrency;
    }

    @Basic
    @Column(name = "target_currency")
    public Currency getTargetCurrency() {
        return targetCurrency;
    }

    public void setTargetCurrency(Currency targetCurrency) {
        this.targetCurrency = targetCurrency;
    }

    @Basic
    @Column(name = "rate")
    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    @Basic
    @Column(name = "calculated")
    public BigDecimal getCalculated() {
        return calculated;
    }

    public void setCalculated(BigDecimal calculated) {
        this.calculated = calculated;
    }
}
