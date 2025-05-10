package marcowidesott.CapstoneBE.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.Valid;
import marcowidesott.CapstoneBE.entities.User;
import marcowidesott.CapstoneBE.entities.Valuta;
import marcowidesott.CapstoneBE.exceptions.UserNotFoundException;
import marcowidesott.CapstoneBE.payloads.UserDTO;
import marcowidesott.CapstoneBE.payloads.UserLoginDTO;
import marcowidesott.CapstoneBE.payloads.UserUpdateDTO;
import marcowidesott.CapstoneBE.repositories.UserRepository;
import marcowidesott.CapstoneBE.security.JwtUtils;
import marcowidesott.CapstoneBE.services.CloudinaryService;
import marcowidesott.CapstoneBE.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@CrossOrigin(origins = {
    "http://localhost:3000",                      // per sviluppo locale
    "https://capstone-marco.vercel.app"           // per produzione su Vercel
})
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

    private boolean hasPermissionToUpdate(Principal principal, UUID userId) {
        String loggedInUsername = principal.getName();

        User loggedInUser = userRepository.findByUsername(loggedInUsername)
                .orElseThrow(() -> new UserNotFoundException("Utente autenticato non trovato."));

        return loggedInUser.getId().equals(userId);
    }


    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateUser(
            @PathVariable UUID id,
            @RequestBody @Valid UserUpdateDTO userUpdateDTO,
            Principal principal) throws JsonProcessingException {

        if (!hasPermissionToUpdate(principal, id)) {
            throw new AccessDeniedException("Non hai il permesso per modificare questo utente.");
        }

        UserDTO updatedUser = userService.updateUser(id, userUpdateDTO);

        String newToken = jwtUtils.generateJwtToken(updatedUser.username());

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Dati aggiornati con successo!");
        response.put("newToken", newToken);
        response.put("user", updatedUser);

        return ResponseEntity.ok(response);
    }


    @PostMapping("/{id}/uploadImage")
    public ResponseEntity<String> uploadProfileImage(@PathVariable UUID id, @RequestParam("file") MultipartFile file) {
        try {

            userService.uploadProfileImage(id, file);
            return ResponseEntity.ok("Immagine caricata con successo.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Errore nel caricamento dell'immagine: " + e.getMessage());
        }
    }


    @GetMapping("/profile")
    public ResponseEntity<Map<String, Object>> getUserProfile(Principal principal) {
        String username = principal.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("Utente non trovato."));


        String newToken = jwtUtils.generateJwtToken(user.getUsername());


        UserDTO userDTO = new UserDTO(
                user.getUsername(),
                null,
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getProfileImageUrl(),
                user.getId(),
                user.getValuta()
        );

        Map<String, Object> response = new HashMap<>();
        response.put("user", userDTO);
        response.put("newToken", newToken);

        return ResponseEntity.ok(response);
    }


    @GetMapping("/utente/{userId}/valuta")
    public ResponseEntity<Valuta> getValutaPreferita(@PathVariable UUID userId) {
        User user = userService.getUserById(userId);
        Valuta valutaPreferita = user.getValuta();
        return ResponseEntity.ok(valutaPreferita);
    }


}



