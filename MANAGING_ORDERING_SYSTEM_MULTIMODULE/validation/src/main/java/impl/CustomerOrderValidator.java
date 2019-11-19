package impl;

import exceptions.MyException;
import generic.Validator;
import model.CustomerOrder;
import model.Stock;
import repositories.StockRepository;

import static exceptions.ExceptionCode.CUSTOMER_ORDERS;
import static exceptions.ExceptionCode.STOCKS;

public class CustomerOrderValidator implements Validator<CustomerOrder> {
    @Override
    public void validate(CustomerOrder customerOrder) {

        StockRepository stockRepository = new StockRepositoryImpl();

        if (stockRepository
                .findAll()
                .stream()
                .noneMatch(x -> x.getProduct().getName()
                        .equals(customerOrder.getProduct().getName())
                        && x.getProduct().getCategory().getName()
                        .equals(customerOrder.getProduct().getCategory().getName()))) {
            throw new MyException(CUSTOMER_ORDERS, "PRODUCT IN ORDER IS OUT OF STOCKROOM");
        }
        Stock toChange = stockRepository
                .findAll()
                .stream()
                .filter(x -> x.getProduct().getName().equals(customerOrder.getProduct().getName())
                        && x.getProduct().getCategory().getName().equals(customerOrder.getProduct().getCategory().getName()))
                .findFirst()
                .orElseThrow(() -> new MyException(STOCKS, "PRODUCT OUT OF STOCKROOM"));
        System.out.println(toChange);
        toChange.setQuantity(toChange.getQuantity() - customerOrder.getQuantity());
        if (toChange.getQuantity() < 0) {
            throw new MyException(STOCKS, "ORDER PRODUCT QUANTITY ARE BIGGER THEN ACTUAL STOCK QUANTITY ");
        }
        System.out.println(toChange);
        stockRepository
                .addOrUpdate(stockRepository
                        .findById(toChange.getId())
                        .orElseThrow(() -> new MyException(STOCKS, "PRODUCT OUT OF STOCKROOM")));
    }
}
