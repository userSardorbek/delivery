package com.example.delivery.repository;

import com.example.delivery.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

@SuppressWarnings("ALL")
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {

    @Query("select t from Transaction t where t.id = ?1")
    Optional<Transaction> findById(Integer id);

    @Query("select t from Transaction t where t.score>?1")
    List<Transaction> getScoreAndCarrier(Integer min);

    List<Transaction> findByCarrier(Carrier carrier);

    @Query("select request from Transaction")
    List<Request> getRequestFromTran();



    boolean existsByOffer(Offer offer);

    boolean existsByRequest(Request request);

    @Query("select l from Location l inner join Request r on l.locationId=r.location.locationId inner join" +
            " Transaction t on r.requestId = t.request.requestId")
    List<Location>  getLocationsFromReqFromTran();
}
