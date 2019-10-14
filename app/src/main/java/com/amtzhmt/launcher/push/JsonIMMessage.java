package com.amtzhmt.launcher.push;

import com.google.gson.Gson;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * @author wangss
 * @date 2016年09月19日
 * @version 1.0
 */
public final class JsonIMMessage {

    private int crcCode = 0;

    private int length;// 消息长度

    private long sessionID;// 会话ID

    private String sessionName;// 会话名称
    
    private int sessionType;

    public int getSessionType() {
		return sessionType;
	}

	public void setSessionType(int sessionType) {
		this.sessionType = sessionType;
	}

	public String getToSessionName() {
        return toSessionName;
    }

    public void setToSessionName(String toSessionName) {
        this.toSessionName = toSessionName;
    }

    private String toSessionName;// 对象会话名称

    private String password;// 会话密码

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSessionName() {
        return sessionName;
    }

    public void setSessionName(String sessionName) {
        this.sessionName = sessionName;
    }

    private int type;// 消息类型

    private int actiontype;// 消息类型

    public int getActiontype() {
        return actiontype;
    }

    public void setActiontype(int actiontype) {
        this.actiontype = actiontype;
    }

    private int priority;// 消息优先级

    private String body = ""; // 消息主体

    /**
     * @return the crcCode
     */
    public final int getCrcCode() {
        return crcCode;
    }

    /**
     * @param crcCode the crcCode to set
     */
    public final void setCrcCode(int crcCode) {
        this.crcCode = crcCode;
    }

    /**
     * @return the length
     */
    public final int getLength() {
        return length;
    }

    /**
     * @param length the length to set
     */
    public final void setLength(int length) {
        this.length = length;
    }

    /**
     * @return the sessionID
     */
    public final long getSessionID() {
        return sessionID;
    }

    /**
     * @param sessionID the sessionID to set
     */
    public final void setSessionID(long sessionID) {
        this.sessionID = sessionID;
    }

    /**
     * @return the type
     */
    public final int getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public final void setType(int type) {
        this.type = type;
    }

    /**
     * @return the priority
     */
    public final int getPriority() {
        return priority;
    }

    /**
     * @param priority the priority to set
     */
    public final void setPriority(int priority) {
        this.priority = priority;
    }

    public ByteBuf returnByteBuf() {
        Gson gson = new Gson();
        String strJson = OriginalUtil.TripleDES.encrypt(gson.toJson(this)) + "$_";
        ByteBuf echo = Unpooled.copiedBuffer(strJson.getBytes());
        return echo;
    }

    /**
     * @return the body
     */
    public final String getBody() {
        return body;
    }

    /**
     * @param body the body to set
     */
    public final void setBody(String body) {
        this.body = body;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        Gson gson = new Gson();
        String strJson = gson.toJson(this);
        return strJson;
    }

    public static JsonIMMessage getObject(String message) {

        message = OriginalUtil.TripleDES.decrypt(message);
        Gson gson = new Gson();
        JsonIMMessage json = null;
        try {
            json = gson.fromJson(message, JsonIMMessage.class);
        } catch (Exception ex) {
            json = null;
        }

        return json;
    }
}