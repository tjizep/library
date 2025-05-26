package org.booklibrary.repository;

import org.booklibrary.model.User;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository extends HashMapRepository<User, String> {

    @Override
    protected <S extends User> String getEntityId(S user) {
        return user.getUsername();
    }

    @Override
    public void deleteAllById(Iterable<? extends String> strings) {
        strings.forEach(id -> entities.remove(id));
    }
}
