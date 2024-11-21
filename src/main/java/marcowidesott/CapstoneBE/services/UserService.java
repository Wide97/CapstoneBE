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
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

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

    public UserDTO updateUser(Long id, UserUpdateDTO userUpdateDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Utente non trovato con ID: " + id));


        user.setUsername(userUpdateDTO.username());
        user.setPassword(passwordEncoder.encode(userUpdateDTO.password()));  // Cripta la password

        userRepository.save(user);


        return new UserDTO(
                user.getUsername(),
                null,
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getProfileImageUrl()
        );
    }


    public void uploadProfileImage(Long userId, MultipartFile file) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Utente non trovato con ID: " + userId));

        try {
            // Carica l'immagine nel cloud (Cloudinary ad esempio)
            String imageUrl = cloudinaryService.uploadFile(file);

            // Salva l'URL dell'immagine nel database
            user.setProfileImageUrl(imageUrl);
            userRepository.save(user);
        } catch (Exception e) {
            throw new RuntimeException("Errore nel caricamento dell'immagine: " + e.getMessage());
        }
    }
}

