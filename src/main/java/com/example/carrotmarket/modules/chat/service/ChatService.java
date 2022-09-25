package com.example.carrotmarket.modules.chat.service;

import com.example.carrotmarket.enums.ResponseEnum;
import com.example.carrotmarket.handler.exception.CustomApiException;
import com.example.carrotmarket.modules.chat.domain.dto.ChatRoomDto;
import com.example.carrotmarket.modules.chat.domain.entity.ChatRoom;
import com.example.carrotmarket.modules.chat.repository.ChatRoomRepository;
import com.example.carrotmarket.modules.product.domain.entity.Product;
import com.example.carrotmarket.modules.product.repository.ProductRepository;
import com.example.carrotmarket.modules.user.domain.entity.User;
import com.example.carrotmarket.modules.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ChatService {

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;


    @Transactional
    public ChatRoomDto makeRoom(Long userIdx, Long productIdx){

        User user = userRepository.findById(userIdx).orElseThrow(() -> new CustomApiException(ResponseEnum.USER_NOT_FOUND));

        Optional<ChatRoom> chatRoomCkOpt = chatRoomRepository.findByProduct_IdxAndSender_Idx(productIdx,user.getIdx());
        if(chatRoomCkOpt.isPresent()){
            // 현재 방이 존재하면 throw!
            throw new CustomApiException(ResponseEnum.CHAT_ROOM_MAKE_EXIST);
        }

        // 존재하지 않는 상품이면 throw
        Product product =  productRepository.findByIdx(productIdx).orElseThrow(() -> new CustomApiException(ResponseEnum.CHAT_ROOM_MAKE_PRODUCT_NOT_EXIST));

        if(product.getUser().getIdx().equals(userIdx)){
            // 자기 자신에게는 보내지 못하게!
            throw new CustomApiException(ResponseEnum.CHAT_ROOM_CAN_NOT_TO_ME);
        }

        ChatRoom chatRoom = ChatRoom.builder()
                        .product(product)
                        .sender(user)
                        .build();

        chatRoomRepository.save(chatRoom);
        return new ChatRoomDto(chatRoom);

    }

    @Transactional
    public List<ChatRoomDto> roomList(User user){
        List<ChatRoom> roomOpt = chatRoomRepository.findAllByUserIdxFetchAll(user.getIdx());
        return roomOpt.stream().map(ChatRoomDto::new).collect(Collectors.toList());
    }

}