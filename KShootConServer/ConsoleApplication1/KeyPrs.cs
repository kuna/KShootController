using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Runtime.InteropServices; //<-- 선언꼭해줘야함
using System.IO;

namespace KShootConServer
{
    class KeyPrs
    {
        [DllImport("user32.dll")]
        public static extern void keybd_event(byte bVk, byte bScan, int dwFlags, int dwExtraInfo);

        [DllImport("kernel32")]
        static extern int GetPrivateProfileString(string Section, string Key, string Default, StringBuilder RetVal, int Size, string FilePath);

        public static byte VK_A = 0x41;
        public static byte VK_B = 0x42;
        public static byte VK_C = 0x43;
        public static byte VK_D = 0x44;
        public static byte VK_E = 0x45;
        public static byte VK_F = 0x46;
        public static byte VK_G = 0x47;
        public static byte VK_H = 0x48;
        public static byte VK_I = 0x49;
        public static byte VK_J = 0x4A;
        public static byte VK_K = 0x4B;
        public static byte VK_L = 0x4C;
        public static byte VK_M = 0x4D;
        public static byte VK_N = 0x4E;
        public static byte VK_O = 0x4F;
        public static byte VK_P = 0x50;
        public static byte VK_Q = 0x51;
        public static byte VK_R = 0x52;
        public static byte VK_S = 0x53;
        public static byte VK_T = 0x54;
        public static byte VK_U = 0x55;
        public static byte VK_V = 0x56;
        public static byte VK_W = 0x57;
        public static byte VK_X = 0x58;
        public static byte VK_Y = 0x59;
        public static byte VK_Z = 0x5A;
        public static byte VK_RETURN = 0x0D;
        public static byte VK_ESCAPE = 0x1B;

        public static byte VK_HENKAN = 0x1C;
        public static byte VK_MUHENKAN = 0x1D;   // or, 122 / 123 (0x15, 0x19)

        public static byte keyS = VK_S;
        public static byte keyD = VK_D;
        public static byte keyK = VK_K;
        public static byte keyL = VK_L;
        public static byte keyQ = VK_Q;
        public static byte keyW = VK_W;
        public static byte keyO = VK_O;
        public static byte keyP = VK_P;
        public static byte keyLEFT = VK_MUHENKAN;
        public static byte keyRIGHT = VK_HENKAN;
        public static byte keyENTER = VK_RETURN;

        public static void loadSetting()
        {
            string ininame = "setting.ini";
            string Path = new FileInfo(ininame).FullName.ToString();
            var RetVal = new StringBuilder(255);

            GetPrivateProfileString("Key", "keyS", "83", RetVal, 255, Path);
            keyS = (byte)Int32.Parse(RetVal.ToString());
            GetPrivateProfileString("Key", "keyS", "68", RetVal, 255, Path);
            keyD = (byte)Int32.Parse(RetVal.ToString());
            GetPrivateProfileString("Key", "keyS", "75", RetVal, 255, Path);
            keyK = (byte)Int32.Parse(RetVal.ToString());
            GetPrivateProfileString("Key", "keyS", "76", RetVal, 255, Path);
            keyL = (byte)Int32.Parse(RetVal.ToString());
            GetPrivateProfileString("Key", "keyS", "81", RetVal, 255, Path);
            keyQ = (byte)Int32.Parse(RetVal.ToString());
            GetPrivateProfileString("Key", "keyS", "87", RetVal, 255, Path);
            keyW = (byte)Int32.Parse(RetVal.ToString());
            GetPrivateProfileString("Key", "keyS", "79", RetVal, 255, Path);
            keyO = (byte)Int32.Parse(RetVal.ToString());
            GetPrivateProfileString("Key", "keyS", "80", RetVal, 255, Path);
            keyP = (byte)Int32.Parse(RetVal.ToString());
            GetPrivateProfileString("Key", "keyS", "29", RetVal, 255, Path);
            keyLEFT = (byte)Int32.Parse(RetVal.ToString());
            GetPrivateProfileString("Key", "keyS", "28", RetVal, 255, Path);
            keyRIGHT = (byte)Int32.Parse(RetVal.ToString());
            GetPrivateProfileString("Key", "keyS", "13", RetVal, 255, Path);
            keyENTER = (byte)Int32.Parse(RetVal.ToString());
        }

        public static void PressKey(byte code)
        {
            keybd_event(code, 0x9e, 0, 0);
        }

        public static void ReleaseKey(byte code)
        {
            keybd_event(code, 0x9e, 2, 0);
        }
    }
}
