package service;

import dto.*;
import dto.mapper.ModelMapper;
import exceptions.ExceptionCode;
import exceptions.MyException;
import lombok.RequiredArgsConstructor;
import model.*;
import repositories.*;
import utils.EPayment;

import static exceptions.ExceptionCode.*;

@RequiredArgsConstructor
public class AddingToDateBaseService {
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


    public CustomerDto addCustomerWithCountry(CustomerDto customerDto) {

        if (customerDto == null) {
            throw new MyException(CUSTOMERS, "ADD CUSTOMER EXCEPTION - CUSTOMER IS NULL");
        }

        if (customerDto.getCountryDto() == null) {
            throw new MyException(ExceptionCode.COUNTRIES, "ADD CUSTOMER EXCEPTION - COUNTRY IS NULL");
        }
        if (customerDto.getCountryDto().getName().isEmpty() || customerDto.getCountryDto().getName() == null) {
            throw new MyException(ExceptionCode.COUNTRIES, "ADD CUSTOMER EXCEPTION - COUNTRY NAME IS NULL OR EMPTY");
        }

        String countryName = customerDto.getCountryDto().getName();

        Country country = countryRepository
                .findByName(countryName)
                .orElseGet(() -> countryRepository
                        .addOrUpdate(Country.builder().name(countryName).build())
                        .orElseThrow(() -> new MyException(COUNTRIES, "ADD CUSTOMER EXCEPTION")));

        Customer customer = ModelMapper.fromCustomerDtoToCustomer(customerDto);
        customer.setCountry(country);
        customerRepository.addOrUpdate(customer);

        return ModelMapper.fromCustomerToCustomerDto(customerRepository
                .findLast()
                .orElseThrow(() -> new MyException(CUSTOMERS, "CUSTOMER LAST FIND EXCEPTION")));


    }

    ShopDto addShopWithCountry(ShopDto shopDto) {
        if (shopDto == null) {
            throw new MyException(ExceptionCode.SHOPS, "ADD SHOP IS NULL EXCEPTION");
        }
        if (shopDto.getCountryDto() == null) {
            throw new MyException(ExceptionCode.COUNTRIES, "ADD SHOP - COUNTRY NULL EXCEPTION");
        }
        String countryName = shopDto.getCountryDto().getName();
        Long countryId = shopDto.getCountryDto().getId();

        if (countryId == null && countryName == null) {
            throw new MyException(ExceptionCode.COUNTRIES, "ADD SHOP - COUNTRY ID OR NAME NULL EXCEPTION");
        }
        Country country = countryRepository
                .findByName(countryName)
                .orElseGet(() -> countryRepository
                        .addOrUpdate(Country.builder().name(countryName).build())
                        .orElseThrow(() -> new MyException(COUNTRIES, "ADD SHOP - COUNTRY EXCEPTION")));

        Shop shop = ModelMapper.fromShopDtoToShop(shopDto);
        shop.setCountry(country);
        shopRepository.addOrUpdate(shop);

        return ModelMapper.fromShopToshopDto(shopRepository
                .findLast()
                .orElseThrow(() -> new MyException(SHOPS, "CUSTOMER LAST FIND EXCEPTION")));
    }

