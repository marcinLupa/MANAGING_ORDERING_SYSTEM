package dto.mapper;


import dto.*;
import model.*;

import java.util.HashSet;

public interface ModelMapper {

    static CategoryDto fromCategoryToCategoryDto(Category category) {
        return category == null ? null : CategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }

    static Category fromCategoryDtoToCategory(CategoryDto categoryDto) {
        return categoryDto == null ? null : Category.builder()
                .id(categoryDto.getId())
                .name(categoryDto.getName())
                .products(new HashSet<>())
                .build();
    }

    static CountryDto fromCountryToCountryDto(Country country) {
        return country == null ? null : CountryDto.builder()
                .id(country.getId())
                .name(country.getName())
                .build();
    }

    static Country fromCountryDtoToCountry(CountryDto countryDto) {
        return countryDto == null ? null : Country.builder()
                .id(countryDto.getId())
                .name(countryDto.getName())
                .shops(new HashSet<>())
                .build();
    }

    static CustomerDto fromCustomerToCustomerDto(Customer customer) {
        return customer == null ? null : CustomerDto.builder()
                .id(customer.getId())
                .age(customer.getAge())
                .countryDto(customer.getCountry() == null ? null : fromCountryToCountryDto(customer.getCountry()))
                .name(customer.getName())
                .surname(customer.getSurname())
                .build();
    }

    static Customer fromCustomerDtoToCustomer(CustomerDto customerDto) {
        return customerDto == null ? null : Customer.builder()
                .id(customerDto.getId())
                .age(customerDto.getAge())
                .country(customerDto.getCountryDto() == null ? null : fromCountryDtoToCountry(customerDto.getCountryDto()))
                .name(customerDto.getName())
                .surname(customerDto.getSurname())
                .customerOrders(new HashSet<>())
                .build();
    }

    static CustomerOrderDto fromCustomerOrderToCustomerOrderDto(CustomerOrder customerOrder) {
        return customerOrder == null ? null : CustomerOrderDto.builder()
                .id(customerOrder.getId())
                .customerDto(customerOrder.getCustomer() == null ? null : fromCustomerToCustomerDto(customerOrder.getCustomer()))
                .date(customerOrder.getDate())
                .discount(customerOrder.getDiscount())
                .paymentDto(customerOrder.getPayment() == null ? null : fromPaymentToPaymentDto(customerOrder.getPayment()))
                .productDto(customerOrder.getProduct() == null ? null : fromProductToProductDto(customerOrder.getProduct()))
                .quantity(customerOrder.getQuantity())
                .build();
    }

    static CustomerOrder fromCustomerOrderDtoToCustomerOrder(CustomerOrderDto customerOrderDto) {
        return customerOrderDto == null ? null : CustomerOrder.builder()
                .id(customerOrderDto.getId())
                .customer(customerOrderDto.getCustomerDto() == null ? null : fromCustomerDtoToCustomer(customerOrderDto.getCustomerDto()))
                .date(customerOrderDto.getDate())
                .discount(customerOrderDto.getDiscount())
                .payment(customerOrderDto.getPaymentDto() == null ? null : fromPaymentDtoToPayment(customerOrderDto.getPaymentDto()))
                .product(customerOrderDto.getProductDto() == null ? null : fromProductDtoToProduct(customerOrderDto.getProductDto()))
                .quantity(customerOrderDto.getQuantity())
                .build();
    }

    static PaymentDto fromPaymentToPaymentDto(Payment payment) {
        return payment == null ? null : PaymentDto.builder()
                .id(payment.getId())
                .payment(payment.getPayment())
                .build();
    }

    static Payment fromPaymentDtoToPayment(PaymentDto paymentDto) {
        return paymentDto == null ? null : Payment.builder()
                .id(paymentDto.getId())
                .payment(paymentDto.getPayment())
                .customerOrders(new HashSet<>())
                .build();
    }

    static ProducerDto fromProducerToProducerDto(Producer producer) {
        return producer == null ? null : ProducerDto.builder()
                .id(producer.getId())
                .name(producer.getName())
                .countryDto(producer.getCountry() == null ? null : fromCountryToCountryDto(producer.getCountry()))
                .tradeDto(producer.getTrade() == null ? null : fromTradeToTradeDto(producer.getTrade()))
                .build();
    }

