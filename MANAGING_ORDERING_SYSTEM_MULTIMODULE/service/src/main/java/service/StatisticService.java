package service;

import dto.CountryDto;
import dto.mapper.ModelMapper;
import dto.TradeDto;
import exceptions.ExceptionCode;
import exceptions.MyException;
import model.*;
import repositories.*;
import utils.EnumGuaranteeComponents;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static exceptions.ExceptionCode.*;

public class StatisticService {
    private final CountryRepository countryRepository;
    private final CustomerOrderRepository customerOrderRepository;
    private final ProducerRepository producerRepository;
    private final StockRepository stockRepository;
    private final ErrorsRepository errorsRepository;


    StatisticService(CountryRepository countryRepository,
                     CustomerOrderRepository customerOrderRepository,
                     ProducerRepository producerRepository,
                     StockRepository stockRepository,
                     ErrorsRepository errorsRepository) {
        this.countryRepository = countryRepository;
        this.customerOrderRepository = customerOrderRepository;
        this.producerRepository = producerRepository;
        this.stockRepository = stockRepository;
        this.errorsRepository = errorsRepository;
    }

    Map<Producer, List<Product>> productStatisticGroupedByProducer
            (String name, String surname, CountryDto countryDto) {
        if (name == null || surname == null || countryDto == null) {
            throw new MyException(CUSTOMERS, "NULL NAME OR SURNAME OR COUNTRY DTO - PRODUCT STATISTIC GROUPED BY PRODUCER");
        }

        final CountryDto countryDtoCheck = ModelMapper.fromCountryToCountryDto(countryRepository
                .findByName(countryDto.getName())
                .orElseThrow(() -> new MyException(COUNTRIES, "COUNTRY OUT OF BASE")));
        return customerOrderRepository
                .findAll()
                .stream()
                .filter(x -> x.getCustomer().getName().equals(name)
                        && x.getCustomer().getSurname().equals(surname)
                        && x.getCustomer().getCountry().getName().equals(countryDtoCheck.getName()))
                .map(CustomerOrder::getProduct)
                .collect(Collectors.groupingBy(Product::getProducer));
    }

