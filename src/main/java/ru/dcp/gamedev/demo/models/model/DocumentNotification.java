package ru.dcp.gamedev.demo.models.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.dcp.gamedev.demo.models.AbstractBaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "notification_document")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DocumentNotification extends AbstractBaseEntity {

    @Column(name = "type", nullable = false)
    @NotNull
    private String type;

    @Column(name = "message", nullable = false, columnDefinition = "TEXT")
    @NotNull
    @NotBlank
    private  String message;

    @Lob
    @Column(name = "data", nullable = false)
    private byte[] data;

    @Column(name = "state", nullable = false)
    @NotNull
    @NotBlank
    private  String state;

    @Column(name = "chatId", nullable = false)
    @NotNull
    @NotBlank
    private Long chatId;

    @Column(name = "filename", nullable = false)
    @NotNull
    @NotBlank
    private  String filename;

    public DocumentNotification(@NotNull@NotBlank String type, @NotNull@NotBlank String message, @NotNull@NotBlank byte[] data , @NotNull@NotBlank Long chatId, @NotNull@NotBlank String filename){
        this.type = type;
        this.message = message;
        this.data = data;
        this.state = "QUEUED";
        this.chatId = chatId;
        this.filename = filename;
    }

}
