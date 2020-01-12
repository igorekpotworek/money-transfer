package igorekpotworek.transfer

import igorekpotworek.transfer.domain.Account
import igorekpotworek.transfer.dto.AccountDto
import io.micronaut.context.annotation.Value
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.test.annotation.MicronautTest
import spock.lang.Specification

import javax.inject.Inject

import static io.micronaut.http.HttpRequest.POST
import static io.micronaut.http.HttpStatus.*
import static java.util.UUID.randomUUID

@MicronautTest
class AccountTest extends Specification {

    @Inject
    @Client('/')
    HttpClient client

    @Value('${account.uri}')
    String accountUri

    def "should create account"() {
        given: "account with initial balance"
        def accountDto = new AccountDto(1000.0)

        when: "create account"
        def result = client.toBlocking().exchange(POST(accountUri, accountDto), Account)

        then: "request is successful"
        result.status() == CREATED
    }

    def "should return unprocessable entity error when try to create account with negative balance"() {
        given: "account with negative initial balance"
        def accountDto = new AccountDto(-1000.0)

        when: "try to create account"
        client.toBlocking().exchange(POST(accountUri, accountDto), Account)

        then: "unprocessable entity error is returned"
        def e = thrown(HttpClientResponseException)
        e.getStatus() == UNPROCESSABLE_ENTITY
    }

    def "should return unprocessable entity error when try to create account with null balance"() {
        given: "account with null initial balance"
        def accountDto = new AccountDto(null)

        when: "try to create account"
        client.toBlocking().exchange(POST(accountUri, accountDto), Account)

        then: "unprocessable entity error is returned"
        def e = thrown(HttpClientResponseException)
        e.getStatus() == UNPROCESSABLE_ENTITY
    }

    def "should return account"() {
        setup: "create account"
        def accountId = client.toBlocking().exchange(POST(accountUri, new AccountDto(1000.0)), Account).body().getId()

        when: "retrieve account"
        def account = client.toBlocking().retrieve("${accountUri}/${accountId}", Account)

        then: "account with proper balance is returned"
        account.getBalance() == 1000.0
    }

    def "should return not found error if non existing account id is used to retrieve"() {
        given: "non existing account id"
        def accountId = randomUUID().toString()

        when: "retrieve account"
        client.toBlocking().retrieve("${accountUri}/${accountId}", Account)

        then: "not found error is returned"
        def e = thrown(HttpClientResponseException)
        e.getStatus() == NOT_FOUND
    }
}