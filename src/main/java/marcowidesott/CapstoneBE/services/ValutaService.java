package marcowidesott.CapstoneBE.services;

import marcowidesott.CapstoneBE.entities.User;
import marcowidesott.CapstoneBE.entities.Valuta;
import marcowidesott.CapstoneBE.exceptions.UserNotFoundException;
import marcowidesott.CapstoneBE.exceptions.ValutaNotFoundException;
import marcowidesott.CapstoneBE.repositories.UserRepository;
import marcowidesott.CapstoneBE.repositories.ValutaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ValutaService {

    @Autowired
    private ValutaRepository valutaRepository;

    @Autowired
    private UserRepository userRepository;

    public List<Valuta> getAllValute() {
        return valutaRepository.findAll();
    }

    public Valuta getValutaById(UUID id) {
        System.out.println("Tentativo di recupero valuta con ID: " + id);
        return valutaRepository.findById(id)
                .orElseThrow(() -> new ValutaNotFoundException("Valuta non trovata con ID: " + id));
    }
    

    public Valuta createValuta(Valuta valuta) {
        return valutaRepository.save(valuta);
    }

    public Valuta updateValuta(UUID id, Valuta valutaDetails) {
        Valuta valuta = getValutaById(id);
        valuta.setCodice(valutaDetails.getCodice());
        valuta.setNome(valutaDetails.getNome());
        valuta.setSimbolo(valutaDetails.getSimbolo());
        return valutaRepository.save(valuta);
    }

    public void deleteValuta(UUID id) {
        Valuta valuta = getValutaById(id);
        valutaRepository.delete(valuta);
    }

    public User aggiornaValutaPerUtente(UUID userId, UUID valutaId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Utente non trovato con ID: " + userId));

        Valuta valuta = getValutaById(valutaId);
        user.setValuta(valuta);

        return userRepository.save(user);
    }

}
