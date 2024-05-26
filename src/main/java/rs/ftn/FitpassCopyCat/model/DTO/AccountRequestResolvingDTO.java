package rs.ftn.FitpassCopyCat.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rs.ftn.FitpassCopyCat.model.entity.AccountRequest;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AccountRequestResolvingDTO {
    @NotBlank
    private Long id;
    @NotBlank
    @Email(message = "Invalid email format", regexp = ".+[@].+[\\.]+")
    private String email;
    @NotBlank
    private LocalDate createdAt;
    @NotBlank
    private String address;
    @NotBlank
    private String requestStatus;

    public AccountRequestResolvingDTO(AccountRequest accountRequest) {
        this.id = accountRequest.getId();
        this.email = accountRequest.getEmail();
        this.createdAt = accountRequest.getCreatedAt();
        this.address = accountRequest.getAddress();
        this.requestStatus = accountRequest.getStatus().toString();
    }
}
