package com.openclassrooms.paymybuddy.service;

import com.openclassrooms.paymybuddy.model.Transaction;
import com.openclassrooms.paymybuddy.model.User;
import com.openclassrooms.paymybuddy.repository.TransactionRepository;
import com.openclassrooms.paymybuddy.utils.Constante;
import com.openclassrooms.paymybuddy.Dto.TransactionDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionServiceImpl implements TransactionService{

    private final TransactionRepository transactionRepository;
    private final UserService userService;

    /**
     * Instantiates a new Transaction service.
     *
     * @param transactionRepositoryParam the transaction repository param
     * @param userServiceParam           the user service param

     */
    public TransactionServiceImpl(
            final TransactionRepository transactionRepositoryParam,
            final UserService userServiceParam ) {
        transactionRepository = transactionRepositoryParam;
        userService = userServiceParam;
    }

    @Override
    public Optional<Transaction> findById(final int id) {
        return transactionRepository.findById(id);
    }

    @Override
    @Transactional
    public Transaction insert(final Transaction transactionParam) {
        return transactionRepository.save(transactionParam);
    }

    @Override
    public Page<Transaction> findAllByEmitterId(final int idUser, Pageable pageableParam) {
        return transactionRepository.findAllByUserSender_id(idUser, pageableParam);
    }

    @Override
    public List<Transaction> findAllByEmitterId(final int idUser) {
        return transactionRepository.findAllByUserSender_id(idUser);
    }
    @Override
    public Page<Transaction> findAllByReceiverId(final int idReceiver, Pageable pageableParam) {
        return transactionRepository.findAllByUserReceiver_id(idReceiver, pageableParam);
    }

    @Override
    @Transactional
    public Transaction addTransaction(final int idEmitter, final String connection,
                                      final String description, final double amount) {
        User receiver;
        User emitter;
        Optional<User>  optionalReceiver = Optional.ofNullable(userService.findUserByEmail(connection));
        Optional<User> optionalEmitter = userService.findById(idEmitter);

        if (optionalReceiver.isPresent() && optionalEmitter.isPresent()) {
            receiver = optionalReceiver.get();
            emitter = optionalEmitter.get();

            Transaction transaction = new Transaction();
            transaction.setAmount(amount);
            transaction.setDescription(description);
            transaction.setUserSender(emitter);
            transaction.setUserReceiver(receiver);
            transactionRepository.save(transaction);

            TransactionDTO transactionDTOLocal = new TransactionDTO();
            transactionDTOLocal.setConnection(receiver.getUsername());
            transactionDTOLocal.setDescription(description);
            transactionDTOLocal.setAmount(amount);

            emitter.setSolde(emitter.getSolde() - amount - (amount * Constante.COMMISSION));
            receiver.setSolde(receiver.getSolde() + amount);

            userService.saveUser(emitter);
            userService.saveUser(receiver);

            return transaction;
        } else {
            return null;
        }
    }

    @Override
    public List<Transaction> findAll() {
        return transactionRepository.findAll();
    }

    @Override
    public Page<Transaction> findAllByEmitterAndReceiverId(int idUser, Pageable pageableParam) {
        return transactionRepository.findAllByUserEmitterAndUserReceiver_Id(idUser, pageableParam);
    }

}