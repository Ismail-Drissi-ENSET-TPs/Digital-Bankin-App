package io.ismaildrissi.app.digitalbanking.web;

import io.ismaildrissi.app.digitalbanking.dtos.*;
import io.ismaildrissi.app.digitalbanking.entities.BankAccount;
import io.ismaildrissi.app.digitalbanking.exceptions.BankAccountNotFoundException;
import io.ismaildrissi.app.digitalbanking.exceptions.InsufficientBalanceException;
import io.ismaildrissi.app.digitalbanking.services.BankAccountService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
// @CrossOrigin("*")
public class BankAccountRestController {

    private BankAccountService bankAccountService;

    @GetMapping("/accounts/{accountId}")
    @PreAuthorize("hasAuthority('SCOPE_ROLE_USER')")
    public BankAccountDTO getBankAccount(@PathVariable String accountId) throws BankAccountNotFoundException {
        return bankAccountService.getBankAccountById(accountId);
    }

    @PreAuthorize("hasAuthority('SCOPE_ROLE_USER')")
    @GetMapping("/accounts/")
    public List<BankAccountDTO> listAccounts(){
        return bankAccountService.listBankAccount();
    }

    @PreAuthorize("hasAuthority('SCOPE_ROLE_USER')")
    @GetMapping("/accounts/{accountId}/operations")
    public List<AccountOperationDTO> getOperations(@PathVariable String accountId){
        return bankAccountService.accountHistory(accountId);
    }

    @PreAuthorize("hasAuthority('SCOPE_ROLE_USER')")
    @GetMapping("/accounts/{accountId}/pageOperations")
    public AccountHistoryDTO getOperations(
            @PathVariable String accountId,
            @RequestParam(name="p", defaultValue = "0") int page,
            @RequestParam(name = "s", defaultValue = "5") int size) throws BankAccountNotFoundException{
        return bankAccountService.accountHistory(accountId, page, size);
    }

    @PreAuthorize("hasAuthority('SCOPE_ROLE_USER')")
    @GetMapping("/accounts/histories")
    public List<AccountHistoryDTO> getAccountsHistory(){
        return bankAccountService.accountsHistories();
    }

    @PreAuthorize("hasAuthority('SCOPE_ROLE_USER')")
    @PostMapping("/accounts/transfer")
    public void transfer(@RequestBody TransferDTO transferDTO) throws BankAccountNotFoundException, InsufficientBalanceException {
        bankAccountService.transfer(transferDTO.getReceiverAccountId(), transferDTO.getSenderAccountId(), transferDTO.getBalance());
    }

    @PreAuthorize("hasAuthority('SCOPE_ROLE_USER')")
    @PostMapping("/accounts/credit")
    public void credit(@RequestBody CreditDebitDTO creditDebitDTO) throws BankAccountNotFoundException {
        bankAccountService.credit(creditDebitDTO.getAccountId(), creditDebitDTO.getAmount(), creditDebitDTO.getDescription());
    }

    @PreAuthorize("hasAuthority('SCOPE_ROLE_USER')")
    @PostMapping("/accounts/debit")
    public void debit(@RequestBody CreditDebitDTO creditDebitDTO) throws BankAccountNotFoundException, InsufficientBalanceException {
        bankAccountService.debit(creditDebitDTO.getAccountId(), creditDebitDTO.getAmount(), creditDebitDTO.getDescription());
    }
}
