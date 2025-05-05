package io.ismaildrissi.app.digitalbanking.mappers;

import io.ismaildrissi.app.digitalbanking.dtos.CustomerDTO;
import io.ismaildrissi.app.digitalbanking.entities.Customer;

public interface BankAccountMapper {

    CustomerDTO toCustomerDTO(Customer customer);
    Customer toCustomer(CustomerDTO customerDTO);
}
