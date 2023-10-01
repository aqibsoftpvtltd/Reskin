package com.kasa77.modal;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class PaymentMode{

	@SerializedName("data")
	public List<DataItem> data;

	@SerializedName("message")
	public String message;

	@SerializedName("status")
	public int status;

	public List<DataItem> getData(){
		return data;
	}

	public String getMessage(){
		return message;
	}

	public int getStatus(){
		return status;
	}

	public class DataItem{

		@SerializedName("mode")
		public String mode;

		@SerializedName("redirectURL")
		public Object redirectURL;

		@SerializedName("disabled")
		public boolean disabled;

		@SerializedName("_id")
		public String id;

		public String getMode(){
			return mode;
		}

		public Object getRedirectURL(){
			return redirectURL;
		}

		public boolean isDisabled(){
			return disabled;
		}

		public String getId(){
			return id;
		}
	}
}