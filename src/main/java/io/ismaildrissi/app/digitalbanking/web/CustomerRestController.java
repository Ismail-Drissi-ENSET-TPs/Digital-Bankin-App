package io.ismaildrissi.app.digitalbanking.web;

import io.ismaildrissi.app.digitalbanking.dtos.BankAccountDTO;
import io.ismaildrissi.app.digitalbanking.dtos.CustomerDTO;
import io.ismaildrissi.app.digitalbanking.entities.BankAccount;
import io.ismaildrissi.app.digitalbanking.entities.Customer;
import io.ismaildrissi.app.digitalbanking.exceptions.CustomerNotFoundException;
import io.ismaildrissi.app.digitalbanking.repositories.CustomerRepository;
import io.ismaildrissi.app.digitalbanking.services.BankAccountService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@Slf4j
// @CrossOrigin("*")
public class CustomerRestController {
    private final CustomerRepository customerRepository;
    private BankAccountService bankAccountService;

    @GetMapping("/customers")
    @PreAuthorize("hasAuthority('SCOPE_ROLE_USER')")
    public List<CustomerDTO> customers() {
        return bankAccountService.listCustomers();
    }

    @GetMapping("/customer/{id}")
    @PreAuthorize("hasAuthority('SCOPE_ROLE_USER')")
    public CustomerDTO customer(@PathVariable(name = "id") Long id) throws CustomerNotFoundException {
        return bankAccountService.getCustomer(id);
    }

    @PostMapping("/customer/")
    @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
    public CustomerDTO createCustomer(@RequestBody CustomerDTO customerDTO) {
        return bankAccountService.saveCustomer(customerDTO);
    }

    @PutMapping("/customer/")
    @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
    public CustomerDTO updateCustomer(@RequestBody CustomerDTO customerDTO) {
        return bankAccountService.updateCustomer(customerDTO);
    }

    @DeleteMapping("/customer/{id}")
    @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
    public void deleteCustomer(@PathVariable Long id) {
        bankAccountService.deleteCustomer(id);
    }

    @PreAuthorize("hasAuthority('SCOPE_ROLE_USER')")
    @GetMapping("/customers/search")
    public List<CustomerDTO> searchCustomer(@RequestParam(value = "k", defaultValue = "") String keyword){
        return bankAccountService.searchCustomers(keyword);
    }

    @PreAuthorize("hasAuthority('SCOPE_ROLE_USER')")
    @GetMapping("/customer/{id}/accounts")
    public List<BankAccountDTO> getBankAccountByCustomerId(@PathVariable Long id){
        return bankAccountService.listBankAccountByCustomerId(id);
    }
}
