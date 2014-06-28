using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading;

namespace KShootConServer
{
    class Program
    {
        public const int KEY_D = 0x44;

        static void Main(string[] args)
        {
            KeyPrs.loadSetting();
            Console.WriteLine("KShootController Server v1.0 by kuna");
            SocketServer.Start();
        }
    }
}
