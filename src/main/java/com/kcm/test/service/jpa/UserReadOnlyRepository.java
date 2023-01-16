package com.kcm.test.service.jpa;

import com.kcm.test.service.jpa.entity.User;
import java.util.List;

public interface UserReadOnlyRepository extends ReadOnlyRepository<User, Long> {

  List<User> findByLogin(String login);

  List<User> findByAccountType(String accountType);

}
