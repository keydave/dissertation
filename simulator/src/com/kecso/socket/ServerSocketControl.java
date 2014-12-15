package com.kecso.socket;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import com.kecso.game.planephysics.IO.OutputParameters;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.NetworkInterface;
import java.net.SocketTimeoutException;
import org.apache.commons.lang3.SerializationUtils;

public class ServerSocketControl implements Runnable {

    private boolean running = false;
    private UdpMessage udpMessage;
    private OutputParameters output;
    private final Object syncToken = new Object();

    public ServerSocketControl() {
    }

    @Override
    public void run() {
        DatagramSocket sock = null;

        try {
            sock = new DatagramSocket(8888);
            sock.setSoTimeout(1000);

            byte[] buffer = new byte[65536];
            DatagramPacket incoming = new DatagramPacket(buffer, buffer.length);
            

            while (!Thread.currentThread().isInterrupted()) {
                try {
                    sock.receive(incoming);
                    byte[] data = incoming.getData();
                    this.udpMessage = SerializationUtils.deserialize(data);

                    byte[] response = SerializationUtils.serialize(this.output != null ? new UdpResponse((float) output.getSpeed(), (float) output.getVerticalSpeed(), (float) output.getAltitude(), (float) output.getRpm()) : null);
                    DatagramPacket dp = new DatagramPacket(response, response.length, incoming.getAddress(), incoming.getPort());
                    sock.send(dp);
                } catch (SocketTimeoutException e) {
                }
            }
        } catch (Exception e) {
            System.err.println("IOException " + e);
        } finally {
            if (sock != null) {
                sock.close();
            }

        }
    }

    /**
     * @return the running
     */
    public boolean isRunning() {
        return running;
    }

    /**
     * @param running the running to set
     */
    public void setRunning(boolean running) {
        synchronized (syncToken) {
            this.running = running;
        }
    }

    public UdpMessage getUdpMessage() {
        synchronized (syncToken) {
            return udpMessage;
        }
    }

    public void setOutput(OutputParameters output) {
        synchronized (syncToken) {
            this.output = output;
        }
    }
}
