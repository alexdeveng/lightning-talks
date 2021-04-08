package io.github.alexdeveng.talks.bddmockito;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static io.github.alexdeveng.talks.bddmockito.TestItems.*;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
class MockitoBasketShould {

    @Mock
    BasketRepository basketRepository;

    @InjectMocks
    BasketService basketService;

    @Test
    void add_item() {
        //given
        Mockito.when(basketRepository.findById(BAGUETTE.id()))
                .thenReturn(Optional.empty());
        //when
        basketService.addItem(BAGUETTE);
        //then
        Mockito.verify(basketRepository)
                .insert(BAGUETTE);
    }

    @Test
    void should_in_order_add_items() {
        Mockito.when(basketRepository.findById(BAGUETTE.id()))
                .thenReturn(Optional.empty());
        Mockito.when(basketRepository.findById(CROISSANT.id()))
                .thenReturn(Optional.empty());

        basketService.addItem(BAGUETTE);
        basketService.addItem(CROISSANT);

        InOrder inOrder = BDDMockito.inOrder(basketRepository);
        inOrder.verify(basketRepository)
                .insert(BAGUETTE);
        inOrder.verify(basketRepository)
                .insert(CROISSANT);
    }

    @Test
    void increase_an_existing_item() {
        Mockito.when(basketRepository.findById(BAGUETTE.id()))
                .thenReturn(Optional.of(BAGUETTE));

        basketService.addItem(BAGUETTE);

        Mockito.verify(basketRepository,
                Mockito.atMostOnce())
                .update(BAGUETTE_X2);
        Mockito.verify(basketRepository,
                Mockito.never())
                .insert(BAGUETTE);
    }

    @Test
    void checking_not_iterations_with_mock() {
        Mockito.verify(basketRepository, never()).insert(BAGUETTE);
        Mockito.verify(basketRepository, never()).update(BAGUETTE);
        Mockito.verify(basketRepository, never()).findById(BAGUETTE.id());
    }
}
