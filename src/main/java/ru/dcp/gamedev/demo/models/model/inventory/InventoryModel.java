package ru.dcp.gamedev.demo.models.model.inventory;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.dcp.gamedev.demo.models.AbstractBaseEntity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InventoryModel {

   private int item;
   private int character;
   private boolean isActive;
}
