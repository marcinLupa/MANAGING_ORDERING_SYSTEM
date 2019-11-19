package service;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import connections.DbConnection;
import dto.ErrorsDto;
import exceptions.ExceptionCode;
import exceptions.MyException;
import repositories.*;
import utils.DataFromUserService;


public class MenuService {

    private MenuHelpService menuHelpService;


    private final CustomerOrderRepository customerOrderRepository;
    private final CustomerRepository customerRepository;
    private final ProducerRepository producerRepository;
    private final ProductRepository productRepository;
    private final ShopRepository shopRepository;
    private final StockRepository stockRepository;


    public MenuService(
            CategoryRepository categoryRepository,
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


        this.customerOrderRepository = customerOrderRepository;
        this.customerRepository = customerRepository;
        this.producerRepository = producerRepository;
        this.productRepository = productRepository;
        this.shopRepository = shopRepository;
        this.stockRepository = stockRepository;

        this.menuHelpService = new MenuHelpService(
                categoryRepository,
                countryRepository,
                customerOrderRepository,
                customerRepository,
                paymentRepository,
                producerRepository,
                productRepository,
                shopRepository,
                stockRepository,
                tradeRepository,
                errorsRepository);
    }

    public void mainMenuManagement() {

        while (true) {
            try {
                System.out.println("MENU GŁOWNE: " + "\n");
                System.out.println("JAKIE DANE CHCESZ EDYTOWAC LUB WYSWIETLIC?" + "\n" +
                        "1 - DODAWANIE DANYCH DO TABELI" + "\n" +
                        "2 - WYSWIETLANIE DANYCH W TABELACH" + "\n" +
                        "3 - INICJALIZACJA PRZYKLADOWEJ BAZY DANYCH" + "\n" +
                        "4 - MENU DANYCH STATYSTYCZNYCH" + "\n" +
                        "5 - STATYSTYKI BLEDOW" + "\n" +
                        "6 - WYJSCIE Z PROGRAMU");


                int menuOption = DataFromUserService.getInt(6);

                switch (menuOption) {
                    case 1 -> addMenuManagement();
                    case 2 -> displayMenuManagement();
                    case 3 -> menuHelpService.manageDataBaseInitialization();
                    case 4 -> statisticDateBaseMenuManagement();
                    case 5 -> errorsStatisticMenuManagement();
                    case 6 -> {
                        DbConnection.getInstance().close();
                        System.out.println("WYJSCIE Z PROGRAMU");
                        return;
                    }
                }


            } catch (MyException e) {
                e.printStackTrace();
                System.err.println(e.getExceptionInfo().getMessage());
                System.err.println(e.getExceptionInfo().getCode());

                menuHelpService.addingToDateBaseService
                        .addErrors(ErrorsDto
                                .builder()
                                .date(e.getExceptionInfo().getDateTime())
                                .message(e.getExceptionInfo().getCode() + ";" + e.getExceptionInfo().getMessage())
                                .build()
                        );
            }
        }
    }

    private void addMenuManagement() {
        boolean continuation = true;
        while (continuation) {
            System.out.println("MENU:");
            System.out.println("1 - DODAJ KLIENTA" + "\n"
                    + "2 - DODAJ SKLEP" + "\n"
                    + "3 - DODAJ PRODUCENTA" + "\n"
                    + "4 - DODAJ PRODUKT" + "\n"
                    + "5 - DODAJ POZYCJE W MAGAZNYNIE" + "\n"
                    + "6 - DODAJ ZAMOWIENIE" + "\n"
                    + "7 - WYJSCIE Z MENU DODAWANIA DANYCH DO TABELI " + "\n");
            System.out.println("PODAJ OPCJE Z KTOREJ CHCESZ SKORZYSTAC");

            int optionAdd = DataFromUserService.getInt(9);

            switch (optionAdd) {
                case 1 -> menuHelpService.addCustomerWithCountryFromUser();
                case 2 -> menuHelpService.addShopWithCountryFromUser();
                case 3 -> menuHelpService.addProducerWithCountryAndTradeFromUser();
                case 4 -> menuHelpService.addProductWithCategoryAndProducerFromUser();
                case 5 -> menuHelpService.addStockWithProductAndCategoryFromUser();
                case 6 -> menuHelpService.addCustomerOrderWithCustomerCategoryPaymentFromUser();
                case 7 -> continuation = false;
            }
        }
    }

    private void displayMenuManagement() {
        boolean continuation = true;
        while (continuation) {
            System.out.println("MENU:");
            System.out.println("1 - WYSWIETL AKTUALNA LISTE KLIENTOW" + "\n"
                    + "2 - WYSWIETL AKTUALNA LISTE SKLEPOW" + "\n"
                    + "3 - WYSWIETL AKTUALNA LISTE PRODUCENTOW" + "\n"
                    + "4 - WYSWIETL AKTUALNA LISTE PRODUKTOW" + "\n"
                    + "5 - WYSWIETL AKTUALNA LISTE POZYCJI W MAGAZNYNIE" + "\n"
                    + "6 - WYSWIETL AKTUALNA LISTE ZAMOWIEN" + "\n"
                    + "7 - WYJSCIE Z MENU WYSWIETLANI AKTUALNYCH DANYCH W TABELACH DANYCH DO TABELI " + "\n");
            System.out.println("PODAJ OPCJE Z KTOREJ CHCESZ SKORZYSTAC");
            int optionAdd = DataFromUserService.getInt(7);
            switch (optionAdd) {
                case 1 -> customerRepository
                        .findAll()
                        .forEach(System.out::println);
                case 2 -> shopRepository
                        .findAll()
                        .forEach(System.out::println);
                case 3 -> producerRepository
                        .findAll()
                        .forEach(System.out::println);
                case 4 -> productRepository
                        .findAll()
                        .forEach(System.out::println);
                case 5 -> stockRepository
                        .findAll()
                        .forEach(System.out::println);
                case 6 -> customerOrderRepository
                        .findAll()
                        .forEach(System.out::println);
                case 7 -> continuation = false;
            }
        }
    }

