package service;

import dto.*;
import dto.mapper.ModelMapper;
import exceptions.ExceptionCode;
import exceptions.MyException;
import impl.*;

import model.*;
import repositories.*;
import utils.DataFromUserService;
import utils.EPayment;
import utils.EnumGuaranteeComponents;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


public class MenuHelpService {
    AddingToDateBaseService addingToDateBaseService;
    private StatisticService statisticService;
    private final DateBaseInitializationService dateBaseInitializationService;

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

    public MenuHelpService(CategoryRepository categoryRepository,
                           CountryRepository countryRepository,
                           CustomerOrderRepository customerOrderRepository,
                           CustomerRepository customerRepository,
                           PaymentRepository paymentRepository,
                           ProducerRepository producerRepository,
                           ProductRepository productRepository,
                           ShopRepository shopRepository,
                           StockRepository stockRepository,
                           TradeRepository tradeRepository,
                           ErrorsRepository errorsRepository) {

        this.categoryRepository = categoryRepository;
        this.countryRepository = countryRepository;
        this.customerOrderRepository = customerOrderRepository;
        this.customerRepository = customerRepository;
        this.paymentRepository = paymentRepository;
        this.producerRepository = producerRepository;
        this.productRepository = productRepository;
        this.shopRepository = shopRepository;
        this.stockRepository = stockRepository;
        this.tradeRepository = tradeRepository;
        this.errorsRepository = errorsRepository;

        this.addingToDateBaseService = new AddingToDateBaseService(categoryRepository
                , countryRepository
                , customerOrderRepository
                , customerRepository
                , paymentRepository
                , producerRepository
                , productRepository
                , shopRepository
                , stockRepository
                , tradeRepository
                , errorsRepository);

        this.statisticService = new StatisticService(countryRepository
                , customerOrderRepository
                , producerRepository
                , stockRepository
                , errorsRepository);
        this.dateBaseInitializationService=new DateBaseInitializationService(

                addingToDateBaseService,
                categoryRepository
                , countryRepository
                , customerOrderRepository
                , customerRepository
                , paymentRepository
                , producerRepository
                , productRepository
                , shopRepository
                , stockRepository
                , tradeRepository
                , errorsRepository

                );
    }

    void addCustomerWithCountryFromUser() {

        System.out.println("PODAJ IMIE KLIENTA");
        String customerName = DataFromUserService.getStringSpaceAndUpperCase();
        System.out.println("PODAJ NAZWISKO KLIENTA");
        String customerSurname = DataFromUserService.getStringSpaceAndUpperCase();
        System.out.println("PODAJ WIEK KLIENTA");
        Integer customerAge = DataFromUserService.getInt();
        System.out.println("PODAJ NAZWE KRAJU POCHODZENIA KLIENTA");
        String customerCountryName = DataFromUserService.getStringSpaceAndUpperCase();
        CustomerDto customerDtoFromUser = CustomerDto.builder()
                .name(customerName)
                .surname(customerSurname)
                .age(customerAge)
                .countryDto(CountryDto.builder()
                        .name(customerCountryName)
                        .build())
                .build();
        if (customerDtoFromUser == null) {
            throw new MyException(ExceptionCode.CUSTOMERS, "NULL VALUE OF CUSTOMER");
        }
        new CustomerValidator().validate(ModelMapper.fromCustomerDtoToCustomer(customerDtoFromUser));
        addingToDateBaseService.addCustomerWithCountry(customerDtoFromUser);

        System.out.println("CZY CHCESZ DODAC JESZCZE JAKIEGOS KLIENTA?");
        if (DataFromUserService.getYesOrNo()) {
            addCustomerWithCountryFromUser();
        }
        System.out.println("CZY CHCESZ WYSWIETLIC AKTUALNA LISTE KLIENTOW?");
        if (DataFromUserService.getYesOrNo()) {
            customerRepository.findAll().forEach(System.out::println);
        }
    }

