package io.ismaildrissi.app.digitalbanking.services;

import io.ismaildrissi.app.digitalbanking.dtos.*;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
        return bankAccountMapper.getCustomerDTO(customerRepository.save(bankAccountMapper.getCustomer(customer)));
    }

    @Override
    public CustomerDTO updateCustomer(CustomerDTO customer) {
        return bankAccountMapper.getCustomerDTO(customerRepository.save(bankAccountMapper.getCustomer(customer)));
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
    public List<CustomerDTO> listCustomers() {
        List<Customer> customers = customerRepository.findAll();
        return customers.stream().map(bankAccountMapper::getCustomerDTO).toList();
    }

    @Override
    public BankAccountDTO getBankAccountById(String id) throws BankAccountNotFoundException {
        BankAccount bankAccount = bankAccountRepository.findById(id).orElseThrow(() -> new BankAccountNotFoundException("Bank account not found"));
        if(bankAccount instanceof CurrentAccount){
            return bankAccountMapper.getCurrentAccountDTO((CurrentAccount) bankAccount);
        } else {
            return bankAccountMapper.getSavingAccountDTO((SavingAccount) bankAccount);
        }
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
    public List<BankAccountDTO> listBankAccount(){

        List<BankAccount> bankAccounts = bankAccountRepository.findAll();
        return bankAccounts.stream().map(bankAccount -> {
            if(bankAccount instanceof CurrentAccount){
                return bankAccountMapper.getCurrentAccountDTO((CurrentAccount) bankAccount);
            } else {
                return bankAccountMapper.getSavingAccountDTO((SavingAccount) bankAccount);
            }
        }).toList();
    }

    @Override
    public CustomerDTO getCustomer(Long id) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(id).orElseThrow(() -> new CustomerNotFoundException("Customer not found"));
        return bankAccountMapper.getCustomerDTO(customer);
    }

    @Override
    public CurrentAccountDTO saveCurrentAccount(double initialBalance, double overdraft, Long customerId) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(customerId).orElse(null);
        if(customer==null){
            throw new CustomerNotFoundException("Customer not found");
        }
        CurrentAccount currentAccount = new CurrentAccount();
        currentAccount.setId(UUID.randomUUID().toString());
        currentAccount.setCreatedAt(new Date());
        currentAccount.setBalance(initialBalance);
        currentAccount.setOverDraft(overdraft);
        currentAccount.setCustomer(customer);
        return bankAccountMapper.getCurrentAccountDTO(bankAccountRepository.save(currentAccount));
    }

    @Override
    public SavingAccountDTO saveSavingAccount(double initialBalance, double interestRate, Long customerId) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(customerId).orElse(null);
        if(customer==null){
            throw new CustomerNotFoundException("Customer not found");
        }
        SavingAccount savingAccount = new SavingAccount();
        savingAccount.setId(UUID.randomUUID().toString());
        savingAccount.setCreatedAt(new Date());
        savingAccount.setBalance(initialBalance);
        savingAccount.setInterestRate(interestRate);
        savingAccount.setCustomer(customer);
        return bankAccountMapper.getSavingAccountDTO(bankAccountRepository.save(savingAccount));
    }

    @Override
    public List<AccountOperationDTO> accountHistory(String accountId){
        List<AccountOperation> accountOperations = accountOperationRepository.findAccountOperationByBankAccountId(accountId);
        return accountOperations.stream().map(bankAccountMapper::getAccountOperationDTO).toList();
    }


    @Override
    public AccountHistoryDTO accountHistory(String accountId, int page, int size) throws BankAccountNotFoundException {
        BankAccount bankAccount = bankAccountRepository.findById(accountId).orElseThrow(() -> new BankAccountNotFoundException("Bank account not found"));
        Page<AccountOperation> accountOperations = accountOperationRepository.findAccountOperationByBankAccountId(accountId, PageRequest.of(page, size));
        AccountHistoryDTO accountHistoryDTO = new AccountHistoryDTO();
        accountHistoryDTO.setAccountId(accountId);
        accountHistoryDTO.setBalance(bankAccount.getBalance());
        accountHistoryDTO.setCurrentPage(page);
        accountHistoryDTO.setPageSize(size);
        accountHistoryDTO.setAccountOperationDTOS(accountOperations.map(accountOperation -> { return bankAccountMapper.getAccountOperationDTO(accountOperation);}).toList());

        return accountHistoryDTO;
    }

    @Override
    public List<CustomerDTO> searchCustomers(String keyword) {
        return customerRepository.findByNameContains(keyword).stream().map(bankAccountMapper::getCustomerDTO).toList();
    }
}
