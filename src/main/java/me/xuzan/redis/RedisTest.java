package me.xuzan.redis;

import me.xuzan.redis.conf.RedisConfig;
import me.xuzan.redis.domain.po.Department;
import me.xuzan.redis.domain.po.Role;
import me.xuzan.redis.domain.po.User;
import me.xuzan.redis.repository.UserRedis;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {RedisConfig.class, UserRedis.class})
@SpringBootTest(classes = Application.class)//启动项目测试
public class RedisTest {

    private static Logger logger = LoggerFactory.getLogger(RedisTest.class);

    @Autowired
    private UserRedis userRedis;

    @Before
    public void setup() {
        Department department = new Department();
        department.setName("开发部");

        Role role = new Role();
        role.setName("admin");

        User user = new User();
        user.setName("user");
        user.setCreateDate(new Date());
        user.setDepartment(department);

        List<Role> roles = new ArrayList<Role>();
        roles.add(role);

        user.setRoles(roles);

        userRedis.delete(this.getClass().getName() + ":userByName: " + user.getName());
        userRedis.add(this.getClass().getName() + ":userByName:" + user.getName(), 10L, user);
    }

    @Test
    public void get() {
        User user = userRedis.get(this.getClass().getName() + ":userByName:user");
        Assert.notNull(user, "未查询到用户信息");
        logger.info("=====user===== name:{}, department:{}, role:{}",
                user.getName(), user.getDepartment().getName(), user.getRoles().get(0).getName());
    }
}
