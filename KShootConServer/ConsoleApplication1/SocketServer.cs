using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Net;
using System.Net.Sockets;
using System.Threading;

namespace KShootConServer
{
    class SocketServer
    {
        public static Socket Server, Client;

        public static byte[] getByte = new byte[1024];

        public const int sPort = 10070;

        public static void sendByte(byte b)
        {
            if (Client != null)
            {
                byte[] bs = new byte[10];
                bs[0] = b;
                Client.Send(bs);
            }
        }

        private static void peek()
        {
            while (Client.Connected)
            {
                PingPong.sendPing();
                Thread.Sleep(5000);
                if (PingPong.isPinging())
                {
                    Client.Disconnect(true);
                    PingPong.clearPing();
                }
            }
        }

        //[STAThread]
        public static void Start()
        {
            IPAddress serverIP = IPAddress.Parse("0.0.0.0");
            IPEndPoint serverEndPoint = new IPEndPoint(serverIP, sPort);

            try
            {
                Server = new Socket(AddressFamily.InterNetwork, SocketType.Stream, ProtocolType.Tcp);
                Server.Bind(serverEndPoint);
                Server.Listen(10);

                while (true)
                {
                    Console.WriteLine("waiting for client...");

                    Client = Server.Accept();
                    Console.WriteLine("client connected...");


                    Thread t = new Thread(new ThreadStart(peek));
                    t.Start();

                    while (Client.Connected)
                    {
                        try
                        {
                            Client.Receive(getByte, 0, getByte.Length, SocketFlags.None);

                            switch (getByte[0])
                            {
                                case 10:
                                    KeyPrs.PressKey(KeyPrs.keyS);
                                    break;
                                case 11:
                                    KeyPrs.PressKey(KeyPrs.keyD);
                                    break;
                                case 12:
                                    KeyPrs.PressKey(KeyPrs.keyK);
                                    break;
                                case 13:
                                    KeyPrs.PressKey(KeyPrs.keyL);
                                    break;
                                case 14:
                                    KeyPrs.PressKey(KeyPrs.keyLEFT);
                                    break;
                                case 15:
                                    KeyPrs.PressKey(KeyPrs.keyRIGHT);
                                    break;
                                case 16:
                                    KeyPrs.PressKey(KeyPrs.keyENTER);
                                    break;
                                case 20:
                                    KeyPrs.PressKey(KeyPrs.keyQ);
                                    break;
                                case 21:
                                    KeyPrs.PressKey(KeyPrs.keyW);
                                    break;
                                case 22:
                                    KeyPrs.PressKey(KeyPrs.keyO);
                                    break;
                                case 23:
                                    KeyPrs.PressKey(KeyPrs.keyP);
                                    break;
                                case 50:
                                    KeyPrs.ReleaseKey(KeyPrs.keyS);
                                    break;
                                case 51:
                                    KeyPrs.ReleaseKey(KeyPrs.keyD);
                                    break;
                                case 52:
                                    KeyPrs.ReleaseKey(KeyPrs.keyK);
                                    break;
                                case 53:
                                    KeyPrs.ReleaseKey(KeyPrs.keyL);
                                    break;
                                case 54:
                                    KeyPrs.ReleaseKey(KeyPrs.keyLEFT);
                                    break;
                                case 55:
                                    KeyPrs.ReleaseKey(KeyPrs.keyRIGHT);
                                    break;
                                case 56:
                                    KeyPrs.ReleaseKey(KeyPrs.keyENTER);
                                    break;
                                case 60:
                                    KeyPrs.ReleaseKey(KeyPrs.keyQ);
                                    break;
                                case 61:
                                    KeyPrs.ReleaseKey(KeyPrs.keyW);
                                    break;
                                case 62:
                                    KeyPrs.ReleaseKey(KeyPrs.keyO);
                                    break;
                                case 63:
                                    KeyPrs.ReleaseKey(KeyPrs.keyP);
                                    break;
                                case 100:
                                    PingPong.recvPong();
                                    break;
                            }
                            getByte = new byte[1024];
                        }
                        catch (Exception e)
                        {
                            // do nope, just disconnect
                            Console.WriteLine("sudden exception or disconnection.");
                        }
                    }
                    Console.WriteLine("client gone...");
                }
            }
            catch (System.Net.Sockets.SocketException socketEx)
            {
                Console.WriteLine("[Error]:{0}", socketEx.Message);
            }
            catch (System.Exception commonEx)
            {
                Console.WriteLine("[Error]:{0}", commonEx.Message);
            }
            finally
            {
                Client.Close();
                Server.Close();
            }
        }

        public static int byteArrayDefrag(byte[] sData)
        {
            int endLength = 0;

            for (int i = 0; i < sData.Length; i++)
            {
                if ((byte)sData[i] != (byte)0)
                {
                    endLength = i;
                }
            }

            return endLength;
        }
    }
}
