package etsisi.upm.controllers;

import etsisi.upm.models.users.Client;

import java.util.TreeMap;

public class ClientController {
    private TreeMap<String, Client> clients;

    private static final String JSON_CC = "ClientController{";
    private static final String JSON_CLIENT = ":";
    private static final String NEW_LINE = "\n";
    private static final String CLOSE_BRACE = "}";

    public ClientController() {
        clients = new TreeMap<>();
    }

    private String newClient(String dni, String nombre, String emal, String cashId) {
        try {
            Client client = new Client(dni, nombre, emal, cashId);
            clients.put(dni, client);
            return client.toString();
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    private String deleteClient(String dni) {
        try {
            String client = clients.toString();
            clients.remove(dni);
            return client.toString();
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer(JSON_CC).append(NEW_LINE);
        sb.append(JSON_CLIENT).append(clients).append(NEW_LINE);
        sb.append(CLOSE_BRACE).append(NEW_LINE);
        return sb.toString();
    }

}
