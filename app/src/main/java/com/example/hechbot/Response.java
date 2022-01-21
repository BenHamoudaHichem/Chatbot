package com.example.hechbot;

public class Response {
    private String cnt;

    public Response(String msg, String sender) {
        this.cnt = cnt;
    }

    @Override
    public String toString() {
        return "Response{" +
                "msg='" + cnt + '\'' +

                '}';
    }

    public String getCnt() {
        return cnt;
    }

    public void setCnt(String cnt) {
        this.cnt = cnt;
    }
}
