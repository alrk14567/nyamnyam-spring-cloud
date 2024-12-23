package kr.chat.controller;





import kr.chat.document.Chat;
import kr.chat.service.ChatRoomService;
import kr.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;


import java.time.LocalDateTime;



@RequiredArgsConstructor
@RestController
@CrossOrigin
@RequestMapping("/chats")
public class ChatController {

    private final ChatService chatService;
    private final ChatRoomService chatRoomService;


    // 채팅 방에서 메세지를 보내면 저장하는 친구
    @PostMapping("/{chatRoomId}")
    public Mono<Chat> setMessage(@RequestBody Chat chat, @PathVariable String chatRoomId) {
        chat.setCreatedAt(LocalDateTime.now());
        chat.setChatRoomId(chatRoomId);

        // 채팅방 ID로 채팅방 정보를 가져옴
        return chatRoomService.findById(chatRoomId)
                .map(chatRoom -> {
                    // 참가자 수를 가져와서 chat 객체에 설정
                    chat.setTotalParticipants(chatRoom.getParticipants().stream().count());
                    return chat; // 처리한 chat 객체 반환
                })
                .flatMap(chatService::saveMessage); // 채팅 메시지 저장
    }

    //얘는 보낸 메세지를 바로 채널에다가  뿌려주는 친구
    @GetMapping( "/{chatRoomId}")
    public Flux<Chat> getMessageByChannel(@PathVariable String chatRoomId) {

        return chatService.mFindByChatRoomId(chatRoomId).subscribeOn(Schedulers.boundedElastic());
    }


    // 채팅방 별 읽지 않은 메시지 수
    @GetMapping("/{chatRoomId}/unreadCount/{nickname}")
    public Mono<Long> getUnreadCount(@PathVariable String chatRoomId, @PathVariable String nickname) {
        return chatService.getUnreadMessageCountByChatRoomId(chatRoomId, nickname);
    }

    // 특정 메시지에서 읽지 않은 참가자 수
    @GetMapping("/{chatId}/notReadParticipantsCount")
    public Mono<Long> getNotReadParticipantsCount(@PathVariable String chatId) {
        return chatService.getParticipantsNotReadCount(chatId);
    }

    @PatchMapping("/{chatId}/read/{nickname}")
    public Mono<Chat> markMessageAsRead(@PathVariable String chatId, @PathVariable String nickname) {
        return chatService.markAsRead(chatId, nickname);
    }

    @PutMapping("/{chatId}/read/{nickname}")
    public Mono<Chat> updateReadBy(@PathVariable String chatId, @PathVariable String nickname) {
        return chatService.updateReadBy(chatId, nickname);
    }





}