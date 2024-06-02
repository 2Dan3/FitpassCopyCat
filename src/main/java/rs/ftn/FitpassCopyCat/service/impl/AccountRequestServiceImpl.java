package rs.ftn.FitpassCopyCat.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.ftn.FitpassCopyCat.model.entity.AccountRequest;
import rs.ftn.FitpassCopyCat.repository.AccountRequestRepository;
import rs.ftn.FitpassCopyCat.service.AccountRequestService;

import java.util.List;

@Service
public class AccountRequestServiceImpl implements AccountRequestService {

    private AccountRequestRepository accountRequestRepository;
    @Autowired
    public AccountRequestServiceImpl(/*@Lazy PasswordEncoder passwordEnc,*/ AccountRequestRepository accountRequestRepository/*, UserRepository userRepository*/) {
//        this.passwordEncoder = passwordEnc;
        this.accountRequestRepository = accountRequestRepository;
//        this.userRepository = userRepository;
    }

    @Override
    public List<AccountRequest> findAll() {
        return this.accountRequestRepository.findAll();
    }

    @Override
    public AccountRequest findById(Long id) {
        return accountRequestRepository.findById(id).orElse(null);
    }

}
