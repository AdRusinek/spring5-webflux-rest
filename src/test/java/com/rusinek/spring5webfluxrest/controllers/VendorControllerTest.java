package com.rusinek.spring5webfluxrest.controllers;

import com.rusinek.spring5webfluxrest.domain.Vendor;
import com.rusinek.spring5webfluxrest.repositories.VendorRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.junit.Assert.*;

public class VendorControllerTest {

     WebTestClient webTestClient;
     VendorRepository vendorRepository;
     VendorController vendorController;

    @Before
    public void setUp() throws Exception {
        vendorRepository = Mockito.mock(VendorRepository.class);
        vendorController = new VendorController(vendorRepository);
        webTestClient = WebTestClient.bindToController(vendorController).build();

    }

    @Test
    public void list() {
        BDDMockito.given(vendorRepository.findAll())
                .willReturn(Flux.just(Vendor.builder().firstName("Adrian").build(),
                            Vendor.builder().firstName("Robert").build()));

        webTestClient.get()
                .uri("/api/v1/vendors")
                .exchange()
                .expectBodyList(Vendor.class)
                .hasSize(2);


    }

    @Test
    public void getById() {
        BDDMockito.given(vendorRepository.findById("cos"))
                .willReturn(Mono.just(Vendor.builder().firstName("Dawid").build()));

        webTestClient.get()
                .uri("/api/v1/vendors/cos")
                .exchange()
                .expectBody(Vendor.class);
    }
}