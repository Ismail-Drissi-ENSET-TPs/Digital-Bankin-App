package io.ismaildrissi.app.digitalbanking.repositories;

import io.ismaildrissi.app.digitalbanking.entities.AccountOperation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountOperationRepository extends JpaRepository<AccountOperation, Long> {
}
