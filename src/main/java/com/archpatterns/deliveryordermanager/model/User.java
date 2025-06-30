package com.archpatterns.deliveryordermanager.model;

import jakarta.persistence.*;
import lombok.*;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    private Long id;
}


