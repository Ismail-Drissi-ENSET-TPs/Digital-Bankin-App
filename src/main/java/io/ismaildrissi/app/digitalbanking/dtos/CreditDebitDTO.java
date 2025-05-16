package io.ismaildrissi.app.digitalbanking.dtos;

import lombok.Data;

@Data
public class CreditDebitDTO {
    String accountId;
    int amount;
    String description;
}
