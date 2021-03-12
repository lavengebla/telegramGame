package ru.dcp.gamedev.demo.models.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.BatchSize;
import ru.dcp.gamedev.demo.models.AbstractBaseEntity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.FetchType.EAGER;

@Entity
@Table(name = "users", uniqueConstraints = {@UniqueConstraint(columnNames = "user_id", name = "users_unique_id_idx")})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User extends AbstractBaseEntity {
    @Column(name = "user_id", unique = true, nullable = false)
    @NotNull
    private int userId;

    @Column(name = "name", unique = true, nullable = true)
    @NotBlank
    private String name;

    @Column(name = "surname", unique = true, nullable = true)
    @NotBlank
    private String surname;

    @Column(name = "username", unique = true, nullable = true)
    @NotBlank
    private String username;

    @Enumerated(STRING)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"),
            uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "role"}, name = "user_roles_unique_idx")})
    @Column(name = "role")
    @ElementCollection(fetch = EAGER)
    @BatchSize(size = 200)
    private Set<Role> roles;


    public User(int userId, String name, String username, String surname) {
        Set<Role> roleSet = new HashSet<>();
        roleSet.add(Role.НЕАВТОРИЗОВАННЫЙ);

        this.userId = userId;
        this.name = name;
        this.surname = surname;
        this.username = username;
        this.roles = roleSet;
    }

    public User(Integer id, @NotNull int userId, @NotBlank String name, @NotBlank String usernname, Set<Role> roles) {
        super(id);
        this.userId = userId;
        this.name = name;
        this.username = usernname;
        this.roles = roles;
    }

    @Override
    public String toString() {
        return "пользователь{" +
                "userId=" + userId +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", username='" + username + '\'' +
                ", roles=" + roles +
                '}';
    }
}
