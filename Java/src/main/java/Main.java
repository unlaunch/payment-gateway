import io.unlaunch.UnlaunchAttribute;
import io.unlaunch.UnlaunchClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class Main {

    private static final String Sdk_Key = "prod-server-0bdc1324-cbec-4740-83e9-86045d1cd93f";
    private static final String Flag_Key = "payment-gateway";
    private static final String Set_Attribute_Name = "country";

    // Test users in Target Users. These users will use new payment gateway
    private static final String Test_User1 = "userId1";
    private static final String Test_User2 = "userId2";

    // Users from these countries will use new payment gateway
    private static final String USA = "USA";
    private static final String CAN = "CAN";

    private static final String[] Users = { Test_User1, "userId3", "userId4", "userId5", "userId6", Test_User2 };
    private static final String[] Countries = {"VNM", "COL", USA, "JPN", CAN, "FRA", "ITA"};

    private static final Random Random = new Random();

    public static void main(String[] args) {
        final UnlaunchClient client = UnlaunchClient.create(Sdk_Key);

        try {
            client.awaitUntilReady(6, TimeUnit.SECONDS);
        } catch (InterruptedException | TimeoutException e) {
            System.out.println("Client wasn't ready, " + e.getMessage());
        }

        for (int i = 0; i < 20; i++)
        {
            final String userId = Users[Random.nextInt(Users.length)];

            final String country = Countries[Random.nextInt(Countries.length)];
            final List<String> countrySet = new ArrayList<>();
            countrySet.add(country);

            final String variation = client.getVariation(Flag_Key,
                    userId,
                    UnlaunchAttribute.newSet(Set_Attribute_Name, countrySet));

            System.out.println("Variation is " + variation);
            if (variation.equals("on")) {
                System.out.println("Use new payment gateway for userId " + userId + " with countryCode " + country);
            } else {
                System.out.println("Use current payment gateway for userId " + userId + " with countryCode " + country);
            }
        }

        client.shutdown();
    }
}
