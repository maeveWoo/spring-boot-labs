-- 프로시저 구문
DELIMITER $$
CREATE PROCEDURE whileProc()
BEGIN
    DECLARE i INT;
    SET i = 694;

    WHILE (i < 1487) DO
            # 구문~
update customer as c,
    (select * from customer order by customer_id) as t
set user_id = i;
SET i = i + 1;
            # where~
END WHILE;
END $$;
DELIMITER ;

-- 프로시저 호출
CALL whileProc();

-- 변수 사용 1.
UPDATE customer JOIN (SELECT @rank := 694) r
SET user_id=@rank:=@rank+1 # 695 부터 들어감

-- 변수 사용 2.
select @user_id:= 694;
UPDATE customer
SET user_id=@rank:=@rank+1 # 695 부터 들어감

-- JPA등을 이용해 코드로 데이터를 조작불가할때,..