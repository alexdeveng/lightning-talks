package io.github.alexdeveng.talks.bddmockito;

public record Item(Long id, String name, int pricePerUnit, int quantity) {
    public Item increaseQuantity() {
       return new Item(id, name, pricePerUnit, quantity + 1);
    }
}
