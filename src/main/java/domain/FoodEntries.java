package domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "food_entries")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FoodEntries {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users user;

    @Column(nullable = false)
    private LocalDateTime entryDate;

    @Column(nullable = false)
    private String foodName;


    private Double calories;
    private Double protein;
    private Double carbs;
    private Double fat;
    private Double quantity;

    @Enumerated(EnumType.STRING)
    private Units units;


    enum Units {
        GRAM("г"),
        MILLILITER("мл"),
        PIECE("шт"),
        ;
        private String value;

        Units(String value) {
            this.value = value;
        }
    }
}
