package domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.User;

import java.sql.Timestamp;
import java.time.LocalDateTime;


@Data
@Entity
@Table(name = "NUTRITION_GOALS")
@AllArgsConstructor
@NoArgsConstructor
@Builder
//Цель питания
public class NutritionGoals {
        @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", unique = true)
    private Users user;


    @Column(nullable = false)
    private Double daily_calories;

    @Column(nullable = false)
    private Double daily_protein;

    @Column(nullable = false)
    private Double daily_fat;

    @Column(nullable = false)
    private Double daily_carbs;

    @Column(nullable = false)


    private LocalDateTime start_date;
    private LocalDateTime end_date;
}
