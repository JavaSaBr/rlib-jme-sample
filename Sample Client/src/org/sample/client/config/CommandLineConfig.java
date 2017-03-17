package org.sample.client.config;

import org.jetbrains.annotations.NotNull;

import java.net.InetSocketAddress;

/**
 * Parser the configuration from command-line arguments.
 *
 * @author JavaSaBr
 */
public class CommandLineConfig {

	public static void args(@NotNull final String[] args) {

		for(String arg : args) {

			final String[] values = arg.split("=", 2);

			if(values.length < 2) {
				continue;
			}

			switch(values[0]) {
				case "server": {
					Config.SERVER_SOCKET_ADDRESS = getAddressFrom(values[1]);
					break;
				}
			}
		}
	}

	@NotNull
	private static InetSocketAddress getAddressFrom(@NotNull final String value) {
		final String[] values = value.split(":", 2);
		return new InetSocketAddress(values[0], Integer.parseInt(values[1]));
	}
}
