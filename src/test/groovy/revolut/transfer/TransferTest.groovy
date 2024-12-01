package revolut.transfer

import io.micronaut.context.annotation.Value
import io.micronaut.http.client.HttpClient
import io.micronaut.http.HttpRequest
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import revolut.transfer.domain.Account
import revolut.transfer.dto.AccountDto
import revolut.transfer.dto.TransferDto
import spock.lang.Specification

import static io.micronaut.http.HttpStatus.*
import static java.util.UUID.randomUUID

@MicronautTest
class TransferTest extends Specification {

    @Inject
    @Client('/')
    HttpClient client

    @Value('${transfer.uri}')
    String transferUri
    @Value('${account.uri}')
    String accountUri

    def "should perform successful transfer"() {
        given: "two accounts"
        def sourceAccountId = client.toBlocking().exchange(HttpRequest.POST(accountUri, new AccountDto(1000.0)), Account).body().getId()
        def destinationAccountId = client.toBlocking().exchange(HttpRequest.POST(accountUri, new AccountDto(2000.0)), Account).body().getId()

        and: "transfer object"
        def transferDto = new TransferDto(sourceAccountId, destinationAccountId, 100.0)

        when: "transfer money from source account to destination"
        def result = client.toBlocking().exchange(HttpRequest.POST(transferUri, transferDto))

        then: "transfer is successful"
        result.status() == CREATED

        and: "balance on source account is updated"
        def sourceAccount = client.toBlocking().retrieve("${accountUri}/${sourceAccountId}", Account)
        sourceAccount.getBalance() == 900.0

        and: "balance on destination account is updated"
        def destinationAccount = client.toBlocking().retrieve("${accountUri}/${destinationAccountId}", Account)
        destinationAccount.getBalance() == 2100.0
    }

    def "should return error when there is insufficient balance to perform transfer"() {
        given: "create two accounts"
        def sourceAccountId = client.toBlocking().exchange(HttpRequest.POST(accountUri, new AccountDto(1000.0)), Account).body().getId()
        def destinationAccountId = client.toBlocking().exchange(HttpRequest.POST(accountUri, new AccountDto(2000.0)), Account).body().getId()

        and: "transfer object with balance greater than balance on source account"
        def transferDto = new TransferDto(sourceAccountId, destinationAccountId, 1100.0)

        when: "try to transfer money"
        client.toBlocking().exchange(HttpRequest.POST(transferUri, transferDto))

        then: "unprocessable entity error is returned"
        def e = thrown(HttpClientResponseException)
        e.getStatus() == UNPROCESSABLE_ENTITY

        and: "balance on source account has not changed"
        def sourceAccount = client.toBlocking().retrieve("${accountUri}/${sourceAccountId}", Account)
        sourceAccount.getBalance() == 1000.0

        and: "balance on destination account has not changed"
        def destinationAccount = client.toBlocking().retrieve("${accountUri}/${destinationAccountId}", Account)
        destinationAccount.getBalance() == 2000.0
    }

    def "should return unprocessable entity error when try to transfer negative amount of money"() {
        given: "two accounts"
        def sourceAccountId = client.toBlocking().exchange(HttpRequest.POST(accountUri, new AccountDto(1000.0)), Account).body().getId()
        def destinationAccountId = client.toBlocking().exchange(HttpRequest.POST(accountUri, new AccountDto(2000.0)), Account).body().getId()

        and: "transfer object with negative amount of money"
        def transferDto = new TransferDto(sourceAccountId, destinationAccountId, -1100.0)

        when: "try to transfer money"
        client.toBlocking().exchange(HttpRequest.POST(transferUri, transferDto))

        then: "unprocessable entity error is returned"
        def e = thrown(HttpClientResponseException)
        e.getStatus() == UNPROCESSABLE_ENTITY
    }

    def "should return not found error when try to transfer money from non existing account"() {
        given: "two account ids, only destination exists"
        def sourceAccountId = randomUUID().toString()
        def destinationAccountId = client.toBlocking().exchange(HttpRequest.POST(accountUri, new AccountDto(2000.0)), Account).body().getId()

        and: "transfer object"
        def transferDto = new TransferDto(sourceAccountId, destinationAccountId, 100.0)

        when: "try to transfer money"
        client.toBlocking().exchange(HttpRequest.POST(transferUri, transferDto))

        then: "not found error is returned"
        def e = thrown(HttpClientResponseException)
        e.getStatus() == NOT_FOUND
    }

    def "should return not found error when try to transfer money to non existing account"() {
        given: "two account ids, only source exists"
        def sourceAccountId = client.toBlocking().exchange(HttpRequest.POST(accountUri, new AccountDto(2000.0)), Account).body().getId()
        def destinationAccountId = randomUUID().toString()

        and: "transfer object"
        def transferDto = new TransferDto(sourceAccountId, destinationAccountId, 100.0)

        when: "try to transfer money"
        client.toBlocking().exchange(HttpRequest.POST(transferUri, transferDto))

        then: "not found error is returned"
        def e = thrown(HttpClientResponseException)
        e.getStatus() == NOT_FOUND
    }

    def "should return unprocessable entity error when try to transfer money from source account with null id"() {
        given: "two account ids, source is null"
        def sourceAccountId = null
        def destinationAccountId = client.toBlocking().exchange(HttpRequest.POST(accountUri, new AccountDto(2000.0)), Account).body().getId()

        and: "transfer object"
        def transferDto = new TransferDto(sourceAccountId, destinationAccountId, 100.0)

        when: "try to transfer money"
        client.toBlocking().exchange(HttpRequest.POST(transferUri, transferDto))

        then: "unprocessable entity error is returned"
        def e = thrown(HttpClientResponseException)
        e.getStatus() == UNPROCESSABLE_ENTITY
    }

    def "should return unprocessable entity error when try to transfer money from destination account with null id"() {
        given: "two account ids, destination is null"
        def sourceAccountId = client.toBlocking().exchange(HttpRequest.POST(accountUri, new AccountDto(2000.0)), Account).body().getId()
        def destinationAccountId = null

        and: "transfer object"
        def transferDto = new TransferDto(sourceAccountId, destinationAccountId, 100.0)

        when: "try to transfer money"
        client.toBlocking().exchange(HttpRequest.POST(transferUri, transferDto))

        then: "unprocessable entity error is returned"
        def e = thrown(HttpClientResponseException)
        e.getStatus() == UNPROCESSABLE_ENTITY
    }
}
