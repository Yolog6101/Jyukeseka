package com.example.sampleapp;

import android.content.Context;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.SecureRandom;
import java.util.Random;

import static android.content.Context.MODE_PRIVATE;

public class Settings {

    // 定数
    private static final String DIRECTORY = "";
    private static final String APPNAME = "OurApp";
    private static final String EXT = ".data";
    private static final String FILENAME_SETTINGS = DIRECTORY + APPNAME + "_Settings" + EXT; // 設定

    // 変数
    private static Context appContext;
    private static String twitterID = "";
    private static String twitterPassword = "";

    private static String address = "";
    private static String mailpass = "";
    private static boolean autoTweet = false;
    private static String password = "";

    // 暗号化クラス
    public static class Cipher {

        // ハッシュ関数(SHA-3)
        public static class SHA3{
            // 定数
            static private final int HASH_BLOCK_SIZE = 1088;
            static private final int HASH_WORD_L = 6;
            static private final int HASH_WORD_LENGTH = 64;
            static private final int HASH_CAPACITY = 25 * HASH_WORD_LENGTH - HASH_BLOCK_SIZE;
            static private final int HASH_OUTPUT_LENGTH = 256;
            static private final int HASH_NUM_ROUNDS = 12 + 2 * HASH_WORD_L;

            // ハッシュ関数
            static public String hashFunc(String msg) {
                // 中断
                if(msg == null) return "";
                // ブロック数
                int numBlocks = msg.length() * 16 / HASH_BLOCK_SIZE + 1;
                // パディング
                char[] msgBuf = new char[numBlocks * HASH_BLOCK_SIZE / 16];
                for(int i = 0; i < msg.length(); i++) msgBuf[i] = msg.charAt(i);
                for(int i = msg.length(); i < msgBuf.length; i++) msgBuf[i] = 0;
                msgBuf[msg.length()] = 0x8000;
                msgBuf[msgBuf.length - 1] += 1;
                // rビット分割
                char[][] msgArray = new char[numBlocks][HASH_BLOCK_SIZE / 16];
                for(int i = 0; i < numBlocks; i++) {
                    for(int j = 0; j < HASH_BLOCK_SIZE / 16; j++) {
                        msgArray[i][j] = msgBuf[j + i * HASH_BLOCK_SIZE / 16];
                    }
                }
                // ブロックごとにブロック置換を適用
                char[] ab = new char[(HASH_CAPACITY + HASH_BLOCK_SIZE) / 16];
                for(int i = 0; i < ab.length; i++) ab[i] = 0;
                for(int i = 0; i < numBlocks; i++) {
                    for(int j = 0; j < HASH_BLOCK_SIZE / 16; j++) ab[HASH_CAPACITY / 16 + j] ^= msgArray[i][j];
                    ab = blockReplacement(ab);
                }
                // 最初の256ビットを出力
                StringBuffer output = new StringBuffer("");
                for(int i = 0; i < HASH_OUTPUT_LENGTH / 16; i++) output.append((char)ab[i]);
                return output.toString();
            }

