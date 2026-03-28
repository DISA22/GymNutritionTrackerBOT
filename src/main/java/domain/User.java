package domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "users")
@NoArgsConstructor
@Builder
@AllArgsConstructor
@ToString
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "telegram_id", nullable = false, unique = true)
    private Long telegramId;

    @Column(nullable = false)
    private String username;

    @Column
    private String name;

    @Column(nullable = false)
    private int age;

    @Column(nullable = false)
    private double weight;

    @Column(nullable = false)
    private double height;

    @OneToOne
    @JoinColumn(name = "goal_id")
    private NutritionGoals nutritionGoals;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
