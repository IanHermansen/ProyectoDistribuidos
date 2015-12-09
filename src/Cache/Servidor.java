/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Cache;

import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
//PROCESAMIENTO
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;


/**
 *
 * @author Xiao
 */
public class Servidor {
    public static LRUCache lru_cache1; //Declaramos atributos
    public static LRUCache lru_cache2;
    public static LRUCache lru_cache3;
    public static CacheEstatico cestatico;
    
    public Servidor(int tamCache) //Constructor
    {
    }
    
    // ===== Método hash =====
    // Retorna un número consistente (siempre el mismo) para un string.
    public static long hash(String hashKey) {
        int b = 378551;
        int a = 63689;
        long hash = 0;
        for (int i = 0; i < hashKey.length(); i++) {
            hash = hash * a + hashKey.charAt(i);
            a = a * b;
        }
        return Math.abs(hash);
    }//=======================
    
    public static void main(String[] args) throws IOException
    {
        // Se calcula el tamaño de las particiones del cache
        // de manera que la relación sea
        // 25% Estático y 75% Dinámico,
        // siendo la porción dinámica particionada en 3 partes.
        
        Lector l = new Lector(); // Obtener tamaño total del cache.
        int tamCache = l.leerTamCache("config.txt");
        //===================================
        int tamCaches = 0;
        if (tamCache%4==0){ // Asegura que el nro sea divisible por 4.
            tamCaches = tamCache/4;}
        else {              // Si no, suma para que lo sea.
            tamCaches = (tamCache - (tamCache)%4 + 4)/4;} // y divide por 4.
        
        System.out.println("Tamaño total Cache: " + (int)tamCache); // imprimir tamaño cache.
        System.out.println("Tamaño particiones y parte estática: " + tamCaches); // imprimir tamaño particiones.
        //===================================
        
        lru_cache1 = new LRUCache(tamCaches); //Instanciar atributos.
        lru_cache2 = new LRUCache(tamCaches);
        lru_cache3 = new LRUCache(tamCaches);
        cestatico = new CacheEstatico(tamCaches);
        
        try{
            ServerSocket servidor = new ServerSocket(4500); // Crear un servidor en pausa hasta que un cliente llegue.
            Socket clienteNuevo = servidor.accept();// Si llega se acepta.
            // Queda en pausa otra vez hasta que un objeto llegue.
            ObjectInputStream entrada = new ObjectInputStream(clienteNuevo.getInputStream());
            
            System.out.println("Objeto llego");
            //===================================
            Cache1 hilox1 = new Cache1(); // Instanciar hebras.
            Cache2 hilox2 = new Cache2();
            Cache3 hilox3 = new Cache3();
            
            try {// Leer el objeto, es un arreglo de String.
                String[] requests = (String[])entrada.readObject();
                
                // Para cada request del arreglo se distribuye
                // en Cache 1 2 o 3 según su hash.
                for (int i = 0; i < requests.length; i++) 
                {
                    String request = requests[i];// Rescatar el string del arreglo.
                    if(hash(request)%3 == 0){
                        hilox1.fn(i,request);  //Y corre la función de una hebra.
                    }else{
                        if(hash(request)%3 == 1){
                            hilox2.fn(i,request);
                        }else{
                            hilox3.fn(i,request);
                        }
                    }
                }
                clienteNuevo.close();
                servidor.close();
            }catch (ClassNotFoundException ex) {
                Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println("");
        }
        catch(IOException ex){
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
