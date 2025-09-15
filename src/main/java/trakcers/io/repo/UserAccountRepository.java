package trakcers.io.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import trakcers.io.model.UserAccount;

import java.util.Optional;

public interface UserAccountRepository extends JpaRepository<UserAccount, Long> {
    Optional<UserAccount> findByUsername(String username);
}