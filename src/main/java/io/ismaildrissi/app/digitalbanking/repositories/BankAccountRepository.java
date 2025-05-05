package io.ismaildrissi.app.digitalbanking.repositories;

import io.ismaildrissi.app.digitalbanking.entities.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankAccountRepository extends JpaRepository<BankAccount, String> {
}
