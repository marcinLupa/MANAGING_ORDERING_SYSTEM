package repositories;

import generic.GenericRepository;
import model.Country;

import java.util.Optional;

public interface CountryRepository extends GenericRepository<Country> {

    Optional<Country> findByName(String countryName);
}
