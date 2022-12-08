package io.web.chewing.Entity;


import com.sun.istack.NotNull;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.NotFound;
import org.springframework.security.oauth2.core.user.OAuth2User;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Entity
@Builder
@DynamicInsert
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Table
public class Member extends BaseEntity implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    private String name;

    @Column(nullable = false, unique = true)
    private String nickname;

    @NotNull
    private String password;

    @NotNull
    private String provider;

    private String phone;

    private String profile;

    @ColumnDefault("0")
    private char delete_yn;
    
    private char gender;

    private boolean verify;

    @Enumerated(EnumType.STRING)
    @ElementCollection(fetch = FetchType.LAZY)
    @Builder.Default
    private Set<MemberRole> roleSet = new HashSet<>();

    @Enumerated(EnumType.STRING)
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
