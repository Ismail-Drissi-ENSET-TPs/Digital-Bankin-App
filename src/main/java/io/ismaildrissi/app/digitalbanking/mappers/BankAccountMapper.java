package io.ismaildrissi.app.digitalbanking.mappers;

import io.ismaildrissi.app.digitalbanking.dtos.AccountOperationDTO;
import io.ismaildrissi.app.digitalbanking.dtos.CurrentAccountDTO;
import io.ismaildrissi.app.digitalbanking.dtos.CustomerDTO;
import io.ismaildrissi.app.digitalbanking.dtos.SavingAccountDTO;
import io.ismaildrissi.app.digitalbanking.entities.AccountOperation;
import io.ismaildrissi.app.digitalbanking.entities.CurrentAccount;
import io.ismaildrissi.app.digitalbanking.entities.Customer;
import io.ismaildrissi.app.digitalbanking.entities.SavingAccount;
import org.springframework.stereotype.Service;

public interface BankAccountMapper {
    CustomerDTO getCustomerDTO(Customer customer);
    Customer getCustomer(CustomerDTO customerDTO);
    SavingAccountDTO getSavingAccountDTO(SavingAccount savingAccount);
    SavingAccount getSavingAccount(SavingAccountDTO savingAccountDTO);
    CurrentAccountDTO getCurrentAccountDTO(CurrentAccount currentAccount);
    CurrentAccount getCurrentAccount(CurrentAccountDTO currentAccountDTO);
    AccountOperation getAccountOperation(AccountOperationDTO accountOperationDTO);
    AccountOperationDTO getAccountOperationDTO(AccountOperation accountOperation);
}