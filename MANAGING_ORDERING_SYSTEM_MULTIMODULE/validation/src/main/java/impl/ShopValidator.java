

package impl;

import exceptions.MyException;
import generic.Validator;
import impl.ShopRepositoryImpl;
import model.Shop;
import repositories.ShopRepository;

import static exceptions.ExceptionCode.SHOPS;

public class ShopValidator implements Validator<Shop> {

    @Override
    public void validate(Shop shop) {
        ShopRepository shopRepository=new ShopRepositoryImpl();
        if (shopRepository.findAll()
                .stream().anyMatch(x -> x.getName().equals(shop.getName())
                        && x.getCountry().getName().equals(shop.getCountry().getName()))) {
            throw new MyException(SHOPS, "ADD SHOP EXCEPTION - SHOP IS ALLREADY IN TABLE");
        }
    }
}
