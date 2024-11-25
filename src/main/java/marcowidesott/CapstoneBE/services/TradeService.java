package marcowidesott.CapstoneBE.services;

import marcowidesott.CapstoneBE.entities.Trade;
import marcowidesott.CapstoneBE.entities.User;
import marcowidesott.CapstoneBE.exceptions.InvalidDateFormatException;
import marcowidesott.CapstoneBE.exceptions.TradeNotFoundException;
import marcowidesott.CapstoneBE.exceptions.UnauthorizedException;
import marcowidesott.CapstoneBE.exceptions.UserNotFoundException;
import marcowidesott.CapstoneBE.payloads.TradeDTO;
import marcowidesott.CapstoneBE.repositories.TradeRepository;
import marcowidesott.CapstoneBE.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TradeService {

    @Autowired
    private TradeRepository tradeRepository;

    @Autowired
    private UserRepository userRepository;


    public Trade createTrade(UUID userId, TradeDTO tradeDTO) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Trade trade = new Trade();


        try {
            trade.setPurchaseDate(LocalDate.parse(tradeDTO.purchaseDate())); // Formato data: yyyy-MM-dd
        } catch (DateTimeParseException e) {
            throw new InvalidDateFormatException("Formato data di acquisto non valido. Usa il formato: yyyy-MM-dd");
        }

        try {
            trade.setSaleDate(LocalDate.parse(tradeDTO.saleDate())); // Formato data: yyyy-MM-dd
        } catch (DateTimeParseException e) {
            throw new InvalidDateFormatException("Formato data di vendita non valido. Usa il formato: yyyy-MM-dd");
        }

        try {
            trade.setPurchaseTime(LocalTime.parse(tradeDTO.purchaseTime())); // Formato ora: HH:mm
        } catch (DateTimeParseException e) {
            throw new InvalidDateFormatException("Formato ora di acquisto non valido. Usa il formato: HH:mm");
        }

        try {
            trade.setSaleTime(LocalTime.parse(tradeDTO.saleTime())); // Formato ora: HH:mm
        } catch (DateTimeParseException e) {
            throw new InvalidDateFormatException("Formato ora di vendita non valido. Usa il formato: HH:mm");
        }


        trade.setPositionSize(tradeDTO.positionSize());
        trade.setLeverage(String.valueOf(tradeDTO.leverage()));
        trade.setStrategy(tradeDTO.strategy());
        trade.setTradeType(tradeDTO.tradeType());
        trade.setOpeningCosts(tradeDTO.openingCosts());
        trade.setClosingCosts(tradeDTO.closingCosts());
        trade.setResult(tradeDTO.result());
        trade.setAsset(tradeDTO.asset());

        trade.setUser(user);

        return tradeRepository.save(trade);
    }

    // Modifica di un trade
    public Trade updateTrade(UUID tradeId, TradeDTO tradeDTO) {
        // Recupera il trade esistente
        Trade trade = tradeRepository.findById(tradeId)
                .orElseThrow(() -> new TradeNotFoundException("Trade non trovato"));

        // Aggiorna i campi con i nuovi valori dal DTO
        try {
            trade.setPurchaseDate(LocalDate.parse(tradeDTO.purchaseDate()));
        } catch (DateTimeParseException e) {
            throw new InvalidDateFormatException("Formato data di acquisto non valido");
        }
        // ...aggiorna altri campi come le date e orari

        trade.setPositionSize(tradeDTO.positionSize());
        trade.setLeverage(String.valueOf(tradeDTO.leverage()));
        trade.setStrategy(tradeDTO.strategy());
        trade.setTradeType(tradeDTO.tradeType());
        trade.setOpeningCosts(tradeDTO.openingCosts());
        trade.setClosingCosts(tradeDTO.closingCosts());
        trade.setResult(tradeDTO.result());
        trade.setAsset(tradeDTO.asset());

        return tradeRepository.save(trade);
    }

    // Ottieni tutti i trade per un dato utente
    public List<TradeDTO> getAllTradesByUserId(UUID userId) {
        List<Trade> trades = tradeRepository.findByUserId(userId);

        return trades.stream()
                .map(trade -> new TradeDTO(
                        trade.getPurchaseDate().toString(),
                        trade.getSaleDate().toString(),
                        trade.getPurchaseTime().toString(),
                        trade.getSaleTime().toString(),
                        trade.getPositionSize(),
                        trade.getLeverage(),
                        trade.getStrategy(),
                        trade.getTradeType(),
                        trade.getOpeningCosts(),
                        trade.getClosingCosts(),
                        trade.getResult(),
                        trade.getAsset(),
                        trade.getId()
                ))
                .collect(Collectors.toList());
    }


    public void deleteTrade(UUID tradeId, UUID userId) {
        Trade trade = tradeRepository.findById(tradeId)
                .orElseThrow(() -> new TradeNotFoundException("Trade non trovato"));

        if (!trade.getUser().getId().equals(userId)) {
            throw new UnauthorizedException("Non hai il permesso per eliminare questo trade.");
        }

        tradeRepository.delete(trade);
    }
}


