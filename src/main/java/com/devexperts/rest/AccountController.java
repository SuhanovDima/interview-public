package com.devexperts.rest;

import com.devexperts.account.Account;
import com.devexperts.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AccountController extends AbstractAccountController {

    @Autowired
    private AccountService accountService;

    @PostMapping("/operations/transfer")
    public ResponseEntity<Void> transfer(@RequestParam("sourceId") long sourceId,
                                         @RequestParam("targetId") long targetId,
                                         @RequestParam("amount") double amount) {
        Account source = accountService.getAccount(sourceId);
        Account target = accountService.getAccount(targetId);

        accountService.transfer(source, target, amount);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
