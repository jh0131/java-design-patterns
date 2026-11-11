public enum VenueType {
    GUKBAP("51장국밥"),
    COVER("바비든든"),
    BUNSIK("폭풍분식"),
    CHINESE("중식대장");

    final String name;

    VenueType(String name) {
        this.name = name;
    }

    static VenueType ofRestaurant(String restaurantName) {
        for (VenueType venue : values()) {
            if (venue.name.equals(restaurantName)) {
                return venue;
            }
        }
        return null;
    }
}
