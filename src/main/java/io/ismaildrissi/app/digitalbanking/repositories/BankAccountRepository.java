package io.ismaildrissi.app.digitalbanking.repositories;

import io.ismaildrissi.app.digitalbanking.dtos.BankAccountDTO;
import io.ismaildrissi.app.digitalbanking.entities.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BankAccountRepository extends JpaRepository<BankAccount, String> {
    List<BankAccount> findAllByCustomerId(Long customerId);
}