    void addShopWithCountryFromUser() {

        System.out.println("PODAJ NAZWE SKLEPU");
        String shopName = DataFromUserService.getStringSpaceAndUpperCase();
        System.out.println("PODAJ NAZWE KRAJU Z KTOREGO JEST SKLEP");
        String shopCountryName = DataFromUserService.getStringSpaceAndUpperCase();

        ShopDto shopDtoFromUser = ShopDto.builder()
                .name(shopName)
                .countryDto(CountryDto.builder()
                        .name(shopCountryName)
                        .build())
                .build();
        new ShopValidator().validate(ModelMapper.fromShopDtoToShop(shopDtoFromUser));
        addingToDateBaseService.addShopWithCountry(shopDtoFromUser);
        System.out.println("CZY CHCESZ DODAC JESZCZE JAKIES SKLEP?");
        if (DataFromUserService.getYesOrNo()) {
            addShopWithCountryFromUser();
        }
        System.out.println("CZY CHCESZ WYSWIETLIC AKTUALNA LISTE SKLEPOW?");
        if (DataFromUserService.getYesOrNo()) {
            shopRepository.findAll().forEach(System.out::println);
        }

    }

    void addProducerWithCountryAndTradeFromUser() {
        System.out.println("PODAJ NAZWE PRODUCENTA");
        String producerName = DataFromUserService.getStringSpaceAndUpperCase();
        System.out.println("PODAJ NAZWE KRAJU Z KTOREGO JEST PRODUCENT");
        String producerCountryName = DataFromUserService.getStringSpaceAndUpperCase();
        System.out.println("PODAJ NAZWE BRANZY");
        String producerTradeName = DataFromUserService.getStringSpaceAndUpperCase();

        ProducerDto producerDtoFromUser = ProducerDto.builder()
                .name(producerName)
                .countryDto(
                        CountryDto.builder()
                                .name(producerCountryName)
                                .build())
                .tradeDto(TradeDto.builder()
                        .name(producerTradeName)
                        .build())
                .build();
        new ProducerValidator().validate(ModelMapper.fromProducerDtoToProducer(producerDtoFromUser));
        addingToDateBaseService.addProducerWithCountryAndTrade(producerDtoFromUser);

        System.out.println("CZY CHCESZ DODAC JESZCZE JAKIEGOS PRODUCENTA?");
        if (DataFromUserService.getYesOrNo()) {
            addProducerWithCountryAndTradeFromUser();
        }
        System.out.println("CZY CHCESZ WYSWIETLIC AKTUALNA LISTE PRODUCENTOW?");
        if (DataFromUserService.getYesOrNo()) {
            producerRepository.findAll().forEach(System.out::println);
        }
    }

    void addProductWithCategoryAndProducerFromUser() {
        System.out.println("PODAJ NAZWE PRODUKTU");
        String productName = DataFromUserService.getStringSpaceAndUpperCase();
        System.out.println("PODAJ CENE PRODUKTU");
        BigDecimal productPrice = DataFromUserService.getPrice();
        System.out.println("PODAJ NAZWE KATEGORII");
        String productCategoryName = DataFromUserService.getStringSpaceAndUpperCase();
        System.out.println("PODAJ NAZWE PRODUCENTA");
        String productProducerName = DataFromUserService.getStringSpaceAndUpperCase();
        System.out.println("PODAJ NAZWE KRAJU PRODUCENTA");
        String productProducerCountryName = DataFromUserService.getStringSpaceAndUpperCase();
        System.out.println("PODAJ LISTE USLUG GWARANCYJNYCH(PO PRZECINKU - HELP_DESK,MONEY_BACK,SERVICE,EXCHANGE)");

        Set<EnumGuaranteeComponents> listGuaranteeComponents =
                DataFromUserService.getSetGuaranteeComponents();

        ProductDto productDtoFromUser = ProductDto.builder()
                .name(productName)
                .price(productPrice)
                .categoryDto(CategoryDto.builder()
                        .name(productCategoryName)
                        .build())
                .producerDto(ProducerDto.builder()
                        .name(productProducerName)
                        .countryDto(CountryDto.builder()
                                .name(productProducerCountryName)
                                .build())
                        .build())
                .enumGuaranteeComponents(listGuaranteeComponents)
                .build();
        new ProductValidator().validate(ModelMapper.fromProductDtoToProduct(productDtoFromUser));
        addingToDateBaseService.addProductWithCategoryAndProducer(productDtoFromUser);


        System.out.println("CZY CHCESZ DODAC JESZCZE JAKIS PRODUKT ?");
        if (DataFromUserService.getYesOrNo()) {
            addProductWithCategoryAndProducerFromUser();
        }
        System.out.println("CZY CHCESZ WYSWIETLIC AKTUALNA LISTE PRODUKTOW?");
        if (DataFromUserService.getYesOrNo()) {
            productRepository.findAll().forEach(System.out::println);
        }
    }

