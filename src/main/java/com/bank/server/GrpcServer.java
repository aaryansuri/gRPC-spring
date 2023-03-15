package com.bank.server;

import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;

public class GrpcServer {

    public static void main(String[] args) throws IOException, InterruptedException {

        Server server = ServerBuilder.forPort(6565).addService(new BankService()).build();
        System.out.println("server is running");
        server.start();
        server.awaitTermination();

    }

}
