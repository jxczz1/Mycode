package co.com.poli.bingo.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server {
    
    
    private static final int PUERTO = 12345;
    private static int[][] bingo = new int[5][5]; // no es final porque se debe modificar aplica para bingocliente tambien
    private static int[][] bingoCliente = new int[5][5];
    
    public static void mostrarBingo(){
        for(int i = 0; i < 5; i++){
            for(int j = 0; j < 5; j++){
                System.out.print(bingo[i][j]+"|");
            }
            System.out.println("");
        }
    }
    
    public static void mostrarBingoCliente(){
        for(int i = 0; i < 5; i++){
            for(int j = 0; j < 5; j++){
                System.out.print(bingoCliente[i][j]+"|");
            }
            System.out.println("");
        }
    }
    
    
    public static boolean compararCodigo(){
        boolean esIgual = false;
        for(int i = 0; i < 5; i++){
            for(int j = 0; j < 5; j++){
                //if(bingo)
            }
        }
        return esIgual;
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
    
    public static void main(String[] args) {
  
        DatagramSocket conexion;
        DatagramPacket entrada;
        DatagramPacket salida;
        byte[] dataEntrada;
        byte[] dataSalida;
        String mensaje;
   
        
        llenarBingo();
        System.out.println("El bingo se ha inicializado.....");
        try {
            conexion = new DatagramSocket(PUERTO);
            while(true){
                
                dataEntrada = new byte[1028];
                entrada = new DatagramPacket(dataEntrada, 
                                             dataEntrada.length);
                conexion.receive(entrada);
                mensaje = new String(entrada.getData(),
                                      0, dataEntrada.length);
                String[] msg = mensaje.split("-");//se capturan datos y se captura lo necesario
                
                
                int r = Integer.parseInt(msg[0]);
                int f = Integer.parseInt(msg[1]);
                int col = Integer.parseInt(msg[2].trim());// bug llega el ultimo con espacio ,se le aplica trim
                // int c = Integer.parseInt(msg[2]);//de esta forma tira error ya que tiene un espacio y  lanza exepcion
                
                bingoCliente[f][col] = r; // se llena el vector para compararlo con el del server
                System.out.println("\n\n Bingo cliente \n");
                mostrarBingoCliente();//bingo del cliente
                
                
              
                if( (f + col) == 10){
                   
                                     mensaje= "Comparando resultados....";
                                     dataSalida = mensaje.getBytes();
                                     System.out.println("Stop to see results");
                                     salida = new DatagramPacket(dataSalida, 
                                     dataSalida.length, 
                                     entrada.getAddress(), 
                                     entrada.getPort());
                
                
                                   }
                                   else{   
                                            mensaje= "Comparando resultados....";
                                            dataSalida = mensaje.getBytes();
                                            salida = new DatagramPacket(dataSalida, 
                                            dataSalida.length, 
                                            entrada.getAddress(), 
                                            entrada.getPort());
                    
                                          conexion.send(salida);
                                        }
                

            }
            
        } catch (SocketException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
}
