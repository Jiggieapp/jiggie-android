package com.jiggie.android.component;

/**
 * Created by LTE on 2/9/2016.
 */
public class ConnectionSpecTest {

    /*@Test
    public void cleartextBuilder() throws Exception {
        ConnectionSpec cleartextSpec = new ConnectionSpec.Builder(false).build();
        assertFalse(cleartextSpec.isTls());
    }

    @Test
    public void tlsBuilder_explicitCiphers() throws Exception {
        ConnectionSpec tlsSpec = new ConnectionSpec.Builder(true)
                .cipherSuites(CipherSuite.TLS_RSA_WITH_RC4_128_MD5)
                .tlsVersions(TlsVersion.TLS_1_2)
                .supportsTlsExtensions(true)
                .build();
        assertEquals(Arrays.asList(CipherSuite.TLS_RSA_WITH_RC4_128_MD5), tlsSpec.cipherSuites());
        assertEquals(Arrays.asList(TlsVersion.TLS_1_2), tlsSpec.tlsVersions());
        assertTrue(tlsSpec.supportsTlsExtensions());
    }

    @Test
    public void tlsBuilder_defaultCiphers() throws Exception {
        ConnectionSpec tlsSpec = new ConnectionSpec.Builder(true)
                .tlsVersions(TlsVersion.TLS_1_2)
                .supportsTlsExtensions(true)
                .build();
        assertNull(tlsSpec.cipherSuites());
        assertEquals(Arrays.asList(TlsVersion.TLS_1_2), tlsSpec.tlsVersions());
        assertTrue(tlsSpec.supportsTlsExtensions());
    }

    @Test
    public void tls_defaultCiphers_noFallbackIndicator() throws Exception {
        ConnectionSpec tlsSpec = new ConnectionSpec.Builder(true)
                .tlsVersions(TlsVersion.TLS_1_2)
                .supportsTlsExtensions(false)
                .build();

        SSLSocket socket = (SSLSocket) SSLSocketFactory.getDefault().createSocket();
        socket.setEnabledCipherSuites(new String[] {
                CipherSuite.TLS_RSA_WITH_RC4_128_MD5.javaName,
                CipherSuite.TLS_RSA_WITH_RC4_128_SHA.javaName,
        });
        socket.setEnabledProtocols(new String[] {
                TlsVersion.TLS_1_2.javaName,
                TlsVersion.TLS_1_1.javaName,
        });

        assertTrue(tlsSpec.isCompatible(socket));
        tlsSpec.apply(socket, false *//* isFallback *//*);

        assertEquals(createSet(TlsVersion.TLS_1_2.javaName), createSet(socket.getEnabledProtocols()));

        Set<String> expectedCipherSet =
                createSet(
                        CipherSuite.TLS_RSA_WITH_RC4_128_MD5.javaName,
                        CipherSuite.TLS_RSA_WITH_RC4_128_SHA.javaName);
        assertEquals(expectedCipherSet, expectedCipherSet);
    }

    @Test
    public void tls_defaultCiphers_withFallbackIndicator() throws Exception {
        ConnectionSpec tlsSpec = new ConnectionSpec.Builder(true)
                .tlsVersions(TlsVersion.TLS_1_2)
                .supportsTlsExtensions(false)
                .build();

        SSLSocket socket = (SSLSocket) SSLSocketFactory.getDefault().createSocket();
        socket.setEnabledCipherSuites(new String[] {
                CipherSuite.TLS_RSA_WITH_RC4_128_MD5.javaName,
                CipherSuite.TLS_RSA_WITH_RC4_128_SHA.javaName,
        });
        socket.setEnabledProtocols(new String[] {
                TlsVersion.TLS_1_2.javaName,
                TlsVersion.TLS_1_1.javaName,
        });

        assertTrue(tlsSpec.isCompatible(socket));
        tlsSpec.apply(socket, true *//* isFallback *//*);

        assertEquals(createSet(TlsVersion.TLS_1_2.javaName), createSet(socket.getEnabledProtocols()));

        Set<String> expectedCipherSet =
                createSet(
                        CipherSuite.TLS_RSA_WITH_RC4_128_MD5.javaName,
                        CipherSuite.TLS_RSA_WITH_RC4_128_SHA.javaName);
        if (Arrays.asList(socket.getSupportedCipherSuites()).contains("TLS_FALLBACK_SCSV")) {
            expectedCipherSet.add("TLS_FALLBACK_SCSV");
        }
        assertEquals(expectedCipherSet, expectedCipherSet);
    }

    @Test
    public void tls_explicitCiphers() throws Exception {
        ConnectionSpec tlsSpec = new ConnectionSpec.Builder(true)
                .cipherSuites(CipherSuite.TLS_RSA_WITH_RC4_128_MD5)
                .tlsVersions(TlsVersion.TLS_1_2)
                .supportsTlsExtensions(false)
                .build();

        SSLSocket socket = (SSLSocket) SSLSocketFactory.getDefault().createSocket();
        socket.setEnabledCipherSuites(new String[] {
                CipherSuite.TLS_RSA_WITH_RC4_128_MD5.javaName,
                CipherSuite.TLS_RSA_WITH_RC4_128_SHA.javaName,
        });
        socket.setEnabledProtocols(new String[] {
                TlsVersion.TLS_1_2.javaName,
                TlsVersion.TLS_1_1.javaName,
        });

        assertTrue(tlsSpec.isCompatible(socket));
        tlsSpec.apply(socket, true *//* isFallback *//*);

        assertEquals(createSet(TlsVersion.TLS_1_2.javaName), createSet(socket.getEnabledProtocols()));

        Set<String> expectedCipherSet = createSet(CipherSuite.TLS_RSA_WITH_RC4_128_MD5.javaName);
        if (Arrays.asList(socket.getSupportedCipherSuites()).contains("TLS_FALLBACK_SCSV")) {
            expectedCipherSet.add("TLS_FALLBACK_SCSV");
        }
        assertEquals(expectedCipherSet, expectedCipherSet);
    }

    @Test
    public void tls_stringCiphersAndVersions() throws Exception {
        // Supporting arbitrary input strings allows users to enable suites and versions that are not
        // yet known to the library, but are supported by the platform.
        ConnectionSpec tlsSpec = new ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
                .cipherSuites("MAGIC-CIPHER")
                .tlsVersions("TLS9k")
                .build();
    }

    public void tls_missingRequiredCipher() throws Exception {
        ConnectionSpec tlsSpec = new ConnectionSpec.Builder(true)
                .cipherSuites(CipherSuite.TLS_RSA_WITH_RC4_128_MD5)
                .tlsVersions(TlsVersion.TLS_1_2)
                .supportsTlsExtensions(false)
                .build();

        SSLSocket socket = (SSLSocket) SSLSocketFactory.getDefault().createSocket();
        socket.setEnabledProtocols(new String[] {
                TlsVersion.TLS_1_2.javaName,
                TlsVersion.TLS_1_1.javaName,
        });

        socket.setEnabledCipherSuites(new String[] {
                CipherSuite.TLS_RSA_WITH_RC4_128_SHA.javaName,
                CipherSuite.TLS_RSA_WITH_RC4_128_MD5.javaName,
        });
        assertTrue(tlsSpec.isCompatible(socket));

        socket.setEnabledCipherSuites(new String[] {
                CipherSuite.TLS_RSA_WITH_RC4_128_SHA.javaName,
        });
        assertFalse(tlsSpec.isCompatible(socket));
    }

    @Test
    public void tls_missingTlsVersion() throws Exception {
        ConnectionSpec tlsSpec = new ConnectionSpec.Builder(true)
                .cipherSuites(CipherSuite.TLS_RSA_WITH_RC4_128_MD5)
                .tlsVersions(TlsVersion.TLS_1_2)
                .supportsTlsExtensions(false)
                .build();

        SSLSocket socket = (SSLSocket) SSLSocketFactory.getDefault().createSocket();
        socket.setEnabledCipherSuites(new String[] {
                CipherSuite.TLS_RSA_WITH_RC4_128_MD5.javaName,
        });

        socket.setEnabledProtocols(
                new String[] { TlsVersion.TLS_1_2.javaName, TlsVersion.TLS_1_1.javaName });
        assertTrue(tlsSpec.isCompatible(socket));

        socket.setEnabledProtocols(new String[] { TlsVersion.TLS_1_1.javaName });
        assertFalse(tlsSpec.isCompatible(socket));
    }

    private static Set<String> createSet(String... values) {
        return new LinkedHashSet<String>(Arrays.asList(values));
    }*/

}