            // ブロック置換
            static private char[] blockReplacement(char[] input) {
                // 計算しやすいようにbyteの配列を作成
                byte[][][] A = new byte[5][5][HASH_WORD_LENGTH];
                for(int y = 0; y < 5; y++) {
                    for(int x = 0; x < 5; x++) {
                        for(int z = 0; z < HASH_WORD_LENGTH; z++) {
                            A[x][y][z] = (input[(z + (5 * y + x) * HASH_WORD_LENGTH) / 16] & ((char)0x0001 << (15 - z % 16))) != 0 ? (byte)1 : (byte)0;
                        }
                    }
                }
                // ラウンド数の繰り返し
                for(int i = 0; i < HASH_NUM_ROUNDS; i++) {
                    // θ
                    byte[][][] Ad = A.clone();
                    for(int y = 0; y < 5; y++) {
                        for(int x = 0; x < 5; x++) {
                            for(int z = 0; z < HASH_WORD_LENGTH; z++) {
                                byte parity1 = 0, parity2 = 0;
                                for(int j = 0; j < 5; j++) {
                                    parity1 ^= Ad[(x + 4) % 5][j][z];
                                    parity2 ^= Ad[(x + 6) % 5][j][(z + HASH_WORD_LENGTH - 1) % HASH_WORD_LENGTH];
                                }
                                A[x][y][z] = (byte)(Ad[x][y][z] ^ parity1 ^ parity2);
                            }
                        }
                    }
                    // ρ
                    Ad = A.clone();
                    int x2 = 1, y2 = 0;
                    for(int t = 0; t < 24; t++) {
                        int z2shift = (t + 1) * (t + 2) / 2;
                        for(int z = 0; z < HASH_WORD_LENGTH; z++) {
                            int z2 = z - z2shift;
                            while(z2 < 0) z2 += HASH_WORD_LENGTH;
                            A[x2][y2][z] = Ad[x2][y2][z2];
                        }
                        int x22 = x2;
                        x2 = y2;
                        y2 = 2 * x22 + 3 * y2;
                        x2 %= 5; y2 %= 5;
                    }
                    // π
                    Ad = A.clone();
                    for(int y = 0; y < 5; y++) {
                        for(int x = 0; x < 5; x++) {
                            for(int z = 0; z < HASH_WORD_LENGTH; z++) {
                                A[x][y][z] = Ad[(x + 3 * y) % 5][x][z];
                            }
                        }
                    }
                    // χ
                    Ad = A.clone();
                    for(int y = 0; y < 5; y++) {
                        for(int x = 0; x < 5; x++) {
                            for(int z = 0; z < HASH_WORD_LENGTH; z++) {
                                A[x][y][z] = (byte)(Ad[x][y][z] ^ (((byte)1 ^ Ad[(x + 1) % 5][y][z]) & Ad[(x + 2) % 5][y][z]));
                            }
                        }
                    }
                    // ι
                    Ad = A.clone();
                    for(int m = 0; m <= HASH_WORD_L; m++) {
                        int z2 = 1;
                        for(int j = 0; j < m; j++) z2 *= 2;
                        z2 -= 1;
                        A[0][0][z2] = (byte)(Ad[0][0][z2] ^ roundConstant(m + 7 * i));
                    }
                }
                // 出力の配列
                char[] output = new char[input.length];
                for(int y = 0; y < 5; y++) {
                    for(int x = 0; x < 5; x++) {
                        for(int c = 0; c < HASH_WORD_LENGTH / 16; c++) {
                            output[c + (5 * y + x) * HASH_WORD_LENGTH / 16] = 0;
                            for(int z = 0; z < 16; z++) {
                                output[c + (5 * y + x) * HASH_WORD_LENGTH / 16] |= (A[x][y][z + c * 16] != 0 ? (char)1 : (char)0) << z;
                            }
                        }
                    }
                }
                return output;
            }
            // ラウンド定数の計算
            static private byte roundConstant(int t) {
                t %= 255;
                if(t == 0) return 1;
                byte[] R = new byte[9];
                R[0] = 1;
                for(int i = 1; i < 8; i++) R[i] = 0;
                for(int i = 1; i <= t; i++) {
                    for(int j = 8; j > 0; j--) R[j] = R[j - 1];
                    R[0] = 0;
                    R[0] = (byte)(R[0] ^ R[8]);
                    R[4] = (byte)(R[4] ^ R[8]);
                    R[5] = (byte)(R[5] ^ R[8]);
                    R[6] = (byte)(R[6] ^ R[8]);
                }
                return R[0];
            }
        }

        // 暗号(AES)
        public static class AES{

