package com.rideshare.app.model;

import com.rideshare.app.converter.PositionConverter;
import com.rideshare.app.dto.UserRegisterDTOIn;
import com.rideshare.app.dto.UserUpdateDTOIn;
import jakarta.persistence.*;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name="user")
/*TODO remove NamedQueries (just an example)
can be used in service class like this for example:
public Collection<User> getAllUsers(){
 return entityManager.createNamedQuery("getAllUsers",User.class).getResultList();
 }
* */
@NamedQueries({
        @NamedQuery(name = "getAllUsers", query = "SELECT u FROM User u"),
        @NamedQuery(name = "findUserByName",
                query = "SELECT u FROM User u WHERE u.username = :username")
})
public class User
{


    @Id
    @Column(name = "user_id", nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userId;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, length = 20)
    private byte[] passwordHash;

    @Column(nullable = false, length = 32)
    private byte[] passwordSalt;

    private String firstname;
    private String lastname;
    @Convert(converter = PositionConverter.class)
    @Column(nullable = false, columnDefinition = "GEOMETRY")
    private Position position;
    @Embedded
    private Address address;

    @Column(unique = true)
    private String email;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Collection<Offer> offers;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Collection<Request> requests;

    public User() {

    }

    public static User from(UserRegisterDTOIn dto) {
        User user = new User();
        user.setUsername(dto.username());
        user.setFirstname(dto.firstname());
        user.setLastname(dto.lastname());
        //new Address(dto.streetNumber(), dto.street(), dto.zip(), dto.city())
        user.setAddress(dto.address());
        user.setEmail(dto.email());
        return user;
    }

    public static User from(UserUpdateDTOIn dto) {
        User user = new User();
        user.setUserId(dto.userId());
        user.setUsername(dto.username());
        user.setFirstname(dto.firstname());
        user.setLastname(dto.lastname());
        user.setAddress(new Address(dto.streetNumber(), dto.street(), dto.zip(), dto.city()));
        user.setEmail(dto.email());
        return user;
    }

    public int setUserId(int userId) {
        return this.userId=userId;
    }
    public int getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public  byte[] getPasswordHash() {
        return passwordHash;
    }

    public  byte[] getPasswordSalt() {
        return passwordSalt;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public String getEmail() {
        return email;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPasswordHash(byte[] passwordHash) {
        this.passwordHash = passwordHash;
    }

    public void setPasswordSalt(byte[] passwordSalt) {
        this.passwordSalt = passwordSalt;
    }

    public int getId() {
        return this.userId;
    }
    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Collection<Offer> getOffers() {
        return offers;
    }

    public void setOffers(Collection<Offer> offers) {
        this.offers = offers;
    }

    public Collection<Request> getRequests() {
        return requests;
    }

    public void setRequests(Collection<Request> requests) {
        this.requests = requests;
    }
}
