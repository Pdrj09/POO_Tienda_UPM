package etsisi.upm.controllers;

import etsisi.upm.Constants;
import etsisi.upm.io.View;
import etsisi.upm.models.repositories.Repository;
import etsisi.upm.models.users.Cashier;
import etsisi.upm.models.users.Client;
import etsisi.upm.util.Utilities;

import java.util.Collection;

public class ClientController {
    private final Repository<String, Client> clientRepository;
    private final Repository<String, Cashier> cashierRepository;

    public ClientController(Repository<String, Client> repository, Repository<String, Cashier> cashierRepository) {
        this.clientRepository = repository;
        this.cashierRepository = cashierRepository;
    }

    public String clientQuery(String[] query) {
        String command = query[Constants.QUERY_CLIENT_POS_CLASS] + Constants.STR_BLANK_SPACE + query[Constants.QUERY_CLIENT_POS_INSTRUCTION];
        switch (query[Constants.QUERY_CLIENT_POS_INSTRUCTION]) {
            case Constants.CASH_ADD -> {
                if (query.length == Constants.QUERY_CLIENT_POS_MAXARGS) {
                    Client newClient = addClient(Utilities.cleanName(query[Constants.QUERY_CLIENT_POS_NAME]), query[Constants.QUERY_CLIENT_POS_DNI],
                            Utilities.cleanName(query[Constants.QUERY_CLIENT_POS_EMAIL]), query[Constants.QUERY_CLIENT_POS_WORKER_ID]);
                    return View.getString(newClient, command);
                } else
                    throw new IllegalArgumentException(Constants.ERROR_FEW_PARAMS);
            }
            case Constants.CLIENT_LIST -> {
                Collection<Client> clients = listClients();
                if (clients.isEmpty())
                    return Constants.ERROR_NO_CLIENTS_FOUND;
                return View.getString(clients, command);
            }
            case Constants.CLIENT_REMOVE -> {
                if (query.length != Constants.THREE)
                    throw new IllegalArgumentException(Constants.ERROR_FEW_PARAMS);
                return View.getString(removeClients(query[Constants.TWO]), command);
            }
            default -> throw new IllegalArgumentException(Constants.ERROR_INVALID_OPTION);
        }
    }

    private Client addClient(String name, String dni, String email, String UW) {

        Client client = new Client(dni, Utilities.cleanName(name), Utilities.cleanName(email), UW);
        clientRepository.add(dni, client);

        return client;
    }


    private Client removeClients(String id) {
        return clientRepository.removeById(id);
    }

    private Collection<Client> listClients() {
        return clientRepository.findAll();
    }
}
