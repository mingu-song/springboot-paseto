package mingu.spring.paseto.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import mingu.spring.paseto.common.RoleEnum;

import java.io.Serializable;

@Data
@Entity(name = "app_role")
public class GroupRole implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(length = 15)
    private RoleEnum code;

    private String description;
}
