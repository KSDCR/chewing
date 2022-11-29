package io.web.chewing.Entity;


import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@Table
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String name;

    private String nickname;
    private String password;

    private String client_gb;

    private String phone;

    private String profile;

    @ColumnDefault("0")
    private char delete_yn;

    private char gender;

    private boolean verify;

    @ElementCollection(fetch = FetchType.LAZY)
    @Builder.Default
    private Set<MemberRole> roleSet = new HashSet<>();

    @ElementCollection(fetch = FetchType.LAZY)
    @Builder.Default
    private Set<Categories> categoriesSet = new HashSet<>();

    public void addMemberRole(MemberRole memberRole) {
        roleSet.add(memberRole);
    }

    public void clearRoles() {
        this.roleSet.clear();
    }

    public void addCategories(Categories categories) {
        categoriesSet.add(categories);
    }

    public void clearCategories() {
        this.categoriesSet.clear();
    }


}
