package io.web.chewing.Entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

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


    public void change(String name, String detail, String address, String phone, String file, String open_time, String close_time) {
        this.name = name;
        this.detail = detail;
        this.address = address;
        this.phone = phone;
        this.file = file;
        this.open_time = open_time;
        this.close_time = close_time;
    }
}
