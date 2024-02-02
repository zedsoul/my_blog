package com.example.blogserver.Utils;

import com.example.blogserver.chatroom.ResultMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


public class MessageUtils {

    public static String getMessage(Integer mesType, String fromName, Object message,Integer type,String avatar ,Long id,Long sendId) {
        try {
            ResultMessage result = new ResultMessage();
            result.setMesType(mesType).setMessage(message);
            result.setType(type);
            result.setSendId(sendId);
            result.setId(id);
            result.setAvatar(avatar);
            if (fromName != null) {
                result.setFromName(fromName);
            }
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(result);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static String getMessage(Integer mesType, String fromName, Object message,Integer type ,Long id) {
        try {
            ResultMessage result = new ResultMessage();
            result.setMesType(mesType).setMessage(message);
            result.setType(type);

            result.setId(id);

            if (fromName != null) {
                result.setFromName(fromName);
            }
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(result);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static String getMessage(Integer mesType, String fromName, Object message,Integer type) {
        try {
            ResultMessage result = new ResultMessage();
            result.setMesType(mesType).setMessage(message);
            result.setType(type);

            if (fromName != null) {
                result.setFromName(fromName);
            }
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(result);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
