insert into events(seller_id, created_by, event_type, event_title, event_descript, event_start_at, event_end_at, event_banner_img, event_detail_img, event_status, created_at, updated_at, is_deleted)
values (1, 'SELLER', 'RAFFLE', '러버덕 할로윈 콘테스트', '러버덕 할로윈 콘테스트 2022', '2022-10-30 15:00:00', '2022-11-30 22:00:00', 'https://contents.lotteon.com/display/dshoplnk/12905/2/M001402/278229/P8CB1E6B4CAEFE2760EBE65F00A06849FBE13305B33EA0EC1AC9A578E79E7E109/file/dims/optimize', 'https://contents.lotteon.com/ec/public/P1B8BDC87A8EBE485D7E4C11436BCD0620D7AD633381B31FA5B53714620E734D9/file', 0,'2022-11-15 09:00:00', '2022-11-16 11:00:00', 0);
insert into events(seller_id, created_by, event_type, event_title, event_descript, event_start_at, event_end_at, event_banner_img, event_detail_img, event_status, created_at, updated_at, is_deleted)
values (2, 'SELLER', 'RAFFLE', '라페스타 세일 UP TO 80%', 'LA FESTA를 즐기기 위한 준비', '2022-11-08 15:00:00', '2022-11-30 22:00:00', 'https://contents.lotteon.com/display/dshop/41845/PBA61D8ABA4A3AE91B28E7A34F3B6B909B64BD4E8ED5F75E669FF0832C32FACA1/file', 'https://contents.lotteon.com/ec/public/P550F79770C9B2A35CF9329CA0F879AE81999250ABBCE7452DAA18BF56D78364B/file', 0, '2022-11-16 11:00:00','2022-11-16 13:00:00', 0);
insert into events(seller_id, created_by, event_type, event_title, event_descript, event_start_at, event_end_at, event_banner_img, event_detail_img, event_status, created_at, updated_at, is_deleted)
values (2, 'SELLER', 'FCFS', 'LOTTE 선착순 래플 이벤트', '바로 이곳이 브랜드 컬렉션', '2022-11-01 20:00:00', '2022-11-02 20:50:00', 'https://contents.lotteon.com/display/dshop/43932/PE5649CB6BADF5069F7CD6125D126865125D9EDF5EFA6EAD420A61716312E2E5E/file', 'https://contents.lotteon.com/ec/public/P9E8EDB7D41DB776A67E68406C992D9DF67F36B5E94B8BA8B90A729A735707CE1/file', 0,'2022-11-16 11:00:00', '2022-11-16 11:50:00', 0);
insert into events(seller_id, created_by, event_type, event_title, event_descript, event_start_at, event_end_at, event_banner_img, event_detail_img, event_status, created_at, updated_at, is_deleted)
values (1, 'SELLER', 'RAFFLE', '요리하다 구매고객 비스포크 추첨 증정 행사', '지금만 드리는 혜택 놓치지마세요!', '2022-10-28 20:00:00', '2022-11-05 20:50:00', 'https://contents.lotteon.com/display/dshoplnk/12908/207/M000010/282011/P85148A5C7FABAB042D95126A67DAB7D6C14E9502B82676811754E78209CBD91B/file/dims/optimize', 'https://contents.lotteon.com/ec/public/P305972B21565D707552257D357C0863ABAC4BC7267BE647AA23ADC076B782FFA/file', 0, '2022-11-17 09:00:00', '2022-11-18 13:00:00', 0);

insert into event_prizes(event_id, prize_type, product_id, stock, prize_name) values (1, 'PRODUCT', 1, 50,"한우 국밥");
insert into event_prizes(event_id, prize_type, product_id, stock, prize_name) values (1, 'PRODUCT', 2, 50,"돼지 국밥");

insert into event_prizes(event_id, prize_type, coupon_id, stock, prize_name) values (2, 'COUPON', 1, 100,'할로윈 기념 쿠폰');
insert into event_prizes(event_id, prize_type, product_id, stock, prize_name) values (2, 'PRODUCT', 3, 50,'순대 국밥');
insert into event_prizes(event_id, prize_type, product_id, stock, prize_name) values (2, 'PRODUCT', 4, 50,'차돌 짬뽕 국밥');

insert into event_prizes(event_id, prize_type, product_id, stock, prize_name) values (3, 'PRODUCT', 5, 10,'비빔 냉면');

insert into event_prizes(event_id, prize_type, coupon_id, stock,prize_name) values (4, 'COUPON', 1, 10,'크리스마스 겸 연말 쿠폰');
insert into event_prizes(event_id, prize_type, product_id, stock, prize_name) values (4, 'PRODUCT', 5, 10,'물냉면');

INSERT INTO event_participants(event_id, event_time_start_at,event_prize_id, user_id) VALUES(1,now(),1,3);
INSERT INTO event_participants(event_id, event_time_start_at,event_prize_id, user_id) VALUES(1,now(),2,3);
INSERT INTO event_participants(event_id, event_time_start_at,event_prize_id, user_id) VALUES(1,now(),2,3);
INSERT INTO event_participants(event_id, event_time_start_at,event_prize_id, user_id) VALUES(2,now(),3,3);
INSERT INTO event_participants(event_id, event_time_start_at,event_prize_id, user_id) VALUES(2,now(),4,3);
INSERT INTO event_participants(event_id, event_time_start_at,event_prize_id, user_id) VALUES(2,now(),5,3);