            // 定数
            static private final int KEY_LENGTH = 256;
            static private final int NUM_ROUNDS = 14;
            static private final int[] S_BOX = {
                    0x63, 0x7c, 0x77, 0x7b, 0xf2, 0x6b, 0x6f, 0xc5, 0x30, 0x01, 0x67, 0x2b, 0xfe, 0xd7, 0xab, 0x76,
                    0xca, 0x82, 0xc9, 0x7d, 0xfa, 0x59, 0x47, 0xf0, 0xad, 0xd4, 0xa2, 0xaf, 0x9c, 0xa4, 0x72, 0xc0,
                    0xb7, 0xfd, 0x93, 0x26, 0x36, 0x3f, 0xf7, 0xcc, 0x34, 0xa5, 0xe5, 0xf1, 0x71, 0xd8, 0x31, 0x15,
                    0x04, 0xc7, 0x23, 0xc3, 0x18, 0x96, 0x05, 0x9a, 0x07, 0x12, 0x80, 0xe2, 0xeb, 0x27, 0xb2, 0x75,
                    0x09, 0x83, 0x2c, 0x1a, 0x1b, 0x6e, 0x5a, 0xa0, 0x52, 0x3b, 0xd6, 0xb3, 0x29, 0xe3, 0x2f, 0x84,
                    0x53, 0xd1, 0x00, 0xed, 0x20, 0xfc, 0xb1, 0x5b, 0x6a, 0xcb, 0xbe, 0x39, 0x4a, 0x4c, 0x58, 0xcf,
                    0xd0, 0xef, 0xaa, 0xfb, 0x43, 0x4d, 0x33, 0x85, 0x45, 0xf9, 0x02, 0x7f, 0x50, 0x3c, 0x9f, 0xa8,
                    0x51, 0xa3, 0x40, 0x8f, 0x92, 0x9d, 0x38, 0xf5, 0xbc, 0xb6, 0xda, 0x21, 0x10, 0xff, 0xf3, 0xd2,
                    0xcd, 0x0c, 0x13, 0xec, 0x5f, 0x97, 0x44, 0x17, 0xc4, 0xa7, 0x7e, 0x3d, 0x64, 0x5d, 0x19, 0x73,
                    0x60, 0x81, 0x4f, 0xdc, 0x22, 0x2a, 0x90, 0x88, 0x46, 0xee, 0xb8, 0x14, 0xde, 0x5e, 0x0b, 0xdb,
                    0xe0, 0x32, 0x3a, 0x0a, 0x49, 0x06, 0x24, 0x5c, 0xc2, 0xd3, 0xac, 0x62, 0x91, 0x95, 0xe4, 0x79,
                    0xe7, 0xc8, 0x37, 0x6d, 0x8d, 0xd5, 0x4e, 0xa9, 0x6c, 0x56, 0xf4, 0xea, 0x65, 0x7a, 0xae, 0x08,
                    0xba, 0x78, 0x25, 0x2e, 0x1c, 0xa6, 0xb4, 0xc6, 0xe8, 0xdd, 0x74, 0x1f, 0x4b, 0xbd, 0x8b, 0x8a,
                    0x70, 0x3e, 0xb5, 0x66, 0x48, 0x03, 0xf6, 0x0e, 0x61, 0x35, 0x57, 0xb9, 0x86, 0xc1, 0x1d, 0x9e,
                    0xe1, 0xf8, 0x98, 0x11, 0x69, 0xd9, 0x8e, 0x94, 0x9b, 0x1e, 0x87, 0xe9, 0xce, 0x55, 0x28, 0xdf,
                    0x8c, 0xa1, 0x89, 0x0d, 0xbf, 0xe6, 0x42, 0x68, 0x41, 0x99, 0x2d, 0x0f, 0xb0, 0x54, 0xbb, 0x16,
            };
            static private final int[] R_CON = {
                    0x01, 0x02, 0x04, 0x08, 0x10, 0x20, 0x40
            };

