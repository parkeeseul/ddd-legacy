package kitchenpos.application;

import kitchenpos.domain.*;
import kitchenpos.infra.KitchenridersClient;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static kitchenpos.fixture.KitchenposFixture.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceMockTest {

    @InjectMocks
    private OrderService orderService;

    @Mock
    private MenuRepository menuRepository;

    @Mock
    private OrderTableRepository orderTableRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private KitchenridersClient kitchenridersClient;

    private Order order;

    @DisplayName("주문할 메뉴가 없으면 주문을 등록할 수 없다.")
    @ParameterizedTest
    @NullAndEmptySource
    void invalidOrderMenuQuantity(List<OrderLineItem> orderLineItems) {
        order = orderEatIn();
        order.setOrderLineItems(orderLineItems);

        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("전시되어 있지 않은 메뉴의 주문은 등록할 수 없다.")
    @Test
    void unableDisplayMenu() {
        Menu menu = new Menu();
        menu.setDisplayed(false);
        order = orderEatIn(menu);

        when(menuRepository.findAllByIdIn(anyList())).thenReturn(Arrays.asList(menu));
        when(menuRepository.findById(any())).thenReturn(Optional.of(menu));

        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalStateException.class);
    }

    //    - [ ] 주문한 메뉴당 갯수는 1개 이상이여야 한다. -- 테이크 아웃, 배달 주문
    @DisplayName("테이크 아웃 주문일 경우 주문한 메뉴당 수량은 0 미만일 수 없다.")
    @Test
    void negativeQuantityTakeout() {
        MenuGroup menuGroup = menuGroup();
        Menu menu = menu(menuGroup, chickenProduct(), pastaProduct());

        order = orderTakeoutWithOrderItems(orderLineItem(menu, -1));

        when(menuRepository.findAllByIdIn(anyList())).thenReturn(Arrays.asList(menu));

        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("배달 주문 주문일 경우 주문한 메뉴당 수량은 0 미만일 수 없다.")
    @Test
    void negativeQuantityDelivery() {
        MenuGroup menuGroup = menuGroup();
        Menu menu = menu(menuGroup, chickenProduct(), pastaProduct());

        order = orderDeliveryWithOrderItems(orderLineItem(menu, -1));

        when(menuRepository.findAllByIdIn(anyList())).thenReturn(Arrays.asList(menu));

        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("배달 주소를 입력하지 않으면 배달 주문을 등록할 수 없다.")
    @ParameterizedTest
    @NullAndEmptySource
    void noAddress(String address) {
        MenuGroup menuGroup = menuGroup();
        Menu menu = menu(menuGroup, chickenProduct(), pastaProduct());

        order = orderDelivery(menu);
        order.setDeliveryAddress(address);

        when(menuRepository.findAllByIdIn(anyList())).thenReturn(Arrays.asList(menu));
        when(menuRepository.findById(any())).thenReturn(Optional.of(menu));

        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("매장내 주문시 사용중인 주문 테이블일 경우 주문이 불가능 하다.")
    @Test
    void fullOrderTable() {
        OrderTable orderTable = orderTable();
        orderTable.setEmpty(true);

        MenuGroup menuGroup = menuGroup();
        Menu menu = menu(menuGroup, chickenProduct(), pastaProduct());

        order = orderEatIn(menu);

        when(menuRepository.findAllByIdIn(anyList())).thenReturn(Arrays.asList(menu));
        when(menuRepository.findById(any())).thenReturn(Optional.of(menu));
        when(orderTableRepository.findById(any())).thenReturn(Optional.of(orderTable));

        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalStateException.class);
    }

    @DisplayName("주문 대기 상태가 아니면 주문을 수락할 수 없다.")
    @ParameterizedTest
    @EnumSource(value = OrderStatus.class, names = {"ACCEPTED", "SERVED", "DELIVERING", "DELIVERED", "COMPLETED"})
    void accept(OrderStatus status) {
        Order order = new Order();
        order.setStatus(status);

        when(orderRepository.findById(any())).thenReturn(Optional.of(order));

        assertThatThrownBy(() -> orderService.accept(order.getId()))
                .isInstanceOf(IllegalStateException.class);
    }

    @DisplayName("주문을 수락 했을 경우에만 주문을 제공할 수 있다.")
    @ParameterizedTest
    @EnumSource(value = OrderStatus.class, names = {"WAITING", "SERVED", "DELIVERING", "DELIVERED", "COMPLETED"})
    void serve(OrderStatus status) {
        Order order = new Order();
        order.setStatus(status);

        when(orderRepository.findById(any())).thenReturn(Optional.of(order));

        assertThatThrownBy(() -> orderService.serve(order.getId()))
                .isInstanceOf(IllegalStateException.class);
    }

    @DisplayName("배달 주문일 경우에만 배달을 시작할 수 있다.")
    @ParameterizedTest
    @EnumSource(value = OrderType.class, names = {"TAKEOUT", "EAT_IN"})
    void startDelivery(OrderType type) {
        Order order = new Order();
        order.setType(type);

        when(orderRepository.findById(any())).thenReturn(Optional.of(order));

        assertThatThrownBy(() -> orderService.startDelivery(order.getId()))
                .isInstanceOf(IllegalStateException.class);
    }

    @DisplayName("주문을 제공 했을 경우에만 배달 주문을 시작할 수 있다.")
    @ParameterizedTest
    @EnumSource(value = OrderStatus.class, names = {"WAITING", "ACCEPTED", "DELIVERING", "DELIVERED", "COMPLETED"})
    void serveDelivery(OrderStatus status) {
        Order order = new Order();
        order.setStatus(status);
        order.setType(OrderType.DELIVERY);

        when(orderRepository.findById(any())).thenReturn(Optional.of(order));

        assertThatThrownBy(() -> orderService.startDelivery(order.getId()))
                .isInstanceOf(IllegalStateException.class);
    }

    @DisplayName("배달 주문일 경우에만 배달을 완료할 수 있다.")
    @ParameterizedTest
    @EnumSource(value = OrderType.class, names = {"TAKEOUT", "EAT_IN"})
    void completeDelivery(OrderType type) {
        Order order = new Order();
        order.setType(type);

        when(orderRepository.findById(any())).thenReturn(Optional.of(order));

        assertThatThrownBy(() -> orderService.completeDelivery(order.getId()))
                .isInstanceOf(IllegalStateException.class);
    }

    @DisplayName("배달이 완료되지 않은 주문은 주문 완료 처리를 할 수 없다.")
    @ParameterizedTest
    @EnumSource(value = OrderStatus.class, names = {"WAITING", "ACCEPTED", "DELIVERING", "SERVED", "COMPLETED"})
    void deliveryOrderComplete(OrderStatus status) {
        Order order = new Order();
        order.setType(OrderType.DELIVERY);
        order.setStatus(status);

        when(orderRepository.findById(any())).thenReturn(Optional.of(order));

        assertThatThrownBy(() -> orderService.complete(order.getId()))
                .isInstanceOf(IllegalStateException.class);
    }

    @DisplayName("테이크 아웃 주문 시 주문한 메뉴가 제공 되지 않으면 주문 완료 처리를 할 수 없다.")
    @ParameterizedTest
    @EnumSource(value = OrderStatus.class, names = {"WAITING", "ACCEPTED", "DELIVERING", "DELIVERED", "COMPLETED"})
    void completeTakeout(OrderStatus status) {
        Order order = new Order();
        order.setType(OrderType.TAKEOUT);
        order.setStatus(status);

        when(orderRepository.findById(any())).thenReturn(Optional.of(order));

        assertThatThrownBy(() -> orderService.complete(order.getId()))
                .isInstanceOf(IllegalStateException.class);
    }

    @DisplayName("매장 내 주문 시 주문한 메뉴가 제공 되지 않으면 주문 완료 처리를 할 수 없다.")
    @ParameterizedTest
    @EnumSource(value = OrderStatus.class, names = {"WAITING", "ACCEPTED", "DELIVERING", "DELIVERED", "COMPLETED"})
    void completeEatIn(OrderStatus status) {
        Order order = new Order();
        order.setType(OrderType.EAT_IN);
        order.setStatus(status);

        when(orderRepository.findById(any())).thenReturn(Optional.of(order));

        assertThatThrownBy(() -> orderService.complete(order.getId()))
                .isInstanceOf(IllegalStateException.class);
    }
}