#pragma once

#include "IDevice.hpp"

class Laptop : public IDevice {
public:
	Laptop(const unsigned int uid, const unsigned int numberOfReadings) : IDevice{ uid, numberOfReadings } {}

	std::list<Message> getMessages(unsigned int day, unsigned int month, unsigned int year) const override;
};