            // 暗号化
            static public String encode(String input, String key) {
                // 中断
                if(key.length() != KEY_LENGTH / 16) return input;
                // サブ鍵を生成
                byte[][] subKeys = generateSubKeys(key.toCharArray());
                // ブロック数
                int numBlocks = (input.length() + 7) / 8;
                if(numBlocks < 1) numBlocks = 1;
                // メッセージ分割・パディング
                byte[][] msgArray = new byte[numBlocks][16];
                for(int i = 0; i < numBlocks; i++) {
                    for(int j = 0; j < 8; j++) {
                        int index = j + i * 8;
                        if(index < input.length()) {
                            char c = input.charAt(index);
                            msgArray[i][j * 2 + 0] = (byte)(c >> 8);
                            msgArray[i][j * 2 + 1] = (byte)c;
                        }
                        else {
                            msgArray[i][j * 2 + 0] = 0;
                            msgArray[i][j * 2 + 1] = 0;
                        }
                    }
                }
                // 暗号文の配列
                byte[][] cipher = new byte[numBlocks + 1][16];
                // 初期値をランダムに設定
                SecureRandom rand = new SecureRandom();
                for(int i = 0; i < 16; i++) rand.nextBytes(cipher[0]);
                // ブロック暗号化(CBCモード)
                for(int i = 0; i < numBlocks; i++) {
                    // 前回の出力とメッセージの排他的論理和をとる
                    for(int j = 0; j < 16; j++) msgArray[i][j] ^= cipher[i][j];
                    // 暗号化
                    cipher[i + 1] = encodeBlock(msgArray[i], subKeys);
                }
                // 暗号文をStringに
                StringBuffer output = new StringBuffer("");
                for(int i = 0; i < numBlocks + 1; i++) {
                    for(int j = 0; j < 8; j++) {
                        char c = (char)(((char)cipher[i][j * 2 + 0] << 8) | ((char)cipher[i][j * 2 + 1] & 0x00ff));
                        output.append(c);
                    }
                }
                return output.toString();
            }
            // 復号
            static public String decode(String input, String key) {
                // 中断
                if(key.length() != KEY_LENGTH / 16) return input;
                // サブ鍵を生成
                byte[][] subKeys = generateSubKeys(key.toCharArray());
                // ブロック数
                int numBlocks = (input.length() + 7) / 8;
                if(numBlocks < 1) numBlocks = 1;
                // メッセージ分割・パディング
                byte[][] cipherArray = new byte[numBlocks][16];
                for(int i = 0; i < numBlocks; i++) {
                    for(int j = 0; j < 8; j++) {
                        int index = j + i * 8;
                        if(index < input.length()) {
                            cipherArray[i][j * 2 + 0] = (byte)(input.charAt(index) >> 8);
                            cipherArray[i][j * 2 + 1] = (byte)input.charAt(index);
                        }
                        else {
                            cipherArray[i][j * 2 + 0] = 0;
                            cipherArray[i][j * 2 + 1] = 0;
                        }
                    }
                }
                // 平文の配列
                byte[][] msg = new byte[numBlocks - 1][16];
                // ブロック復号(CBCモード)
                for(int i = 0; i < numBlocks - 1; i++) {
                    // 復号
                    byte[] decMsg = decodeBlock(cipherArray[i + 1], subKeys);
                    // 前回の入力と復号メッセージの排他的論理和をとる
                    for(int j = 0; j < 16; j++) msg[i][j] = (byte)(cipherArray[i][j] ^ decMsg[j]);
                }
                // 平文をStringに
                StringBuffer output = new StringBuffer("");
                for(int i = 0; i < numBlocks - 1; i++) {
                    for(int j = 0; j < 8; j++) {
                        char append = (char)(((char)msg[i][j * 2 + 0] << 8) | ((char)msg[i][j * 2 + 1] & 0x00ff));
                        if(append == 0) break;
                        output.append(append);
                    }
                }
                return output.toString();
            }

