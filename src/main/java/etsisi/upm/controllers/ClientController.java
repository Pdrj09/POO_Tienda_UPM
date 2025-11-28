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

    public String clientQuery(String[] query) {
        StringBuilder regex = new StringBuilder();
        regex.append(Constants.REGEX_INIT);

        if (query[Constants.QUERY_CASH_POS_INSTRUCTION].equals(Constants.CASH_ADD)) {
            if (query.length == Constants.QUERY_CLIENT_POS_MAXARGS){
                Client newClient = addClient(query[Constants.TWO], query[Constants.THREE],
                        query[Constants.FOUR], query[Constants.FIVE]);
                return View.getString(newClient);
            }
        }/*
        else if (query.startsWith(Constants.CLIENT_LIST)) {
            regex.append(Constants.CLIENT_LIST)
                    .append(Constants.REGEX_BLANK_SPACE);
            query = query.replaceFirst(Constants.CLIENT_LIST, Constants.STR_EMPTY);

            Collection<Client> clients = listClients();

            //TODO formatear
            StringBuilder list = new StringBuilder();
            for (Client client : clients) {
                list.append(View.getString(client));
            }

            return list.toString();

        } else if (query.startsWith(Constants.CLIENT_REMOVE)) {
            regex.append(Constants.CLIENT_REMOVE)
                    .append(Constants.REGEX_BLANK_SPACE);
            query = query.replaceFirst(Constants.CLIENT_REMOVE, Constants.STR_EMPTY);

            String[] query = query.split(Constants.REGEX_INIT);

            if (query.length != Constants.TWO) {
                throw new IllegalArgumentException(Constants.ERROR_FEW_PARAMS);
            }

            return View.getString(removeClients(query[Constants.ONE]));

        } else {
            throw new IllegalArgumentException(Constants.ERROR_INVALID_OPTION);
        }*/
        return "error";
    }

    public String clientAddControl(String[] query){
        //client add "<nombre>" <DNI> <email> <cashId>
        // Client{identifier='Y8682724P', name='Pepe1', email='pepe3@upm.es', cash=UW1234567}
        //client add: ok
        StringBuilder builder = new StringBuilder();

        String name = query[Constants.ONE];
        String dni = query[Constants.TWO];
        String email = query[Constants.THREE];
        String UW = query[Constants.FOUR];

        builder.append(View.getString(addClient(name,dni, email,UW)));
        builder.append("/n");
        builder.append(Constants.okStatus("Client","ClientAdd"));

        return builder.toString();
    }

    private Client addClient(String name, String dni, String email, String UW) {

        Client client = new Client(dni, name, email, UW);
        clientRepository.add(dni, client);

        return client;
    }


    public Client removeClients(String id) {
        return clientRepository.removeById(id);
    }

    public Collection<Client> listClients() {
        return clientRepository.findAll();
    }
}