    void addStockWithProductAndCategoryFromUser() {
        System.out.println("PODAJ NAZWE PRODUKTU");
        String stockProductName = DataFromUserService.getStringSpaceAndUpperCase();
        System.out.println("PODAJ NAZWE KATEGORII");
        String stockCategoryName = DataFromUserService.getStringSpaceAndUpperCase();
        System.out.println("PODAJ NAZWE SKLEPU");
        String stockShopName = DataFromUserService.getStringSpaceAndUpperCase();
        System.out.println("PODAJ NAZWE KRAJU Z KTOREGO JEST SKLEP");
        String stockCountryShopName = DataFromUserService.getStringSpaceAndUpperCase();
        System.out.println("PODAJ ILOSC SZTUK");
        Integer stockQuantity = DataFromUserService.getInt();

        addingToDateBaseService.addStockWithProductAndCategory(StockDto.builder()
                .shopDto(
                        ShopDto
                                .builder()
                                .countryDto(
                                        CountryDto
                                                .builder()
                                                .name(stockCountryShopName)
                                                .build())
                                .name(stockShopName)
                                .build())
                .product(
                        ProductDto
                                .builder()
                                .name(stockProductName)
                                .categoryDto(
                                        CategoryDto
                                                .builder()
                                                .name(stockCategoryName)
                                                .build())
                                .build())
                .quantity(stockQuantity)
                .build());

        System.out.println("CZY CHCESZ DODAC JESZCZE JAKAS POZYCJE NA MAGAZYNIE?");
        if (DataFromUserService.getYesOrNo()) {
            addStockWithProductAndCategoryFromUser();
        }

        System.out.println("CZY CHCESZ WYSWIETLIC AKTUALNA LISTE POZYCJI NA MAGAZYNIE?");
        if (DataFromUserService.getYesOrNo()) {
            stockRepository.findAll().forEach(System.out::println);
        }
    }

    void addCustomerOrderWithCustomerCategoryPaymentFromUser() {
        System.out.println("PODAJ IMIE KLIENTA");
        String customerOrderCustomerName = DataFromUserService.getStringSpaceAndUpperCase();
        System.out.println("PODAJ NAZWISKO KLIENTA");
        String customerOrderCustomerSurname = DataFromUserService.getStringSpaceAndUpperCase();
        System.out.println("PODAJ NAZWE KRAJU Z KTOREGO JEST KLIENT");
        String customerOrderCustomerCountryName = DataFromUserService.getStringSpaceAndUpperCase();
        System.out.println("PODAJ NAZWE PRODUKTU");
        String customerOrderProductName = DataFromUserService.getStringSpaceAndUpperCase();
        System.out.println("PODAJ NAZWE KATEGORII PRODUKTU");
        String customerOrderCategoryName = DataFromUserService.getStringSpaceAndUpperCase();
        System.out.println("PODAJ ILOSC SZTUK PRODUKTU");
        Integer customerOrderQuantity = DataFromUserService.getInt();
        System.out.println("PODAJ DATE ZAMOWIENIA");
        LocalDate customerOrderOrderDate = DataFromUserService.getLocalDateNotPast();
        System.out.println("PODAJ ZNIZKE");
        BigDecimal customerOrderDiscount = DataFromUserService.getPrice();
        System.out.println("PODAJ RODZAJ PLATNOSCI");
        EPayment customerOrderPayment = DataFromUserService.getEPayment();

        CustomerOrderDto customerOrderDtoFromUser = CustomerOrderDto.builder()
                .productDto(ProductDto.builder()
                        .name(customerOrderProductName)
                        .categoryDto(CategoryDto.builder()
                                .name(customerOrderCategoryName)
                                .build())
                        .build())
                .paymentDto(PaymentDto.builder()
                        .payment(customerOrderPayment)
                        .build())
                .customerDto(CustomerDto.builder()
                        .countryDto(CountryDto.builder()
                                .name(customerOrderCustomerCountryName)
                                .build())
                        .name(customerOrderCustomerName)
                        .surname(customerOrderCustomerSurname)
                        .build())
                .quantity(customerOrderQuantity)
                .discount(customerOrderDiscount)
                .date(customerOrderOrderDate)
                .build();
        new CustomerOrderValidator().validate(ModelMapper.fromCustomerOrderDtoToCustomerOrder(customerOrderDtoFromUser));
        addingToDateBaseService.addCustomerOrderWithCustomerCategoryPayment(customerOrderDtoFromUser);

        System.out.println("CZY CHCESZ DODAC JESZCZE JAKIES ZAMOWIENIE?");
        if (DataFromUserService.getYesOrNo()) {
            addCustomerOrderWithCustomerCategoryPaymentFromUser();
        }

        System.out.println("CZY CHCESZ WYSWIETLIC AKTUALNA LISTE ZAMOWIEN?");
        if (DataFromUserService.getYesOrNo()) {
            customerOrderRepository.findAll().forEach(System.out::println);
        }
    }

