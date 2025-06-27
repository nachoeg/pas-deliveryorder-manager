package com.archpatterns.deliveryordermanager.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "USER_APP")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    private Long id;
}
