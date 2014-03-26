/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package a2tp5_server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 *
 * @author Tigrou
 */
public class Server {

    ServerSocket serverSocket;
    ArrayList<String> pieceTheatre;
    ArrayList<Integer> nbPlaces;
    ArrayList<Integer> nbPlacesDemandees;
//    ArrayList<PieceTheatre> allPiecs;

    Server() {
        pieceTheatre = new ArrayList();
        pieceTheatre.add("piece 1");
        pieceTheatre.add("Piece 2");
        pieceTheatre.add("Piece 3");

        nbPlaces = new ArrayList();
        nbPlaces.add(33);
        nbPlaces.add(3);
        nbPlaces.add(99);

        placerestante();

    }

    public static void main(String[] args) {

        new Server().begin(4444);
    }

    private void placerestante() {
        System.out.println("Il reste " + nbPlaces.get(0) + " places pour la " + pieceTheatre.get(0));
        System.out.println("Il reste " + nbPlaces.get(1) + " places pour la " + pieceTheatre.get(1));
        System.out.println("Il reste " + nbPlaces.get(2) + " places pour la " + pieceTheatre.get(2));
    }

    public void begin(int port) {
        try {
            serverSocket = new ServerSocket(port);
            while (true) {
                System.out.println("En attente d'un client sur le port " + port + " ...");
                new ProtocolThread(serverSocket.accept()).start();
                //Thread.start() calls Thread.run()
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    class ProtocolThread extends Thread {

        Socket socket;
        PrintWriter out_socket;
        BufferedReader in_socket;

        ProtocolThread(Socket socket) {
            System.out.println("acceptation de connection avec " + socket.getInetAddress());
            this.socket = socket;
            try {
                out_socket = new PrintWriter(socket.getOutputStream(), true);
                in_socket = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void run() {
            try {
                //envoie de la liste des pièce
                out_socket.println(pieceTheatre.size());
                for (int i = 0; i < pieceTheatre.size(); i++) {
                    out_socket.println(pieceTheatre.get(i).toString());
                }

                //Récupère le nom, la pièce et le nombre de places que lui envoie le client 
                String nomReserve = in_socket.readLine();
                System.out.println("Nom de la réserve : " + nomReserve);

                String nomPiece = in_socket.readLine();
                System.out.println("Nom de la pièce : " + nomPiece);

                int nbPlacesDemandees = Integer.valueOf(in_socket.readLine());
                System.out.println("Nombre de place : " + nbPlacesDemandees);

                //On vérifie pour la pièce demandée si il reste des places 
                int indexPieceDemandee = pieceTheatre.indexOf(nomPiece);

                if (nbPlaces.get(indexPieceDemandee) >= nbPlacesDemandees) {
                    nbPlaces.set(indexPieceDemandee, nbPlaces.get(indexPieceDemandee).hashCode() - nbPlacesDemandees);
                    out_socket.println(-1);
                } else {
                    out_socket.println(-2);
                }

                placerestante();


            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    System.out.println("Closing connection.");
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }
}
