package rs.ftn.FitpassCopyCat.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rs.ftn.FitpassCopyCat.model.enums.Roles;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "user")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "role", discriminatorType = DiscriminatorType.STRING)
@DiscriminatorValue("USER")
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false, unique = true)
    private Long id;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false)
    private String password;
    @Column
    private String name;
    @Column
    private String surname;
    @Column(nullable = false)
    private LocalDate createdAt;
    @Column
    private String phoneNumber;
    @Column
    private LocalDate birthday;
    @Column
    private String address;
    @Column
    private String city;
    @Column
    private String zipCode;
//    todo check CascadeType.MERGE for potential issues
    @OneToOne(mappedBy = "user", cascade = {CascadeType.REMOVE, CascadeType.PERSIST, CascadeType.MERGE},fetch = FetchType.EAGER)
    private Image image;

    @ManyToMany( cascade = {CascadeType.REMOVE, CascadeType.REFRESH, CascadeType.DETACH }, fetch = FetchType.EAGER )
    @JoinTable(
            name = "manages",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "facility_id", referencedColumnName = "facility_id")
    )
    private Set<Facility> managedFacilities  = new HashSet<Facility>();

//    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY, cascade = {CascadeType.REMOVE,   CascadeType.REFRESH})
//    private Set<Review> reviews = new HashSet<Review>();

    @Column(nullable = false, insertable = false, updatable = false)
    @Enumerated(EnumType.STRING)
    private Roles role;
    @Transient
    public String getRole(){
        return this.getClass().getAnnotation(DiscriminatorValue.class).value();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        if (user.email == null || email == null) return false;
        return Objects.equals(email, user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }
}