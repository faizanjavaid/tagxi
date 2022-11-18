package com.tyt.driver.utilz;

import java.util.List;
import java.util.Map;

class KeySearchClass {

    private static String searchValue;

    static String KeySearch(Map.Entry<String, List<String>> search) {
        if (search.getKey() != null)
            if (search.getKey().equalsIgnoreCase("mobile"))
                searchValue = search.getValue().get(0);
            else if (search.getKey().equalsIgnoreCase("email"))
                searchValue = search.getValue().get(0);
            else if (search.getKey().equalsIgnoreCase("terms_condition"))
                searchValue = search.getValue().get(0);
            else if (search.getKey().equalsIgnoreCase("uuid"))
                searchValue = search.getValue().get(0);
            else if (search.getKey().equalsIgnoreCase("otp"))
                searchValue = search.getValue().get(0);
            else if (search.getKey().equalsIgnoreCase("cart_items"))
                searchValue = search.getValue().get(0);
            else if (search.getKey().equalsIgnoreCase("coupon_code"))
                searchValue = search.getValue().get(0);
            else if (search.getKey().equalsIgnoreCase("order_id"))
                searchValue = search.getValue().get(0);
            else if (search.getKey().equalsIgnoreCase("old_password"))
                searchValue = search.getValue().get(0);
            else if (search.getKey().equalsIgnoreCase("last_number"))
                searchValue = search.getValue().get(0);
        return searchValue;
    }
}
