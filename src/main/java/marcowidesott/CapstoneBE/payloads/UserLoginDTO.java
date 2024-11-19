package marcowidesott.CapstoneBE.payloads;

import jakarta.validation.constraints.NotEmpty;

public record UserLoginDTO(
        @NotEmpty(message = "Il nome utente è obbligatorio.")
        String username,

        @NotEmpty(message = "La password è obbligatoria.")
        String password
) {
}

