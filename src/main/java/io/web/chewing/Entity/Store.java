package io.web.chewing.Entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table
public class Store extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(nullable = false, unique = true)
    private String name;

    private String detail;

    private String open_time;

    private String close_time;

    private String address;

    private String phone;

    private String file;

    private String category;

//    @OneToOne
//    @JoinColumn(name = "owner", referencedColumnName = "owner")
//    private Member member_id;


    public void change(String name, String detail, String address, String phone, String file, String open_time, String close_time, String category) {
        this.name = name;
        this.detail = detail;
        this.address = address;
        this.phone = phone;
        this.file = file;
        this.open_time = open_time;
        this.close_time = close_time;
        this.category = category;
    }

//    public void assignUser(Member member) {
//
//        this.member_id = member;
//    }
}
