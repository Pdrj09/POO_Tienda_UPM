package etsisi.upm.models.users;

import java.util.Objects;

public abstract class User {
    private final String id; //unique id
    private String name;
    private String email;

    //Validation messages.
    private static final String ERR_ID_EMPTY = "El ID no puede estar vacío";
    private static final String ERR_NAME_EMPTY = "El nombre no puede estar vacío";
    private static final String ERR_EMAIL_EMPTY = "El email no puede estar vacío";


    public User(String id, String name, String email) {
        if (id == null || id.isBlank())
            throw new IllegalArgumentException(ERR_ID_EMPTY);
        if (name == null || name.isBlank())
            throw new IllegalArgumentException(ERR_NAME_EMPTY);
        if (email == null || email.isBlank())
            throw new IllegalArgumentException(ERR_EMAIL_EMPTY);
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.isBlank())
            throw new IllegalArgumentException(ERR_NAME_EMPTY);
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (email == null || email.isBlank())
            throw new IllegalArgumentException(ERR_EMAIL_EMPTY);
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
