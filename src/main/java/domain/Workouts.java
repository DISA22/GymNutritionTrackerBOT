package domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "workouts")
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class Workouts {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;

    @Column(name = "workout_date", nullable = false) // если колонка называется иначе
    private LocalDateTime workoutDate;

    @Column(nullable = false)
    private String type;


    private String notes;
}
