package com.devsuperior.dsbootcampcap1.repositories;

import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Repository;

import com.devsuperior.dsbootcampcap1.entities.Client;


@Repository
public interface ClientRepository  extends JpaRepositoryImplementation<Client, Long> {

}
