package marcowidesott.CapstoneBE.payloads;

import jakarta.validation.constraints.Size;

public record UserUpdateDTO(
        @Size(min = 2, max = 30, message = "Il nome utente deve contenere tra 2 e 30 caratteri.")
        String username,

        @Size(min = 8, message = "La password deve contenere almeno 8 caratteri.")
        String password,

        String profileImageUrl
) {
}



