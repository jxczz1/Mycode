
package co.com.poli.bingo.cliente;

import static co.com.poli.bingo.server.Server.verificarNumeroNoRepetido;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Random;
import java.util.Scanner;
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
                        numero = verificarNumeroNoRepetido(1, 16);
                    }

                    if(j == 1){
                        numero=verificarNumeroNoRepetido(16, 31);
                    }

                    if(j == 2){
                        numero = verificarNumeroNoRepetido(31, 46);
                    }

                    if(j == 3){
                        numero = verificarNumeroNoRepetido(46, 61);
                    }

                    if(j == 4){
                        numero = verificarNumeroNoRepetido(61, 76);
                    }

                    bingo[i][j] = numero;
                }
            }
        }
     
         
    public static int verificarNumeroNoRepetido(int rangoMinimo, int rangoMaximo){
        int numero = aleatorioRango(rangoMinimo, rangoMaximo);
        
        /*
        boolean repetido = false;
        
        for(int i = 0; i < 5; i++){
            for(int j = 0; j < 5; j++){
               if(numero==bingo[i][j]){
                repetido=true;
                numero = aleatorioRango(rangoMinimo, rangoMaximo); 
               }
               else{
               
               }
            }
        }*/
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
        
        try {
            InetAddress IP = InetAddress.getLocalHost();
            conexion = new DatagramSocket();
            llenarBingo();
            
            while(true){
                for(int i = 0; i < 5; i++){
                    for(int j = 0; j < 5; j++){
                        String msg = (String.valueOf(bingo[i][j])).concat("-"+i).concat("-"+j);
                        dataSalida = msg.getBytes();
                        salida = new DatagramPacket(dataSalida, 
                            dataSalida.length, IP, PUERTO);
                        conexion.send(salida);
                    }
                  }
                
                
                dataEntrada = new byte[1028];
                entrada = new DatagramPacket(dataEntrada, 
                                             dataEntrada.length);
                conexion.receive(entrada);
                mensaje =  new String(entrada.getData(), 
                                       0, dataEntrada.length);
                System.out.println("Server >> "+mensaje);
                        
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
