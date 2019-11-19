package service;

import dto.*;
import dto.mapper.ModelMapper;
import exceptions.ExceptionCode;
import exceptions.MyException;
import impl.*;
import model.*;
import org.hibernate.mapping.Any;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.mockito.stubbing.Answer;
import repositories.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.any;


@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class AddingToDateBaseTest {


    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private PaymentRepository paymentRepository;
    @Mock
    private ProducerRepository producerRepository;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private ShopRepository shopRepository;
    @Mock
    private StockRepository stockRepository;
    @Mock
    private TradeRepository tradeRepository;
    @Mock
    private ErrorsRepository errorsRepository;
    @Mock
    private CountryRepository countryRepository;

    @InjectMocks
    private AddingToDateBaseService addingToDateBaseService;

    @InjectMocks
    StatisticService statisticService;

    private static List<CustomerOrder> customerOrderList;
    private static List<Stock> stockList;
    private static List<Producer> producerList;
    private static List<Errors> errorsList;
    private static List<Country> countryList;
    private static List<Customer> customerList;
    private static List<Shop> shopList;

    @BeforeAll
    static void give_value_to_the_fields() {

        customerOrderList = new CustomerOrderListJSonConverter(
                "src/main/resources/json/toTestLists/customerOrder.json")
                .fromJson().orElseThrow(() -> new MyException(ExceptionCode.JSON, "TEST JSON FILE EXCEPTION"));
        stockList = new StockListJsonConverter(
                "src/main/resources/json/toTestLists/stock.json")
                .fromJson().orElseThrow(() -> new MyException(ExceptionCode.JSON, "TEST JSON FILE EXCEPTION"));
        producerList = new ProducerListJsonConverter(
                "src/main/resources/json/toTestLists/producer.json")
                .fromJson().orElseThrow(() -> new MyException(ExceptionCode.JSON, "TEST JSON FILE EXCEPTION"));
        errorsList = new ErrorListJsonConverter(
                "src/main/resources/json/toTestLists/errors.json")
                .fromJson().orElseThrow(() -> new MyException(ExceptionCode.JSON, "TEST JSON FILE EXCEPTION"));
        customerList = new CustomerListJsonConverter(
                "src/main/resources/json/toTestLists/customer.json")
                .fromJson().orElseThrow(() -> new MyException(ExceptionCode.JSON, "TEST JSON FILE EXCEPTION"));
        countryList = new CountryListJsonConverter(
                "src/main/resources/json/toTestLists/country.json")
                .fromJson().orElseThrow(() -> new MyException(ExceptionCode.JSON, "TEST JSON FILE EXCEPTION"));

        shopList = new ShopListJsonConverter(
                "src/main/resources/json/toTestLists/shop.json")
                .fromJson().orElseThrow(() -> new MyException(ExceptionCode.JSON, "TEST JSON FILE EXCEPTION"));
    }

    @Test
    @DisplayName("test of addCustomerWithCountry - throws exception if customerDto is null ")
    public void test1() {


        MyException ex = Assertions.assertThrows(
                MyException.class,
                () -> addingToDateBaseService.addCustomerWithCountry(ModelMapper
                        .fromCustomerToCustomerDto(null)));

        Assertions.assertEquals("ADD CUSTOMER EXCEPTION - CUSTOMER IS NULL", ex.getExceptionInfo().getMessage());
    }

    @Test
    @DisplayName("test of addCustomerWithCountry - throws exception if  customerDto.getCountryDto() is null")
    public void test2() {

        MyException ex = Assertions.assertThrows(
                MyException.class,
                () -> addingToDateBaseService.addCustomerWithCountry(CustomerDto
                        .builder()
                        .countryDto(null)
                        .build()));

        Assertions.assertEquals("ADD CUSTOMER EXCEPTION - COUNTRY IS NULL", ex.getExceptionInfo().getMessage());
    }

    @Test
    @DisplayName("test of addCustomerWithCountry - empty find by name")
    public void test3() {

        Mockito.when(countryRepository.findByName(anyString()))
                .thenReturn(Optional.empty());

        Mockito.when(countryRepository.addOrUpdate(any(Country.class)))
                .thenReturn(Optional.of(Country.builder().build()));

        Mockito.when(customerRepository.addOrUpdate(any(Customer.class)))
                .thenReturn(Optional.of(Customer.builder().build()));

        Mockito.when(customerRepository.findLast())
                .thenReturn(Optional.of(Customer.builder().build()));

        Assertions.assertNotNull(addingToDateBaseService
                .addCustomerWithCountry(ModelMapper.fromCustomerToCustomerDto(customerList.get(0))));
    }


    @DisplayName("test of addCustomerWithCountry - countryRepository.addOrUpdate empty")
    @Test
    public void test4() {

        Mockito.when(countryRepository.findByName(anyString()))
                .thenReturn(Optional.empty());

        Mockito.when(countryRepository.addOrUpdate(any(Country.class)))
                .thenReturn(Optional.empty());

        Mockito.when(customerRepository.addOrUpdate(any(Customer.class)))
                .thenReturn(Optional.empty());

        Mockito.when(customerRepository.findLast())
                .thenReturn(Optional.empty());
        MyException ex = Assertions.assertThrows(MyException.class, () -> addingToDateBaseService
                .addCustomerWithCountry(ModelMapper.fromCustomerToCustomerDto(customerList.get(0))));

        Assertions.assertEquals("ADD CUSTOMER EXCEPTION", ex.getExceptionInfo().getMessage());

    }

    @DisplayName("test of addCustomerWithCountry - customerRepository.findLast empty")
    @Test
    public void test5() {

        Mockito.when(countryRepository.findByName(anyString()))
                .thenReturn(Optional.empty());

        Mockito.when(countryRepository.addOrUpdate(any(Country.class)))
                .thenReturn(Optional.of(Country.builder().build()));

        Mockito.when(customerRepository.addOrUpdate(any(Customer.class)))
                .thenReturn(Optional.empty());

        Mockito.when(customerRepository.findLast())
                .thenReturn(Optional.empty());
        MyException ex = Assertions.assertThrows(MyException.class, () -> addingToDateBaseService
                .addCustomerWithCountry(ModelMapper.fromCustomerToCustomerDto(customerList.get(0))));

        Assertions.assertEquals("CUSTOMER LAST FIND EXCEPTION", ex.getExceptionInfo().getMessage());

    }

    @DisplayName("test of addCustomerWithCountry - not empty every repository method")
    @Test
    public void test6() {

        Mockito.when(countryRepository.findByName(anyString()))
                .thenReturn(Optional.of(Country.builder().build()));

        Mockito.when(countryRepository.addOrUpdate(any(Country.class)))
                .thenReturn(Optional.of(Country.builder().build()));

        Mockito.when(customerRepository.addOrUpdate(any(Customer.class)))
                .thenReturn(Optional.of(Customer.builder().build()));

        Mockito.when(customerRepository.findLast())
                .thenReturn(Optional.of(Customer.builder().build()));

        Assertions.assertDoesNotThrow(() -> addingToDateBaseService
                .addCustomerWithCountry(ModelMapper.fromCustomerToCustomerDto(customerList.get(0))));

    }

    @Test
    @DisplayName("test of addShopWithCountry - throws exception if shopDto is null ")
    public void test7() {


        MyException ex = Assertions.assertThrows(
                MyException.class,
                () -> addingToDateBaseService.addShopWithCountry(ModelMapper
                        .fromShopToshopDto(null)));

        Assertions.assertEquals("ADD SHOP IS NULL EXCEPTION", ex.getExceptionInfo().getMessage());
    }

    @Test
    @DisplayName("test of addShopWithCountry - throws exception if  customerDto.getCountryDto() is null")
    public void test8() {


        MyException ex = Assertions.assertThrows(
                MyException.class,
                () -> addingToDateBaseService.addShopWithCountry(ShopDto
                        .builder()
                        .countryDto(null)
                        .build()));

        Assertions.assertEquals("ADD SHOP - COUNTRY NULL EXCEPTION", ex.getExceptionInfo().getMessage());
    }

    @Test
    @DisplayName("test of addShopWithCountry - throws exception if  shopDto.getCountryDto() is null")
    public void test9() {

        MyException ex = Assertions.assertThrows(
                MyException.class,
                () -> addingToDateBaseService.addShopWithCountry(ShopDto
                        .builder()
                        .countryDto(CountryDto
                                .builder()
                                .id(null)
                                .build())
                        .build()));

        Assertions.assertEquals("ADD SHOP - COUNTRY ID OR NAME NULL EXCEPTION", ex.getExceptionInfo().getMessage());
    }


    @Test
    @DisplayName("test of addShopWithCountry - throws exception if  shopDto.getCountryDto() is null")
    public void test10() {

        MyException ex = Assertions.assertThrows(
                MyException.class,
                () -> addingToDateBaseService.addShopWithCountry(ShopDto
                        .builder()
                        .countryDto(CountryDto
                                .builder()
                                .name(null)
                                .build())
                        .build()));

        Assertions.assertEquals("ADD SHOP - COUNTRY ID OR NAME NULL EXCEPTION", ex.getExceptionInfo().getMessage());
    }

    @Test
    @DisplayName("test of addShopWithCountry - empty find by name")
    public void test11() {

        Mockito.when(countryRepository.findByName(anyString()))
                .thenReturn(Optional.empty());

        Mockito.when(countryRepository.addOrUpdate(any(Country.class)))
                .thenReturn(Optional.of(Country.builder().build()));

        Mockito.when(shopRepository.addOrUpdate(any(Shop.class)))
                .thenReturn(Optional.of(Shop.builder().build()));

        Mockito.when(shopRepository.findLast())
                .thenReturn(Optional.of(Shop.builder().build()));

        Assertions.assertNotNull(addingToDateBaseService
                .addShopWithCountry(ModelMapper.fromShopToshopDto(shopList.get(0))));
    }

    @DisplayName("test of addShopWithCountry - countryRepository.addOrUpdate empty")
    @Test
    public void test12() {

        Mockito.when(countryRepository.findByName(anyString()))
                .thenReturn(Optional.empty());

        Mockito.when(countryRepository.addOrUpdate(any(Country.class)))
                .thenReturn(Optional.empty());

        Mockito.when(shopRepository.addOrUpdate(any(Shop.class)))
                .thenReturn(Optional.of(Shop.builder().build()));

        Mockito.when(shopRepository.findLast())
                .thenReturn(Optional.of(Shop.builder().build()));

        MyException ex = Assertions.assertThrows(
                MyException.class,
                () -> addingToDateBaseService.addShopWithCountry(ModelMapper.fromShopToshopDto(shopList.get(0))));

        Assertions.assertEquals("ADD SHOP - COUNTRY EXCEPTION", ex.getExceptionInfo().getMessage());

    }

    @DisplayName("test of addShopWithCountry - shopRepository.addOrUpdate empty")
    @Test
    public void test13() {

        Mockito.when(countryRepository.findByName(anyString()))
                .thenReturn(Optional.empty());

        Mockito.when(countryRepository.addOrUpdate(any(Country.class)))
                .thenReturn(Optional.of(Country.builder().build()));

        Mockito.when(shopRepository.addOrUpdate(any(Shop.class)))
                .thenReturn(Optional.empty());

        Mockito.when(shopRepository.findLast())
                .thenReturn(Optional.of(Shop.builder().build()));

        Assertions.assertNotNull(addingToDateBaseService
                .addShopWithCountry(ModelMapper.fromShopToshopDto(shopList.get(0))));

    }

    @DisplayName("test of addShopWithCountry - shopRepository.findLast empty")
    @Test
    public void test14() {

        Mockito.when(countryRepository.findByName(anyString()))
                .thenReturn(Optional.empty());

        Mockito.when(countryRepository.addOrUpdate(any(Country.class)))
                .thenReturn(Optional.of(Country.builder().build()));

        Mockito.when(shopRepository.addOrUpdate(any(Shop.class)))
                .thenReturn(Optional.empty());

        Mockito.when(shopRepository.findLast())
                .thenReturn(Optional.empty());

        MyException ex = Assertions.assertThrows(
                MyException.class,
                () -> addingToDateBaseService.addShopWithCountry(ModelMapper.fromShopToshopDto(shopList.get(0))));

        Assertions.assertEquals("CUSTOMER LAST FIND EXCEPTION", ex.getExceptionInfo().getMessage());
    }

    @Test
    @DisplayName("test of addProducerWithCountryAndTrade - throws exception if  producerDto is null")
    public void test15() {

        MyException ex = Assertions.assertThrows(
                MyException.class,
                () -> addingToDateBaseService.addProducerWithCountryAndTrade(null));

        Assertions.assertEquals("ADD PRODUCER EXCEPTION - PRODUCER IS NULL", ex.getExceptionInfo().getMessage());
    }

    @Test
    @DisplayName("test of addProducerWithCountryAndTrade - throws exception if  ProducerDto.countryDto is null")
    public void test16() {

        MyException ex = Assertions.assertThrows(
                MyException.class,
                () -> addingToDateBaseService.addProducerWithCountryAndTrade(ProducerDto.builder()
                        .countryDto(null)
                        .build()));

        Assertions.assertEquals("ADD PRODUCER EXCEPTION - COUNTRY IS NULL", ex.getExceptionInfo().getMessage());
    }

    @Test
    @DisplayName("test of addProducerWithCountryAndTrade - throws exception if  ProducerDto.countryDto.id is null")
    public void test17() {

        MyException ex = Assertions.assertThrows(
                MyException.class,
                () -> addingToDateBaseService.addProducerWithCountryAndTrade(ProducerDto.builder()
                        .countryDto(CountryDto
                                .builder()
                                .id(null)
                                .name(null)
                                .build())
                        .tradeDto(TradeDto.builder().build())
                        .build()));

        Assertions.assertEquals("ADD PRODUCER COUNTRY ID OR NAME IS NULL EXCEPTION", ex.getExceptionInfo().getMessage());
    }

    @Test
    @DisplayName("test of addProducerWithCountryAndTrade - throws exception if  ProducerDto.countryDto.name is null")
    public void test18() {

        MyException ex = Assertions.assertThrows(
                MyException.class,
                () -> addingToDateBaseService.addProducerWithCountryAndTrade(ProducerDto.builder()
                        .countryDto(CountryDto
                                .builder()
                                .name(null)
                                .build())
                        .tradeDto(TradeDto.builder().build())
                        .build()));

        Assertions.assertEquals("ADD PRODUCER COUNTRY ID OR NAME IS NULL EXCEPTION", ex.getExceptionInfo().getMessage());
    }

    @Test
    @DisplayName("test of addProductWithCategoryAndProducer - throws exception if  ProductDto is null")
    public void test19() {

        MyException ex = Assertions.assertThrows(
                MyException.class,
                () -> addingToDateBaseService.addProductWithCategoryAndProducer(null));

        Assertions.assertEquals("ADD PRODUCT WITH CATEGORY AND PRODUCER EXCEPTION", ex.getExceptionInfo().getMessage());
    }

    @Test
    @DisplayName("test of addProductWithCategoryAndProducer - throws exception if CategoryDto is null")
    public void test20() {

        MyException ex = Assertions.assertThrows(
                MyException.class,
                () -> addingToDateBaseService.addProductWithCategoryAndProducer(ProductDto.builder()
                        .categoryDto(null)
                        .build()));

        Assertions.assertEquals("ADD PRODUCT EXCEPTION - CATEGORY IS NULL", ex.getExceptionInfo().getMessage());
    }

    @Test
    @DisplayName("test of addProductWithCategoryAndProducer - throws exception if producerDto is null")
    public void test21() {

        MyException ex = Assertions.assertThrows(
                MyException.class,
                () -> addingToDateBaseService.addProductWithCategoryAndProducer(ProductDto.builder()
                        .producerDto(null)
                        .build()));

        Assertions.assertEquals("ADD PRODUCT EXCEPTION - CATEGORY IS NULL", ex.getExceptionInfo().getMessage());
    }

    @Test
    @DisplayName("test of addProductWithCategoryAndProducer - throws exception if enumGuaranteeComponents is null")
    public void test22() {

        MyException ex = Assertions.assertThrows(
                MyException.class,
                () -> addingToDateBaseService.addProductWithCategoryAndProducer(ProductDto.builder()
                        .enumGuaranteeComponents(null)
                        .categoryDto(CategoryDto.builder().build())
                        .producerDto(ProducerDto.builder().build())
                        .build()));

        Assertions.assertEquals("ADD PRODUCT EXCEPTION - GUARANTEE_COMPONENTS ARE NULL", ex.getExceptionInfo().getMessage());
    }

    @Test
    @DisplayName("test of addProductWithCategoryAndProducer - throws exception if categoryDto id and name is null")
    public void test23() {

        MyException ex = Assertions.assertThrows(
                MyException.class,
                () -> addingToDateBaseService.addProductWithCategoryAndProducer(ProductDto.builder()
                        .categoryDto(CategoryDto
                                .builder()
                                .id(null)
                                .name(null)
                                .build())
                        .producerDto(ProducerDto.builder().build())
                        .categoryDto(CategoryDto.builder().build())
                        .enumGuaranteeComponents(Set.of())
                        .build()));

        Assertions.assertEquals("ADD PRODUCT CATEGORY ID OR NAME IS NULL EXCEPTION", ex.getExceptionInfo().getMessage());
    }

    @Test
    @DisplayName("test of addStockWithProductAndCategory - throws exception if stockDto is null")
    public void test25() {

        MyException ex = Assertions.assertThrows(
                MyException.class,
                () -> addingToDateBaseService.addStockWithProductAndCategory(null));

        Assertions.assertEquals("ADD STOCK IS NULL EXCEPTION", ex.getExceptionInfo().getMessage());
    }

    @Test
    @DisplayName("test of addStockWithProductAndCategory - throws exception if productDto is null")
    public void test26() {

        MyException ex = Assertions.assertThrows(
                MyException.class,
                () -> addingToDateBaseService.addStockWithProductAndCategory(StockDto.builder()
                        .product(null)
                        .build()));

        Assertions.assertEquals("ADD STOCK PRODUCT IS NULL EXCEPTION", ex.getExceptionInfo().getMessage());
    }

    @Test
    @DisplayName("test of addStockWithProductAndCategory - throws exception if shopDto is null")
    public void test27() {

        MyException ex = Assertions.assertThrows(
                MyException.class,
                () -> addingToDateBaseService.addStockWithProductAndCategory(StockDto.builder()
                        .shopDto(null)
                        .product(ProductDto.builder().build())
                        .build()));

        Assertions.assertEquals("ADD STOCK SHOP IS NULL EXCEPTION", ex.getExceptionInfo().getMessage());
    }


    @Test
    @DisplayName("test of addCustomerOrderWithCustomerCategoryPayment - throws exception if customerOrderDto is null")
    public void test30() {

        MyException ex = Assertions.assertThrows(
                MyException.class,
                () -> addingToDateBaseService.addCustomerOrderWithCustomerCategoryPayment(null));

        Assertions.assertEquals("ADD CUSTOMER ORDER EXCEPTION - CUSTOMER ORDER IS NULL", ex.getExceptionInfo().getMessage());
    }

    @Test
    @DisplayName("test of addCustomerOrderWithCustomerCategoryPayment - throws exception if paymentDto is null")
    public void test31() {

        MyException ex = Assertions.assertThrows(
                MyException.class,
                () -> addingToDateBaseService.addCustomerOrderWithCustomerCategoryPayment(CustomerOrderDto
                        .builder()
                        .paymentDto(null)
                        .build()));

        Assertions.assertEquals("ADD CUSTOMER ORDER EXCEPTION - PAYMENT IS NULL", ex.getExceptionInfo().getMessage());
    }

    @Test
    @DisplayName("test of addCustomerOrderWithCustomerCategoryPayment - throws exception if ProductDto is null")
    public void test32() {

        MyException ex = Assertions.assertThrows(
                MyException.class,
                () -> addingToDateBaseService.addCustomerOrderWithCustomerCategoryPayment(CustomerOrderDto
                        .builder()
                        .productDto(null)
                        .paymentDto(PaymentDto.builder().build())
                        .build()));

        Assertions.assertEquals("ADD CUSTOMER ORDER EXCEPTION - PRODUCT IS NULL", ex.getExceptionInfo().getMessage());
    }

    @Test
    @DisplayName("test of addCustomerOrderWithCustomerCategoryPayment - throws exception if customerDto are null")
    public void test33() {

        MyException ex = Assertions.assertThrows(
                MyException.class,
                () -> addingToDateBaseService.addCustomerOrderWithCustomerCategoryPayment(CustomerOrderDto
                        .builder()
                        .customerDto(null)
                        .paymentDto(PaymentDto.builder().build())
                        .productDto(ProductDto.builder().build())
                        .build()));

        Assertions.assertEquals("ADD CUSTOMER ORDER EXCEPTION -  CUSTOMER IS NULL", ex.getExceptionInfo().getMessage());
    }



    @Test
    @DisplayName("test of addErrors - throws exception if errorsDto are null")
    public void test37() {

        MyException ex = Assertions.assertThrows(
                MyException.class,
                () -> addingToDateBaseService.addErrors(null));

        Assertions.assertEquals("ADD ERRORS EXCEPTION", ex.getExceptionInfo().getMessage());
    }
}



