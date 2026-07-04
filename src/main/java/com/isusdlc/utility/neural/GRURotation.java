package com.isusdlc.utility.neural;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

@Environment(EnvType.CLIENT)
public class GRURotation {
   public static final String WEIGHTS_PATH = "config/isusdlc/gru_rotation.json";
   private static final int HIDDEN = 24;
   private double[][] Wz;
   private double[][] Uz;
   private double[] bz;
   private double[][] Wr;
   private double[][] Ur;
   private double[] br;
   private double[][] Wh;
   private double[][] Uh;
   private double[] bh;
   private double[][] Wy;
   private double[] by;
   private double meanYaw;
   private double meanPitch;
   private double stdYaw = 1.0;
   private double stdPitch = 1.0;
   private final double[] h = new double[HIDDEN];
   private boolean loaded;
   private static GRURotation INSTANCE;

   public static GRURotation get() {
      if (INSTANCE == null) INSTANCE = new GRURotation();
      return INSTANCE;
   }

   private GRURotation() {
      initFallback();
      tryLoad();
   }

   public void reload() {
      Arrays.fill(h, 0.0);
      tryLoad();
   }

   private void tryLoad() {
      if (Files.exists(Path.of(WEIGHTS_PATH))) {
         load(WEIGHTS_PATH);
      }
   }

   public float[] forward(float dYaw, float dPitch) {
      double[] x = new double[]{(dYaw - meanYaw) / stdYaw, (dPitch - meanPitch) / stdPitch};
      double[] z = sigmoid(addB(linear(Wz, x, Uz, h), bz));
      double[] r = sigmoid(addB(linear(Wr, x, Ur, h), br));
      double[] hc = tanh(addB(linear(Wh, x, Uh, hadamard(r, h)), bh));

      for (int i = 0; i < HIDDEN; i++) {
         h[i] = (1.0 - z[i]) * h[i] + z[i] * hc[i];
      }

      double[] y = addB(matVec(Wy, h), by);
      return new float[]{(float) (y[0] * stdYaw + meanYaw), (float) (y[1] * stdPitch + meanPitch)};
   }

   public void resetState() {
      Arrays.fill(h, 0.0);
   }

   public boolean isLoaded() {
      return loaded;
   }

   public void load(String path) {
      try {
         String json = Files.readString(Path.of(path));
         JsonObject o = JsonParser.parseString(json).getAsJsonObject();
         Wz = mat(o, "Wz");
         Uz = mat(o, "Uz");
         bz = vec(o, "bz");
         Wr = mat(o, "Wr");
         Ur = mat(o, "Ur");
         br = vec(o, "br");
         Wh = mat(o, "Wh");
         Uh = mat(o, "Uh");
         bh = vec(o, "bh");
         Wy = mat(o, "Wy");
         by = vec(o, "by");
         if (o.has("meanYaw")) meanYaw = o.get("meanYaw").getAsDouble();
         if (o.has("meanPitch")) meanPitch = o.get("meanPitch").getAsDouble();
         if (o.has("stdYaw")) stdYaw = o.get("stdYaw").getAsDouble();
         if (o.has("stdPitch")) stdPitch = o.get("stdPitch").getAsDouble();
         loaded = true;
      } catch (Exception e) {
         initFallback();
      }
   }

   private void initFallback() {
      double s = 0.6;
      Wz = zeros2(HIDDEN, 2);
      Uz = zeros2(HIDDEN, HIDDEN);
      bz = fill(HIDDEN, -4.0);
      Wr = zeros2(HIDDEN, 2);
      Ur = zeros2(HIDDEN, HIDDEN);
      br = fill(HIDDEN, 4.0);
      Wh = zeros2(HIDDEN, 2);
      Uh = zeros2(HIDDEN, HIDDEN);
      bh = zeros1(HIDDEN);

      for (int i = 0; i < HIDDEN; i++) Wh[i][i % 2] = s;

      Wy = zeros2(2, HIDDEN);
      by = zeros1(2);

      for (int i = 0; i < 2; i++)
         for (int j = 0; j < HIDDEN; j++)
            if (j % 2 == i) Wy[i][j] = 1.0 / HIDDEN;

      loaded = false;
   }

   private double[] linear(double[][] W, double[] x, double[][] U, double[] hh) {
      double[] o = matVec(W, x);
      double[] u = matVec(U, hh);
      for (int i = 0; i < o.length; i++) o[i] += u[i];
      return o;
   }

   private double[] matVec(double[][] M, double[] v) {
      double[] o = new double[M.length];
      for (int i = 0; i < M.length; i++)
         for (int j = 0; j < v.length; j++)
            o[i] += M[i][j] * v[j];
      return o;
   }

   private double[] addB(double[] a, double[] b) {
      double[] o = new double[a.length];
      for (int i = 0; i < a.length; i++) o[i] = a[i] + b[i];
      return o;
   }

   private double[] hadamard(double[] a, double[] b) {
      double[] o = new double[a.length];
      for (int i = 0; i < a.length; i++) o[i] = a[i] * b[i];
      return o;
   }

   private double[] sigmoid(double[] x) {
      double[] o = new double[x.length];
      for (int i = 0; i < x.length; i++) o[i] = 1.0 / (1.0 + Math.exp(-x[i]));
      return o;
   }

   private double[] tanh(double[] x) {
      double[] o = new double[x.length];
      for (int i = 0; i < x.length; i++) o[i] = Math.tanh(x[i]);
      return o;
   }

   private double[] zeros1(int n) { return new double[n]; }
   private double[][] zeros2(int r, int c) { return new double[r][c]; }

   private double[] fill(int n, double v) {
      double[] a = new double[n];
      Arrays.fill(a, v);
      return a;
   }

   private double[][] mat(JsonObject o, String k) {
      JsonArray rows = o.getAsJsonArray(k);
      double[][] M = new double[rows.size()][];
      for (int i = 0; i < rows.size(); i++) {
         JsonArray row = rows.get(i).getAsJsonArray();
         M[i] = new double[row.size()];
         for (int j = 0; j < row.size(); j++) M[i][j] = row.get(j).getAsDouble();
      }
      return M;
   }

   private double[] vec(JsonObject o, String k) {
      JsonArray a = o.getAsJsonArray(k);
      double[] v = new double[a.size()];
      for (int i = 0; i < a.size(); i++) v[i] = a.get(i).getAsDouble();
      return v;
   }
}
