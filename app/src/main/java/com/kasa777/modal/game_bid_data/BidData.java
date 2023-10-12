package com.kasa777.modal.game_bid_data;

public class BidData {

    public String digits, points, gameType, gemeSession, gameDate,gameTypeId;
    public String gameTypePrice ;

    public BidData(String digits, String points, String gameType, String gemeSession, String gameDate, String gameTypePrice, String gameTypeId) {
        this.digits = digits;
        this.points = points;
        this.gameType = gameType;
        this.gemeSession = gemeSession;
        this.gameDate = gameDate;
        this.gameTypePrice = gameTypePrice;
        this.gameTypeId = gameTypeId;
    }

    public String getGameType() {
        return gameType;
    }

    public void setGameType(String gameType) {
        this.gameType = gameType;
    }

    public String getGemeSession() {
        return gemeSession;
    }

    public void setGemeSession(String gemeSession) {
        this.gemeSession = gemeSession;
    }

    public String getGameDate() {
        return gameDate;
    }

    public void setGameDate(String gameDate) {
        this.gameDate = gameDate;
    }

    public String getGameTypePrice() {
        return gameTypePrice;
    }

    public void setGameTypePrice(String gameTypePrice) {
        this.gameTypePrice = gameTypePrice;
    }

    public String getGameTypeId() {
        return gameTypeId;
    }

    public void setGameTypeId(String gameTypeId) {
        this.gameTypeId = gameTypeId;
    }

    public String getPoints() {

        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    public String getDigits() {
        return digits;
    }

    public void setDigits(String digits) {
        this.digits = digits;
    }
}
