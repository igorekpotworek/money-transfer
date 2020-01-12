package igorekpotworek.transfer.controller;

import igorekpotworek.transfer.service.AccountNotFoundException;
import io.micronaut.context.annotation.Requires;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.hateoas.JsonError;
import io.micronaut.http.server.exceptions.ExceptionHandler;

import javax.inject.Singleton;

@Produces
@Singleton
@Requires(classes = {AccountNotFoundException.class, ExceptionHandler.class})
public class AccountNotFoundExceptionHandler implements ExceptionHandler<AccountNotFoundException, HttpResponse<?>> {

    @Override
    public HttpResponse<?> handle(HttpRequest request, AccountNotFoundException exception) {
        return HttpResponse.notFound(new JsonError(exception.getMessage()));
    }
}
