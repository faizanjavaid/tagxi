package com.tyt.client.ui.homeScreen.inAppChat.chathistory;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datum {

@SerializedName("id")
@Expose
private String id;
@SerializedName("message")
@Expose
private String message;
@SerializedName("from_type")
@Expose
private int fromType;
@SerializedName("request_id")
@Expose
private String requestId;
@SerializedName("user_id")
@Expose
private int userId;
@SerializedName("delivered")
@Expose
private int delivered;
@SerializedName("seen")
@Expose
private int seen;
@SerializedName("created_at")
@Expose
private String createdAt;
@SerializedName("updated_at")
@Expose
private String updatedAt;
@SerializedName("message_status")
@Expose
private String messageStatus;
@SerializedName("converted_created_at")
@Expose
private String convertedCreatedAt;

    public String getConvertedCreatedAt() {
        return convertedCreatedAt;
    }

    public void setConvertedCreatedAt(String convertedCreatedAt) {
        this.convertedCreatedAt = convertedCreatedAt;
    }

    public String getId() {
return id;
}

public void setId(String id) {
this.id = id;
}

public String getMessage() {
return message;
}

public void setMessage(String message) {
this.message = message;
}

public int getFromType() {
return fromType;
}

public void setFromType(int fromType) {
this.fromType = fromType;
}

public String getRequestId() {
return requestId;
}

public void setRequestId(String requestId) {
this.requestId = requestId;
}

public int getUserId() {
return userId;
}

public void setUserId(int userId) {
this.userId = userId;
}

public int getDelivered() {
return delivered;
}

public void setDelivered(int delivered) {
this.delivered = delivered;
}

public int getSeen() {
return seen;
}

public void setSeen(int seen) {
this.seen = seen;
}

public String getCreatedAt() {
return createdAt;
}

public void setCreatedAt(String createdAt) {
this.createdAt = createdAt;
}

public String getUpdatedAt() {
return updatedAt;
}

public void setUpdatedAt(String updatedAt) {
this.updatedAt = updatedAt;
}

public String getMessageStatus() {
return messageStatus;
}

public void setMessageStatus(String messageStatus) {
this.messageStatus = messageStatus;
}

}