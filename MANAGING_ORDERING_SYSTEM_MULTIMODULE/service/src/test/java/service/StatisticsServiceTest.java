package service;

import abstractConverter.JsonConverter;
import dto.CountryDto;
import dto.mapper.ModelMapper;
import dto.TradeDto;
import exceptions.ExceptionCode;
import exceptions.MyException;
import impl.*;
import model.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import repositories.*;
import utils.EnumGuaranteeComponents;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static utils.EnumGuaranteeComponents.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class StatisticsServiceTest {
    @Mock
    private CountryRepository countryRepository;
    @Mock
    private CustomerOrderRepository customerOrderRepository;
    @Mock
    private ProducerRepository producerRepository;
    @Mock
    private StockRepository stockRepository;
    @Mock
    private ErrorsRepository errorsRepository;

    @InjectMocks
    StatisticService statisticService;

    private static List<CustomerOrder> customerOrderList;
    private static List<Stock> stockList;
    private static List<Producer> producerList;
    private static List<Errors> errorsList;

    @BeforeAll
    static void give_value_to_the_fields() {

        customerOrderList = new CustomerOrderListJSonConverter(
                "src/main/resources/json/toTestLists/customerOrder.json")
                .fromJson().orElseThrow(()->new MyException(ExceptionCode.JSON,"TEST JSON FILE EXCEPTION"));
        stockList = new StockListJsonConverter(
                "src/main/resources/json/toTestLists/stock.json")
                .fromJson().orElseThrow(()->new MyException(ExceptionCode.JSON,"TEST JSON FILE EXCEPTION"));
        producerList = new ProducerListJsonConverter(
                "src/main/resources/json/toTestLists/producer.json")
                .fromJson().orElseThrow(()->new MyException(ExceptionCode.JSON,"TEST JSON FILE EXCEPTION"));
        errorsList = new ErrorListJsonConverter(
                "src/main/resources/json/toTestLists/errors.json")
                .fromJson().orElseThrow(()->new MyException(ExceptionCode.JSON,"TEST JSON FILE EXCEPTION"));

    }

    @Test
    @DisplayName("test of productsFilteredByAgeCountry - throws exception if countryDto is null ")
    public void test1() {

        MyException ex = Assertions.assertThrows(
                MyException.class,
                () -> statisticService.productsFilteredByAgeCountry(null, 10, 20));

        Assertions.assertEquals("DATA FROM USER ARE NULL OR NOT VALIDATE", ex.getExceptionInfo().getMessage());
    }

    @Test
    @DisplayName("test of productsFilteredByAgeCountry - throws exception if ageMin is bigger then ageMax")
    public void test2() {
        CountryDto countryDto = CountryDto
                .builder().name("COUNTRY").build();
        int ageMin = 20;
        int ageMax = 10;

        MyException ex = Assertions.assertThrows(
                MyException.class,
                () -> statisticService.productsFilteredByAgeCountry(countryDto, ageMin, ageMax));

        Assertions.assertEquals("DATA FROM USER ARE NULL OR NOT VALIDATE", ex.getExceptionInfo().getMessage());
    }

    @Test
    @DisplayName("test of productsFilteredByAgeCountry - not throws exception if ageMin is equal ageMax")
    public void test3() {
        CountryDto countryDto = CountryDto
                .builder().name("COUNTRY").build();
        int ageMin = 10;
        int ageMax = 10;

        Assertions.assertDoesNotThrow(
                () -> statisticService.productsFilteredByAgeCountry(countryDto, ageMin, ageMax));
    }

    @Test
    @DisplayName("test of productsFilteredByAgeCountry - list should return one position country filter check")
    public void test4() {
        CountryDto countryDto = CountryDto
                .builder().name("COUNTRY").build();
        int ageMin = 10;
        int ageMax = 20;

        Mockito.when(customerOrderRepository
                .findAll())
                .thenReturn(customerOrderList);
        Assertions.assertEquals(1, statisticService.productsFilteredByAgeCountry(countryDto, ageMin, ageMax).size());
    }

    @Test
    @DisplayName("test of productsFilteredByAgeCountry - list should return two position country filter check")
    public void test5() {
        CountryDto countryDto = CountryDto
                .builder().name("COUNTRY").build();
        int ageMin = 10;
        int ageMax = 20;

        List<CustomerOrder> customerOrderListEdited = new ArrayList<>(customerOrderList);

        customerOrderListEdited.add(
                CustomerOrder
                        .builder()
                        .customer(
                                Customer
                                        .builder()
                                        .age(10)
                                        .country(Country
                                                .builder()
                                                .id(0L)
                                                .name("COUNTRY")
                                                .build())
                                        .build())
                        .build());

        Mockito.when(customerOrderRepository
                .findAll())
                .thenReturn(customerOrderListEdited);
        Assertions.assertEquals(2, statisticService.productsFilteredByAgeCountry(countryDto, ageMin, ageMax).size());
    }

    @Test
    @DisplayName("test of productsFilteredByAgeCountry - list should return one position age country filter check")
    public void test6() {

        int ageMin = 25;
        int ageMax = 35;

        List<CustomerOrder> customerOrderListEdited = new ArrayList<>(customerOrderList);
        CountryDto countryDto = CountryDto
                .builder().name("COUNTRY").build();
        customerOrderListEdited.add(
                CustomerOrder
                        .builder()
                        .customer(
                                Customer
                                        .builder()
                                        .age(30)
                                        .country(Country
                                                .builder()
                                                .id(0L)
                                                .name("COUNTRY")
                                                .build())
                                        .build())
                        .build());

        Mockito.when(customerOrderRepository
                .findAll())
                .thenReturn(customerOrderListEdited);
        Assertions.assertEquals(1, statisticService.productsFilteredByAgeCountry(countryDto, ageMin, ageMax).size());
    }

    @Test
    @DisplayName("test - productsAndCategoryRosterByComponents - throws exception if guaranteeComponentsFromUser is null")
    void test7() {

        MyException ex = Assertions.assertThrows(
                MyException.class,
                () -> statisticService.productsAndCategoryRosterByComponents(null));

        Assertions.assertEquals("DATA FROM USER ARE NULL OR NOT VALIDATE", ex.getExceptionInfo().getMessage());
    }

    @Test
    @DisplayName("test - productsAndCategoryRosterByComponents - throws exception if guaranteeComponentsFromUser is empty")
    void test8() {
        MyException ex = Assertions.assertThrows(
                MyException.class,
                () -> statisticService.productsAndCategoryRosterByComponents(Collections.emptySet()));

        Assertions.assertEquals("DATA FROM USER ARE NULL OR NOT VALIDATE", ex.getExceptionInfo().getMessage());
    }

    @Test
    @DisplayName("test - productsAndCategoryRosterByComponents - should return two positions checking filter function components")
    void test9() {
        Set<EnumGuaranteeComponents> guaranteeComponents = Set.of(MONEY_BACK, SERVICE, HELP_DESK, EXCHANGE);

        Mockito.when(customerOrderRepository
                .findAll())
                .thenReturn(customerOrderList);

        int result = statisticService.productsAndCategoryRosterByComponents(guaranteeComponents).size();

        Assertions.assertEquals(2, result);
    }

    @Test
    @DisplayName("test - productsAndCategoryRosterByComponents - throws exception if customerOrderRepository findAll is null")
    void test10() {
        Set<EnumGuaranteeComponents> guaranteeComponents = Set.of(MONEY_BACK, SERVICE, HELP_DESK, EXCHANGE);

        Mockito.when(customerOrderRepository
                .findAll())
                .thenReturn(null);

        MyException ex = Assertions.assertThrows(
                MyException.class,
                () -> statisticService.productsAndCategoryRosterByComponents(guaranteeComponents));

        Assertions.assertEquals("CUSTOMER ORDER REPOSITORY LIST ARE NULL OR NOT VALIDATE", ex.getExceptionInfo().getMessage());
    }

    @Test
    @DisplayName("test - productsAndCategoryRosterByComponents - throws exception if customerOrderRepository findAll is empty")
    void test11() {
        Set<EnumGuaranteeComponents> guaranteeComponents = Set.of(MONEY_BACK, SERVICE, HELP_DESK, EXCHANGE);

        Mockito.when(customerOrderRepository
                .findAll())
                .thenReturn(Collections.emptyList());

        MyException ex = Assertions.assertThrows(
                MyException.class,
                () -> statisticService.productsAndCategoryRosterByComponents(guaranteeComponents));

        Assertions.assertEquals("CUSTOMER ORDER REPOSITORY LIST ARE NULL OR NOT VALIDATE", ex.getExceptionInfo().getMessage());
    }

    @Test
    @DisplayName("test - shopsByCountryAndProductCountryList - throws exception if stockRepository findAll is empty")
    void test12() {

        Mockito.when(stockRepository
                .findAll())
                .thenReturn(Collections.emptyList());

        MyException ex = Assertions.assertThrows(
                MyException.class,
                () -> statisticService.shopsByCountryAndProductCountryList());

        Assertions.assertEquals("STOCK REPOSITORY LIST ARE NULL OR EMPTY", ex.getExceptionInfo().getMessage());
    }

    @Test
    @DisplayName("test - shopsByCountryAndProductCountryList - throws exception if stockRepository findAll is null")
    void test13() {

        Mockito.when(stockRepository
                .findAll())
                .thenReturn(null);

        MyException ex = Assertions.assertThrows(
                MyException.class,
                () -> statisticService.shopsByCountryAndProductCountryList());

        Assertions.assertEquals("STOCK REPOSITORY LIST ARE NULL OR EMPTY", ex.getExceptionInfo().getMessage());
    }

    @Test
    @DisplayName("test - shopsByCountryAndProductCountryList - should return two positions checking filter country function")
    void test14() {

        Mockito.when(stockRepository
                .findAll())
                .thenReturn(stockList);

        int result = statisticService.shopsByCountryAndProductCountryList().size();

        Assertions.assertEquals(2, result);
    }

    @Test
    @DisplayName("test - filteredProducers - should return two positions checking filter trade function")
    void test15() {

        TradeDto tradeDto = TradeDto.builder().name("TRADE").build();
        Mockito.when(producerRepository
                .findAll())
                .thenReturn(producerList);

        int result = statisticService.filteredProducers(tradeDto, 1).size();

        Assertions.assertEquals(2, result);
    }

    @Test
    @DisplayName("test - filteredProducers - should return two positions checking filter trade and quantity function")
    void test16() {

        TradeDto tradeDto = TradeDto.builder().name("TRADE1").build();

        Mockito.when(producerRepository
                .findAll())
                .thenReturn(producerList);

        int result = statisticService.filteredProducers(tradeDto, 5).size();

        Assertions.assertEquals(1, result);
    }

    @Test
    @DisplayName("test - filteredProducers - should return zero positions checking filter trade and quantity function")
    void test17() {

        TradeDto tradeDto = TradeDto.builder().name("TRADE1").build();

        Mockito.when(producerRepository
                .findAll())
                .thenReturn(producerList);

        int result = statisticService.filteredProducers(tradeDto, 10).size();

        Assertions.assertEquals(0, result);
    }

    @Test
    @DisplayName("test - filteredProducers - throws exception if producerRepository findAll is null")
    void test18() {
        TradeDto tradeDto = TradeDto.builder().name("TRADE").build();
        Mockito.when(producerRepository
                .findAll())
                .thenReturn(null);

        MyException ex = Assertions.assertThrows(
                MyException.class,
                () -> statisticService.filteredProducers(tradeDto, 10));

        Assertions.assertEquals("PRODUCER REPOSITORY LIST ARE NULL OR EMPTY", ex.getExceptionInfo().getMessage());
    }

    @Test
    @DisplayName("test - filteredProducers - throws exception if producerRepository findAll is empty")
    void test19() {
        TradeDto tradeDto = TradeDto.builder().name("TRADE").build();
        Mockito.when(producerRepository
                .findAll())
                .thenReturn(Collections.emptyList());

        MyException ex = Assertions.assertThrows(
                MyException.class,
                () -> statisticService.filteredProducers(tradeDto, 10));

        Assertions.assertEquals("PRODUCER REPOSITORY LIST ARE NULL OR EMPTY", ex.getExceptionInfo().getMessage());
    }

    @Test
    @DisplayName("test - filteredProducers - throws exception if tradeDto is null")
    void test20() {
        TradeDto tradeDto = null;
        Mockito.when(producerRepository
                .findAll())
                .thenReturn(producerList);

        MyException ex = Assertions.assertThrows(
                MyException.class,
                () -> statisticService.filteredProducers(tradeDto, 10));

        Assertions.assertEquals("DATA FROM USER ARE NULL OR NOT VALIDATE", ex.getExceptionInfo().getMessage());
    }

    @Test
    @DisplayName("test - filteredProducers - throws exception if quantity is under zero")
    void test21() {
        TradeDto tradeDto = TradeDto.builder().name("TRADE").build();
        Mockito.when(producerRepository
                .findAll())
                .thenReturn(producerList);

        MyException ex = Assertions.assertThrows(
                MyException.class,
                () -> statisticService.filteredProducers(tradeDto, -10));

        Assertions.assertEquals("DATA FROM USER ARE NULL OR NOT VALIDATE", ex.getExceptionInfo().getMessage());
    }

    @Test
    @DisplayName("test - customerOrderByDateAmount - throws exception if min is null")
    void test22() {
        LocalDate min = null;
        LocalDate max = LocalDate.now();
        BigDecimal amount = new BigDecimal(100);

        Mockito.when(customerOrderRepository
                .findAll())
                .thenReturn(customerOrderList);

        MyException ex = Assertions.assertThrows(
                MyException.class,
                () -> statisticService.customerOrderByDateAmount(min, max, amount));

        Assertions.assertEquals("DATA FROM USER ARE NULL OR NOT VALIDATE", ex.getExceptionInfo().getMessage());
    }

    @Test
    @DisplayName("test - customerOrderByDateAmount - throws exception if min is after max")
    void test23() {
        LocalDate min = LocalDate.now();
        LocalDate max = LocalDate.of(2019, 1, 1);
        BigDecimal amount = new BigDecimal(100);

        Mockito.when(customerOrderRepository
                .findAll())
                .thenReturn(customerOrderList);

        MyException ex = Assertions.assertThrows(
                MyException.class,
                () -> statisticService.customerOrderByDateAmount(min, max, amount));

        Assertions.assertEquals("DATA FROM USER ARE NULL OR NOT VALIDATE", ex.getExceptionInfo().getMessage());
    }

    @Test
    @DisplayName("test - customerOrderByDateAmount - throws exception if amount is under zero")
    void test24() {
        LocalDate min = LocalDate.of(2019, 1, 1);
        LocalDate max = LocalDate.of(2019, 6, 1);
        BigDecimal amount = new BigDecimal(-100);

        Mockito.when(customerOrderRepository
                .findAll())
                .thenReturn(customerOrderList);

        MyException ex = Assertions.assertThrows(
                MyException.class,
                () -> statisticService.customerOrderByDateAmount(min, max, amount));

        Assertions.assertEquals("DATA FROM USER ARE NULL OR NOT VALIDATE", ex.getExceptionInfo().getMessage());
    }

    @Test
    @DisplayName("test - customerOrderByDateAmount - throws exception if customerOrderRepository is null")
    void test25() {
        LocalDate min = LocalDate.of(2019, 1, 1);
        LocalDate max = LocalDate.of(2019, 6, 1);
        BigDecimal amount = new BigDecimal(100);

        Mockito.when(customerOrderRepository
                .findAll())
                .thenReturn(null);

        MyException ex = Assertions.assertThrows(
                MyException.class,
                () -> statisticService.customerOrderByDateAmount(min, max, amount));

        Assertions.assertEquals("CUSTOMER ORDER REPOSITORY LIST ARE NULL OR EMPTY", ex.getExceptionInfo().getMessage());
    }

    @Test
    @DisplayName("test - customerOrderByDateAmount - throws exception if customerOrderRepository is empty")
    void test26() {
        LocalDate min = LocalDate.of(2019, 1, 1);
        LocalDate max = LocalDate.of(2019, 6, 1);
        BigDecimal amount = new BigDecimal(100);

        Mockito.when(customerOrderRepository
                .findAll())
                .thenReturn(Collections.emptyList());

        MyException ex = Assertions.assertThrows(
                MyException.class,
                () -> statisticService.customerOrderByDateAmount(min, max, amount));

        Assertions.assertEquals("CUSTOMER ORDER REPOSITORY LIST ARE NULL OR EMPTY", ex.getExceptionInfo().getMessage());
    }

    @Test
    @DisplayName("test - customerOrderByDateAmount - should return two positions checking filter date order and amount")
    void test27() {

        LocalDate min = LocalDate.of(2018, 1, 1);
        LocalDate max = LocalDate.of(2019, 12, 1);
        BigDecimal amount = new BigDecimal(10);

        Mockito.when(customerOrderRepository
                .findAll())
                .thenReturn(customerOrderList);

        int result = statisticService.customerOrderByDateAmount(min, max, amount).size();

        Assertions.assertEquals(2, result);
    }

    @Test
    @DisplayName("test - customerOrderByDateAmount - should return zero positions checking filter date order and amount")
    void test28() {

        LocalDate min = LocalDate.of(2018, 1, 1);
        LocalDate max = LocalDate.of(2019, 12, 1);
        BigDecimal amount = new BigDecimal(10000);

        Mockito.when(customerOrderRepository
                .findAll())
                .thenReturn(customerOrderList);

        int result = statisticService.customerOrderByDateAmount(min, max, amount).size();

        Assertions.assertEquals(0, result);
    }

    @Test
    @DisplayName("test - customerOrderByDateAmount - should return one positions checking filter date order")
    void test29() {

        LocalDate min = LocalDate.of(2019, 3, 1);
        LocalDate max = LocalDate.of(2019, 12, 1);
        BigDecimal amount = new BigDecimal(10);

        Mockito.when(customerOrderRepository
                .findAll())
                .thenReturn(customerOrderList);

        int result = statisticService.customerOrderByDateAmount(min, max, amount).size();

        Assertions.assertEquals(1, result);
    }

    @Test
    @DisplayName("test - customerOrderByDateAmount - should return zero positions checking filter date order")
    void test30() {

        LocalDate min = LocalDate.of(2019, 7, 1);
        LocalDate max = LocalDate.of(2019, 12, 1);
        BigDecimal amount = new BigDecimal(10);

        Mockito.when(customerOrderRepository
                .findAll())
                .thenReturn(customerOrderList);

        int result = statisticService.customerOrderByDateAmount(min, max, amount).size();

        Assertions.assertEquals(0, result);
    }


    @Test
    @DisplayName("test of mostErrorsTest - should return empty map  ")
    public void test32() {

        Mockito.when(errorsRepository
                .allEntitiesName())
                .thenReturn(List.of());
        Mockito.when(errorsRepository
                .findAll())
                .thenReturn(errorsList);


        Assertions.assertEquals(statisticService.mostErrors().size(), 0);
    }

    @Test
    @DisplayName("test of mostErrorsTest - should return size 1, string CUSTOMER, long value 2  ")
    public void test33() {

        Mockito.when(errorsRepository
                .allEntitiesName())
                .thenReturn(List.of("customer", "customerorder"));
        Mockito.when(errorsRepository
                .findAll())
                .thenReturn(errorsList);

        Assertions.assertEquals(statisticService.mostErrors().size(), 1);
        Assertions.assertTrue(statisticService.mostErrors().entrySet().stream().anyMatch(x -> x.getKey().equals("CUSTOMER")));
        Assertions.assertTrue(statisticService.mostErrors().entrySet().stream().anyMatch(x -> x.getValue() == 2));
    }

    @Test
    @DisplayName("test of mostErrorsTest - should return empty map  ")
    public void test34() {

        Mockito.when(errorsRepository
                .allEntitiesName())
                .thenReturn(List.of("customer", "customerorder"));
        Mockito.when(errorsRepository
                .findAll())
                .thenReturn(List.of());

        Assertions.assertEquals(statisticService.mostErrors().size(), 0);

    }

    @Test
    @DisplayName("test of productStatisticGroupedByProducert  ")
    public void test35() {
        CountryDto countryDto = null;
        String name = "NAME";
        String surname = "SURNAME";

        MyException exception =
                Assertions
                        .assertThrows(MyException.class, () -> statisticService.productStatisticGroupedByProducer(name, surname, countryDto));
        Assertions.assertEquals("NULL NAME OR SURNAME OR COUNTRY DTO - PRODUCT STATISTIC GROUPED BY PRODUCER", exception.getExceptionInfo().getMessage());

    }

    @Test
    @DisplayName("test of productStatisticGroupedByProducert  ")
    public void test36() {
        CountryDto countryDto = CountryDto.builder().name("COUNTRY").build();
        String name = null;
        String surname = "SURNAME";

        MyException exception =
                Assertions
                        .assertThrows(MyException.class, () -> statisticService.productStatisticGroupedByProducer(name, surname, countryDto));
        Assertions.assertEquals("NULL NAME OR SURNAME OR COUNTRY DTO - PRODUCT STATISTIC GROUPED BY PRODUCER", exception.getExceptionInfo().getMessage());

    }

    @Test
    @DisplayName("test of productStatisticGroupedByProducert  ")
    public void test37() {
        CountryDto countryDto = CountryDto.builder().name("COUNTRY").build();
        String name = "NAME";
        String surname = null;

        MyException exception =
                Assertions
                        .assertThrows(MyException.class, () -> statisticService.productStatisticGroupedByProducer(name, surname, countryDto));
        Assertions.assertEquals("NULL NAME OR SURNAME OR COUNTRY DTO - PRODUCT STATISTIC GROUPED BY PRODUCER", exception.getExceptionInfo().getMessage());

    }

    @Test
    @DisplayName("test of productStatisticGroupedByProducert  ")
    public void test38() {
        CountryDto countryDto = CountryDto.builder().name("COUNTRY_OUT").build();
        String name = "NAME";
        String surname = "SURNAME";

        MyException exception =
                Assertions
                        .assertThrows(MyException.class, () -> statisticService.productStatisticGroupedByProducer(name, surname, countryDto));
        Assertions.assertEquals("COUNTRY OUT OF BASE", exception.getExceptionInfo().getMessage());

    }

    @Test
    @DisplayName("test of productStatisticGroupedByProducert  ")
    public void test39() {
        CountryDto countryDto = CountryDto.builder().name("COUNTRY").build();
        String name = "NAME_OUT";
        String surname = "SURNAME";

        MyException exception =
                Assertions
                        .assertThrows(MyException.class, () -> statisticService.productStatisticGroupedByProducer(name, surname, countryDto));
        Assertions.assertEquals("COUNTRY OUT OF BASE", exception.getExceptionInfo().getMessage());

    }

    @Test
    @DisplayName("test of productStatisticGroupedByProducert  ")
    public void test40() {
        CountryDto countryDto = CountryDto.builder().name("COUNTRY").build();
        String name = "NAME";
        String surname = "SURNAME_OUT";

        Mockito.when(countryRepository
                .findByName(countryDto.getName()))
                .thenReturn(Optional.of(ModelMapper.fromCountryDtoToCountry(countryDto)));

        Assertions.assertEquals(0, statisticService.productStatisticGroupedByProducer(name, surname, countryDto).size());
    }

    @Test
    @DisplayName("test of productStatisticGroupedByProducert  ")
    public void test41() {
        CountryDto countryDto = CountryDto.builder().name("COUNTRY_OUT").build();
        String name = "NAME";
        String surname = "SURNAME";

        Mockito.when(countryRepository
                .findByName(countryDto.getName()))
                .thenReturn(Optional.of(ModelMapper.fromCountryDtoToCountry(countryDto)));

        Assertions.assertEquals(0, statisticService.productStatisticGroupedByProducer(name, surname, countryDto).size());
    }

    @Test
    @DisplayName("test of customersProductsIdenticalCountriesTest  ")
    public void test42() {

        Mockito.when(customerOrderRepository.findAll())
                .thenReturn(customerOrderList);

        Assertions.assertEquals(2, statisticService.customersProductsIdenticalCountries().size());
    }

    @Test
    @DisplayName("test of customersProductsIdenticalCountriesTest")
    public void test43() {

        Mockito.when(customerOrderRepository.findAll())
                .thenReturn(List.of());

        Assertions.assertEquals(0, statisticService.customersProductsIdenticalCountries().size());
    }

    @Test
    @DisplayName("test of mostErrorsInDayTest")
    public void test44() {

        Mockito.when(errorsRepository.findAll())
                .thenReturn(errorsList);

        Assertions.assertEquals(1, statisticService.mostErrorsInDay().size());
    }

    @Test
    @DisplayName("test of mostErrorsInDayTest")
    public void test45() {

        List<Errors> errors = new ArrayList<>(errorsList);

        errors.add(Errors
                .builder()
                .message("")
                .date(LocalDateTime.of(2019, 10, 01, 10, 10))
                .build());

        Mockito.when(errorsRepository.findAll())
                .thenReturn(errors);

        Assertions.assertEquals(2, statisticService.mostErrorsInDay().size());
    }

    @Test
    @DisplayName("test of mostErrorsInDayTest")
    public void test46() {

        Mockito.when(errorsRepository.findAll())
                .thenReturn(List.of());

        Assertions.assertEquals(0, statisticService.mostErrorsInDay().size());
    }

    @Test
    @DisplayName("test of mostMessageErrorsTest")
    public void test47() {
        Mockito.when(errorsRepository.findAll())
                .thenReturn(errorsList);
        Assertions.assertEquals(statisticService.mostMessageErrors().size(), 1);
    }

    @Test
    @DisplayName("test of mostMessageErrorsTest")
    public void test48() {
        List<Errors> errors = new ArrayList<>(errorsList);

        errors.add(Errors
                .builder()
                .message("CUSTOMER_ORDER;CUSTOMER_ORDER ADD EXCEPTION")
                .build());
        Mockito.when(errorsRepository.findAll())
                .thenReturn(errors);
        Assertions.assertEquals(2, statisticService.mostMessageErrors().size());
    }

    @Test
    @DisplayName("test of mostMessageErrorsTest")
    public void test49() {
        Mockito.when(errorsRepository.findAll())
                .thenReturn(List.of());
        Assertions.assertEquals(statisticService.mostMessageErrors().size(), 0);
    }

    @Test
    @DisplayName("test of mostExpensiveCategoryTest")
    public void test50() {
        Mockito.when(customerOrderRepository.findAll())
                .thenReturn(null);

        MyException myException = Assertions.assertThrows(MyException.class, () -> statisticService.mostExpensiveCategory());
        Assertions.assertEquals(myException.getExceptionInfo().getMessage(), "CUSTOMER ORDER REPOSITORY LIST - NULL OR EMPTY");
    }

    @Test
    @DisplayName("test of mostExpensiveCategoryTest")
    public void test51() {
        Mockito.when(customerOrderRepository.findAll())
                .thenReturn(List.of());

        MyException myException = Assertions.assertThrows(MyException.class, () -> statisticService.mostExpensiveCategory());
        Assertions.assertEquals(myException.getExceptionInfo().getMessage(), "CUSTOMER ORDER REPOSITORY LIST - NULL OR EMPTY");
    }

    @Test
    @DisplayName("test of mostExpensiveCategoryTest")
    public void test52() {
        List<CustomerOrder> customerOrders = new ArrayList<>(customerOrderList);
        customerOrders.add(CustomerOrder
                .builder()
                .product(
                        Product
                                .builder()
                                .category(Category
                                        .builder()
                                        .name("C")
                                        .build())
                                .price(new BigDecimal(200))
                                .build())
                .build());
        Mockito.when(customerOrderRepository.findAll())
                .thenReturn(customerOrderList);

        Assertions.assertEquals(statisticService.mostExpensiveCategory().size(), 2);
    }

}

