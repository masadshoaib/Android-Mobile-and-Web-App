package com.example.p4t2webservice;

/**
 * @author mshoaib | Muhammad Asad Shoaib. Project 4 Task 2. This is to store the results in mongoDB.
 */
public class MongoP4T2 {
    // Instance Variables
    private String word;
    private long timeTaken;
    private String response; // response from API
    private String ua;
    private long dateTime;
    private String sentToPhone; // sent to Phone

    /**
     * Getter
     * @return the response to be sent to phone
     */
    public String getSentToPhone() {
        return sentToPhone;
    }

    /**
     * Setter
     * @param sentToPhone is what needs to be sent to phone
     */
    public void setSentToPhone(String sentToPhone) {
        this.sentToPhone = sentToPhone;
    }

    public long getTimeTaken() {
        return timeTaken;
    }

    public void setTimeTaken(long timeTaken) {
        this.timeTaken = timeTaken;
    }

    public long getDateTime() {
        return dateTime;
    }

    public void setDateTime(long dateTime) {
        this.dateTime = dateTime;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }


    public String getUa() {
        return ua;
    }

    public void setUa(String ua) {
        this.ua = ua;
    }


    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}
