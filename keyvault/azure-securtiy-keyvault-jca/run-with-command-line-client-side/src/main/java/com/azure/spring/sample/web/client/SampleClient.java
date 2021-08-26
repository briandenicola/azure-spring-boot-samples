// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
package com.azure.spring.sample.web.client;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.*;

public class SampleClient {
  private static final String[] PROTOCOLS = new String[]{"TLSv1.3"};
  private static final String[] CIPHER_SUITES = new String[]{"TLS_AES_128_GCM_SHA256"};

  public static void main(String[] args) throws Exception {
    SSLSocket sslSocket = null;
    PrintWriter printWriter = null;
    BufferedReader bufferedReader = null;

    try {
      SSLSocketFactory factory = (SSLSocketFactory) SSLSocketFactory.getDefault();
      sslSocket = (SSLSocket) factory.createSocket("localhost", 8443);
      sslSocket.setEnabledProtocols(PROTOCOLS);
      sslSocket.setEnabledCipherSuites(CIPHER_SUITES);
      sslSocket.startHandshake();
      printWriter = new PrintWriter(
                      new BufferedWriter(
                            new OutputStreamWriter(
                                    sslSocket.getOutputStream())));
      printWriter.println("GET / HTTP/1.0");
      printWriter.println();
      printWriter.flush();
      if (printWriter.checkError()){
        System.out.println("SampleClient: error in PrintWriter");
      }
      bufferedReader = new BufferedReader(
                         new InputStreamReader(
                                 sslSocket.getInputStream()));

      String inputLine;
      while ((inputLine = bufferedReader.readLine()) != null) {
        System.out.println(inputLine);
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      if (sslSocket != null)
        sslSocket.close();
      if (printWriter != null)
        printWriter.close();
      if (bufferedReader != null)
        bufferedReader.close();
    }
  }
}
