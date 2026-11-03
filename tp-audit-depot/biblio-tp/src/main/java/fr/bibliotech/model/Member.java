package fr.bibliotech.model;

import java.time.LocalDate;

/**
 * Représente un membre inscrit à la bibliothèque.
 */
public class Member {

    private long id;
    private String name;
    private String email;
    private LocalDate membershipDate;

    public Member(long id, String name, String email, LocalDate membershipDate) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.membershipDate = membershipDate;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public LocalDate getMembershipDate() {
        return membershipDate;
    }

    @Override
    public String toString() {
        return "#" + id + " - " + name + " <" + email + ">";
    }
}