    ProducerDto addProducerWithCountryAndTrade(ProducerDto producerDto) {
        if (producerDto == null) {
            throw new MyException(PRODUCERS, "ADD PRODUCER EXCEPTION - PRODUCER IS NULL");
        }
        if (producerDto.getCountryDto() == null) {
            throw new MyException(ExceptionCode.COUNTRIES, "ADD PRODUCER EXCEPTION - COUNTRY IS NULL");
        }
        if (producerDto.getTradeDto() == null) {
            throw new MyException(ExceptionCode.TRADES, "ADD PRODUCER EXCEPTION - TRADE IS NULL");
        }
        Long countryId = producerDto.getCountryDto().getId();
        String countryName = producerDto.getCountryDto().getName();

        if (countryId == null && countryName == null) {
            throw new MyException(ExceptionCode.COUNTRIES, "ADD PRODUCER COUNTRY ID OR NAME IS NULL EXCEPTION");
        }

        Country country = countryRepository
                .findByName(countryName)
                .orElseGet(() -> countryRepository
                        .addOrUpdate(Country.builder().name(countryName).build())
                        .orElseThrow(() -> new MyException(COUNTRIES, "ADD PRODUCER ADD COUNTRY EXCEPTION")));

        Long tradeId = producerDto.getTradeDto().getId();
        String tradeName = producerDto.getTradeDto().getName();

        if (tradeId == null && tradeName == null) {
            throw new MyException(TRADES, "ADD PRODUCER TRADE ID OR NAME IS NULL EXCEPTION");
        }

        Trade trade = tradeRepository.findByName(tradeName).orElse(null);
        if (trade == null) {
            trade = tradeRepository
                    .addOrUpdate(Trade.builder().name(tradeName).build())
                    .orElseThrow(() -> new MyException(TRADES, "ADD PRODUCER ADD TRADE EXCEPTION"));
        }
        Producer producer = ModelMapper.fromProducerDtoToProducer(producerDto);

        producer.setCountry(country);
        producer.setTrade(trade);
        producerRepository.addOrUpdate(producer);
        return ModelMapper.fromProducerToProducerDto(producerRepository
                .findLast()
                .orElseThrow(() -> new MyException(PRODUCERS, "CUSTOMER LAST FIND EXCEPTION")));

    }

    ProductDto addProductWithCategoryAndProducer(ProductDto productDto) {
        if (productDto == null) {
            throw new MyException(PRODUCTS, "ADD PRODUCT WITH CATEGORY AND PRODUCER EXCEPTION");
        }
        if (productDto.getCategoryDto() == null) {
            throw new MyException(CATEGORIES, "ADD PRODUCT EXCEPTION - CATEGORY IS NULL");
        }
        if (productDto.getProducerDto() == null) {
            throw new MyException(PRODUCERS, "ADD PRODUCT EXCEPTION - PRODUCER IS NULL");
        }
        if (productDto.getEnumGuaranteeComponents() == null) {
            throw new MyException(ExceptionCode.GUARANTEE_COMPONENTS, "ADD PRODUCT EXCEPTION - GUARANTEE_COMPONENTS ARE NULL");
        }

        Long categoryId = productDto.getCategoryDto().getId();
        String categoryName = productDto.getCategoryDto().getName();

        if (categoryId == null && categoryName == null) {
            throw new MyException(CATEGORIES, "ADD PRODUCT CATEGORY ID OR NAME IS NULL EXCEPTION");
        }
        Category category = categoryRepository.findByName(categoryName).orElse(null);
        if (category == null) {
            category = categoryRepository
                    .addOrUpdate(Category.builder().name(categoryName).build())
                    .orElseThrow(() -> new MyException(CATEGORIES, "ADD PRODUCT CATEGORY EXCEPTION - ADDING CATEGORY"));
        }

        Long prodcuerId = productDto.getProducerDto().getId();
        String producerName = productDto.getProducerDto().getName();
        String producerCountryName = productDto.getProducerDto().getCountryDto().getName();

        if (prodcuerId == null && producerName == null) {
            throw new MyException(PRODUCERS, "ADD PRODUCT PRODUCER ID OR NAME IS NULL EXCEPTION");
        }
        Producer producer = producerRepository
                .findAll()
                .stream()
                .filter(x -> x.getName().equals(producerName)
                        && x.getCountry().getName().equals(producerCountryName))
                .findAny()
                .orElseThrow(() -> new MyException(PRODUCERS, "ADD PRODUCT - PRODUCER OUT OF BASE"));

        Product product = ModelMapper.fromProductDtoToProduct(productDto);
        product.setCategory(category);
        product.setProducer(producer);
        product.setEnumGuaranteeComponents(productDto.getEnumGuaranteeComponents());

        productRepository.addOrUpdate(product);

        return ModelMapper.fromProductToProductDto(productRepository
                .findLast()
                .orElseThrow(() -> new MyException(PRODUCTS, "CUSTOMER LAST FIND EXCEPTION")));
    }

