/*
package com.ps.service.impl;

import com.ps.domain.User;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@RunWith(SpringRunner.class)
public class UserServiceImplTest {

    @Autowired
    private UserServiceImpl userService;


    // @Test(expected = RuntimeException.class)
    public void testAddMoney() {
        Integer money = 20000;
        String idcard = "330683200012205043";

        Assert.assertNotNull("null", money);
        Assert.assertNotNull("idcanrdnull", idcard);

        userService.addMoney(money, idcard);

    }


    // @Test(expected = NullPointerException.class)
    public void testIsExistIphone() {
        User user = userService.isExistIphone("13957546280");
        System.out.println(user);

        Assert.assertNotNull(user);
    }



    */
/*@Test(expected = RuntimeException.class)
    public void testLogin1() {
        User user = new User();
        user.setCode("1111");

        System.out.println(user);

        userService.login(user, 3);
    }

    // @Test(expected = RuntimeException.class)
    public void testLogin2() {
        User user = new User();
        user.setIphone("1111111111111");

        userService.login(user, 3);

    }

    @Test
    public void testLogin3() {
        User user = new User();
        user.setCode("1111");
        user.setIphone("1111111111111");

        userService.login(user, 3);
    }*//*


}*/
