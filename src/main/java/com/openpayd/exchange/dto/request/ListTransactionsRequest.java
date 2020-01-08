package com.openpayd.exchange.dto.request;

import javax.validation.constraints.AssertTrue;
import java.time.LocalDate;

public class ListTransactionsRequest {
    private Long id;
    private LocalDate date;

    @AssertTrue(message = "You have to specify one of the id or date parameters, at leas")
    public boolean checkFields() {
        return id != null || date != null;
    }

    public Long getId() {
        return id;
    }

    public LocalDate getDate() {
        return date;
    }
}
