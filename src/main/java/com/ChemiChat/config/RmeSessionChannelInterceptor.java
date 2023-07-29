package com.ChemiChat.config;

import com.ChemiChat.security.JwtTokenProvider;
import com.ChemiChat.security.JwtTokenValidator;
import com.ChemiChat.service.CustomUserDetailsService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.simp.user.DefaultUserDestinationResolver;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.messaging.*;

import java.security.Principal;
import java.util.Map;
import java.util.List;
import java.util.Objects;

public class RmeSessionChannelInterceptor implements ChannelInterceptor {

    @Value("${app.jwt.header}")
    private String tokenRequestHeader;

    @Value("${app.jwt.header.prefix}")
    private String tokenRequestHeaderPrefix;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private JwtTokenValidator jwtTokenValidator;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        //StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            if (accessor.getNativeHeader("Authorization") != null) {
                List<String> authorization = accessor.getNativeHeader("Authorization");
                String accessToken = authorization.get(0).split(" ")[1];

                String jwt = getJwtFromHeader(accessToken);
                if (StringUtils.hasText(jwt) && jwtTokenValidator.validateToken(jwt)) {
                    Long userId = jwtTokenProvider.getUserIdFromJWT(jwt);
                    UserDetails userDetails = customUserDetailsService.loadUserById(userId);
                    List<GrantedAuthority> authorities = jwtTokenProvider.getAuthoritiesFromJWT(jwt);
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, jwt, authorities);
                    //authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    if(SecurityContextHolder.getContext().getAuthentication() == null) {
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                    accessor.setUser(authentication);

                    if(StompCommand.SEND.equals(accessor.getCommand())) {
                        boolean isSent = channel.send(message);
                        if(isSent) {
                            return message;
                        }
                    }
                }
            }
        } else if (StompCommand.DISCONNECT.equals(accessor.getCommand())) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (Objects.nonNull(authentication))
                System.out.println("Disconnected Auth : " + authentication.getName());
            else
                System.out.println("Disconnected Sess : " + accessor.getSessionId());
        }
        return message;
    }


       /* ///////////////////


        *//*StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        List<String> tokenList = accessor.getNativeHeader("Authorization");
        accessor.removeNativeHeader("Authorization");

        String token = null;
        if(tokenList != null && tokenList.size() > 0) {
            token = tokenList.get(0);
        }

        // validate and convert to a Principal based on your own requirements e.g.
        // authenticationManager.authenticate(JwtAuthentication(token))

        if(token != null) {
            String jwt = getJwtFromHeader(token);
            if (StringUtils.hasText(jwt) && jwtTokenValidator.validateToken(jwt)) {
                Long userId = jwtTokenProvider.getUserIdFromJWT(jwt);
                UserDetails userDetails = customUserDetailsService.loadUserById(userId);
                List<GrantedAuthority> authorities = jwtTokenProvider.getAuthoritiesFromJWT(jwt);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, jwt, authorities);
                //authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
                accessor.setUser(authentication);
            }
        }

        *//**//*if (accessor.getMessageType() == SimpMessageType.CONNECT) {
            userRegistry.onApplicationEvent(new SessionConnectedEvent(this, (Message<byte[]>) message, yourAuth));
        } else if (accessor.getMessageType() == SimpMessageType.SUBSCRIBE) {
            userRegistry.onApplicationEvent(new SessionSubscribeEvent(this, (Message<byte[]>)message, yourAuth));
        } else if (accessor.getMessageType() == SimpMessageType.UNSUBSCRIBE) {
            userRegistry.onApplicationEvent(new SessionUnsubscribeEvent(this, (Message<byte[]>)message, yourAuth));
        } else if (accessor.getMessageType() == SimpMessageType.DISCONNECT) {
            userRegistry.onApplicationEvent(new SessionDisconnectEvent(this, (Message<byte[]>)message, accessor.getSessionId(), CloseStatus.NORMAL));
        }*//**//*

        // not documented anywhere but necessary otherwise NPE in StompSubProtocolHandler!
        accessor.setLeaveMutable(true);
        return MessageBuilder.createMessage(message.getPayload(), accessor.getMessageHeaders());*//*
    }*/

    private String getJwtFromHeader(String bearerToken) {
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(tokenRequestHeaderPrefix)) {
            System.out.println("Extracted Token: " + bearerToken);
            return bearerToken.replace(tokenRequestHeaderPrefix, "");
        }
        return null;
    }
}
