package impl;


import exceptions.ExceptionCode;
import exceptions.MyException;
import generic.Validator;
import model.Customer;
import repositories.CustomerRepository;

import static exceptions.ExceptionCode.CUSTOMERS;

public class CustomerValidator implements Validator<Customer> {

    @Override
    public void validate(Customer customer) {
        CustomerRepository customerRepository=new CustomerRepositoryImpl();

            final int minAge = 18;
            if (customer.getAge() < minAge) {
                throw new MyException(CUSTOMERS,"AGE IS NOT CORRECT " );

            }

            if (customerRepository.findAll()
                    .stream().anyMatch(x -> x.getName().equals(customer.getName())
                            && x.getSurname().equals(customer.getSurname())
                            && x.getCountry().getName().equals(customer.getCountry().getName()))) {
                throw new MyException(CUSTOMERS, "ADD CUSTOMER EXCEPTION - CUSTOMER IS ALLREADY IN TABLE");
            }

    }


}
