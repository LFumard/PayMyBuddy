package com.openclassrooms.paymybuddy.Dto;

import com.openclassrooms.paymybuddy.model.User;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Objects;

@Data
public class UserDto {
    private String nom;
    private String prenom;
    @NotBlank
    private String email;
    private double solde;

    public static UserDto fromEntityUser(User user) {

        UserDto userDto = new UserDto();
        userDto.email = user.getEmail();
        userDto.nom = user.getLastName();
        userDto.prenom = user.getFirstName();
        userDto.solde = user.getSolde();

        return userDto;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDto userDto = (UserDto) o;
        return Double.compare(userDto.solde, solde) == 0 && Objects.equals(nom, userDto.nom) && Objects.equals(prenom, userDto.prenom) && Objects.equals(email, userDto.email);
    }

/*
    @Override
    public int hashCode() {
        return Objects.hash(nom, prenom, email, solde);
    }
*/

    public UserDto(String nom, String prenom, String email, double solde) {
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.solde = solde;
    }

    public UserDto() {}
}