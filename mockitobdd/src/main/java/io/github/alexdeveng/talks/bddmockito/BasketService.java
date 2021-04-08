package io.github.alexdeveng.talks.bddmockito;

public class BasketService {

    private final BasketRepository basketRepository;

    public BasketService(BasketRepository basketRepository) {
        this.basketRepository = basketRepository;
    }

    public void addItem(Item item) {
        var itemFound = basketRepository.findById(item.id());
        itemFound.ifPresentOrElse(
                i -> basketRepository.update(i.increaseQuantity()),
                () -> basketRepository.insert(item));
    }

    public boolean existItem(Item item) {
        return basketRepository.findById(item.id())
                .isPresent();
    }
}
