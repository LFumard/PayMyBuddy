package com.openclassrooms.paymybuddy.Dto;

import com.openclassrooms.paymybuddy.model.User;
import lombok.Data;

import javax.validation.constraints.NotBlank;

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
}