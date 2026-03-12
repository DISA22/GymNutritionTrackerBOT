package domain;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "EXERCISES")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
        //Упражнения
public class Exercise {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(name = "muscle_group",nullable = false)
    private String muscleGroup;

    private String description;

    private String difficulty;

    private String equipment;
}