    void categoryMostExpensiveProducts() {
        Map<Category, Map<Product, Long>> categoryAndMap = statisticService.mostExpensiveCategory();
        System.out.println("NAJDROZSZE PRODUKTY W DANEJ KATEGORII: ");
        categoryAndMap.forEach((k, v) -> System.out.println(k.getName() + " " + v.keySet()
                .stream()
                .map(Product::getName)
                .collect(Collectors.toList()) + " " + v.values()));

    }

    void productsFiltredByAgeAndCountry() {

        System.out.println("PODAJ NAZWE SZUKANEGO KRAJU KLIENTA");
        Country country = new CountryRepositoryImpl()
                .findByName(DataFromUserService.getStringSpaceAndUpperCase())
                .orElseThrow(() -> new MyException(ExceptionCode.COUNTRIES, "COUNTRY OUT OF BASE"));
        System.out.println("PODAJ ZAKRES WIEKU, MINIMALNY:");
        int ageMin = DataFromUserService.getInt();
        System.out.println("MAKSYMALNY: ");
        int ageMax = DataFromUserService.getInt();
        List<Product> products = statisticService
                .productsFilteredByAgeCountry(ModelMapper.fromCountryToCountryDto(country), ageMin, ageMax);
        System.out.println("LISTA WYBRANYCH PRODUKTOW: ");
        products.forEach(x -> System.out.println(
                x.getName() + "; KATEGORIA: " +
                        x.getCategory().getName() + "; CENA: " +
                        x.getPrice()));
    }

    void productsByGuaranteeComponents() {

        System.out.println("PODAJ LISTE USLUG SERWISOWYCH JAKICH SZUKASZ W POSZCZEGOLNYCH PRODUKTACH (PO PRZECINKU)");
        Set<EnumGuaranteeComponents> guaranteeComponentsSet = DataFromUserService.getSetGuaranteeComponents();

        statisticService
                .productsAndCategoryRosterByComponents(guaranteeComponentsSet)
                .forEach((key, value) -> System.out.println(
                        key.getName() + "," + value
                                .stream()
                                .map(x -> "\n " + x.getName() + "\n " +
                                        "USLUGI: " + x.getEnumGuaranteeComponents())
                                .collect(Collectors.toList())));

    }

    void shopsProductsCountriesDifferences() {
        List<Shop> filteredShops = statisticService
                .shopsByCountryAndProductCountryList();
        System.out.println("OTO SKLEPY KTORYCH KRAJ POCHODZENIA JEST INNY NIZ PRODUKTOW W NICH SIE ZNAJDUJACYCH: " + "\n");
        filteredShops
                .forEach(x -> System.out.println(
                        "NAZWA SKLEPU: " + x.getName() +
                                " NAZWA KRAJU POCHODZENIA SKLEPU: " + x.getCountry().getName()));
    }

    void producersFiltredByDataFromUser() {
        System.out.println("PODAJ NAZWE SZUKANEJ BRANZY: ");
        Trade trade = new TradeRepositoryImpl()
                .findByName(DataFromUserService.getStringSpaceAndUpperCase())
                .orElseThrow(() -> new MyException(ExceptionCode.TRADES, "TRADE NAME OUT OF BASE"));
        System.out.println("PODAJ MINIMALNA LICZBE PRODUKTOW U DANEGO PRODUCENTA WYPRODUKOWANYCH, KTORE CHCESZ MIEC W ZESTAWIENIU : ");
        int quantity = DataFromUserService.getInt() - 1;
        List<Producer> producers = statisticService
                .filteredProducers(ModelMapper.fromTradeToTradeDto(trade), quantity);
        System.out.println("OTO LISTA PRODUCENTOW: " + "\n");
        producers
                .forEach(x -> System.out.println("NAZWA PRODUCENTA: " + x.getName() +
                        "; NAZWA BRANZY: " + x.getTrade().getName() +
                        "; NAZWA KRAJU POCHODZENIA PRODUCENTA: " + x.getCountry().getName()));

    }

