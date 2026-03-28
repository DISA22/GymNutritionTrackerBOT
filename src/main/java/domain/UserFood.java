package domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.FetchProfile;

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
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "food_id")
    private Food food;

    @Column
    private Double amount;

    @CreationTimestamp
    @Column(name = "date_time", nullable = false, updatable = false)
    private LocalDate date;
}
