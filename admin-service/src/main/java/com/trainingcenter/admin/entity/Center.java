package com.trainingcenter.admin.entity;

import com.trainingcenter.admin.enums.CenterStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "centers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Center {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long centerId;

    @Column(nullable = false, unique = true, length = 20)
    private String centerCode;

    @Column(nullable = false, length = 100)
    private String centerName;

    @Column(nullable = false, length = 200)
    private String location;

    @Column(length = 20)
    private String contactNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private CenterStatus status = CenterStatus.ACTIVE;
}
