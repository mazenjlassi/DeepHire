package com.deephire.Models;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
@Entity
@Data
@Setter
@Table(name="users",uniqueConstraints = {
        @UniqueConstraint(columnNames = "username"),
        @UniqueConstraint(columnNames = "email")
})
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String email;

    private String password;


    private String firstName;
    private String lastName;



    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }


    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name="user_roles",
            joinColumns = @JoinColumn(name="user_id"),inverseJoinColumns = @JoinColumn(name="role_id"))
    private Set<Role> roles= new HashSet<>();



    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] profilePicture;

    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] backGroundImage;

    private String bio;
    private String location;



    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Profile profile;

    @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL)
    private List<Message> sentMessages;

    @OneToMany(mappedBy = "receiver", cascade = CascadeType.ALL)
    private List<Message> receivedMessages;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Post> posts;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Comment> comments;



}
