
package co.com.poli.bingo.cliente;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Clientes  {

        private static final int PUERTO = 12345;
        private static  int[][] bingo = new int[5][5];
        
        public static void llenarBingo(){
            for(int i = 0; i < 5; i++){
                for(int j = 0; j < 5; j++){
                    int numero = 0;
                    if(j == 0){
                        numero = insertarDatosBingo(1, 16);
                    }

                    if(j == 1){
                        numero=insertarDatosBingo(16, 31);
                    }

                    if(j == 2){
                        numero = insertarDatosBingo(31, 46);
                    }

                    if(j == 3){
                        numero = insertarDatosBingo(46, 61);
                    }

                    if(j == 4){
                        numero = insertarDatosBingo(61, 76);
                    }

                    bingo[i][j] = numero;
                }
            }
        }
        
          public static  boolean verificarNumNoRepetido(int num){
      
          boolean result = false ;
      
          for(int i = 0; i < 5; i++){
            for(int j = 0; j < 5; j++){
                if(num!=bingo[i][j]){
                    result=true;
                
                  }else{
                        result =false;
                        j=5;
                        i=5;
                       }
            }
        }
    
       return result;
    }
        
     
        
        
         
    public static int insertarDatosBingo(int rangoMinimo, int rangoMaximo){
           boolean condicion=false;
            int numero = 0;
            
            while(condicion==false){                       
                             numero = aleatorioRango(rangoMinimo, rangoMaximo);
                             condicion = verificarNumNoRepetido( numero);
                            }         
         
            return numero;
    }
    
    public static int aleatorioRango(int rangoMinimo, int rangoMaximo){
        Random r = new Random();
        int bajo = rangoMinimo;
        int mayor = rangoMaximo;
        int resultado = r.nextInt(mayor-bajo) + bajo;
        return resultado;
    }
    
    public static void main(String[] args) {
        
        DatagramSocket conexion;
        DatagramPacket entrada;
        DatagramPacket salida;
        byte[] dataEntrada;
        byte[] dataSalida;
        String mensaje;
        int band= 1;
        
        try {
            InetAddress IP = InetAddress.getLocalHost();
            conexion = new DatagramSocket();
            llenarBingo();
            
            
            while(true){
                
             if (band==1){
                for(int i = 0; i < 5; i++){
                    for(int j = 0; j < 5; j++){
                        String msg = (String.valueOf(bingo[i][j])).concat("-"+i).concat("-"+j);
                        dataSalida = msg.getBytes();
                        salida = new DatagramPacket(dataSalida, 
                            dataSalida.length, IP, PUERTO);
                        System.out.println("\n envio al servidor..");
                        conexion.send(salida);
                        
                        dataEntrada = new byte[1028];
                        entrada = new DatagramPacket(dataEntrada, 
                                             dataEntrada.length);
                         conexion.receive(entrada);
                         mensaje =  new String(entrada.getData(), 
                                       0, dataEntrada.length);
                
                       System.out.println("Server >> "+mensaje);
                    
                         
                    }
                  }
                 band=0;
                 }//end if
                 else{
                        dataEntrada = new byte[1028];
                        entrada = new DatagramPacket(dataEntrada, 
                                             dataEntrada.length);
                         conexion.receive(entrada);
                         mensaje =  new String(entrada.getData(), 
                                       0, dataEntrada.length);
                
                       System.out.println("Server >> "+mensaje);
             
                     }
                        
            }
            
            
        } catch (UnknownHostException ex) {
            Logger.getLogger(Clientes.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SocketException ex) {
            Logger.getLogger(Clientes.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Clientes.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
}
