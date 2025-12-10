#include "ExitManager.hpp"
#include "CommandLineParser.hpp"
#include "DeviceFactory.hpp"
#include "MessagePublisher.hpp"

#include <algorithm>

int main(int argc, char* argv[])
{
	const auto& [uid, deviceType, numberOfReadings, day, month, year] = CommandLineParser::parseArguments(argc, argv);

	if (numberOfReadings <= 0)
		return ExitManager::exit(ExitManager::NOTHING_PUBLISHED);

	DeviceFactory factory{};
	std::list<std::unique_ptr<IDevice>> myDevices{};
	myDevices.push_back(factory.createDevice(uid, deviceType, numberOfReadings));	
	
	MessagePublisher publisher{};
	publisher.start();
	std::for_each(myDevices.begin(), myDevices.end(), [&publisher, day, month, year](const auto& device) { publisher.publish(device->getMessages(day, month, year)); });
	publisher.end();

	return ExitManager::exit(ExitManager::SUCCESS);
}