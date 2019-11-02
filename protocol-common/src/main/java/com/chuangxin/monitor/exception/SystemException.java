package com.chuangxin.monitor.exception;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//


public class SystemException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private String m_message;
    private String m_details;
    private Throwable m_root;

    public SystemException(String m_message) {
        this.m_message = m_message;
    }

    public SystemException(String m_message, Throwable m_root) {
        super(m_message, m_root);
    }

    public SystemException(String m_message, String m_details, Throwable m_root) {
        this.m_message = m_message;
        this.m_details = m_details;
        this.m_root = m_root;
    }

    public String getM_details() {
        return this.m_details;
    }

    public void setM_details(String m_details) {
        this.m_details = m_details;
    }

    public String getM_message() {
        return this.m_message;
    }

    public void setM_message(String m_message) {
        this.m_message = m_message;
    }

    public Throwable getM_root() {
        return this.m_root;
    }

    public void setM_root(Throwable m_root) {
        this.m_root = m_root;
    }
}
