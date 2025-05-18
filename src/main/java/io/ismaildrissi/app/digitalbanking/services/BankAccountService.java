package io.ismaildrissi.app.digitalbanking.services;

import io.ismaildrissi.app.digitalbanking.dtos.*;
import io.ismaildrissi.app.digitalbanking.entities.BankAccount;
import io.ismaildrissi.app.digitalbanking.entities.CurrentAccount;
import io.ismaildrissi.app.digitalbanking.entities.Customer;
import io.ismaildrissi.app.digitalbanking.entities.SavingAccount;
import io.ismaildrissi.app.digitalbanking.exceptions.BankAccountNotFoundException;
import io.ismaildrissi.app.digitalbanking.exceptions.CustomerNotFoundException;
import io.ismaildrissi.app.digitalbanking.exceptions.InsufficientBalanceException;

import java.util.List;

public interface BankAccountService {

    CustomerDTO saveCustomer(CustomerDTO customer);
    CustomerDTO updateCustomer(CustomerDTO customer);
    void deleteCustomer(Long id);
    List<CustomerDTO> listCustomers();
    CustomerDTO getCustomer(Long id) throws CustomerNotFoundException;
    BankAccountDTO getBankAccountById(String id) throws BankAccountNotFoundException;
    void debit(String accountId, double amount, String description) throws BankAccountNotFoundException, InsufficientBalanceException;
    void credit(String accountId, double amount, String description) throws BankAccountNotFoundException;
    void transfer(String accountIdSource, String accountIdDestination, double amount) throws BankAccountNotFoundException, InsufficientBalanceException;
    List<BankAccountDTO> listBankAccount();
    CurrentAccountDTO saveCurrentAccount(double initialBalance, double overdraft, Long customerId) throws CustomerNotFoundException;
    SavingAccountDTO saveSavingAccount(double intitialBalance, double interestRate, Long customerId) throws CustomerNotFoundException;

    List<AccountOperationDTO> accountHistory(String accountId);

    List<AccountHistoryDTO> accountsHistories();

    List<CustomerDTO> searchCustomers(String keyword);
    AccountHistoryDTO accountHistory(String accountId, int page, int size) throws BankAccountNotFoundException;
}
