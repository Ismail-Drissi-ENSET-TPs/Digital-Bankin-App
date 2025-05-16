package io.ismaildrissi.app.digitalbanking.web;

import io.ismaildrissi.app.digitalbanking.dtos.*;
import io.ismaildrissi.app.digitalbanking.entities.BankAccount;
import io.ismaildrissi.app.digitalbanking.exceptions.BankAccountNotFoundException;
import io.ismaildrissi.app.digitalbanking.exceptions.InsufficientBalanceException;
import io.ismaildrissi.app.digitalbanking.services.BankAccountService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@CrossOrigin("*")
public class BankAccountRestController {

    private BankAccountService bankAccountService;

    @GetMapping("/accounts/{accountId}")
    public BankAccountDTO getBankAccount(@PathVariable String accountId) throws BankAccountNotFoundException {
        return bankAccountService.getBankAccountById(accountId);
    }

    @GetMapping("/accounts/")
    public List<BankAccountDTO> listAccounts(){
        return bankAccountService.listBankAccount();
    }

    @GetMapping("/accounts/{accountId}/operations")
    public List<AccountOperationDTO> getOperations(@PathVariable String accountId){
        return bankAccountService.accountHistory(accountId);
    }

    @GetMapping("/accounts/{accountId}/pageOperations")
    public AccountHistoryDTO getOperations(
            @PathVariable String accountId,
            @RequestParam(name="p", defaultValue = "0") int page,
            @RequestParam(name = "s", defaultValue = "5") int size) throws BankAccountNotFoundException{
        return bankAccountService.accountHistory(accountId, page, size);
    }

    @PostMapping("/accounts/transfer")
    public void transfer(@RequestBody TransferDTO transferDTO) throws BankAccountNotFoundException, InsufficientBalanceException {
        bankAccountService.transfer(transferDTO.getReceiverAccountId(), transferDTO.getSenderAccountId(), transferDTO.getBalance());
    }

    @PostMapping("/accounts/credit")
    public void credit(@RequestBody CreditDebitDTO creditDebitDTO) throws BankAccountNotFoundException {
        bankAccountService.credit(creditDebitDTO.getAccountId(), creditDebitDTO.getAmount(), creditDebitDTO.getDescription());
    }

    @PostMapping("/accounts/debit")
    public void debit(@RequestBody CreditDebitDTO creditDebitDTO) throws BankAccountNotFoundException, InsufficientBalanceException {
        bankAccountService.debit(creditDebitDTO.getAccountId(), creditDebitDTO.getAmount(), creditDebitDTO.getDescription());
    }
}
