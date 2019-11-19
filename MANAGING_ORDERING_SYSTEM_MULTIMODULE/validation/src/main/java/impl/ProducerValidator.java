package impl;


import exceptions.MyException;
import generic.Validator;
import model.Producer;
import repositories.ProducerRepository;

import static exceptions.ExceptionCode.PRODUCERS;

public class ProducerValidator  implements Validator<Producer> {

    @Override
    public void validate(Producer producer) {
        ProducerRepository producerRepository = new ProducerRepositoryImpl();

        if (producerRepository.findAll()
                .stream().anyMatch(x -> x.getName().equals(producer.getName())
                        && x.getCountry().getName().equals(producer.getCountry().getName()))) {
            throw new MyException(PRODUCERS, "ADD PRODUCER EXCEPTION - PRODUCER IS ALLREADY IN TABLE");
        }
    }
}
