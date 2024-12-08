package com.capstone.bankit.data.models;

public class LoginResponse {
    private int statusCode;
    private String message;
    private Data data;

    // Getters and setters
    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public static class Data {
        private String customToken;

        public String getCustomToken() {
            return customToken;
        }

        public void setCustomToken(String customToken) {
            this.customToken = customToken;
        }
    }
}