    static Producer fromProducerDtoToProducer(ProducerDto producerDto) {
        return producerDto == null ? null : Producer.builder()
                .id(producerDto.getId())
                .name(producerDto.getName())
                .country(producerDto.getCountryDto() == null ? null : fromCountryDtoToCountry(producerDto.getCountryDto()))
                .trade(producerDto.getTradeDto() == null ? null : fromTradeDtoToTrade(producerDto.getTradeDto()))
                .products(new HashSet<>())
                .build();
    }

    static ProductDto fromProductToProductDto(Product product) {
        return product == null ? null : ProductDto.builder()
                .id(product.getId())
                .categoryDto(product.getCategory() == null ? null : fromCategoryToCategoryDto(product.getCategory()))
                .name(product.getName())
                .price(product.getPrice())
                .producerDto(product.getProducer() == null ? null : fromProducerToProducerDto(product.getProducer()))
                .enumGuaranteeComponents(product.getEnumGuaranteeComponents())
                .build();
    }

    static Product fromProductDtoToProduct(ProductDto productDto) {
        return productDto == null ? null : Product.builder()
                .id(productDto.getId())
                .category(productDto.getCategoryDto() == null ? null : fromCategoryDtoToCategory(productDto.getCategoryDto()))
                .name(productDto.getName())
                .price(productDto.getPrice())
                .producer(productDto.getProducerDto() == null ? null : fromProducerDtoToProducer(productDto.getProducerDto()))
                .enumGuaranteeComponents(productDto.getEnumGuaranteeComponents())
                .stocks(new HashSet<>())
                .build();
    }

    static ShopDto fromShopToshopDto(Shop shop) {
        return shop == null ? null : ShopDto.builder()
                .id(shop.getId())
                .countryDto(shop.getCountry() == null ? null : fromCountryToCountryDto(shop.getCountry()))
                .name(shop.getName())
                .build();
    }

    static Shop fromShopDtoToShop(ShopDto shopDto) {
        return shopDto == null ? null : Shop.builder()
                .id(shopDto.getId())
                .country(shopDto.getCountryDto() == null ? null : fromCountryDtoToCountry(shopDto.getCountryDto()))
                .name(shopDto.getName())
                .stocks(new HashSet<>())
                .build();
    }

    static StockDto fromStockToStockDto(Stock stock) {
        return stock == null ? null : StockDto.builder()
                .id(stock.getId())
                .product(stock.getProduct() == null ? null : fromProductToProductDto(stock.getProduct()))
                .quantity(stock.getQuantity())
                .shopDto(stock.getShop() == null ? null : fromShopToshopDto(stock.getShop()))
                .build();
    }

    static Stock fromStockDtoToStock(StockDto stockDto) {
        return stockDto == null ? null : Stock.builder()
                .id(stockDto.getId())
                .product(stockDto.getProduct() == null ? null : fromProductDtoToProduct(stockDto.getProduct()))
                .quantity(stockDto.getQuantity())
                .shop(stockDto.getShopDto() == null ? null : fromShopDtoToShop(stockDto.getShopDto()))
                .build();
    }

    static TradeDto fromTradeToTradeDto(Trade trade) {
        return trade == null ? null : TradeDto.builder()
                .id(trade.getId())
                .name(trade.getName())
                .build();
    }

    static Trade fromTradeDtoToTrade(TradeDto tradeDto) {
        return tradeDto == null ? null : Trade.builder()
                .id(tradeDto.getId())
                .name(tradeDto.getName())
                .producers(new HashSet<>())
                .build();
    }

    static ErrorsDto fromErrorsToErrorsDto(Errors errors) {
        return errors == null ? null : ErrorsDto.builder()
                .message(errors.getMessage())
                .date(errors.getDate())
                .build();
    }

    static Errors fromErrorsDtoToErrors(ErrorsDto errorsDto) {
        return errorsDto == null ? null : Errors.builder()
                .message(errorsDto.getMessage())
                .date(errorsDto.getDate())
                .build();
    }

    }
