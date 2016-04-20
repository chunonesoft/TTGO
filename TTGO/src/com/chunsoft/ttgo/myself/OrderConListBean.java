package com.chunsoft.ttgo.myself;

import java.io.Serializable;
import java.util.List;

public class OrderConListBean implements Serializable {
	public String status;
	public String receiveMobile;
	public String statusName;
	public String deliveryNo;
	public String deliveryName;
	public String orderTime;
	public String receiveProvince;
	public String orderNo;
	public String CashOrder;
	public String receiveName;
	public String receiveAddress;
	public String proTotalPrice;
	public List<ProductListBean> productList;
}
