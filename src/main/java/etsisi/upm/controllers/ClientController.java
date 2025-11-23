package etsisi.upm.controllers;

import etsisi.upm.Constants;
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

    public String clientAddControl(String[] querySplit){
        //client add "<nombre>" <DNI> <email> <cashId>
        // Client{identifier='Y8682724P', name='Pepe1', email='pepe3@upm.es', cash=UW1234567}
        //client add: ok
        StringBuilder builder = new StringBuilder();

        String name = querySplit[Constants.ONE];
        String dni = querySplit[Constants.TWO];
        String email = querySplit[Constants.THREE];
        String UW = querySplit[Constants.FOUR];

        builder.append( addClient(name,dni, email,UW).toString());
        builder.append("/n");
        builder.append(Constants.okStatus("Client","ClientAdd"));

        return builder.toString();
    }

    private Client addClient(String name, String dni, String email, String UW) {
        if (clientRepository.findById(dni) == null) {
            if (cashierRepository.findById(dni)!=null) {
                Client client = new Client(dni, name, email, UW);
                clientRepository.add(dni, client);

                return client;
            } else  {
                throw new IllegalArgumentException(CASIER_NOT_EXIST);
            }
        } else {
            throw new IllegalArgumentException(DUPLICATED_ID_ERROR);
        }
    }


    public Client removeClients(String id) {
        return clientRepository.removeById(id);
    }

    public Collection<Client> listClients() {
        return clientRepository.findAll();
    }
}
