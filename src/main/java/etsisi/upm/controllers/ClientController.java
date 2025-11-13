package etsisi.upm.controllers;

import etsisi.upm.models.repositories.Repository;
import etsisi.upm.models.users.Client;

import java.util.Collection;

public class ClientController {
    private static final Repository<String, Client> repository = new Repository<>();
    private static final String DUPLICATED_ID_ERROR  = "El id pasado como pararametro ya existe, añada otro";
    private static final String CASIER_NOT_EXIST  = "El casier dado no existe";


    public ClientController() {
    }

    public Client addClient(String name, String dni, String email, String UW) {
        if (repository.findById(dni) == null) {
            if (CashierController.existCashier(dni)) {
                Client client = new Client(dni, name, email, UW);
                repository.add(dni, client);

                return client;
            } else  {
                throw new IllegalArgumentException(CASIER_NOT_EXIST);
            }
        } else {
            throw new IllegalArgumentException(DUPLICATED_ID_ERROR);
        }
    }


    public Client removeClients(String id) {
        return repository.removeById(id);
    }

    public Collection<Client> listClients() {
        return repository.findAll();
    }
}
