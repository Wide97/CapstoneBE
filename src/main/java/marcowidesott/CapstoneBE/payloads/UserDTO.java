package marcowidesott.CapstoneBE.payloads;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record UserDTO(
        @NotEmpty(message = "Il nome utente è obbligatorio.")
        @Size(min = 3, max = 20, message = "Il nome utente deve contenere tra 3 e 20 caratteri.")
        String username,

        @NotEmpty(message = "La password è obbligatoria.")
        @Size(min = 8, message = "La password deve contenere almeno 8 caratteri.")
        String password,

        @NotEmpty(message = "L'email è obbligatoria.")
        @Email(message = "Inserisci un'email valida.")
        String email,

        @NotEmpty(message = "Il nome è obbligatorio.")
        @Size(min = 2, max = 30, message = "Il nome deve contenere tra 2 e 30 caratteri.")
        String firstName,

        @NotEmpty(message = "Il cognome è obbligatorio.")
        @Size(min = 2, max = 30, message = "Il cognome deve contenere tra 2 e 30 caratteri.")
        String lastName
) {
}

