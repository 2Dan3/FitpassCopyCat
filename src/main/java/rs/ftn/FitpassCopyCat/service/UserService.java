package rs.ftn.FitpassCopyCat.service;

import rs.ftn.FitpassCopyCat.model.DTO.AccountRequestDTO;
import rs.ftn.FitpassCopyCat.model.DTO.ChangeDataUserDTO;
import rs.ftn.FitpassCopyCat.model.entity.AccountRequest;
import rs.ftn.FitpassCopyCat.model.entity.User;

import java.util.List;

public interface UserService {

    User findByEmail(String email);
//
//    User createUser(UserDTO userDTO);
//
//    List<User> findAll();
//
//    User findById(Long id);
//
//    boolean changeOwnData(ChangeDataUserDTO newData, User currentUserData);
//
//    boolean changeOwnPassword(String oldPassword, String newPassword, User foundUser);
//
//    void remove(User foundUser);
//
//    boolean isLoggedUser(User subjectUser);
//
//    void banUserFromCommunity(Community community, User userBeingBanned, User moderator);
//
//    void unbanUserFromCommunity(Community community, User userBeingUnBanned);
//
    boolean managesFacility(Long facilityId, User manager);

    AccountRequest createAccountRequest(AccountRequestDTO newAccount);

    boolean changeOwnData(ChangeDataUserDTO newData, User foundUser);

    boolean changeOwnPassword(String oldPass, String newPass, User subjectUser);

    void save(User newManager);

    User findById(Long id);

    User createUser(AccountRequest accReq);
//    //    TODO: use for checks in Web's configure()
//    boolean isUserBanned(User user, Community community);
//
//    User save(User user);
}