            // ブロック暗号化
            static private byte[] encodeBlock(byte[] msg, byte[][] subKeys) {
                // 出力
                byte[] out = msg.clone();
                // AddRoundKey
                out = addRoundKey(out, subKeys[0]);
                // ラウンド回数の繰り返し
                for(int i = 0; i < NUM_ROUNDS; i++) {
                    // SubBytes処理
                    for(int j = 0; j < 16; j++) out[j] = subBytes(out[j]);
                    // ShiftRows処理
                    byte[] out2 = out.clone();
                    for(int j = 0; j < 4; j++) for(int k = 0; k < 4; k++)
                        out[k + j * 4] = out2[(k + j) % 4 + j * 4];
                    // MixColumn処理
                    if(i < NUM_ROUNDS - 1) {
                        out2 = out.clone();
                        for(int j = 0; j < 4; j++) {
                            out[0  + j] = (byte)(polynomialDot(out2[0 + j], (byte)0x02) ^ polynomialDot(out2[4 + j], (byte)0x03) ^ polynomialDot(out2[8 + j], (byte)0x01) ^ polynomialDot(out2[12 + j], (byte)0x01));
                            out[4  + j] = (byte)(polynomialDot(out2[0 + j], (byte)0x01) ^ polynomialDot(out2[4 + j], (byte)0x02) ^ polynomialDot(out2[8 + j], (byte)0x03) ^ polynomialDot(out2[12 + j], (byte)0x01));
                            out[8  + j] = (byte)(polynomialDot(out2[0 + j], (byte)0x01) ^ polynomialDot(out2[4 + j], (byte)0x01) ^ polynomialDot(out2[8 + j], (byte)0x02) ^ polynomialDot(out2[12 + j], (byte)0x03));
                            out[12 + j] = (byte)(polynomialDot(out2[0 + j], (byte)0x03) ^ polynomialDot(out2[4 + j], (byte)0x01) ^ polynomialDot(out2[8 + j], (byte)0x01) ^ polynomialDot(out2[12 + j], (byte)0x02));
                        }
                    }
                    // AddRoundKey
                    out = addRoundKey(out, subKeys[i + 1]);
                }
                return out;
            }
            // ブロック復号
            static private byte[] decodeBlock(byte[] msg, byte[][] subKeys) {
                // 出力
                byte[] out = msg.clone();
                // ラウンド回数の繰り返し
                for(int i = NUM_ROUNDS - 1; i >= 0; i--) {
                    // InvAddRoundKey
                    out = addRoundKey(out, subKeys[i + 1]);
                    // InvMixColumn処理
                    byte[] out2 = out.clone();
                    if(i < NUM_ROUNDS - 1) {
                        out2 = out.clone();
                        for(int j = 0; j < 4; j++) {
                            out[0  + j] = (byte)(polynomialDot(out2[0 + j], (byte)0x0e) ^ polynomialDot(out2[4 + j], (byte)0x0b) ^ polynomialDot(out2[8 + j], (byte)0x0d) ^ polynomialDot(out2[12 + j], (byte)0x09));
                            out[4  + j] = (byte)(polynomialDot(out2[0 + j], (byte)0x09) ^ polynomialDot(out2[4 + j], (byte)0x0e) ^ polynomialDot(out2[8 + j], (byte)0x0b) ^ polynomialDot(out2[12 + j], (byte)0x0d));
                            out[8  + j] = (byte)(polynomialDot(out2[0 + j], (byte)0x0d) ^ polynomialDot(out2[4 + j], (byte)0x09) ^ polynomialDot(out2[8 + j], (byte)0x0e) ^ polynomialDot(out2[12 + j], (byte)0x0b));
                            out[12 + j] = (byte)(polynomialDot(out2[0 + j], (byte)0x0b) ^ polynomialDot(out2[4 + j], (byte)0x0d) ^ polynomialDot(out2[8 + j], (byte)0x09) ^ polynomialDot(out2[12 + j], (byte)0x0e));
                        }
                    }
                    // InvShiftRows処理
                    out2 = out.clone();
                    for(int j = 0; j < 4; j++) for(int k = 0; k < 4; k++)
                        out[k + j * 4] = out2[(k - j + 4) % 4 + j * 4];
                    // SubBytes処理
                    for(int j = 0; j < 16; j++) out[j] = invSubBytes(out[j]);
                }
                // InvAddRoundKey
                out = addRoundKey(out, subKeys[0]);
                return out;
            }
            // サブ鍵の生成
            static private byte[][] generateSubKeys(char[] sk) {
                // 鍵の配列
                byte[][] subKeys = new byte[NUM_ROUNDS + 1][16];
                // ワード分割
                byte[][] words = new byte[8][4];
                for(int i = 0; i < 8; i++) {
                    for(int j = 0; j < 4; j++) words[i][j] = (byte)(sk[i * 2 + j / 2] >> (8 - (j % 2) * 8));
                }
                // ラウンドを繰り返す
                for(int i = 0; i < NUM_ROUNDS / 2; i++) {
                    // 鍵をコピー
                    for(int j = 0; j < 16; j++) subKeys[i * 2 + 0][j] = words[j / 4][j % 4];
                    for(int j = 0; j < 16; j++) subKeys[i * 2 + 1][j] = words[4 + j / 4][j % 4];
                    // ワードを複製
                    byte[][] words2 = words.clone();
                    // RotWord処理
                    byte[] temp = words[7].clone();
                    byte[] temp2 = temp.clone();
                    for(int j = 0; j < 4; j++) temp[j] = temp2[(j + 1) % 4];
                    // SubWord処理
                    for(int j = 0; j < 4; j++) temp[j] = subBytes(temp[j]);
                    // Rcon処理
                    temp[0] ^= (byte)R_CON[i];
                    // 前半4ワードを計算
                    for(int j = 0; j < 4; j++) {
                        for(int k = 0; k < 4; k++) words[j][k] = (byte)(temp[k] ^ words2[j][k]);
                        for(int k = 0; k < 4; k++) temp[k] = words[j][k];
                    }
                    // 後半4ワードを計算
                    for(int j = 0; j < 4; j++) temp[j] = subBytes(temp[j]);
                    for(int j = 4; j < 8; j++) {
                        for(int k = 0; k < 4; k++) words[j][k] = (byte)(temp[k] ^ words2[j][k]);
                        for(int k = 0; k < 4; k++) temp[k] = words[j][k];
                    }
                }
                // 鍵をコピー
                for(int j = 0; j < 16; j++) subKeys[NUM_ROUNDS][j] = words[j / 4][j % 4];
                // 出力
                return subKeys;
            }
            // AddRoundKey
            static private byte[] addRoundKey(byte[] s, byte[] k) {
                byte[] out = s.clone();
                for(int i = 0; i < out.length; i++) out[i] ^= k[i];
                return out;
            }
            // SubBytes変換
            static private byte subBytes(byte input) {
                return (byte)S_BOX[((int)input + 256) % 256];
            }
            // SubBytes逆変換
            static private byte invSubBytes(byte input) {
                for(int out = 0; out < 256; out++) {
                    if(input == (byte)S_BOX[out]) return (byte)out;
                }
                return 0;
            }
            // 多項式の積(mod x^8+x^4+x^3+x+1)
            static private byte polynomialDot(byte p1, byte p2) {
                char temp = 0;
                for(int i = 0; i < 8; i++) {
                    if((p2 & (0x01 << i)) != 0) temp ^= (((char)p1 & 0x00ff) << i);
                }
                if((temp & 0x0100) != 0) temp ^= 0x001b;
                if((temp & 0x0200) != 0) temp ^= 0x0036;
                if((temp & 0x0400) != 0) temp ^= 0x006c;
                if((temp & 0x0800) != 0) temp ^= 0x00d8;
                if((temp & 0x1000) != 0) temp ^= 0x00ab;
                if((temp & 0x2000) != 0) temp ^= 0x004d;
                if((temp & 0x4000) != 0) temp ^= 0x009a;
                if((temp & 0x8000) != 0) temp ^= 0x002f;
                return (byte)temp;
            }
        }
    }

