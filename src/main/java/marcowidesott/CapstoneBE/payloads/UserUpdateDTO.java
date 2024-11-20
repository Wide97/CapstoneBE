package marcowidesott.CapstoneBE.payloads;

import jakarta.validation.constraints.Size;

public record UserUpdateDTO(
        @Size(min = 2, max = 30, message = "Il nome deve contenere tra 2 e 30 caratteri.")
        String firstName,

        @Size(min = 2, max = 30, message = "Il cognome deve contenere tra 2 e 30 caratteri.")
        String lastName,

        @Size(min = 8, message = "La password deve contenere almeno 8 caratteri.")
        String password
) {
}


