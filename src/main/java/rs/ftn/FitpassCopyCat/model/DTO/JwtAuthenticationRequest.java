package rs.ftn.FitpassCopyCat.model.DTO;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

// DTO for login
public class JwtAuthenticationRequest {

    @NotBlank
    @Email(message = "Invalid email format", regexp = ".+[@].+[\\.]+")
    private String username;
    @NotBlank
    private String password;

    public JwtAuthenticationRequest() {
        super();
    }

    public JwtAuthenticationRequest(String username, String password) {
        this.setUsername(username);
        this.setPassword(password);
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
