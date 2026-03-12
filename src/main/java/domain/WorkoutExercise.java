package domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.Duration;

@Getter
@Setter
@Entity
@Table(name = "WORKOUT_EXERCISES")
@AllArgsConstructor
@NoArgsConstructor
@Builder
//Тренировочные упражнения что именно делал на тренировке
public class WorkoutExercise {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "workout_id", nullable = false)
    private Workout workout;

    @ManyToOne
    @JoinColumn(name = "exercise_id", nullable = false)
    private Exercise exercise;

    @Column(nullable = false)
    private Integer sets;

    @Column(nullable = false)
    private Integer reps;

    @Column(nullable = false)
    private Double weight;

    @Column(columnDefinition = "interval")
    private Duration duration;
}
