package com.example.nicustejar.appamadeus.Models;

import java.util.ArrayList;

/**
 * Contains all the services of the current energy provider
 */
public class ListaFurnizori {
    private ArrayList<String> lista;

    /**
     * Creates a new list and adds the services
     * Not intended to be modified
     * */
    public ListaFurnizori() {
        this.lista = new ArrayList<String>();
        this.lista.add("Furnizor_0 serviciu gaze naturale");
        this.lista.add("Furnizor_0 serviciu energie electrica");
        this.lista.add(" - ");
    }

    /**
     * Returns the current list of services
     * */
    public ArrayList<String> getLista() {
        return this.lista;
    }
}
