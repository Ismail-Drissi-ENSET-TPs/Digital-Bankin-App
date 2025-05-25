package io.ismaildrissi.app.digitalbanking.repositories;

import io.ismaildrissi.app.digitalbanking.entities.AccountOperation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountOperationRepository extends JpaRepository<AccountOperation, Long> {
    List<AccountOperation> findAccountOperationByBankAccountId(String accountId);
    Page<AccountOperation> findAccountOperationByBankAccountId(String accountId, Pageable pageable);

    List<AccountOperation> findAllByBankAccountId(String id);
}
