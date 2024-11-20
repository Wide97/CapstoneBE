package marcowidesott.CapstoneBE.services;

import marcowidesott.CapstoneBE.entities.User;
import marcowidesott.CapstoneBE.exceptions.InvalidCredentialsException;
import marcowidesott.CapstoneBE.exceptions.UserNotFoundException;
import marcowidesott.CapstoneBE.payloads.UserDTO;
import marcowidesott.CapstoneBE.payloads.UserUpdateDTO;
import marcowidesott.CapstoneBE.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User authenticateUser(String username, String password) {
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isEmpty()) {
            throw new UserNotFoundException("Utente con username '" + username + "' non trovato.");
        }
        User user = userOpt.get();
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new InvalidCredentialsException("Le credenziali sono errate.");
        }
        return user;
    }

    public User registerUser(User user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new RuntimeException("Username già in uso.");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userRepository.save(user);
    }

    public UserDTO updateUser(Long id, UserUpdateDTO userUpdateDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Utente non trovato con ID: " + id));

        user.setFirstName(userUpdateDTO.firstName());
        user.setLastName(userUpdateDTO.lastName());
        user.setPassword(passwordEncoder.encode(userUpdateDTO.password()));
        
        userRepository.save(user);

        return new UserDTO(
                user.getUsername(),
                null,
                user.getEmail(),
                user.getFirstName(),
                user.getLastName()
        );
    }
}

