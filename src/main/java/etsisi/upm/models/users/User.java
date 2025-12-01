package etsisi.upm.models.users;

import etsisi.upm.util.Constants;
import etsisi.upm.io.KV;
import etsisi.upm.io.Presentable;
import etsisi.upm.util.Utilities;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class User implements Comparable<User>, Presentable {
    private final String id; //unique id
    private String name;
    private String email;


    public User(String id, String name, String email) {
        if (id == null || id.isBlank())
            throw new IllegalArgumentException(Constants.ERROR_ID_EMPTY);
        if (name == null || name.isBlank())
            throw new IllegalArgumentException(Constants.ERROR_NAME_EMPTY);
        if (email == null || email.isBlank())
            throw new IllegalArgumentException(Constants.ERROR_EMAIL_EMPTY);
        if (!Utilities.isValidEmail(email))
            throw new IllegalArgumentException(Constants.ERROR_EMAIL_FORMAT);
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
            throw new IllegalArgumentException(Constants.ERROR_NAME_EMPTY);
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (email == null || email.isBlank())
            throw new IllegalArgumentException(Constants.ERROR_EMAIL_EMPTY);
        this.email = email;
    }

    //COMPARABLE BY NAME
    @Override
    public abstract int compareTo(User o);

    //this method is for the view
    @Override
    public List<KV> toViewKVList() {
        List<KV> kvs = new ArrayList<>();
        kvs.add(new KV(Constants.ID, this.id));
        kvs.add(new KV(Constants.NAME_LOWERCASE, this.name));
        kvs.add(new KV(Constants.EMAIL, this.email));
        return kvs;
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
