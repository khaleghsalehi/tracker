package trakcers.io.iam;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import trakcers.io.model.UserAccount;
import trakcers.io.repo.UserAccountRepository;

import java.io.IOException;

@RestController
@RequestMapping("/v1/")
public class UserAccountController {

    private final UserAccountRepository repo;
    private final PasswordEncoder passwordEncoder;

    public UserAccountController(UserAccountRepository repo, PasswordEncoder passwordEncoder) {
        this.repo = repo;
        this.passwordEncoder = passwordEncoder;
    }

    // Create new account
    @PostMapping("/signUp")
    public void registerUser(@ModelAttribute("registerRequest") RegisterRequest request, HttpServletResponse response) throws IOException {
        System.out.println("username  -> " + request.getUsername());
        System.out.println("password  -> " + request.getPassword());
        // Check if username already exists
        if (repo.findByUsername(request.getUsername()).isPresent()) {
//            return String.valueOf(ResponseEntity.badRequest().body("Username already taken!"));
            response.sendRedirect("/register");
            return;
            //return "/register";
        }

        UserAccount user = new UserAccount();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword())); // 🔐 encrypt
        user.setRole("ROLE_USER"); // default role

        repo.save(user);
        response.sendRedirect("/login");
        return;
//       // return "/login";
//        return String.valueOf(ResponseEntity.ok("User registered successfully!"));
    }
}

