package com.ib.ib.controller;

import com.ib.ib.model.User;
import com.ib.ib.security.EmailPasswordDTO;
import com.ib.ib.security.JwtTokenUtil;
import com.ib.ib.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Objects;

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
//
//    // create /api/passenger
//    @PostMapping(consumes = "application/json")
//    public ResponseEntity<User> register(@Valid @RequestBody User passengerDTO) throws Exception {
//        Passenger passenger = new Passenger();
//
//        if(userService.getUser(passengerDTO.getEmail())!=null){
//            throw new UberException(HttpStatus.BAD_REQUEST, "User with that email already exists! ");
//        }
//        if(userService.getUserByTelephoneNumber(passengerDTO.getTelephoneNumber())!=null){
//            throw new UberException(HttpStatus.BAD_REQUEST, "User with that telephone number already exists! ");
//        }
//        if(!passengerDTO.getConfirmPassword().equals(passengerDTO.getPassword())){
//            throw new UberException(HttpStatus.BAD_REQUEST, "Passwords do not match!");
//        }
//        passenger.setName(passengerDTO.getName());
//        passenger.setSurname(passengerDTO.getSurname());
//        passenger.setEmail(passengerDTO.getEmail());
//        passenger.setPassword(passwordEncoder.encode(passengerDTO.getPassword()));
//        passenger.setTelephoneNumber(passengerDTO.getTelephoneNumber());
//        passenger.setAddress(passengerDTO.getAddress());
//        passenger.setProfilePicture(passengerDTO.getProfilePicture());
//        passenger.setActive(false);
//        passenger.setBlocked(false);
//        passenger = passengerServiceJPA.save(passenger);
//
//        if (passengerActivationRepository.existsByPassenger(passenger)) {
//            passengerActivationRepository.deleteByPassenger(passenger);
//        }
//        PassengerActivation activation = new PassengerActivation(rand.nextInt(Integer.MAX_VALUE), passenger, LocalDateTime.now());
//        passengerActivationRepository.save(activation);
//
//        emailService.sendSimpleMessage(passenger.getEmail(), "Confirm your email",
//                "Dear, " + passenger.getName() + "!\n\nTo finish your registration, please, " +
//                        "enter this activation code:\n" + activation.getActivationId() + "\n\n" +
//                        "If you did not perform registration - contact our support:\n" +
//                        "support@easy.go\n\nBest regards,\nEasyGo team!");
//        return new ResponseEntity<>(new PassengerDTOResult(passenger), HttpStatus.OK);  // it should be created......
//    }
//
//    @GetMapping(value="/activate/{activationId}")
//    public ResponseEntity<MsgDTO> activatePassenger(@PathVariable("activationId") Integer activationId) throws UberException {
//        passengerActivationService.activate(activationId);
//        MsgDTO msgDTO = new MsgDTO("Successful account activation!");
//        return new ResponseEntity<MsgDTO>(msgDTO, HttpStatus.OK);
//
//    }
//

    @GetMapping(value="/test")
    public ResponseEntity<String> activatePassenger() {
        return new ResponseEntity<String>("Yeah", HttpStatus.OK);
    }

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

        return new ResponseEntity<>(jwtTokenUtil.generateToken(passwordDTO.getEmail(), user.getId()), HttpStatus.OK);
    }
}
