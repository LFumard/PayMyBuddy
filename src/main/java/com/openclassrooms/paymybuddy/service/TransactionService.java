package com.openclassrooms.paymybuddy.service;

import com.openclassrooms.paymybuddy.model.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;


public interface TransactionService {

    /**
     * Find by id optional.
     *
     * @param id the id
     * @return the optional
     */
    Optional<Transaction> findById(final int id);

    /**
     * Insert transaction.
     *
     * @param transactionParam the transaction param
     * @return the transaction
     */
    Transaction insert(Transaction transactionParam);

    /**
     * Find all by emitter id page.
     *
     * @param id            the id
     * @param pageableParam the pageable param
     * @return the page
     */
    Page<Transaction> findAllByEmitterId(final int id, Pageable pageableParam);

    List<Transaction> findAllByEmitterId(final int id);

    /**
     * Find all by receiver id page.
     *
     * @param pageableParam the pageable param
     * @param id            the id
     * @param of
     * @return the page
     */
    //Page<Transaction> findAllByReceiverId(final int id, Pageable pageableParam);
    Page<Transaction> findAllByReceiverId(final int idReceiver, Pageable pageableParam);

    /**
     * Create transaction transaction.
     *
     * @param idEmitter   the id emitter
     * @param connection  the connection
     * @param description the description
     * @param amount      the amount
     * @return the transaction
     */
    Transaction addTransaction(final int idEmitter, final String connection, final String description, final double amount);

    /**
     * Find all list.
     *
     * @return the list
     */
    List<Transaction> findAll();

    Page<Transaction> findAllByEmitterAndReceiverId(int idUser, Pageable pageableParam);

}
