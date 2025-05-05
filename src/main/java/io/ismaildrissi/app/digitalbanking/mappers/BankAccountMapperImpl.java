package io.ismaildrissi.app.digitalbanking.mappers;

import io.ismaildrissi.app.digitalbanking.dtos.CustomerDTO;
import io.ismaildrissi.app.digitalbanking.entities.Customer;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;


@Component
public class BankAccountMapperImpl  implements BankAccountMapper {
    @Override
    public CustomerDTO toCustomerDTO(Customer customer) {
        CustomerDTO customerDTO = new CustomerDTO();
        BeanUtils.copyProperties(customer, customerDTO);
        return customerDTO;
    }

    @Override
    public Customer toCustomer(CustomerDTO customerDTO) {
        Customer customer = new Customer();
        BeanUtils.copyProperties(customerDTO, customer);
        return customer;
    }
}
