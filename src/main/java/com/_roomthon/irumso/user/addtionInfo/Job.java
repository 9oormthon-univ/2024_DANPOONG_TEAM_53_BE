package com._roomthon.irumso.user.addtionInfo;

public enum Job {
    STUDENT("대학생/대학원생"),
    WORKER("근로자/직장인"),
    JOB_SEEKER("구직자/실업자");

    private final String description;

    Job(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
