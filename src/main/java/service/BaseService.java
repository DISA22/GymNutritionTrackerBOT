package service;

import config.TransactionSessionManager;
import domain.User;
import lombok.extern.slf4j.Slf4j;
import repository.BaseRepository;
@Slf4j
public abstract class BaseService<T, ID, R extends BaseRepository<T, ID>> {
    protected final R repository;
    protected final TransactionSessionManager transactionSessionManager;

    public BaseService(R repository, TransactionSessionManager transactionSessionManager) {
        this.repository = repository;
        this.transactionSessionManager = transactionSessionManager;
    }

    public void save(T entity) {
        transactionSessionManager.inTx(session -> repository.save(session, entity));
    }

    public T findById(ID id) {
        return transactionSessionManager.inSession(session -> repository.findById(session, id).orElseThrow(() -> {
            log.atError().addKeyValue("userId", id).log("User with this ID not found");
            return new RuntimeException("User with id " + id + " not found");
        }));
    }
}
