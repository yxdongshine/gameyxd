/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package org.apache.mina.filter.ssl;

import java.security.KeyStore;
import java.security.SecureRandom;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.ManagerFactoryParameters;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSessionContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

public class SslContextFactory {
	private String provider = null;

	private String protocol = "TLS";

	private SecureRandom secureRandom = null;

	private KeyStore keyManagerFactoryKeyStore = null;

	private char[] keyManagerFactoryKeyStorePassword = null;

	private KeyManagerFactory keyManagerFactory = null;

	private String keyManagerFactoryAlgorithm = null;

	private String keyManagerFactoryProvider = null;

	private boolean keyManagerFactoryAlgorithmUseDefault = true;

	private KeyStore trustManagerFactoryKeyStore = null;

	private TrustManagerFactory trustManagerFactory = null;

	private String trustManagerFactoryAlgorithm = null;

	private String trustManagerFactoryProvider = null;

	private boolean trustManagerFactoryAlgorithmUseDefault = true;

	private ManagerFactoryParameters trustManagerFactoryParameters = null;

	private int clientSessionCacheSize = -1;

	private int clientSessionTimeout = -1;

	private int serverSessionCacheSize = -1;

	private int serverSessionTimeout = -1;

	public SSLContext newInstance() throws Exception {
		KeyManagerFactory kmf = this.keyManagerFactory;
		TrustManagerFactory tmf = this.trustManagerFactory;

		if (kmf == null) {
			String algorithm = this.keyManagerFactoryAlgorithm;
			if ((algorithm == null)
					&& (this.keyManagerFactoryAlgorithmUseDefault)) {
				algorithm = KeyManagerFactory.getDefaultAlgorithm();
			}
			if (algorithm != null) {
				if (this.keyManagerFactoryProvider == null)
					kmf = KeyManagerFactory.getInstance(algorithm);
				else {
					kmf = KeyManagerFactory.getInstance(algorithm,
							this.keyManagerFactoryProvider);
				}
			}
		}

		if (tmf == null) {
			String algorithm = this.trustManagerFactoryAlgorithm;
			if ((algorithm == null)
					&& (this.trustManagerFactoryAlgorithmUseDefault)) {
				algorithm = TrustManagerFactory.getDefaultAlgorithm();
			}
			if (algorithm != null) {
				if (this.trustManagerFactoryProvider == null)
					tmf = TrustManagerFactory.getInstance(algorithm);
				else {
					tmf = TrustManagerFactory.getInstance(algorithm,
							this.trustManagerFactoryProvider);
				}
			}
		}

		KeyManager[] keyManagers = (KeyManager[]) null;
		if (kmf != null) {
			kmf.init(this.keyManagerFactoryKeyStore,
					this.keyManagerFactoryKeyStorePassword);
			keyManagers = kmf.getKeyManagers();
		}
		TrustManager[] trustManagers = (TrustManager[]) null;
		if (tmf != null) {
			if (this.trustManagerFactoryParameters != null)
				tmf.init(this.trustManagerFactoryParameters);
			else {
				tmf.init(this.trustManagerFactoryKeyStore);
			}
			trustManagers = tmf.getTrustManagers();
		}

		SSLContext context = null;
		if (this.provider == null)
			context = SSLContext.getInstance(this.protocol);
		else {
			context = SSLContext.getInstance(this.protocol, this.provider);
		}

		context.init(keyManagers, trustManagers, this.secureRandom);

		if (this.clientSessionCacheSize >= 0) {
			context.getClientSessionContext().setSessionCacheSize(
					this.clientSessionCacheSize);
		}

		if (this.clientSessionTimeout >= 0) {
			context.getClientSessionContext().setSessionTimeout(
					this.clientSessionTimeout);
		}

		if (this.serverSessionCacheSize >= 0) {
			context.getServerSessionContext().setSessionCacheSize(
					this.serverSessionCacheSize);
		}

		if (this.serverSessionTimeout >= 0) {
			context.getServerSessionContext().setSessionTimeout(
					this.serverSessionTimeout);
		}

		return context;
	}

	public void setProvider(String provider) {
		this.provider = provider;
	}

	public void setProtocol(String protocol) {
		if (protocol == null) {
			throw new IllegalArgumentException("protocol");
		}
		this.protocol = protocol;
	}

	public void setKeyManagerFactoryAlgorithmUseDefault(boolean useDefault) {
		this.keyManagerFactoryAlgorithmUseDefault = useDefault;
	}

	public void setTrustManagerFactoryAlgorithmUseDefault(boolean useDefault) {
		this.trustManagerFactoryAlgorithmUseDefault = useDefault;
	}

	public void setKeyManagerFactory(KeyManagerFactory factory) {
		this.keyManagerFactory = factory;
	}

	public void setKeyManagerFactoryAlgorithm(String algorithm) {
		this.keyManagerFactoryAlgorithm = algorithm;
	}

	public void setKeyManagerFactoryProvider(String provider) {
		this.keyManagerFactoryProvider = provider;
	}

	public void setKeyManagerFactoryKeyStore(KeyStore keyStore) {
		this.keyManagerFactoryKeyStore = keyStore;
	}

	public void setKeyManagerFactoryKeyStorePassword(String password) {
		if (password != null)
			this.keyManagerFactoryKeyStorePassword = password.toCharArray();
		else
			this.keyManagerFactoryKeyStorePassword = null;
	}

	public void setTrustManagerFactory(TrustManagerFactory factory) {
		this.trustManagerFactory = factory;
	}

	public void setTrustManagerFactoryAlgorithm(String algorithm) {
		this.trustManagerFactoryAlgorithm = algorithm;
	}

	public void setTrustManagerFactoryKeyStore(KeyStore keyStore) {
		this.trustManagerFactoryKeyStore = keyStore;
	}

	public void setTrustManagerFactoryParameters(
			ManagerFactoryParameters parameters) {
		this.trustManagerFactoryParameters = parameters;
	}

	public void setTrustManagerFactoryProvider(String provider) {
		this.trustManagerFactoryProvider = provider;
	}

	public void setSecureRandom(SecureRandom secureRandom) {
		this.secureRandom = secureRandom;
	}

	public void setClientSessionCacheSize(int size) {
		this.clientSessionCacheSize = size;
	}

	public void setClientSessionTimeout(int seconds) {
		this.clientSessionTimeout = seconds;
	}

	public void setServerSessionCacheSize(int serverSessionCacheSize) {
		this.serverSessionCacheSize = serverSessionCacheSize;
	}

	public void setServerSessionTimeout(int serverSessionTimeout) {
		this.serverSessionTimeout = serverSessionTimeout;
	}
}