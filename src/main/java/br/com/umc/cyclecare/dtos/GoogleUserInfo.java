package br.com.umc.cyclecare.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GoogleUserInfo {
    private String sub;
    private String email;
    private String name;
    private String picture;

    @JsonProperty("given_name")
    private String givenName;

    @JsonProperty("family_name")
    private String familyName;

    @JsonProperty("email_verified")
    private Boolean emailVerified;
}