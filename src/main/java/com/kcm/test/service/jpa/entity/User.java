package com.kcm.test.service.jpa.entity;

import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "user_accounts")
@Getter
@Setter
@NoArgsConstructor
public class User {

  @Id @GeneratedValue private Long id;

  private String login;

  @Column(name = "account_type")
  private String accountType;

  @Column(name = "password_hash")
  private String hashedPassword;

  @OneToMany(cascade = CascadeType.ALL)
  @JoinTable(
      name = "user_accounts_roles",
      joinColumns = @JoinColumn(name = "id_user_accounts"),
      inverseJoinColumns = @JoinColumn(name = "id_roles")
  )
  private Set<UserRole> roles;
}
