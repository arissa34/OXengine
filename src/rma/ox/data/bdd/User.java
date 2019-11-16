package rma.ox.data.bdd;

import org.dizitart.no2.objects.Id;

public class User {
    private String username;
    @Id
    private String email;

    // needed for deserialization
    public User(){}

    public User(String username, String email) {
        this.username = username;
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}