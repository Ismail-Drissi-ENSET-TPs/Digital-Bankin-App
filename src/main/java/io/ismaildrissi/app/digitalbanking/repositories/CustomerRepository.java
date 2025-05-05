package io.ismaildrissi.app.digitalbanking.repositories;

import io.ismaildrissi.app.digitalbanking.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
