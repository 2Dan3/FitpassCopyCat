package rs.ftn.FitpassCopyCat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import rs.ftn.FitpassCopyCat.model.DTO.*;
import rs.ftn.FitpassCopyCat.model.entity.AccountRequest;
import rs.ftn.FitpassCopyCat.model.entity.Facility;
import rs.ftn.FitpassCopyCat.model.entity.User;
import rs.ftn.FitpassCopyCat.security.TokenUtils;
import rs.ftn.FitpassCopyCat.service.AccountRequestService;
import rs.ftn.FitpassCopyCat.service.UserService;
import rs.ftn.FitpassCopyCat.service.impl.UserServiceImpl;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping(path = "${apiPrefix}/users")
public class UserController {
    private UserService userService;
    private AccountRequestService accountRequestService;
    private UserDetailsService userDetailsService;
    private AuthenticationManager authenticationManager;
    private TokenUtils tokenUtils;

    @Autowired
    public UserController(UserServiceImpl userService, AuthenticationManager authenticationManager,
                          UserDetailsService userDetailsService, AccountRequestService accountRequestService, TokenUtils tokenUtils){
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.accountRequestService = accountRequestService;
        this.tokenUtils = tokenUtils;
    }

    @PostMapping(consumes = "application/json", value = "/")
    public ResponseEntity requestAccountRegistration(@Valid @RequestBody AccountRequestDTO newAccount){

        AccountRequest createdAccountRequest = userService.createAccountRequest(newAccount);
        if(createdAccountRequest == null){
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
        }
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping(consumes = "application/json", value = "/login")
    public ResponseEntity<UserTokenState> createAuthenticationToken(
            @Valid @RequestBody JwtAuthenticationRequest authRequest, HttpServletResponse response) {
//        System.out.println("\n------------\nENTERED LOGIN\n------------\n");

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(
                        authRequest.getUsername(), authRequest.getPassword() );
        // Ukoliko kredencijali nisu ispravni, logovanje nece biti uspesno, desice se
        // AuthenticationException
        Authentication authentication = authenticationManager.authenticate(authenticationToken);

        // Ukoliko je autentifikacija uspesna, ubaci korisnika u trenutni security
        // kontekst
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Kreiraj token za tog korisnika
//        UserDetails user = (UserDetails) authentication.getPrincipal();  ILI
        try {
            UserDetails user = userDetailsService.loadUserByUsername(authRequest.getUsername());
            String jwt = tokenUtils.generateToken(user);
            int expiresIn = tokenUtils.getExpiredIn();

            // Vrati token kao odgovor na uspesnu autentifikaciju
            return new ResponseEntity<>(new UserTokenState(jwt, expiresIn), HttpStatus.OK);
        }
        catch (UsernameNotFoundException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<AccountRequestResolvingDTO>> loadAllAccountRequests() {
//        System.out.println("\n-----------\nENTERED LOADALL ACC REQS\n-----------\n");

        List<AccountRequest> requests = this.accountRequestService.findAll();

        // convert users to userDTOs
        List<AccountRequestResolvingDTO> DTOs = new ArrayList<>();
        for (AccountRequest accReq : requests) {
            DTOs.add(new AccountRequestResolvingDTO(accReq));
        }

        return new ResponseEntity<>(DTOs, HttpStatus.OK);
    }


// todo
//    @PostMapping(consumes = "application/json", value = "/register")
//    public ResponseEntity<UserDTO> registerNewUser(@Valid @RequestBody UserDTO newUser){
//
//        User createdUser = userService.createUser(newUser);
//
//        if(createdUser == null){
//            return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
//        }
//        UserDTO userDTO = new UserDTO(createdUser);
//
//        return new ResponseEntity<>(userDTO, HttpStatus.CREATED);
//    }

//  todo
//    @GetMapping("/all")
//    @PreAuthorize("hasRole('ADMIN')")
//    public ResponseEntity<List<UserDTO>> loadAllUsers() {
//        System.out.println("\n-----------\nENTERED LOADALL USERS\n-----------\n");
//
//        List<User> users = this.userService.findAll();
//
//        // convert users to userDTOs
//        List<UserDTO> usersDTO = new ArrayList<>();
//        for (User u : users) {
//            usersDTO.add(new UserDTO(u));
//        }
//
//        return new ResponseEntity<>(usersDTO, HttpStatus.OK);
//    }
//  todo
//    @GetMapping("/whoami")
//    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
//    public ResponseEntity<UserDTO> user(Authentication user) {
//        User foundUser = this.userService.findByUsername(user.getName());
//        return new ResponseEntity<>(new UserDTO(foundUser), HttpStatus.OK);
//    }

//  todo
//    @GetMapping("/{id}")
//    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
//    public ResponseEntity<UserDTO> getUser(@PathVariable Long id) {
//        User foundUser = this.userService.findById(id);
//
//        if (foundUser == null) {
//            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
//        }
//        return new ResponseEntity<UserDTO>(new UserDTO(foundUser), HttpStatus.OK);
//    }

    @PutMapping("/")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Void> changeOwnPassword(Authentication loggedUser, @RequestBody @NotBlank HashMap<String, String> passwords) {
//        if (password == null || "".equals(password.trim()))
        if (passwords == null || passwords.isEmpty())
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        User subjectUser = userService.findByEmail(loggedUser.getName());

        if (subjectUser == null)
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        boolean passwordChangeSuccessful = userService.changeOwnPassword(passwords.get("oldPass"), passwords.get("newPass"), subjectUser);
        if (!passwordChangeSuccessful)
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/edit")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity changeOwnData(Authentication loggedUser, @Valid @RequestBody ChangeDataUserDTO newData) {
        User foundUser = userService.findByEmail(loggedUser.getName() );

        if (foundUser == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        if (userService.changeOwnData(newData, foundUser) == false)
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();

        return ResponseEntity.status(HttpStatus.OK).build();
    }

//            todo (name, surname, birthday) is separated in API Endpoint "verifyAccount",
//              where account data is only being put in for the very 1st time



//  todo
//    @PutMapping("/{id}")
//    @PreAuthorize("hasRole('ADMIN')")
//    public ResponseEntity<String> resolveAccountRequestStatus(@PathVariable Long id) {
//        User foundUser = userService.findById(id);
//        if(foundUser == null) {
//            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
//        }
//        userService.remove(foundUser);
//        return new ResponseEntity<>(null, HttpStatus.OK);
//    }

    @GetMapping("/{id}/moderates")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<List<FacilityOverviewDTO>> getFacilitiesManagedBy(@PathVariable(name = "id") Long managerId, Authentication loggedUser) {

        User foundUser = userService.findById(managerId);
        if (foundUser == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        List<FacilityOverviewDTO> managedFacilities = new ArrayList<>();
        for (Facility f : foundUser.getManagedFacilities()) {
            managedFacilities.add(new FacilityOverviewDTO(f));
        }
        return new ResponseEntity<>(managedFacilities, HttpStatus.OK);
    }

    @PostMapping(value = "/logout")
    public ResponseEntity<Void> invalidateToken() {
        // TODO:
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
