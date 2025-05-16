package io.ismaildrissi.app.digitalbanking.dtos;

import io.ismaildrissi.app.digitalbanking.entities.BankAccount;
import io.ismaildrissi.app.digitalbanking.enums.OperationType;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
public class AccountOperationDTO {
    private Date operationDate;
    private double amount;
    private OperationType type;
    private String description;
}