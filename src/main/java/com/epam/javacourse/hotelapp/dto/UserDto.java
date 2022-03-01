package com.epam.javacourse.hotelapp.dto;

import com.epam.javacourse.hotelapp.utils.validation.customannotations.PasswordMatches;
import com.epam.javacourse.hotelapp.utils.validation.customannotations.ValidEmail;
import com.epam.javacourse.hotelapp.utils.validation.customannotations.ValidPassword;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@PasswordMatches
public class UserDto {

    private int id;

    @NotBlank(message = "First Name can't be empty")
    @Size(min = 2, max = 20, message = "First Name should be between 2 and 20 characters")
    private String firstName;

    @NotBlank(message = "Last Name can't be empty")
    @Size(min = 2, max = 20, message = "Last Name should be between 2 and 20 characters")
    private String lastName;

    @ValidEmail
    @NotBlank(message = "Email can't be empty")
    private String email;

    @ValidPassword(message = "Password must contain at least one digit, at least one lowercase and one uppercase " +
            "Latin characters as well as at least one special character like ! @ # & ( ) etc.")
    @NotBlank(message = "Password can't be empty")
    @Size(min = 8, max = 20, message = "Password must contain a length of at least 8 characters and a maximum of 20 characters.")
    private String password;

    @NotBlank(message = "Password should match")
    String confirmPassword;

    @NotBlank(message = "Country can't be empty")
    private String country;

    private String role;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }


}
