package etsisi.upm.controllers;

import etsisi.upm.models.repositories.ClientRepository;
import etsisi.upm.models.repositories.RepositoryInterface;
import etsisi.upm.models.users.Client;

public class ClientController {
    private final RepositoryInterface<Client> clientRepository;

    public ClientController() {
        this.clientRepository = new ClientRepository();
    }
}
