package com.openclassrooms.paymybuddy.repository;

import com.openclassrooms.paymybuddy.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
    Page<Transaction> findAllByUserSender_id(final int id, Pageable pageableParam);

    List<Transaction> findAllByUserSender_id(final int id);
    Page<Transaction> findAllByUserReceiver_id(final int id, Pageable pageableParam);

   @Query(value = "SELECT user_id_payer, user_id_beneficiary, description, amount as amount, created_dt, id FROM Transaction WHERE user_id_beneficiary = :idUser union SELECT user_id_payer, user_id_beneficiary, description, -1.00 * amount as amount, created_dt, id FROM Transaction WHERE user_id_payer != user_id_beneficiary AND user_id_payer = :idUser ORDER BY created_dt DESC",
          countQuery = "SELECT count(*) FROM Transaction WHERE user_id_beneficiary = :idUser OR (user_id_payer = :idUser AND user_id_beneficiary != :idUser)",
          nativeQuery = true)
    Page<Transaction> findAllByUserEmitterAndUserReceiver_Id(int idUser,Pageable pageableParam);

}
