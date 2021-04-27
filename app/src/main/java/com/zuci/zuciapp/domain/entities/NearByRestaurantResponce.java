package com.zuci.zuciapp.domain.entities;

public class NearByRestaurantResponce {

    private OperatingDays[] operatingDays;

    private Distance distance;

    private RestaurantRating restaurantRating;

    private String name;

    private String description;

    private String[] cuisine;

    private String _id;

    private String profileImage;

    private String[] services;

    public OperatingDays[] getOperatingDays() {
        return operatingDays;
    }

    public void setOperatingDays(OperatingDays[] operatingDays) {
        this.operatingDays = operatingDays;
    }

    public Distance getDistance() {
        return distance;
    }

    public void setDistance(Distance distance) {
        this.distance = distance;
    }

    public RestaurantRating getRestaurantRating() {
        return restaurantRating;
    }

    public void setRestaurantRating(RestaurantRating restaurantRating) {
        this.restaurantRating = restaurantRating;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String[] getCuisine() {
        return cuisine;
    }

    public void setCuisine(String[] cuisine) {
        this.cuisine = cuisine;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String[] getServices() {
        return services;
    }

    public void setServices(String[] services) {
        this.services = services;
    }

    @Override
    public String toString() {
        return "ClassPojo [operatingDays = " + operatingDays + ", distance = " + distance + ", restaurantRating = " + restaurantRating + ", name = " + name + ", description = " + description + ", cuisine = " + cuisine + ", _id = " + _id + ", profileImage = " + profileImage + ", services = " + services + "]";
    }

    public class RestaurantRating {
        private String count;

        private String rating;

        public String getCount() {
            return count;
        }

        public void setCount(String count) {
            this.count = count;
        }

        public String getRating() {
            return rating;
        }

        public void setRating(String rating) {
            this.rating = rating;
        }

        @Override
        public String toString() {
            return "ClassPojo [count = " + count + ", rating = " + rating + "]";
        }
    }

    public class Location {
        private String[] coordinates;

        private String type;

        public String[] getCoordinates() {
            return coordinates;
        }

        public void setCoordinates(String[] coordinates) {
            this.coordinates = coordinates;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        @Override
        public String toString() {
            return "ClassPojo [coordinates = " + coordinates + ", type = " + type + "]";
        }
    }

    public class OperatingDays {
        private String monday;

        public String getMonday() {
            return monday;
        }

        public void setMonday(String monday) {
            this.monday = monday;
        }

        @Override
        public String toString() {
            return "ClassPojo [monday = " + monday + "]";
        }
    }

    public class Distance {
        private Location location;

        private String calculated;

        public Location getLocation() {
            return location;
        }

        public void setLocation(Location location) {
            this.location = location;
        }

        public String getCalculated() {
            return calculated;
        }

        public void setCalculated(String calculated) {
            this.calculated = calculated;
        }

        @Override
        public String toString() {
            return "ClassPojo [location = " + location + ", calculated = " + calculated + "]";
        }
    }

}
