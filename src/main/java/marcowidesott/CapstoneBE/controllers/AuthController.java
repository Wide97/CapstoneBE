package marcowidesott.CapstoneBE.controllers;

import jakarta.validation.Valid;
import marcowidesott.CapstoneBE.entities.User;
import marcowidesott.CapstoneBE.exceptions.UserNotFoundException;
import marcowidesott.CapstoneBE.payloads.UserDTO;
import marcowidesott.CapstoneBE.payloads.UserLoginDTO;
import marcowidesott.CapstoneBE.payloads.UserUpdateDTO;
import marcowidesott.CapstoneBE.repositories.UserRepository;
import marcowidesott.CapstoneBE.security.JwtUtils;
import marcowidesott.CapstoneBE.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtils jwtUtils;

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

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(
            @PathVariable Long id,
            @RequestBody @Valid UserUpdateDTO userUpdateDTO,
            Principal principal
    ) {
        if (!hasPermissionToUpdate(principal, id)) {
            throw new AccessDeniedException("Non hai il permesso per modificare questo utente.");
        }

        UserDTO updatedUser = userService.updateUser(id, userUpdateDTO);

        return ResponseEntity.ok(updatedUser);
    }


    private boolean hasPermissionToUpdate(Principal principal, Long userId) {
        String loggedInUsername = principal.getName();

        User loggedInUser = userRepository.findByUsername(loggedInUsername)
                .orElseThrow(() -> new UserNotFoundException("Utente autenticato non trovato."));

        return loggedInUser.getId().equals(userId);
    }


}



