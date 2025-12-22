package etsisi.upm.models.users;

import etsisi.upm.io.KV;
import etsisi.upm.util.Constants;

import java.util.List;

public class CompanyClient extends Client{

    public CompanyClient(String dni, String name, String email, String idCashier) {
        super(dni, name, email, idCashier);
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
