package com.kasa777.ui.fragment;

import org.jetbrains.annotations.Nullable;

public interface GameConstantMessages {
    @Nullable
    String AddBids = "Please Add Bids!!!";
    String AddDigit = "Please Enter Digit!!!";
    String AddPoint = "Please Enter Point!!!";
    String NonZeroPoint = "Please Enter Valid Point!!!";

   int MinPointValue = 5;
   int MaxPointValue = 10000;

    String MinPoint = "Note : Bid Point can't be less than \n₹"+MinPointValue+" !!!";
    String MaxPoint = "Note : Bid Point can't be more than \n₹"+MaxPointValue+" !!!";
    String InvalidWalletAmount = "You Don't Have Required Bid Amount\nPlease Add Fund!!!";
    String SelectGameType = "Please Select Game Type!!!";
    String SelectGameDate = "Please Select Game Date!!!";
    String NoBidsFound = "Please enter valid digits";
    String BidClosedForDay ="BID IS CLOSED FOR THIS DAY,\nPLEASE SELECT ANOTHER DATE";
    String NoDateForBid ="No Date Is Available For Bid";
}
