package io.ismaildrissi.app.digitalbanking.services;

import io.ismaildrissi.app.digitalbanking.dtos.CustomerDTO;
import io.ismaildrissi.app.digitalbanking.entities.*;
import io.ismaildrissi.app.digitalbanking.entities.Customer;
import io.ismaildrissi.app.digitalbanking.enums.AccountStatus;
import io.ismaildrissi.app.digitalbanking.enums.OperationType;
import io.ismaildrissi.app.digitalbanking.exceptions.BankAccountNotFoundException;
import io.ismaildrissi.app.digitalbanking.exceptions.CustomerNotFoundException;
import io.ismaildrissi.app.digitalbanking.exceptions.InsufficientBalanceException;
import io.ismaildrissi.app.digitalbanking.mappers.BankAccountMapperImpl;
import io.ismaildrissi.app.digitalbanking.repositories.AccountOperationRepository;
import io.ismaildrissi.app.digitalbanking.repositories.BankAccountRepository;
import io.ismaildrissi.app.digitalbanking.repositories.CustomerRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class BankAccountServiceImpl implements BankAccountService{

    private final BankAccountRepository bankAccountRepository;
    private final CustomerRepository customerRepository;
    private final AccountOperationRepository accountOperationRepository;
    private final BankAccountMapperImpl bankAccountMapper;

    @Override
    public CustomerDTO saveCustomer(CustomerDTO customer) {
        return bankAccountMapper.toCustomerDTO(customerRepository.save(bankAccountMapper.toCustomer(customer)));
    }

    @Override
    public CustomerDTO updateCustomer(CustomerDTO customer) {
        return bankAccountMapper.toCustomerDTO(customerRepository.save(bankAccountMapper.toCustomer(customer)));
    }

    @Override
    public void deleteCustomer(Long id) {
        customerRepository.deleteById(id);
    }

    private BankAccount saveBankAccount(BankAccount bankAccount, Long customerId) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(customerId).orElseThrow(() -> new CustomerNotFoundException("Customer not found"));
        bankAccount.setCustomer(customer);
        bankAccount.setCreatedAt(new Date());
        bankAccount.setStatus(AccountStatus.ACTIVATED);
        bankAccount.setId(UUID.randomUUID().toString());
        return bankAccountRepository.save(bankAccount);
    }

    @Override
    public CurrentAccount saveCurrentBankAccount(double initialBalance, Long customerId) throws CustomerNotFoundException {
        CurrentAccount currentAccount = new CurrentAccount();
        currentAccount.setOverDraft(9000);
        return (CurrentAccount)saveBankAccount(currentAccount, customerId);
    }

    @Override
    public SavingAccount saveSavingBankAccount(double initialBalance, Long customerId) throws CustomerNotFoundException {
        SavingAccount savingAccount = new SavingAccount();
        savingAccount.setInterestRate(5.5);
        return (SavingAccount) saveBankAccount(savingAccount, customerId);
    }

    @Override
    public List<CustomerDTO> listCustomers() {
        List<Customer> customers = customerRepository.findAll();
        List<CustomerDTO> customerDTOS = customers.stream().map(bankAccountMapper::toCustomerDTO).toList();
        return customerDTOS;
    }

    @Override
    public BankAccount getBankAccountById(String id) throws BankAccountNotFoundException {
        return bankAccountRepository.findById(id).orElseThrow(() -> new BankAccountNotFoundException("Bank account not found"));
    }

    @Override
    public void debit(String accountId, double amount, String description) throws BankAccountNotFoundException, InsufficientBalanceException {
        BankAccount bankAccount = bankAccountRepository.findById(accountId).orElseThrow(() -> new BankAccountNotFoundException("Bank account not found"));
        if (bankAccount.getBalance() < amount) {
            throw new InsufficientBalanceException("Insufficient balance");
        }
        bankAccount.setBalance(bankAccount.getBalance() - amount);
        bankAccountRepository.save(bankAccount);
        AccountOperation accountOperation = new AccountOperation();
        accountOperation.setAmount(amount);
        accountOperation.setDescription(description);
        accountOperation.setType(OperationType.DEBIT);
        accountOperation.setBankAccount(bankAccount);
        accountOperation.setOperationDate(new Date());
        accountOperationRepository.save(accountOperation);
    }

    @Override
    public void credit(String accountId, double amount, String description) throws BankAccountNotFoundException {
        BankAccount bankAccount = bankAccountRepository.findById(accountId).orElseThrow(() -> new BankAccountNotFoundException("Bank account not found"));
        bankAccount.setBalance(bankAccount.getBalance() + amount);
        bankAccountRepository.save(bankAccount);
        AccountOperation accountOperation = new AccountOperation();
        accountOperation.setAmount(amount);
        accountOperation.setDescription(description);
        accountOperation.setType(OperationType.CREDIT);
        accountOperation.setBankAccount(bankAccount);
        accountOperation.setOperationDate(new Date());
        accountOperationRepository.save(accountOperation);
    }

    @Override
    public void transfer(String accountIdSource, String accountIdDestination, double amount) throws BankAccountNotFoundException, InsufficientBalanceException {
        debit(accountIdSource, amount, "Transfer from " + accountIdSource + " to " + accountIdDestination);
        credit(accountIdDestination, amount, "Transfer from " + accountIdSource + " to " + accountIdDestination);
    }

    @Override
    public List<BankAccount> listBankAccount(){
        return bankAccountRepository.findAll();
    }

    @Override
    public CustomerDTO getCustomer(Long id) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(id).orElseThrow(() -> new CustomerNotFoundException("Customer not found"));
        return bankAccountMapper.toCustomerDTO(customer);
    }
}
