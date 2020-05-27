package com.ruida.sharding.jdbc.learning;

import com.ruida.sharding.jdbc.learning.entity.TOrder;
import com.ruida.sharding.jdbc.learning.entity.User;
import com.ruida.sharding.jdbc.learning.mapper.TOrderMapper;
import com.ruida.sharding.jdbc.learning.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
class ApplicationTests {

	@Autowired
	private UserMapper userMapper;
	@Autowired
	private TOrderMapper tOrderMapper;

	@Test
	public void testSelect() {
		System.out.println(("----- selectAll method test ------"));
		List<User> userList = userMapper.selectList(null);
		Assert.assertEquals(5, userList.size());
		userList.forEach(System.out::println);
	}

	@Test
	public void testInsert() {
		TOrder tOrder = new TOrder();
		tOrder.setUserId(0L);
		tOrder.setDescr("测试");
		tOrderMapper.insert(tOrder);
		tOrderMapper.insert(tOrder);
		tOrderMapper.insert(tOrder);
		tOrderMapper.insert(tOrder);
		tOrderMapper.insert(tOrder);

		//设置数据源
		List<TOrder> TOrders = tOrderMapper.selectList(null);
		log.info("orders:{}", TOrders);
	}
}
