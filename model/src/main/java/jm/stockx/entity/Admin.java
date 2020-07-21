package jm.stockx.entity;

import lombok.*;

import javax.persistence.Entity;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Entity
public class Admin extends User {

    public Admin(String firstName, String lastName, String email, String username, String password, Byte sellerLevel, Boolean vacationMode) {
        super(firstName, lastName, email, username, password, sellerLevel, vacationMode);
    }
}