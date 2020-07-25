package com.robot.gpk.base.bo;

import lombok.Data;

import java.util.List;

@Data
public class OrderQueryBO {

    private List<OrderPageData> PageData;
    private boolean IsSuccess;
    public void setPageData(List<OrderPageData> PageData) {
         this.PageData = PageData;
     }
     public List<OrderPageData> getPageData() {
         return PageData;
     }

    public void setIsSuccess(boolean IsSuccess) {
         this.IsSuccess = IsSuccess;
     }
     public boolean getIsSuccess() {
         return IsSuccess;
     }

}