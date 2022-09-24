package com.example.carrotmarket.modules.user.controller;

import com.example.carrotmarket.common.ResponseDto;
import com.example.carrotmarket.enums.ResponseEnum;
import com.example.carrotmarket.modules.user.domain.dto.JoinRequestDto;
import com.example.carrotmarket.modules.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    @GetMapping("/nicknameCk")
    public ResponseEntity<?> nicknameCk(@RequestParam(value="nickname") String nickname){
        userService.nicknameCk(nickname);
        return new ResponseEntity<>(new ResponseDto<>(ResponseEnum.USER_USERNAME_CK_SUCCESS), HttpStatus.OK);
    }

    @PostMapping("/join")
    public ResponseEntity<?> join(@Valid @RequestBody JoinRequestDto joinRequestDto, BindingResult bindingResult){
        userService.join(joinRequestDto);
        return new ResponseEntity<>(new ResponseDto<>(ResponseEnum.USER_JOIN_SUCCESS), HttpStatus.OK);
    }

}
