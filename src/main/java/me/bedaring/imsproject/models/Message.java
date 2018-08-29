package me.bedaring.imsproject.models;

import java.util.HashSet;
import java.util.Set;

/**
 * This class handles the sms messaging
 */
public class Message {

    // used to hold the list of selected groups to send the message to
    private Set<AssignedGroup> selectedGroups = new HashSet<>();

    // used to hold the list of individually selected users to send the message to
    private Set<User> selectedUsers = new HashSet<>();

    // the content of the sms message
    private String smsMessage;

    // used to build the list of addresses to send the message to
    private Set<String> sendList = new HashSet<>();

    // message subject
    private String subject;

    public Message() {
    }

    public Set<AssignedGroup> getSelectedGroups() {
        return selectedGroups;
    }

    public void setSelectedGroups(Set<AssignedGroup> selectedGroups) {
        this.selectedGroups = selectedGroups;
    }

    public Set<User> getSelectedUsers() {
        return selectedUsers;
    }

    public void setSelectedUsers(Set<User> selectedUsers) {
        this.selectedUsers = selectedUsers;
    }

    public String getSmsMessage() {
        return smsMessage;
    }

    public void setSmsMessage(String smsMessage) {
        this.smsMessage = smsMessage;
    }

    public Set<String> getSendList() {
        return sendList;
    }

    public void setSendList(Set<String> sendList) {
        this.sendList = sendList;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
}
