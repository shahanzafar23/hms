package com.hms.repository;

import com.hms.domain.Wallet;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data MongoDB repository for the Wallet entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WalletRepository extends MongoRepository<Wallet, String> {
    Optional<Wallet> findOneByUserId(String userId);

    Optional<Wallet> findOneByPublicKey(String publicKey);
}
