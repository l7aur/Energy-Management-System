#include "DeviceType.hpp"

DeviceType::Id DeviceType::fromString(const std::string& device)
{
	if (device == "TV")
		return TV;
	if (device == "CoffeeMachine")
		return CoffeeMachine;
	if (device == "PC")
		return PC;
	if (device == "Laptop")
		return Laptop;
	if (device == "Fridge")
		return Fridge;
	return Unknown;
}
