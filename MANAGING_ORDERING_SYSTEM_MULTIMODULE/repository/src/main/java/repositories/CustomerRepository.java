package repositories;

import generic.GenericRepository;
import model.Customer;

import java.util.Optional;

public interface CustomerRepository extends GenericRepository<Customer> {
    Optional<Customer> findByName(String customerName);

}
