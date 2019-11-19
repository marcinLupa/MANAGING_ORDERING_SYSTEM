package repositories;

import generic.GenericRepository;
import model.Payment;
import utils.EPayment;

import java.util.Optional;

public interface PaymentRepository extends GenericRepository<Payment> {
    Optional<Payment> findByName(EPayment ePayment);

}
