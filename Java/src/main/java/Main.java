import io.unlaunch.UnlaunchAttribute;
import io.unlaunch.UnlaunchClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class Main {

    private static final String SDK_KEY = "prod-server-0bdc1324-cbec-4740-83e9-86045d1cd93f";

    private static final String FLAG_KEY = "payment-gateway";

    // Name of the attribute defined in the feature flag
    private static final String COUNTRY_ATTRIBUTE_NAME = "country";

    // Test users in Target Users. These users will use NEW payment gateway regardless of their
    // country
    private static final String TEST_USER_1 = "user1a@company.com";
    private static final String TEST_USER_2 = "user1b@company.com";

    // Users from these countries will use NEW payment gateway
    private static final String USA = "USA";
    private static final String CAN = "CAN";

    private static final String[] USERS = {
            TEST_USER_1,
            "user2_@company.com",
            "user3_@company.com",
            "user4_@company.com",
            "user5_@company.com",
            TEST_USER_2
    };
    private static final String[] COUNTRIES = {USA, "VNM", "COL",  "JPN", "FRA", "ITA", CAN};

    // Number of time to iterate and generate random users
    private static final int NUM_ITERATIONS = 20;

    public static void main(String[] args) {
        final UnlaunchClient client = UnlaunchClient.create(SDK_KEY);

        try {
            client.awaitUntilReady(6, TimeUnit.SECONDS);
        } catch (InterruptedException | TimeoutException e) {
            System.out.println("Client wasn't ready, " + e.getMessage());
        }

        printBanner();

        final Random Random = new Random();
        // Randomly generate userIds and countries to mock request
        for (int i = 0; i < NUM_ITERATIONS ; i++)
        {
            final String userId = USERS[Random.nextInt(USERS.length)];

            final String country = COUNTRIES[Random.nextInt(COUNTRIES.length)];
            final List<String> countrySet = new ArrayList<>();
            countrySet.add(country);

            final String variation = client.getVariation(FLAG_KEY,
                    userId,
                    UnlaunchAttribute.newSet(COUNTRY_ATTRIBUTE_NAME, countrySet));

            if (variation.equals("on")) {
                System.out.println(userId + " from " + country + ": use NEW payment gateway");
            } else {
                System.out.println(userId + " from " + country + ": use old payment gateway");
            }
        }

        client.shutdown();
    }

    private static void printBanner() {
        System.out.println("Users allowed for new payment gateway: " + TEST_USER_1 + ", " + TEST_USER_2);
        System.out.println("Countries allowed for new payment gateway: " + USA + ", " + CAN);
        System.out.println("");
        System.out.println("Mocking " + NUM_ITERATIONS  + " requests");
        System.out.println("---");
    }
}
