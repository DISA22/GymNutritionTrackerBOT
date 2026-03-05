package domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "EXERCISES")
@NoArgsConstructor
@AllArgsConstructor
@Builder
        //Упражнения
public class Exercises {
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
