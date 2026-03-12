package domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "workouts")
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class Workout {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "workout_date", nullable = false) // если колонка называется иначе
    private LocalDateTime workoutDate;

    @Column(nullable = false)
    private String type;


    private String notes;
}
