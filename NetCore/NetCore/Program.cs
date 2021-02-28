using System;
using io.unlaunch;

namespace NetCore
{
    class Program
    {
        private const string SdkKey = "prod-server-0bdc1324-cbec-4740-83e9-86045d1cd93f";
        private const string FlagKey = "payment-gateway";
        // Name of the attribute defined in the feature flag
        private const string CountryAttributeName = "country";

        // Test users in Target Users. These users will use NEW payment gateway regardless of their
        // country
        private const string TestUser1 = "user1a@company.com";
        private const string TestUser2 = "user1b@company.com";

        // Users from these countries will use new payment gateway
        private const string Usa = "USA";
        private const string Can = "CAN";

        // Users from these countries will use NEW payment gateway
        private static readonly string[] Users =
        {
            TestUser1,
            "user2_@company.com",
            "user3_@company.com",
            "user4_@company.com",
            "user5_@company.com",
            TestUser2
        };

        private static readonly string[] Countries =
        {
            "VNM",
            "COL",
            Usa,
            "JPN",
            Can,
            "FRA",
            "ITA"
        };

        private const int NumIterations = 20;


        static void Main(string[] args)
        {
            var client = UnlaunchClient.Create(SdkKey);

            try
            {
                client.AwaitUntilReady(TimeSpan.FromSeconds(6));
            }
            catch (TimeoutException e)
            {
                Console.WriteLine($"Client wasn't ready, {e.Message}");
            }

            PrintBanner();

            var random = new Random();
            for (var i = 0; i < NumIterations; i++)
            {
                var userId = Users[random.Next(Users.Length)];
                var country = Countries[random.Next(Countries.Length)];
                var attributes = new[]
                {
                    UnlaunchAttribute.NewSet(CountryAttributeName, new[] {country})
                };

                var variation = client.GetVariation(FlagKey, userId, attributes);
                Console.WriteLine($"{userId} from country {country} use {(variation == "on" ? "new" : "old")} payment gateway");
            }

            client.Shutdown();
        }

        private static void PrintBanner()
        {
            Console.WriteLine($"Users allowed for new payment gateway: {TestUser1}, {TestUser2}");
            Console.WriteLine($"Countries allowed for new payment gateway: {Usa}, {Can}");
            Console.WriteLine("");
            Console.WriteLine($"Mocking {NumIterations} requests");
            Console.WriteLine("---");
        }
    }
}