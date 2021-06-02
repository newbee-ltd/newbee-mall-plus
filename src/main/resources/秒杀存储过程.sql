-- 创建名称为execute_seckill的存储过程
CREATE PROCEDURE `execute_seckill`(IN v_seckill_id bigint, IN v_user_id BIGINT,
                                   IN v_kill_time TIMESTAMP, OUT r_result INT)
BEGIN
		DECLARE
insert_count INT DEFAULT 0;
START TRANSACTION;
INSERT INTO tb_newbee_mall_seckill_success (seckill_id, user_id, create_time)
VALUES (v_seckill_id, v_user_id, v_kill_time);
SELECT ROW_COUNT()
INTO insert_count;
IF
(insert_count = 0) THEN
			ROLLBACK;
			SET
r_result = -1;
		ELSEIF
(insert_count < 0) THEN
			ROLLBACK ;
			SET
r_result = -2;
ELSE
update tb_newbee_mall_seckill
set seckill_num = seckill_num - 1
where seckill_id = v_seckill_id
  and seckill_num > 0
  and seckill_begin <= v_kill_time
  and seckill_end >= v_kill_time;
SELECT ROW_COUNT()
INTO insert_count;
IF
(insert_count = 0) THEN
				ROLLBACK;
				SET
r_result = 0;
			ELSEIF
(insert_count < 0) THEN
				ROLLBACK;
				SET
r_result = -2;
ELSE
				COMMIT;
			SET
r_result = 1;
END IF;
END IF;
END;

-- 查看名称为execute_seckill的存储过程
show procedure status where Name='execute_seckill';

-- 删除名称为execute_seckill的存储过程
drop procedure if exists execute_seckill;