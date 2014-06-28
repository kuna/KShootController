using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace KShootConServer
{
    class PingPong
    {
        private static long ms = 0;

        public static void sendPing()
        {
            SocketServer.sendByte(100);
            ms = DateTime.Now.Ticks / TimeSpan.TicksPerMillisecond;
        }

        public static void recvPong()
        {
            if (ms > 0)
            {
                long t = DateTime.Now.Ticks / TimeSpan.TicksPerMillisecond - ms;
                System.Console.WriteLine("Ping " + t + "ms");
                ms = 0;
            }
        }

        public static void clearPing()
        {
            ms = 0;
        }

        public static Boolean isPinging()
        {
            return (ms > 0);
        }
    }
}
