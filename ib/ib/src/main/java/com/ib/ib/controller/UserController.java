package com.ib.ib.controller;

import com.ib.ib.model.User;
import com.ib.ib.security.dtos.EmailPasswordDTO;
import com.ib.ib.security.JwtTokenUtil;
import com.ib.ib.security.dtos.NewUserDTO;
import com.ib.ib.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.support.MethodArgumentNotValidException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/user")
@AllArgsConstructor
public class UserController {
    @Autowired
    private UserService userService;


    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private PasswordEncoder passwordEncoder;

    // create /api/passenger
    @PostMapping(value = "/register", consumes = "application/json")
    public ResponseEntity<Void> register(@Valid @RequestBody NewUserDTO userDTO) throws Exception {
        User user = new User();

        if(userService.loadUserByEmail(userDTO.getEmail())!=null){
            throw new Exception("User with that email already exists!");
        }
        if(userService.loadUserByEmail(userDTO.getTelephoneNumber())!=null){
            throw new Exception("User with that telephone number already exists! ");
        }
        if(!userDTO.getPasswordConfirmation().equals(userDTO.getPassword())){
            throw new Exception("Passwords do not match!");
        }
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmail());
        user.setPasswordHash(passwordEncoder.encode(userDTO.getPassword()));
        user.setTelephoneNumber(userDTO.getTelephoneNumber());
        user.setActivated(true);  // TODO: set false
        user.setAdmin(false);
        userService.save(user);
//
//        if (passengerActivationRepository.existsByPassenger(user)) {
//            passengerActivationRepository.deleteByPassenger(user);
//        }
//        PassengerActivation activation = new PassengerActivation(rand.nextInt(Integer.MAX_VALUE), user, LocalDateTime.now());
//        passengerActivationRepository.save(activation);
//
//        emailService.sendSimpleMessage(user.getEmail(), "Confirm your email",
//                "Dear, " + user.getName() + "!\n\nTo finish your registration, please, " +
//                        "enter this activation code:\n" + activation.getActivationId() + "\n\n" +
//                        "If you did not perform registration - contact our support:\n" +
//                        "support@easy.go\n\nBest regards,\nEasyGo team!");
        return new ResponseEntity<>(HttpStatus.OK);
    }

//    @GetMapping(value="/activate/{activationId}")
//    public ResponseEntity<MsgDTO> activatePassenger(@PathVariable("activationId") Integer activationId) throws UberException {
//        passengerActivationService.activate(activationId);
//        MsgDTO msgDTO = new MsgDTO("Successful account activation!");
//        return new ResponseEntity<MsgDTO>(msgDTO, HttpStatus.OK);
//
//    }
//
//
//    @GetMapping(value="/test")
//    public ResponseEntity<String> activatePassenger() {
//        return new ResponseEntity<String>("Yeah", HttpStatus.OK);
//    }

//
//    @PutMapping(value = "/{id}/changePassword")
//    public ResponseEntity<Void> changePassword(
//            @PathVariable("id") Integer id,
//            Principal user,
//            @RequestBody ChangePasswordDTO passwords
//    ) throws UberException {
//        var actualUser = userService.getUser(user.getName());
//        if (actualUser.getRole() != Role.ADMIN && !Objects.equals(actualUser.getId(), id)) {
//            throw new UberException(HttpStatus.NOT_FOUND, "User does not exist!");
//        }
//        userService.changePassword(id, passwords);
//        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//    }
//
//    @GetMapping(value = "/{id}/resetPassword")
//    public ResponseEntity<Void> sendEmail(
//            @PathVariable("id") Integer id
//    ) throws UberException {
//        User user = userService.getUserById(id);
//        if (user == null) {
//            throw new UberException(HttpStatus.NOT_FOUND, "User does not exist!");
//        }
//        userService.resetPassword(user.getEmail());
//        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//    }
//
//    @PutMapping(value = "/{id}/resetPassword")
//    public ResponseEntity<Void> resetPassword(
//            @Valid
//            @PathVariable("id") Integer id,
//            @RequestBody ResetPasswordDTO newPassword
//    ) throws UberException {
//        var user = userService.getUserById(id);
//        if (user == null) {
//            throw new UberException(HttpStatus.NOT_FOUND, "User does not exist!");
//        }
//        userService.resetPassword(user.getEmail(), newPassword);
//        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//    }
//

    @PostMapping(value = "/login")
    public ResponseEntity<String> login(@RequestBody EmailPasswordDTO passwordDTO) {
        UsernamePasswordAuthenticationToken authReq = new UsernamePasswordAuthenticationToken(
                passwordDTO.getEmail(),
                passwordDTO.getPassword()
        );
        Authentication auth = authenticationManager.authenticate(authReq);

        SecurityContext sc = SecurityContextHolder.getContext();
        sc.setAuthentication(auth);

        User user = userService.loadUserByEmail(passwordDTO.getEmail());
//        if (!user.getActive()) {
//            throw new UberException(HttpStatus.BAD_REQUEST, "User not activated (check email)!");
//        }

        return new ResponseEntity<>(jwtTokenUtil.generateToken(passwordDTO.getEmail(), user.isAdmin(), user.getId()), HttpStatus.OK);
    }

    @GetMapping(value = "/me")
    public ResponseEntity<String> userDTOResponseEntity(Principal principal) {
        User user = userService.loadUserByEmail(principal.getName());
        return new ResponseEntity<>(user.getFirstName(), HttpStatus.OK);
    }
}
