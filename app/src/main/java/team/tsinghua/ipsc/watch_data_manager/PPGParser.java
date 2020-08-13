package team.tsinghua.ipsc.watch_data_manager;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class PPGParser {

    private static double[] a;
    private static double[] b;
    private static double[] c;
    private static double[] d;
    private static double[] e;
    private static double[] f;
    private static double[] g;
    private static double[] h;
    private static double[] i;
    private static double[] j;
    private static double[] k;
    private static double[] l;
    private static Integer[] m;
    private static Integer[] n;
    private static Integer[] o;
    private static int[] p;
    private static int[] q;
    private static String r = "5A0391000A0906870B";

    public PPGParser() {
    }

    private static int[] a(String var0, String var1) {
        ArrayList var2 = new ArrayList();
        int var3 = var0.indexOf(var1);
        var2.add(var3 / 2);

        while(var3 != -1) {
            var3 = var0.indexOf(var1, var3 + 1);
            var2.add(var3 / 2);
        }

        int[] var4 = new int[var2.size()];
        int var5 = 0;

        Integer var7;
        for(Iterator var6 = var2.iterator(); var6.hasNext(); var4[var5++] = var7) {
            var7 = (Integer)var6.next();
        }

        return var4;
    }

    private static int[] a(String var0) {
        int[] var1 = new int[var0.length() / 2];

        for(int var3 = 0; var3 < var0.length() / 2; ++var3) {
            String var2 = var0.substring(2 * var3, 2 * (var3 + 1));
            var1[var3] = Integer.parseInt(var2.toUpperCase(), 16);
        }

        return var1;
    }

    private static int[] a(int var0, int var1) {
        int[] var2 = new int[var1];

        for(int var3 = 0; var3 < var1; ++var3) {
            var2[var3] = var0 + var3;
        }

        return var2;
    }

    private static void a(String var0, int var1) {
        int[] var26 = a(16, 6);
        int[] var2 = a(46, 90);
        int[] var3 = a(136, 480);
        String var5 = r;
        String var4 = var0;
        ArrayList var6 = new ArrayList();
        int var7 = var0.indexOf(var5);
        var6.add(var7 / 2);

        while(var7 != -1) {
            var7 = var4.indexOf(var5, var7 + 1);
            var6.add(var7 / 2);
        }

        int[] var30 = new int[var6.size()];
        int var32 = 0;

        Integer var35;
        for(Iterator var33 = var6.iterator(); var33.hasNext(); var30[var32++] = var35) {
            var35 = (Integer)var33.next();
        }

        var30 = var30;
        int[] var24 = a(var0);
        var32 = var30.length;
        int var34 = var26.length;
        int[][] var37 = new int[var32][var34];
        int[][] var8 = new int[var32][var34 / 2];

        int var10;
        for(int var9 = 0; var9 < var32; ++var9) {
            for(var10 = 0; var10 < var34; ++var10) {
                var37[var9][var10] = var24[var26[var10] + var30[var9] - 1];
            }

            for(var10 = 0; var10 < var34 - 1; var10 += 2) {
                var8[var9][var10 / 2] = var37[var9][var10] + var37[var9][var10 + 1];
            }

            var8[var9][0] /= 6;
            var8[var9][1] = (var37[var9][2] + (var37[var9][3] << 8)) / 48;
            var8[var9][2] /= 6;
            if (var8[var9][0] > 15) {
                var8[var9][0] = 15;
            }

            if (var8[var9][2] > 15) {
                var8[var9][2] = 15;
            }
        }

        ArrayList var39 = new ArrayList();

        for(var10 = 0; var10 < var32 / 2; ++var10) {
            if (var8[var10][1] == 0) {
                var39.add(var10);
            }
        }

        var34 = var2.length;
        int[][] var40 = new int[var32][var34];
        ArrayList var27 = new ArrayList();
        ArrayList var38 = new ArrayList();
        ArrayList var11 = new ArrayList();
        int[] var12 = new int[var34 / 2];

        int var13;
        for(var13 = 0; var13 < var34 / 2; ++var13) {
            var12[var13] = var13 + 1;
        }

        int var15;
        for(var13 = 0; var13 < var32; ++var13) {
            if (var39.indexOf(var13) < 0) {
                for(int var14 = 0; var14 < var34; ++var14) {
                    var40[var13][var14] = var24[var2[var14] + var30[var13] - 1];
                    if (var14 % 2 == 1) {
                        if ((var15 = var40[var13][var14 - 1] + (var40[var13][var14] << 8)) > 32768) {
                            var15 = -(65536 - var15);
                        }

                        boolean var16 = var12[var14 / 2] < var8[var13][0] * 3;
                        if (var14 / 2 % 3 == 0 && var16) {
                            var27.add(var15);
                        }

                        if (var14 / 2 % 3 == 1 && var16) {
                            var38.add(var15);
                        }

                        if (var14 / 2 % 3 == 2 && var16) {
                            var11.add(var15);
                        }
                    }
                }
            }
        }

        var34 = var3.length;
        int[][] var43 = new int[var32][var34];
        double[][] var44 = new double[var32][var34 / 4];

        int var46;
        for(var15 = 0; var15 < var32; ++var15) {
            for(var46 = 0; var46 < var34; ++var46) {
                var43[var15][var46] = var24[var3[var46] + var30[var15] - 1];
            }
        }

        for(var15 = 0; var15 < var32; ++var15) {
            for(var46 = 0; var46 < var34 - 1; ++var46) {
                if (var46 % 4 == 0) {
                    var44[var15][var46 / 4] = (double)var43[var15][var46] + (double)var43[var15][var46 + 1] * Math.pow(2.0D, 8.0D) + (double)var43[var15][var46 + 2] * Math.pow(2.0D, 16.0D) + (double)var43[var15][var46 + 3] * Math.pow(2.0D, 24.0D);
                    if (var44[var15][var46 / 4] > 2.147483647E9D) {
                        var44[var15][var46 / 4] -= 4.294967296E9D;
                    }
                }
            }
        }

        double[] var47 = new double[(var15 = var44[0].length / 12) * var32];
        double[] var25 = new double[var15 * var32];
        double[] var28 = new double[var15 * var32];
        double[] var29 = new double[var15 * var32];
        double[] var31 = new double[var15 * var32];
        double[] var36 = new double[var15 * var32];
        double[] var41 = new double[var15 * var32];
        double[] var42 = new double[var15 * var32];
        double[] var45 = new double[var15 * var32];
        double[] var17 = new double[var15 * var32];
        double[] var18 = new double[var15 * var32];
        double[] var19 = new double[var15 * var32];
        int[] var20 = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        int var21 = 0;

        int var22;
        for(var22 = 3; var22 < 20; ++var22) {
            System.out.printf("%d ", var8[var22][1]);
        }

        for(var22 = 0; var22 < var32; ++var22) {
            if (var39.indexOf(var22) < 0) {
                for(int var23 = 0; var23 < var15; ++var23) {
                    if (var20[var23] <= var8[var22][1]) {
                        var47[var21] = var44[var22][var23 * 12];
                        var25[var21] = var44[var22][var23 * 12 + 1];
                        var28[var21] = var44[var22][var23 * 12 + 2];
                        var29[var21] = var44[var22][var23 * 12 + 3];
                        var31[var21] = var44[var22][var23 * 12 + 4];
                        var36[var21] = var44[var22][var23 * 12 + 5];
                        var41[var21] = var44[var22][var23 * 12 + 6];
                        var42[var21] = var44[var22][var23 * 12 + 7];
                        var45[var21] = var44[var22][var23 * 12 + 8];
                        var17[var21] = var44[var22][var23 * 12 + 9];
                        var18[var21] = var44[var22][var23 * 12 + 10];
                        var19[var21] = var44[var22][var23 * 12 + 11];
                        ++var21;
                    }
                }
            }
        }

        p = new int[var32];
        q = new int[var32];

        for(var22 = 0; var22 < var32; ++var22) {
            q[var22] = var8[var22][0];
            p[var22] = var8[var22][1];
        }

        m = new Integer[var27.size()];
        var27.toArray(m);
        n = new Integer[var38.size()];
        var38.toArray(n);
        o = new Integer[var11.size()];
        var11.toArray(o);
        a = Arrays.copyOfRange(var47, 0, var21);
        b = Arrays.copyOfRange(var25, 0, var21);
        c = Arrays.copyOfRange(var28, 0, var21);
        d = Arrays.copyOfRange(var29, 0, var21);
        Arrays.copyOfRange(var31, 0, var21);
        Arrays.copyOfRange(var36, 0, var21);
        Arrays.copyOfRange(var41, 0, var21);
        Arrays.copyOfRange(var42, 0, var21);
        Arrays.copyOfRange(var45, 0, var21);
        Arrays.copyOfRange(var17, 0, var21);
        Arrays.copyOfRange(var18, 0, var21);
        Arrays.copyOfRange(var19, 0, var21);
    }

    private static List b(String var0) {
        ArrayList var1 = new ArrayList();

        try {
            String var2 = "GBK";
            File var5;
            if ((var5 = new File(var0)).isFile() && var5.exists()) {
                InputStreamReader var6 = new InputStreamReader(new FileInputStream(var5), var2);
                BufferedReader var7 = new BufferedReader(var6);

                String var3;
                while((var3 = var7.readLine()) != null) {
                    var1.add(var3);
                }

                var7.close();
                var6.close();
            } else {
                System.out.println("can not find file");
            }
        } catch (Exception var4) {
            System.out.println("error to reading");
        }

        return var1;
    }

    private static double[] a() {
        return a;
    }

    private static double[] b() {
        return b;
    }

    private static double[] c() {
        return c;
    }

    private static double[] d() {
        return d;
    }

    private static Integer[] e() {
        return m;
    }

    private static Integer[] f() {
        return n;
    }

    private static Integer[] g() {
        return o;
    }

    public static void decode(String[] var0, String tar_dir, String[] user, String[] fnames) {

        String[] var1 = var0;
        int var2 = var0.length;

        int var3;
        for(var3 = 0; var3 < var2; ++var3) {
            String var4 = var1[var3];
            System.out.println(var4);
        }

        List var29 = b(var0[0]);

        double[] var30;
        double[] var39;
        for(var2 = 0; var2 < var29.size(); ++var2) {
            System.out.println(((String)var29.get(var2)).substring(0, 10));
            String var10000 = (String)var29.get(var2);
            boolean var26 = true;
            String var27 = var10000;
            int[] var36 = a(16, 6);
            int[] var38 = a(46, 90);
            int[] var5 = a(136, 480);
            String var7 = r;
            String var6 = var27;
            ArrayList var8 = new ArrayList();
            int var9 = var27.indexOf(var7);
            var8.add(var9 / 2);

            while(var9 != -1) {
                var9 = var6.indexOf(var7, var9 + 1);
                var8.add(var9 / 2);
            }

            int[] var43 = new int[var8.size()];
            int var45 = 0;

            Integer var48;
            for(Iterator var46 = var8.iterator(); var46.hasNext(); var43[var45++] = var48) {
                var48 = (Integer)var46.next();
            }

            var43 = var43;
            int[] var28 = a(var27);
            var45 = var43.length;
            int var47 = var36.length;
            int[][] var50 = new int[var45][var47];
            int[][] var10 = new int[var45][var47 / 2];

            int var12;
            for(int var11 = 0; var11 < var45; ++var11) {
                for(var12 = 0; var12 < var47; ++var12) {
                    var50[var11][var12] = var28[var36[var12] + var43[var11] - 1];
                }

                for(var12 = 0; var12 < var47 - 1; var12 += 2) {
                    var10[var11][var12 / 2] = var50[var11][var12] + var50[var11][var12 + 1];
                }

                var10[var11][0] /= 6;
                var10[var11][1] = (var50[var11][2] + (var50[var11][3] << 8)) / 48;
                var10[var11][2] /= 6;
                if (var10[var11][0] > 15) {
                    var10[var11][0] = 15;
                }

                if (var10[var11][2] > 15) {
                    var10[var11][2] = 15;
                }
            }

            ArrayList var52 = new ArrayList();

            for(var12 = 0; var12 < var45 / 2; ++var12) {
                if (var10[var12][1] == 0) {
                    var52.add(var12);
                }
            }

            var47 = var38.length;
            int[][] var53 = new int[var45][var47];
            ArrayList var37 = new ArrayList();
            ArrayList var51 = new ArrayList();
            ArrayList var13 = new ArrayList();
            int[] var14 = new int[var47 / 2];

            int var15;
            for(var15 = 0; var15 < var47 / 2; ++var15) {
                var14[var15] = var15 + 1;
            }

            int var17;
            for(var15 = 0; var15 < var45; ++var15) {
                if (var52.indexOf(var15) < 0) {
                    for(int var16 = 0; var16 < var47; ++var16) {
                        var53[var15][var16] = var28[var38[var16] + var43[var15] - 1];
                        if (var16 % 2 == 1) {
                            if ((var17 = var53[var15][var16 - 1] + (var53[var15][var16] << 8)) > 32768) {
                                var17 = -(65536 - var17);
                            }

                            boolean var18 = var14[var16 / 2] < var10[var15][0] * 3;
                            if (var16 / 2 % 3 == 0 && var18) {
                                var37.add(var17);
                            }

                            if (var16 / 2 % 3 == 1 && var18) {
                                var51.add(var17);
                            }

                            if (var16 / 2 % 3 == 2 && var18) {
                                var13.add(var17);
                            }
                        }
                    }
                }
            }

            var47 = var5.length;
            int[][] var56 = new int[var45][var47];
            double[][] var57 = new double[var45][var47 / 4];

            int var59;
            for(var17 = 0; var17 < var45; ++var17) {
                for(var59 = 0; var59 < var47; ++var59) {
                    var56[var17][var59] = var28[var5[var59] + var43[var17] - 1];
                }
            }

            for(var17 = 0; var17 < var45; ++var17) {
                for(var59 = 0; var59 < var47 - 1; ++var59) {
                    if (var59 % 4 == 0) {
                        var57[var17][var59 / 4] = (double)var56[var17][var59] + (double)var56[var17][var59 + 1] * Math.pow(2.0D, 8.0D) + (double)var56[var17][var59 + 2] * Math.pow(2.0D, 16.0D) + (double)var56[var17][var59 + 3] * Math.pow(2.0D, 24.0D);
                        if (var57[var17][var59 / 4] > 2.147483647E9D) {
                            var57[var17][var59 / 4] -= 4.294967296E9D;
                        }
                    }
                }
            }

            double[] var60 = new double[(var17 = var57[0].length / 12) * var45];
            var30 = new double[var17 * var45];
            var39 = new double[var17 * var45];
            double[] var42 = new double[var17 * var45];
            double[] var44 = new double[var17 * var45];
            double[] var49 = new double[var17 * var45];
            double[] var54 = new double[var17 * var45];
            double[] var55 = new double[var17 * var45];
            double[] var58 = new double[var17 * var45];
            double[] var19 = new double[var17 * var45];
            double[] var20 = new double[var17 * var45];
            double[] var21 = new double[var17 * var45];
            int[] var22 = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
            int var23 = 0;

            int var24;
            for(var24 = 3; var24 < 20; ++var24) {
                System.out.printf("%d ", var10[var24][1]);
            }

            for(var24 = 0; var24 < var45; ++var24) {
                if (var52.indexOf(var24) < 0) {
                    for(int var25 = 0; var25 < var17; ++var25) {
                        if (var22[var25] <= var10[var24][1]) {
                            var60[var23] = var57[var24][var25 * 12];
                            var30[var23] = var57[var24][var25 * 12 + 1];
                            var39[var23] = var57[var24][var25 * 12 + 2];
                            var42[var23] = var57[var24][var25 * 12 + 3];
                            var44[var23] = var57[var24][var25 * 12 + 4];
                            var49[var23] = var57[var24][var25 * 12 + 5];
                            var54[var23] = var57[var24][var25 * 12 + 6];
                            var55[var23] = var57[var24][var25 * 12 + 7];
                            var58[var23] = var57[var24][var25 * 12 + 8];
                            var19[var23] = var57[var24][var25 * 12 + 9];
                            var20[var23] = var57[var24][var25 * 12 + 10];
                            var21[var23] = var57[var24][var25 * 12 + 11];
                            ++var23;
                        }
                    }
                }
            }

            p = new int[var45];
            q = new int[var45];

            for(var24 = 0; var24 < var45; ++var24) {
                q[var24] = var10[var24][0];
                p[var24] = var10[var24][1];
            }

            m = new Integer[var37.size()];
            var37.toArray(m);
            n = new Integer[var51.size()];
            var51.toArray(n);
            o = new Integer[var13.size()];
            var13.toArray(o);
            a = Arrays.copyOfRange(var60, 0, var23);
            b = Arrays.copyOfRange(var30, 0, var23);
            c = Arrays.copyOfRange(var39, 0, var23);
            d = Arrays.copyOfRange(var42, 0, var23);
            Arrays.copyOfRange(var44, 0, var23);
            Arrays.copyOfRange(var49, 0, var23);
            Arrays.copyOfRange(var54, 0, var23);
            Arrays.copyOfRange(var55, 0, var23);
            Arrays.copyOfRange(var58, 0, var23);
            Arrays.copyOfRange(var19, 0, var23);
            Arrays.copyOfRange(var20, 0, var23);
            Arrays.copyOfRange(var21, 0, var23);
        }


        double[] PPG1 = a();
        double[] PPG2 = b();
        double[] PPG3 = c();
        double[] PPG4 = d();
        Integer[] accx = e();
        Integer[] accy = f();
        Integer[] accz = g();


        saveToFile(tar_dir + fnames[0], Arrays.toString(PPG1));
        saveToFile(tar_dir + fnames[1], Arrays.toString(PPG2));
        saveToFile(tar_dir + fnames[2], Arrays.toString(PPG3));
        saveToFile(tar_dir + fnames[3], Arrays.toString(PPG4));
        saveToFile(tar_dir + fnames[4], Arrays.toString(accx));
        saveToFile(tar_dir + fnames[5], Arrays.toString(accy));
        saveToFile(tar_dir + fnames[6], Arrays.toString(accz));

    }

    public static void saveToFile(String fAbsPath, String txt){
        BufferedWriter writer = null;
        try {

            File file = new File(fAbsPath);
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, false), StandardCharsets.UTF_8));
            writer.write(txt);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

