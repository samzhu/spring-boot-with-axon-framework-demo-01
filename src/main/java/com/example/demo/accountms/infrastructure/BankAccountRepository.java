package com.example.demo.accountms.infrastructure;

import com.example.demo.accountms.domain.projections.BankAccount;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

@RepositoryRestResource
public interface BankAccountRepository extends JpaRepository<BankAccount, String> {
    
    @Override
    @RestResource(exported = false)
    // Prevents POST /bankAccounts and PATCH /bankAccounts/:id
    BankAccount save(BankAccount bankAccount);

    @Override
    @RestResource(exported = false)
    // Prevents DELETE /bankAccounts/:id
    void deleteById(String accountNumber);

    @Override
    @RestResource(exported = false)
    void delete(BankAccount entity);

}
