using System;
using io.unlaunch;

namespace NetCore
{
    class Program
    {
        private const string SdkKey = "prod-server-0bdc1324-cbec-4740-83e9-86045d1cd93f";
        private const string FlagKey = "payment-gateway";
        private const string SetAttributeName = "country"; 

        // Test users in Target Users. These users will use new payment gateway
        private const string TestUser1 = "userId1";
        private const string TestUser2 = "userId2";
        
        // Users from these countries will use new payment gateway
        private const string Usa = "USA";
        private const string Can = "CAN";

        private static readonly string[] Users = { TestUser1, "userId3", "userId4", "userId5", "userId6", TestUser2 };
        private static readonly string[] Countries = {"VNM", "COL", Usa, "JPN", Can, "FRA", "ITA"};
        
        private static readonly Random Random = new Random();

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

            for (var i = 0; i < 20; i++)
            {
                var userId = Users[Random.Next(Users.Length)];
                var country = Countries[Random.Next(Countries.Length)];
                var attributes = new[]
                {
                    UnlaunchAttribute.NewSet(SetAttributeName, new[] {country}) 
                };

                var variation = client.GetVariation(FlagKey, userId, attributes);
                Console.WriteLine($"Variation is {variation}");
                Console.WriteLine($"Use {(variation == "on" ? "new" : "current")} payment gateway for userId {userId} with countryCode {country}");
            }

            client.Shutdown();
        }
    }
}
