package io.ismaildrissi.app.digitalbanking.dtos;

import io.ismaildrissi.app.digitalbanking.entities.AccountOperation;
import lombok.Data;

import java.util.List;

@Data
public class AccountHistoryDTO {
    private String accountId;
    private double balance;
    private int currentPage;
    private int pageSize;
    private List<AccountOperationDTO> accountOperationDTOS;
}
