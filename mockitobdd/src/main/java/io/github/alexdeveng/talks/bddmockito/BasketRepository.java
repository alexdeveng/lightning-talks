package io.github.alexdeveng.talks.bddmockito;

import java.util.Optional;

public interface BasketRepository {
    void insert(Item item);

    void update(Item item);

    Optional<Item> findById(Long id);
}
