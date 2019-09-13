package com.example.springbootwebsocket.server;

import com.example.springbootwebsocket.domain.Message;
import com.example.springbootwebsocket.utils.IdGenerator;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Websocket 文字通讯
 *
 */
@ServerEndpoint(value = "/websocket/chat")
@Component
public class TextController extends BaseController {

	private static final List<AbstractWsController> CONNECTIONS = new CopyOnWriteArrayList<>();

	private Message.RoomInfo roomInfo;

	@Override
	@OnOpen
	public void onOpen(Session session, EndpointConfig config) {
		// 设置用户信息
		setUserName(IdGenerator.getNextId());
		setSession(session);
		// 设置聊天室信息
		if (CONNECTIONS.size() == 0) {
			setRoomInfo(new Message.RoomInfo(getUserName(), (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(new Date())));
		} else {
			Iterator<AbstractWsController> it = CONNECTIONS.iterator();
			TextController client = (TextController) it.next();
			setRoomInfo(client.getRoomInfo());
		}
		Message msg = new Message(getUserName(), Message.MsgConstant.SET_NAME);
		msg.setRoomInfo(getRoomInfo());
		call(msg.toString());
		super.onOpen(session, config);
	}

	@Override
	@OnClose
	public void onClose() {
		super.onClose();
	}

	@Override
	@OnMessage(maxMessageSize = 10000000)
	public void onMessage(String message) {
		super.onMessage(message);
	}

	@Override
	@OnMessage(maxMessageSize = 10000000)
	public void onMessage(ByteBuffer message) {
		super.onMessage(message);
	}

	@Override
	@OnError
	public void onError(Throwable t) {
	}

	@Override
	List<AbstractWsController> getConnections() {
		return CONNECTIONS;
	}

	/**
	 * 设置聊天室信息
	 */
	private void setRoomInfo(Message.RoomInfo roomInfo) {
		this.roomInfo = roomInfo;
	}

	private Message.RoomInfo getRoomInfo() {
		return roomInfo;
	}

	@Override
	String getConnectType() {
		return "text";
	}

}
