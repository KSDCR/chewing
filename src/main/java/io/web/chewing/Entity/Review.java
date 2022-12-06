package io.web.chewing.Entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@Table
public class Review extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "store_id")
    private Store store;

    @NonNull
    private double rate;

    @NonNull
    @Column(length = 50)
    private String title;

    @OneToOne
    @JoinColumn(name = "member_id", referencedColumnName = "id")
    private Member member_id;

    @Column(length = 500)
    private String content;

    public void change(String content) {
        this.content = content;
    }
}
