/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Cache;

import static Cache.Servidor.cestatico;
import static Cache.Servidor.lru_cache3;

/**
 * @author Xiao
 */
public class Cache3 extends Thread {

    int numHilo; // Atributos
    String consulta;

    public Cache3() // Constructor
    {
    }

    public Cache3(int num, String q) {

        numHilo = num;
        consulta = q;
    }

    public void fn(int i, String q) {

        this.numHilo = i;
        this.consulta = q;

        //System.out.println("hola soy la hebra"+ numHilo);
        System.out.println("===== ===== ===== ===== =====");
        String request = this.consulta;
        String[] tokens = request.split(" ");
        String parametros = tokens[1];
        String http_method = tokens[0];

        String[] tokens_parametros = parametros.split("/");

        String resource = tokens_parametros.length > 1 ? tokens_parametros[1] : "";
        String id = tokens_parametros.length > 2 ? tokens_parametros[2] : "";

        String meta_data = tokens.length > 2 ? tokens[2] : "";

        System.out.println("Consulta: " + request);
        System.out.println("HTTP METHOD: " + http_method);
        System.out.println("Resource: " + resource);
        System.out.println("ID:          " + id);
        System.out.println("META DATA:    " + meta_data);

        switch (http_method) {
            case "GET":
                if (id == "") {
                    System.out.println("Buscando en la base de datos los ultimos 10 registros de tipo '" + resource + "'");

                } else {
                    System.out.println("La hebra " + this.numHilo + " esta Buscando en el cache de '" + resource + "' el registro con id " + id);

                    //OPERACIONES CON CACHE
                    //=======================================================================================================
                    // La hebra busca en la partición estática del cache.
                    // En caso de no encontrar la entrada, busca en la partición LRU correspondiente.
                    // Si queda espacio en el cache estático, la entrada se agrega al mismo.
                    // Si no se encuentra en el LRU, se pide al IndexService.
                    
                    String estaEnCacheEstatico = cestatico.getEntryFromCache(id);   // busco en el cache estatico
                    if (estaEnCacheEstatico == null) {// si no esta en el cache estatico
                        int lleno = cestatico.lleno();
                        if (lleno == 0)// y aún no esta lleno, agregar al mismo.
                        {
                            System.out.println("Miss en cache estatico");
                            String result = IndexService.getEntry(id);
                            cestatico.addEntryToCache(id, result);
                            cestatico.print();
                        } 
                        
                        else //si no esta en el cache estatico y este esta lleno entonces pasamos al dinamico 
                        {

                            String result = lru_cache3.getEntryFromCache(id);// busco en el cache el id del request
                            if (result == null) { // MISS
                                System.out.println("MISS :(");
                                //total_miss++;
                                result = IndexService.getEntry(id);//sino lo encuentra en cache lo busca en el index
                                lru_cache3.addEntryToCache(id, result);// luego lo pongo en el cache
                            } else {
                                System.out.println("HIT!");
                                //total_hits++;
                            }
                            lru_cache3.print(3);
                            System.out.println("");

                        }
                    } else// si esta en el cache estatico
                    {
                        System.out.println("Hit en el cache estatico");
                        cestatico.print();
                    }
                }
                break;
            case "POST":
                System.out.println ("POST.");
                //POST funcional, pero no utilizado.
            /*  String aux = null;
                System.out.println("Creando un usuario con los siguientes datos: (" + meta_data + ")");
                for (String params : meta_data.split("&")) {
                    String[] parametros_meta = params.split("=");
                    System.out.println("\t* " + parametros_meta[0] + " -> " + parametros_meta[1]);
                }

                String result = lru_cache3.getEntryFromCache(id);
                if (result == null)// si no esta en el cache
                {
                    // voy al index y actualizo
                    IndexService.post(id, aux);
                    //luego actualizo el cache
                    lru_cache3.addEntryToCache(id, aux);
                    lru_cache3.print(3);
                } else// si esta en el cache
                {
                    //actualizo el cache 
                    lru_cache3.addEntryToCache(id, aux);
                    lru_cache3.print(3);
                    // y luego actualizo el index
                    IndexService.post(id, aux);
                }
                break;
            */
            default:
                System.out.println("Not a valid HTTP Request");
                break;
        }
    }

    public void run() {
    }

}