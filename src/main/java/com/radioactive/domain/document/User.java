package com.radioactive.domain.document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@ToString
@Document("users")
public class User {

    @Id
    private String id;
    @NotNull
    @NotEmpty(message = "Field name can not empty")
    private String name;
    @NotNull
    @NotEmpty(message = "Field email can not empty")
    private String email;
    @NotNull
    @NotEmpty(message = "Field password can not empty")
    private String password;
}
