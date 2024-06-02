package rs.ftn.FitpassCopyCat.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rs.ftn.FitpassCopyCat.model.entity.User;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserAccountConfirmationDTO {
    private Long id;
    private String email;
    private LocalDate createdAt;
    private String address;

    public UserAccountConfirmationDTO(User u) {
        this.id = u.getId();
        this.email = u.getEmail();
        this.createdAt = u.getCreatedAt();
        this.address = u.getAddress();
    }
}
