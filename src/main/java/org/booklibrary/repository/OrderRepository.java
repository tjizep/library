package org.booklibrary.repository;

import org.booklibrary.model.Borrow;
import org.springframework.stereotype.Repository;

@Repository
public class OrderRepository extends HashMapRepository<Borrow, Long> {

    @Override
    protected <S extends Borrow> Long getEntityId(S order) {
        return order.getId();
    }

    @Override
    public void deleteAllById(Iterable<? extends Long> longs) {
        longs.forEach(id -> entities.remove(id));
    }
}