    List<Customer> customersProductsIdenticalCountries() {
        return customerOrderRepository
                .findAll()
                .stream()
                .collect(Collectors.groupingBy(CustomerOrder::getCustomer,
                        Collectors.mapping(CustomerOrder::getProduct, Collectors.toList())))
                .entrySet()
                .stream()
                .filter(x -> x.getValue()
                        .stream()
                        .map(y -> y.getProducer().getCountry().getName())
                        .collect(Collectors.toList())
                        .contains(x.getKey().getCountry().getName()))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    Map<String, Long> mostErrors() {

        Map<String, Long> errors = errorsRepository
                .findAll()
                .stream()
                .map(x -> x.getMessage().split(";")[0])
                .filter(x -> errorsRepository
                        .allEntitiesName()
                        .contains(x.toLowerCase()))
                .collect(Collectors.groupingBy(x -> x, Collectors.counting()));

        return errors
                .entrySet()
                .stream()
                .filter(x -> x.getValue().equals(errors.values()
                        .stream()
                        .max(Comparator.comparing(Function.identity()))
                        .orElseThrow(() -> new MyException(ERRORS_STATISTIC, "ERRORS STATISTIC EXCEPTION"))
                ))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue));

    }

    List<LocalDate> mostErrorsInDay() {

        Map<LocalDate, Long> errors = errorsRepository
                .findAll()
                .stream()
                .collect(Collectors.groupingBy(x -> LocalDate.from(x.getDate()), Collectors.counting()));

        return errors
                .entrySet()
                .stream()
                .filter(x -> errors
                        .entrySet()
                        .stream()
                        .max(Comparator.comparing(Map.Entry::getValue))
                        .orElseThrow(() -> new MyException(ERRORS_STATISTIC, "ERRORS STATISTIC EXCEPTION"))
                        .getValue()
                        .compareTo(x.getValue()) == 0)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    List<String> mostMessageErrors() {
        Map<String, Long> errors = errorsRepository
                .findAll()
                .stream()
                .collect(Collectors.groupingBy(Errors::getMessage, Collectors.counting()));

        return errors
                .entrySet()
                .stream()
                .filter(x -> x.getValue().equals(errors
                        .entrySet()
                        .stream()
                        .max(Comparator.comparing(Map.Entry::getValue))
                        .orElseThrow(() -> new MyException(ERRORS_STATISTIC, "ERRORS STATISTIC EXCEPTION"))
                        .getValue()))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    Map<Category, Map<Product, Long>> mostExpensiveCategory() {
        if (customerOrderRepository.findAll() == null || customerOrderRepository.findAll().isEmpty()) {
            throw new MyException(CUSTOMER_ORDERS, "CUSTOMER ORDER REPOSITORY LIST - NULL OR EMPTY");
        }
        Map<Category, Map<Product, Long>> categoryMapMap = customerOrderRepository
                .findAll()
                .stream()
                .collect(Collectors.groupingBy(x -> x.getProduct().getCategory(), Collectors.groupingBy(CustomerOrder::getProduct, Collectors.counting())));

        return categoryMapMap
                .entrySet()
                .stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        v -> v.getValue()
                                .entrySet()
                                .stream()
                                .filter(x -> x.getKey().getPrice().equals(categoryMapMap
                                        .entrySet()
                                        .stream()
                                        .filter(y -> y.getKey().equals(v.getKey()))
                                        .flatMap(y -> y.getValue().entrySet().stream())
                                        .map(y -> y.getKey().getPrice())
                                        .max(Comparator.comparing(y -> y))
                                        .orElseThrow(() -> new MyException(PRODUCTS, "MAX COMPARATOR EXCEPTION"))))
                                .collect(Collectors.toMap(Map.Entry::getKey,
                                        v1 -> categoryMapMap.entrySet()
                                                .stream()
                                                .filter(z -> z.getKey().equals(v.getKey()))
                                                .flatMap(z -> z.getValue().values()
                                                        .stream())
                                                .reduce(Long::sum)
                                                .orElseThrow(() -> new MyException(PRODUCTS, "MAX COMPARATOR EXCEPTION"))))));

    }

    public List<Product> productsFilteredByAgeCountry(CountryDto countryDto, int ageMin, int ageMax) {
        if (countryDto == null || ageMin > ageMax) {
            throw new MyException(STATISTIC, "DATA FROM USER ARE NULL OR NOT VALIDATE");
        }
        return customerOrderRepository
                .findAll()
                .stream()
                .filter(x -> x.getCustomer().getCountry().getName().equals(countryDto.getName())
                        && x.getCustomer().getAge() >= ageMin
                        && x.getCustomer().getAge() <= ageMax)
                .map(CustomerOrder::getProduct)
                .collect(Collectors.toList());
    }

    public Map<Category, List<Product>> productsAndCategoryRosterByComponents
            (Set<EnumGuaranteeComponents> guaranteeComponentsFromUser) {
        if (guaranteeComponentsFromUser == null || guaranteeComponentsFromUser.isEmpty()) {
            throw new MyException(STATISTIC, "DATA FROM USER ARE NULL OR NOT VALIDATE");
        }
        if (customerOrderRepository.findAll() == null || customerOrderRepository.findAll().isEmpty()) {
            throw new MyException(STATISTIC, "CUSTOMER ORDER REPOSITORY LIST ARE NULL OR NOT VALIDATE");
        }

        return customerOrderRepository
                .findAll()
                .stream()
                .filter(x -> x.getProduct().getEnumGuaranteeComponents().containsAll(guaranteeComponentsFromUser))
                .collect(
                        Collectors.groupingBy(x -> x.getProduct().getCategory(),
                                Collectors.mapping(CustomerOrder::getProduct,
                                        Collectors.toList())));
    }

    public List<Shop> shopsByCountryAndProductCountryList() {
        if (stockRepository.findAll() == null || stockRepository.findAll().isEmpty()) {
            throw new MyException(STATISTIC, "STOCK REPOSITORY LIST ARE NULL OR EMPTY");
        }
        return stockRepository
                .findAll()
                .stream()
                .filter(Predicate.not(x -> x.getProduct().getProducer().getCountry().equals(x.getShop().getCountry())))
                .map(Stock::getShop)
                .collect(Collectors.toList());
    }

    public List<Producer> filteredProducers(TradeDto tradeDto, int quantity) {
        if (tradeDto == null || quantity < 0) {
            throw new MyException(STATISTIC, "DATA FROM USER ARE NULL OR NOT VALIDATE");
        }
        if (producerRepository.findAll() == null || producerRepository.findAll().isEmpty()) {
            throw new MyException(STATISTIC, "PRODUCER REPOSITORY LIST ARE NULL OR EMPTY");
        }
        return producerRepository
                .findAll()
                .stream()
                .filter(x -> x.getProducts().size() > quantity
                        && x.getTrade().getName().equals(tradeDto.getName()))
                .collect(Collectors.toList());
    }

    public List<CustomerOrder> customerOrderByDateAmount
            (LocalDate min, LocalDate max, BigDecimal amount) {
        if (min == null || max == null || amount == null || min.isAfter(max)||amount.compareTo(BigDecimal.ZERO)<0) {
            throw new MyException(STATISTIC, "DATA FROM USER ARE NULL OR NOT VALIDATE");
        }
        if (customerOrderRepository.findAll() == null || customerOrderRepository.findAll().isEmpty()) {
            throw new MyException(STATISTIC, "CUSTOMER ORDER REPOSITORY LIST ARE NULL OR EMPTY");
        }
        return customerOrderRepository
                .findAll()
                .stream()
                .filter(x -> x.getDate().isAfter(min)
                        && x.getDate().isBefore(max)
                        && (x.getProduct().getPrice()
                        .multiply(new BigDecimal(x.getQuantity())))
                        .subtract(x.getProduct().getPrice()
                                .multiply(new BigDecimal(x.getQuantity()))
                                .multiply(x.getDiscount().divide(new BigDecimal(100), RoundingMode.CEILING)))
                        .compareTo(amount) > 0)
                .collect(Collectors.toList());
    }
}
