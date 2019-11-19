package main;

import abstractConverter.JsonConverter;
import com.google.gson.annotations.JsonAdapter;
import impl.*;

import service.MenuService;

public class Main {
    public static void main(String[] args) {

       System.out.println(Main.class.getResource(""));

        var categoryRepository = new CategoryRepositoryImpl();
        var countryRepository = new CountryRepositoryImpl();
        var customerOrderRepository = new CustomerOrderRepositoryImpl();
        var customerRepository=new CustomerRepositoryImpl();
        var paymentRepository=new PaymentRepositoryImpl();
        var producerRepository = new ProducerRepositoryImpl();
        var productRepository=new ProductRepositoryImpl();
        var shopRepository=new ShopRepositoryImpl();
        var stockRepository = new StockRepositoryImpl();
        var tradeRepository=new TradeRepositoryImpl();
        var errorsRepository = new ErrorsRepositoryImpl();


        MenuService menuService = new MenuService( categoryRepository, countryRepository,
                customerOrderRepository, customerRepository, paymentRepository, producerRepository,
                productRepository, shopRepository, stockRepository, tradeRepository, errorsRepository);

        menuService.mainMenuManagement();

    }
}
