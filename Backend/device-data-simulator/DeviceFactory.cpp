#include "DeviceFactory.hpp"
#include "TV.hpp"
#include "PC.hpp"
#include "CoffeeMachine.hpp"
#include "Laptop.hpp"
#include "Fridge.hpp"


DeviceFactory::DeviceFactory()
{
	srand(static_cast<unsigned int>(time(NULL)));
}

std::unique_ptr<IDevice> DeviceFactory::createDevice(const unsigned int uid, const DeviceType::Id id, const unsigned int numberOfReadings)
{
	switch (id)
	{
	case DeviceType::TV:
		return std::make_unique<TV>(uid, numberOfReadings);
	case DeviceType::PC:
		return std::make_unique<PC>(uid, numberOfReadings);
	case DeviceType::CoffeeMachine:
		return std::make_unique<CoffeeMachine>(uid, numberOfReadings);
	case DeviceType::Laptop:
		return std::make_unique<Laptop>(uid, numberOfReadings);
	case DeviceType::Fridge:
		return std::make_unique<Fridge>(uid, numberOfReadings);
	default:
		return nullptr;
	}
}
