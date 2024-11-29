package marcowidesott.CapstoneBE.services;

import jakarta.transaction.Transactional;
import marcowidesott.CapstoneBE.entities.User;
import marcowidesott.CapstoneBE.entities.Valuta;
import marcowidesott.CapstoneBE.exceptions.InvalidCredentialsException;
import marcowidesott.CapstoneBE.exceptions.UserNotFoundException;
import marcowidesott.CapstoneBE.payloads.UserDTO;
import marcowidesott.CapstoneBE.payloads.UserUpdateDTO;
import marcowidesott.CapstoneBE.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CloudinaryService cloudinaryService;

    public User registerUser(User user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new RuntimeException("Username già in uso.");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

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

    public UserDTO updateUser(UUID id, UserUpdateDTO userUpdateDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Utente non trovato con ID: " + id));

        // Update username and password
        user.setUsername(userUpdateDTO.username());
        user.setPassword(passwordEncoder.encode(userUpdateDTO.password()));

        userRepository.save(user);

        return new UserDTO(
                user.getUsername(),
                null,
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getProfileImageUrl(),
                user.getId()
        );
    }


    public void uploadProfileImage(UUID id, MultipartFile file) throws IOException {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Utente non trovato con ID: " + id));


        String imageUrl = cloudinaryService.uploadFile(file);


        user.setProfileImageUrl(imageUrl);
        userRepository.save(user);
    }

    @Transactional
    public User aggiornaValutaPreferita(UUID userId, Valuta nuovaValuta) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utente non trovato con ID: " + userId));
        user.setValuta(nuovaValuta);
        return userRepository.save(user);
    }

}

