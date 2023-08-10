package com.example.demo.Resource;

import com.example.demo.enumeration.Status;
import com.example.demo.model.Response;
import com.example.demo.model.Server;
import com.example.demo.service.ServerService;
import com.example.demo.service.implementation.ServerServiceImplementation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Map;

import static org.springframework.http.MediaType.IMAGE_PNG_VALUE;


@RestController
@RequestMapping("/server")
//necessary for dependency injection
@RequiredArgsConstructor
public class ServerResource {
    // Injecting dependency of server service
    private final ServerServiceImplementation serverService;

    // returns all servers in the response
    @GetMapping("/list")
    public ResponseEntity<Response> getServers() {
        // ResponseEntity.ok(T body) generates successful response with body given as the parameter
        return ResponseEntity.ok((
                // using builder pattern to create response body
                Response.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("servers", serverService.list(30)))
                        .message("Servers retrieved")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build()
                )
        );
    }

    // used to simulate pinging servers. If the ping is unsuccessful returns response with Status.SERVER_DOWN
    @GetMapping("/ping/{ipAddress}")
    public ResponseEntity<Response> pingServer(@PathVariable("ipAddress") String ipAddress) throws IOException {
        Server server = serverService.ping(ipAddress);
        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("server", server))
                        .message(server.getStatus() == Status.SERVER_UP ? "Ping success" : "Ping failed")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build()

        );
    }

    //POST HTTP request
    @PostMapping("/save")
    // @Valid ensures that the field checks all the constraints specified in the class declaration.
    // for example, we specified that the ipAddress should be unique in the table in the server class
    // so if server given in the argument has the same ipAddress as a different record in the table then server cannot be saved
    public ResponseEntity<Response> saveServer(@RequestBody @Valid Server server) {
        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("server", serverService.create(server)))
                        .message("Server created")
                        .status(HttpStatus.CREATED)
                        .statusCode(HttpStatus.CREATED.value())
                        .build()

        );
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Response> getServer(@PathVariable("id") Long id) {
        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("server", serverService.get(id)))
                        .message("Server retrieved")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build()

        );
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Response> deleteServer(@PathVariable("id") Long id) {
        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("deleted", serverService.delete(id)))
                        .message("Server deleted")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build()

        );
    }

    @GetMapping(path = "/image/{fileName}", produces = IMAGE_PNG_VALUE)
    public byte[] getServerImage(@PathVariable("fileName") String fileName) throws IOException {
        // reads images from the project folder in resources/images
        return Files.readAllBytes(Paths.get(System.getProperty("user.dir")+ "/src/main/resources/images/" + fileName));
    }




}