    // アプリの軌道を通知
    static public void notifyRun(Context appContextIn){
        // アプリコンテキストを保存
        appContext = appContextIn;
    }
    // パスワード設定済みフラグを返す
    static public boolean isPasswordSet(){
        // ロード
        loadSettings();
        // 判定
        return password.length() > 0;
    }
    // パスワードを設定
    static public void setPassword(String pass){
        // ロード
        loadSettings();
        // メールパスワード記憶
        String twpass = getTwitterPassword();
        // 鍵
        String key = generateKey(pass);
        // 暗号化
        password = Cipher.AES.encode(pass, key);
        // セーブ
        saveSettings();
        // パスワードを記憶
        password = Cipher.SHA3.hashFunc(pass);
        // メールパスワード再設定
        setTwitterPassword(twpass);
    }
    // パスワードが正しいか判定
    static public boolean checkPassword(String pass){
        // ロード
        loadSettings();
        // 鍵
        String key = generateKey(pass);
        // 復号
        String dec = Cipher.AES.decode(password, key);
        // 一致判定
        boolean ret = dec.equals(pass);
        if(ret) password = Cipher.SHA3.hashFunc(pass);
        return ret;
    }
    // ツイッターIDを設定
    static public void setTwitterID(String id){
        // ロード
        loadSettings();
        // 設定
        twitterID = id;
        // セーブ
        saveSettings();
    }
    // ツイッターIDを取得
    static public String getTwitterID(){
        // ロード
        loadSettings();
        // 戻り値
        return twitterID;
    }


