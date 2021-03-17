package ru.dcp.gamedev.demo.models.model.inventory;

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
@Table(name = "item", uniqueConstraints = {@UniqueConstraint(columnNames = {"itemType"}, name = "item_unique_id_idx")})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Item extends AbstractBaseEntity {

    @Column(name = "itemType", unique = true)
    @NotBlank
    private item_type itemType;
    @Column(name = "base_cost")
    @NotBlank
    private int base_cost;

    public enum item_type{
        WEAPON_RANGE,
        WEAPON_MAGIC,
        WEAPON_MELEE,
        RING,
        BODY,
        HEAD,
        BOOT
    }
}
