package com.robot.gpk.base.ao;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public class OrderQueryAO {

    private String Account;
    private LocalDateTime WagersTimeBegin;
    private LocalDateTime WagersTimeEnd;
    private LocalDateTime PayoffTimeEnd;
    private List<String> GameCategories;
    private String RawWagersId;
    private String connectionId;
    public void setAccount(String Account) {
         this.Account = Account;
     }
     public String getAccount() {
         return Account;
     }

    public void setWagersTimeBegin(LocalDateTime WagersTimeBegin) {
         this.WagersTimeBegin = WagersTimeBegin;
     }
     public LocalDateTime getWagersTimeBegin() {
         return WagersTimeBegin;
     }

    public void setWagersTimeEnd(LocalDateTime WagersTimeEnd) {
         this.WagersTimeEnd = WagersTimeEnd;
     }
     public LocalDateTime getWagersTimeEnd() {
         return WagersTimeEnd;
     }

    public void setPayoffTimeEnd(LocalDateTime PayoffTimeEnd) {
         this.PayoffTimeEnd = PayoffTimeEnd;
     }
     public LocalDateTime getPayoffTimeEnd() {
         return PayoffTimeEnd;
     }

    public void setGameCategories(List<String> GameCategories) {
         this.GameCategories = GameCategories;
     }
     public List<String> getGameCategories() {
         return GameCategories;
     }

    public void setRawWagersId(String RawWagersId) {
         this.RawWagersId = RawWagersId;
     }
     public String getRawWagersId() {
         return RawWagersId;
     }

    public void setConnectionId(String connectionId) {
         this.connectionId = connectionId;
     }
     public String getConnectionId() {
         return connectionId;
     }

}