    void customerOdersFiltredByDataFromUser() {

        System.out.println("PODAJ DATE POCZATKOWA: ");
        LocalDate localDateMin = DataFromUserService.getLocalDate();
        System.out.println("PODAJ DATE KONCOWA: ");
        LocalDate localDateMax = DataFromUserService.getLocalDate();
        System.out.println("PODAJ KWOTE POWYZEJ JAKIEJ MAM WYSWIETLIC ZAMOWIENIA: ");
        BigDecimal amountFromUser = DataFromUserService.getPrice();

        List<CustomerOrder> customerOrderList =
                statisticService.customerOrderByDateAmount(localDateMin, localDateMax, amountFromUser);
        System.out.println("LISTA ZAMOWIEN PO FILTROWANIU: " + "\n");
        customerOrderList.forEach(x -> System.out.println("ID ZAMOWIENIA: " + x.getId() + "; NAZWA PRODUKTU: " + x.getProduct().getName()));
    }

    void productsFiltredByDataFromUser() {
        System.out.println("PODAJ IMIE KLIENTA");
        String name = DataFromUserService.getStringSpaceAndUpperCase();
        System.out.println("PODAJ NAZWISKO KLIENTA");
        String surname = DataFromUserService.getStringSpaceAndUpperCase();
        System.out.println("PODAJ NAZWE KRAJU KLIENTA");
        CountryDto countryDto = CountryDto
                .builder()
                .name(DataFromUserService.getStringSpaceAndUpperCase())
                .build();


        Map<Producer, List<Product>> producerWithListProduct = statisticService
                .productStatisticGroupedByProducer(name, surname, countryDto);
        System.out.println("LISTA POGRUPOWANYCH PRODUKTOW WEDLUG PRODUCENTOW: ");
        producerWithListProduct.forEach((k, v) -> System.out.println("PRODUCER: " + k.getName() +
                "; BRANZA: " + k.getTrade() +
                "; NAZWA KRAJU: " + k.getCountry().getName() +
                ";" + "\n" + "LISTA PRODUKTOW: [" + v
                .stream()
                .map(x -> x.getName() + " CENA: " + x.getPrice() + " KATEGORIA: " + x.getCategory() + ", ")
                .collect(Collectors.joining()) + "]"));
    }

    void utilsCustomersWithIdenticalCountriesLikeProducts() {

        List<Customer> customers = statisticService
                .customersProductsIdenticalCountries();
        System.out.println("LISTA KLIENTOW SPELNIJACYCH WARUNEK: ");
        customers.forEach(x -> System.out.println(x.getName() + ", " + x.getSurname() + ", " + x.getCountry().getName() + ", WIEK: " + x.getAge()));
    }

    void utilsMostErrorsInTable() {
        System.out.println("NAZWA TABELI KTORA MA NAJWIECEJ BLEDOW");
        System.out.println(statisticService.mostErrors());
    }

    void utilsMostErrorsInDay() {
        System.out.println("KTOREGO DNIA POPELNIONO NAJWIECEJ BLEDOW");
        System.out.println(statisticService.mostErrorsInDay());
    }

    void utilsMostErrorsMessage() {
        System.out.println("KTORY KOMUNIKAT POJAWIAL SIE NAJCZESCIEJ");
        System.out.println(statisticService.mostMessageErrors());
    }

    void manageDataBaseInitialization() {
        System.out.println("CZY JESTES PEWIEN ZE CHCESZ USUNAC DANE I ZAINICJOWAC BAZE?(TAK/NIE)");
        if (DataFromUserService.getYesOrNo()) {
            dateBaseInitializationService.deleteAll();
        } else {
            return;
        }
        System.out.println("INICJALIZACJA BAZY DANYCH...");
        dateBaseInitializationService.fromJsonDBinitialization();
        System.out.println("BAZA DANYCH ZOSTAŁA ZAINICJALIZOWANA");


    }
}


