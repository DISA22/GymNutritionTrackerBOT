package domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "foods")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Food {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    @Column(name = "date_time", nullable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT NOW()")
    private LocalDateTime dateTime;

    @Column(name = "food_name", nullable = false)
    private String foodName;

    @Column(name = "calories_cal", nullable = false)
    private Double caloriesCal;

    @Column(name = "fat_g", nullable = false)
    private Double fatG;

    @Column(name = "protein_g", nullable = false)
    private Double proteinG;

    @Column(name = "carbohydrates_g", nullable = false)
    private Double carbohydratesG;
}