    StockDto addStockWithProductAndCategory(StockDto stockDto) {
        if (stockDto == null) {
            throw new MyException(ExceptionCode.STOCKS, "ADD STOCK IS NULL EXCEPTION");
        }
        if (stockDto.getProduct() == null) {
            throw new MyException(ExceptionCode.PRODUCTS, "ADD STOCK PRODUCT IS NULL EXCEPTION");
        }
        if (stockDto.getShopDto() == null) {
            throw new MyException(ExceptionCode.SHOPS, "ADD STOCK SHOP IS NULL EXCEPTION");
        }
        //PRODUCT
        String productName = stockDto.getProduct().getName();
        Long productId = stockDto.getProduct().getId();
        String productCategoryName = stockDto.getProduct().getCategoryDto().getName();

        if (productId == null && productName == null) {
            throw new MyException(ExceptionCode.PRODUCTS, "ADD PRODUCT ID OR NAME IS NULL EXCEPTION");
        }
        Product product = productRepository
                .findAll().stream()
                .filter(x -> x.getName().equals(productName) && x.getCategory().getName().equals(productCategoryName))
                .findAny()
                .orElseThrow(() -> new MyException(PRODUCTS, "ADD STOCK - PRODUCT OUT OF BASE"));
        //SHOP
        String shopName = stockDto.getShopDto().getName();
        Long shopId = stockDto.getShopDto().getId();

        String shopCountryName = stockDto.getShopDto().getCountryDto().getName();
        if (shopId == null && shopName == null) {
            throw new MyException(ExceptionCode.SHOPS, "ADD STOCK SHOP ID OR NAME IS NULL EXCEPTION");
        }
        Shop shop = shopRepository
                .findAll()
                .stream()
                .filter(x -> x.getName().equals(shopName) && x.getCountry().getName().equals(shopCountryName))
                .findAny()
                .orElseThrow(() -> new MyException(PRODUCTS, "ADD STOCK - SHOP OUT OF BASE"));
        //ADD QUANTITY
        if (stockRepository
                .findAll()
                .stream()
                .anyMatch(x -> x.getProduct().getName().equals(productName)
                        && x.getProduct().getCategory().getName().equals(productCategoryName)
                        && x.getShop().getName().equals(shopName)
                        && x.getShop().getCountry().getName().equals(shopCountryName))) {

            stockRepository
                    .findAll()
                    .stream()
                    .filter(x -> x.getProduct().getName().equals(productName)
                            && x.getProduct().getCategory().getName().equals(productCategoryName)
                            && x.getShop().getName().equals(shopName)
                            && x.getShop().getCountry().getName().equals(shopCountryName))
                    .findFirst()
                    .ifPresent(duplicatStock -> stockRepository
                            .addOrUpdate(Stock
                                    .builder()
                                    .id(duplicatStock.getId())
                                    .product(duplicatStock.getProduct())
                                    .shop(duplicatStock.getShop())
                                    .quantity(duplicatStock.getQuantity() + stockDto.getQuantity())
                                    .build()));
        }
        //STOCK
        Stock stock = ModelMapper.fromStockDtoToStock(stockDto);
        stock.setProduct(product);
        stock.setShop(shop);
        stockRepository.addOrUpdate(stock);

        return ModelMapper.fromStockToStockDto(stockRepository
                .findLast()
                .orElseThrow(() -> new MyException(STOCKS, "STOCK LAST FIND EXCEPTION")));
    }