    // ツイッターパスワードを設定
    static public void setTwitterPassword(String pass){
        // ロード
        loadSettings();
        // 設定
        twitterPassword = encodePassword(pass);
        // セーブ
        saveSettings();
    }
    // ツイッターパスワードを取得
    static public String getTwitterPassword(){
        // ロード
        loadSettings();
        // 戻り値
        return decodePassword(twitterPassword);
    }
    // 自動ツイート設定
    static public void enableAutoTweet(boolean enable){
        // ロード
        loadSettings();
        // 設定
        autoTweet = enable;
        // セーブ
        saveSettings();
    }
    // 自動ツイート設定を取得
    static public boolean isAutoTweetEnabled(){
        // ロード
        loadSettings();
        // 戻り値
        return autoTweet;
    }

    // ロード
    static private void loadSettings(){
        try {
            // ファイルオープン
            ObjectInputStream ois = new ObjectInputStream(appContext.openFileInput(FILENAME_SETTINGS));
            // ロード
            twitterID = ois.readUTF();
            twitterPassword = ois.readUTF();
            autoTweet = ois.readBoolean();
            password = ois.readUTF();
            // ファイルクローズ
            ois.close();
        } catch (Exception e) {
            // 初期化
            twitterID = "";
            twitterPassword = "";
            autoTweet = false;
            password = "";
        }
    }
    // セーブ
    static private void saveSettings(){
        try {
            // ファイルオープン
            ObjectOutputStream oos = new ObjectOutputStream(appContext.openFileOutput(FILENAME_SETTINGS, MODE_PRIVATE));
            // セーブ
            oos.writeUTF(twitterID);
            oos.writeUTF(twitterPassword);
            oos.writeBoolean(autoTweet);
            oos.writeUTF(password);
            // ファイルクローズ
            oos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // デバイスIDを取得
    static private String getDeviceID(){
        return appContext != null ? android.provider.Settings.System.getString(appContext.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID) : "";
    }
    // パスワードの暗号化
    static private String encodePassword(String input){
        // AESにより暗号化
        return Cipher.AES.encode(input, generateKey(password));
    }
    // パスワードの復号
    static private String decodePassword(String input){
        return Cipher.AES.decode(input, generateKey(password));
    }
    // 秘密鍵を生成
    static public String generateKey(String pass){
        String str1 = "入力が正しくありません。整数を入力してください"; // ダミー
        String str2 = "ツイートに失敗しました。ID・パスワードを確認してください。";
        int strlen = Math.min(str1.length(), str2.length());
        StringBuffer str3 = new StringBuffer(pass);
        for(int i = 0; i < strlen; i++) str3.append((char)(str1.charAt(i) ^ str2.charAt(i)));
        str3.append(getDeviceID());
        return Cipher.SHA3.hashFunc(str3.toString());
    }
}
