package com.kasa77.modal.passbookDetail

import com.google.gson.annotations.SerializedName

data class PassBookDetails(

	@field:SerializedName("data")
	val data: Data? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Int? = null
)

data class ToAccount(

	@field:SerializedName("accNumber")
	val accNumber: String? = null,

	@field:SerializedName("accName")
	val accName: String? = null,

	@field:SerializedName("bankName")
	val bankName: String? = null,

	@field:SerializedName("ifscCode")
	val ifscCode: String? = null
)


data class Data(

	@field:SerializedName("gameTypeId")
	val gameTypeId: String? = null,

	@field:SerializedName("bidDigit")
	val bidDigit: String? = null,

	@field:SerializedName("winStatus")
	val winStatus: Int? = null,

	@field:SerializedName("gameDate")
	val gameDate: String? = null,

	@field:SerializedName("mobileNumber")
	val mobileNumber: String? = null,

	@field:SerializedName("userName")
	val userName: String? = null,



	@field:SerializedName("gameTypePrice")
	val gameTypePrice: Double? = null,

	@field:SerializedName("biddingPoints")
	val biddingPoints: Int? = null,

	@field:SerializedName("gameWinPoints")
	val gameWinPoints: Double? = null,

	@field:SerializedName("createdAt")
	val createdAt: String? = null,

	@field:SerializedName("dateStamp")
	val dateStamp: Int? = null,

	@field:SerializedName("providerId")
	val providerId: String? = null,

	@field:SerializedName("gameTypeName")
	val gameTypeName: String? = null,

	@field:SerializedName("_id")
	val id: String? = null,

	@field:SerializedName("providerName")
	val providerName: String? = null,

	@field:SerializedName("gameSession")
	val gameSession: String? = null,

	@field:SerializedName("updatedAt")
	val updatedAt: String? = null,


	@field:SerializedName("toAccount")
	val toAccount: ToAccount? = null,

	@field:SerializedName("withdrawalMode")
	val withdrawalMode: String? = null,

	@field:SerializedName("mobile")
	val mobile: String? = null,

	@field:SerializedName("reqTime")
	val reqTime: String? = null,

	@field:SerializedName("userId")
	val userId: String? = null,

	@field:SerializedName("reqStatus")
	val reqStatus: String? = null,

	@field:SerializedName("reqDate")
	val reqDate: String? = null,

	@field:SerializedName("reqUpdatedAt")
	val reqUpdatedAt: String? = null,

	@field:SerializedName("reqType")
	val reqType: String? = null,

	@field:SerializedName("reqAmount")
	val reqAmount: Double? = null,


	@field:SerializedName("fullname")
	val fullname: String? = null,

	@field:SerializedName("username")
	val username: String? = null,

	@field:SerializedName("timestamp")
	val timestamp: Int? = null



)
