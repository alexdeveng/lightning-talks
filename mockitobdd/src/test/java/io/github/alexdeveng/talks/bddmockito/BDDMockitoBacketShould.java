package io.github.alexdeveng.talks.bddmockito;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static io.github.alexdeveng.talks.bddmockito.TestItems.*;

@ExtendWith(MockitoExtension.class)
class BDDMockitoBacketShould {

    @Mock
    BasketRepository basketRepository;

    @InjectMocks
    BasketService basketService;

    @Test
    void add_item() {
        //given
        BDDMockito.given(basketRepository.findById(BAGUETTE.id()))
                .willReturn(Optional.empty());
        //when
        basketService.addItem(BAGUETTE);
        //then
        BDDMockito.then(basketRepository)
                .should()
                .insert(BAGUETTE);
    }

    @Test
    void should_in_order_add_items() {
        BDDMockito.given(basketRepository.findById(BAGUETTE.id()))
                .willReturn(Optional.empty());
        BDDMockito.given(basketRepository.findById(CROISSANT.id()))
                .willReturn(Optional.empty());

        basketService.addItem(BAGUETTE);
        basketService.addItem(CROISSANT);

        InOrder inOrder = BDDMockito.inOrder(basketRepository);
        BDDMockito.then(basketRepository)
                .should(inOrder)
                .insert(BAGUETTE);
        BDDMockito.then(basketRepository)
                .should(inOrder)
                .insert(CROISSANT);
    }

    @Test
    void increase_an_existing_item() {
        BDDMockito.given(basketRepository.findById(BAGUETTE.id()))
                .willReturn(Optional.of(BAGUETTE));

        basketService.addItem(BAGUETTE);

        BDDMockito.then(basketRepository)
                .should(BDDMockito.atMostOnce())
                .update(BAGUETTE_X2);
        BDDMockito.then(basketRepository)
                .shouldHaveNoMoreInteractions();
        BDDMockito.then(basketRepository)
                .should(BDDMockito.never())
                .insert(BAGUETTE);
    }

    @Test
    void checking_not_iterations_with_mock() {
        BDDMockito.then(basketRepository)
                .shouldHaveNoInteractions();
    }

    @Test
    void exists_item_in_basket() {
        BDDMockito.given(basketRepository.findById(BAGUETTE.id()))
                .willAnswer((onMock -> onMock
                        .getArgument(0).equals(BAGUETTE.id())
                                ? Optional.of(BAGUETTE)
                                : Optional.empty()));

        final boolean baguetteInBasket = basketService.existItem(BAGUETTE);
        final boolean cakeInBasket = basketService.existItem(CAKE);

        Assertions.assertTrue(baguetteInBasket);
        Assertions.assertFalse(cakeInBasket);
    }

    @Test
    void throw_exception_when_not_valid_item() {
        Item notValidItem = new Item(
                null, "DUKE", -500, 0);
        BDDMockito.willThrow(new RuntimeException())
                .given(basketRepository)
                .insert(notValidItem);

        Assertions.assertThrows(RuntimeException.class,
                () -> basketService.addItem(notValidItem));
    }
}
