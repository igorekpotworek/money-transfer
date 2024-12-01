package revolut.transfer.controller;

import jakarta.inject.Singleton;
import revolut.transfer.service.InsufficientBalanceException;
import io.micronaut.context.annotation.Requires;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.hateoas.JsonError;
import io.micronaut.http.server.exceptions.ExceptionHandler;


@Produces
@Singleton
@Requires(classes = {InsufficientBalanceException.class, ExceptionHandler.class})
public class InsufficientBalanceExceptionHandler implements ExceptionHandler<InsufficientBalanceException, HttpResponse<?>> {

    @Override
    public HttpResponse<?> handle(HttpRequest request, InsufficientBalanceException exception) {
        return HttpResponse.unprocessableEntity().body(new JsonError(exception.getMessage()));
    }
}

