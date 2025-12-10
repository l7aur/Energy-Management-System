#pragma once

#include "Message.hpp"

#include <list>

class IDevice {
public:
	IDevice(const unsigned int uid_, const unsigned int noReadings) :uid{ uid_ }, numberOfReadings { noReadings } {}
	virtual ~IDevice() = default;

	virtual std::list<Message> getMessages(unsigned int day, unsigned int month, unsigned int year) const = 0;

protected:
	const unsigned int numberOfReadings{ 0 };
	const unsigned int uid{ 0 };
};