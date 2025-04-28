package io.ismaildrissi.app.digitalbanking.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@DiscriminatorValue("SA")
@AllArgsConstructor
@NoArgsConstructor
public class SavingAccount extends BankAccount {
    private double interestRate;
}