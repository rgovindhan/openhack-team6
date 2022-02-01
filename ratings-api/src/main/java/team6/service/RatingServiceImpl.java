package team6.service;

import java.util.Calendar;

import team6.domain.Rating;

public class RatingServiceImpl implements RatingService {

    public Rating getRatingById(String id) {
        switch(id){
            case "123":
            return new Rating("123", "user1", "product1", "LondonIceCreamShop", 3, "Treat in summer", Calendar.getInstance());
            case "456":
            return new Rating("456", "user2", "product1", "LondonIceCreamShop", 3, "New flavor", Calendar.getInstance());
            default:
            return null;
        }
        
    }
}