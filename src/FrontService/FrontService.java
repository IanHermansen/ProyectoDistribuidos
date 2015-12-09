/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FrontService;

// Ingrese peticiones en este archivo.
// Para contestar, el IndexService contiene:
// {"query1", "query2", "query3", "query4", "query5", "query6", "query7", "query8", "query9", "query10", "query11", "query12", "query13", "query14", "query15", "query16", "query17", "query18", "query19", "query20"}

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FrontService {
    
    public static void main(String[] args)
    {
        try
        {
            Socket cliente= new Socket("localhost",4500); // nos conectamos con el servidor
            ObjectOutputStream mensaje = new ObjectOutputStream(cliente.getOutputStream()); // get al output del servidor, que es cliente : socket del cliente q se conecto al server
            
            String[] requests = { 
                "GET /users/query2",
                "GET /users/query1",
                "GET /users/query1",
                "GET /users/query3",
                "GET /users/query5",
                "GET /users/query6",
                "GET /users/query7",
                "GET /users/query4",
                "GET /users/query10",
                "GET /users/query13",
                "GET /users/query7",
                "GET /users/query10",
                "GET /users/query11",
                "GET /users/query12",
                "GET /users/query13",
                "GET /users/query14",
                "GET /users/query15",
                "GET /users/query16",};
            
            mensaje.writeObject(requests); // Enviar los request al servidor
            
        }
        catch(UnknownHostException ex)// Excepcion cuando no se reconoce el servidor.
        {
              Logger.getLogger(FrontService.class.getName()).log(Level.SEVERE,null,ex);
        }
        catch(IOException ex)
        {
            Logger.getLogger(FrontService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}