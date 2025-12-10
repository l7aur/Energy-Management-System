#include "CommandLineParser.hpp"

#include <iostream>

auto parseUIntFromString = [](const std::string& str, const int startingIndex, const int numberOfCharacters) {
	return static_cast<unsigned int>(std::atoi(str.substr(startingIndex, numberOfCharacters).c_str()));
	};

std::tuple<int, DeviceType::Id, unsigned int, unsigned int, unsigned int, unsigned int> CommandLineParser::parseArguments(int argc, char* argv[])
{
	if (NUMBER_OF_EXPECTED_ARGS != argc || strlen(argv[4]) != 10 || argv[4][2] != '.' || argv[4][5] != '.') {
		std::cout << "Incorrect usage of the application!\n"
			"./DeviceDataSimulator.exe <device_id> <device_name> <number_of_readings> <date>\n";
		return { -1, DeviceType::Unknown, 0, 0, 0, 0 };
	}

	const unsigned int uid = std::atoi(argv[1]);
	const DeviceType::Id type = DeviceType::fromString(argv[2]);
	unsigned int numberOfReadings = std::atoi(argv[3]);
	unsigned int d = parseUIntFromString(std::string{ argv[4] }, 0, 2);
	unsigned int m = parseUIntFromString(std::string{ argv[4] }, 3, 2);
	unsigned int y = parseUIntFromString(std::string{ argv[4] }, 6, 4);

	if (!validateNumberOfReadings(numberOfReadings))
		numberOfReadings = MAX_NUMBER_OF_READINGS;

	if (!validateType(type) || !validateUid(uid) || !validateDate(d, m, y)) {
		std::cout << "Incorrect usage of the application!\n\n"
			"./DeviceDataSimulator.exe <device_id> <device_name> <number_of_readings>\n"
			"<device_id>: UINT > 0\n"
			"<device_name>: [TV | PC | CoffeeMachine | Laptop | Fridge]\n"
			"<number_of_readings>: INT <= " << MAX_NUMBER_OF_READINGS << "\n"
			"<date>: dd.mm.yyyy\n";
		return { -1, DeviceType::Unknown, 0, 0, 0, 0 };
	}

	return { uid, type, numberOfReadings < MAX_NUMBER_OF_READINGS ? numberOfReadings : MAX_NUMBER_OF_READINGS, d, m, y };
}

bool CommandLineParser::validateNumberOfReadings(const unsigned int maybe)
{
	if (maybe < MAX_NUMBER_OF_READINGS)
		return true;
	std::cout << "Maximum number of readings that can be generated is: " << MAX_NUMBER_OF_READINGS << std::endl;
	std::cout << "Using " << MAX_NUMBER_OF_READINGS << " instead of " << maybe << std::endl;
	return false;
}

bool CommandLineParser::validateType(const DeviceType::Id type)
{
	return type != DeviceType::Unknown;
}

bool CommandLineParser::validateUid(const unsigned int uid)
{
	return uid > 0;
}

bool CommandLineParser::validateDate(const unsigned int day, const unsigned int month, const unsigned int year) 
{
	enum _MONTH { JAN = 1, FEB, MARCH, APR, MAY, JUN, JUL, AUG, SEP, OCT, NOV, DEC };
	switch (static_cast<_MONTH>(month))
	{
	case JAN:case MARCH: case MAY: case JUL: case AUG: case OCT: case DEC:
		if (day > 31) return false;
		break;
	case FEB:
		if (day > 28 && year % 4 != 0) return false;
		if (day > 29 && year % 4 == 0) return false;
		break;
	case APR: case JUN: case SEP: case NOV:
		if (day > 30) return false;
		break;
	default:
		return false;
	}

	if (year > 2025) return false;
	return true;
}