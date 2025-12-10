#pragma once 

#include "DeviceType.hpp"

#include <utility>

class CommandLineParser {
public:
	static std::tuple<int, DeviceType::Id, unsigned int, unsigned int, unsigned int, unsigned int> parseArguments(int argc, char *argv[]);

private:
	static const unsigned int NUMBER_OF_EXPECTED_ARGS = 5;
	static const int MAX_NUMBER_OF_READINGS = 144;

	static bool validateNumberOfReadings(const unsigned int maybe);
	static bool validateType(const DeviceType::Id type);
	static bool validateUid(const unsigned int uid);
	static bool validateDate(const unsigned int day, const unsigned int month, const unsigned int year);
};