package domain;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Table(name = "nutrition_goals")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NutritionGoals {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "calories", nullable = false)
    private Double calories;
}
