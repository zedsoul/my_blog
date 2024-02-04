package com.example.blogserver.chatroom;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.example.blogserver.Utils.BeanCopyUtils;
import com.example.blogserver.Utils.MessageUtils;
import com.example.blogserver.Vo.GroupChatVO;
import com.example.blogserver.Vo.VoiceVO;
import com.example.blogserver.entity.ChatLog;
import com.example.blogserver.entity.GroupChat;
import com.example.blogserver.entity.User;
import com.example.blogserver.enums.FilePathEnum;
import com.example.blogserver.mapper.ChatLogMapper;
import com.example.blogserver.mapper.GroupChatMapper;
import com.example.blogserver.service.UserService;
import com.example.blogserver.strategy.context.UploadStrategyContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static com.zlc.blogcommon.constant.WebsocketMessageConstant.*;


@ServerEndpoint(value = "/admin/chat/{uid}", configurator = GetHttpSessionConfigurator.class)
@Service
@Slf4j
public class ChatEndpoint {

    //用map管理endpoint对象
    private static Map<String, ChatEndpoint> onlineUsers = new ConcurrentHashMap<>();

    //声明session对象，通过该对象可以发送消息给指定用户
    private Session session;

    private Long uid;

    //声明HttpSession,该对象存了用户名
    private HttpSession httpSession;

    private String nickname;

    @Resource
    private UserService userServiceAuto;
    @Resource
    private ChatLogMapper chatLogDaoAuto;
    @Resource
    private GroupChatMapper groupChatDaoAuto;

    private static UserService userService;

    private static ChatLogMapper chatLogDao;

    private static GroupChatMapper groupChatDao;

    @Autowired
    public void setUploadStrategyContext(UploadStrategyContext uploadStrategyContext) {
        ChatEndpoint.uploadStrategyContext = uploadStrategyContext;
    }

    private static UploadStrategyContext uploadStrategyContext;

    @PostConstruct
    public void init() {
        userService = this.userServiceAuto;
        groupChatDao = this.groupChatDaoAuto;
        chatLogDao = this.chatLogDaoAuto;//将注入的对象交给静态对象管理
    }

    /**
     * 连接建立时被调用
     */
    @OnOpen
    public void onOpen(@PathParam("uid") Long uid, Session session, EndpointConfig config) {
        this.session = session;
        com.example.blogserver.entity.User user = userService.selectByUid(uid);

        log.info("聊天建立：{}", nickname);
        this.nickname = user.getNickname() ;
        this.uid=uid;
        //将当前对象存到容器里
        onlineUsers.put(nickname, this);

        //将当前在线用户的用户名推送给所以客户端
        //1.获取消息
        String message = MessageUtils.getMessage(1, "online", this.nickname,0,uid);
        //2.调用方法进行系统消息推送
        broadcastAllUsers(message);
    }

    private void broadcastAllUsers(String message) {
        try {
            // 将消息推送给所有客户端
            Set<String> nickname = onlineUsers.keySet();
            for (String name : nickname) {
                ChatEndpoint chatEndpoint = onlineUsers.get(name);
                chatEndpoint.session.getBasicRemote().sendText(message);
            }
        } catch (Exception e) {
            log.error("群聊播送出现异常");
            e.printStackTrace();
        }
    }

    private Set<String> getNames() {
        return onlineUsers.keySet();
    }

    /**
     * 收到客户端发来消息
     *
     * @param message 消息对象
     */
    @OnMessage
    public void onMessage(String message, Session session) {
//        log.info("服务端收到客户端发来的消息: {}", message);
        try {
            ObjectMapper mapper = new ObjectMapper();
            Message mess = mapper.readValue(message, Message.class);
            switch (mess.getMesType()) {
                case PRIVATE_IMAGE_MESSAGE:
                case PRIVATE_TEXT_MESSAGE: // 私聊消息
                    // 获取要发送的用户的用户名
                    String toName = mess.getToName();
                    String data = mess.getMessage();
                    // 发送给客户端的message
                    String messageToSend = MessageUtils.getMessage(mess.getMesType(), this.nickname, data,2,mess.getAvatar(), mess.getId(),mess.getSendId());
                    onlineUsers.get(toName).session.getBasicRemote().sendText(messageToSend);
                    break;
                case GROUP_IMAGE_MESSAGE:
                case GROUP_TEXT_MESSAGE: // 群聊消息
                    // 调用service查出该用户的昵称和头像
                    User user = userService.selectByUid(mess.getId());
                    GroupChatVO groupChatVO = new GroupChatVO();
                    BeanUtils.copyProperties(user, groupChatVO);
                    groupChatVO.setContent(mess.getMessage());
                    String groupData = mess.getMessage();
                    groupChatVO.setType(mess.getMesType());
                    String groupMess = MessageUtils.getMessage(mess.getMesType(), this.nickname, groupData,3,mess.getAvatar(),  mess.getId(),mess.getSendId());
                    //调用方法进行群聊消息推送
                    broadcastAllUsers(groupMess);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 链接关闭时调用
     *
     * @param session session
     */
    @OnClose
    public void onClose(Session session, @PathParam("uid") Long uid, CloseReason closeReason) {
        log.info("有用户断开了, id为:{}", session.getId());
        log.info("关闭原因: {}", closeReason);
        com.example.blogserver.entity.User user = userService.selectByUid(uid);

        //从容器追踪删除指定用户
        onlineUsers.remove(user.getNickname());
        //1.获取消息
        String message = MessageUtils.getMessage(SYSTEM_BROADCAST, "offline", user.getNickname(),0,uid);
        //2.调用方法进行系统消息推送
        broadcastAllUsers(message);
    }

    /**
     * 发生错误
     *
     * @param throwable e
     */
    @OnError
    public void onError(Throwable throwable) {
        throwable.printStackTrace();
    }


    /**
     * 发送语音
     *
     * @param voiceVO 语音路径
     */
    public HashMap<String, String> sendVoice(VoiceVO voiceVO) {
        // 上传语音文件
        String content = uploadStrategyContext.executeUploadStrategy(voiceVO.getFile(), FilePathEnum.VOICE.getPath());
        voiceVO.setContent(content);
        Long msgId = IdWorker.getId(ChatLog.class);
        HashMap<String, String> map = new HashMap<>();
        map.put("url", content);
        map.put("msgId", msgId.toString());
        if (Objects.isNull(voiceVO.getReceiver())) { //代表是群聊语音
            GroupChat groupChat = BeanCopyUtils.copyObject(voiceVO, GroupChat.class);
            groupChat.setMsgId(msgId);
            groupChat.setUid(voiceVO.getSender());
            groupChatDao.insert(groupChat);
            //调用方法进行群聊消息推送
            // 发送消息
            String messageToSend = MessageUtils.getMessage(GROUP_VOICE_MESSAGE, voiceVO.getFromName(), map,1);
            broadcastAllUsers(messageToSend);
        } else {
            // 保存记录
            ChatLog chatLog = BeanCopyUtils.copyObject(voiceVO, ChatLog.class);
            chatLog.setMsgId(msgId);
            chatLogDao.insert(chatLog);
            // 发送消息
            String messageToSend = MessageUtils.getMessage(PRIVATE_VOICE_MESSAGE, voiceVO.getFromName(), map,1);
            // 广播消息
            try {
                onlineUsers.get(voiceVO.getToName()).session.getBasicRemote().sendText(messageToSend);
            } catch (Exception e) {
                log.info("接收语音的人" + voiceVO.getToName() + "未上线：" + e.getMessage());
            }
        }
        return map;
    }

}
