package main.java.seller_handlers;

import com.google.api.services.gmail.model.Message;
import main.java.MessageHandler;
import main.java.Purchase;

public class HostelworldHandler extends SellerHandler {


    private static String SELLER = "HostelWorld";
    private static String QUERY = "from: \" hostelworld.com\", subject: \"confirmed booking from hostelworld.com\"";

    private String seller;
    private String query;


    /**
     * sets seller name and query that retrieves the relevant seller messages.
     */
    public HostelworldHandler() {
        this.seller = this.SELLER;
        this.query = this.QUERY;
    }

    public String getSeller(){return this.seller;}
    public String getQuery(){return this.query;}


    /**
     * return a purchase object created from parsed data of a message
     *
     * @param msg relevant message that answered the search query.
     * @return a purchase object corresponding msg data.
     */
    public Purchase parseMsg (Message msg){
    String from = seller;
    String item = getItem(MessageHandler.getMessageBody(msg));
    String price = getPrice(MessageHandler.getMessageBody(msg));
    String date = MessageHandler.getMessageDate(msg);
    Purchase purchase = new Purchase(item, from, price, date);
    if (purchase.isValid())
    return purchase;
    return null;
}

private String getPrice (String input){
    int PricesLocation;
    int exactStartOfTotalPriceLocation;
    int exactEndOfTotalPriceLocation;

    PricesLocation = input.indexOf("Total: ");
    exactStartOfTotalPriceLocation = PricesLocation+7; //7 chars in "Total: "
    exactEndOfTotalPriceLocation = input.indexOf("\n",exactStartOfTotalPriceLocation+1);
    return input.substring(exactStartOfTotalPriceLocation, exactEndOfTotalPriceLocation);
}

    private String getItem (String input){
        int itemLocation;
        int exactStartOfItemLocation;
        int exactEndOfItemLocation;

        itemLocation = input.indexOf("booking information");
        exactStartOfItemLocation = itemLocation+23; //lineBreak after "booking information"
        exactEndOfItemLocation = input.indexOf("\n",exactStartOfItemLocation+1);
        String res = input.substring(exactStartOfItemLocation, exactEndOfItemLocation-1);
        res = res.replace("\n", " ").replace("\r", ""); //remove line breaks
        return res;
    }
}
