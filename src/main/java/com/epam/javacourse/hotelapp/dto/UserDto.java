
package com.epam.javacourse.hotelapp.dto;

import com.epam.javacourse.hotelapp.utils.validation.customannotations.PasswordMatches;
import com.epam.javacourse.hotelapp.utils.validation.customannotations.ValidEmail;
import com.epam.javacourse.hotelapp.utils.validation.customannotations.ValidPassword;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;

@PasswordMatches(message = "Passwords should match")
public class UserDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;

    @Size(min = 2, max = 20, message = "{firstname.size}")
    @NotBlank(message = "{firstname.notempty}")
    private String firstName;

    @Size(min = 2, max = 20, message = "{lastname.size}")
    @NotBlank(message = "{lastname.notempty}")
    private String lastName;

    @ValidEmail
    @NotBlank(message = "{email.notempty}")
    private String email;

    @ValidPassword(message = "{password.regex}")
    @NotBlank(message = "{password.notempty}")
    @Size(min = 8, max = 20, message = "{password.size}")
    private String password;

    @NotBlank(message = "{password.match}")
    String confirmPassword;

    @NotBlank(message = "{country.notempty}")
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
