package com.example.pt6_joel;

public class Team {
    private int teamId;
    private String teamName;
    private String teamAbbreviation;

    public Team(int teamId, String teamName, String teamAbbreviation) {
        this.teamId = teamId;
        this.teamName = teamName;
        this.teamAbbreviation = teamAbbreviation;
    }

    public int getTeamId() {
        return teamId;
    }

    public String getTeamName() {
        return teamName;
    }

    public String getTeamAbbreviation() {
        return teamAbbreviation;
    }
}
