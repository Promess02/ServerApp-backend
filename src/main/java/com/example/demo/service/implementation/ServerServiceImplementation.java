package com.example.demo.service.implementation;

import com.example.demo.model.Server;
import com.example.demo.repo.ServerRepo;
import com.example.demo.service.ServerService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Collection;
import java.util.Random;

import static com.example.demo.enumeration.Status.SERVER_DOWN;
import static com.example.demo.enumeration.Status.SERVER_UP;

//Lombok creates constructor and injects serverRepo - dependency injection
@RequiredArgsConstructor
@Service
// all methods in a class are to be executed in a transactional context.
//If any part of the transaction fails, all changes made during the transaction
// are rolled back
@Transactional
// SLF4J - Simple logging facade for java - adds logging to the class
@Slf4j
public class ServerServiceImplementation implements ServerService {

    //injecting repository
    private final ServerRepo serverRepo;
    @Override
    public Server create(Server server) {
        //log from SLF4J
        log.info("Saving new server: {}", server.getName());
        server.setImageURL(setServerImageUrl());
        return serverRepo.save(server);
    }

    @Override
    public Collection<Server> list(int limit) {
        log.info("Fetching all servers");
        return serverRepo.findAll(PageRequest.of(0,limit)).toList();
    }

    @Override
    public Server get(Long id) {
        log.info("Fetching server by id: " + id);
        if(serverRepo.findById(id).isPresent()) return serverRepo.findById(id).get();
        else throw new RuntimeException("server not found");
    }

    @Override
    public Server update(Server server) {
        log.info("updating server: " + server.getName());
        return serverRepo.save(server);
    }

    @Override
    public Boolean delete(Long id) {
        log.info("delete server with id: " + id);
        serverRepo.deleteById(id);

        return true;
    }

    @Override
    public Server ping(String ipAddress) throws IOException {
        log.info("Pinging server IP: {}", ipAddress);
        Server server = serverRepo.findByIpAddress(ipAddress);
        // creates an internet address object from the given ip address
        InetAddress address = InetAddress.getByName(ipAddress);
        // checks if the internet adress is reachable and sets the server status accordingly
        server.setStatus(address.isReachable(10000)? SERVER_UP: SERVER_DOWN);
        serverRepo.save(server);
        return server;
    }

    private String setServerImageUrl() {
        String[] imageNames = {"server1.png", "server2.png", "server3.png", "server4.png"};
        return ServletUriComponentsBuilder.fromCurrentContextPath().path("/server/image/" + imageNames[new Random().nextInt(4)]).toUriString();

    }

}
