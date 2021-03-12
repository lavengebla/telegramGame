package ru.dcp.gamedev.demo.models.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.dcp.gamedev.demo.models.AbstractBaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "notification")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Notification extends AbstractBaseEntity {

    @Column(name = "type", nullable = false)
    @NotNull
    private String type;

    @Column(name = "message", nullable = false)
    @NotNull
    @NotBlank
    private  String message;

    @Column(name = "state", nullable = false)
    @NotNull
    @NotBlank
    private  String state;

    @Column(name = "chatId", nullable = false)
    @NotNull
    @NotBlank
    private Long chatId;

    public Notification(@NotNull@NotBlank String type, @NotNull@NotBlank String message, @NotNull@NotBlank Long chatId){
        this.type = type;
        this.message = message;
        this.state = "QUEUED";
        this.chatId = chatId;
    }

}
