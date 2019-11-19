package service;

import dto.mapper.ModelMapper;
import exceptions.ExceptionCode;
import exceptions.MyException;
import impl.*;
import lombok.RequiredArgsConstructor;
import repositories.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
class DateBaseInitializationService {

    private static final String FOLDER_NAME = "service/src/main/resources/json/";

    private final AddingToDateBaseService addingToDateBaseService;
    private final CategoryRepository categoryRepository;
    private final CountryRepository countryRepository;
    private final CustomerOrderRepository customerOrderRepository;
    private final CustomerRepository customerRepository;
    private final PaymentRepository paymentRepository;
    private final ProducerRepository producerRepository;
    private final ProductRepository productRepository;
    private final ShopRepository shopRepository;
    private final StockRepository stockRepository;
    private final TradeRepository tradeRepository;
    private final ErrorsRepository errorsRepository;


    void deleteAll() {

        categoryRepository.deleteAll();
        countryRepository.deleteAll();
        customerOrderRepository.deleteAll();
        customerRepository.deleteAll();
        paymentRepository.deleteAll();
        producerRepository.deleteAll();
        productRepository.deleteAll();
        shopRepository.deleteAll();
        stockRepository.deleteAll();
        tradeRepository.deleteAll();
        errorsRepository.deleteAll();
    }

    void fromJsonDBinitialization() {
//CATEGORY
        new CategoryListJsonConverter(FOLDER_NAME.concat("categoryIni.json"))
                .fromJson()
                .ifPresent(x -> x.forEach(categoryRepository::addOrUpdate));

//COUNTRY
        new CountryListJsonConverter(FOLDER_NAME.concat("countryIni.json"))
                .fromJson()
                .ifPresent(x -> x.forEach(countryRepository::addOrUpdate));
//SHOP
        new ShopListJsonConverter(FOLDER_NAME.concat("shopIni.json"))
                .fromJson()
                .ifPresent(x -> x.forEach(c -> addingToDateBaseService.addShopWithCountry(ModelMapper.fromShopToshopDto(c))));

//PRODUCER
        new ProducerListJsonConverter(FOLDER_NAME.concat("producerIni.json"))
                .fromJson()
                .ifPresent(x -> x.forEach(c -> addingToDateBaseService.addProducerWithCountryAndTrade(ModelMapper.fromProducerToProducerDto(c))));

//CUSTOMER
        new CustomerListJsonConverter(FOLDER_NAME.concat("customerIni.json"))
                .fromJson()
                .ifPresent(x -> x.forEach(c -> addingToDateBaseService.addCustomerWithCountry(ModelMapper.fromCustomerToCustomerDto(c))));

//PRODUCTS
        new ProductListJsonConverter(FOLDER_NAME.concat("productIni.json"))
                .fromJson()
                .ifPresent(x -> x.forEach(c -> addingToDateBaseService.addProductWithCategoryAndProducer(ModelMapper.fromProductToProductDto(c))));


//STOCK
        new StockListJsonConverter(FOLDER_NAME.concat("stockIni.json"))
                .fromJson()
                .ifPresent(x -> x.forEach(c -> addingToDateBaseService.addStockWithProductAndCategory(ModelMapper.fromStockToStockDto(c))));

//CUSTOMER ORDER

        new CustomerOrderListJSonConverter(FOLDER_NAME.concat("customerOrderIni.json"))
                .fromJson()
                .ifPresent(x -> x.forEach(c -> addingToDateBaseService.addCustomerOrderWithCustomerCategoryPayment(ModelMapper.fromCustomerOrderToCustomerOrderDto(c))));


//ERRORS
        new ErrorListJsonConverter(FOLDER_NAME.concat("errorsIni.json"))
                .fromJson()
                .ifPresent(x -> x.forEach(c -> addingToDateBaseService.addErrors(ModelMapper.fromErrorsToErrorsDto(c))));
    }
}
