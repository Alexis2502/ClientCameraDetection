package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamUtils;

import com.google.common.base.Stopwatch;
import lombok.SneakyThrows;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.entity.mime.MultipartEntityBuilder;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.Base64;

public class Main {
    @SneakyThrows({IOException.class})
    public static void main(String[] args) {
        // Configuration
        var endpointBaseUrl = "https://7six8mafg2.execute-api.eu-central-1.amazonaws.com/prod";
        var endpointPath = "/api/v1/photos";
        var authUsername = args[0];
        var authPassword = args[1];
        File file = new File("file.png");
        Stopwatch stopwatch = Stopwatch.createStarted();

        // Constructing the required objects
        var endpointUri = URI.create(endpointBaseUrl + endpointPath);
        var authCredentialsBase64 = Base64.getEncoder().encodeToString("%s:%s".formatted(authUsername, authPassword).getBytes());

        while(true) {
            if(stopwatch.elapsed().toMinutes()>=1) {
                Webcam webcam = Webcam.getDefault();
                webcam.open();
                WebcamUtils.capture(webcam, file);

                try (var httpClient = HttpClients.createDefault()) {
                    // Creating POST request object
                    var httpRequest = new HttpPost(endpointUri);
                    httpRequest.addHeader("Authorization", "Basic " + authCredentialsBase64);

                    // Adding image file to the request
                    var httpMultipartBuilder = MultipartEntityBuilder.create();
                    httpMultipartBuilder.addBinaryBody("imageToAnalyse", file);
                    var httpEntity = httpMultipartBuilder.build();
                    httpRequest.setEntity(httpEntity);

                    // Sending the request and fetching the response
                    try (var httpResponse = httpClient.execute(httpRequest)) {
                        handleResponse(httpResponse);
                    }
                }
                stopwatch.reset().start();
                webcam.close();
            }
        }
    }

    @SneakyThrows(IOException.class)
    public static void handleResponse(CloseableHttpResponse response) {
        // Serwer zwraca błąd 429 (Too Many Requests), gdy klient zarzuca go zbyt dużą ilością zdjęć
        // Aktualnie serwer akceptuje nowe zdjęcia co 60 sekund
        if (response.getCode() == 429) {
            System.out.println("Wait a moment before sending another photo.");
        }

        if (response.getCode() != 200) {
            System.out.println("Server returned error code: " + response.getCode());
            return;
        }

        ObjectMapper jsonMapper = new ObjectMapper();
        EventLogEntry serverResponse = jsonMapper.readValue(response.getEntity().getContent(), EventLogEntry.class);

        if (serverResponse.getDetectedPeople().isEmpty()) {
            System.out.println("No people detected on the photo");
        } else {
            System.out.printf("People detected on the photo: %s\n", serverResponse.getDetectedPeople().size());
            System.out.println(serverResponse.getDetectedPeople());
        }
    }
}