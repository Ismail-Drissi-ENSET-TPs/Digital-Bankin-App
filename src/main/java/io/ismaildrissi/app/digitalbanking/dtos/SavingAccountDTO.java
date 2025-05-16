package io.ismaildrissi.app.digitalbanking.dtos;

import io.ismaildrissi.app.digitalbanking.enums.AccountStatus;
import lombok.Data;

import java.util.Date;

@Data
public class SavingAccountDTO extends BankAccountDTO {
    private String id;
    private double balance;
    private Date createdAt;
    private AccountStatus status;
    private CustomerDTO customer;
    private double interestRate;
}