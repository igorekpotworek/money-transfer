package revolut.transfer.repository;

import revolut.transfer.domain.Account;
import io.micronaut.data.annotation.Query;
import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.repository.CrudRepository;

import java.util.Optional;

@JdbcRepository(dialect = Dialect.H2)
public interface AccountRepository extends CrudRepository<Account, String> {

    @Query("select * from account where id = :id for update")
    Optional<Account> findByIdPessimistic(String id);
}
