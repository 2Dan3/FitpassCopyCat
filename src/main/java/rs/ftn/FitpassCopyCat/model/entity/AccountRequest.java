package rs.ftn.FitpassCopyCat.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rs.ftn.FitpassCopyCat.model.enums.RequestStatus;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "account_request")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "acc_req_id", nullable = false, unique = true)
    private Long id;
    @Column(nullable = false)
    private String email;
    @Column
    private LocalDate createdAt;
    @Column(nullable = false)
    private String address;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private RequestStatus status;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AccountRequest a = (AccountRequest) o;
        return id != null && id.equals(a.getId());
    }

    @Override
    public int hashCode() {
        return 1111;
    }
}