package etsisi.upm.controllers;

import etsisi.upm.Constants;
import etsisi.upm.io.View;
import etsisi.upm.models.repositories.Repository;
import etsisi.upm.models.users.Cashier;
import etsisi.upm.models.users.Client;

import java.util.Collection;

public class ClientController {
    private final Repository<String, Client> clientRepository;
    private final Repository<String, Cashier> cashierRepository;
    private static final String DUPLICATED_ID_ERROR  = "El id pasado como pararametro ya existe, añada otro";
    private static final String CASIER_NOT_EXIST  = "El casier dado no existe";


    public ClientController(Repository<String, Client> repository, Repository<String, Cashier> cashierRepository) {
        this.clientRepository = repository;
        this.cashierRepository = cashierRepository;
    }

    public String clientQuery(String query) {
        StringBuilder regex = new StringBuilder();
        regex.append(Constants.REGEX_INIT);

        if (query.startsWith(Constants.CLIENT_ADD)) {
            regex.append(Constants.CLIENT_ADD)
                    .append(Constants.REGEX_BLANK_SPACE);
            query = query.replaceFirst(Constants.CLIENT_ADD, Constants.STR_EMPTY);

            String[] querySplit = query.split(Constants.REGEX_INIT);

            if (querySplit.length != Constants.FOUR) {
                throw new IllegalArgumentException(Constants.ERROR_FEW_PARAMS);
            }

            Client newClient = addClient(querySplit[Constants.ZERO], querySplit[Constants.ONE],
                                            querySplit[Constants.TWO],  querySplit[Constants.THREE]);

            return View.print(newClient);


        } else if (query.startsWith(Constants.CLIENT_LIST)) {
            regex.append(Constants.CLIENT_LIST)
                    .append(Constants.REGEX_BLANK_SPACE);
            query = query.replaceFirst(Constants.CLIENT_LIST, Constants.STR_EMPTY);

            Collection<Client> clients = listClients();

            return View.print(clients);

        } else if (query.startsWith(Constants.CLIENT_REMOVE)) {
            regex.append(Constants.CLIENT_REMOVE)
                    .append(Constants.REGEX_BLANK_SPACE);
            query = query.replaceFirst(Constants.CLIENT_REMOVE, Constants.STR_EMPTY);

            String[] querySplit = query.split(Constants.REGEX_INIT);

            if (querySplit.length != Constants.ONE) {
                throw new IllegalArgumentException(Constants.ERROR_FEW_PARAMS);
            }

            return View.print(removeClients(querySplit[Constants.ZERO]).toString()).toString();

        } else {
            throw new IllegalArgumentException(Constants.ERROR_INVALID_OPTION);
        }
    }

    public String clientAddControl(String[] querySplit){
        //client add "<nombre>" <DNI> <email> <cashId>
        // Client{identifier='Y8682724P', name='Pepe1', email='pepe3@upm.es', cash=UW1234567}
        //client add: ok
        StringBuilder builder = new StringBuilder();

        String name = querySplit[Constants.ZERO];
        String dni = querySplit[Constants.ONE];
        String email = querySplit[Constants.TWO];
        String UW = querySplit[Constants.THREE];

        builder.append(View.print(addClient(name,dni, email,UW)));
        builder.append("/n");
        builder.append(Constants.okStatus("Client","ClientAdd"));

        return builder.toString();
    }

    private Client addClient(String name, String dni, String email, String UW) {
        if (cashierRepository.findById(dni)!=null) {
            Client client = new Client(dni, name, email, UW);
            clientRepository.add(dni, client);

            return client;
        } else  {
            throw new IllegalArgumentException(CASIER_NOT_EXIST);
        }
    }


    public Client removeClients(String id) {
        return clientRepository.removeById(id);
    }

    public Collection<Client> listClients() {
        return clientRepository.findAll();
    }
}
