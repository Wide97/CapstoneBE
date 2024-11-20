package marcowidesott.CapstoneBE.controllers;

import jakarta.validation.Valid;
import marcowidesott.CapstoneBE.entities.User;
import marcowidesott.CapstoneBE.exceptions.UserNotFoundException;
import marcowidesott.CapstoneBE.payloads.UserDTO;
import marcowidesott.CapstoneBE.payloads.UserLoginDTO;
import marcowidesott.CapstoneBE.payloads.UserUpdateDTO;
import marcowidesott.CapstoneBE.repositories.UserRepository;
import marcowidesott.CapstoneBE.security.JwtUtils;
import marcowidesott.CapstoneBE.services.CloudinaryService;
import marcowidesott.CapstoneBE.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private CloudinaryService cloudinaryService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/register")
    public String registerUser(@Valid @RequestBody UserDTO userDTO) {
        User user = User.builder()
                .username(userDTO.username())
                .password(userDTO.password())
                .email(userDTO.email())
                .firstName(userDTO.firstName())
                .lastName(userDTO.lastName())
                .build();

        userService.registerUser(user);
        return "Utente registrato con successo!";
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@Valid @RequestBody UserLoginDTO loginRequest) {
        User user = userService.authenticateUser(loginRequest.username(), loginRequest.password());
        String jwtToken = jwtUtils.generateJwtToken(user.getUsername());
        return ResponseEntity.ok(jwtToken);
    }

    @GetMapping("/validateToken")
    public ResponseEntity<Void> validateToken(@RequestParam String token) {
        jwtUtils.validateToken(token);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(
            @PathVariable Long id,
            @RequestBody @Valid UserUpdateDTO userUpdateDTO
    ) {
        UserDTO updatedUser = userService.updateUser(id, userUpdateDTO);
        return ResponseEntity.ok(updatedUser);
    }

    @PostMapping("/{id}/uploadImage")
    public ResponseEntity<String> uploadProfileImage(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        try {
            String imageUrl = cloudinaryService.uploadFile(file);
            userService.updateProfileImage(id, imageUrl);
            return ResponseEntity.ok("Immagine caricata con successo: " + imageUrl);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Errore durante il caricamento dell'immagine: " + e.getMessage());
        }
    }

    @GetMapping("/profile")
    public ResponseEntity<UserDTO> getUserProfile(Principal principal) {
        String username = principal.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("Utente non trovato."));

        UserDTO userDTO = new UserDTO(
                user.getUsername(),
                null,  // La password non viene restituita
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getProfileImageUrl()  // Aggiungi l'URL dell'immagine
        );

        return ResponseEntity.ok(userDTO);
    }
}


