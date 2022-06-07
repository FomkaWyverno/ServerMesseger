package com.wyverno.client;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

public class Client {
    public static void main(String[] args) throws IOException, InterruptedException {
        for (int i = 0; i < 3; i++) {
            System.out.println(i+1);
            Thread.sleep(TimeUnit.SECONDS.toMillis(1));
        }
        Socket socket = new Socket("localhost",50);
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        writer.write("Hello i client");
        writer.flush();
        writer.close();
        System.out.println("finish");
    }
}
