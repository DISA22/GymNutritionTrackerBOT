package domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;

@Data
@Entity
@Table(name = "WORKOUT_EXERCISES")
@AllArgsConstructor
@NoArgsConstructor
@Builder
//Тренировочные упражнения что именно делал на тренировке
public class WorkoutExercises {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "workout_id", nullable = false)
    private Workouts workout;

    @ManyToOne
    @JoinColumn(name = "exercise_id", nullable = false)
    private Exercises exercise;

    @Column(nullable = false)
    private Integer sets;

    @Column(nullable = false)
    private Integer reps;

    @Column(nullable = false)
    private Double weight;

    private Duration duration;
}
