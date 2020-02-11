package com.devexperts.rest;

import com.devexperts.account.Account;
import com.devexperts.account.AccountKey;
import com.devexperts.service.AccountService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AccountControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private AccountService accountService;

    @Before
    public void setUp() {
        accountService.createAccount(new Account(AccountKey.valueOf(1), "Test", "Test2", 4D));
        accountService.createAccount(new Account(AccountKey.valueOf(2), "Test", "Test2", 3D));
    }

    @Test
    public void testTransfer200() {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("sourceId", "1");
        map.add("targetId", "2");
        map.add("amount", "1");

        ResponseEntity<Void> response = restTemplate.postForEntity("/api/operations/transfer",
                new HttpEntity<>(map, new HttpHeaders()), Void.class);

        Assert.assertEquals(response.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void testTransfer400_1() {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("sourceId", "1");
        map.add("amount", "1");

        ResponseEntity<Void> response = restTemplate.postForEntity("/api/operations/transfer",
                new HttpEntity<>(map, new HttpHeaders()), Void.class);

        Assert.assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    public void testTransfer400_2() {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("sourceId", "1");
        map.add("targetId", "2");
        map.add("amount", "0");

        ResponseEntity<Void> response = restTemplate.postForEntity("/api/operations/transfer",
                new HttpEntity<>(map, new HttpHeaders()), Void.class);

        Assert.assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    public void testTransfer404() {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("sourceId", "1");
        map.add("targetId", "3");
        map.add("amount", "1");

        ResponseEntity<Void> response = restTemplate.postForEntity("/api/operations/transfer",
                new HttpEntity<>(map, new HttpHeaders()), Void.class);

        Assert.assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
    }

    @Test
    public void testTransfer500() {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("sourceId", "1");
        map.add("targetId", "2");
        map.add("amount", "10");

        ResponseEntity<Void> response = restTemplate.postForEntity("/api/operations/transfer",
                new HttpEntity<>(map, new HttpHeaders()), Void.class);

        Assert.assertEquals(response.getStatusCode(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
