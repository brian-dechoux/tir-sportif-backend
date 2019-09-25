package com.tirsportif.backend;

import org.apache.shiro.authc.credential.PasswordService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TemporaryPasswordGenerator {

    @Autowired
    private PasswordService passwordService;

    @Test
    public void generate() {
        String encryptedPassword = passwordService.encryptPassword("password");
        System.out.println("Generated encrypted passsword: "+encryptedPassword);
    }

}
