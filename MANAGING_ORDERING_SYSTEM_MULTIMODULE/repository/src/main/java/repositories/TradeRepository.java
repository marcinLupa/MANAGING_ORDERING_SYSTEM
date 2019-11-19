package repositories;

import generic.GenericRepository;
import model.Trade;

import java.util.Optional;

public interface TradeRepository extends GenericRepository<Trade> {
     Optional<Trade> findByName(String tradeName);
}
