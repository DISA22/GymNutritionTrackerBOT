package domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Getter
@Setter
@Entity
@Table(name = "user_foods")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserFood {
        @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", unique = true)
    private User user;

    @Column(nullable = false)
    private Double caloriesPerDay;

    @Column(nullable = false)
    private Double proteinPerDay;

    @Column(nullable = false)
    private Double fatPerDay;

    @Column(nullable = false)
    private Double carbsPerDay;

    @CreationTimestamp
    @Column(name = "date_time", nullable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT NOW()")
    private LocalDate date;
}
