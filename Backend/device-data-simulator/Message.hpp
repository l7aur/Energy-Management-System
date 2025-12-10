#pragma once

#include <string>

struct Message {
	struct _timestamp {
		unsigned int year{ 0 };
		unsigned int month{ 0 };
		unsigned int day{ 0 };

		unsigned int hour{ 0 };
		unsigned int minute{ 0 };
		unsigned int second{ 0 };

		std::string getBody() const {
			return std::string{ "{ " }
				.append("\"year\": ")
				.append(std::to_string(year))
				.append(", ")
				.append("\"month\": ")
				.append(std::to_string(month))
				.append(", ")
				.append("\"day\": ")
				.append(std::to_string(day))
				.append(", ")
				.append("\"hour\": ")
				.append(std::to_string(hour))
				.append(", ")
				.append("\"minute\": ")
				.append(std::to_string(minute))
				.append(", ")
				.append("\"second\": ")
				.append(std::to_string(second))
				.append(" }");
		}
	} timestamp;
	unsigned int deviceId{ 0 };
	float measuredValue{ 0 };

	std::string getBody() const {
		return std::string{ "{ " }
			.append("\"timestamp\": ")
			.append(timestamp.getBody())
			.append(", ")
			.append("\"deviceId\": ")
			.append(std::to_string(deviceId))
			.append(", ")
			.append("\"measuredValue\": ")
			.append(std::to_string(measuredValue))
			.append(" }");
	}
};