    private void statisticDateBaseMenuManagement() {

        boolean continuation = true;
        while (continuation) {
            System.out.println("MENU DANYCH STATYSTYCZNYCH:");
            System.out.println("1 - WYSWIETL PRODUKTY O NAJWYZSZEJ CENIE W " +
                    "KAZDEJ KATEGORII I ILE PRODUKTOW Z KAZDEJ KATEGORII LACZNIE WYSTAPILO NA LISCIE ZAMOWIEN" + "\n"
                    + "2 - WYSWIETL AKTUALNA LISTE PRODUKTOW ZAMAWIANYCH PRZEZ KLIENTOW Z PODANU KRAJU I W PODANYM WIEKU " + "\n"
                    + "3 - WYSWIETL AKTUALNA LISTE PRODUKTOW OBJETYCH PODANA PRZEZ UZYTKOWNIKA LISTA USLUG GWARANCYJNYCH" + "\n"
                    + "4 - WYSWIETL AKTUALNA LISTE SKLEPOW KTORE MAJA INNY KRAJ POCHODZENIA NIZ PRODUKTY NA MAGAZYNIE" + "\n"
                    + "5 - WYSWIETL AKTUALNA LISTE PRODUCENTOW, WEDLUG PODANEJ BRANZY ORAZ ILOSCI MINIMALNEJ" +
                    " PRODUKTOW WYPRODUKOWANYCH U DANEGO PRODUCENTA" + "\n"
                    + "6 - WYSWIETL AKTUALNA LISTE ZAMOWIEN W ZAKRESIE DAT PODANYCH PRZEZ UZYTKOWNIKA" +
                    " ORAZ O KWOCIE ZAMOWIENIA WYZSZEJ NIZ PODANA PRZEZ UZYTKOWNIKA" + "\n"
                    + "7 - WYSWIETL PRODUKTY KTORE ZAMOWIL KLIENT WYBRANY PRZEZ UZYTKOWNIKA" +
                    "(POGRUPOWANE WEDLUG PRODUCENTA PRODUKTU)" + "\n"
                    + "8 - STATYSTYKA KLIENTOW KOTORZY ZAMOWILI CO NAJMNIEJ JEDEN PRODUKT Z TEGO SAMEGO KRAJU CO KLIENT" + "\n"
                    + "9 - WYJSCIE Z MENU DANYCH STATYSTYCZNYCH" + "\n");
            System.out.println("PODAJ OPCJE Z KTOREJ CHCESZ SKORZYSTAC");
            int optionAdd = DataFromUserService.getInt(9);
            switch (optionAdd) {
                case 1 -> menuHelpService.categoryMostExpensiveProducts();
                case 2 -> menuHelpService.productsFiltredByAgeAndCountry();
                case 3 -> menuHelpService.productsByGuaranteeComponents();
                case 4 -> menuHelpService.shopsProductsCountriesDifferences();
                case 5 -> menuHelpService.producersFiltredByDataFromUser();
                case 6 -> menuHelpService.customerOdersFiltredByDataFromUser();
                case 7 -> menuHelpService.productsFiltredByDataFromUser();
                case 8 -> menuHelpService.utilsCustomersWithIdenticalCountriesLikeProducts();
                case 9 -> continuation = false;
            }
        }
    }

    private void errorsStatisticMenuManagement() {
        boolean continuation = true;
        while (continuation) {
            System.out.println("MENU DANYCH STATYSTYCZNYCH:");
            System.out.println("1 - WYSWIETL NAZWE TABELI KTORA GENEROWALA NAJWIECEJ BLEDOW" + "\n"
                    + "2 - WYSWIETL DATE DLA KTOREJ POWSTALO NAJWIECEJ BLEDOW" + "\n"
                    + "3 - WYSWIETL KOMUNIKAT KTORY NAJCZESCIEJ POJAWIAL SIE W TABELI BLEDOW" + "\n"
                    + "4 - WYJDZ Z MENU STATYSTYKI BLEDOW");
            int optionAdd = DataFromUserService.getInt(7);
            switch (optionAdd) {
                case 1 -> menuHelpService.utilsMostErrorsInTable();
                case 2 -> menuHelpService.utilsMostErrorsInDay();
                case 3 -> menuHelpService.utilsMostErrorsMessage();
                case 4 -> continuation = false;

            }
        }
    }

    static <T> String toJson(T t) {
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            return gson.toJson(t);

        } catch (Exception e) {
            throw new MyException(ExceptionCode.SERVICE, "to json exception");
        }
    }
}


