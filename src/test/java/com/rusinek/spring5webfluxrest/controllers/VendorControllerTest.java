package com.rusinek.spring5webfluxrest.controllers;

import com.rusinek.spring5webfluxrest.domain.Vendor;
import com.rusinek.spring5webfluxrest.repositories.VendorRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.reactivestreams.Publisher;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;

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

    @Test
    public void testCreateVendor() {
        BDDMockito.given(vendorRepository.saveAll(any(Publisher.class)))
                .willReturn(Flux.just(Vendor.builder().build()));

        Mono<Vendor> createToUpdate = Mono.just(Vendor.builder().firstName("Adrian").build());

        webTestClient.post()
                .uri("/api/v1/vendors")
                .body(createToUpdate,Vendor.class)
                .exchange()
                .expectStatus()
                .isCreated();
    }

    @Test
    public void testUpdateCustomer() {
        BDDMockito.given(vendorRepository.save(any(Vendor.class)))
                .willReturn(Mono.just(Vendor.builder().build()));

        Mono<Vendor> catToUpdateMono = Mono.just(Vendor.builder().firstName("Name").build());

        webTestClient.put()
                .uri("/api/v1/vendors/asdfasdf")
                .body(catToUpdateMono, Vendor.class)
                .exchange()
                .expectStatus()
                .isOk();
    }
}