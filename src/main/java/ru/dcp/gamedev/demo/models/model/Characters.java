package ru.dcp.gamedev.demo.models.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.dcp.gamedev.demo.models.AbstractBaseEntity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "characters", uniqueConstraints = {@UniqueConstraint(columnNames = {"name"}, name = "characters_unique_id_idx")})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Characters extends AbstractBaseEntity {

    @ManyToOne
    @JoinColumn()
    private User user;

    @Column(name = "name", nullable = false)
    @NotBlank
    private String name;

    @Column(name = "base_int")
    @NotBlank
    private int base_int;

    @Column(name = "base_agi")
    @NotBlank
    private int base_agi;

    @Column(name = "base_str")
    @NotBlank
    private int base_str;

    @Column(name = "pic")
    private byte[] pic;

    public Characters(User user,String name,int base_int, int base_agi, int base_str) {
        this.user = user;
        this.name = name;
        this.base_int = base_int;
        this.base_agi = base_agi;
        this.base_str = base_str;
        this.pic = null;
    }
}
