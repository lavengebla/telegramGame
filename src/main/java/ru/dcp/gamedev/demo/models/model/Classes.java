package ru.dcp.gamedev.demo.models.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.dcp.gamedev.demo.models.AbstractBaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "class", uniqueConstraints = {@UniqueConstraint(columnNames = {"name"}, name = "class_unique_id_idx")})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Classes extends AbstractBaseEntity {

    @Column(name = "name", nullable = false)
    @NotBlank
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    @NotBlank
    private String description;

    @Column(name = "base_int")
    @NotBlank
    private int base_int;

    @Column(name = "base_agi")
    @NotBlank
    private int base_agi;

    @Column(name = "base_str")
    @NotBlank
    private int base_str;

    @Column(name = "pic", nullable = true)
    private byte[] pic;

    public Classes (String name, String description, int base_int, int base_agi, int base_str) {
        this.name = name;
        this.description = description;
        this.base_int = base_int;
        this.base_agi = base_agi;
        this.base_str = base_str;
        this.pic = null;
    }
}
