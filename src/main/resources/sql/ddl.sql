create table labs.customer
(
    customerid   varchar(15) charset utf8 not null comment '고객id',
    customername varchar(50) charset utf8 not null comment '고객이름',
    customertype varchar(50) charset utf8 null comment '고객구분',
    country      varchar(50) charset utf8 null comment '국적',
    city         varchar(50) charset utf8 null comment '도시',
    state        varchar(50) charset utf8 null comment '주',
    postcode     int                      null comment '우편번호',
    regiontype   varchar(50) charset utf8 null comment '지역구분'
);

create table labs.`order`
(
    seq        int(10)                  not null comment '순번',
    orderid    varchar(15) charset utf8 not null comment '주문id',
    orderdate  datetime                 null comment '주문일자',
    shipdate   datetime                 null comment '배송일자',
    customerid varchar(15) charset utf8 null comment '고객id',
    productid  varchar(15) charset utf8 null comment '제품id',
    quantity   int(10)                  null comment '주문수량',
    discount   decimal(10,
                   2)                   null comment '할인금액'
);
create table labs.product
(
    productid   varchar(15) charset utf8  not null comment '제품id',
    bigcategory varchar(50) charset utf8  null comment '대분류명',
    subcaregory varchar(50) charset utf8  null comment '소분류명',
    productname varchar(100) charset utf8 not null comment '제품명',
    price       decimal(10,
                    2)                    null comment '가격'
);