package repositories;

import generic.GenericRepository;
import model.Producer;

import java.util.Optional;

public interface ProducerRepository extends GenericRepository<Producer> {
    Optional<Producer> findByName(String producerName);

    Optional<Producer> findByCountry(String countryName);
}
