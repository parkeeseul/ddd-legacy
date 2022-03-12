# 키친포스

## 요구 사항
- 키친포스를 구현한다.
- 메뉴 (Menu)
  - [x] 메뉴의 가격이 옳바르지 않으면 메뉴를 등록할 수 없다.
    - [x] 메뉴의 가격은 반드시 입력해야 한다.
    - [x] 메뉴의 가격은 0원 이상이여야 한다.
    - [x] 메뉴의 가격은 메뉴 상품에 속해 있는 상품들의 총 가격의 합보다 클 수 없다.
  - [x] 메뉴의 이름이 옳바르지 않으면 메뉴를 등록할 수 없다.
    - [x] 메뉴의 이름이 입력되지 않으면 메뉴를 등록할 수 없다. 
    - [x] 메뉴 이름에 나쁜말이 포함되어 있으면 메뉴를 등록할 수 없다.
  - [x] 메뉴 전시 여부를 설정할 수 있다.
  - [x] 메뉴에는 여러 메뉴 상품들이 속해 있을 수 있다.
    - [x] 메뉴 상품이 없으면 메뉴를 등록할 수 없다.
  - [x] 메뉴는 하나의 메뉴 그룹에 속해 있어야 한다.
  - [x] 등록한 메뉴의 정보를 변경할 수 있다.
    - [x] 등록한 메뉴의 변경할 가격이 옳바르지 않을 경우 가격을 변경할 수 없다.
      - [x] 변경할 가격이 존재하지 않을 경우 가격을 변경할 수 없다.
      - [x] 변경할 가격이 0 보다 작을 경우 가격을 변경할 수 없다.
      - [x] 변경할 가격이 메뉴에 있는 모든 상품 가격의 총 합보다 클 경우 가격을 수정할 수 없다.
    - [x] 등록된 메뉴의 가격을 변경할 수 있다.
    - [x] 등록된 메뉴의 전시 여부를 변경할 수 있다.
  - [x] 메뉴 정보를 조회할 수 있다.
- 메뉴 그룹 (MenuGroup)
  - [x] 메뉴 그룹의 이름이 옳바르지 않으면 메뉴 그룹을 등록할 수 없다.
    - [x] 이름은 필수로 입력해야 한다.
  - [x] 메뉴 그룹 정보를 조회할 수 있다.
- 메뉴 상품 (MenuProduct)
  - [x] 메뉴 상품의 상품 수량은 0 이하 일 수 없다.
- 주문 (Order)
  - [x] 주문을 등록할 수 없다.
    - [x] 주문할 메뉴가 없으면 주문을 등록할 수 없다.
    - [X] 전시되어 있지 않는 메뉴의 주문은 등록할 수 없다.
  - [ ] 주문을 등록할 수 있다.
    - [x] 주문을 하면 주문 대기 상태가 된다.
    - [x] 테이크 아웃 주문을 할 수 있다.
      - [x] 테이크 아웃 주문일 경우 주문한 메뉴당 수량은 0 미만일 수 없다.
    - [x] 배달 주문을 할 수 있다.
      - [x] 배달 주문 주문일 경우 주문한 메뉴당 수량은 0 미만일 수 없다.
      - [x] 배달 주소를 입력하지 않으면 배달 주문을 등록할 수 없다.
    - [x] 매장 내 식사 주문을 할 수 있다.
      - [x] 사용중인 주문 테이블일 경우 주문이 불가능 하다.
  - [x] 주문을 수락할 수 있다.
    - [x] 주문 대기 상태일 경우만 주문을 수락한다.
    - [x] 테이크 아웃 주문을 수락한다.
    - [x] 매장 내 주문을 수락한다.
    - [x] 배달 내 주문을 수락한다.
  - [x] 주문한 메뉴를 제공한다.
    - [x] 주문을 수락 했을 경우에만 주문을 제공할 수 있다.
  - [x] 배달 주문을 시작할 수 있다.
    - [x] 배달 주문일 경우에만 배달을 시작할 수 있다.
    - [x] 주문을 제공 했을 경우에만 배달 주문을 시작할 수 있다.
  - [x] 배달 주문을 완료시킬 수 있다.
    - [x] 배달중인 주문만 배달 완료가 가능하다.
  - [x] 주문을 완료 시킬 수 있다.
    - [x] 배달 주문을 완료 시킬 수 있다.
      - [x] 배달이 완료된 주문만 주문 완료 처리를 할 수 있다. 
    - [x] 테이크 아웃 주문을 완료 시킬 수 있다.
      - [x] 주문한 메뉴가 제공 되지 않으면 주문 완료 처리를 할 수 없다. 
    - [x] 매장 내 식사 주문을 완료 시킬 수 있다.
      - [x] 주문한 메뉴가 제공 되지 않으면 주문 완료 처리를 할 수 없다.
      - [x] 주문이 완료되면 주문 테이블이 사용 가능해진다.
  - [x] 주문 정보를 조회할 수 있다.
- 주문 테이블 (OrderTable)
  - [x] 주문 테이블의 이름이 옳바르지 않으면 주문 테이블을 등록할 수 없다.
    - [x] 주문 테이블의 이름이 입력되지 않으면 주문 테이블을 등록할 수 없다.ㄷ
    - [x] 주문 테이블의 이름은 빈값이 될 수 없다.
  - [x] 주문 테이블의 현재 사용여부를 변경할 수 있다.
  - [x] 주문이 완료되지 않은 테이블은 초기화 할 수 없다.
  - [x] 주문 테이블의 손님의 수를 변경 할 수 있다.
    - [x] 손님의 수는 항상 0이상 이여야 한다.
  - [x] 주문 테이블의 정보를 조회할 수 있다.
- 상품 (Product)
  - [x] 상품을 가격이 옳바르지 않으면 상품을 등록할 수 없다.
    - [x] 상품의 가격은 필수로 입력해야 한다.
    - [x] 상품의 가격은 0원 이상이여야 한다.
  - [x] 상품의 이름이 옳바르지 않으면 상품을 등록할 수 없다.
    - [x] 상품의 이름은 필수로 입력해야 한다.
    - [x] 상품의 이름에는 나쁜말이 포함될 수 없다.
  - [x] 생성된 상품의 가격을 변경할 수 있다.
    - [x] 상품의 가격을 변경했을 때 변경된 상품을 포함한 메뉴의 가격이 메뉴의 모든 상품의 가격을 더한 가격보다 작으면 메뉴를 전시하지 않는다.
  - [x] 상품 정보를 조회할 수 있다.

## 용어 사전

| 한글명 | 영문명 | 설명 |
| --- | --- | --- |
|  |  |  |

## 모델링
