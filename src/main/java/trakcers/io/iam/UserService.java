package trakcers.io.iam;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import trakcers.io.model.UserAccount;
import trakcers.io.repo.UserAccountRepository;

@Service
public class UserService {
    private final UserAccountRepository repo;
    private final PasswordEncoder encoder;

    public UserService(UserAccountRepository repo, PasswordEncoder encoder) {
        this.repo = repo;
        this.encoder = encoder;
    }

    public UserAccount register(String username, String password, String role) {
        UserAccount user = new UserAccount();
        user.setUsername(username);
        user.setPassword(encoder.encode(password));
        user.setRole(role);
        return repo.save(user);
    }
}
