package rs.ftn.FitpassCopyCat.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AccountRequestDTO {
    @NotBlank
    @Email(message = "Invalid email format", regexp = ".+[@].+[\\.].+")
    private String email;
    @NotBlank
    private String address;
}
