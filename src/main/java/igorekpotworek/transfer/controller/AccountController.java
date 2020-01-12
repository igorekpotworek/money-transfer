package igorekpotworek.transfer.controller;

import igorekpotworek.transfer.domain.Account;
import igorekpotworek.transfer.dto.AccountDto;
import igorekpotworek.transfer.service.AccountService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import lombok.RequiredArgsConstructor;

import javax.validation.Valid;

@Controller("${account.uri}")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @Post
    public HttpResponse<Account> create(@Body @Valid AccountDto accountDto) {
        var account = accountService.create(accountDto.getInitialBalance());
        return HttpResponse.created(account);
    }

    @Get("/{id}")
    public HttpResponse<Account> get(String id) {
        var account = accountService.findById(id);
        return HttpResponse.ok(account);
    }
}
