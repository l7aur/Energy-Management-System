#pragma once

#include "DeviceType.hpp"
#include "IDevice.hpp"

#include <memory>

class DeviceFactory {
public:
	DeviceFactory();
	~DeviceFactory() = default;

	std::unique_ptr<IDevice> createDevice(const unsigned int uid, const DeviceType::Id id, const unsigned int numberOfReadings);
};