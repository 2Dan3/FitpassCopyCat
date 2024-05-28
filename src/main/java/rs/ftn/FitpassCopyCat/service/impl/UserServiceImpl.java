package rs.ftn.FitpassCopyCat.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import rs.ftn.FitpassCopyCat.model.DTO.AccountRequestDTO;
import rs.ftn.FitpassCopyCat.model.DTO.ChangeDataUserDTO;
import rs.ftn.FitpassCopyCat.model.entity.AccountRequest;
import rs.ftn.FitpassCopyCat.model.entity.User;
import rs.ftn.FitpassCopyCat.model.enums.RequestStatus;
import rs.ftn.FitpassCopyCat.repository.AccountRequestRepository;
import rs.ftn.FitpassCopyCat.repository.UserRepository;
import rs.ftn.FitpassCopyCat.service.UserService;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private AccountRequestRepository accountRequestRepository;
    private UserRepository userRepository;

    private PasswordEncoder passwordEncoder;
    @Autowired
    public UserServiceImpl(@Lazy PasswordEncoder passwordEnc, AccountRequestRepository accountRequestRepository, UserRepository userRepository) {
        this.passwordEncoder = passwordEnc;
        this.accountRequestRepository = accountRequestRepository;
        this.userRepository = userRepository;
    }

    @Override
    public User findByEmail(String email) {
        Optional<User> user = userRepository.findFirstByEmail(email);
        if (!user.isEmpty()) {
            return user.get();
        }
        return null;
    }
    @Override
    public User findById(Long id) {
        Optional<User> user = userRepository.findById(id);
        if (!user.isEmpty()) {
            return user.get();
        }
        return null;
    }

    @Override
    public boolean managesFacility(Long facilityId, User manager) {
        return userRepository.findManager(facilityId, manager.getId()) == 0 ? false : true;
    }

    @Override
    public AccountRequest createAccountRequest(AccountRequestDTO newAccount) {
        Optional<AccountRequest> foundAccountRequest = accountRequestRepository.findFirstByEmail(newAccount.getEmail());
        if (foundAccountRequest.isPresent()) {

            if (foundAccountRequest.get().getStatus() != RequestStatus.REJECTED) {
                return null;
            }else {
                foundAccountRequest.get().setStatus(RequestStatus.PENDING);
                return accountRequestRepository.save(foundAccountRequest.get());
            }
        }else {
            AccountRequest createdAccount = new AccountRequest(null, newAccount.getEmail(), LocalDate.now(), newAccount.getAddress(), RequestStatus.PENDING);
            return accountRequestRepository.save(createdAccount);
        }
    }

    @Override
    public boolean changeOwnData(ChangeDataUserDTO newData, User currentUser) {
//        if (emailBelongsToUser(newData.getEmail(), currentUser.getEmail()) || !emailIsTaken(newData.getEmail()) )
//        {
//            currentUser.setEmail(newData.getEmail().trim());

            currentUser.setAddress(newData.getAddress());
            currentUser.setZipCode(newData.getZipCode());
            currentUser.setCity(newData.getCity());
            currentUser.setPhoneNumber(newData.getPhoneNumber());
//            todo the rest of user data is separated in API Endpoint "verifyAccount",
//             where account data is only being put in for the very 1st time

            userRepository.save(currentUser);
            return true;
//        }
//        return false;
    }
//    private boolean emailBelongsToUser(String newEmail, String existingUserEmail) { return existingUserEmail.equalsIgnoreCase(newEmail.trim() );}
    private boolean emailIsTaken(String newEmail) {
        return userRepository.findFirstByEmail(newEmail).isPresent();
    }

    @Override
    public boolean changeOwnPassword(String oldPassword, String newPassword, User foundUser) {
        if (!passwordEncoder.matches(oldPassword, foundUser.getPassword()) )
            return false;
        foundUser.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(foundUser);
        return true;
    }

    @Override
    public void save(User newManager) {
        userRepository.save(newManager);
    }
}
