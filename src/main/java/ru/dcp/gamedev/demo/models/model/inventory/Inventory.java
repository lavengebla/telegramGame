package ru.dcp.gamedev.demo.models.model.inventory;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.dcp.gamedev.demo.models.AbstractBaseEntity;
import ru.dcp.gamedev.demo.models.model.Characters;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "inventory")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Inventory extends AbstractBaseEntity {

   @JoinColumn()
   @ManyToOne()
   private Item item;

   @JoinColumn()
   @ManyToOne()
   private Characters character;

   @Column(name = "isActive")
   @NotBlank
   private boolean isActive;

}