    CustomerOrderDto addCustomerOrderWithCustomerCategoryPayment(CustomerOrderDto customerOrderDto) {

        if (customerOrderDto == null) {
            throw new MyException(ExceptionCode.CUSTOMER_ORDERS, "ADD CUSTOMER ORDER EXCEPTION - CUSTOMER ORDER IS NULL");
        }
        if (customerOrderDto.getPaymentDto() == null) {
            throw new MyException(ExceptionCode.PAYMENTS, "ADD CUSTOMER ORDER EXCEPTION - PAYMENT IS NULL");
        }
        if (customerOrderDto.getProductDto() == null) {
            throw new MyException(ExceptionCode.PRODUCTS, "ADD CUSTOMER ORDER EXCEPTION - PRODUCT IS NULL");
        }
        if (customerOrderDto.getCustomerDto() == null) {
            throw new MyException(CUSTOMERS, "ADD CUSTOMER ORDER EXCEPTION -  CUSTOMER IS NULL");
        }
        //CUSTOMER
        Long customerId = customerOrderDto.getCustomerDto().getId();
        String customerName = customerOrderDto.getCustomerDto().getName();
        String customerSurname = customerOrderDto.getCustomerDto().getSurname();
        String customerCountryName = customerOrderDto.getCustomerDto().getCountryDto().getName();

        if (customerName == null
                && customerSurname == null
                && customerCountryName == null
                && customerId == null) {

            throw new MyException(CUSTOMERS, "ADD CUSTOMER ORDER - CUSTOMER NAME, SURNAME OR COUNTRY NAME IS NULL");
        }
        Customer customer = customerRepository
                .findAll()
                .stream()
                .filter(
                        x -> x.getName().equals(customerName)
                                && x.getSurname().equals(customerSurname)
                                && x.getCountry().getName().equals(customerCountryName))
                .findFirst().orElseThrow(() -> new MyException(CUSTOMERS, "ADD CUSTOMER ORDER - CUSTOMER OUT OF BASE"));

        //PAYMENT
        Long paymentId = customerOrderDto.getPaymentDto().getId();
        EPayment paymentName = customerOrderDto.getPaymentDto().getPayment();

        if (paymentName == null && paymentId == null) {
            throw new MyException(PAYMENTS, "ADD CUSTOMER ORDER EXCEPTION - PAYMENT NAME OR ID IS NULL");
        }
        Payment payment = paymentRepository.findByName(customerOrderDto.getPaymentDto().getPayment()).orElse(null);
        if (payment == null) {
            payment = paymentRepository
                    .addOrUpdate(Payment.builder().payment(customerOrderDto.getPaymentDto().getPayment()).build())
                    .orElseThrow(() -> new MyException(ExceptionCode.PAYMENTS, "ADD CUSTOMER ORDER EXCEPTION - ADD PAYMENT"));
        }
        //PRODUCT
        String productName = customerOrderDto.getProductDto().getName();
        Long productId = customerOrderDto.getProductDto().getId();

        if (productId == null && productName == null) {
            throw new MyException(ExceptionCode.PRODUCTS, "ADD CUSTOMER ORDER EXCEPTION - PRODUCT ID OR NAME NULL");
        }
        Product product = productRepository
                .findAll()
                .stream()
                .filter(x -> x.getName().equals(customerOrderDto.getProductDto().getName())
                        && x.getCategory().getName().equals(customerOrderDto.getProductDto().getCategoryDto().getName()))
                .findAny()
                .orElseThrow(() -> new MyException(PRODUCTS, "ADD CUSTOMER ORDER EXCEPTION - PRODUCT OUT OF BASE"));
        //CUSTOMER ORDER
        CustomerOrder customerOrder = ModelMapper.fromCustomerOrderDtoToCustomerOrder(customerOrderDto);
        customerOrder.setProduct(product);
        customerOrder.setPayment(payment);
        customerOrder.setCustomer(customer);

        customerOrderRepository.addOrUpdate(customerOrder);

        return ModelMapper.fromCustomerOrderToCustomerOrderDto(customerOrderRepository
                .findLast()
                .orElseThrow(() -> new MyException(CUSTOMER_ORDERS, "CUSTOMER_ORDER LAST FIND EXCEPTION")));
    }

    ErrorsDto addErrors(ErrorsDto errorsDto) {

        if (errorsDto == null) {
            throw new MyException(ExceptionCode.ERRORS, "ADD ERRORS EXCEPTION");
        }

        errorsRepository.addOrUpdate(ModelMapper.fromErrorsDtoToErrors(errorsDto));

        return ModelMapper.fromErrorsToErrorsDto(errorsRepository
                .findLast()
                .orElseThrow(() -> new MyException(ERRORS, "ERRORS LAST FIND EXCEPTION")));
    }

}
