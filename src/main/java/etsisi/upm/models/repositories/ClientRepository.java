package etsisi.upm.models.repositories;

import etsisi.upm.models.users.Client;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ClientRepository implements RepositoryInterface<Client> {
    private final Map<String, Client> clientMap;

    public ClientRepository() {
        clientMap = new HashMap<>();
    }


    @Override
    public void add(Client client) {
        clientMap.put(client.getId(), client);
    }

    @Override
    public Client findById(String id) {return clientMap.get(id);}

    @Override
    public Client removeById(String id) {
        return clientMap.remove(id);
    }

    @Override
    public Collection<Client> findAll() {
        return clientMap.values();
    }
}
