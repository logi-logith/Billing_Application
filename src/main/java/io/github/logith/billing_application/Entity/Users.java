package io.github.logith.billing_application.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Users")
public class Users {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "user_gen_seq"
    )
    @SequenceGenerator(
            name = "user_gen_seq",
            sequenceName = "USER_SEQ",
            allocationSize = 1
    )
    private Long id;
    private String name;
    private String email;
    private String password;
    private enum role
    {
        USER,
        ADMIN
    }
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;




}
