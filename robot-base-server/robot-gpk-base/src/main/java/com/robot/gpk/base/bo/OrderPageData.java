package com.robot.gpk.base.bo;

import lombok.Data;

@Data
public class OrderPageData {

    private long Id;
    private String Account;
    private int GameCategoryEnum;
    private String GameCategory;
    private String GameType;
    private String WagersTime;
    private int BetAmount;
    private String PayoffTime;
    private int Payoff;
    private double Commissionable;
    private int RawDataType;
    private long RawDataId;
    private String Raw_Result;
    private String Raw_GameType;
    private String RawWagersId;
    public void setId(long Id) {
         this.Id = Id;
     }
     public long getId() {
         return Id;
     }

    public void setAccount(String Account) {
         this.Account = Account;
     }
     public String getAccount() {
         return Account;
     }

    public void setGameCategoryEnum(int GameCategoryEnum) {
         this.GameCategoryEnum = GameCategoryEnum;
     }
     public int getGameCategoryEnum() {
         return GameCategoryEnum;
     }

    public void setGameCategory(String GameCategory) {
         this.GameCategory = GameCategory;
     }
     public String getGameCategory() {
         return GameCategory;
     }

    public void setGameType(String GameType) {
         this.GameType = GameType;
     }
     public String getGameType() {
         return GameType;
     }

    public void setWagersTime(String WagersTime) {
         this.WagersTime = WagersTime;
     }
     public String getWagersTime() {
         return WagersTime;
     }

    public void setBetAmount(int BetAmount) {
         this.BetAmount = BetAmount;
     }
     public int getBetAmount() {
         return BetAmount;
     }

    public void setPayoffTime(String PayoffTime) {
         this.PayoffTime = PayoffTime;
     }
     public String getPayoffTime() {
         return PayoffTime;
     }

    public void setPayoff(int Payoff) {
         this.Payoff = Payoff;
     }
     public int getPayoff() {
         return Payoff;
     }

    public void setCommissionable(double Commissionable) {
         this.Commissionable = Commissionable;
     }
     public double getCommissionable() {
         return Commissionable;
     }

    public void setRawDataType(int RawDataType) {
         this.RawDataType = RawDataType;
     }
     public int getRawDataType() {
         return RawDataType;
     }

    public void setRawDataId(long RawDataId) {
         this.RawDataId = RawDataId;
     }
     public long getRawDataId() {
         return RawDataId;
     }

    public void setRaw_Result(String Raw_Result) {
         this.Raw_Result = Raw_Result;
     }
     public String getRaw_Result() {
         return Raw_Result;
     }

    public void setRaw_GameType(String Raw_GameType) {
         this.Raw_GameType = Raw_GameType;
     }
     public String getRaw_GameType() {
         return Raw_GameType;
     }

    public void setRawWagersId(String RawWagersId) {
         this.RawWagersId = RawWagersId;
     }
     public String getRawWagersId() {
         return RawWagersId;
     }

}