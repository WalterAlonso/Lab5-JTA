/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.losalpes.excepciones;

import javax.ejb.ApplicationException;

/**
 *
 * @author Juan Paz * Clase de excepción que se presenta cuando no hay cupo
 * suficiente
 */
@ApplicationException(rollback = true)
public class CupoInsuficienteException extends Exception {

    /**
     * Creates a new instance of <code>CupoInsuficienteException</code> without
     * detail message.
     */
    public CupoInsuficienteException() {
    }

    /**
     * Constructs an instance of <code>CupoInsuficienteException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public CupoInsuficienteException(String msg) {
        super(msg);
    }
}
