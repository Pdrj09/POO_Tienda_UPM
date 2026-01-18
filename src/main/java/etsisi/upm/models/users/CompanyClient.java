package etsisi.upm.models.users;

import etsisi.upm.io.KV;
import etsisi.upm.util.Constants;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.util.List;

@Entity
@Table(name = "company_clients")
@DiscriminatorValue("COMPANY_CLIENT")
public class CompanyClient extends Client {

    public CompanyClient(String dni, String name, String email, Cashier cashier) {
        super(dni, name, email, cashier);
    }

    public CompanyClient() {
        super();
    }

    @Override
    public List<KV> toViewKVList() {
        List<KV> kvs = super.toViewKVList();
        kvs.removeIf(kv -> kv.key.equals(Constants.CLI_DNI));
        kvs.add(new KV("NIF", getId()));
        return kvs;
    }

    @Override
    public String toString() {
        return super.toString().replace(Constants.STR_CLIENT, "{class:CompanyClient");
    }
}
