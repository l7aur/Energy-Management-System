#include "ExitManager.hpp"

#include <iostream>

int ExitManager::exit(const Code exitCode)
{
	switch (exitCode) {
	case SUCCESS:
		std::cout << "Exit success!\n";
		return 0;
	case FAILURE:
		std::cout << "Exit failure!\n";
		return 1;
	case NOTHING_PUBLISHED:
		std::cout << "Nothing has been published!\nExit nothing done!\n";
		return 2;
	case FAILED_TO_CONNECT:
		std::cout << "Failed to connect to RabbitMQ!\nExit nothing done!\n";
		return 3;
	}
	return 255;
}
