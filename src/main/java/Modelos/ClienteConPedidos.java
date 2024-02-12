/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelos;


public class ClienteConPedidos {
    private int id;
    private String nombreCliente;
    private int totalPedidos;

    public ClienteConPedidos() {
    }

    public ClienteConPedidos(int id, String nombreCliente, int totalPedidos) {
        this.id = id;
        this.nombreCliente = nombreCliente;
        this.totalPedidos = totalPedidos;
    }

    
    
    // getters y setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public int getTotalPedidos() {
        return totalPedidos;
    }

    public void setTotalPedidos(int totalPedidos) {
        this.totalPedidos = totalPedidos;
    }
    
}
