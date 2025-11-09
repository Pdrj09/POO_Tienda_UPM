package etsisi.upm.models.users;

import java.util.Objects;

public abstract class User {
    private final String id; //unique id
    private String name;
    private String email;

    public User(String id, String name, String email) {
        if (id == null || id.isEmpty())
            throw new IllegalArgumentException("El ID no puede estar vacío");
        if (name == null || name.isBlank())
            throw new IllegalArgumentException("El nombre no puede estar vacío");
        if (email == null || email.isBlank())
            throw new IllegalArgumentException("El email no puede estar vacío");
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
            throw new IllegalArgumentException("El nombre no puede estar vacío");
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (email == null || email.isBlank())
            throw new IllegalArgumentException("El email no puede estar vacío");
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
