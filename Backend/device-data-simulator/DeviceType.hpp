#pragma once

#include <string>

class DeviceType {
public:
	enum Id {
		TV,
		PC,
		CoffeeMachine,
		Laptop,
		Fridge,
		Unknown
	};

	static Id fromString(const std::string& device);
};
