package rs.ftn.FitpassCopyCat.model.DTO;

import lombok.NoArgsConstructor;

// DTO that encapsulates JWT & its duration - both are returned to the client
@NoArgsConstructor
public class UserTokenState {

    private String accessToken;
    private Long expiresIn;

    public UserTokenState(String accessToken, long expiresIn) {
        this.accessToken = accessToken;
        this.expiresIn = expiresIn;
    }
}